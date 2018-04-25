package au.edu.uow.fyp01.abas.QueryClass;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import au.edu.uow.fyp01.abas.Model.RecordModel;

/**
 * Created by Athens on 2018/04/23.
 */

public class RecordQueryClass {

  private ArrayList<RecordModel> recordList;
  private String sID;
  private String subject;
  private FirebaseDatabase db;
  private DatabaseReference dbref;
  private Query query;

  public RecordQueryClass(String sID, String subject) {

    recordList = new ArrayList<RecordModel>();

    this.sID = sID;
    this.subject = subject;

    //instantiate the database
    db = FirebaseDatabase.getInstance();
    dbref = db.getReference().child("Record").child(sID).child(subject);
    query = dbref.orderByChild("timestamp");

    query.addChildEventListener(new ChildEventListener() {
      @Override
      public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if (dataSnapshot.exists()) {

          //get values of retrieved nodes
          for (DataSnapshot node : dataSnapshot.getChildren()) {
            RecordModel recordModel = node.getValue(RecordModel.class);
            recordList.add(recordModel);
            setRecordList(recordList);
          }


        }
      }

      //<editor-fold desc="others">
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
      //</editor-fold>
    });


  }

  public ArrayList<RecordModel> getRecordList() {

    //TODO make a backup query if recordList is null
    return recordList;
  }

  //<editor-fold desc="setRecordList - Sets the recordList">
  public void setRecordList(ArrayList<RecordModel> recordList) {
    this.recordList = recordList;
  }
  //</editor-fold>


}
