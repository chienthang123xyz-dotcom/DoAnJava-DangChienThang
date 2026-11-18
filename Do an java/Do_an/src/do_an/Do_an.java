package do_an;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Do_an {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlserver://DESKTOP-LC65K7T\\SQLEXPRESS:1433;databaseName=QuanLyBanHang;encrypt=true;trustServerCertificate=true";
            String user = "sa";
            String password = "123456"; 
            
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Kết nối cơ sở dữ liệu thành công!");
        } catch (SQLException e) {
            System.out.println("Kết nối thất bại: " + e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
        Connection c = getConnection();
        System.out.println("Đối tượng Connection: " + c);
    }
}