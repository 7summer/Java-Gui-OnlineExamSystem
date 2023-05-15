package Mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlTeacher extends MySqlDemo {
    /**
     *
     * @param id 存储教师编号
     * @return 返回该教师执教科目的科目名
     */
    public static List<String> selectTeacherSubjects(String id) {
        CallableStatement csm = null;
        ResultSet rs = null;
        List<String> subjectNames = new ArrayList<String>(); //字符串内容:科目名

        try {
            csm = con.prepareCall("{call selectTeacherSubjects(?)}");
            csm.setString(1, id);

            rs = csm.executeQuery();
            while (rs.next()) {
                subjectNames.add(rs.getString("subjectName"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                csm.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return subjectNames;
        }
    }

    /**
     *
     * @param subjectName 科目名
     * @return 返回该科目在题库中的题目数
     */
    public static long selectSubjectProblemCount(String subjectName) {
        CallableStatement csm = null;
        long count = 0;

        try {
            csm = con.prepareCall("{call selectSubjectProblemCount(?,?)}");
            csm.setString(1, subjectName);
            csm.registerOutParameter(2, Types.BIGINT);

            csm.execute();
            count = csm.getLong(2);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                csm.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return count;
    }

    /**
     *
     * @param subjectName 科目名
     * @return 返回科目编号
     */
    public static String selectSubjectId(String subjectName)
    {
        PreparedStatement psm = null;
        ResultSet rs = null;
        String subjectId = null;

        try {
            psm = con.prepareStatement("select SubjectId from subject where SubjectName=?");
            psm.setString(1, subjectName);

            rs = psm.executeQuery();

            if(rs.next())
            {
                subjectId = rs.getString("SubjectId");
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
            if(psm != null) {
                try {
                    psm.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            return subjectId;
        }
    }
    /**
     * 这些参数都是questionbank表需要的数据
     * @param subjectId 科目号
     * @param problemId 科目名
     * @param problmeType 题目类型
     * @param problem 题目
     * @param grade 分值
     */
    public static void insertSubjectiveQuestion(String subjectId, String problemId, int problmeType, String problem, double grade)
    {
        PreparedStatement psm = null;

        String sql = "insert into questionbank values(?, ?, ?, ?, ?)";

        try {
            psm = con.prepareStatement(sql);
            psm.setString(1, subjectId);
            psm.setString(2, problemId);
            psm.setInt(3, problmeType);
            psm.setString(4, problem);
            psm.setDouble(5, grade);

            psm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            if(psm != null) {
                try {
                    psm.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 这些参数都是questionbank,objectivequestions表需要的数据
     * @param subjectId
     * @param problemId
     * @param problemType
     * @param problem
     * @param grade
     * @param option1 A
     * @param option2 B
     * @param option3 C
     * @param option4 D
     */
    public static void insertObjectiveQuestion(String subjectId, String problemId, int problemType, String problem, double grade,
                                               String answer ,String option1, String option2, String option3, String option4)
    {
        PreparedStatement psm = null;
        String sql1 = "insert into questionbank values(?, ?, ?, ?, ?)";
        String sql2 = "insert into objectivequestions values(?, ?, ?, ?, ?, ?, ?)";

        try {
            psm = con.prepareStatement(sql1);
            psm.setString(1, subjectId);
            psm.setString(2, problemId);
            psm.setInt(3, problemType);
            psm.setString(4, problem);
            psm.setDouble(5, grade);

            psm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            if(psm != null) {
                try {
                    psm.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        try {
            psm = con.prepareStatement(sql2);
            psm.setString(1, subjectId);
            psm.setString(2, problemId);
            psm.setString(3, answer);
            psm.setString(4, option1);
            psm.setString(5, option2);
            psm.setString(6, option3);
            psm.setString(7, option4);

            psm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            if(psm != null) {
                try {
                    psm.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /***
     *
     * @param teacherId 存储教师编号
     * @param subjectName 存储科目名
     * @return 返回教师执教科目的班级名
     */
    public static List<String> selectTeacherSubjectClass(String teacherId, String subjectName)
    {
        List<String> classes = new ArrayList<String>();

        CallableStatement csm = null;
        ResultSet rs = null;

        try {
            csm = con.prepareCall("call selectTeacherSubjectClass(?,?)");
            csm.setString(1, teacherId);
            csm.setString(2, subjectName);

            rs = csm.executeQuery();
            while(rs.next())
            {
                classes.add(rs.getString("ClassName"));
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

            return classes;
        }
    }

    /**
     *
     * @param pageId 教师输入的试卷号
     * @return 判断教师输入的试卷号是否存在，确保试卷号的唯一性
     */
    public static boolean verifyPageId(String pageId)
    {
        CallableStatement csm = null;
        ResultSet rs = null;

        boolean flag = false;

        try {
            csm = con.prepareCall("call verifyPageId(?)");
            csm.setString(1, pageId);

            rs = csm.executeQuery();

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

            if(csm != null)
            {
                try {
                    csm.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            return flag;
        }
    }

    /**
     *
     * @param teacherId 教师编号
     * @param subjectName 科目名
     * @param pageId 试卷编号
     * @param num 题目数
     * @param classNames 班级名
     */
    public static void releaseExam(String teacherId, String subjectName, String pageId, String[] classNames, long num)
    {
        String subjectId = selectSubjectId(subjectName); //科目号
        String[] classIds = selectClassId(classNames); //考试的班级号
        Problem[] problems = pickRondomProblem(subjectId, num); //考试题目

        PreparedStatement psm = null;

        try {
            psm = con.prepareStatement("insert into page values(?,?,?,?)");

            for(Problem problem : problems)
            {
                psm.setString(1, pageId);
                psm.setString(2, subjectId);
                psm.setString(3, problem.getProblemId());
                psm.setInt(4, problem.getProblemType());

                psm.addBatch();
            }
            psm.executeBatch();
            psm.clearBatch();
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

        try {
            psm = con.prepareStatement("insert into groupvolumes values(?,?,?,?,?)");

            for(String classId : classIds)
            {
                psm.setString(1, pageId);
                psm.setString(2, teacherId);
                psm.setString(3, classId);
                psm.setString(4, subjectId);
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
     * @param classNames
     * @return 返回班级名对应的班级号
     */
    public static String[] selectClassId(String[] classNames)
    {
        PreparedStatement psm = null;
        ResultSet rs = null;

        String[] classIds = new String[classNames.length];

        StringBuffer sql = new StringBuffer();
        sql.append("select ClassId from class where ClassName in (?");

        for(int i=1; i<classNames.length; i++)
        {
            sql.append(",?");
        }
        sql.append(')');

        try {
            psm = con.prepareStatement(sql.toString());

            for(int i=1; i<=classNames.length; i++)
            {
                psm.setString(i, classNames[i-1]);
            }

            int i = 0;
            rs = psm.executeQuery();
            while(rs.next())
            {
                classIds[i++] = rs.getString("ClassId");
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

            return classIds;
        }
    }

    /**
     *
     * @param subjectId 科目号
     * @param num 题目数
     * @return 返回随机挑选的题目
     */
    public static Problem[] pickRondomProblem(String subjectId, long num)
    {
        Problem[] problems = new Problem[(int)num];

        PreparedStatement psm = null;
        ResultSet rs = null;

        try {
            psm = con.prepareStatement("select * from questionbank where SubjectId=? order by rand() limit ?");
            psm.setString(1, subjectId);
            psm.setInt(2, (int)num);

            int i = 0; //作为problem数组的下标
            rs = psm.executeQuery();
            while(rs.next())
            {
                problems[i] = new Problem();

                problems[i].setSubjectId(rs.getString("SubjectId"));
                problems[i].setProblemId(rs.getString("ProblemId"));
                problems[i].setProblemType(rs.getInt("ProblemType"));
                problems[i].setProblem(rs.getString("Problem"));
                problems[i].setGrade(rs.getDouble("grade"));

                i++;
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

            return problems;
        }
    }

    /**
     *
     * @param teacherId 教师编号
     * @param state 表示试卷的状态 0为考试状态 1为结束状态
     * @return 返回教师发布考试的试卷号+科目名
     */
    public static List<String[]> selectPage(String teacherId, int state)
    {
        List<String[]> pageIds = new ArrayList<String[]>();

        CallableStatement csm = null;
        ResultSet rs = null;

        try {
            csm = con.prepareCall("call teacherselectExamPage(?, ?)");
            csm.setString(1, teacherId);
            csm.setInt(2, state);

            rs = csm.executeQuery();

            while(rs.next())
            {
                pageIds.add(new String[]{rs.getString("PageId"), rs.getString("SubjectName")});
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

            return pageIds;
        }
    }

    /**
     *
     * @param teacherId 教师编号
     * @param pageId 试卷号
     */
    public static void endExan(String teacherId, String pageId)
    {
        PreparedStatement psm = null;

        try {
            //groupvolumes表state=1代表该试卷考试结束
            psm = con.prepareStatement("update groupvolumes set state=1 where Tno=? and PageId=?");
            psm.setString(1, teacherId);
            psm.setString(2, pageId);

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
     * @param teacherId 教师编号
     * @return 试卷号+题目号
     */
    public static List<String[]> selectGradeSubjectiveProblem(String teacherId)
    {
        List<String[]> problems = new ArrayList<String[]>();
        List<String> pageIds = selectGradePageId(teacherId);

        PreparedStatement psm = null;
        ResultSet rs = null;

        int length = pageIds.size();
        for(int i=0; i<length; i++)
        {
            String pageId = pageIds.get(i);

            try {
                //studnetanswer表中state=0代表该主观题还未评分
                psm = con.prepareStatement("select distinct ProblemId from studentanswer where PageId=? and state=0");
                psm.setString(1, pageId);

                rs = psm.executeQuery();
                while(rs.next())
                {
                    problems.add(new String[]{pageId, rs.getString("ProblemId")});
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

        return problems;
    }

    /**
     *
     * @param teacherId 教师编号
     * @return 返回教师需要批改试卷的试卷号
     */
    public static List<String> selectGradePageId(String teacherId)
    {
        PreparedStatement psm = null;
        ResultSet rs = null;

        List<String> pageIds = new ArrayList<String>();

        try {
            //groupvolumes表中state=1代表该试卷已结束考试
            psm = con.prepareStatement("select distinct PageId from groupvolumes where Tno=? and state=1");
            psm.setString(1, teacherId);

            rs = psm.executeQuery();

            while(rs.next())
            {
                pageIds.add(rs.getString("PageId"));
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

            return pageIds;
        }
    }

    /**
     *
     * @param pageId 试卷号
     * @param problemId 题目编号
     * @return 返回试卷中题目编号对应的主观题的学生答案 学号+学生答案
     */
    public static List<String[]> selectSubjectiveStudentAnswer(String pageId, String problemId)
    {
        PreparedStatement psm = null;
        ResultSet rs = null;

        List<String[]> studentAnswers = new ArrayList<String[]>();

        try {
            //studentanswer表state=0代表学生主观题还未评分
            psm = con.prepareStatement("select * from studentanswer where PageId=? and ProblemId=? and state=0");
            psm.setString(1, pageId);
            psm.setString(2, problemId);

            rs = psm.executeQuery();
            while(rs.next())
            {
                studentAnswers.add(new String[]{rs.getString("Sno"), rs.getString("Answer")});
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

            return studentAnswers;
        }
    }

    /**
     *
     * @param pageId 试卷号
     * @param problemId 题目号
     * @return 返回题目+分值
     */
    public static String[] queryProblemAccordingProblemId(String pageId, String problemId)
    {
        CallableStatement csm = null;
        ResultSet rs = null;

        String[] problem = null;

        try {
            csm = con.prepareCall("call queryProblemAccordingProblemId(?, ?)");
            csm.setString(1, pageId);
            csm.setString(2, problemId);

            rs = csm.executeQuery();
            if(rs.next())
            {
                problem = new String[]{rs.getString("Problem"), String.valueOf(rs.getDouble("grade"))};
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

            return problem;
        }
    }

    /**
     *
     * @param pageId 试卷号
     * @param studentSubjectiveProblemScore 学号+主观题得分
     * @return score表中的SubjectiveScore列
     */
    public static List<Double> queryStudentSubjectiveScore(String pageId, List<String[]> studentSubjectiveProblemScore)
    {
        PreparedStatement psm = null;
        ResultSet rs = null;

        List<Double> studnetSubjectiveScore = new ArrayList<Double>();

        int length = studentSubjectiveProblemScore.size();
        for(int i=0; i<length; i++)
        {
            String[] temp = studentSubjectiveProblemScore.get(i);

            try {
                psm = con.prepareStatement("select * from score where Sno=? and PageId=?");
                psm.setString(1, temp[0]);
                psm.setString(2, pageId);

                rs = psm.executeQuery();
                if(rs.next())
                {
                    studnetSubjectiveScore.add(rs.getDouble("SubjectiveScore"));
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

        return studnetSubjectiveScore;
    }

    /**
     *
     * @param pageId 试卷号
     * @param studentSubjectiveProblemScore 学生主观题分数 学号+得分
     * @param studnetSubjectiveScore score表中的SubjectiveScore列
     */
    public static void submitStudentSubjectiveProblemGrade(String pageId, List<String[]> studentSubjectiveProblemScore
    ,List<Double> studnetSubjectiveScore)
    {
        PreparedStatement psm = null;

        try {
            psm = con.prepareStatement("update score set SubjectiveScore=? where Sno=? and PageId=?");

            int length = studentSubjectiveProblemScore.size();
            for(int i=0; i<length; i++)
            {
                String[] temp = studentSubjectiveProblemScore.get(i);
                double score = studnetSubjectiveScore.get(i);

                psm.setDouble(1, Double.parseDouble(temp[1])+score);
                psm.setString(2, temp[0]);
                psm.setString(3, pageId);

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
     * @param pageId 试卷号
     * @param problemId 题目号
     * @param studentSubjectiveProblemScore 学生主观题分数 学号+得分
     */
    public static void updateStudentAnswerState(String pageId, String problemId, List<String[]> studentSubjectiveProblemScore)
    {
        PreparedStatement psm = null;

        try {
            //studentanswer表state=1代表该题已评分
            psm = con.prepareStatement("update studentanswer set state=1 where sno=? and PageId=? and ProblemId=?");

            int length = studentSubjectiveProblemScore.size();
            for(int i=0; i<length; i++)
            {
                String[] temp = studentSubjectiveProblemScore.get(i);

                psm.setString(1, temp[0]);
                psm.setString(2, pageId);
                psm.setString(3, problemId);

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
     * @param pageId 试卷编号
     * @return 根据试卷编号查询学生成绩 班级号+班名+学号+姓名+成绩
     */
    public static List<String[]> teacherQueryScore(String pageId)
    {
        CallableStatement csm = null;
        ResultSet rs = null;

        List<String[]> score = new ArrayList<String[]>();

        try {
            csm = con.prepareCall("call teacherQueryScore(?)");
            csm.setString(1, pageId);

            rs = csm.executeQuery();
            while(rs.next())
            {
                String[] temp = new String[5];
                temp[0] = rs.getString("ClassId"); //班级号
                temp[1] = rs.getString("ClassName"); //班级名
                temp[2] = rs.getString("Sno"); //学号
                temp[3] = rs.getString("Sname"); //姓名
                temp[4] = String.valueOf(rs.getDouble("ObjectiveScore") + rs.getDouble("SubjectiveScore")); //成绩

                score.add(temp);
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

            return score;
        }
    }
}
