<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cf" tagdir="/WEB-INF/tags/cloudfinance" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:url var="reportUrl" value="/report"></c:url>

<section>
	<h2>Relatórios</h2>
	<nav>
		<ul id="subnav">
			<li><a href="${reportUrl}/statement">Extrato de Conta</a></li>
			<li><a href="${reportUrl}/balanceSheet">Balanço Patrimonial</a></li>
		</ul>
	</nav>
</section>