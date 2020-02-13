package dao;

import entiry.Merchandise;
import global.Contacts;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


public class TokenTicketDao {


    public TokenTicketDao() {

    }

    public String getTicket(Connection con) {
        String ticket = null;
        String sql = "select * from token_ticket where id=1";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ticket = rs.getString("jsapi_ticket");
            }
            return ticket;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("error----------TokenTicketDao.getTicket++"+e.toString());
        }
        return null;
    }

    public void updataTicket(Connection con){
        String temp = getTicketFromWX();
        String sql = "UPDATE token_ticket SET jsapi_ticket = \""+temp+"\" WHERE id = 1";
        PreparedStatement ps;
        try {
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("error----------TokenTicketDao.updataTicket++"+e.toString());
        }
    }

    //从微信服务器获取ticket
    private String getTicketFromWX() {
        //https请求方式: GET https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
        String requestUrl =
                "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + Contacts.APPID + "&secret=" + Contacts.SECRET;
        URL url = null;
        try {
            url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();

            JSONObject job = new JSONObject(buffer.toString());
            String temp_token = job.getString("access_token");
            if (null != temp_token) {
                return getJsapi_ticket(temp_token);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error----------TokenTicketDao.getTicketFromWX++"+e.toString());
        }
        return null;
    }

    private String getJsapi_ticket(String token) {
        //用第一步拿到的access_token 采用http GET方式请求获得jsapi_ticket（有效期7200秒
        String requestUrl =
                "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + token + "&type=jsapi";
        URL url = null;
        try {
            url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();

            JSONObject job = new JSONObject(buffer.toString());
            String temp_token = job.getString("ticket");
            if (null != temp_token) {
                return temp_token;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error----------TokenTicketDao.getJsapi_ticket++"+e.toString());
        }
        return null;
    }


}
