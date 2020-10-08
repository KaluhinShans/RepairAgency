<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set value='${sessionScope["user"]}' var="user"/>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <a class="navbar-brand" href="/"><fmt:message key="logo" /></a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/"><fmt:message key="home" /></a>
            </li>
            <c:if test="${user != null}">
                <c:if test="${user.isManager()}">
                <li class="nav-item">
                    <a class="nav-link" href="/usersList"><fmt:message key="users" /></a>
                </li>
                </c:if>
                <c:if test="${user.isMaster()}">
                <li class="nav-item">
                    <a class="nav-link" href="/work">Work</a>
                </li>
                </c:if>
            </c:if>
        </ul>
        <div class="dropdown ml-4">
            <button class="btn btn-dark dropdown-toggle" type="button" id="butLang" data-toggle="dropdown">
                <c:out value="${sessionScope.lang}"/>
                <span class="caret"></span></button>
            <div class="dropdown-menu" aria-labelledby="dropdownMenuButton" id="demoList">
                <a class="dropdown-item" href="?lang=En">En</a>
                <a class="dropdown-item" href="?lang=Ru">Ru</a>
                <a class="dropdown-item" href="?lang=Ua">Ua</a>
                <a class="dropdown-item" href="?lang=Ja">Ja</a>
            </div>
        </div>
        <c:choose>
            <c:when test="${user != null}">
                <a class="nav-link disabled" href="/replenish"><fmt:message key="balance" />: </a>
                <span class="navbar-text">
                    <c:out value="${user.balance}"/>
                  </span>

                <form class="form-inline my-4 my-lg-0 ml-4" action="/profile" method="get">
                    <button class="btn btn-outline-success my-2 my-sm-0" type="submit"><c:out value="${user.name}"/>
                    </button>
                </form>
            </c:when>
            <c:otherwise>
                <form class="form-inline my-2 my-lg-0 ml-4" action="/login" method="get">
                    <button class="btn btn-outline-success my-2 my-sm-0" type="submit"><fmt:message key="login" /></button>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</nav>

<script>
    $(document).ready(function () {
        $('#demoList').on('click', function () {
            location.reload();
        });
    });
</script>


