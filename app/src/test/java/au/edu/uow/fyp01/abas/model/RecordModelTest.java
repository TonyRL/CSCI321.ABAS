package au.edu.uow.fyp01.abas.model;

import java.util.UUID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RecordModelTest {

  private RecordModel recordModel;
  private String recordID;

  @Before
  public void setUp() {
    recordID = UUID.randomUUID().toString();
    recordModel = new RecordModel("A", "31-12-2018", Integer.toUnsignedLong(1546257600), recordID);
  }

  @After
  public void tearDown() {
    recordID = null;
    recordModel = null;
  }

  @Test
  public void testGetDate() {
    String expected = "31-12-2018";
    String actual = recordModel.getDate();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetGrade() {
    String expected = "A";
    String actual = recordModel.getGrade();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetRecordID() {
    String expected = recordID;
    String actual = recordModel.getRecordID();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetTimestamp() {
    Long expected = Integer.toUnsignedLong(1546257600);
    Long actual = recordModel.getTimestamp();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetType() {
    String expected = null;
    String actual = recordModel.getType();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetGradename() {
    String expected = null;
    String actual = recordModel.getGradename();
    Assert.assertEquals(expected, actual);
  }
}