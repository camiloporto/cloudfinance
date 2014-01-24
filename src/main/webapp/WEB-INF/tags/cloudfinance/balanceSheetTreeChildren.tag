<%@ attribute name="children" type="java.util.List" required="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cf" tagdir="/WEB-INF/tags/cloudfinance" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${not empty children}">
	<ul>
		<c:forEach var="childNode" items="${children}">
			<li>
				<span>
					<spring:message code="${childNode.account.name}"  text="${childNode.account.name}"></spring:message>
				</span>
				
				<c:if test="${childNode.balance < 0}">
				 	<c:set var="valueColorClass" value="debit-color"></c:set>
				</c:if>
				<span class="pull-right ${valueColorClass}">
					<fmt:formatNumber value="${childNode.balance}" type="currency" pattern="#,#00.00#"/>
				</span>
			</li>
			<c:if test="${not empty childNode.children}">
				<cf:balanceSheetTreeChildren children="${childNode.children}"></cf:balanceSheetTreeChildren>
			</c:if>
		</c:forEach>
	</ul>
</c:if>