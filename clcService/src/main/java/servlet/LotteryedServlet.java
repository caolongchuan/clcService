package servlet;

import dao.OutTradeNoDao;
import entiry.out_trade_no_table;
import org.json.JSONException;
import org.json.JSONObject;
import util.DbUtil;
import wx_result.getopenid_code;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;

/**
 * 判断是进领奖页还是领奖后宣传页 （打开新的页面）
 */
public class LotteryedServlet extends HttpServlet {

    DbUtil db = new DbUtil();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Content-type", "text/html;charset=UTF-8");  //这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
        resp.setCharacterEncoding("UTF-8");

        String code = req.getParameter("code");
//        System.out.println("code=======LotteryedServlet==============" + code);
        String openid = getOpenID(code, req);
//        System.out.println("openid=======LotteryedServlet=============" + openid);
//        String out_trade_no = req.getParameter("out_trade_no");
//        String lotterValue = req.getParameter("lotterValue");
//        String device_info = req.getParameter("device_info");
//        System.out.println("lotterValue=======LotteryedServlet=============" + lotterValue);

        if(openid!=null){
            OutTradeNoDao otnd = new OutTradeNoDao();
            Connection con = null;
            try {
                con = db.getCon();
                out_trade_no_table otnt = otnd.getLoterStatusLastTime(con, openid);

                String out_trade_no = otnt.out_trade_no;
                String device_info = otnt.out_trade_no.replaceAll("\\d+","");
                String lotterValue = String.valueOf(otnt.lottery_status % 10);
                int lotterStatus = otnt.lottery_status;
                int total_fee = otnt.total_fee;

                if(-1==lotterStatus){
                    req.setAttribute("lotterValue", lotterValue);
                    req.getRequestDispatcher("/WEB-INF/page/leaflet2.jsp").forward(req, resp);//中奖后的宣传页
                }else if(10<=lotterStatus&&20>lotterStatus){
                    req.setAttribute("out_trade_no", out_trade_no);
                    req.setAttribute("lotterValue", lotterValue);
                    req.setAttribute("device_info", device_info);
                    req.setAttribute("openid", openid);
                    req.getRequestDispatcher("/WEB-INF/page/lotteryed.jsp").forward(req, resp);//领奖页
                } else{
                    req.setAttribute("lotterValue", lotterValue);
                    req.getRequestDispatcher("/WEB-INF/page/leaflet2.jsp").forward(req, resp);//中奖后的宣传页
                }

                DbUtil.getClose(con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    private String getOpenID(String code, HttpServletRequest req) {
        getopenid_code goc = new getopenid_code(code);
        String url = goc.generateUrl();

        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("contentType", "UTF-8");
            // 建立实际的连接
            connection.connect();

            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }

        String openid = "";
        try {
            JSONObject jsonObject = new JSONObject(result.toString());
            if(jsonObject.has("openid")){
                openid = jsonObject.getString("openid");
                if (openid != null && !openid.equals("")) {
                    return openid;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return e.toString();
        }
        return null;
    }

}
