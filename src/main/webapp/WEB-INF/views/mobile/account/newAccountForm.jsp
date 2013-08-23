<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<section>
	<h2 id="pageTitle">Nova Conta</h2>
	<form action="">
		<h3><spring:message code="${response.account.name}" text="${response.account.name}"></spring:message></h3>
	</form>
	
</section>