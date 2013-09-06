<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="/user/signup" var="signup"></c:url>
<section>
	<h2>Cadastro de Novo Usuario</h2>
	<form action="${signup}" method="post">
		<c:if test="${not response.success}">
			<p id="statusMessage">Operação não realizada</p>
		</c:if>
		<ul id="errors">
			<c:forEach var="error" items="${response.errors}">
				<li>${error}</li>
			</c:forEach>
		</ul>
		<input name="userName" type="email" placeholder="E-mail"> 
		<input name="pass" type="password" placeholder="Senha"> 
		<input name="confirmPass" type="password" placeholder="Confirme a senha"> 
		<input id="btnSubmit" type="submit" value="Cadastrar">
	</form>
</section>