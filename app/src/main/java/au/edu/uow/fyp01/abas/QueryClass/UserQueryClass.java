package au.edu.uow.fyp01.abas.QueryClass;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import au.edu.uow.fyp01.abas.Model.UserModel;

/**
 * Created by Athens on 2018/04/27.
 */

public class UserQueryClass {

    private String uID;
    private UserModel userModel;
    private FirebaseDatabase db;
    private DatabaseReference dbref;
    private Query query;
    private FirebaseAuth auth;

    public UserQueryClass() {
        //get current user
        uID = auth.getInstance().getCurrentUser().getUid();
        //instantiate the database
        db = FirebaseDatabase.getInstance();
        dbref = db.getReference().child("User").child(uID);

        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                userModel = dataSnapshot.getValue(UserModel.class);
                setUserModel(userModel);
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

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public String getuID() {
        return uID;
    }
}
