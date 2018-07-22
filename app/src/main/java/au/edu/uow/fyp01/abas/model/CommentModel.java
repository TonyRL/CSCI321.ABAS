package au.edu.uow.fyp01.abas.model;

/**
 * Created by Athens on 2018/04/21.
 */

public class CommentModel {

  String comment;
  String commentor;
  String date;
  Long timestamp;
  String commentID;

  public CommentModel() {
  }

  public CommentModel(String comment, String commentor, String date, Long timestamp,
      String commentID) {
    this.comment = comment;
    this.commentor = commentor;
    this.date = date;
    this.timestamp = timestamp;
    this.commentID = commentID;
  }

  /**
   * Get comment
   *
   * @return comment
   */
  //<editor-fold desc="comment setter and getter">
  public String getComment() {
    return comment;
  }

  /**
   * Set comment
   *
   * @param comment comment
   */
  public void setComment(String comment) {
    this.comment = comment;
  }
  //</editor-fold>

  /**
   * Get commentor
   *
   * @return commentor
   */
  //<editor-fold desc="commentor setter and getter">
  public String getCommentor() {
    return commentor;
  }

  /**
   * Set commentor
   *
   * @param commentor commentor
   */
  public void setCommentor(String commentor) {
    this.commentor = commentor;
  }

  //</editor-fold>

  /**
   * Get comment date
   *
   * @return comment date
   */
  //<editor-fold desc="date setter and getter">
  public String getDate() {
    return date;
  }

  /**
   * Set  comment date
   *
   * @param date comment date
   */
  public void setDate(String date) {
    this.date = date;
  }
  //</editor-fold>

  /**
   * Get comment time
   *
   * @return comment time
   */
  //<editor-fold desc="timestamp setter and getter">
  public Long getTimestamp() {
    return timestamp;
  }

  /**
   * Set  comment time
   *
   * @param timestamp comment time
   */
  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }
  //</editor-fold>

  /**
   * Get comment ID
   *
   * @return comment ID
   */
  //<editor-fold desc="commentID setter and getter">
  public String getCommentID() {
    return commentID;
  }

  /**
   * Set comment ID
   *
   * @param commentID comment ID
   */
  public void setCommentID(String commentID) {
    this.commentID = commentID;
  }
  //</editor-fold>
}

