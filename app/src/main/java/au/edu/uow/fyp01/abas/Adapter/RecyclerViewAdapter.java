package au.edu.uow.fyp01.abas.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import au.edu.uow.fyp01.abas.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import org.altbeacon.beacon.Beacon;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

  private ArrayList<Beacon> mData;

  public RecyclerViewAdapter(ArrayList<Beacon> data) {
    this.mData = data;
  }

  public void updateData(ArrayList<Beacon> data) {
    this.mData = data;
    notifyDataSetChanged();
  }

  /**
   * inflate the layout for the list item
   */
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.activity_search_beacon_item, parent, false);
    ViewHolder viewHolder = new ViewHolder(view);
    return viewHolder;
  }

  /**
   * configures the layouts for the list item
   */
  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    //holder.mTv.setText(mData.get(position));
    Beacon beacon = mData.iterator().next();
    holder.proximity_uuid.setText(beacon.getId1().toString());
    holder.major.setText(beacon.getId2().toString());
    holder.minor.setText(beacon.getId3().toString());
  }

  /**
   * returns the size of the list
   */
  @Override
  public int getItemCount() {
    return mData == null ? 0 : mData.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.proximity_uuid)
    TextView proximity_uuid;
    @BindView(R.id.major)
    TextView major;
    @BindView(R.id.minor)
    TextView minor;
    @BindView(R.id.itemTV)
    TextView mTv;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      //mTv = itemView.findViewById(R.id.itemTV);
    }
  }
}