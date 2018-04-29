package au.edu.uow.fyp01.abas.Model;

/**
 * Created by Athens on 2018/04/21.
 */

public class RecordModel {

  String grade;
  String date;
  Long timestamp;

  public RecordModel() {
  }

  public RecordModel(String grade, String date, Long timestamp) {
    this.grade = grade;
    this.date = date;
    this.timestamp = timestamp;
  }


  //<editor-fold desc="date setter and getter">
  public void setDate(String date) {
    this.date = date;
  }

  public String getDate() {
    return date;
  }
  //</editor-fold>


  //<editor-fold desc="grade setter and getter">
  public void setGrade(String grade) {
    this.grade = grade;
  }

  public String getGrade() {
    return grade;
  }
  //</editor-fold>
}
