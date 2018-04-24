package au.edu.uow.fyp01.abas.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import au.edu.uow.fyp01.abas.Model.RecordModel;
import au.edu.uow.fyp01.abas.QueryClass.RecordQueryClass;
import au.edu.uow.fyp01.abas.R;

public class RecordOverviewFragment extends Fragment {

    private String sID;
    private String subject;
    private RecordQueryClass recordQueryClass;
    private ArrayList<RecordModel> recordList;


    public RecordOverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Grabbing args (sID, subject from RecordFragment)
        sID = getArguments().getString("sID");
        subject = getArguments().getString("subject");

        if (sID == null || subject == null) {
            //TODO prevent NULL here (refer to RecordFragment)
        }

        //setup the Queryclasses used to fetch data from a database
        recordQueryClass = new RecordQueryClass(sID, subject);
        recordList = null;
        recordList = recordQueryClass.getRecordList();

        if (container != null) {
            container.removeAllViews();
        }

        return inflater.inflate(R.layout.fragment_recordoverview, container, false);

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        //Average Grade
        TextView recordOverviewAverageGrade = view.findViewById(R.id.recordOverviewAverageGrade);
        recordOverviewAverageGrade.setText(findAverageGrade());

        //Highest Grade
        TextView recordOverviewHighestGrade = view.findViewById(R.id.recordOverviewHighestGrade);
        recordOverviewHighestGrade.setText(findHighestGrade());

        //Lowest Grade
        TextView recordOverviewLowestGrade = view.findViewById(R.id.recordOverviewLowestGrade);
        recordOverviewLowestGrade.setText(findLowestGrade());

        //Set up the graph
        GraphView recordOverviewGraph = view.findViewById(R.id.recordOverviewGraph);

        //<editor-fold desc="Graph plotting">
        //Assuming the list of grades was retrieved,
        //Plot the graph
        if (recordList.size() != 0) {

            //Declare an array of Datapoints first
            DataPoint[] dataPoint = new DataPoint[recordList.size()];

            for (int i = 0; i < recordList.size() ; i++){
                String datefromSQL = recordList.get(i).getDate();
                try {
                    //String date -> Date date1
                    Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(datefromSQL);
                    //A point is (DATE, GRADE). E.g. (25-04-2018, 70)
                    dataPoint[i] = new DataPoint(date1,Integer.parseInt(recordList.get(i).getGrade()));
                } catch (Exception e) {
                    //
                }
            }

        LineGraphSeries<DataPoint> lineGraphSeries = new LineGraphSeries<>(dataPoint);

        recordOverviewGraph.addSeries(lineGraphSeries);
        }
        //</editor-fold>

    }

    //<editor-fold desc="findHighestGrade() -> Finds the highest grade in the list">
    public String findHighestGrade() {

        //In case there is no records found for this particular subject
        if (recordList.size() == 0) {
            return "Record not found!";
        }

        int highest = 0;

        for (int i = 0; i < recordList.size() ; i++) {

            if (Integer.parseInt(recordList.get(i).getGrade()) > highest) {
                highest = Integer.parseInt(recordList.get(i).getGrade());
            }

        }

        return Integer.toString(highest);

    }
    //</editor-fold>

    //<editor-fold desc="findLowestGrade() -> Finds the lowest grade in the list">
    public String findLowestGrade(){

        //In case there is no records found for this particular subject
        if (recordList.size() == 0) {
            return "Record not found!";
        }

        int lowest = 100;

        for (int i = 0; i < recordList.size() ; i++) {

            if (Integer.parseInt(recordList.get(i).getGrade()) < lowest) {
                lowest = Integer.parseInt(recordList.get(i).getGrade());
            }

        }

        return Integer.toString(lowest);
    }
    //</editor-fold>

    //<editor-fold desc="findAverageGrade() -> Finds the average grade of the subject">
    public String findAverageGrade(){

        //In case there is no records found for this particular subject
        if (recordList.size() == 0) {
            return "Record not found!";
        }

        int average = 0;
        int total = 0;
        int temp = 0;

        for (int i = 0; i < recordList.size();i++) {
            temp = Integer.parseInt(recordList.get(i).getGrade());
            temp += total;
        }

        average = total / (recordList.size());

        return Integer.toString(average);
    }
    //</editor-fold>
}
