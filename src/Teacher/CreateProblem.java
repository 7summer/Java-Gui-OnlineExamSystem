package Teacher;

import Mysql.SqlTeacher;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class CreateProblem extends JFrame {
    String teacherId; //存放教师编号
    JPanel panel = null;
    JScrollPane scroll = null;

    Box boxH = null;
    Box boxOne = null, boxTwo = null;
    Box boxThree = null;
    Box boxOption1 = null, boxOption2 = null;
    Box boxOption3 = null, boxOption4 = null;

    JLabel title = null; //制作题目

    JLabel label1 = null; //科目
    JLabel label2 = null; //题目号
    JLabel label3 = null; //题目
    JLabel label4 = null; //类型
    JLabel label5 = null; //答案
    JLabel label6 = null; //分值
    JLabel A = null, B = null, C = null, D = null;

    JComboBox subjects = null; //选择科目
    JTextField problemId = null; //设置题目号
    JTextArea problem = null; //输入问题
    JComboBox type = null; //选择题目类型（单选、多选）
    JTextField answer = null; //设置答案
    JTextField score = null; //设置分值

    JTextField option1 = null; //选项
    JTextField option2 = null;
    JTextField option3 = null;
    JTextField option4 = null;

    JButton submit = null; //提交按钮

    boolean jdgment = true; //判断创建题库界面是否打开
    public CreateProblem(String teacherId)
    {
        this.teacherId = teacherId;

        init();

        addButtionAction(); //给（提交）按钮添加事件监听器
        addWindowAction(); //给（关闭）按钮添加事件监听器

        setTitle("添加题目界面");
        setVisible(true);
        setBounds(300,300,700,500);
        //setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void init()
    {
        panel = new JPanel();

        boxH = Box.createVerticalBox();

        boxOne = Box.createHorizontalBox();
        boxTwo = Box.createHorizontalBox();
        boxThree = Box.createHorizontalBox();

        title = new JLabel("制作题目");
        title.setFont(new Font("宋体", 1, 40));

        label1 = new JLabel("科目");
        label2 = new JLabel("题目号");
        label3 = new JLabel("题目");
        label4 = new JLabel("类型");
        label5 = new JLabel("答案");
        label6 = new JLabel("分值");
        label1.setFont(new Font("宋体", 1, 15));
        label2.setFont(new Font("宋体", 1, 15));
        label3.setFont(new Font("宋体", 1, 15));
        label4.setFont(new Font("宋体", 1, 15));
        label5.setFont(new Font("宋体", 1, 15));
        label6.setFont(new Font("宋体", 1, 15));

        subjects = new JComboBox<String>();
        subjects.addItem("请选择科目");
        List<String> subjectNames = SqlTeacher.selectTeacherSubjects(teacherId); //字符串内容:科目名
        for(int i=0; i<subjectNames.size(); i++)
        {
            subjects.addItem(subjectNames.get(i));
        }

        problemId = new JTextField(10); //题目编号由系统生成

        problem = new JTextArea(10,5);

        type = new JComboBox<String>();
        type.addItem("单选");
        type.addItem("多选");
        type.addItem("主观题");
        answer = new JTextField(10);
        score = new JTextField(10);

        option1 = new JTextField(10);
        option2 = new JTextField(10);
        option3 = new JTextField(10);
        option4 = new JTextField(10);

        submit = new JButton("提交");
        submit.setFont(new Font("宋体",1, 15));

        boxOne.add(Box.createHorizontalStrut(20));
        boxOne.add(label1);
        boxOne.add(Box.createHorizontalStrut(20));
        boxOne.add(subjects);
        boxOne.add(Box.createHorizontalStrut(20));
        boxOne.add(label2);
        boxOne.add(Box.createHorizontalStrut(20));
        boxOne.add(problemId);

        boxTwo.add(Box.createHorizontalStrut(20));
        boxTwo.add(label3);
        boxTwo.add(Box.createHorizontalStrut(20));
        boxTwo.add(problem);


        boxThree.add(Box.createHorizontalStrut(20));
        boxThree.add(label4);
        boxThree.add(Box.createHorizontalStrut(20));
        boxThree.add(type);
        boxThree.add(Box.createHorizontalStrut(20));
        boxThree.add(label5);
        boxThree.add(Box.createHorizontalStrut(20));
        boxThree.add(answer);
        boxThree.add(Box.createHorizontalStrut(20));
        boxThree.add(label6);
        boxThree.add(Box.createHorizontalStrut(20));
        boxThree.add(score);

        boxH.add(Box.createVerticalStrut(50));
        boxH.add(title);
        boxH.add(Box.createVerticalStrut(20));
        boxH.add(boxOne);
        boxH.add(Box.createVerticalStrut(20));
        boxH.add(boxTwo);
        boxH.add(Box.createVerticalStrut(20));
        boxH.add(boxThree);

        A = new JLabel("A");
        A.setFont(new Font("宋体",1, 15));
        B = new JLabel("B");
        B.setFont(new Font("宋体",1, 15));
        C = new JLabel("C");
        C.setFont(new Font("宋体",1, 15));
        D = new JLabel("D");
        D.setFont(new Font("宋体",1, 15));

        boxOption1 = Box.createHorizontalBox();
        boxOption2 = Box.createHorizontalBox();
        boxOption3 = Box.createHorizontalBox();
        boxOption4 = Box.createHorizontalBox();

        boxOption1.add(Box.createHorizontalStrut(20));
        boxOption1.add(A);
        boxOption1.add(Box.createHorizontalStrut(20));
        boxOption1.add(option1);

        boxOption2.add(Box.createHorizontalStrut(20));
        boxOption2.add(B);
        boxOption2.add(Box.createHorizontalStrut(20));
        boxOption2.add(option2);

        boxOption3.add(Box.createHorizontalStrut(20));
        boxOption3.add(C);
        boxOption3.add(Box.createHorizontalStrut(20));
        boxOption3.add(option3);

        boxOption4.add(Box.createHorizontalStrut(20));
        boxOption4.add(D);
        boxOption4.add(Box.createHorizontalStrut(20));
        boxOption4.add(option4);

        boxH.add(Box.createVerticalStrut(20));
        boxH.add(boxOption1);
        boxH.add(Box.createVerticalStrut(20));
        boxH.add(boxOption2);
        boxH.add(Box.createVerticalStrut(20));
        boxH.add(boxOption3);
        boxH.add(Box.createVerticalStrut(20));
        boxH.add(boxOption4);
        boxH.add(Box.createVerticalStrut(20));
        boxH.add(submit);

        scroll = new JScrollPane(boxH);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scroll, BorderLayout.CENTER);
    }

    public void addButtionAction()
    {
        type.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(type.getSelectedItem().toString().equals("主观题"))
                {
                    //题目类型为主观题时，禁入输入选项、答案
                    option1.setEditable(false);
                    option2.setEditable(false);
                    option3.setEditable(false);
                    option4.setEditable(false);
                    answer.setEditable(false);
                }
                else
                {
                    option1.setEditable(true);
                    option2.setEditable(true);
                    option3.setEditable(true);
                    option4.setEditable(true);
                    answer.setEditable(true);
                }
            }
        });

        subjects.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String subjectName = subjects.getSelectedItem().toString();

                if(subjectName.equals("请选择科目"))
                {
                    problemId.setText("请选择科目");
                    problemId.setEditable(false);
                }
                else
                {
                    long count = SqlTeacher.selectSubjectProblemCount(subjectName); //获取该科目在题库中的题目数
                    String temp = String.valueOf(count + 1);
                    int length = temp.length();
                    if(length == 1)
                    {
                        temp = "00" + temp;
                    }
                    else if(length == 2)
                    {
                        temp = "0" + temp;
                    }

                    problemId.setText(temp);
                    problemId.setEditable(false);
                }
            }
        });

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(subjects.getSelectedIndex() == 0 || problem.getText().length() == 0 || score.getText().length() == 0)
                {
                    JOptionPane.showMessageDialog(CreateProblem.this,
                            "请检查信息", "提示框", JOptionPane.WARNING_MESSAGE);
                }
                else if(type.getSelectedIndex() != 2 && (answer.getText().length() == 0 || option1.getText().length() == 0
                        || option2.getText().length() == 0 || option3.getText().length() == 0
                        || option4.getText().length() == 0))
                {
                    JOptionPane.showMessageDialog(CreateProblem.this,
                            "请检查信息", "提示框", JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    int selection = type.getSelectedIndex();
                    int subjectSelection = subjects.getSelectedIndex();
                    String subjectId = null;

                    if(selection == 2) //主观题
                    {
                        subjectId = SqlTeacher.selectSubjectId(subjects.getSelectedItem().toString());
                        //将主观题提交到数据库
                        SqlTeacher.insertSubjectiveQuestion(subjectId, problemId.getText(), type.getSelectedIndex(),
                                problem.getText(), Double.parseDouble(score.getText()));
                    }
                    else //客观题
                    {
                        subjectId = SqlTeacher.selectSubjectId(subjects.getSelectedItem().toString());
                        //将客观题提交到数据库
                        SqlTeacher.insertObjectiveQuestion(subjectId, problemId.getText(), type.getSelectedIndex(),
                                problem.getText(), Double.parseDouble(score.getText()),
                                 answer.getText(), option1.getText(), option2.getText(), option3.getText(), option4.getText());
                    }

                    JOptionPane.showMessageDialog(CreateProblem.this, "插入成功", "提示框", JOptionPane.INFORMATION_MESSAGE);
                    subjects.setSelectedIndex(subjectSelection);

                    problem.setText(null);
                    type.setSelectedIndex(selection);
                    answer.setText(null);
                    score.setText(null);
                    option1.setText(null);
                    option2.setText(null);
                    option3.setText(null);
                    option4.setText(null);
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
