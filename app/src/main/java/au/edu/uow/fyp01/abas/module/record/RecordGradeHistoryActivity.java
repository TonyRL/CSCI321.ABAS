package au.edu.uow.fyp01.abas.module.record;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import au.edu.uow.fyp01.abas.R;
import au.edu.uow.fyp01.abas.model.RecordModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecordGradeHistoryActivity extends AppCompatActivity {

  private ProgressDialog progressDialog;

  private FirebaseDatabase db;
  private DatabaseReference dbref;

  private RecyclerView recordGradeHistoryRecyclerView;
  private FirebaseRecyclerOptions<RecordModel> options;
  private FirebaseRecyclerAdapter<RecordModel, RecordModelViewHolder> firebaseRecyclerAdapter;

  private String sID;
  private String subjectID;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recordgradehistory);

    Bundle bundle = getIntent().getExtras();
    //Grabbing args
    sID = bundle.getString("sID");
    subjectID = bundle.getString("subjectID");

    showProgressDialog();

    //set up the database
    db = FirebaseDatabase.getInstance();
    dbref = db.getReference().child("Record").child(sID).child(subjectID);

    recordGradeHistoryRecyclerView = findViewById(R.id.recordGradeHistoryRecylcerView);
    recordGradeHistoryRecyclerView.setHasFixedSize(true);
    recordGradeHistoryRecyclerView.setLayoutManager
        (new LinearLayoutManager(getApplicationContext()));

    //set options
    options = new FirebaseRecyclerOptions.Builder<RecordModel>()
        .setQuery(dbref.orderByChild("timestamp"), RecordModel.class).build();

    firebaseRecyclerAdapter =
        new FirebaseRecyclerAdapter<RecordModel, RecordModelViewHolder>(options) {
          @Override
          protected void onBindViewHolder(@NonNull RecordModelViewHolder holder, int position,
              @NonNull RecordModel model) {
            //bind object
            holder.setRecordID(model.getRecordID());
            holder.setDate(model.getDate());
            holder.setTimestamp(model.getTimestamp());
            holder.setGrade(model.getGrade());
            holder.setGradename(model.getGradename());
            holder.setType(model.getType());
            holder.setButton();
          }

          @NonNull
          @Override
          public RecordModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view1 = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_model_layout_grade, parent, false);
            return new RecordModelViewHolder(view1);
          }
        };

    recordGradeHistoryRecyclerView.setAdapter(firebaseRecyclerAdapter);
    hideProgressDialog();
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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_record_grade_history, menu);
    return true;
  }

  //<editor-fold desc="Transaction to move to 'RecordAddNewGradeActivity'">
  public void addRecord(MenuItem mi) {
    Intent i = new Intent(getApplicationContext(), RecordAddNewGradeActivity.class);

    //Passing args to RecordEditGradeActivity
    Bundle args = new Bundle();
    args.putString("sID", sID);
    args.putString("subjectID", subjectID);
    i.putExtras(args);

    startActivity(i);
  }

  public class RecordModelViewHolder extends RecyclerView.ViewHolder {

    View mView;
    String recordID;
    String date;
    String grade;
    Long timestamp;
    String gradename;
    String type;

    public RecordModelViewHolder(View itemView) {
      super(itemView);
      mView = itemView;
    }

    public void setRecordID(String recordID) {
      this.recordID = recordID;
    }

    public void setDate(String date) {
      this.date = date;
      TextView gradeModelDateTextView = mView.findViewById(R.id.gradeModelDateTextView);
      gradeModelDateTextView.setText(date);
    }

    public void setGrade(String grade) {
      this.grade = grade;
      TextView gradeModelGradeTextView = mView.findViewById(R.id.gradeModelGradeTextView);
      gradeModelGradeTextView.setText(grade);
    }

    public void setTimestamp(Long timestamp) {
      this.timestamp = timestamp;
    }

    public void setGradename(String gradename) {
      this.gradename = gradename;
      TextView gradeModelNameTextView = mView.findViewById(R.id.gradeModelNameTextView);
      gradeModelNameTextView.setText(gradename);
    }

    public void setType(String type) {
      this.type = type;
      TextView gradeModelTypeTextView = mView.findViewById(R.id.gradeModelRecordTypeTextView);
      gradeModelTypeTextView.setText(type);
    }

    public void setButton() {
      //points to recycler_model_layout_grade
      //The button is for each record
      final Button recordButtonView = mView.findViewById(R.id.editGradeBtn);
      recordButtonView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          //<editor-fold desc="Transaction to move to 'RecordEditGradeActivity'">
          Intent i = new Intent(getApplicationContext(), RecordEditGradeActivity.class);

          //Passing args to RecordEditGradeActivity
          Bundle args = new Bundle();
          args.putString("sID", sID);
          args.putString("subjectID", subjectID);
          args.putString("recordID", recordID);
          args.putString("grade", grade);
          args.putString("date", date);
          args.putLong("timestamp", timestamp);
          args.putString("gradename", gradename);
          args.putString("type", type);

          i.putExtras(args);

          startActivity(i);

          //</editor-fold>
        }
      });
    }
  }
  //</editor-fold>
}
