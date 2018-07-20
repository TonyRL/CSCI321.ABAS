package au.edu.uow.fyp01.abas.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import au.edu.uow.fyp01.abas.R;
import au.edu.uow.fyp01.abas.adapter.SearchBeaconRecyclerViewAdapter.BeaconViewHolder;
import au.edu.uow.fyp01.abas.model.BeaconModel;
import au.edu.uow.fyp01.abas.model.StudentModel;
import au.edu.uow.fyp01.abas.model.UserModel;
import au.edu.uow.fyp01.abas.module.record.RecordActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import org.altbeacon.beacon.Beacon;

public class SearchBeaconRecyclerViewAdapter extends RecyclerView.Adapter<BeaconViewHolder> {

  private static final String TAG = "SearchBeaconAdapter";
  private ArrayList<Beacon> mData = new ArrayList<>();

//  public SearchBeaconRecyclerViewAdapter(ArrayList<Beacon> data) {
//    this.mData = data;
//  }

  /**
   * configures the layouts for the list item
   */
  @Override
  public void onBindViewHolder(BeaconViewHolder holder, int position) {
    Beacon beacon = mData.get(position);

    holder.proximity_uuid.setText(beacon.getId1().toString());
    holder.major.setText(String.format("Major: %s", beacon.getId2().toString()));
    holder.minor.setText(String.format("Minor: %s", beacon.getId3().toString()));
    holder.setStudentName();
  }

  /**
   * inflate the layout for the list item
   */
  @Override
  public BeaconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.activity_search_beacon_item, parent, false);
    return new BeaconViewHolder(view, mData);
  }

  /**
   * returns the size of the list
   */
  @Override
  public int getItemCount() {
    return mData == null ? 0 : mData.size();
  }

  public void addBeacon(Beacon beacon) {
    if (mData != null) {
      mData.add(beacon);
      notifyDataSetChanged();
      //Log.d(TAG, "Beacon added: " + beacon.getId1());
    }
  }

  public void updateBeacon(ArrayList<Beacon> data) {
    if (mData != null) {
      this.mData = data;
      notifyDataSetChanged();
    }
  }

  public void cleanBeacon() {
    if (mData != null) {
      mData.clear();
      notifyDataSetChanged();
    }
  }

  public static class BeaconViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.proximity_uuid)
    TextView proximity_uuid;
    @BindView(R.id.major)
    TextView major;
    @BindView(R.id.minor)
    TextView minor;
    @BindView(R.id.beaconUsername)
    TextView studentName;

    ArrayList<Beacon> beacons;
    String uuid;

    BeaconViewHolder(View itemView, ArrayList<Beacon> data) {
      super(itemView);
      this.beacons = data;
      ButterKnife.bind(this, itemView);
    }

    void setStudentName() {
      FirebaseAuth auth = null;
      String uID = auth.getInstance().getCurrentUser().getUid();
      int position = getAdapterPosition();
      uuid = beacons.get(position).getId1().toString();

      //set up db
      final FirebaseDatabase db = FirebaseDatabase.getInstance();

      //this one points to User nodes
      DatabaseReference dbref1 = db.getReference().child("User").child(uID);
      dbref1.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

          UserModel userModel = dataSnapshot.getValue(UserModel.class);
          String schID = userModel.getSchID();
          // Bad approach:
          // See https://stackoverflow.com/questions/38574912/how-to-access-the-data-source-of-a-recyclerview-adapters-viewholder/38577915#38577915

          DatabaseReference dbref = db.getReference().child("Beacon").child(schID).child(uuid);
          dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              if (dataSnapshot.exists()) {

                BeaconModel beaconModel = dataSnapshot.getValue(BeaconModel.class);

                DatabaseReference dbref2 = db.getReference().child("Student")
                    .child(beaconModel.getSchID())
                    .child(beaconModel.getClassID())
                    .child(beaconModel.getSid());

                dbref2.addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                      StudentModel studentModel = dataSnapshot.getValue(StudentModel.class);
                      //this shows the student ID and name owner of the beacon
                      String beaconInfo = studentModel.getSid() + ": " + studentModel.getFirstname()
                          + " " + studentModel.getLastname();
                      studentName.setText(beaconInfo);
                    } else {
                      studentName.setText("Beacon not registered");
                    }
                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {

                  }
                });//end inner inner query
              }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
          }); //end inner query
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
      });//end query
    }

    @OnClick
    void onClick(final View view) {
      //COMPLETED: grab user's SchID
      //get user ID
      FirebaseAuth auth = null;
      String uID = auth.getInstance().getCurrentUser().getUid();

      //set up db
      final FirebaseDatabase db = FirebaseDatabase.getInstance();

      //this one points to User nodes
      DatabaseReference dbref1 = db.getReference().child("User").child(uID);
      dbref1.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

          UserModel userModel = dataSnapshot.getValue(UserModel.class);
          String schID = userModel.getSchID();
          // Bad approach:
          // See https://stackoverflow.com/questions/38574912/how-to-access-the-data-source-of-a-recyclerview-adapters-viewholder/38577915#38577915
          int position = getAdapterPosition();
          String uuid = beacons.get(position).getId1().toString();

          DatabaseReference dbref = db.getReference().child("Beacon").child(schID).child(uuid);
          dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              if (dataSnapshot.exists()) {

                BeaconModel beaconModel = dataSnapshot.getValue(BeaconModel.class);

                //<editor-fold desc="Transaction to move to 'RecordFragment'">
                Intent i = new Intent(itemView.getContext(), RecordActivity.class);
                Log.d(TAG, "attempting to move");

                //Passing 'subjectname','sID' and 'subjectID' to RecordOverviewFragment
                Bundle args = new Bundle();
                args.putString("classID", beaconModel.getClassID());
                args.putString("schID", beaconModel.getSchID());
                args.putString("sID", beaconModel.getSid());
                i.putExtras(args);

                view.getContext().startActivity(i);
                //</editor-fold>

              } else {
                Toast.makeText(view.getContext(), "Beacon is not student ID",
                    Toast.LENGTH_SHORT)
                    .show();
              }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
          }); //end inner query
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
      });//end query

      //Toast.makeText(itemView.getContext(), "You clicked " + uuid, Toast.LENGTH_SHORT).show();
    }
  }
}
