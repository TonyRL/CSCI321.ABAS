package au.edu.uow.fyp01.abas.module.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import au.edu.uow.fyp01.abas.MainActivity;
import au.edu.uow.fyp01.abas.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

  @BindView(R.id.nameEditText)
  EditText nameText;
  @BindView(R.id.emailEditText)
  EditText emailText;
  @BindView(R.id.titleSpinner)
  Spinner titleSpinner;
  @BindView(R.id.staffIdEditText)
  EditText staffIdText;
  @BindView(R.id.passwordEditText)
  EditText passwordText;
  @BindView(R.id.confPasswordEditText)
  EditText confPasswordText;
  @BindView(R.id.signUpBtn)
  Button signUpBtn;
  @BindView(R.id.backBtn)
  Button backBtn;

  private FirebaseAuth firebaseAuth;
  private DatabaseReference dbRef;

  private ProgressDialog progressDialog;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);
    ButterKnife.bind(this);

    firebaseAuth = FirebaseAuth.getInstance();

    ArrayAdapter<CharSequence> adapter = ArrayAdapter
        .createFromResource(this, R.array.user_title_array, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    titleSpinner.setAdapter(adapter);
    titleSpinner.setSelection(0);
  }

  /**
   * Sent all information to firebase database and login user to the application
   */
  private void signUp() {
    if (!isFormValid()) {
      return;
    }

    showProgressDialog();

    firebaseAuth.createUserWithEmailAndPassword(emailText.getText().toString(),
        passwordText.getText().toString()).addOnCompleteListener(
        this, new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
              //noinspection ConstantConditions
              String newUserUid = firebaseAuth.getCurrentUser().getUid();
              dbRef = FirebaseDatabase.getInstance().getReference().child("User").child(newUserUid);
              dbRef.child("email").setValue(emailText.getText().toString());
              //noinspection SpellCheckingInspection
              dbRef.child("fullname").setValue(nameText.getText().toString());
              dbRef.child("staffID").setValue(staffIdText.getText().toString());
              dbRef.child("status").setValue("unregistered");
              dbRef.child("title").setValue(titleSpinner.getSelectedItem().toString());

              hideProgressDialog();

              Intent mainActivityIntent = new Intent(SignUpActivity.this, MainActivity.class);
              startActivity(mainActivityIntent);
              finish();
              overridePendingTransition(R.anim.anim_slide_in_to_right,
                  R.anim.anim_slide_out_to_right);
            } else {
              Log.w(this.getClass().getSimpleName(), "createUserWithEmail:failure",
                  task.getException());
              Toast.makeText(SignUpActivity.this, "User already exist", Toast.LENGTH_SHORT).show();
            }
          }
        });
  }

  /**
   * Show a progress dialog during internet connection
   */
  private void showProgressDialog() {
    if (progressDialog == null) {
      progressDialog = new ProgressDialog(this);
      progressDialog.setIndeterminate(true);
      progressDialog.setMessage("Loading...");
    }
    progressDialog.show();
  }

  /**
   * Hide the progress dialog
   */
  private void hideProgressDialog() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }

  /**
   * Add animation when going back
   */
  @Override
  public void onBackPressed() {
    finish();
    this.overridePendingTransition(R.anim.anim_slide_in_to_right, R.anim.anim_slide_out_to_right);
  }

  private boolean isFormValid() {
    boolean valid = true;
    String name = nameText.getText().toString();
    String email = emailText.getText().toString();
    String staffId = staffIdText.getText().toString();
    String password = passwordText.getText().toString();
    String confPassword = confPasswordText.getText().toString();

    //<editor-fold desc="Sign up form check logic">
    if (TextUtils.isEmpty(name)) {
      nameText.setError("Please enter your name");
      nameText.requestFocus();
      valid = false;
    } else {
      nameText.setError(null);
    }

    if ((TextUtils.isEmpty(email) || email.indexOf('@') == -1) && valid) {
      emailText.setError("Please enter your email");
      emailText.requestFocus();
      valid = false;
    } else {
      emailText.setError(null);
    }

    if ((TextUtils.isEmpty(staffId)) && valid) {
      staffIdText.setError("Please enter your ID");
      staffIdText.requestFocus();
      valid = false;
    } else {
      staffIdText.setError(null);
    }

    if (TextUtils.isEmpty(password) && valid) {
      passwordText.setError("Please enter the password");
      passwordText.requestFocus();
      valid = false;
    } else {
      passwordText.setError(null);
    }

    if (TextUtils.isEmpty(confPassword) && valid) {
      confPasswordText.setError("Please enter the password again");
      confPasswordText.requestFocus();
      valid = false;
    } else {
      confPasswordText.setError(null);
    }

    if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(confPassword) && valid) {
      if (!TextUtils.equals(password, confPassword)) {
        passwordText.setText("");
        confPasswordText.setText("");
        valid = false;
      } else {
        confPasswordText.setError(null);
      }

      //Firebase password requirement
      if (password.length() <= 5 && valid) {
        passwordText.setText("");
        confPasswordText.setText("");
        passwordText.requestFocus();
        passwordText.setError("Password length must be more than 5 digits");
        valid = false;
      } else {
        passwordText.setError(null);
      }
    }
    //</editor-fold>

    return valid;

  }

  /**
   * Handle UI clicks
   *
   * @param view The trigger view object
   */
  @OnClick({R.id.signUpBtn, R.id.backBtn})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.signUpBtn:
        signUp();
        break;
      case R.id.backBtn:
        onBackPressed();
        break;
    }
  }
}
