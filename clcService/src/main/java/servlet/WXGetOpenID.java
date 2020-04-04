package servlet;

import dao.LeafletsDao;
import global.Contacts;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONException;
import org.json.JSONObject;
import util.CusAccessObjectUtil;
import util.DbUtil;
import util.MD5Utils;
import util.WXPaySignUtil;
import wx_parameter.OrderUniformlyParam;
import wx_result.getopenid_code;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取用户的openID 微信统一下单 （不打开新的页面）
 */
public class WXGetOpenID extends HttpServlet {
    DbUtil db = new DbUtil();

    public WXGetOpenID() {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String device_info = req.getParameter("device_info");
        String attach = req.getParameter("attach");
        String total_fee = req.getParameter("total_fee");
        Double aLong = Double.valueOf(total_fee);
        int total_Fee = (int) (aLong * 100);
        String code = req.getParameter("code");
        String state = req.getParameter("state");

        String openid = getOpenID(code, req);
        if(null != openid){
//        System.out.println("get-code----"+code);
//        System.out.println("get-openid----"+openid);
//        System.out.println(openid);
            String temp = "";
            String out_trade_no = "";//商户订单号
            if (openid != null && !openid.equals("")) {
                String ip = CusAccessObjectUtil.getIpAddress(req);
                //统一下单
                OrderUniformlyParam oup = new OrderUniformlyParam(device_info, "test_body", attach, total_Fee, ip, openid);
                temp = oup.orderUniformly();
                out_trade_no = oup.get_out_trade_no();
//            System.out.println(temp);
            }

            String return_code = "";
            String return_msg = "";
            String result_code = "";
            String prepay_id = "";

            String leafletsOneTitle = null;
            String leafletsOneImgUrl = null;
            try {
                LeafletsDao ld = new LeafletsDao(device_info);
                Connection con = db.getCon();
                leafletsOneTitle = ld.getLeafletsOneTitle(con);
                leafletsOneImgUrl = ld.getLeafletsOneImgUrl(con);

                DbUtil.getClose(con);
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (null != temp && !temp.equals("httpsRequest-error")) {
                HashMap map = readStringXmlOut(temp);
                if (null != map) {
                    return_code = (String) map.get("return_code");
                    return_msg = (String) map.get("return_msg");
                    result_code = (String) map.get("result_code");
                    prepay_id = (String) map.get("prepay_id");
                    //支付
//                System.out.println(prepay_id);
                    req.setAttribute("device_info", device_info);
                    req.setAttribute("result", "SUCCESS");
                    req.setAttribute("appId", Contacts.APPID);
                    String timeStamp = String.valueOf(new Date().getTime());
                    req.setAttribute("timeStamp", timeStamp);
                    String nonceStr = MD5Utils.digest(String.valueOf(Math.random()));
                    req.setAttribute("nonceStr", nonceStr);
                    req.setAttribute("package1", "prepay_id=" + prepay_id);
                    req.setAttribute("signType", Contacts.SIGN_TYPE);

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("appId", Contacts.APPID);
                    params.put("timeStamp", timeStamp);
                    params.put("nonceStr", nonceStr);
                    params.put("package", "prepay_id=" + prepay_id);
                    params.put("signType", Contacts.SIGN_TYPE);
                    req.setAttribute("paySign", WXPaySignUtil.Sign(params, Contacts.SECRET));

                    req.setAttribute("out_trade_no", out_trade_no);
//                System.out.println("1");
                    if(null!=leafletsOneTitle&&null!=leafletsOneImgUrl){
                        req.setAttribute("leafletsOneTitle", leafletsOneTitle);
                        req.setAttribute("leafletsOneImgUrl", leafletsOneImgUrl);
                    }else{
                        req.setAttribute("leafletsOneTitle", "欢迎光临");
                        req.setAttribute("leafletsOneImgUrl", "http://www.wxfslp.xyz/mer_picture/car.png");
                    }


                    req.getRequestDispatcher("/WEB-INF/page/wx_pay.jsp").forward(req, resp);
                } else {
//                System.out.println("2");
                    req.setAttribute("result", "FAIL");
//                System.out.println(prepay_id);
                    req.getRequestDispatcher("/WEB-INF/page/wx_pay.jsp").forward(req, resp);
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }


    private String getOpenID(String code, HttpServletRequest req) {
        getopenid_code goc = new getopenid_code(code);
        String url = goc.generateUrl();

        StringBuilder result = new StringBuilder();
        BufferedReader in;
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

            String openid = "";
            JSONObject jsonObject = new JSONObject(result.toString());
            openid = jsonObject.getString("openid");
            if (openid != null && !openid.equals("")) {
                return openid;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("--WXGetOpenID.getOpenID-1-" + e.toString());
        }

        return null;
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
