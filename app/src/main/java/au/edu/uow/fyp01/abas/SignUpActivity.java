package au.edu.uow.fyp01.abas;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

  private FirebaseAuth firebaseAuth;
  private DatabaseReference dbRef;

  private ProgressDialog progressDialog;

  private EditText nameText;
  private EditText emailText;
  private EditText passwordText;
  private EditText confPasswordText;

  private Button signUpBtn;
  private Button backBtn;
  private View.OnClickListener onClickListener = new OnClickListener() {
    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.signUpBtn:
          signUp();
          break;
        case R.id.backBtn:
          onBackPressed();
          break;
        default:
          break;
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);

    nameText = findViewById(R.id.nameEditText);
    emailText = findViewById(R.id.emailEditText);
    passwordText = findViewById(R.id.passwordEditText);
    confPasswordText = findViewById(R.id.confPasswordEditText);
    signUpBtn = findViewById(R.id.signUpBtn);
    backBtn = findViewById(R.id.backBtn);

    signUpBtn.setOnClickListener(onClickListener);
    backBtn.setOnClickListener(onClickListener);

    firebaseAuth = FirebaseAuth.getInstance();
  }

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

              hideProgressDialog();

              Intent mainActivityIntent = new Intent(SignUpActivity.this, MainActivity.class);
              startActivity(mainActivityIntent);
            } else {
              Log.w(this.getClass().getSimpleName(), "createUserWithEmail:failure",
                  task.getException());
              Toast.makeText(SignUpActivity.this, "User already exist", Toast.LENGTH_SHORT).show();
            }
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

  private boolean isFormValid() {
    boolean valid = true;
    String name = nameText.getText().toString();
    String email = emailText.getText().toString();
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
}
