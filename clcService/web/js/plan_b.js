var choice_num = [-1, -1, -1, -1, -1];
var choice_price = [0, 0, 0, 0, 0];

function addDiv(num1, name, gride_status, price, img_param) {
    //首先找到Id为d的元素
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
    var div_add_in = document.createElement('div');
    div_add_in.id = "div_add_in_"+num1;
    if (gride_status == '1' && price != '0') {
        div_add_in.className = 'add_in_yes';
    } else {
        div_add_in.className = 'add_in_no';
    }
    div_add_in.innerHTML = '加入购物车';
    div_add_in.onclick = function () {
        grid_oncli(num1, price, gride_status);
    };

    div_inner.appendChild(img);
    div_inner.appendChild(div_title);
    div_inner.appendChild(div_name);
    div_inner.appendChild(div_price);
    div_inner.appendChild(div_add_in);
    div_card.appendChild(div_inner);
    main.appendChild(div_card);
}

function grid_oncli(num, price, gride_status) {
    if (gride_status == '0') {
        Toast("空的", 1000);
    } else if (price == '0') {
        Toast("未设置物品", 1000);
    } else {
        for (j = 0; j < choice_num.length; j++) {
            if (num == choice_num[j]) {
                Toast("同一个物品只能选择一次", 1000);
                return;
            }
        }
        //将点中的商品加入购物车按钮设置为灰色
        document.getElementById('div_add_in_'+num).className = 'add_in_no';

        for (i = 0; i < choice_num.length; i++) {
            if (choice_num[i] == -1) {
                choice_num[i] = num;
                choice_price[i] = price;
                document.getElementById("choice_" + i).style.visibility = "visible";
                document.getElementById("c_" + i).innerHTML = num;

                var num = choice_price[0] + choice_price[1] + choice_price[2] + choice_price[3] + choice_price[4];
                num = num.toFixed(2); // 输出结果为 小数点后只留两位
                document.getElementById("test_button").innerHTML
                    = '￥' + num;
                return;
            }
        }
        Toast("您只能选择5个", 1000);
    }
}

function initData(jsonString) {
    for (i = 0; i < choice_num.length; i++) {
        // alert("choice_"+i);
        document.getElementById("choice_" + i).style.visibility = "hidden";
        choice_num [i] = -1;
        choice_price[i] = 0;
    }
    document.getElementById("header").innerHTML = "月亮基地";
    var result = JSON.parse(jsonString);
    if ("SUCCESS" == result.status) {
        var data = result.merchandises;
        for (var i = 0; i < data.length; i++) {
            addDiv(i + 1, data[i].name, data[i].gridstatus, data[i].price, data[i].img);
        }
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

function choice_onclick(num) {
    //将点中的商品加入购物车按钮设置为可用
    document.getElementById('div_add_in_'+choice_num[num]).className = 'add_in_yes';

    document.getElementById("choice_" + num).style.visibility = "hidden";
    choice_num[num] = -1;
    choice_price[num] = 0;
    document.getElementById("test_button").innerHTML
        = '￥' + (choice_price[0] + choice_price[1] + choice_price[2] + choice_price[3] + choice_price[4]);

}


function getTotalNum() {
    return choice_price[0] + choice_price[1] + choice_price[2] + choice_price[3] + choice_price[4];
}

//预支付接口 获取用户openid
function Pay(device_info) {
    var total_num = getTotalNum();
    if (total_num > 0) {
        var v = "";
        for (i = 0; i < choice_num.length; i++) {
            if (choice_num[i] >= 0) {
                v = v + "+" + choice_num[i];
            }
        }
        var v1 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxedc7f7cb6de6705e&redirect_uri=http%3A%2F%2Fwww.wxfslp.xyz%2FWXGetOpenID";
        var v2 = "%3Fdevice_info%3D" + device_info;
        var v3 = "%26attach%3D" + v;
        var v4 = "%26total_fee%3D" + total_num;
        var v5 = "&response_type=code&scope=snsapi_base&connect_redirect=1&state=123#wechat_redirect";

        window.location.href = v1 + v2 + v3 + v4 + v5;
    } else {
        Toast("请选择商品");
    }
}



