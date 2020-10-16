<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false"%>

<c:set value='${sessionScope["user"]}' var="user"/>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>

<html>
<head>
    <title><fmt:message key="home"/></title>
    <jsp:include page="./includes/headers.jsp"/>
</head>
<body>
<jsp:include page="./includes/navbar.jsp"/>

<main role="main">

    <div class="jumbotron">
        <div class="container">
            <h1 class="display-3">Hello, Epam!</h1>
            <p>This is a simple site for my final project in Epam.</p>
            <p><a class="btn btn-primary btn-lg" href="/orders" role="button">Lets go &raquo;</a></p>
        </div>
    </div>

    <div class="container">
        <div class="row">
            <div class="col-md-4">
                <h2>Telegram Bot</h2>
                <p>My telegram bot for introduce me. You can leave me message, download CV, and other. I have implemented a GitHub parser, so you can test it. </p>
                <p><a class="btn btn-secondary" href="https://t.me/Introduceme_bot" role="button">Chat  &raquo;</a></p>
            </div>
            <div class="col-md-4">
                <h2>Linked In</h2>
                <p>I'ts just my Linked In profile. Add me to contacts &#128522;</p>
                <p><a class="btn btn-secondary" href="https://www.linkedin.com/in/kaluhinshans/" role="button">Profile  &raquo;</a></p>
            </div>
            <div class="col-md-4">
                <h2>Git Hub</h2>
                <p>I'ts just my github profile. </p>
                <p><a class="btn btn-secondary" href="https://github.com/MadgelS" role="button">Link  &raquo;</a></p>
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