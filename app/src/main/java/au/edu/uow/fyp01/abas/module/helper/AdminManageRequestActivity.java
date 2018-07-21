package au.edu.uow.fyp01.abas.module.helper;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import au.edu.uow.fyp01.abas.model.RequestListModel;
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

public class AdminManageRequestActivity extends AppCompatActivity {

  private ProgressDialog progressDialog;

  private RecyclerView requestListRecyclerView;
  private FirebaseDatabase db;
  private DatabaseReference dbref;
  private FirebaseRecyclerOptions<RequestListModel> options;
  private FirebaseRecyclerAdapter<RequestListModel,
      RequestListModelViewHolder> adapter;

  //user metadata
  private FirebaseAuth auth;
  private String uID; //THIS IS THE ADMIN'S USERID
  private UserModel userModel;
  private String schID;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_manage_request);

    showProgressDialog();

    //get current user
    uID = auth.getInstance().getCurrentUser().getUid();

    UserQueryClass(new FirebaseCallBack() {
      @Override
      public void onCallBack(UserModel userModel1) {
        userModel = userModel1;

        //set up schID (School ID)
        schID = userModel.getSchID();

        //instantiate db
        db = FirebaseDatabase.getInstance();
        dbref = db.getReference().child("RequestList").child(schID);

        //RecyclerView
        requestListRecyclerView = findViewById(R.id.requestListRecyclerView);
        requestListRecyclerView.setHasFixedSize(true);
        requestListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //dbref set in order by FULLNAME
        options = new FirebaseRecyclerOptions.Builder<RequestListModel>()
            .setQuery(dbref.orderByChild("fullname"),
                RequestListModel.class).build();

        adapter =
            new FirebaseRecyclerAdapter<RequestListModel, RequestListModelViewHolder>(options) {
              @Override
              protected void onBindViewHolder(@NonNull RequestListModelViewHolder holder,
                  int position,
                  @NonNull RequestListModel model) {
                //bind object
                holder.setUserID(model.getUserID());
                holder.setFullname(model.getFullname());
                holder.setTitle(model.getTitle());
                holder.setStaffID(model.getStaffID());
                holder.setButton();

              }

              @NonNull
              @Override
              public RequestListModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                  int viewType) {
                View view1 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_model_layout_single_button,
                        parent, false);
                return new RequestListModelViewHolder(view1);
              }
            };

        requestListRecyclerView.setAdapter(adapter);
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

  public class RequestListModelViewHolder extends RecyclerView.ViewHolder {

    View mView;
    String fullname;
    String title;
    String userID;
    String staffID;


    public RequestListModelViewHolder(View itemView) {
      super(itemView);
      mView = itemView;
    }

    public void setUserID(String userID) {
      this.userID = userID;
    }

    public void setFullname(String fullname) {
      this.fullname = fullname;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public void setStaffID(String staffID) {
      this.staffID = staffID;
    }

    public void setButton() {
      //e.g. Mr. John Smith (abc123)
      String temp = title
          + " "
          + fullname
          + " ("
          + staffID
          + ")";

      final Button requestBtn = mView.findViewById(R.id.modelSingleBtn);

      //set up name
      requestBtn.setText(temp);

      requestBtn.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          //Ask for user confirmation
          AlertDialog.Builder builder1 = new AlertDialog.Builder(
              AdminManageRequestActivity.this);
          String tempstring = "Options:";
          builder1.setMessage(tempstring);
          builder1.setCancelable(true);

          builder1.setPositiveButton(
              "Approve",
              new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                  //send user's info to school's staff list
                  DatabaseReference dbref2 = db.getReference()
                      .child("Staff")
                      .child(schID)
                      .child(userID); //this is the requester's user ID!

                  //handle user input into database
                  Map<String, Object> addToDatabase = new HashMap<>();

                  addToDatabase.put("fullname", fullname);
                  addToDatabase.put("title", title);
                  addToDatabase.put("staffID", staffID);
                  addToDatabase.put("usertype", "Teacher");

                  dbref2.updateChildren(addToDatabase);

                  //Set user's status to 'registered' and change school ID
                  DatabaseReference dbref3 = db.getReference()
                      .child("User")
                      .child(userID);

                  Map<String, Object> updateUser = new HashMap<>();

                  updateUser.put("status", "registered");
                  updateUser.put("usertype", "Teacher");
                  updateUser.put("schID", schID);

                  dbref3.updateChildren(updateUser);

                  //Remove request from list
                  DatabaseReference dbref4 = db.getReference()
                      .child("RequestList")
                      .child(schID)
                      .child(userID);

                  dbref4.removeValue();

                  Toast.makeText(AdminManageRequestActivity.this, "User approved",
                      Toast.LENGTH_SHORT)
                      .show();

                }
              });
          builder1.setNeutralButton(
              "Reject",
              new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                  //change user status
                  DatabaseReference dbref5 = db.getReference()
                      .child("User")
                      .child(userID);

                  dbref5.child("status").setValue("unregistered");

                  //Remove request from list
                  DatabaseReference dbref6 = db.getReference()
                      .child("RequestList")
                      .child(schID)
                      .child(userID);

                  dbref6.removeValue();

                  Toast.makeText(AdminManageRequestActivity.this, "User rejected",
                      Toast.LENGTH_SHORT)
                      .show();
                }
              });

          builder1.setNegativeButton(
              "Cancel",
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
