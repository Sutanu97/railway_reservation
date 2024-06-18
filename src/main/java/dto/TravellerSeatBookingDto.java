package dto;

import java.io.Serializable;

public class TravellerSeatBookingDto implements Serializable{

	private static final long serialVersionUID = 1L;
	private Traveller traveller;
	private String confirmationStatus;
	
	
	public String getConfirmationStatus() {
		return confirmationStatus;
	}
	public void setConfirmationStatus(String confirmationStatus) {
		this.confirmationStatus = confirmationStatus;
	}
	public Traveller getTraveller() {
		return traveller;
	}
	public void setTraveller(Traveller traveller) {
		this.traveller = traveller;
	}
	
	
}
