package au.edu.uow.fyp01.abas.Model;

/**
 * Created by Athens on 2018/04/21.
 */

public class StudentModel {

  public String classname;
  public String classnumber;
  public String firstname;
  public String lastname;
  public String sID;

  public StudentModel() {
  }

  public StudentModel(String classname, String classnumber, String firstname,
      String lastname, String sid) {
    this.classname = classname;
    this.classnumber = classnumber;
    this.firstname = firstname;
    this.lastname = lastname;
    this.sID = sid;
  }

  //<editor-fold desc="classname setter and getter">
  public String getClassname() {
    return classname;
  }

  public void setClassname(String classname) {
    this.classname = classname;
  }
  //</editor-fold>

  //<editor-fold desc="classnumber setter and getter">
  public String getClassnumber() {
    return classnumber;
  }

  public void setClassnumber(String classnumber) {
    this.classnumber = classnumber;
  }
  //</editor-fold>

  //<editor-fold desc="firstname setter and getter">
  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }
  //</editor-fold>

  //<editor-fold desc="lastname setter and getter">
  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }
  //</editor-fold>

  //<editor-fold desc="sID setter and getter">
  public String getSid() {
    return sID;
  }

  public void setSid(String sID) {
    this.sID = sID;
  }
  //</editor-fold>
}
