package au.edu.uow.fyp01.abas.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SubjectSettingModelTest {

  private SubjectSettingModel subjectSettingModel;

  @Before
  public void setUp() {
    subjectSettingModel = new SubjectSettingModel("0.1", "0.2", "0.3", "0.4");
  }

  @After
  public void tearDown() {
    subjectSettingModel = null;
  }

  @Test
  public void testGetAssignmentratio() {
    String expected = "0.1";
    String actual = subjectSettingModel.getAssignmentratio();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetQuizratio() {
    String expected = "0.2";
    String actual = subjectSettingModel.getQuizratio();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetTestratio() {
    String expected = "0.3";
    String actual = subjectSettingModel.getTestratio();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetExamratio() {
    String expected = "0.4";
    String actual = subjectSettingModel.getExamratio();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testOverallRation() {
    double assignmentRatio = Double.parseDouble(subjectSettingModel.getAssignmentratio());
    double quizRatio = Double.parseDouble(subjectSettingModel.getQuizratio());
    double testRatio = Double.parseDouble(subjectSettingModel.getTestratio());
    double examRatio = Double.parseDouble(subjectSettingModel.getExamratio());
    String expected = "1.0";
    String actual = Double.toString(assignmentRatio + quizRatio + testRatio + examRatio);
    Assert.assertEquals(expected, actual);
  }
}