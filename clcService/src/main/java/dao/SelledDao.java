package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelledDao {
    private String device_info;

    public SelledDao(String device_info){
        this.device_info = device_info+"_selled";
    }

    /**
     * 插入数据到数据库
     */
    public void insert(Connection con,
                       String out_trade_no, String create_time, String mer_name,
                       int total_fee, int refund_fee, int coupon_fee, int lottery_value){
        String sql = "insert into " + device_info + "" +
                "(out_trade_no,create_time,mer_name,total_fee," +
                "refund_fee,coupon_fee,lottery_value) values(?,?,?,?,?,?,?)";
        boolean flag = false;
        try {
            PreparedStatement psta = con.prepareStatement(sql);//
            psta.setString(1, out_trade_no);
            psta.setString(2,create_time);
            psta.setString(3,mer_name);
            psta.setInt(4, total_fee);
            psta.setInt(5,refund_fee);
            psta.setInt(6, coupon_fee);
            psta.setInt(7,lottery_value);
            flag = psta.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int get_total_fee(Connection con,String out_trade_no){
        String sql;
        if(con!=null){
            sql = "select * from "+device_info+" where out_trade_no = \""+out_trade_no + "\"" ;
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("total_fee");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error----SelledDao.total_fee++"+e.toString());
            }
        }
        return -1;
    }

    /**
     * 设置退款金额
     */
    public void set_refund_fee(Connection con,String out_trade_no,int fee){
        String sql = "UPDATE "+device_info+""+" SET refund_fee = "+fee+" WHERE out_trade_no = \""+out_trade_no + "\"" ;
        PreparedStatement ps;
        try {
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("error----------SelledDao.set_refund_fee++"+e.toString());
        }
    }

    /**
     * 设置中的奖项
     */
    public void set_lottery_value(Connection con,String out_trade_no,int lottery_value){
        String sql = "UPDATE "+device_info+""+" SET lottery_value = "+lottery_value+" WHERE out_trade_no = \""+out_trade_no + "\"" ;
        PreparedStatement ps;
        try {
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("error----------SelledDao.set_lottery_value++"+e.toString());
        }
    }

}
