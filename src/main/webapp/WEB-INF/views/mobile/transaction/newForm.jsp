<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cf" tagdir="/WEB-INF/tags/cloudfinance" %>

<section>
	<h2>Nova Transação</h2>
	<form id="formNewTransaction" action="" method="POST">
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
		<input name="date" type="date" placeholder="Data da transação">
		<input name="amount" type="number" placeholder="Valor">
		<input name="description" type="text" placeholder="Descrição">
		<input type="submit" value="Salvar">
	</form>
</section>