/// <reference path="jquery-1.10.2.min.js" />
$(function () {
    $(".regitem input").blur(function () {
        var id = $(this).attr("id");//获取到了当前鼠标离开的那个文本框
        Check(id);

    });

    //ajax--start
    $("#submit").click(function(){

        var UserName = $("#UserName").val();
        var password = $("#password").val();

        if (UserName == "") {
            alert("用户名不能为空");
            return false;
        }
        else if (password == "") {
            alert("密码不能为空");
            return false;
        }
        }
    //ajax--end

    $(".dregbtn a").click(function () {
        Check("UserName");
        Check("password");
    });

});

function Check(id) {
    var regStr = "";
    var sResult = "";
    var txt = $.trim($("#" + id).val());
    if (txt == "") {
        sResult = "各项不能为空";
        layer.tips(sResult, "#" + id, {
            tips: [2, '#78BA32']
        });
    }
    else {
        switch (id) {
            case "UserName":
                regStr = /[\u4E00-\u9FA5]{2,4}/;
                break;
            case "password":
                regStr = /^[0-9]+$/;
                break;
        }
        if (regStr && regStr.test && !regStr.test(txt)) {
            sResult = "输入的内容格式不对";
            layer.tips(sResult, "#" + id, {
                tips: [2, '#78BA32']
	            });
	        }
	    }
	}
});
