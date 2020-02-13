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
    <script src="http://code.jquery.com/jquery-latest.js" type="text/javascript"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/lottery.css" type="text/css">
    <title>测试</title>
</head>

<style>
    #lottery{width:570px;height:510px;margin:0px auto;border:4px solid #ba1809;}
    #lottery table{background-color:yellow;}
    #lottery table td{position:relative;width:190px;height:170px;text-align:center;color:#333;font-index:-999}
    #lottery table td img{display:block;width:190px;height:170px;}
    #lottery table td a{width:190px;height:170px;display:block;text-decoration:none;background:url(mer_picture/9.jpg) no-repeat top center;}
    #lottery table td a:hover{background-image:url(mer_picture/10.jpg);}
    #lottery table td.active .mask{display:block;}
    .mask {
        width: 100%;
        height: 100%;
        position: absolute;
        left: 0;
        top: 0;
        background-color: rgba(252,211,4,0.5);
        display: none
    }
</style>
<script type="text/javascript">
    var lottery={
        index:-1,    //当前转动到哪个位置，起点位置
        count:0,    //总共有多少个位置
        timer:0,    //setTimeout的ID，用clearTimeout清除
        speed:20,    //初始转动速度
        times:0,    //转动次数
        cycle:50,    //转动基本次数：即至少需要转动多少次再进入抽奖环节
        prize:-1,    //中奖位置
        init:function(id){
            if ($("#"+id).find(".lottery-unit").length>0) {
                $lottery = $("#"+id);
                $units = $lottery.find(".lottery-unit");
                this.obj = $lottery;
                this.count = $units.length;
                $lottery.find(".lottery-unit-"+this.index).addClass("active");
            };
        },
        roll:function(){
            var index = this.index;
            var count = this.count;
            var lottery = this.obj;
            $(lottery).find(".lottery-unit-"+index).removeClass("active");
            index += 1;
            if (index>count-1) {
                index = 0;
            };
            $(lottery).find(".lottery-unit-"+index).addClass("active");
            this.index=index;
            return false;
        },
        stop:function(index){
            this.prize=index;
            return false;
        }
    };

    function roll(){
        lottery.times += 1;
        lottery.roll();//转动过程调用的是lottery的roll方法，这里是第一次调用初始化
        if (lottery.times > lottery.cycle+10 && lottery.prize==lottery.index) {
            clearTimeout(lottery.timer);
            lottery.prize=-1;
            lottery.times=0;
            click=false;
        }else{
            if (lottery.times<lottery.cycle) {
                lottery.speed -= 10;
            }else if(lottery.times==lottery.cycle) {
                // var index = Math.random()*(lottery.count)|0;
                var index = 1;
                lottery.prize = index;
            }else{
                if (lottery.times > lottery.cycle+10 && ((lottery.prize==0 && lottery.index==7) || lottery.prize==lottery.index+1)) {
                    lottery.speed += 110;
                }else{
                    lottery.speed += 20;
                }
            }
            if (lottery.speed<40) {
                lottery.speed=40;
            };
            //console.log(lottery.times+'^^^^^^'+lottery.speed+'^^^^^^^'+lottery.prize);
            lottery.timer = setTimeout(roll,lottery.speed);//循环调用
        }
        return false;
    }

    var click=false;

    window.onload=function(){
        lottery.init('lottery');
        $("#lottery a").click(function(){
            if (click) {//click控制一次抽奖过程中不能重复点击抽奖按钮，后面的点击不响应
                return false;
            }else{

                lottery.speed=100;
                roll();    //转圈过程不响应click事件，会将click置为false
                click=true; //一次抽奖完成后，设置click为true，可继续抽奖
                return false;
            }
        });
    };
</script>

<body class="keBody">

<!--效果html开始-->
<div id="lottery">
    <table border="0" cellpadding="0" cellspacing="0">
        <tr>
            <td class="lottery-unit lottery-unit-0"><img src="mer_picture/leaflet_1.jpg"><div class="mask"></div></td>
            <td class="lottery-unit lottery-unit-1"><img src="mer_picture/leaflet_2.jpg"><div class="mask"></div></td>
            <td class="lottery-unit lottery-unit-2"><img src="mer_picture/LingJiang.jpg"><div class="mask"></div></td>
        </tr>
        <tr>
            <td class="lottery-unit lottery-unit-7"><img src="mer_picture/8.jpg"><div class="mask"></div></td>
            <td><a href="#"></a></td>
            <td class="lottery-unit lottery-unit-3"><img src="mer_picture/4.jpg"><div class="mask"></div></td>
        </tr>
        <tr>
            <td class="lottery-unit lottery-unit-6"><img src="mer_picture/7.jpg"><div class="mask"></div></td>
            <td class="lottery-unit lottery-unit-5"><img src="mer_picture/6.jpg"><div class="mask"></div></td>
            <td class="lottery-unit lottery-unit-4"><img src="mer_picture/5.jpg"><div class="mask"></div></td>
        </tr>
    </table>
</div>

</body>
</html>