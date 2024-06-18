package dao;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import dto.ClassFareDto;
import dto.TrainStationTimingDto;
import entity.Station;
import entity.Train;

public interface TrainsDao {

	public List<Station> fetchStations();

	public List<Train> fetchTrainsBetweenStations(String fromStation, String toStation, String doj);

	public List<TrainStationTimingDto> fetchTrainTimings(String sourceStation, String destinationStation);

	public Map<Integer, LinkedHashMap<String, Integer>> fetchNoOfTicketsAvailable(LocalDate doj, String fromStation, String toStation);

	public ClassFareDto fetchBaseFareBetweenStations(String fromStation, String toStation);

	public ClassFareDto fetchBaseFareBetweenStations(Integer stationId1, Integer stationId2);

	public Train getTrainUsingNumber(Integer trainNo);

	public List<String> fetchStationsForTrain(Train train);

	public LocalDate fetchMinDepartureDateFromTicketAvailability();

	public Boolean updateDepartureDates(int dayDiff);

	public Boolean updateDepartureAndArrivalDates(int dayDiff);

	public Boolean updateArrivalDates();

	public LocalDate getArrivalDateFromTicketAvailability(Integer trainNumber, String toStation, LocalDate dateOfJourney);

	public Map<String, Integer> fetchDayForStations(Integer trainNumber);

	public Integer noOfTicketsAvlForDateTrainAndClass(String sourceStation, String destinationStation, LocalDate doj,
			Integer trainNumber, String trainClass);

}
