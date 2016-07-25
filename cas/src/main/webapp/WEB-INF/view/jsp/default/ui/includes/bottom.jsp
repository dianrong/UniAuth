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
</div> <!-- END #content -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.dianrong.common.uniauth.cas.helper.CasCfgResourceRefreshHelper"%>       
<%@page import="com.dianrong.common.uniauth.cas.util.I18nLanguageConstantUtil"%> 
<%@page import="com.dianrong.common.uniauth.cas.util.I18nLanguageConstantUtil.I18nContent"%>       
<footer>
    <div id="copyright">
        <p><%=CasCfgResourceRefreshHelper.instance.getImageCacheDto("CAS_ALL_RIGHT")==null?"":CasCfgResourceRefreshHelper.instance.getImageCacheDto("CAS_ALL_RIGHT").getValue() %></p>
    </div>
    <div class="i18n-div">
    	<%
    	for(I18nContent i18n: I18nLanguageConstantUtil.getAllI18nLanguages()) {
    	    %>
    	    <span class="<%=i18n.isSelected()?"selected":"normal" %>">
    			<a onclick="<%=i18n.isSelected()?"#":"javascrpt:i18nset('"+i18n.getLocaleStr()+"');"%>"><%=i18n.getLanguage()%></a>
    		</span>
    	    <%
    	}
    	%>
    </div>
</footer>

</div> <!-- END #container -->
<spring:theme code="head.javascript.file" var="headJavascriptFile" text="" />
<script type="text/javascript" src="<c:url value="${headJavascriptFile}" />"></script>
<spring:theme code="cas.javascript.file" var="casJavascriptFile" text="" />
<script type="text/javascript" src="<c:url value="${casJavascriptFile}" />"></script>
</body>
</html>

