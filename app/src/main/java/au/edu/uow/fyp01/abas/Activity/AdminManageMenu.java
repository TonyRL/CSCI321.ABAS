package au.edu.uow.fyp01.abas.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import au.edu.uow.fyp01.abas.R;

public class AdminManageMenu extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_adminmanagemenu);

    Button adminManageMenuClasses = findViewById(R.id.adminManageMenuClasses);
    adminManageMenuClasses.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(getApplicationContext(), AdminClassListActivity.class);
        startActivity(i);
      }
    });
  }
}
