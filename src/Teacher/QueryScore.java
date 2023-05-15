package Teacher;

import Mysql.SqlTeacher;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class QueryScore extends JFrame {
    String teacherId;
    JTabbedPane tab = null; //试卷选项卡
    JTable table[] = null; //成绩表
    public QueryScore(String teacherId)
    {
        this.teacherId = teacherId;

        init();

        setTitle("查询成绩界面");
        setVisible(true);
        setBounds(300,300,700,700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void init()
    {
        tab = new JTabbedPane(); //创建科目选项卡窗口

        //试卷号+科目名
        List<String[]> teacherPage = SqlTeacher.selectPage(teacherId, 1); //1代表选择已结束考试的试卷
        int length = teacherPage.size();

        table = new JTable[length];

        String[] column = {"班级号", "班名", "学号", "姓名", "成绩"};

        for(int i=0; i<length; i++)
        {
            //试卷号+科目名
            String[] temp = teacherPage.get(i);
            //班级号+班名+学号+姓名+成绩
            List<String[]> score = SqlTeacher.teacherQueryScore(temp[0]);
            int scorelength = score.size();

            String[][] data = new String[score.size()][5];

            for(int j=0; j<scorelength; j++)
            {
                String[] studentScore = score.get(j);

                for(int k=0; k<5; k++)
                {
                    data[j][k] = studentScore[k];
                }
            }

            table[i] = new JTable(data, column);
            table[i].setEnabled(false);

            JScrollPane scrollPane = new JScrollPane(table[i]);

            tab.add(temp[0]+"-"+temp[1], scrollPane);
        }

        add(tab, BorderLayout.CENTER);
    }
}
