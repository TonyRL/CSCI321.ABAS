package au.edu.uow.fyp01.abas.model;

/**
 * Created by Athens on 2018/04/21.
 */

public class RecordModel {

  String grade;
  String date;
  Long timestamp;
  String recordID;
  String type;
  String gradename;

  public RecordModel() {
  }

  public RecordModel(String grade, String date, Long timestamp, String recordID) {
    this.grade = grade;
    this.date = date;
    this.timestamp = timestamp;
    this.recordID = recordID;
  }

  /**
   * Get grade record date
   *
   * @return grade record date
   */
  //<editor-fold desc="date setter and getter">
  public String getDate() {
    return date;
  }

  /**
   * Set grade record date
   *
   * @param date grade record date
   */
  public void setDate(String date) {
    this.date = date;
  }
  //</editor-fold>

  /**
   * Get grade
   *
   * @return grade
   */
  //<editor-fold desc="grade setter and getter">
  public String getGrade() {
    return grade;
  }

  /**
   * Set grade
   *
   * @param grade grade
   */
  public void setGrade(String grade) {
    this.grade = grade;
  }
  //</editor-fold>

  /**
   * Get grade ID
   *
   * @return grade ID
   */
  public String getRecordID() {
    return recordID;
  }

  /**
   * Set grade ID
   *
   * @param recordID grade ID
   */
  public void setRecordID(String recordID) {
    this.recordID = recordID;
  }


  /**
   * Get grade record time
   *
   * @return grade record time
   */
  public Long getTimestamp() {
    return timestamp;
  }


  /**
   * Set grade record time
   *
   * @param timestamp grade record time
   */
  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Get grade type
   *
   * @return grade type
   */
  public String getType() {
    return type;
  }

  /**
   * Set  grade type
   *
   * @param type grade type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Get grade name
   *
   * @return grade name
   */
  public String getGradename() {
    return gradename;
  }

  /**
   * Set  grade name
   *
   * @param gradename grade name
   */
  public void setGradename(String gradename) {
    this.gradename = gradename;
  }
}
