package au.edu.uow.fyp01.abas.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import au.edu.uow.fyp01.abas.Model.CommentModel;
import au.edu.uow.fyp01.abas.Model.RecordModel;
import au.edu.uow.fyp01.abas.Model.SubjectSettingsModel;
import au.edu.uow.fyp01.abas.R;

public class RecordOverviewActivity extends Activity {

    private String sID;
    private String subjectname;
    private String subjectID;
    private ArrayList<RecordModel> recordList;
    private ArrayList<CommentModel> commentList;
    private String schID;

    private SubjectSettingsModel subjectSettingsModel;

    //these vars are used for finding the overall grade with weigh
    private Double overall;
    private ArrayList<RecordModel> assignmentList;
    private ArrayList<RecordModel> quizList;
    private ArrayList<RecordModel> testList;
    private ArrayList<RecordModel> examList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordoverview);

        Bundle bundle = getIntent().getExtras();
        //Grabbing args (sID, subject from RecordFragment)
        sID = bundle.getString("sID");
        subjectname = bundle.getString("subjectname");
        subjectID = bundle.getString("subjectID");
        schID = bundle.getString("schID");

        recordList = new ArrayList<RecordModel>();
        commentList = new ArrayList<CommentModel>();

        assignmentList = new ArrayList<RecordModel>();
        quizList = new ArrayList<RecordModel>();
        testList = new ArrayList<RecordModel>();
        examList = new ArrayList<RecordModel>();


        final ProgressBar recordOverviewProgressBar = findViewById(R.id.recordOverviewProgressBar);
        recordOverviewProgressBar.setIndeterminate(true);

        //-> RecordQueryClass
        RecordQueryClass(new RecordCallBack() {
            @Override
            public void onCallBack(ArrayList<RecordModel> recordList1) {
                recordList = new ArrayList<RecordModel>();
                recordList = recordList1;

                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference dbref = db.getReference()
                        .child("SubjectSettings")
                        .child(schID)
                        .child(subjectID);

                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            subjectSettingsModel = dataSnapshot.getValue(SubjectSettingsModel.class);

                            /*
                            ||
                            ||  Initialize the StatisticsEngine here
                            ||
                            */
                            StatisticsEngine statisticsEngine = new StatisticsEngine(recordList);
                            assignmentList = statisticsEngine.getAssignmentList();
                            quizList = statisticsEngine.getQuizList();
                            testList = statisticsEngine.getTestList();
                            examList = statisticsEngine.getExamList();

                            /*
                            %%
                            %% Finding Overall with weigh
                            %%
                             */

                            double assignmentratio = Double.parseDouble(subjectSettingsModel.getAssignmentratio());
                            double quizratio = Double.parseDouble(subjectSettingsModel.getQuizratio());
                            double testratio = Double.parseDouble(subjectSettingsModel.getTestratio());
                            double examratio = Double.parseDouble(subjectSettingsModel.getExamratio());

                            //In case there is no records found for this particular subject
                            if (recordList.size() == 0) {



                            } else {
                                overall = 0.0;
                                double total = 0;
                                double totalassignment = 0;
                                double totalquiz = 0;
                                double totaltest = 0;
                                double totalexam = 0;

                                if (!assignmentList.isEmpty()) { //if not empty
                                    for (int i = 0; i < assignmentList.size(); i++) {
                                        totalassignment += Double.parseDouble(assignmentList.get(i).getGrade());
                                    }
                                }

                                if (!quizList.isEmpty()) { //if not empty
                                    for (int i = 0; i < quizList.size(); i++) {
                                        totalquiz += Double.parseDouble(quizList.get(i).getGrade());
                                    }
                                }

                                if (!testList.isEmpty()) { //if not empty
                                    for (int i = 0; i < testList.size(); i++) {
                                        totaltest += Double.parseDouble(testList.get(i).getGrade());

                                    }
                                }

                                if (!examList.isEmpty()) { //if not empty
                                    for (int i = 0; i < examList.size(); i++) {
                                        totalexam += Double.parseDouble(examList.get(i).getGrade());

                                    }
                                }

                                //weigh the marks
                                overall = totalassignment * assignmentratio/100 +
                                        totalquiz  * quizratio / 100 +
                                        totaltest * testratio / 100 +
                                        totalexam * examratio / 100;




                            }

                            /*
                            ||
                            ||  Views for grades (stats) goes under here
                            ||
                            */
                            //Subject
                            TextView recordOverviewSubject = findViewById(R.id.recordOverviewSubject);
                            recordOverviewSubject.setText(subjectname);

                            //Average Grade
                            TextView recordOverviewAverageGrade = findViewById(R.id.recordOverviewAverageGrade);
                            //comment the line under once views are set
                            recordOverviewAverageGrade.setText(Double.toString(overall));
                            //e.g. (the overall average grade text view).setText(
                            //                  statisticsEngine.findAverageGrade());

                            //Highest Grade
                            TextView recordOverviewHighestGrade = findViewById(R.id.recordOverviewHighestGrade);
                            recordOverviewHighestGrade.setText(statisticsEngine.findOverallHighestGrade());

                            //Lowest Grade
                            TextView recordOverviewLowestGrade = findViewById(R.id.recordOverviewLowestGrade);
                            recordOverviewLowestGrade.setText(findLowestGrade());

                            //Latest Comment
                            Button recordOverviewComments = findViewById(R.id.recordOverviewComments);

                            /*
                            ||
                            ||  Views for grades (stats) goes above here
                            ||
                             */

                            //Latest Comment on click
                            //Goes to Comments overview
                            recordOverviewComments.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent commentListActivityIntent = new Intent(getApplicationContext(),
                                            CommentListActivity.class);

                                    Bundle args = new Bundle();
                                    args.putString("sID", sID);
                                    args.putString("subjectID", subjectID);
                                    args.putString("subjectname", subjectname);

                                    commentListActivityIntent.putExtras(args);
                                    startActivity(commentListActivityIntent);
                                }
                            });

                            //Set up the graph
                            GraphView recordOverviewGraph = findViewById(R.id.recordOverviewGraph);

                            //<editor-fold desc="Graph plotting">
                            //Assuming the list of grades was retrieved,
                            //Plot the graph
                            if (recordList.size() != 0) {

                                //Declare an array of Datapoints first
                                DataPoint[] dataPoint = new DataPoint[recordList.size()];

                                //this section turns the x axis into dates (retrieved from the recordLists)
                                for (int i = 0; i < recordList.size(); i++) {
                                    String datefromSQL = recordList.get(i).getDate();
                                    try {
                                        //String date -> Date date1
                                        Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(datefromSQL);
                                        //A point is (DATE, GRADE). E.g. (25-04-2018, 70)
                                        dataPoint[i] = new DataPoint(date1,
                                                Integer.parseInt(recordList.get(i).getGrade()));
                                    } catch (Exception e) {
                                        //
                                    }
                                }

                                LineGraphSeries<DataPoint> lineGraphSeries = new LineGraphSeries<>(dataPoint);

                                recordOverviewGraph.addSeries(lineGraphSeries);
                                recordOverviewGraph.getGridLabelRenderer().setLabelFormatter(
                                        new DateAsXAxisLabelFormatter(getApplicationContext(),
                                                new SimpleDateFormat("dd/MM")));

                                //max is 100 marks
                                recordOverviewGraph.getViewport().setMaxY(100.0);
                                recordOverviewGraph.getViewport().setYAxisBoundsManual(true);

                                //get the smallest date and convert to double
                                double d = (double) recordList.get(0).getTimestamp();
                                recordOverviewGraph.getViewport().setMinX(d);
                                recordOverviewGraph.getViewport().setMaxX(d + 604800000.0);
                                recordOverviewGraph.getViewport().setXAxisBoundsManual(true);
                                recordOverviewGraph.getViewport().setScalable(true);
                            }


                            recordOverviewProgressBar.setVisibility(View.GONE);
                            //</editor-fold>

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }); //end query for SUBJECTSETTINGS


            }
        });//RecordQueryClass end

        //<editor-fold desc="Grades History button">
        Button recordOverviewGradeHistoryBtn = findViewById(R.id.recordOverviewGradesHistoryBtn);
        recordOverviewGradeHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RecordGradesHistoryActivity.class);

                Bundle args = new Bundle();
                args.putString("sID", sID);
                args.putString("subjectID", subjectID);

                i.putExtras(args);
                startActivity(i);
            }
        });
        //</editor-fold>

    }

    /*
    ||
    || Remove the lines under this once the old views are replaced with dialogs
    ||
     */
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

  /*
  ||
  || Remove the lines above this once the old views are replaced with dialogs
  ||
  */

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

    //Query class for grabbing the Record Models
    private void RecordQueryClass(final RecordCallBack recordCallBack) {
        FirebaseDatabase db2 = FirebaseDatabase.getInstance();

        DatabaseReference dbref2 = db2.getReference().child("Record").child(this.sID)
                .child(this.subjectID);
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


}


