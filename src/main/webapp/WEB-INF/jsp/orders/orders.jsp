<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://shans.su" prefix="shans" %>

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
    <h2><fmt:message key="orders"/></h2>
    <br>
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/orders/add" class="form-inline">
                <button class="btn btn-primary "><fmt:message key="orders.addOrder"/></button>
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
        <c:when test="${orders != null }">
            <div class="card-deck">
                <c:forEach var="order" items="${orders}">
                    <div class="card my-3">
                        <div class="card-body">
                            <h5 class="card-title"><fmt:message key="orders.problem"/>: ${order.name}
                                <c:if test="${!order.isReject() && !order.isDone()}">
                                    <a href="/orders/reject?orderId=${order.id}"
                                       class="btn btn-outline-danger btn-sm float-right"><fmt:message
                                            key="orders.reject"/></a>
                                </c:if>
                            </h5>
                            <h6 class="card-subtitle mb-2"><fmt:message key="orders.status"/>: <fmt:message
                                    key="${order.status}"/></h6>
                            <br>
                            <h6 class="card-subtitle mb-2"><fmt:message
                                    key="orders.description"/>: ${order.description}</h6>
                            <br>
                            <c:if test="${order.masterId > 0}">
                                <c:set var="orderMaster" value="${order.master}"/>
                                <br>
                                <h6 class="card-subtitle mb-2"><fmt:message key="master"/>: </h6>
                                <div class="media">
                                    <img src="/images/users/${orderMaster.photo}" class="align-self-start mr-3"
                                         width="42"
                                         height="42">
                                    <div class="media-body">
                                        <h5 class="mt-0"><a
                                                href="/comments?id=${orderMaster.id}">${orderMaster.fullName}</a></h5>
                                        <fmt:message key="email"/>: ${orderMaster.email}
                                    </div>
                                </div>
                                <br>
                            </c:if>
                            <h6 class="card-subtitle mb-2 text-muted"><fmt:message
                                    key="orders.location"/>: ${order.location}</h6>
                            <h6 class="card-subtitle mb-2 text-muted"><fmt:message
                                    key="orders.date"/>: ${order.date}</h6>
                            <div class="dropdown-divider"></div>
                            <c:if test="${order.isDone()}">
                                <c:choose>
                                    <c:when test="${order.commentId == 0}">
                                        <form action="/orders" method="post">
                                            <input type="hidden" name="orderId" value="${order.id}">
                                            <input type="hidden" name="masterId" value="${order.masterId}">
                                            <input type="hidden" name="userId" value="${order.userId}">

                                            <label for="textarea"><fmt:message key="rate"/></label>
                                            <div class="rate">
                                                <input type="radio" id="star5" name="rate" value="5"/>
                                                <label for="star5" title="text">5 stars</label>
                                                <input type="radio" id="star4" name="rate" value="4"/>
                                                <label for="star4" title="text">4 stars</label>
                                                <input type="radio" id="star3" name="rate" value="3"/>
                                                <label for="star3" title="text">3 stars</label>
                                                <input type="radio" id="star2" name="rate" value="2"/>
                                                <label for="star2" title="text">2 stars</label>
                                                <input type="radio" id="star1" name="rate" value="1"/>
                                                <label for="star1" title="text">1 star</label>
                                            </div>
                                            <br>
                                            <br>
                                            <div class="form-group">
                                                <small class="form-text text-muted"><fmt:message
                                                        key="rate.comment.description"/></small>
                                                <textarea class="form-control" required rows="3" id="textarea"
                                                          name="description"></textarea>
                                            </div>
                                            <button type="submit" class="btn btn-primary"><fmt:message
                                                    key="orders.submit"/></button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="comment" value="${order.comment}"/>
                                        <h6 class="card-subtitle mb-2"><fmt:message key="rate"/>: </h6>
                                        <shans:rateTag rate="${comment.rate}"> </shans:rateTag>
                                        <label><fmt:message key="comments"/>: ${comment.description}</label>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                            <hr>
                            <c:if test="${order.price > 0}">
                                <br>
                                <h6 class="card-subtitle mb-2"><fmt:message key="orders.price"/>: ${order.price}</h6>
                            </c:if>
                            <c:if test="${order.isPayment()}">
                                <form action="/payment" method="get">
                                    <input type="hidden" name="orderId" value="${order.id}">
                                    <button type="submit" class="btn btn-primary"><fmt:message
                                            key="orders.pay"/></button>
                                </form>
                            </c:if>

                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info" role="alert">
                <fmt:message key="orders.noOrders"/>
            </div>
        </c:otherwise>
    </c:choose>

</div>
</body>
</html>



