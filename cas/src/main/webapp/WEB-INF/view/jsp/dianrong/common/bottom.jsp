</div><!-- end of content -->
<%@page import="com.dianrong.common.uniauth.cas.helper.CasCfgResourceRefreshHelper"%>
<%@page import="com.dianrong.common.uniauth.cas.util.I18nLanguageConstantUtil"%> 
<%@page import="com.dianrong.common.uniauth.cas.util.I18nLanguageConstantUtil.I18nContent"%>   
<footer class="footer">
    <div>
		<p><%=CasCfgResourceRefreshHelper.instance.getImageCacheDto("CAS_ALL_RIGHT") == null? "":CasCfgResourceRefreshHelper.instance.getImageCacheDto("CAS_ALL_RIGHT").getValue() %></p>
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
</body>
<script type="text/javascript" src="jquery/jquery-1.12.1.min.js" ></script>
<script type="text/javascript" src="jquery/jquery.i18n.properties-min-1.0.9.js" ></script>
<script type="text/javascript" src="bootstrap-3.3.5/js/bootstrap.min.js" ></script>
<script type="text/javascript" src="js/common.js" ></script>
<script type="text/javascript" src="js/pwdforget.js" ></script>
<script type="text/javascript" src="js/userinfoedit.js" ></script>
<script type="text/javascript" src="js/initpwd.js" ></script>
</html>

