package au.edu.uow.fyp01.abas.Activity;

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

public class FileReceiveHome extends AppCompatActivity {

    //Recyclerview
    private RecyclerView fileRecyclerView;
    private FirebaseRecyclerOptions<FileReceiveHomeRecyclerClass> firebaseOptions;
    private FirebaseRecyclerAdapter<FileReceiveHomeRecyclerClass, FileReceiveHome.FileReceiveHomeHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_receive_home);



        fileRecyclerView = (RecyclerView) findViewById(R.id.activity_file_receive_home_recyclerview);

        fileRecyclerView.setHasFixedSize(true);
        fileRecyclerView.setLayoutManager(new LinearLayoutManager(FileReceiveHome.this));

        fileRecyclerView.addItemDecoration(new FileReceiveHome.SpacesItemDecoration(5));



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Received_Files").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        firebaseOptions = new FirebaseRecyclerOptions.Builder<FileReceiveHomeRecyclerClass>().
                setQuery(ref,FileReceiveHomeRecyclerClass.class).build();

        //to load the list
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FileReceiveHomeRecyclerClass, FileReceiveHome.FileReceiveHomeHolder>(firebaseOptions) {
            @Override
            protected void onBindViewHolder(@NonNull FileReceiveHome.FileReceiveHomeHolder holder, int position, @NonNull FileReceiveHomeRecyclerClass model) {


                holder.setFileName(model.getFileName());
                holder.setDate_Expire(model.getDate_Expire());
                holder.setTime_Expire(model.getTime_Expire());

            }

            @NonNull
            @Override
            public FileReceiveHome.FileReceiveHomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_file_receive_home_sample_recyclerview,parent,false);
                return new FileReceiveHome.FileReceiveHomeHolder(view1);
            }
        };

        fileRecyclerView.setAdapter(firebaseRecyclerAdapter);



    }

    public static class FileReceiveHomeHolder extends RecyclerView.ViewHolder {
        View mView;

        TextView fileNameDisplay;
        TextView dateExpireDisplay;
        TextView timeExpireDisplay;

        public FileReceiveHomeHolder(View itemView) {
            super(itemView);
            mView = itemView;


        }

        public void setFileName(String file_Name) {
            //text
            fileNameDisplay = mView.findViewById(R.id.activity_file_receive_home_user_recyclerview_sample_file_name);
            fileNameDisplay.setText(file_Name);

        }

        public void setFile_Type(String file_Type) {


        }

        public void setDate_Expire(String date_Expire) {
            //text
            dateExpireDisplay = mView.findViewById(R.id.activity_file_receive_home_user_recyclerview_sample_date);
            dateExpireDisplay.setText(date_Expire);

        }

        public void setTime_Expire(String time_Expire) {
            //text
            timeExpireDisplay= mView.findViewById(R.id.activity_file_receive_home_user_recyclerview_sample_time);
            timeExpireDisplay.setText(time_Expire);
        }

        public void setLink(String link) {



        }

        public  void setSender(String sender){
            //Sender = sender;
            //text
            //userFromDisplay.setText(sender);

        }


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
