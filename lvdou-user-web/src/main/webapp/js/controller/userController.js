/** 定义控制器层 */
app.controller('userController', function($scope, baseService){


    // 发送短信验证码
    $scope.sendCode = function(){
        // 判断手机号码
        if ($scope.user.phone && /^1[3|5|7|8]\d{9}$/.test($scope.user.phone)){
            // 发送异步请求
            baseService.sendGet("/user/sendCode?phone=" + $scope.user.phone)
                .then(function(response){
                    if (response.data){
                        alert("发送成功！请注意查收");
                    }
                },function () {
                    alert('发送失败！');
                });
        }else{
            alert("手机号码格式不正确wss！");
        }
    };
    //登录

    $scope.checkvcode=function(){
      baseService.sendGet("/user/checkvcode")
          .then(function (response) {
              if(response.data==true){
                  alert('验证码验证成功');
              }
          },function (response) {
              if(response.data=false){
                 alert('验证码验证失败！');
              }
          });
    };
    $scope.vcode="";
    $scope.phone="";
    $scope.login=function(){
        baseService.sendPost("/user/login2",$scope.user,$scope.vcode)
            .then(function (response) {
                alert('最起码有响应！');
                if(response.data=="3"){
                    alert("验证码有误");
                }else if(response.data=="2"){
                    alert("手机号码格式有误")
                }else if(response.data=="4"){
                    alert("账户不存在");
                }else if(response.data="5"){
                    alert('密码错误');
                }else if(response.data="6"){
                    alert('亲，作为日理万鸡的系统，我很忙啦，请稍后再试！');
                }
                location.href = "/index.html";
            },function () {
                alert('亲，作为日理万鸡的系统，我很忙啦，请稍后再试！');
            })
    };

    $scope.login2=function(){
        baseService.sendPost("/user/login2",$scope.user,$scope.vcode)
            .then(function (response) {
                $scope.user=response.data;
                location.href = "/index.html";
            },function () {
                alert('啦啦啦！！！！失败了！')
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
            },function (response) {
                alert("出现异常，请重试！")
            })
    };
    $scope.registerUser=function () {
        baseService.sendPost("/user/registerUser",$scope.user)
            .then(function (response) {
                if(response.data.registerResult=="1"){
                    location.href = "/login.html";
                    alert("注册成功！");
                }else{
                    alert("注册失败！");
                }
            },function (repsonse) {
                alert("连接异常，请重试！");
            })
    }
});