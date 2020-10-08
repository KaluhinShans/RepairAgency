<%@ page import="com.shans.kaluhin.entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false"%>

<%User user = (User) request.getSession(false).getAttribute("user");%>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>
<html lang="${sessionScope.lang}">
<head>
    <title><fmt:message key="users"/></title>
    <jsp:include page="./includes/headers.jsp"/>
</head>
<body>
<jsp:include page="./includes/navbar.jsp"/>
<%if( user == null || !user.isManager()){%>
<jsp:forward page="/"></jsp:forward>
<%}%>


<div class="container mt-5">
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="post" action="/filter" class="form-inline">
                <input type="text" name="filter" class="form-control"
                       placeholder="Search by email">
                <button type="submit" class="btn btn-primary ml-2">Search</button>
            </form>
        </div>
    </div>

    <ul class="list-group">
        <c:forEach var="user" items="${users}">
            <li class="list-group-item">${user.email}, ${user.fullName}, Roles: ${user.roles}
                <button type="submit" class="btn btn-primary">Give role</button>
            </li>
        </c:forEach>

    </ul>
</div>
</body>
</html>
