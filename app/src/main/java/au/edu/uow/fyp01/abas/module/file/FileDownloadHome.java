package au.edu.uow.fyp01.abas.module.file;

import android.app.DownloadManager;
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.IOException;

public class FileDownloadHome extends AppCompatActivity {

  //RecyclerView
  private RecyclerView fileRecyclerView;
  private FirebaseRecyclerOptions<FileDownloadHomeRecyclerClass> firebaseOptions;
  private FirebaseRecyclerAdapter<FileDownloadHomeRecyclerClass, FileDownloadHome.FileReceiveHomeHolder> firebaseRecyclerAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_file_receive_home);

    fileRecyclerView = findViewById(R.id.activity_file_receive_home_recycler_view);

    fileRecyclerView.setHasFixedSize(true);
    fileRecyclerView.setLayoutManager(new LinearLayoutManager(FileDownloadHome.this));
    fileRecyclerView.addItemDecoration(new FileDownloadHome.SpacesItemDecoration(2));

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
        .child("Received_Files").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    firebaseOptions = new FirebaseRecyclerOptions.Builder<FileDownloadHomeRecyclerClass>().
        setQuery(ref, FileDownloadHomeRecyclerClass.class).build();

    //to load the list
    firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FileDownloadHomeRecyclerClass, FileDownloadHome.FileReceiveHomeHolder>(
        firebaseOptions) {
      @Override
      protected void onBindViewHolder(@NonNull FileDownloadHome.FileReceiveHomeHolder holder,
          int position, @NonNull final FileDownloadHomeRecyclerClass model) {

        //Beware
        //Highly faulty area
        //Remeber to keep the string variable names accordingly
        holder.setFileName(model.getFileName());
        holder.setDate_Expire(model.getDate_Expire());
        holder.setTime_Expire(model.getTime_Expire());

        holder.mView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

            Toast.makeText(getApplicationContext(), "Toast", Toast.LENGTH_LONG).show();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            storageRef = storageRef.child("Shared_Files/" + model.getID());

            try {
              final File localFile = File
                  .createTempFile(model.getFileName(), "." + model.getFile_Type(),
                      getApplicationContext().getExternalFilesDir("Download"));
              storageRef.getFile(localFile)
                  .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                      Toast.makeText(getApplicationContext(),
                          Environment.getExternalStorageDirectory().getAbsolutePath(),
                          Toast.LENGTH_LONG).show();

//                  File fileN = new File(localFile,model.getFileName()+"."+model.getFile_Type())
//                  File file =new File(Environment.getExternalStorageDirectory().getAbsolutePath(),fileN);
                      DownloadManager downloadManager = (DownloadManager) getApplicationContext()
                          .getSystemService(DOWNLOAD_SERVICE);
                      downloadManager
                          .addCompletedDownload(localFile.getName(), localFile.getName(), true,
                              "*/*", localFile.getAbsolutePath(), localFile.length(), true);

                    }
                  });
            } catch (IOException e) {
              e.printStackTrace();
            }


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

  public static class FileReceiveHomeHolder extends RecyclerView.ViewHolder {

    View mView;

    TextView fileNameDisplay;
    TextView dateExpireDisplay;
    TextView timeExpireDisplay;
    String ID = null;

    FileReceiveHomeHolder(View itemView) {
      super(itemView);
      mView = itemView;
    }

    public void setFileName(String file_Name) {
      //text
      fileNameDisplay = mView
          .findViewById(R.id.activity_file_receive_home_user_recyclerview_file_name);
      fileNameDisplay.setText(file_Name);
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


    public String getID() {
      return ID;
    }

    public void setID(String ID) {
      this.ID = ID;
    }
  }

  public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int halfSpace;

    SpacesItemDecoration(int space) {
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
