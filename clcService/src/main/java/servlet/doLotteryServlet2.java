package servlet;

import dao.OutTradeNoDao;
import dao.PrizeDao;
import dao.SelledDao;
import org.json.JSONException;
import org.json.JSONObject;
import util.DbUtil;
import util.WXPayUtil;
import util.WXRefundUtil;
import util.WXSendCoupon;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Map;

/**
 * 点击领奖按钮后触发的服务 （不打开新的页面）
 */
public class doLotteryServlet2 extends HttpServlet {

    DbUtil db = new DbUtil();

    public doLotteryServlet2() {
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

        String device_info = request.getParameter("device_info");
        String out_trade_no = request.getParameter("out_trade_no");
        String openid = request.getParameter("openid");
        String resultString = "error";
//        System.out.println("out_trade_no========doLotteryServlet============" + out_trade_no);

        OutTradeNoDao otnd = new OutTradeNoDao();
        PrizeDao pd = new PrizeDao(device_info);
        SelledDao sd = new SelledDao(device_info);

        int lotteryStatus = 0;
        try {
            Connection con = db.getCon();

            lotteryStatus = otnd.getLotteryStatus(con, out_trade_no) + 10;
            otnd.setLotteryStatus(con, out_trade_no, lotteryStatus);
            //判断是退款还是发放优惠劵
            int temp_lotterStatus = lotteryStatus - 20;
            String coupon_stock_id = pd.get_coupon_stock_id(con, temp_lotterStatus);
            if(null!=coupon_stock_id && !coupon_stock_id.equals("")){//发放优惠劵
                resultString = doCoupon(coupon_stock_id,openid);//发放优惠劵
            }else{//退款
                int refund_fee = pd.getPriceValue(con, temp_lotterStatus);
//                System.out.println("refund_fee------------------"+refund_fee);
                if(refund_fee!=0){//中奖了 refund_fee是退款的值 单位是分
                    int total_fee = sd.get_total_fee(con, out_trade_no);
                    if(-1!=total_fee){
                        resultString = doRefund(device_info, out_trade_no, total_fee,refund_fee);//退款
                        sd.set_refund_fee(con,out_trade_no,refund_fee);
                    }
                }
            }
            sd.set_lottery_value(con,out_trade_no,temp_lotterStatus);
//            System.out.println("resultString------------"+resultString);

            DbUtil.getClose(con);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject jo = new JSONObject();
        try {
            jo.put("out_trade_no", out_trade_no);
            jo.put("lotteryStatus", lotteryStatus - 20);
            jo.put("resultString",resultString);
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("doLotteryServlet2---"+e.toString());
        }

        String json_string = jo.toString();

        writer.write(json_string);
    }

    /**
     * 退款
     */
    private String doRefund(String device_info, String out_trade_no,int total_fee, int refund_fee) {
        WXRefundUtil wx_ru = new WXRefundUtil(device_info,out_trade_no,total_fee,refund_fee);
        String result = wx_ru.onRefund();
        String resultString = "退款失败 支付的金额小于退款的金额";
        // xml转换为map
        try {
            Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
            String return_code = resultMap.get("return_code");
            String result_code = resultMap.get("result_code");
//            System.out.println("return_code------------------"+return_code);
//            System.out.println("result_code------------------"+result_code);
            if(return_code.equals("SUCCESS")&&result_code.equals("SUCCESS")){
                resultString = "退款成功 稍后会直接退款到账";
            }
//            System.out.println("--doLotteryServlet2.doRefund--"+result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("--doLotteryServlet2.doRefund--"+e.toString());
        }
        return resultString;
    }

    /**
     * 发放优惠劵
     * coupon_stock_id 优惠劵的批次号
     */
    private String doCoupon(String coupon_stock_id,String openid) {
        WXSendCoupon wx_sc = new WXSendCoupon(coupon_stock_id,openid);
        String result = wx_sc.onSendCoupon();
        String resultString = "发放优惠劵失败";
        // xml转换为map
        try {
            Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
            String return_code = resultMap.get("return_code");
            String result_code = resultMap.get("result_code");
            if(return_code.equals("SUCCESS")&&result_code.equals("SUCCESS")){
                resultString = "发放优惠劵成功";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("--doLotteryServlet2.doCoupon--"+e.toString());
        }
        return resultString;
    }

}
