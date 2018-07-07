package au.edu.uow.fyp01.abas.Activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
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
import au.edu.uow.fyp01.abas.utils.RecyclerViewDividerItemDecoration;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;

public class FileDownloadHome extends AppCompatActivity {

  //RecyclerView
  private RecyclerView fileRecyclerView;
  private FirebaseRecyclerOptions<FileDownloadHomeRecyclerClass> firebaseOptions;
  private FirebaseRecyclerAdapter<FileDownloadHomeRecyclerClass, FileDownloadHome.FileReceiveHomeHolder> firebaseRecyclerAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_file_receive_home);

    fileRecyclerView = findViewById(R.id.activity_file_receive_home_recyclerview);

    fileRecyclerView.setHasFixedSize(true);
    fileRecyclerView.setLayoutManager(new LinearLayoutManager(FileDownloadHome.this));

//    fileRecyclerView.addItemDecoration(new FileDownloadHome.SpacesItemDecoration(5));
    fileRecyclerView.addItemDecoration(
        new RecyclerViewDividerItemDecoration(this, LinearLayoutManager.VERTICAL));

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
        .child("Received_Files").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    firebaseOptions = new FirebaseRecyclerOptions.Builder<FileDownloadHomeRecyclerClass>().
        setQuery(ref, FileDownloadHomeRecyclerClass.class).build();

    //to load the list
    firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FileDownloadHomeRecyclerClass, FileDownloadHome.FileReceiveHomeHolder>(
        firebaseOptions) {
      @Override
      protected void onBindViewHolder(@NonNull final FileDownloadHome.FileReceiveHomeHolder holder,
          int position, @NonNull FileDownloadHomeRecyclerClass model) {

        //Beware
        //Highly faulty area
        //Remeber to keep the string variable names accordingly
        holder.setFileName(model.getFileName());
        holder.setDate_Expire(model.getDate_Expire());
        holder.setTime_Expire(model.getTime_Expire());
        holder.setID(model.getID());

        holder.mView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

            //To download
            final String IDReference = holder.getID().trim();

            Toast.makeText(FileDownloadHome.this, IDReference, Toast.LENGTH_SHORT).show();

            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().
                child("Received_Files").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child(IDReference);

            dbRef.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {

                String filename = dataSnapshot.child("Filename").getValue().toString().trim();
                String fileType = dataSnapshot.child("File_Type").getValue().toString().trim();
                String link = dataSnapshot.child("Link").getValue().toString().trim();

                final StorageReference downloadRef = FirebaseStorage.getInstance().getReference()
                    .child("Shared_Files/").child(IDReference);
                String DOWNLOAD_DIR = Environment.getExternalStorageDirectory() + "/Downloads/";

                File fileDownload = null;
                fileDownload = new File(DOWNLOAD_DIR + "/" + filename + "." + fileType);
                downloadRef.getFile(fileDownload)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                      @Override
                      public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        Toast
                            .makeText(FileDownloadHome.this, "Downloading File", Toast.LENGTH_SHORT)
                            .show();
                      }
                    });
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
      public FileDownloadHome.FileReceiveHomeHolder onCreateViewHolder(@NonNull ViewGroup parent,
          int viewType) {
        View view1 = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.activity_file_receive_home_recyclerview, parent, false);
        return new FileDownloadHome.FileReceiveHomeHolder(view1);
      }
    };

    fileRecyclerView.setAdapter(firebaseRecyclerAdapter);
  }

  public static class FileReceiveHomeHolder extends RecyclerView.ViewHolder {

    View mView;

    TextView fileNameDisplay;
    TextView dateExpireDisplay;
    TextView timeExpireDisplay;
    String ID = null;

    public FileReceiveHomeHolder(View itemView) {
      super(itemView);
      mView = itemView;
    }

    public void setFileName(String file_Name) {
      //text
      fileNameDisplay = mView
          .findViewById(R.id.activity_file_receive_home_user_recyclerview_file_name);
      fileNameDisplay.setText(file_Name);
    }

    public void setFile_Type(String file_Type) {

    }

    public void setDate_Expire(String date_Expire) {
      //text
      dateExpireDisplay = mView
          .findViewById(R.id.activity_file_receive_home_user_recyclerview_date);
      dateExpireDisplay.setText(date_Expire);
    }

    public void setTime_Expire(String time_Expire) {
      //text
      timeExpireDisplay = mView
          .findViewById(R.id.activity_file_receive_home_user_recyclerview_time);
      timeExpireDisplay.setText(time_Expire);
    }

    public void setLink(String link) {
    }

    public void setSender(String sender) {
      //Sender = sender;
      //text
      //userFromDisplay.setText(sender);
    }

    public void setID(String ID) {
      this.ID = ID;
    }

    public String getID() {
      return ID;
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
