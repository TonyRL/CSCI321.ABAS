package au.edu.uow.fyp01.abas.Model;

public class SchoolInfoModel {
    public String schID;
    public String schoolname;

    public SchoolInfoModel(){}

    public SchoolInfoModel(String schID, String schoolname){
        this.schID = schID;
        this.schoolname = schoolname;
    }

    public void setSchID(String schID) {
        this.schID = schID;
    }

    public String getSchID() {
        return schID;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public String getSchoolname() {
        return schoolname;
    }
}
