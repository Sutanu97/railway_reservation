package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="ticket_cost")
public class TicketCost {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="pk_ticket_cost_id")
	private Integer pkTicketCostId;
	@ManyToOne
	@JoinColumn(name = "fk_source_station_id")
	private Station stationId1;
	@ManyToOne
	@JoinColumn(name = "fk_destination_station_id")
	private Station stationId2;
	private String fare;
	
	public Integer getPkTicketCostId() {
		return pkTicketCostId;
	}
	public void setPkTicketCostId(Integer pkTicketCostId) {
		this.pkTicketCostId = pkTicketCostId;
	}
	
	public Station getStationId1() {
		return stationId1;
	}
	public void setStationId1(Station stationId1) {
		this.stationId1 = stationId1;
	}
	public Station getStationId2() {
		return stationId2;
	}
	public void setStationId2(Station stationId2) {
		this.stationId2 = stationId2;
	}
	public String getFare() {
		return fare;
	}
	public void setFare(String fare) {
		this.fare = fare;
	}
	
	
	
}
