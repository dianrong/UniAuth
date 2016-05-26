<%@ page session="false" contentType="application/json; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
{
<c:if test="${status != null}">"status": ${status},</c:if> "content":{"result": "${actorId}"<c:if test="${needCaptcha != null}">, "needCaptcha": ${needCaptcha}</c:if>},
"result":"${result}", "errors": [<c:if test="${errorKey != null}">"<spring:message code="error.${errorKey}"/>"</c:if>]
<c:if test="${serviceTicket != null}">, "serviceTicket": "${serviceTicket}"</c:if>
<c:if test="${service != null}">, "service": "${service}"</c:if>
<c:if test="${code != null}">, "code": ${code}</c:if>
}
