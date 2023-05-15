package Mysql;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStudent extends MySqlDemo{

    /**
     *
     * @param studentId 学生id
     * @return 学生所在班级的班级号
     */
    public static String selectClassId(String studentId)
    {
        PreparedStatement psm = null;
        ResultSet rs = null;

        String classId = null;

        try {
            psm = con.prepareStatement("select * from student where Sno=?");
            psm.setString(1, studentId);

            rs = psm.executeQuery();
            if(rs.next())
            {
                classId = rs.getString("ClassId");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            if(rs != null)
            {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(psm != null)
            {
                try {
                    psm.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            return classId;
        }
    }

    /**
     *
     * @param studentId 学生id
     * @return 返回学生需要考试的试卷 试卷号+科目名
     */
    public static List<String[]> selectExamPage(String studentId)
    {
        String classId = selectClassId(studentId);

        CallableStatement csm = null;
        ResultSet rs = null;

        List<String[]> ans = new ArrayList<String[]>(); //字符串内容为:试卷号+科目名

        try {
            csm = con.prepareCall("call studnetSelectExamPage(?)");
            csm.setString(1, classId);

            rs = csm.executeQuery();
            while(rs.next())
            {
                ans.add(new String[]{rs.getString("PageId"), rs.getString("SubjectName")});
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            if(rs != null)
            {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(csm != null)
            {
                try {
                    csm.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            return ans;
        }
    }

    /**
     *
     * @param pageId 试卷号
     * @param studentId 学生id
     * @return 判断该学生是否已参加考试
     */
    public static boolean whetherExam(String pageId, String studentId)
    {
        PreparedStatement psm = null;
        ResultSet rs = null;

        boolean flag = false;

        try {
            psm = con.prepareStatement("select * from score where Sno=? and PageId=?");
            psm.setString(1, studentId);
            psm.setString(2, pageId);

            rs = psm.executeQuery();
            if(rs.next())
            {
                flag = true;
            }
            else
            {
                flag = false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            if(rs != null)
            {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(psm != null)
            {
                try {
                    psm.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            return flag;
        }
    }

    /**
     * @param pageId 试卷号
     * @param subjectId 科目号
     * @return 返回考试题目
     */
    public static List<Problem> importExamProblem(String pageId, String subjectId)
    {
        List<Problem> problems = new ArrayList<Problem>();

        CallableStatement csm = null;
        ResultSet rs = null;

        try {
            csm = con.prepareCall("call importExamProblem(?, ?)");
            csm.setString(1, pageId);
            csm.setString(2, subjectId);

            rs = csm.executeQuery();

            while(rs.next())
            {
                Problem temp = new Problem();
                temp.setSubjectId(rs.getString("SubjectId"));
                temp.setProblemId(rs.getString("ProblemId"));
                temp.setProblemType(rs.getInt("ProblemType"));
                temp.setProblem(rs.getString("Problem"));
                temp.setGrade(rs.getDouble("grade"));

                int type = temp.getProblemType();
                if(type == 0 || type == 1) //如果题目类型为单选或多选，导入选项和答案
                {
                    importObjectOption(temp);
                }
                problems.add(temp);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if(csm != null)
            {
                try {
                    csm.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            return problems;
        }
    }

    /**
     *
     * @param problem 需要导入选项、答案的题目
     */
    public static void importObjectOption(Problem problem)
    {
        String subjectId = problem.getSubjectId();
        String problemId = problem.getProblemId();

        PreparedStatement psm = null;
        ResultSet rs = null;

        try {
            psm = con.prepareStatement("select * from objectivequestions where SubjectId=? and ProblemId=?");
            psm.setString(1, subjectId);
            psm.setString(2, problemId);

            rs = psm.executeQuery();

            if(rs.next())
            {
                String[] options = new String[4];
                options[0] = new String(rs.getString("Selection1")); //Selection1
                options[1] = new String(rs.getString("Selection2"));
                options[2] = new String(rs.getString("Selection3"));
                options[3] = new String(rs.getString("Selection4"));
                problem.setOptions(options);

                problem.setAnswer(rs.getString("Answer"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            if(rs != null)
            {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(psm != null)
            {
                try {
                    psm.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     *
     * @param studentId 学生id
     * @param pageId 试卷号
     * @param pageScore 试卷本身总分
     * @param objectiveScore 学生客观题得分
     */
    public static void submitObjectiveScore(String studentId, String pageId, double pageScore, double objectiveScore)
    {
        PreparedStatement psm = null;
        String sql = "insert into score values(?, ?, ?, ?, ?)";

        try {
            psm = con.prepareStatement(sql);
            psm.setString(1, studentId);
            psm.setString(2, pageId);
            psm.setDouble(3, pageScore);
            psm.setDouble(4, objectiveScore);
            psm.setDouble(5, 0);

            psm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            if(psm != null)
            {
                try {
                    psm.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     *
     * @param studentId 学生id
     * @param pageId 试卷号
     * @param studentAnswer 存储学生主观题答案
     */
    public static void submitSubjectiveStudentAnswer(String studentId, String pageId, List<String[]> studentAnswer)
    {
        PreparedStatement psm = null;

        String sql = "insert into studentanswer values(?, ?, ?, ?, ?)";

        try {
            psm = con.prepareStatement(sql);

            int length = studentAnswer.size();
            for(int i=0; i<length; i++)
            {
                String[] temp = studentAnswer.get(i);

                psm.setString(1, studentId);
                psm.setString(2, pageId);
                psm.setString(3, temp[0]);
                psm.setString(4, temp[1]);
                psm.setInt(5, 0);

                psm.addBatch();
            }

            psm.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            if(psm != null)
            {
                try {
                    psm.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     *
     * @param studentId 学生编号
     * @return 学生成绩 科目号+科目名+试卷号+试卷总分+成绩
     */
    public static List<String[]> studentQueryScore(String studentId)
    {
        CallableStatement csm = null;
        ResultSet rs = null;

        List<String[]> studentScore = new ArrayList<String[]>();

        try {
            csm = con.prepareCall("call studentQueryScore(?)");
            csm.setString(1, studentId);

            rs = csm.executeQuery();
            while(rs.next())
            {
                String[] temp = new String[5];
                temp[0] = rs.getString("SubjectId"); //科目号
                temp[1] = rs.getString("SubjectName"); //科目名
                temp[2] = rs.getString("PageId"); //试卷编号
                temp[3] = String.valueOf(rs.getDouble("PageScore")); //试卷总分
                temp[4] = String.valueOf(rs.getDouble("ObjectiveScore") + rs.getDouble("SubjectiveScore")); //成绩

                studentScore.add(temp);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if(csm != null)
            {
                try {
                    csm.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            return studentScore;
        }
    }
}
