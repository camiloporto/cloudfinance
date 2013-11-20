<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
	<head>
		<c:url value="/resources/styles" var="css_resource_url"></c:url>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
		<title>CloudFinance - Tranquilidade para sua vida e suas financas</title>
		<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>
<%-- 		<link rel="stylesheet" type="text/css" media="screen" href="${css_resource_url}/reset.css" /> --%>
		<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap.min.css">
		<link rel="stylesheet" type="text/css" media="screen" href="${css_resource_url}/copy.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="${css_resource_url}/mobile-layout.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="${css_resource_url}/components.css" />
		
	</head>
	<body>
		<div class="container">
			<tiles:insertAttribute name="header" ignore="true" />
			<tiles:insertAttribute name="body"/> 
		   	<tiles:insertAttribute name="footer" ignore="true"/>
	   </div>
	</body>
</html>