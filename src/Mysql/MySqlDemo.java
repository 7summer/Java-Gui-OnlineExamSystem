package Mysql;

import java.sql.*;

public class MySqlDemo {

    public static Connection con = null;

    public static void setCon(Connection con) {
        MySqlDemo.con = con;
    }

    MySqlDemo() {
    }

    public static void connectDatabase() //连接数据库
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("注册jdbc驱动失败");
        }

        String url = "jdbc:mysql://localhost:3306/onlineexams?characterEncoding=utf-8&useSSL=true";
        try {
            con = DriverManager.getConnection(url, "root", "12345");
        } catch (SQLException e) {
            System.out.println("连接数据库失败");
        }
    }

    public static void disconnectDatabase() //断开数据库
    {
        if(con != null)
        {
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     *
     * @param selection 0代表学生登录 1代表老师登录
     * @param id 用户名
     * @param password 密码
     * @return 登录成功返回true 登录失败返回false
     */
    public static boolean verifyLogin(int selection, String id, String password) //登录验证
    {
        CallableStatement csm = null;
        ResultSet rs = null;
        boolean flag = false;

        if(selection == 0)
        {
            try {
                csm = con.prepareCall("call verifyStudentLogin(?, ?)");
                csm.setString(1, id);
                csm.setString(2, password);

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
                if(rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                if(csm != null) {
                    try {
                        csm.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                return flag;
            }
        }
        else if(selection == 1)
        {
            try {
                csm = con.prepareCall("call verifyTeacherLogin(?, ?)");
                csm.setString(1, id);
                csm.setString(2, password);

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
                try {
                    if(rs != null) rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if(csm != null) csm.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                return flag;
            }
        }

        return false;
    }
}
