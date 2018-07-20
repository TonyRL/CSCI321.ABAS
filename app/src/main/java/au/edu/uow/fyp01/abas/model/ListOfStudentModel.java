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

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setSid(String sid) {
    this.sid = sid;
  }

  public String getSid() {
    return sid;
  }
}
