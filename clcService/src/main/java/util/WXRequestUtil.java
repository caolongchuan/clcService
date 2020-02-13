package util;

import java.io.*;
import java.net.InetAddress;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

//public class WXRequestUtil {
//    public static void main(String[] args) {
//        SendPayment("苹果","20170106113324",1,"1");
//    }
//
//    /*
//     * 发起支付请求
//     * body	商品描述
//     * out_trade_no	订单号
//     * total_fee	订单金额		单位  元
//     * product_id	商品ID
//     */
//    public static Map<String,String> SendPayment(String body,String out_trade_no,double total_fee,String product_id){
//        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
//        String xml = WXParamGenerate(body,out_trade_no,total_fee,product_id);
//        String res = httpsRequest(url,"POST",xml);
//
//        Map<String,String> data = null;
//        try {
//            data = doXMLParse(res);
//        } catch (Exception e) {
//        }
//        return data;
//    }
//
//    public static String NonceStr(){
//        String res = Base64.encodeAsString(Math.random()+"::"+new Date().toString()).substring(0, 30);
//        return res;
//    }
//
//    public static String GetIp() {
//        InetAddress ia=null;
//        try {
//            ia=InetAddress.getLocalHost();
//            String localip=ia.getHostAddress();
//            return localip;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public static String GetSign(Map<String,String> param){
//        String StringA =  Util.formatUrlMap(param, false, false);
//        String stringSignTemp = MD5.md5(StringA+"&key="+ConstantUtil.API_KEY).toUpperCase();
//        return stringSignTemp;
//    }
//
//    //Map转xml数据
//    public static String GetMapToXML(Map<String,String> param){
//        StringBuffer sb = new StringBuffer();
//        sb.append("<xml>");
//        for (Map.Entry<String,String> entry : param.entrySet()) {
//            sb.append("<"+ entry.getKey() +">");
//            sb.append(entry.getValue());
//            sb.append("</"+ entry.getKey() +">");
//        }
//        sb.append("</xml>");
//        return sb.toString();
//    }
//
//
//    //微信统一下单参数设置
//    public static String WXParamGenerate(String description,String out_trade_no,double total_fee,String product_id){
//        int fee = (int)(total_fee * 100.00);
//        Map<String,String> param = new HashMap<String,String>();
//        param.put("appid", ConstantUtil.APP_ID);
//        param.put("mch_id", ConstantUtil.MCH_ID);
//        param.put("nonce_str",NonceStr());
//        param.put("body", description);
//        param.put("out_trade_no",out_trade_no);
//        param.put("total_fee", fee+"");
//        param.put("spbill_create_ip", GetIp());
//        param.put("notify_url", ConstantUtil.WEIXIN_NOTIFY);
//        param.put("trade_type", "NATIVE");
//        param.put("product_id", product_id+"");
//
//        String sign = GetSign(param);
//
//        param.put("sign", sign);
//        return GetMapToXML(param);
//    }
//
//    //发起微信支付请求
//    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
//        try {
//            URL url = new URL(requestUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setUseCaches(false);
//            // 设置请求方式（GET/POST）
//            conn.setRequestMethod(requestMethod);
//            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
//            // 当outputStr不为null时向输出流写数据
//            if (null != outputStr) {
//                OutputStream outputStream = conn.getOutputStream();
//                // 注意编码格式
//                outputStream.write(outputStr.getBytes("UTF-8"));
//                outputStream.close();
//            }
//            // 从输入流读取返回内容
//            InputStream inputStream = conn.getInputStream();
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            String str = null;
//            StringBuffer buffer = new StringBuffer();
//            while ((str = bufferedReader.readLine()) != null) {
//                buffer.append(str);
//            }
//            // 释放资源
//            bufferedReader.close();
//            inputStreamReader.close();
//            inputStream.close();
//            inputStream = null;
//            conn.disconnect();
//            return buffer.toString();
//        } catch (ConnectException ce) {
//            System.out.println("连接超时：{}"+ ce);
//        } catch (Exception e) {
//            System.out.println("https请求异常：{}"+ e);
//        }
//        return null;
//    }
//
//    //退款的请求方法
//    public static String httpsRequest2(String requestUrl, String requestMethod, String outputStr) throws Exception {
//        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
//        StringBuilder res = new StringBuilder("");
//        FileInputStream instream = new FileInputStream(new File("/home/apiclient_cert.p12"));
//        try {
//            keyStore.load(instream, "".toCharArray());
//        } finally {
//            instream.close();
//        }
//
//        // Trust own CA and all self-signed certs
//        SSLContext sslcontext = SSLContexts.custom()
//                .loadKeyMaterial(keyStore, "1313329201".toCharArray())
//                .build();
//        // Allow TLSv1 protocol only
//        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
//                sslcontext,
//                new String[] { "TLSv1" },
//                null,
//                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
//        CloseableHttpClient httpclient = HttpClients.custom()
//                .setSSLSocketFactory(sslsf)
//                .build();
//        try {
//
//            HttpPost httpost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");
//            httpost.addHeader("Connection", "keep-alive");
//            httpost.addHeader("Accept", "*/*");
//            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//            httpost.addHeader("Host", "api.mch.weixin.qq.com");
//            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
//            httpost.addHeader("Cache-Control", "max-age=0");
//            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
//            StringEntity entity2 = new StringEntity(outputStr ,Consts.UTF_8);
//            httpost.setEntity(entity2);
//            System.out.println("executing request" + httpost.getRequestLine());
//
//            CloseableHttpResponse response = httpclient.execute(httpost);
//
//            try {
//                HttpEntity entity = response.getEntity();
//
//                System.out.println("----------------------------------------");
//                System.out.println(response.getStatusLine());
//                if (entity != null) {
//                    System.out.println("Response content length: " + entity.getContentLength());
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
//                    String text = "";
//                    res.append(text);
//                    while ((text = bufferedReader.readLine()) != null) {
//                        res.append(text);
//                        System.out.println(text);
//                    }
//
//                }
//                EntityUtils.consume(entity);
//            } finally {
//                response.close();
//            }
//        } finally {
//            httpclient.close();
//        }
//        return  res.toString();
//
//    }
//
//    //xml解析
//    public static Map<String, String> doXMLParse(String strxml) throws Exception {
//        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
//        if(null == strxml || "".equals(strxml)) {
//            return null;
//        }
//
//        Map<String,String> m = new HashMap<String,String>();
//        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
//        SAXBuilder builder = new SAXBuilder();
//        Document doc = builder.build(in);
//        Element root = doc.getRootElement();
//        List list = root.getChildren();
//        Iterator it = list.iterator();
//        while(it.hasNext()) {
//            Element e = (Element) it.next();
//            String k = e.getName();
//            String v = "";
//            List children = e.getChildren();
//            if(children.isEmpty()) {
//                v = e.getTextNormalize();
//            } else {
//                v = getChildrenText(children);
//            }
//
//            m.put(k, v);
//        }
//
//        //关闭流
//        in.close();
//        return m;
//    }
//
//    public static String getChildrenText(List children) {
//        StringBuffer sb = new StringBuffer();
//        if(!children.isEmpty()) {
//            Iterator it = children.iterator();
//            while(it.hasNext()) {
//                Element e = (Element) it.next();
//                String name = e.getName();
//                String value = e.getTextNormalize();
//                List list = e.getChildren();
//                sb.append("<" + name + ">");
//                if(!list.isEmpty()) {
//                    sb.append(getChildrenText(list));
//                }
//                sb.append(value);
//                sb.append("</" + name + ">");
//            }
//        }
//        return sb.toString();
//    }
//
//}
