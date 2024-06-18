<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Navbar</title>
<script src="https://kit.fontawesome.com/fe4cd96652.js" crossorigin="anonymous"></script>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body>
	<nav class="navbar navbar-expand-lg bg-body-tertiary">
		<div class="container-fluid">
			<a class="navbar-brand" href="#"> 
				<span class="fas fa-train fa-3x"></span>
			</a>
			<div class="collapse navbar-collapse" id="navbarSupportedContent">
				<ul class="navbar-nav me-auto mb-2 mb-lg-0">
					<li class="nav-item"><a class="nav-link active mx-4"
						aria-current="page" href="home"><span class="fa fa-search me-1"></span>Find Trains</a></li>
					<li class="nav-item"><a class="nav-link mx-3" href="#"><span class="fa fa-vcard-o me-1"></span> Contact</a>
					</li>
				</ul>
				<div class="d-flex">
					<ul class="navbar-nav">
						<li class="nav-item"><a class="nav-link"
							href="getSignupPage"><span class="fa fa-user-circle"></span>
								Sign up</a></li>
						<li class="nav-item"><a class="nav-link"
							href="getLoginPage"><span class="fa fa-sign-in"></span> Login</a></li>
					</ul>
				</div>
			</div>
		</div>
	</nav>
	<script type="text/javascript"
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>