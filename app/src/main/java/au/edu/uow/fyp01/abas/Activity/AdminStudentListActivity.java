package au.edu.uow.fyp01.abas.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import au.edu.uow.fyp01.abas.Model.StudentModel;
import au.edu.uow.fyp01.abas.R;

public class AdminStudentListActivity extends Activity {

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

        //Instantiate the database
        db = FirebaseDatabase.getInstance();

        //Progress bar
        ProgressBar adminStudentListProgressBar = findViewById(R.id.adminStudentListProgressBar);
        adminStudentListProgressBar.setIndeterminate(true);

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
        adminStudentListProgressBar.setVisibility(View.GONE);

        //<editor-fold desc="Add Button for new students in a class">
        Button adminStudentListAddBtn = findViewById(R.id.adminStudentListAddBtn);
        adminStudentListAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //<editor-fold desc="Transaction to move to 'AdminAddStudentActivity'">


                    Intent i = new Intent(getApplicationContext(),AdminStudentListActivity.class);

                    //Passing 'sID','classID','schID' to AdminAddStudentActivity
                    Bundle args = new Bundle();
                    args.putString("classID", classID);
                    args.putString("schID", schID);
                    args.putString("classname",classname);

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

                    //<editor-fold desc="Transaction to move to 'AdminStudentDetails'">

                    /*
                    Intent i = new Intent(getApplicationContext(),RecordActivity.class);

                    //Passing 'sID','classID','schID' to RecordFragment
                    Bundle args = new Bundle();
                    args.putString("sID", sID);
                    args.putString("classID", classID);
                    args.putString("schID", schID);

                    i.putExtras(args);

                    startActivity(i);
                    */

                    //</editor-fold>
                }
            });
        }


    }
}
