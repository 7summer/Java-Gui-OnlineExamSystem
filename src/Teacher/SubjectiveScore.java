package Teacher;

import Mysql.SqlStudent;
import Mysql.SqlTeacher;
import Student.ExamView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectiveScore extends JFrame{
    String pageId; //试卷号
    String problemId; //题目号
    List<String[]> studentAnswers = null; //学号+学生答案
    int index = 0; //studentAnswers的索引
    String[] problem = null; //题目+分值
    JTextArea problemText = null; //题目
    JTextArea studentAnswer = null; //学生答案
    JLabel label1 = null; //分值
    JTextField grade = null;
    JComboBox<String> score = null; //教师评分
    List<String[]> studentSubjectiveProblemScore = null; //学生主观题得分 学号+得分
    JPanel panel = null;
    JScrollPane scroll = null;

    JMenuBar bar = null;
    JLabel label2 = null; //试卷号+问题号
    Box boxH = null;

    Box boxOne = null;

    JButton sure = null;

    public SubjectiveScore(String teacherId, String pageId, String problemId)
    {
        this.pageId = pageId;
        this.problemId = problemId;
        this.studentAnswers = SqlTeacher.selectSubjectiveStudentAnswer(pageId, problemId);
        this.studentSubjectiveProblemScore = new ArrayList<String[]>();

        init();

        addButtonAction(); //给（提交）按钮添加监听器
        addWindowAction(); //给（关闭）按钮添加监听器

        setTitle("评分界面");
        setVisible(true);
        setBounds(400, 400, 1000, 1000);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    public void init()
    {
        panel = new JPanel();

        boxH = Box.createVerticalBox();
        boxOne = Box.createHorizontalBox();

        boxH.add(Box.createHorizontalStrut(20));
        boxH.add(Box.createVerticalStrut(20));

        //获取题目+分值
        problem = SqlTeacher.queryProblemAccordingProblemId(pageId, problemId);

        problemText = new JTextArea(5, 5);
        problemText.setText(problem[0]);
        problemText.setEditable(false);

        studentAnswer = new JTextArea(20, 10);
        studentAnswer.setText(studentAnswers.get(index++)[1]);
        studentAnswer.setEditable(false);

        label1 = new JLabel("分值");
        label1.setFont(new Font("宋体", 1, 15));
        grade = new JTextField(15);
        grade.setText(problem[1]);
        grade.setEditable(false);

        score = new JComboBox<String>();
        score.addItem("请选择分值");
        for(double i=0; i<=Double.parseDouble(problem[1]); i+=0.5)
        {
            score.addItem(String.valueOf(i));
        }

        sure = new JButton("确认");
        sure.setFont(new Font("宋体", 1, 15));

        boxH.add(problemText);
        boxH.add(Box.createVerticalStrut(20));
        boxH.add(studentAnswer);

        boxOne.add(label1);
        boxOne.add(Box.createHorizontalStrut(10));
        boxOne.add(grade);
        boxOne.add(Box.createHorizontalStrut(30));
        boxOne.add(score);

        boxH.add(Box.createVerticalStrut(20));
        boxH.add(boxOne);
        boxH.add(Box.createVerticalStrut(20));
        boxH.add(sure);

        scroll = new JScrollPane(boxH);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scroll, BorderLayout.CENTER);

        bar = new JMenuBar();
        label2 = new JLabel(pageId + "-" + problemId);
        label2.setFont(new Font("宋体", 1, 15));
        bar.add(label2);

        add(bar, BorderLayout.NORTH);
    }

    public void addButtonAction()
    {
        sure.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String studentId = studentAnswers.get(index-1)[0];
                studentSubjectiveProblemScore.add(new String[]{studentId, score.getSelectedItem().toString()});

                if(index == studentAnswers.size())
                {
                    JOptionPane.showMessageDialog(SubjectiveScore.this, "已完成评分",
                            "提示框", JOptionPane.INFORMATION_MESSAGE);

                    //score表中学生的SubjectiveScore
                    List<Double> studentSubjectiveScore = SqlTeacher.queryStudentSubjectiveScore(pageId, studentSubjectiveProblemScore);

                    //将教师对学生客观题的评分提交数据库
                    SqlTeacher.submitStudentSubjectiveProblemGrade(pageId, studentSubjectiveProblemScore, studentSubjectiveScore);
                    //更新学生客观题答案的状态
                    SqlTeacher.updateStudentAnswerState(pageId, problemId, studentSubjectiveProblemScore);

                    dispose();
                }
                else
                {
                    studentAnswer.setText(studentAnswers.get(index++)[1]);
                }
            }
        });
    }

    public void addWindowAction()
    {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(SubjectiveScore.this, "确定提交",
                        "提示框", JOptionPane.YES_NO_OPTION);

                if(option == JOptionPane.YES_OPTION)
                {
                    //score表中学生的SubjectiveScore
                    List<Double> studentSubjectiveScore = SqlTeacher.queryStudentSubjectiveScore(pageId, studentSubjectiveProblemScore);

                    //将教师对学生客观题的评分提交数据库
                    SqlTeacher.submitStudentSubjectiveProblemGrade(pageId, studentSubjectiveProblemScore, studentSubjectiveScore);
                    //更新学生客观题答案的状态
                    SqlTeacher.updateStudentAnswerState(pageId, problemId, studentSubjectiveProblemScore);

                    dispose();
                }
                else
                {
                    return;
                }
            }
        });
    }
}
