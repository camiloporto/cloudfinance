<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="/mobile/status" var="status"></c:url>
<section>
	<h2>Cadastro de Novo Usuario</h2>
	<form action="${status}">
		<input name="userName" type="email" placeholder="E-mail"> 
		<input name="password" type="password" placeholder="Senha"> 
		<input name="confirmPassword" type="password" placeholder="Confirme a senha"> 
		<input id="btnSubmit" type="submit" value="Cadastrar">
	</form>
</section>