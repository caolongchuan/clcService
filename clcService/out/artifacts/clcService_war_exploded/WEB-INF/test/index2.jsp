<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2019/11/26
  Time: 8:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ch">

<head>
    <meta charset="UTF-8">
    <title>卡片效果</title>
    <link href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/card.css" rel="stylesheet">
</head>

<body>
<div id="main">
    <div class="cardBox">
        <div class="header_div" style="background-color: #4caf50;">
            <p>
                <a title="查看详情" style="cursor: pointer; color:white" onclick="viewXmInfo('${var.OMP_XM_ID}');">项目名称一</a>
            </p>
        </div>
        <div class="bodyBox">
            <p>项目经理：成柯</p>
            <p>项目主管：王江</p>
            <p>项目状态：
                <a href="javascript:void(0)" class="label label-success" style="border-radius: .25em;">启动</a>
            </p>
            <p>异常状态：<span style="color:green">无异常</span></p>
            <p>加工量：1,817,375</p>
        </div>
    </div>

    <div class="cardBox">
        <div class="header_div" style="background-color: #5BC0DE;">
            <p>项目名称二</p>
        </div>
        <div class="bodyBox">
            <p>项目经理：朱焕宏</p>
            <p>项目主管：朱焕宏</p>
            <p>项目状态：
                <a href="javascript:void(0)" class="label label-info" style="border-radius: .25em;">初始化</a>
            </p>
            <p>异常状态：<span style="color:green">无异常</span></p>
            <p>加工量：0</p>
        </div>
    </div>

    <div class="cardBox">
        <div class="header_div" style="background-color: #5BC0DE;">
            <p>项目名称三</p>
        </div>
        <div class="bodyBox">
            <p>项目经理：朱焕宏</p>
            <p>项目主管：朱焕宏</p>
            <p>项目状态：
                <a href="javascript:void(0)" class="label label-info" style="border-radius: .25em;">初始化</a>
            </p>
            <p>异常状态：<span style="color:green">无异常</span></p>
            <p>加工量：0</p>
        </div>
    </div>

    <div class="cardBox">
        <div class="header_div" style="background-color: #5BC0DE;">
            <p>项目名称四</p>
        </div>
        <div class="bodyBox">
            <p>项目经理：朱焕宏</p>
            <p>项目主管：朱焕宏</p>
            <p>项目状态：
                <a href="javascript:void(0)" class="label label-info" style="border-radius: .25em;">初始化</a>
            </p>
            <p>异常状态：<span style="color:green">无异常</span></p>
            <p>加工量：0</p>
        </div>
    </div>

    <div class="cardBox">
        <div class="header_div" style="background-color: #5BC0DE;">
            <p>项目名称四</p>
        </div>
        <div class="bodyBox">
            <p>项目经理：朱焕宏</p>
            <p>项目主管：朱焕宏</p>
            <p>项目状态：
                <a href="javascript:void(0)" class="label label-info" style="border-radius: .25em;">初始化</a>
            </p>
            <p>异常状态：<span style="color:green">无异常</span></p>
            <p>加工量：0</p>
        </div>
    </div>
    <div class="cardBox">
        <div class="header_div" style="background-color: #5BC0DE;">
            <p>项目名称四</p>
        </div>
        <div class="bodyBox">
            <p>项目经理：朱焕宏</p>
            <p>项目主管：朱焕宏</p>
            <p>项目状态：
                <a href="javascript:void(0)" class="label label-info" style="border-radius: .25em;">初始化</a>
            </p>
            <p>异常状态：<span style="color:green">无异常</span></p>
            <p>加工量：0</p>
        </div>
    </div>
    <div class="cardBox">
        <div class="header_div" style="background-color: #5BC0DE;">
            <p>项目名称五</p>
        </div>
        <div class="bodyBox">
            <p>项目经理：朱焕宏</p>
            <p>项目主管：朱焕宏</p>
            <p>项目状态：
                <a href="javascript:void(0)" class="label label-info" style="border-radius: .25em;">初始化</a>
            </p>
            <p>异常状态：<span style="color:green">无异常</span></p>
            <p>加工量：0</p>
        </div>
    </div>
    <div class="cardBox">
        <div class="header_div" style="background-color: #4caf50;">
            <p>
                <a title="查看详情" style="cursor: pointer; color:white" onclick="viewXmInfo('${var.OMP_XM_ID}');">项目名称一</a>
            </p>
        </div>
        <div class="bodyBox">
            <p>项目经理：成柯</p>
            <p>项目主管：王江</p>
            <p>项目状态：
                <a href="javascript:void(0)" class="label label-success" style="border-radius: .25em;">启动</a>
            </p>
            <p>异常状态：<span style="color:green">无异常</span></p>
            <p>加工量：1,817,375</p>
        </div>
    </div>

</div>

</body>

</html>

