package au.edu.uow.fyp01.abas.activity;

/**
 * Created by Manish on 27/06/2018.
 */

public class ClassroomHomeSettingRecyclerClass {

  private String Classroom_ID;
  private String Course_ID;
  private String Name_Course;
  private String Gmail_Account;
  private String Status;

  public ClassroomHomeSettingRecyclerClass() {

  }

  //String classroom_ID, String course_ID,, String gmail_Account, String status
  public ClassroomHomeSettingRecyclerClass(String name_Course) {
//        Classroom_ID = classroom_ID;
//        Course_ID = course_ID;
    Name_Course = name_Course;
//        Gmail_Account = gmail_Account;
//        Status = status;
  }

//    public String getClassroom_ID() {
//        return Classroom_ID;
//    }
//
//    public void setClassroom_ID(String classroom_ID) {
//        Classroom_ID = classroom_ID;
//    }
//
//    public String getCourse_ID() {
//        return Course_ID;
//    }
//
//    public void setCourse_ID(String course_ID) {
//        Course_ID = course_ID;
//    }

  public String getName_Course() {
    return Name_Course;
  }

  public void setName_Course(String name_Course) {
    Name_Course = name_Course;
  }

//    public String getGmail_Account() {
//        return Gmail_Account;
//    }
//
//    public void setGmail_Account(String gmail_Account) {
//        Gmail_Account = gmail_Account;
//    }
//
//    public String getStatus() {
//        return Status;
//    }
//
//    public void setStatus(String status) {
//        Status = status;
//    }
}
