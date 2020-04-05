package servlet;

import dao.TokenTicketDao;
import global.Contacts;
import org.json.JSONException;
import org.json.JSONObject;
import util.DbUtil;
import util.MD5Utils;
import util.WXPaySignUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 领奖之前的分享服务 （不打开新的页面）
 */
public class ShareServlet2 extends HttpServlet {

    DbUtil db=new DbUtil();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Content-type", "text/html;charset=UTF-8");  //这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();

        String jsapi_ticket = null;
        TokenTicketDao ttd = new TokenTicketDao();
        try {
            Connection con = db.getCon();
            jsapi_ticket = ttd.getTicket(con);
            DbUtil.getClose(con);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(jsapi_ticket!=null){
            String url = req.getParameter("url");
            String code = req.getParameter("code");
            String state = req.getParameter("state");
            String device_info = req.getParameter("device_info");
            String Url = url +"&device_info=" +device_info+ "&code=" + code +"&state=" + state;
//            System.out.println("Url-----------------------------"+Url);
//            System.out.println("state-----------------------------"+state);
//            System.out.println("device_info-----------------------------"+device_info);

            String timestamp1 = String.valueOf(new Date().getTime());
            String timestamp = timestamp1.substring(timestamp1.length()-10);
            String nonceStr = MD5Utils.digest(String.valueOf(new Date().getTime()));
//        String Url = "http://www.wxfslp.xyz/clcService_war_exploded/";

            Map<String, String> params = new HashMap<String, String>();
            params.put("noncestr",nonceStr);
            params.put("jsapi_ticket",jsapi_ticket);
            params.put("timestamp",timestamp);
            params.put("url",Url);
            String signature = WXPaySignUtil.WXShapeSign(params);


            JSONObject job = new JSONObject();
            try {
                job.put("appId", Contacts.APPID);
                job.put("timestamp",timestamp);
                job.put("nonceStr",nonceStr);
                job.put("signature",signature);
            } catch (JSONException e) {
                System.out.println("--ShareServlet2.doPost--"+e.toString());
                e.printStackTrace();
            }
            String jsonString = job.toString();
            writer.write(jsonString);
//            System.out.println("--ShareServlet2.jsonString--"+jsonString);
        }else {
            System.out.println("jsapi_ticket=================ShareServlet2======================error");
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }

}
