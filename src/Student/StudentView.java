package Student;

import Mysql.SqlStudent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class StudentView extends JFrame implements ActionListener {
    String studentId = null; //存储学生学号

    JMenuBar bar = null; //菜单条
    JMenu menu = null; //菜单

    JMenuItem jMenuItem1 = null; //考试
    JMenuItem jMenuItem2 = null; //成绩

    SelectExamView examView = null; //选择考试界面
    QueryScore queryScore = null; //查询成绩界面

    public StudentView(String id) //student代表学号
    {
        studentId = new String(id);

        init();

        jMenuItem1.addActionListener(this);
        //（考试）菜单项添加监听器
        jMenuItem2.addActionListener(this);
        //（成绩）菜单项添加监听器

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {  //给（窗口关闭）按钮添加监听器
                int option = JOptionPane.showConfirmDialog(StudentView.this,
                        "确定退出学生界面", "提示",
                        JOptionPane.YES_NO_OPTION);

                if(option == JOptionPane.YES_OPTION)
                {
                    dispose();
                }
                else
                {
                    return;
                }
            }
        });

        setTitle("学生" + studentId);
        setVisible(true);
        setBounds(200,200,500,500);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        //退出该界面时把跟它相关的界面关闭
    }

    public void init()
    {
        bar = new JMenuBar();

        menu = new JMenu("菜单");

        jMenuItem1 = new JMenuItem("考试");
        jMenuItem2 = new JMenuItem("成绩");

        menu.add(jMenuItem1);
        menu.add(jMenuItem2);

        bar.add(menu);

        setJMenuBar(bar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jMenuItem1)
            //选择考试
        {
            if(examView == null)
            {
                examView = new SelectExamView(studentId);
            }
            else if(examView.jdgment == false)
            {
                examView.exams.removeAllItems();
                examView.exams.addItem("请选择考试");
                List<String[]> ans = SqlStudent.selectExamPage(studentId); //试卷号+科目名
                for(int i=0; i<ans.size(); i++)
                {
                    String[] temp = ans.get(i);
                    examView.exams.addItem(temp[0] + "-" + temp[1]);
                }

                examView.jdgment = true;
                examView.setVisible(true);
            }
            else
            {
                return;
            }
        }
        else if(e.getSource() == jMenuItem2)
            //查询考试成绩
        {
            QueryScore queryScore = new QueryScore(studentId);
        }
    }

    public void dispose()
    {
        if(examView != null)
        {
            examView.dispose();
        }

        if(queryScore != null)
        {
            queryScore.dispose();
        }

        super.dispose();
    }
}
