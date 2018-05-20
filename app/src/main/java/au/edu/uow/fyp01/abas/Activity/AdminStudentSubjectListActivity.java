package au.edu.uow.fyp01.abas.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.uow.fyp01.abas.Model.ListOfSubjectsModel;
import au.edu.uow.fyp01.abas.Model.SubjectModel;
import au.edu.uow.fyp01.abas.R;

public class AdminStudentSubjectListActivity extends Activity {

    private String schID;
    private String sID;
    private String firstname;
    private String lastname;

    private FirebaseDatabase db;
    private RecyclerView adminStudentListRecyclerView;
    private DatabaseReference dbref;
    private FirebaseRecyclerOptions<SubjectModel> options;
    private FirebaseRecyclerAdapter<SubjectModel, SubjectModelViewHolder> firebaseRecyclerAdapter;

    private List<String> subjectsList;
    private Map<String, ListOfSubjectsModel> subjectsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminstudentsubjectlist);


        Bundle bundle = getIntent().getExtras();

        //Grabbing args (classID and schID from AdminStudentListActivity)
        schID = bundle.getString("schID");
        sID = bundle.getString("sID");
        firstname = bundle.getString("firstname");
        lastname = bundle.getString("lastname");

        //Set the subject's textview (the title)
        TextView adminStudentSubjectNameTextView = findViewById(R.id.adminStudentSubjectNameTextView);
        adminStudentSubjectNameTextView.setText(firstname + " " + lastname + "'s Subjects");

        final ProgressBar adminStudentSubjectListProgressBar = findViewById(R.id.adminStudentSubjectProgressBar);
        adminStudentSubjectListProgressBar.setIndeterminate(true);

        //Arrays for spinner
        subjectsList = new ArrayList<>();
        subjectsMap = new HashMap<>();

        //QueryClass
        ListOfSubjectsQueryClass(new FirebaseCallBack() {
            @Override
            public void onCallBack(List<String> subjectsList1, Map<String, ListOfSubjectsModel> subjectsMap1) {

                //instantiate db
                db = FirebaseDatabase.getInstance();
                dbref = db.getReference().child("Subject").child(sID);


                subjectsList = subjectsList1;
                subjectsMap = subjectsMap1;

                //RecyclerView
                adminStudentListRecyclerView = findViewById(R.id.adminStudentSubjectRecyclerView);
                adminStudentListRecyclerView.setHasFixedSize(true);
                adminStudentListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                //set options for adapter
                options = new FirebaseRecyclerOptions.Builder<SubjectModel>().
                        setQuery(dbref, SubjectModel.class).build();


                firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<SubjectModel, SubjectModelViewHolder>(options) {
                            @Override
                            protected void onBindViewHolder(@NonNull SubjectModelViewHolder holder, int position,
                                                            @NonNull SubjectModel model) {
                                //bind object
                                holder.setSubjectID(model.getSubjectID());
                                holder.setSubjectname(model.getSubjectname());
                            }

                            @NonNull
                            @Override
                            public SubjectModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                             int viewType) {
                                View view1 = LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.recyclermodellayout_singlebutton, parent, false);
                                return new SubjectModelViewHolder(view1);
                            }
                        };



                adminStudentListRecyclerView.setAdapter(firebaseRecyclerAdapter);
                firebaseRecyclerAdapter.startListening();
                adminStudentSubjectListProgressBar.setVisibility(View.GONE);

                //<editor-fold desc="Add button for subjects">
                Button adminStudentSubjectAddBtn = findViewById(R.id.adminStudentSubjectAddBtn);

                adminStudentSubjectAddBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminStudentSubjectListActivity.this);
                        builder.setTitle("Add subject to student: ");

                        //Set up the layout
                        LinearLayout layout = new LinearLayout(AdminStudentSubjectListActivity.this);

                        //set up the spinner as a drop down box
                        final Spinner dropdown = new Spinner(AdminStudentSubjectListActivity.this);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdminStudentSubjectListActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, subjectsList);
                        dropdown.setAdapter(adapter);
                        //add dropdown to the dialog
                        layout.addView(dropdown);

                        builder.setView(layout);

                        //dialog's OK button
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get the ListofSubjetsModel from map first
                                ListOfSubjectsModel listOfSubjectsModel = subjectsMap.get(
                                        dropdown.getSelectedItem().toString());
                                //get the subject ID
                                String subjectID = listOfSubjectsModel.getSubjectID();

                                //put user input into database
                                final Map<String, Object> addToDatabase = new HashMap<>();
                                addToDatabase.put("subjectname", dropdown.getSelectedItem().toString());
                                addToDatabase.put("subjectID", subjectID);

                                //new db ref
                                DatabaseReference dbref2 = db.getReference().child("Subject").child(sID)
                                        .child(subjectID);

                                dbref2.updateChildren(addToDatabase);
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

            } //end oncallback
        }); //end queryclass
    }

    public class SubjectModelViewHolder extends RecyclerView.ViewHolder {

        View mView;
        String subjectname;
        String subjectID;

        public SubjectModelViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setSubjectID(String subjectID) {
            this.subjectID = subjectID;
        }

        public void setSubjectname(String subjectname1) {
            Button subjectNameButtonView = mView.findViewById(R.id.modelSingleBtn);
            subjectNameButtonView.setText(subjectname1);
            this.subjectname = subjectname1;

            //<editor-fold desc="Remove subject from student">
            subjectNameButtonView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //Ask for user confirmation
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AdminStudentSubjectListActivity.this);
                    builder1.setMessage("Remove subject from student?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Delete Subject->StudentID->SubjectID
                                    dbref.child(subjectID).removeValue();

                                    //Remove students' records
                                    dbref = db.getReference().child("Record").child(sID)
                                            .child(subjectID);
                                    dbref.removeValue();

                                    //Remove students' comments
                                    dbref = db.getReference().child("Comment").child(sID)
                                            .child(subjectID);
                                    dbref.removeValue();
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
                    return true;
                }
            });
            //</editor-fold>
        }
    }

    private void ListOfSubjectsQueryClass(final FirebaseCallBack firebaseCallBack) {
        FirebaseDatabase db2 = FirebaseDatabase.getInstance();
        DatabaseReference dbref2 = db2.getReference().child("ListOfSubjects").child(schID);
        dbref2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {


                        ListOfSubjectsModel listOfSubjectsModel =
                                dataSnapshot.getValue(ListOfSubjectsModel.class);

                        subjectsList.add(listOfSubjectsModel.getSubjectname());
                        subjectsMap.put(listOfSubjectsModel.getSubjectname(), listOfSubjectsModel);

                }

                firebaseCallBack.onCallBack(subjectsList, subjectsMap);
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
        void onCallBack(List<String> subjectsList,
                        Map<String, ListOfSubjectsModel> subjectsMap);
    }
}
