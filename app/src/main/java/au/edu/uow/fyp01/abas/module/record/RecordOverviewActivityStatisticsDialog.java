package au.edu.uow.fyp01.abas.module.record;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import au.edu.uow.fyp01.abas.R;

public class RecordOverviewActivityStatisticsDialog extends DialogFragment {

  private String overall;
  private String examAll;
  private String quizAll;
  private String testAll;
  private String assignmentAll;

  private String overallHighestGrade;
  private String overallLowestGrade;
  private String overallAverageGrade;


  private String examHighestGrade;
  private String examLowestGrade;
  private String examAverageGrade;

  private String assignmentHighest;
  private String assignmentLowest;
  private String assignmentAverage;

  private String quizHighest;
  private String quizLowest;
  private String quizAverage;

  private String testHighest;
  private String testLowest;
  private String testAverage;
  private TextView overallTextView;
  private TextView overallExamView;
  private TextView overallQuizView;
  private TextView overallTestView;
  private TextView overallAssignmentView;
  private TextView overallHighestGradeTextView;
  private TextView overallLowestGradeTextView;
  private TextView overallAverageGradeTextView;
  private TextView examHighestGradeTextView;
  private TextView examLowestGradeTextView;
  private TextView examAverageGradeTextView;
  private TextView assignmentHighestTextView;
  private TextView assignmentLowestTextView;
  private TextView assignmentAverageTextView;
  private TextView quizHighestTextView;
  private TextView quizLowestTextView;
  private TextView quizAverageTextView;
  private TextView testHighestTextView;
  private TextView testLowestTextView;
  private TextView testAverageTextView;

  public String getExamAverageGrade() {
    return examAverageGrade;
  }

  public void setExamAverageGrade(String examAverageGrade) {
    this.examAverageGrade = examAverageGrade;
  }

  public String getExamHighestGrade() {
    return examHighestGrade;
  }

  public void setExamHighestGrade(String examHighestGrade) {
    this.examHighestGrade = examHighestGrade;
  }

  public String getExamLowestGrade() {
    return examLowestGrade;
  }

  public void setExamLowestGrade(String examLowestGrade) {
    this.examLowestGrade = examLowestGrade;
  }

  public String getOverall() {
    return overall;
  }

  public void setOverall(String overall) {
    this.overall = overall;
  }

  public String getOverallHighestGrade() {
    return overallHighestGrade;
  }

  public void setOverallHighestGrade(String overallHighestGrade) {
    this.overallHighestGrade = overallHighestGrade;
  }

  public String getOverallLowestGrade() {
    return overallLowestGrade;
  }

  public void setOverallLowestGrade(String overallLowestGrade) {
    this.overallLowestGrade = overallLowestGrade;
  }

  public String getOverallAverageGrade() {
    return overallAverageGrade;
  }

  public void setOverallAverageGrade(String overallAverageGrade) {
    this.overallAverageGrade = overallAverageGrade;
  }

  public String getAssignmentHighest() {
    return assignmentHighest;
  }

  public void setAssignmentHighest(String assignmentHighest) {
    this.assignmentHighest = assignmentHighest;
  }

  public String getAssignmentLowest() {
    return assignmentLowest;
  }

  public void setAssignmentLowest(String assignmentLowest) {
    this.assignmentLowest = assignmentLowest;
  }

  public String getAssignmentAverage() {
    return assignmentAverage;
  }

  public void setAssignmentAverage(String assignmentAverage) {
    this.assignmentAverage = assignmentAverage;
  }

  public String getQuizHighest() {
    return quizHighest;
  }

  public void setQuizHighest(String quizHighest) {
    this.quizHighest = quizHighest;
  }

  public String getQuizLowest() {
    return quizLowest;
  }

  public void setQuizLowest(String quizLowest) {
    this.quizLowest = quizLowest;
  }

  public String getQuizAverage() {
    return quizAverage;
  }

  public void setQuizAverage(String quizAverage) {
    this.quizAverage = quizAverage;
  }

  public String getTestHighest() {
    return testHighest;
  }

  public void setTestHighest(String testHighest) {
    this.testHighest = testHighest;
  }

  public String getTestLowest() {
    return testLowest;
  }

  public void setTestLowest(String testLowest) {
    this.testLowest = testLowest;
  }

  public String getTestAverage() {
    return testAverage;
  }

  public void setTestAverage(String testAverage) {
    this.testAverage = testAverage;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.activity_record_overview_statistics_dialog, null);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    overallHighestGradeTextView = view
        .findViewById(R.id.activity_record_overview_statisitcs_dialog_OH);
    overallHighestGradeTextView.setText(overallHighestGrade);
    overallLowestGradeTextView = view
        .findViewById(R.id.activity_record_overview_statisitcs_dialog_OL);
    overallLowestGradeTextView.setText(overallLowestGrade);
    overallAverageGradeTextView = view
        .findViewById(R.id.activity_record_overview_statisitcs_dialog_OA);
    overallAverageGradeTextView.setText(overallAverageGrade);

    TextView examHighestGradeTextView = view
        .findViewById(R.id.activity_record_overview_statisitcs_dialog_EH);
    examHighestGradeTextView.setText(examHighestGrade);
    examLowestGradeTextView = view.findViewById(R.id.activity_record_overview_statisitcs_dialog_EL);
    examLowestGradeTextView.setText(examLowestGrade);
    examAverageGradeTextView = view
        .findViewById(R.id.activity_record_overview_statisitcs_dialog_EA);
    examAverageGradeTextView.setText(examHighestGrade);

    TextView assignmentHighestTextView = view
        .findViewById(R.id.activity_record_overview_statisitcs_dialog_AH);
    assignmentHighestTextView.setText(assignmentHighest);
    TextView assignmentLowestTextView = view
        .findViewById(R.id.activity_record_overview_statisitcs_dialog_AL);
    assignmentLowestTextView.setText(assignmentLowest);
    TextView assignmentAverageTextView = view
        .findViewById(R.id.activity_record_overview_statisitcs_dialog_AA);
    assignmentAverageTextView.setText(assignmentAverage);

    TextView quizHighestTextView = view
        .findViewById(R.id.activity_record_overview_statisitcs_dialog_QH);
    quizHighestTextView.setText(quizHighest);
    TextView quizLowestTextView = view
        .findViewById(R.id.activity_record_overview_statisitcs_dialog_QL);
    quizLowestTextView.setText(quizLowest);
    TextView quizAverageTextView = view
        .findViewById(R.id.activity_record_overview_statisitcs_dialog_QA);
    quizAverageTextView.setText(quizAverage);

    TextView testHighestTextView = view
        .findViewById(R.id.activity_record_overview_statisitcs_dialog_TH);
    testHighestTextView.setText(testHighest);
    TextView testLowestTextView = view
        .findViewById(R.id.activity_record_overview_statisitcs_dialog_TL);
    testLowestTextView.setText(testLowest);
    TextView testAverageTextView = view
        .findViewById(R.id.activity_record_overview_statisitcs_dialog_TA);
    testAverageTextView.setText(testAverage);

  }
}
