<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2019/12/21
  Time: 13:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>领奖</title>
</head>

<script type="text/javascript">
    var lingjinged = false;

    window.onload = function () {
        var xmlhttp;
        if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {// code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                document.getElementById("main").src = xmlhttp.responseText;
            }
        };
        xmlhttp.open("POST", "http://www.wxfslp.xyz/clcService_war_exploded/LeafletImgServlet", true);
        // xmlhttp.open("POST","http://172.16.18.134:8080/clcService_war_exploded/LeafletImgServlet",true);
        xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xmlhttp.send("type=lingjiang");
    };


    function lingjiang() {
        if(!lingjinged){
            var lingjiang = document.getElementById("lingjiang");
            lingjiang.src = "mer_picture/wait.gif";
            var xmlhttp;
            if (window.XMLHttpRequest)
            {// code for IE7+, Firefox, Chrome, Opera, Safari
                xmlhttp=new XMLHttpRequest();
            }
            else
            {// code for IE6, IE5
                xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
            }
            xmlhttp.onreadystatechange=function()
            {
                if (xmlhttp.readyState==4 && xmlhttp.status==200)
                {
                    document.getElementById("lingjiang").style.display="none";
                    var result = JSON.parse(xmlhttp.responseText);
                    Toast(result.resultString);
                    setTimeout( function(){
                        window.location.href="http://www.wxfslp.xyz/shape2.jsp?out_trade_no=${out_trade_no}&lotterValue="+result.lotteryStatus;
                    }, 3 * 1000 );//延迟3秒
                }
            };
            xmlhttp.open("POST","http://www.wxfslp.xyz/clcService_war_exploded/doLotteryServlet2",true);
            // xmlhttp.open("POST","http://172.16.18.134:8080/clcService_war_exploded/doLotteryServlet",true);
            xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
            xmlhttp.send("out_trade_no=${out_trade_no}&device_info=${device_info}&openid=${openid}");
            lingjinged = true;
        }
    }

    function Toast(msg, duration) {
        duration = isNaN(duration) ? 3000 : duration;
        var m = document.createElement('div');
        m.innerHTML = msg;
        m.style.cssText = "width: 60%;min-width: 50px;opacity: 0.7;height: 60px;color: rgb(255, 255, 255);line-height: 30px;text-align: center;border-radius: 5px;position: fixed;top: 80%;left: 20%;z-index: 999999;background: rgb(0, 0, 0);font-size: 30px;";
        document.body.appendChild(m);
        setTimeout(function () {
            var d = 0.5;
            m.style.webkitTransition = '-webkit-transform ' + d + 's ease-in, opacity ' + d + 's ease-in';
            m.style.opacity = '0';
            setTimeout(function () {
                document.body.removeChild(m)
            }, d * 1000);
        }, duration);
    }

</script>

<body>
<div style="position: absolute;left: 0;top: 0;width: 100%;height: auto;"><img id="main" style="width: 100%;height: auto;"></div>
<div onclick="lingjiang()" style="position: absolute;left: 30%;bottom: 10%;width: 40%;height:auto;"><img id="lingjiang" src="mer_picture/icon_lingjiang.png" style="width: 100%;height:auto;"></div>

</body>
</html>
