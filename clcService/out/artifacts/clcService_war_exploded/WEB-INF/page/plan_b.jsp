<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2019/11/28
  Time: 10:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>商品列表</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/plan_b.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/plan_b.css" type="text/css">
    <script src="http://pv.sohu.com/cityjson?ie=utf-8"></script>
</head>
<script type="text/javascript">
    window.onload = function () {
        initData('${jsonString}');
    }

</script>

<body>

<div id="main">
    <div id="header"></div>
</div>
<div style="position: absolute;color: #ba1809;left: 0;top: 88%;font-size: 30px;">提示:点击下面的购物车小图标可取消选择指定的商品</div>

<div id="footer">
    <div id="choice">
        <div id="choice_0" onclick="choice_onclick(0)">
            <div id="c_0" class="text_in_car">88</div>
            <img class="img_in_car" src="mer_picture/car.png" alt="logo">
        </div>
        <div id="choice_1" onclick="choice_onclick(1)">
            <div id="c_1" class="text_in_car">88</div>
            <img class="img_in_car" src="mer_picture/car.png" alt="logo"/>
        </div>
        <div id="choice_2" onclick="choice_onclick(2)">
            <div id="c_2" class="text_in_car">88</div>
            <img class="img_in_car" src="mer_picture/car.png" alt="logo"/>
        </div>
        <div id="choice_3" onclick="choice_onclick(3)">
            <div id="c_3" class="text_in_car">88</div>
            <img class="img_in_car" src="mer_picture/car.png" alt="logo"/>
        </div>
        <div id="choice_4" onclick="choice_onclick(4)">
            <div id="c_4" class="text_in_car">88</div>
            <img class="img_in_car" src="mer_picture/car.png" alt="logo"/>
        </div>
    </div>
    <button id="test_button" onclick="Pay('${device_info}')">支付</button>
</div>

</body>
</html>