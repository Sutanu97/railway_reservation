package dto;

import java.time.LocalTime;

public class TrainStationTimingDto {
	private Integer trainNumber;
	private String sourceStationCode;
	private String destinationStationCode;
	private Integer sourceDay;
	private Integer destinationDay;
	private LocalTime sourceTime;
	private LocalTime destinationTime;
	private String travelTime;
	public Integer getTrainNumber() {
		return trainNumber;
	}
	public void setTrainNumber(Integer trainNumber) {
		this.trainNumber = trainNumber;
	}
	public String getSourceStationCode() {
		return sourceStationCode;
	}
	public void setSourceStationCode(String sourceStationCode) {
		this.sourceStationCode = sourceStationCode;
	}
	public String getDestinationStationCode() {
		return destinationStationCode;
	}
	public void setDestinationStationCode(String destinationStationCode) {
		this.destinationStationCode = destinationStationCode;
	}
	public Integer getSourceDay() {
		return sourceDay;
	}
	public void setSourceDay(Integer sourceDay) {
		this.sourceDay = sourceDay;
	}
	public Integer getDestinationDay() {
		return destinationDay;
	}
	public void setDestinationDay(Integer destinationDay) {
		this.destinationDay = destinationDay;
	}
	public LocalTime getSourceTime() {
		return sourceTime;
	}
	public void setSourceTime(LocalTime sourceTime) {
		this.sourceTime = sourceTime;
	}
	public LocalTime getDestinationTime() {
		return destinationTime;
	}
	public void setDestinationTime(LocalTime destinationTime) {
		this.destinationTime = destinationTime;
	}
	public String getTravelTime() {
		return travelTime;
	}
	public void setTravelTime(String travelTime) {
		this.travelTime = travelTime;
	}
	
		
}
