<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cf" tagdir="/WEB-INF/tags/cloudfinance" %>

<c:url var="newTransactionUrl" value="/transaction"></c:url>
<section class="content-inner">
	<h2>Nova Transação</h2>
	<form id="formNewTransaction" action="${newTransactionUrl}" method="POST">
	<c:if test="${not empty response.errors}">
		<ul id="errors" class="error-list">
			<c:forEach var="error" items="${response.errors}">
				<li>${error}</li>
			</c:forEach>
		</ul>
	</c:if>
		<div class="form-group">
			<label>
				Conta de Origem:
				<select name="originAccountId" class="form-control">
					<cf:accountOptionList accounts="${response.originAccountList}"></cf:accountOptionList>
				</select>
			</label>
		</div>
		<div class="form-group">
			<label>
				Conta de Destino:
				<select name="destAccountId" class="form-control">
					<cf:accountOptionList accounts="${response.destAccountList}"></cf:accountOptionList>
				</select>
			</label>
		</div>
		<input class="form-control form-group" name="date" type="text" placeholder="Data da transação">
		<input class="form-control form-group" name="amount" type="text" placeholder="Valor">
		<input class="form-control form-group" name="description" type="text" placeholder="Descrição">
		<input class="btn btn-primary" type="submit" value="Salvar">
	</form>
</section>