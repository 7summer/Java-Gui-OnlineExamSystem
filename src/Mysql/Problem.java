package Mysql;

import javax.swing.*;
import java.util.List;

public class Problem {
    private String SubjectId;//科目号
    private String ProblemId;//题目号
    private int ProblemType; //题目类型 0代表当选 1代表多选 2代表主观题
    private String problem;//题目
    private double grade;//分值

    /*
        以下属性在学生考试时使用到
     */
    private String[] options;

    /*
        以下属性在学生交卷时 统计客观题分数用到
     */
    private JRadioButton[] radioSelection = null;
    private  JCheckBox[] multipleSelection = null;
    private String answer; //题目类型为单选或多选时，存在答案
    private  JTextArea studentAnswer = null; //题目类型为主观题时，需要记录学生答案

    public String getSubjectId() {
        return SubjectId;
    }

    public void setSubjectId(String subjectId) {
        SubjectId = subjectId;
    }

    public String getProblemId() {
        return ProblemId;
    }

    public void setProblemId(String problemId) {
        ProblemId = problemId;
    }

    public int getProblemType() {
        return ProblemType;
    }

    public void setProblemType(int problemType) {
        ProblemType = problemType;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public JTextArea getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(JTextArea studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public JRadioButton[] getRadioSelection() {
        return radioSelection;
    }

    public void setRadioSelection(JRadioButton[] radioSelection) {
        this.radioSelection = radioSelection;
    }

    public JCheckBox[] getMultipleSelection() {
        return multipleSelection;
    }

    public void setMultipleSelection(JCheckBox[] multipleSelection) {
        this.multipleSelection = multipleSelection;
    }


}
