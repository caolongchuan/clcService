package wx_parameter;

import global.Contacts;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GetOpenIdParam {

    private String appid;//公众号的唯一标识
    private String redirect_uri;//授权后重定向的回调链接地址， 请使用 urlEncode 对链接进行处理
    private String response_type;//返回类型，请填写code
    private String scope;//应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且， 即使在未关注的情况下，只要用户授权，也能获取其信息 ）
    private String state;//重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
    private String wechat_redirect;//#wechat_redirect  无论直接打开还是做页面302重定向时候，必须带此参数

    private String self_param;//自定义的参数名称 会在调用回调链接地址时传过去

    public GetOpenIdParam(String state, String self_param){
        appid = Contacts.APPID;
        redirect_uri = Contacts.REDIRECT_URI;
        response_type = "code";
        scope = "snsapi_base";
        this.state = state;
        this.wechat_redirect = "#wechat_redirect";
        this.self_param = self_param;
    }

    public String generateAddr(){
        String result = "";
        try {
            String urlString = URLEncoder.encode(redirect_uri,"UTF-8");
            result =  Contacts.WX_GET_OPENID_URL+"?appid="+appid+"&redirect_uri="+urlString+self_param+"&response_type="+response_type+"&scope="+scope+"&connect_redirect=1&state="+state+wechat_redirect;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            result = e.toString();
        }
        return result;
    }
}
