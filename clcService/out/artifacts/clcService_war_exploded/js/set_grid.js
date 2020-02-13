var device_info;

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


function addDiv(num1, name, gride_status, price, img_param) {
    var main = document.getElementById('main');
    var div_card = document.createElement('div');
    div_card.className = 'cardBox';
    var div_inner = document.createElement('div');
    div_inner.className = 'cardBox_inner';
    var img = document.createElement('img');
    img.className = 'img';
    if (price == '0') {
        //未设置商品
        img.src = 'mer_picture/no_mer.png';
    } else if (img_param == "") {
        //商品没有设置图片
        img.src = 'mer_picture/no_pic.png';
    } else {
        img.src = img_param;
    }

    var div_title = document.createElement('div');
    div_title.className = 'title';
    div_title.innerHTML = num1;
    var div_name = document.createElement('div');
    div_name.className = 'name';
    div_name.innerHTML = name;
    var div_price = document.createElement('div');
    div_price.className = 'price';
    div_price.innerHTML = '￥' + price;

    var div_button_add = document.createElement('div');
    div_button_add.innerHTML = "补充";
    div_button_add.id = "div_button_add_id_" + num1;
    div_button_add.onclick = function () {
        button_add(num1);
    };
    if(gride_status == '0'&&price!='0'){
        div_button_add.className = 'button_add';
    }else{
        div_button_add.className = 'button_add_no';
    }

    var div_button_change = document.createElement('div');
    div_button_change.className= 'button_change';
    div_button_change.innerHTML = "修改";
    div_button_change.onclick = function () {
        button_change(num1);
    };
    var div_button_open = document.createElement('div');
    div_button_open.className = 'button_open';
    div_button_open.innerHTML = "开开";
    div_button_open.onclick = function () {
        button_open(num1);
    };

    div_inner.appendChild(img);
    div_inner.appendChild(div_title);
    div_inner.appendChild(div_name);
    div_inner.appendChild(div_price);
    div_inner.appendChild(div_button_add);
    div_inner.appendChild(div_button_change);
    div_inner.appendChild(div_button_open);
    div_card.appendChild(div_inner);
    main.appendChild(div_card);
}

function initData(deviceinfo,jsonString){
    device_info = deviceinfo;
    document.getElementById("header").innerHTML="设置";
    var result = JSON.parse(jsonString);
    if ("SUCCESS" == result.status) {
        var data = result.merchandises;
        for (var i = 0; i < data.length; i++) {
            addDiv(i + 1, data[i].name, data[i].gridstatus, data[i].price, data[i].img);
        }
    }
}

/**
 * 补充按钮
 * @param no
 */
function button_add(no) {
    var className = document.getElementById("div_button_add_id_"+no).className;
    if(className == "button_add"){
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
                Toast(xmlhttp.responseText,1000);
                //补充成功 改变按钮状态
                document.getElementById("div_button_add_id_"+no).className = "button_add_no";
            }
        };
        xmlhttp.open("POST","http://www.wxfslp.xyz/clcService_war_exploded/SetGridServlet",true);
        xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        xmlhttp.send("device_info="+device_info+"&no="+no+"&action=close");
    }
}

/**
 * 修改按钮
 */
function button_change(no) {
    var dialog_change_mer = document.createElement('div');
    dialog_change_mer.id = 'dialog_change_mer';

    var p_change_mer = document.createElement("p");
    p_change_mer.id = 'p_change_mer';
    p_change_mer.innerHTML = "请输入商品编号！！！";

    var input_change_mer = document.createElement('input');
    input_change_mer.id = 'input_change_mer';

    var div_change_mer_ok = document.createElement('div');
    div_change_mer_ok.id = 'div_change_mer_ok';
    div_change_mer_ok.innerHTML = "确定";
    div_change_mer_ok.onclick = function () {
        _change_mer_ok(no);
    };

    var div_change_mer_cancle = document.createElement('div');
    div_change_mer_cancle.id = 'div_change_mer_cancle';
    div_change_mer_cancle.innerHTML = '取消';
    div_change_mer_cancle.onclick = function () {
        _change_mer_cancle();
    };

    dialog_change_mer.appendChild(p_change_mer);
    dialog_change_mer.appendChild(input_change_mer);
    dialog_change_mer.appendChild(div_change_mer_ok);
    dialog_change_mer.appendChild(div_change_mer_cancle);

    document.body.appendChild(dialog_change_mer);
}

/**
 * 开开按钮
 * @param no
 */
function button_open(no) {
    var div_open_confirm = document.createElement('div');
    div_open_confirm.id = 'dialog_open_confirm';

    var p_open_confirm = document.createElement("p");
    p_open_confirm.id = 'p_open_confirm';
    p_open_confirm.innerHTML = "您确定要打开吗???"

    var div_open_confirm_ok = document.createElement('div');
    div_open_confirm_ok.id = 'div_open_confirm_ok';
    div_open_confirm_ok.innerHTML = "确定";
    div_open_confirm_ok.onclick = function () {
        _open_confirm_ok(no);
    };

    var div_open_confirm_cancle = document.createElement('div');
    div_open_confirm_cancle.id = 'div_open_confirm_cancle';
    div_open_confirm_cancle.innerHTML = '取消';
    div_open_confirm_cancle.onclick = function () {
        _open_confirm_cancle();
    };

    div_open_confirm.appendChild(p_open_confirm);
    div_open_confirm.appendChild(div_open_confirm_ok);
    div_open_confirm.appendChild(div_open_confirm_cancle);

    document.body.appendChild(div_open_confirm);
}
//取消打开
function _open_confirm_cancle() {
    document.body.removeChild(dialog_open_confirm);
}
//确认打开
function _open_confirm_ok(no) {
    document.body.removeChild(dialog_open_confirm);
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
            Toast(xmlhttp.responseText,1000);
            //打开成功 改变补充按钮状态
            document.getElementById("div_button_add_id_"+no).className = "button_add";
        }
    };
    xmlhttp.open("POST","http://www.wxfslp.xyz/clcService_war_exploded/SetGridServlet",true);
    // xmlhttp.open("POST","http://172.16.18.134:8080/clcService_war_exploded/SetGridServlet",true);
    xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xmlhttp.send("device_info="+device_info+"&no="+no+"&action=open");
}

//确认修改商品按钮
function _change_mer_ok(no) {
    var mer_no = document.getElementById("input_change_mer").value;
    if(0<mer_no&&500>mer_no){
        document.body.removeChild(dialog_change_mer);
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
                Toast(xmlhttp.responseText,1000);
                //修改成功
                location.reload();
            }
        }
        xmlhttp.open("POST","http://www.wxfslp.xyz/clcService_war_exploded/SetGridServlet",true);
        xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        xmlhttp.send("device_info="+device_info+"&no="+no+"&action=change"+"&mer_no="+mer_no);
    }else{
        alert("请输入正确的数值！！！");
    }
}
//取消修改商品按钮
function _change_mer_cancle() {
    document.body.removeChild(dialog_change_mer);
}
