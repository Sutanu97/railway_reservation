<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Home</title>
<link rel="stylesheet" href="res/css/styles.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css">
</head>
<body class="train_bg">
	<div class="container-fluid">
		<c:choose>
			<c:when
				test="${sessionScope.CurrentUser != null && sessionScope.isAuthRequired == false}">
				<%@include file="navbarLoggedIn.jsp"%>
			</c:when>
			<c:otherwise>
				<%@include file="navbar.jsp"%>
			</c:otherwise>
		</c:choose>
		<div class="alert alert-danger display-none" id="errDiv">
			<c:if test="${isCountMore == true}">
				<div id="errMsg">
					<c:out value='Failed to book because the number of travellers is more than the seats available!'></c:out>
				</div> 
			</c:if>
		</div>
		<div class="mb-3"></div>
		<div class="row">
			<div class="col col-md-4">
				<%@include file="findTrains.jsp"%>
			</div>
			<div id="date-range"></div>
		</div>
	</div>
	<script src="https://code.jquery.com/jquery-3.7.0.min.js"
		integrity="sha256-2Pmvv0kuTBOenSvLm6bvfBSSHrUJ+3A7x6P5Ebd07/g="
		crossorigin="anonymous"></script>
	<script type="text/javascript" src="res/js/script.js"></script>
	<script type="text/javascript"
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"></script>
</body>
<script>
	let errMsg = document.getElementById('errMsg');
	if(errMsg){
		document.getElementById('errDiv').classList.remove('display-none');
	}
	

</script>
</html>