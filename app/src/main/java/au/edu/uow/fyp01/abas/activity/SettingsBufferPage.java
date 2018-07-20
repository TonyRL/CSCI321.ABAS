package au.edu.uow.fyp01.abas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import au.edu.uow.fyp01.abas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsBufferPage extends AppCompatActivity {

  private Button classroomButton;
  private Button registerButton;
  private boolean isRegistered = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings_buffer_page);

    classroomButton = findViewById(R.id.activity_settings_buffer_page_classroom_btn);
    classroomButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent classroomIntent = new Intent(getApplicationContext(), ClassroomHomeSetting.class);
        startActivity(classroomIntent);
      }
    });

    registerButton = findViewById(R.id.activity_settings_buffer_page_register_btn);
    registerButton.setEnabled(false);
    classroomButton.setEnabled(false);

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");
    ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
        .addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot snap : dataSnapshot.getChildren()) {
              if (snap.getKey().equals("status")) {
                if (!snap.getValue().equals("registered")) {
                  isRegistered = false;
                  registerButton.setEnabled(true);
                  classroomButton.setEnabled(false);
                }
                if (snap.getValue().equals("registered")) {
                  isRegistered = true;
                  registerButton.setEnabled(false);
                  classroomButton.setEnabled(true);
                }
              }
            }
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
        });

    registerButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (!isRegistered) {
          Intent i = new Intent(getApplicationContext(), SchoolListActivity.class);
          startActivity(i);
        } else {
          Toast.makeText(getApplicationContext(),
              "You cannot register to any school twice!", Toast.LENGTH_LONG).show();
        }
      }
    });

  }
}
