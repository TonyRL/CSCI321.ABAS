package au.edu.uow.fyp01.abas.Model;

/**
 * Created by Athens on 2018/04/27.
 */

public class UserModel {

  public String email;
  public String fullname;
  public String schID;
  public String title;

  public UserModel() {

  }

  public UserModel(String email, String fullname, String schID, String title) {
    this.email = email;
    this.fullname = fullname;
    this.schID = schID;
    this.title = title;
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

}
