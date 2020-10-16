<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>

<html>
<head>
    <title><fmt:message key="manager"/></title>
    <jsp:include page="./includes/headers.jsp"/>
</head>
<body>
<jsp:include page="./includes/navbar.jsp"/>
<div class="container mt-5">

    <div class="form-row">
        <div class="form-group col-md-8">
            <form method="get" action="/ordersList" class="form-inline">
                <select required name="searchByMaster" class="form-control">
                    <c:choose>
                        <c:when test="${param['searchByMaster'] != null && param['searchByMaster'] != -1}">
                            <option value="${param["searchByMaster"]}" selected hidden>Selected master</option>
                            <option value="-1">None master</option>
                        </c:when>
                        <c:otherwise>
                            <option value="-1" selected>None master</option>
                        </c:otherwise>
                    </c:choose>
                    <c:forEach var="master" items="${masters}">
                        <option value="${master.id}">${master.fullName}
                            , ${master.email}</option>
                    </c:forEach>
                </select>

                <select required name="searchByStatus" class="form-control ml-2">
                    <option>ALL</option>
                    <c:choose>
                        <c:when test="${param['searchByStatus'] != null}">
                            <option selected hidden>${param["searchByStatus"]}</option>
                            <option>VERIFICATION</option>
                        </c:when>
                        <c:otherwise>
                            <option selected>VERIFICATION</option>
                        </c:otherwise>
                    </c:choose>
                    <option>PAYMENT</option>
                    <option>PENDING</option>
                    <option>PROCESS</option>
                    <option>DONE</option>
                    <option>REJECT</option>
                </select>

                <button type="submit" class="btn btn-primary ml-2">Search</button>
            </form>
        </div>
    </div>

    <jsp:include page="./includes/pagination.jsp"/>
    <c:choose>
        <c:when test="${orders != null && !orders.isEmpty()}">
            <div class="card-deck">
                <c:forEach var="order" items="${orders}">
                    <div class="card my-3">
                        <div class="card-body ">

                            <h5 class="card-title">Status: ${order.status}
                                <c:if test="${!order.isReject() && !order.isDone()}">
                                <a href="/orders/reject?orderId=${order.id}"
                                   class="btn btn-outline-danger btn-sm float-right">Reject</a>
                                </c:if>
                            <h6 class="card-subtitle mb-2">Problem: ${order.name}</h6>

                            <br>
                            <h6 class="card-subtitle mb-2">Description: ${order.description}</h6>
                            <br>
                            <c:set var="orderUser" value="${order.user}"/>
                            <h6 class="card-subtitle mb-2">User: </h6>
                            <div class="media">
                                <img src="/images/users/${orderUser.photo}" class="align-self-start mr-3" width="42"
                                     height="42">
                                <div class="media-body">
                                    <h5 class="mt-0">${orderUser.fullName}</h5>
                                    Email: ${orderUser.email}
                                </div>
                            </div>
                            <c:if test="${order.masterId > 0}">
                                <c:set var="orderMaster" value="${order.master}"/>
                                <br>
                                <h6 class="card-subtitle mb-2">Master: </h6>
                                <div class="media">
                                    <img src="/images/users/${orderMaster.photo}" class="align-self-start mr-3" width="42"
                                         height="42">
                                    <div class="media-body">
                                        <h5 class="mt-0">${orderMaster.fullName}</h5>
                                        Email: ${orderMaster.email}
                                    </div>
                                </div>
                                <br>
                                <h6 class="card-subtitle mb-2">Price: ${order.price} $</h6>
                            </c:if>
                            <br>
                            <h6 class="card-subtitle mb-2 text-muted">Location: ${order.location}</h6>
                            <h6 class="card-subtitle mb-2 text-muted">Date: ${order.date}</h6>
                            <c:if test="${order.isNew()}">
                                <div class="dropdown-divider"></div>
                                <form action="/ordersList" method="post">
                                    <input type="hidden" name="orderId" value="${order.id}">
                                    <div class="form-group">
                                        <label>Master: </label>
                                        <select required name="selectedMaster" class="form-control">
                                            <c:forEach var="master" items="${masters}">
                                                <option value="${master.id}">${master.fullName}
                                                    , ${master.email}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label>Price: </label>
                                        <input required type="number" name="price" class="form-control">
                                    </div>
                                    <button type="submit" class="btn btn-primary">Submit</button>
                                </form>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info" role="alert">
                Users have no orders
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
