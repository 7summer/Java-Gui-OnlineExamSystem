package Student;

import Mysql.Problem;
import Mysql.SqlStudent;
import Mysql.SqlTeacher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;


public class ExamView extends JFrame {
    List<Problem> problems = null; //存储考题
    String studentId; //学生学号
    String pageId; //试卷号
    String subjectId; //科目号
    String subjectName; //科目名

    JPanel panel = null;
    JScrollPane scroll = null;

    JMenuBar bar = null;
    JLabel label1 = null;
    JLabel label2 = null;

    Box boxH = null;
    Box[] box = null;

    JButton submit = null; //（提交）按钮

    public ExamView(String studentId, String pageId, String subjectId, String subjectName)
    {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.problems = SqlStudent.importExamProblem(pageId, subjectId); //导出考题
        this.studentId = studentId;
        this.pageId = pageId;

        init();

        addButtonAction(); //给（提交）按钮添加监听器
        addWindowAction(); //给（关闭）按钮添加监听器

        setTitle("考试界面");
        setVisible(true);
        setBounds(400, 400, 1000, 1000);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    public void init()
    {
        int num = problems.size();

        panel = new JPanel();

        boxH = Box.createVerticalBox(); //获得一个具有列型盒式的布局的盒式容器
        box = new Box[num];

        boxH.add(Box.createHorizontalStrut(20));
        boxH.add(Box.createVerticalStrut(20));

        for (int i = 0; i < num; i++) //将考题添加到面板上
        {
            Problem temp = problems.get(i);

            box[i] = Box.createVerticalBox(); //获得一个具有行型盒式布局的盒式容器

            JTextArea problem = new JTextArea(5, 5); //题目
            problem.setText(temp.getProblem());
            problem.setFont(new Font("宋体", 1, 15));
            problem.setEditable(false);
            box[i].add(problem); //将题目添加到窗口
            box[i].add(Box.createVerticalStrut(20));

            int type = temp.getProblemType(); //题目类型
            if(type == 0) //单选题
            {
                ButtonGroup buttonGroup = new ButtonGroup();
                JRadioButton[] radioSelection = new JRadioButton[4];
                String[] options = temp.getOptions();

                temp.setRadioSelection(radioSelection);

                for(int j = 0; j < radioSelection.length; j++)
                {
                    radioSelection[j] = new JRadioButton(options[j]);
                    radioSelection[j].setFont(new Font("宋体", 1, 15));

                    buttonGroup.add(radioSelection[j]);
                    box[i].add(radioSelection[j]);
                    box[i].add(Box.createVerticalStrut(20));
                }
            }
            else if(type == 1) //多选题
            {
                JCheckBox[] multipleSelection = new JCheckBox[4];
                String[] options = temp.getOptions();

                temp.setMultipleSelection(multipleSelection);

                for (int j = 0; j < multipleSelection.length; j++)
                {
                    multipleSelection[j] = new JCheckBox(options[j]);
                    multipleSelection[j].setFont(new Font("宋体", 1, 15));

                    box[i].add(multipleSelection[j]);
                    box[i].add(Box.createVerticalStrut(20));
                }
            }
            else if(type == 2) //主观题
            {
                JTextArea answer = new JTextArea(20, 10);

                temp.setStudentAnswer(answer);

                box[i].add(answer);
                box[i].add(Box.createVerticalStrut(20));
            }
        }

        for (int i = 0; i < box.length; i++) //将行型盒式布局的盒式容器添加到列型盒式的盒式容器
        {
            boxH.add(box[i]);
            boxH.add(Box.createHorizontalStrut(20));
        }

        submit = new JButton("提交");
        submit.setFont(new Font("宋体",1, 15));

        boxH.add(submit);

        scroll = new JScrollPane(boxH); //带滚轮的面板
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scroll, BorderLayout.CENTER);

        bar = new JMenuBar(); //菜单条
        label1 = new JLabel(pageId + "-" + subjectName);
        label1.setFont(new Font("宋体", 1, 15));
        label2 = new JLabel(studentId);
        label2.setFont(new Font("宋体", 1, 15));
        bar.add(label1);
        bar.add(Box.createHorizontalStrut(20));
        bar.add(label2);

        add(bar, BorderLayout.NORTH);
    }

    public void addButtonAction()
    {
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(ExamView.this, "确定提交",
                        "提示框", JOptionPane.YES_NO_OPTION);

                if(option == JOptionPane.YES_OPTION)
                {
                    submitPage(); //提交试卷
                    dispose();
                }
                else
                {
                    return;
                }
            }
        });
    }

    public void addWindowAction()
    {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(ExamView.this, "确定提交",
                        "提示框", JOptionPane.YES_NO_OPTION);

                if(option == JOptionPane.YES_OPTION)
                {
                    submitPage(); //提交试卷
                    dispose();
                }
                else
                {
                    return;
                }
            }
        });
    }

    public void submitPage()
    {
        double pageScore = 0; //试卷总分
        double objectiveScore = 0; //客观题得分

        //存储学生主观题答案 ProbmlemId+Answer
        List<String[]> studentAnswer = new ArrayList<String[]>();

        int length = problems.size();
        for (int i = 0; i < length; i++)
        {
            Problem temp = problems.get(i);
            pageScore += temp.getGrade();

            int type = temp.getProblemType();
            if(type == 0) //单选
            {
                char option = 'A';

                for (int j = 0; j < 4; j++)
                {
                    if(temp.getRadioSelection()[j].isSelected())
                    {
                        option = (char) (option + j);
                        break;
                    }
                }

                if(option == temp.getAnswer().charAt(0))
                {
                    objectiveScore += temp.getGrade();
                }
            }
            else if(type == 1) //多选
            {
                StringBuilder multipleOption = new StringBuilder(7);

                for (int j = 0; j < 4; j++)
                {
                    if(temp.getMultipleSelection()[j].isSelected())
                    {
                        multipleOption.append((char) ('A' + j));
                    }
                }

                if(multipleOption.toString().equals(temp.getAnswer()))
                {
                    objectiveScore += temp.getGrade();
                }
            }
            else if(type == 2)
            {
                studentAnswer.add(new String[]{temp.getProblemId(), temp.getStudentAnswer().getText()});
            }
        }

        //将学生客观题成绩录入到数据库
        SqlStudent.submitObjectiveScore(studentId, pageId, pageScore, objectiveScore);
        //将学生主观题答案录入到数据库
        SqlStudent.submitSubjectiveStudentAnswer(studentId, pageId, studentAnswer);
    }
}
