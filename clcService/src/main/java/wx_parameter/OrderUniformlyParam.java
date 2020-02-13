package wx_parameter;

import global.Contacts;
import util.MD5Utils;
import util.WXPaySignUtil;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderUniformlyParam {

    private Map params;

    private String appid;//公众号id 商户注册具有支付权限的公众号成功后即可获得
    private String mch_id;//商户号 微信支付分配的商户号
    private String device_info;//设备号 自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
    private String nonce_str;//随机字符串，不长于32位
    private String sign;//签名
    private String sign_type;//签名类型，默认为MD5，支持HMAC-SHA256和MD5
    private String body;//商品描述 商品简单描述，该字段请按照规范传递，
    private String attach;//附加信息 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。
    private String out_trade_no;//商户订单号 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一
    private int total_fee;//支付金额 订单总金额，单位为分
    private String spbill_create_ip;//终端ip 支持IPV4和IPV6两种格式的IP地址。用户的客户端IP
    private String goods_tag;//订单优惠标记，使用代金券或立减优惠功能时需要的参数
    private String notify_url;//通知地址 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
    private String trade_type;//交易类型 SAPI -JSAPI支付 NATIVE -Native支付 APP -APP支付
    private String openid;//用户标识 trade_type=JSAPI时（即JSAPI支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识。

    public OrderUniformlyParam(String device_info, String body, String attach, int total_fee, String ip, String openid) {
        appid = Contacts.APPID;
        mch_id = Contacts.MCH_ID;
        this.device_info = device_info;
        nonce_str = gene_nonceStr();
        sign_type = Contacts.SIGN_TYPE;
        this.body = body;
        this.attach = attach;
        out_trade_no = gene_out_trade_no(device_info);
        this.total_fee = total_fee;
        this.spbill_create_ip = ip;
        this.goods_tag = device_info;
        notify_url = Contacts.PAY_RESULT_NOTIFY_URL;
        trade_type = Contacts.TRADE_TYPE;
        this.openid = openid;

        params = gene_Sign();
    }

    public String orderUniformly() {
        String s = "orderUniformly--cuowu";
        if (null != params) {
            String XMLStr = GetMapToXML(params);
            return httpsRequest(Contacts.UNITE_ORDER, "POST", XMLStr);
        } else {
            return s;
        }
    }

    public String get_out_trade_no() {
        return this.out_trade_no;
    }

    //生成签名以及参数集合map
    private Map gene_Sign() {
        String sign = "";
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", appid);
        params.put("mch_id", mch_id);
        params.put("device_info", device_info);
        params.put("nonce_str", nonce_str);
        params.put("sign_type", sign_type);
        params.put("body", body);
        params.put("attach", attach);
        params.put("out_trade_no", out_trade_no);
        params.put("total_fee", String.valueOf(total_fee));
        params.put("spbill_create_ip", spbill_create_ip);
        params.put("goods_tag", goods_tag);
        params.put("notify_url", notify_url);
        params.put("trade_type", trade_type);
        params.put("openid", openid);

        try {
            sign = WXPaySignUtil.Sign(params, Contacts.SECRET);
            params.put("sign", sign);
            return params;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    //生成随机字符串
    private String gene_nonceStr() {
        return MD5Utils.digest(String.valueOf(Math.random()));
    }

    //生成商户订单号 根据时间生成
    private String gene_out_trade_no(String device_info) {
        return device_info + String.valueOf(new Date().getTime());
    }

    //Map转xml数据
    public static String GetMapToXML(Map<String, String> param) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (Map.Entry<String, String> entry : param.entrySet()) {
            sb.append("<" + entry.getKey() + ">");
            sb.append(entry.getValue());
            sb.append("</" + entry.getKey() + ">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    //发起微信支付请求
    private String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        String r = "httpsRequest-error";
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            System.out.println("连接超时：{}" + ce);
        } catch (Exception e) {
            System.out.println("https请求异常：{}" + e);
        }
        return r;
    }


}
