package au.edu.uow.fyp01.abas.module.record;

/**
 * Created by Manish on 17/07/2018.
 */

public class RecordOverActivityClassroomMatchRecyclerClass {

  //coursework name
  //gmailaccount of student
  //Assignment Name
  //Full Score -> grade
  private String Coursework_Name;
  private String Gmail_Account;
  private String Grade;
  private String Classroom_Submission_ID;

  public RecordOverActivityClassroomMatchRecyclerClass() {

  }

  public String getClassroom_Submission_ID() {
    return Classroom_Submission_ID;
  }

  public void setClassroom_Submission_ID(String classroom_Submission_ID) {
    Classroom_Submission_ID = classroom_Submission_ID;
  }

  public String getCoursework_Name() {
    return Coursework_Name;
  }

  public void setCoursework_Name(String coursework_Name) {
    Coursework_Name = coursework_Name;
  }

  public String getGmail_Account() {
    return Gmail_Account;
  }

  public void setGmail_Account(String gmail_Account) {
    Gmail_Account = gmail_Account;
  }

  public String getGrade() {
    return Grade;
  }

  public void setGrade(String grade) {
    Grade = grade;
  }
}
