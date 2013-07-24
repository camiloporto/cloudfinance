<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="/mobile/newUser" var="newUser"></c:url>
<section>
	<h2>Tenha dinheiro ate o ultimo dia do mes</h2>
	<p>Controle seu dinheiro sem complicacao e passe o mes inteiro sem
		aperto</p>
	<ul>
		<li>Saiba, antes, se o seu dinheiro chegara ate o final do mes</li>
		<li>Descubra para onde o seu dinheiro esta indo</li>
		<li>Nada de planilhas ou controles complicados</li>
	</ul>
	<a href="${newUser}">Quero Experimentar, sem Pagar</a>
	<form>
		<input type="text" placeholder="Nome de Usuario"> 
		<input type="password" placeholder="Senha"> 
		<input type="submit" value="Login">
	</form>
</section>