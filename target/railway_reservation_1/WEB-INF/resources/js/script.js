var staticCounter = 1;

document.querySelectorAll('.seat-avl-card').forEach(card =>{
	card.addEventListener('click',function(){
		var cardId = card.getAttribute('id');
		var trainNo = cardId.split("-")[0];
		var trainClass = cardId.split("-")[1].split("=")[0];
		var ticketsAvl = cardId.split("=")[1];
		console.log(trainNo);
		console.log(trainClass);
		console.log(ticketsAvl);
		if(ticketsAvl == 0){
			document.getElementById('zero-seat-warning').classList.remove('display-none');
		}else{
			var bookTicketUrl = "/railway_reservation_1/bookTicket?trainNo=" + encodeURIComponent(trainNo) + "&trainClass=" + encodeURIComponent(trainClass) + "&ticketsAvl=" + encodeURIComponent(ticketsAvl);
			window.location.href=bookTicketUrl;  
		}
		
	})
})

document.getElementById('alert-close-btn').addEventListener('click',function(){
	document.getElementById('zero-seat-warning').classList.add('display-none');
})

	
	/*
	var counter = 1;
	document
			.querySelector('#addTravellerForm')
			.addEventListener(
					'submit',
					function(event) {
						console.log('submitted!');
						event.preventDefault();
						var form = new FormData(this);

						let name = form.get('traveller_name');
						let age = form.get('traveller_age');
						let gender = form.get('traveller_gender');

						var nameGenderSubDiv = document.createElement('div');
						nameGenderSubDiv.innerHTML = name + "(" + gender.charAt(0)
								+ ")";

						var ageSubDiv = document.createElement('div');
						ageSubDiv.innerHTML = age + " years";

						var newDiv = document.createElement('div');
						newDiv.className = "traveller";

						newDiv.appendChild(nameGenderSubDiv);
						newDiv.appendChild(ageSubDiv);
						document.querySelector('#showPassengers').appendChild(
								newDiv);
						document.querySelector('#addTravellerModal').append;

						var alertDiv = document.createElement('div');
						alertDiv.innerHTML = '<div class="alert alert-success alert-dismissible fade show" role="alert">Traveller saved! You can add more travellers or click the close button to dismiss'
								+ '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button></div>';
						this.prepend(alertDiv);

					})
					*/

document.querySelector('#payment-btn').addEventListener('click',event =>{
	var travellers = [];
	document.querySelectorAll('.traveller').forEach(ele =>{
		var travellerDetails = ele.children;
		let nameGender = travellerDetails[0].innerHTML;
		let age = travellerDetails[1].innerHTML;
		travellers.push(travellerDetails);
		
	})
})


function incrementStaticCounter(){
	staticCounter ++;
}


