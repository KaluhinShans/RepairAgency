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
    <title><fmt:message key="profile"/></title>
    <jsp:include page="../includes/headers.jsp"/>
</head>
<body>
<jsp:include page="../includes/navbar.jsp"/>
<div class="container mt-5">

    <c:if test="${message != null}">
    <div class="alert alert-success" role="alert">
        <fmt:message key="${message}"/>
    </div>
    </c:if>
    <c:if test="${error != null}">
    <div class="alert alert-danger" role="alert">
        <fmt:message key="${error}"/>
    </div>
    </c:if>

    <div class="container-fluid well span6">
        <div class="container emp-profile">

            <div class="row">
                <div class="col-md-4">
                    <div class="card" style="width: 12rem;">
                        <img src="/images/users/<%=user.getPhoto()%>?t=<%=System.currentTimeMillis()%>"
                             class="card-img-top"/>
                    </div>
                    <div class="my-4">

                        <div class="profile-work">
                            <form action="/logout" method="get">
                                <button type="submit" class="btn btn-primary"><fmt:message key="signOut"/></button>
                            </form>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="profile-head">
                        <h5>
                            <%=user.getFullName()%>
                        </h5>
                        <h6>
                            Customer
                        </h6>
                        <p class="proile-rating">RANKINGS : <span>8/10</span></p>
                        <ul class="nav nav-tabs" id="myTab" role="tablist">
                            <li class="nav-item" role="presentation">
                                <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab"
                                   aria-controls="home" aria-selected="true"><fmt:message key="profile"/></a>
                            </li>
                        </ul>

                    </div>
                    <br>
                    <div class="col-md-8">
                        <div class="tab-content profile-tab" id="myTabContent">
                            <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">

                                <div class="row">
                                    <div class="col-md-6">
                                        <label><fmt:message key="name"/></label>
                                    </div>
                                    <div class="col-md-6">
                                        <p><%=user.getName()%>
                                        </p>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6">
                                        <label><fmt:message key="surname"/></label>
                                    </div>
                                    <div class="col-md-6">
                                        <p><%=user.getLastName()%>
                                        </p>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6">
                                        <label><fmt:message key="email"/>
                                            <%if (user.getActivationCode() == null) { %>
                                            <label class="fa fa-check-circle-o"></label>
                                            <%}%>
                                        </label>
                                    </div>
                                    <div class="col-md-6">
                                        <p><%=user.getEmail()%></p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-2">
                    <form class="form-inline my-4 my-lg-0 ml-4" action="/profile/edit" method="get">
                        <button type="submit" class="btn btn-primary"><fmt:message key="editProfile"/></button>
                    </form>
                </div>

            </div>
            <br><br><br>
            <hr>
            <div class="list-group">
                <button type="button" class="list-group-item list-group-item-action active">
                    Transactions
                </button>
                <c:forEach var="transaction" items="${transactions}">
                    <button type="button" class="list-group-item list-group-item-action">
                            ${transaction.date}:
                        Transaction #${transaction.id},
                        By: ${transaction.card}

                        <div class="float-right">
                            Amount: ${transaction.amount},
                            Reminder: ${transaction.reminder}
                        </div>
                    </button>
                </c:forEach>

            </div>
            <br>
            <jsp:include page="../includes/pagination.jsp"/>
        </div>


    </div>

</body>
</html>
