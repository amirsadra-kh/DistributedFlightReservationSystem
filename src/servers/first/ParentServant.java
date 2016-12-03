package servers.first;


/**
 * Created by masseeh on 12/3/16.
 */
abstract public class ParentServant {

    public int countPort;
    public int transPort;
    public int cityPort;
    public String cityOrigin;


    public ParentServant(int countPort, int transPort, int cityPort, String cityOrigin) {
        this.countPort = countPort;
        this.transPort = transPort;
        this.cityPort = cityPort;
        this.cityOrigin = cityOrigin;
    }

    public abstract int bookFlight(String firsName, String lastName, String address, String phone, String destination, String date, String flightClass);

    public abstract String getBookedFlightCount();

    public abstract int editRecord(String recordId, String fieldName, String newValue);

    public abstract int addFlight(String destination, String date, String ec, String bus, String fir);

    public abstract int removeFlight(String recordId);

    public abstract int transferReservation(String clientId, String currentCity, String otherCity);
}
