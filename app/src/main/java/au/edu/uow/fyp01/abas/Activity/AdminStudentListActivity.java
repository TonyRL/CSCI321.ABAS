package au.edu.uow.fyp01.abas.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ProgressBar;
import au.edu.uow.fyp01.abas.Model.StudentModel;
import au.edu.uow.fyp01.abas.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminStudentListActivity extends AppCompatActivity {

  private ProgressDialog progressDialog;

  private RecyclerView adminStudentListRecyclerView;
  private DatabaseReference dbref;
  private FirebaseRecyclerOptions<StudentModel> options;
  private FirebaseRecyclerAdapter<StudentModel, StudentModelViewHolder> firebaseRecyclerAdapter;
  private FirebaseDatabase db;

  private String classID;
  private String schID;
  private String classname;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_adminstudentlist);
    Bundle bundle = getIntent().getExtras();

    //Grabbing args (classID and schID from ClassListActivity)
    classname = bundle.getString("classname");
    classID = bundle.getString("classID");
    schID = bundle.getString("schID");

    showProgressDialog();

    //Instantiate the database
    db = FirebaseDatabase.getInstance();


    //RecyclerView
    adminStudentListRecyclerView = findViewById(R.id.adminStudentListRecyclerView);
    adminStudentListRecyclerView.setHasFixedSize(true);
    adminStudentListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    dbref = db.getReference().child("Student").child(schID).child(classID);

    //set options for adapter
    //dbref is set to order the list by CLASS NUMBER.
    options = new FirebaseRecyclerOptions.Builder<StudentModel>().
        setQuery(dbref.orderByChild("classnumber"), StudentModel.class).build();

    firebaseRecyclerAdapter =
        new FirebaseRecyclerAdapter<StudentModel, StudentModelViewHolder>(options) {
          @Override
          protected void onBindViewHolder(@NonNull StudentModelViewHolder holder, int position,
              @NonNull StudentModel model) {
            //bind object
            holder.setsID(model.getSid());
            holder.setClassnumber(model.getClassnumber());
            holder.setFirstname(model.getFirstname());
            holder.setLastname(model.getLastname());
            holder.setButton();
          }

          @NonNull
          @Override
          public StudentModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
              int viewType) {
            View view1 = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclermodellayout_singlebutton, parent, false);
            return new StudentModelViewHolder(view1);
          }
        };

    adminStudentListRecyclerView.setAdapter(firebaseRecyclerAdapter);
    hideProgressDialog();

    //<editor-fold desc="Add Button for new students in a class">
    Button adminStudentListAddBtn = findViewById(R.id.adminStudentListAddBtn);
    adminStudentListAddBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //<editor-fold desc="Transaction to move to 'AdminAddStudentActivity'">

        Intent i = new Intent(getApplicationContext(), AdminAddStudentActivity.class);

        //Passing 'sID','classID','schID' to AdminAddStudentActivity
        Bundle args = new Bundle();
        args.putString("classID", classID);
        args.putString("schID", schID);
        args.putString("classname", classname);

        i.putExtras(args);

        startActivity(i);

        //</editor-fold>
      }
    });
    //</editor-fold>

    //<editor-fold desc="Delete class button">
    Button adminStudentListDeleteClassBtn = findViewById(R.id.adminStudentListDeleteClassBtn);
    adminStudentListDeleteClassBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //Ask for user confirmation
        AlertDialog.Builder builder1 = new AlertDialog.Builder(AdminStudentListActivity.this);
        builder1.setMessage("Are you sure you want to delete this class?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
            "Yes",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                //Delete Student->SchID->ClassID
                dbref.removeValue();
                dbref = db.getReference().child("School").child(schID).child(classID);
                //Delete School->SchID->ClassID
                dbref.removeValue();

                //close activity
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
    //</editor-fold>

    //<editor-fold desc="Class Options Button">
    Button adminClassOptionsBtn = findViewById(R.id.adminClassOptionsBtn);
    adminClassOptionsBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //<editor-fold desc="Transaction to move to 'AdminClassOptionsActivity'">

        Intent i = new Intent(getApplicationContext(), AdminClassOptionsActivity.class);

        //Passing to AdminClassOptionsActivity
        Bundle args = new Bundle();
        args.putString("classID", classID);
        args.putString("schID", schID);

        i.putExtras(args);

        startActivity(i);

        //</editor-fold>
      }
    });
    //</editor-fold>

  }


  @Override
  public void onStart() {
    super.onStart();
    firebaseRecyclerAdapter.startListening();
  }

  @Override
  public void onStop() {
    super.onStop();
    firebaseRecyclerAdapter.stopListening();
  }

  public class StudentModelViewHolder extends RecyclerView.ViewHolder {

    View mView;
    String sID;
    String classnumber;
    String firstname;
    String lastname;

    public StudentModelViewHolder(View itemView) {
      super(itemView);
      mView = itemView;
    }

    public void setsID(String sID) {
      this.sID = sID;
    }

    public void setClassnumber(String classnumber) {
      this.classnumber = classnumber;
    }

    public void setFirstname(String firstname) {
      this.firstname = firstname;
    }

    public void setLastname(String lastname) {
      this.lastname = lastname;
    }

    public void setButton() {
      //points to recyclermodellayout_singlebutton
      //The button is for each student (e.g. 1A, 1B, 1C)
      final Button studentButtonView = mView.findViewById(R.id.modelSingleBtn);

      //set up name of the button
      //Order - Class number : Last name, First name
      //Example button - 1 : Lastname, Firstname
      String temp = classnumber + " : " + lastname + ", " + firstname;
      studentButtonView.setText(temp);

      studentButtonView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          //<editor-fold desc="Transaction to move to 'AdminStudentDetailsActivity'">

          Intent i = new Intent(getApplicationContext(), AdminStudentDetailsActivity.class);

          //Passing 'sID','classID','schID' to AdminStudentDetailsActivity
          Bundle args = new Bundle();

          args.putString("classID", classID);
          args.putString("sID", sID);
          args.putString("classID", classID);
          args.putString("schID", schID);
          args.putString("classname", classname);

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
