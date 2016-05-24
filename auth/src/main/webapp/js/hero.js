/*
  hero.js
  hero

  Created by gpliu on 14/10/16.
  Copyright (c) 2015年 GPLIU. All rights reserved.
*/

var host = window.location.origin;
var path = host+'/borrower-static/wallet/v4' //如果url路径有前缀请加上;
var ui;
var err_ui = {
    nav:{
        title:"界面出错啦",
    },
    views:
    [
        {
            class:"HeroButton",
            frame:{x:"0.3x",y:"44",w:"0.4x",h:"66"},
            title:"刷新",
            click:{command:"refresh"}
        }
    ]
};
var loadedClass = [];

(function () {
    var w = window;
    var _deviceType = 'PC';
    var _userid;
    var _card;
    var _initData;
    document.onreadystatechange = function () {
      var state = document.readyState;
      if (state == 'complete') {
        _initData = API.getInitData();
        if (_initData.test) {
            var js = document.createElement('script');
            js.src = host+window.location.pathname.replace(/html/,'js');
            document.head.appendChild(js);
        };
        var ua = navigator.userAgent.toLowerCase();
        if(ua.indexOf("hero-ios") > 0){
            API.setDeviceType('IOS');
        }else if (ua.indexOf('hero-android') > 0) {
            API.setDeviceType('ANDROID');
        }else if(ua.indexOf('micromessenger') > 0){
            API.setDeviceType('WECHAT');
        }else{
            API.setDeviceType('PC');
        };
      }
    };
    w.API = {
        connect:function()
        {
            io = io.connect();
            io.on('connect',function(){
                API.out({'datas':[{'name':'netState','backgroundColor':'00ff00'}]});
            });
            io.on('msg',function(data){
                API.out(data);
            });
        },
        disconnect:function()
        {
            io.disconnect();
        },
        out:function(data)
        {
            if (_deviceType == 'IOS') {
                if (typeof(data) === 'object') {
                    data = JSON.stringify(data);
                };
                data = encodeURIComponent(data);
                var nativeObject = 'hero://' + data;
                var iframe = document.createElement('iframe');
                iframe.setAttribute('src', nativeObject);
                document.documentElement.appendChild(iframe);
                iframe.parentNode.removeChild(iframe);
                iframe = null;
            }else if(_deviceType == 'IOS8'){
                window.webkit.messageHandlers.native.postMessage(data)
            }else if(_deviceType == 'ANDROID'){
                if (typeof(data) === 'object') {
                    data = JSON.stringify(data);
                };
                window.native.on(data);
            }else{
                API.on(data);
            }
        },
        in:function(data){
            if (typeof(data) === 'string') {
                data = eval('(' + data + ')');
            };
            if (data.her) {
                if (_card.charAt(0) == '/') {
                    data = data.her;
                    xhr({
                        url:_card,
                        data:data,
                        async:true,
                        method:'GET',
                        success: function  (data) {
                            API.out(data);
                        },
                        error:function(data){
                            API.out(data);
                        }
                    });
                }else{
                    data = data.her;
                    data['userid'] = _userid;
                    data['card'] = _card;
                    io.emit('on', data);
                };
            }else if (data.socket) {
                data = data.socket;
                data['userid'] = _userid;
                data['card'] = _card;
                io.emit('on', data);
            }else if(data.http){
                data = data.http;
                var api = data.url;
                var success = data.success;
                xhr({
                    url:(api.search(/ttp/)>0?api:host+api),
                    async:true,
                    data:data.data,
                    contentType:data.contentType,
                    method:data.method?data.method:'GET',
                    success: function(data){
                        API.out({command:'stopLoading'});
                        if (typeof(data) === 'string') {
                            data = JSON.parse(data);
                        };
                        if (data.result === 'success') {
                            if (success) {
                                success(data);
                            }else{
                                data.api = api;
                                API.reloadData(data);
                            }
                        }else if(data.result === 'login'){
                            API.out({command:'present:'+path+'/login.html'});
                        }
                        else if(data.errors) {
                            API.out({datas:{name:'toast',text:data.errors[0]}});
                        }
                        if (data.result === 'error') {
                            if (data.content) {
                                if (data.content.apiReturn) {
                                    if (data.content.apiReturn.ValidationError || data.content.apiReturn.ErrorMessage) {
                                        API.out({datas:{name:'toast',text:data.content.apiReturn.ValidationError+data.content.apiReturn.ErrorMessage}});
                                    };
                                };
                            };
                        }
                    },
                    error:function(data){
                        API.out({command:'stopLoading'});
                        API.out({datas:{name:'toast',text:'网络错误'}});
                    }
                });
            }else{
                API.special_logic(data);
            };
        },
        special_logic:function(){
            //需要被各个页面重写的方法
        },
        boot:function(){
            //需要被各个页面重写的方法
        },
        reloadData:function(){
            //需要被各个页面重写的方法
        },
        deviceType:function()
        {
            return _deviceType;
        },
        setDeviceType:function(deviceType)
        {
            _deviceType = deviceType;
            if (ui === undefined) {
                ui = err_ui;
            };
            if (ui !== 'blank') {
                API.out({ui:ui});
            };
            if(_deviceType === 'IOS'){
                API.boot(_initData);
            }else if(_deviceType === 'ANDROID'){
                API.boot(_initData);
            }else{
                setTimeout(function() {
                    API.boot(_initData);
                }, 500);
            }
        },
        setUserid:function(userid)
        {
            _userid = userid;
        },
        setCard:function(card){
            _card = card;
            if (_card.charAt(0) !== '/') {
                API.connect();
            }
        },
        getInitData:function(){
            if (localStorage.boot) {
                _initData = eval('(' + localStorage.boot + ')')
                localStorage.boot = '';
            };  
            _initData = _initData || {};  
            var params = (window.location.search.split('?')[1] || '').split('&');
            for(var param in params) {  
                if (params.hasOwnProperty(param)){
                    paramParts = params[param].split('=');  
                    _initData[paramParts[0]] = decodeURIComponent(paramParts[1] || "");  
                 }
            }
            return _initData;
        },
        getDeviceType:function()
        {
            return _deviceType;
        },
        getVersion:function()
        {
            return '0.0.1';
        },
        getCookie:function(name) {
            var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
            if(arr = document.cookie.match(reg))
                return unescape(arr[2]);
            else
                return null;
        },
        camelCase2bar:function(str){
          var low = str.toLowerCase();
          var des = low.substr(0,2);
          for(var i = 2 ; i < str.length;i++){
            if (str.charAt(i) !== low.charAt(i)) {
              des = des.concat('-' + low.charAt(i));
            }else{
              des = des.concat(low.charAt(i));
            };
          }
          return des;
        },
        createElementFromJson:function(json){
            var viewName = json.class || json.res;
            if (viewName ) {
                var className = API.camelCase2bar(viewName);
                var str = '<'+className+' json='+encodeURIComponent(JSON.stringify(json))+'></'+className+'>';
                return str;
            };
            return '';
        },
        contain:function(objs,obj){
            var i = objs.length;
            while (i--) {
                if (objs[i] === obj) {
                    return true;
                }
            }
            return false;
        },
        on:function(json){
            if (json instanceof Array) {
                for(var num in json){
                    API.on(json[num]);
                }
                return;
            };
            if (json.ui_fragment) {
                for(var num in json.ui_fragment){
                    var view = json.ui_fragment[num];
                    if (view.parent) {
                        var element = document.getElementById(view.parent);
                        element.json = {subViews:[view]};
                    }else{
                      var content = document.getElementById('content');
                      var fragment=document.createElement('div');
                      var html=API.createElementFromJson(view);
                      fragment.innerHTML=html;
                      content.appendChild(fragment);
                    }
                }
            };
            if (json.ui) {
                // fix: duplicate loading meta and webcomponents
                var metas=document.getElementsByTagName('meta'),
                 metaLength=metas.length,
                 metaLoaded=false;
                 
                 if(metaLength>0){
                   while(metaLength--){
                     if(metas[metaLength].name==='viewport'){
                       metaLoaded=true;
                       break;
                     }
                   }
                 }
                if(!metaLoaded){
                  var meta = document.createElement('meta');
                  meta.content = "width=device-width,height=device-height,initial-scale=1.0,maximum-scale=1,minimum-scale=1,user-scalable=no";
                  meta.name = "viewport";
                  document.head.appendChild(meta);
                  document.body.style.margin = '0px';
                  document.body.style.padding = '0px';
                  var js = document.createElement('script');
                  js.src = path + '/bower_components/webcomponentsjs/webcomponents-lite.min.js';
                  document.head.appendChild(js);
                }
               
                var innerHTML = '';
                var ui = json.ui;
                var content = document.createElement('div');
                content.id = 'content';
                document.body.appendChild(content);
                if (ui.nav) {
                    if (ui.nav.title) {
                        document.title = ui.nav.title;
                    };
                    if (ui.nav.titleView) {

                    };
                    if (ui.nav.leftItems || ui.nav.rightItems){
                        content.style.top = '44px';
                        content.style.display = 'blcok';
                        content.style.position = 'absolute';
                        var link = document.createElement('link');
                        link.rel = 'import'
                        link.href = path+'/elements/hero-toolbar.html';
                        document.body.appendChild(link);
                        var element = API.createElementFromJson(
                        {
                            class:'HeroToolbar',
                            frame:{w:'1x',h:'44'},
                            title:ui.nav.title,
                            right:ui.nav.rightItems?ui.nav.rightItems[0]:undefined,
                            left:ui.nav.leftItems?ui.nav.leftItems[0]:undefined,
                        });
                        var bar = document.createElement('div');
                        bar.innerHTML = element;
                        document.body.appendChild(bar);
                    }
                };
                if (ui.views) {
                    var delayViews = [];
                    for(var num in ui.views){
                        var view = ui.views[num];
                        if (view.parent) {
                            delayViews.push(view);
                            continue;
                        };
                        try{
                            var viewName = view.class || view.res;
                            if(!API.contain(loadedClass,viewName)){
                                loadedClass.push(viewName);
                                var link = document.createElement('link');
                                link.rel = 'import'
                                link.href = path+'/elements/' + API.camelCase2bar(viewName)+".html";
                                document.body.appendChild(link);
                            }
                            var element = API.createElementFromJson(view);
                            innerHTML = innerHTML.concat(element);
                        }catch(e){
                            console.log(e);
                        }
                    }
                    content.innerHTML = innerHTML;
                    setTimeout(function(){
                        for(var num in delayViews){
                            var view = delayViews[num];
                            var element = document.getElementById(view.parent);
                            if(element){
                              element.json = {subViews:[view]};
                            }
                        }
                    },200);
                    if (ui.backgroundColor) {
                        document.body.style.backgroundColor = '#'+ui.backgroundColor;
                    };
                };

            }else if (json.datas) {
                var datas = json.datas;
                if (datas instanceof Array) {
                    for(var num in datas){
                        var data = datas[num];
                        if (data.frame) {
                            data.frame.p = {w:window.innerWidth,h:window.innerHeight};
                        };
                        var element = document.getElementById(data.name);
                        if (element) {
                            element.oon(data);
                        };
                    }
                }else{
                        var element = document.getElementById(datas.name);
                        if (element) {
                            element.oon(datas);
                        };
                };
            }else if (json.command) {
                var command = json.command;
                if (typeof(command) === 'string') {
                    if (command === 'refresh') {
                        window.location.reload();
                    }
                    else if(command==='showLoading'){
                      if(!API.contain(loadedClass,'hero-loading')){
                        loadedClass.push('hero-loading');
                        var link = document.createElement('link');
                        link.rel = 'import'
                        link.href = path+'/elements/hero-loading.html';
                        document.body.appendChild(link);
                      }
                      var loading=document.getElementById('loading');
                      if(!loading){
                        var element = API.createElementFromJson({
                            class:'HeroLoading',
                            show:true,
                            name:'loading'
                        });
                        var div=document.createElement('div');
                        div.innerHTML=element;
                        document.body.appendChild(div);
                      }
                      else{
                        API.out({datas:{name:'loading',show:true}});
                      }
                    }
                    else if(command==='stopLoading'){
                      API.out({datas:{name:'loading',show:false}});
                    }
                    else if(command.substring(0,5) === 'goto:'){
                        window.location.href = command.substring(5,command.length);
                    }else if(command.substring(0,5) === 'load:'){
                        window.location.href = command.substring(5,command.length);
                    }else if(command.substring(0,4) === 'back'){
                        window.history.back()
                    }else if(command.substring(0,8) === 'present:'){
                        window.location.href = command.substring(8,command.length);
                    }else if(command.substring(0,7) === 'dismiss'){
                        window.history.back()
                    }else if(command.substring(0,6) === 'submit'){
                        //todo 
                    }
                }else{
                    if (command.show) {
                        if (command.show.action) {
                          // confirm(command.show.content) && API.out(command.show.action);
                          if (!API.contain(loadedClass, 'hero-confirm')) {
                            loadedClass.push('hero-confirm');
                            var link = document.createElement('link');
                            link.rel = 'import'
                            link.href = path + '/elements/hero-confirm.html';
                            document.body.appendChild(link);

                            var element = API.createElementFromJson({
                              class: 'HeroConfirm',
                              name: 'confirm'
                            });
                            var div = document.createElement('div');
                            div.innerHTML = element;
                            document.body.appendChild(div);
                          }

                          setTimeout(function () {
                            API.out({ datas: { name: 'confirm', 'text': command.show.content || '',action:command.show.action} });
                          }, 200);
                        }else if(command.show.title){
                            // alert(command.show.title);
                          if (!API.contain(loadedClass, 'hero-alert')) {
                            loadedClass.push('hero-alert');
                            var link = document.createElement('link');
                            link.rel = 'import'
                            link.href = path + '/elements/hero-alert.html';
                            document.body.appendChild(link);

                            var element = API.createElementFromJson({
                              class: 'HeroAlert',
                              name: 'alert'
                            });
                            var div = document.createElement('div');
                            div.innerHTML = element;
                            document.body.appendChild(div);
                          }

                          setTimeout(function () {
                            API.out({ datas: { name: 'alert', 'text': command.show.title || '' } });
                          }, 200);
                      
                        }else if(command.show.class){
                            if(!API.contain(loadedClass,'hero-dialog')){
                                loadedClass.push('hero-dialog');
                                var link = document.createElement('link');
                                link.rel = 'import'
                                link.href = path+'/elements/hero-dialog.html';
                                document.body.appendChild(link);
                            }
                           
                            var element = API.createElementFromJson(
                            {
                                class:'HeroDialog',
                                str:JSON.stringify(command.show),
                            });
                            var div=document.createElement('div');
                            div.innerHTML=element;
                            document.body.appendChild(div);
                        }
                    };
                    if (command.delay) {
                        var delayObj = command.delay;
                        var delayTime = command.delayTime;
                        setTimeout(function() {
                                 API.out(delayObj);
                                 }, delayTime);
                    };
                    if (command.viewWillAppear) {
                        if (command.viewWillAppear instanceof Array) {
                            API.in(command.viewWillAppear[0]);
                        }else{
                            API.in(command.viewWillAppear);
                        };
                    };
                    if (command.viewWillDisappear) {
                        
                    };
                };
            }else{
                API.special_logic(json);
            }

        }
    };
})();

