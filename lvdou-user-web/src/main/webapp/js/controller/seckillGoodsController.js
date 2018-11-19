/** 定义秒杀商品控制器 */
app.controller("seckillGoodsController", function($scope,$controller,
                                                  $location,$timeout,baseService){

    /** 指定继承cartController */
    $controller("baseController", {$scope:$scope});

    /** 查询正在秒杀的商品 */
    $scope.findSeckillGoods = function(){
        // 发送异步请求
        baseService.sendGet("/seckill/findSeckillGoods")
            .then(function (response) {
                // 获取响应数据
                $scope.seckillGoodsList = response.data;
            });
    };

    /** 根据秒杀商品的id获取秒杀商品 */
    $scope.findOne = function(){
        // 获取请求URL后面的参数
        var id = $location.search().id;
        baseService.sendGet("/seckill/findOne?id=" + id).then(function (response) {
            // 获取响应数据
            $scope.entity = response.data;

            // 显示秒杀商品的倒计时
            $scope.downCount(response.data.endTime);

        });
    };

    /** 倒计时方法 */
    $scope.downCount = function(endTime){
        // 计算出相差的毫秒数(结束时间的毫秒数 - 当前时间的毫秒数)
        var millsSeconds = new Date(endTime).getTime() - new Date().getTime();
        // 格式 1天 10:10:10

        // 计算出相差的秒数
        var seconds = Math.floor(millsSeconds / 1000);
        // 判断秒数
        if (seconds > 0){
            // 计算出相差的分钟
            var minutes = Math.floor(seconds / 60);
            // 计算出相差的小时
            var hours = Math.floor(minutes / 60);
            // 计算出相差的天数
            var day = Math.floor(hours / 24);

            //  定义res数组封装最后显示的数据
            var res = new Array();

            if (day > 0){
                res.push(calc(day) + "天 ");
            }
            if (hours > 0){
                res.push(calc(hours - day * 24) + ":");
            }
            if (minutes > 0){
                res.push(calc(minutes - hours * 60) + ":");
            }
            if (seconds > 0){
                res.push(calc(seconds - minutes * 60));
            }
            $scope.timeStr = res.join("");

            // 延迟定时器 setTimeout()
            $timeout(function(){

               $scope.downCount(endTime);
            }, 1000);
        }else{
            $scope.timeStr = "秒杀已结束！";
        }
    };
    var calc = function(num){
        return num > 9 ? num : "0" + num;
    };


    /** 秒杀下单方法 */
    $scope.submitOrder = function(){
        // 判断用户是否登录
        if ($scope.loginName){ // 登录用户

            baseService.sendGet("/seckillorder/submitOrder?id=" + $scope.entity.id)
                .then(function(response){

                    if (response.data){// 秒杀下单成功
                        // 跳转到微信扫码支付页面,当然也可以跳转到确认订单需求页面getOrderInfo.html
                        location.href = "/order/pay.html";
                    }else{
                        alert("下单失败,请重试！");
                    }
                 },function () {
                    alert("提交订单失败！")
                });

        }else{
            // 跳转到CAS认证系统
            location.href = "/login.html?service="+$scope.redirectUrl;
        }
    };

});