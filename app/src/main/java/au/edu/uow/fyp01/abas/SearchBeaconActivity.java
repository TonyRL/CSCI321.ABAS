package au.edu.uow.fyp01.abas;

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
    beaconManager.getBeaconParsers().add(
        //new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        new BeaconParser().setBeaconLayout("s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19"));
    beaconManager.bind(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    beaconManager.unbind(this);
  }

  @Override
  public void onBeaconServiceConnect() {
    beaconManager.addRangeNotifier(new RangeNotifier() {
      @Override
      public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        if (beacons.size() > 0) {
          Log.i(TAG, "The first beacon I see is about " + beacons.iterator().next().getDistance()
              + " meters away.");
        }
      }
    });

    try {
      beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
    } catch (RemoteException e) {
    }

  }
}
