package dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import dto.ReservedTicketDto;
import dto.TravellerSeatBookingDto;
import entity.TicketBookingHistory;
import entity.Train;
import entity.User;

public interface TicketBookingDao {
	public Integer generatePNR();

	public Map<Boolean, Object> bookTicket(TicketBookingHistory ticketBookingHistory);

	public List<TicketBookingHistory> getOccupiedSeatsforDateTrainAndClass(Train train, String trainClass,
			LocalDate dateOfJourney);

	public void saveOccupiedSeats(List<TicketBookingHistory> seatOccupancies);

	public void updateTicketAvailability(String fromStation, String toStation, LocalDate dateOfJourney, Train train,
			String trainClass, int seatsOccupied, List<String> stationsList, boolean isCancellation);

	public Map<String, List<TicketBookingHistory>> fetchBookingsForUser(User loggedInUser);

	public boolean cancelTicket(Integer pnr, List<String> jsonConvertedStringArr);

	public String getRecipientEmail(int pnr);

	public List<TicketBookingHistory> getBookingFromPnr(Integer pnr);
}
