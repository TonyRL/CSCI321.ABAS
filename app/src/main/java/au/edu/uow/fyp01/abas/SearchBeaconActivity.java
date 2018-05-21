package au.edu.uow.fyp01.abas;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import au.edu.uow.fyp01.abas.Adapter.RecyclerViewAdapter;
import au.edu.uow.fyp01.abas.Utils.RecyclerViewDividerItemDecoration;
import java.util.ArrayList;
import java.util.Collection;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

public class SearchBeaconActivity extends AppCompatActivity implements BeaconConsumer {

  protected static final String TAG = "RangingActivity";

  private RecyclerView recyclerView;
  private RecyclerView.Adapter adapter;
  private RecyclerView.LayoutManager layoutManager;

  private BeaconManager beaconManager;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_beacon);
    initData();
    initView();

    beaconManager = BeaconManager.getInstanceForApplication(this);
    // Detect iBeacon only. No EddyStone, no AltBeacon
    beaconManager.getBeaconParsers().clear();
    beaconManager.getBeaconParsers()
        .add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
    beaconManager.bind(this);
  }

  private void initData() {
    layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    adapter = new RecyclerViewAdapter(getData());
  }

  private void initView() {
    recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.addItemDecoration(
        new RecyclerViewDividerItemDecoration(this, LinearLayoutManager.VERTICAL));
  }

  private ArrayList<String> getData() {
    ArrayList<String> data = new ArrayList<>();
    String temp = " item";
    for (int i = 0; i < 20; i++) {
      data.add(i + temp);
    }
    return data;
  }


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
          Beacon firstBeacon = beacons.iterator().next();
          Log.i(TAG,
              "The first beacon UUID:" + firstBeacon.getId1() + " Major:" + firstBeacon.getId2()
                  + " Minor: " + firstBeacon.getId3() + " I see is about " + firstBeacon
                  .getDistance() + " meters away.");
        }
      }
    });

    try {
      beaconManager
          .startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
    } catch (RemoteException e) {
    }

  }
}
