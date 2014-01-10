<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="/account" var="accountURL"></c:url>
<section class="content-inner">
	<h2>Sistemas de Contas</h2>
	<ul class="accountSystem-list list-unstyled" id="rootAccountList">
		<c:forEach var="accountSystem" items="${response.accountSystems}">
			<li><a href="${accountURL}/${accountSystem.id}">${accountSystem.rootAccount.name}</a></li>
		</c:forEach>
	</ul>
</section>