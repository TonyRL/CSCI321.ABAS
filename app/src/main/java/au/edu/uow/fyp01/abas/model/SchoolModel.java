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

  /**
   * Get the class ID
   *
   * @return class ID
   */
  //<editor-fold desc="classID setter and getter">
  public String getClassID() {
    return classID;
  }

  /**
   * Set the class ID
   *
   * @param classID the class ID
   */
  public void setClassID(String classID) {
    this.classID = classID;
  }
  //</editor-fold>

  /**
   * Set the class name
   *
   * @param classname class name
   */
  //<editor-fold desc="classname setter and getter">
  public void setClassname(String classname) {
    this.classname = classname;
  }

  /**
   * Get  the class name
   *
   * @return the class name
   */
  public String getClassname() {
    return classname;
  }
  //</editor-fold>
}
