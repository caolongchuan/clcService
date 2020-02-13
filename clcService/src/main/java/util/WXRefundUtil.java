package util;

import global.Contacts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WXRefundUtil {

    private Map<String, String> param;

    public WXRefundUtil(String device_info, String out_trade_no,int total_fee,int refund_fee){
        String sign = "";
        param = new HashMap<String, String>();
        param.put("appid", Contacts.APPID);
        param.put("mch_id",Contacts.MCH_ID);
        param.put("nonce_str",MD5Utils.digest(String.valueOf(Math.random())));
        param.put("out_trade_no",out_trade_no);
        String out_refund_no = gene_out_refund_no(device_info).trim();
//        System.out.println("out_refund_no------------"+out_refund_no);
        param.put("out_refund_no",out_refund_no);
        param.put("total_fee",String.valueOf(total_fee));
        param.put("refund_fee",String.valueOf(refund_fee));
        try {
            sign = WXPaySignUtil.Sign(param, Contacts.SECRET);
            param.put("sign", sign);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String onRefund(){
        String s = "onRefund--error";
        if (null != param) {
            String XMLStr = GetMapToXML(param);
            try {
                String sss = doRefund(Contacts.REFUND_URL, XMLStr);
//                System.out.println("sss------------"+sss);
                return doRefund(Contacts.REFUND_URL, XMLStr);
            } catch (Exception e) {
                e.printStackTrace();
                s = e.toString();
            }
        }
        return s;
    }

    //根据时间生成商户退款单号
    private String gene_out_refund_no(String device_info) {
        return device_info + "out_refund_no" + String.valueOf(new Date().getTime());
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

    /**
     * 退款
     * @param url
     * @param data
     * @return
     * @throws Exception
     */
    private String doRefund(String url,String data) throws Exception {
        /**
         * 注意PKCS12证书 是从微信商户平台-》账户设置-》 API安全 中下载的
         */

        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File(Contacts.WXSSL_STRING_PAHT));//P12文件在服务器磁盘中的目录
        try {
            /**xinzhang1577515721510
             * 此处要改成你的MCHID
             * */
            keyStore.load(instream, Contacts.MCHID.toCharArray());//这里写密码..默认是你的MCHID
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        /**
         * 此处要改成你的MCHID
         * */
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, Contacts.MCHID.toCharArray())//这里也是写密码的
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        try (CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build()) {
            HttpPost httpost = new HttpPost(url); // 设置响应头信息
            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));
            try (CloseableHttpResponse response = httpclient.execute(httpost)) {
                HttpEntity entity = response.getEntity();

                String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                EntityUtils.consume(entity);
                return jsonStr;
            }
        }
    }



}

