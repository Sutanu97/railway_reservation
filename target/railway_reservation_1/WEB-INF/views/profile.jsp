<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<link rel="stylesheet" href="res/css/styles.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">


</head>
<body>
	<%@include file="navbarLoggedIn.jsp" %>
	<div class="welcome">Welcome &nbsp;${sessionScope.CurrentUser.name }</div>
	<div class="mt-2">
		<ul class="nav nav-tabs">
		  <li class="nav-item">
		    <a class="nav-link active" aria-current="page" href="#" id = 'upcoming'>Upcoming journeys</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" href="#" id = 'past'>Past journeys</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" href="#" id = 'cancelled'>Cancelled journeys</a>
		  </li>
		</ul>
	</div>
	<div id = "populate-journeys">
		
	</div>
</body>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<!-- <script type="text/javascript" src="res/js/script.js"></script> -->

<script type="text/javascript">


const UPCOMING_JOURNEYS = 'UPCOMING_JOURNEYS';
const PAST_JOURNEYS = 'PAST_JOURNEYS';
const CANCELLED_BOOKINGS = 'CANCELLED_BOOKINGS';
const TICKET_HTML = `
	<div class="container reviewContainerMaxWidth mt-5">
		<div class="card p-4">
			<div class="row">
				<div class="col-md-8">
					<div id="train-name">
					</div>
					<div class="train-info d-flex">
						<div id="train-number">
						</div>
						<div class="v-line mx-3"></div>
						<div id="train-class">
						</div>
					</div>
				</div>
				<div class="col-md-4" id="pnr"></div>
			</div>
			<!-- train no. and pnr -->
			<div class="row mt-4">
				<div class="col-md-4">
					 <p class="station fs-5" id="fromStation"></p> 
					 <p class="date" id="doj"></p>

					<p class="time fw-light fs-5" id="toj">
						
					</p>
				</div>
				<div class="col-md-4">
					<div class="journey-duration d-flex">
						<div class="float-start journey-duration-line"></div>
						<div class="duration" id="duration">

						</div>
						<div class="float-end journey-duration-line"></div>
					</div>
				</div>
				<div class="col-md-4">
					<p class="station fs-5" id="toStation"></p>
					<p class="date" id="doa">
					</p>
					<p class="time fw-light fs-5" id="toa">
					</p>
				</div>
			</div>
			<!-- <%--  stations and time required--%> -->

			<div class="text-center important-details" id="important-details">
			</div>
			<!-- <%-- desc --%> -->
			<div id="travellers-section">
				<!-- <%-- passenger details --%> -->
				<div class="mt-2">
					<div class="flex">
						<h6>Travellers</h6>
						<h6>BookingStatus</h6>
					</div>
					<ol id = "traveller-details">
					</ol>
				</div>
			</div>
		</div>
		<div class="mt-2 display-none" >
			<button class="btn btn-primary confirmation-btn" id = "confirmation-btn-">SEND CONFIRMATION</button>
			<button class="btn btn-danger cancel-btn" id="cancel-btn-">CANCEL</button>
		</div>
	</div>
`;

let ticketCancellationModal = `<div class="modal" id="ticket-cancellation-modal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="text-center">Ticket cancellation</h4>
				</div>
				<div class="modal-body">
					<div class="container">
						<p>
							Select the travellers for which you want to cancel the ticket
						</p>
						<hr>
						<form id="ticket-cancellation-form" action="cancelTicket" method="get">
							<div id="travellers-list">
							</div>
							<div class='ip-field text-center'><button type='submit' id='cancel-ticket-btn' class='btn btn-danger'>PROCEED WITH CANCELLATION</button></div>
						</form>

					</div>
				</div>
			</div>
		</div>
	</div>`;

// Switching the active tab
document.querySelector('#upcoming')
	.addEventListener('click',switchActiveTab);

	document.querySelector('#past')
	.addEventListener('click',switchActiveTab);

	document.querySelector('#cancelled')
	.addEventListener('click',switchActiveTab);



	function switchActiveTab(){
		console.log('switch active tab called from', this);
		Array.from(document.getElementsByClassName('nav-link')).forEach(
			function(ele){
				console.log("Element :: ",ele);
				ele.classList.remove('active');
			}
		);
		document.getElementById(this.id).classList.add('active');
	}
// Fetching data from rest controller and populating

	document.querySelector('#upcoming')
	.addEventListener('click',populateJourneys.bind(null,UPCOMING_JOURNEYS));
	
	document.querySelector('#past')
	.addEventListener('click',populateJourneys.bind(null,PAST_JOURNEYS));
	
	document.querySelector('#cancelled')
	.addEventListener('click',populateJourneys.bind(null,CANCELLED_BOOKINGS));

function populateJourneys(type){
	console.log('clicked on '+type);
	
	let xhr = new XMLHttpRequest();
		xhr.open('GET',"profileData?type="+type, true);
		xhr.responseType = 'json';
		xhr.onload = function(){
			if(xhr.status === 200 && xhr.readyState === 4){
				let str = "";
				let res = "";
				console.log("res ", res);
				// if(res == undefined)
				// 	str = "No journeys to display!";
				
					str = `<ul>`;
					res = xhr.response;
					for (let dto of res) {
					console.log(dto);
					console.log(dto.trainName);
					str += `<li>`;
					let parser = new DOMParser();
					var parsedDoc = parser.parseFromString(TICKET_HTML, 'text/html');
					if(type === UPCOMING_JOURNEYS){
						[...parsedDoc.getElementsByClassName('display-none')].forEach(e => e.classList.remove('display-none'));
					}
					parsedDoc.getElementById('train-name').innerText = dto.trainName;
					parsedDoc.getElementById('train-number').innerText = dto.trainNumber;
					parsedDoc.getElementById('train-class').innerText = dto.trainClass;
					parsedDoc.getElementById('pnr').innerText = dto.pnr;
					parsedDoc.getElementById('fromStation').innerText = dto.fromStation;
					parsedDoc.getElementById('doj').innerText = dto.formattedDepartureDate;
					parsedDoc.getElementById('toj').innerText = dto.departureTime;
					parsedDoc.getElementById('duration').innerText = dto.travelTime;
					parsedDoc.getElementById('toStation').innerText = dto.toStation;
					parsedDoc.getElementById('doa').innerText = dto.formattedArrivalDate;
					parsedDoc.getElementById('toa').innerText = dto.arrivalTime;
					

					let passengerCount = Object.keys(dto.travellerVsBerth).length;
					let info = passengerCount + " adult(s)| Boarding at " + dto.fromStation + " | Boarding on " + dto.formattedDepartureDate + ", at  " + dto.departureTime; 
					parsedDoc.getElementById('important-details').innerText = info;
					
					let travellers = dto.travellerVsBerth;
					let travellerDetails = "";
					for([key, value] of Object.entries(travellers)){
						let travellerInfo = "<li><div class=\"flex\">";
						travellerInfo += "<div id=\"passenger-info\">";
						console.log("KEY : ",key);
						console.log("VALUE : ",value);
						let keyStr = String(key);
						let keyArr = keyStr.split(",");
						let nameValue = "";
						let genderValue = "";
						let ageValue = "";
						keyArr.forEach(e =>{
							let subKey = e.split("=")[0];
							let subVal = e.split("=")[1];
							switch (subKey) {
								case "name":
									nameValue = subVal;
									break;
								case "gender":
									genderValue = subVal;
									break;
								case "age":
									ageValue = subVal;
									break;
								default:
									break;
							}
						})
						travellerInfo += (nameValue + "(" + genderValue.substring(0,1) + ", " + ageValue + ")");
						travellerInfo += "</div><div id=\"berth\">";
						if(type !== CANCELLED_BOOKINGS)
							travellerInfo += ("CNF" + "|" + value);
						else
							travellerInfo += ("CAN");
						travellerInfo += "</div></div></li>";
						travellerDetails += travellerInfo;
					}	
					
					// parsedDoc.getElementById('toa').innerText = dto.arrivalTime;	
					
					parsedDoc.getElementById('confirmation-btn-').setAttribute('id', 'confirmation-btn-'+dto.pnr);
					parsedDoc.getElementById('cancel-btn-').setAttribute('id', 'cancel-btn-'+dto.pnr);
					parsedDoc.getElementById('traveller-details').innerHTML = travellerDetails;	
					
					str += new XMLSerializer().serializeToString(parsedDoc);
					str += `</li>`;

				}
				str += `</ul>`
				
				document.querySelector('#populate-journeys').innerHTML = str;
			}else{
				console.error("error occured while fetching upcoming journeys");
			}
		}
		xhr.send();
	
}

document.addEventListener('click', function(event) {
    if (event.target.classList.contains('cancel-btn')) {
		let btnId = document.getElementById(event.target.id).getAttribute('id');
		cancelTicket(btnId);
    }
	if(event.target.classList.contains('confirmation-btn')){
		let btnId = document.getElementById(event.target.id).getAttribute('id');
		console.log("id of confirmation btn : ", btnId);
		sendBookingConfirmation(btnId);
	}

	// if(event.target.id === 'cancel-ticket-btn'){
	// 	confirmCancellation();
	// }
});

document.addEventListener('submit',function(event){
	if(event.target.id === 'ticket-cancellation-form'){
		confirmCancellation();
	}
})


function cancelTicket(btnId){
	console.log(btnId);
	let pnr = String(btnId).split("-")[2];
	let xhr = new XMLHttpRequest();
	let str = "";
	xhr.open('GET', 'fetchTravellers?pnr='+pnr, true);
	xhr.responseType = 'json';
	xhr.onload = function(){
		if(xhr.status === 200 && xhr.readyState === 4){
			let travellers = xhr.response;
			let checkBoxDiv = "";
			let count = 1;
			let str = "";
			for(const traveller of travellers){
				console.log("Traveller ", traveller, "traveller name", traveller.name, "age ", traveller.age, "gender", traveller.gender);
				// let valueToBeDisplayed = `${traveller.name.trim()} (${traveller.gender.charAt(0)}, ${traveller.age}) `;
				let valueToBeDisplayed = traveller.name + "(" + traveller.gender + ", " + traveller.age + ")";
				console.log("valueToBeDisplayed", valueToBeDisplayed, " and count ", count );
				// let checkBoxIp = `<input type='checkbox' id='traveller-'+${count} name="traveller-"+${count} value=${valueToBeDisplayed} >`;
				let checkBoxIp = "<input type='checkbox' id='traveller-" + count + "' name='traveller' value='" + valueToBeDisplayed +"'>  ";
				let label = "<label for='traveller-'" + count + ">" + valueToBeDisplayed + "</label></br>";
				checkBoxIp += label;
				checkBoxDiv += checkBoxIp;
				count++;
			}
			checkBoxDiv += "<input type='hidden' id='pnr' name='pnr' value=" + pnr +" >  ";
			let parser = new DOMParser();
			let parsedDoc = parser.parseFromString(ticketCancellationModal, 'text/html');
			parsedDoc.getElementById('travellers-list').innerHTML = checkBoxDiv;
			parsedDoc.getElementById('ticket-cancellation-form').setAttribute('action','cancelTicket');
			str += new XMLSerializer().serializeToString(parsedDoc);
			document.body.innerHTML += str;
			let modal = new bootstrap.Modal(document.getElementById('ticket-cancellation-modal'));
			if(modal)
				modal.show();
			else
				console.error("ticketCancellationModal is = ",modal);
		}
	}
	xhr.send();	
}

function confirmCancellation(){
	event.preventDefault();
	console.log("confirming cancellation");
	let form = new FormData(document.getElementById('ticket-cancellation-form'), document.getElementById('cancel-ticket-btn'));
	let travellers="";
	for (var [key, value] of form.entries()) { 
 		 console.log(key, value);
		 if(key === 'traveller'){
			travellers += value;
			travellers += ":";
		 }
	}
	travellers = String(travellers).slice(0,-1);
	let xhr = new XMLHttpRequest();
	
	let params = ("pnr="+form.get('pnr') + "&" + "travellers="+travellers);
	xhr.open('post','cancelTicket', true);
	xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xhr.responseType = "text";
	xhr.onload = function(){
		if(xhr.status === 200 && xhr.readyState === 4){
			if(xhr.responseText.startsWith('success')){
				let fare = xhr.responseText.split("-")[1];
				swal.fire({
					icon: "success",
					title : "Your booking has been successfully cancelled",
					text : "Your refund of ₹"+ fare +" will be processed in 1-2 business days!",

					confirmButtonText: "Back to profile"
				}).then(res =>{
					console.log('res',res);
					if(res.isConfirmed){
						sendCancellationConfirmation(form.get('pnr'), travellers);
						location.reload();
						let ele = document.getElementById('upcoming');
						console.log("upcoming ele = ", ele);
						ele.click();
					}
				})
			}else{
				swal.fire({
					icon: "error",
					title : "Your cancellation request could not be processed!",
					confirmButtonText: "Back to profile"
				})
			}	
		}
	}
	xhr.send(params);
}

function sendCancellationConfirmation(pnr, travellers){
	let xhr = new XMLHttpRequest();
	xhr.open('get','sendCancellationConfirmation?pnr='+pnr+"&travellers="+travellers,true);
	xhr.send();
}

function sendBookingConfirmation(id){
	let pnr = String(id).split("-")[2];
	let xhr = new XMLHttpRequest();
	xhr.open('get','resendConfirmation?pnr='+pnr, true);
	xhr.onload = function(){
		if(xhr.status === 200 && xhr.readyState === 4){
			swal.fire({
					icon: "success",
					title : "Booking confirmation email will be sent shortly!",
					timer: 3000
				})
		}
	}
	xhr.send();
}

window.onload = function(){
	console.log('clicking on upcoming tab');
	document.getElementById('upcoming').click();
}



</script>
</html>