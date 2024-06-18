package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import dto.ClassFareDto;
import dto.TrainStationTimingDto;
import entity.Station;
import entity.Train;
import jakarta.servlet.http.HttpSession;
import manager.TrainsManager;
import util.TrainInfoDisplay;
import util.TrainsUtil;

@Controller
public class TrainSearchController {
	
	@Autowired
	private TrainsManager trainsManager;

	@RequestMapping("/home")
	public String findTrains(Model model, HttpSession session, @RequestParam(name = "isLoggedOut", required = false, defaultValue = "false") String isLoggedOut) {
		//fetching the list of stations. This will be sent to home jsp.
		if(isLoggedOut.equals("true")) {
			session.setAttribute("CurrentUser", null);
		}
		List<Station> stations = trainsManager.fetchStations();
		List<String> stationNames = new ArrayList<>();
		for(Station stn : stations) {
			stn.setDisplay(stn.getStationName() + " : " + stn.getStationCode()) ;
			stationNames.add(stn.getDisplay());
		}
		model.addAttribute("stations",stationNames);
		return "home";
	}
	
	@RequestMapping(path = "/searchTrainsHandler", method = RequestMethod.GET)
	public String getTrains(@RequestParam("from_station") String fromStation, 
			@RequestParam("to_station") String toStation, 
			@RequestParam("date_of_journey") String doj, Model model, HttpSession session) {
		LocalDate dateOfJourney = TrainsUtil.getLocalDateFromString(doj);
		String day = TrainInfoDisplay.getDayFromDate(dateOfJourney).toString();
		List<Train> trains = trainsManager.fetchTrainsBetweenStations(fromStation, toStation, day);
		List<TrainStationTimingDto> timingDtos = trainsManager.fetchTrainTimings(fromStation, toStation);
		Map<Integer, LinkedHashMap<String,Integer>> ticketsAvailablilityMap =  trainsManager.fetchNoOfTicketsAvailable(dateOfJourney, fromStation, toStation);
		ClassFareDto classFareDto =  trainsManager.fetchBaseFareBetweenStations(fromStation, toStation);
		Map<Integer, String> trainNumberVsTravelTime = new HashMap<>();
		Map<Integer, String> trainNumberVsFormattedArrivalDate = new HashMap<>();
		Map<Integer, String> trainNumberVsFormattedDepartureTime = new HashMap<>();
		Map<Integer, String> trainNumberVsFormattedArrivalTime = new HashMap<>();
		for(TrainStationTimingDto dto : timingDtos){
			trainNumberVsTravelTime.put(dto.getTrainNumber(), dto.getTravelTime());
			trainNumberVsFormattedArrivalDate.put(dto.getTrainNumber(), TrainInfoDisplay.getArrivalDate(dto,fromStation, toStation, dateOfJourney));
			trainNumberVsFormattedDepartureTime.put(dto.getTrainNumber(), TrainInfoDisplay.getFormattedTime(dto.getSourceTime()));
			trainNumberVsFormattedArrivalTime.put(dto.getTrainNumber(), TrainInfoDisplay.getFormattedTime(dto.getDestinationTime()));
		}
		trains = TrainInfoDisplay.organizeTrainsOnTravelTime(trains,timingDtos);
		String formattedFromDate = TrainInfoDisplay.getFormattedDate(dateOfJourney, "MMMM dd") + " "+ day;
		
		model.addAttribute("trains",trains);
		model.addAttribute("formattedFromDate",formattedFromDate);
		model.addAttribute("trainNumberVsTravelTime",trainNumberVsTravelTime);
		model.addAttribute("trainNumberVsFormattedArrivalDate", trainNumberVsFormattedArrivalDate);
		model.addAttribute("trainNumberVsFormattedDepartureTime", trainNumberVsFormattedDepartureTime);
		model.addAttribute("trainNumberVsFormattedArrivalTime", trainNumberVsFormattedArrivalTime);
		model.addAttribute("ticketsAvailablilityMap", ticketsAvailablilityMap);
		model.addAttribute("classFareDto", classFareDto);
		model.addAttribute("fromStation",fromStation);
		model.addAttribute("toStation",toStation);
		model.addAttribute("doj",dateOfJourney);
		model.addAttribute("day",day);
		
		addAttributesToSession(session, trains, formattedFromDate, trainNumberVsTravelTime, trainNumberVsFormattedArrivalDate, trainNumberVsFormattedDepartureTime, trainNumberVsFormattedArrivalTime, ticketsAvailablilityMap, classFareDto, fromStation, toStation, dateOfJourney, day);
		return "viewTrains";
	}

	private void addAttributesToSession(HttpSession session, List<Train> trains, String formattedFromDate,
			Map<Integer, String> trainNumberVsTravelTime,
			Map<Integer, String> trainNumberVsFormattedArrivalDate,
			Map<Integer, String> trainNumberVsFormattedDepartureTime,
			Map<Integer, String> trainNumberVsFormattedArrivalTime,
			Map<Integer, LinkedHashMap<String, Integer>> ticketsAvailablilityMap, ClassFareDto classFareDto,
			String fromStation, String toStation, LocalDate dateOfJourney, String day) {
		session.setAttribute("trains",trains);
		session.setAttribute("formattedFromDate",formattedFromDate);
		session.setAttribute("trainNumberVsTravelTime",trainNumberVsTravelTime);
		session.setAttribute("trainNumberVsFormattedArrivalDate", trainNumberVsFormattedArrivalDate);
		session.setAttribute("trainNumberVsFormattedDepartureTime", trainNumberVsFormattedDepartureTime);
		session.setAttribute("trainNumberVsFormattedArrivalTime", trainNumberVsFormattedArrivalTime);
		session.setAttribute("ticketsAvailablilityMap", ticketsAvailablilityMap);
		session.setAttribute("classFareDto", classFareDto);
		session.setAttribute("fromStation",fromStation);
		session.setAttribute("toStation",toStation);
		session.setAttribute("doj",dateOfJourney);
		session.setAttribute("day",day);
	}
	
}
