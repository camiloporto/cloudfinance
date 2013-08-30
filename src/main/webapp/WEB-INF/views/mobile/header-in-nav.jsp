<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="/user/logoff" var="logoff"></c:url>
<c:url value="/account" var="accountHome"></c:url>
<c:url value="/transaction" var="transactionHome"></c:url>
<c:url value="/report" var="reportHome"></c:url>
<header>
	<h1>CloudFinance</h1>
	<form action="${logoff}" method="POST">
		<input type="submit" value="Logout" id="logoffBtn">
	</form>
	<nav>
		<ul id="nav">
			<li><a href="${accountHome}/roots">Sistema de Contas</a></li>
			<li><a href="${accountHome}">Contas</a></li>
			<li><a href="${accountHome}">Transações</a></li>
			<li><a href="${accountHome}">Relatórios</a></li>
		</ul>
	</nav>
</header>