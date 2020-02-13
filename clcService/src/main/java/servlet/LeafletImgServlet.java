package servlet;

import dao.AllBuyedDao;
import dao.LeafletsDao;
import util.DbUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

/**
 * Android控制终端服务 （不打开新的页面）
 */
public class LeafletImgServlet extends HttpServlet {

    DbUtil db = new DbUtil();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");
        resp.setHeader("Content-type", "text/html;charset=UTF-8");  //这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();

        LeafletsDao ld = new LeafletsDao("xinzhang");
        if (type != null) {
            try {
                Connection con = db.getCon();
                switch (type) {
                    case "leaflet_1":
                        String leafletsOneContentImgUrl = ld.getLeafletsOneContentImgUrl(con);
                        if(null!=leafletsOneContentImgUrl){
                            writer.write(leafletsOneContentImgUrl);
                        }
                        break;
                    case "leaflet_2":
                        String leafletsTwoContentImgUrl = ld.getLeafletsTwoContentImgUrl(con);
                        if(null!=leafletsTwoContentImgUrl){
                            writer.write(leafletsTwoContentImgUrl);
                        }
                        break;
                    case "lingjiang":
                        String leafletsReceivePrizePagerUrl = ld.getLeafletsReceivePrizePagerUrl(con);
                        if(null!=leafletsReceivePrizePagerUrl){
                            writer.write(leafletsReceivePrizePagerUrl);
                        }
                        break;
                }
                DbUtil.getClose(con);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}