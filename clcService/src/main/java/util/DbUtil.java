package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
    private String driver="com.mysql.jdbc.Driver";
    private String url="jdbc:mysql://localhost:3306/testMaven?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
    private String user="root";
    private String password="123456";

    public Connection getCon() throws Exception{
        Class.forName(driver);
        Connection con= DriverManager.getConnection(url,user,password);
        return con;
    }
    public static void getClose(Connection con) throws SQLException {
        if(con!=null){
            con.close();
        }
    }

}
