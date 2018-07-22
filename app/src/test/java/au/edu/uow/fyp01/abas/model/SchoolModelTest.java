package au.edu.uow.fyp01.abas.model;

import java.util.UUID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SchoolModelTest {

  private SchoolModel schoolModel;
  private String classID;

  @Before
  public void setUp() {
    classID = UUID.randomUUID().toString();
    schoolModel = new SchoolModel(classID, "1A");
  }

  @After
  public void tearDown() {
    classID = null;
    schoolModel = null;
  }

  @Test
  public void testGetClassID() {
    String expected = classID;
    String actual = schoolModel.getClassID();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetClassname() {
    String expected = "1A";
    String actual = schoolModel.getClassname();
    Assert.assertEquals(expected, actual);
  }
}