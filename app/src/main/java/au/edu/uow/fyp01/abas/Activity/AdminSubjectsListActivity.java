package au.edu.uow.fyp01.abas.Activity;

import static android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import au.edu.uow.fyp01.abas.Model.ListOfSubjectsModel;
import au.edu.uow.fyp01.abas.Model.UserModel;
import au.edu.uow.fyp01.abas.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AdminSubjectsListActivity extends AppCompatActivity {

  private ProgressDialog progressDialog;

  private RecyclerView adminSubjectRecyclerView;
  private FirebaseDatabase db;
  private DatabaseReference dbref;
  private FirebaseRecyclerOptions<ListOfSubjectsModel> options;
  private FirebaseRecyclerAdapter<ListOfSubjectsModel, ListOfSubjectsModelViewHolder> firebaseRecyclerAdapter;

  //Current user's metadata
  private UserModel userModel;
  private String uID;
  private FirebaseAuth auth;
  private String schID;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_adminsubjectlist);

    //get current user
    uID = auth.getInstance().getCurrentUser().getUid();

    schID = "";

    showProgressDialog();

    UserQueryClass(new FirebaseCallBack() {
      @Override
      public void onCallBack(UserModel userModel) {
        schID = userModel.getSchID();

        //instantiate db
        db = FirebaseDatabase.getInstance();

        //RecyclerView
        adminSubjectRecyclerView = findViewById(R.id.adminSubjectRecyclerView);
        adminSubjectRecyclerView.setHasFixedSize(true);
        adminSubjectRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //ListOfSubjects -> SchoolID
        dbref = db.getReference().child("ListOfSubjects").child(schID);

        //set options
        options = new FirebaseRecyclerOptions.Builder<ListOfSubjectsModel>().
            setQuery(dbref.orderByChild("subjectname"), ListOfSubjectsModel.class)
            .build();

        firebaseRecyclerAdapter =
            new FirebaseRecyclerAdapter<ListOfSubjectsModel, ListOfSubjectsModelViewHolder>(
                options) {
              @Override
              protected void onBindViewHolder(@NonNull ListOfSubjectsModelViewHolder holder,
                  int position, @NonNull ListOfSubjectsModel model) {
                //bind object
                holder.setSubjectID(model.getSubjectID());
                holder.setSubjectname(model.getSubjectname());
              }

              @NonNull
              @Override
              public ListOfSubjectsModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                  int viewType) {

                View view1 =
                    LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recyclermodellayout_singlebutton,
                            parent, false);
                return new ListOfSubjectsModelViewHolder(view1);
              }
            };

        adminSubjectRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
        hideProgressDialog();

        Button adminManageClassSubjectsBtn = findViewById(R.id.adminManageClassSubjectBtn);
        adminManageClassSubjectsBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

            //<editor-fold desc="move to AdminManageClassSubjectActivity.class>

            Intent i = new Intent(getApplicationContext(), AdminManageClassSubjectActivity.class);

            Bundle args = new Bundle();

            args.putString("schID", schID);

            i.putExtras(args);

            startActivity(i);
            //</editor-fold>
          }
        });

      }//end callback
    }); //end query class

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_adminsubjectlist, menu);
    return true;
  }

  //<editor-fold desc="Add new subjectbutton">
  public void addNewSubject(MenuItem mi) {
    //BUTTON BUILDER SET STYLE HERE
    AlertDialog.Builder builder = new AlertDialog.Builder(AdminSubjectsListActivity.this,
        THEME_DEVICE_DEFAULT_DARK);
    builder.setTitle("Add new subject: ");

    // Set up the input
    final EditText input = new EditText(getApplicationContext());
    input.setTextColor(Color.BLACK);
    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
    input.setInputType(InputType.TYPE_CLASS_TEXT);
    builder.setView(input);

    // Set up the buttons
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {

        //get the user input for comment
        String input_Text = input.getText().toString();

        //create a new unique comment ID
        String subjectID = UUID.randomUUID().toString();

        //handle user input into database input
        Map<String, Object> addToDatabase = new HashMap<>();

        addToDatabase.put("subjectname", input_Text);
        addToDatabase.put("subjectID", subjectID);

        //push to database
        dbref.child(subjectID).updateChildren(addToDatabase);

        //default subject settings
        DatabaseReference dbref2 = db.getReference().child("SubjectSettings")
            .child(schID).child(subjectID);
        Map<String, Object> subjectRatios = new HashMap<>();
        subjectRatios.put("assignmentratio", "25");
        subjectRatios.put("quizratio", "25");
        subjectRatios.put("testratio", "25");
        subjectRatios.put("examratio", "25");
        dbref2.updateChildren(subjectRatios);
      }
    });
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
      }
    });

    builder.show();
  }
  //</editor-fold>

  private void UserQueryClass(final FirebaseCallBack firebaseCallBack) {
    FirebaseDatabase db2 = FirebaseDatabase.getInstance();
    DatabaseReference dbref2 = db2.getReference().child("User").child(uID);
    dbref2.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        userModel = dataSnapshot.getValue(UserModel.class);
        firebaseCallBack.onCallBack(userModel);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }

  private interface FirebaseCallBack {

    void onCallBack(UserModel userModel);
  }

  public class SubjectModelViewHolder extends RecyclerView.ViewHolder {

    View mView;
    String subjectname;
    String subjectID;

    public SubjectModelViewHolder(View itemView) {
      super(itemView);
      mView = itemView;
    }

    public void setSubjectID(String subjectID) {
      this.subjectID = subjectID;
    }

    public void setSubjectname(String subjectname1) {
      Button subjectNameButtonView = mView.findViewById(R.id.modelSingleBtn);
      subjectNameButtonView.setText(subjectname1);
      this.subjectname = subjectname1;


    }
  }

  public class ListOfSubjectsModelViewHolder extends RecyclerView.ViewHolder {

    View mView;

    String subjectname;
    String subjectID;

    public ListOfSubjectsModelViewHolder(View itemView) {
      super(itemView);
      mView = itemView;
    }

    public void setSubjectID(String subjectID) {
      this.subjectID = subjectID;
    }

    public void setSubjectname(String subjectname) {
      this.subjectname = subjectname;

      final Button subjectNameBtn = mView.findViewById(R.id.modelSingleBtn);
      subjectNameBtn.setText(subjectname);

      //<editor-fold desc="Move to AdminSubjectSettings">
      subjectNameBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent i = new Intent(getApplicationContext(),
              AdminSubjectSettingActivity.class);

          //passing 'subjectID', 'schID' to AdminSubjectSettingActivity
          Bundle args = new Bundle();
          args.putString("subjectID", subjectID);
          args.putString("schID", schID);

          i.putExtras(args);

          startActivity(i);
        }
      });

      //</editor-fold>
    }


  }

  private void showProgressDialog() {
    if (progressDialog == null) {
      progressDialog = new ProgressDialog(this);
      progressDialog.setIndeterminate(true);
      progressDialog.setMessage("Loading...");
    }
    progressDialog.show();
  }

  private void hideProgressDialog() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }
}
