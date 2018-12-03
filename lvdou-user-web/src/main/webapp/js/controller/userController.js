/** 定义控制器层 */
app.controller('userController', function($scope, baseService){

    // 发送短信验证码
    $scope.sendCode = function(){
        // 判断手机号码
        if ($scope.user.phone && /^1[3|5|7|8]\d{9}$/.test($scope.user.phone)){
            // 发送异步请求
            baseService.sendGet("/user/sendCode?phone=" + $scope.user.phone)
                .then(function(response){
                    if (response.data==true){
                        alert("发送成功！请注意查收");
                    }
                },function () {
                    alert('发送失败！');
                });
        }else{
            alert("手机号码格式不正确！");
        }
    };
    //登录
    $scope.login=function(){
        baseService.sendPost("/user/login2?vcode="+$scope.vcode,$scope.user)
            .then(function (response) {
                if(response.data.user){
                    $scope.user=response.data.user;
                    location.href = "/index.html";
                    alert("登录成功！");
                }else {
                    alert(response.data.msg);
                }
            },function () {
                alert('亲，作为日理万鸡的系统，我很忙啦，请稍后再试！');
            })
    };


    $scope.checkUserName=function () {
        baseService.sendGet("/user/checkUserName?userName="+$scope.user.username)
            .then(function (response) {
                if(response.data.checkResult=="1"){
                    alert("用户名可以使用！")
                }else {
                    alert("用户名不可以使用！")
                }
            },function () {
                alert("出现异常，请重试！")
            })
    };
    $scope.saveUser=function () {
        if($scope.address2==null||$scope.address2==""){
            baseService.sendPost("/user/saveUser?vcode="+$scope.vcode+"&address="+
                $scope.address1,$scope.user)
                .then(function (response) {
                    if(response.data.user){

                        alert("注册成功！");
                        location.href = "/index.html";
                    }else {
                        alert(response.data.msg);
                    }
                },function () {
                    alert("连接异常，请重试！");
                });
        }else{
            alert('请先不要输入第二个地址！')
        }

    }
});