package au.edu.uow.fyp01.abas.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import au.edu.uow.fyp01.abas.R;

public class FileSharingHome extends AppCompatActivity {


    //textview
    private TextView fileNameDisplay;
    private TextView userFromDisplay;
    private TextView dateExpireDisplay;
    private TextView timeExpireDisplay;

    //Buttons
    private Button activity_file_sharing_add_file_button;

    //Recyclerview
    private RecyclerView fileRecyclerView;

    private DatabaseReference allDatabaseUserReference;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDataBase;
    private FirebaseRecyclerOptions<FileSharingHomeRecyclerClass> firebaseOptions;
    private FirebaseRecyclerAdapter<FileSharingHomeRecyclerClass, FileSharingHome.FileSharingHomeHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_sharing_home);
        //Bundle bundle = getIntent().getExtras();

        //text
        fileNameDisplay = findViewById(R.id.activity_file_sharing_home_user_recyclerview_smaple_file_name);
         userFromDisplay = findViewById(R.id.activity_file_sharing_home_user_recyclerview_sample_from_users);;
        dateExpireDisplay = findViewById(R.id.activity_file_sharing_home_user_recyclerview_sample_date);
         timeExpireDisplay = findViewById(R.id.activity_file_sharing_home_user_recyclerview_sample_time);

        activity_file_sharing_add_file_button = (Button) findViewById(R.id.activity_file_sharing_add_file_button);
        fileRecyclerView = (RecyclerView) findViewById(R.id.activity_file_sharing_home_recycler_view);

        fileRecyclerView.setHasFixedSize(true);
        fileRecyclerView.setLayoutManager(new LinearLayoutManager(FileSharingHome.this));

        fileRecyclerView.addItemDecoration(new SpacesItemDecoration(5));



        mDataBase = FirebaseDatabase.getInstance();
        allDatabaseUserReference = mDataBase.getReference().child("Shared_Files_Link");

        firebaseOptions = new FirebaseRecyclerOptions.Builder<FileSharingHomeRecyclerClass>().setQuery(allDatabaseUserReference,FileSharingHomeRecyclerClass.class).build();

        //to load the list
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FileSharingHomeRecyclerClass, FileSharingHomeHolder>(firebaseOptions) {
            @Override
            protected void onBindViewHolder(@NonNull FileSharingHomeHolder holder, int position, @NonNull FileSharingHomeRecyclerClass model) {

            }

            @NonNull
            @Override
            public FileSharingHomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_file_sharing_home_user_recyclerview_sample,parent,false);
                return new FileSharingHomeHolder(view1);
            }
        };



        activity_file_sharing_add_file_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(FileSharingHome.this,FileSharingAdd.class));

            }
        });


    }

    public static class FileSharingHomeHolder extends RecyclerView.ViewHolder {
        View mView;
        private String Date_Expire;
        private String Time_Expire;
        private String File_Name;
        private String File_Type;
        private String ID;
        private String Receiver;
        private String Sender;
        private String Link;



        public FileSharingHomeHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setFile_Name(String file_Name) {
            File_Name = file_Name;

        }

        public void setFile_Type(String file_Type) {
            File_Type = file_Type;
        }

        public void setDate_Expire(String date_Expire) {
            Date_Expire = date_Expire;
        }

        public void setTime_Expire(String time_Expire) {
            Time_Expire = time_Expire;
        }

        public void setLink(String link) {
            Link = link;
        }


    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int halfSpace;

        public SpacesItemDecoration(int space) {
            this.halfSpace = space / 2;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

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

