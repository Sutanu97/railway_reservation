package dto;

import java.util.List;

public class BookTicketDto {
	private List<String> travellerName;
	private List<Integer> travellerAge;
	private List<String> travellerGender;
	private String username;
	private String password;
	private Long phoneNumber;
	private String emailId;
	
	
	public List<String> getTravellerName() {
		return travellerName;
	}
	public void setTravellerName(List<String> travellerName) {
		this.travellerName = travellerName;
	}
	public List<Integer> getTravellerAge() {
		return travellerAge;
	}
	public void setTravellerAge(List<Integer> travellerAge) {
		this.travellerAge = travellerAge;
	}
	public List<String> getTravellerGender() {
		return travellerGender;
	}
	public void setTravellerGender(List<String> travellerGender) {
		this.travellerGender = travellerGender;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	@Override
	public String toString() {
		return "BookTicketDto [travellerName=" + travellerName + ", travellerAge=" + travellerAge + ", travellerGender="
				+ travellerGender + ", username=" + username + ", password=" + password + ", phoneNumber=" + phoneNumber
				+ ", emailId=" + emailId + "]";
	}
	
	
}
