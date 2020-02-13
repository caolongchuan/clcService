package servlet;

import dao.AllBuyedDao;
import dao.GridDao;
import util.DbUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

/**
 * 设置柜子状态、物品服务 （不打开新的页面）
 */
public class SetGridServlet extends HttpServlet {

    DbUtil db=new DbUtil();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String device_info = req.getParameter("device_info");
        String no = req.getParameter("no");
        String action = req.getParameter("action");
        String mer_no = req.getParameter("mer_no");

        resp.setHeader("Content-type", "text/html;charset=UTF-8");  //这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();

        GridDao gridDao = new GridDao(device_info);
        Connection con = null;
        try {
            con = db.getCon();
            if(action.equals("open")){
                gridDao.setGridStatus(con,Integer.valueOf(no),0);
                AllBuyedDao abd = new AllBuyedDao();
                abd.setMsgByInfo(con,device_info," "+no+" ");
//                TimeUpdataWXToken.getInstance().setDataByName("xinzhang", no);
                writer.write("打开成功");
            }else if(action.equals("close")){
                gridDao.setGridStatus(con,Integer.valueOf(no),1);
                writer.write("补充成功");
            }else if(action.equals("change")){
                gridDao.setGridMerNo(con,Integer.valueOf(no),Integer.valueOf(mer_no));
                writer.write("修改成功");
            }
            DbUtil.getClose(con);
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("失败");
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }

}
