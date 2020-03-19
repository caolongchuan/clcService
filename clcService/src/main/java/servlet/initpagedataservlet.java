package servlet;

import dao.AllBuyedDao;
import dao.GridDao;
import dao.MerchandiseDao;
import entiry.Grid;
import entiry.Merchandise;
import global.TimeUpdataWXToken;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import result_json.Mer_Det_Result;
import util.DbUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 显示所有物品 供选择 （打开新的页面 供客户选择物品的页面）
 */
public class initpagedataservlet extends HttpServlet {
    DbUtil db = new DbUtil();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //初始化定时获取微信token的定时任务，单例模式 只初始化一次 然后每24小时更新一次
        TimeUpdataWXToken.init();

        String device_info = req.getParameter("device_info");
        if(null!=device_info){


            AllBuyedDao abd = new AllBuyedDao();
            GridDao gridDao = new GridDao(device_info);
            MerchandiseDao merchandiseDao = new MerchandiseDao(device_info);
            Mer_Det_Result mDResult = new Mer_Det_Result("000", 0, null, null);

            Connection con = null;
            try {
                con = db.getCon();
                //先判断该设备是否在线
                String lastTime = abd.getLastTime(con, device_info);
                if(lastTime.equals("0")){//设备不在线 跳转到指定页面
                    DbUtil.getClose(con);
                    req.getRequestDispatcher("/WEB-INF/page/noline.jsp").forward(req, resp);
                }else {//设备在线
                    ArrayList<Grid> Grid_All = gridDao.getAll(con);
                    if (null != Grid_All) {
                        mDResult.setAmount(Grid_All.size());
                        ArrayList<Integer> grid_status = new ArrayList<Integer>();
                        ArrayList<Merchandise> merchandises = new ArrayList<Merchandise>();
                        for (Grid g : Grid_All) {
                            grid_status.add(g.getGrid_status());

                            int merchandise_id = g.getMerchandise_id();
                            Merchandise m = merchandiseDao.getMerchandiseByID(con, merchandise_id);
                            if (null == m) {
                                m = new Merchandise();
                            }
                            merchandises.add(m);
                        }
                        mDResult.setGrid_status(grid_status);
                        mDResult.setMerchandise(merchandises);
                        mDResult.setStatus("SUCCESS");
                    }
                    String jsonString = mDResult.toJsonString();

                    resp.setHeader("Content-type", "text/html;charset=UTF-8");  //这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
                    resp.setCharacterEncoding("UTF-8");
                    req.setAttribute("jsonString", jsonString);
                    req.setAttribute("device_info", device_info);

                    DbUtil.getClose(con);
                    req.getRequestDispatcher("/WEB-INF/page/plan_b.jsp").forward(req, resp);
                }
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


    /**
     * @param xml
     * @return Map
     * @description 将xml字符串转换成map
     */
    public HashMap readStringXmlOut(String xml) {
        HashMap map = new HashMap();
        Document doc = null;
        try {
            // 将字符串转为XML
            doc = DocumentHelper.parseText(xml);
            // 获取根节点
            Element rootElt = doc.getRootElement();
            String name = rootElt.getName();
            // 拿到根节点的名称
//            System.out.println("根节点：" + rootElt.getName());

            String return_code = rootElt.elementText("return_code");
            String return_msg = rootElt.elementText("return_msg");

            if (return_code.equals("SUCCESS")) {
                String nonce_str = rootElt.elementText("nonce_str");
                map.put("nonce_str", nonce_str);
                String sign = rootElt.elementText("sign");
                map.put("sign", sign);
                String result_code = rootElt.elementText("result_code");
                if (result_code.equals("SUCCESS")) {
                    String prepay_id = rootElt.elementText("prepay_id");
                    map.put("prepay_id", prepay_id);
                    return map;
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}

