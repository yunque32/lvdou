/** 定义控制器层 */
app.controller('goodController', function($scope, $controller, baseService){

    /** 指定继承baseController */
    $controller('baseController',{$scope:$scope});

    /** 定义搜索对象 */
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



});