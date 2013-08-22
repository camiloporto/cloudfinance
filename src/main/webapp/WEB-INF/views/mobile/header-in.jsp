<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="/user/logoff" var="logoff"></c:url>
<header>
	<h1>CloudFinance</h1>
	<form action="${logoff}" method="POST">
		<input type="submit" value="Logout" id="logoffBtn">
	</form>
</header>