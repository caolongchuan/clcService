package servlet;

import dao.LeafletsDao;
import dao.OutTradeNoDao;
import dao.PrizeDao;
import entiry.Prize;
import entiry.out_trade_no_table;
import global.Contacts;
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
import java.util.ArrayList;
import java.util.Random;

/**
 * 生成中奖值得服务 （打开新的页面 进入抽奖页面）
 */
public class LotteryServlet extends HttpServlet {

    DbUtil db = new DbUtil();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Content-type", "text/html;charset=UTF-8");  //这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
        resp.setCharacterEncoding("UTF-8");

        String code = req.getParameter("code");
//        System.out.println("code=====================" + code);
        String openid = getOpenID(code, req);
//        System.out.println("openid====================" + openid);
//        String out_trade_no = req.getParameter("out_trade_no");
//        System.out.println("out_trade_no====================" + out_trade_no);
//        String device_info = req.getParameter("device_info");
        //先根据openID去查找OutTradeNo数据库，看有没有该用户购买过东西并且没有进行过抽奖
        // 如果有先显示抽奖页面 如果已经抽过或者没有该用户openID则显示宣传页

        if(openid!=null){
            String leafletsTwoTitle = null;
            String leafletsTwoImgUrl = null;
            OutTradeNoDao otnd = new OutTradeNoDao();
            Connection con;
            try {
                con = db.getCon();

                out_trade_no_table otnt = otnd.getLoterStatusLastTime(con, openid);
                String out_trade_no = otnt.out_trade_no;
                String device_info = otnt.out_trade_no.replaceAll("\\d+","");
                int lotterStatus = otnt.lottery_status;

//                System.out.println("device_info====================" + device_info);
                LeafletsDao ld = new LeafletsDao(device_info);
                leafletsTwoTitle = ld.getLeafletsTwoTitle(con);
                leafletsTwoImgUrl = ld.getLeafletsTwoImgUrl(con);
                PrizeDao pd = new PrizeDao(device_info);

//                System.out.println("out_trade_no====================" + out_trade_no);
//                System.out.println("openid====================" + openid);
//                System.out.println("lotterStatus====================" + lotterStatus);
                int total_fee = otnd.getTotalFee(con, out_trade_no);
                if(-1 == lotterStatus){
                    req.getRequestDispatcher("/WEB-INF/page/leaflet.jsp").forward(req, resp);
                }else if(10>lotterStatus){
                    int lotterValue = onLottery(device_info,total_fee);
                    otnd.setLotteryStatus(con, out_trade_no, lotterValue);
                    Random ran = new Random();
                    int quanshu = ran.nextInt(3) + 5;
                    int run_time = quanshu * 8 + lotterValue;
                    req.setAttribute("out_trade_no", out_trade_no);
                    req.setAttribute("lotterValue", lotterValue);
//                req.setAttribute("quanshu", quanshu);
//                req.setAttribute("run_time", run_time);
                    req.setAttribute("device_info",device_info);
//                    System.out.println("out_trade_no---"+out_trade_no);
                    System.out.println("lotterValue---"+lotterValue);
//                    System.out.println("quanshu---"+quanshu);
//                    System.out.println("run_time---"+run_time);

//                    otnd.setLotterStatus(con, out_trade_no, i);
                    //设置各个奖项的名字
                    ArrayList<Prize> allPrize = pd.getAllPrize(con);
                    for(int i=0;i<allPrize.size();i++){
                        String name = allPrize.get(i).getName();
                        req.setAttribute("lotter_name_"+i,name);
                    }

                    if(null!=leafletsTwoTitle&&null!=leafletsTwoImgUrl){
                        req.setAttribute("leafletsTwoTitle", leafletsTwoTitle);
                        req.setAttribute("leafletsTwoImgUrl", leafletsTwoImgUrl);
                    }else{
                        req.setAttribute("leafletsTwoTitle", "欢迎光临");
                        req.setAttribute("leafletsTwoImgUrl", "http://www.wxfslp.xyz/mer_picture/car.png");
                    }


                    req.getRequestDispatcher("/WEB-INF/page/lottery.jsp").forward(req, resp);//抽奖页
                }else{
                    req.getRequestDispatcher("/WEB-INF/page/leaflet.jsp").forward(req, resp);//一号宣传页
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

    //抽奖 返回中奖值
    private int onLottery(String device_info,int fee) {

        ArrayList<Prize> allPrize = null;
        PrizeDao pd = new PrizeDao(device_info);
        try {
            Connection con = db.getCon();
            allPrize = pd.getAllPrize(con);

            DbUtil.getClose(con);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error---LotteryServlet.onLottery++" + e.toString());
        }
        return geneLotteryValue(Contacts.MAX_FEE, fee, allPrize);
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

    //生成中奖码
    private int geneLotteryValue(int max_fee, int fee, ArrayList<Prize> list) {
        if (null != list) {
            int[][] weight_bord = new int[list.size()][2];
            int move = 0;
            for (int i = 0; i < list.size(); i++) {
                weight_bord[i][0] = move;
                weight_bord[i][1] = move + list.get(i).getWeigth();
                move = weight_bord[i][1];
            }
            Random ran = new Random();

            int lottery_value = ran.nextInt(move) + 1;
            for (int i = 0; i < list.size(); i++) {
                if (lottery_value > weight_bord[i][0] && lottery_value < weight_bord[i][1]) {
                    if (fee >= max_fee) {
                        return 1;
                    }
                    return i + 1;
                }
            }
        }
        return 1;
    }


}
