package au.edu.uow.fyp01.abas.module.helper;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import au.edu.uow.fyp01.abas.R;
import au.edu.uow.fyp01.abas.model.ListOfSubjectModel;
import au.edu.uow.fyp01.abas.model.SubjectModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminStudentSubjectListActivity extends AppCompatActivity {

  private ProgressDialog progressDialog;

  private String schID;
  private String sID;
  private String firstname;
  private String lastname;
  private String classID;

  private FirebaseDatabase db;
  private RecyclerView adminStudentListRecyclerView;
  private DatabaseReference dbref;
  private FirebaseRecyclerOptions<SubjectModel> options;
  private FirebaseRecyclerAdapter<SubjectModel, SubjectModelViewHolder> firebaseRecyclerAdapter;

  private List<String> subjectsList;
  private Map<String, ListOfSubjectModel> subjectsMap;
  private Spinner dropdown;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_student_subject_list);

    Bundle bundle = getIntent().getExtras();

    //Grabbing args (classID and schID from AdminStudentDetailActivity)
    schID = bundle.getString("schID");
    sID = bundle.getString("sID");
    firstname = bundle.getString("firstname");
    lastname = bundle.getString("lastname");
    classID = bundle.getString("classID");

    //Set the subject's (the title)
    getSupportActionBar().setTitle(firstname + " " + lastname + "'s Subjects");

    showProgressDialog();

    //Arrays for spinner
    subjectsList = new ArrayList<>();
    subjectsMap = new HashMap<>();

    //QueryClass
    ListOfSubjectsQueryClass(new FirebaseCallBack() {
      @Override
      public void onCallBack(List<String> subjectsList1,
          Map<String, ListOfSubjectModel> subjectsMap1) {

        //instantiate db
        db = FirebaseDatabase.getInstance();
        dbref = db.getReference().child("Subject").child(schID).child(classID).child(sID);

        subjectsList = subjectsList1;
        subjectsMap = subjectsMap1;

        //RecyclerView
        adminStudentListRecyclerView = findViewById(R.id.adminStudentSubjectRecyclerView);
        adminStudentListRecyclerView.setHasFixedSize(true);
        adminStudentListRecyclerView
            .setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //set options for adapter
        options = new FirebaseRecyclerOptions.Builder<SubjectModel>().
            setQuery(dbref.orderByChild("subjectname"), SubjectModel.class).build();

        firebaseRecyclerAdapter =
            new FirebaseRecyclerAdapter<SubjectModel, SubjectModelViewHolder>(options) {
              @Override
              protected void onBindViewHolder(@NonNull SubjectModelViewHolder holder, int position,
                  @NonNull SubjectModel model) {
                //bind object
                holder.setSubjectID(model.getSubjectID());
                holder.setSubjectname(model.getSubjectname());
              }

              @NonNull
              @Override
              public SubjectModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                  int viewType) {
                View view1 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_model_layout_single_button, parent, false);
                return new SubjectModelViewHolder(view1);
              }
            };

        adminStudentListRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
        hideProgressDialog();
      } //end oncallback
    }); //end queryclass
  }

  private void ListOfSubjectsQueryClass(final FirebaseCallBack firebaseCallBack) {
    FirebaseDatabase db2 = FirebaseDatabase.getInstance();
    DatabaseReference dbref2 = db2.getReference().child("ListOfSubjects").child(schID);
    dbref2.orderByChild("subjectname").addChildEventListener(new ChildEventListener() {
      @Override
      public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if (dataSnapshot.exists()) {

          ListOfSubjectModel listOfSubjectModel =
              dataSnapshot.getValue(ListOfSubjectModel.class);

          subjectsList.add(listOfSubjectModel.getSubjectName());
          subjectsMap.put(listOfSubjectModel.getSubjectName(), listOfSubjectModel);

        }

        firebaseCallBack.onCallBack(subjectsList, subjectsMap);
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


    });

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
    getMenuInflater().inflate(R.menu.menu_admin_student_subject_list, menu);
    return true;
  }

  //<editor-fold desc="Add button for subjects">
  public void addNewSubject(MenuItem menuItem) {
    AlertDialog.Builder builder = new AlertDialog.Builder(AdminStudentSubjectListActivity.this);
    builder.setTitle("Add subject to student: ");

    //Set up the layout
    LinearLayout layout = new LinearLayout(AdminStudentSubjectListActivity.this);

    //set up the spinner as a drop down box
    dropdown = new Spinner(AdminStudentSubjectListActivity.this);
    ArrayAdapter<String> adapter = new ArrayAdapter<>(AdminStudentSubjectListActivity.this,
        android.R.layout.simple_spinner_dropdown_item, subjectsList);
    dropdown.setAdapter(adapter);
    //add dropdown to the dialog
    layout.addView(dropdown);

    builder.setView(layout);

    //dialog's OK button
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        //get the ListofSubjectModel from map first
        ListOfSubjectModel listOfSubjectModel = subjectsMap
            .get(dropdown.getSelectedItem().toString());
        //get the subject ID
        String subjectID = listOfSubjectModel.getSubjectID();

        //put user input into database
        final Map<String, Object> addToDatabase = new HashMap<>();
        addToDatabase.put("subjectname", dropdown.getSelectedItem().toString());
        addToDatabase.put("subjectID", subjectID);

        //new db ref
        DatabaseReference dbref2 = db.getReference().child("Subject").child(schID).child(classID)
            .child(sID).child(subjectID);

        dbref2.updateChildren(addToDatabase);
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

  private interface FirebaseCallBack {

    void onCallBack(List<String> subjectsList,
        Map<String, ListOfSubjectModel> subjectsMap);
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

      //<editor-fold desc="Remove subject from student">
      subjectNameButtonView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          //Ask for user confirmation
          AlertDialog.Builder builder1 = new AlertDialog.Builder(
              AdminStudentSubjectListActivity.this);
          builder1.setMessage("Remove subject from student?");
          builder1.setCancelable(true);

          builder1.setPositiveButton(
              "Yes",
              new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                  //Delete Subject->StudentID->SubjectID
                  dbref.child(subjectID).removeValue();

                  //Remove students' records
                  dbref = db.getReference().child("Record").child(sID)
                      .child(subjectID);
                  dbref.removeValue();

                  //Remove students' comments
                  dbref = db.getReference().child("Comment").child(sID)
                      .child(subjectID);
                  dbref.removeValue();
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
  //</editor-fold>
}
