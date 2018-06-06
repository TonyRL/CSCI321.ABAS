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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import au.edu.uow.fyp01.abas.Model.RecordModel;
import au.edu.uow.fyp01.abas.R;

public class RecordGradesHistoryActivity extends Activity {

  private FirebaseDatabase db;
  private DatabaseReference dbref;

  private RecyclerView recordGradesHistoryRecyclerView;
  private FirebaseRecyclerOptions<RecordModel> options;
  private FirebaseRecyclerAdapter<RecordModel, RecordModelViewHolder> firebaseRecyclerAdapter;

  private String sID;
  private String subjectID;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recordgradeshistory);

    Bundle bundle = getIntent().getExtras();
    //Grabbing args
    sID = bundle.getString("sID");
    subjectID = bundle.getString("subjectID");

    final ProgressBar recordGradesHistoryProgressBar = findViewById(
        R.id.recordGradesHistoryProgressBar);
    recordGradesHistoryProgressBar.setIndeterminate(true);

    //set up the database
    db = FirebaseDatabase.getInstance();
    dbref = db.getReference().child("Record").child(sID).child(subjectID);

    recordGradesHistoryRecyclerView = findViewById(R.id.recordGradesHistoryRecylcerView);
    recordGradesHistoryRecyclerView.setHasFixedSize(true);
    recordGradesHistoryRecyclerView.setLayoutManager
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
            holder.setButton();
          }

          @NonNull
          @Override
          public RecordModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view1 = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclermodellayout_singlebutton, parent, false);
            return new RecordModelViewHolder(view1);
          }
        };

    recordGradesHistoryRecyclerView.setAdapter(firebaseRecyclerAdapter);
    recordGradesHistoryProgressBar.setVisibility(View.GONE);

    Button recordAddBtn = findViewById(R.id.recordAddBtn);
    recordAddBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //<editor-fold desc="Transaction to move to 'RecordAddNewGradeActivity'">
        Intent i = new Intent(getApplicationContext(), RecordAddNewGradeActivity.class);

        //Passing args to RecordEditGradeActivity
        Bundle args = new Bundle();
        args.putString("sID", sID);
        args.putString("subjectID", subjectID);

        i.putExtras(args);

        startActivity(i);

        //</editor-fold>
      }
    });


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

  public class RecordModelViewHolder extends RecyclerView.ViewHolder {

    View mView;
    String recordID;
    String date;
    String grade;
    Long timestamp;

    public RecordModelViewHolder(View itemView) {
      super(itemView);
      mView = itemView;
    }

    public void setRecordID(String recordID) {
      this.recordID = recordID;
    }

    public void setDate(String date) {
      this.date = date;
    }

    public void setGrade(String grade) {
      this.grade = grade;
    }

    public void setTimestamp(Long timestamp) {
      this.timestamp = timestamp;
    }

    public void setButton() {
      //points to recyclermodellayout_singlebutton
      //The button is for each record
      final Button recordButtonView = mView.findViewById(R.id.modelSingleBtn);

      //set up name of the button
      String temp = date + " : " + grade;
      recordButtonView.setText(temp);

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

          i.putExtras(args);

          startActivity(i);

          //</editor-fold>
        }
      });
    }
  }
}
