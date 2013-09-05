<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cf" tagdir="/WEB-INF/tags/cloudfinance" %>

<c:url var="newTransactionUrl" value="/transaction"></c:url>
<section>
	<h2>Nova Transação</h2>
	<form id="formNewTransaction" action="${newTransactionUrl}" method="POST">
		<ul id="errors">
			<c:forEach var="error" items="${response.errors}">
				<li>${error}</li>
			</c:forEach>
		</ul>
		<label>
			Conta de Origem:
			<select name="originAccountId">
				<cf:accountOptionList accounts="${response.originAccountList}"></cf:accountOptionList>
			</select>
		</label>
		<label>
			Conta de Destino:
			<select name="destAccountId">
				<cf:accountOptionList accounts="${response.destAccountList}"></cf:accountOptionList>
			</select>
		</label>
		<input name="date" type="text" placeholder="Data da transação">
		<input name="amount" type="text" placeholder="Valor">
		<input name="description" type="text" placeholder="Descrição">
		<input type="submit" value="Salvar">
	</form>
</section>