package dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

public class ReservedTicketDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer trainNumber;
	private String trainName;
	private String trainClass;
	private Integer pnr;
	private LocalDate dateOfJourney;
	private String fromStation;
	private String toStation;
	private String travelTime;
	private String formattedDepartureDate;
	private String formattedArrivalDate;
	private String departureTime;
	private String arrivalTime;
	private Map<Traveller, String> travellerVsBerth;
	private Boolean isCancelled;
	public Integer getTrainNumber() {
		return trainNumber;
	}
	public void setTrainNumber(Integer trainNumber) {
		this.trainNumber = trainNumber;
	}
	public String getTrainName() {
		return trainName;
	}
	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}
	public String getTrainClass() {
		return trainClass;
	}
	public void setTrainClass(String trainClass) {
		this.trainClass = trainClass;
	}
	public Integer getPnr() {
		return pnr;
	}
	public void setPnr(Integer pnr) {
		this.pnr = pnr;
	}
	public String getFromStation() {
		return fromStation;
	}
	public void setFromStation(String fromStation) {
		this.fromStation = fromStation;
	}
	public String getToStation() {
		return toStation;
	}
	public void setToStation(String toStation) {
		this.toStation = toStation;
	}
	public String getTravelTime() {
		return travelTime;
	}
	public void setTravelTime(String travelTime) {
		this.travelTime = travelTime;
	}
	public String getFormattedDepartureDate() {
		return formattedDepartureDate;
	}
	public void setFormattedDepartureDate(String formattedDepartureDate) {
		this.formattedDepartureDate = formattedDepartureDate;
	}
	public String getFormattedArrivalDate() {
		return formattedArrivalDate;
	}
	public void setFormattedArrivalDate(String formattedArrivalDate) {
		this.formattedArrivalDate = formattedArrivalDate;
	}
	public String getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
	public String getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public Map<Traveller, String> getTravellerVsBerth() {
		return travellerVsBerth;
	}
	public void setTravellerVsBerth(Map<Traveller, String> travellerVsBerth) {
		this.travellerVsBerth = travellerVsBerth;
	}
	public Boolean getIsCancelled() {
		return isCancelled;
	}
	public void setIsCancelled(Boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
	public LocalDate getDateOfJourney() {
		return dateOfJourney;
	}
	public void setDateOfJourney(LocalDate dateOfJourney) {
		this.dateOfJourney = dateOfJourney;
	}
	
	
	
}
