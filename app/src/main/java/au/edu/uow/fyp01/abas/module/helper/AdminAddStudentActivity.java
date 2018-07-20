package au.edu.uow.fyp01.abas.module.helper;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import au.edu.uow.fyp01.abas.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class AdminAddStudentActivity extends AppCompatActivity {

  private DatabaseReference dbref;
  private String classID;
  private String schID;
  private String classname;
  private FirebaseDatabase db;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_add_student);
    Bundle bundle = getIntent().getExtras();

    //Grabbing args (classID and schID from AdminStudentListActivity)
    classID = bundle.getString("classID");
    schID = bundle.getString("schID");
    classname = bundle.getString("classname");

    //firebase database
    db = FirebaseDatabase.getInstance();
    dbref = db.getReference().child("Student").child(schID).child(classID);

    final EditText adminAddStudentFirstName = findViewById(R.id.adminAddStudentFirstName);
    final EditText adminAddStudentLastName = findViewById(R.id.adminAddStudentLastName);
    final EditText adminAddStudentClassNumber = findViewById(R.id.adminAddStudentClassNumber);
    final EditText adminAddStudentID = findViewById(R.id.adminAddStudentID);

    Button adminAddStudentBtn = findViewById(R.id.adminAddStudentBtn);
    adminAddStudentBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //Ask for user confirmation
        AlertDialog.Builder builder1 = new AlertDialog.Builder(AdminAddStudentActivity.this);
        builder1.setMessage("Add new student?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
            "Yes",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                //GET TEXT FROM INPUT FIRST
                String firstname = adminAddStudentFirstName.getText().toString();
                String lastname = adminAddStudentLastName.getText().toString();
                String classnumber = adminAddStudentClassNumber.getText().toString();
                String sID = adminAddStudentID.getText().toString();

                //DO CHECKING IF ANY OF THE BOXES ARE EMPTY
                if (firstname.matches("") || lastname.matches("") ||
                    classnumber.matches("") || sID.matches("")) {
                  Toast.makeText(AdminAddStudentActivity.this, "One of more boxes are empty!",
                      Toast.LENGTH_SHORT)
                      .show();
                  return;
                } else {
                  //handle user input into database
                  Map<String, Object> addToDatabase = new HashMap<>();

                  //For ListOfStudents
                  Map<String, Object> addToDatabase2 = new HashMap<>();

                  addToDatabase.put("classname", classname);
                  addToDatabase.put("classnumber", classnumber);
                  addToDatabase.put("firstname", firstname);
                  addToDatabase.put("lastname", lastname);
                  addToDatabase.put("sid", sID);

                  //update children of Student->SchID->ClassID
                  //move one level down to ->StudentID
                  dbref.child(sID).updateChildren(addToDatabase);

                  addToDatabase2.put("firstname", classnumber);
                  addToDatabase2.put("lastname", lastname);
                  addToDatabase2.put("sid", sID);

                  //this points to ListOfStudents->SchID->StudentID
                  dbref = db.getReference().child("ListOfStudents").child(schID)
                      .child(sID);
                  dbref.updateChildren(addToDatabase2);

                  //For Beacon
                  Map<String, Object> addtoBeacon = new HashMap<>();

                  addtoBeacon.put("classID", classID);
                  addtoBeacon.put("schID", schID);
                  addtoBeacon.put("sid", sID);
                  addtoBeacon.put("beaconID", "NOTSET");
                  //this points to Beacon->SchoolID->NOTSET(overriding this doesnt matter)
                  dbref = db.getReference().child("Beacon").child(schID).child("NOTSET");
                  dbref.updateChildren(addtoBeacon);

                  Toast.makeText(AdminAddStudentActivity.this,
                      "Added new student to class " + classname, Toast.LENGTH_SHORT)
                      .show();
                  finish();
                }
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
      //</editor-fold>
    });
    //end add student

    /*
    //Add existing student
    final EditText adminExistingSID = findViewById(R.id.adminExistingSID);
    final EditText adminExistingClassNumber = findViewById(R.id.adminExistingClassNumber);
    Button adminAddExistingStudentBtn = findViewById(R.id.adminAddExistingStudentBtn);
    adminAddExistingStudentBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //Ask for user confirmation
        AlertDialog.Builder builder1 = new AlertDialog.Builder(AdminAddStudentActivity.this);
        builder1.setMessage("Add new student?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
            "Yes",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                //GET TEXT FROM INPUT FIRST
                final String existingSID = adminExistingSID.getText().toString();
                final String existingClassNumber = adminExistingClassNumber.getText().toString();

                //DO CHECKING IF ANY OF THE BOXES ARE EMPTY
                if (existingSID.matches("") || existingClassNumber.matches("")) {
                  Toast.makeText(AdminAddStudentActivity.this, "One of more boxes are empty!",
                      Toast.LENGTH_SHORT)
                      .show();
                  return;
                } else {
                  FirebaseDatabase db2 = FirebaseDatabase.getInstance();
                  DatabaseReference dbref2 = db2.getReference().child("ListOfStudents")
                      .child(schID);
                  Query query = dbref2.orderByChild("sid").equalTo(existingSID);

                  query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                      if (dataSnapshot.exists()) {

                        for (DataSnapshot node : dataSnapshot.getChildren()) {
                          //School's record of student, not classroom
                          ListOfStudentsModel student1 = node.getValue(ListOfStudentsModel.class);

                          //handle user input into database
                          Map<String, Object> addToDatabase = new HashMap<>();

                          addToDatabase.put("classname", classname);
                          addToDatabase.put("classnumber", existingClassNumber);
                          addToDatabase.put("firstname", student1.getFirstname());
                          addToDatabase.put("lastname", student1.getLastname());
                          addToDatabase.put("sid", existingSID);

                          //update children of Student->SchID->ClassID
                          //move one level down to ->StudentID
                          dbref.child(existingSID).updateChildren(addToDatabase);

                          Toast.makeText(AdminAddStudentActivity.this,
                              "Added student to class " + classname, Toast.LENGTH_SHORT)
                              .show();
                          finish();
                        }

                      } else {
                        Toast.makeText(AdminAddStudentActivity.this,
                            "Student not found!" + classname, Toast.LENGTH_SHORT)
                            .show();
                      }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                  });
                }


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
    }); // end on click
    */

  }
}
