package au.edu.uow.fyp01.abas.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import au.edu.uow.fyp01.abas.R;

public class HomeFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    if (container != null) {
      container.removeAllViews();
    }

    return inflater.inflate(R.layout.fragment_home, container, false);
  }
}
