package au.edu.uow.fyp01.abas.model;

/**
 * Created by Athens on 2018/04/21.
 */

public class SchoolModel {

  public String classID;
  public String classname;

  public SchoolModel() {
  }

  public SchoolModel(String classID, String classname) {
    this.classID = classID;
    this.classname = classname;
  }

  //<editor-fold desc="classID setter and getter">
  public String getClassID() {
    return classID;
  }

  public void setClassID(String classID) {
    this.classID = classID;
  }
  //</editor-fold>

  //<editor-fold desc="classname setter and getter">
  public void setClassname() {
    this.classname = classname;
  }

  public String getClassname() {
    return classname;
  }
  //</editor-fold>
}
