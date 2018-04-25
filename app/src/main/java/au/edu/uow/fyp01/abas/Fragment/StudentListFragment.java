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
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import au.edu.uow.fyp01.abas.Model.StudentModel;
import au.edu.uow.fyp01.abas.R;

public class StudentListFragment extends Fragment {


  private RecyclerView studentListRecyclerView;
  private DatabaseReference dbref;
  private FirebaseRecyclerOptions<StudentModel> options;
  private FirebaseRecyclerAdapter<StudentModel, StudentModelViewHolder> firebaseRecyclerAdapter;
  private FirebaseDatabase db;

  private String classID;
  private String schID;


  public StudentListFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    //Grabbing args (classID and schID from ClassListFragment)
    classID = getArguments().getString("classID");
    schID = getArguments().getString("schID");

    if (container != null) {
      container.removeAllViews();
    }

    return inflater.inflate(R.layout.fragment_studentlist, container, false);

  }

  public void onViewCreated(View view, Bundle savedInstanceState) {

    //Instantiate the database
    db = FirebaseDatabase.getInstance();

    //RecyclerView
    studentListRecyclerView = view.findViewById(R.id.studentListRecyclerView);
    studentListRecyclerView.setHasFixedSize(true);
    studentListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    dbref = db.getReference().child("Student").child(schID).child(classID);

    //set options for adapter
    //dbref is set to order the list by CLASS NUMBER.
    options = new FirebaseRecyclerOptions.Builder<StudentModel>().
        setQuery(dbref.orderByChild("classnumber"), StudentModel.class).build();

    firebaseRecyclerAdapter =
        new FirebaseRecyclerAdapter<StudentModel, StudentModelViewHolder>(options) {
          @Override
          protected void onBindViewHolder(@NonNull StudentModelViewHolder holder, int position,
              @NonNull StudentModel model) {
            //bind object
            holder.setsID(model.getSid());
            holder.setClassnumber(model.getClassnumber());
            holder.setFirstname(model.getFirstname());
            holder.setLastname(model.getLastname());
            holder.setButton();
          }

          @NonNull
          @Override
          public StudentModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
              int viewType) {
            View view1 = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclermodellayout_singlebutton, parent, false);
            return new StudentModelViewHolder(view1);
          }
        };

    studentListRecyclerView.setAdapter(firebaseRecyclerAdapter);

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

  public class StudentModelViewHolder extends RecyclerView.ViewHolder {

    View mView;
    String sID;
    String classnumber;
    String firstname;
    String lastname;

    public StudentModelViewHolder(View itemView) {
      super(itemView);
      mView = itemView;
    }

    public void setsID(String sID) {
      this.sID = sID;
    }

    public void setClassnumber(String classnumber) {
      this.classnumber = classnumber;
    }

    public void setFirstname(String firstname) {
      this.firstname = firstname;
    }

    public void setLastname(String lastname) {
      this.lastname = lastname;
    }

    public void setButton() {
      //points to recyclermodellayout_singlebutton
      //The button is for each student (e.g. 1A, 1B, 1C)
      final Button studentButtonView = mView.findViewById(R.id.modelSingleBtn);

      //set up name of the button
      //Order - Class number : Last name, First name
      //Example button - 1 : Lastname, Firstname
      String temp = classnumber + " : " + lastname + ", " + firstname;
      studentButtonView.setText(temp);

      studentButtonView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          //<editor-fold desc="Transaction to move to 'RecordFragment'">
          Fragment newFragment = new RecordFragment();
          FragmentTransaction transaction = getFragmentManager().beginTransaction();

          //Passing 'sID','classID','schID' to RecordFragment
          Bundle args = new Bundle();
          args.putString("sID", sID);
          args.putString("classID", classID);
          args.putString("schID", schID);
          newFragment.setArguments(args);

          transaction.replace(R.id.studentListFrame, newFragment);
          transaction.addToBackStack(null);

          transaction.commit();
          //</editor-fold>
        }
      });
    }


  }


}
