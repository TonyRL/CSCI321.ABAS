package au.edu.uow.fyp01.abas.Activity;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import au.edu.uow.fyp01.abas.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.classroom.ClassroomScopes;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.CourseWork;
import com.google.api.services.classroom.model.ListCourseWorkResponse;
import com.google.api.services.classroom.model.ListCoursesResponse;
import com.google.api.services.classroom.model.ListStudentSubmissionsResponse;
import com.google.api.services.classroom.model.ListStudentsResponse;
import com.google.api.services.classroom.model.ListTeachersResponse;
import com.google.api.services.classroom.model.Student;
import com.google.api.services.classroom.model.StudentSubmission;
import com.google.api.services.classroom.model.Teacher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class ClassroomHomeSetting extends Activity implements EasyPermissions.PermissionCallbacks {

  GoogleAccountCredential mCredential;
  private TextView accountTextView;
  private TextView statusTextView;
  private Button mCallApiButton;
  private Button reconnectButton;
  private RecyclerView recyclerView;
  private FirebaseRecyclerOptions firebaseRecyclerOptions;
  private FirebaseRecyclerAdapter<ClassroomHomeSettingRecyclerClass, ClassroomHomeSettingHolder> firebaseRecyclerAdapter;
  ProgressDialog mProgress;

  static final int REQUEST_ACCOUNT_PICKER = 1000;
  static final int REQUEST_AUTHORIZATION = 1001;
  static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
  static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

  private static final String BUTTON_TEXT = "Connect to Classroom";
  private static final String PREF_ACCOUNT_NAME = "accountName";
  private static final String[] SCOPES = {
      ClassroomScopes.CLASSROOM_COURSEWORK_STUDENTS, ClassroomScopes.CLASSROOM_COURSEWORK_ME,
      ClassroomScopes.CLASSROOM_ANNOUNCEMENTS, ClassroomScopes.CLASSROOM_ROSTERS,
      ClassroomScopes.CLASSROOM_COURSES, ClassroomScopes.CLASSROOM_GUARDIANLINKS_STUDENTS,
      ClassroomScopes.CLASSROOM_PROFILE_EMAILS, ClassroomScopes.CLASSROOM_PROFILE_PHOTOS};

  /**
   * Create the main activity.
   *
   * @param savedInstanceState previously saved instance data.
   */

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_class_room_home_setting);
    mCallApiButton = findViewById(R.id.class_room_home_api_button);
    reconnectButton = findViewById(R.id.class_room_home_api_button_1);
    accountTextView = findViewById(R.id.class_room_home_gmail_textview);
    accountTextView.setText("N/A (Account)");
    accountTextView.setTextColor(Color.BLACK);
    statusTextView = findViewById(R.id.class_room_home_status_textview);
    statusTextView.setText("N/A (Status)");
    statusTextView.setTextColor(Color.BLACK);
    mCallApiButton.setText(BUTTON_TEXT);
    recyclerView = findViewById(R.id.acitivity_class_room_home_setting_recyclerview);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(ClassroomHomeSetting.this));

    mCallApiButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
            ClassroomHomeSetting.this, Arrays.asList(SCOPES))
            .setBackOff(new ExponentialBackOff());
        mCallApiButton.setEnabled(false);
        getResultsFromApi();
        mCallApiButton.setEnabled(true);
      }
    });

    reconnectButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
            ClassroomHomeSetting.this, Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());
        reconnectButton.setEnabled(false);
        getResultsFromApi();
        reconnectButton.setTextColor(Color.RED);
        reconnectButton.setEnabled(true);
      }
    });

    DatabaseReference setTextRefAccount = FirebaseDatabase.getInstance().getReference();

    //check
    setTextRefAccount.child("Classroom_Linked_Account_General")
        .addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
              if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                  .exists()) {
                String userName = dataSnapshot
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Account_Details").child("Gmail_Account").getValue().toString();
                String status = dataSnapshot
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Account_Details").child("Status").getValue().toString();
                accountTextView.setText(userName);
                accountTextView.setTextColor(Color.GREEN);
                statusTextView.setText(status);
                statusTextView.setTextColor(Color.GREEN);
                mCallApiButton.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {

                  }
                });
                mCallApiButton.setTextColor(Color.GREEN);
              } else {
                Toast.makeText(ClassroomHomeSetting.this, "Account->nope".toString(),
                    Toast.LENGTH_SHORT).show();
//
              }
            } else {
              Toast.makeText(ClassroomHomeSetting.this, "Empty".toString(), Toast.LENGTH_SHORT)
                  .show();
            }
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
        });

    mProgress = new ProgressDialog(this);
    mProgress.setMessage("Calling Classroom API ...");

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
        .child("Classroom_Linked_Account_General")
        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Class_List");
    firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<ClassroomHomeSettingRecyclerClass>()
        .setQuery(ref, ClassroomHomeSettingRecyclerClass.class).build();

    firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ClassroomHomeSettingRecyclerClass, ClassroomHomeSettingHolder>(
        firebaseRecyclerOptions) {
      @Override
      protected void onBindViewHolder(@NonNull ClassroomHomeSettingHolder holder, int position,
          @NonNull ClassroomHomeSettingRecyclerClass model) {
        holder.setCourseName(model.getName_Course());
      }

      @NonNull
      @Override
      public ClassroomHomeSettingHolder onCreateViewHolder(@NonNull ViewGroup parent,
          int viewType) {
        View view1 = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.activity_class_room_home_setting_recyclerview_item, parent, false);
        return new ClassroomHomeSettingHolder(view1);
      }
    };

    recyclerView.setAdapter(firebaseRecyclerAdapter);
  }

  /**
   * Attempt to call the API, after verifying that all the preconditions are satisfied. The
   * preconditions are: Google Play Services installed, an account was selected and the device
   * currently has online access. If any of the preconditions are not satisfied, the app will prompt
   * the user as appropriate.
   */
  private void getResultsFromApi() {
    if (!isGooglePlayServicesAvailable()) {
      acquireGooglePlayServices();
    } else if (mCredential.getSelectedAccountName() == null) {
      chooseAccount();
    } else if (!isDeviceOnline()) {
      Toast.makeText(ClassroomHomeSetting.this, "No network connection available.",
          Toast.LENGTH_LONG).show();
    } else {
      new MakeRequestTask(mCredential).execute();
      //Store name
      //Toast.makeText(ClassroomHomeSetting.this, mCredential.getSelectedAccountName(), Toast.LENGTH_LONG).show();
    }
  }

  /**
   * Attempts to set the account used with the API credentials. If an account name was previously
   * saved it will use that one; otherwise an account picker dialog will be shown to the user. Note
   * that the setting the account to use with the credentials object requires the app to have the
   * GET_ACCOUNTS permission, which is requested here if it is not already present. The
   * AfterPermissionGranted annotation indicates that this function will be rerun automatically
   * whenever the GET_ACCOUNTS permission is granted.
   */
  @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
  private void chooseAccount() {
    if (EasyPermissions.hasPermissions(this, Manifest.permission.GET_ACCOUNTS)) {
      startActivityForResult(mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    } else {
      // Request the GET_ACCOUNTS permission via a user dialog
      EasyPermissions.requestPermissions(
          this, "This app needs to access your Google account (via Contacts).",
          REQUEST_PERMISSION_GET_ACCOUNTS, Manifest.permission.GET_ACCOUNTS);
      startActivityForResult(mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }
  }

  /**
   * Called when an activity launched here (specifically, AccountPicker and authorization) exits,
   * giving you the requestCode you started it with, the resultCode it returned, and any additional
   * data from it.
   *
   * @param requestCode code indicating which activity result is incoming.
   * @param resultCode code indicating the result of the incoming activity result.
   * @param data Intent (containing result data) returned by incoming activity result.
   */
  @Override
  protected void onActivityResult(
      int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case REQUEST_GOOGLE_PLAY_SERVICES:
        if (resultCode != RESULT_OK) {
          Toast.makeText(ClassroomHomeSetting.this,
              "This app requires Google Play Services. Please install " +
                  "Google Play Services on your device and relaunch this app.", Toast.LENGTH_LONG)
              .show();
        } else {
          getResultsFromApi();
        }
        break;
      case REQUEST_ACCOUNT_PICKER:
        if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
          String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
          Toast.makeText(ClassroomHomeSetting.this, "FIRST:" + AccountManager.KEY_ACCOUNT_NAME,
              Toast.LENGTH_SHORT).show();
          if (accountName != null) {
            SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(PREF_ACCOUNT_NAME, accountName);
            Toast.makeText(ClassroomHomeSetting.this, "FIRST:" + PREF_ACCOUNT_NAME,
                Toast.LENGTH_SHORT).show();
            editor.apply();
            mCredential.setSelectedAccountName(accountName);
            Toast.makeText(ClassroomHomeSetting.this, "FIRST:" + accountName, Toast.LENGTH_SHORT)
                .show();
            getResultsFromApi();
          }
        }
        break;
      case REQUEST_AUTHORIZATION:
        if (resultCode == RESULT_OK) {
          getResultsFromApi();
        }
        break;
    }
  }

  /**
   * Respond to requests for permissions at runtime for API 23 and above.
   *
   * @param requestCode The request code passed in requestPermissions(android.app.Activity, String,
   * int, String[])
   * @param permissions The requested permissions. Never null.
   * @param grantResults The grant results for the corresponding permissions which is either
   * PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
   */
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
  }

  /**
   * Callback for when a permission is granted using the EasyPermissions library.
   *
   * @param requestCode The request code associated with the requested permission
   * @param list The requested permission list. Never null.
   */
  @Override
  public void onPermissionsGranted(int requestCode, List<String> list) {
    // Do nothing.
  }

  /**
   * Callback for when a permission is denied using the EasyPermissions library.
   *
   * @param requestCode The request code associated with the requested permission
   * @param list The requested permission list. Never null.
   */
  @Override
  public void onPermissionsDenied(int requestCode, List<String> list) {
    // Do nothing.
  }

  /**
   * Checks whether the device currently has a network connection.
   *
   * @return true if the device has a network connection, false otherwise.
   */
  private boolean isDeviceOnline() {
    ConnectivityManager connMgr =
        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    return (networkInfo != null && networkInfo.isConnected());
  }

  /**
   * Check that Google Play services APK is installed and up to date.
   *
   * @return true if Google Play Services is available and up to date on this device; false
   * otherwise.
   */
  private boolean isGooglePlayServicesAvailable() {
    GoogleApiAvailability apiAvailability =
        GoogleApiAvailability.getInstance();
    final int connectionStatusCode =
        apiAvailability.isGooglePlayServicesAvailable(this);
    return connectionStatusCode == ConnectionResult.SUCCESS;
  }

  /**
   * Attempt to resolve a missing, out-of-date, invalid or disabled Google Play Services
   * installation via a user dialog, if possible.
   */
  private void acquireGooglePlayServices() {
    GoogleApiAvailability apiAvailability =
        GoogleApiAvailability.getInstance();
    final int connectionStatusCode =
        apiAvailability.isGooglePlayServicesAvailable(this);
    if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
      showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
    }
  }


  /**
   * Display an error dialog showing that Google Play Services is missing or out of date.
   *
   * @param connectionStatusCode code describing the presence (or lack of) Google Play Services on
   * this device.
   */
  void showGooglePlayServicesAvailabilityErrorDialog(
      final int connectionStatusCode) {
    GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
    Dialog dialog = apiAvailability.getErrorDialog(
        ClassroomHomeSetting.this,
        connectionStatusCode,
        REQUEST_GOOGLE_PLAY_SERVICES);
    dialog.show();
  }

  /**
   * An asynchronous task that handles the Classroom API call. Placing the API calls in their own
   * task ensures the UI stays responsive.
   */
  private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {

    private com.google.api.services.classroom.Classroom mService = null;
    private Exception mLastError = null;
    private List<Course> listOfCourse = new ArrayList<>();
    private List<String> listOfIDs = new ArrayList<>();
    private List<List<Student>> listOfSTDIDs = new ArrayList<>();
    private List<List<Teacher>> listOfTeacherIDs = new ArrayList<>();
    private List<List<CourseWork>> listOfCourseWork = new ArrayList<>();
    private List<List<List<StudentSubmission>>> listOfStudentSubmission = new ArrayList<>();

    MakeRequestTask(GoogleAccountCredential credential) {
      HttpTransport transport = AndroidHttp.newCompatibleTransport();
      JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
      mService = new com.google.api.services.classroom.Classroom.Builder(
          transport, jsonFactory, credential).setApplicationName("ABAS").build();
    }

    /**
     * Background task to call Classroom API.
     *
     * @param params no parameters needed for this task.
     */
    @Override
    protected List<String> doInBackground(Void... params) {
      try {
        return getDataFromApi();
      } catch (Exception e) {
        mLastError = e;
        cancel(true);
        return null;
      }
    }

    /**
     * Fetch a list of the names of the first 10 courses the user has access to.
     *
     * @return List course names, or a simple error message if no courses are found.
     */
    //Use the if else statements as reference points to avoid error
    private List<String> getDataFromApi() throws IOException {
      ListCoursesResponse response = mService.courses().list().execute();

      List<Course> courses = response.getCourses();
      List<String> names = new ArrayList<>();
      List<String> courseID = new ArrayList<>();
      List<String> sectionList = new ArrayList<>();

      List<List<Student>> stdlist = new ArrayList<>();
      List<List<Teacher>> teacherList = new ArrayList<>();
      List<List<CourseWork>> courseWorkList = new ArrayList<>();
      List<List<List<StudentSubmission>>> studentSubmissionList = new ArrayList<>();

      if (courses != null) {
        for (Course course : courses) {
          names.add(course.getName());
          courseID.add(course.getId());
          sectionList.add(course.getSection());

          ListTeachersResponse teachersResponse = mService.courses().teachers().list(course.getId())
              .execute();
          List<Teacher> teacherList1 = teachersResponse.getTeachers();
          if (teacherList1 != null) {
            teacherList.add(teacherList1);
          } else {
            teacherList.add(null);
          }

          ListStudentsResponse studentsResponse = mService.courses().students().list(course.getId())
              .execute();
          List<Student> studentList = studentsResponse.getStudents();
          if (studentList != null) {
            stdlist.add(studentList);
          } else {
            stdlist.add(null);
          }
          ListCourseWorkResponse courseWorkResponse = mService.courses().courseWork()
              .list(course.getId()).execute();
          List<CourseWork> courseWorkList1 = courseWorkResponse.getCourseWork();
          if (courseWorkList1 != null) {
            Iterator cwlitr = courseWorkList1.iterator();
            List<List<StudentSubmission>> stdsublist = new ArrayList<>();
            while (cwlitr.hasNext()) {
              CourseWork courseWork = (CourseWork) cwlitr.next();
              ListStudentSubmissionsResponse studentSubmissionsResponse = mService.courses()
                  .courseWork().studentSubmissions()
                  .list(courseWork.getCourseId(), courseWork.getId()).execute();
              List<StudentSubmission> liststdList = studentSubmissionsResponse
                  .getStudentSubmissions();
              if (liststdList != null) {
                stdsublist.add(liststdList);
                studentSubmissionList.add(stdsublist);
                courseWorkList.add(courseWorkList1);
              } else {
                stdsublist.add(null);
                studentSubmissionList.add(stdsublist);
                courseWorkList.add(courseWorkList1);
              }
            }
          } else {
            courseWorkList.add(null);
          }
        }
      }

      setList(courseID);
      setList3(teacherList);
      setList2(stdlist);
      setList4(courseWorkList);
      setList5(studentSubmissionList);
      setList6(courses);

      return names;
    }


    public void setList(List<String> list) {
      listOfIDs = list;
    }

    public List<String> getListID() {
      return listOfIDs;
    }

    public void setList2(List<List<Student>> list) {
      listOfSTDIDs = list;
    }

    public List<List<Student>> getListSTDID() {
      return listOfSTDIDs;
    }

    public void setList3(List<List<Teacher>> list) {
      listOfTeacherIDs = list;
    }

    public List<List<Teacher>> getListTeacherID() {
      return listOfTeacherIDs;
    }

    public void setList4(List<List<CourseWork>> list) {
      listOfCourseWork = list;
    }

    public List<List<CourseWork>> getListOfCourseWork() {
      return listOfCourseWork;
    }

    public void setList5(List<List<List<StudentSubmission>>> list) {
      listOfStudentSubmission = list;
    }

    public List<List<List<StudentSubmission>>> getListOfStudentSubmission() {
      return listOfStudentSubmission;
    }

    public void setList6(List<Course> list) {
      listOfCourse = list;
    }

    public List<Course> getListOfCourse() {
      return listOfCourse;
    }

    @Override
    protected void onPreExecute() {
      mProgress.show();
    }

    @Override
    protected void onPostExecute(List<String> output) {

      DatabaseReference classroomLinkedAccountDBREF = FirebaseDatabase.getInstance().getReference()
          .child("Classroom_Linked_Account_General").
              child(FirebaseAuth.getInstance().getCurrentUser().getUid());
      DatabaseReference classListDBREF = FirebaseDatabase.getInstance().getReference()
          .child("Classroom_Class_List").
              child(FirebaseAuth.getInstance().getCurrentUser().getUid());
      List<String> listOfCourseNames = output;

      boolean toBreak = false;

      /*In case no classroom*/
      if (listOfCourse == null) {

        Map accountDetailsMap = new HashMap();
        accountDetailsMap.put("Gmail_Account", mCredential.getSelectedAccountName());
        accountDetailsMap.put("Status", "Connected\nNO CLASSROOMS");
        accountDetailsMap
            .put("Account_App_UID", FirebaseAuth.getInstance().getCurrentUser().getUid());

        classroomLinkedAccountDBREF.child("Account_Details")
            .updateChildren(accountDetailsMap, new DatabaseReference.CompletionListener() {
              @Override
              public void onComplete(DatabaseError databaseError,
                  DatabaseReference databaseReference) {
                if (databaseError != null) {
                  Log.d("Chat_Log", databaseError.getMessage().toString());
                }
              }
            });

        classroomLinkedAccountDBREF.child("Class_List").removeValue();
        classListDBREF.removeValue();

        //App stuff
        //Disable button
        accountTextView.setText(mCredential.getSelectedAccountName());
        accountTextView.setTextColor(Color.GREEN);
        statusTextView.setText("Connected");
        statusTextView.setTextColor(Color.GREEN);
        mCallApiButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Toast.makeText(ClassroomHomeSetting.this, "Already Connected", Toast.LENGTH_LONG)
                .show();
          }
        });
        mCallApiButton.setTextColor(Color.GREEN);
        mProgress.hide();
        Toast.makeText(ClassroomHomeSetting.this, "No Google Classrooms!.", Toast.LENGTH_LONG)
            .show();
      } else {

        /*Simple Account Link with Google*/
        Map accountDetailsMap = new HashMap();
        accountDetailsMap.put("Gmail_Account", mCredential.getSelectedAccountName());
        accountDetailsMap.put("Status", "Connected\nHAVE ClASSROOMS");
        accountDetailsMap
            .put("Account_App_UID", FirebaseAuth.getInstance().getCurrentUser().getUid());

        classroomLinkedAccountDBREF.child("Account_Details")
            .updateChildren(accountDetailsMap, new DatabaseReference.CompletionListener() {
              @Override
              public void onComplete(DatabaseError databaseError,
                  DatabaseReference databaseReference) {
                if (databaseError != null) {
                  Log.d("Chat_Log", databaseError.getMessage().toString());
                }
              }
            });
        /*Simple Account Link with Google - END*/

        classroomLinkedAccountDBREF.child("Class_List").removeValue();
        classListDBREF.removeValue();

        int counterNumberOfCourseCounter = 0;
        for (Course courseObject : listOfCourse) {
          DatabaseReference classroomLinkedAccountTeachersDBREF = FirebaseDatabase.getInstance()
              .getReference().child("Classroom_Linked_Account_Teachers")
              .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
          List<Teacher> teacherListPerCourse = listOfTeacherIDs.get(counterNumberOfCourseCounter);

            DatabaseReference classroomLinkedAccountDBREF = FirebaseDatabase.getInstance().getReference().child("Classroom_Linked_Account_General").
                    child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            DatabaseReference classListDBREF = FirebaseDatabase.getInstance().getReference().child("Classroom_Class_List_Teacher_Reference").
                    child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            final DatabaseReference classDetailsOnlyREF = FirebaseDatabase.getInstance().getReference().child("Classroom_List_General_Details");
            List<String> listOfCourseNames = output;

          //Toast.makeText(ClassroomHomeSetting.this, "Is teacher!.:" + teacherObject.getProfile().getEmailAddress(), Toast.LENGTH_LONG).show();

          if (teacherObject.getProfile().getEmailAddress() == null) {
            Map accountDetailsUpdateMap = new HashMap();
            accountDetailsUpdateMap.put("Gmail_Account", mCredential.getSelectedAccountName());
            accountDetailsUpdateMap.put("Status", "Connected\nNot Teachers ");
            accountDetailsUpdateMap
                .put("Account_App_UID", FirebaseAuth.getInstance().getCurrentUser().getUid());

            classroomLinkedAccountDBREF.child("Account_Details")
                .updateChildren(accountDetailsUpdateMap,
                    new DatabaseReference.CompletionListener() {
                      @Override
                      public void onComplete(DatabaseError databaseError,
                          DatabaseReference databaseReference) {
                        if (databaseError != null) {
                          Log.d("Chat_Log", databaseError.getMessage().toString());
                        }
                      }
                    });
          } else {
            Teacher teacherObject2 = (Teacher) teacherListPerCourse.get(0);

            Map classListDetailGeneralMap = new HashMap();
            classListDetailGeneralMap.put("Name_Course", courseObject.getName());
            classListDetailGeneralMap.put("Classroom_ClassID", courseObject.getId());
            classListDetailGeneralMap
                .put("Classroom_Teacher_UID", teacherObject2.getProfile().getEmailAddress());
            classListDetailGeneralMap
                .put("ABAS_UID", FirebaseAuth.getInstance().getCurrentUser().getUid());
            classListDetailGeneralMap.put("Section", courseObject.getSection());

            classroomLinkedAccountDBREF.child("Class_List").child(courseObject.getId())
                .updateChildren(classListDetailGeneralMap,
                    new DatabaseReference.CompletionListener() {
                      @Override
                      public void onComplete(DatabaseError databaseError,
                          DatabaseReference databaseReference) {
                        if (databaseError != null) {
                          Log.d("Chat_Log", databaseError.getMessage().toString());
                        }
                      }
                    });

            Map classListDetailIndepentdentMap = new HashMap();
            classListDetailIndepentdentMap.put("Name_Course", courseObject.getName());
            classListDetailIndepentdentMap.put("Classroom_ClassID", courseObject.getId());
            classListDetailIndepentdentMap
                .put("Classroom_Teacher_UID", teacherObject2.getProfile().getEmailAddress());
            classListDetailIndepentdentMap
                .put("ABAS_UID", FirebaseAuth.getInstance().getCurrentUser().getUid());
            classListDetailIndepentdentMap.put("Section", courseObject.getSection());

                classDetailsOnlyREF.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot uidSnap : dataSnapshot.getChildren()) {
                            boolean toNotBreak = true;
                            String UID = uidSnap.getKey().toString();
                            for (DataSnapshot uidSnap2 : uidSnap.getChildren()) {
                                if (uidSnap2.getKey().equals("ABAS_UID")) {
                                    if (uidSnap2.getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                        DatabaseReference removeClassroomGeneralDBref = FirebaseDatabase.getInstance().getReference().
                                                child("Classroom_List_General_Details");
                                        removeClassroomGeneralDBref.child(UID).removeValue();
                                    }
                                }
                            }
                        }

                        classDetailsOnlyREF.removeEventListener(this);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                SystemClock.sleep(400);

                //App stuff
                //Disable button
                accountTextView.setText(mCredential.getSelectedAccountName());
                accountTextView.setTextColor(Color.GREEN);
                statusTextView.setText("Connected");
                statusTextView.setTextColor(Color.GREEN);
                mCallApiButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(classRoomHomeSetting.this, "Already Connected", Toast.LENGTH_LONG).show();
                    }
                });
                mCallApiButton.setTextColor(Color.GREEN);
                mProgress.hide();
                Toast.makeText(classRoomHomeSetting.this, "No Google Classrooms!.", Toast.LENGTH_LONG).show();
            }

            else {

                /*Simple Account Link with Google*/
                Map accountDetailsMap = new HashMap();
                accountDetailsMap.put("Gmail_Account", mCredential.getSelectedAccountName());
                accountDetailsMap.put("Status", "Connected\nHAVE ClASSROOMS");
                accountDetailsMap.put("Account_App_UID", FirebaseAuth.getInstance().getCurrentUser().getUid());


                classroomLinkedAccountDBREF.child("Account_Details").updateChildren(accountDetailsMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                          Log.d("Chat_Log", databaseError.getMessage().toString());
                        }
                      }
                    });

            //Toast.makeText(ClassroomHomeSetting.this, "Is teacher!.:" + teacherObject2.getProfile().getEmailAddress(), Toast.LENGTH_LONG).show();
          }

                classDetailsOnlyREF.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot uidSnap : dataSnapshot.getChildren()) {
                            boolean toNotBreak = true;
                            String UID = uidSnap.getKey().toString();
                            for (DataSnapshot uidSnap2 : uidSnap.getChildren()) {
                                if (uidSnap2.getKey().equals("ABAS_UID")) {
                                    if (uidSnap2.getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                        DatabaseReference removeClassroomGeneralDBref = FirebaseDatabase.getInstance().getReference().
                                                child("Classroom_List_General_Details");
                                        removeClassroomGeneralDBref.child(UID).removeValue();
                                        toNotBreak = false;
                                    }
                                }
                            }
                        }
                        classDetailsOnlyREF.removeEventListener(this);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                SystemClock.sleep(400);

                int counterNumberOfCourseCounter = 0;
                for (Course courseObject : listOfCourse) {
                    DatabaseReference classroomLinkedAccountTeachersDBREF = FirebaseDatabase.getInstance().getReference().child("Classroom_Linked_Account_Teachers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    List<Teacher> teacherListPerCourse = listOfTeacherIDs.get(counterNumberOfCourseCounter);

      }
    }

    @Override
    protected void onCancelled() {
      mProgress.hide();
      if (mLastError != null) {
        if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
          showGooglePlayServicesAvailabilityErrorDialog(
              ((GooglePlayServicesAvailabilityIOException) mLastError)
                  .getConnectionStatusCode());
        } else if (mLastError instanceof UserRecoverableAuthIOException) {
          startActivityForResult(
              ((UserRecoverableAuthIOException) mLastError).getIntent(),
              ClassroomHomeSetting.REQUEST_AUTHORIZATION);
        } else {
          Toast.makeText(ClassroomHomeSetting.this, "The following error occurred:\n"
              + mLastError.getMessage(), Toast.LENGTH_LONG).show();
        }
      } else {
        Toast.makeText(ClassroomHomeSetting.this, "Request cancelled.", Toast.LENGTH_LONG).show();
      }
    }
  }

  public static class ClassroomHomeSettingHolder extends RecyclerView.ViewHolder {

                        classroomLinkedAccountDBREF.child("Account_Details").updateChildren(accountDetailsUpdateMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.d("Chat_Log", databaseError.getMessage().toString());
                                }
                            }
                        });
                    } else {
                        Teacher teacherObject2 = (Teacher) teacherListPerCourse.get(0);

                        Map classListDetailGeneralMap = new HashMap();
                        classListDetailGeneralMap.put("Name_Course", courseObject.getName());
                        classListDetailGeneralMap.put("Classroom_ClassID", courseObject.getId());
                        classListDetailGeneralMap.put("Classroom_Teacher_UID", teacherObject2.getProfile().getEmailAddress());
                        classListDetailGeneralMap.put("ABAS_UID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        classListDetailGeneralMap.put("Section", courseObject.getSection());

                        classroomLinkedAccountDBREF.child("Class_List").child(courseObject.getId()).updateChildren(classListDetailGeneralMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.d("Chat_Log", databaseError.getMessage().toString());
                                }
                            }
                        });


                        Map classListDetailIndepentdentMap = new HashMap();
                        classListDetailIndepentdentMap.put("Name_Course", courseObject.getName());
                        classListDetailIndepentdentMap.put("Classroom_ClassID", courseObject.getId());
                        classListDetailIndepentdentMap.put("Classroom_Teacher_UID", teacherObject2.getProfile().getEmailAddress());
                        classListDetailIndepentdentMap.put("ABAS_UID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        classListDetailIndepentdentMap.put("Section", courseObject.getSection());

                        classListDBREF.child(courseObject.getId()).updateChildren(classListDetailIndepentdentMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.d("Chat_Log", databaseError.getMessage().toString());
                                }
                            }
                        });


                        classDetailsOnlyREF.child(courseObject.getId()).updateChildren(classListDetailIndepentdentMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.d("Chat_Log", databaseError.getMessage().toString());
                                }
                            }
                        });

                    }

                    counterNumberOfCourseCounter++;
                }
                //App stuff
                //Disable button
                accountTextView.setText(mCredential.getSelectedAccountName());
                accountTextView.setTextColor(Color.GREEN);
                statusTextView.setText("Connected");
                statusTextView.setTextColor(Color.GREEN);
                mCallApiButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(classRoomHomeSetting.this, "Already Connected", Toast.LENGTH_LONG).show();
                    }
                });
                mCallApiButton.setTextColor(Color.GREEN);
                mProgress.hide();

            }
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            classRoomHomeSetting.REQUEST_AUTHORIZATION);
                } else {
                    Toast.makeText(classRoomHomeSetting.this, "The following error occurred:\n"
                            + mLastError.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(classRoomHomeSetting.this, "Request cancelled.", Toast.LENGTH_LONG).show();
            }
        }
    }

    //        public void setCourse_ID(String Course_ID){
//            coursenameTextView = mView.findViewById(R.id.activity_class_room_setting_recyclerview_item_classroom_name);
//            coursenameTextView.setText(Course_ID);
//        }
    public void setCourseName(String Name_Course) {
      coursenameTextView = mView
          .findViewById(R.id.activity_class_room_setting_recyclerview_item_classroom_name);
      coursenameTextView.setText(Name_Course);
    }

  }

  @Override
  public void onStart() {
    super.onStart();
    firebaseRecyclerAdapter.startListening();
  }

  @Override
  public void onStop() {
    super.onStop();
    firebaseRecyclerAdapter.stopListening();
  }
}
