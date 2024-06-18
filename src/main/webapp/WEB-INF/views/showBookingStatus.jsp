<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Booking Confirmation</title>
<link rel="stylesheet" href="res/css/styles.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
	<div class="container reviewContainer newTicketBooking mt-5">
		<h5 class="font-monospace">Thank you for booking with us!</h5>
		<div class="card p-4">
			<div class="row">
				<div class="col-md-8">
					<div class="train-name">
						<c:out value="${sessionScope.train.name }"></c:out>
					</div>
					<div class="train-info d-flex">
						<div class="train-number">
							<c:out value="${sessionScope.train.number }"></c:out>
						</div>
						<div class="v-line mx-3"></div>
						<div class="train-class">
							<c:out value="${sessionScope.trainClass }"></c:out>
						</div>
					</div>
				</div>
				<div class="col-md-4">PNR: ${bookingStatus['pnr'] }</div>
			</div>
			<!-- train no. and pnr -->
			<div class="row mt-4">
				<div class="col-md-4">
					<p class="station fs-5"><%=session.getAttribute("fromStation")%></p>
					<p class="date"><%=session.getAttribute("formattedFromDate")%></p>

					<p class="time fw-light fs-5">
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
					<p class="station fs-5">${sessionScope.toStation }</p>

					<p class="date">
						<c:out
							value="${sessionScope.trainNumberVsFormattedArrivalDate[train.number] }"></c:out>
					</p>
					<p class="time fw-light fs-5">
						<c:out
							value="${sessionScope.trainNumberVsFormattedArrivalTime[train.number] }"></c:out>
					</p>
				</div>
			</div>
			<%--  stations and time required--%>

			<div class="text-center important-details">
				<c:out
					value="${sessionScope.counter -1 } adult(s)| Boarding at ${sessionScope.fromStation } | Boarding on ${sessionScope.formattedFromDate }, at ${sessionScope.trainNumberVsFormattedDepartureTime[train.number] }"></c:out>
			</div>
			<%-- desc --%>
			<div class="passenger-info">
				<%-- passenger details --%>
				<div class="mt-2">
					<div class="flex">
						<h6>Travellers</h6>
						<h6>BookingStatus</h6>
					</div>
					
						<c:set var="counter" value="1"></c:set>
						<c:forEach var="entry" items="${bookingStatus['travellerVsBerth'] }">
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
	
	<!-- ticket list for a logged in user begins -->

	
</body>

<script type="text/javascript" src="res/js/script.js"></script>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"
	integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
	crossorigin="anonymous"></script>
</html>