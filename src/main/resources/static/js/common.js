/**
 * 删除字符串左右两端的空格
 * 
 * @param str
 * @returns
 */
function trim(str){
    if (typeof(str) == 'string') {
        return str.replace(/(^\s*)|(\s*$)/g, "");
    } else {
        return str;
    }
}

/**
 * 判断数据是否为空
 * 
 * @param data
 * @returns {Boolean}
 */
function isEmpty(data) {
    return typeof(data) == 'undefined' || data == null || trim(data) == "";
}

/**
 * 随机字符串
 * 
 * @param len 长度
 * @returns {String}
 */
function randomString(len) {
    var arr = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
               'a', 'b', 'c', 'd', 'e', 'f', 'g', 
               'h', 'i', 'j', 'k', 'l', 'm', 'n', 
               'o', 'p', 'q', 'r', 's', 't', 
               'u', 'v', 'w', 'x', 'y', 'z', 
               'A', 'B', 'C', 'D', 'E', 'F', 'G', 
               'H', 'I', 'J', 'K', 'L', 'M', 'N', 
               'O', 'P', 'Q', 'R', 'S', 'T', 
               'U', 'V', 'W', 'X', 'Y', 'Z'];
    var str = "";
    for(var i = 0; i < len; i++){
        str += arr[Math.round(Math.random() * (arr.length - 1))];
    }
    return str;
}

/**
 * Form表单转JSON
 * 
 * @param formObj
 * @param numKeys value为数值类型的key
 * @param boolKeys value为布尔类型的key
 * @returns
 */
function formToJson(formObj, numKeys, boolKeys) {
    if (numKeys == undefined) {
        numKeys = [];
    }
    if (boolKeys == undefined) {
        boolKeys = [];
    }
    // form表单数据
    var formData = formObj.serializeArray();
    var json = {};
    var key = null;
    var value = null;
    for(var i =0; i < formData.length; i++) {
        key = formData[i].name;
        value = formData[i].value;
        if (isEmpty(key) || isEmpty(value)) {
            continue;
        }
        if (numKeys.indexOf(key) != -1) {
            value = Number(value);
        }
        if (boolKeys.indexOf(key) != -1) {
            value = value == "true" ? true : false;
        }
        json[key] = value;
    }
    return json;
}

/**
 * Form表单转JSON字符串
 * 
 * @param formObj
 * @param numKeys value为数值类型的key
 * @param boolKeys value为布尔类型的key
 * @returns
 */
function formToJsonString(formObj, numKeys, boolKeys) {
    return JSON.stringify(formToJson(formObj, numKeys, boolKeys));
}

/**
 * 同步POST请求(JSON数据)
 * 
 * @param url
 * @param req
 */
function postJsonSync(url, req) {
    var data;
    $.ajax({
        url: url,
        type: "post",
        async: false,
        data: JSON.stringify(req),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (resp) {
            data = resp;
        },
    });
    return data;
}

/**
 * 异步POST请求(JSON数据)
 * 
 * @param url
 * @param req
 * @param callback
 */
function postJson(url, req, callback) {
    $.ajax({
        url: url,
        type: "post",
        data: JSON.stringify(req),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (resp) {
            if(callback != undefined && typeof(callback) == 'function'){
                callback(resp);
            }
        },
    });
}

/**
 * 倒计时
 * 
 * @param el 元素
 * @param tl 时长
 * @param bgcolor 背景色
 */
function countDown(el, tl, bgcolor) {
    if (isEmpty(bgcolor)) {
        bgcolor = "#6c757d";
    }
    // 创建样式
    var styleContent = ".btn-count-down{background-color:" 
        + bgcolor + " !important;border-color:" 
        + bgcolor + " !important;color:#fff !important;}";
    createStyle(styleContent);
    // 设置按钮
    el.addClass("btn-count-down").prop("disabled", true).text(tl + " 秒");
    var interval = setInterval(function() {
        if (tl <= 1) {
            el.removeClass("btn-count-down").prop("disabled", false).text("获取验证码");
            clearInterval(interval);
            return;
        }
        tl--;
        el.text(tl + " 秒");
    }, 1000);
}

/**
 * 创建style样式
 * 
 * @param content
 */
function createStyle(content) {
    var style = document.querySelector("style");
    if (style == null) {
        style = document.createElement("style")
        style.type = "text/css";
    }
    if (style.innerText.indexOf(content) != -1) {
        return;
    }
    try{
        style.appendChild(document.createTextNode(content));
    }catch(ex){
        // 针对IE
        style.styleSheet.cssText = content;
    }
    document.querySelector("head").appendChild(style);
}

/**
 * 成功提示窗
 * 
 * @param msg
 * @param callback
 */
function alertSucc(msg, callback) {
    // 上次计时器ID
    var timeoutId = $("#alertTips>input[type='hidden']").val();
    // 先清除已存在的计时器
    window.clearTimeout(timeoutId);
    $('#alertTips').attr("class", "alert alert-success text-center");
    $('#alertTips strong').text(msg);
    $('#alertTips').fadeIn();
    // 关闭按钮事件
    if(callback != undefined && typeof(callback) == 'function'){
        $('#alertTips button').on("click", callback);
    }
    timeoutId = window.setTimeout(function(){
        $('#alertTips').fadeOut();
        if(callback != undefined && typeof(callback) == 'function'){
            callback();
        }
    }, 3000);
    // 保存当前计时器ID
    $("#alertTips>input[type='hidden']").val(timeoutId);
}

/**
 * 失败提示窗
 * 
 * @param msg
 * @param callback
 */
function alertFail(msg, callback) {
    // 上次计时器ID
    var timeoutId = $("#alertTips>input[type='hidden']").val();
    // 先清除已存在的计时器
    window.clearTimeout(timeoutId);
    $('#alertTips').attr("class", "alert alert-danger text-center");
    $('#alertTips strong').text(msg);
    $('#alertTips').fadeIn();
    // 关闭按钮事件
    if(callback != undefined && typeof(callback) == 'function'){
        $('#alertTips button').on("click", callback);
    }
    timeoutId = window.setTimeout(function(){
        $('#alertTips').fadeOut();
        if(callback != undefined && typeof(callback) == 'function'){
            callback();
        }
    }, 3000);
    // 保存当前计时器ID
    $("#alertTips>input[type='hidden']").val(timeoutId);
}

/**
 * 上传文件回显文件名
 * 
 * @param fileId file元素ID
 */
function fileChangeEcho(fileId) {
    var files = $(fileId)[0].files;
    var name = "";
    for(var i = 0; i < files.length; i++) {
        name += files[i].name + (i != files.length - 1 ? ", " : "");
    }
    $(fileId).siblings(".custom-file-label").html(name == "" ? "选择文件" : name);
}

/**
 * 上传单个文件
 * 
 * @param fileId
 * @param callback
 */
function uploadFile(fileId, callback) {
    // 表单数据
    var formData = new FormData();
    formData.append("file", $(fileId)[0].files[0]);
    $.ajax({
        url: "/file/upload",
        dataType: "json",
        type: "post",
        data: formData,
        processData: false,    // 不对数据进行处理（默认会转为查询字符串）
        contentType: false,    // 不设置Content-Type请求头
        success: function(resp){
            callback(resp);
        },
        error: function(resp){
            console.log(resp);
        }
    });
}

/**
 * 上传多个文件
 * 
 * @param fileId
 * @param callback
 */
function uploadFiles(fileId, callback) {
    // 表单数据
    var formData = new FormData();
    formData.append("files", $(fileId)[0].files);
    $.ajax({
        url: "/file/uploads",
        dataType: "json",
        type: "post",
        data: formData,
        processData: false,    // 不对数据进行处理（默认会转为查询字符串）
        contentType: false,    // 不设置Content-Type请求头
        success: function(resp){
            callback(resp);
        },
        error: function(resp){
            console.log(resp);
        }
    });
}

/**
 * 下载文件(POST请求,JSON参数,返回二进制数据)
 * 
 * @param url 下载地址
 * @param req 参数
 * @param fileName 文件名(为空时,取响应头中设置的文件名)
 */
function downloadFile(url, req, fileName) {
    if (req == null) {
        req = {};
    }
    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, true);
    // 返回类型blob
    xhr.responseType = "blob";  
    xhr.setRequestHeader("Content-Type", "application/json; charset=utf-8");
    // 请求完成的处理函数
    xhr.onload = function () {
        if (this.status === 200) {
            var blob = this.response;
            var reader = new FileReader();
            // 转换为base64,可以直接放入a标签href
            reader.readAsDataURL(blob); 
            reader.onload = function (e) {
                // 创建一个a标签用于下载
                var a = document.createElement("a");
                if (isEmpty(fileName)) {
                    // 获取响应头中的文件名
                    var headerFileName = xhr.getResponseHeader("Content-disposition");
                    if(headerFileName){
                        fileName = decodeURI(headerFileName.replace("attachment;filename=", ""));
                    }
                }
                a.download = fileName;
                a.href = e.target.result;
                $("body").append(a); 
                a.click();
                $(a).remove();
            }
        }
    };
    // 以JSON格式发送Ajax请求
    xhr.send(JSON.stringify(req));
}

/**
 * 是否响应成功(状态码大于0)
 * 
 * @param resp
 * @returns {Boolean}
 */
function isSucc(resp) {
    return resp.code > 0;
}

/**
 * 从Java对象字符串中解析JSON对象
 * 
 * @param javaObjStr
 * @returns
 */
function parseJson(javaObjStr) {
    var start = javaObjStr.indexOf("=");
    var jsonStr = javaObjStr.substring(start + 1, javaObjStr.length);
    return JSON.parse(jsonStr);
}

/**
 * 添加加载中效果
 * 
 * @param e
 */
function addLoading(e) {
    var loadingText = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>'
        + '<span class="sr-only">Loading...</span>';
    e.html(loadingText);
}

/**
 * 去除标签
 * 
 * @param text
 * @returns
 */
function removeTag(text) {
    return text.replace(/<.*?>/g, "");
}

/**
 * 富文本编辑器
 * 
 * @param selector
 * @returns {Quill}
 */
function richEditor(selector) {
    var toolbarOptions = [
      ['bold', 'italic', 'underline', 'strike'],    // 加粗、斜体、下划线、删除线
      ['blockquote', 'code-block'],    // 引用、代码块                   
      [{ 'header': 1 }, { 'header': 2 }],    // 标题，1、2表示字体大小
      [{ 'list': 'ordered'}, { 'list': 'bullet' }],    // 列表
      [{ 'script': 'sub'}, { 'script': 'super' }],    // 上下标
      [{ 'indent': '-1'}, { 'indent': '+1' }],    // 缩进
      [{ 'direction': 'rtl' }],    // 文本方向
      [{ 'size': ['small', false, 'large', 'huge'] }],    // 字体大小
      [{ 'header': [1, 2, 3, 4, 5, 6, false] }],    // 几级标题
      [{ 'color': [] }, { 'background': [] }],    // 字体颜色、字体背景颜色
      [{ 'font': [] }],    // 字体
      [{ 'align': [] }],    // 对齐方式
      ['link', 'image', 'video', 'formula'],    // 链接、图片、视频、公式
      ['clean']    // 清除样式
    ];

    var quill = new Quill(selector, {
      modules: {
        toolbar: toolbarOptions
      },
      placeholder: "请输入内容",
      readOnly: false,
      theme: 'snow'
    });
    return quill;
}

/**
 * 图片转Base64
 * 
 * @param image
 * @returns
 */
function imageToBase64(image) {
    var canvas = document.createElement("canvas");
    canvas.width = image.width;
    canvas.height = image.height;
    var ctx = canvas.getContext("2d");
    ctx.drawImage(image, 0, 0, image.width, image.height);
    var ext = image.src.substring(image.src.lastIndexOf(".") + 1).toLowerCase();
    var dataURL = canvas.toDataURL("image/" + ext);
    return dataURL;
}

/**
 * 图片转Base64
 * 
 * @param imageUrl
 * @param callback
 */
function imageUrlToBase64(imageUrl, callback) {
    var base64;
    var image = new Image();
    image.onload = function(){
        base64 = imageToBase64(image);
        callback(base64);
    }
    image.src = imageUrl;
}