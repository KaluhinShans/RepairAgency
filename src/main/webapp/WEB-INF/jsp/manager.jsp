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
                <select name="master" class="form-control">
                    <c:choose>
                        <c:when test="${param['master'] != null && param['master'] != -1}">
                            <option selected hidden value="${param["master"]}">
                                <fmt:message key="search.selectedMaster"/>
                            </option>
                            <option value="-1"><fmt:message key="search.noneMaster"/></option>
                        </c:when>
                        <c:otherwise>
                            <option value="-1" selected><fmt:message key="search.noneMaster"/></option>
                        </c:otherwise>
                    </c:choose>
                    <c:forEach var="master" items="${masters}">
                        <option value="${master.id}">${master.fullName}
                            , ${master.email}</option>
                    </c:forEach>
                </select>

                <select name="status" class="form-control ml-2">
                    <option value="ALL"><fmt:message key="ALL"/></option>
                    <c:choose>
                        <c:when test="${param['status'] != null}">
                            <option selected hidden value="${param['status']}">
                                <fmt:message key="${param['status']}"/>
                            </option>
                            <option value="VERIFICATION"><fmt:message key="VERIFICATION"/></option>
                        </c:when>
                        <c:otherwise>
                            <option value="VERIFICATION" selected><fmt:message key="VERIFICATION"/></option>
                        </c:otherwise>
                    </c:choose>
                    <option value="PAYMENT"><fmt:message key="PAYMENT"/></option>
                    <option value="PENDING"><fmt:message key="PENDING"/></option>
                    <option value="PROCESS"><fmt:message key="PROCESS"/></option>
                    <option value="DONE"><fmt:message key="DONE"/></option>
                    <option value="REJECT"><fmt:message key="REJECT"/></option>
                </select>

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
                    <option value="date ASC"><fmt:message key="date ASC"/></option>
                    <option value="date DESC"><fmt:message key="date DESC"/></option>
                    <option value="price ASC"><fmt:message key="price ASC"/></option>
                    <option value="price DESC"><fmt:message key="price DESC"/></option>
                </select>

                <button type="submit" class="btn btn-primary ml-2"><fmt:message key="search"/></button>
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

                            <h5 class="card-title"><fmt:message key="orders.status"/>:
                                    <fmt:message key="${order.status}"/>
                                <c:if test="${!order.isReject() && !order.isDone()}">
                                <a href="/orders/reject?orderId=${order.id}"
                                   class="btn btn-outline-danger btn-sm float-right"><fmt:message
                                        key="orders.reject"/></a>
                                </c:if>
                                <h6 class="card-subtitle mb-2"><fmt:message key="orders.problem"/>: ${order.name}</h6>

                                <br>
                                <h6 class="card-subtitle mb-2"><fmt:message
                                        key="orders.description"/>: ${order.description}</h6>
                                <br>
                                    <c:set var="orderUser" value="${order.user}"/>
                                <h6 class="card-subtitle mb-2"><fmt:message key="orders.user"/>: </h6>
                                <div class="media">
                                    <img src="/images/users/${orderUser.photo}" class="align-self-start mr-3" width="42"
                                         height="42">
                                    <div class="media-body">
                                        <h5 class="mt-0">${orderUser.fullName}</h5>
                                        <fmt:message key="email"/>: ${orderUser.email}
                                    </div>
                                </div>
                                <c:if test="${order.masterId > 0}">
                                    <c:set var="orderMaster" value="${order.master}"/>
                                <br>
                                <h6 class="card-subtitle mb-2"><fmt:message key="master"/>: </h6>
                                <div class="media">
                                    <img src="/images/users/${orderMaster.photo}" class="align-self-start mr-3"
                                         width="42"
                                         height="42">
                                    <div class="media-body">
                                        <h5 class="mt-0">${orderMaster.fullName}</h5>
                                        <fmt:message key="email"/>: ${orderMaster.email}
                                    </div>
                                </div>
                                <br>
                                <h6 class="card-subtitle mb-2"><fmt:message key="orders.price"/>: ${order.price} $</h6>
                                </c:if>
                                <br>
                                <h6 class="card-subtitle mb-2 text-muted"><fmt:message
                                        key="orders.location"/>: ${order.location}</h6>
                                <h6 class="card-subtitle mb-2 text-muted"><fmt:message
                                        key="orders.date"/>: ${order.date}</h6>
                                <c:if test="${order.isNew()}">
                                <div class="dropdown-divider"></div>
                                <form action="/ordersList" method="post">
                                    <input type="hidden" name="orderId" value="${order.id}">
                                    <div class="form-group">
                                        <label><fmt:message key="master"/>: </label>
                                        <select required name="selectedMaster" class="form-control">
                                            <c:forEach var="master" items="${masters}">
                                                <option value="${master.id}">${master.fullName}
                                                    , ${master.email}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label><fmt:message key="orders.price"/>: </label>
                                        <input required type="number" name="price" class="form-control">
                                    </div>
                                    <button type="submit" class="btn btn-primary"><fmt:message
                                            key="orders.submit"/></button>
                                </form>
                                </c:if>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info" role="alert">
                <fmt:message key="orders.users.noOrders"/>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
