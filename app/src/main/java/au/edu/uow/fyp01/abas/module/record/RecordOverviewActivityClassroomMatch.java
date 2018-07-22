package au.edu.uow.fyp01.abas.module.record;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import au.edu.uow.fyp01.abas.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RecordOverviewActivityClassroomMatch extends AppCompatActivity {

  private String schID;
  private String classID;
  private String sID;
  private String subjectname;
  private String subjectID;
  private String className;

  private String date;
  private Long timestamp;


  private RecyclerView recyclerView;
  private FirebaseRecyclerOptions firebaseRecyclerOptions;
  private FirebaseRecyclerAdapter<RecordOverActivityClassroomMatchRecyclerClass, RecordOverviewActivityClassroomMatchHolder> firebaseRecyclerAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_record_overview_classroom_match);

    Bundle bundle = getIntent().getExtras();
    //Grabbing args (sID, subject from RecordFragment)
    sID = bundle.getString("sID");
    subjectname = bundle.getString("subjectname");
    subjectID = bundle.getString("subjectID");
    schID = bundle.getString("schID");
    classID = bundle.getString("classID");
    className = bundle.getString("className");

    recyclerView = findViewById(R.id.activity_record_overview_classroom_match_recycler_view_main);
    recyclerView.setHasFixedSize(true);
    recyclerView
        .setLayoutManager(new LinearLayoutManager(RecordOverviewActivityClassroomMatch.this));
    recyclerView
        .addItemDecoration(new RecordOverviewActivityClassroomMatch.SpacesItemDecoration(8));

    Toast.makeText(getApplicationContext(), sID, Toast.LENGTH_LONG).show();

    final DatabaseReference Classroom_User_Matching_ABAS_UIDDBREF = FirebaseDatabase.getInstance()
        .getReference()
        .child("Classroom_User_Matching_ABAS_UID")
        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
        .child(schID)
        .child(className)
        .child(subjectID)
        .child("Submissions");
    firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<RecordOverActivityClassroomMatchRecyclerClass>()
        .setQuery(Classroom_User_Matching_ABAS_UIDDBREF,
            RecordOverActivityClassroomMatchRecyclerClass.class).build();
    firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RecordOverActivityClassroomMatchRecyclerClass, RecordOverviewActivityClassroomMatchHolder>(
        firebaseRecyclerOptions) {
      @Override
      protected void onBindViewHolder(@NonNull RecordOverviewActivityClassroomMatchHolder holder,
          int position, @NonNull final RecordOverActivityClassroomMatchRecyclerClass model) {

        holder.setCourse_Name(model.getCoursework_Name());
        holder.setGmailAccount(model.getGmail_Account());
        holder.setGrade(model.getGrade());

        holder.mView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Classroom_User_Matching_ABAS_UIDDBREF
                .addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapSubmissionID : dataSnapshot.getChildren()) {
                      if (snapSubmissionID.getKey().equals(model.getClassroom_Submission_ID())) {

                        String ABAS_Teacher_UID = "";
                        String Assigned_Status = "";
                        String Classroom_Course_Id = "";
                        String Classroom_Coursework_ID = "";
                        String Classroom_Student_UID = "";
                        String Classroom_Submission_ID = "";
                        String Classroom_Teacher_Google_Account = "";
                        String Coursework_Name = "";
                        String Description = "";
                        String Draft_Grade = "";
                        String Due_Date = "";
                        String Due_Time = "";
                        String Gmail_Account = "";
                        String Grade = "";
                        String Max_Points = "";
                        String Name_Of_Student = "";
                        String type = "";
                        for (DataSnapshot details : snapSubmissionID.getChildren()) {

                          if (details.getKey().equals("ABAS_Teacher_UID")) {
                            ABAS_Teacher_UID = details.getValue().toString();
                          }

                          if (details.getKey().equals("Assigned_Status")) {
                            Assigned_Status = details.getValue().toString();
                          }

                          if (details.getKey().equals("Classroom_Course_Id")) {
                            Classroom_Course_Id = details.getValue().toString();
                          }

                          if (details.getKey().equals("Classroom_Coursework_ID")) {
                            Classroom_Coursework_ID = details.getValue().toString();
                          }

                          if (details.getKey().equals("Classroom_Student_UID")) {
                            Classroom_Student_UID = details.getValue().toString();
                          }

                          if (details.getKey().equals("Classroom_Submission_ID")) {
                            Classroom_Submission_ID = details.getValue().toString();
                          }

                          if (details.getKey().equals("Classroom_Teacher_Google_Account")) {
                            Classroom_Teacher_Google_Account = details.getValue().toString();
                          }

                          if (details.getKey().equals("Coursework_Name")) {
                            Coursework_Name = details.getValue().toString();
                          }

                          if (details.getKey().equals("Description")) {
                            Description = details.getValue().toString();
                          }

                          if (details.getKey().equals("Draft_Grade")) {
                            Draft_Grade = details.getValue().toString();
                          }

                          if (details.getKey().equals("Due_Date")) {
                            Due_Date = details.getValue().toString();
                          }

                          if (details.getKey().equals("Due_Time")) {
                            Due_Time = details.getValue().toString();
                          }

                          if (details.getKey().equals("Gmail_Account")) {
                            Gmail_Account = details.getValue().toString();
                          }

                          if (details.getKey().equals("Grade")) {
                            Grade = details.getValue().toString();
                          }

                          if (details.getKey().equals("Max_Points")) {
                            Max_Points = details.getValue().toString();
                          }

                          if (details.getKey().equals("Name_Of_Student")) {
                            Name_Of_Student = details.getValue().toString();
                          }

                          if (details.getKey().equals("type")) {
                            type = details.getValue().toString();
                          }
                        }

                        Map submissionDetailsMap = new HashMap();
                        submissionDetailsMap.put("ABAS_Teacher_UID", ABAS_Teacher_UID);
                        submissionDetailsMap.put("Assigned_Status", Assigned_Status);
                        submissionDetailsMap.put("Classroom_Course_Id", Classroom_Course_Id);
                        submissionDetailsMap
                            .put("Classroom_Coursework_ID", Classroom_Coursework_ID);
                        submissionDetailsMap.put("Classroom_Student_UID", Classroom_Student_UID);
                        submissionDetailsMap
                            .put("Classroom_Submission_ID", Classroom_Submission_ID);
                        submissionDetailsMap.put("Classroom_Teacher_Google_Account",
                            Classroom_Teacher_Google_Account);
                        submissionDetailsMap.put("Coursework_Name", Coursework_Name);
                        submissionDetailsMap.put("Description", Description);
                        submissionDetailsMap.put("Draft_Grade", Draft_Grade);
                        submissionDetailsMap.put("Due_Date", Due_Date);
                        submissionDetailsMap.put("Due_Time", Due_Time);
                        submissionDetailsMap.put("Gmail_Account", Gmail_Account);
                        submissionDetailsMap.put("Grade", Grade);
                        submissionDetailsMap.put("Max_Points", Max_Points);
                        submissionDetailsMap.put("Name_Of_Student", Name_Of_Student);
                        submissionDetailsMap.put("type", type);

                        DatabaseReference newDBREF = FirebaseDatabase.getInstance()
                            .getReference().child("Classroom_Submissions_Linked")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        newDBREF.child(Classroom_Submission_ID).updateChildren(submissionDetailsMap,
                            new DatabaseReference.CompletionListener() {
                              @Override
                              public void onComplete(DatabaseError databaseError,
                                  DatabaseReference databaseReference) {

                              }
                            });

                        Classroom_User_Matching_ABAS_UIDDBREF
                            .child(model.getClassroom_Submission_ID())
                            .removeValue(new DatabaseReference.CompletionListener() {
                              @Override
                              public void onComplete(DatabaseError databaseError,
                                  DatabaseReference databaseReference) {

                              }
                            });

                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        Date todaysdate = new Date();
                        try {
                          todaysdate = dateFormat.parse(Due_Date);
                        } catch (ParseException e) {
                          e.printStackTrace();
                        }
                        setDate(dateFormat.format(todaysdate));
                        setTimestamp(todaysdate.getTime());

                        Map pushAssign = new HashMap();
                        pushAssign.put("date", Due_Date);
                        pushAssign.put("grade", Grade);
                        pushAssign.put("gradename", Coursework_Name);
                        pushAssign.put("recordID", model.getClassroom_Submission_ID());
                        pushAssign.put("timestamp", timestamp);
                        pushAssign.put("type", type);

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                            .child("Record").child(sID).child(subjectname);
                        ref.child(model.getClassroom_Submission_ID())
                            .updateChildren(pushAssign, new DatabaseReference.CompletionListener() {
                              @Override
                              public void onComplete(DatabaseError databaseError,
                                  DatabaseReference databaseReference) {
                              }
                            });

                        Intent i = new Intent(getApplicationContext(),
                            RecordOverviewActivity.class);

                        //Passing 'subjectname','sID' and 'subjectID' to RecordOverviewFragment
                        Bundle args = new Bundle();
                        args.putString("subjectname", subjectname);
                        args.putString("subjectID", subjectID);
                        args.putString("sID", sID);
                        args.putString("schID", schID);
                        args.putString("classID", classID);
                        args.putString("className", className);
                        i.putExtras(args);

                        startActivity(i);
                        finish();


                      }

                    }
                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {

                  }
                });
          }
        });

      }

      @NonNull
      @Override
      public RecordOverviewActivityClassroomMatchHolder onCreateViewHolder(
          @NonNull ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.activity_record_overview_classroom_match_recyclerview_item, parent,
                false);
        return new RecordOverviewActivityClassroomMatch.RecordOverviewActivityClassroomMatchHolder(
            view1);
      }
    };

    recyclerView.setAdapter(firebaseRecyclerAdapter);
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

  public void setDate(String date) {
    this.date = date;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  public static class RecordOverviewActivityClassroomMatchHolder extends RecyclerView.ViewHolder {

    View mView;
    TextView coursenameTextView;
    TextView gmailAccountTextView;
    TextView gradeTextView;
    String Classroom_Submission_ID;

    public RecordOverviewActivityClassroomMatchHolder(View itemView) {
      super(itemView);
      mView = itemView;
    }

    public void setCourse_Name(String Course_Name) {
      String coursename = Course_Name;
      coursenameTextView = mView
          .findViewById(R.id.activity_record_overview_classroom_nameofcoursework);
      coursenameTextView.setText(coursename);
    }

    public void setGmailAccount(String Gmail_Account) {
      String gmailAccount = Gmail_Account;
      gmailAccountTextView = mView.findViewById(R.id.activity_record_overview_classroom_gmail);
      gmailAccountTextView.setText(gmailAccount);
    }

    public void setGrade(String Grade) {
      String grade = Grade + "/100";
      gradeTextView = mView.findViewById(R.id.activity_record_overview_classroom_fullscore);
      gradeTextView.setText(grade);
    }


  }

  public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int halfSpace;

    public SpacesItemDecoration(int space) {
      this.halfSpace = space / 2;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
        RecyclerView.State state) {

      if (parent.getPaddingLeft() != halfSpace) {
        parent.setPadding(halfSpace, halfSpace, halfSpace, halfSpace);
        parent.setClipToPadding(false);
      }

      outRect.top = halfSpace;
      outRect.bottom = halfSpace;
      outRect.left = halfSpace;
      outRect.right = halfSpace;
    }
  }
}
