package au.edu.uow.fyp01.abas.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import au.edu.uow.fyp01.abas.Activity.ClassListActivity;
import au.edu.uow.fyp01.abas.Activity.ClassroomHomeSetting;
import au.edu.uow.fyp01.abas.Activity.FileDownloadHome;
import au.edu.uow.fyp01.abas.Activity.FileSharingHome;
import au.edu.uow.fyp01.abas.R;
import au.edu.uow.fyp01.abas.Activity.SearchBeaconActivity;

public class HomeFragment extends Fragment {

  private NavigationView navigationView;

  private CardView searchBtn;
  private CardView uploadFileBtn;
  private CardView recordBtn;
  private CardView settingBtn;
  private CardView downloadFileBtn;

  private View.OnClickListener onClickListener = new OnClickListener() {
    @Override
    public void onClick(View v) {
      navigationView = getActivity().findViewById(R.id.nav_view);
      // Create a new fragment and specify the fragment to show based on nav item clicked
      switch (v.getId()) {
        case R.id.searchBtn:
          Intent searchBeaconActivityIntent = new Intent(getActivity(), SearchBeaconActivity.class);
          startActivity(searchBeaconActivityIntent);
          break;
        case R.id.uploadFileBtn:
          Intent fileSendFile = new Intent(getActivity(), FileSharingHome.class);
          startActivity(fileSendFile);
          break;
        case R.id.downloadFileBtn:
          Intent fileReceiveFile = new Intent(getActivity(), FileDownloadHome.class);
          startActivity(fileReceiveFile);
          break;
        case R.id.recordBtn:
          Intent recordActivityIntent = new Intent(getActivity(), ClassListActivity.class);
          startActivity(recordActivityIntent);
          break;
        case R.id.settingBtn:
          Intent classroomSetting = new Intent(getActivity(), ClassroomHomeSetting.class);
          startActivity(classroomSetting);
          break;
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
    uploadFileBtn = view.findViewById(R.id.uploadFileBtn);
    downloadFileBtn = view.findViewById(R.id.downloadFileBtn);
    recordBtn = view.findViewById(R.id.recordBtn);
    settingBtn = view.findViewById(R.id.settingBtn);

    searchBtn.setOnClickListener(onClickListener);
    uploadFileBtn.setOnClickListener(onClickListener);
    downloadFileBtn.setOnClickListener(onClickListener);
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
