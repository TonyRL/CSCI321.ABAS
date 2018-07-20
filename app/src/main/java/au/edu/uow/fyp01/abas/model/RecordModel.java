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

  //<editor-fold desc="date setter and getter">
  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }
  //</editor-fold>

  //<editor-fold desc="order setter and getter">
  public Long getOrder() {
    return timestamp;
  }

  public void setOrder(Long timestamp) {
    this.timestamp = timestamp;
  }
  //</editor-fold>

  //<editor-fold desc="grade setter and getter">
  public String getGrade() {
    return grade;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }
  //</editor-fold>

  public String getRecordID() {
    return recordID;
  }

  public void setRecordID(String recordID) {
    this.recordID = recordID;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setGradename(String gradename) {
    this.gradename = gradename;
  }

  public String getGradename() {
    return gradename;
  }
}
