<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="/mobile/newUser" var="newUser"></c:url>
<c:url value="/user/login" var="loginUser"></c:url>
<section class="main">
	<h2 class="headline">Dinheiro até o último dia do mês</h2>
	<p class="lead">Controle seu dinheiro sem complicação e passe o mês inteiro sem
		aperto</p>
	<a class="btn btn-lg btn-success btn-convert" href="${newUser}">Experimente Grátis!</a>
	
	<ul class="benefit-list">
		<li>Saiba, antes, se o seu dinheiro chegará até o final do mês</li>
		<li>Descubra para onde o seu dinheiro está indo</li>
		<li>Nada de planilhas ou controles complicados</li>
	</ul>
	
	<h3>Efetuar Login:</h3>
	<form action="${loginUser}" method="POST">
		<c:if test="${not empty response && not response.success}">
			<p id="loginStatus">Login failed</p>
		</c:if>
		<input class="form-control form-group" type="text" placeholder="Email" name="userName"> 
		<input class="form-control form-group" type="password" placeholder="Senha" name="pass"> 
		<input class="btn btn-default" type="submit" value="Entrar" id="btnSubmit">
	</form>
</section>