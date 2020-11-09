<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>
<html>
<head>
    <title><fmt:message key="error404"/></title>
    <jsp:include page="WEB-INF/jsp/includes/headers.jsp"/>
</head>
<body>
<jsp:include page="WEB-INF/jsp/includes/navbar.jsp"/>
<div style="text-align: center;">

    <img src="/images/error404.gif"
         alt=""/>

</div>
</body>
</html>
