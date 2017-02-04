</div> <!-- END #content -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.dianrong.common.uniauth.cas.helper.CasCfgResourceRefreshHelper"%>       
<%@page import="com.dianrong.common.uniauth.cas.util.I18nLanguageConstantUtil"%> 
<%@page import="com.dianrong.common.uniauth.cas.util.I18nLanguageConstantUtil.I18nContent"%>       
<jsp:directive.include file="/WEB-INF/view/jsp/dianrong/common/version.jsp" />
<%
String bpath = request.getContextPath(); 
String bversion = (String)application.getAttribute("cas_v");
%>   
<footer>
    <div id="copyright">
        <p><%=CasCfgResourceRefreshHelper.INSTANCE.getImageCacheDto("CAS_ALL_RIGHT")==null?"":CasCfgResourceRefreshHelper.INSTANCE.getImageCacheDto("CAS_ALL_RIGHT").getValue() %></p>
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
<script type="text/javascript" src="<%=bpath %>/jquery/jquery-1.12.1.min.js?v=<%=bversion %>" ></script>
<script type="text/javascript" src="jquery/jquery.i18n.properties-min-1.0.9.js?v=<%=bversion %>" ></script>
<script type="text/javascript" src="https://cdn.bootcss.com/jqueryui/1.11.4/jquery-ui.min.js?v=<%=bversion %>" ></script>
<script type="text/javascript" src="https://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js?v=<%=bversion %>" ></script>

<script type="text/javascript" src="<%=bpath %>/js/common.js?v=<%=bversion %>" ></script>
<script type="text/javascript" src="<%=bpath %>/js/head.min.js?v=<%=bversion %>" ></script>
<script type="text/javascript" src="<%=bpath %>/js/cas.js?v=<%=bversion %>" ></script>
</body>
</html>

