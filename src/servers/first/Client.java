package servers.first;


/**
 * Created by masseeh on 10/4/16.
 */
public class Client {

	public static int NUMBER = 0;
	
    private int clientId;
    private String destination;
    private String date;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private Integer flightClass;
    private Character firstLetter;

    public Client(String firstName, String lastName, String address, String phone, String destination, Integer flightClass, String date) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.date = date;
        this.address = address;
        this.flightClass = flightClass;
        this.destination = destination;
        this.firstLetter = this.lastName.charAt(0);
        clientId = NUMBER;
        NUMBER = NUMBER + 1;
    }

    public int getClientId() {
        return clientId;
    }

    public Character getFirstLetter() {
        return firstLetter;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getFlightClass() {
        return flightClass;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }

    @Override
	public String toString() {
		return this.firstName + " " + this.lastName + " " + this.address + " " + this.flightClass;
	}
    
    
}
