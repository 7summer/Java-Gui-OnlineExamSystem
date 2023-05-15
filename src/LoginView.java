import Teacher.TeacherView;
import Mysql.MySqlDemo;
import Student.StudentView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginView extends JFrame implements ActionListener{
    Box boxH = null; //列式盒
    Box boxOne = null,boxTwo = null; //行式盒
    Box boxThree = null,boxFour = null;  //行式盒

    JLabel title = null;

    JLabel label1 = null;  //用户名
    JLabel label2 = null;  //密码
    JLabel label3 = null; //身份

    JTextField id = null;  //用户名输入
    JPasswordField password = null;  //密码输入

    JComboBox identity = null;  //身份选择（学生、管理员）

    JButton login = null; //登录按钮

    TeacherView managerView = null; //管理员界面
    StudentView studentView = null; //学生界面
    public LoginView()
    {
        init(); //创建登录界面

        login.addActionListener(this); //给（登录）按钮添加事件监听器

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {  //给（窗口关闭）按钮添加监听器

                int option = JOptionPane.showConfirmDialog(LoginView.this, "确定退出系统", "提示",
                        JOptionPane.YES_NO_OPTION);

                if(option == JOptionPane.YES_OPTION)
                {
                    MySqlDemo.disconnectDatabase();
                    dispose();
                    System.exit(0);
                }
                else
                {
                    return;
                }
            }
        });

        setTitle("登录界面");
        setVisible(true);
        setBounds(100,100,500,500);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    void init()
    {
        boxH = Box.createVerticalBox();

        title = new JLabel("欢迎来到考试系统");
        title.setFont(new Font("宋体",1,40));

        boxOne = Box.createHorizontalBox();
        boxTwo = Box.createHorizontalBox();
        boxThree = Box.createHorizontalBox();
        boxFour = Box.createHorizontalBox();

        label1 = new JLabel("用户名：");
        label2 = new JLabel("密码：");
        label3 = new JLabel("身份：");
        label1.setFont(new Font("宋体",1,15));
        label2.setFont(new Font("宋体",1,15));
        label3.setFont(new Font("宋体",1,15));

        id = new JTextField(10);
        password = new JPasswordField(10);
        identity = new JComboBox<String>();
        identity.addItem("学生");
        identity.addItem("老师");

        login = new JButton("登录");
        login.setFont(new Font("宋体",1, 15));

        boxOne.add(label1);
        boxOne.add(Box.createHorizontalStrut(20));
        boxOne.add(id);

        boxTwo.add(label2);
        boxTwo.add(Box.createHorizontalStrut(20));
        boxTwo.add(password);

        boxThree.add(label3);
        boxThree.add(Box.createHorizontalStrut(20));
        boxThree.add(identity);

        boxFour.add(login);

        boxH.add(Box.createVerticalStrut(50));
        boxH.add(title);
        boxH.add(Box.createVerticalStrut(20));
        boxH.add(boxOne);
        boxH.add(Box.createVerticalStrut(20));
        boxH.add(boxTwo);
        boxH.add(Box.createVerticalStrut(20));
        boxH.add(boxThree);
        boxH.add(Box.createVerticalStrut(20));
        boxH.add(boxFour);

        add(boxH);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(identity.getSelectedIndex() == 0) //如果选择学生
        {
            String inputId = id.getText(); //获取用户名
            String inputPassword = new String(password.getPassword());

            if(MySqlDemo.verifyLogin(0, inputId, inputPassword))
            {
                password.setText(null);
                studentView = new StudentView(inputId);
            }
            else
            {
                JOptionPane.showMessageDialog(this, "学号或密码错误",
                        "错误", JOptionPane.WARNING_MESSAGE);
                password.setText(null);
            }
        }
        else if(identity.getSelectedIndex() == 1) //如果选择老师
        {
            String inputId = id.getText(); //获取用户名
            String inputPassword = new String(password.getPassword());

            if(MySqlDemo.verifyLogin(1, inputId, inputPassword))
            {
                password.setText(null);
                managerView = new TeacherView(inputId); //进入老师界面
            }
            else
            {
                JOptionPane.showMessageDialog(this, "老师编号或密码错误",
                        "错误", JOptionPane.WARNING_MESSAGE);
                password.setText(null);
            }
        }
    }
}
