/*
* Name: xhr,AJAX封装函数
* Description: 一个ajax调用封装类,仿jquery的ajax调用方式
* Author:十年灯
*/
var xhr = function () {
    var 
    ajax = function  () {
        return ('XMLHttpRequest' in window) ? function  () {
                return new XMLHttpRequest();
            } : function  () {
            return new ActiveXObject("Microsoft.XMLHTTP");
        }
    }(),
    formatData= function (fd) {
        var res = '';
        for(var f in fd) {
            if (fd[f]) {
                res += f+'='+fd[f]+'&';
            };
        }
        return res.slice(0,-1);
    },
    AJAX = function(ops) {
        var     
        root = this,
        req = ajax();
        root.url = ops.url;
        root.contentType = ops.contentType;
        root.type = ops.type || 'responseText';
        root.method = ops.method || 'GET';
        root.async = ops.async || true;     
        root.data = ops.data || {};
        root.complete = ops.complete || function  () {};
        root.success = ops.success || function(){};
        root.error =  ops.error || function (s) { alert(root.url+'->status:'+s+'error!')};
        root.abort = req.abort;
        root.setData = function  (data) {
            for(var d in data) {
                root.data[d] = data[d];
            }
        }
        root.send = function  () {
            var datastring = formatData(root.data),
            sendstring,get = false,
            async = root.async,
            complete = root.complete,
            method = root.method,
            type=root.type;
            if(method === 'GET') {
                root.url+='?'+datastring;
                get = true;
            }
            req.open(method,root.url,async);

            if(!get) {
                if (root.contentType) {
                    req.setRequestHeader("Content-type",root.contentType);
                }else{
                    req.setRequestHeader("Content-type","application/x-www-form-urlencoded");
                }
                sendstring = datastring;
            }      
            //在send之前重置onreadystatechange方法,否则会出现新的同步请求会执行两次成功回调(chrome等在同步请求时也会执行onreadystatechange)
            req.onreadystatechange = async ? function  () {
                // console.log('async true');
                if (req.readyState ==4){
                    complete();
                    if(req.status == 200) {
                        root.success(req[type]);
                    } else {
                        root.error(req.status);
                    }                   
                }
            } : null;
            req.send(sendstring);
            if(!async) {
                //console.log('async false');
                complete();
                root.success(req[type]);
            }
        }
        root.url && root.send();        
    };
    return function(ops) {return new AJAX(ops);}    
}();