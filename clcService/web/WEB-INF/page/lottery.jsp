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
    <script src="http://code.jquery.com/jquery-latest.js" type="text/javascript"></script>
    <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.4.0.js"></script>
    <title>抽奖</title>
</head>
<style>
    #back_img {
        position: absolute;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
    }

    #lottery {
        position: absolute;
        left: 10%;
        top: 42.5%;
        width: 80%;
        height: 50%;
    }

</style>

<script type="text/javascript">
    var click = false;

    window.onload = function () {
        window.setInterval(back_img_change, 200);
        //初始化九宫格内各个格格的文字内容
        initLotteryNameText();

        lottery.init('lottery');
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

    //抽奖按钮点击事件
    function doit() {
        if (click) {//click控制一次抽奖过程中不能重复点击抽奖按钮，后面的点击不响应
            return false;
        } else {
            lottery.speed = 100;
            roll();    //转圈过程不响应click事件，会将click置为false
            click = true; //一次抽奖完成后，设置click为true，可继续抽奖
            return false;
        }
    }

    //改变中奖状态
    function changeLotteryStatus() {
        //改变中奖状态
        var xmlhttp;
        if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {// code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                //分享朋友圈
                <%--alert("out_trade_no=${out_trade_no}&lotterValue=${lotterValue}&device_info=${device_info}");--%>
                fenxiang();
            }
        };
        xmlhttp.open("POST", "http://www.wxfslp.xyz/clcService_war_exploded/doLotteryServlet", true);
        // xmlhttp.open("POST","http://172.16.18.134:8080/clcService_war_exploded/doLotteryServlet",true);
        xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xmlhttp.send("out_trade_no=${out_trade_no}&lotterValue=${lotterValue}&device_info=${device_info}");
    }

    //设置分享朋友圈
    function fenxiang() {
        var xmlhttp;
        if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {// code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                var result = JSON.parse(xmlhttp.responseText);
                // alert(xmlhttp.responseText);
                if (!result == "") {
                    wx.config({
                        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                        appId: result.appId, // 必填，公众号的唯一标识
                        timestamp: result.timestamp, // 必填，生成签名的时间戳
                        nonceStr: result.nonceStr, // 必填，生成签名的随机串
                        signature: result.signature,// 必填，签名
                        jsApiList: ['updateAppMessageShareData', 'updateTimelineShareData'] // 必填，需要使用的JS接口列表
                    });
                    wx.ready(function () {
                        // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
                        wx.updateTimelineShareData({
                            title: '${leafletsTwoTitle}', // 分享标题
                            link: 'http://www.wxfslp.xyz/shape2.jsp?out_trade_no=${out_trade_no}&lotterValue=${lotterValue}&device_info=+${device_info}', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                            imgUrl: '${leafletsTwoImgUrl}', // 分享图标
                            success: function () {
                                document.getElementById("wait").style.display="none";
                                // 设置成功
                                if (1 == ${lotterValue}) {
                                    Toast("谢谢惠顾", 1000);
                                } else {
                                    Toast("抽奖完成 点击右上角选择分享至朋友圈后可去您分享的页面中领奖", 50000);
                                }
                            }
                        });
                    });
                }
            }
        };
        xmlhttp.open("POST", "http://www.wxfslp.xyz/clcService_war_exploded/ShareServlet2", true);
        // xmlhttp.open("POST","http://172.16.18.134:8080/clcService_war_exploded/ShareServlet",true);
        xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        // alert("url=" + window.location.href.split('#')[0]);
        xmlhttp.send("url=" + window.location.href.split('#')[0]+"&device_info=${device_info}");
    }


    var lottery = {
        index: -1,    //当前转动到哪个位置，起点位置
        count: 0,    //总共有多少个位置
        timer: 0,    //setTimeout的ID，用clearTimeout清除
        speed: 20,    //初始转动速度
        times: 0,    //转动次数
        cycle: 50,    //转动基本次数：即至少需要转动多少次再进入抽奖环节
        prize: -1,    //中奖位置
        init: function (id) {
            if ($("#" + id).find(".lottery-unit").length > 0) {
                $lottery = $("#" + id);
                $units = $lottery.find(".lottery-unit");
                this.obj = $lottery;
                this.count = $units.length;
                // $lottery.find(".lottery-unit-" + this.index).addClass("active");
                if (0 <= this.index && 8 > this.index) {
                    var img11 = document.getElementById("img_" + this.index);
                    img11.src = "mer_picture/lottery_choiced.png";
                }
            }
        },
        roll: function () {
            var index = this.index;
            var count = this.count;
            var lottery = this.obj;
            // $(lottery).find(".lottery-unit-" + index).removeClass("active");
            if (0 <= this.index && 8 > this.index) {
                var img12 = document.getElementById("img_" + index);
                img12.src = "mer_picture/lottery.png";
            }
            index += 1;
            if (index > count - 1) {
                index = 0;
            }
            ;
            // $(lottery).find(".lottery-unit-" + index).addClass("active");
            if (0 <= this.index && 8 > this.index) {
                var img13 = document.getElementById("img_" + index);
                img13.src = "mer_picture/lottery_choiced.png";
            }
            this.index = index;
            return false;
        },
        stop: function (index) {
            this.prize = index;
            return false;
        }
    };


    function roll() {
        lottery.times += 1;
        lottery.roll();//转动过程调用的是lottery的roll方法，这里是第一次调用初始化
        if (lottery.times > lottery.cycle + 10 && lottery.prize == lottery.index) {
            clearTimeout(lottery.timer);
            lottery.prize = -1;
            lottery.times = 0;
            // click = false;//保证只能点击一次抽奖
            document.getElementById("wait").style.display="inline";
            changeLotteryStatus()//改变中奖状态 并且设置分享朋友圈参数
        } else {
            if (lottery.times < lottery.cycle) {
                lottery.speed -= 10;
            } else if (lottery.times == lottery.cycle) {
                // var index = Math.random()*(lottery.count)|0;
                var index = ${lotterValue}-1;
                lottery.prize = index;
            } else {
                if (lottery.times > lottery.cycle + 10 && ((lottery.prize == 0 && lottery.index == 7) || lottery.prize == lottery.index + 1)) {
                    lottery.speed += 110;
                } else {
                    lottery.speed += 20;
                }
            }
            if (lottery.speed < 40) {
                lottery.speed = 40;
            }
            ;
            //console.log(lottery.times+'^^^^^^'+lottery.speed+'^^^^^^^'+lottery.prize);
            lottery.timer = setTimeout(roll, lottery.speed);//循环调用
        }
        return false;
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

<div id="lottery">
    <table style="width:100%;height: 100%;" border="0" cellpadding="0" cellspacing="0">
        <tr>
            <td class="lottery-unit lottery-unit-0"><img id="img_0"
                                                         style="position: absolute;left: 0%;top: 0%;height: 33.33%;width: 33.33%;"
                                                         src="../../mer_picture/lottery.png">
                <div class="mask"></div>
            </td>
            <td class="lottery-unit lottery-unit-1"><img id="img_1"
                                                         style="position: absolute;left: 33.33%;top: 0%;height: 33.33%;width: 33.33%;"
                                                         src="../../mer_picture/lottery.png">
                <div class="mask"></div>
            </td>
            <td class="lottery-unit lottery-unit-2"><img id="img_2"
                                                         style="position: absolute;left: 66.66%;top: 0%;height: 33.33%;width: 33.33%;"
                                                         src="../../mer_picture/lottery.png">
                <div class="mask"></div>
            </td>
        </tr>
        <tr>
            <td width="33.33%" height="33.33%" class="lottery-unit lottery-unit-7"><img id="img_7"
                                                                                        style="position: absolute;left: 0%;top: 33.33%;height: 33.33%;width: 33.33%;"
                                                                                        src="../../mer_picture/lottery.png">
                <div class="mask"></div>
            </td>
            <td width="33.33%" height="33.33%">
                <img src="../../mer_picture/button_img.png"
                     style="position:absolute;left: 33.33%;top: 33.33%;width: 33.33%;height: 33.33%;" onclick="doit()">
            </td>
            <td width="33.33%" height="33.33%" class="lottery-unit lottery-unit-3"><img id="img_3"
                                                                                        style="position: absolute;left: 66.66%;top: 33.33%;height: 33.33%;width: 33.33%;"
                                                                                        src="../../mer_picture/lottery.png">
                <div class="mask"></div>
            </td>
        </tr>
        <tr>
            <td class="lottery-unit lottery-unit-6"><img id="img_6"
                                                         style="position: absolute;left: 0;top:  66.66%;height: 33.33%;width: 33.33%;"
                                                         src="../../mer_picture/lottery.png">
                <div class="mask"></div>
            </td>
            <td class="lottery-unit lottery-unit-5"><img id="img_5"
                                                         style="position: absolute;left: 33.33%;top:  66.66%;height: 33.33%;width: 33.33%;"
                                                         src="../../mer_picture/lottery.png">
                <div class="mask"></div>
            </td>
            <td class="lottery-unit lottery-unit-4"><img id="img_4"
                                                         style="position: absolute;left: 66.66%;top: 66.66%;height: 33.33%;width: 33.33%;"
                                                         src="../../mer_picture/lottery.png">
                <div class="mask"></div>
            </td>
        </tr>
    </table>
</div>

<div id="lottery_text_1"
     style="font-size: 40px;position: absolute;color: red;left: 18%;top: 46%;width:10%;height: 5%;"></div>
<div id="lottery_text_2"
     style="font-size: 40px;position: absolute;color: red;left: 46%;top: 46%;width:10%;height: 5%;"></div>
<div id="lottery_text_3"
     style="font-size: 40px;position: absolute;color: red;left: 73%;top: 46%;width:10%;height: 5%;"></div>
<div id="lottery_text_4"
     style="font-size: 40px;position: absolute;color: red;left: 73%;top: 63%;width:10%;height: 5%;"></div>
<div id="lottery_text_5"
     style="font-size: 40px;position: absolute;color: red;left: 73%;top: 80%;width:10%;height: 5%;"></div>
<div id="lottery_text_6"
     style="font-size: 40px;position: absolute;color: red;left: 46%;top: 80%;width:10%;height: 5%;"></div>
<div id="lottery_text_7"
     style="font-size: 40px;position: absolute;color: red;left: 18%;top: 80%;width:10%;height: 5%;"></div>
<div id="lottery_text_8"
     style="font-size: 40px;position: absolute;color: red;left: 18%;top: 63%;width:10%;height: 5%;"></div>

<div style="position: absolute;left: 40%;bottom: 20%;width: 20%;height:auto;"><img id="wait" src="mer_picture/wait.gif" style="display: none;width: 100%;height:auto;"></div>


</body>
</html>
