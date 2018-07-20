package au.edu.uow.fyp01.abas.module.record;

import static android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import au.edu.uow.fyp01.abas.model.CommentModel;
import au.edu.uow.fyp01.abas.model.UserModel;
import au.edu.uow.fyp01.abas.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommentListActivity extends AppCompatActivity {

  private ProgressDialog progressDialog;

  private RecyclerView commentListRecyclerView;
  private DatabaseReference dbref;
  private FirebaseRecyclerOptions<CommentModel> options;
  private FirebaseRecyclerAdapter<CommentModel, CommentModelViewHolder> firebaseRecyclerAdapter;
  private FirebaseDatabase db;
  //Model to hold user metadata
  private UserModel userModel;

  private String sID;
  private String subjectname;
  private String subjectID;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_comment_list);
    Bundle bundle = getIntent().getExtras();

    //Grabbing args (sID and subject from RecordFragment)
    sID = bundle.getString("sID");
    subjectname = bundle.getString("subjectname");
    subjectID = bundle.getString("subjectID");

    showProgressDialog();

    //Setup userModel
    UserQueryClass(new FirebaseCallBack() {
      @Override
      public void onCallBack(UserModel userModel1) {
        userModel = userModel1;

        //instantiate the database
        db = FirebaseDatabase.getInstance();

        //RecyclerView
        commentListRecyclerView = findViewById(R.id.commentListRecyclerView);
        commentListRecyclerView.setHasFixedSize(true);
        commentListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        dbref = db.getReference().child("Comment").child(sID).child(subjectID);

        //set options for adapter
        options = new FirebaseRecyclerOptions.Builder<CommentModel>().
            setQuery(dbref.orderByChild("timestamp"), CommentModel.class).build();

        firebaseRecyclerAdapter =
            new FirebaseRecyclerAdapter<CommentModel, CommentModelViewHolder>(options) {
              @Override
              protected void onBindViewHolder(@NonNull CommentModelViewHolder holder, int position,
                  @NonNull CommentModel model) {
                //bind object
                holder.setCommentID(model.getCommentID());
                holder.setComment(model.getComment());
                holder.setCommentor(model.getCommentor());
                holder.setDate(model.getDate());
              }

              @NonNull
              @Override
              public CommentModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                  int viewType) {
                View view1 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_model_layout_comment, parent, false);
                return new CommentModelViewHolder(view1);
              }
            };

        commentListRecyclerView.setAdapter(firebaseRecyclerAdapter);

        firebaseRecyclerAdapter.startListening();
        hideProgressDialog();
      }
    });
  }

  private void UserQueryClass(final FirebaseCallBack firebaseCallBack) {

    //get current user
    String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseDatabase db2 = FirebaseDatabase.getInstance();
    DatabaseReference dbref2 = db2.getReference().child("User").child(uID);
    dbref2.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        userModel = dataSnapshot.getValue(UserModel.class);
        firebaseCallBack.onCallBack(userModel);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }


  private interface FirebaseCallBack {

    void onCallBack(UserModel userModel);
  }

  public class CommentModelViewHolder extends RecyclerView.ViewHolder {

    View mView;
    String commentID;

    public CommentModelViewHolder(View itemView) {
      super(itemView);
      mView = itemView;
    }

    public void setCommentID(String commentID) {
      this.commentID = commentID;
    }

    public void setComment(String comment) {
      final TextView commentModelComment = mView.findViewById(R.id.commentModelCommentBoxTextView);
      commentModelComment.setText(comment);
      commentModelComment.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          deleteComment();
          return true;
        }
      });

    }

    public void setDate(String date) {
      final TextView commentModelDate = mView.findViewById(R.id.commentModelDate);
      commentModelDate.setText(date);
      commentModelDate.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          deleteComment();
          return true;
        }
      });
    }

    public void setCommentor(String commentor) {
      final TextView commentModelCommentor = mView.findViewById(R.id.commentModelCommentor);
      commentModelCommentor.setText(commentor);
      commentModelCommentor.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          deleteComment();
          return true;
        }
      });
    }

    //<editor-fold desc="deleteComment() -> Prompts a dialog to delete a comment">
    public void deleteComment() {

      FirebaseDatabase db = FirebaseDatabase.getInstance();
      DatabaseReference dbref = db.getReference().child("Comment").child(sID).child(subjectID);
      final Query query = dbref.orderByChild("commentID").equalTo(commentID);

      //Ask for user confirmation
      AlertDialog.Builder builder1 = new AlertDialog.Builder(CommentListActivity.this);
      builder1.setMessage("Delete Comment?");
      builder1.setCancelable(true);

      builder1.setPositiveButton(
          "Yes",
          new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
              // FIND THE SPECIFIC KEY THROUGH QUERY
              query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                  if (dataSnapshot.exists()) {

                    //get the values of the retrieved node
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {

                      //delete node (this points to the event child node)
                      issue.getRef().removeValue();


                    }

                  }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
              });
              //end query
            }
          });

      builder1.setNegativeButton(
          "No",
          new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
              dialog.cancel();
            }
          });

      AlertDialog alert11 = builder1.create();
      alert11.show();
      //end of confirmation
    }
    //</editor-fold>
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
    getMenuInflater().inflate(R.menu.menu_comment_list, menu);
    return true;
  }

  //<editor-fold desc="Add button for new comments">
  public void addComment(MenuItem mi) {
    //BUTTON BUILDER SET STYLE HERE
    AlertDialog.Builder builder = new AlertDialog.Builder(CommentListActivity.this,
        THEME_DEVICE_DEFAULT_DARK);
    builder.setTitle("Comment/Remark: ");

    // Set up the input
    final EditText input = new EditText(getApplicationContext());
    input.setTextColor(Color.BLACK);
    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
    input.setInputType(InputType.TYPE_CLASS_TEXT);
    builder.setView(input);

    // Set up the buttons
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        //Set up the data and timestamp
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        //date in string
        String strDate = dateFormat.format(date);
        long timestamp = date.getTime();

        //get the user input for comment
        String input_Text = input.getText().toString();

        //create a new unique comment ID
        String commentID = UUID.randomUUID().toString();

        //handle user input into database input
        Map<String, Object> addToDatabase = new HashMap<>();

        //DONE Change TestUser to retrieved User's name
        //<editor-fold desc="PROTOTYPE commentor is refered to as 'TestUser'>
        addToDatabase.put("commentor", userModel.getFullname());
        //</editor-fold>

        addToDatabase.put("comment", input_Text);
        addToDatabase.put("commentID", commentID);
        addToDatabase.put("date", strDate);
        addToDatabase.put("timestamp", timestamp);

        //push to database
        dbref.child(commentID).updateChildren(addToDatabase);
      }
    });
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
      }
    });

    builder.show();
  }
  //</editor-fold>
}
