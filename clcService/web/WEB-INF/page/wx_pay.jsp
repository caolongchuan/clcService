<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2019/12/5
  Time: 10:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
 <!DOCTYPE html>
<html>
  
<head>
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.4.0.js"></script>
    <title>微信支付</title>    
</head>
  
<script type="text/javascript">
    var out_trade_no = "${out_trade_no}";
    var device_info = "${device_info}";

    window.onload = pay();

    //支付
    function pay() {
        // alert(out_trade_no);
        var result = "${result}";
        if (result == 'SUCCESS') {
            if (typeof WeixinJSBridge == "undefined") {
                if (document.addEventListener) {
                    document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
                } else if (document.attachEvent) {
                    document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                    document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
                }
            } else {
                alert("onBridgeReady");
                onBridgeReady();
            }
        } else {
            alert("支付失败");
        }
    }

    //调用微信提供的支付SDK
    function onBridgeReady() {
        WeixinJSBridge.invoke(
            'getBrandWCPayRequest', {
                "appId": "${appId}",     //公众号名称，由商户传入
                "timeStamp": "${timeStamp}",         //时间戳，自1970年以来的秒数
                "nonceStr": "${nonceStr}", //随机串
                "package": "${package1}",
                "signType": "${signType}",         //微信签名方式：
                "paySign": "${paySign}" //微信签名
            },
            function (res) {
                if (res.err_msg == "get_brand_wcpay_request:ok") {
                    // 使用以上方式判断前端返回,微信团队郑重提示：
                    //res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
                    document.getElementById("back_2").style.display = "inline";
                    document.getElementById("back_3").style.display = "inline";
                    //分享朋友圈
                    fenxiang();
                    // interval = window.setInterval("share()","3000");
                    // window.clearInterval(interval);     //停止执行setInterval循环
                } else {
                    alert("支付失败 请重新扫码")
                }
            });
    }

    //设置分享朋友圈
    function fenxiang() {
        var link1 = 'http://www.wxfslp.xyz/shape.jsp';
        // alert(link1);
        var xmlhttp;
        if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {// code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                var result = JSON.parse(xmlhttp.responseText);
                if (!result == "") {
                    wx.config({
                        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                        appId: result.appId, // 必填，公众号的唯一标识
                        timestamp: result.timestamp, // 必填，生成签名的时间戳
                        nonceStr: result.nonceStr, // 必填，生成签名的随机串
                        signature: result.signature,// 必填，签名
                        // 必填，把要使用的方法名放到这个数组中。
                        jsApiList: ['updateAppMessageShareData', 'updateTimelineShareData'] // 必填，需要使用的JS接口列表
                    });
                    wx.ready(function () {
                        // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
                        wx.updateTimelineShareData({
                            title: '${leafletsOneTitle}', // 分享标题
                            // link: 'http://www.wxfslp.xyz/shape.jsp?out_trade_no=' + out_trade_no + '&device_info=' + device_info, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                            link: link1,// 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                            imgUrl: '${leafletsOneImgUrl}', // 分享图标
                            success: function () {
                                // 设置成功
                                document.getElementById("back_1").style.display = "inline";
                                document.getElementById("back_2").style.display = "none";
                                document.getElementById("back_3").style.display = "none";
                            }
                        });
                    });
                }
            }
        };
        xmlhttp.open("POST", "http://www.wxfslp.xyz/clcService_war_exploded/ShareServlet", true);
        // xmlhttp.open("POST","http://172.16.18.134:8080/clcService_war_exploded/ShareServlet",true);
        xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xmlhttp.send("url=" + window.location.href.split('#')[0]);
    }

</script>
<body>
<img id="back_1" src="${pageContext.request.contextPath}/mer_picture/back_1.png"
     style="display:none;position: absolute;left: 0%;top: 0%;width: 100%;height: 100%">
<img id="back_2" src="${pageContext.request.contextPath}/mer_picture/wx_pyq_bg.png"
     style="position: absolute;left: 0%;top: 0%;width: 100%;height: 100%;display: none">
<img id="back_3" src="${pageContext.request.contextPath}/mer_picture/wait.gif"
     style="position: absolute;left: 40%;top: 50%;width: 20%;height: auto;display: none">
</body>
</html>