<%@ attribute name="accounts" type="br.com.camiloporto.cloudfinance.model.Account[]" required="true"  %>
<%@ attribute name="accountSelected" type="java.lang.Long" required="false"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cloudfinance" tagdir="/WEB-INF/tags/cloudfinance" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<c:forEach var="a" items="${accounts}">
	<c:set var="isSelected" value=""></c:set>
	<c:if test="${a.id == accountSelected}">
		<c:set var="isSelected" value="selected"></c:set>
	</c:if>
	<option value="${a.id}" ${isSelected}>
		<spring:message code="${a.name}"  text="${a.name}"></spring:message>
	</option>
</c:forEach>

	
