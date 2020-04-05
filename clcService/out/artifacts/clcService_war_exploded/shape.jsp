<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2019/11/18
  Time: 16:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <title></title>
</head>
<script type="text/javascript">
    <%
            String out_trade_no=request.getParameter("out_trade_no");
            String device_info = request.getParameter("device_info");
         %>
    window.onload = function () {
        <%--alert("<%=out_trade_no %>");--%>
        var v1 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxedc7f7cb6de6705e&redirect_uri=http%3A%2F%2Fwww.wxfslp.xyz%2FLotteryServlet";
        var v2 = "%3Fout_trade_no%3Dout_trade_no";
        var v3 = "%26device_info%3Ddevice_info";
        var v4 = "&response_type=code&scope=snsapi_base&connect_redirect=1&state=123#wechat_redirect";

        window.location.href = v1 + v2 +v3+v4;
    }
</script>
<body>
</body>
</html>