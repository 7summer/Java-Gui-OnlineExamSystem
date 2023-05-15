import Mysql.MySqlDemo;

public class Main {
    public static void main(String[] args) {
        MySqlDemo.connectDatabase(); //连接数据库
        //在窗口关闭后，默认断开数据库连接
        LoginView loginView = new LoginView();
    }
}
