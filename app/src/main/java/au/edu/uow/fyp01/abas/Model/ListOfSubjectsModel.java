package au.edu.uow.fyp01.abas.Model;

/**
 * Created by Athens on 2018/04/30.
 */

public class ListOfSubjectsModel {

  public String subjectname;
  public String subjectID;

  public ListOfSubjectsModel() {
  }

  public ListOfSubjectsModel(String subjectname, String subjectID) {
    this.subjectname = subjectname;
    this.subjectID = subjectID;
  }

  public void setSubjectID(String subjectID) {
    this.subjectID = subjectID;
  }

  public String getSubjectID() {
    return subjectID;
  }

  public void setSubjectname(String subjectname) {
    this.subjectname = subjectname;
  }

  public String getSubjectname() {
    return subjectname;
  }
}
