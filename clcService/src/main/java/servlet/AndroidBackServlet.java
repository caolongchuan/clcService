package servlet;

import dao.AllBuyedDao;
import util.DbUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Date;

/**
 * Android控制终端服务 （不打开新的页面）
 */
public class AndroidBackServlet extends HttpServlet{

    DbUtil db=new DbUtil();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String device_info = req.getParameter("device_info");
        resp.setHeader("Content-type", "text/html;charset=UTF-8");  //这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        String msg;
        AllBuyedDao abd = new AllBuyedDao();
        try {
            Connection con = db.getCon();
            msg = abd.getMsgByMsg(con, device_info);
            abd.setNullByInfo(con,device_info);
            String s = String.valueOf(new Date().getTime());//获取当前时间
            abd.setLastTime(con,device_info,s);
            DbUtil.getClose(con);
        } catch (Exception e) {
            System.out.println("--AndroidBackServlet.doPost--"+e.toString());
            msg = e.toString();
            e.printStackTrace();
        }
        writer.write(msg);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}