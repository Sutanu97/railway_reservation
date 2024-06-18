package entity;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="train_station_timing")
public class TrainStationTiming {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="pk_train_station_timing_id")
	private Integer pkTrainStationTimingId;
	@Column(name="fk_train_number")
	private Integer fkTrainNumber;
	@Column(name="station_code")
	private String stationCode;
	private Integer day;
	@Column(name="arrival_time")
	private LocalTime arrivalTime;
	@Column(name="departure_time")
	private LocalTime departureTime;
	
	
	public Integer getPkTrainStationTimingId() {
		return pkTrainStationTimingId;
	}
	public void setPkTrainStationTimingId(Integer pkTrainStationTimingId) {
		this.pkTrainStationTimingId = pkTrainStationTimingId;
	}
	public Integer getFkTrainNumber() {
		return fkTrainNumber;
	}
	public void setFkTrainNumber(Integer fkTrainNumber) {
		this.fkTrainNumber = fkTrainNumber;
	}
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	public LocalTime getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(LocalTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public LocalTime getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(LocalTime departureTime) {
		this.departureTime = departureTime;
	}
	
	
	
}
