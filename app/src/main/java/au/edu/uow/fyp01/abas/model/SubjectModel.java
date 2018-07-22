package au.edu.uow.fyp01.abas.model;

/**
 * Created by Athens on 2018/04/30.
 */

public class SubjectModel {

  public String subjectname;
  public String subjectID;

  public SubjectModel() {
  }

  public SubjectModel(String subjectname, String subjectID) {
    this.subjectname = subjectname;
    this.subjectID = subjectID;
  }

  /**
   * Get subject ID
   * @return subject ID
   */
  public String getSubjectID() {
    return subjectID;
  }

  /**
   * Set subject ID
   * @param subjectID subject ID
   */
  public void setSubjectID(String subjectID) {
    this.subjectID = subjectID;
  }

  /**
   * Get subject name
   * @return
   */
  public String getSubjectname() {
    return subjectname;
  }

  /**
   * Set subject name
   * @param subjectname subject name
   */
  public void setSubjectname(String subjectname) {
    this.subjectname = subjectname;
  }
}
