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
    <jsp:include page="./includes/pagination.jsp"/>
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/master" class="form-inline">
                <select required name="searchByStatus" class="form-control ml-2">
                    <option>ALL</option>
                    <c:choose>
                        <c:when test="${param['searchByStatus'] != null}">
                            <option selected hidden>${param["searchByStatus"]}</option>
                            <option>PENDING</option>
                        </c:when>
                        <c:otherwise>
                            <option selected>PENDING</option>
                        </c:otherwise>
                    </c:choose>
                    <option>PROCESS</option>
                    <option>DONE</option>
                    <option>PAYMENT</option>
                    <option>REJECT</option>
                </select>

                <button type="submit" class="btn btn-primary ml-2">Search</button>
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
                    <h5 class="card-title">Problem: ${order.name}
                        <c:if test="${!order.isReject() && !order.isDone()}">
                        <a href="/orders/reject?orderId=${order.id}"
                           class="btn btn-outline-danger btn-sm float-right">Reject</a>
                        </c:if>
                        <h6 class="card-subtitle mb-2">Status: ${order.status}</h6>
                        <br>
                        <h6 class="card-subtitle mb-2">Description: ${order.description}</h6>
                        <br>
                        <c:if test="${order.userId > 0}">
                            <c:set var="orderUser" value="${order.user}"/>
                        <br>
                        <h6 class="card-subtitle mb-2">User: </h6>
                        <div class="media">
                            <img src="/images/users/${orderUser.photo}" class="align-self-start mr-3" width="42"
                                 height="42">
                            <div class="media-body">
                                <h5 class="mt-0">${orderUser.fullName}</h5>
                                Email: ${orderUser.email}
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
                        <c:if test="${order.isPending()}">
                        <form method="post" action="/master">
                            <input type="hidden" name="orderId" value="${order.id}">
                            <button type="submit" class="btn btn-primary">Start work</button>
                        </form>
                        </c:if>
                        <c:if test="${order.isProcess()}">
                        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#askMaster">
                            Finish
                        </button>

                        <!-- Modal -->
                        <div class="modal fade" id="askMaster" tabindex="-1" role="dialog"
                             aria-labelledby="exampleModalLabel" aria-hidden="true">
                            <div class="modal-dialog" role="document">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="exampleModalLabel">Are you sure?</h5>
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <div class="modal-body">
                                        Are you sure want to finish this order?
                                        <hr>
                                        <form method="post" action="/master/finish">
                                            <input type="hidden" name="orderId" value="${order.id}">
                                            <button type="button" class="btn btn-secondary float-left"
                                                    data-dismiss="modal">Close
                                            </button>
                                            <button type="submit" class="btn btn-primary float-right">Finish</button>
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
        You have no orders
    </div>
    </c:otherwise>
    </c:choose>

</body>
</html>
