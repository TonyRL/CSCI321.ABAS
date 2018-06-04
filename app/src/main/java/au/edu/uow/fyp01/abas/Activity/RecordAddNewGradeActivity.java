package au.edu.uow.fyp01.abas.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import au.edu.uow.fyp01.abas.R;

public class RecordAddNewGradeActivity extends Activity {

  private FirebaseDatabase db;
  private DatabaseReference dbref;

  private String sID;
  private String subjectID;
  private String date;
  private Long timestamp;

  //date variables
  private int year;
  private int monthOfYear;
  private int dayOfMonth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recordaddnewgradeactivity);

    Bundle bundle = getIntent().getExtras();
    sID = bundle.getString("sID");
    subjectID = bundle.getString("subjectID");

    //instantiate the database
    db = FirebaseDatabase.getInstance();
    dbref = db.getReference().child("Record").child(sID)
        .child(subjectID);

    final EditText recordNewGradeEditView = findViewById(R.id.recordNewGradeEditView);

    final TextView recordNewDateTextView = findViewById(R.id.recordNewDateTextView);
    //Set up date picker dialog
    final DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker view, int year1, int month1, int dayOfMonth1) {
        year = year1;
        monthOfYear = month1;
        dayOfMonth = dayOfMonth1;
        String dateInString = Integer.toString(dayOfMonth) + "-" + Integer.toString(monthOfYear)
            + "-" + Integer.toString(year);
        //update global date variable
        setDate(dateInString);

        //update global timestamp variable
        try {
          Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(dateInString);
          setTimestamp(date1.getTime());
        } catch (ParseException e) {
          e.printStackTrace();
        }

        //set the textview to show new date
        recordNewDateTextView.setText(dateInString);


      }
    };

    recordNewDateTextView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Calendar myCalendar = Calendar.getInstance();
        new DatePickerDialog(RecordAddNewGradeActivity.this,
            datepicker,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
      }
    });

    Button recordAddGradeBtn = findViewById(R.id.recordAddGradeBtn);
    recordAddGradeBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //Ask for user confirmation
        AlertDialog.Builder builder1 = new AlertDialog.Builder(RecordAddNewGradeActivity.this);
        builder1.setMessage("Add New Record?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
            "Yes",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                Map<String, Object> addToDatabase = new HashMap<>();

                //create a new unique record ID
                String recordID = UUID.randomUUID().toString();

                addToDatabase.put("date", date);
                addToDatabase.put("grade", recordNewGradeEditView.getText().toString());
                addToDatabase.put("recordID", recordID);
                addToDatabase.put("timestamp", timestamp);

                dbref.child(recordID).updateChildren(addToDatabase);

                Toast.makeText(RecordAddNewGradeActivity.this, "Added new grade record",
                    Toast.LENGTH_SHORT)
                    .show();
                finish();
              }
            });

        builder1.setNegativeButton(
            "No",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
              }
            });

        AlertDialog alert11 = builder1.create();
        alert11.show();
        //end of confirmation
      }
    });


  }

  public void setDate(String date) {
    this.date = date;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }


}
