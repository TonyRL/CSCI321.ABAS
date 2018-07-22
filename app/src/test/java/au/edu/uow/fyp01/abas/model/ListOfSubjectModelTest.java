package au.edu.uow.fyp01.abas.model;

import java.util.UUID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ListOfSubjectModelTest {

  private ListOfSubjectModel list;
  private String subjectID;

  @Before
  public void setUp() {
    subjectID = UUID.randomUUID().toString();
    list = new ListOfSubjectModel("English", subjectID);
  }

  @After
  public void tearDown() {
    subjectID = null;
    list = null;
  }

  @Test
  public void testGetSubjectID() {
    String expected = subjectID;
    String actual = list.getSubjectID();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetSubjectName() {
    String expected = "English";
    String actual = list.getSubjectName();
    Assert.assertEquals(expected, actual);
  }
}