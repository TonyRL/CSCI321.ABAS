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

  //<editor-fold desc="comment setter and getter">
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
  //</editor-fold>

  //<editor-fold desc="commentor setter and getter">
  public String getCommentor() {
    return commentor;
  }

  public void setCommentor(String commentor) {
    this.commentor = commentor;
  }

  //</editor-fold>

  //<editor-fold desc="date setter and getter">
  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }
  //</editor-fold>

  //<editor-fold desc="timestamp setter and getter">
  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }
  //</editor-fold>

  //<editor-fold desc="commentID setter and getter">
  public String getCommentID() {
    return commentID;
  }

  public void setCommentID(String commentID) {
    this.commentID = commentID;
  }
  //</editor-fold>
}

