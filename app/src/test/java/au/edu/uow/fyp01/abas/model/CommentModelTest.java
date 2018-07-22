package au.edu.uow.fyp01.abas.model;

import java.util.UUID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CommentModelTest {

  private CommentModel commentModel;
  private String commentID;

  @Before
  public void setUp() {
    commentID = UUID.randomUUID().toString();
    commentModel = new CommentModel("This is a test comment", "Mr. Chan", "31-12-2018",
        Integer.toUnsignedLong(1546257600), commentID);
  }

  @After
  public void tearDown() {
    commentID = null;
    commentModel = null;
  }

  @Test
  public void testGetComment() {
    String expected = "This is a test comment";
    String actual = commentModel.getComment();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetCommentor() {
    String expected = "Mr. Chan";
    String actual = commentModel.getCommentor();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetDate() {
    String expected = "31-12-2018";
    String actual = commentModel.getDate();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetTimestamp() {
    Long expected = Integer.toUnsignedLong(1546257600);
    Long actual = commentModel.getTimestamp();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetCommentID() {
    String expected = commentID;
    String actual = commentModel.getCommentID();
    Assert.assertEquals(expected, actual);
  }
}