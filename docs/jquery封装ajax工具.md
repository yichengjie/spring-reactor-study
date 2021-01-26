1. 封装ajax工具类
    ```javascript
    var httpUtil = {};
    httpUtil.dealAjaxRequest4SimpleParam = function(serverURL,simpleJsonData){//异步操作
         var defer = $.Deferred();
         var option = {
           url:serverURL,
           type: 'POST',
           timeout : 100000, //超时时间设置，单位毫秒
           data:simpleJsonData,
           dataType:'json',
           error: function (err) {   
               defer.reject(err) ;
           },
           success:function (result) {
               defer.resolve(result);
           }
         };
         $.ajax(option); //发送ajax请
         return defer.promise() ;
    }
    //使用contentType:'application/json'后，
    // data必须json字符串，且后台必须使用@RequestBody接收数据
    httpUtil.dealAjaxRequest4JSObj = function(serverURL,jsObjData){//异步操作 
        var defer = $.Deferred();
        var option = {
           contentType:'application/json' ,
           url:serverURL,
           type: 'POST',
           timeout : 100000, //超时时间设置，单位毫秒
           data:JSON.stringify(jsObjData),
           dataType:'json',
           error: function (err) {   
               defer.reject(err) ;
           },
           success:function (result) {
               defer.resolve(result);
           }
        };
        $.ajax(option); //发送ajax请
        return defer.promise() ;
    }
    ```
2. 使用示例
    ```javascript
    // 使用示例后台使用@RequestBody获取
    function demo001() {
        var serverURL = "/api/user/add" ;
        var jsObjData = {"username": "yicj","addr":"bjs"} ;
		// spring mvc接收@RequestBody注解的参数
        var ajaxing = httpUtil.dealAjaxRequest4JSObj(serverURL,jsObjData) ;
        $.when(ajaxing).then(function (resp) {
            console.info(resp)
        }, function (err) {
            console.error(err) ;
        }) ;
    }
    // 使用示例后台使用@RequestBody获取
    function demo002() {
        var serverURL = "/api/user/add" ;
        var jsObjData = {"username": "yicj","addr":"bjs"} ;
		//后台public JsonResult<String> add(String username, String addr){}
        //后台也可获取public JsonResult add2(User user){}
        var ajaxing = httpUtil.dealAjaxRequest4SimpleParam(serverURL,jsObjData) ;
        $.when(ajaxing).then(function (resp) {
            console.info(resp)
        }, function (err) {
            console.error(err) ;
        }) ;
    }
    ```