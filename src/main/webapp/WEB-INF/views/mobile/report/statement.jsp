<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cf" tagdir="/WEB-INF/tags/cloudfinance" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:url var="statementUrl" value="/report/statement"></c:url>
<section class="content-inner">
	<h2>Extrato de Conta</h2>
	<form id="statementForm" action="${statementUrl}" method="GET">
		<div class="form-group">
			<label>
				Conta:
				<select name="accountId" class="form-control">
					<cf:accountOptionList accounts="${response.accountList}"></cf:accountOptionList>
				</select>
			</label>
		</div>
		<input class="form-control form-group" type="text" placeholder="Data Inicial" name="begin">
		<input class="form-control form-group" type="text" placeholder="Data Final" name="end">
		<input class="btn btn-primary" type="submit" value="Extrato">
	</form>
	<c:if test="${not empty response.accountStatement}">
	<table id="statementTable" class="accountStatement table table-striped table-condensed table-hover">
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
						<div>
							<c:if test="${entry.amount >= 0}">
								<span class="glyphicon glyphicon-arrow-up"></span>
							</c:if>
							<c:if test="${entry.amount < 0}">
								<span class="glyphicon glyphicon-arrow-down"></span>
							</c:if>
							<span class="entry-involvedAccount">
								<spring:message code="${entry.involvedAccount.name}"  text="${entry.involvedAccount.name}"></spring:message>
							</span>
						</div>
						<div class="entry-description"><small>${entry.description}</small></div>
					</td>
					<td><fmt:formatNumber value="${entry.amount}" type="currency" pattern="#,#00.00#"/></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</c:if>
</section>