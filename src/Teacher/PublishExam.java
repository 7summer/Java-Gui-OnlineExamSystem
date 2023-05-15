package Teacher;

import Mysql.SqlTeacher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;

public class PublishExam extends JFrame {
    String teacherId; //存放教师编号
    Box boxH = null;
    Box boxOne = null, boxTwo = null;
    Box boxThree = null;

    JLabel title = null; //发布考试

    JLabel label1 = null; //试卷号
    JLabel label2 = null; //科目
    JLabel label3 = null; //题目数
    JTextField maxProblemNumber = null; //查看该科目的题量

    JTextField pageId = null; //试卷号
    JComboBox subjects = null; //选择科目
    JTextField problemNumber = null; //获取题目数

    MultiComboBox selectClass = null; //下拉列表多选框

    JButton release = null; //发布按钮

    boolean jdgment = true; //判断发布考试界面是否打开

    long count; //记录该科目在题库中有多少道题
    public PublishExam(String teacherId)
    {
        this.teacherId = teacherId;

        init();

        addAction(); //给某些部件添加事件监听器
        addWindowAction(); //给关闭按钮添加事件监听器

        setTitle("发布考试界面");
        setVisible(true);
        setBounds(300, 300, 500, 500);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void init()
    {
        boxH = Box.createVerticalBox();

        boxOne = Box.createHorizontalBox();
        boxTwo = Box.createHorizontalBox();
        boxThree = Box.createVerticalBox();

        title = new JLabel("发布考试");
        title.setFont(new Font("宋体", 1, 40));

        label1 = new JLabel("试卷号");
        label2 = new JLabel("科目");
        label3 = new JLabel("题目数");
        label1.setFont(new Font("宋体",1, 15));
        label2.setFont(new Font("宋体",1, 15));
        label3.setFont(new Font("宋体",1, 15));

        pageId = new JTextField(10);

        subjects = new JComboBox<String>();
        subjects.addItem("请选择科目");
        List<String> subjectNames = SqlTeacher.selectTeacherSubjects(teacherId); //字符串内容:科目名
        for(int i=0; i<subjectNames.size(); i++)
        {
            subjects.addItem(subjectNames.get(i));
        }

        problemNumber = new JTextField(10);
        maxProblemNumber = new JTextField(15);
        maxProblemNumber.setEditable(false);

        selectClass = new MultiComboBox(new String[]{"全选"}); //一个下拉列表多选框

        release = new JButton("发布");
        release.setFont(new Font("宋体", 1, 15));

        boxOne.add(label1);
        boxOne.add(Box.createHorizontalStrut(20));
        boxOne.add(pageId);
        boxOne.add(Box.createHorizontalStrut(20));
        boxOne.add(label2);
        boxOne.add(Box.createHorizontalStrut(20));
        boxOne.add(subjects);

        boxTwo.add(label3);
        boxTwo.add(Box.createHorizontalStrut(20));
        boxTwo.add(problemNumber);
        boxTwo.add(Box.createHorizontalStrut(20));
        boxTwo.add(maxProblemNumber);

        boxH.add(Box.createVerticalStrut(50));
        boxH.add(title);
        boxH.add(Box.createVerticalStrut(20));
        boxH.add(boxOne);
        boxH.add(Box.createVerticalStrut(20));
        boxH.add(boxTwo);
        boxH.add(Box.createVerticalStrut(20));
        boxH.add(boxThree);
        boxH.add(Box.createVerticalStrut(20));
        boxH.add(selectClass);
        boxH.add(Box.createVerticalStrut(20));
        boxH.add(release);

        add(boxH);
    }

    public void addAction()
    {
        subjects.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String subjectName = subjects.getSelectedItem().toString();
                if(subjectName.equals("请选择科目"))
                {
                    maxProblemNumber.setText(null);

                    selectClass.readdItem(new String[]{"全选"}); //下拉列表多选框重新载入选项
                }
                else
                {
                    count = SqlTeacher.selectSubjectProblemCount(subjectName); //该科目在题库中的题目数
                    maxProblemNumber.setText(e.getItem().toString() + "有"
                            + count + "道题目");

                    List<String> classes = SqlTeacher.selectTeacherSubjectClass(teacherId, subjectName); //班级名
                    classes.add(0, "全选");
                    selectClass.readdItem(classes.toArray()); //下拉列表多选框重新载入选项
                }
            }
        });

        release.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int length = pageId.getText().length();
                if(length == 0)
                {
                    JOptionPane.showMessageDialog(PublishExam.this,
                            "试卷号不能为空", "提示框", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(length != 5)
                {
                    JOptionPane.showMessageDialog(PublishExam.this,
                            "试卷号长度为5", "提示框", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(SqlTeacher.verifyPageId(pageId.getText()))
                {
                    JOptionPane.showMessageDialog(PublishExam.this,
                            "该试卷号已存在", "提示框", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(subjects.getSelectedItem().toString().equals("请选择科目"))
                {
                    JOptionPane.showMessageDialog(PublishExam.this, "请选择科目",
                            "提示框", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                length = problemNumber.getText().length();
                if(length == 0)
                {
                    JOptionPane.showMessageDialog(PublishExam.this,
                            "题目数不能为空", "提示框", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                long num = Long.valueOf(problemNumber.getText());
                if(num<=0 || num>count)
                {
                    JOptionPane.showMessageDialog(PublishExam.this,
                            "检查题目数", "提示框", JOptionPane.WARNING_MESSAGE);
                    problemNumber.setText(null);
                    return;
                }

                String[] selectedClass = Arrays.stream(selectClass.getSelectedValues()).toArray(String[]::new);
                /*
                Object[] objectArray = { "A", "B", "C" };
                String stringArray[] = Arrays.stream(objectArray).toArray(String[]::new);
                 */
                if(selectedClass.length == 0)
                {
                    JOptionPane.showMessageDialog(PublishExam.this,
                            "请选择考试班级", "提示框", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                SqlTeacher.releaseExam(teacherId, subjects.getSelectedItem().toString(), pageId.getText(), selectedClass, num);
                JOptionPane.showMessageDialog(PublishExam.this,
                        "发布成功", "提示框", JOptionPane.INFORMATION_MESSAGE);

                pageId.setText(null);
                problemNumber.setText(null);
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
