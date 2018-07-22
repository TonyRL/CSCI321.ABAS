package au.edu.uow.fyp01.abas.model;

import java.util.Random;
import java.util.UUID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserModelTest {

  UserModel userModel;
  Random rand;
  int emailNo;
  String schoolID;
  String staffID;

  @Before
  public void setUp() {
    rand = new Random();
    emailNo = rand.nextInt(1000) + 1;
    schoolID = UUID.randomUUID().toString();
    staffID = UUID.randomUUID().toString();
    userModel = new UserModel("test" + emailNo + "@test.com", "Test User" + emailNo, schoolID,
        staffID, "waiting", "Mr.", "Admin");
  }

  @Test
  public void testGetEmail() {
    String expected = "test" + emailNo + "@test.com";
    String actual = userModel.getEmail();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetFullname() {
    String expected = "Test User" + emailNo;
    String actual = userModel.getFullname();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetSchID() {
    String expected = schoolID;
    String actual = userModel.getSchID();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetTitle() {
    String expected = "Mr.";
    String actual = userModel.getTitle();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetUsertype() {
    String expected = "Admin";
    String actual = userModel.getUsertype();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetStaffID() {
    String expected = staffID;
    String actual = userModel.getStaffID();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetStatus() {
    String expected = "waiting";
    String actual = userModel.getStatus();
    Assert.assertEquals(expected, actual);
  }

  @After
  public void tearDown() {
    rand = null;
    emailNo = 0;
    schoolID = null;
    staffID = null;
    userModel = null;
  }
}