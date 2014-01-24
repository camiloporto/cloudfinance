<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="/user/logoff" var="logoff"></c:url>
<c:url value="/account" var="accountHome"></c:url>
<c:url value="/transaction" var="transactionHome"></c:url>
<c:url value="/report" var="reportHome"></c:url>
<header class="navbar navbar-default navbar-fixed-top">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar">
		      <span class="sr-only">Toggle navigation</span>
		      <span class="icon-bar"></span>
		      <span class="icon-bar"></span>
		      <span class="icon-bar"></span>
		    </button>
			<a href="#" class="navbar-brand">Bufunfa</a>
		</div>
		<nav class="collapse navbar-collapse" role="navigation" id="navbar">
			<ul id="nav" class="nav navbar-nav">
				<li><a href="${accountHome}/roots">Sistema de Contas</a></li>
				<li><a href="${accountHome}">Contas</a></li>
				<li><a href="${transactionHome}">Transações</a></li>
				<li><a href="${reportHome}">Relatórios</a></li>
			</ul>
			<form action="${logoff}" method="POST">
				<input class="btn btn-link navbar-btn navbar-right" type="submit" value="Logout" id="logoffBtn">
			</form>
		</nav>
	</div>
	
</header>