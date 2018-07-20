package au.edu.uow.fyp01.abas.activity;

/**
 * Created by Manish on 16/05/2018.
 */

public class FileSharingAddUserRetrieveClass {

  //    private String UID;
  private String email;
  private String fullname;

  public FileSharingAddUserRetrieveClass() {

  }

  public FileSharingAddUserRetrieveClass(String fullname, String email) {
//         this.UID = UID;
    this.email = email;
    this.fullname = fullname;
  }

  //    public String getUID() {
//        return UID;
//    }
//
//    public void setUID(String UID) {
//        this.UID = UID;
//    }
//
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFullName() {
    return fullname;
  }

  public void setFullName(String fullname) {

    this.fullname = fullname;
  }
}
