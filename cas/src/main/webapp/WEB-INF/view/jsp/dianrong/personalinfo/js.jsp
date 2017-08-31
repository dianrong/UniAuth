<%
String js_path = request.getContextPath(); 
String js_version = (String)application.getAttribute("cas_v");
%>   
<script type="text/javascript" src="<%=js_path %>/js/userinfoedit.js?v=<%=js_version %>" ></script>
<div class="hidden-element" role="alert" id="window_notice_div">
  <div id="top_show_info" class="showinfo"></div>
</div>