package agent;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import manager.impl.TrainsManagerImpl;

public class TicketAvailabilityUpdationAgent extends CommonAgent{
	
	
	private TrainsManagerImpl trainsManagerImpl;
	private boolean isFullRefresh = false;
	private int dayDiff = 1;
	
	
	@Autowired
	public void setTrainsManagerImpl(TrainsManagerImpl trainsManagerImpl) {
		System.out.println("trainsManagerImpl being set to "+trainsManagerImpl);
		this.trainsManagerImpl = trainsManagerImpl;
	}

	public TicketAvailabilityUpdationAgent() {
	}
	
	public TicketAvailabilityUpdationAgent(boolean isFullRefresh) {
		super();
		this.isFullRefresh = isFullRefresh;
	}

	public void run() {
		if(!isFullRefresh) {
			trainsManagerImpl.updateDepartureAndArrivalDates(dayDiff);
		}else {
			Boolean updateDepatureDateStatus = updateDepartureDates();
			if(updateDepatureDateStatus)
				updateArrivalDates();	
		}
		
	}

	private Boolean updateArrivalDates() {
		return trainsManagerImpl.updateArrivalDates();
	}

	private Boolean updateDepartureDates() {
		LocalDate minDepartureDate = trainsManagerImpl.fetchMinDepartureDateFromTicketAvailability();
		dayDiff = minDepartureDate.until(LocalDate.now().plusDays(1)).getDays();
		return trainsManagerImpl.updateDepartureDates(dayDiff);
	}
	
	public static void main(String args[]) {
		loadContext();
		Arrays.stream(getBeans()).forEach(System.out::println);
		boolean isFullRefresh = Boolean.parseBoolean(args[0]);
		TicketAvailabilityUpdationAgent agent = new TicketAvailabilityUpdationAgent(isFullRefresh);
		agent.setTrainsManagerImpl(context.getBean("trainsManagerImpl", TrainsManagerImpl.class));
		agent.run();
	}
}
