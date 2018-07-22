package au.edu.uow.fyp01.abas.model;

import java.util.UUID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SchoolInfoModelTest {

  private SchoolInfoModel schoolInfoModel;
  private String schoolID;

  @Before
  public void setUp() {
    schoolID = UUID.randomUUID().toString();
    schoolInfoModel = new SchoolInfoModel(schoolID, "Secondary School");
  }

  @After
  public void tearDown() {
    schoolID = null;
    schoolInfoModel = null;
  }

  @Test
  public void testGetSchID() {
    String expected = schoolID;
    String actual = schoolInfoModel.getSchID();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetSchoolname() {
    String expected = "Secondary School";
    String actual = schoolInfoModel.getSchoolname();
    Assert.assertEquals(expected, actual);
  }
}