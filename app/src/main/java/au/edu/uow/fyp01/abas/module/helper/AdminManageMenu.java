package au.edu.uow.fyp01.abas.module.helper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import au.edu.uow.fyp01.abas.R;

public class AdminManageMenu extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_manage_menu);

    Button adminManageMenuClasses = findViewById(R.id.adminManageMenuClasses);
    adminManageMenuClasses.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(getApplicationContext(), AdminClassListActivity.class);
        startActivity(i);
      }
    });

    Button adminManageMenuSubjects = findViewById(R.id.adminManageMenuSubjects);
    adminManageMenuSubjects.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(getApplicationContext(), AdminSubjectsListActivity.class);
        startActivity(i);
      }
    });

    Button adminManageRequestMenu = findViewById(R.id.adminManageMenuRequestsBtn);
    adminManageRequestMenu.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(getApplicationContext(), AdminManageRequestActivity.class);
        startActivity(i);
      }
    });

    final Button staffManagement = findViewById(R.id.adminManageMenuStaffBtn);
    staffManagement.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Toast.makeText(getApplicationContext(), "Not Admin", Toast.LENGTH_LONG).show();
      }
    });

    final DatabaseReference userReference = FirebaseDatabase.getInstance().getReference()
            .child("User");

    userReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
       for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
         if(dataSnapshot1.getKey().equals("usertype")){
          if(dataSnapshot1.getValue().equals("Admin")){
            userReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
               for(final DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                if(dataSnapshot2.getKey().equals("schID")){
                  staffManagement.setEnabled(true);
                  staffManagement.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      Intent i = new Intent(getApplicationContext(), AdminManageMenuStaff.class);
                      Bundle args = new Bundle();
                      args.putString("schID", dataSnapshot2.getValue().toString());

                      i.putExtras(args);
                      startActivity(i);
                    }
                  });
                }
               }
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
            });

          }
         }
       }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });


  }
}
