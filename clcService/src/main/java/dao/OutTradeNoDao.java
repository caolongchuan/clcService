package dao;

import global.Contacts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * 操作商户订单号数据库
 * 用于判断接收到的支付通知消息 避免重复处理
 */
public class OutTradeNoDao {

    public OutTradeNoDao(){

    }

    /**
     * 根据商户订单号与用户openID获取该订单是否有参与过抽奖
     * @param con
     * @param out_trade_no
     * @return
     */
    public int getLotterStatus(Connection con,String out_trade_no,String openid){
        String sql;
        if(con!=null){
            sql = "select * from out_trade_no_table where out_trade_no=? and user_openid=?";
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, out_trade_no);
                ps.setString(2,openid);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("lottery_status");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    //根据订单号获取付款金额
    public int getTotalFee(Connection con,String out_trade_no){
        String sql;
        if(con!=null){
            sql = "select * from out_trade_no_table where out_trade_no=?";
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, out_trade_no);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("total_fee");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    //根据订单号获取中奖状态
    public int getLotteryStatus(Connection con,String out_trade_no){
        String sql;
        if(con!=null){
            sql = "select * from out_trade_no_table where out_trade_no=?";
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, out_trade_no);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("lottery_status");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    /**
     * 判断指定的商户订单号是否已经在数据库里了
     * @param con
     * @param out_trade_no
     * @return
     */
    public boolean checkIsHave(Connection con,String out_trade_no){
        String sql = "select * from out_trade_no_table where out_trade_no=?";
        PreparedStatement ps = null;
        boolean isHave = false;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1,out_trade_no);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String create_time = rs.getString("create_time");
                isHave = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isHave;
    }

    /**
     * 插入数据到数据库
     * @param con
     * @param out_trade_no
     * @param cutten_time
     * @return
     */
    public boolean insert(Connection con,String out_trade_no,Long cutten_time,String user_openid,int total_fee){
        String sql = "insert into out_trade_no_table(out_trade_no,create_time,lottery_status,user_openid,total_fee) values(?,?,?,?,?)";
        boolean flag = false;
        try {
            PreparedStatement psta = con.prepareStatement(sql);//
            psta.setString(1, out_trade_no);
            psta.setLong(2,cutten_time);
            psta.setInt(3,0);
            psta.setString(4, user_openid);
            psta.setInt(5,total_fee);
            flag = psta.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除数据库中所有超过25个小时的数据
     * @param con
     */
    public boolean deleteOutTime(Connection con){
        boolean flag = false;
        Long cutten_time = new Date().getTime();//获取当前时间
        String sql = "delete from out_trade_no_table where create_time<?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setLong(1,cutten_time- Contacts.HOUR_25);
            flag = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public void setLotteryStatus(Connection con,String out_trade_no,int lottery_status ){
        String sql;
        if(con!=null){
            sql = "UPDATE out_trade_no_table SET lottery_status = " + lottery_status + " WHERE out_trade_no = \"" + out_trade_no+"\"";
            PreparedStatement ps;
            try {
                ps = con.prepareStatement(sql);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
