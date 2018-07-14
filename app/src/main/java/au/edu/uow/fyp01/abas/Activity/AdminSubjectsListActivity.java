package au.edu.uow.fyp01.abas.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import au.edu.uow.fyp01.abas.Model.ListOfStudentsModel;
import au.edu.uow.fyp01.abas.Model.ListOfSubjectsModel;
import au.edu.uow.fyp01.abas.Model.SubjectModel;
import au.edu.uow.fyp01.abas.Model.UserModel;
import au.edu.uow.fyp01.abas.R;

import static android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK;

public class AdminSubjectsListActivity extends Activity {

  private RecyclerView adminSubjectRecyclerView;
  private FirebaseDatabase db;
  private DatabaseReference dbref;
  private FirebaseRecyclerOptions<ListOfSubjectsModel> options;
  private FirebaseRecyclerAdapter<ListOfSubjectsModel, ListOfSubjectsModelViewHolder> firebaseRecyclerAdapter;

  //Current user's metadata
  private UserModel userModel;
  private String uID;
  private FirebaseAuth auth;
  private String schID;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_adminsubjectslist);

    //get current user
    uID = auth.getInstance().getCurrentUser().getUid();

    schID = "";

    final ProgressBar adminSubjectProgressBar = findViewById(R.id.adminSubjectProgressBar);
    adminSubjectProgressBar.setIndeterminate(true);

    UserQueryClass(new FirebaseCallBack() {
      @Override
      public void onCallBack(UserModel userModel) {
        schID = userModel.getSchID();

        //instantiate db
        db = FirebaseDatabase.getInstance();

        //RecyclerView
        adminSubjectRecyclerView = findViewById(R.id.adminSubjectRecyclerView);
        adminSubjectRecyclerView.setHasFixedSize(true);
        adminSubjectRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //ListOfSubjects -> SchoolID
        dbref = db.getReference().child("ListOfSubjects").child(schID);

        //set options
        options = new FirebaseRecyclerOptions.Builder<ListOfSubjectsModel>().
            setQuery(dbref.orderByChild("subjectname"), ListOfSubjectsModel.class)
            .build();

        firebaseRecyclerAdapter =
            new FirebaseRecyclerAdapter<ListOfSubjectsModel, ListOfSubjectsModelViewHolder>(
                options) {
              @Override
              protected void onBindViewHolder(@NonNull ListOfSubjectsModelViewHolder holder,
                  int position, @NonNull ListOfSubjectsModel model) {
                //bind object
                holder.setSubjectID(model.getSubjectID());
                holder.setSubjectname(model.getSubjectname());
              }

              @NonNull
              @Override
              public ListOfSubjectsModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                  int viewType) {

                View view1 =
                    LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recyclermodellayout_singlebutton,
                            parent, false);
                return new ListOfSubjectsModelViewHolder(view1);
              }
            };

        adminSubjectRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
        adminSubjectProgressBar.setVisibility(View.GONE);

        //<editor-fold desc="Add new subjectbutton">
        Button adminSubjectAddBtn = findViewById(R.id.adminSubjectAddBtn);
        adminSubjectAddBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            //BUTTON BUILDER SET STYLE HERE
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminSubjectsListActivity.this,
                THEME_DEVICE_DEFAULT_DARK);
            builder.setTitle("Add new subject: ");

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

                //get the user input for comment
                String input_Text = input.getText().toString();

                //create a new unique comment ID
                String subjectID = UUID.randomUUID().toString();

                //handle user input into database input
                Map<String, Object> addToDatabase = new HashMap<>();

                addToDatabase.put("subjectname", input_Text);
                addToDatabase.put("subjectID", subjectID);

                //push to database
                dbref.child(subjectID).updateChildren(addToDatabase);
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
        });
        //</editor-fold>

        Button adminManageClassSubjectsBtn = findViewById(R.id.adminManageClassSubjectBtn);
        adminManageClassSubjectsBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

            //<editor-fold desc="move to AdminManageClassSubjectsActivity.class>

            Intent i = new Intent(getApplicationContext(), AdminManageClassSubjectsActivity.class);

            Bundle args = new Bundle();

            args.putString("schID", schID);

            i.putExtras(args);

            startActivity(i);
            //</editor-fold>
          }
        });

      }//end callback
    }); //end query class

  }

  private void UserQueryClass(final FirebaseCallBack firebaseCallBack) {
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

  public class SubjectModelViewHolder extends RecyclerView.ViewHolder {

    View mView;
    String subjectname;
    String subjectID;

    public SubjectModelViewHolder(View itemView) {
      super(itemView);
      mView = itemView;
    }

    public void setSubjectID(String subjectID) {
      this.subjectID = subjectID;
    }

    public void setSubjectname(String subjectname1) {
      Button subjectNameButtonView = mView.findViewById(R.id.modelSingleBtn);
      subjectNameButtonView.setText(subjectname1);
      this.subjectname = subjectname1;

      //<editor-fold desc="Add subject to school">
      subjectNameButtonView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

          return true;
        }
      });
      //</editor-fold>
    }
  }

  public class ListOfSubjectsModelViewHolder extends RecyclerView.ViewHolder {

    View mView;

    String subjectname;
    String subjectID;

    public ListOfSubjectsModelViewHolder(View itemView) {
      super(itemView);
      mView = itemView;
    }

    public void setSubjectID(String subjectID) {
      this.subjectID = subjectID;
    }

    public void setSubjectname(String subjectname) {
      this.subjectname = subjectname;

      final Button subjectNameBtn = mView.findViewById(R.id.modelSingleBtn);
      subjectNameBtn.setText(subjectname);

      //<editor-fold desc="Remove subject from school">
      subjectNameBtn.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          //Ask for user confirmation
          AlertDialog.Builder builder1 = new AlertDialog.Builder(AdminSubjectsListActivity.this);
          builder1.setMessage("Remove subject from school?");
          builder1.setCancelable(true);

          builder1.setPositiveButton(
              "Yes",
              new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                  //Delete Subject->StudentID->SubjectID
                  dbref.child(subjectID).removeValue();

                  //TODO delete subject from records and comments as well
                  //QUERY TO REMOVE SUBJECT FROM ALL STUDENTS IN SCHOOL
                  FirebaseDatabase db2 = FirebaseDatabase.getInstance();
                  final DatabaseReference dbref2 = db2.getReference().child("Subject")
                      .child(schID);
                  dbref2.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                      if (dataSnapshot.exists()) {
                        //Subject -> SchID -> ClassID
                        for (DataSnapshot classIDnode : dataSnapshot.getChildren()) {
                          if (classIDnode.exists()) {

                            //Subject -> SchID -> ClassID -> StudentID
                            for (DataSnapshot studentIDnode : classIDnode.getChildren()) {

                              SubjectModel subjectModel =
                                  studentIDnode.getValue(SubjectModel.class);

                              if (subjectModel.getSubjectID().equals(subjectID)) {
                                studentIDnode.getRef().removeValue();
                              }
                            }
                          }
                        }
                      }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                  });//end query

                  //QUERY TO REMOVE SUBJECT RECORDS FROM STUDENT
                  FirebaseDatabase db3 = FirebaseDatabase.getInstance();
                  final DatabaseReference dbref3 = db3.getReference().child("Record");
                  dbref3.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                      if (dataSnapshot.exists()) {
                        //Record->StudentID
                        for (DataSnapshot studentIDnode : dataSnapshot.getChildren()) {
                          if (studentIDnode.exists()) {
                            //Record->StudentID->SubjectID
                            for (DataSnapshot subjectIDnode : studentIDnode.getChildren()) {

                              if (subjectIDnode.getKey().toString().equals(subjectID)) {
                                subjectIDnode.getRef().removeValue();
                              }
                            }
                          }
                        }
                      }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                  });//end query

                  //QUERY TO REMOVE SUBJECTS FROM COMMENTS
                  //QUERY TO REMOVE SUBJECT RECORDS FROM STUDENT
                  FirebaseDatabase db4 = FirebaseDatabase.getInstance();
                  final DatabaseReference dbref4 = db4.getReference().child("Comment");
                  dbref3.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                      if (dataSnapshot.exists()) {
                        //Record->StudentID
                        for (DataSnapshot studentIDnode : dataSnapshot.getChildren()) {
                          if (studentIDnode.exists()) {
                            //Record->StudentID->SubjectID
                            for (DataSnapshot subjectIDnode : studentIDnode.getChildren()) {
                              if (subjectIDnode.getKey().toString().equals(subjectID)) {
                                subjectIDnode.getRef().removeValue();
                              }
                            }
                          }
                        }
                      }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                  });//end query

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
          return true;
        }
      });
      //</editor-fold>
    }


  }
}
