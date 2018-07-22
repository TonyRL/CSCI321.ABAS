package au.edu.uow.fyp01.abas.model;

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

  /**
   * Get the user email
   *
   * @return user email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Set user email
   *
   * @param email user email
   */
  public void setEmail(String email) {
    this.email = email;
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
   * Get user's school ID
   *
   * @return user's school ID
   */
  public String getSchID() {
    return schID;
  }

  /**
   * Set user's school ID
   *
   * @param schID user's school ID
   */
  public void setSchID(String schID) {
    this.schID = schID;
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
   * Get user's account type
   *
   * @return user's account type
   */
  public String getUsertype() {
    return usertype;
  }

  /**
   * Set user's account type
   *
   * @param usertype user's account type
   */
  public void setUsertype(String usertype) {
    this.usertype = usertype;
  }

  /**
   * Get
   */
  public String getStaffID() {
    return staffID;
  }

  public void setStaffID(String staffID) {
    this.staffID = staffID;
  }

  /**
   * Get user's staff ID
   *
   * @return user's staff ID
   */
  public String getStatus() {
    return status;
  }

  /**
   * Set user's staff ID
   *
   * @param status user's staff ID
   */
  public void setStatus(String status) {
    this.status = status;
  }
}
