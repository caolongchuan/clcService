<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2019/11/18
  Time: 16:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>登录页面</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">

    <link rel="stylesheet" type="text/css"
          href="./bootstrap-3.3.7-dist/css/bootstrap.css">
    <script src="./jquery-3.2.1/jquery-3.2.1.min.js"></script>
    <script src="./bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
</head>
<style>
    #re {
        margin-top: 10px;
        float: right;
    }
</style>
<script type="text/javascript">

    function vform(){
        var name = $("#na").val();
        var password = $("#pw").val();
        if(name == ""){
            alert("请输入用户名");
            return false;
        }
        if(password == ""){
            alert("请输入密码");
            return false;
        }
        return true;
    }
</script>
<body>
<div class="container">
    <!--模态框，点击注册按钮弹窗-->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog"
         aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"
                            aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">用户clc注册</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" action="Register" method="post" onsubmit="return vform()">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">帐号</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="un"
                                       name="username" placeholder="请输入帐号">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">密码</label>
                            <div class="col-sm-10">
                                <input type="password" class="form-control" id="pw"
                                       name="password" placeholder="Password">
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="submit" class="btn btn-default">提交</button>
                                <font color="red">${error}</font>

                            </div>
                        </div>

                    </form>

                </div>

            </div>
        </div>
    </div>
    <!-- ============================================================================= -->
    <button class="btn btn-default" id="re"
            data-toggle="modal" data-target="#myModal">注册</button>
    <center>
        <h1>用户登录</h1>
    </center>
    <hr>
    <form class="form-horizontal" action="LoginServlet" method="post">

        <div class="form-group">
            <label class="col-sm-2 control-label">帐号</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="username"
                       name="username" value="${username}" placeholder="请输入帐号">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">密码</label>
            <div class="col-sm-10">
                <input type="password" class="form-control" id="password"
                       name="password" value="${password}" placeholder="Password">
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default">登录</button>
                <font color="red">${error}</font>

            </div>
        </div>
    </form>

</div>
</body>

</html>
