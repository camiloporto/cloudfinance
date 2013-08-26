<%@tag import="java.util.ArrayList"%>
<%@tag import="java.util.List"%>
<%@tag import="java.util.HashMap"%>
<%@tag import="java.util.Map"%>
<%@tag import="br.com.camiloporto.cloudfinance.model.Account"%>
<%@ attribute name="accounts" type="br.com.camiloporto.cloudfinance.model.Account[]" required="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cloudfinance" tagdir="/WEB-INF/tags/cloudfinance" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%
Map<String, List<Account>> groupsOptionMap = new HashMap<String, List<Account>>();
for(int i = 0; i < accounts.length; i++) {
	if(!groupsOptionMap.containsKey(accounts[i].getParentAccount().getName())) {
		groupsOptionMap.put(accounts[i].getParentAccount().getName(), new ArrayList<Account>());
	}
	groupsOptionMap.get(accounts[i].getParentAccount().getName()).add(accounts[i]);
}

for (String group : groupsOptionMap.keySet()) {
%>
	<optgroup label="<spring:message code="<%=group%>"  text="<%=group%>"></spring:message>">
	
	<%
	List<Account> accounts = groupsOptionMap.get(group);
	for (Account a : accounts) {
		%>
		<option value="<%=a.getId()%>">
			<spring:message code="<%=a.getName() %>"  text="<%=a.getName() %>"></spring:message>
		</option>
		<%
	}//for account names
	%>
	
	</optgroup>
<%
} //for groups
%>

	
