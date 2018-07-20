package au.edu.uow.fyp01.abas.module.setting;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import au.edu.uow.fyp01.abas.model.SchoolInfoModel;
import au.edu.uow.fyp01.abas.model.UserModel;
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

public class SchoolListActivity extends AppCompatActivity {

  private ProgressDialog progressDialog;

  private RecyclerView schoolListRecyclerView;
  private FirebaseDatabase db;
  private DatabaseReference dbref;
  private FirebaseRecyclerOptions<SchoolInfoModel> options;
  private FirebaseRecyclerAdapter<SchoolInfoModel, SchoolInfoModelViewHolder> adapter;

  //user metadata
  private FirebaseAuth auth;
  private String uID;
  private UserModel userModel;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_school_list);

    showProgressDialog();

    //get current user
    uID = auth.getInstance().getCurrentUser().getUid();

    //instantiate db
    db = FirebaseDatabase.getInstance();
    dbref = db.getReference().child("SchoolInfo");

    UserQueryClass(new FirebaseCallBack() {
      @Override
      public void onCallBack(UserModel userModel1) {
        userModel = userModel1;

        //Recyclerview
        schoolListRecyclerView = findViewById(R.id.schoolListRecyclerView);
        schoolListRecyclerView.setHasFixedSize(true);
        schoolListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //dbref set in order by SCHOOL NAME
        options = new FirebaseRecyclerOptions.Builder<SchoolInfoModel>().
            setQuery(dbref.orderByChild("schoolname"), SchoolInfoModel.class).build();

        adapter =
            new FirebaseRecyclerAdapter<SchoolInfoModel, SchoolInfoModelViewHolder>(options) {
              @Override
              protected void onBindViewHolder(@NonNull SchoolInfoModelViewHolder holder,
                  int position, @NonNull SchoolInfoModel model) {
                //bind object
                holder.setSchID(model.getSchID());
                holder.setSchoolname(model.getSchoolname());
              }

              @NonNull
              @Override
              public SchoolInfoModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                  int viewType) {
                View view1 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_model_layout_single_button, parent, false);
                return new SchoolInfoModelViewHolder(view1);
              }
            };

        schoolListRecyclerView.setAdapter(adapter);
        adapter.startListening();
        hideProgressDialog();

      } //end callback
    }); //end query
  }


  private void UserQueryClass(final FirebaseCallBack firebaseCallBack) {

    //get current user
    String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
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


  public class SchoolInfoModelViewHolder extends RecyclerView.ViewHolder {

    View mView;
    String schID;
    String schoolname;

    public SchoolInfoModelViewHolder(View itemView) {
      super(itemView);
      mView = itemView;
    }

    public void setSchID(String schID) {
      this.schID = schID;
    }

    public void setSchoolname(final String schoolname) {
      this.schoolname = schoolname;

      final Button schoolBtn = mView.findViewById(R.id.modelSingleBtn);

      //set up name
      schoolBtn.setText(schoolname);

      schoolBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          //Ask for user confirmation
          AlertDialog.Builder builder1 = new AlertDialog.Builder(
              SchoolListActivity.this);
          String tempstring = "Send a request to " +
              schoolname + " ?";
          builder1.setMessage(tempstring);
          builder1.setCancelable(true);

          builder1.setPositiveButton(
              "Yes",
              new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                  //send a request to school's request list
                  DatabaseReference dbref2 = db.getReference().child("RequestList")
                      .child(schID).child(uID);

                  //handle user input into database
                  Map<String, Object> addToDatabase = new HashMap<>();

                  addToDatabase.put("userID", uID);
                  addToDatabase.put("fullname", userModel.getFullname());
                  addToDatabase.put("title", userModel.getTitle());
                  addToDatabase.put("staffID", userModel.getStaffID());

                  dbref2.updateChildren(addToDatabase);

                  //Set user's status to 'waiting'
                  DatabaseReference dbref3 = db.getReference().child("User").child(uID);

                  dbref3.child("status").setValue("waiting");

                  Toast.makeText(SchoolListActivity.this, "Request sent", Toast.LENGTH_SHORT)
                      .show();
                  finish();
                }
              });

          builder1.setNegativeButton(
              "No",
              new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                  dialog.cancel();
                }
              });

          AlertDialog alert11 = builder1.create();
          alert11.show();
          //end of confirmation
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
