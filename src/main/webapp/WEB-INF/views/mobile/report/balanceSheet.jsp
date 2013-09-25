<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cf" tagdir="/WEB-INF/tags/cloudfinance" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:url var="balanceUrl" value="/report/balanceSheet"></c:url>
<section>
	<h2>Balanço</h2>
	<form id="balanceForm" action="${balanceUrl}" method="GET">
		<input name="balanceDate" placeholder="Data do Balanço" type="text">
		<input type="submit" value="Balanço">
	</form>
</section>