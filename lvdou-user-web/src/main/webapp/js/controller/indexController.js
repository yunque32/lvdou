/** 定义控制器层 */
app.controller('indexController', function($scope, baseService){

    /** 获取登录用户名方法 */
    $scope.showLoginName = function(){
        baseService.sendGet("/user/showLoginName").then(function (response) {
            // 获取响应数据

            $scope.loginName = response.data.loginName;
        });
    };

    $scope.selectBXG=function () {
        baseService.sendGet("/seckill/findSeckillGoods")
            .then(function (response) {
                // 获取响应数据
                $scope.GoodsList = response.data;
            });
    }
});