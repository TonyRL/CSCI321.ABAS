package au.edu.uow.fyp01.abas.QueryClassReference;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
        userModel = new UserModel();

        readData(new FirebaseCallBack() {
            @Override
            public void onCallBack(UserModel userModel1) {
                userModel = userModel1;
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

    private void readData(final FirebaseCallBack firebaseCallBack) {
        dbref.addValueEventListener(new ValueEventListener() {
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
