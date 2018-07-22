package au.edu.uow.fyp01.abas.model;

/**
 * Created by Athens on 2018/05/21.
 */

public class ListOfStudentModel {

  public String firstname;
  public String lastname;
  public String sid;

  public ListOfStudentModel() {

  }

  public ListOfStudentModel(String firstname, String lastname, String sid) {
    this.firstname = firstname;
    this.lastname = lastname;
    this.sid = sid;
  }

  /**
   * Get student first name
   *
   * @return student first name
   */
  public String getFirstname() {
    return firstname;
  }

  /**
   * Set  student first name
   *
   * @param firstname student first name
   */
  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  /**
   * Get student last name
   *
   * @return student last name
   */
  public String getLastname() {
    return lastname;
  }

  /**
   * Set  student last name
   *
   * @param lastname student last name
   */
  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  /**
   * Get student ID
   *
   * @return student ID
   */
  public String getSid() {
    return sid;
  }

  /**
   * Set student ID
   *
   * @param sid student ID
   */
  public void setSid(String sid) {
    this.sid = sid;
  }
}
