package au.edu.uow.fyp01.abas.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import au.edu.uow.fyp01.abas.R;
import au.edu.uow.fyp01.abas.SettingActivity;

public class HomeFragment extends Fragment {

  private NavigationView navigationView;

  private Button searchBtn;
  private Button fileBtn;
  private Button recordBtn;
  private Button settingBtn;

  private View.OnClickListener onClickListener = new OnClickListener() {
    @Override
    public void onClick(View v) {
      navigationView = getActivity().findViewById(R.id.nav_view);
      // Create a new fragment and specify the fragment to show based on nav item clicked
      switch (v.getId()) {
        case R.id.searchBtn:
          swapFragment(R.id.nav_search_beacon);
          navigationView.getMenu().getItem(1).setChecked(true);
          break;
        case R.id.fileBtn:
          swapFragment(R.id.nav_file);
          navigationView.getMenu().getItem(2).setChecked(true);
          break;
        case R.id.recordBtn:
          swapFragment(R.id.nav_record);
          navigationView.getMenu().getItem(3).setChecked(true);
          break;
        case R.id.settingBtn:
          Intent settingActivityIntent = new Intent(getActivity(), SettingActivity.class);
          startActivity(settingActivityIntent);
          return;
        default:
          break;
      }
    }
  };

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    if (container != null) {
      container.removeAllViews();
    }

    getActivity().setTitle("Home");

    View view = inflater.inflate(R.layout.fragment_home, container, false);

    searchBtn = view.findViewById(R.id.searchBtn);
    fileBtn = view.findViewById(R.id.fileBtn);
    recordBtn = view.findViewById(R.id.recordBtn);
    settingBtn = view.findViewById(R.id.settingBtn);

    searchBtn.setOnClickListener(onClickListener);
    fileBtn.setOnClickListener(onClickListener);
    recordBtn.setOnClickListener(onClickListener);
    settingBtn.setOnClickListener(onClickListener);

    return view;
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
        //TODO ClassList no longer a fragment
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
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction tx = fragmentManager.beginTransaction();

    tx.replace(R.id.fragment_home, fragment).addToBackStack(null).commit();
  }
}
