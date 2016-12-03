package servers.first;

/**
 * Created by masseeh on 10/4/16.
 */
public class Flight {
	
	public static int NUMBER = 0;
	private static final Object numberLock = new Object();
	
    private int flightId;
    private String destination;
    private String flightDate;
    public MyHashMap<Integer,Integer> maximumSeats;
    public MyHashMap<Integer,Integer> availableSeats;
    public MyArrayList<Client> onBoard;

    public Flight(String destination, String flightDate, int economy, int business, int first) {

		this.flightId = NUMBER;
		this.incId();
		this.destination = destination;
		this.flightDate = flightDate;
		maximumSeats = new MyHashMap<>();
		maximumSeats.put(FlightClass.ECONOMY, economy);
		maximumSeats.put(FlightClass.BUSINESS, business);
		maximumSeats.put(FlightClass.FIRST, first);
		
		onBoard = new MyArrayList<>();
		availableSeats = new MyHashMap<>(maximumSeats);

	}
    
    public int count(int recordType) {
    	int result = 0;
    	switch (recordType) {
		case 0:
			result = maximumSeats.get(FlightClass.ECONOMY) - availableSeats.get(FlightClass.ECONOMY);
			break;
		case 1:
			result = maximumSeats.get(FlightClass.BUSINESS) - availableSeats.get(FlightClass.BUSINESS);
			break;
		case 2:
			result = maximumSeats.get(FlightClass.FIRST) - availableSeats.get(FlightClass.FIRST);
			break;
		case 3:
			result = maximumSeats.get(FlightClass.ECONOMY) - availableSeats.get(FlightClass.ECONOMY) +
					maximumSeats.get(FlightClass.BUSINESS) - availableSeats.get(FlightClass.BUSINESS) +
					maximumSeats.get(FlightClass.FIRST) - availableSeats.get(FlightClass.FIRST);
			break;
		default:
			break;
		}
    	return result;
    }

	public int getFlightId() {
        return flightId;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

    public int available(String destination, String date, Integer flightClass) {
        if (this.destination.equals(destination) && this.flightDate.equals(date)) {
            if (availableSeats.get(flightClass) > 0) {
                addPassenger(flightClass);
                return flightId;
            }
            else {
            	return -2;
            }
        }
        return -1;
    }

    public synchronized void addPassenger(Integer flightClass) {
        Integer available = availableSeats.get(flightClass);
        availableSeats.put(flightClass, available-1);
    }
    
    public void incId() {
    	synchronized (numberLock) {
        	this.NUMBER++;	
		}
    }
    public void decId() {
    	synchronized (numberLock) {
        	this.NUMBER--;	
		}
    }

    @Override
    public String toString() {
        return "Flight{" +
        		"id = " + flightId +
                " destination = " + destination +
                ", flightDate = " + flightDate +
                ", maximumSeats = " + maximumSeats +
                ", availableSeats = " + availableSeats +
                " }";
    }

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Flight) {
			Flight flight = (Flight)(obj);
			if(this.destination.equals(flight.destination) &&
					this.flightDate.equals(flight.flightDate) &&
					this.maximumSeats.get(FlightClass.ECONOMY) == flight.maximumSeats.get(FlightClass.ECONOMY) &&
					this.maximumSeats.get(FlightClass.BUSINESS) == flight.maximumSeats.get(FlightClass.BUSINESS) &&
					this.maximumSeats.get(FlightClass.FIRST) == flight.maximumSeats.get(FlightClass.FIRST)) {
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}
  
    
}
