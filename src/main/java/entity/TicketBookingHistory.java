package entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;



@Entity
@Table(name="ticket_booking_history")
public class TicketBookingHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pk_seat_occupancy_id")
	private Integer pkSeatOccupancyId;
	
	@Column(name="pnr_number")
	private Integer pnrNumber;
	
	@ManyToOne
	@JoinColumn(name="fk_user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "fk_train_number")
	private Train train;
	
	@Column(name = "train_departure_date")
	private LocalDate trainDepartureDate;
	
	@Column(name = "date_of_journey")
	private LocalDate dateOfJourney;
	
	@Column(name = "departure_time")
	private LocalTime departureTime;
	
	@Column(name = "arrival_date")
	private LocalDate arrivalDate;
	
	@Column(name = "arrival_time")
	private LocalTime arrivalTime;
	
	@Column(name = "travel_time")
	private LocalTime travelTime;
	
	@Column(name = "class")
	private String trainClass;
	
	@Column(name = "coach")
	private String coach;
	
	@Column(name = "seat_number")
	private Integer seatNumber;
	
	@Column(name = "source_station_code")
	private String sourceStation;
	
	@Column(name = "target_station_code")
	private String destinationStation;
	
	@Column(name = "traveller_details")
	private String travellerDetails;

	@Column(name = "is_cancelled")
	private Boolean isCancelled;
	
	@Column(name = "confirmation_email")
	private String confirmationEmailId;
	
	@Column(name = "fare")
	private Double fare;
	
	public Integer getPkSeatOccupancyId() {
		return pkSeatOccupancyId;
	}

	public void setPkSeatOccupancyId(Integer pkSeatOccupancyId) {
		this.pkSeatOccupancyId = pkSeatOccupancyId;
	}

	public Integer getPnrNumber() {
		return pnrNumber;
	}

	public void setPnrNumber(Integer pnrNumber) {
		this.pnrNumber = pnrNumber;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Train getTrain() {
		return train;
	}

	public void setTrain(Train train) {
		this.train = train;
	}

	public LocalDate getDateOfJourney() {
		return dateOfJourney;
	}

	public void setDateOfJourney(LocalDate dateOfJourney) {
		this.dateOfJourney = dateOfJourney;
	}

	public String getTrainClass() {
		return trainClass;
	}

	public void setTrainClass(String trainClass) {
		this.trainClass = trainClass;
	}

	public String getCoach() {
		return coach;
	}

	public void setCoach(String coach) {
		this.coach = coach;
	}

	public Integer getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(Integer seatNumber) {
		this.seatNumber = seatNumber;
	}

	public String getSourceStation() {
		return sourceStation;
	}

	public void setSourceStation(String sourceStation) {
		this.sourceStation = sourceStation;
	}

	public String getDestinationStation() {
		return destinationStation;
	}

	public void setDestinationStation(String destinationStation) {
		this.destinationStation = destinationStation;
	}

	public String getTravellerDetails() {
		return travellerDetails;
	}

	public void setTravellerDetails(String travellerDetails) {
		this.travellerDetails = travellerDetails;
	}

	public Boolean getIsCancelled() {
		return isCancelled;
	}

	public void setIsCancelled(Boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	public LocalDate getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(LocalDate arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public LocalTime getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(LocalTime travelTime) {
		this.travelTime = travelTime;
	}

	public LocalTime getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(LocalTime departureTime) {
		this.departureTime = departureTime;
	}

	public LocalTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(LocalTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	public LocalDate getTrainDepartureDate() {
		return trainDepartureDate;
	}

	public void setTrainDepartureDate(LocalDate trainDepartureDate) {
		this.trainDepartureDate = trainDepartureDate;
	}

	public String getConfirmationEmailId() {
		return confirmationEmailId;
	}

	public void setConfirmationEmailId(String confirmationEmailId) {
		this.confirmationEmailId = confirmationEmailId;
	}

	public Double getFare() {
		return fare;
	}

	public void setFare(Double fare) {
		this.fare = fare;
	}
	
	
}
 