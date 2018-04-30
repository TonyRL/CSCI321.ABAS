package au.edu.uow.fyp01.abas.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import au.edu.uow.fyp01.abas.Model.SchoolModel;
import au.edu.uow.fyp01.abas.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This fragment lists out the classes in a particular school (the user's school).
 */
public class ClassListFragment extends Fragment {

  private RecyclerView classListRecyclerView;
  private DatabaseReference dbref;
  private FirebaseRecyclerOptions<SchoolModel> options;
  private FirebaseRecyclerAdapter<SchoolModel, SchoolModelViewHolder> firebaseRecyclerAdapter;
  private FirebaseDatabase db;

  private String schID;


  public ClassListFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    if (container != null) {
      container.removeAllViews();
    }

    getActivity().setTitle("Record");

    return inflater.inflate(R.layout.fragment_classlist, container, false);

  }

  public void onViewCreated(View view, Bundle savedInstanceState) {

    //TODO retrieve schID from Users in database
    //<editor-fold desc="PROTOTYPE: schID directly refers to SchID1>
    schID = "SchID1";
    //</editor-fold>

    //Instantiate the database
    db = FirebaseDatabase.getInstance();

    //RecyclerView
    classListRecyclerView = view.findViewById(R.id.classListRecyclerView);
    classListRecyclerView.setHasFixedSize(true);
    classListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    //TODO replace '.child("SchID1")' with .child(SchID) whereas SchID is grabbed from a query class
    //<editor-fold desc="PROTOTYPE: dbref refers directly to School->SchID1">
    //Instantiate dbref
    dbref = db.getReference().child("School").child("SchID1");
    //</editor-fold>

    //set options for adapter
    options = new FirebaseRecyclerOptions.Builder<SchoolModel>().
        setQuery(dbref, SchoolModel.class).build();

    firebaseRecyclerAdapter =
        new FirebaseRecyclerAdapter<SchoolModel, SchoolModelViewHolder>(options) {
          @Override
          protected void onBindViewHolder(@NonNull SchoolModelViewHolder holder, int position,
              @NonNull SchoolModel model) {
            //bind object
            holder.setClassID(model.getClassID());
            holder.setClassname(model.getClassname());
          }

          @NonNull
          @Override
          public SchoolModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view1 = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclermodellayout_singlebutton, parent, false);
            return new SchoolModelViewHolder(view1);
          }
        };

    classListRecyclerView.setAdapter(firebaseRecyclerAdapter);


  }

  @Override
  public void onStart() {
    super.onStart();
    firebaseRecyclerAdapter.startListening();
  }

  @Override
  public void onStop() {
    super.onStop();
    firebaseRecyclerAdapter.stopListening();
  }

  public class SchoolModelViewHolder extends RecyclerView.ViewHolder {

    View mView;
    String classID;

    public SchoolModelViewHolder(View itemView) {
      super(itemView);
      mView = itemView;
    }

    public void setClassID(String classID) {
      this.classID = classID;
    }

    public void setClassname(String classname) {
      //points to recyclermodellayout_singlebutton
      //The button is for each class (e.g. 1A, 1B, 1C)
      final Button classNameButtonView = mView.findViewById(R.id.modelSingleBtn);
      classNameButtonView.setText(classname);

      classNameButtonView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          //<editor-fold desc="Transaction to move to 'StudentListFragment'">
          Fragment newFragment = new StudentListFragment();
          FragmentTransaction transaction = getFragmentManager().beginTransaction();

          //Passing 'classID' & 'schID' to StudentListFragment
          Bundle args = new Bundle();
          args.putString("classID", classID);
          args.putString("schID", schID);
          newFragment.setArguments(args);

          transaction.replace(R.id.classListFrame, newFragment);
          transaction.addToBackStack(null);

          transaction.commit();
          //</editor-fold>
        }
      });
    }


  }


}
