package entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="ticket_availability")
public class TicketAvailability {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pk_ticket_availability_id")
	private Integer pkTicketAvailablityId;
	
	@ManyToOne
	@JoinColumn(name="fk_train_number")
	
	private Train train;
	
	@Column(name="departure_date")
	private LocalDate departureDate;
	
	@Column(name="arrival_date")
	private LocalDate arrivalDate;
	
	@Column(name="class")
	private String category;
	
	@Column(name="source_station_code")
	private String sourceStation;
	
	@Column(name="target_station_code")
	private String targetStation;
	
	@Column(name="tickets_available")
	private Integer ticketsAvailable;
	
	public Integer getPkTicketAvailablityId() {
		return pkTicketAvailablityId;
	}
	public void setPkTicketAvailablityId(Integer pkTicketAvailablityId) {
		this.pkTicketAvailablityId = pkTicketAvailablityId;
	}
	

	public LocalDate getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(LocalDate departureDate) {
		this.departureDate = departureDate;
	}
	public LocalDate getArrivalDate() {
		return arrivalDate;
	}
	public void setArrivalDate(LocalDate arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Integer getTicketsAvailable() {
		return ticketsAvailable;
	}
	public void setTicketsAvailable(Integer ticketsAvailable) {
		this.ticketsAvailable = ticketsAvailable;
	}
	public Train getTrain() {
		return train;
	}
	public void setTrain(Train train) {
		this.train = train;
	}
	public String getSourceStation() {
		return sourceStation;
	}
	public void setSourceStation(String sourceStation) {
		this.sourceStation = sourceStation;
	}
	public String getTargetStation() {
		return targetStation;
	}
	public void setTargetStation(String targetStation) {
		this.targetStation = targetStation;
	}

	
	
}
