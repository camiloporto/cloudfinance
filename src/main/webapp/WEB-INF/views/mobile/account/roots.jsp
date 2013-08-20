<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<section>
	<h2>Sistemas de Contas</h2>
	<ul id="rootAccountList">
		<c:forEach var="account" items="${response.rootAccounts}">
			<li>${account.name}</li>
		</c:forEach>
	</ul>
</section>