<!-- include some java object -->
<%@ page import="com.dianrong.common.uniauth.common.cons.AppConstants" %>
<%@ page import="com.dianrong.common.uniauth.cas.model.CasLoginCaptchaInfoModel"%>
<jsp:directive.include file="top.jsp" />
<div id="cookiesDisabled" class="errors" style="display:none;">
    <h2><spring:message code="screen.cookies.disabled.title" /></h2>
    <p><spring:message code="screen.cookies.disabled.message" /></p>
</div>

<p><spring:message code="screen.welcome.security"/></p>

<div class="box" id="login">
    <form:form method="post" id="fm1" commandName="${commandName}" htmlEscape="true">
        <form:errors path="*" id="msg" cssClass="errors" element="div" htmlEscape="false" />
		<c:if test="${not empty param.dupsession}">
			<p><font color="red"><spring:message code="screen.tips.session.dup" /></font></p><br>
		</c:if>
		
		<!-- show login page title -->
		<c:if test="${empty edituserinfo}">
        	<h2><spring:message code="screen.welcome.instructions" /></h2>
        </c:if>
        <c:if test="${not empty edituserinfo}">
        	<h2 class="text_decoration_none">
				<a href="login"><spring:message code="screen.password.reset.step.backtofirstpage" /></a>
				&gt;
				<spring:message code="screen.personal.info.edit.title" />
			</h2>
		</c:if>
		<!-- show login page title -->
			<c:choose>  
			   <c:when test="${not empty edituserinfo}">  
			   			<section class="row hiddenbtn"  id="login_domain_select">
			   </c:when>  
			   <c:otherwise> 
			   			<section class="row" id="login_domain_select">
			   </c:otherwise>  
			</c:choose>  
	            <label for="domain"><spring:message code="screen.welcome.label.domain" /></label>
	            <spring:message code="screen.welcome.label.domain.accesskey" var="domainAccessKey" />
	            <div class="select">
		            <form:select id="domain" tabindex="0" accesskey="${domainAccessKey}" path="domain">
		            	<c:if test="${not empty domains}">
		            		<c:forEach items="${domains}" var="domain">
	  							<form:option value="${domain.zkDomainUrlEncoded}"  customLoginPage="${domain.isCustomizedLoginPage}">${domain.code}</form:option>
							</c:forEach>
		            	</c:if>
		            </form:select>
	            </div>
   		 </section>
 		<c:if test="${not empty edituserinfo}">
  		 		<section class="row height55">
            	<label class="notice-red padding-top20"><spring:message code="screen.personal.info.goto.edit.relogin.notice"/></label>
           </section>
	     </c:if>
   		 
        <section class="row">
            <label for="username"><spring:message code="screen.welcome.label.netid" /></label>
            <c:choose>
                <c:when test="${not empty sessionScope.openIdLocalId}">
                    <strong><c:out value="${sessionScope.openIdLocalId}" /></strong>
                    <input type="hidden" id="username" name="username" value="<c:out value="${sessionScope.openIdLocalId}" />" />
                </c:when>
                <c:otherwise>
                    <spring:message code="screen.welcome.label.netid.accesskey" var="userNameAccessKey" />
                    <form:input cssClass="required" cssErrorClass="error" id="username" size="25" tabindex="1" accesskey="${userNameAccessKey}" path="username" autocomplete="off" htmlEscape="true" value="${sessionScope.pwdg_emailVal}"/>
                </c:otherwise>
            </c:choose>
        </section>
    
        <section class="row">
            <label for="password"><spring:message code="screen.welcome.label.password" /></label>
                <%--
                NOTE: Certain browsers will offer the option of caching passwords for a user.  There is a non-standard attribute,
                "autocomplete" that when set to "off" will tell certain browsers not to prompt to cache credentials.  For more
                information, see the following web page:
                http://www.technofundo.com/tech/web/ie_autocomplete.html
                --%>
            <spring:message code="screen.welcome.label.password.accesskey" var="passwordAccessKey" />
            <form:password cssClass="required" cssErrorClass="error" id="password" size="25" tabindex="2" path="password"  accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" />
            <span id="capslock-on" style="display:none;"><p><img src="<%=path %>/images/warning.png" valign="top"> <spring:message code="screen.capslock.on" /></p></span>
        </section>

		<!-- captcha box -->
		<%
			Object casCaptchaObj = session.getAttribute(AppConstants.CAS_USER_LOGIN_CAPTCHA_VALIDATION_SESSION_KEY);
			if(casCaptchaObj != null){
				CasLoginCaptchaInfoModel  tcasCaptchaObj = (CasLoginCaptchaInfoModel)casCaptchaObj;
				if(!tcasCaptchaObj.canLoginWithoutCaptcha()){
					%>
						<section class="row">
				            <label for="captcha"><spring:message code="screen.welcome.label.captcha" /></label>
				            <div class="captcha-class">
						            <input type="text" size="12" tabindex="3" id="captcha" name="captcha">
						    		<img alt="picture" src="<%=path %>/uniauth/verification/captcha" title="<spring:message code="screen.init.password.step1.content.verifycode.title"/>"  id="cas_login_captcha_change_img" >
									 <a  href="javascript:void(0);" tabindex="4"  id="cas_login_captcha_change_a"><spring:message code="screen.welcome.button.captcha.change"/></a>
				            </div>
				        </section>
					<%
				}
			}
		%>
		
        <section class="row btn-row">
            <input type="hidden" name="lt" value="${loginTicket}" />
            <input type="hidden" name="execution" value="${flowExecutionKey}" />
            <input type="hidden" name="_eventId" value="submit" />

            <input class="btn-submit  enable-after-init-success"  name="cas_submit"  disabled="disabled"  accesskey="l"  value="<spring:message code="screen.welcome.button.login" />"  tabindex="6"   id="btn_cas_submit"   type="button" />
            <input class="btn-reset" name="reset" accesskey="c" value="<spring:message code="screen.welcome.button.clear" />" tabindex="7" type="reset" />
            
            <c:if test="${empty edituserinfo}">
            		<div class="personal-info-link text_decoration_none">
		            	<div class="gotoedit-link">
		            		<a href="cas/login?edituserinfo=go"  title="<spring:message code="screen.welcome.link.userinfo.goedit.title"/>" id="to_edit_userinfo_btn"><spring:message code="screen.welcome.link.userinfo.goedit"/></a>
		            	</div>
			            <div class="forgetpwd-link">
			            	<a href="uniauth/forgetPassword?step=0" title="<spring:message code="screen.welcome.link.password.forget.title"/>" id="to_reset_pwd_btn"><spring:message code="screen.welcome.link.password.forget"/></a>
			            </div>
		            </div>
        	</c:if>
        </section>
    </form:form>
</div>
<div id="sidebar">
    <div class="sidebar-content cas-ad hiddenbtn" id="cas-ad-div">
    </div>
</div>
<jsp:directive.include file="bottom.jsp" />
<script type="text/javascript" src="<%=path %>/js/loginpage.js?v=<%=version %>" ></script>
<script type="text/javascript" src="<%=path %>/js/pwdforget.js?v=<%=version %>" ></script>
<script type="text/javascript" src="<%=path %>/js/userinfoedit.js?v=<%=version %>" ></script>
