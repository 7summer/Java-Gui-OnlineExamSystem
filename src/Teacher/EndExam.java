package Teacher;

import Mysql.SqlTeacher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class EndExam extends JFrame {
    String teacherId; //教师编号
    Box boxH = null;
    Box boxOne = null;

    JLabel title = null; //结束考试

    JLabel label1 = null; //科目

    JComboBox subjects = null; //选择考试

    JButton end = null; //结束按钮
    boolean jdgment = true; //判断结束考试界面是否打开

    public EndExam(String teacherId)
    {
        this.teacherId = teacherId;

        init();

        addButtionAction(); //给（结束）按钮添加事件监听器

        addWindowAction(); //给（关闭）按钮添加事件监听器

        setTitle("结束考试界面");
        setVisible(true);
        setBounds(300,300,500,500);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void init()
    {
        boxH = Box.createVerticalBox();

        boxOne = Box.createHorizontalBox();

        title = new JLabel("结束考试");
        title.setFont(new Font("宋体", 1, 40));

        label1 = new JLabel("科目");
        label1.setFont(new Font("宋体", 1, 15));

        subjects = new JComboBox<String>();
        subjects.addItem("请选择考试");
        //试卷号+科目名
        List<String[]> pageIds = SqlTeacher.selectPage(teacherId, 0); //0代表选择正在考试的试卷
        for(int i=0; i<pageIds.size(); i++)
        {
            String[] temp = pageIds.get(i);
            subjects.addItem(temp[0] + "-" + temp[1]);
        }

        end = new JButton("结束");
        end.setFont(new Font("宋体", 1, 15));

        boxOne.add(label1);
        boxOne.add(Box.createHorizontalStrut(20));
        boxOne.add(subjects);

        boxH.add(Box.createVerticalStrut(50));
        boxH.add(title);
        boxH.add(Box.createVerticalStrut(20));
        boxH.add(boxOne);
        boxH.add(Box.createVerticalStrut(20));
        boxH.add(end);

        add(boxH);
    }

    public void addButtionAction()
    {
        end.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(subjects.getSelectedItem().toString().equals("请选择考试"))
                {
                    JOptionPane.showMessageDialog(EndExam.this, "请选择考试",
                            "提示框", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    String temp1 = subjects.getSelectedItem().toString(); //试卷号+科目名
                    String[] temp2 = temp1.split("-"); //试卷号+科目名
                    String pageId = temp2[0];

                    SqlTeacher.endExan(teacherId, pageId);

                    JOptionPane.showMessageDialog(EndExam.this,   temp1 + "考试已结束",
                            "提示框", JOptionPane.INFORMATION_MESSAGE);
                    subjects.removeItem(temp1);
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
