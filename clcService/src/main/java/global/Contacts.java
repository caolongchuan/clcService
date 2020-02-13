package global;

public class Contacts {

    //25小时的毫秒数
    public static final Long HOUR_25 = 90000000L;
    public static final Long HOUR_24 = 86400000L;

    //微信给提供的用于获取用户openID的接口地址
    public static final String WX_GET_OPENID_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";
    //通过code换取网页授权access_token
    public static final String WX_GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
    //获取用户openid的回调接口地址
    public static final String REDIRECT_URI = "http://www.wxfslp.xyz/WXGetOpenID?grid_name=";
    //支付通知地址，异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
    public static final String PAY_RESULT_NOTIFY_URL = "http://www.wxfslp.xyz/PayComplete";


    //统一下单接口参数
    public static final String UNITE_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public static final String APPID = "wxedc7f7cb6de6705e";//公众号id
    public static final String SECRET = "7c8d7c40795f0ac7bae3adeb3ccc176f";
    public static final String MCH_ID = "1566280841";
    public static final String SIGN_TYPE= "MD5";
    public static final String NOTIFY_URL = "http://www.wxfslp.xyz/clcService_war_exploded/PayComplete";
    public static final String TRADE_TYPE = "JSAPI";

    //免单限制
    public static final int MAX_FEE = 5000;//单位是分不是元

    //发放优惠劵接口
    public static final String SEND_COUPON_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/send_coupon";
    //退款接口
    public static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    public static final String MCHID = "1566280841";
    public static final String WXSSL_STRING_PAHT = "C:\\CLC\\asdfaiowenfoa;jeaneoaiam\\WXCertUtil\\cert\\1566280841_20191228_cert\\apiclient_cert.p12";

}
