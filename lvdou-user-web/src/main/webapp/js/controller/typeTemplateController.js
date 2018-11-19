/** 定义控制器层 */
app.controller('typeTemplateController', function($scope, $controller, baseService){

    /** 指定继承baseController */
    $controller('baseController',{$scope:$scope});

    /** 定义搜索对象 */
    $scope.searchEntity = {};
    /** 分页查询 */
    $scope.search = function(page, rows){
        baseService.findByPage("/typeTemplate/findByPage", page, 
			rows, $scope.searchEntity)
            .then(function(response){
                $scope.dataList = response.data.rows;
                /** 更新总记录数 */
                $scope.paginationConf.totalItems = response.data.total;
            });
    };

    /** 添加或修改 */
    $scope.saveOrUpdate = function(){
        var url = "save";
        if ($scope.entity.id){
            url = "update";
        }
        /** 发送post请求 */
        baseService.sendPost("/typeTemplate/" + url, $scope.entity)
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
        // 把品牌json数组字符串转化成json数组对象
        $scope.entity.brandIds = JSON.parse($scope.entity.brandIds);
        // 把规格json数组字符串转化成json数组对象
        $scope.entity.specIds = JSON.parse($scope.entity.specIds);
        // 把扩展属性json数组字符串转化成json数组对象
        $scope.entity.customAttributeItems = JSON.parse($scope.entity.customAttributeItems);
    };

    /** 批量删除 */
    $scope.delete = function(){
        if ($scope.ids.length > 0){
            baseService.deleteById("/typeTemplate/delete", $scope.ids)
                .then(function(response){
                    if (response.data){
                        $scope.reload();
                    }else{
                        alert("删除失败！");
                    }
                });
        }
    };



    /** 获取所有的品牌 */
    $scope.findBrandList  = function(){
        // 发送异步请求查询品牌表中的数据
        baseService.sendGet("/brand/findBrandList").then(function(response){
            // 定义select2组件需要的数据 List<Map>
            // [{id : 1, text : '华为'},{id : 2, text : '小米'}]
            $scope.brandList = {data:response.data};
        });
    };

    /** 获取所有的规格 */
    $scope.findSpecList  = function(){
        // 发送异步请求查询规格表中的数据
        baseService.sendGet("/specification/findSpecList").then(function(response){
            // 定义select2组件需要的数据 List<Map>
            // [{id : 1, text : ''},{id : 2, text : ''}]
            $scope.specList = {data:response.data};
        });
    };

    /** 增加一行 */
    $scope.addTableRow = function(){
        // 往数组中添加一个元素
        $scope.entity.customAttributeItems.push({});
    };
    // 删除一行
    $scope.deleteTableRow = function(idx){
        // 从数组中删除一个元素
        $scope.entity.customAttributeItems.splice(idx, 1)
    };

    /** 把品牌与规格选项存入Redis */
    $scope.updateRedis = function(){
        baseService.sendGet("/typeTemplate/updateRedis").then(function(response){
            if (response.data){
                alert("更新缓存成功！");
            }else{
                alert("更新缓存失败！");
            }
        });
    };

});