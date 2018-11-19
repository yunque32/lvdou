/// <reference path="jquery-1.10.2.min.js" />
$(function () {
    $(".regitem input").blur(function () {
        var id = $(this).attr("id");//获取到了当前鼠标离开的那个文本框
        Check(id);

    });

    //ajax--start
    $("#submit").click(function(){

        var UserName = $("#UserName").val();
        var address = $("#address").val();
        var txtPhone = $("#txtPhone").val();
        var validate = $("#validate").val();

        if (UserName == "") {
            alert("用户名不能为空");
            return false;
        }
        else if (address == "") {
            alert("行业不能为空");
            return false;
        }
        else if(txtPhone == "") {
            alert("联系电话不能为空");
            return false;
        }
        else if(validate == "") {
            alert("验证码不能为空");
            return false;
        }
        else {
            $.ajax({
                type: "post",
                url: "user/add.action",
                data:$('#userdata').serialize(),
                dataType:'json',
                success: function (result) {
                    alert(result.message);
                    window.location.href="success.html";
                },
                error: function () { alert("用户名密码验证失败") }
            });


        }
    //ajax--end

    $(".dregbtn a").click(function () {
        Check("txtPhone");
        Check("address");
        Check("UserName");
        Check("validate");
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
            case "address":
                regStr = /[\u4E00-\u9FA5]{2,4}/;
                break;
            case "txtPhone":
                regStr = /0?(13|14|15|18)[0-9]{9}/;
                break;
            case "validate":
                regStr = /[0-9]{6}/;
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
})
