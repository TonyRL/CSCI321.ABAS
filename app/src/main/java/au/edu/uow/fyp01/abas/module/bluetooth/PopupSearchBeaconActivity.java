package au.edu.uow.fyp01.abas.module.bluetooth;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import au.edu.uow.fyp01.abas.adapter.DialogRecyclerViewAdapter;
import au.edu.uow.fyp01.abas.R;
import au.edu.uow.fyp01.abas.utils.RecyclerViewDividerItemDecoration;
import java.util.ArrayList;
import java.util.Collection;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

public class PopupSearchBeaconActivity extends AppCompatActivity implements BeaconConsumer {

  protected static final String TAG = "PopupSearchBeaconActivity";

  private ArrayList<Beacon> mFoundBeacons = new ArrayList<>();
  private RecyclerView recyclerView;
  private DialogRecyclerViewAdapter adapter;
  private RecyclerView.LayoutManager layoutManager;

  private BeaconManager beaconManager;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_search_beacon);

    initData();
    initView();

    DisplayMetrics dm = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(dm);
    int width = dm.widthPixels;
    int height = dm.heightPixels;
    getWindow().setLayout((int) (width * 0.7), (int) (height * 0.7));

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
    adapter = new DialogRecyclerViewAdapter();
  }

  private void initView() {
    recyclerView = findViewById(R.id.dialogRecyclerView);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.addItemDecoration(
        new RecyclerViewDividerItemDecoration(this, LinearLayoutManager.VERTICAL));
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
