package au.edu.uow.fyp01.abas.Model;

/**
 * Created by Athens on 2018/04/21.
 */

public class CommentModel {

  String comment;
  String commentor;
  String date;

  public CommentModel() {
  }

  public CommentModel(String comment, String commentor, String date) {
    this.comment = comment;
    this.commentor = commentor;
    this.date = date;
  }

  //<editor-fold desc="comment setter and getter">
  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getComment() {
    return comment;
  }
  //</editor-fold>

  //<editor-fold desc="commentor setter and getter">
  public void setCommentor(String commentor) {
    this.commentor = commentor;
  }

  public String getCommentor() {
    return commentor;
  }
  //</editor-fold>

  //<editor-fold desc="date setter and getter">
  public void setDate(String date) {
    this.date = date;
  }

  public String getDate() {
    return date;
  }
  //</editor-fold>
}

