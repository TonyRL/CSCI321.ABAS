package au.edu.uow.fyp01.abas.module.bluetooth;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import au.edu.uow.fyp01.abas.adapter.SearchBeaconRecyclerViewAdapter;
import au.edu.uow.fyp01.abas.R;
import au.edu.uow.fyp01.abas.utils.SearchBeaconRecyclerDividerItemDecoration;
import java.util.ArrayList;
import java.util.Collection;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

public class SearchBeaconActivity extends AppCompatActivity implements BeaconConsumer {

  protected static final String TAG = "SearchBeaconActivity";

  private ArrayList<Beacon> mFoundBeacons = new ArrayList<>();
  private RecyclerView recyclerView;
  private SearchBeaconRecyclerViewAdapter adapter;
  private RecyclerView.LayoutManager layoutManager;

  private BeaconManager beaconManager;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_beacon);

    initData();
    initView();

    beaconManager = BeaconManager.getInstanceForApplication(this);
    beaconManager.setForegroundScanPeriod(3000);
    // Detect iBeacon only. No EddyStone, no AltBeacon
    beaconManager.getBeaconParsers().clear();
    beaconManager.getBeaconParsers()
        .add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
    beaconManager.bind(this);
  }

  private void initData() {
    layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    //adapter = new SearchBeaconRecyclerViewAdapter(getData());
    adapter = new SearchBeaconRecyclerViewAdapter();
  }

  private void initView() {
    recyclerView = findViewById(R.id.searchBeaconRecyclerView);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.addItemDecoration(
        new SearchBeaconRecyclerDividerItemDecoration(this, LinearLayoutManager.VERTICAL));
  }

//  private ArrayList<Beacon> getData() {
//    ArrayList<Beacon> data = new ArrayList<>();
////    String temp = "Beacon ";
////    for (int i = 0; i < 20; i++) {
////      data.add(temp + i);
////    }
//    return data;
//  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    beaconManager.unbind(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  public void onBeaconServiceConnect() {
    beaconManager.addRangeNotifier(new RangeNotifier() {
      @Override
      public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        if (beacons.size() > 0) {

          // Only the original thread that created a view hierarchy can touch its views.
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              adapter.cleanBeacon();
            }
          });
          mFoundBeacons.clear();

          for (final Beacon beacon : beacons) {
            if (!isBeaconAlreadyFound(beacon, mFoundBeacons)) {
              mFoundBeacons.add(beacon);
              runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  adapter.addBeacon(beacon);
                }
              });
              //Log.d(TAG, "Trying to add " + beacon.getId1());
            }
          }
        }
      }
    });

    try {
      beaconManager
          .startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  private boolean isBeaconAlreadyFound(Beacon beacon, ArrayList<Beacon> foundBeacon) {
    boolean isFound = false;
    for (int i = 0; i < foundBeacon.size(); i++) {
      if (beacon.getId1().equals(foundBeacon.get(i).getId1())) {
        isFound = true;
        break;
      }
    }
    return isFound;
  }
}
