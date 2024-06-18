<%@page import="org.hibernate.internal.build.AllowSysOut"%>
<%@page import="entity.Train"%>
<%@page import="util.TrainInfoDisplay"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Book Ticket</title>
<link rel="stylesheet" href="res/css/styles.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

</head>

<body>
	<c:choose>
		<c:when
			test="${sessionScope.CurrentUser != null && sessionScope.isAuthRequired == false}">
			<%@include file="navbarLoggedIn.jsp"%>
		</c:when>
		<c:otherwise>
			<%@include file="navbar.jsp"%>
		</c:otherwise>
	</c:choose>
	<div class="modal fade" id="login-modal" data-bs-backdrop="static"
		data-bs-keyboard="false" tabindex="-1"
		aria-labelledby="staticBackdropLabelLogin" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="text-center">Login</h4>
				</div>
				<div class="modal-body">
					<div class="alert" id="login-status"></div>
					<div class="container">
						<form action="bookTicket" id="login-form" method="post">
							<div class="ip-field">
								<input type="text" name="irctc_ID" class="form-control"
									placeholder="Enter your IRCTC user id">
							</div>
							<div class="ip-field">
								<input type="password" name="irctc_password"
									class="form-control" placeholder="Enter your IRCTC password">
							</div>
							<div class="ip-field text-center">
								<button type="submit" class="btn btn-secondary">Login</button>
							</div>
						</form>

					</div>
				</div>
				<div class="modal-footer">
					<div>
						<p>
							New to this page? Please <a href="getSignupPage">sign up</a>
						</p>
					</div>
				</div>

			</div>
		</div>
	</div>

	<div class="container bookTicketContainer mt-5">
		<div class="row">
			<div class="col-md-4">
				<div class="train-name">
					<c:out value="${train.name }"></c:out>
				</div>
				<div class="train-info d-flex">
					<div class="train-number">
						<c:out value="${train.number }"></c:out>
					</div>
					<div class="v-line"></div>
					<div class="departs">
						Departs on
						<%
					Train train = (Train) request.getAttribute("train");
					String operatesOn = TrainInfoDisplay.getAbbreviatedOperatesOn(train.getOperatesOn());
					%>
						<p class="operatesOn">
							<%=operatesOn%></p>
					</div>
				</div>
			</div>
			<div class="col-md-8">
				<div class="row">
					<div class="col-md-4">
						<p class="station"><%=session.getAttribute("fromStation")%></p>
						<p class="date"><%=session.getAttribute("formattedFromDate")%></p>

						<p class="time">
							<c:out
								value="${sessionScope.trainNumberVsFormattedDepartureTime[train.number] }"></c:out>
						</p>
					</div>
					<div class="col-md-4">
						<div class="journey-duration d-flex">
							<div class="float-start journey-duration-line"></div>
							<div class="duration">

								<c:out
									value="${sessionScope.trainNumberVsTravelTime[train.number] }"></c:out>
							</div>
							<div class="float-end journey-duration-line"></div>
						</div>
					</div>
					<div class="col-md-4">
						<p class="station">${sessionScope.toStation }</p>

						<p class="date">
							<c:out
								value="${sessionScope.trainNumberVsFormattedArrivalDate[train.number] }"></c:out>
						</p>
						<p class="time">
							<c:out
								value="${sessionScope.trainNumberVsFormattedArrivalTime[train.number] }"></c:out>
						</p>
					</div>
				</div>
			</div>
		</div>
		<div class="availability-status">
			<h6>Availability status</h6>
			<div class="availability-status-inner">
				<div class="train-class">${trainClass }</div>
				<div class="ticket-avl-green">AVAILABLE ${ticketsAvl }</div>
			</div>
		</div>

		<hr>
		<form action="review" method="post">
			<!-- passenger details section starts -->
			<div class="passenger-details">
				<h3 class="my-3 ">Passenger details</h3>
				<div class="passenger-details-inner">
					<div class="showPassengers" id="showPassengers"></div>
					<p class="add-passenger h5">
						<span class="fa fa-plus" id="addTraveller"></span> Add Traveller
					</p>
				</div>
			</div>
			<!-- end of passenger details section -->
			<!-- irctc account details starts -->
			<hr>
			<!-- <div class="irctc-account-details">
				<h3 class="my-3 ">IRCTC Account details</h3>
				<div class="irctc-account-details-inner">
					<div class="important-details">Please ensure you have entered
						the correct username and password.</div>
					<input type="text" class="form-control mb-3" name="username"
						placeholder="IRCTC Username" value="${CurrentUser.irctcId }"
						required="required"> <input type="password"
						class="form-control mb-3" placeholder="IRCTC password"
						name="password" required="required">
				</div>
			</div> -->
			<!-- end of irctc account details section -->
			<hr>
			<div class="contact-information">
				<h3 class="my-3 ">
					Contact Information <span class="ticket-span">(We'll send
						your ticket and status updates here)</span>
				</h3>
				<div class="contact-inner">
					<div class="phone-no mb-2">
						<h6>Phone Number</h6>
						<input class="form-control" type="tel" name="phoneNumber"
							placeholder="Enter your phone number"
							value="${CurrentUser.contactNumber }">
					</div>
					<div class="email mb-2">
						<h6>Email Id</h6>
						<input class="form-control" type="email" name="emailId"
							placeholder="Enter your email address">
					</div>
				</div>
			</div>
			<hr>
			<div class="payment-information">
				<h3 class="my-3">Pay and Book now</h3>
				<div class="payment-information-inner">
					<ul>
						<li class="flex">
							<div>Base fare per adult</div>
							<div>
								<c:choose>
									<c:when test="${trainClass == 'First AC'}">
										<c:out value="${classFareDto.class_1A }"></c:out>
										<c:set var="baseFare" value="${classFareDto.class_1A }"></c:set>

									</c:when>
									<c:when test="${trainClass == 'Second AC'}">
										<c:out value="${classFareDto.class_2A }"></c:out>
										<c:set var="baseFare" value="${classFareDto.class_2A }"></c:set>
									</c:when>
									<c:when test="${trainClass == 'Third AC'}">
										<c:out value="${classFareDto.class_3A }"></c:out>
										<c:set var="baseFare" value="${classFareDto.class_3A }"></c:set>
									</c:when>
									<c:when test="${trainClass == 'Executive Chair Car'}">
										<c:out value="${classFareDto.class_EC }"></c:out>
										<c:set var="baseFare" value="${classFareDto.class_EC }"></c:set>
									</c:when>
									<c:when test="${trainClass == 'AC Chair Car'}">
										<c:out value="${classFareDto.class_CC }"></c:out>
										<c:set var="baseFare" value="${classFareDto.class_CC }"></c:set>
									</c:when>
									<c:when test="${trainClass == 'Sleeper'}">
										<c:out value="${classFareDto.class_sleeper }"></c:out>
										<c:set var="baseFare" value="${classFareDto.class_sleeper }"></c:set>
									</c:when>
								</c:choose>
								<c:set var="totalFarePerPassenger" value="${baseFare }"></c:set>
							</div>
						</li>
						<li class="flex">
							<div>Tax</div>
							<div>
								<c:out value="${baseFare*0.18 }"></c:out>
								<c:set var="totalFarePerPassenger" value="${baseFare*1.18 }"></c:set>
							</div>
						</li>
						<li class="flex">
							<div>Superfast charges</div>
							<div>
								<c:out value="45"></c:out>
								<c:set var="totalFarePerPassenger"
									value="${totalFarePerPassenger + 45 }"></c:set>
							</div>
						</li>
						<li class="flex">
							<div>Premium charges</div>
							<div>
								<c:choose>
									<c:when test="${train.isPremium }">
										<c:out value="100"></c:out>
										<c:set var="totalFarePerPassenger"
											value="${totalFarePerPassenger + 100 }"></c:set>
									</c:when>
									<c:otherwise>
										<c:out value="0"></c:out>
									</c:otherwise>
								</c:choose>
							</div>
						</li>
						<li class="flex">
							<div>Total fare per traveller</div>
							<div>
								<c:out value="${totalFarePerPassenger }"></c:out>
								<c:set var="totalFarePerPassenger"
									value="${totalFarePerPassenger }" scope="session"></c:set>
							</div>
						</li>
					</ul>

				</div>
				<button type="submit" id="payment-btn" class="btn btn-secondary">PROCEED
					TO PAYMENT</button>
			</div>
		</form>
	</div>
</body>
<script type="text/javascript" src="res/js/script.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"
	integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
	crossorigin="anonymous"></script>
<script>
	$(document).ready(function() {
		let conditionResult = ${isAuthRequired}
		;
		if (conditionResult) {
			$('#login-modal').modal('show');
		} else {
			$('#login-modal').modal('hide');
		}
	})
	function hideModal(){
		$('#login-modal').modal('hide');
	}
	document.getElementById('login-form').addEventListener('submit', function(){
		event.preventDefault();
		let irctcId=null;
		let irctcPassword=null;
		let form = new FormData(document.getElementById('login-form'));
		for(let [key,value] of form.entries()){
			if(key === 'irctc_ID')
				irctcId = value;
			else if(key === 'irctc_password')
				irctcPassword = value;
		}
		let params = "irctc_ID="+irctcId+"&irctc_password="+irctcPassword;
		let xhr = new XMLHttpRequest();
		let ele = document.getElementById('login-status');
		xhr.open('post','bookTicket',true);
		xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		xhr.onload = function(){
			if(xhr.status === 200 && xhr.readyState === 4){
				// ele.classList.add('alert-success');
				// ele.innerHTML = 'Successfully logged you in!';
				// let modal = new Bootstrap.Modal(document.getElementById('login-modal'));
				// if(modal){
				// 	modal.hide();
				// }
				hideModal();
				const Toast = Swal.mixin({
				toast: true,
				position: "top-end",
				showConfirmButton: false,
				timer: 3000,
				timerProgressBar: true,
				didOpen: (toast) => {
					toast.onmouseenter = Swal.stopTimer;
					toast.onmouseleave = Swal.resumeTimer;
				}
				});
				Toast.fire({
				icon: "success",
				title: "Logging you in!"
				});
				setTimeout(function(){
					window.location.reload();
				}, 3000);
				
			}else{
				ele.classList.remove('alert-danger');
				ele.classList.add('alert-danger');
				ele.innerHTML = 'Error occued while logging in!';
			}
		}
		xhr.send(params);

	})
	
	document.querySelector("#addTraveller").addEventListener('click',
			addTravellerField);

	function addTravellerField() {
		let n = staticCounter;
		incrementStaticCounter();
		var nameWrapper = document.createElement('div');
		var ageWrapper = document.createElement('div');
		var genderWrapper = document.createElement('div');
		
		
		var nameLabel = document.createElement('label');
		nameLabel.htmlFor = "name"+n;
		nameLabel.innerHTML = "Name";
		
		var nameInput = document.createElement('input');
		nameInput.className = "form-control";
		nameInput.type = "text";
		nameInput.id = "name"+n;
		nameInput.name = "travellerName";
		
		nameWrapper.append(nameLabel, nameInput);
		
		
		var ageLabel = document.createElement('label');
		ageLabel.htmlFor = "age"+n;
		ageLabel.innerHTML = "Age";
		
		var ageInput = document.createElement('input');
		ageInput.className = "form-control";
		ageInput.type = "number";
		ageInput.name = "travellerAge";
		ageInput.id = "age"+n;
		
		ageWrapper.append(ageLabel, ageInput);
		
		var genderLabel = document.createElement('label');
		genderLabel.htmlFor = "gender"+n;
		genderLabel.innerHTML = "Gender";
		
		var genderSelect = document.createElement('select');
		genderSelect.className = "form-select";
		genderSelect.name = "travellerGender";
		genderSelect.id="gender"+n;
		
		genderWrapper.append(genderLabel, genderSelect);
		
		var male = document.createElement('option');
		male.innerHTML = "Male";
		var female = document.createElement('option');
		female.innerHTML = "Female";
		var others = document.createElement('option');
		others.innerHTML = "Others";
		
		genderSelect.appendChild(male);
		genderSelect.appendChild(female);
		genderSelect.appendChild(others);
		
		var containerDiv = document.createElement('div');
		containerDiv.className = 'travellerDetails';
		containerDiv.classList.add('flex');
		containerDiv.appendChild(nameWrapper);
		containerDiv.appendChild(ageWrapper);
		containerDiv.appendChild(genderWrapper);

		
		document.querySelector('#showPassengers').appendChild(
								containerDiv);
	}
	
</script>
</html>