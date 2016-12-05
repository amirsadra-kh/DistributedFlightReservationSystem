package servers.second; /**
 * Created by Sadra on 10/10/16.
 */


import frontEnd.Protocol;
import servers.first.ParentServant;

import javax.jws.WebMethod;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Sadra on 10/10/16.
 */

public class WST extends ParentServant {
    //Flight Map and Passenger Map databases
    static Map<String, ArrayList<String>> flightMap = new HashMap<>();
    static Map<String, Map<String, ArrayList<String>>> wstPassengerMap = new HashMap<>();

    //Static file assignment for logging
    static Path fileAddress = Paths.get("/Users/Sadra/IdeaProjects/DFRS_WEBSERVICES/src/Server Logs/WST_SERVER_LOG.txt");
    static Path protPath = Paths.get("/resources/wst1/actions.log");
    static ArrayList<String> protLines = new ArrayList<>();
    static ArrayList<String> lines = new ArrayList<>();

    //Alphabetic hash maps for Passengers
    static Map<String, ArrayList<String>> mapVal = new HashMap<>();
    static Map<String, ArrayList<String>> mapValA = new HashMap<>();
    static Map<String, ArrayList<String>> mapValB = new HashMap<>();
    static Map<String, ArrayList<String>> mapValC = new HashMap<>();
    static Map<String, ArrayList<String>> mapValD = new HashMap<>();
    static Map<String, ArrayList<String>> mapValE = new HashMap<>();
    static Map<String, ArrayList<String>> mapValF = new HashMap<>();
    static Map<String, ArrayList<String>> mapValG = new HashMap<>();
    static Map<String, ArrayList<String>> mapValH = new HashMap<>();
    static Map<String, ArrayList<String>> mapValI = new HashMap<>();
    static Map<String, ArrayList<String>> mapValJ = new HashMap<>();
    static Map<String, ArrayList<String>> mapValK = new HashMap<>();
    static Map<String, ArrayList<String>> mapValL = new HashMap<>();
    static Map<String, ArrayList<String>> mapValM = new HashMap<>();
    static Map<String, ArrayList<String>> mapValN = new HashMap<>();
    static Map<String, ArrayList<String>> mapValO = new HashMap<>();
    static Map<String, ArrayList<String>> mapValP = new HashMap<>();
    static Map<String, ArrayList<String>> mapValQ = new HashMap<>();
    static Map<String, ArrayList<String>> mapValR = new HashMap<>();
    static Map<String, ArrayList<String>> mapValS = new HashMap<>();
    static Map<String, ArrayList<String>> mapValT = new HashMap<>();
    static Map<String, ArrayList<String>> mapValU = new HashMap<>();
    static Map<String, ArrayList<String>> mapValV = new HashMap<>();
    static Map<String, ArrayList<String>> mapValW = new HashMap<>();
    static Map<String, ArrayList<String>> mapValX = new HashMap<>();
    static Map<String, ArrayList<String>> mapValY = new HashMap<>();
    static Map<String, ArrayList<String>> mapValZ = new HashMap<>();

    Object lock = new Object();
    static String departure = "WST";

    public WST(int cityPort) {
        super(cityPort);
    }

    //Initial flight data and managers
    public static void flightMapDB() {
        String uniqueID = UUID.randomUUID().toString().substring(0, 5);
        ArrayList<String> initial1 = new ArrayList<>();
        ArrayList<String> initial2 = new ArrayList<>();
        ArrayList<String> initial3 = new ArrayList<>();
        ArrayList<String> initial4 = new ArrayList<>();
        initial1.add("WST");
        initial1.add("MTL");
        initial1.add("10.10.16");
        initial1.add("120");
        initial1.add("10");
        initial1.add("5");
        flightMap.put(uniqueID, initial1);

        uniqueID = UUID.randomUUID().toString().substring(0, 5);
        initial2.add("WST");
        initial2.add("NDH");
        initial2.add("02.12.16");
        initial2.add("150");
        initial2.add("20");
        initial2.add("5");
        flightMap.put(uniqueID, initial2);

        uniqueID = UUID.randomUUID().toString().substring(0, 5);
        initial3.add("WST");
        initial3.add("NDH");
        initial3.add("02.11.16");
        initial3.add("160");
        initial3.add("60");
        initial3.add("20");
        flightMap.put(uniqueID, initial3);

        uniqueID = UUID.randomUUID().toString().substring(0, 5);
        initial4.add("WST");
        initial4.add("MTL");
        initial4.add("22.10.16");
        initial4.add("120");
        initial4.add("10");
        initial4.add("5");
        flightMap.put(uniqueID, initial4);

        lines.add("Initial flights database initiated at [" + new Date().toString() + "].");
        try {
            Files.write(fileAddress, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\nCurrent flights on WST server: \n");
        for (String key : flightMap.keySet())
            System.out.println(key + " " + "\n" + flightMap.get(key));
    }

    public static Map managerDB() {
        Map<Integer, String> wstMGRmap = new HashMap<>();
        wstMGRmap.put(1, "WST1000");
        wstMGRmap.put(2, "WST2000");
        wstMGRmap.put(3, "WST3000");
        lines.add("Managers database created at [" + new Date().toString() + "].");
        try {
            Files.write(fileAddress, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wstMGRmap;
    }

    public static void psngrDB(){
//        new WST().bookFlight("TEST 1_F","TEST 1_L","TEST 1_ADD","TEST 1_N","MTL","10.10.16","ECO");
//        new WST().bookFlight("TEST 2_F","TEST 2_L","TEST 2_ADD","TEST 2_N","WST","10.10.16","ECO");
//        new WST().bookFlight("TEST 3_F","TEST 3_L","TEST 3_ADD","TEST 3_N","WST","10.10.16","FIR");
//        new WST().bookFlight("TEST 4_F","TEST 4_L","TEST 4_ADD","TEST 4_N","WST","10.10.16","FIR");
//        new WST().bookFlight("TEST 5_F","TEST 5_L","TEST 5_ADD","TEST 5_N","NDH","02.11.16","FIR");
//        new WST().bookFlight("TEST 6_F","TEST 6_L","TEST 6_ADD","TEST 6_N","NDH","02.11.16","FIR");

    }

    //Remote functions implementations
    @WebMethod
    public synchronized String getBookedFlightCount () {

        synchronized (lock){
            protLines.add(Protocol.createLogMsg(Protocol.GET_BOOKED_FLIGHT_COUNT)+"\n");
            try {
                Files.write(protPath, protLines , Charset.forName("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String serverSentence = "";
        String[] results = new String[2];

        try {
            InetAddress IPAddress = InetAddress.getByName("localhost");

            Thread MTLUDP = new Thread(new Runnable() {
                @Override
                public void run() {
                    DatagramSocket clientSocket = null;
                    try {
                        byte[] receiveDataMTL = new byte[20];
                        byte[] sendData = "WST".getBytes();
                        DatagramPacket sendPacketToMTL = new DatagramPacket(sendData, sendData.length, IPAddress, 9811);
                        clientSocket = new DatagramSocket();
                        clientSocket.send(sendPacketToMTL);
                        DatagramPacket receivePacketMTL = new DatagramPacket(receiveDataMTL, receiveDataMTL.length);
                        clientSocket.receive(receivePacketMTL);

                        byte[] actualResult = new byte[receivePacketMTL.getLength()];
                        System.arraycopy(receivePacketMTL.getData() , receivePacketMTL.getOffset() , actualResult , 0 , receivePacketMTL.getLength());
                        results[0] = new String(actualResult);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    finally {
                        clientSocket.close();
                    }

                }
            });
            MTLUDP.start();

            Thread NDHUDP = new Thread(new Runnable() {
                @Override
                public void run() {
                    DatagramSocket clientSocket = null;
                    try {
                        byte[] receiveDataNDH = new byte[20];
                        byte[] sendData = "WST".getBytes();
                        DatagramPacket sendPacketToNDH = new DatagramPacket(sendData, sendData.length, IPAddress, 9822);
                        clientSocket = new DatagramSocket();
                        clientSocket.send(sendPacketToNDH);
                        DatagramPacket receivePacketNDH = new DatagramPacket(receiveDataNDH, receiveDataNDH.length);
                        clientSocket.receive(receivePacketNDH);

                        byte[] actualResult = new byte[receivePacketNDH.getLength()];
                        System.arraycopy(receivePacketNDH.getData() , receivePacketNDH.getOffset() , actualResult , 0 , receivePacketNDH.getLength());
                        results[1] = new String(actualResult);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    finally {
                        clientSocket.close();
                    }
                }
            });
            NDHUDP.start();

            MTLUDP.join();
            NDHUDP.join();

            lines.add("Get booked flight data requested at [" + new Date().toString() + "].");
            Files.write(fileAddress, lines, Charset.forName("UTF-8"));


            serverSentence = results[0] + " " + results[1] + " " + "WST: " + mapVal.size();

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return serverSentence;
    }

    @WebMethod
    public int transferReservation(String recordID, String currentCity, String otherCity){
        synchronized (lock){
            protLines.add(Protocol.createLogMsg(Protocol.TRANSFER_RESERVATION, recordID, currentCity, otherCity)+"\n");
            try {
                Files.write(protPath, protLines , Charset.forName("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int isDone = -1;
        if (mapVal.containsKey(recordID)) {
            StringBuilder sendData = new StringBuilder();
            ArrayList<String> client = mapVal.get(recordID);

            Set<Map.Entry<String, ArrayList<String>>> flights = flightMap.entrySet();
            Iterator<Map.Entry<String, ArrayList<String>>> itr = flights.iterator();

            ArrayList<String> edit = null;
            String editKey = "";

            while (itr.hasNext()) {
                Map.Entry<String, ArrayList<String>> t = itr.next();
                ArrayList<String> temp = t.getValue();
                if (temp.contains(client.get(4)) && temp.contains(client.get(5))) {
                    edit = temp;
                    editKey = t.getKey();
                    break;
                }
            }
            synchronized (edit) {
                try {
                    InetAddress IPAddress = InetAddress.getByName("localhost");
                    DatagramSocket clientSocket = null;
                    int port;
                    if (otherCity.equals("NDH"))
                        port = 9722;
                    else
                        port = 9711;


                    for (String c : client) {
                        sendData.append(c + ",");
                    }
                    sendData.deleteCharAt(sendData.length() - 1);
                    byte[] receiveData = new byte[20];

                    DatagramPacket sendPacket = new DatagramPacket(sendData.toString().getBytes(), sendData.length(), IPAddress, port);
                    clientSocket = new DatagramSocket();
                    clientSocket.send(sendPacket);
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    clientSocket.receive(receivePacket);

                    byte[] actualResult = new byte[receivePacket.getLength()];
                    System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), actualResult, 0, receivePacket.getLength());
                    String result = new String(actualResult);
                    if (result.equals("OK")) {
                        synchronized (mapVal) {
                            mapVal.remove(recordID);
                        }
                        Field f = WST.class.getDeclaredField("mapVal" + client.get(1).toUpperCase().charAt(0));
                        HashMap<String, ArrayList<String>> a = (HashMap<String, ArrayList<String>>) f.get(new HashMap<String, ArrayList<String>>());
                        synchronized (a) {
                            a.remove(recordID);
                        }
                        isDone = 0;

                        String desti = "";
                        if (port == 9722)
                            desti = "NDH";
                        else
                            desti = "MTL";
                        lines.add("Transfer reservation is done from [WST] to [" + desti + "] at [" + new Date().toString() + "].");
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } else {
                        isDone = -1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return isDone;
        }
        else
            return isDone;
    }

    @WebMethod
    public synchronized boolean managerAuth(String mgrString) {
        boolean auth = false;
        if (managerDB().containsValue(mgrString))
            auth = true;
        lines.add("Manager [" + mgrString + "] logged in at [" + new Date().toString() + "].");
        try {
            Files.write(fileAddress, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return auth;
    }

    @WebMethod
    public synchronized int addFlight(String destination, String flightDate, String ECO, String BUS, String FIR) {
        synchronized (lock){
            protLines.add(Protocol.createLogMsg(Protocol.ADD_FLIGHT, destination, flightDate, ECO, BUS, FIR)+"\n");
            try {
                Files.write(protPath, protLines , Charset.forName("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String uniqueID = UUID.randomUUID().toString().substring(0, 5);
        String result;
        ArrayList<String> flightData = new ArrayList<>();
        flightData.add(departure);
        flightData.add(destination);
        flightData.add(flightDate);
        flightData.add(ECO);
        flightData.add(BUS);
        flightData.add(FIR);
        if (flightMap.containsValue(flightData)) {
            result = "Flight data {" + flightData + "} already exists. Please try again.";
            System.out.println(result);
            return -1;
        } else {
            flightMap.put(uniqueID, flightData);
            result = "Flight record has been added successfully with the ID: " + uniqueID;
            System.out.println(result);
            System.out.println("\nCurrent data map is: \n");
            for (String key : flightMap.keySet())
                System.out.println(key + " " + "\n" + flightMap.get(key));
            lines.add("Flight [" + uniqueID + "] " + flightData + " has been added on " + new Date().toString() + "].");
            try {
                Files.write(fileAddress, lines, Charset.forName("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    @WebMethod
    public synchronized int removeFlight(String recordID) {
        synchronized (lock){
            protLines.add(Protocol.createLogMsg(Protocol.REMOVE_FLIGHT, recordID)+"\n");
            try {
                Files.write(protPath, protLines , Charset.forName("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String result;
        flightMap.remove(recordID);
        result = "Flight record [" + recordID + "] has been removed successfully.";
        System.out.println(result);
        lines.add("Flight [" + recordID + "] has been removed on [" + new Date().toString() + "].");
        try {
            Files.write(fileAddress, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\nUpdated flight map is: \n");
        for (String key : flightMap.keySet()) {
            System.out.println(key + " " + flightMap.get(key));

        }

        return 0;
    }

    @WebMethod
    public synchronized int bookFlight(String firstName, String lastName, String address, String phone, String destination, String flightDate, String flightClass) {
        synchronized (lock){
            protLines.add(Protocol.createLogMsg(Protocol.BOOK_FLIGHT, firstName, lastName, address, phone, destination, flightDate, flightClass)+"\n");
            try {
                Files.write(protPath, protLines , Charset.forName("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        ArrayList<String> wstPassengerList = new ArrayList<>();
        String mainKey = lastName.substring(0, 1).toUpperCase();
        String uniqueID = UUID.randomUUID().toString().substring(0, 5);
        String result = "";

        wstPassengerList.add(firstName);
        wstPassengerList.add(lastName);
        wstPassengerList.add(address);
        wstPassengerList.add(phone);
        wstPassengerList.add(destination);
        wstPassengerList.add(flightDate);
        wstPassengerList.add(flightClass);

        Set<Map.Entry<String,ArrayList<String>>> flights = flightMap.entrySet();
        Iterator<Map.Entry<String,ArrayList<String>>> itr = flights.iterator();

        ArrayList<String> edit = null;
        String editKey = "";

        while (itr.hasNext()) {
            Map.Entry<String,ArrayList<String>> t = itr.next();
            ArrayList<String> temp = t.getValue();
            if (temp.contains(destination) && temp.contains(flightDate)) {
                edit = temp;
                editKey = t.getKey();
                break;
            }
        }

        if (edit == null) {
            result = "No flight data has been found. Please try again.";
            System.out.println(result);
            return -1;
        }

        int fClass = 0;

        switch (flightClass) {
            case "ECO":
                fClass = 3;
                break;
            case "BUS":
                fClass = 4;
                break;
            case "FIR":
                fClass = 5;
                break;
        }

        if (edit.get(fClass).equals("0")) {
            result = "No more seats available for reservation ";
            System.out.println(result);
            return -2;

        }

        if (wstPassengerMap.containsKey(mainKey) && edit != null && !result.equals("No more seats available for reservation.")) {


            if (mapVal.containsValue(wstPassengerList)) {
                result = "Passenger info already exists. Please try again.";
                System.out.println(result);
                return -3;
            } else {
                int newVal;
                if (flightClass.equals("ECO")){
                    newVal = Integer.valueOf(edit.get(3)) - 1;
                    edit.remove(3);
                    edit.add(3, String.valueOf(newVal));
                    flightMap.replace(editKey, edit);
                    for (String key : flightMap.keySet())
                        System.out.println(key + " " +"\n"+ flightMap.get(key));

                }
                else if (flightClass.equals("BUS")){
                    newVal = Integer.valueOf(edit.get(4)) - 1;
                    edit.remove(4);
                    edit.add(4, String.valueOf(newVal));
                    flightMap.replace(editKey, edit);
                    for (String key : flightMap.keySet())
                        System.out.println(key + " " +"\n"+ flightMap.get(key));

                }
                else if (flightClass.equals("FIR")){
                    newVal = Integer.valueOf(edit.get(5)) - 1;
                    edit.remove(5);
                    edit.add(5, String.valueOf(newVal));
                    flightMap.replace(editKey, edit);
                    for (String key : flightMap.keySet())
                        System.out.println(key + " " +"\n"+ flightMap.get(key));

                }

                mapVal.put(uniqueID, wstPassengerList);

                if (mainKey.equals("A")) {
                    mapValA.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValA);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("B")) {
                    mapValB.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValB);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("C")) {
                    mapValC.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValC);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("D")) {
                    mapValD.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValD);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("E")) {
                    mapValE.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValE);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("F")) {
                    mapValF.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValF);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("G")) {
                    mapValG.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValG);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("H")) {
                    mapValH.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValH);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("I")) {
                    mapValI.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValI);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("J")) {
                    mapValJ.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValJ);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("K")) {
                    mapValK.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValK);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("L")) {
                    mapValL.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValL);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("M")) {
                    mapValM.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValM);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("N")) {
                    mapValN.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValN);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("O")) {
                    mapValO.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValO);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("P")) {
                    mapValP.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValP);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("Q")) {
                    mapValQ.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValQ);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("R")) {
                    mapValR.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValR);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("S")) {
                    mapValS.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValS);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("T")) {
                    mapValT.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValT);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("U")) {
                    mapValU.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValU);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("V")) {
                    mapValV.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValV);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("W")) {
                    mapValW.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValW);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("X")) {
                    mapValX.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValX);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("Y")) {
                    mapValY.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValY);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mainKey.equals("Z")) {
                    mapValZ.put(uniqueID, wstPassengerList);
                    wstPassengerMap.put(mainKey, mapValZ);
                    result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                    System.out.println(result);
                    lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                    try {
                        Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            return 0;
        } else if (edit != null && !result.equals("No more seats available for reservation.")){
            int newVal;
            if (flightClass.equals("ECO")){
                newVal = Integer.valueOf(edit.get(3)) - 1;
                edit.remove(3);
                edit.add(3, String.valueOf(newVal));
                flightMap.replace(editKey, edit);
                for (String key : flightMap.keySet())
                    System.out.println(key + " " +"\n"+ flightMap.get(key));

            }
            else if (flightClass.equals("BUS")){
                newVal = Integer.valueOf(edit.get(4)) - 1;
                edit.remove(4);
                edit.add(4, String.valueOf(newVal));
                flightMap.replace(editKey, edit);
                for (String key : flightMap.keySet())
                    System.out.println(key + " " +"\n"+ flightMap.get(key));

            }
            else if (flightClass.equals("FIR")){
                newVal = Integer.valueOf(edit.get(5)) - 1;
                edit.remove(5);
                edit.add(5, String.valueOf(newVal));
                flightMap.replace(editKey, edit);
                for (String key : flightMap.keySet())
                    System.out.println(key + " " +"\n"+ flightMap.get(key));

            }

            mapVal.put(uniqueID, wstPassengerList);

            if (mainKey.equals("A")) {
                mapValA.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValA);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("B")) {
                mapValB.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValB);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("C")) {
                mapValC.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValC);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("D")) {
                mapValD.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValD);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("E")) {
                mapValE.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValE);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("F")) {
                mapValF.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValF);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("G")) {
                mapValG.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValG);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("H")) {
                mapValH.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValH);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("I")) {
                mapValI.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValI);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("J")) {
                mapValJ.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValJ);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("K")) {
                mapValK.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValK);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("L")) {
                mapValL.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValL);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("M")) {
                mapValM.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValM);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("N")) {
                mapValN.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValN);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("O")) {
                mapValO.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValO);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("P")) {
                mapValP.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValP);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("Q")) {
                mapValQ.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValQ);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("R")) {
                mapValR.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValR);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("S")) {
                mapValS.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValS);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("T")) {
                mapValT.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValT);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("U")) {
                mapValU.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValU);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("V")) {
                mapValV.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValV);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("W")) {
                mapValW.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValW);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("X")) {
                mapValX.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValX);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("Y")) {
                mapValY.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValY);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mainKey.equals("Z")) {
                mapValZ.put(uniqueID, wstPassengerList);
                wstPassengerMap.put(mainKey, mapValZ);
                result = "Your flight has been booked with the following information. Bon Voyage!" + "\n" + wstPassengerList + "\n";
                System.out.println(result);
                lines.add("Passenger [" + uniqueID + "] booked the flight reservation at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (String key : wstPassengerMap.keySet())
                System.out.println(key + " " + "\n" + wstPassengerMap.get(key));

            return 0;
        }
        return -1;
    }

    @WebMethod
    public int editRecord(String recordID, String fieldName, String newValue) {
        synchronized (lock){
            protLines.add(Protocol.createLogMsg(Protocol.EDIT_RECORD, recordID, fieldName, newValue)+"\n");
            try {
                Files.write(protPath, protLines , Charset.forName("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ArrayList<String> edit = new ArrayList<>();
        String result = "";
        ArrayList<String> f = flightMap.get(recordID);

        Set<Map.Entry<String, ArrayList<String>>> clients = mapVal.entrySet();
        Iterator<Map.Entry<String, ArrayList<String>>> itr = clients.iterator();

        ArrayList<String> edits = null;
        String editKey = "";

        synchronized (f) {

            if (fieldName.equals("departure")) {
                edit.addAll(f);
                edit.remove(0);
                edit.add(0, newValue);
                flightMap.replace(recordID, edit);
                result = fieldName + " data on flight [" + recordID + "] has been modified to: " + newValue + "\n[" + recordID + "]" + " " + flightMap.get(recordID) + "\n";
                System.out.println(result);
                lines.add("Manager has edited the flight [" + recordID + "] on the field [" + fieldName + "] to [" + newValue + "] at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fieldName.equals("destination")) {
                edit.addAll(f);
                edit.remove(1);
                edit.add(1, newValue);
                flightMap.replace(recordID, edit);
                result = fieldName + " data on flight [" + recordID + "] has been modified to: " + newValue + "\n[" + recordID + "]" + " " + flightMap.get(recordID) + "\n";
                System.out.println(result);
                lines.add("Manager has edited the flight [" + recordID + "] on the field [" + fieldName + "] to [" + newValue + "] at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fieldName.equals("flightDate")) {
                edit.addAll(f);
                edit.remove(2);
                edit.add(2, newValue);
                flightMap.replace(recordID, edit);
                result = fieldName + " data on flight [" + recordID + "] has been modified to: " + newValue + "\n[" + recordID + "]" + " " + flightMap.get(recordID) + "\n";
                System.out.println(result);
                lines.add("Manager has edited the flight [" + recordID + "] on the field [" + fieldName + "] to [" + newValue + "] at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fieldName.equals("ECO")) {
                edit.addAll(f);
                edit.remove(3);
                edit.add(3, newValue);
                flightMap.replace(recordID, edit);

                if(newValue.equals("0")){
                    while (itr.hasNext()) {
                        Map.Entry<String, ArrayList<String>> t = itr.next();
                        ArrayList<String> temp = t.getValue();
                        if (temp.contains(f.get(1)) && temp.contains(f.get(2))) {
                            edits = temp;
                            editKey = t.getKey();
                            mapVal.remove(editKey);
                        }
                    }
                }

                result = fieldName + " data on flight [" + recordID + "] has been modified to: " + newValue + "\n[" + recordID + "]" + " " + flightMap.get(recordID) + "\n";
                System.out.println(result);
                lines.add("Manager has edited the flight [" + recordID + "] on the field [" + fieldName + "] to [" + newValue + "] at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fieldName.equals("BUS")) {
                edit.addAll(f);
                edit.remove(4);
                edit.add(4, newValue);
                flightMap.replace(recordID, edit);

                if(newValue.equals("0")){
                    while (itr.hasNext()) {
                        Map.Entry<String, ArrayList<String>> t = itr.next();
                        ArrayList<String> temp = t.getValue();
                        if (temp.contains(f.get(1)) && temp.contains(f.get(2))) {
                            edits = temp;
                            editKey = t.getKey();
                            mapVal.remove(editKey);
                        }
                    }
                }

                result = fieldName + " data on flight [" + recordID + "] has been modified to: " + newValue + "\n[" + recordID + "]" + " " + flightMap.get(recordID) + "\n";
                System.out.println(result);
                lines.add("Manager has edited the flight [" + recordID + "] on the field [" + fieldName + "] to [" + newValue + "] at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fieldName.equals("FIR")) {
                edit.addAll(f);
                edit.remove(5);
                edit.add(5, newValue);
                flightMap.replace(recordID, edit);

                if(newValue.equals("0")){
                    while (itr.hasNext()) {
                        Map.Entry<String, ArrayList<String>> t = itr.next();
                        ArrayList<String> temp = t.getValue();
                        if (temp.contains(f.get(1)) && temp.contains(f.get(2))) {
                            edits = temp;
                            editKey = t.getKey();
                            mapVal.remove(editKey);
                        }
                    }
                }

                result = fieldName + " data on flight [" + recordID + "] has been modified to: " + newValue + "\n[" + recordID + "]" + " " + flightMap.get(recordID) + "\n";
                System.out.println(result);
                lines.add("Manager has edited the flight [" + recordID + "] on the field [" + fieldName + "] to [" + newValue + "] at [" + new Date().toString() + "].");
                try {
                    Files.write(fileAddress, lines, Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return 0;

        }
    }

    public static void main(String args[]) {
        try {
            //flightMapDB();
//            managerDB();
            //psngrDB();

            WST wst = new WST(Protocol.FIRST_REPLICA_PORT_WA);

            System.out.println
                    ("WST Server ready and waiting ...");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        DatagramSocket serverSocket = new DatagramSocket(9733);
                        byte[] receiveData = new byte[100];

                        while(true)
                        {
                            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                            serverSocket.receive(receivePacket);
                            byte[] actualResult = new byte[receivePacket.getLength()];
                            System.arraycopy(receivePacket.getData() , receivePacket.getOffset() , actualResult , 0 , receivePacket.getLength());
                            String[] client = new String(actualResult).split(",");
                            int result = wst.bookFlight(client[0], client[1], client[2], client[3], client[4], client[5], client[6]);
                            String transferRes;
                            if (result == 0)
                                transferRes = "OK";
                            else
                                transferRes = "NOTOK";
                            DatagramPacket sendPacket =
                                    new DatagramPacket(transferRes.getBytes(), transferRes.length(), receivePacket.getAddress(), receivePacket.getPort());
                            serverSocket.send(sendPacket);
                        }
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }

                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        DatagramSocket serverSocket = new DatagramSocket(9833);
                        byte[] receiveData = new byte[8];
                        byte[] sendData = new byte[8];
                        String clientSentence;
                        String serverSentence;
                        while(true)
                        {
                            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                            serverSocket.receive(receivePacket);
                            clientSentence = new String(receivePacket.getData());
                            System.out.println("Client said: " + clientSentence);
                            InetAddress IPAddress = receivePacket.getAddress();
                            int port = receivePacket.getPort();
                            serverSentence = "WST: " + flightMap.size();
                            sendData = serverSentence.getBytes();
                            DatagramPacket sendPacket =
                                    new DatagramPacket(sendData, sendData.length, IPAddress, port);
                            serverSocket.send(sendPacket);
                        }
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }

                }
            }).start();

            // wait for invocations from clients
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.out);
        }


    }
}

