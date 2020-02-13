package servlet;

import dao.*;
import entiry.Merchandise;
import global.Contacts;
import util.DbUtil;
import util.WXPayConstants;
import util.WXPayUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


/**
 * 微信付款通知
 *
 * 该链接是通过【统一下单API】中提交的参数notify_url设置，如果链接无法访问，商户将无法接收到微信通知。
 * 通知url必须为直接可访问的url，不能携带参数。示例：notify_url：“https://pay.weixin.qq.com/wxpay/pay.action”
 * <p>
 * 支付完成后，微信会把相关支付结果和用户信息发送给商户，商户需要接收处理，并返回应答。
 * 对后台通知交互时，如果微信收到商户的应答不是成功或超时，微信认为通知失败，微信会通过一定的策略定期重新发起通知，尽可能提高通知的成功率，但微信不保证通知最终能成功。
 * （通知频率为15/15/30/180/1800/1800/1800/1800/3600，单位：秒）
 * 注意：同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知。
 * 推荐的做法是，当收到通知进行处理时，首先检查对应业务数据的状态，判断该通知是否已经处理过，如果没有处理过再进行处理，如果处理过直接返回结果成功。在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱。
 * 特别提醒：商户系统对于支付结果通知的内容一定要做签名验证，防止数据泄漏导致出现“假通知”，造成资金损失。
 */
public class PayComplete extends HttpServlet {
    /**
     * 返回成功xml
     */
    private String resSuccessXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";

    /**
     * 返回失败xml
     */
    private String resFailXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml>";

    DbUtil db = new DbUtil();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String resXml = "";
        InputStream inStream;
        try {
            inStream = req.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            // 获取微信调用我们notify_url的返回信息
            String result = new String(outSteam.toByteArray(), "utf-8");

            // 关闭流
            outSteam.close();
            inStream.close();

            // xml转换为map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
            boolean isSuccess = false;
            if (WXPayConstants.SUCCESS.equalsIgnoreCase(resultMap.get("return_code"))) {
                if (WXPayUtil.isSignatureValid(resultMap, Contacts.SECRET)) {
                    // 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
                    resXml = resSuccessXml;
                    isSuccess = true;
//                    System.out.println("wxnotify:微信支付----判断签名正确");
                    String device_info = resultMap.get("device_info");
//                    System.out.println("device_info:设备信息----" + device_info);
                    String total_fee = resultMap.get("total_fee");
//                    System.out.println("total_fee:支付金额----" + total_fee);
                    String coupon_fee = resultMap.get("coupon_fee");
                    if(null==coupon_fee){
                        coupon_fee = "0";
                    }
//                    System.out.println(coupon_fee+"coupon_fee-----------------");
                    String out_trade_no = resultMap.get("out_trade_no");
//                    System.out.println("out_trade_no:商户订单号----" + out_trade_no);
                    String attach = resultMap.get("attach");
//                    System.out.println("attach:自定义信息----" + attach);
                    String openid = resultMap.get("openid");
//                    System.out.println("openid:自定义信息----" + openid);

                    //将该笔交易的订单号暂时存到数据库中 以后程序运行到这里的时候自动删除超过25小时的数据
                    Connection con = db.getCon();
                    OutTradeNoDao otnd = new OutTradeNoDao();
                    //判断该订单信息是否已经保存到数据库 如果有就不保存 如果没有保存 用于保证只处理一次微信发来的支付成功信息
                    boolean isHave = otnd.checkIsHave(con, out_trade_no);
                    if(!isHave){
                        int fee = Integer.valueOf(total_fee);
                        //将该订单临时保存到数据库
                        otnd.insert(con,out_trade_no,new Date().getTime(),openid,fee);
                        //将该订单永久保存到数据库
                        SelledDao sd = new SelledDao(device_info);
                        //获取当前时间并格式化
                        Date d = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String create_time = sdf.format(d).trim();
                        //获取此次卖出去的所以商品名称的组合
                        String mername = getMerName(con,device_info,attach);
                        sd.insert(con,out_trade_no,create_time,mername,
                                Integer.valueOf(total_fee),0,Integer.valueOf(coupon_fee),0);

                        //将卖出去的东西保存到数据库 等待终端机取走并置空字符串
                        AllBuyedDao abd = new AllBuyedDao();
                        abd.setMsgByInfo(con,device_info,attach);
//                        TimeUpdataWXToken.getInstance().setDataByName(device_info, attach);
                        //改变柜子状态
                        GridDao gridDao = new GridDao(device_info);
                        String[] attach_arr = attach.split(" ");
                        for(int i=0;i<attach_arr.length;i++){
                            gridDao.setGridStatus(con,Integer.valueOf(attach_arr[i]),0);
                        }
                    }
                    otnd.deleteOutTime(con);

                    DbUtil.getClose(con);
                } else {
                    System.out.println("wxnotify:微信支付----判断签名错误");
                }
            } else {
                System.out.println("wxnotify:微信支付----判断签名错误" + resultMap.get("return_msg"));
                resXml = resFailXml;
            }

        } catch (Exception e) {
            System.out.println("wxnotify:支付回调发布异常：" + e);
        } finally {
            try {
                // 处理业务完毕
                BufferedOutputStream out = new BufferedOutputStream(resp.getOutputStream());
                out.write(resXml.getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                System.out.println("wxnotify:支付回调发布异常：" + e);
                WXPayUtil.getLogger().error("wxnotify:支付回调发布异常:out：", e);
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    private String getMerName(Connection con,String device_info,String attach){
        GridDao gd = new GridDao(device_info);
        MerchandiseDao md = new MerchandiseDao(device_info);
        StringBuilder mer_name = new StringBuilder(" ");

        //获取每个柜号
        String[] split = attach.split(" ");
        if (split.length != 0) {
            for (int i = 0; i < split.length; i++) {
                if(!split[i].equals("")){
                    //根据柜号获取商品号
                    int mer_no = gd.getGridMerNoByGridNo(con, Integer.valueOf(split[i]));
                    Merchandise merchandiseByID = md.getMerchandiseByID(con, mer_no);
                    String temp = merchandiseByID.getName();
                    mer_name.append(" ").append(temp);
                }
            }
        }
        return mer_name.toString();
    }
}