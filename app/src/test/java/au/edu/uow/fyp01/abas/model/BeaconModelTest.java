package au.edu.uow.fyp01.abas.model;

import java.util.Random;
import java.util.UUID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BeaconModelTest {

  private BeaconModel beaconModel;
  private Random rand;
  private String beaconID;
  private String classID;
  private String schoolID;
  private String sid;

  @Before
  public void setUp() {
    rand = new Random();
    beaconID = UUID.randomUUID().toString();
    classID = UUID.randomUUID().toString();
    schoolID = UUID.randomUUID().toString();
    sid = Integer.toString(rand.nextInt(30) + 1);
    beaconModel = new BeaconModel(beaconID, classID, schoolID, sid);
  }

  @After
  public void tearDown() {
    rand = null;
    beaconID = null;
    classID = null;
    schoolID = null;
    sid = null;
    beaconModel = null;
  }

  @Test
  public void testGetSid() {
    String expected = sid;
    String actual = beaconModel.getSid();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetSchID() {
    String expected = schoolID;
    String actual = beaconModel.getSchID();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetClassID() {
    String expected = classID;
    String actual = beaconModel.getClassID();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetBeaconID() {
    String expected = beaconID;
    String actual = beaconModel.getBeaconID();
    Assert.assertEquals(expected, actual);
  }
}