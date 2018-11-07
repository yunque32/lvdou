/** 定义后台首页控制器 */
app.controller('indexController', function($scope, baseService){

    // 定义获取登录用户名的方法
    $scope.showLoginName = function () {
        // 发送异步请求
        baseService.sendGet("/user/loginName").then(function(response){
            // 获取登录用户名
            alert("获取成功！")
            $scope.loginName = response.data.loginName;
        },function (response) {
            alert("获取用户名失败！")
        });
    };
});