package au.edu.uow.fyp01.abas.module.record;

import au.edu.uow.fyp01.abas.model.RecordModel;
import java.util.ArrayList;

public class StatisticsEngine {

  //this holds all of the RecordModels
  //see RecordModel
  private ArrayList<RecordModel> recordList;

  //this holds the record models belonging
  //to the type of
  // ASSIGNMENT
  private ArrayList<RecordModel> assignmentList;

  //this holds the record models belonging
  //to the type of
  // QUIZ
  private ArrayList<RecordModel> quizList;

  //this holds the record models belonging
  //to the type of
  // TEST
  private ArrayList<RecordModel> testList;

  //this holds the record models belonging
  //to the type of
  // EXAMS
  private ArrayList<RecordModel> examList;

  public StatisticsEngine(ArrayList<RecordModel> recordList1) {

    //initialize all of the arrays
    recordList = new ArrayList<RecordModel>();
    assignmentList = new ArrayList<RecordModel>();
    quizList = new ArrayList<RecordModel>();
    testList = new ArrayList<RecordModel>();
    examList = new ArrayList<RecordModel>();

    //grab the recordList from RecordOverviewActivity
    this.recordList = recordList1;

    sortRecords();
  }

  //This function sorts all of the records into
  // the four categories (assignment, quiz, test, exam)
  public void sortRecords() {
    if (recordList.size() == 0) {
      //do nothing to prevent crash
    } else {

      for (int i = 0; i < recordList.size(); i++) {

        //<editor-fold desc="sort record into assignment list">
        //</editor-fold>
        if (recordList.get(i).getType().toString().equals("assignment")) {
          assignmentList.add(recordList.get(i));
        }

        //<editor-fold desc="sort record into quiz list">
        //</editor-fold>
        else if (recordList.get(i).getType().toString().equals("quiz")) {
          quizList.add(recordList.get(i));
        }

        //<editor-fold desc="sort record into test list">
        //</editor-fold>
        else if (recordList.get(i).getType().toString().equals("test")) {
          testList.add(recordList.get(i));
        }

        //<editor-fold desc="sort record into exam list">
        //</editor-fold>
        else {
          examList.add(recordList.get(i));
        }

      }

    } //end if
  } //end sortRecords()

    /*
    &&
    && Statistics functions down below
    && which finds highest, lowest, and average grades
    && functions follow naming conventions:
    && public String find[type][level]grade() {method};
    && type = Overall/Assignment/Quiz/Test/Exam
    && level = Highest/Lowest/Average
    &&
    && Extra note: Quickly jump to different types
    && by Finding the phrase 'BOOKMARK [type]'
    && e.g. Find 'BOOKMARK Overall'
    &&
     */


  /*
  ||
  || BOOKMARK Overall
  || OVERALL GRADE (Highest/Lowest/Average)
  ||
   */
  public String findOverallHighestGrade() {

    //In case there is no records found for this particular subject
    if (recordList.size() == 0) {
      return "Record \nnot found!";
    }

    int highest = 0;

    for (int i = 0; i < recordList.size(); i++) {

      if (Integer.parseInt(recordList.get(i).getGrade()) > highest) {
        highest = Integer.parseInt(recordList.get(i).getGrade());
      }

    }

    return Integer.toString(highest);

  }
  //</editor-fold>


  //<editor-fold desc="findLowestGrade() -> Finds the lowest grade in the list">
  public String findOverallLowestGrade() {

    //In case there is no records found for this particular subject
    if (recordList.size() == 0) {
      return "Record \nnot found!";
    }

    int lowest = 100;

    for (int i = 0; i < recordList.size(); i++) {

      if (Integer.parseInt(recordList.get(i).getGrade()) < lowest) {
        lowest = Integer.parseInt(recordList.get(i).getGrade());
      }

    }

    return Integer.toString(lowest);
  }
  //</editor-fold>

  //<editor-fold desc="findAverageGrade() -> Finds the average grade of the subject">
  public String findOverallAverageGrade() {

    //In case there is no records found for this particular subject
    if (recordList.size() == 0) {

      return "Record \nnot found!";

    } else {
      double average = 0;
      double total = 0;
      double temp = 0;

      for (int i = 0; i < recordList.size(); i++) {
        temp = Double.parseDouble(recordList.get(i).getGrade());
        total += temp;
      }

      average = total / (recordList.size());

      return Double.toString(average);
    }
  }
  //</editor-fold>

  /*
  || BOOKMARK Assignment
  || ASSIGNMENT GRADE (Highest/Lowest/Average)
  ||
  */
  public String findAssignmentHighestGrade() {

    //In case there is no records found for this particular subject
    if (assignmentList.size() == 0) {
      return "Record \nnot found!";
    }

    int highest = 0;

    for (int i = 0; i < assignmentList.size(); i++) {

      if (Integer.parseInt(assignmentList.get(i).getGrade()) > highest) {
        highest = Integer.parseInt(assignmentList.get(i).getGrade());
      }

    }

    return Integer.toString(highest);

  }
  //</editor-fold>


  //<editor-fold desc="findLowestGrade() -> Finds the lowest grade in the list">
  public String findAssignmentLowestGrade() {

    //In case there is no records found for this particular subject
    if (assignmentList.size() == 0) {
      return "Record \nnot found!";
    }

    int lowest = 100;

    for (int i = 0; i < assignmentList.size(); i++) {

      if (Integer.parseInt(assignmentList.get(i).getGrade()) < lowest) {
        lowest = Integer.parseInt(assignmentList.get(i).getGrade());
      }

    }

    return Integer.toString(lowest);
  }
  //</editor-fold>

  //<editor-fold desc="findAverageGrade() -> Finds the average grade of the subject">
  public String findAssignmentAverageGrade() {

    //In case there is no records found for this particular subject
    if (assignmentList.size() == 0) {

      return "Record \nnot found!";

    } else {
      double average = 0;
      double total = 0;
      double temp = 0;

      for (int i = 0; i < assignmentList.size(); i++) {
        temp = Double.parseDouble(assignmentList.get(i).getGrade());
        total += temp;
      }

      average = total / (assignmentList.size());

      return Double.toString(average);
    }
  }

  /*
  ||
  || QUIZ GRADE (Highest/Lowest/Average)
  ||
  */
  public String findQuizHighestGrade() {

    //In case there is no records found for this particular subject
    if (quizList.size() == 0) {
      return "Record \nnot found!";
    }

    int highest = 0;

    for (int i = 0; i < quizList.size(); i++) {

      if (Integer.parseInt(quizList.get(i).getGrade()) > highest) {
        highest = Integer.parseInt(quizList.get(i).getGrade());
      }

    }

    return Integer.toString(highest);

  }
  //</editor-fold>


  //<editor-fold desc="findLowestGrade() -> Finds the lowest grade in the list">
  public String findQuizLowestGrade() {

    //In case there is no records found for this particular subject
    if (quizList.size() == 0) {
      return "Record \nnot found!";
    }

    int lowest = 100;

    for (int i = 0; i < quizList.size(); i++) {

      if (Integer.parseInt(quizList.get(i).getGrade()) < lowest) {
        lowest = Integer.parseInt(quizList.get(i).getGrade());
      }

    }

    return Integer.toString(lowest);
  }
  //</editor-fold>

  //<editor-fold desc="findAverageGrade() -> Finds the average grade of the subject">
  public String findQuizAverageGrade() {

    //In case there is no records found for this particular subject
    if (quizList.size() == 0) {

      return "Record \nnot found!";

    } else {
      double average = 0;
      double total = 0;
      double temp = 0;

      for (int i = 0; i < quizList.size(); i++) {
        temp = Double.parseDouble(quizList.get(i).getGrade());
        total += temp;
      }

      average = total / (quizList.size());

      return Double.toString(average);
    }
  }

  /*
  ||
  || BOOKMARK Test
  || TEST GRADE (Highest/Lowest/Average)
  ||
  */
  public String findTestHighestGrade() {

    //In case there is no records found for this particular subject
    if (testList.size() == 0) {
      return "Record \nnot found!";
    }

    int highest = 0;

    for (int i = 0; i < testList.size(); i++) {

      if (Integer.parseInt(testList.get(i).getGrade()) > highest) {
        highest = Integer.parseInt(testList.get(i).getGrade());
      }

    }

    return Integer.toString(highest);

  }
  //</editor-fold>


  //<editor-fold desc="findLowestGrade() -> Finds the lowest grade in the list">
  public String findTestLowestGrade() {

    //In case there is no records found for this particular subject
    if (testList.size() == 0) {
      return "Record \nnot found!";
    }

    int lowest = 100;

    for (int i = 0; i < testList.size(); i++) {

      if (Integer.parseInt(testList.get(i).getGrade()) < lowest) {
        lowest = Integer.parseInt(testList.get(i).getGrade());
      }

    }

    return Integer.toString(lowest);
  }
  //</editor-fold>

  //<editor-fold desc="findAverageGrade() -> Finds the average grade of the subject">
  public String findTestAverageGrade() {

    //In case there is no records found for this particular subject
    if (testList.size() == 0) {

      return "Record \nnot found!";

    } else {
      double average = 0;
      double total = 0;
      double temp = 0;

      for (int i = 0; i < testList.size(); i++) {
        temp = Double.parseDouble(testList.get(i).getGrade());
        total += temp;
      }

      average = total / (testList.size());

      return Double.toString(average);
    }
  }

  /*
  ||
  || BOOKMARK Exam
  || EXAM GRADE (Highest/Lowest/Average)
  ||
  */
  public String findExamHighestGrade() {

    //In case there is no records found for this particular subject
    if (examList.size() == 0) {
      return "Record \nnot found!";
    }

    int highest = 0;

    for (int i = 0; i < examList.size(); i++) {

      if (Integer.parseInt(examList.get(i).getGrade()) > highest) {
        highest = Integer.parseInt(examList.get(i).getGrade());
      }

    }

    return Integer.toString(highest);

  }
  //</editor-fold>


  //<editor-fold desc="findLowestGrade() -> Finds the lowest grade in the list">
  public String findExamLowestGrade() {

    //In case there is no records found for this particular subject
    if (examList.size() == 0) {
      return "Record \nnot found!";
    }

    int lowest = 100;

    for (int i = 0; i < examList.size(); i++) {

      if (Integer.parseInt(examList.get(i).getGrade()) < lowest) {
        lowest = Integer.parseInt(examList.get(i).getGrade());
      }

    }

    return Integer.toString(lowest);
  }
  //</editor-fold>

  //<editor-fold desc="findAverageGrade() -> Finds the average grade of the subject">
  public String findExamAverageGrade() {

    //In case there is no records found for this particular subject
    if (examList.size() == 0) {

      return "Record \nnot found!";

    } else {
      double average = 0;
      double total = 0;
      double temp = 0;

      for (int i = 0; i < examList.size(); i++) {
        temp = Double.parseDouble(examList.get(i).getGrade());
        total += temp;
      }

      average = total / (examList.size());

      return Double.toString(average);
    }
  }


  public ArrayList<RecordModel> getAssignmentList() {
    return assignmentList;
  }

  public ArrayList<RecordModel> getQuizList() {
    return quizList;
  }

  public ArrayList<RecordModel> getTestList() {
    return testList;
  }

  public ArrayList<RecordModel> getExamList() {
    return examList;
  }

}
