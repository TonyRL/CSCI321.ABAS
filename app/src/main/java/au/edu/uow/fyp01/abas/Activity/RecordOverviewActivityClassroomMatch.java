package au.edu.uow.fyp01.abas.Activity;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import au.edu.uow.fyp01.abas.R;

public class RecordOverviewActivityClassroomMatch extends AppCompatActivity {

    private String schID;
    private String classID;
    private String sID;
    private String subjectname;
    private String subjectID;
    private String className;


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
        recyclerView.setLayoutManager(new LinearLayoutManager(RecordOverviewActivityClassroomMatch.this));
        recyclerView.addItemDecoration(new RecordOverviewActivityClassroomMatch.SpacesItemDecoration(8));


        DatabaseReference Classroom_User_Matching_ABAS_UIDDBREF = FirebaseDatabase.getInstance().getReference()
                .child("Classroom_User_Matching_ABAS_UID")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(schID)
                .child(className)
                .child(subjectID)
                .child("Submissions");
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<RecordOverActivityClassroomMatchRecyclerClass>().setQuery(Classroom_User_Matching_ABAS_UIDDBREF, RecordOverActivityClassroomMatchRecyclerClass.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RecordOverActivityClassroomMatchRecyclerClass, RecordOverviewActivityClassroomMatchHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull RecordOverviewActivityClassroomMatchHolder holder, int position, @NonNull RecordOverActivityClassroomMatchRecyclerClass model) {

                holder.setCourse_Name(model.getCoursework_Name());
                holder.setGmailAccount(model.getGmail_Account());
                holder.setGrade(model.getGrade());

            }

            @NonNull
            @Override
            public RecordOverviewActivityClassroomMatchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_record_overview_classroom_match_recyclerview_item, parent, false);
                return new RecordOverviewActivityClassroomMatch.RecordOverviewActivityClassroomMatchHolder(view1);
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

    public static class RecordOverviewActivityClassroomMatchHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView coursenameTextView;
        TextView gmailAccountTextView;
        TextView gradeTextView;

        public RecordOverviewActivityClassroomMatchHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setCourse_Name(String Course_Name){
            String coursename = Course_Name;
            coursenameTextView = mView.findViewById(R.id.activity_record_overview_classroom_nameofcoursework);
            coursenameTextView.setText(coursename);
        }
        public void setGmailAccount(String Gmail_Account){
            String gmailAccount = Gmail_Account;
            gmailAccountTextView = mView.findViewById(R.id.activity_record_overview_classroom_gmail);
            gmailAccountTextView.setText(gmailAccount);
        }

        public void setGrade(String Grade){
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
