package servers.first;
import frontEnd.Protocol;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by masseeh on 11/21/16.
 */

public class FSServant extends ParentServant{

  public MyHashMap<Character,MyArrayList<Client>> passengers;
  public MyHashMap<Integer,Flight> flights;
  private Logger generalLogger = Logger.getLogger("GENERAL LOG");
  private Logger actionLogger = Logger.getLogger("ACTION LOG");


  public FSServant(int countPort, int transPort, int cityPort, String cityOrigin) {
    super(countPort, transPort, cityPort, cityOrigin);

    flights = new MyHashMap<>();
    passengers = new MyHashMap<>();
    initLoggingSystem();
  }

  public void initLoggingSystem() {

    actionLogger.setLevel(Level.ALL);
    FileHandler aFileHandler;
    try {
      aFileHandler = new FileHandler("resources/" + cityOrigin.toLowerCase() + ReplicaManager.ID
              + "/actions.log", true);
      aFileHandler.setFormatter(new SimpleFormatter());
      actionLogger.addHandler(aFileHandler);
    } catch (SecurityException | IOException e) {
      e.printStackTrace();
    }

    generalLogger.setLevel(Level.ALL);
    FileHandler uFileHandler;
    try {
      uFileHandler = new FileHandler("resources/" + cityOrigin.toLowerCase() + ReplicaManager.ID
              + "/general.log", true);
      uFileHandler.setFormatter(new SimpleFormatter());
      generalLogger.addHandler(uFileHandler);
    } catch (SecurityException | IOException e) {
      e.printStackTrace();
    }

    generalLogger.info(cityOrigin + " server is up!");
  }

  @Override
  public String getBookedFlightCount() {

    actionLogger.info(Protocol.createLogMsg(Protocol.GET_BOOKED_FLIGHT_COUNT));

    String result = "";
    try {

      final DatagramSocket dSocket = new DatagramSocket();
      byte[] data = new byte[1];
      InetAddress ip = InetAddress.getByName("localhost");

      int ports[] = {5555,5556,5557};
      ArrayList<Thread> serversRequest = new ArrayList<>();

      ArrayList<String> res = new ArrayList<>();

      for(int i = 0; i< ReplicaManager.NUMBER; i++) {
        if (ports[i] != countPort) {
          final int idx = i;
          Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
              try {

                DatagramPacket request = new DatagramPacket(data, data.length, ip, ports[idx]);
                byte[] raw = new byte[100];
                DatagramPacket respose = new DatagramPacket(raw, raw.length);

                dSocket.send(request);
                dSocket.receive(respose);
                byte[] f = new byte[respose.getLength()];
                System.arraycopy(raw, respose.getOffset(), f, 0, respose.getLength());
                String a = new String(f);
                res.add(a);

              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          });
          t.start();
          serversRequest.add(t);
        }
      }

      for (int i = 0; i < ReplicaManager.NUMBER-1; i++) {
        serversRequest.get(i).join();
      }

      for (int i = 0; i < ReplicaManager.NUMBER-1; i++) {
        result += res.get(i);
        result += " - ";
      }

      result += cityOrigin + " : " + flightCount(3);

      dSocket.close();

    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    generalLogger.info(" there is " + result + " total booked flights");
    return result;
  }


  @Override
  public int bookFlight(String firsName, String lastName, String address, String phone,
                        String destination, String date, String flightClass) {

    actionLogger.info(Protocol.createLogMsg(Protocol.BOOK_FLIGHT, firsName, lastName, address, phone, destination, date, flightClass));

    int flightId = searchFlight(destination, date, Integer.valueOf(flightClass));

    if (flightId == -1) {
      generalLogger.info("flight has not found for " + firsName + " " + lastName + " " + address + " " + flightClass);
      return -1;
    }
    else if (flightId == -2) {
      generalLogger.info("currently there is no seat available for "  + firsName + " " + lastName + " " + address + " " + flightClass);
      return -2;
    }

    Flight flight = flights.get(flightId);
    Client client = new Client(firsName, lastName, address, phone, destination, Integer.valueOf(flightClass), date);
    flight.onBoard.tryAdd(client);

    if (passengers.containsKey(client.getFirstLetter())) {
      passengers.get(client.getFirstLetter()).tryAdd(client);
    }
    else {
      MyArrayList<Client> newClientList = new MyArrayList<Client>();
      newClientList.tryAdd(client);
      MyArrayList<Client> retrieved = passengers.tryPut(client.getFirstLetter() , newClientList);
      if(retrieved != null) {
        retrieved.tryAdd(client);
      }
    }

    generalLogger.info(client + " has been added to reservation list");
    return client.getClientId();
  }

  @Override
  public int editRecord(String recordId, String fieldName, String newValue) {

    actionLogger.info(Protocol.createLogMsg(Protocol.EDIT_RECORD , recordId, fieldName, newValue));

    Flight flight = flights.get(recordId);
    synchronized (flight) {
      switch (fieldName) {
        case "flightDate":
          flight.setFlightDate(newValue);
          break;
        case "destination":
          flight.setDestination(newValue);
          break;
        case "1":
          syncFlight(flight, FlightClass.ECONOMY , Integer.parseInt(newValue));
          flight.maximumSeats.put(FlightClass.ECONOMY, Integer.parseInt(newValue));
          break;
        case "2":
          syncFlight(flight, FlightClass.BUSINESS , Integer.parseInt(newValue));
          flight.maximumSeats.put(FlightClass.BUSINESS, Integer.parseInt(newValue));
          break;
        case "3":
          syncFlight(flight, FlightClass.FIRST , Integer.parseInt(newValue));
          flight.maximumSeats.put(FlightClass.FIRST, Integer.parseInt(newValue));
          break;
        default:
          break;
      }
    }

    generalLogger.info(fieldName + " of flight with " + recordId + " id has been updated to " + newValue);
    return 0;
  }

  public int searchFlight(String destination, String date, Integer flightClass) {

    Collection<Flight> f = flights.values();
    int flightId = -1;
    synchronized (f) {
      Iterator<Flight> flightIterator = f.iterator();
      while (flightIterator.hasNext()) {
        Flight flight = flightIterator.next();
        flightId = flight.available(destination, date, flightClass);
        if (flightId != -1 && flightId != -2) {
          break;
        }
      }
      return flightId;
    }
  }

  @Override
  public int addFlight(String destination, String date, String eco, String bus, String fir) {

    actionLogger.info(Protocol.createLogMsg(Protocol.ADD_FLIGHT, destination, date, eco, bus, fir));

    Flight flight = new Flight(destination, date, Integer.valueOf(eco), Integer.valueOf(bus), Integer.valueOf(fir));

    if(!isFlightDuplicated(flight)) {
      generalLogger.info(" Flight " + flight + " has been added");
      return 0;
    }
    else {
      generalLogger.info(" flight is already added");
    }
    return -1;
  }

  private synchronized boolean isFlightDuplicated(Flight flight) {
    Iterator<Flight> f = flights.values().iterator();
    while(f.hasNext()) {
      if(f.next().equals(flight)) {
        return true;
      }
    }
    flights.tryPut(flight.getFlightId(), flight);
    return false;
  }

  @Override
  public int removeFlight(String recordId) {

    actionLogger.info(Protocol.createLogMsg(Protocol.REMOVE_FLIGHT, recordId));

    Flight flight = flights.tryRemove(Integer.valueOf(recordId));

    if(flight == null) {
      generalLogger.info("there is no flight");
      return -1;
    }
    for(int i=0; i<flight.onBoard.size(); i++) {
      Client client = flight.onBoard.get(i);
      MyArrayList<Client> ar = passengers.get(client.getFirstLetter());
      synchronized (ar) {
        ar.remove(client);
      }
    }
    flight.decId();
    generalLogger.info(flight + " has been removed");
    return 0;
  }

  @Override
  public int transferReservation(String clientId, String currentCity, String otherCity) {

    actionLogger.info(Protocol.createLogMsg(Protocol.TRANSFER_RESERVATION, clientId, currentCity, otherCity));

    Client client = null;
    Flight flight = null;

    Collection<MyArrayList<Client>> clients = passengers.values();

    Iterator<MyArrayList<Client>> itr = clients.iterator();

    while (itr.hasNext()) {
      MyArrayList<Client> entry = itr.next();
      for (Client c : entry) {
        if (c.getClientId() == Integer.valueOf(clientId)) {
          client = c;
          break;
        }
      }
      if (client != null) {
        break;
      }
    }


    Collection<Flight> values = flights.values();

    Iterator<Flight> flightIterator = values.iterator();

    while (flightIterator.hasNext()) {
      Flight f = flightIterator.next();
      for (int i=0;i<f.onBoard.size();i++) {
        if (f.onBoard.get(i).getClientId() == client.getClientId()) {
          flight = f;
          break;
        }

      }
      if (flight != null) {
        break;
      }
    }

    if (flight == null) {
      return -1;
    }

    synchronized (flight) {

      StringBuilder data = new StringBuilder();
      data.append(client.getFirstName() + ",");
      data.append(client.getLastName() + ",");
      data.append(client.getAddress() + ",");
      data.append(client.getPhone() + ",");
      data.append(client.getDate() + ",");
      data.append(client.getFlightClass() + ",");
      data.append(client.getDestination());


      byte[] tampReceive = new byte[10];

      try {
        InetAddress ip = InetAddress.getByName("localhost");
        DatagramSocket datagramSocket = new DatagramSocket();
        DatagramPacket request = new DatagramPacket(data.toString().getBytes(), data.length(), ip, ReplicaManager.transferPorts.get(otherCity));
        datagramSocket.send(request);

        DatagramPacket response = new DatagramPacket(tampReceive, tampReceive.length);
        datagramSocket.receive(response);
        byte[] actualData = new byte[response.getLength()];
        System.arraycopy(response.getData(), response.getOffset(), actualData, 0, response.getLength());

        String res = new String(actualData);

        if (res.equals("S")) {

          ArrayList<Client> c = passengers.get(client.getFirstLetter());
          int t = flight.availableSeats.get(client.getFlightClass());
          flight.availableSeats.put(client.getFlightClass(),t+1);

          int idxTobeRemoved = -1;

          for (int i = 0; i < c.size(); i++) {
            if (c.get(i).getClientId() == client.getClientId()) {
              idxTobeRemoved = i;
              break;
            }
          }

          synchronized (c) {
            c.remove(idxTobeRemoved);
          }

          generalLogger.info("flight have been changed from " + currentCity + " to " + otherCity);
          return 1;
        } else {

          return -1;

        }

      } catch (IOException e) {
        e.printStackTrace();
      }


      return 0;
    }
  }

  public void syncFlight(Flight flight , Integer fc , int newMax) {
    int dif = flight.count(fc) - newMax;
    if(dif >= 0) {
      MyArrayList<Client> clone = (MyArrayList<Client>)flight.onBoard.clone();
      for(int i=0; i< dif; i++) {
        Client client = clone.get(i);
        if(client.getFlightClass() == fc) {
          MyArrayList<Client> ar = passengers.get(client.getFirstLetter());
          synchronized (ar) {
            ar.remove(client);
            flight.onBoard.remove(client);
            generalLogger.info(client + " has been removed from flight " + flight.getFlightId());
          }
        }
      }
      flight.availableSeats.put(fc,0);
    }
    else if(dif < 0 ) {
      synchronized (flight.availableSeats) {
        int n = newMax - flight.maximumSeats.get(fc);
        int a = flight.availableSeats.get(fc);
        flight.availableSeats.put(fc , a + n);
      }
    }
    generalLogger.info("flight " + flight.getFlightId() + " is now synced");
  }

  public int flightCount(int recordType) {
    int result = 0;

    Iterator<Flight> f = flights.values().iterator();
    while(f.hasNext()) {
      result += f.next().count(recordType);
    }

    return result;
  }

  public static void main(String[] args) {

    String trans = "";

    switch (args[0]) {
      case "MTL" :
        trans = "montreal";
        break;
      case "WA" :
        trans = "washington";
        break;
      case "NDL" :
        trans = "new delhi";
        break;
    }

    FSServant fsServant = new FSServant(ReplicaManager.flightCountPorts.get(args[0]), ReplicaManager.transferPorts.get(trans), 9987, args[0]);

//    fsServant.populate(args[0]);

    fsServant.udpListener(fsServant.countPort, fsServant.transPort, fsServant);

    RequestHandler requestHandler = new RequestHandler(fsServant);

    requestHandler.listen();

  }


  private void populate(String city) {

    try {
      Scanner input = new Scanner(new FileReader(city + ".txt")).useDelimiter(",|\n");
      do {
        Flight flight = new Flight(input.next(), input.next(), input.nextInt(),
                input.nextInt(), input.nextInt());
        flights.tryPut(flight.getFlightId(), flight);
      } while (input.hasNext());

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void udpListener(int countPort ,int transPort , FSServant servant) {

    new Thread(new Runnable() {
      @Override
      public void run() {
        DatagramSocket dSocket = null;
        try {
          dSocket = new DatagramSocket(countPort);
          byte[] buffer = new byte[1];
          while(true) {
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            dSocket.receive(request);
            Integer count = servant.flightCount(3);
            String s = servant.cityOrigin + " : " + count;
            byte[] c = s.getBytes();
            DatagramPacket reply = new DatagramPacket(c, c.length, request.getAddress(), request.getPort());
            dSocket.send(reply);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
        finally {
          dSocket.close();
        }
      }
    }).start();

    new Thread(new Runnable() {
      @Override
      public void run() {
        DatagramSocket dSocket = null;
        try {
          dSocket = new DatagramSocket(transPort);
          byte[] buffer = new byte[1024];
          while(true) {
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            dSocket.receive(request);
            byte[] actualData = new byte[request.getLength()];
            System.arraycopy(buffer , request.getOffset() , actualData , 0 , request.getLength());
            String client = new String(actualData);
            String[] info = client.split(",");
            String firstName = info[0];
            String lastName = info[1];
            String address = info[2];
            String phone = info[3];
            String date = info[4];
            String flightClass = info[5];
            String destination = info[6];

            int res = servant.bookFlight(firstName , lastName , address , phone , destination , date , flightClass);

            if (res == -1 || res == -2) {
              String r = "F";
              DatagramPacket reply = new DatagramPacket(r.getBytes() , r.length() , request.getAddress() , request.getPort());
              dSocket.send(reply);
            }
            else {
              String r = "S";
              DatagramPacket reply = new DatagramPacket(r.getBytes() , r.length() , request.getAddress() , request.getPort());
              dSocket.send(reply);
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
        finally {
          dSocket.close();
        }
      }
    }).start();

  }




}
