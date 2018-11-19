/** 定义购物车控制器 */
app.controller('cartController', function($scope, $controller, baseService){

    /** 继承baseController控制器 */
    $controller('baseController', {$scope: $scope});




    /** 查询购物车数据 */
    $scope.findCart = function () {
        baseService.sendGet("/cart/findCart").then(function(response){
            // 获取响应数据
            $scope.carts = response.data;

            // 定义对象封装合计数量、总金额
            $scope.totalEntity = {totalNum : 0, totalMoney : 0};

            // 循环购物车集合
            for (var i = 0; i < response.data.length; i++){
                // 获取一个商家的购物车
                var cart = response.data[i];
                // 循环购物车商品集合
                for (var j = 0; j < cart.orderItems.length; j++){
                    // 获取OrderItem
                    var orderItem = cart.orderItems[j];
                    // 合计数量
                    $scope.totalEntity.totalNum += orderItem.num;
                    // 总金额
                    $scope.totalEntity.totalMoney += orderItem.totalFee;
                }
            }

        });
    };

    /** 添加商品到购物车(加一、减一、删除) */
    $scope.addCart = function(itemId, num){
        baseService.sendGet("/cart/addCart?itemId="
            + itemId + "&num=" + num).then(function(response){
                if (response.data){
                    // 重新查询购物车数据
                    $scope.findCart();
                }else{
                    alert("操作失败！");
                }
        });
    };

});