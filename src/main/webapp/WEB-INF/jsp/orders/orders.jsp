<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<c:set value='${sessionScope["user"]}' var="user"/>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>

<html>
<head>
    <title><fmt:message key="orders"/></title>
    <jsp:include page="../includes/headers.jsp"/>
</head>
<body>
<jsp:include page="../includes/navbar.jsp"/>
<div class="container mt-5">
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/orders/add" class="form-inline">
                <button class="btn btn-primary ml-2"><fmt:message key="addOrder"/></button>
            </form>
        </div>
    </div>

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
    
    <jsp:include page="../includes/pagination.jsp"/>
    <c:choose>
        <c:when test="${orders != null}">
            <div class="card-deck">
                <c:forEach var="order" items="${orders}">
                    <div class="card my-3">
                        <div class="card-body">
                            <h5 class="card-title">Problem: ${order.name}
                                <c:if test="${!order.isReject() && !order.isDone()}">
                                    <a href="/orders/reject?orderId=${order.id}"
                                       class="btn btn-outline-danger btn-sm float-right">Reject</a>
                                </c:if>
                            </h5>
                            <h6 class="card-subtitle mb-2">Status: ${order.status}</h6>
                            <br>
                            <h6 class="card-subtitle mb-2">Description: ${order.description}</h6>
                            <br>
                            <c:if test="${order.masterId > 0}">
                                <c:set var="orderMaster" value="${order.master}"/>
                                <br>
                                <h6 class="card-subtitle mb-2">Master: </h6>
                                <div class="media">
                                    <img src="/images/users/${orderMaster.photo}" class="align-self-start mr-3"
                                         width="42"
                                         height="42">
                                    <div class="media-body">
                                        <h5 class="mt-0">${orderMaster.fullName}</h5>
                                        Email: ${orderMaster.email}
                                    </div>
                                </div>
                                <br>
                            </c:if>
                            <h6 class="card-subtitle mb-2 text-muted">Location: ${order.location}</h6>
                            <h6 class="card-subtitle mb-2 text-muted">Date: ${order.date}</h6>
                            <div class="dropdown-divider"></div>
                            <c:if test="${order.price > 0}">
                                <br>
                                <h6 class="card-subtitle mb-2">Price: ${order.price}</h6>
                            </c:if>
                            <c:if test="${order.isPayment()}">
                                <form action="/payment" method="get">
                                    <input type="hidden" name="orderId" value="${order.id}">
                                    <button type="submit" class="btn btn-primary">Pay</button>
                                </form>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info" role="alert">
                You have no orders
            </div>
        </c:otherwise>
    </c:choose>

</div>
</body>
</html>

