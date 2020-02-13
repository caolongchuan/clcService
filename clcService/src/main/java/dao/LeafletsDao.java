package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 控制宣传页标题和页面内容的dao
 */
public class LeafletsDao {
    private String mDianName;
    private String mTableName;

    public LeafletsDao(String name){
        mTableName = "leaflets";
        mDianName = name;
    }

    public String getLeafletsOneTitle(Connection con) {
        String sql;
        if (con != null) {
            sql = "select * from " + mTableName + " where dian_name=?";
            PreparedStatement ps = null;
            try {
                ps = con.prepareStatement(sql);
                ps.setString(1, mDianName);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getString("leaflets_1_title");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getLeafletsOneImgUrl(Connection con){
        String sql;
        if (con != null) {
            sql = "select * from " + mTableName + " where dian_name=?";
            PreparedStatement ps = null;
            try {
                ps = con.prepareStatement(sql);
                ps.setString(1, mDianName);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getString("leaflets_1_img_url");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getLeafletsOneContentImgUrl(Connection con){
        String sql;
        if (con != null) {
            sql = "select * from " + mTableName + " where dian_name=?";
            PreparedStatement ps = null;
            try {
                ps = con.prepareStatement(sql);
                ps.setString(1, mDianName);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getString("leaflets_1_content_img_url");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getLeafletsTwoTitle(Connection con) {
        String sql;
        if (con != null) {
            sql = "select * from " + mTableName + " where dian_name=?";
            PreparedStatement ps = null;
            try {
                ps = con.prepareStatement(sql);
                ps.setString(1, mDianName);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getString("leaflets_2_title");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getLeafletsTwoImgUrl(Connection con){
        String sql;
        if (con != null) {
            sql = "select * from " + mTableName + " where dian_name=?";
            PreparedStatement ps = null;
            try {
                ps = con.prepareStatement(sql);
                ps.setString(1, mDianName);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getString("leaflets_2_img_url");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getLeafletsTwoContentImgUrl(Connection con){
        String sql;
        if (con != null) {
            sql = "select * from " + mTableName + " where dian_name=?";
            PreparedStatement ps = null;
            try {
                ps = con.prepareStatement(sql);
                ps.setString(1, mDianName);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getString("leaflets_2_content_img_url");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getLeafletsReceivePrizePagerUrl(Connection con){
        String sql;
        if (con != null) {
            sql = "select * from " + mTableName + " where dian_name=?";
            PreparedStatement ps = null;
            try {
                ps = con.prepareStatement(sql);
                ps.setString(1, mDianName);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getString("receive_prize_pager");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
