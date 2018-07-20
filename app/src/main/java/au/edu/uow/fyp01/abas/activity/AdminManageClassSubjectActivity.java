package au.edu.uow.fyp01.abas.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import au.edu.uow.fyp01.abas.model.ListOfSubjectsModel;
import au.edu.uow.fyp01.abas.model.SchoolModel;
import au.edu.uow.fyp01.abas.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdminManageClassSubjectActivity extends AppCompatActivity {

  private String schID;

  private List<String> subjectsList;
  private Map<String, ListOfSubjectsModel> subjectsMap;
  private List<String> classesList;
  private Map<String, SchoolModel> classesMap;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_adminmanageclasssubject);

    Bundle bundle = getIntent().getExtras();
    schID = bundle.getString("schID");

    //Arrays for spinners (subject)
    subjectsList = new ArrayList<>();
    subjectsMap = new HashMap<>();

    classesList = new ArrayList<>();
    classesMap = new HashMap<>();

    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbref = db.getReference().child("ListOfSubjects").child(schID);
    dbref.orderByChild("subjectname").addChildEventListener(new ChildEventListener() {
      @Override
      public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if (dataSnapshot.exists()) {

          ListOfSubjectsModel listOfSubjectsModel =
              dataSnapshot.getValue(ListOfSubjectsModel.class);

          subjectsList.add(listOfSubjectsModel.getSubjectname());
          subjectsMap.put(listOfSubjectsModel.getSubjectname(), listOfSubjectsModel);

        }

        DatabaseReference dbref2 = db.getReference().child("School").child(schID);
        dbref2.orderByChild("classname").addChildEventListener(new ChildEventListener() {
          @Override
          public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot.exists()) {

              SchoolModel schoolModel =
                  dataSnapshot.getValue(SchoolModel.class);

              classesList.add(schoolModel.getClassname());
              classesMap.put(schoolModel.getClassname(), schoolModel);

              Set<String> set = new HashSet<>();
              set.addAll(classesList);
              classesList.clear();
              classesList.addAll(set);
              Collections.sort(classesList, Collator.getInstance());

            }
            //Log.d(TAG, String.valueOf(classesList));

            //the rest of the code
            final Spinner subjectSpinner = findViewById(R.id.subjectSpinner);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                AdminManageClassSubjectActivity.this,
                android.R.layout.simple_spinner_dropdown_item, subjectsList);
            subjectSpinner.setAdapter(adapter1);

            final Spinner classSpinner = findViewById(R.id.classSpinner);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                AdminManageClassSubjectActivity.this,
                android.R.layout.simple_spinner_dropdown_item, classesList);
            classSpinner.setAdapter(adapter2);

            final Button addSubjectToClassBtn = findViewById(R.id.addSubjectToClassBtn);
            addSubjectToClassBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                //Ask for user confirmation
                AlertDialog.Builder builder1 = new AlertDialog.Builder(
                    AdminManageClassSubjectActivity.this);
                builder1.setMessage("Add subject to class?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int id) {

                        try {
                          //get the ListOfSubjectsModel from map
                          ListOfSubjectsModel listOfSubjectsModel =
                              subjectsMap.get(subjectSpinner.getSelectedItem().toString());
                          //get the SchoolModel from map
                          SchoolModel schoolModel =
                              classesMap.get(classSpinner.getSelectedItem().toString());

                          //get the subject ID
                          final String subjectID = listOfSubjectsModel.getSubjectID();
                          //get the class ID
                          String classID = schoolModel.getClassID();

                          //put user input into data
                          final Map<String, Object> addToDatabase = new HashMap<>();
                          addToDatabase
                              .put("subjectname", subjectSpinner.getSelectedItem().toString());
                          addToDatabase.put("subjectID", subjectID);

                          //new db ref
                          DatabaseReference dbref3 = db.getReference().child("Subject")
                              .child(schID).child(classID);
                          dbref3.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                              if (dataSnapshot.exists()) {

                                dataSnapshot.getRef().child(subjectID)
                                    .updateChildren(addToDatabase);


                              }
                            }

                            //<editor-fold>
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
                            //</editor-fold>
                          }); // end new db ref

                          Toast.makeText(AdminManageClassSubjectActivity.this,
                              "Added " + subjectSpinner.getSelectedItem().toString() +
                                  " to class " + schoolModel.getClassname(), Toast.LENGTH_SHORT)
                              .show();

                        } catch (Exception e) {
                          Toast.makeText(AdminManageClassSubjectActivity.this,
                              "Please ensure all options are picked properly", Toast.LENGTH_SHORT)
                              .show();
                        }
                      } // end on click
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

          //<editor-fold>
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
          //</editor-fold>
        });//end query for classeslist
      }

      //<editor-fold>
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
      //</editor-fold>
    }); //end query for listofsubjects
  }
}
