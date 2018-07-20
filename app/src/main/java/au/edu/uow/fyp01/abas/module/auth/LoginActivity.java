package au.edu.uow.fyp01.abas.module.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

  private ProgressDialog progressDialog;
  private FirebaseAuth firebaseAuth;
  private FirebaseUser firebaseUser;

  @BindView(R.id.emailEditText)
  EditText emailText;
  @BindView(R.id.passwordEditText)
  EditText passwordText;
  @BindView(R.id.loginBtn)
  Button loginBtn;
  @BindView(R.id.signUpBtn)
  Button signUpBtn;

  /**
   * Handle UI clicks
   *
   * @param v The trigger View
   */
  @OnClick({R.id.loginBtn, R.id.signUpBtn})
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.loginBtn:
        login();
        break;
      case R.id.signUpBtn:
        signUp();
        break;
      default:
        break;
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);

    firebaseAuth = FirebaseAuth.getInstance();
  }

  /**
   * Auto login if user didn't logout
   */
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

  /**
   * Go to sign up page
   */
  private void signUp() {
    Intent signUpActivity = new Intent(LoginActivity.this, SignUpActivity.class);
    startActivity(signUpActivity);
    this.overridePendingTransition(R.anim.anim_slide_in_to_left, R.anim.anim_slide_out_to_left);
  }

  /**
   * Login into the application
   */
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
              overridePendingTransition(R.anim.anim_slide_in_to_right,
                  R.anim.anim_slide_out_to_right);
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

  /**
   * Check if user enter proper credential
   * @return the form checking result
   */
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
}
