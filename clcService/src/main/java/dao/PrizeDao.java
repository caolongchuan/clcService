package dao;

import entiry.Prize;
import global.Contacts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * 操作商户订单号数据库
 * 用于判断接收到的支付通知消息 避免重复处理
 */
public class PrizeDao {

    String mName;

    public PrizeDao(String name){
        this.mName = name + "_prize";
    }

    public ArrayList<Prize> getAllPrize(Connection con){
        ArrayList<Prize> list = new ArrayList<>();
        String sql;
        if(con!=null){
            sql = "select * from "+mName;
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Prize p = new Prize();
                    p.setId(rs.getInt("id"));
                    p.setName(rs.getString("name"));
                    p.setWeigth(rs.getInt("weigth"));
                    list.add(p);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error----PrizeDao.getAllPrize++"+e.toString());
            }
        }
        return list;
    }

    /**
     * 根据id获取获奖值
     */
    public int getPriceValue(Connection con,int id){
        String sql;
        if(con!=null){
            sql = "select * from "+mName+" where id="+id;
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("value");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error----PrizeDao.getAllPrize++"+e.toString());
            }
        }
        return -1;
    }

    /**
     * 根据id获取优惠劵批次号
     */
    public String get_coupon_stock_id(Connection con,int id){
        String sql;
        if(con!=null){
            sql = "select * from "+mName+" where id="+id;
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getString("coupon_stock_id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error----PrizeDao.get_coupon_stock_id++"+e.toString());
            }
        }
        return "";
    }

}
