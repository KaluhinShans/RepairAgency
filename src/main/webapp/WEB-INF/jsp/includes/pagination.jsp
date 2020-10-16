<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>

<nav aria-label="Navigation for countries">
    <ul class="pagination">
        <c:if test="${page != 1}">
            <li class="page-item">
                <a class="page-link" onclick="addUrlParameter('${page-1}')">Previous</a>
            </li>
        </c:if>

        <c:forEach begin="1" end="${nOfPages}" var="i">
            <c:choose>
                <c:when test="${page eq i}">
                    <li class="page-item active"><a class="page-link">
                            ${i} <span class="sr-only">(current)</span></a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item"><a class="page-link"
                                             onclick="addUrlParameter('${i}')">${i}</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <c:if test="${page lt nOfPages}">
            <li class="page-item"><a class="page-link"
                                     onclick="addUrlParameter('${page+1}')">Next</a>
            </li>
        </c:if>
    </ul>
</nav>

<script>
    function addUrlParameter(value) {
        var searchParams = new URLSearchParams(window.location.search)
        searchParams.set('page', value)
        window.location.search = searchParams.toString()
    }
</script>
