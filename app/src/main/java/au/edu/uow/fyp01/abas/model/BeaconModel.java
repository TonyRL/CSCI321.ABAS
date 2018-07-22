package au.edu.uow.fyp01.abas.model;

/**
 * Created by Athens on 2018/06/10.
 */

public class BeaconModel {

  String beaconID;
  String classID;
  String schID;
  String sid;

  public BeaconModel() {
  }

  public BeaconModel(String beaconID, String classID, String schID, String sid) {
    this.beaconID = beaconID;
    this.classID = classID;
    this.schID = schID;
    this.sid = sid;
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

  /**
   * Get school ID
   *
   * @return school ID
   */
  public String getSchID() {
    return schID;
  }

  /**
   * Set school ID
   *
   * @param schID school ID
   */
  public void setSchID(String schID) {
    this.schID = schID;
  }

  /**
   * Get class ID
   *
   * @return class ID
   */
  public String getClassID() {
    return classID;
  }

  /**
   * Set class ID
   *
   * @param classID class ID
   */
  public void setClassID(String classID) {
    this.classID = classID;
  }

  /**
   * Get beacon ID
   *
   * @return beacon ID
   */
  public String getBeaconID() {
    return beaconID;
  }

  /**
   * Set beacon ID
   *
   * @param beaconID beacon ID
   */
  public void setBeaconID(String beaconID) {
    this.beaconID = beaconID;
  }

}
