<%@ page import="com.shans.kaluhin.entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<%User user = (User) request.getSession(false).getAttribute("user");%>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>

<html>
<head>
    <title><fmt:message key="users"/></title>
    <jsp:include page="./includes/headers.jsp"/>
</head>
<body>
<jsp:include page="./includes/navbar.jsp"/>

<div class="container mt-5">

    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/usersList" class="form-inline">
                <input type="email" name="email" class="form-control"
                       placeholder="<fmt:message key="search.byEmail"/>">

                <select name="sort" class="form-control ml-2">
                    <c:choose>
                        <c:when test="${param['sort'] != null}">
                            <option selected hidden value="${param['sort']}">
                                <fmt:message key="${param['sort']}"/>
                            </option>
                            <option value="none"> <fmt:message key="none"/></option>
                        </c:when>
                        <c:otherwise>
                            <option value="none" selected><fmt:message key="none"/></option>
                        </c:otherwise>
                    </c:choose>
                    <option value="name ASC"><fmt:message key="name ASC"/></option>
                    <option value="name DESC"><fmt:message key="name DESC"/></option>
                    <option value="balance ASC"><fmt:message key="balance ASC"/></option>
                    <option value="balance DESC"><fmt:message key="balance DESC"/></option>
                </select>

                <button type="submit" class="btn btn-primary ml-2"><fmt:message key="search"/></button>
            </form>
        </div>
    </div>

    <jsp:include page="./includes/pagination.jsp"/>

    <c:if test="${users.size() <= 1}">
        <a class="btn btn-light" href="/usersList" role="button">&laquo; <fmt:message key="back"/></a>
        <br>
        <br>
    </c:if>
    <ul class="list-group">
        <c:choose>
            <c:when test="${!users.isEmpty()}">
                <c:forEach var="user" items="${users}">
                    <li class="list-group-item">
                        <div class="media">
                            <img src="/images/users/${user.photo}" class="align-self-start mr-3" width="64" height="64">
                            <div class="media-body">
                                <h5 class="mt-0">${user.fullName}
                                    <a type="button" class="btn btn-outline-success btn-sm float-right"
                                       data-toggle="modal" data-target="#replenishUser${user.id}"><fmt:message key="balance.replenish"/> <fmt:message key="balance"/></a>
                                </h5>
                                <fmt:message key="email"/>: ${user.email} <br>
                                <fmt:message key="roles"/>: ${user.roles} <br>
                                <fmt:message key="balance"/>: ${user.balance} $
                                <div class="float-right">
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="checkbox" id="inlineCheckbox2"
                                               onclick="changeRole(${user.id}, ${user.isManager()}, 'MANAGER')"
                                               <c:if test="${user.isManager()}">checked</c:if>>
                                        <label class="form-check-label" for="inlineCheckbox2"><fmt:message key="manager"/></label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="checkbox" id="inlineCheckbox3"
                                               onclick="changeRole(${user.id}, ${user.isMaster()}, 'MASTER')"
                                               <c:if test="${user.isMaster()}">checked</c:if>>
                                        <label class="form-check-label" for="inlineCheckbox3"><fmt:message key="master"/></label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </li>

                    <!-- MODAL -->
                    <div class="modal fade bd-example-modal-sm" id="replenishUser${user.id}" tabindex="-1" role="dialog"
                         aria-labelledby="exampleModalCenterTitle">
                        <div class="modal-dialog modal-sm" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title"><fmt:message key="balance.paymentInfo"/>:</h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-body">
                                    <form action="/usersList/replenishUser" method="post">
                                        <input type="hidden" name="userId" value="${user.id}">
                                        <div class="input-wrapper">
                                            <input class="form-control input-with-icon" type="number" placeholder="Sum" name="sum" >
                                            <label class="fa fa-dollar input-icon"></label>
                                        </div>
                                        <hr>
                                        <div class="modal-footer justify-content-center">
                                            <button type="submit" class="btn btn-primary"><fmt:message key="balance.replenish"/></button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info" role="alert">
                    <fmt:message key="search.noOneUser"/>
                </div>
            </c:otherwise>
        </c:choose>

    </ul>
    <br>
</div>

</body>
</html>

<script>
    function changeRole(id, hasRole, role) {
        $.post("/usersList", {hasRole: hasRole, id: id, role: role}, function(response) {
            scrollPosition = [window.scrollX, window.scrollY]
            Turbolinks.visit(window.location.toString(), { action: 'replace' })
        });
    }
</script>
