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
    <title>设置</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/set_grid.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/set_grid.css" type="text/css">
    <script src="http://pv.sohu.com/cityjson?ie=utf-8"></script>
</head>
<script type="text/javascript">
    var _h = 0;
    function SetH(o) {
        _h = o.scrollTop;
        SetCookie("a", _h)
    }
    function SetCookie(sName, sValue) {
        document.cookie = sName + "=" + escape(sValue) + "; ";
    }
    function GetCookie(sName) {
        var aCookie = document.cookie.split("; ");
        for (var i = 0; i < aCookie.length; i++) {
            var aCrumb = aCookie[i].split("=");
            if (sName == aCrumb[0])
                return unescape(aCrumb[1]);
        }
        return 0;
    }
    window.onload = function () {
        initData('${device_info}', '${jsonString}');
        document.getElementById("main").scrollTop = GetCookie("a");//页面加载时设置scrolltop高度
    }
</script>

<div id="main" onscroll="SetH(this)">
    <div id="header"></div>
</div>

</body>
</html>
