package au.edu.uow.fyp01.abas.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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

            }

            @NonNull
            @Override
            public RecordOverviewActivityClassroomMatchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        };
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

        public RecordOverviewActivityClassroomMatchHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }


    }
}
