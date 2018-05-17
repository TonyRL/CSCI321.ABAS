package au.edu.uow.fyp01.abas.Activity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import au.edu.uow.fyp01.abas.R;

public class FileSharingAdd extends AppCompatActivity {

    private ImageButton addFileButton;
    private ImageButton dateImageButton;
    private ImageButton timeImageButton;

    private TextView fileNameDisplay;
    private TextView fileTypeDisplay;
    private TextView dateInput;
    private TextView timeInput;

    private String nameOfFile;
    private String nameFileType;

    private static int file_pick =1;
    private File filePicked;
    private RecyclerView allUsersList;
    private Uri resultsURI;

    private DatabaseReference allDatabaseUserReference;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDataBase;
    private FirebaseRecyclerOptions<FileSharingAddUserRetrieveClass> firebaseOptions;
    private FirebaseRecyclerAdapter<FileSharingAddUserRetrieveClass, FileSharingAdd.FileSharingAddViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_sharing_add);

        //Initialize the onscreen items and set them
        addFileButton = (ImageButton) findViewById(R.id.activity_file_sharing_add_addFile_button);
        dateImageButton = (ImageButton) findViewById(R.id.activity_file_sharing_image_button_date);
        timeImageButton = (ImageButton) findViewById(R.id.activity_file_sharing_time_image_button);

        fileNameDisplay = (TextView) findViewById(R.id.activity_file_sharing_add_filename);
        fileTypeDisplay = (TextView) findViewById(R.id.activity_file_sharing_file_type_view);
        dateInput = (TextView) findViewById(R.id.activity_file_sharing_date_input);
        timeInput = (TextView) findViewById(R.id.activity_file_sharing_time_input);

        allUsersList = (RecyclerView) findViewById(R.id.activity_file_sharing_add_recylerview);

        allUsersList.setHasFixedSize(true);
        allUsersList.setLayoutManager(new LinearLayoutManager(FileSharingAdd.this));

        allUsersList.addItemDecoration(new SpacesItemDecoration(3));


        filePicked = null;
        resultsURI = null;

        mDataBase = FirebaseDatabase.getInstance();
        allDatabaseUserReference = mDataBase.getReference().child("User");

        firebaseOptions = new FirebaseRecyclerOptions.Builder<FileSharingAddUserRetrieveClass>().setQuery(allDatabaseUserReference,FileSharingAddUserRetrieveClass.class).build();

        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<FileSharingAddUserRetrieveClass, FileSharingAddViewHolder>(firebaseOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull FileSharingAddViewHolder holder, final int position, @NonNull FileSharingAddUserRetrieveClass model) {
                        holder.setFullName(model.getFullName());


                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String vist_profile_id = getRef(position).getKey();
                                upload(view,vist_profile_id);
                                Intent back = new Intent(FileSharingAdd.this,FileSharingHome.class);
                                startActivity(back);


                        }});
                        }


                    @NonNull
                    @Override
                    public FileSharingAddViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view1 = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.activity_file_sharing_add_user_recyclerview_layout,parent,false);
                        return new FileSharingAddViewHolder(view1);
                    }
                };

        allUsersList.setAdapter(firebaseRecyclerAdapter);

        addFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent filePickedIntent = new Intent();
                filePickedIntent.setAction(Intent.ACTION_GET_CONTENT);
                filePickedIntent.setType("*/*");
                startActivityForResult(filePickedIntent, file_pick);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data!=null) {
            Uri selectedFile = data.getData();
            resultsURI = selectedFile;

            filePicked = new File(selectedFile.getPath());

            nameFileType = getMimeType(this,selectedFile);

            fileTypeDisplay.setText("."+nameFileType);
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

    public void upload(View v, String ID ){

        final String idToPass = ID;


        if(resultsURI!=null){

            final String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference storeFileStorageReference = FirebaseStorage.getInstance()
                    .getReference().child("Shared_Files");

            DatabaseReference storeSharedFileReferenceDB = FirebaseDatabase.getInstance().getReference()
                    .child("Shared_Files").child(currentUserID).child(idToPass).push();
            final String messsage_push_id = storeSharedFileReferenceDB.getKey();


            StorageReference filePath = storeFileStorageReference.child(messsage_push_id);

            filePath.putFile(resultsURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){

                        final String downloadURL = task.getResult().getDownloadUrl().toString();


                        DatabaseReference passDB1 = FirebaseDatabase.getInstance().getReference()
                                .child("Shared_Files").child(currentUserID).child(idToPass).child(messsage_push_id);

                        DatabaseReference passDB2 = FirebaseDatabase.getInstance().getReference()
                                .child("Shared_Files").child(idToPass).child(currentUserID).child(messsage_push_id);

                        DatabaseReference passDB1link = FirebaseDatabase.getInstance().getReference()
                                .child("Shared_Files_Link").child(currentUserID).child(idToPass);

                        DatabaseReference passDB2link = FirebaseDatabase.getInstance().getReference()
                                .child("Shared_Files_Link").child(idToPass).child(currentUserID);


                        Map map1 = new HashMap();
                        map1.put("Link:",downloadURL);
                        map1.put("Filename:",resultsURI.getLastPathSegment());
                        map1.put("Sender:",currentUserID);
                        map1.put("Receiver:",idToPass);

                        Map map2 = new HashMap();
                        map2.put("Link:",downloadURL);
                        map2.put("Filename:",resultsURI.getLastPathSegment());
                        map2.put("Sender:",currentUserID);
                        map2.put("Receiver:",idToPass);

                        Map map1link = new HashMap();
                        map1link.put("Link:",downloadURL);
                        map1link.put("Filename:",resultsURI.getLastPathSegment());
                        map1link.put("Sender:",currentUserID);
                        map1link.put("Receiver:",idToPass);
                        map1link.put("ID:",messsage_push_id);


                        Map map2link = new HashMap();
                        map2link.put("Link:",downloadURL);
                        map2link.put("Filename:",resultsURI.getLastPathSegment());
                        map2link.put("Sender:",currentUserID);
                        map2link.put("Receiver:",idToPass);
                        map2link.put("ID",idToPass);



                        passDB1.updateChildren(map1, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference)
                            {
                                if(databaseError!=null)
                                {
                                    Log.d("Chat_Log",databaseError.getMessage().toString());
                                }
                                Toast.makeText(FileSharingAdd.this,"Sent!" , Toast.LENGTH_LONG).show();
                            }
                        });

                        passDB2.updateChildren(map2, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference)
                            {
                                if(databaseError!=null)
                                {
                                    Log.d("Chat_Log",databaseError.getMessage().toString());
                                }
                            }
                        });

                        passDB1link.updateChildren(map1link, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference)
                            {
                                if(databaseError!=null)
                                {
                                    Log.d("Chat_Log",databaseError.getMessage().toString());
                                }
                                Toast.makeText(FileSharingAdd.this,"Sent!" , Toast.LENGTH_LONG).show();
                            }
                        });

                        passDB2link.updateChildren(map2link, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference)
                            {
                                if(databaseError!=null)
                                {
                                    Log.d("Chat_Log",databaseError.getMessage().toString());
                                }
                            }
                        });



                    }
                }
            });

        }

    }

    public static class FileSharingAddViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public FileSharingAddViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setFullName(String fullname) {
            CheckBox nameCheck = (CheckBox) mView.findViewById(R.id.activity_file_sharing_add_username);
            nameCheck.setText(fullname);
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

    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }


}
