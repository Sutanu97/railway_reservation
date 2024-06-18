package manager.impl;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.TrainsDao;
import dto.ClassFareDto;
import dto.TrainStationTimingDto;
import entity.Station;
import entity.Train;
import manager.TrainsManager;

@Service
public class TrainsManagerImpl implements TrainsManager{
	
	@Autowired
	private TrainsDao trainsDao;

	public TrainsDao getTrainsDao() {
		return trainsDao;
	}

	public void setTrainsDao(TrainsDao trainsDao) {
		this.trainsDao = trainsDao;
	}
	
	@Override
	public List<Station> fetchStations() {
		return trainsDao.fetchStations();
	}

	@Override
	public List<Train> fetchTrainsBetweenStations(String fromStation, String toStation, String doj) {
		return trainsDao.fetchTrainsBetweenStations(fromStation, toStation, doj);
	}

	@Override
	public List<TrainStationTimingDto> fetchTrainTimings(String sourceStation, String destinationStation) {
		return trainsDao.fetchTrainTimings( sourceStation, destinationStation);
	}

	@Override
	public Map<Integer, LinkedHashMap<String, Integer>> fetchNoOfTicketsAvailable(LocalDate doj, String fromStation, String toStation) {
		return trainsDao.fetchNoOfTicketsAvailable(doj, fromStation, toStation);
	}

	@Override
	public ClassFareDto fetchBaseFareBetweenStations(String fromStation, String toStation) {
		return trainsDao.fetchBaseFareBetweenStations(fromStation, toStation);
	}

	@Override
	public ClassFareDto fetchBaseFareBetweenStations(Integer stationId1, Integer stationId2) {
		return trainsDao.fetchBaseFareBetweenStations(stationId1, stationId2);
	}

	@Override
	public Train getTrainUsingNumber(Integer trainNo) {
		return trainsDao.getTrainUsingNumber(trainNo);
	}

	@Override
	public List<String> fetchStationsForTrain(Train train) {
		return trainsDao.fetchStationsForTrain(train);
	}

	@Override
	public LocalDate fetchMinDepartureDateFromTicketAvailability() {
		return trainsDao.fetchMinDepartureDateFromTicketAvailability();
	}

	@Override
	public Boolean updateDepartureDates(int dayDiff) {
		return trainsDao.updateDepartureDates(dayDiff);
	}

	@Override
	public Boolean updateDepartureAndArrivalDates(int dayDiff) {
		return trainsDao.updateDepartureAndArrivalDates(dayDiff);
	}

	@Override
	public Boolean updateArrivalDates() {
		return trainsDao.updateArrivalDates();
	}

	@Override
	public LocalDate getArrivalDateFromTicketAvailability(Integer trainNumber, String toStation, LocalDate dateOfJourney) {
		return trainsDao.getArrivalDateFromTicketAvailability(trainNumber, toStation, dateOfJourney);
	}

	@Override
	public Map<String, Integer> fetchDayForStations(Integer trainNumber) {
		return trainsDao.fetchDayForStations(trainNumber);
	}

	@Override
	public Integer noOfTicketsAvlForDateTrainAndClass(String sourceStation, String destinationStation, LocalDate doj,
			Integer trainNumber, String trainClass) {
		return trainsDao.noOfTicketsAvlForDateTrainAndClass(sourceStation, destinationStation, doj, trainNumber, trainClass);
	}

	
	
}
