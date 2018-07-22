package au.edu.uow.fyp01.abas.model;

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

  /**
   * Get the class name
   *
   * @return class name
   */
  //<editor-fold desc="classname setter and getter">
  public String getClassname() {
    return classname;
  }

  /**
   * Set the class name
   *
   * @param classname class name
   */
  public void setClassname(String classname) {
    this.classname = classname;
  }
  //</editor-fold>

  /**
   * Get class number
   *
   * @return class number
   */
  //<editor-fold desc="classnumber setter and getter">
  public String getClassnumber() {
    return classnumber;
  }

  /**
   * Set class number
   *
   * @param classnumber class number
   */
  public void setClassnumber(String classnumber) {
    this.classnumber = classnumber;
  }
  //</editor-fold>

  /**
   * Get student's first name
   *
   * @return student's first name
   */
  //<editor-fold desc="firstname setter and getter">
  public String getFirstname() {
    return firstname;
  }

  /**
   * Set student's first name
   *
   * @param firstname student's first name
   */
  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }
  //</editor-fold>

  /**
   * Get student's last name
   *
   * @return student's last name
   */
  //<editor-fold desc="lastname setter and getter">
  public String getLastname() {
    return lastname;
  }

  /**
   * Set  student's last name
   *
   * @param lastname student's last name
   */
  public void setLastname(String lastname) {
    this.lastname = lastname;
  }
  //</editor-fold>

  /**
   * Get student ID
   *
   * @return student ID
   */
  //<editor-fold desc="sID setter and getter">
  public String getSid() {
    return sid;
  }

  /**
   * Set student ID
   *
   * @param sID student ID
   */
  public void setSid(String sID) {
    this.sid = sID;
  }
  //</editor-fold>
}
