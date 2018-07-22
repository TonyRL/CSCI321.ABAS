package au.edu.uow.fyp01.abas.model;

import java.util.Random;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RequestListModelTest {

  private RequestListModel list;
  private String userID;
  private String staffID;
  private Random rand;

  @Before
  public void setUp() {
    rand = new Random();
    userID = Integer.toString(rand.nextInt(30) + 1);
    staffID = Integer.toString(rand.nextInt(30) + 1);
    list = new RequestListModel("Alan Chan", "Mr.", userID, staffID);
  }

  @After
  public void tearDown() {
    rand = null;
    userID = null;
    staffID = null;
    list = null;
  }

  @Test
  public void testGetFullname() {
    String expected = "Alan Chan";
    String actual = list.getFullname();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetUserID() {
    String expected = userID;
    String actual = list.getUserID();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetTitle() {
    String expected = "Mr.";
    String actual = list.getTitle();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetStaffID() {
    String expected = staffID;
    String actual = list.getStaffID();
    Assert.assertEquals(expected, actual);
  }
}