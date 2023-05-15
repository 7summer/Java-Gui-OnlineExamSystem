package Student;

import Mysql.SqlStudent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class QueryScore extends JFrame {
    JTable table = null; //成绩表
    JScrollPane scrollPane = null;

    String studentId; //记录学生学号
    public QueryScore(String studentId)
    {
        this.studentId = studentId;

        init();

        setTitle("查询成绩界面");
        setVisible(true);
        setBounds(300,300,700,700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void init()
    {
        //表单标签
        String[] column = {"科目号","科目名", "试卷号", "试卷总分", "成绩"};

        //学生成绩 科目号+科目名+试卷号+试卷总分+成绩
        List<String[]> studentScore = SqlStudent.studentQueryScore(studentId);

        int length = studentScore.size();
        String[][] data = new String[length][5];
        for(int i=0; i<length; i++)
        {
            String[] temp = studentScore.get(i);
            for(int j=0; j<5; j++)
            {
                data[i][j] = temp[j];
            }
        }

        table = new JTable(data, column);
        table.setEnabled(false);

        scrollPane = new JScrollPane(table); //带滚轮的表格

        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }
}
