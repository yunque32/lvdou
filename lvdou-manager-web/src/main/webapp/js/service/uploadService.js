app.service('uploadService', function($http){

    /** 定义文件异步上传的方法 */
    this.uploadExcelFile = function(){

        // 创建表单数据对象
        var formData = new FormData();
        // 表单数据对象追加上传的文件
        // 第一个参数：请求参数名 <input type="file" name='file'/>
        // 第二个参数：取html页面中第一个file
        formData.append("file", file.files[0]);
        var file = document.getElementById("file").files[0];
        if(file.name) {
            var fileName = file.name.substring(file.name.lastIndexOf(".") + 1);
            if (fileName == 'xlsx' || fileName == "xls") {
                formData.append('file', file);
                $http({
                    method: "post",
                    url: commonService.projectName + "/so/assetmanage/upload",
                    data: formData,
                    headers: {'Content-Type': undefined},
                    transformRequest: angular.identity
                }).then(function (response) {
                    if (response.status == 200) {
                        alert("文件上传成功！！！");
                    } else {
                        alert("文件上传失败！！！");
                    }
                });
            } else {
                alert("文件格式不正确，请上传以.xlsx 或 .xls为后缀的文件。");
                $("#file").val("");
            }

        }

    };
    this.uploadFile=function () {

        var formData = new FormData();
        formData.append("file",file.files[0]);
        return $http({
            method : 'post',
            // 请求URL
            url : '/upload/uploadFile',
            // 表单数据对象
            data : formData,
            headers : {"Content-Type": undefined}, // 设置请求头
            transFormRequest : angular.identity // 转换表单的请求对象(把文件转化成字节)
        });

    }

});