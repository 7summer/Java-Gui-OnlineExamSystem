package Student;

import Mysql.Problem;
import Mysql.SqlTeacher;
import Mysql.SqlStudent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class SelectExamView extends JFrame {
    String studentId; //存储学生学号

    JLabel title = null; //选择考试

    Box boxH = null;
    Box boxOne = null;

    JLabel label1 = null; //待考科目

    JComboBox exams = null; //选择科目

    JButton sure = null; //（确定）按钮

    boolean jdgment = true;

    public SelectExamView(String id)
    {
        studentId = new String(id);

        init();

        addButtionAction(); //给（确定）按钮添加事件监听器
        addWindowAction(); //给（关闭）按钮添加事件监听器

        setTitle("选择考试界面");
        setVisible(true);
        setBounds(300,300,500,500);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void init()
    {
        boxH = Box.createVerticalBox();

        boxOne = Box.createHorizontalBox();

        title = new JLabel("考试");
        title.setFont(new Font("宋体",1,40));

        label1 = new JLabel("待考试卷");
        label1.setFont(new Font("宋体", 1, 15));

        exams = new JComboBox<String>();
        exams.addItem("请选择考试");
        List<String[]> ans = SqlStudent.selectExamPage(studentId); //试卷号+科目名
        for(int i=0; i<ans.size(); i++)
        {
            String[] temp = ans.get(i);
            exams.addItem(temp[0] + "-" + temp[1]);
        }

        sure = new JButton("确定");
        sure.setFont(new Font("宋体", 1, 15));

        boxOne.add(label1);
        boxOne.add(Box.createHorizontalStrut(20));
        boxOne.add(exams);

        boxH.add(Box.createVerticalStrut(50));
        boxH.add(title);
        boxH.add(Box.createVerticalStrut(20));
        boxH.add(boxOne);
        boxH.add(Box.createVerticalStrut(20));
        boxH.add(sure);

        add(boxH);
    }

    public void addButtionAction()
    {
        sure.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(exams.getSelectedIndex() == 0)
                {
                    JOptionPane.showMessageDialog(SelectExamView.this, "请选择考试",
                            "提示框", JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    String[] temp = exams.getSelectedItem().toString().split("-"); //5位试卷号+科目名
                    String pageId = temp[0]; //获取试卷号
                    String subjectName = temp[1]; //获取科目名
                    String subjectId = SqlTeacher.selectSubjectId(subjectName); //获取科目id

                    if(SqlStudent.whetherExam(pageId, studentId)) //判断学生是否已参加该场考试
                    {
                        JOptionPane.showMessageDialog(SelectExamView.this, "你已参加该场考试",
                              "提示框", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else
                    {
                        jdgment = false; //进入考试界面后，自动退出选择考试界面
                        setVisible(false);

                        ExamView examView = new ExamView(studentId, pageId, subjectId, subjectName);
                    }
                }
            }
        });
    }

    public void addWindowAction()
    {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                jdgment = false;
            }
        });
    }
}

