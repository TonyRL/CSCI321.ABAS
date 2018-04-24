package au.edu.uow.fyp01.abas.Model;

/**
 * Created by Athens on 2018/04/21.
 */

public class RecordModel {
    String grade;
    String date;
    String order;

    public RecordModel() {}

    public RecordModel(String grade, String date, String order) {
        this.grade = grade;
        this.date = date;
        this.order = order;
    }


    //<editor-fold desc="date setter and getter">
    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
    //</editor-fold>


    //<editor-fold desc="order setter and getter">
    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrder() {
        return order;
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
