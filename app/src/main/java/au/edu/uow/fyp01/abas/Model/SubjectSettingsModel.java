package au.edu.uow.fyp01.abas.Model;

public class SubjectSettingsModel {

    public String assignmentratio;
    public String quizratio;
    public String testratio;
    public String examratio;

    public SubjectSettingsModel(){

    }

    public SubjectSettingsModel(String assignmentratio,
                                String quizratio,
                                String testratio,
                                String examratio){
        this.assignmentratio = assignmentratio;
        this.quizratio = quizratio;
        this.testratio = testratio;
        this.examratio = examratio;
    }

    public void setAssignmentratio(String assignmentratio) {
        this.assignmentratio = assignmentratio;
    }

    public String getAssignmentratio() {
        return assignmentratio;
    }

    public void setQuizratio(String quizratio) {
        this.quizratio = quizratio;
    }

    public String getQuizratio() {
        return quizratio;
    }

    public void setTestratio(String testratio) {
        this.testratio = testratio;
    }

    public String getTestratio() {
        return testratio;
    }

    public void setExamratio(String examratio) {
        this.examratio = examratio;
    }

    public String getExamratio() {
        return examratio;
    }
}
