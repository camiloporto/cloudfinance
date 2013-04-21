<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<c:url var="styleFolder" value="/styles"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="${styleFolder}/bootstrap.min.css">
<title>Bufunfa - Controlando suas finanças pessoais</title>
</head>
<body>
	<tiles:insertAttribute name="header" ignore="true" />
	<tiles:insertAttribute name="menu" ignore="true" />
	<tiles:insertAttribute name="body"/> 
   	<tiles:insertAttribute name="footer" ignore="true"/>
</body>
</html>