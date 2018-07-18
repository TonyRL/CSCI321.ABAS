package au.edu.uow.fyp01.abas.Activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.uow.fyp01.abas.R;

public class AdminManageMenuStaffDialog extends AppCompatActivity {

    private String schID;
    private String staffID;
    private String type;
    private String name;
    private String finalUserType = "";


    private TextView nameTextView;
    private TextView staffIDTextView;
    private TextView typeTextView;
    private Button button;

    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_menu_staff_dialog);

        nameTextView = findViewById(R.id.activity_admin_manage_menu_staff_dialog_staff_name);
        staffIDTextView = findViewById(R.id.activity_admin_manage_menu_staff_dialog_staffID);
        typeTextView = findViewById(R.id.activity_admin_manage_menu_staff_dialog_current_user_type);
        button = findViewById(R.id.activity_admin_manage_menu_staff_dialog_button);

        Bundle bundle = getIntent().getExtras();
        schID = bundle.get("schID").toString();
        name = bundle.get("name").toString();
        type = bundle.get("type").toString();
        staffID = bundle.get("staffID").toString();

        nameTextView.setText(name);
        staffIDTextView.setText(staffID);
        typeTextView.setText(type);


        spinner = findViewById(R.id.spinneractivity_admin_manage_menu_staff_dialog_user_type_spinner);
        List<String> item = new ArrayList<String>();
        item.add("Admin");
        item.add("Teacher");

        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.user_type_arrays, android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        finalUserType = "Admin";
                        break;
                    case 1:
                        finalUserType = "Teacher";
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalUserType.equals("")) {
                    Toast.makeText(getApplicationContext(), "No selected choice made", Toast.LENGTH_LONG).show();
                }
                if (finalUserType.equals("Admin") || finalUserType.equals("Teacher")) {

                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
                    ref1.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapUID : dataSnapshot.getChildren()) {
                                String UID = snapUID.getKey().toString();
                                for (DataSnapshot detailsSnap : snapUID.getChildren()) {
                                    if (detailsSnap.getKey().equals("staffID")) {
                                        if (detailsSnap.getValue().equals(staffID)) {
                                            Toast.makeText(getApplicationContext(), UID, Toast.LENGTH_LONG).show();

                                            Map map = new HashMap();
                                            map.put("usertype", finalUserType);
                                            FirebaseDatabase.getInstance().getReference().child("User").child(UID).updateChildren(map, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    Toast.makeText(getApplicationContext(), "Completed user node", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    ref1.child("Staff").child(schID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot uidSnap : dataSnapshot.getChildren()) {
                                String UID = uidSnap.getKey().toString();
                                for (DataSnapshot detailsSnap : uidSnap.getChildren()) {
                                    if (detailsSnap.getKey().equals("staffID")) {
                                        if (detailsSnap.getValue().toString().equals(staffID)) {
                                            Map map = new HashMap();
                                            map.put("usertype", finalUserType);
                                            FirebaseDatabase.getInstance().getReference().child("Staff").child(schID).child(UID).updateChildren(map, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    Toast.makeText(getApplicationContext(), "Completed staff node", Toast.LENGTH_LONG).show();

                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    Intent i = new Intent(getApplicationContext(), AdminManageMenuStaff.class);
                    Bundle args = new Bundle();
                    args.putString("schID", schID);
                    i.putExtras(args);
                    startActivity(i);

                }
            }
        });

    }
}
