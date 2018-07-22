package au.edu.uow.fyp01.abas.model;

import java.util.Random;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ListOfStudentModelTest {

  private ListOfStudentModel list;
  private Random rand;
  private String studentID;

  @Before
  public void setUp() {
    rand = new Random();
    studentID = Integer.toString(rand.nextInt(30) + 1);
    list = new ListOfStudentModel("Alan", "Chan", studentID);
  }

  @After
  public void tearDown() {
    rand = null;
    studentID = null;
    list = null;
  }

  @Test
  public void testGetFirstname() {
    String expected = "Alan";
    String actual = list.getFirstname();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetLastname() {
    String expected = "Chan";
    String actual = list.getLastname();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetSid() {
    String expected = studentID;
    String actual = list.getSid();
    Assert.assertEquals(expected, actual);
  }


}