package au.edu.uow.fyp01.abas.Model;

/**
 * Created by Athens on 2018/04/27.
 */

public class UserModel {

  public String email;
  public String fullname;
  public String schID;
  public String staffID;
  public String status;
  public String title;
  public String usertype;


  public UserModel() {

  }

  public UserModel(String email, String fullname, String schID, String staffID, String status,
      String title, String usertype) {
    this.email = email;
    this.fullname = fullname;
    this.schID = schID;
    this.staffID = staffID;
    this.status = status;
    this.title = title;
    this.usertype = usertype;

  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  public String getFullname() {
    return fullname;
  }

  public void setSchID(String schID) {
    this.schID = schID;
  }

  public String getSchID() {
    return schID;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public void setUsertype(String usertype) {
    this.usertype = usertype;
  }

  public String getUsertype() {
    return usertype;
  }

  public void setStaffID(String staffID) {
    this.staffID = staffID;
  }

  public String getStaffID() {
    return staffID;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
