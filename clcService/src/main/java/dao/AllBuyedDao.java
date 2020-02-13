package dao;

import entiry.Grid;
import entiry.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 所有需要打开的柜号
 */
public class AllBuyedDao {

    /**
     * 将指定的设备信息设置成空字符串
     * @param con
     * @param device_info
     */
    public void setNullByInfo(Connection con,String device_info){
        String sql;
        if(con!=null){
            sql = "UPDATE all_buyed SET mer_msg = \"\" WHERE device_info = \"" + device_info+"\"";
            PreparedStatement ps;
            try {
                ps = con.prepareStatement(sql);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("--AllBuyedDao.setNullByInfo--"+e.toString());
            }
        }
    }

    /**
     * 设置制定柜子状态
     * @param con
     * @param device_info
     * @param mer_msg
     */
    public void setMsgByInfo(Connection con,String device_info,String mer_msg){
        String gridMerNo = getMsgByMsg(con, device_info);
        String temp = gridMerNo+mer_msg;

        String sql;
        if(con!=null){
            sql = "UPDATE all_buyed SET mer_msg = \"" + temp + "\" WHERE device_info = \"" + device_info+"\"";
            PreparedStatement ps;
            try {
                ps = con.prepareStatement(sql);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("--AllBuyedDao.setMsgByInfo--"+e.toString());
            }
        }
    }

    /**
     * 获取信息
     * @param con
     * @param device_info
     */
    public String getMsgByMsg(Connection con,String device_info){
        String sql;
        if(con!=null){
            sql = "select * from all_buyed where device_info=?";
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, device_info);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getString("mer_msg");
                }
            } catch (SQLException e) {
                System.out.println("--AllBuyedDao.getMsgByMsg--"+e.toString());
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 获取设备的密码
     */
    public String getPassword(Connection con,String device_info){
        String sql;
        if(con!=null){
            sql = "select * from all_buyed where device_info=?";
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, device_info);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getString("password");
                }
            } catch (SQLException e) {
                System.out.println("--AllBuyedDao.getPassword--"+e.toString());
                e.printStackTrace();
            }
        }
        return null;
    }

}
