<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	
	<div class="container reviewContainer showTickets mt-5">
		<div class="card p-4">
			<div class="row">
				<div class="col-md-8">
					<div class="train-name">
						<c:out value="${trainName }"></c:out>
					</div>
					<div class="train-info d-flex">
						<div class="train-number">
							<c:out value="${trainNumber }"></c:out>
						</div>
						<div class="v-line mx-3"></div>
						<div class="train-class">
							<c:out value="${trainClass }"></c:out>
						</div>
					</div>
				</div>
				<div class="col-md-4">PNR: ${pnr }</div>
			</div>
			<!-- train no. and pnr -->
			<div class="row mt-4">
				<div class="col-md-4">
					<p class="station fs-5">{fromStation}</p>
					<p class="date">{formattedFromDate}</p>

					<p class="time fw-light fs-5">
						<c:out
							value="${formattedDepartureTime}"></c:out>
					</p>
				</div>
				<div class="col-md-4">
					<div class="journey-duration d-flex">
						<div class="float-start journey-duration-line"></div>
						<div class="duration">

							<c:out
								value="${travelTime}"></c:out>
						</div>
						<div class="float-end journey-duration-line"></div>
					</div>
				</div>
				<div class="col-md-4">
					<p class="station fs-5">${toStation }</p>

					<p class="date">
						<c:out
							value="${formattedArrivalDate }"></c:out>
					</p>
					<p class="time fw-light fs-5">
						<c:out
							value="${formattedArrivalTime}"></c:out>
					</p>
				</div>
			</div>
			<%--  stations and time required--%>

			<div class="text-center important-details">
				<c:out
					value="${noOfPassengers} adult(s)| Boarding at ${fromStation } | Boarding on ${formattedFromDate }, at ${formattedDepartureTime}"></c:out>
			</div>
			<%-- desc --%>
			<div class="booking-list-passenger-info">
				<%-- passenger details --%>
				<div class="mt-2">
					<div class="flex">
						<h6>Travellers</h6>
						<h6>BookingStatus</h6>
					</div>
					
						<c:set var="counter" value="1"></c:set>
						<c:forEach var="entry" items="${travellerVsBerth }">
							<div class="flex">
								<div>
									<c:out value="${counter}. ${entry.key.name } (${fn:substring(entry.key.gender, 0,1) }, ${entry.key.age })"></c:out>
								</div>
								<div>
									<c:out value="CNF | ${entry.value }"></c:out>
								</div>
							</div>
							
							<c:set var="counter" value="${counter + 1 }"></c:set>
						</c:forEach>
					
				</div>
			</div>
		</div>

	</div>
</body>
</html>