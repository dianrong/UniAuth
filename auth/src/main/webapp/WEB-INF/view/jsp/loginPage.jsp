<%@ page session="false" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
  <title>用户登陆-点融网官网</title>
  <link rel="shortcut icon" href="../images/favicon.ico"/>
  <link rel="stylesheet" type="text/css" href="../css/main.css">
</head>
<body>
  <div class="login-container">
    <header class="sl-header">
      <div class="container">
        <a href="https://www.dianrong.com/" class="header-logo">点融网 - DianRong</a>
        <div class="service-number"><i class="sl-icon-bold-phone"></i>客服热线：400-921-9218</div>
      </div>
    </header>

    <div id="login-form" class="container login-content">
      <div class="login-content">
        <div class="content-row clearfix">
          <div class="login-content-banner">
            <img src="../images/login-banner.jpeg">
          </div>
          <div class="login-content-form">
            <header class="login-content-form-title">登录</header>
            <form name="loginForm" class="loginForm" role="form" action="/auth-server/users/loginsmart" method="POST" novalidate>
              <div class="input-wrapper">
                <input name="identity" id="identity" type="text" value="${identity}" maxlength="32" class="usernameSingle" placeholder="请输入用户名／手机号码／邮箱名" required="">
              </div>
              <div class="input-wrapper">
                <input name="password" id="password" type="password" maxlength="32" class="passwordSingle" placeholder="请输入登录密码" required="">
                <c:if test="${redirectUrl != null}">
                <input type="hidden" name="targetUrl" value="${redirectUrl}">
                </c:if>
                <input type="hidden" name="service" value="${service}">
              </div>
              <c:if test="${needCaptcha == true}">
              <div id="captcha-area" class="code-verify-row clearfix">
                <div class="input-code">
                  <div class="input-wrapper">
                    <input type="text" id="captcha" maxlength="6" class="input-code" name="captcha" placeholder="请输入验证码">
                  </div>
                </div>
                <div class="captcha-line">
                  <img id="captcha-img" class="captcha-img" src="../images/captcha.jpg">
                  <a id="captcha-refresh-btn" class="btn btn-primary refresh-captcha"><span class="sl-icon-repayment"></span></a>
                </div>
              </div>
              </c:if>
              <div class="check-find-pwd clearfix">
                <div class="check-remember">
                  <input type="checkbox" id="remember" name="rememberMe">
                  <label for="remember"><span class="tick-wrapper"><span></span></span> </label>
                </div>
                <div class="remember-words">记住我</div>
                <div class="forget-pwd">
                  <a class="forget-password-link" href="https://www.dianrong.com/account/password-reset" target="_self">忘记密码？</a>
                </div>
              </div>
              <div id="error-msg" class="alert-msg">
              <c:if test="${errorKey != null}">
              <spring:message code="error.${errorKey}"/>
              </c:if>
              </div>
              <div class="button-wrapper">
                <button id="login-btn" type="submit" class="btn btn-primary" disabled="disabled">登录</button>
              </div>
              <div class="third-login">
                <span class="third-party-login-platform">使用第三方账号登录</span>
                <a href="https://api.weibo.com/oauth2/authorize?client_id=4271802753&redirect_uri=https%3A%2F%2Fwww.dianrong.com%2Faccount%2FweiboLoginCallback&forcelogin=true&state=login" title="用新浪微博登录" class="weibo-wrapper">
                  <span class="font-icon weibo-sina"></span>
                </a>
                <a href="https://graph.qq.com/oauth/show?which=Login&display=pc&response_type=code&client_id=100498332&redirect_uri=https%3A%2F%2Fwww.dianrong.com%2Faccount%2FqqLoginCallback&scope=get_user_info&state=login" title="用腾讯QQ登录" class="qq-wrapper">
                  <span class="sl-icon-qq font-icon tencent-qq"></span>
                </a>
              </div>
              <div class="borrower-link">
                借款人登录<a href="https://www.dianrong.com/new-borrower/login">请点击此处</a>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
    <footer id="sl-footer" class="sl-footer">
      <div class="container footer-site-map clearfix">
        <dl>
          <dt class="title footer-home">公司信息</dt>
          <dd><a href="https://www.dianrong.com/public/about#/company">公司简介</a></dd>
          <dd><a href="https://www.dianrong.com/public/about#/news">点融动态</a></dd>
          <dd><a href="https://www.dianrong.com/public/about#/media">媒体报道</a></dd>
          <dd><a href="https://www.dianrong.com/public/about#/job">加入我们</a></dd>
          <dd><a href="https://www.dianrong.com/public/about#/company?anchor=office">联系我们</a></dd>
          <dd><a href="http://forum.dianrong.com/" target="_bank">点融社区</a></dd>
        </dl>
        <dl class="policy">
          <dt class="title footer-policy">相关政策</dt>
          <dd><a href="https://www.dianrong.com/public/about#/safety" target="_blank">安全保障</a></dd>
          <dd><a href="https://www.dianrong.com/public/rates-and-fees" target="_blank">费率说明</a></dd>
          <dd><a href="https://www.dianrong.com/public/principal-protection" target="_blank">本金保障</a></dd>    <dd><a href="https://www.dianrong.com/public/terms-of-use" target="_blank">使用条款</a></dd>
          <dd><a href="https://www.dianrong.com/public/terms-of-use?protocol=privacy-and-security" target="_blank">隐私保护</a></dd>
          <dd><a href="https://www.dianrong.com/public/disclaimer" target="_blank">免责声明</a></dd>
        </dl>
        <dl class="app-download">
          <dt class="title footer-mobile">手机应用</dt>
          <dd class="download-link"><a href="https://itunes.apple.com/us/app/dian-rong-wang/id725186555?mt=8" target="_blank" rel="nofollow" class="iPhone"><i class="sl-icon-apple"></i>iPhone</a></dd>
          <dd class="download-link"><a href="http://app.mi.com/detail/56106" target="_blank" rel="nofollow" class="android"><i class="sl-icon-android"></i>Android</a></dd>
          <dd class="qr-code"></dd>
          <dt class="title footer-public">关注公众号</dt>
          <dd class="public-code"></dd>
        </dl>
        <dl class="our-office">
          <dt>联系我们</dt>
          <dd class="map"></dd>
          <dd>北京</dd>
          <dd>上海</dd>
          <dd>广州</dd>
          <dd>重庆</dd>
          <dd>南京</dd>
          <dd>成都</dd>
          <dd>天津</dd>
          <dd>武汉</dd>
          <dd>大连</dd>
          <dd>深圳</dd>
          <dd>合肥</dd>
          <dd><a href="https://www.dianrong.com/public/about#/company?anchor=office">更多</a></dd>
          <dd class="media">如果您有广告投放，媒体合作需求，请发送邮件至：<a href="mailto:media@dianrong.com">media@dianrong.com</a></dd>
        </dl>
      </div>
      <dl class="container footer-friend-link clearfix">
        <dt>友情链接</dt>
        <dd><a href="http://iof.hexun.com/" target="_blank">和讯理财</a></dd>
        <dd><a href="http://licai.cofool.com/" target="_blank">叩富理财 </a></dd>
        <dd><a href="http://www.trustores.cn/" target="_blank">香港保险</a></dd>
        <dd><a href="http://www.asiafinance.cn/" target="_blank">金融导航</a></dd>
        <dd><a href="http://www.szjstz.cn/" target="_blank">金苏财富</a></dd>
        <dd><a href="http://www.guijinshu.com/" target="_blank">贵金属</a></dd>
        <dd><a href="http://cngold.com.cn/" target="_blank">中金网</a></dd>
        <dd><a href="http://www.dingniugu.com/" target="_blank">顶牛股网</a></dd>
        <dd><a href="http://www.ucai123.com/" target="_blank">航运指数</a></dd>
        <dd><a href="http://www.fxsol-uk.com/" target="_blank">Fxsol官网</a></dd>
        <dd><a href="http://www.leadbank.com.cn/" target="_blank" class="lead-position">利得财富</a></dd>
        <dd><a href="http://www.dianrong.com/caifu/" target="_blank">P2P理财</a></dd>
      </dl>
      <ul class="footer-copyright">
        <li>©2015 点融网</li>
        <li>沪ICP备14028311号</li>
        <li>上海点荣金融信息服务有限责任公司</li>
      </ul>
      <div class="footer-authentication-info">
        <a target="_blank" class="picp" href="http://www.zx110.org/picp?sn=310101100041386" rel="nofollow">
          <span>310101100041386</span>
        </a>
        <a target="_blank" class="norton" href="https://trustsealinfo.verisign.com/splash?form_file=fdf/splash.fdf&dn=www.dianrong.com&lang=zh_cn" rel="nofollow"></a>
        <a target="_blank" class="verifyseal" href="https://ss.knet.cn/verifyseal.dll?sn=e15010631011557181nh4l000000&ct=df&a=1&pa=0.6343199273105711">
        </a>
        <a id="__szfw_logo__" class="szfw" href="https://search.szfw.org/cert/l/CX20150107006323006821" target="_blank"></a>
        <a target="_blank" href="http://www.itrust.org.cn/yz/pjwx.asp?wm=1062547634" class="itrust" rel="nofollow"></a>
      </div>
    </footer>
  </div>
  <script src="../js/main.js"></script>
</body>
</html>
