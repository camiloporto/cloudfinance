<%@ attribute name="accounts" type="br.com.camiloporto.cloudfinance.model.Account[]" required="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cloudfinance" tagdir="/WEB-INF/tags/cloudfinance" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<c:forEach var="a" items="${accounts}">
	<option value="${a.id}">
		<spring:message code="${a.name}"  text="${a.name}"></spring:message>
	</option>
</c:forEach>

	
