package au.edu.uow.fyp01.abas.Activity;

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


    public RecordOverActivityClassroomMatchRecyclerClass() {

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
