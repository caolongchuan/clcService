package wx_parameter;

public class H5PayInWXParam {

    private String appId;//公众号id 商户注册具有支付权限的公众号成功后即可获得
    private String timeStamp;//时间戳 当前的时间
    private String nonceStr;//随机字符串，不长于32位
    private String package_;//统一下单接口返回的prepay_id参数值，提交格式如：prepay_id=***
    private String signType;//签名类型，默认为MD5，支持HMAC-SHA256和MD5
    private String paySign;//签名

}
