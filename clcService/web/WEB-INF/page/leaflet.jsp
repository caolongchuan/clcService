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
    <title>宣传页</title>
</head>
<script type="text/javascript">
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
        xmlhttp.send("type=leaflet_1");
    };

</script>
<body>
<img id="main" style="position:absolute;width: 100%;height: auto;" alt="欢迎您的光临">
</body>
</html>
