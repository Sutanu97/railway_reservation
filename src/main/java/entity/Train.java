package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name="lu_train")
public class Train {
	@Id
	@Column(name="train_number")
	private Integer number;
	@Column(name="train_name")	
	private String name;
	@Column(name="category")
	private String category;
	@Column(name="average_speed")
	private Integer averageSpeed;
	@Column(name="max_permissible_speed")
	private Integer maxSpeed;
	@Column(name="is_premium")
	private boolean isPremium;
	@Column(name="operates_on")
	private String operatesOn;
	@Column(name="type")
	private String type;
	
	
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Integer getAverageSpeed() {
		return averageSpeed;
	}
	public void setAverageSpeed(Integer averageSpeed) {
		this.averageSpeed = averageSpeed;
	}
	public Integer getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(Integer maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public boolean getIsPremium() {
		return isPremium;
	}
	public void setIsPremium(boolean isPremium) {
		this.isPremium = isPremium;
	}
	public String getOperatesOn() {
		return operatesOn;
	}
	public void setOperatesOn(String operatesOn) {
		this.operatesOn = operatesOn;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
