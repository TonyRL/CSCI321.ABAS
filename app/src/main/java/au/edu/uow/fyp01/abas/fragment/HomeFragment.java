package au.edu.uow.fyp01.abas.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import au.edu.uow.fyp01.abas.Activity.ClassListActivity;
import au.edu.uow.fyp01.abas.Activity.ClassroomHomeSetting;
import au.edu.uow.fyp01.abas.Activity.FileDownloadHome;
import au.edu.uow.fyp01.abas.Activity.FileSharingHome;
import au.edu.uow.fyp01.abas.Activity.SettingsBufferPage;
import au.edu.uow.fyp01.abas.Activity.SchoolListActivity;
import au.edu.uow.fyp01.abas.Activity.SearchBeaconActivity;
import au.edu.uow.fyp01.abas.Model.UserModel;
import au.edu.uow.fyp01.abas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

  private NavigationView navigationView;

  private CardView searchBtn;
  private CardView uploadFileBtn;
  private CardView recordBtn;
  private CardView settingBtn;
  private CardView downloadFileBtn;

  private ProgressDialog progressDialog;

  private FirebaseDatabase db;
  private DatabaseReference dbRef;

  private boolean isUserRegistered = false;

  private View.OnClickListener onClickListener = new OnClickListener() {
    @Override
    public void onClick(View v) {
      navigationView = getActivity().findViewById(R.id.nav_view);
      // Create a new fragment and specify the fragment to show based on nav item clicked
      if (isUserRegistered) {
        switch (v.getId()) {
          case R.id.searchBtn:
            Intent searchBeaconActivityIntent = new Intent(getActivity(),
                SearchBeaconActivity.class);
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
            Intent classroomSetting = new Intent(getActivity(), SettingsBufferPage.class);
            startActivity(classroomSetting);
            break;
          default:
            break;
        }
      } else {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
    }
  };

  private void checkUserRegistration(final FirebaseCallBack firebaseCallBack) {
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

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    if (container != null) {
      container.removeAllViews();
    }

    getActivity().setTitle("Home");

    //Checking reg status
    db = FirebaseDatabase.getInstance();
    showProgressDialog();
    checkUserRegistration(new FirebaseCallBack() {
      @Override
      public void onCallBack(UserModel userModel) {
        hideProgressDialog();
        if (userModel.getStatus().equals("registered")) {
          isUserRegistered = true;
        } else {
          AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
          builder.setMessage("Your account has not registered to any school" +
              "\nWould you like to register now?");
          builder.setCancelable(true);
          builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              Intent intent = new Intent(getActivity(), SchoolListActivity.class);
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

  private void showProgressDialog() {
    if (progressDialog == null) {
      progressDialog = new ProgressDialog(getContext());
      progressDialog.setIndeterminate(true);
      progressDialog.setCancelable(false);
      progressDialog.setMessage("Loading...");
    }
    progressDialog.show();
  }

  private void hideProgressDialog() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }
}
