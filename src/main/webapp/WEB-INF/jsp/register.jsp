<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.lang}">
<head>
    <title><fmt:message key="registration"/></title>
    <jsp:include page="./includes/headers.jsp"/>
    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
</head>
<body>
<jsp:include page="./includes/navbar.jsp"/>
<div class="container mt-5">
    <c:if test="${error != null}">
        <div class="alert alert-danger" role="alert">
            <fmt:message key="${error}"/>
        </div>
    </c:if>

    <form action="/register" method="post">
        <div class="form-group">
            <label for="InputEmail"><fmt:message key="emailAddress"/></label>
            <input type="email" class="form-control" id="InputEmail" name="email"
                   aria-describedby="emailHelp" placeholder="<fmt:message key="enterEmail"/>">
            <small id="emailHelp" class="form-text text-muted"><fmt:message key="neverShare"/></small>
        </div>
        <div class="form-group">
            <label for="InputPassword"><fmt:message key="password"/></label>
            <input type="password" class="form-control" id="InputPassword" name="password"
                   placeholder="<fmt:message key="enterPassword"/>">
        </div>
        <div class="form-group">
            <label for="name"><fmt:message key="name"/></label>
            <input type="text" class="form-control" id="name"  name="name"
                   placeholder="<fmt:message key="enterName"/>">
        </div>
        <div class="form-group">
            <label for="lastName"><fmt:message key="surname"/></label>
            <input type="text" class="form-control" id="lastName" name="lastName"
                   placeholder="<fmt:message key="enterSurname"/>">
        </div>
        <div class="g-recaptcha" data-sitekey="6Le077sZAAAAAIeMmf4scOUpty-44ymjx8yMb39t"></div>
        <br>
        <a href="/login"><fmt:message key="orLogin"/></a><br>
        <div class="col-sm-6 mt-3">
            <button type="submit" class="btn btn-primary"><fmt:message key="registration"/></button>
        </div>
    </form>
</div>
</body>
</html>
