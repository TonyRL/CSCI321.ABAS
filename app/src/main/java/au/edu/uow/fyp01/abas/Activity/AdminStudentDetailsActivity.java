package au.edu.uow.fyp01.abas.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import au.edu.uow.fyp01.abas.Model.BeaconModel;
import au.edu.uow.fyp01.abas.Model.StudentModel;
import au.edu.uow.fyp01.abas.PopupSearchBeaconActivity;
import au.edu.uow.fyp01.abas.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class AdminStudentDetailsActivity extends Activity {

    private FirebaseDatabase db;
    private DatabaseReference dbref;
    private Query query;

    private String classID;
    private String schID;
    private String classname;
    private String sID;
    private String beaconID;

    private StudentModel studentModel;

    private EditText adminStudentDetailsBeaconID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminstudentdetails);

        Bundle bundle = getIntent().getExtras();

        //Referencing current layout for popup window
//    frameLayout = findViewById(R.id.adminStudentDetails);

        //Grabbing args (classID and schID from AdminStudentListActivity)
        classname = bundle.getString("classname");
        classID = bundle.getString("classID");
        schID = bundle.getString("schID");
        sID = bundle.getString("sID");

        //preventing crash here
        try {
            beaconID = bundle.getString("beaconID");

        } catch (Exception e) {

            //create a temp database
            FirebaseDatabase db1 = FirebaseDatabase.getInstance();
            DatabaseReference dbref1 = db1.getReference().child("Beacon").child(schID);

            //look for corresponding beaconID in Beacon->SchoolID->BeaconID
            Query query = dbref1.orderByChild("sid").equalTo(sID);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.d(TAG, "dataSnapShot ID: " + dataSnapshot.getKey().toString());
                        for (DataSnapshot node : dataSnapshot.getChildren()) {

                            Log.d(TAG, "node ID: " + node.getKey().toString());
                            //get the node in BeaconModel
                            BeaconModel beaconModel = node.getValue(BeaconModel.class);
                            beaconID = beaconModel.getBeaconID();
                            Log.d(TAG, "beaconID = " + beaconID);

                        }
                    } else {
                        beaconID = "NOTSET";
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            }); //endquery
        }

        //instantiate db
        db = FirebaseDatabase.getInstance();
        dbref = db.getReference().child("Student").child(schID).child(classID).child(sID);

        //Beacon ID
        adminStudentDetailsBeaconID = findViewById(R.id.adminStudentDetailsBeaconID);

        StudentQueryClass(new FirebaseCallBack() {
            @Override
            public void onCallBack(StudentModel studentModel1) {
                studentModel = studentModel1;

                //create a temp database
                FirebaseDatabase db1 = FirebaseDatabase.getInstance();
                DatabaseReference dbref1 = db1.getReference().child("Beacon").child(schID);

                //look for corresponding beaconID in Beacon->SchoolID->BeaconID
                Query query = dbref1.orderByChild("sid").equalTo(sID);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Log.d(TAG, "dataSnapShot ID: " + dataSnapshot.getKey().toString());
                            for (DataSnapshot node : dataSnapshot.getChildren()) {

                                Log.d(TAG, "node ID: " + node.getKey().toString());
                                //get the node in BeaconModel
                                BeaconModel beaconModel = node.getValue(BeaconModel.class);
                                beaconID = beaconModel.getBeaconID();
                                Log.d(TAG, "beaconID = " + beaconID);

                            }
                        } else {
                            beaconID = "NOTSET";
                        }

                        adminStudentDetailsBeaconID.setText(beaconID);


                        //First name
                        final EditText adminStudentDetailsFirstName = findViewById(
                                R.id.adminStudentDetailsFirstName);
                        adminStudentDetailsFirstName.setText(studentModel.getFirstname());

                        //Last name
                        final EditText adminStudentDetailsLastName = findViewById(R.id.adminStudentDetailsLastName);
                        adminStudentDetailsLastName.setText(studentModel.getLastname());

                        //Class number
                        final EditText adminStudentDetailsClassNumber = findViewById(
                                R.id.adminStudentDetailsClassNumber);
                        adminStudentDetailsClassNumber.setText(studentModel.getClassnumber());

                        //Student ID
                        final EditText adminStudentDetailsID = findViewById(R.id.adminStudentDetailsID);
                        adminStudentDetailsID.setText(studentModel.getSid());

                        //<editor-fold desc="Edit student details button">
                        Button adminStudentDetailsEditBtn = findViewById(R.id.adminStudentDetailsEditBtn);
                        adminStudentDetailsEditBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //GET TEXT FROM INPUT FIRST
                                String firstname = adminStudentDetailsFirstName.getText().toString();
                                String lastname = adminStudentDetailsLastName.getText().toString();
                                String classnumber = adminStudentDetailsClassNumber.getText().toString();
                                String sID = adminStudentDetailsID.getText().toString();

                                //handle user input into database
                                Map<String, Object> addToDatabase = new HashMap<>();

                                addToDatabase.put("classnumber", classnumber);
                                addToDatabase.put("firstname", firstname);
                                addToDatabase.put("lastname", lastname);
                                addToDatabase.put("sid", sID);
                                //update children of Student->SchID->ClassID->StudentID
                                dbref.updateChildren(addToDatabase);

                                //FIX THE BEACONID
                                FirebaseDatabase db1 = FirebaseDatabase.getInstance();
                                DatabaseReference dbref1 = db1.getReference().child("Beacon").child(schID).child(beaconID);
                                //this is for putting into Beacon node
                                Map<String, Object> addToBeacon = new HashMap<>();
                                addToBeacon.put("schID", schID);
                                addToBeacon.put("beaconID", beaconID);
                                addToBeacon.put("classID", classID);
                                addToBeacon.put("sid", sID);
                                //push to Beacon node
                                dbref1.updateChildren(addToBeacon);

                                Toast.makeText(AdminStudentDetailsActivity.this, "Student details saved",
                                        Toast.LENGTH_SHORT)
                                        .show();

                            }
                        });
                        //</editor-fold>

                        //<editor-fold desc="Remove student from class button">
                        Button adminStudentDetailsDeleteBtn = findViewById(R.id.adminStudentDetailsDeleteBtn);
                        adminStudentDetailsDeleteBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Ask for user confirmation
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(
                                        AdminStudentDetailsActivity.this);
                                builder1.setMessage("Are you sure you want to remove the student from this class?");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //Delete Student->SchID->ClassID->StudentID
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

                        //<editor-fold desc="Student's subjects button>
                        Button adminStudentDetailsSubjectsBtn = findViewById(R.id.adminStudentDetailsSubjectsBtn);
                        adminStudentDetailsSubjectsBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //<editor-fold desc="Transaction to move to 'AdminStudentSubjectListActivity'">

                                Intent i = new Intent(getApplicationContext(), AdminStudentSubjectListActivity.class);

                                //Passing 'sID','classID','schID' to AdminStudentSubjectListActivity
                                Bundle args = new Bundle();
                                args.putString("sID", sID);
                                args.putString("schID", schID);
                                args.putString("firstname", studentModel.getFirstname());
                                args.putString("lastname", studentModel.getLastname());
                                args.putString("classID", classID);

                                i.putExtras(args);

                                startActivity(i);

                                //</editor-fold>
                            }
                        });
                        //</editor-fold>

                        //<editor-fold desc="Search beacon button>
                        ImageButton adminStudentDetailSearchBeaconBtn = findViewById(
                                R.id.adminStudentDetailsSearchBeaconBtn);
                        adminStudentDetailSearchBeaconBtn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Better to use PopupWindow
                                //startActivity(new Intent(AdminStudentDetailsActivity.this, PopupSearchBeaconActivity.class));
                                startActivityForResult(
                                        new Intent(AdminStudentDetailsActivity.this, PopupSearchBeaconActivity.class),
                                        1234);
                            }
                        });


                        //</editor-fold>
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }); //endquery
            } //on callback end
        });//query class end
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            adminStudentDetailsBeaconID.setText(data.getStringExtra("UUID"));

            //set beacon ID here
            beaconID = data.getStringExtra("UUID");
        }
    }

    private void StudentQueryClass(final FirebaseCallBack firebaseCallBack) {
        FirebaseDatabase db2 = FirebaseDatabase.getInstance();
        DatabaseReference dbref2 = db2.getReference().child("Student").child(this.schID)
                .child(this.classID);

        query = dbref2.orderByChild("sid").equalTo(this.sID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot node : dataSnapshot.getChildren()) {
                        StudentModel studentModel = node.getValue(StudentModel.class);
                        firebaseCallBack.onCallBack(studentModel);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private interface FirebaseCallBack {

        void onCallBack(StudentModel studentModel);
    }

}
