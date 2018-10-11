/** 定义基础模块(不带分页模块) */
var app = angular.module('lvdou',[]);

/** $sce服务写成过滤器 '内容' | trustHtml */
app.filter("trustHtml", function($sce){
    return function(html){
        return $sce.trustAsHtml(html);
    };
});