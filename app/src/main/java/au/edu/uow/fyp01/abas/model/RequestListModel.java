package au.edu.uow.fyp01.abas.model;

public class RequestListModel {

  public String fullname;
  public String title;
  public String userID;
  public String staffID;

  public RequestListModel() {
  }

  public RequestListModel(String fullname, String title, String userID, String staffID) {

    this.fullname = fullname;
    this.title = title;
    this.userID = userID;
    this.staffID = staffID;

  }

  /**
   * Get user's full name
   *
   * @return user's full name
   */
  public String getFullname() {
    return fullname;
  }

  /**
   * Set user's full name
   *
   * @param fullname user's full name
   */
  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  /**
   * Get user ID
   *
   * @return user ID
   */
  public String getUserID() {
    return userID;
  }

  /**
   * Set user ID
   *
   * @param userID user ID
   */
  public void setUserID(String userID) {
    this.userID = userID;
  }

  /**
   * Get user's title
   *
   * @return user's title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Set user's title
   *
   * @param title user's title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Get user's staff ID
   *
   * @return user's staff ID
   */
  public String getStaffID() {
    return staffID;
  }

  /**
   * Set user's staff ID
   *
   * @param staffID user's staff ID
   */
  public void setStaffID(String staffID) {
    this.staffID = staffID;
  }
}
