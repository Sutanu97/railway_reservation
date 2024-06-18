package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="lu_station")
public class Station {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="pk_station_id")
	private Integer pkStationId;
	@Column(name="station_name")
	private String stationName;
	@Column(name="station_code")
	private String stationCode;
	@Transient
	private String display;
	@Column(name="no_of_platforms")
	private Integer noOfPlatforms;
	private String state;
	public Integer getPkStationId() {
		return pkStationId;
	}
	public void setPkStationId(Integer pkStationId) {
		this.pkStationId = pkStationId;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public Integer getNoOfPlatforms() {
		return noOfPlatforms;
	}
	public void setNoOfPlatforms(Integer noOfPlatforms) {
		this.noOfPlatforms = noOfPlatforms;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
}
