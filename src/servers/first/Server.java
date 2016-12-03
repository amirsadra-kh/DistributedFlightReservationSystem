package servers.first;

import javax.xml.ws.Endpoint;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by masseeh on 11/1/16.
 */
public class Server {
    public static int NUMBER = 3;
    public static HashMap<String , Integer> flightCountPorts = new HashMap<>();
    public static HashMap<String , Integer> transferPorts = new HashMap<>();


    static
    {
        flightCountPorts.put("MTL" , 5555);
        flightCountPorts.put("WA" , 5556);
        flightCountPorts.put("NDL" , 5557);

        transferPorts.put("montreal" , 6666);
        transferPorts.put("washington" , 6667);
        transferPorts.put("new delhi" , 6668);
    }

    public static void main(String args[]) {

        FSServant impl = new FSServant();

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

        populate(impl , args[0]);


        udpListener(Server.flightCountPorts.get(args[0]) , Server.transferPorts.get(trans) , impl);


        System.out.println( trans + " ready and waiting ...");

    }


    public static void populate(FSServant fsServant , String city) {

        String[] managers = {"1111"};
        FSServant.initLoggingSystem(city);

        fsServant.init(Server.flightCountPorts.get(city));

        try {
            Scanner input = new Scanner(new FileReader(city + ".txt")).useDelimiter(",|\n");
            do {
                Flight flight = new Flight(input.next(), input.next(), input.nextInt(),
                        input.nextInt(), input.nextInt());
                fsServant.flights.tryPut(flight.getFlightId(), flight);
            } while (input.hasNext());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void udpListener(int countPort ,int transPort , FSServant servant) {
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
