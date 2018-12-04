/** 定义控制器层 */
app.controller('goodController', function($scope, $controller, baseService){

    /** 指定继承baseController */
    $controller('baseController',{$scope:$scope});

    $scope.findAllGoods = function(){
        // 发送异步请求
        baseService.sendGet("/goods/findAllGoods")
            .then(function (response) {
                // 获取响应数据
                $scope.GoodsList = response.data;
            });
    };
    var map = new Map();
    $scope.jian=function(goods){
        if(parseInt($("#"+goods.id).val())>0){
            $("#"+goods.id).val(parseInt($("#"+goods.id).val())-1);
            $scope.priceRecount(goods,'jian');
         
        }
    };
    $scope.jia=function(goods){
         $("#"+goods.id).val(parseInt($("#"+goods.id).val())+1);
        $scope.priceRecount(goods,'jia');
        map.put("a","b");
        alert(map);
    };
    $scope.totalPrice=0;
    //对总金额四舍五入并保留2位
    $scope.priceRecount=function(goods,suanfa){
        if(suanfa=='jia'){
            $scope.totalPrice =  $scope.totalPrice+parseInt(goods.price) ;
        }else {
            $scope.totalPrice =$scope.totalPrice- parseInt(goods.price);
        }
    };
    $scope.searchEntity = {};
    /** 分页查询 */
    $scope.search = function(page, rows){
        baseService.findByPage("/good/findByPage", page,
            rows, $scope.searchEntity)
            .then(function(response){
                // $scope.dataList = response.data.rows;
                // /** 更新总记录数 */
                // $scope.paginationConf.totalItems = response.data.total;
            });
    };

    /** 定义商品状态数组 */
    $scope.status = ['未审核','已审核','审核未通过','关闭'];


    /** 审核商家商品(修改商品状态) */
    $scope.updateStatus = function(status){
        if ($scope.ids.length > 0) {
            // 发送异步请求
            baseService.sendGet("/good/updateStatus",
                "ids=" + $scope.ids + "&status=" + status)
                .then(function (response) {
                    if (response.data) { // 审核成功
                        // 重新加载数据
                        $scope.reload();
                        // 清空ids数据
                        $scope.ids = [];
                    } else {
                        alert("审核失败！");
                    }
                });
        }else{
            alert("请选择要审核的商品！");
        }
    };

    /** 删除商家商品(修改商品删除状态) */
    $scope.delete = function(){
        if ($scope.ids.length > 0) {
            // 发送异步请求
            baseService.sendGet("/good/delete", "ids=" + $scope.ids)
                .then(function (response) {
                    if (response.data) { // 审核成功
                        // 重新加载数据
                        $scope.reload();
                        // 清空ids数据
                        $scope.ids = [];
                    } else {
                        alert("删除失败！");
                    }
                });
        }else{
            alert("请选择要删除的商品！");
        }
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

    $scope.confirmOrder=function () {
        baseService.sendGet("/goods/confirmOrder")
            .then(function () {
                alert('1');
            },function () {
                alert('2');
            });
    };
    $scope.uploadFile = function(){

        // 调用服务层方法
        uploadService.uploadFile().then(function(response){
            // 判断上传状态 {status : 200, url : ''}
            if (response.data.status == 200){

                // {url : ''}
                $scope.image_entity.url = response.data.url;
            }else{
                alert("上传失败！");
            }
        });
    };

    /** 定义保存商品图片的数据格式 */
    $scope.goods = {goodsDesc : {itemImages : [], specificationItems : []}}


});