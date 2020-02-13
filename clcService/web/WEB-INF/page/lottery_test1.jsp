<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2019/12/19
  Time: 14:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/lottery.css" type="text/css">
    <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.4.0.js"></script>
    <title>抽奖</title>
</head>
<script type="text/javascript">
    var lottery_status = 0;

    // var out_trade_no;
    var lotter_value;
    var quanshu;
    var run_time;
    var run_index;
    var lotter_img_index;

    window.onload = function () {
        window.setInterval(back_img_change, 200);
        //初始化九宫格内各个格格的文字内容
        initLotteryNameText();
    };

    //初始化九宫格内各个格格的文字内容
    function initLotteryNameText() {
        document.getElementById("lottery_text_1").innerHTML = "${lotter_name_0}";
        document.getElementById("lottery_text_2").innerHTML = "${lotter_name_1}";
        document.getElementById("lottery_text_3").innerHTML = "${lotter_name_2}";
        document.getElementById("lottery_text_4").innerHTML = "${lotter_name_3}";
        document.getElementById("lottery_text_5").innerHTML = "${lotter_name_4}";
        document.getElementById("lottery_text_6").innerHTML = "${lotter_name_5}";
        document.getElementById("lottery_text_7").innerHTML = "${lotter_name_6}";
        document.getElementById("lottery_text_8").innerHTML = "${lotter_name_7}";
    }

    function back_img_change() {
        //获取事件
        var img1 = document.getElementById("back_img");
        if ("http://www.wxfslp.xyz/mer_picture/back_img_1.png" == img1.src) {
            img1.src = "${pageContext.request.contextPath}/mer_picture/back_img_2.png";
        } else {
            img1.src = "${pageContext.request.contextPath}/mer_picture/back_img_1.png";
        }
    }

    function lottery_img_change() {
        if (run_index <= run_time) {
            if (lotter_img_index > 8) {
                lotter_img_index = 1;
            }
            var img1 = document.getElementById("lottrey_img");
            img1.src = "${pageContext.request.contextPath}/mer_picture/lottery_" + lotter_img_index + ".png";
            lotter_img_index++;
            run_index++;
            // window.setTimeout(lottery_img_change,run[run_index]);
            window.setTimeout(lottery_img_change, 60);
        } else{
            //改变中奖状态
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
                    //分享朋友圈
                    fenxiang(xmlhttp.responseText);
                }
            };
            xmlhttp.open("POST","http://www.wxfslp.xyz/clcService_war_exploded/doLotteryServlet",true);
            // xmlhttp.open("POST","http://172.16.18.134:8080/clcService_war_exploded/doLotteryServlet",true);
            xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
            xmlhttp.send("out_trade_no=${out_trade_no}&lotterValue=${lotterValue}&device_info=${device_info}");
        }
    }

    //点击抽奖按钮
    function Lottery() {
        if(0 == lottery_status){
            lottery_status = 1;

            lotter_value = '${lotterValue}';
            // Toast(lotter_value,2000);
            // alert(lotter_value + "");
            quanshu = '${quanshu}';
            // alert(quanshu + "");
            run_time = '${run_time}';
            // alert(run_time + "");

            run_index = 1;
            lotter_img_index = 1;
            var img1 = document.getElementById("lottrey_img");
            img1.src = "${pageContext.request.contextPath}/mer_picture/lottery_1.png";
            window.setTimeout(lottery_img_change, 0);
        }
    }

    //设置分享朋友圈
    function fenxiang(device_info) {
        // alert(device_info);
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
                var result = JSON.parse(xmlhttp.responseText);
                if(!result == ""){
                    wx.config({
                        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                        appId: result.appId, // 必填，公众号的唯一标识
                        timestamp: result.timestamp, // 必填，生成签名的时间戳
                        nonceStr: result.nonceStr, // 必填，生成签名的随机串
                        signature: result.signature,// 必填，签名
                        jsApiList: ['updateAppMessageShareData','updateTimelineShareData'] // 必填，需要使用的JS接口列表
                    });
                    wx.ready(function(){
                        // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
                        wx.updateTimelineShareData({
                            title: '领奖的title', // 分享标题
                            link: 'http://www.wxfslp.xyz/shape2.jsp?out_trade_no=${out_trade_no}&lotterValue=${lotterValue}&device_info='+device_info, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                            imgUrl: 'http://www.wxfslp.xyz/mer_picture/car.png', // 分享图标
                            success: function () {
                                // 设置成功
                                if(0 == ${lotterValue}){
                                    alert("谢谢惠顾")
                                }else {
                                    alert("抽奖完成 点击右上角选择分享至朋友圈后可去您分享的页面中领奖");
                                }
                            }
                        });
                    });
                }
            }
        };
        xmlhttp.open("POST","http://www.wxfslp.xyz/clcService_war_exploded/ShareServlet2",true);
        // xmlhttp.open("POST","http://172.16.18.134:8080/clcService_war_exploded/ShareServlet",true);
        xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        xmlhttp.send("url="+window.location.href.split('#')[0]);
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
<img id="back_img" src="${pageContext.request.contextPath}/mer_picture/back_img_1.png">
<img id="lottrey_img" src="${pageContext.request.contextPath}/mer_picture/lottery_1.png">
<img id="button_img" onclick="Lottery()" src="${pageContext.request.contextPath}/mer_picture/button_img.png">

<div id="lottery_text_1" style="font-size: 40px;position: absolute;color: red;left: 18%;top: 46%;width:10%;height: 5%;"></div>
<div id="lottery_text_2" style="font-size: 40px;position: absolute;color: red;left: 46%;top: 46%;width:10%;height: 5%;"></div>
<div id="lottery_text_3" style="font-size: 40px;position: absolute;color: red;left: 73%;top: 46%;width:10%;height: 5%;"></div>
<div id="lottery_text_4" style="font-size: 40px;position: absolute;color: red;left: 73%;top: 63%;width:10%;height: 5%;"></div>
<div id="lottery_text_5" style="font-size: 40px;position: absolute;color: red;left: 73%;top: 80%;width:10%;height: 5%;"></div>
<div id="lottery_text_6" style="font-size: 40px;position: absolute;color: red;left: 46%;top: 80%;width:10%;height: 5%;"></div>
<div id="lottery_text_7" style="font-size: 40px;position: absolute;color: red;left: 18%;top: 80%;width:10%;height: 5%;"></div>
<div id="lottery_text_8" style="font-size: 40px;position: absolute;color: red;left: 18%;top: 63%;width:10%;height: 5%;"></div>
<%--<p>${out_trade_no}</p>--%>
<%--<p>${lotterValue}</p>--%>
</body>
</html>
