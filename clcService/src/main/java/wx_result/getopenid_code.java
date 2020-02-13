package wx_result;

import global.Contacts;

public class getopenid_code {

    private static final String ERROR_10003 = "10003";//redirect_uri域名与后台配置不一致
    private static final String ERROR_10004 = "10004";//此公众号被封禁
    private static final String ERROR_10005 = "10005";//此公众号并没有这些scope的权限
    private static final String ERROR_10006 = "10006";//必须关注此测试号
    private static final String ERROR_10009 = "10009";//操作太频繁了，请稍后重试
    private static final String ERROR_10010 = "10010";//scope不能为空
    private static final String ERROR_10011 = "10011";//redirect_uri不能为空
    private static final String ERROR_10012 = "10012";//appid不能为空
    private static final String ERROR_10013 = "10013";//state不能为空
    private static final String ERROR_10015 = "10015";//公众号未授权第三方平台，请检查授权状态
    private static final String ERROR_10016 = "10016";//不支持微信开放平台的Appid，请使用公众号Appid

    private String appid;
    private String secret;
    private String code;
    private String grant_type;

//    https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code

    public getopenid_code(String code){
        appid = Contacts.APPID;
        secret = Contacts.SECRET;
        this.code = code;
        grant_type = "authorization_code";
    }

    public String generateUrl(){
        String s = Contacts.WX_GET_ACCESS_TOKEN_URL+"?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type="+grant_type;
        return s;
    }

}
