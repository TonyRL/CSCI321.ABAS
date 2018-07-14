package au.edu.uow.fyp01.abas;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import au.edu.uow.fyp01.abas.Activity.AdminManageMenu;
import au.edu.uow.fyp01.abas.Activity.ClassListActivity;
import au.edu.uow.fyp01.abas.Activity.ClassroomHomeSetting;
import au.edu.uow.fyp01.abas.Activity.FileSharingHome;
import au.edu.uow.fyp01.abas.Activity.SearchBeaconActivity;
import au.edu.uow.fyp01.abas.Fragment.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import org.altbeacon.beacon.BeaconManager;


public class MainActivity extends AppCompatActivity {

  protected static final String TAG = "MainActivity";
  private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
  private DrawerLayout drawer;
  private Toolbar toolbar;
  private NavigationView navigationView;

  @SuppressWarnings("StatementWithEmptyBody")
  private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      // Handle navigation view item clicks here.
      int id = item.getItemId();

      switch (id) {
        case R.id.nav_home:
          swapFragment(R.id.nav_home);
          break;
        case R.id.nav_search_beacon:
          Intent searchBeaconActivityIntent = new Intent(MainActivity.this,
              SearchBeaconActivity.class);
          startActivity(searchBeaconActivityIntent);
          break;
        case R.id.nav_file:
          Intent fileActivityIntent = new Intent(MainActivity.this, FileSharingHome.class);
          startActivity(fileActivityIntent);
          break;
        case R.id.nav_record:
          Intent recordActivityIntent = new Intent(MainActivity.this, ClassListActivity.class);
          startActivity(recordActivityIntent);
          break;
        case R.id.nav_setting:
          Intent settingActivityIntent = new Intent(MainActivity.this, ClassroomHomeSetting.class);
          startActivity(settingActivityIntent);
          break;
        case R.id.nav_logout:
          FirebaseAuth.getInstance().signOut();
          Intent loginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
          startActivity(loginActivityIntent);
          finish();
          break;

        //Hide/Delete
        case R.id.nav_admin:
          Intent adminActivityIntent = new Intent(MainActivity.this, AdminManageMenu.class);
          startActivity(adminActivityIntent);
          break;

        default:
          break;
      }
      drawer.closeDrawer(GravityCompat.START);
      return true;
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

//    searchBtn = findViewById(R.id.searchBtn);
//    fileBtn = findViewById(R.id.fileBtn);
//    recordBtn = findViewById(R.id.recordBtn);
//    settingBtn = findViewById(R.id.settingBtn);
//
//    searchBtn.setOnClickListener(onClickListener);
//    fileBtn.setOnClickListener(onClickListener);
//    recordBtn.setOnClickListener(onClickListener);
//    settingBtn.setOnClickListener(onClickListener);

    //TODO Replace with search beacon
    FloatingActionButton searchBeaconFab = findViewById(R.id.searchBeaconFab);
    searchBeaconFab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        Intent searchBeaconActivityIntent = new Intent(MainActivity.this,
            SearchBeaconActivity.class);
        startActivity(searchBeaconActivityIntent);
      }
    });

    drawer = findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    navigationView = findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
    swapFragment(R.id.nav_home);

    this.getSupportFragmentManager().addOnBackStackChangedListener(
        new FragmentManager.OnBackStackChangedListener() {
          public void onBackStackChanged() {
            Fragment current = getCurrentFragment();
            if (current instanceof HomeFragment) {
              navigationView.setCheckedItem(R.id.nav_home);
            }
            /* else if (current instanceof ClassListFragment) {
              navigationView.setCheckedItem(R.id.nav_search_beacon);
            } else if (current instanceof ClassListFragment) {
              navigationView.setCheckedItem(R.id.nav_file);
            }
            else if (current instanceof ClassListFragment) {
              navigationView.setCheckedItem(R.id.nav_record);
            }*/
          }
        });

    verifyBluetooth();
    verifyLocation();
  }

  private void verifyLocation() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      // Android M Permission check
      if (this.checkSelfPermission(
          android.Manifest.permission.ACCESS_COARSE_LOCATION)
          != PackageManager.PERMISSION_GRANTED) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("This app needs location access");
        builder.setMessage(
            "Please grant location access so this app can detect beacons in the background.");
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

          @TargetApi(23)
          @Override
          public void onDismiss(DialogInterface dialog) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSION_REQUEST_COARSE_LOCATION);
          }

        });
        builder.show();
      }
    }
  }

  /**
   * Check the device: whether bluetooth is on and support BLE technology
   */
  private void verifyBluetooth() {
    try {
      if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bluetooth is not enabled");
        builder.setMessage("The application requires Location Permission to start Bluetooth.");
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
          @Override
          public void onDismiss(DialogInterface dialog) {
            BluetoothAdapter.getDefaultAdapter().enable();
          }
        });
        builder.show();
      }
    } catch (RuntimeException e) {
      final AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setTitle("Bluetooth LE not available");
      builder.setMessage(
          "Sorry, this device does not support Bluetooth LE.\nYou cannot search any beacons.");
      builder.setPositiveButton(android.R.string.ok, null);
      builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
//          finish();
//          System.exit(0);
        }
      });
      builder.show();
    }
  }

  @Override
  public void onBackPressed() {
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      if (getFragmentManager().getBackStackEntryCount() > 1) {
        getFragmentManager().popBackStack();
        //additional code
      } else {
        super.onBackPressed();
      }
    }

  }

  /**
   * Do not create option menu in app bar/action bar
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    return false;
  }

  private void swapFragment(int itemId) {
    Fragment fragment = null;
    Class fragmentClass;

    switch (itemId) {
      case R.id.nav_home:
        fragmentClass = HomeFragment.class;
        break;
      case R.id.nav_search_beacon:
        fragmentClass = HomeFragment.class;
        //fragmentClass = searchFragment.class;
        break;
      case R.id.nav_file:
        fragmentClass = HomeFragment.class;
        //fragmentClass = fileFragment.class;
        break;
      case R.id.nav_record:
        //TODO ClassList changed to Activity
        fragmentClass = HomeFragment.class;
        break;
      default:
        fragmentClass = HomeFragment.class;
        break;
    }

    try {
      fragment = (Fragment) fragmentClass.newInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Insert the fragment by replacing any existing fragment
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction tx = fragmentManager.beginTransaction();

    tx.replace(R.id.activity_main_content, fragment).addToBackStack(null).commit();
  }

  private Fragment getCurrentFragment() {
    return this.getSupportFragmentManager().findFragmentById(R.id.activity_main_content);
  }
}
