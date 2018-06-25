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
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.api.services.classroom.model.ListCoursesResponse;
import com.google.api.services.classroom.model.ListStudentsResponse;
import com.google.api.services.classroom.model.ListTeachersResponse;
import com.google.api.services.classroom.model.Student;
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

import au.edu.uow.fyp01.abas.R;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class classRoomHomeSetting extends Activity implements EasyPermissions.PermissionCallbacks {

    GoogleAccountCredential mCredential;
    private TextView accountTextView;
    private TextView statusTextView;
    private Button mCallApiButton;
    private Button reconnectButton;
    ProgressDialog mProgress;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Connect to Classroom";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {ClassroomScopes.CLASSROOM_COURSEWORK_STUDENTS,ClassroomScopes.CLASSROOM_COURSEWORK_ME,
            ClassroomScopes.CLASSROOM_ANNOUNCEMENTS,ClassroomScopes.CLASSROOM_ROSTERS,ClassroomScopes.CLASSROOM_COURSES,ClassroomScopes.CLASSROOM_GUARDIANLINKS_STUDENTS,
            ClassroomScopes.CLASSROOM_PROFILE_EMAILS,ClassroomScopes.CLASSROOM_PROFILE_PHOTOS};

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
        //recyclerView = findViewById(R.id.classRoomHomeSetting_Recycler_view);
        accountTextView = findViewById(R.id.class_room_home_gmail_textview);
        accountTextView.setText("N/A (Account)");
        accountTextView.setTextColor(Color.BLACK);
        statusTextView = findViewById(R.id.class_room_home_status_textview);
        statusTextView.setText("N/A (Status)");
        statusTextView.setTextColor(Color.BLACK);
        mCallApiButton.setText(BUTTON_TEXT);


        mCallApiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize credentials and service object.
                mCredential = GoogleAccountCredential.usingOAuth2(
                        classRoomHomeSetting.this, Arrays.asList(SCOPES))
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
                        classRoomHomeSetting.this, Arrays.asList(SCOPES))
                        .setBackOff(new ExponentialBackOff());
                reconnectButton.setEnabled(false);
                getResultsFromApi();
                reconnectButton.setTextColor(Color.RED);
                reconnectButton.setEnabled(true);
            }
        });


        DatabaseReference setTextRefAccount = FirebaseDatabase.getInstance().getReference();


        //check
        setTextRefAccount.child("Classroom_Link_Account").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()) {
                        String userName = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Account_Details").child("Gmail_Address").getValue().toString();
                        String status = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Account_Details").child("Status").getValue().toString();
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
                        Toast.makeText(classRoomHomeSetting.this, "Account->nope".toString(), Toast.LENGTH_SHORT).show();
//

                    }
                } else {
                    Toast.makeText(classRoomHomeSetting.this, "Empty".toString(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Classroom API ...");


    }

    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            Toast.makeText(classRoomHomeSetting.this, "No network connection available.", Toast.LENGTH_LONG).show();
        } else {
            new MakeRequestTask(mCredential).execute();
            //Store name
            //Toast.makeText(classRoomHomeSetting.this, mCredential.getSelectedAccountName(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.GET_ACCOUNTS)) {
            startActivityForResult(mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
            startActivityForResult(mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode  code indicating the result of the incoming
     *                    activity result.
     * @param data        Intent (containing result data) returned by incoming
     *                    activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(classRoomHomeSetting.this,
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.", Toast.LENGTH_LONG).show();
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    Toast.makeText(classRoomHomeSetting.this, "FIRST:" + AccountManager.KEY_ACCOUNT_NAME, Toast.LENGTH_SHORT).show();
                    if (accountName != null) {
                        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        Toast.makeText(classRoomHomeSetting.this, "FIRST:" + PREF_ACCOUNT_NAME, Toast.LENGTH_SHORT).show();
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        Toast.makeText(classRoomHomeSetting.this, "FIRST:" + accountName, Toast.LENGTH_SHORT).show();
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
     * @param requestCode  The request code passed in
     *                     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     *
     * @param requestCode The request code associated with the requested
     *                    permission
     * @param list        The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     *
     * @param requestCode The request code associated with the requested
     *                    permission
     * @param list        The requested permission list. Never null.
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
     * @return true if Google Play Services is available and up to
     * date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
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
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     *
     * @param connectionStatusCode code describing the presence (or lack of)
     *                             Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                classRoomHomeSetting.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * An asynchronous task that handles the Classroom API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.classroom.Classroom mService = null;
        private Exception mLastError = null;
        private List<String> listOfIDs = new ArrayList<>();
        private List<List<Student>> listOfSTDIDs = new ArrayList<>();
        private List<List<Teacher>> listOfTeacherIDs = new ArrayList<>();

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.classroom.Classroom.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("ABAS")
                    .build();
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
         * @return List course names, or a simple error message if no courses are
         * found.
         * @throws IOException
         */
        private List<String> getDataFromApi() throws IOException {
            ListCoursesResponse response = mService.courses().list().execute();



            List<Course> courses = response.getCourses();
            List<String> names = new ArrayList<String>();
            List<String> courseID = new ArrayList<String>();
            List <List<Student>> stdlist = new ArrayList<List<Student>>();
            List <List<Teacher>> teacherList = new ArrayList<List<Teacher>>();

            if (courses != null) {
                for (Course course : courses) {
                    names.add(course.getName());
                    courseID.add(course.getId());

                    ListStudentsResponse studentsResponse = mService.courses().students().list(course.getId()).execute();
                    List<Student> studentList = studentsResponse.getStudents();
                    stdlist.add(studentList);

                    ListTeachersResponse teachersResponse = mService.courses().teachers().list(course.getId()).execute();
                    List<Teacher> teacherList1 = teachersResponse.getTeachers();
                    teacherList.add(teacherList1);
                }
            }
            setList(courseID);
            setList2(stdlist);
            setList3(teacherList);
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


        @Override
        protected void onPreExecute() {
            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            if (output == null || output.size() == 0) {
                Toast.makeText(classRoomHomeSetting.this, "No results returned.", Toast.LENGTH_LONG).show();
                mProgress.hide();
            } else {
                //KEYYYYY -> success
                List<String> output2;
                output2 = getListID();
                List <List<Teacher>> outputTeacher;
                outputTeacher = getListTeacherID();

                if (output2 != null && output2.size() > 0) {

                    DatabaseReference dbRef_to_push = FirebaseDatabase.getInstance()
                            .getReference().child("Classroom_Link_Account").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    Map mapDetails = new HashMap();

                    mapDetails.put("Gmail_Address", mCredential.getSelectedAccountName());
                    mapDetails.put("Status", "Connected");
                    mapDetails.put("Account_App_UID", FirebaseAuth.getInstance().getCurrentUser().getUid());


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


                    dbRef_to_push.child("Account_Details").updateChildren(mapDetails, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Log.d("Chat_Log", databaseError.getMessage().toString());
                            }
                        }
                    });



                    int counter = 0;

                    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Google_Classroom_List");

                    dbRef_to_push.child("Class_List").removeValue();

                    for(String id:output2){
                        String name;
                        name = output.get(counter);


                        List<Teacher> list1 = listOfTeacherIDs.get(counter);
                        Iterator teacheritr = list1.iterator();
                        Teacher teacher = new Teacher();
                        while(teacheritr.hasNext()){
                            Teacher temp = (Teacher) teacheritr.next();
                            if(temp.getProfile().getEmailAddress().equals(mCredential.getSelectedAccountName())){
                                Map mapToPush = new HashMap();
                                mapToPush.put("Name_Course", name);
                                mapToPush.put("Course_ID", id);
                                mapToPush.put("Gmail_Account", mCredential.getSelectedAccountName());
                                mapToPush.put("Status", "Teacher");
                                mapToPush.put("Classroom_ID", temp.getProfile().getId());

                                dbRef_to_push.child("Class_List").child(id).updateChildren(mapToPush, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError != null) {
                                            Log.d("Chat_Log", databaseError.getMessage().toString());
                                        }
                                        Toast.makeText(classRoomHomeSetting.this, "Sent!", Toast.LENGTH_LONG).show();
                                    }
                                });



                                List <Student> list = listOfSTDIDs.get(counter);

                                dbref = dbref.child(id);
                                Iterator itr = list.iterator();
                                while(itr.hasNext()){
                                    Student std = (Student)itr.next();

                                    Map mapToPushStudent = new HashMap();
                                    mapToPushStudent.put("Classroom_ID", std.getUserId());
                                    mapToPushStudent.put("Gmail",std.getProfile().getEmailAddress());
                                    mapToPushStudent.put("Fullname",std.getProfile().getName().getFullName());
                                    mapToPushStudent.put("Account_App_UID", "null");
                                    mapToPushStudent.put("Status","Student");

                                    dbref.child(std.getUserId()).updateChildren(mapToPushStudent, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if(databaseError!=null){
                                                Log.d("Chat_log", databaseError.getMessage().toString());
                                            }
                                        }
                                    });
                                }


                                counter++;
                            }
                        }


                    }
                }
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
}
