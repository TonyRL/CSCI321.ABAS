package au.edu.uow.fyp01.abas.module.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import au.edu.uow.fyp01.abas.model.SubjectModel;
import au.edu.uow.fyp01.abas.model.SubjectSettingModel;
import au.edu.uow.fyp01.abas.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class AdminSubjectSettingActivity extends AppCompatActivity {

  FirebaseDatabase db;
  DatabaseReference dbref;

  EditText assignmentRatioEditText;
  EditText quizRatioEditText;
  EditText testRatioEditText;
  EditText examRatioEditText;
  private String schID;
  private String subjectID;
  private SubjectSettingModel subjectSettingModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_subject_setting);

    Bundle bundle = getIntent().getExtras();

    //Grabbing args (classID and schID from AdminStudentDetailActivity)
    schID = bundle.getString("schID");
    subjectID = bundle.getString("subjectID");

    //set up db
    db = FirebaseDatabase.getInstance();
    dbref = db.getReference().child("SubjectSettings").child(schID).child(subjectID);

    SubjectSettingsQueryClass(new FirebaseCallBack() {
      @Override
      public void onCallBack(SubjectSettingModel subjectSettingModel1) {
        subjectSettingModel = subjectSettingModel1;

        assignmentRatioEditText = findViewById(R.id.assignmentRatioEditText);
        assignmentRatioEditText.setText(subjectSettingModel.getAssignmentratio());

        quizRatioEditText = findViewById(R.id.quizRatioEditText);
        quizRatioEditText.setText(subjectSettingModel.getQuizratio());

        testRatioEditText = findViewById(R.id.testRatioEditText);
        testRatioEditText.setText(subjectSettingModel.getTestratio());

        examRatioEditText = findViewById(R.id.examRatioEditText);
        examRatioEditText.setText(subjectSettingModel.getExamratio());
      } //end oncallback
    }); //end queryclass


  }

  private void SubjectSettingsQueryClass(final FirebaseCallBack firebaseCallBack) {

    FirebaseDatabase db2 = FirebaseDatabase.getInstance();
    DatabaseReference dbref2 = db2.getReference().child("SubjectSettings").child(schID)
        .child(subjectID);

    dbref2.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {

          SubjectSettingModel subjectSettingModel = dataSnapshot
              .getValue(SubjectSettingModel.class);
          firebaseCallBack.onCallBack(subjectSettingModel);


        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });

  }

  private interface FirebaseCallBack {
    void onCallBack(SubjectSettingModel subjectSettingModel);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_admin_subject_setting, menu);
    return true;
  }

  //Delete Button
  public void removeSubject(MenuItem mi) {
    //Ask for user confirmation
    AlertDialog.Builder builder1 = new AlertDialog.Builder(
        AdminSubjectSettingActivity.this);
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

            Toast.makeText(AdminSubjectSettingActivity.this, "Subject removed",
                Toast.LENGTH_SHORT)
                .show();
            finish();
          }
        });

    builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        dialog.cancel();
      }
    });

    AlertDialog alert11 = builder1.create();
    alert11.show();
    //end of confirmation
  }

  //Save Button
  public void saveChange(MenuItem mi) {
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

    Toast.makeText(AdminSubjectSettingActivity.this, "Subject settings saved", Toast.LENGTH_SHORT)
        .show();

    //Go back
    finish();
  }
}