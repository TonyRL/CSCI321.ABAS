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

import au.edu.uow.fyp01.abas.R;

public class RecordEditGradeActivity extends Activity {

  private FirebaseDatabase db;
  private DatabaseReference dbref;

  private String sID;
  private String subjectID;
  private String recordID;
  private String grade;
  private String date;
  private Long timestamp;

  //date variables
  private int year;
  private int monthOfYear;
  private int dayOfMonth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recordeditgrade);
    Bundle bundle = getIntent().getExtras();

    sID = bundle.getString("sID");
    subjectID = bundle.getString("subjectID");
    recordID = bundle.getString("recordID");
    grade = bundle.getString("grade");
    date = bundle.getString("date");
    timestamp = bundle.getLong("timestamp");

    db = FirebaseDatabase.getInstance();
    //Record -> StudentID -> SubjectID -> RecordID
    dbref = db.getReference().child("Record").child(sID)
        .child(subjectID).child(recordID);

    final EditText recordGradeEditView = findViewById(R.id.recordGradeEditView);
    recordGradeEditView.setText(grade);

    final TextView recordDateTextView = findViewById(R.id.recordDateTextView);
    recordDateTextView.setText(date);
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
        recordDateTextView.setText(dateInString);


      }
    };

    recordDateTextView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Calendar myCalendar = Calendar.getInstance();
        new DatePickerDialog(RecordEditGradeActivity.this,
            datepicker,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
      }

    });

    TextView recordIDTextView = findViewById(R.id.recordIDTextView);
    recordIDTextView.setText(recordID);

    Button recordEditGradeSaveBtn = findViewById(R.id.recordEditGradeSaveBtn);
    recordEditGradeSaveBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Map<String, Object> addToDatabase = new HashMap<>();

        addToDatabase.put("date", date);
        addToDatabase.put("grade", recordGradeEditView.getText().toString());
        addToDatabase.put("recordID", recordID);
        addToDatabase.put("timestamp", timestamp);

        dbref.updateChildren(addToDatabase);

        Toast.makeText(RecordEditGradeActivity.this, "Grade record saved", Toast.LENGTH_SHORT)
            .show();
      }
    });

    Button recordEditGradeDeleteBtn = findViewById(R.id.recordEditGradeDeleteBtn);
    recordEditGradeDeleteBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //Ask for user confirmation
        AlertDialog.Builder builder1 = new AlertDialog.Builder(RecordEditGradeActivity.this);
        builder1.setMessage("Delete Record?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
            "Yes",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                dbref.removeValue();
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
