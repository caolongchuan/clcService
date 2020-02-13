package servlet;

import com.alibaba.fastjson.JSONObject;
import dao.OutTradeNoDao;
import dao.UserDao;
import entiry.User;
import util.DbUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

/**
 *抽奖后改变中奖状态的服务 （不打开新的页面）
 */
public class doLotteryServlet extends HttpServlet {

    DbUtil db = new DbUtil();

    public doLotteryServlet() {
        super();
    }

    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Content-type", "text/html;charset=UTF-8");  //这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();

        String out_trade_no = request.getParameter("out_trade_no");
        String lotterValue = request.getParameter("lotterValue");
        String device_info = request.getParameter("device_info");
//        System.out.println("out_trade_no========doLotteryServlet============" + out_trade_no);
//        System.out.println("lotterValue========doLotteryServlet============" + lotterValue);

        OutTradeNoDao otnd = new OutTradeNoDao();
        try {
            Connection con = db.getCon();
            otnd.setLotteryStatus(con, out_trade_no, 10 + Integer.valueOf(lotterValue));

            DbUtil.getClose(con);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        System.out.println("device_info========doLotteryServlet============" + device_info);
        writer.write(device_info);

    }


}
