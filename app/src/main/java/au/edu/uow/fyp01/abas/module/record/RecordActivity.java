package au.edu.uow.fyp01.abas.module.record;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import au.edu.uow.fyp01.abas.R;
import au.edu.uow.fyp01.abas.model.StudentModel;
import au.edu.uow.fyp01.abas.model.SubjectModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RecordActivity extends AppCompatActivity {

  private ProgressDialog progressDialog;

  private String sID;
  private String schID;
  private String classID;
  private StudentModel studentModel;
  private Query query;

  private FirebaseDatabase db;
  private RecyclerView recordRecyclerView;
  private DatabaseReference dbref;
  private FirebaseRecyclerOptions<SubjectModel> options;
  private FirebaseRecyclerAdapter<SubjectModel, SubjectModelViewHolder> firebaseRecyclerAdapter;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_record);
    Bundle bundle = getIntent().getExtras();

    sID = bundle.getString("sID");
    schID = bundle.getString("schID");
    classID = bundle.getString("classID");

    showProgressDialog();

    StudentQueryClass(new FirebaseCallBack() {
      @Override
      public void onCallBack(StudentModel studentModel1) {
        studentModel = studentModel1;

        //First name
        TextView recordFirstNameTextView = findViewById(R.id.recordFirstNameTextView);
        recordFirstNameTextView.setText(studentModel.getFirstname());

        //Last name
        TextView recordLastNameTextView = findViewById(R.id.recordLastNameTextView);
        recordLastNameTextView.setText(studentModel.getLastname());

        //SID
        TextView recordSIDTextView = findViewById(R.id.recordSIDTextView);
        recordSIDTextView.setText(studentModel.getSid());

        //instantiate db
        db = FirebaseDatabase.getInstance();

        //RecyclerView
        recordRecyclerView = findViewById(R.id.recordRecyclerView);
        recordRecyclerView.setHasFixedSize(true);
        recordRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        dbref = db.getReference().child("Subject").child(schID).child(classID).child(sID);

        //set options for adapter
        options = new FirebaseRecyclerOptions.Builder<SubjectModel>().
            setQuery(dbref, SubjectModel.class).build();

        firebaseRecyclerAdapter =
            new FirebaseRecyclerAdapter<SubjectModel, SubjectModelViewHolder>(options) {
              @Override
              protected void onBindViewHolder(@NonNull SubjectModelViewHolder holder, int position,
                  @NonNull SubjectModel model) {
                //bind object
                holder.setSubjectID(model.getSubjectID());
                holder.setSubjectname(model.getSubjectname());
              }

              @NonNull
              @Override
              public SubjectModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                  int viewType) {
                View view1 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_model_layout_single_button, parent, false);
                return new SubjectModelViewHolder(view1);
              }
            };

        recordRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
        hideProgressDialog();

      }
    });


  }

  private void StudentQueryClass(final FirebaseCallBack firebaseCallBack) {
    FirebaseDatabase db2 = FirebaseDatabase.getInstance();
    DatabaseReference dbref2 = db2.getReference().child("Student").child(this.schID)
        .child(this.classID);

    query = dbref2.orderByChild("sid").equalTo(this.sID);
    query.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {

          for (DataSnapshot node : dataSnapshot.getChildren()) {
            StudentModel studentModel = node.getValue(StudentModel.class);
            firebaseCallBack.onCallBack(studentModel);
          }

        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }

  private void showProgressDialog() {
    if (progressDialog == null) {
      progressDialog = new ProgressDialog(this);
      progressDialog.setIndeterminate(true);
      progressDialog.setMessage("Loading...");
    }
    progressDialog.show();
  }

  private void hideProgressDialog() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }

  private interface FirebaseCallBack {

    void onCallBack(StudentModel studentModel);
  }

  public class SubjectModelViewHolder extends RecyclerView.ViewHolder {

    View mView;
    String subjectname;
    String subjectID;

    public SubjectModelViewHolder(View itemView) {
      super(itemView);
      mView = itemView;
    }

    public void setSubjectID(String subjectID) {
      this.subjectID = subjectID;
    }

    public void setSubjectname(String subjectname1) {
      Button subjectNameButtonView = mView.findViewById(R.id.modelSingleBtn);
      subjectNameButtonView.setText(subjectname1);
      this.subjectname = subjectname1;

      subjectNameButtonView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //<editor-fold desc="Transaction to move to 'RecordOverviewFragment'">
          Intent i = new Intent(getApplicationContext(), RecordOverviewActivity.class);

          //Passing 'subjectname','sID' and 'subjectID' to RecordOverviewFragment
          Bundle args = new Bundle();
          args.putString("subjectname", subjectname);
          args.putString("subjectID", subjectID);
          args.putString("sID", sID);
          args.putString("schID", schID);
          args.putString("classID", classID);
          i.putExtras(args);

          startActivity(i);
          //</editor-fold>
        }
      });
    }
  }


}

