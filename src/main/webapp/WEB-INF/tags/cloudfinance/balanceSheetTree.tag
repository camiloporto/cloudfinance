<%@ attribute name="topNode" type="br.com.camiloporto.cloudfinance.service.impl.BalanceSheetNode" required="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cf" tagdir="/WEB-INF/tags/cloudfinance" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<ul>
	<li>
		<span>
			<spring:message code="${topNode.account.name}"  text="${topNode.account.name}"></spring:message>
		</span>
		<span>
			<fmt:formatNumber value="${topNode.balance}" type="currency" pattern="#,#00.00#"/>
		</span>
		<cf:balanceSheetTreeChildren children="${topNode.children}"></cf:balanceSheetTreeChildren>
	</li>
</ul>
	
