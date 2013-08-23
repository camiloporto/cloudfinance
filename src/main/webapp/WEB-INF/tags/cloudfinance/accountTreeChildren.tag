<%@ attribute name="children" type="java.util.List" required="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cloudfinance" tagdir="/WEB-INF/tags/cloudfinance" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<c:if test="${not empty children}">
	<ul>
		<c:forEach var="childNode" items="${children}">
			<li>
				<h3>
					<spring:message code="${childNode.account.name}" text="${childNode.account.name}"></spring:message>
				</h3>
				<p>${childNode.account.description}</p>
			</li>
			<c:if test="${not empty childNode.children}">
				<cloudfinance:accountTreeChildren children="${childNode.children}"></cloudfinance:accountTreeChildren>
			</c:if>
		</c:forEach>
	</ul>
</c:if>