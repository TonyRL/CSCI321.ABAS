package au.edu.uow.fyp01.abas.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import au.edu.uow.fyp01.abas.R;
import au.edu.uow.fyp01.abas.adapter.DialogRecyclerViewAdapter.BeaconViewHolder;
import au.edu.uow.fyp01.abas.model.BeaconModel;
import au.edu.uow.fyp01.abas.model.StudentModel;
import au.edu.uow.fyp01.abas.model.UserModel;
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
import java.util.Collections;
import java.util.Comparator;
import org.altbeacon.beacon.Beacon;

public class DialogRecyclerViewAdapter extends RecyclerView.Adapter<BeaconViewHolder> {

  private static final String TAG = "DialogRecyclerViewAdapter";
  private ArrayList<Beacon> mData = new ArrayList<>();

//  public DialogRecyclerViewAdapter(ArrayList<Beacon> data) {
//    this.mData = data;
//  }

  /**
   * configures the layouts for the list item
   */
  @Override
  public void onBindViewHolder(DialogRecyclerViewAdapter.BeaconViewHolder holder, int position) {
    Beacon beacon = mData.get(position);

    holder.proximity_uuid.setText(beacon.getId1().toString());
    holder.major.setText(String.format("Major: %s", beacon.getId2().toString()));
    holder.minor.setText(String.format("Minor: %s", beacon.getId3().toString()));
    holder.distance.setText(
        String.valueOf((double) Math.round(beacon.getDistance() * 100.0) / 100.0) + "m away");
    holder.setStudentName();
  }

  /**
   * inflate the layout for the list item
   */
  @Override
  public DialogRecyclerViewAdapter.BeaconViewHolder onCreateViewHolder(ViewGroup parent,
      int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.activity_search_beacon_item, parent, false);
    return new DialogRecyclerViewAdapter.BeaconViewHolder(view, mData);
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
      Collections.sort(mData, new Comparator<Beacon>() {
        @Override
        public int compare(Beacon o1, Beacon o2) {
          return Double.compare(o1.getDistance(), o2.getDistance());
        }
      });
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
    @BindView(R.id.distance)
    TextView distance;

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
    void onClick(View view) {
      // Bad approach:
      // See https://stackoverflow.com/questions/38574912/how-to-access-the-data-source-of-a-recyclerview-adapters-viewholder/38577915#38577915
      int position = getAdapterPosition();
      String uuid = beacons.get(position).getId1().toString();

      //Toast.makeText(itemView.getContext(), "You clicked " + uuid, Toast.LENGTH_SHORT).show();
      Intent i = new Intent();
      i.putExtra("UUID", uuid);

      ((Activity) view.getContext()).setResult(Activity.RESULT_OK, i);
      ((Activity) view.getContext()).finish();
    }
  }
}
