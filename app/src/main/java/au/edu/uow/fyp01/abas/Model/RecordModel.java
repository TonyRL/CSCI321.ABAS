package au.edu.uow.fyp01.abas.Model;

/**
 * Created by Athens on 2018/04/21.
 */

public class RecordModel {
    String chinese;
    String date;
    String english;
    String math;

    public RecordModel() {}

    //add more subjects if needed
    //also add subjects in database!
    public RecordModel(String chinese, String date, String english, String math) {
        this.chinese = chinese;
        this.date = date;
        this.english = english;
        this.math = math;
    }

    //<editor-fold desc="chinese setter and getter">
    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getChinese() {
        return chinese;
    }
    //</editor-fold>

    //<editor-fold desc="date setter and getter">
    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
    //</editor-fold>

    //<editor-fold desc="english setter and getter">
    public void setEnglish(String english) {
        this.english = english;
    }

    public String getEnglish() {
        return english;
    }
    //</editor-fold>

    //<editor-fold desc="math setter and getter">
    public void setMath(String math) {
        this.math = math;
    }

    public String getMath() {
        return math;
    }
    //</editor-fold>
}
