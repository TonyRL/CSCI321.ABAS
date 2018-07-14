package au.edu.uow.fyp01.abas.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
  }
}
