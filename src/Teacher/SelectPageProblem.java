package Teacher;

import Mysql.SqlTeacher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

//选择题目评分界面
public class SelectPageProblem extends JFrame {
    String teacherId; //存放教师编号
    Box boxH = null;
    Box boxOne = null;

    JLabel title = null; //请选择需要评分的题目
    JLabel label1 = null; //题目
    JComboBox selectProblem = null; //选择题目
    JButton sure = null; //确定按钮
    boolean jdgment = true; //判断评分

    public SelectPageProblem(String teacherId)
    {
        this.teacherId = teacherId;

        init();

        addButtionAction(); //给（结束）按钮添加事件监听器

        addWindowAction(); //给（关闭）按钮添加事件监听器

        setTitle("选择题目评分界面");
        setVisible(true);
        setBounds(300,300,500,500);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void init()
    {
        boxH = Box.createVerticalBox();

        boxOne = Box.createHorizontalBox();

        title = new JLabel("选择评分题目");
        title.setFont(new Font("宋体", 1, 40));

        label1 = new JLabel("题目");
        label1.setFont(new Font("宋体", 1, 15));

        selectProblem = new JComboBox<String>();
        selectProblem.addItem("请选择题目");
        List<String[]> problems = SqlTeacher.selectGradeSubjectiveProblem(teacherId); //试卷号+题目号
        int length = problems.size();
        for(int i=0; i<length; i++)
        {
            String[] temp = problems.get(i);
            selectProblem.addItem(temp[0] + "-" + temp[1]);
        }

        sure = new JButton("确定");
        sure.setFont(new Font("宋体", 1, 15));

        boxOne.add(label1);
        boxOne.add(Box.createHorizontalStrut(20));
        boxOne.add(selectProblem);

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
            public void actionPerformed(ActionEvent e)
            {
                if(selectProblem.getSelectedIndex() == 0)
                {
                    JOptionPane.showMessageDialog(SelectPageProblem.this, "请选择题目",
                            "提示框", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    String[] temp = selectProblem.getSelectedItem().toString().split("-");
                    String pageId = temp[0];
                    String problemId = temp[1];

                    jdgment = false;
                    setVisible(false);

                    SubjectiveScore subjectiveScore = new SubjectiveScore(teacherId, pageId, problemId);
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
