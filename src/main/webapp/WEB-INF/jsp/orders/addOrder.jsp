<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<c:set value='${sessionScope["user"]}' var="user"/>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>

<html>
<head>
    <title><fmt:message key="addOrder"/></title>
    <jsp:include page="../includes/headers.jsp"/>
</head>
<body>
<jsp:include page="../includes/navbar.jsp"/>
<div class="container mt-5">
    <a class="btn btn-light" href="/orders" role="button">&laquo; Back</a>
    <br>
    <br>
    <form action="/orders/add" method="post">
        <div class="form-row">
            <div class="form-group col-md-6">
                <label for="inputEmail4">Problem</label>
                <input type="text" required class="form-control" id="inputEmail4" name="orderName">
                <small class="form-text text-muted"><fmt:message key="neverShare"/></small>
            </div>
            <div class="form-group col-md-6"></div>
        </div>
        <div class="form-group">
            <label for="textarea">Description</label>
            <small class="form-text text-muted"><fmt:message key="neverShare"/></small>
            <textarea class="form-control" required rows="3" id="textarea" name="description"></textarea>
        </div>
        <div class="form-group">
            <label for="inputAddress">Address</label>
            <input type="text" required class="form-control" id="inputAddress" placeholder="Street, house, flat" name="address">
            <small class="form-text text-muted"><fmt:message key="neverShare"/></small>
        </div>

        <button type="submit" class="btn btn-primary">Add</button>
    </form>

</div>
</body>
</html>