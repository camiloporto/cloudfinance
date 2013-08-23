<%@ attribute name="topNode" type="br.com.camiloporto.cloudfinance.model.AccountNode" required="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cloudfinance" tagdir="/WEB-INF/tags/cloudfinance" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<c:url var="showFormNewAccount" value="/account/showForm"></c:url>
<ul>
	<li>
		<h3>
			<a href="${showFormNewAccount}/${topNode.account.id}">
				<spring:message code="${topNode.account.name}"  text="${topNode.account.name}"></spring:message>
			</a>
		</h3>
		<p>${topNode.account.description}</p>
		<cloudfinance:accountTreeChildren children="${topNode.children}"></cloudfinance:accountTreeChildren>
	</li>
</ul>
	
