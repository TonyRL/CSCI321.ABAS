package au.edu.uow.fyp01.abas.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import au.edu.uow.fyp01.abas.Model.RecordModel;
import au.edu.uow.fyp01.abas.Model.StudentModel;
import au.edu.uow.fyp01.abas.QueryClass.RecordQueryClass;
import au.edu.uow.fyp01.abas.QueryClass.StudentQueryClass;
import au.edu.uow.fyp01.abas.R;

public class RecordFragment extends Fragment {

    private String sID;
    private String schID;
    private String classID;
    private StudentQueryClass studentQueryClass;
    private StudentModel studentModel;
    private FragmentPagerAdapter adapterViewPager;
    private ViewPager viewPager;


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

        //setup the QueryClasses used to fetch data from a database
        studentQueryClass = new StudentQueryClass(schID,sID,classID);
        studentModel = new StudentModel(null, null,null,null,null);
        studentModel = studentQueryClass.getStudentModel();


        if (container != null) {
            container.removeAllViews();
        }

        return inflater.inflate(R.layout.fragment_record, container, false);

    }

    public void onViewCreated(View view, Bundle savedInstanceState){

        //First name
        TextView recordFirstNameTextView = view.findViewById(R.id.recordFirstNameTextView);
        recordFirstNameTextView.setText(studentModel.getFirstname());

        //Last name
        TextView recordLastNameTextView = view.findViewById(R.id.recordLastNameTextView);
        recordLastNameTextView.setText(studentModel.getLastname());

        //SID
        TextView recordSIDTextView = view.findViewById(R.id.recordSIDTextView);
        recordSIDTextView.setText(studentModel.getSid());

        //viewpager (the sliding thing)
        viewPager = view.findViewById(R.id.recordPager);
        adapterViewPager = new RecordPagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapterViewPager);

    }


    //<editor-fold desc="RecordPagerAdapter - The PageViewer adapter>
    public class RecordPagerAdapter extends FragmentPagerAdapter {

        //the number of pages
        //TODO dynamically change the number of pages (just in case theres different subjects)
        //idea = make a list of nodes with a list of subjects in a school
        // SCHID -> SubjectID1 + subjectID2 + subjectID3 etc...

        private int NUM_ITEMS = 3;

        public RecordPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // ENGLISH

                    Fragment newfragment0 = new RecordOverviewFragment();
                    Bundle args0 = new Bundle();
                    args0.putString("sID", sID);
                    args0.putString("subject", "English");
                    newfragment0.setArguments(args0);
                    return newfragment0;

                case 1: // MATH

                    Fragment newfragment1 = new RecordOverviewFragment();
                    Bundle args1 = new Bundle();
                    args1.putString("sID", sID);
                    args1.putString("subject", "Math");
                    newfragment1.setArguments(args1);
                    return newfragment1;

                case 2: // CHINESE

                    Fragment newfragment2 = new RecordOverviewFragment();
                    Bundle args2 = new Bundle();
                    args2.putString("sID", sID);
                    args2.putString("subject", "Chinese");
                    newfragment2.setArguments(args2);
                    return newfragment2;

                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            //return "Page " + position;
            switch (position) {

                case 0: //ENGLISH
                    return "English";

                case 1: //MATH
                    return "Math";

                case 2: //CHINESE
                    return "Chinese";

                default:
                    return null;

            }
        }

    }
    //</editor-fold>

}
