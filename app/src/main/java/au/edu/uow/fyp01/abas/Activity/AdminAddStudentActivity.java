package au.edu.uow.fyp01.abas.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import au.edu.uow.fyp01.abas.R;

public class AdminAddStudentActivity extends Activity {

    private DatabaseReference dbref;
    private String classID;
    private String schID;
    private String classname;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminstudentlist);
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
        //onclick
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
                                if (firstname.matches("")|| lastname.matches("")||
                                        classnumber.matches("")|| sID.matches("")){
                                    Toast.makeText(AdminAddStudentActivity.this,"One of more boxes are empty!", Toast.LENGTH_SHORT)
                                            .show();
                                    return;
                                }
                                else {
                                    //handle user input into database
                                    Map<String, Object> addToDatabase = new HashMap<>();

                                    addToDatabase.put("classname",classname);
                                    addToDatabase.put("classnumber",classnumber);
                                    addToDatabase.put("firstname",firstname);
                                    addToDatabase.put("lastname",lastname);
                                    addToDatabase.put("sid",sID);

                                    dbref.updateChildren(addToDatabase);

                                    Toast.makeText(AdminAddStudentActivity.this,"Added to new student to class " + classname, Toast.LENGTH_SHORT)
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
        //end onclick

    }
}
