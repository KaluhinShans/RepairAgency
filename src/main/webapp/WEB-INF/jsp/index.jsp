<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false"%>

<c:set value='${sessionScope["user"]}' var="user"/>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>

<html>
<head>
    <title><fmt:message key="logo"/></title>
    <jsp:include page="./includes/headers.jsp"/>
</head>
<body>
<jsp:include page="./includes/navbar.jsp"/>

<main role="main">

    <div class="jumbotron">
        <div class="container">
            <h1 class="display-3"><fmt:message key="index.helloEpam"/></h1>
            <p><fmt:message key="index.thisIsSimple"/></p>
            <p><a class="btn btn-primary btn-lg" href="/orders" role="button"><fmt:message key="index.letsGo"/> &raquo;</a></p>
        </div>
    </div>

    <div class="container">
        <div class="row">
            <div class="col-md-4">
                <h2><fmt:message key="index.TelegramBot"/></h2>
                <p><fmt:message key="index.TelegramBot.description"/> </p>
                <p><a class="btn btn-secondary" href="https://t.me/Introduceme_bot" role="button"><fmt:message key="index.chat"/>  &raquo;</a></p>
            </div>
            <div class="col-md-4">
                <h2>Linked In</h2>
                <p><fmt:message key="index.LinkedIn.description"/> &#128522;</p>
                <p><a class="btn btn-secondary" href="https://www.linkedin.com/in/kaluhinshans/" role="button"><fmt:message key="profile"/>  &raquo;</a></p>
            </div>
            <div class="col-md-4">
                <h2>Git Hub</h2>
                <p<fmt:message key="index.GitHub.description"/> </p>
                <p><a class="btn btn-secondary" href="https://github.com/MadgelS" role="button"><fmt:message key="index.link"/>  &raquo;</a></p>
            </div>
        </div>

        <hr>

    </div>
</main>

<footer class="container">
    <p>&copy; Kaluhin Shans 2020</p>
</footer>
</body>
</html>