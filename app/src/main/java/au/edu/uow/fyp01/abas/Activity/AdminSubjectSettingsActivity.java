package au.edu.uow.fyp01.abas.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import au.edu.uow.fyp01.abas.Model.SubjectModel;
import au.edu.uow.fyp01.abas.Model.SubjectSettingsModel;
import au.edu.uow.fyp01.abas.R;

public class AdminSubjectSettingsActivity extends Activity {

    private String schID;
    private String subjectID;
    private SubjectSettingsModel subjectSettingsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminsubjectsettings);


        Bundle bundle = getIntent().getExtras();

        //Grabbing args (classID and schID from AdminStudentDetailsActivity)
        schID = bundle.getString("schID");
        subjectID = bundle.getString("subjectID");

        //set up db
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference dbref = db.getReference().child("SubjectSettings")
                .child(schID).child(subjectID);

        SubjectSettingsQueryClass(new FirebaseCallBack() {
            @Override
            public void onCallBack(SubjectSettingsModel subjectSettingsModel1) {
                subjectSettingsModel = subjectSettingsModel1;


                final EditText assignmentRatioEditText = findViewById(R.id.assignmentRatioEditText);
                assignmentRatioEditText.setText(subjectSettingsModel.getAssignmentratio());

                final EditText quizRatioEditText = findViewById(R.id.quizRatioEditText);
                quizRatioEditText.setText(subjectSettingsModel.getQuizratio());

                final EditText testRatioEditText = findViewById(R.id.testRatioEditText);
                testRatioEditText.setText(subjectSettingsModel.getTestratio());

                final EditText examRatioEditText = findViewById(R.id.examRatioEditText);
                examRatioEditText.setText(subjectSettingsModel.getExamratio());

                //Save Button
                Button subjectSettingsSaveBtn = findViewById(R.id.subjectSettingsSaveBtn);
                subjectSettingsSaveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //GET TEXT FROM INPUT FIRST
                        String assignmentratio = assignmentRatioEditText.getText().toString();
                        String quizratio = quizRatioEditText.getText().toString();
                        String testratio = testRatioEditText.getText().toString();
                        String examratio = examRatioEditText.getText().toString();

                        //handle user input into database
                        Map<String, Object> addToDatabase = new HashMap<>();

                        addToDatabase.put("assignmentratio", assignmentratio);
                        addToDatabase.put("quizratio", quizratio);
                        addToDatabase.put("testratio", testratio);
                        addToDatabase.put("examratio", examratio);

                        //update children of SubjectSettings->SchID->Subject ID
                        dbref.updateChildren(addToDatabase);

                        Toast.makeText(AdminSubjectSettingsActivity.this, "Subject settings saved",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                });

                //Delete Button
                Button subjectSettingsDeleteBtn = findViewById(R.id.subjectSettingsDeleteBtn);
                subjectSettingsDeleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Ask for user confirmation
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(AdminSubjectSettingsActivity.this);
                        builder1.setMessage("Remove subject from school?");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Delete Subject->StudentID->SubjectID
                                        DatabaseReference dbref1 =
                                                db.getReference().child("ListOfSubjects").child(schID);

                                        dbref1.child(subjectID).removeValue();

                                        //TODO delete subject from records and comments as well
                                        //QUERY TO REMOVE SUBJECT FROM ALL STUDENTS IN SCHOOL
                                        FirebaseDatabase db2 = FirebaseDatabase.getInstance();
                                        final DatabaseReference dbref2 = db2.getReference().child("Subject")
                                                .child(schID);
                                        dbref2.addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                                if (dataSnapshot.exists()) {
                                                    //Subject -> SchID -> ClassID
                                                    for (DataSnapshot classIDnode : dataSnapshot.getChildren()) {
                                                        if (classIDnode.exists()) {


                                                            //Subject -> SchID -> ClassID -> StudentID
                                                            for (DataSnapshot studentIDnode : classIDnode.getChildren()) {

                                                                SubjectModel subjectModel =
                                                                        studentIDnode.getValue(SubjectModel.class);

                                                                if (subjectModel.getSubjectID().equals(subjectID)) {
                                                                    studentIDnode.getRef().removeValue();
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                            }

                                            @Override
                                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                                            }

                                            @Override
                                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });//end query

                                        //QUERY TO REMOVE SUBJECT RECORDS FROM STUDENT
                                        FirebaseDatabase db3 = FirebaseDatabase.getInstance();
                                        final DatabaseReference dbref3 = db3.getReference().child("Record");
                                        dbref3.addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                if (dataSnapshot.exists()) {
                                                    //Record->StudentID
                                                    for (DataSnapshot studentIDnode : dataSnapshot.getChildren()) {
                                                        if (studentIDnode.exists()) {
                                                            //Record->StudentID->SubjectID
                                                            for (DataSnapshot subjectIDnode : studentIDnode.getChildren()) {

                                                                if (subjectIDnode.getKey().toString().equals(subjectID)) {
                                                                    subjectIDnode.getRef().removeValue();
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                            }

                                            @Override
                                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                                            }

                                            @Override
                                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });//end query

                                        //QUERY TO REMOVE SUBJECT RECORDS FROM STUDENT
                                        FirebaseDatabase db4 = FirebaseDatabase.getInstance();
                                        final DatabaseReference dbref4 = db4.getReference().child("Comment");
                                        dbref3.addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                if (dataSnapshot.exists()) {
                                                    //Record->StudentID
                                                    for (DataSnapshot studentIDnode : dataSnapshot.getChildren()) {
                                                        if (studentIDnode.exists()) {
                                                            //Record->StudentID->SubjectID
                                                            for (DataSnapshot subjectIDnode : studentIDnode.getChildren()) {
                                                                if (subjectIDnode.getKey().toString().equals(subjectID)) {
                                                                    subjectIDnode.getRef().removeValue();
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                            }

                                            @Override
                                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                                            }

                                            @Override
                                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });//end query

                                        //QUERY TO REMOVE SUBJECT SETTINGS
                                        FirebaseDatabase db5 = FirebaseDatabase.getInstance();
                                        DatabaseReference dbref5 = db5.getReference()
                                                .child("SubjectSettings")
                                                .child(schID)
                                                .child(subjectID);
                                        dbref5.removeValue();

                                        Toast.makeText(AdminSubjectSettingsActivity.this, "Subject removed",
                                                Toast.LENGTH_SHORT)
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
            } //end oncallback
        }); //end queryclass


    }

    private void SubjectSettingsQueryClass(final FirebaseCallBack firebaseCallBack) {

        FirebaseDatabase db2 = FirebaseDatabase.getInstance();
        DatabaseReference dbref2 = db2.getReference().child("SubjectSettings")
                .child(schID).child(subjectID);

        dbref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    SubjectSettingsModel subjectSettingsModel =
                            dataSnapshot.getValue(SubjectSettingsModel.class);
                    firebaseCallBack.onCallBack(subjectSettingsModel);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private interface FirebaseCallBack {

        void onCallBack(SubjectSettingsModel subjectSettingsModel);

    }
}

/*

 */
