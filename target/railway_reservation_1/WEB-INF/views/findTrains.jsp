<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Find Trains</title>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css">
</head>
<body>
	<div class="container">
		<div class="card bg-transparent">
			<div class="card-header">
				<b>Find trains</b>
			</div>
			<div class="card-body">
				<div class="alert alert-secondary" id="date-range">

				</div>
				<form action="searchTrainsHandler" method="get">
					<input type="text" name="from_station" id="from_station"
						placeholder="Enter source station" class="form-control mb-3"
						<%-- onkeyup="getStations(<c:out value="${stations }"></c:out>, this)" --%>
						>
					<div class="showStations" id="fromStations"></div>
					<input type="text" name="to_station" id="to_station"
						placeholder="Enter destination station" class="form-control mb-3"
						<%-- onkeyup="getStations(<c:out value="${stations }"></c:out>, this)" --%>
						>
					<div class="showStations" id="toStations"></div>
					<input type="date" min="" max=""
					name="date_of_journey" id="date_of_journey"
						placeholder="Enter the date of journey"
						class="form-control mb-3">
					<div class="text-center">
						<button type="submit" value="search" class="btn btn-secondary">FIND
							TRAINS</button>
					</div>

				</form>
			</div>
		</div>
	</div>
	<script type="text/javascript"
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"></script>
		<script>
			let minDate = new Date();
	let newMinDate = new Date(minDate);
	console.log(newMinDate);
	let dayOfMaxDate = minDate.getDate() + 7;
	minDate.setDate(dayOfMaxDate);
	document.getElementById('date-range').innerText = 'Please select a date between '+newMinDate.toLocaleDateString()+' and '+minDate.toLocaleDateString();
		</script>
</body>

</html>