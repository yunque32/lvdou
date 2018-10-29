/** 定义控制器层 */
app.controller('indexController', function($scope, baseService){

    /** 获取登录用户名方法 */
    $scope.showLoginName = function(){
        baseService.sendGet("/user/showName").then(function (response) {
            // 获取响应数据

            $scope.loginName = response.data.loginName;
            alert("获取用户名成功");
        },function (response) {
            alert("获取用户名失败！");
        });
    };
});