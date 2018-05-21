package au.edu.uow.fyp01.abas;


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class searchBeaconPage extends AppCompatActivity {

  private static final String TAG = "searchBeaconPage";


  BluetoothAdapter mBluetoothAdapter;

  // Create a BroadcastReceiver for ACTION_FOUND.
  private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver(){
    public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
        final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,mBluetoothAdapter.ERROR);

        switch (state){
          case BluetoothAdapter.STATE_OFF:
            Log.d(TAG,"onReceive : STATE OFF");
            break;
          case BluetoothAdapter.STATE_TURNING_OFF:
            Log.d(TAG,"mBroadcastReceiver1 : STATE Turning OFF");
            break;
          case BluetoothAdapter.STATE_ON:
            Log.d(TAG,"mBroadcastReceiver1 : STATE ON");
            break;
          case BluetoothAdapter.STATE_TURNING_ON:
            Log.d(TAG,"mBroadcastReceiver1 : STATE Turning ON");
            break;
            }


      }

    }

  };

  @Override
  protected void onDestroy() {
    Log.d(TAG,"onDestroy :called.");
    super.onDestroy();
    unregisterReceiver(mBroadcastReceiver1);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_searchbeaconpage);
    Button buttonONOFF = (Button) findViewById(R.id.btn_onoff);

    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    buttonONOFF.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View view) {
        Log.d(TAG,"onClick :enable.");
        enableDisableBt();
      }


    });


  }

  public void enableDisableBt() {
    if (mBluetoothAdapter == null) {
      Log.d(TAG, "enableDisableBT :Does not have Bluetooth capabilities.");
    }
    if (!mBluetoothAdapter.isEnabled()) {
      Log.d(TAG,"enableDisableBT :enable BT.");
      Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
      startActivity(enableBTIntent);

      IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
      registerReceiver(mBroadcastReceiver1, BTIntent);

    }
    if (mBluetoothAdapter.isEnabled()) {
      Log.d(TAG,"enableDisableBT :disable BT.");
      mBluetoothAdapter.disable();

      IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
      registerReceiver(mBroadcastReceiver1, BTIntent);
    }
  }









}
