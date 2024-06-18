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
		<div class="alert" id="errors">
			
		</div>
		<form action="signup" method="post" id="signup-form">
			<div class="ip-field">
				<input type="text" name="name" class="form-control" placeholder="Enter your name">	
			</div>
			<div class="ip-field">
				<input type="number" name="age" class="form-control" placeholder="Enter your age">	
			</div>
			
			<div class="ip-field">
				Select your gender:<br>
				<input type="radio" name="gender" id="male" value="male"> 
				<label for="male">Male</label>
				<input type="radio" name="gender" id="female" value="female">
				<label for="female">Female</label>	
				<input type="radio" name="gender" id="others" value="others">
				<label for="others">Others</label>
			</div>
			<div class="ip-field">
				<input type="email" name="emailId" class="form-control" placeholder="Enter your email ID">	
			</div>
			<div class="ip-field">
				<input type="tel" name="contactNumber" class="form-control" placeholder="Enter your contact details">	
			</div>
			<div class="ip-field">
				<input type="text" name="irctcId" class="form-control" placeholder="Enter your IRCTC user id">	
			</div>
			<div class="ip-field">
				<input type="password" name="irctcPassword" class="form-control" placeholder="Enter your IRCTC password">
			</div>
			<div class="ip-field text-center">
				<button type="submit" id="submit-btn" class="btn btn-secondary">Sign up</button>
			</div>
		</form>
		
	</div>
	
</body>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script type="text/javascript" src="res/js/script.js"></script>

<script>

	


	document.getElementById('signup-form').addEventListener('submit',function(event){
	let errors = "";
	event.preventDefault();
	let formData = new FormData(document.getElementById('signup-form'));
	let params = "";
	for(let [key, value] of formData.entries()){
		params += (key+"="+value+"&");
		switch (key) {
			case "age":
				if(value <= 0 && value >= 150){
					errors += "age cannot be "+value;
				}
				break;
			case "contactNumber":
				if(value.length != 10){
					errors += "contact number should be of 10 digits";
				}
				for(let ch of value){
					if(ch >= '0' && ch <= '9'){
						continue;	
					}else{
						errors += "contact number should be only numeric";
						break;
					}
				}
		}
	}
	params = params.substring(0, params.length-1);
	if(errors !== ""){
		document.getElementById('errors').innerHTML = errors;
		document.getElementById('errors').classList.add('alert-danger');
	}else{
		// let xhr = new XMLHttpRequest();
		// xhr.open('post','signup', true);
		// xhr.responseType = 'text';
		// xhr.onload = function(){
		// 	if(xhr.status === 200 && xhr.readyState === 4){
		// 		swal.fire({
		// 			icon: "success",
		// 			title : "New user registered successfully!",
		// 			timer: 3000,
					
		// 		})
		// 		location = xhr.responseText;
		// 	}
		// }
		// xhr.send(form);

		fetch('signup',{
			method:'post',
			body:params,
			headers:{
				"Content-Type": 'application/x-www-form-urlencoded'
			}
		})
		.then(res =>{
			swal.fire({
					icon: "success",
					title : "New user registered successfully!",
					confirmButtonText:"Redirect to home"
				})
				.then((result)=>{
					if(result.isConfirmed){
						window.location = "home";
					}
				})
		})
		.catch(err =>{
			swal.fire({
					icon: "error",
					title : "User could not be registered!",
					timer: 3000,
					
				})
		})

		
	}
	

})
</script>

</html>