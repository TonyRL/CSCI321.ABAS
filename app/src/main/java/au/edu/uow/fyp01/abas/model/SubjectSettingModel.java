package au.edu.uow.fyp01.abas.model;

public class SubjectSettingModel {

  public String assignmentratio;
  public String quizratio;
  public String testratio;
  public String examratio;

  public SubjectSettingModel() {

  }

  public SubjectSettingModel(String assignmentratio, String quizratio, String testratio,
      String examratio) {
    this.assignmentratio = assignmentratio;
    this.quizratio = quizratio;
    this.testratio = testratio;
    this.examratio = examratio;
  }

  /**
   * Get assignment ratio
   *
   * @return assignment ratio
   */
  public String getAssignmentratio() {
    return assignmentratio;
  }

  /**
   * Set assignment ratio
   *
   * @param assignmentratio assignment ratio
   */
  public void setAssignmentratio(String assignmentratio) {
    this.assignmentratio = assignmentratio;
  }

  /**
   * Get quiz ratio
   *
   * @return quiz ratio
   */
  public String getQuizratio() {
    return quizratio;
  }

  /**
   * Set quiz ratio
   *
   * @param quizratio quiz ratio
   */
  public void setQuizratio(String quizratio) {
    this.quizratio = quizratio;
  }

  /**
   * Get test ratio
   *
   * @return test ratio
   */
  public String getTestratio() {
    return testratio;
  }

  /**
   * Set  test ratio
   *
   * @param testratio test ratio
   */
  public void setTestratio(String testratio) {
    this.testratio = testratio;
  }

  /**
   * Get exam ratio
   *
   * @return exam ratio
   */
  public String getExamratio() {
    return examratio;
  }

  /**
   * Set exam ratio
   *
   * @param examratio exam ratio
   */
  public void setExamratio(String examratio) {
    this.examratio = examratio;
  }
}
