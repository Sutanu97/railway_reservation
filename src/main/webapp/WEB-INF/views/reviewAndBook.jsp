<%@page import="util.TrainInfoDisplay"%>
<%@page import="entity.Train"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Review and Book</title>
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
	<div class="container reviewContainer mt-5">
		<div class="card reviewCard">
			<h6>Review your booking</h6>
			<div class="train-name mt-2">
				<c:out value="${train.name }"></c:out>
			</div>
			<div class="train-info d-flex">
				<div class="train-number">
					<c:out value="${train.number }"></c:out>
				</div>
				<div class="v-line mx-3"></div>
				<div class="train-class">
					<c:out value="${sessionScope.trainClass }"></c:out>
				</div>
			</div>
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
			<div class="mt-2">
				<h6>Travellers</h6>
				<c:set var="counter" value="1" scope="session"></c:set>
				<c:forEach var="item" items="${travellers }">
					<div>
						<c:out
							value="${sessionScope.counter}. ${item.name } (${fn:substring(item.gender, 0,1) }, ${item.age })"></c:out>
					</div>
					<c:set var="counter" value="${sessionScope.counter + 1 }"
						scope="session"></c:set>
				</c:forEach>

			</div>
			<hr>
			<div class="pay-summary">
				<h6>Fare summary</h6>
				<div class="flex">
					<div>Fare per traveller</div>
					<div>
						<c:out value="${totalFarePerPassenger }"></c:out>
					</div>
				</div>
				<div class="flex">
					<div>Total fare</div>
					<div>
						<c:out value="${totalFarePerPassenger*(sessionScope.counter-1) }"></c:out>
					</div>
				</div>

			</div>
			<form class="text-center" action="sendConfirmation">
				<button type="submit" class="btn btn-secondary my-3">PAY
					AND BOOK</button>
			</form>
		</div>
	</div>
</body>
<script type="text/javascript" src="res/js/script.js"></script>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"
	integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
	crossorigin="anonymous"></script>
</html>