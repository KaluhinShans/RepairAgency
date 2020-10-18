<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>

<html>
<head>
    <title><fmt:message key="master"/></title>
    <jsp:include page="./includes/headers.jsp"/>
</head>
<body>
<jsp:include page="./includes/navbar.jsp"/>
<div class="container mt-5">
    <jsp:include page="./includes/pagination.jsp"/>
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/master" class="form-inline">
                <select required name="searchByStatus" class="form-control ml-2">
                    <option value="ALL"><fmt:message key="ALL"/></option>
                    <c:choose>
                        <c:when test="${param['searchByStatus'] != null}">
                            <option selected hidden value="${param['searchByStatus']}"><fmt:message key="${param['searchByStatus']}"/></option>
                            <option value="PENDING"><fmt:message key="PENDING"/></option>
                        </c:when>
                        <c:otherwise>
                            <option selected value="PENDING"><fmt:message key="PENDING"/></option>
                        </c:otherwise>
                    </c:choose>
                    <option value="PROCESS"><fmt:message key="PROCESS"/></option>
                    <option value="DONE"><fmt:message key="DONE"/></option>
                    <option value="PAYMENT"><fmt:message key="PAYMENT"/></option>
                    <option value="REJECT"><fmt:message key="REJECT"/></option>
                </select>

                <button type="submit" class="btn btn-primary ml-2"><fmt:message key="search"/></button>
            </form>
        </div>
    </div>
    <jsp:include page="./includes/pagination.jsp"/>

    <c:choose>
    <c:when test="${orders != null}">
    <div class="card-deck">
        <c:forEach var="order" items="${orders}">
            <div class="card my-3">
                <div class="card-body ">
                    <h5 class="card-title"><fmt:message key="orders.problem"/>: ${order.name}
                        <c:if test="${!order.isReject() && !order.isDone()}">
                        <a href="/orders/reject?orderId=${order.id}"
                           class="btn btn-outline-danger btn-sm float-right"><fmt:message key="orders.reject"/></a>
                        </c:if>
                        <h6 class="card-subtitle mb-2"><fmt:message key="orders.status"/>: ${order.status}</h6>
                        <br>
                        <h6 class="card-subtitle mb-2"><fmt:message key="orders.description"/>: ${order.description}</h6>
                        <br>
                        <c:if test="${order.userId > 0}">
                            <c:set var="orderUser" value="${order.user}"/>
                        <br>
                        <h6 class="card-subtitle mb-2"><fmt:message key="orders.user"/>: </h6>
                        <div class="media">
                            <img src="/images/users/${orderUser.photo}" class="align-self-start mr-3" width="42"
                                 height="42">
                            <div class="media-body">
                                <h5 class="mt-0">${orderUser.fullName}</h5>
                                <fmt:message key="email"/>: ${orderUser.email}
                            </div>
                        </div>
                        <br>
                        </c:if>
                        <h6 class="card-subtitle mb-2 text-muted"><fmt:message key="orders.location"/>: ${order.location}</h6>
                        <h6 class="card-subtitle mb-2 text-muted"><fmt:message key="orders.date"/>: ${order.date}</h6>
                        <div class="dropdown-divider"></div>
                        <c:if test="${order.price > 0}">
                        <br>
                        <h6 class="card-subtitle mb-2"><fmt:message key="orders.price"/>: ${order.price}</h6>
                        </c:if>
                        <c:if test="${order.isPending()}">
                        <form method="post" action="/master">
                            <input type="hidden" name="orderId" value="${order.id}">
                            <button type="submit" class="btn btn-primary"><fmt:message key="orders.start"/></button>
                        </form>
                        </c:if>
                        <c:if test="${order.isProcess()}">
                        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#askMaster">
                            <fmt:message key="orders.finish"/>
                        </button>

                        <!-- Modal -->
                        <div class="modal fade" id="askMaster" tabindex="-1" role="dialog"
                             aria-labelledby="exampleModalLabel" aria-hidden="true">
                            <div class="modal-dialog" role="document">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="exampleModalLabel"><fmt:message key="orders.areYouSure"/></h5>
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <div class="modal-body">
                                        <fmt:message key="orders.areYouSure.description"/>
                                        <hr>
                                        <form method="post" action="/master/finish">
                                            <input type="hidden" name="orderId" value="${order.id}">
                                            <button type="button" class="btn btn-secondary float-left"
                                                    data-dismiss="modal"><fmt:message key="orders.close"/>
                                            </button>
                                            <button type="submit" class="btn btn-primary float-right"><fmt:message key="orders.finish"/></button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>

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

</body>
</html>
