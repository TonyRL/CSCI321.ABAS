package au.edu.uow.fyp01.abas.model;

public class SchoolInfoModel {

  public String schID;
  public String schoolname;
  public String title;

  public SchoolInfoModel() {
  }

  public SchoolInfoModel(String schID, String schoolname) {
    this.schID = schID;
    this.schoolname = schoolname;
  }

  /**
   * Get the school ID
   *
   * @return the school ID
   */
  public String getSchID() {
    return schID;
  }

  /**
   * Set the school ID
   *
   * @param schID the school ID
   */
  public void setSchID(String schID) {
    this.schID = schID;
  }

  /**
   * Get the school name
   *
   * @return the school name
   */
  public String getSchoolname() {
    return schoolname;
  }

  /**
   * Set   the school name
   *
   * @param schoolname the school name
   */
  public void setSchoolname(String schoolname) {
    this.schoolname = schoolname;
  }
}
