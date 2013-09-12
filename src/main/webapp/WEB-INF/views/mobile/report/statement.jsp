<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cf" tagdir="/WEB-INF/tags/cloudfinance" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:url var="statementUrl" value="/report/statement"></c:url>
<section>
	<h2>Extrato de Conta</h2>
	<form id="statementForm" action="${statementUrl}" method="GET">
		<label>
			Conta:
			<select name="accountId">
				<cf:accountOptionList accounts="${response.accountList}"></cf:accountOptionList>
			</select>
		</label>
		<input type="text" placeholder="Data Inicial" name="begin">
		<input type="text" placeholder="Data Final" name="end">
		<input type="submit" value="Extrato">
	</form>
	<c:if test="${not empty response.accountStatement}">
	<table id="statementTable">
		<thead>
			<tr>
				<th colspan="2">Saldo Anterior:</th>
				<th>
					<fmt:formatNumber value="${response.accountStatement.balanceBeforeInterval}" type="currency" pattern="#,#00.00#"/>
				</th>
			</tr>
		</thead>
		<tfoot>
			<tr>
				<th colspan="2">Saldo Final:</th>
				<th>
					<fmt:formatNumber value="${response.accountStatement.balanceAfterInterval}" type="currency" pattern="#,#00.00#"/>
				</th>
			</tr>
		</tfoot>
		<tbody>
			<c:forEach var="entry" items="${response.accountStatement.entries}">
				<tr>
					<td>
						<fmt:formatDate value="${entry.date}" pattern="dd/MM/yyyy"/>
					</td>
					<td>
						<div><spring:message code="${entry.involvedAccount.name}"  text="${entry.involvedAccount.name}"></spring:message></div>
						<span>${entry.description}</span>
					</td>
					<td><fmt:formatNumber value="${entry.amount}" type="currency" pattern="#,#00.00#"/></td>
					<td></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
		<div id="statement" style="visibility: hidden;">
			<!-- ESTRUTURAR O EXTRATO COMO UMA TABELA. -->
			<p>Saldo Anterior: 
				<span id="previousBalance">
					<fmt:formatNumber value="${response.accountStatement.balanceBeforeInterval}" type="currency" pattern="#,#00.00#"/> 
				</span>
			</p>
			<ul>
				<c:forEach var="entry" items="${response.accountStatement.entries}">
					<li> 
						<p><fmt:formatDate value="${entry.date}" pattern="dd/MM/yyyy"/> </p>
						<p><spring:message code="${entry.involvedAccount.name}"  text="${entry.involvedAccount.name}"></spring:message></p>
						<p>${entry.description}</p>
						<p><fmt:formatNumber value="${entry.amount}" type="currency" pattern="#,#00.00#"/> </p>
					</li>
				</c:forEach>
			</ul>
			<p>Saldo Final: 
				<span id="finalBalance">
					<fmt:formatNumber value="${response.accountStatement.balanceAfterInterval}" type="currency" pattern="#,#00.00#"/> 
				</span>
			</p>
		</div>
	</c:if>
</section>