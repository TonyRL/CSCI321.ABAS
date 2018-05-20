package au.edu.uow.fyp01.abas;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.util.Collection;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

public class SearchBeaconActivity extends AppCompatActivity implements BeaconConsumer {

  protected static final String TAG = "RangingActivity";

  private BeaconManager beaconManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_beacon);

    beaconManager = BeaconManager.getInstanceForApplication(this);
    // Detect iBeacon only. No EddyStone, no AltBeacon
    beaconManager.getBeaconParsers().clear();
    beaconManager.getBeaconParsers()
        .add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
    beaconManager.bind(this);
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
      beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
    } catch (RemoteException e) {
    }

  }
}
