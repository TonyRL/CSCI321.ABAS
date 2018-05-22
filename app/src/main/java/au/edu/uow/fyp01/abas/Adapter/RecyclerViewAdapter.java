package au.edu.uow.fyp01.abas.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import au.edu.uow.fyp01.abas.R;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

  private ArrayList<String> mData;

  public RecyclerViewAdapter(ArrayList<String> data) {
    this.mData = data;
  }

  public void updateData(ArrayList<String> data) {
    this.mData = data;
    notifyDataSetChanged();
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.activity_search_beacon_item, parent, false);
    ViewHolder viewHolder = new ViewHolder(view);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.mTv.setText(mData.get(position));
  }

  @Override
  public int getItemCount() {
    return mData == null ? 0 : mData.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    TextView mTv;

    public ViewHolder(View itemView) {
      super(itemView);
      mTv = itemView.findViewById(R.id.itemTV);
    }
  }
}