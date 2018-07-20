package au.edu.uow.fyp01.abas.module.record;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import au.edu.uow.fyp01.abas.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RecordEditGradeActivity extends AppCompatActivity {

  private FirebaseDatabase db;
  private DatabaseReference dbref;

  DatePickerDialog.OnDateSetListener datepicker;
  Spinner dropdown;

  EditText recordNameEditView;
  EditText recordGradeEditView;
  TextView recordDateTextView;
  TextView recordTypeTextView;

  private String sID;
  private String subjectID;
  private String recordID;
  private String grade;
  private String date;
  private Long timestamp;
  private String gradename;
  private String type;
  private String[] gradetypes = {"assignment", "quiz", "test", "exam"};

  //date variables
  private int year;
  private int monthOfYear;
  private int dayOfMonth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_record_edit_grade);
    Bundle bundle = getIntent().getExtras();

    sID = bundle.getString("sID");
    subjectID = bundle.getString("subjectID");
    recordID = bundle.getString("recordID");
    grade = bundle.getString("grade");
    date = bundle.getString("date");
    timestamp = bundle.getLong("timestamp");
    gradename = bundle.getString("gradename");
    type = bundle.getString("type");

    db = FirebaseDatabase.getInstance();
    //Record -> StudentID -> SubjectID -> RecordID
    dbref = db.getReference().child("Record").child(sID).child(subjectID).child(recordID);

    recordNameEditView = findViewById(R.id.recordNameEditView);
    recordNameEditView.setText(gradename);

    recordGradeEditView = findViewById(R.id.recordGradeEditView);
    recordGradeEditView.setText(grade);

    recordDateTextView = findViewById(R.id.recordDateTextView);
    recordDateTextView.setText(date);
    //Set up date picker dialog
    datepicker = new DatePickerDialog.OnDateSetListener() {
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
        new DatePickerDialog(RecordEditGradeActivity.this, datepicker,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
      }

    });

    TextView recordIDTextView = findViewById(R.id.recordIDTextView);
    recordIDTextView.setText(recordID);

    recordTypeTextView = findViewById(R.id.recordTypeTextView);
    recordTypeTextView.setText(type);

    recordTypeTextView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
            RecordEditGradeActivity.this);
        builder.setTitle("Change type to: ");

        //Set up the layout
        LinearLayout layout = new LinearLayout(RecordEditGradeActivity.this);

        //set up the spinner as a drop down box
        dropdown = new Spinner(RecordEditGradeActivity.this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(RecordEditGradeActivity.this,
            android.R.layout.simple_spinner_dropdown_item, gradetypes);
        dropdown.setAdapter(adapter);
        //add dropdown to the dialog
        layout.addView(dropdown);

        builder.setView(layout);

        //dialog's OK button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            type = dropdown.getSelectedItem().toString();
            recordTypeTextView.setText(dropdown.getSelectedItem().toString());
          }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        });

        builder.show();
      }
    });
  }

  public void setDate(String date) {
    this.date = date;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_record_edit_grade, menu);
    return true;
  }

  public void saveGrade(MenuItem mi) {
    Map<String, Object> addToDatabase = new HashMap<>();

    addToDatabase.put("date", date);
    addToDatabase.put("grade", recordGradeEditView.getText().toString());
    addToDatabase.put("recordID", recordID);
    addToDatabase.put("timestamp", timestamp);
    addToDatabase.put("gradename", recordNameEditView.getText().toString());
    addToDatabase.put("type", recordTypeTextView.getText().toString());

    dbref.updateChildren(addToDatabase);

    Toast.makeText(RecordEditGradeActivity.this, "Grade record saved", Toast.LENGTH_SHORT)
        .show();
    finish();
  }

  public void deleteGrade(MenuItem mi) {
    //Ask for user confirmation
    AlertDialog.Builder builder1 = new AlertDialog.Builder(RecordEditGradeActivity.this);
    builder1.setMessage("Delete Record?");
    builder1.setCancelable(true);

    builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        dbref.removeValue();
        finish();
      }
    });

    builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        dialog.cancel();
      }
    });

    AlertDialog alert11 = builder1.create();
    alert11.show();
    //end of confirmation
  }
}
