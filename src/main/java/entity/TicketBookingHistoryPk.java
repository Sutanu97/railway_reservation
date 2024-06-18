package entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;



@Embeddable
public class TicketBookingHistoryPk implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer pnrNumber;
	@ManyToOne
	private User fkUserId;

	public TicketBookingHistoryPk() {
	}

	public TicketBookingHistoryPk(Integer pnrNumber, User fkUserId) {
		super();
		this.pnrNumber = pnrNumber;
		this.fkUserId = fkUserId;
	}

	public Integer getPnrNumber() {
		return pnrNumber;
	}

	public void setPnrNumber(Integer pnrNumber) {
		this.pnrNumber = pnrNumber;
	}

	public User getFkUserId() {
		return fkUserId;
	}

	public void setFkUserId(User fkUserId) {
		this.fkUserId = fkUserId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fkUserId, pnrNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TicketBookingHistoryPk other = (TicketBookingHistoryPk) obj;
		return Objects.equals(fkUserId, other.fkUserId) && Objects.equals(pnrNumber, other.pnrNumber);
	}

	
	
	
}
