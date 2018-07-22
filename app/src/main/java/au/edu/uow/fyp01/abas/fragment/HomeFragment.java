package au.edu.uow.fyp01.abas.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import au.edu.uow.fyp01.abas.R;
import au.edu.uow.fyp01.abas.model.UserModel;
import au.edu.uow.fyp01.abas.module.bluetooth.SearchBeaconActivity;
import au.edu.uow.fyp01.abas.module.file.FileDownloadHome;
import au.edu.uow.fyp01.abas.module.file.FileSharingHome;
import au.edu.uow.fyp01.abas.module.record.ClassListActivity;
import au.edu.uow.fyp01.abas.module.setting.SettingsBufferPage;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

  NavigationView navigationView;
  @BindView(R.id.searchBtn)
  CardView searchBtn;
  @BindView(R.id.uploadFileBtn)
  CardView uploadFileBtn;
  @BindView(R.id.downloadFileBtn)
  CardView recordBtn;
  @BindView(R.id.recordBtn)
  CardView settingBtn;
  @BindView(R.id.settingBtn)
  CardView downloadFileBtn;
  private Unbinder unbinder;

  private ProgressDialog progressDialog;

  private FirebaseDatabase db;
  private DatabaseReference dbRef;

  private boolean isUserRegistered = false;

  /**
   * Check user account information stored on database
   *
   * @param firebaseCallBack the firebase database user data model
   */
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

  /**
   * Butter knife unbinder
   */
  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  /**
   * Create a new activity to show base on nav item clicked
   *
   * @param view Triggered view
   */
  @OnClick({R.id.searchBtn, R.id.uploadFileBtn, R.id.recordBtn, R.id.downloadFileBtn,
      R.id.settingBtn})
  public void onViewClicked(View view) {
    if (isUserRegistered) {
      switch (view.getId()) {
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
      Builder builder = new Builder(getContext());
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
    checkUserAccount(new FirebaseCallBack() {
      @Override
      public void onCallBack(UserModel userModel) {
        hideProgressDialog();
        if (userModel.getStatus().equals("registered")) {
          isUserRegistered = true;
        }
      }
    });

    View view = inflater.inflate(R.layout.fragment_home, container, false);
    navigationView = getActivity().findViewById(R.id.nav_view);
    unbinder = ButterKnife.bind(this, view);

    return view;
  }

  /**
   * Show a progress dialog during internet connection
   */
  private void showProgressDialog() {
    if (progressDialog == null) {
      progressDialog = new ProgressDialog(getContext());
      progressDialog.setIndeterminate(true);
      progressDialog.setCancelable(false);
      progressDialog.setMessage("Checking your account access");
    }
    progressDialog.show();
  }

  /**
   * Hide the progress dialog
   */
  private void hideProgressDialog() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }

  private interface FirebaseCallBack {

    void onCallBack(UserModel userModel);
  }
}
