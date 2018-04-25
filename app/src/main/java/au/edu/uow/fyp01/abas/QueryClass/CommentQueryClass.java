package au.edu.uow.fyp01.abas.QueryClass;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import au.edu.uow.fyp01.abas.Model.CommentModel;

/**
 * Created by Athens on 2018/04/25.
 */

public class CommentQueryClass {

    private ArrayList<CommentModel> commentList;
    private String sID;
    private String subject;
    private FirebaseDatabase db;
    private DatabaseReference dbref;
    private Query query;

    public CommentQueryClass(String sID, String subject) {

        commentList = new ArrayList<CommentModel>();
        this.sID = sID;
        this.subject = subject;

        //instantiate the database
        db = FirebaseDatabase.getInstance();
        dbref = db.getReference().child("Comment").child(sID).child(subject);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {

                    //get values of retrieved nodes
                    for (DataSnapshot node : dataSnapshot.getChildren()){
                        CommentModel commentModel = node.getValue(CommentModel.class);
                        commentList.add(commentModel);
                        setCommentList(commentList);
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

    public ArrayList<CommentModel> getCommentList() {

        //TODO make a backup query if commentList is null
        return commentList;
    }

    //<editor-fold desc="setCommentList - Sets the commentList">
    public void setCommentList(ArrayList<CommentModel> commentList) {
        this.commentList = commentList;
    }
    //</editor-fold>

}
