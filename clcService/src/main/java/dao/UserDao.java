package dao;

import entiry.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    // 登录
    public User login(Connection con, User user) throws SQLException {
        User resultUser = null;
        String sql = "select * from user where username=? and password=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            resultUser = new User();
            resultUser.setUsername(rs.getString("username"));
            resultUser.setPassword(rs.getString("password"));
        }
        return resultUser;
    }
    // 显示所有信息
    public User getAll(Connection con, User user) throws SQLException {
        User resultUser = null;
        String sql = "select * from user ";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            resultUser = new User();
        }
        return resultUser;
    }

    // 注册
    public boolean insert(Connection con, User user) {
        String sql = "insert into user(username,password) values(?,?)";
        boolean flag = false;
        try {
            PreparedStatement psta = con.prepareStatement(sql);//
            psta.setString(1, user.getUsername());
            psta.setString(2, user.getPassword());
            flag = psta.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            con.close();
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return flag;
    }

}
