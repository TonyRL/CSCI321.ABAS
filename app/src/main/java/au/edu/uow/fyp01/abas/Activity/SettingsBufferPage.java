package au.edu.uow.fyp01.abas.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import au.edu.uow.fyp01.abas.R;

public class SettingsBufferPage extends AppCompatActivity {

    private Button classroomButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_buffer_page);
        
        classroomButton = (Button) findViewById(R.id.activity_settings_buffer_page_classroom_btn);
        classroomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent classroomIntent = new Intent(getApplicationContext(),ClassroomHomeSetting.class);
                startActivity(classroomIntent);

            }
        });

    }
}
