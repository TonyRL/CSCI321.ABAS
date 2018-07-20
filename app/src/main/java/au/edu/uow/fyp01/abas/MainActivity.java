package au.edu.uow.fyp01.abas;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import au.edu.uow.fyp01.abas.activity.AdminManageMenu;
import au.edu.uow.fyp01.abas.activity.ClassListActivity;
import au.edu.uow.fyp01.abas.activity.FileSharingHome;
import au.edu.uow.fyp01.abas.activity.SchoolListActivity;
import au.edu.uow.fyp01.abas.activity.SearchBeaconActivity;
import au.edu.uow.fyp01.abas.activity.SettingsBufferPage;
import au.edu.uow.fyp01.abas.fragment.HomeFragment;
import au.edu.uow.fyp01.abas.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.altbeacon.beacon.BeaconManager;


public class MainActivity extends AppCompatActivity {

  protected static final String TAG = "MainActivity";
  private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
  private DrawerLayout drawer;
  private Toolbar toolbar;
  private NavigationView navigationView;

  private FirebaseDatabase db;
  private DatabaseReference dbRef;

  private boolean isUserRegistered = false;
  private boolean isAdmin = false;

  @SuppressWarnings("StatementWithEmptyBody")
  private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      // Handle navigation view item clicks here.

      if (isUserRegistered) {
        int id = item.getItemId();
        switch (id) {
          case R.id.nav_home:
            //inflateHomeFragment(R.id.nav_home);
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
            Intent settingActivityIntent = new Intent(MainActivity.this, SettingsBufferPage.class);
            startActivity(settingActivityIntent);
            break;
          case R.id.nav_logout:
            FirebaseAuth.getInstance().signOut();
            Intent loginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginActivityIntent);
            overridePendingTransition(R.anim.anim_slide_in_to_left, R.anim.anim_slide_out_to_left);
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
      } else {
        if (item.getItemId() == R.id.nav_logout) {
          FirebaseAuth.getInstance().signOut();
          Intent loginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
          startActivity(loginActivityIntent);
          overridePendingTransition(R.anim.anim_slide_in_to_left, R.anim.anim_slide_out_to_left);
          finish();
        } else if (item.getItemId() == R.id.nav_setting) {
          Intent settingActivityIntent = new Intent(MainActivity.this, SettingsBufferPage.class);
          startActivity(settingActivityIntent);
        } else {
          showUnregisteredUserWarning();
        }
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

    db = FirebaseDatabase.getInstance();

    FloatingActionButton searchBeaconFab = findViewById(R.id.searchBeaconFab);
    searchBeaconFab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        if (isUserRegistered) {
          Intent searchBeaconActivityIntent = new Intent(MainActivity.this,
              SearchBeaconActivity.class);
          startActivity(searchBeaconActivityIntent);
        } else {
          showUnregisteredUserWarning();
        }
      }
    });

    drawer = findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    navigationView = findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
    inflateHomeFragment(R.id.nav_home);

    hideAdminBtn();

    checkUserAccount(new FirebaseCallBack() {
      @Override
      public void onCallBack(UserModel userModel) {
        if (userModel.getUsertype() != null && userModel.getUsertype().equals("Admin")) {
          isAdmin = true;
          navigationView.getMenu().findItem(R.id.nav_admin).setVisible(false);
        }

        if (userModel.getStatus().equals("registered")) {
          isUserRegistered = true;
        } else {
          AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
          builder.setMessage("Your account has not registered to any school" +
              "\nWould you like to register now?");
          builder.setCancelable(true);
          builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              Intent intent = new Intent(MainActivity.this, SchoolListActivity.class);
              startActivity(intent);
            }
          });
          builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              dialogInterface.cancel();
            }
          });
          AlertDialog dialog = builder.create();
          dialog.show();
        }
      }
    });
    verifyBluetooth();
    verifyLocation();
  }

  private void hideAdminBtn() {
    navigationView.getMenu().findItem(R.id.nav_admin).setVisible(false);
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
      AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
      builder.setMessage("Do you want to exit the application?");
      builder.setPositiveButton("Yes", new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          System.exit(0);
        }
      });
      builder.setNegativeButton("No", new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          dialog.dismiss();
        }
      });
      AlertDialog dialog = builder.create();
      dialog.show();
    }
  }

  /**
   * Do not create option menu in app bar/action bar
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    return false;
  }

  private void inflateHomeFragment(int itemId) {
    Fragment fragment = null;
    Class fragmentClass = HomeFragment.class;

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

  public void showUnregisteredUserWarning() {
    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    builder.setTitle("Error").setMessage("Unregistered user!");
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.cancel();
      }
    });
    AlertDialog dialog = builder.create();
    dialog.show();
  }

  private void checkUserAccount(final FirebaseCallBack firebaseCallBack) {
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    dbRef = db.getReference().child("User").child(uid);

    dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
          UserModel userModel = dataSnapshot.getValue(UserModel.class);
          firebaseCallBack.onCallBack(userModel);
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
      }
    });
  }

  private interface FirebaseCallBack {

    void onCallBack(UserModel userModel);
  }
}
