package au.edu.uow.fyp01.abas.model;

import java.util.UUID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SubjectModelTest {

  private SubjectModel subjectModel;
  private String subjectID;

  @Before
  public void setUp() {
    subjectID = UUID.randomUUID().toString();
    subjectModel = new SubjectModel("English", subjectID);
  }

  @After
  public void tearDown() {
    subjectID = null;
    subjectModel = null;
  }

  @Test
  public void testGetSubjectID() {
    String expected = subjectID;
    String actual = subjectModel.getSubjectID();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetSubjectname() {
    String expected = "English";
    String actual = subjectModel.getSubjectname();
    Assert.assertEquals(expected, actual);
  }
}