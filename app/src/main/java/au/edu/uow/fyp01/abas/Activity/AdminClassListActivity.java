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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import au.edu.uow.fyp01.abas.Model.SchoolModel;
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

public class AdminClassListActivity extends AppCompatActivity {

  private ProgressDialog progressDialog;

  private RecyclerView adminClassListRecyclerView;
  private DatabaseReference dbref;
  private FirebaseRecyclerOptions<SchoolModel> options;
  private FirebaseRecyclerAdapter<SchoolModel, SchoolModelViewHolder> firebaseRecyclerAdapter;
  private FirebaseDatabase db;

  //Current user's metadata
  private UserModel userModel;
  private String uID;
  private FirebaseAuth auth;
  private String schID;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_adminclasslist);

    showProgressDialog();

    //get current user
    uID = auth.getInstance().getCurrentUser().getUid();

    schID = "";


    UserQueryClass(new FirebaseCallBack() {
      @Override
      public void onCallBack(UserModel userModel) {
        schID = userModel.getSchID();

        //Instantiate the database
        db = FirebaseDatabase.getInstance();

        //RecyclerView
        adminClassListRecyclerView = findViewById(R.id.adminClassListRecyclerView);
        adminClassListRecyclerView.setHasFixedSize(true);
        adminClassListRecyclerView
            .setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //DONE replace '.child("SchID1")' with .child(SchID) whereas SchID is grabbed from a query class
        //<editor-fold desc="PROTOTYPE: dbref refers directly to School->SchID1">
        //Instantiate dbref
        dbref = db.getReference().child("School").child(schID);
        //</editor-fold>

        //set options for adapter
        options = new FirebaseRecyclerOptions.Builder<SchoolModel>().
            setQuery(dbref.orderByChild("classname"), SchoolModel.class).build();

        firebaseRecyclerAdapter =
            new FirebaseRecyclerAdapter<SchoolModel, SchoolModelViewHolder>(options) {
              @Override
              protected void onBindViewHolder(@NonNull SchoolModelViewHolder holder, int position,
                  @NonNull SchoolModel model) {
                //bind object
                holder.setClassID(model.getClassID());
                holder.setClassname(model.getClassname());
              }

              @NonNull
              @Override
              public SchoolModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                  int viewType) {
                View view1 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclermodellayout_singlebutton, parent, false);
                return new SchoolModelViewHolder(view1);
              }
            };

        adminClassListRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
        hideProgressDialog();

        //<editor-fold desc="Add new class button">
        Button adminClassListAddBtn = findViewById(R.id.adminClassListAddBtn);
        adminClassListAddBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            //BUTTON BUILDER SET STYLE HERE
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminClassListActivity.this,
                THEME_DEVICE_DEFAULT_DARK);
            builder.setTitle("Add new class: ");

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
                String classID = UUID.randomUUID().toString();

                //handle user input into database input
                Map<String, Object> addToDatabase = new HashMap<>();

                addToDatabase.put("classname", input_Text);
                addToDatabase.put("classID", classID);

                //push to database
                dbref.child(classID).updateChildren(addToDatabase);
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
        });
        //</editor-fold>

      }
    });
  }

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

  public class SchoolModelViewHolder extends RecyclerView.ViewHolder {

    View mView;
    String classID;

    public SchoolModelViewHolder(View itemView) {
      super(itemView);
      mView = itemView;
    }

    public void setClassID(String classID) {
      this.classID = classID;
    }

    public void setClassname(final String classname) {
      //points to recyclermodellayout_singlebutton
      //The button is for each class (e.g. 1A, 1B, 1C)
      final Button classNameButtonView = mView.findViewById(R.id.modelSingleBtn);
      classNameButtonView.setText(classname);

      classNameButtonView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          //<editor-fold desc="Transaction to move to 'AdminStudentListActivity'">

          Intent i = new Intent(getApplicationContext(), AdminStudentListActivity.class);

          //Passing 'classID' & 'schID' to AdminStudentListFragment
          Bundle args = new Bundle();
          args.putString("classname", classname);
          args.putString("classID", classID);
          args.putString("schID", schID);
          i.putExtras(args);

          startActivity(i);

          //</editor-fold>
        }
      });
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
