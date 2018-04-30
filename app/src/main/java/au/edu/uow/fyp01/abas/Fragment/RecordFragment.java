package au.edu.uow.fyp01.abas.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import au.edu.uow.fyp01.abas.Model.StudentModel;
import au.edu.uow.fyp01.abas.Model.SubjectModel;
import au.edu.uow.fyp01.abas.R;

import static android.content.ContentValues.TAG;

public class RecordFragment extends Fragment {

    private String sID;
    private String schID;
    private String classID;
    private StudentModel studentModel;
    private FragmentPagerAdapter adapterViewPager;
    private ViewPager viewPager;
    private Query query;

    private FirebaseDatabase db;
    private RecyclerView recordRecyclerView;
    private DatabaseReference dbref;
    private FirebaseRecyclerOptions<SubjectModel> options;
    private FirebaseRecyclerAdapter<SubjectModel, SubjectModelViewHolder> firebaseRecyclerAdapter;



    public RecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Grabbing args (sID, schID and classID from StudentListFragment)
        sID = getArguments().getString("sID");
        schID = getArguments().getString("schID");
        classID = getArguments().getString("classID");

        //Checking if args are NULLL
        if (sID == null || schID == null || classID == null) {
            //TODO prevent NULL here (by grabbing all information start from User db node)
        }


        if (container != null) {
            container.removeAllViews();
        }

        return inflater.inflate(R.layout.fragment_record, container, false);

    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {

        StudentQueryClass(new FirebaseCallBack() {
            @Override
            public void onCallBack(StudentModel studentModel1) {
                studentModel = studentModel1;

                //First name
                TextView recordFirstNameTextView = view.findViewById(R.id.recordFirstNameTextView);

                recordFirstNameTextView.setText(studentModel.getFirstname());

                //Last name
                TextView recordLastNameTextView = view.findViewById(R.id.recordLastNameTextView);
                recordLastNameTextView.setText(studentModel.getLastname());

                //SID
                TextView recordSIDTextView = view.findViewById(R.id.recordSIDTextView);
                recordSIDTextView.setText(studentModel.getSid());

                //instantiate db
                db = FirebaseDatabase.getInstance();

                //RecyclerView
                recordRecyclerView = view.findViewById(R.id.recordRecyclerView);
                recordRecyclerView.setHasFixedSize(true);
                recordRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                dbref = db.getReference().child("Subject").child(sID);

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
                            public SubjectModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                View view1 = LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.recyclermodellayout_singlebutton, parent, false);
                                return new SubjectModelViewHolder(view1);
                            }
                        };

                recordRecyclerView.setAdapter(firebaseRecyclerAdapter);
                firebaseRecyclerAdapter.startListening();

            }
        });

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

            subjectNameButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //<editor-fold desc="Transaction to move to 'RecordOverviewFragment'">
                    Fragment newFragment = new RecordOverviewFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    //Passing 'subjectname','sID' and 'subjectID' to RecordOverviewFragment
                    Bundle args = new Bundle();
                    args.putString("subjectname", subjectname);
                    args.putString("subjectID", subjectID);
                    args.putString("sID", sID);
                    newFragment.setArguments(args);

                    transaction.replace(R.id.recordFrame, newFragment);
                    transaction.addToBackStack(null);

                    transaction.commit();
                    //</editor-fold>
                }
            });
        }
    }

    private void StudentQueryClass(final FirebaseCallBack firebaseCallBack) {
        FirebaseDatabase db2 = FirebaseDatabase.getInstance();
        DatabaseReference dbref2 = db2.getReference().child("Student").child(this.schID).child(this.classID);


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
