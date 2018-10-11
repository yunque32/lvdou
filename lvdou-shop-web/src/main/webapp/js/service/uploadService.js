/** 上传文件服务层 */
app.service('uploadService', function($http){

    /** 定义文件异步上传的方法 */
    this.uploadFile = function(){

        // 创建表单数据对象
        var formData = new FormData();
        // 表单数据对象追加上传的文件
        // 第一个参数：请求参数名 <input type="file" name='file'/>
        // 第二个参数：取html页面中第一个file
        formData.append("file", file.files[0]);

        return $http({
            method : 'post', // 请求方式
            url : '/upload', // 请求URL
            data : formData, // 表单数据对象
            headers : {"Content-Type": undefined}, // 设置请求头
            transFormRequest : angular.identity // 转换表单的请求对象(把文件转化成字节)
        });

    };

});