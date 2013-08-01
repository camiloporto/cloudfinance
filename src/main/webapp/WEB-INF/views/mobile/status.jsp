<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<section>
	<h2 id="pageTitle">${operation}</h2>
	<c:if test="${response.success}">
		<p id="statusMessage">Operação realizada com sucesso</p>
	</c:if>
	<c:if test="${not response.success}">
		<p id="statusMessage">Operação não realizada</p>
	</c:if>
	<ul id="errors">
		<c:forEach var="error" items="${response.errors}">
			<li>${error}</li>
		</c:forEach>
	</ul>
</section>