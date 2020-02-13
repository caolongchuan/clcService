package dao;

import entiry.Merchandise;
import entiry.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MerchandiseDao {

    private String device_name;

    public MerchandiseDao(String device_name) {
        this.device_name = device_name + "_merchandise";
    }

    public boolean insert(Connection con, Merchandise merchandise) {
        String sql = "insert into " + device_name + "(id,name,price,img) values(?,?,?,?)";
        boolean flag = false;
        try {
            PreparedStatement psta = con.prepareStatement(sql);//
            psta.setInt(1, merchandise.getId());
            psta.setString(2, merchandise.getName());
            psta.setDouble(3, merchandise.getPrice());
            psta.setString(4, merchandise.getImg());
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

    public Merchandise getMerchandiseByID(Connection con, int id) {
        Merchandise resultMerchandise = null;
        String sql = "select * from " + device_name + " where id=?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                resultMerchandise = new Merchandise();
                resultMerchandise.setId(rs.getInt("id"));
                resultMerchandise.setName(rs.getString("name"));
                resultMerchandise.setPrice(rs.getDouble("price"));
                resultMerchandise.setImg(rs.getString("img"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultMerchandise;
    }


    public ArrayList<Merchandise> getAll(Connection con) throws SQLException {
        ArrayList<Merchandise> result = new ArrayList<Merchandise>();
        String sql = "select * from "+device_name;
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Merchandise resultMerchandise = new Merchandise();
            resultMerchandise.setId(rs.getInt("id"));
            resultMerchandise.setName(rs.getString("name"));
            resultMerchandise.setPrice(rs.getDouble("price"));
            resultMerchandise.setImg(rs.getString("img"));
            result.add(resultMerchandise);
        }
        if (result.size() > 0) {
            return result;
        }
        return null;
    }

}
