package au.edu.uow.fyp01.abas.Fragment;


import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import au.edu.uow.fyp01.abas.Model.CommentModel;
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

import au.edu.uow.fyp01.abas.Model.CommentModel;
import au.edu.uow.fyp01.abas.Model.UserModel;
import au.edu.uow.fyp01.abas.R;

/**
 * This fragment lists out the comments/remarks a student
 */
public class CommentListFragment extends Fragment {

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


  public CommentListFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    //Grabbing args (sID and subject from RecordFragment)
    sID = getArguments().getString("sID");
    subject = getArguments().getString("subject");

    //Grabbing args (sID and subject from RecordFragment)
    sID = getArguments().getString("sID");
    subjectname = getArguments().getString("subjectname");
    subjectID = getArguments().getString("subjectID");

    return inflater.inflate(R.layout.fragment_commentlist, container, false);
  }

  public void onViewCreated(View view, Bundle savedInstanceState) {

    public void onViewCreated ( final View view, Bundle savedInstanceState){

      //Setup userModel
      UserQueryClass(new FirebaseCallBack() {
        @Override
        public void onCallBack(UserModel userModel1) {
          userModel = userModel1;

          //instantiate the database
          db = FirebaseDatabase.getInstance();

          //set options for adapter
          options = new FirebaseRecyclerOptions.Builder<CommentModel>().
              setQuery(dbref, CommentModel.class).build();

          dbref = db.getReference().child("Comment").child(sID).child(subjectID);

          @NonNull
          @Override
          public CommentModelViewHolder onCreateViewHolder (@NonNull ViewGroup parent,
          int viewType){
            View view1 = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclermodellayout_comment, parent, false);
            return new CommentModelViewHolder(view1);
          }
        }

        ;

    commentListRecyclerView.setAdapter(firebaseRecyclerAdapter);

        //<editor-fold desc="Add button for new comments">
        Button commentListAddBtn = view.findViewById(R.id.commentListAddBtn);
    commentListAddBtn.setOnClickListener(new View.OnClickListener()

        {
          @Override
          public void onClick (View v){
          android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
              getContext());
          builder.setTitle("Add new comment/remark: ");

          // Set up the input
          final EditText input = new EditText(getContext());
          // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
          input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
          builder.setView(input);

          // Set up the buttons
          builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              //Handle confirm here

              //Set up the data and timestamp
              DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
              Date date = new Date();
              //date in string
              String strDate = dateFormat.format(date).toString();
              long timestamp = date.getTime();

              firebaseRecyclerAdapter.startListening();

              //<editor-fold desc="Add button for new comments">
              Button commentListAddBtn = view.findViewById(R.id.commentListAddBtn);
              commentListAddBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
                      getContext());
                  builder.setTitle("Add new comment/remark: ");

                  //create a new unique comment ID
                  String commentID = UUID.randomUUID().toString();

                  //handle user input into database input
                  Map<String, Object> addToDatabase = new HashMap<>();

                  //TODO Change TestUser to retrieved User's name
                  //<editor-fold desc="PROTOTYPE commentor is refered to as 'TestUser'>
                  addToDatabase.put("commentor", "TestUser");
                  //</editor-fold>

                  addToDatabase.put("comment", input_Text);
                  addToDatabase.put("commentID", commentID);
                  addToDatabase.put("date", date);
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

              //DONE Change TestUser to retrieved User's name
              //<editor-fold desc="PROTOTYPE commentor is refered to as 'TestUser'>
              addToDatabase.put("commentor", userModel.getFullname());
              //</editor-fold>

              @Override
              public void onStart () {
                super.onStart();
                firebaseRecyclerAdapter.startListening();
              }

              @Override
              public void onStop () {
                super.onStop();
                firebaseRecyclerAdapter.stopListening();
              }

              public class CommentModelViewHolder extends RecyclerView.ViewHolder {

                builder.show();
              }
            });
            //</editor-fold>


          }
        });
        }


        public class CommentModelViewHolder extends RecyclerView.ViewHolder {

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
          DatabaseReference dbref = db.getReference().child("Comment").child(sID).child(subject);
          final Query query = dbref.orderByChild("commentID").equalTo(commentID);

          //Ask for user confirmation
          AlertDialog.Builder builder;
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(),
                android.R.style.Theme_Material_Dialog_Alert);
          } else {
            builder = new AlertDialog.Builder(getContext());
          }
          builder.setTitle("Delete comment")
              .setMessage("Are you sure you want to delete this comment?")
              .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                  // continue with delete

                  // FIND THE SPECIFIC KEY THROUGH QUERY
                  query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                      if (dataSnapshot.exists()) {

                        //get the values of the retrieved node
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {

                          FirebaseDatabase db = FirebaseDatabase.getInstance();
                          DatabaseReference dbref = db.getReference().child("Comment").child(sID)
                              .child(subjectID);
                          final Query query = dbref.orderByChild("commentID").equalTo(commentID);


                        }

                      }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                  });
                  //end query
                }
              })
              .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                  // do nothing
                }
              })
              .setIcon(android.R.drawable.ic_dialog_alert)
              .show();
          //end of confirmation
        }
        //</editor-fold>
      }

      private void UserQueryClass ( final FirebaseCallBack firebaseCallBack){

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

    }




