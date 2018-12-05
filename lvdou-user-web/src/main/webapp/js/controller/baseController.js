/** 基础控制器层 */
app.controller("baseController", function($scope, $http){

    /** 获取登录用户名 */
    $scope.loadUsername = function(){
        /** 定义重定向URL */
        $scope.redirectUrl = window.encodeURIComponent(location.href);
        /** 获取用户登录信息 */
        $http.get("/user/showLoginName")
            .then(function(response){
                if(response.data.loginName){
                    $scope.loginName = response.data.loginName;
                }else{
                    alert(response.data.msg);
                }

        });
    };
    // $(function(){
    //     $(".button").click(function(){
    //         $(this).css({"background-color":"#518f09","color":"yellow"})
    //          .siblings().css({"background-color":"#518f09","color":"#fff"});
    //     })
    // })

});