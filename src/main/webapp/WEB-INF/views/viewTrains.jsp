<%@page import="dto.TrainStationTimingDto"%>
<%@page import="entity.Train"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="util.TrainInfoDisplay"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="util.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%!%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>View Trains</title>
<link rel="stylesheet" href="res/css/styles.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css">
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
	<div class="alert alert-warning display-none" id="zero-seat-warning" role = "alert">No seats are availble in this train for the selection. Please try a different date or class!
		<button type="button" id="alert-close-btn" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
	</div>
	<div class="journey-info mb-3">
		<nav class="navbar navbar-expand-lg p-3">
			<div class="container-fluid">
				<form class="d-flex" action="searchTrainsHandler" method="get">
					<div class="card mx-2 height6rem">
						<div class="card-body">
							<p class="light mb-0">From Station</p>
							<input class="form-control hidden-border" type="text"
								name="from_station" value="${fromStation }"
								placeholder="Enter source station">
						</div>
					</div>
					<div class="card mx-2 height6rem">
						<div class="card-body">
							<p class="light mb-0">To Station</p>
							<input class="form-control hidden-border" type="text"
								name="to_station" value="${toStation }"
								placeholder="Enter destination station">
						</div>
					</div>
					<div class="card mx-2 height6rem">
						<div class="card-body">
							<p class="light mb-0">Date of Journey
							<p>
								<input class="form-control hidden-border" type="text"
									name="date_of_journey" value="${doj }" placeholder="dd/MM/yyyy">
						</div>
					</div>
					<button type="submit" class="btn btn-secondary mx-2 my-auto px-5"
						style="height: 30%; margin-left: 6em !important">SEARCH</button>
				</form>
			</div>
		</nav>
	</div>
	<div class="show-trains text-center container-fluid">
		<c:forEach var="train" items="${trains }">
			<div class="card mb-3">
				<c:if test="${train.isPremium}">
					<div class="premium-tag"></div>
				</c:if>
				<div class="card-body">
					<div class="train-info">
						<div class="row">
							<div class="col-md-4 left-info">
								<p class="train-name">${train.name }</p>
								<p class="train-number">${train.number }</p>
							</div>
							<div class="col-md-8 right-info">
								<div class="row">
									<div class="col-md-4">
										<p class="station">${fromStation }</p>
										<p class="date">${formattedFromDate }</p>
										<p class="time">
											<c:out value="${trainNumberVsFormattedDepartureTime[train.number]}"></c:out>
										</p>

									</div>
									<div class="col-md-4">
										<div class="journey-duration d-flex">
											<div class="float-start journey-duration-line"></div>
											<div class="duration">
												
												<c:out value="${trainNumberVsTravelTime[train.number] }"></c:out>
											</div>
											<div class="float-end journey-duration-line"></div>
										</div>
									</div>
									<div class="col-md-4">
										<p class="station">${toStation }</p>
										
										<p class="date">
											<c:out value="${trainNumberVsFormattedArrivalDate[train.number] }"></c:out>
										</p>
										
										<p class="time">
											<c:out value="${trainNumberVsFormattedArrivalTime[train.number] }"></c:out>
										</p>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="train-classes d-flex">
						<c:forEach var="entryTrain" items="${ticketsAvailablilityMap }">
							<c:if test="${train.number == entryTrain.key}">
								<c:forEach var="entryClass" items="${entryTrain.value }">
									<div class="card seat-avl-card" id="${train.number }-${entryClass}">
										<div class="card-body">
											<div class="container d-flex">
												<div class="class-name">
													<p class="light scale-up">
														<c:out value="${entryClass.key }"></c:out>
													</p>
												</div>
												<div class="fare">
													<c:if test="${entryClass.key == 'First AC'}">
														<p class="scale-up float-end">
														<c:choose>
															<c:when test="${train.isPremium }">
																<c:out value="₹${classFareDto.class_1A * Constants.PREMIUM_FARE_MULTIPLIER}"></c:out>
															</c:when>
															<c:otherwise>
																<c:out value="₹${classFareDto.class_1A }"></c:out>	
															</c:otherwise>
															</c:choose>
														</p>
													</c:if>
													<c:if test="${entryClass.key == 'Second AC'}">
														<p class="scale-up float-end">
														<c:choose>
															<c:when test="${train.isPremium }">
																<c:out value="₹${classFareDto.class_2A * Constants.PREMIUM_FARE_MULTIPLIER}"></c:out>
															</c:when>
															<c:otherwise>
																<c:out value="₹${classFareDto.class_2A }"></c:out>	
															</c:otherwise>
															</c:choose>
														</p>
													</c:if>
													<c:if test="${entryClass.key == 'Third AC'}">
														<p class="scale-up float-end">
														<c:choose>
															<c:when test="${train.isPremium }">
																<c:out value="₹${classFareDto.class_3A * Constants.PREMIUM_FARE_MULTIPLIER}"></c:out>
															</c:when>
															<c:otherwise>
																<c:out value="₹${classFareDto.class_3A }"></c:out>	
															</c:otherwise>
															</c:choose>
														</p>
													</c:if>
													<c:if test="${entryClass.key == 'Executive Chair Car'}">
														<p class="scale-up float-end">
														<c:choose>
															<c:when test="${train.isPremium }">
																<c:out value="₹${classFareDto.class_EC * Constants.PREMIUM_FARE_MULTIPLIER}"></c:out>
															</c:when>
															<c:otherwise>
																<c:out value="₹${classFareDto.class_EC }"></c:out>	
															</c:otherwise>
															</c:choose>
														</p>
													</c:if>
													<c:if test="${entryClass.key == 'AC Chair Car'}">
														<p class="scale-up float-end">
														<c:choose>
															<c:when test="${train.isPremium }">
																<c:out value="₹${classFareDto.class_CC * Constants.PREMIUM_FARE_MULTIPLIER}"></c:out>
															</c:when>
															<c:otherwise>
																<c:out value="₹${classFareDto.class_CC }"></c:out>	
															</c:otherwise>
															</c:choose>
														</p>
													</c:if>
													<c:if test="${entryClass.key == 'Sleeper'}">
														<p class="scale-up float-end">
														<c:choose>
															<c:when test="${train.isPremium }">
																<c:out value="₹${classFareDto.class_sleeper * Constants.PREMIUM_FARE_MULTIPLIER}"></c:out>
															</c:when>
															<c:otherwise>
																<c:out value="₹${classFareDto.class_sleeper }"></c:out>	
															</c:otherwise>
															</c:choose>
														</p>
													</c:if>
												</div>
											</div>
											<div class="container">
												<p class="scale-up seats-avl-count">
													Available
													<c:out value="${entryClass.value}"></c:out>
												</p>
											</div>
										</div>
									</div>
								</c:forEach>
							</c:if>
						</c:forEach>

					</div>
				</div>
			</div>
		</c:forEach>
	</div>
	<script src="https://code.jquery.com/jquery-3.7.0.min.js"
		integrity="sha256-2Pmvv0kuTBOenSvLm6bvfBSSHrUJ+3A7x6P5Ebd07/g="
		crossorigin="anonymous"></script>
	<script type="text/javascript" src="res/js/script.js"></script>
	<script type="text/javascript"
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>