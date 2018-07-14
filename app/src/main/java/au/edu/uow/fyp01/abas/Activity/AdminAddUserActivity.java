package au.edu.uow.fyp01.abas.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import au.edu.uow.fyp01.abas.Model.UserModel;
import au.edu.uow.fyp01.abas.R;

public class AdminAddUserActivity extends Activity {

    private FirebaseDatabase db;
    private DatabaseReference dbref;

    //Current user's metadata
    private UserModel userModel;
    private String uID;
    private FirebaseAuth auth;
    private String schID;
    private String emailsuffix;

    //spinner(drop-down) data
    private String[] titles = {"Mr.", "Ms.", "Mrs.", "Dr."};
    private String[] usertypes = {"Admin", "Teacher"};

    private EditText adminAddUserFullName;
    private EditText adminAddUserTeacherID;
    private EditText adminAddUserPassword;
    private EditText adminAddUserConfirmPassword;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminadduser);

        //get current user
        uID = auth.getInstance().getCurrentUser().getUid();

        schID = "";
        emailsuffix = "";



        UserQueryClass(new FirebaseCallBack() {
            @Override
            public void onCallBack(UserModel userModel) {
                //mother spawns the school ID (schID)
                schID = userModel.getSchID();
                emailsuffix = userModel.getEmailsuffix();


                //instantiate db
                db = FirebaseDatabase.getInstance();
                dbref = db.getReference().child("User");

                adminAddUserFullName = findViewById(R.id.adminAddUserFullName);
                adminAddUserTeacherID = findViewById(R.id.adminAddUserTeacherID);
                adminAddUserPassword = findViewById(R.id.adminAddUserPassword);
                adminAddUserConfirmPassword = findViewById(R.id.adminAddUserConfirmPassword);

                //set up the spinners (drop-downs)
                Spinner adminAddUserTitleSpinner = findViewById(R.id.adminAddUserTitleSpinner);
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                        AdminAddUserActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        titles
                );
                adminAddUserTitleSpinner.setAdapter(adapter1);

                Spinner adminAddUserTypeSpinner = findViewById(R.id.adminAddUserTypeSpinner);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                        AdminAddUserActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        usertypes
                );
                adminAddUserTypeSpinner.setAdapter(adapter2);

                Button adminAddUserBtn = findViewById(R.id.adminAddUserBtn);
                adminAddUserBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (isFormValid()) {




                        }


                    } //end onClick
                }); //end click listener


            } //end onCallBack
        }); //end query class

    }

    private boolean isFormValid(){
        boolean valid = true;

        String fullname = adminAddUserFullName.getText().toString();
        String teacherID = adminAddUserTeacherID.getText().toString();
        String password = adminAddUserPassword.getText().toString();
        String confirmpassword = adminAddUserConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(fullname)) {
            adminAddUserFullName.setError("Please enter name");
            adminAddUserFullName.requestFocus();
            valid = false;
        } else {
            adminAddUserFullName.setError(null);
        }

        if (TextUtils.isEmpty(teacherID)) {
            adminAddUserTeacherID.setError("Please enter ID");
            adminAddUserTeacherID.requestFocus();
            valid = false;
        } else {
            adminAddUserTeacherID.setError(null);
        }



        if (TextUtils.isEmpty(password) && valid) {
            adminAddUserPassword.setError("Please enter the password");
            adminAddUserPassword.requestFocus();
            valid = false;
        } else {
            adminAddUserPassword.setError(null);
        }

        if (TextUtils.isEmpty(confirmpassword) && valid) {
            adminAddUserConfirmPassword.setError("Please enter the password again");
            adminAddUserConfirmPassword.requestFocus();
            valid = false;
        } else {
            adminAddUserConfirmPassword.setError(null);
        }

        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmpassword) && valid) {
            if (!TextUtils.equals(password, confirmpassword)) {
                adminAddUserPassword.setText("");
                adminAddUserConfirmPassword.setText("");
                valid = false;
            } else {
                adminAddUserConfirmPassword.setError(null);
            }

            if (password.length() <= 5 && valid) {
                adminAddUserPassword.setText("");
                adminAddUserConfirmPassword.setText("");
                adminAddUserPassword.requestFocus();
                adminAddUserPassword.setError("Password length must be more than 5 digits");
                valid = false;
            } else {
                adminAddUserPassword.setError(null);
            }
        }

        return valid;

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
}
