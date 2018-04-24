package au.edu.uow.fyp01.abas.QueryClass;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import au.edu.uow.fyp01.abas.Model.StudentModel;

/**
 * Created by Athens on 2018/04/23.
 */

public class StudentQueryClass {

  private String schID;
  private String sID;
  private String classID;
  private StudentModel studentModel;
  private FirebaseDatabase db;
  private DatabaseReference dbref;
  private Query query;

  public StudentQueryClass(String schID, String sID, String classID) {
    this.schID = schID;
    this.sID = sID;
    this.classID = classID;

    //instantiate the database
    db = FirebaseDatabase.getInstance();
    dbref = db.getReference().child("Student").child(schID).child(classID);
    query = dbref.orderByChild("sID").equalTo(sID);
    query.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {

          //get value of retrieved node

          for (DataSnapshot node : dataSnapshot.getChildren()) {
            StudentModel studentModel = node.getValue(StudentModel.class);
            setStudentModel(studentModel);
          }
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }


  public void setStudentModel(StudentModel studentModel) {
    this.studentModel = studentModel;
  }

  public StudentModel getStudentModel() {
    return studentModel;
  }
}
