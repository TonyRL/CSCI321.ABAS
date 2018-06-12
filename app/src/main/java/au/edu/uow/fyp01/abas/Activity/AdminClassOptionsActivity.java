package au.edu.uow.fyp01.abas.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.uow.fyp01.abas.Model.SchoolModel;
import au.edu.uow.fyp01.abas.R;

import static android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK;

public class AdminClassOptionsActivity extends Activity {

    private String schID;
    private String classID;

    private FirebaseDatabase db;
    private DatabaseReference dbrefFrom;
    private DatabaseReference dbrefTo;

    private List<String> classesList;
    private Map<String, SchoolModel> classesMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminclassoptions);

        Bundle bundle = getIntent().getExtras();

        //Grabbing args
        schID = bundle.getString("schID");
        classID = bundle.getString("classID");

        //Arrays for spinner
        classesList = new ArrayList<>();
        classesMap = new HashMap<>();

        //QueryClass
        SchoolQueryClass(new FirebaseCallBack() {
            @Override
            public void onCallBack(List<String> classesList1, Map<String, SchoolModel> classesMap1) {

                //Instantiate db
                db = FirebaseDatabase.getInstance();
                //origin node
                dbrefFrom = db.getReference().child("Student").child(schID).child(classID);

                classesList = classesList1;
                classesMap = classesMap1;

                //<editor-fold desc="MOVE ALL STUDENTS TO ANOTHER CLASS">
                Button adminMoveAllStudentsBtn = findViewById(R.id.adminMoveAllStudentsBtn);
                adminMoveAllStudentsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                AdminClassOptionsActivity.this);
                        builder.setTitle("Move all students to class: ");

                        //Set up the layout
                        LinearLayout layout = new LinearLayout(AdminClassOptionsActivity.this);

                        //set up the spinner as a drop down box
                        final Spinner dropdown = new Spinner(AdminClassOptionsActivity.this);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                AdminClassOptionsActivity.this,
                                android.R.layout.simple_spinner_dropdown_item,classesList);
                        dropdown.setAdapter(adapter);
                        //add dropdown to the dialog
                        layout.addView(dropdown);

                        builder.setView(layout);

                        //dialog's OK button
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get the SchoolModel from map first
                                SchoolModel schoolModel = classesMap.get(
                                        dropdown.getSelectedItem().toString());
                                //get the class ID
                                String classIDTo = schoolModel.getClassID();

                                //set the new class ID ref
                                dbrefTo = db.getReference().child("Student").child(schID).child(classIDTo);

                                //perform the move
                                moveClass(dbrefFrom,dbrefTo);

                                //Toast for success
                                Toast.makeText(AdminClassOptionsActivity.this,
                                        "Moved all students to new class", Toast.LENGTH_SHORT)
                                        .show();
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

                //<editor-fold desc="REMOVE ALL STUDENTS IN CLASS">
                Button adminRemoveAllStudentsBtn = findViewById(R.id.adminRemoveAllStudentsBtn);
                adminRemoveAllStudentsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Ask for user confirmation
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(
                                AdminClassOptionsActivity.this);
                        builder1.setMessage("Remove all students from this class?");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Delete Student->SchID->ClassID
                                        //Removing the classID is easier
                                        dbrefFrom.removeValue();
                                        //Toast for success
                                        Toast.makeText(AdminClassOptionsActivity.this,
                                                "Removed all students in class", Toast.LENGTH_SHORT)
                                                .show();

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
                    } //End of OnClick
                });
                //</editor-fold> //end of OnClickListener

                //<editor-fold desc="RENAME CLASS">
                Button adminClassRenameBtn = findViewById(R.id.adminRenameClassBtn);
                adminClassRenameBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //BUTTON BUILDER SET STYLE HERE
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminClassOptionsActivity.this,
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

                                //handle user input into database input
                                Map<String, Object> addToDatabase = new HashMap<>();

                                addToDatabase.put("classname", input_Text);

                                //push to database
                                DatabaseReference dbref = db.getReference().child("School")
                                        .child(schID).child(classID);
                                dbref.updateChildren(addToDatabase);

                                //Toast for success
                                Toast.makeText(AdminClassOptionsActivity.this,
                                        "Class renamed to " + input_Text, Toast.LENGTH_SHORT)
                                        .show();
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


            } //end of callback
        }); //end of query

    }



    private void SchoolQueryClass(final FirebaseCallBack firebaseCallBack) {
        FirebaseDatabase db2 = FirebaseDatabase.getInstance();
        DatabaseReference dbref2 = db2.getReference().child("School").child(schID);
        dbref2.orderByChild("classname").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {

                    SchoolModel schoolModel =
                            dataSnapshot.getValue(SchoolModel.class);

                    classesList.add(schoolModel.getClassname());
                    classesMap.put(schoolModel.getClassname(), schoolModel);

                }

                firebaseCallBack.onCallBack(classesList, classesMap);
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


        });

    }

    private interface FirebaseCallBack {

        void onCallBack(List<String> classesList,
                        Map<String, SchoolModel> classesMap);
    }

    //This one is to move on node to another
    public void moveClass(final DatabaseReference fromPath, final DatabaseReference toPath)
    {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener()
                {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase)
                    {
                        if (firebaseError != null)
                        {
                            System.out.println("Copy failed");
                        }
                        else
                        {
                            fromPath.removeValue();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Copy failed");
            }


        });
    }


}
