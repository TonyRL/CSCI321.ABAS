package au.edu.uow.fyp01.abas.model;

import java.util.Random;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StudentModelTest {

  private StudentModel studentModel;
  private String classNumber;
  private String studentID;
  private Random rand;

  @Before
  public void setUp() {
    rand = new Random();
    classNumber = Integer.toString(rand.nextInt(30) + 1);
    studentID = Integer.toString(rand.nextInt(6000000) + 1);
    studentModel = new StudentModel("1A", classNumber, "Alan", "Chan", studentID);
  }

  @After
  public void tearDown() {
    rand = null;
    classNumber = null;
    studentID = null;
    studentModel = null;
  }

  @Test
  public void testGetClassname() {
    String expected = "1A";
    String actual = studentModel.getClassname();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetClassnumber() {
    String expected = classNumber;
    String actual = studentModel.getClassnumber();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetFirstname() {
    String expected = "Alan";
    String actual = studentModel.getFirstname();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetLastname() {
    String expected = "Chan";
    String actual = studentModel.getLastname();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetSid() {
    String expected = studentID;
    String actual = studentModel.getSid();
    Assert.assertEquals(expected, actual);
  }
}