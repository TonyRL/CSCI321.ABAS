package au.edu.uow.fyp01.abas.model;

public class RequestListModel {

  public String fullname;
  public String title;
  public String userID;
  public String staffID;

  public RequestListModel() {}

  public RequestListModel(String fullname,
      String title, String userID, String staffID){

    this.fullname = fullname;
    this.title = title;
    this.userID = userID;
    this.staffID = staffID;

  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  public String getFullname() {
    return fullname;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

  public String getUserID() {
    return userID;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public void setStaffID(String staffID) {
    this.staffID = staffID;
  }

  public String getStaffID() {
    return staffID;
  }
}
