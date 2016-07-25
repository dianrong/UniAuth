<%--
    Licensed to Apereo under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Apereo licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License.  You may obtain a
    copy of the License at the following location:

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.

--%>

<!-- include some java object -->
<%@ page import="com.dianrong.common.uniauth.common.cons.AppConstants" %>
<%@ page import="com.dianrong.common.uniauth.cas.model.CasLoginCaptchaInfoModel"%>

<jsp:directive.include file="top.jsp" />


<c:if test="${not empty redirectUrl}">
	<script>
		top.window.location = "${redirectUrl}";
	</script>
</c:if>

<!-- 
<c:if test="${not pageContext.request.secure}">
    <div id="msg" class="errors">
        <h2><spring:message code="screen.nonsecure.title" /></h2>
        <p><spring:message code="screen.nonsecure.message" /></p>
    </div>
</c:if>
 -->
 
<div id="cookiesDisabled" class="errors" style="display:none;">
    <h2><spring:message code="screen.cookies.disabled.title" /></h2>
    <p><spring:message code="screen.cookies.disabled.message" /></p>
</div>

<!-- 
<c:if test="${not empty registeredService}">
    <c:set var="registeredServiceLogo" value="images/webapp.png"/>
    <c:set var="registeredServiceName" value="${registeredService.name}"/>
    <c:set var="registeredServiceDescription" value="${registeredService.description}"/>

    <c:choose>
        <c:when test="${not empty mduiContext}">
            <c:if test="${not empty mduiContext.logoUrl}">
                <c:set var="registeredServiceLogo" value="${mduiContext.logoUrl}"/>
            </c:if>
            <c:set var="registeredServiceName" value="${mduiContext.displayName}"/>
            <c:set var="registeredServiceDescription" value="${mduiContext.description}"/>
        </c:when>
        <c:when test="${not empty registeredService.logo}">
            <c:set var="registeredServiceLogo" value="${registeredService.logo}"/>
        </c:when>
    </c:choose>

    <div id="serviceui" class="serviceinfo">
        <table>
            <tr>
                <td><img src="${registeredServiceLogo}"></td>
                <td id="servicedesc">
                    <h1>${fn:escapeXml(registeredServiceName)}</h1>
                    <p>${fn:escapeXml(registeredServiceDescription)}</p>
                </td>
            </tr>
        </table>
    </div>
    <p/>
</c:if>
 -->
 
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
	        	<%
					Object savedLoginContext = request.getSession().getAttribute("pwdg_savedLoginContext");
					if (savedLoginContext == null) {
				%>
				<a href="<%=path%>/login"><spring:message
						code="screen.password.reset.step.backtofirstpage" /></a>
				<%
					} else {
				%>
				<a href="<%=path%>/login?${fn:escapeXml(sessionScope.pwdg_savedLoginContext)}"><spring:message
						code="screen.password.reset.step.backtofirstpage" /></a>
				<%
					}
				%>
				&gt;
				<spring:message code="screen.personal.info.edit.title" />
			</h2>
		</c:if>
		<!-- show login page title -->
		
		<c:if test="${not empty edituserinfo}">
				<section class="row hiddenbtn">
		            <label for="domain"><spring:message code="screen.welcome.label.domain" /></label>
		            <spring:message code="screen.welcome.label.domain.accesskey" var="domainAccessKey" />
		            <div class="select">
			            <form:select id="domain" tabindex="0" accesskey="${domainAccessKey}" path="domain">
			            	<c:if test="${not empty domains}">
	  							<form:option value="${domains[0].zkDomainUrlEncoded}">${domains[0].code}</form:option>
			            	</c:if>
			            </form:select>
		            </div>
		         </section>
		         <section class="row height55">
		            <label class="notice-red padding-top20"><spring:message code="screen.personal.info.goto.edit.relogin.notice"/></label>
		          </section>
        </c:if>
        <c:if test="${empty edituserinfo}">
	        <section class="row">
		            <label for="domain"><spring:message code="screen.welcome.label.domain" /></label>
		            <spring:message code="screen.welcome.label.domain.accesskey" var="domainAccessKey" />
		            <div class="select">
			            <form:select id="domain" tabindex="0" accesskey="${domainAccessKey}" path="domain">
			            	<c:if test="${not empty domains}">
			            		<c:forEach items="${domains}" var="domain">
		  							<form:option value="${domain.zkDomainUrlEncoded}">${domain.code}</form:option>
								</c:forEach>
			            	</c:if>
			            </form:select>
		            </div>
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
            <span id="capslock-on" style="display:none;"><p><img src="images/warning.png" valign="top"> <spring:message code="screen.capslock.on" /></p></span>
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
						    		<img alt="picture" src="<%=path %>/uniauth/captcha" title="<spring:message code="screen.init.password.step1.content.verifycode.title"/>"  id="cas_login_captcha_change_img" >
									 <a  href="javascript:void(0);" tabindex="4"  id="cas_login_captcha_change_a"><spring:message code="screen.welcome.button.captcha.change"/></a>
				            </div>
				        </section>
					<%
				}
			}
		%>
		
        <!--
        <section class="row check">
            <p>
                <input id="warn" name="warn" value="true" tabindex="3" accesskey="<spring:message code="screen.welcome.label.warn.accesskey" />" type="checkbox" />
                <label for="warn"><spring:message code="screen.welcome.label.warn" /></label>
                <br/>
                <input id="publicWorkstation" name="publicWorkstation" value="false" tabindex="4" type="checkbox" />
                <label for="publicWorkstation"><spring:message code="screen.welcome.label.publicstation" /></label>
                <br/>
                <input type="checkbox" name="rememberMe" id="rememberMe" value="true" tabindex="5"  />
                <label for="rememberMe"><spring:message code="screen.rememberme.checkbox.title" /></label>
            </p>
        </section>
        -->

        <section class="row btn-row">
            <input type="hidden" name="lt" value="${loginTicket}" />
            <input type="hidden" name="execution" value="${flowExecutionKey}" />
            <input type="hidden" name="_eventId" value="submit" />

            <input class="btn-submit" name="submit" accesskey="l" value="<spring:message code="screen.welcome.button.login" />" tabindex="6" type="submit" />
            <input class="btn-reset" name="reset" accesskey="c" value="<spring:message code="screen.welcome.button.clear" />" tabindex="7" type="reset" />
            
            <c:if test="${empty edituserinfo}">
            		<div class="personal-info-link text_decoration_none">
		            	<div class="gotoedit-link">
		            		<a href="javascript:void(0);"  title="<spring:message code="screen.welcome.link.userinfo.goedit.title"/>" id="to_edit_userinfo_btn"><spring:message code="screen.welcome.link.userinfo.goedit"/></a>
		            	</div>
			            <div class="forgetpwd-link">
			            	<a href="javascript:void(0);" title="<spring:message code="screen.welcome.link.password.forget.title"/>" id="to_reset_pwd_btn"><spring:message code="screen.welcome.link.password.forget"/></a>
			            </div>
		            </div>
        	</c:if>
        </section>
    </form:form>
</div>

<div style="display:none">
	<form action="" method="post" id="hidden_post_form_for_loginurl">
		<input type="text" name="savedLoginContext" value="" id="hidden_savedLoginContext">
		<input type="text" name="form_method" value="" id="hidden_form_method">
	</form>
</div>

<div id="sidebar">
    <div class="sidebar-content cas-ad hiddenbtn" id="cas-ad-div">
    	<!-- 
        <p><spring:message code="screen.welcome.security" /></p>
        <div id="list-languages"> 
<%--             <% final String queryString = request.getQueryString() == null ? "" : request.getQueryString().replaceAll("&locale=([A-Za-z][A-Za-z]_)?[A-Za-z][A-Za-z]|^locale=([A-Za-z][A-Za-z]_)?[A-Za-z][A-Za-z]", "");%> --%>
            
<%--             <c:set var='query' value='<%=queryString%>' /> --%>
            <c:set var="xquery" value="${fn:escapeXml(query)}" />

            <h3>Languages:</h3>

            <c:choose>
                <c:when test="${not empty requestScope['isMobile'] and not empty mobileCss}">
                    <form method="get" action="login?${xquery}">
                        <select name="locale">
                            <option value="en">English</option>
                            <option value="es">Spanish</option>
                            <option value="fr">French</option>
                            <option value="ru">Russian</option>
                            <option value="nl">Nederlands</option>
                            <option value="sv">Svenska</option>
                            <option value="it">Italiano</option>
                            <option value="ur">Urdu</option>
                            <option value="zh_CN">Chinese (Simplified)</option>
                            <option value="zh_TW">Chinese (Traditional)</option>
                            <option value="de">Deutsch</option>
                            <option value="ja">Japanese</option>
                            <option value="hr">Croatian</option>
                            <option value="uk">Ukranian</option>
                            <option value="cs">Czech</option>
                            <option value="sl">Slovenian</option>
                            <option value="pl">Polish</option>
                            <option value="ca">Catalan</option>
                            <option value="mk">Macedonian</option>
                            <option value="fa">Farsi</option>
                            <option value="ar">Arabic</option>
                            <option value="pt_PT">Portuguese</option>
                            <option value="pt_BR">Portuguese (Brazil)</option>
                        </select>
                        <input type="submit" value="Switch">
                    </form>
                </c:when>
                <c:otherwise>
                    <c:set var="loginUrl" value="login?${xquery}${not empty xquery ? '&' : ''}locale=" />
                    <ul>
                        <li class="first"><a href="${loginUrl}en">English</a></li>
                        <li><a href="${loginUrl}es">Spanish</a></li>
                        <li><a href="${loginUrl}fr">French</a></li>
                        <li><a href="${loginUrl}ru">Russian</a></li>
                        <li><a href="${loginUrl}nl">Nederlands</a></li>
                        <li><a href="${loginUrl}sv">Svenska</a></li>
                        <li><a href="${loginUrl}it">Italiano</a></li>
                        <li><a href="${loginUrl}ur">Urdu</a></li>
                        <li><a href="${loginUrl}zh_CN">Chinese (Simplified)</a></li>
                        <li><a href="${loginUrl}zh_TW">Chinese (Traditional)</a></li>
                        <li><a href="${loginUrl}de">Deutsch</a></li>
                        <li><a href="${loginUrl}ja">Japanese</a></li>
                        <li><a href="${loginUrl}hr">Croatian</a></li>
                        <li><a href="${loginUrl}uk">Ukranian</a></li>
                        <li><a href="${loginUrl}cs">Czech</a></li>
                        <li><a href="${loginUrl}sl">Slovenian</a></li>
                        <li><a href="${loginUrl}ca">Catalan</a></li>
                        <li><a href="${loginUrl}mk">Macedonian</a></li>
                        <li><a href="${loginUrl}fa">Farsi</a></li>
                        <li><a href="${loginUrl}ar">Arabic</a></li>
                        <li><a href="${loginUrl}pt_PT">Portuguese</a></li>
                        <li><a href="${loginUrl}pt_BR">Portuguese (Brazil)</a></li>
                        <li class="last"><a href="${loginUrl}pl">Polish</a></li>
                    </ul>
                </c:otherwise>
            </c:choose>
        </div>
        -->
    </div>
</div>
<jsp:directive.include file="bottom.jsp" />
<script type="text/javascript" src="<%=bpath %>/js/caslogincaptcha.js" ></script>
<script type="text/javascript" src="<%=bpath %>/js/pwdfoget.js" ></script>
<script type="text/javascript" src="<%=bpath %>/js/userinfoedit.js" ></script>
