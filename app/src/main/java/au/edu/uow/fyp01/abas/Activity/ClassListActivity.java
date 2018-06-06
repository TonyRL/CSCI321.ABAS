package au.edu.uow.fyp01.abas.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import au.edu.uow.fyp01.abas.Model.SchoolModel;
import au.edu.uow.fyp01.abas.Model.UserModel;
import au.edu.uow.fyp01.abas.R;

public class ClassListActivity extends Activity {

  private RecyclerView classListRecyclerView;
  private DatabaseReference dbref;
  private FirebaseRecyclerOptions<SchoolModel> options;
  private FirebaseRecyclerAdapter<SchoolModel, SchoolModelViewHolder> firebaseRecyclerAdapter;
  private FirebaseDatabase db;

  //Current user's metadata
  private UserModel userModel;
  private String uID;
  private FirebaseAuth auth;
  private String schID;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_classlist);

    //DONE retrieve schID from Users in database
    //<editor-fold desc="PROTOTYPE: schID directly refers to SchID1>
    //schID = "SchID1";
    //</editor-fold>

    //get current user
    uID = auth.getInstance().getCurrentUser().getUid();

    schID = "";

    final ProgressBar classListProgressBar = findViewById(R.id.classListProgressBar);
    classListProgressBar.setIndeterminate(true);

    UserQueryClass(new FirebaseCallBack() {
      @Override
      public void onCallBack(UserModel userModel) {
        schID = userModel.getSchID();

        //Instantiate the database
        db = FirebaseDatabase.getInstance();

        //RecyclerView
        classListRecyclerView = findViewById(R.id.classListRecyclerView);
        classListRecyclerView.setHasFixedSize(true);
        classListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //DONE replace '.child("SchID1")' with .child(SchID) whereas SchID is grabbed from a query class
        //<editor-fold desc="PROTOTYPE: dbref refers directly to School->SchID1">
        //Instantiate dbref
        dbref = db.getReference().child("School").child(schID);
        //</editor-fold>

        //set options for adapter
        options = new FirebaseRecyclerOptions.Builder<SchoolModel>().
            setQuery(dbref.orderByChild("classname"), SchoolModel.class).build();

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
              public SchoolModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                  int viewType) {
                View view1 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclermodellayout_singlebutton, parent, false);
                return new SchoolModelViewHolder(view1);
              }
            };

        classListRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
        classListProgressBar.setVisibility(View.GONE);

      }
    });


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

          //<editor-fold desc="Transaction to move to 'StudentListActivity'">
          Intent i = new Intent(getApplicationContext(), StudentListActivity.class);

          //Passing 'classID' & 'schID' to StudentListFragment
          Bundle args = new Bundle();
          args.putString("classID", classID);
          args.putString("schID", schID);
          i.putExtras(args);

          startActivity(i);

          //</editor-fold>
        }
      });
    }


  }


  private void UserQueryClass(final FirebaseCallBack firebaseCallBack) {
    FirebaseDatabase db2 = FirebaseDatabase.getInstance();
    DatabaseReference dbref2 = db2.getReference().child("User").child(uID);
    dbref2.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        userModel = dataSnapshot.getValue(UserModel.class);
        firebaseCallBack.onCallBack(userModel);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }

  private interface FirebaseCallBack {

    void onCallBack(UserModel userModel);
  }
}
