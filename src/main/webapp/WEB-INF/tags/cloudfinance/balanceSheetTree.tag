<%@ attribute name="topNode" type="br.com.camiloporto.cloudfinance.service.impl.BalanceSheetNode" required="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cf" tagdir="/WEB-INF/tags/cloudfinance" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<ul class="balanceSheetTree list-group">
	<li class="list-group-item">
		<span>
			<spring:message code="${topNode.account.name}"  text="${topNode.account.name}"></spring:message>
		</span>
		
		<c:if test="${topNode.balance < 0}">
		 	<c:set var="valueColorClass" value="debit-color"></c:set>
		</c:if>
		<span class="pull-right ${valueColorClass}">
			<fmt:formatNumber value="${topNode.balance}" type="currency" pattern="#,#00.00#"/>
		</span>
		<cf:balanceSheetTreeChildren children="${topNode.children}"></cf:balanceSheetTreeChildren>
	</li>
</ul>
	
