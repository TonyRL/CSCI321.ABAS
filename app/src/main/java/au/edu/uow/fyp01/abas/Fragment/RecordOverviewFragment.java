package au.edu.uow.fyp01.abas.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import au.edu.uow.fyp01.abas.Model.CommentModel;
import au.edu.uow.fyp01.abas.Model.RecordModel;
import au.edu.uow.fyp01.abas.QueryClassReference.CommentQueryClass;
import au.edu.uow.fyp01.abas.QueryClassReference.RecordQueryClass;
import au.edu.uow.fyp01.abas.R;

import static android.content.ContentValues.TAG;

public class RecordOverviewFragment extends Fragment {

    private String sID;
    private String subjectname;
    private String subjectID;
    private ArrayList<RecordModel> recordList;
    private ArrayList<CommentModel> commentList;


    public RecordOverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Grabbing args (sID, subject from RecordFragment)
        sID = getArguments().getString("sID");
        subjectname = getArguments().getString("subjectname");
        subjectID = getArguments().getString("subjectID");



        if (sID == null || subjectname == null || subjectID == null) {
            //TODO prevent NULL here (refer to RecordFragment)

        }


        if (container != null) {
            container.removeAllViews();
        }

        return inflater.inflate(R.layout.fragment_recordoverview, container, false);

    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {

        recordList = new ArrayList<RecordModel>();
        commentList = new ArrayList<CommentModel>();


        //-> RecordQueryClass
        RecordQueryClass(new RecordCallBack() {
            @Override
            public void onCallBack(ArrayList<RecordModel> recordList1) {
                recordList = new ArrayList<RecordModel>();
                recordList = recordList1;


                //->CommentQueryClass
                CommentQueryClass(new CommentCallBack() {
                    @Override
                    public void onCallBack(ArrayList<CommentModel> commentList1) {
                        commentList = new ArrayList<CommentModel>();
                        commentList = commentList1;

                        //Subject
                        TextView recordOverviewSubject = view.findViewById(R.id.recordOverviewSubject);
                        recordOverviewSubject.setText(subjectname);


                        //Average Grade
                        TextView recordOverviewAverageGrade = view.findViewById(R.id.recordOverviewAverageGrade);
                        recordOverviewAverageGrade.setText(findAverageGrade());


                        //Highest Grade
                        TextView recordOverviewHighestGrade = view.findViewById(R.id.recordOverviewHighestGrade);
                        recordOverviewHighestGrade.setText(findHighestGrade());

                        //Lowest Grade
                        TextView recordOverviewLowestGrade = view.findViewById(R.id.recordOverviewLowestGrade);
                        recordOverviewLowestGrade.setText(findLowestGrade());

                        //Latest Comment
                        TextView recordOverviewLatestComment = view.findViewById(R.id.recordOverviewLatestComment);
                        recordOverviewLatestComment.setText(findLatestComment());

                        //Latest Comment on click
                        //Goes to Comments overview
                        recordOverviewLatestComment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //<editor-fold desc="Transaction to move to 'CommentListFragment'">
                                Fragment newFragment = new CommentListFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                                //passing 'sID','subject', and 'subjectID' to CommentListFragment
                                Bundle args = new Bundle();
                                args.putString("sID", sID);
                                args.putString("subjectID", subjectID);
                                args.putString("subjectname",subjectname);
                                newFragment.setArguments(args);

                                transaction.replace(R.id.recordOverviewFrame, newFragment);
                                transaction.addToBackStack(null);

                                transaction.commit();
                                //</editor-fold>
                            }
                        });

                        //Set up the graph
                        GraphView recordOverviewGraph = view.findViewById(R.id.recordOverviewGraph);


                        //<editor-fold desc="Graph plotting">
                        //Assuming the list of grades was retrieved,
                        //Plot the graph
                        if (recordList.size() != 0) {

                            //Declare an array of Datapoints first
                            DataPoint[] dataPoint = new DataPoint[recordList.size()];

                            for (int i = 0; i < recordList.size(); i++) {
                                String datefromSQL = recordList.get(i).getDate();
                                try {
                                    //String date -> Date date1
                                    Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(datefromSQL);
                                    //A point is (DATE, GRADE). E.g. (25-04-2018, 70)
                                    dataPoint[i] = new DataPoint(date1, Integer.parseInt(recordList.get(i).getGrade()));
                                } catch (Exception e) {
                                    //
                                }
                            }

                            LineGraphSeries<DataPoint> lineGraphSeries = new LineGraphSeries<>(dataPoint);

                            recordOverviewGraph.addSeries(lineGraphSeries);
                            recordOverviewGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getContext(), new SimpleDateFormat("dd/MM")));
                        }
                        //</editor-fold>

                    }
                });//CommentQueryClass end
            }
        });//RecordQueryClass end
    }

    @Override
    public void onStop() {
        super.onStop();
        getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
    }

    //<editor-fold desc="findHighestGrade() -> Finds the highest grade in the list">
    public String findHighestGrade() {

        //In case there is no records found for this particular subject
        if (recordList.size() == 0) {
            return "Record not found!";
        }

        int highest = 0;

        for (int i = 0; i < recordList.size(); i++) {

            if (Integer.parseInt(recordList.get(i).getGrade()) > highest) {
                highest = Integer.parseInt(recordList.get(i).getGrade());
            }

        }

        return Integer.toString(highest);

    }
    //</editor-fold>

    //<editor-fold desc="findLowestGrade() -> Finds the lowest grade in the list">
    public String findLowestGrade() {

        //In case there is no records found for this particular subject
        if (recordList.size() == 0) {
            return "Record not found!";
        }

        int lowest = 100;

        for (int i = 0; i < recordList.size(); i++) {

            if (Integer.parseInt(recordList.get(i).getGrade()) < lowest) {
                lowest = Integer.parseInt(recordList.get(i).getGrade());
            }

        }

        return Integer.toString(lowest);
    }
    //</editor-fold>

    //<editor-fold desc="findAverageGrade() -> Finds the average grade of the subject">
    public String findAverageGrade() {

        //In case there is no records found for this particular subject
        if (recordList.size() == 0) {

            return "Record not found!";

        } else {
            double average = 0;
            double total = 0;
            double temp = 0;

            for (int i = 0; i < recordList.size(); i++) {
                temp = Double.parseDouble(recordList.get(i).getGrade());
                total += temp;
            }

            average = total / (recordList.size());

            return Double.toString(average);
        }
    }
    //</editor-fold>

    //<editor-fold desc="findLatestComment() -> Finds the latest comment only">
    public String findLatestComment() {

        if (commentList.size() == 0) {

            return "No comments/remarks yet";

        } else {

            //Comments are retrieved in (assuming) ascending order so last one should be latest!
            CommentModel commentModel = commentList.get(commentList.size() - 1);
            return commentModel.getComment() + " by " + commentModel.getCommentor();

        }

    }
    //</editor-fold>


    private void RecordQueryClass(final RecordCallBack recordCallBack) {
        FirebaseDatabase db2 = FirebaseDatabase.getInstance();

        DatabaseReference dbref2 = db2.getReference().child("Record").child(this.sID).child(this.subjectID);
        Query query;
        query = dbref2.orderByChild("timestamp");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {

                    RecordModel recordModel = dataSnapshot.getValue(RecordModel.class);
                    recordList.add(recordModel);

                }

                recordCallBack.onCallBack(recordList);
            }

            //<editor-fold desc="others">
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
        });
    }

    private interface RecordCallBack {
        void onCallBack(ArrayList<RecordModel> recordList);
    }

    private void CommentQueryClass(final CommentCallBack commentCallBack) {
        FirebaseDatabase db2 = FirebaseDatabase.getInstance();
        DatabaseReference dbref2 = db2.getReference().child("Comment").child(this.sID).child(this.subjectID);
        Query query;
        query = dbref2.orderByChild("timestamp");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {

                    //get values of retrieved nodes

                    CommentModel commentModel = dataSnapshot.getValue(CommentModel.class);
                    commentList.add(commentModel);


                }
                commentCallBack.onCallBack(commentList);
            }

            //<editor-fold desc="others">
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
        });
    }

    private interface CommentCallBack {
        void onCallBack(ArrayList<CommentModel> commentList);
    }

}
