<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://shans.su" prefix="shans" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>
<html>
<head>
    <title><fmt:message key="comments"/></title>
    <jsp:include page="../includes/headers.jsp"/>
</head>
<body>
<jsp:include page="../includes/navbar.jsp"/>
<div class="container mt-5">
    <jsp:include page="../includes/pagination.jsp"/>
    <h2><fmt:message key="master"/>:</h2>
    <br>

    <div class="media">
        <img src="/images/users/${master.photo}" class="align-self-start mr-3"
             width="64"
             height="64">
        <div class="media-body">
            <h5 class="mt-0">${master.fullName}</h5>
            <fmt:message key="email"/>: ${master.email}
        </div>
    </div>

    <br>
    <h3><fmt:message key="comments"/>:</h3>
    <c:choose>
        <c:when test="${comments != null && !comments.isEmpty()}">
            <c:forEach var="comment" items="${comments}">
                <c:set var="user" value="${comment.user}"/>
                <div class="card">
                    <h5 class="card-header">
                        <shans:rateTag rate="${comment.rate}"> </shans:rateTag>
                    </h5>
                    <div class="card-body">
                        <div class="media">
                            <img src="/images/users/${user.photo}" class="align-self-start mr-3" width="64"
                                 height="64">
                            <div class="media-body">
                                <h5 class="mt-0">${user.fullName}</h5>
                                <fmt:message key="rate.comment"/>: ${comment.description}
                            </div>
                        </div>
                    </div>
                </div>
                <br>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info" role="alert">
                <fmt:message key="rate.noOneComment"/>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</div>
</body>
</html>
