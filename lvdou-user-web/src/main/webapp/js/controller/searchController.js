/** 定义搜索控制器 */
app.controller("searchController" ,function ($scope, $location,
                                             $controller, baseService) {
    /** 指定继承baseController */
    $controller("baseController", {$scope:$scope});

    /** 定义搜索参数对象 */
    $scope.searchParam = {keywords : '', category : '',  brand : '',
        spec : {}, price : '', page : 1, rows : 10, sortField : '', sort : ''};

    // 定义搜索方法
    $scope.search = function(){
        // 发送异步请求
        baseService.sendPost("/Search", $scope.searchParam)
            .then(function(response){
                // 获取响应数据(搜索的结果) {"rows" : [{},{}], "categoryList":["",""]}
                $scope.resultMap = response.data;

                // 初始化页码
                $scope.initPageNum();
            });
    };

    /** 添加搜索选项方法 */
    $scope.addSearchItem = function(key, value){
        // 判断是否为商品分类、品牌
        if (key == "category" || key == "brand" || key == 'price'){
            $scope.searchParam[key] = value;
        }else{ // 规格选项
            $scope.searchParam.spec[key] = value;
        }
        /** 执行搜索 */
        $scope.search();
    };

    /** 删除搜索选项方法 */
    $scope.removeSearchItem = function(key){
        // 判断是否为商品分类、品牌
        if (key == "category" || key == "brand" || key == 'price'){
            $scope.searchParam[key] = '';
        }else{ // 规格选项 {key : value, key : value}
           delete $scope.searchParam.spec[key];
        }
        /** 执行搜索 */
        $scope.search();
    };

    /** 定义初始化页码的方法 */
    $scope.initPageNum = function() {

        // 页码数组
        $scope.pageNums = [];
        // 获取总页数
        var totalPages = $scope.resultMap.totalPages;
        // 开始页码
        var firstPage = 1;
        // 结束页码
        var lastPage = totalPages;

        // 前面加点
        $scope.firstDot = true;
        // 后面加点
        $scope.lastDot = true;

        // 判断总页数是否大于5
        if (totalPages > 5) {
            // 当前页码靠前面近些
            if ($scope.searchParam.page <= 3){
                $scope.firstDot = false; // 前面不加点
                lastPage = 5; // 结束页码
            }else if($scope.searchParam.page >= totalPages - 3){ // 当前页码靠后面近些
                firstPage = totalPages - 4; // 开始页码 100
                $scope.lastDot = false; // 后面不加点
            }else{ // 当前页码在中间位置 50
                firstPage = $scope.searchParam.page - 2; // 开始页码
                lastPage = $scope.searchParam.page + 2; // 结束页码
            }
        }else{
            // 前面不加点
            $scope.firstDot = false;
            // 后面不加点
            $scope.lastDot = false;
        }

        // 循环产生页码
        for (var i = firstPage; i <= lastPage; i++){
            $scope.pageNums.push(i);
        }
    };

    // 根据页码搜索
    $scope.pageSearch = function(page){
        page = parseInt(page);
        // 对页码做验证
        if (page >= 1 && page <= $scope.resultMap.totalPages
            && page != $scope.searchParam.page){
            $scope.searchParam.page = page;
            /** 执行搜索 */
            $scope.search();
        }
        if (page > $scope.resultMap.totalPages){
            $scope.searchParam.page = $scope.resultMap.totalPages;
            $scope.jumpNum = $scope.resultMap.totalPages;
            /** 执行搜索 */
            $scope.search();
        }
    };

    // 排序方法
    $scope.sortSearch = function(sortField, sort){
        $scope.searchParam.sortField = sortField;
        $scope.searchParam.sort = sort;
        /** 执行搜索 */
        $scope.search();
    };

    // 判断关键字是不是品牌，如果是品牌就不显示品牌列表
    $scope.keywordsIsBrand = function(){
        // [{"id":1,"text":"联想"},{"id":3,"text":"三星"},{"id":2,"text":"华为"},{"id":5,"text":"OPPO"},
        // {"id":4,"text":"小米"},{"id":9,"text":"苹果"},{"id":8,"text":"魅族"},
        // {"id":6,"text":"360"},{"id":10,"text":"VIVO"},{"id":11,"text":"诺基亚"},{"id":12,"text":"锤子"}]
        for (var i = 0; i < $scope.resultMap.brandList.length; i++){
            var  brand = $scope.resultMap.brandList[i];
            if ($scope.searchParam.keywords == brand.text){
                return true;
            }
        }
        return false;
    };

    // 获取首页传入的检索关键字
    $scope.getkeywords = function(){
        $scope.searchParam.keywords = $location.search().keywords;
        /** 执行搜索 */
        $scope.search();

    };


});
