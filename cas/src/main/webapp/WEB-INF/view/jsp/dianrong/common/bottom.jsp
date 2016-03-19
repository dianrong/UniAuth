</div><!-- end of content -->

<footer class="footer">
    <div>
        <p><spring:message code="copyright" /></p>
        <!-- <p>Powered by <a href="http://www.apereo.org/cas">Apereo Central Authentication Service <%=org.jasig.cas.CasVersion.getVersion()%></a></p> -->
    </div>
</footer>
</body>
<%
	String bpath = request.getContextPath();
%>
<script type="text/javascript" src="<%=bpath %>/jquery/jquery-1.12.1.min.js" ></script>
<script type="text/javascript" src="<%=bpath %>/js/pwdfoget.js" ></script>
<script type="text/javascript" src="<%=bpath %>/js/userinfoedit.js" ></script>
<script type="text/javascript" src="<%=bpath %>/bootstrap-3.3.5/js/bootstrap.min.js" ></script>

</html>

