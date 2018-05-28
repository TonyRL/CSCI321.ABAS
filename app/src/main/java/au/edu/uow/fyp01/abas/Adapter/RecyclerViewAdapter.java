package au.edu.uow.fyp01.abas.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import au.edu.uow.fyp01.abas.R;
import au.edu.uow.fyp01.abas.adapter.RecyclerViewAdapter.BeaconViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import org.altbeacon.beacon.Beacon;

public class RecyclerViewAdapter extends RecyclerView.Adapter<BeaconViewHolder> {

  private static final String TAG = "RecyclerViewAdapter";
  private ArrayList<Beacon> mData = new ArrayList<>();

//  public RecyclerViewAdapter(ArrayList<Beacon> data) {
//    this.mData = data;
//  }

  /**
   * configures the layouts for the list item
   */
  @Override
  public void onBindViewHolder(BeaconViewHolder holder, int position) {
    Beacon beacon = mData.get(position);

    holder.proximity_uuid.setText(beacon.getId1().toString());
    holder.major.setText(beacon.getId2().toString());
    holder.minor.setText(beacon.getId3().toString());
  }

  /**
   * inflate the layout for the list item
   */
  @Override
  public BeaconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.activity_search_beacon_item, parent, false);
    return new BeaconViewHolder(view);
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
      Log.d(TAG, "Beacon added: " + beacon.getId1());
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
    @BindView(R.id.itemTV)
    TextView mTv;

    BeaconViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}