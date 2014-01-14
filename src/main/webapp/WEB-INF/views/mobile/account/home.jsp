<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cf" tagdir="/WEB-INF/tags/cloudfinance" %>

<section class="content-inner">
	<h2>Hierarquia de Contas</h2>
	<cf:accountTree topNode="${response.accountTree}"></cf:accountTree>
</section>