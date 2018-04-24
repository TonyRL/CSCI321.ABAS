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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

  private ProgressDialog progressDialog;
  private FirebaseAuth firebaseAuth;
  private FirebaseUser firebaseUser;
  private EditText emailText;
  private EditText passwordText;
  private Button loginBtn;
  private Button signUpBtn;
  private Button cheatBtn;
  private View.OnClickListener onClickListener = new OnClickListener() {
    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.loginBtn:
          login();
          break;
        case R.id.signUpBtn:
          signUp();
          break;
        case R.id.cheatBtn:
          emailText.setText("test123@test.com");
          passwordText.setText("test123");
          login();
          break;
        default:
          break;
      }
    }
  };

  private void signUp() {
    Intent signUpActivity = new Intent(LoginActivity.this, SignUpActivity.class);
    startActivity(signUpActivity);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    emailText = findViewById(R.id.emailEditText);
    passwordText = findViewById(R.id.passwordEditText);
    loginBtn = findViewById(R.id.loginBtn);
    signUpBtn = findViewById(R.id.signUpBtn);
    cheatBtn = findViewById(R.id.cheatBtn);

    loginBtn.setOnClickListener(onClickListener);
    signUpBtn.setOnClickListener(onClickListener);
    cheatBtn.setOnClickListener(onClickListener);

    firebaseAuth = FirebaseAuth.getInstance();
  }

  @Override
  protected void onStart() {
    super.onStart();
    firebaseUser = firebaseAuth.getCurrentUser();

    if (firebaseUser != null) {
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
      finish();
    }
  }

  private void login() {
    if (!isFormValid()) {
      return;
    }

    showProgressDialog();

    firebaseAuth.signInWithEmailAndPassword(emailText.getText().toString(),
        passwordText.getText().toString())
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {

            hideProgressDialog();

            if (task.isSuccessful()) {
              firebaseUser = firebaseAuth.getCurrentUser();

              passwordText.setText("");

              Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
              startActivity(mainActivityIntent);
              finish();
            } else {
              if (task.getException() != null) {
                try {
                  throw task.getException();
                } catch (FirebaseAuthInvalidUserException e) {
                  passwordText.setText("");
                  Toast.makeText(LoginActivity.this, "Authentication failed.\nUser does not exist!",
                      Toast.LENGTH_LONG).show();

                } catch (FirebaseAuthInvalidCredentialsException e) {
                  passwordText.setText("");
                  Toast.makeText(LoginActivity.this,
                      "Authentication failed.\nThe password is invalid.", Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                  Log.e("SignIn: ", e.getMessage());
                } finally {
                  hideProgressDialog();
                }
              }
            }
          }
        });
  }

  private boolean isFormValid() {
    boolean valid = true;
    String email = emailText.getText().toString();
    String password = passwordText.getText().toString();

    if (TextUtils.isEmpty(email) || email.indexOf('@') == -1) {
      emailText.setError("Invalid email");
      emailText.requestFocus();
      valid = false;
    } else {
      emailText.setError(null);
    }

    if (TextUtils.isEmpty(password) && valid) {
      passwordText.setError("Invalid password");
      passwordText.requestFocus();
      valid = false;
    } else {
      passwordText.setError(null);
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
}
