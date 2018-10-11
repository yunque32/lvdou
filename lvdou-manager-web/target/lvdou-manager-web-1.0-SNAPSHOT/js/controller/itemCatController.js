/** 定义控制器层 */
app.controller('itemCatController', function($scope, $controller, baseService){

    /** 指定继承baseController */
    $controller('baseController',{$scope:$scope});

    /** 根据父级id查询所有的商品分类 */
    $scope.findItemCatByParentId = function(parentId){
        // 发送异步请求
        baseService.sendGet("/itemCat/findItemCatByParentId",
            "parentId=" + parentId)
            .then(function(response){
                // 获取响应数据
                $scope.dataList = response.data;
        });
    };


    // 定义级别的变量
    $scope.grade = 1;
    // 查询下级的点击事件
    $scope.selectList = function(entity, grade){

        // 重新设置级别变量的值
        $scope.grade = grade;

        if (grade == 1){
            $scope.itemCat_1 = null;
            $scope.itemCat_2 = null;
        }
        if (grade == 2) {
            $scope.itemCat_1 = entity; // 二级
            $scope.itemCat_2 = null; // 三级
        }
        if (grade == 3) {
            $scope.itemCat_2 = entity; // 三级
        }

        // 查询分类
        $scope.findItemCatByParentId(entity.id);
    };




    /** 添加或修改 */
    $scope.saveOrUpdate = function(){
        var url = "save";
        if ($scope.entity.id){
            url = "update";
        }
        /** 发送post请求 */
        baseService.sendPost("/itemCat/" + url, $scope.entity)
            .then(function(response){
                if (response.data){
                    /** 重新加载数据 */
                    $scope.reload();
                }else{
                    alert("操作失败！");
                }
            });
    };

    /** 显示修改 */
    $scope.show = function(entity){
        $scope.entity = JSON.parse(JSON.stringify(entity));
    };

    /** 批量删除 */
    $scope.delete = function(){
        if ($scope.ids.length > 0){
            baseService.deleteById("/itemCat/delete", $scope.ids)
                .then(function(response){
                    if (response.data){
                        $scope.reload();
                    }else{
                        alert("删除失败！");
                    }
                });
        }
    };

    /** 把商品分类存入Redis */
    $scope.updateRedis = function(){
        baseService.sendGet("/itemCat/updateRedis").then(function(response){
            if (response.data){
                alert("更新缓存成功！");
            }else{
                alert("更新缓存失败！");
            }
        });
    };

});