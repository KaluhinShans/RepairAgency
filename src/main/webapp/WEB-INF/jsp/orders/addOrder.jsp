<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<c:set value='${sessionScope["user"]}' var="user"/>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>

<html>
<head>
    <title><fmt:message key="orders.addOrder"/></title>
    <jsp:include page="../includes/headers.jsp"/>
</head>
<body>
<jsp:include page="../includes/navbar.jsp"/>
<div class="container mt-5">
    <a class="btn btn-light" href="/orders" role="button">&laquo; <fmt:message key="back"/></a>
    <br>
    <c:if test="${error != null}">
        <div class="alert alert-danger" role="alert">
            <fmt:message key="${error}"/>
        </div>
    </c:if>
    <br>
    <form action="/orders/add" method="post" accept-charset="utf-8">
        <div class="form-row">
            <div class="form-group col-md-6">
                <label for="inputEmail4"><fmt:message key="orders.problem"/></label>
                <input type="text" required class="form-control" id="inputEmail4" name="orderName">
                <small class="form-text text-muted"><fmt:message key="orders.problem.description"/></small>
            </div>
            <div class="form-group col-md-6"></div>
        </div>
        <div class="form-group">
            <label for="textarea"><fmt:message key="orders.description"/></label>
            <small class="form-text text-muted"><fmt:message key="orders.description.description"/></small>
            <textarea class="form-control" required rows="3" id="textarea" name="description"></textarea>
        </div>
        <div class="form-group">
            <label for="inputAddress"><fmt:message key="orders.location"/></label>
            <input type="text" required class="form-control" id="inputAddress" placeholder="Street, house, flat" name="address">
        </div>

        <button type="submit" class="btn btn-primary"><fmt:message key="orders.addOrder"/></button>
    </form>

</div>
</body>
</html>