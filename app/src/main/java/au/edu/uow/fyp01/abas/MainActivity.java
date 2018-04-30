package au.edu.uow.fyp01.abas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.Snackbar;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import au.edu.uow.fyp01.abas.Fragment.ClassListFragment;
import au.edu.uow.fyp01.abas.Fragment.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

  private DrawerLayout drawer;
  private Toolbar toolbar;
  private NavigationView navigationView;

  private Button searchBtn;
  private Button fileBtn;
  private Button recordBtn;
  private Button settingBtn;


  private View.OnClickListener onClickListener = new OnClickListener() {
    @Override
    public void onClick(View v) {
      // Create a new fragment and specify the fragment to show based on nav item clicked

      switch (v.getId()) {
        case R.id.searchBtn:
          swapFragment(v.getId());
          break;
        case R.id.fileBtn:
          swapFragment(v.getId());
          break;
        case R.id.recordBtn:
          swapFragment(v.getId());
          break;
        case R.id.settingBtn:
          Intent settingActivityIntent = new Intent(MainActivity.this, SettingActivity.class);
          startActivity(settingActivityIntent);
          return;
        default:
          break;
      }
    }
  };
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
          swapFragment(R.id.searchBtn);
          break;
        case R.id.nav_file:
          swapFragment(R.id.fileBtn);
          break;
        case R.id.nav_record:
          swapFragment(R.id.recordBtn);
          break;
        case R.id.nav_setting:
          Intent settingActivityIntent = new Intent(MainActivity.this, SettingActivity.class);
          startActivity(settingActivityIntent);
          break;
        case R.id.nav_logout:
          FirebaseAuth.getInstance().signOut();
          Intent loginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
          startActivity(loginActivityIntent);
          finish();
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

    searchBtn = findViewById(R.id.searchBtn);
    fileBtn = findViewById(R.id.fileBtn);
    recordBtn = findViewById(R.id.recordBtn);
    settingBtn = findViewById(R.id.settingBtn);

    searchBtn.setOnClickListener(onClickListener);
    fileBtn.setOnClickListener(onClickListener);
    recordBtn.setOnClickListener(onClickListener);
    settingBtn.setOnClickListener(onClickListener);

    //TODO Replace with search beacon
    FloatingActionButton searchBeaconFab = findViewById(R.id.searchBeaconFab);
    searchBeaconFab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });

    drawer = findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    navigationView = findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
  }

  @Override
  public void onBackPressed() {
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
//      int count = getSupportFragmentManager().getBackStackEntryCount();
//
//      if (count == 0) {
//        super.onBackPressed();
//        //additional code
//      } else {
//        getFragmentManager().popBackStack();
//      }

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
      case R.id.searchBtn:
        fragmentClass = HomeFragment.class;
        //fragmentClass = searchFragment.class;
        break;
      case R.id.fileBtn:
        fragmentClass = HomeFragment.class;
        //fragmentClass = fileFragment.class;
        break;
      case R.id.recordBtn:
        fragmentClass = ClassListFragment.class;
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
}
