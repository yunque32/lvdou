/** 定义控制器层 */
app.controller('goodsController', function($scope, $controller,$location, baseService, uploadService){

    /** 指定继承baseController */
    $controller('baseController',{$scope:$scope});

    /** 添加或修改 */
    $scope.saveOrUpdate = function(){

        var url = "save"; // 添加
        if ($scope.goods.id){
            url = "update"; // 修改
        }

        // 获取富文本编辑器中的内容
        $scope.goods.goodsDesc.introduction = editor.html();
        /** 发送post请求 */
        baseService.sendPost("/goods/" + url, $scope.goods)
            .then(function(response){
                if (response.data){
                    alert("操作成功！");
                    /** 清空数据 */
                    $scope.goods = {};
                    /** 清空富文本编辑器中的内容 */
                    editor.html("");

                    /** 跳转到商品列表页 */
                    location.href = "/admin/goods.html";
                }else{
                    alert("操作失败！");
                }
            });
    };

    /** 上传文件方法 */
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

    /** 把商品的图片添加到图片数组 */
    $scope.add_image_entity = function(){
        $scope.goods.goodsDesc.itemImages.push($scope.image_entity);
    }

    /** 从图片数组中移删数组中一个元素  */
    $scope.remove_image_entity = function(idx){
        $scope.goods.goodsDesc.itemImages.splice(idx, 1);
    };


    // 根据父级id查询商品分类
    $scope.findItemCatByParentId = function(parentId, name){
        // 发送异步请求
        baseService.sendGet("/itemCat/findItemCatByParentId",
            "parentId=" + parentId).then(function(response){

                // 第一个下拉列表中的数据
                $scope[name] = response.data;
        });
    };

    // $watch()：用于监听goods.category1Id变量是否发生改变，如果发生改变时，就会调用后面的函数
    $scope.$watch('goods.category1Id', function(newValue, oldValue){

        if (newValue){ // 判断是否为undefined
            // 根据用户选择的一级分类查询二级分类
            $scope.findItemCatByParentId(newValue, "itemCatList2");
        }else{
            $scope.itemCatList2 = [];
        }
    });

    // $watch()：用于监听goods.category2Id变量是否发生改变，如果发生改变时，就会调用后面的函数
    $scope.$watch('goods.category2Id', function(newValue, oldValue){

        if (newValue){ // 判断是否为undefined
            // 根据用户选择的二级分类查询三级分类
            $scope.findItemCatByParentId(newValue, "itemCatList3");
        }else{
            $scope.itemCatList3 = [];
        }

    });

    // $watch()：用于监听goods.category3Id变量是否发生改变，如果发生改变时，就会调用后面的函数
    $scope.$watch('goods.category3Id', function(newValue, oldValue){

        if (!newValue){ // 判断是否为undefined，如果返回
            return;
        }
        // 循环三级分类数组 List<ItemCat> : [{},{}]
        for (var i = 0; i < $scope.itemCatList3.length; i++){
            // 取一个数组元素 {}
            var itemCat = $scope.itemCatList3[i];
            // 判断id
            if (itemCat.id == newValue){
                $scope.goods.typeTemplateId = itemCat.typeId;
                break;
            }
        }
    });

    // $watch()：用于监听goods.typeTemplateId变量是否发生改变，如果发生改变，就会调用后面的函数
    $scope.$watch('goods.typeTemplateId', function(newValue, oldValue){

        if (!newValue){ // 判断是否为undefined，如果返回
            return;
        }

        // 根据类型模版id查询类型模版对象
        baseService.sendGet("/typeTemplate/findOne", "id=" + newValue)
            .then(function(response){

                // 获取该商品分类对应的类型模版中的品牌 {} response.data: 类型模版对象
                // response.data.brandIds: json格式的字符串 goods.brandId
                $scope.brandIds = JSON.parse(response.data.brandIds);

                if ($location.search().id){ // 修改商品
                    return;
                }
                // 扩展属性: [{"text":"分辨率"},{"text":"摄像头"}]
                // 需要保存到goodsDesc表: custom_attribute_items列
                // [{"text":"分辨率","value":"1920*1080(FHD)"},{"text":"摄像头","value":"1200万像素"}]
                // 把扩展属性json字符串，转化成JSON数组
                $scope.goods.goodsDesc.customAttributeItems = JSON
                        .parse(response.data.customAttributeItems);

            });

        // 根据类型模版id查询规格与规格选项
        baseService.sendGet("/typeTemplate/findSpecByTemplateId", "id=" + newValue)
            .then(function(response){
                //  [{"id":27,"text":"网络", "options" : [{},{}]},
                //  {"id":32,"text":"机身内存","options":[{},{}]}]
                $scope.specList = response.data;
            });
    });


    /** 为规格选项checkbox绑定点击事件 */
    $scope.updateSpecAttr = function($event, specName, optionName){
        /**
         * [{"attributeValue":["联通4G","移动4G","电信4G"],"attributeName":"网络"},
            {"attributeValue":["64G","128G"],"attributeName":"机身内存"}]
         */
        /** 根据json对象的key 从json数组中查询一个json对象 */
        var json = $scope.searchJsonByKey($scope.goods.goodsDesc.specificationItems,
            "attributeName", specName);
        if (json){
            //{"attributeValue":["联通4G","移动4G","电信4G"],"attributeName":"网络"}
            if ($event.target.checked){ // 判断checkbox是否选中
                json.attributeValue.push(optionName);
            }else{ // 没有选中
                // 删除attributeValue数组中的元素
                json.attributeValue.splice(json.attributeValue.indexOf(optionName),1);
                // 判断是否删除完了
                if (json.attributeValue.length == 0){
                    // 删除specificationItems数组中的元素
                    $scope.goods.goodsDesc.specificationItems.splice(
                        $scope.goods.goodsDesc.specificationItems.indexOf(json),1);
                }
            }
        }else {
            $scope.goods.goodsDesc.specificationItems
                .push({attributeValue: [optionName], attributeName: specName});
        }
    };

    /** 根据json对象的key 从json数组中查询一个json对象 */
    $scope.searchJsonByKey = function(jsonArr, key, value){
        for (var i = 0; i < jsonArr.length; i++){
            // 取一个数组元素
            // {"attributeValue":["联通4G","移动4G","电信4G"],"attributeName":"网络"}
            var json = jsonArr[i];
            if (json[key] == value){
                return json;
            }
        }
        return null;
    };

    /** 创建SKU数组(sku表中的数据) */
    $scope.createItems = function(){

        // 定义SKU数组，初始化一个数组元素
        // {spec:{}, price:0, num:9999,status:'0', isDefault:'0'} Item对象
        $scope.goods.items = [{spec:{}, price:0, num:9999,
                        status:'0', isDefault:'0'}];
        // 定义用户选中的规格选项数组
        var specItems = $scope.goods.goodsDesc.specificationItems;
        // 循环规格选项数组生成SKU数组
        // [{"attributeValue":["联通4G","移动4G","电信4G"],"attributeName":"网络"},
        // {"attributeValue":["64G","128G"],"attributeName":"机身内存"}]
        for (var i = 0; i < specItems.length; i++){
            // 取一个数组元素 {"attributeValue":["联通4G","移动4G","电信4G"],"attributeName":"网络"}
            var specItem = specItems[i];
            // 生成新的SKU数组
            $scope.goods.items = $scope.swapItems($scope.goods.items,
                specItem.attributeName, specItem.attributeValue);

        }

    };

    // 生成新的SKU数组
    $scope.swapItems = function(items, attributeName, attributeValue){
        // 定义新的SKU数组
        var newItems = new Array();

        for (var i = 0; i < items.length; i++){
            // 取一个数组元素
            // {spec:{}, price:0, num:9999, status:'0', isDefault:'0'}
            var item = items[i];

            // attributeValue : ["联通4G","移动4G","电信4G"]
            for (var j = 0; j < attributeValue.length; j++){
                // 生成一个新的item
                var newItem = JSON.parse(JSON.stringify(item));
                // 设置spec: {"网络":"联通4G","机身内存":"64G"}
                newItem.spec[attributeName] = attributeValue[j];
                // 添加到新的SKU数组
                newItems.push(newItem);
            }
        }

        return newItems;
    };


    // 初始化封装查询条件的json对象
    $scope.searchEntity = {};

    // 定义分页查询方法
    $scope.search = function(page, rows){
        /** 发送异步请求 */
        baseService.findByPage("/goods/findByPage", page,
            rows, $scope.searchEntity)
            .then(function(response){
                // response.data: List<Map> [{},{}] 与 总记录数
                // {"rows" : [{},{}], "total" : 100}
                // 获取查询的数据
                $scope.dataList = response.data.rows;
                // 更新分页组件中的总记录数
                $scope.paginationConf.totalItems = response.data.total;
            });
    };

    /** 定义商品状态数组 */
    $scope.status = ['未审核','已审核','审核未通过','关闭'];

    /** 根据主键id查询商品 */
    $scope.findOne = function(){
        // http://shop.lvdou.com/admin/goods_edit.html?id=149187842868035&name=admin
        // $location.search把请求URL问号后面请求参数转化成json对象{id : '', name : ''}
        var id = $location.search().id;
        // 发送异步请求查询商品
        baseService.sendGet("/goods/findOne", "id=" + id).then(function(response){
            // 获取响应数据
            $scope.goods = response.data;
            // 设置富文本编辑器中的内容
            editor.html($scope.goods.goodsDesc.introduction);
            // 把商品图片json字符串转化json数组
            $scope.goods.goodsDesc.itemImages = JSON.parse($scope.
                goods.goodsDesc.itemImages);
            // 把商品的扩展属性json字符串转化成json数组
            $scope.goods.goodsDesc.customAttributeItems = JSON.parse($scope.
                goods.goodsDesc.customAttributeItems);
            // [{"attributeValue":["联通4G","移动4G","电信4G"],"attributeName":"网络"},
            // {"attributeValue":["64G","128G"],"attributeName":"机身内存"}]
            // 把用户选中的规格选项json字符串转化json数组
            $scope.goods.goodsDesc.specificationItems = JSON.
                parse( $scope.goods.goodsDesc.specificationItems);
            // 迭代items
            for (var i = 0; i < $scope.goods.items.length; i++){
                // 取一个数组元素 把json对象中的spec字符串转化json对象
                $scope.goods.items[i].spec = JSON.parse($scope.goods.items[i].spec);
            }

        });
    };

    /** 判断规格选项是否选中 */
    $scope.checkAttributeValue = function(name, optionName){
        // [{"attributeValue":["联通4G","移动4G","电信4G"],"attributeName":"网络"},
        // {"attributeValue":["64G","128G"],"attributeName":"机身内存"}]
        var json = $scope.searchJsonByKey($scope.goods.goodsDesc.specificationItems,
            "attributeName",name);
        // json: {"attributeValue":["联通4G","移动4G","电信4G"],"attributeName":"网络"}
        if (json){
            return json.attributeValue.indexOf(optionName) != -1;
        }
        return false;
    };










    /** 批量删除 */
    $scope.delete = function(){
        if ($scope.ids.length > 0){
            baseService.deleteById("/goods/delete", $scope.ids)
                .then(function(response){
                    if (response.data){
                        $scope.reload();
                    }else{
                        alert("删除失败！");
                    }
                });
        }
    };

});