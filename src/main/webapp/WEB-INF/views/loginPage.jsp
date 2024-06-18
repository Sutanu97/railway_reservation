<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<link rel="stylesheet" href="res/css/styles.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css">
</head>
<body>
	<%@include file="navbar.jsp" %>
	<div class="container login-container">
		<form action="login" method="post">
			<div class="ip-field">
				<input type="text" name="irctc_ID" class="form-control" placeholder="Enter your IRCTC user id">	
			</div>
			<div class="ip-field">
				<input type="password" name="irctc_password" class="form-control" placeholder="Enter your IRCTC password">
			</div>
			<div class="ip-field text-center">
				<button type="submit" class="btn btn-secondary">Login</button>
			</div>
		</form>
		
	</div>
	
</body>
</html>