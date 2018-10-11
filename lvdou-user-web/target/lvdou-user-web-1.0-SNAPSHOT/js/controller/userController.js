/** 定义控制器层 */
app.controller('userController', function($scope, baseService){

    // 定义user对象
    $scope.user = {};

    /** 注册用户 */
    $scope.save = function(){
        // 先判断密码是否一致
        if ($scope.user.password == $scope.password){
            baseService.sendPost("/user/save?smsCode=" + $scope.smsCode, $scope.user)
                .then(function(response){
                    if (response.data){
                        // 清空数据
                        $scope.user = {};
                        $scope.password = "";
                        $scope.smsCode = "";
                        alert("注册成功！");
                    }else{
                        alert("注册失败！");
                    }
                });
        }else{
            alert("两次密码不一致！");
        }
    };

    // 发送短信验证码
    $scope.sendCode = function(){
        // 判断手机号码
        if ($scope.user.phone && /^1[3|5|7|8]\d{9}$/.test($scope.user.phone)){
            // 发送异步请求
            baseService.sendGet("/user/sendCode?phone=" + $scope.user.phone)
                .then(function(response){
                    if (response.data){
                        alert("发送成功！");
                    }else {
                        alert("发送失败！");
                    }
                });
        }else{
            alert("手机号码格式不正确！");
        }
    };
});