package au.edu.uow.fyp01.abas.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.concurrent.TimeUnit;

import au.edu.uow.fyp01.abas.R;

public class DUMMYSEARCHBEACON extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummysearchbeacon);

        Button fakeBeacon = findViewById(R.id.fakeBeacon);
        fakeBeacon.setVisibility(View.INVISIBLE);

        ProgressBar searchBeaconProgressBar = findViewById(R.id.searchBeaconProgressBar);
        searchBeaconProgressBar.setIndeterminate(true);


        fakeBeacon.setVisibility(View.VISIBLE);
        searchBeaconProgressBar.setVisibility(View.GONE);

        fakeBeacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //<editor-fold desc="Transaction to move to 'RecordOverviewFragment'">
                Intent i = new Intent(getApplicationContext(),RecordActivity.class);

                //Passing 'subjectname','sID' and 'subjectID' to RecordOverviewFragment
                Bundle args = new Bundle();
                args.putString("classID", "ClassID1");
                args.putString("schID", "SchID1");
                args.putString("sID", "StudentAid");
                i.putExtras(args);

                startActivity(i);
                //</editor-fold>

            }
        });



    }
}
