 /** 定义订单控制器 */
app.controller("orderController", function ($scope,$controller,$location,$interval,baseService) {
    /** 继承cartController */
    $controller("cartController", {$scope:$scope});

    /** 获取收件人地址列表 */
    $scope.findUserAddress = function(){
        // 发送请求
        baseService.sendGet("/order/findUserAddress")
            .then(function(response){
                // 获取响应数据
                $scope.addressList = response.data;
                // 循环全部地址，找出默认地址
                for (var i in response.data){
                    var addr = response.data[i];
                    if (addr.isDefault == 1){
                        $scope.address = addr;
                    }
                }
            })
    };

    /** 用户选择地址 */
    $scope.selectAddress = function(item){
        $scope.address = item;
    };

    /** 判断是否为用户选择的地址 */
    $scope.isSelectedAddress = function (item) {
        return $scope.address == item;
    };

    /** 定义订单对象 */
    $scope.order = {paymentType : '1'};

    /** 用户选择支付方式 */
    $scope.selectPayType = function (payType) {
        $scope.order.paymentType = payType;
    };

    /** 提交订单 */
    $scope.saveOrder = function(){
        // 设置收件人地址
        $scope.order.receiverAreaName = $scope.address.address;
        // 设置收件人手机号码
        $scope.order.receiverMobile = $scope.address.mobile;
        // 设置收件人
        $scope.order.receiver = $scope.address.contact;

        baseService.sendPost("/order/save", $scope.order)
            .then(function(response){
                if (response.data){
                    // 如果是微信支付，跳转到扫码支付页面
                    if ($scope.order.paymentType == '1'){
                        location.href = "/order/pay.html";
                    }else{
                        // 如果是货到付款，跳转到成功页面
                        location.href = "/order/paysuccess.html";
                    }
                }else{
                    alert("订单提交失败！");
                }
        });
    };



    /** 生成微信支付二维码 */
    $scope.genPayCode = function(){

        // 发送异步请求得到支付URL
        baseService.sendGet("/order/genPayCode").then(function(response){
            /** 获取金额(将分转化成元) {totalFee : 1000, outTradeNo: '', codeUrl : ''} */
            $scope.money = (response.data.totalFee / 100).toFixed(2);
            /** 获取订单交易号 */
            $scope.outTradeNo= response.data.outTradeNo;
            // 生成二维码
            var qr = new QRious({
                element : document.getElementById("qrious"),
                size : 250,
                level : 'H',
                value : response.data.codeUrl
            });

            /**
             * 开启定时器
             * 第一个参数：调用的函数
             * 第二个参数：时间毫秒数(3000毫秒也就是3秒)
             * 第三个参数：调用的总次数(60次) 180秒
             * */
            var timer = $interval(function(){
                /** 发送请求，查询支付状态 */
                baseService.sendGet("/order/queryPayStatus?outTradeNo="
                    + $scope.outTradeNo)
                    .then(function(response){
                        if(response.data.status == 1){// 支付成功
                            /** 取消定时器 */
                            $interval.cancel(timer);
                            location.href = "/order/paysuccess.html?money="
                                + $scope.money;

                        }
                        if(response.data.status == 3){// 支付失败
                            /** 取消定时器 */
                            $interval.cancel(timer);
                            location.href = "/order/payfail.html";
                        }
                    });
            }, 3000, 60);

            /** 执行60次(3分钟)之后需要回调的函数 */
            timer.then(function(){

                /** 重新生成微信支付二维码 */
                $scope.genPayCode();
                alert("微信支付二维码失效！已重新生成二维码，请重新扫描支付！");
            });


        },function () {
            alert("生成二维码失败！");
        });
    };

    $scope.afterPaySuccess=function(){
        alert("第一步成功！");
        baseService.sendGet("/order/afterPaySuccess")
            .then(function (response) {
                alert("成功了"+response);
            },function (response) {
                alert("失败了！"+response);
            });
    };
    /** 获取总金额 */
    $scope.getMoney = function () {
        return $location.search().money;
    };
    
});