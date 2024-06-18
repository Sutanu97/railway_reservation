package manager;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import dto.ReservedTicketDto;
import dto.Traveller;
import entity.TicketBookingHistory;
import entity.Train;
import entity.User;

public interface TicketBookingManager {
	public Integer generatePNR();

	public Map<String, Object> bookTicket(User user, Train train, String fromStation, String toStation, String trainClass, String travelTime, String departureTime, String arrivalTime, LocalDate doj, List<Traveller> travellers, List<String> stationsList, String recipientEmailId, Double totalFare);

	public Map<String, List<ReservedTicketDto>> fetchBookingsForUser(User loggedInUser);

	public boolean cancelTicket(Integer pnr, String[] travellerArr);

	public String getRecipientEmail(int pnr);

	public List<TicketBookingHistory> getBookingFromPnr(Integer pnr);

	public void updateTicketAvailability(String fromStation, String toStation, LocalDate dateOfJourney, Train train,
			String trainClass, int noOfTravellers, List<String> stationsList, boolean isCancellation);
}
