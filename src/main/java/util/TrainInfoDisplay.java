package util;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import dto.TrainStationTimingDto;
import entity.Train;
import manager.TrainsManager;

@Component
public class TrainInfoDisplay {
	
	private TrainsManager trainsManager;
	
	
	public TrainInfoDisplay(TrainsManager trainsManager) {
		super();
		this.trainsManager = trainsManager;
	}

	public static String getFormattedDate(LocalDate doj, String formatterStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatterStr);
		return doj.format(formatter);
		
	}
	
	public static String getArrivalDate(TrainStationTimingDto dto, String sourceStation, String destinationStation,
			LocalDate doj) {
		byte day = 0;
		String formattedToDate = "";
		int dayDiff = dto.getDestinationDay() - dto.getSourceDay();
//		cal.set(Integer.parseInt(doj.split("/")[2]), Integer.parseInt(doj.split("/")[1]) -1,
//				Integer.parseInt(doj.split("/")[0]) + dayDiff);
//		SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd");
		formattedToDate = formatter.format(doj.plusDays(dayDiff));
//		Integer newDayOfMonth = Integer.parseInt(doj.split("/")[0]) + dayDiff;
//		String arrivalDate = newDayOfMonth + "/" + doj.substring(doj.indexOf('/')+1);
		DayOfWeek dayOfWeek = getDayFromDate(doj.plusDays(dayDiff));
		formattedToDate += " " + dayOfWeek;
		return formattedToDate;
	}

	public static String getTravelTime(TrainStationTimingDto dto, String fromStation, String toStation) {
		int hrs = 0, mins = 0;
		int sourceDay = dto.getSourceDay();
		int destinationDay = dto.getDestinationDay();
		hrs = (destinationDay - sourceDay) * 24;
		hrs += (int) ChronoUnit.HOURS.between(dto.getSourceTime(), dto.getDestinationTime());
		mins += (int) ChronoUnit.MINUTES.between(dto.getSourceTime(), dto.getDestinationTime()) % 60;
		if (mins < 0) {
			hrs--;
			mins += 60;
		}
		return hrs + "h " + mins + "m";

	}
	
	public static String getFormattedTime(LocalTime time) {
		DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("hh:mm a");
		String formattedTime = formatter.format(time);
		return formattedTime;
	}
	
	public static DayOfWeek getDayFromDate(LocalDate doj) {
		DayOfWeek dayOfWeek = doj.getDayOfWeek();
		return dayOfWeek;
	}

	public static List<Train> organizeTrainsOnTravelTime(List<Train> trains, List<TrainStationTimingDto> timingDtos) {
		List<Train> trainsArranged = new ArrayList<>();
		ListIterator<TrainStationTimingDto> iterator = timingDtos.listIterator();
		List<Integer> selectedTrainNumbers = trains.stream().map(e -> e.getNumber()).collect(Collectors.toList());
		while(iterator.hasNext()) {
			TrainStationTimingDto dto = iterator.next();
			if(!selectedTrainNumbers.contains(dto.getTrainNumber())) {
				iterator.remove();
			}
		}
		Comparator<TrainStationTimingDto> travelTimeComparator = new Comparator<TrainStationTimingDto>() {

			@Override
			public int compare(TrainStationTimingDto o1, TrainStationTimingDto o2) {
				String t1 = o1.getTravelTime();
				String t2 = o2.getTravelTime();
				int hrs1 = Integer.parseInt(t1.substring(0,t1.indexOf('h')));
				int hrs2 = Integer.parseInt(t2.substring(0,t2.indexOf('h')));
				if(hrs1 != hrs2) {
					return (hrs1 > hrs2) ? 1:-1;
				}
				int mins1 = Integer.parseInt(t1.substring(t1.indexOf('h')+1));
				int mins2 = Integer.parseInt(t2.substring(t2.indexOf('h')+1));
				if(mins1 != mins2) {
					return (mins1 > mins2)? 1:-1;
				}
				return 0;
			}
			
		};
		Comparator<TrainStationTimingDto> startingTimeComparator = new Comparator<TrainStationTimingDto>() {

			@Override
			public int compare(TrainStationTimingDto o1, TrainStationTimingDto o2) {
				return o1.getSourceTime().compareTo(o2.getSourceTime());
			}
			
		};
		Collections.sort(timingDtos,startingTimeComparator);
		for(TrainStationTimingDto dto : timingDtos) {
			Integer trno = dto.getTrainNumber();
			for(Train t : trains) {
				if(t.getNumber().intValue() == trno.intValue())
					trainsArranged.add(t);
			}
		}
		return trainsArranged;
	}
	
	public static String getAbbreviatedOperatesOn(String operatesOn) {
		return Arrays.asList(operatesOn.split(",")).stream()
		.map(day -> day.substring(0,3))
		.collect(Collectors.joining(" "));
	}
	
}
