package au.edu.uow.fyp01.abas.Model;

/**
 * Created by Athens on 2018/04/21.
 */

public class StudentModel {

  public String classname;
  public String classnumber;
  public String firstname;
  public String lastname;
  public String sid;

  public StudentModel() {
  }

  public StudentModel(String classname, String classnumber, String firstname,
      String lastname, String sid) {
    this.classname = classname;
    this.classnumber = classnumber;
    this.firstname = firstname;
    this.lastname = lastname;
    this.sid = sid;
  }

  //<editor-fold desc="classname setter and getter">
  public void setClassname(String classname) {
    this.classname = classname;
  }

  public String getClassname() {
    return classname;
  }
  //</editor-fold>

  //<editor-fold desc="classnumber setter and getter">
  public void setClassnumber(String classnumber) {
    this.classnumber = classnumber;
  }

  public String getClassnumber() {
    return classnumber;
  }
  //</editor-fold>

  //<editor-fold desc="firstname setter and getter">
  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getFirstname() {
    return firstname;
  }
  //</editor-fold>

  //<editor-fold desc="lastname setter and getter">
  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getLastname() {
    return lastname;
  }
  //</editor-fold>

  //<editor-fold desc="sID setter and getter">
  public void setSid(String sID) {
    this.sid = sID;
  }

  public String getSid() {
    return sid;
  }
  //</editor-fold>
}
