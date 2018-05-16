package au.edu.uow.fyp01.abas.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import au.edu.uow.fyp01.abas.R;

public class FileSharingHome extends AppCompatActivity {

    private Button activity_file_sharing_add_file_button;
    private RecyclerView fileRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_sharing_home);
        //Bundle bundle = getIntent().getExtras();

        activity_file_sharing_add_file_button = (Button) findViewById(R.id.activity_file_sharing_add_file_button);
        fileRecyclerView = (RecyclerView) findViewById(R.id.activity_file_sharing_home_recycler_view);

        activity_file_sharing_add_file_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(FileSharingHome.this,FileSharingAdd.class));

            }
        });


    }

}

