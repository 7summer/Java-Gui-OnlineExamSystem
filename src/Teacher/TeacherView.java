package Teacher;

import Mysql.SqlTeacher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class TeacherView extends JFrame implements ActionListener{
    String teacherId = null; //存储教师编号

    JMenuBar bar = null; //菜单条
    JMenu menu = null; //菜单

    JMenuItem jMenuItem1 = null; //添加题目

    JMenu subMenu = null; //考试
    JMenuItem jMenuItem2 = null; //发布考试
    JMenuItem jMenuItem3 = null; //结束考试

    JMenuItem jMenuItem4 = null; //主观题评分
    JMenuItem jMenuItem5 = null; //查看成绩

    CreateProblem createProblem = null; //录入题目界面
    PublishExam publishExam = null; //发布考试界面
    EndExam endExam = null; //结束考试界面
    SelectPageProblem selectPageProblem = null; //主观题评分界面

    QueryScore queryScore = null; //查询成绩界面
    public TeacherView(String id) //manager代表管理员号
    {
        teacherId = new String(id);

        init();

        jMenuItem1.addActionListener(this);
        //（添加题目）菜单项添加监听器
        jMenuItem2.addActionListener(this);
        //（发布考试）菜单项添加监听器
        jMenuItem3.addActionListener(this);
        //（结束考试）菜单项添加监听器
        jMenuItem4.addActionListener(this);
        //（主观题评分）菜单项添加监听器
        jMenuItem5.addActionListener(this);
        //（查看成绩）菜单项添加监听器

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {  //给（窗口关闭）按钮添加监听器

                int option = JOptionPane.showConfirmDialog(TeacherView.this,
                        "确定退出教师界面", "提示",
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

        setTitle("教师" + teacherId);
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

        jMenuItem1 = new JMenuItem("添加题目");

        subMenu = new JMenu("考试");
        jMenuItem2 = new JMenuItem("发布考试");
        jMenuItem3 = new JMenuItem("结束考试");
        subMenu.add(jMenuItem2);
        subMenu.add(jMenuItem3);

        jMenuItem4 = new JMenuItem("主观题评分");
        jMenuItem5 = new JMenuItem("查询成绩");

        menu.add(jMenuItem1);
        menu.add(subMenu);
        menu.add(jMenuItem4);
        menu.add(jMenuItem5);

        bar.add(menu);

        setJMenuBar(bar);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == jMenuItem1)
            //添加题目
        {
            if(createProblem == null)
            {
                createProblem = new CreateProblem(teacherId);
            }
            else if(createProblem.jdgment == false)
            {
                createProblem.jdgment = true;
                createProblem.setVisible(true);
            }
            else
            {
                return;
            }
        }

        else if(e.getSource() == jMenuItem2)
            //发布考试
        {
            if(publishExam == null)
            {
                publishExam = new PublishExam(teacherId);
            }
            else if(publishExam.jdgment == false)
            {
                publishExam.jdgment = true;
                publishExam.setVisible(true);
            }
            else
            {
                return;
            }
        }
        else if(e.getSource() == jMenuItem3)
            //结束考试
        {
            if(endExam == null)
            {
                endExam = new EndExam(teacherId);
            }
            else if(endExam.jdgment == false)
            {
                endExam.subjects.removeAllItems();
                endExam.subjects.addItem("请选择考试");
                //试卷号+科目名
                List<String[]> pageIds = SqlTeacher.selectPage(teacherId, 0); //0代表选择正在考试的试卷
                for(int i=0; i<pageIds.size(); i++)
                {
                    String[] temp = pageIds.get(i);
                    endExam.subjects.addItem(temp[0] + "-" + temp[1]);
                }

                endExam.jdgment = true;
                endExam.setVisible(true);
            }
            else
            {
                return;
            }
        }
        else if(e.getSource() == jMenuItem4)
            //客观题评分
        {
            if(selectPageProblem == null)
            {
                selectPageProblem = new SelectPageProblem(teacherId);
            }
            else if(selectPageProblem.jdgment == false)
            {
                selectPageProblem.selectProblem.removeAllItems();
                selectPageProblem.selectProblem.addItem("请选择题目");
                //试卷号+题目号
                List<String[]> problems = SqlTeacher.selectGradeSubjectiveProblem(teacherId);
                int length = problems.size();
                for (int i = 0; i < length; i++)
                {
                    String[] temp = problems.get(i);
                    selectPageProblem.selectProblem.addItem(temp[0] + "-" + temp[1]);
                }

                selectPageProblem.jdgment = true;
                selectPageProblem.setVisible(true);
            }
            else
            {
                return;
            }
        }

        else if(e.getSource() == jMenuItem5)
        //查询成绩界面
        {
            QueryScore queryScore = new QueryScore(teacherId);
        }
    }

    public void dispose()
    {
        if(createProblem != null)
        {
            createProblem.dispose();
        }

        if(publishExam != null)
        {
            publishExam.dispose();
        }

        if(endExam != null)
        {
            endExam.dispose();
        }

        if(selectPageProblem != null)
        {
            selectPageProblem.dispose();
        }

        if(queryScore != null)
        {
            queryScore.dispose();
        }
        super.dispose();
    }
}
