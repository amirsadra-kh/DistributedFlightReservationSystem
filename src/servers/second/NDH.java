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

public class NDH extends ParentServant {
    //Flight Map and Passenger Map databases
    static Map<String, ArrayList<String>> flightMap = new HashMap<>();
    static Map<String, Map<String, ArrayList<String>>> ndhPassengerMap = new HashMap<>();

    //Static file assignment for logging
    static Path fileAddress = Paths.get("/Users/Sadra/IdeaProjects/DFRS_WEBSERVICES/src/Server Logs/NDH_SERVER_LOG.txt");
    static Path protPath = Paths.get("/resources/ndh1/actions.log");
    static ArrayList<String> protLines = new ArrayList<>();
    static ArrayList<String> lines = new ArrayList<>();

    Object lock = new Object();

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


    static String departure = "NDH";

    public NDH(int cityPort) {
        super(cityPort);
    }

    //Initial flight data and managers
    public static void flightMapDB(){
        String uniqueID = UUID.randomUUID().toString().substring(0, 5);
        ArrayList<String> initial1 = new ArrayList<>();
        ArrayList<String> initial2 = new ArrayList<>();
        ArrayList<String> initial3 = new ArrayList<>();
        ArrayList<String> initial4 = new ArrayList<>();
        initial1.add("NDH");
        initial1.add("MTL");
        initial1.add("17.11.16");
        initial1.add("125");
        initial1.add("20");
        initial1.add("10");
        flightMap.put(uniqueID, initial1);

        uniqueID = UUID.randomUUID().toString().substring(0, 5);
        initial2.add("NDH");
        initial2.add("WST");
        initial2.add("02.12.16");
        initial2.add("150");
        initial2.add("20");
        initial2.add("5");
        flightMap.put(uniqueID, initial2);

        uniqueID = UUID.randomUUID().toString().substring(0, 5);
        initial3.add("NDH");
        initial3.add("WST");
        initial3.add("16.10.16");
        initial3.add("150");
        initial3.add("20");
        initial3.add("5");
        flightMap.put(uniqueID, initial3);



//        uniqueID = UUID.randomUUID().toString().substring(0, 5);
//	    initial3.add("NDH");
//	    initial3.add("WST");
//	    initial3.add("07.11.16");
//	    initial3.add("180");
//	    initial3.add("20");
//	    initial3.add("20");
//	    flightMap.put(uniqueID, initial4);

        lines.add("Initial flights database initiated at ["+new Date().toString()+"].");
        try {
            Files.write(fileAddress, lines , Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\nCurrent flights on NDH server: \n" );
        for (String key : flightMap.keySet())
            System.out.println(key + " " +"\n"+ flightMap.get(key));
    }
    public static Map managerDB(){
        Map<Integer, String> ndhMGRmap = new HashMap<>();
        ndhMGRmap.put(1, "NDH1000");
        ndhMGRmap.put(2, "NDH2000");
        ndhMGRmap.put(3, "NDH3000");
        lines.add("Managers database created at ["+new Date().toString()+"].");
        try {
            Files.write(fileAddress, lines , Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ndhMGRmap;
    }

    public static void psngrDB(){
//        new NDH().bookFlight("TEST 1_F","TEST 1_L","TEST 1_ADD","TEST 1_N","MTL","17.11.16","BUS");
//        new NDH().bookFlight("TEST 2_F","TEST 2_L","TEST 2_ADD","TEST 2_N","WST","02.12.16","ECO");
//        new NDH().bookFlight("TEST 3_F","TEST 3_L","TEST 3_ADD","TEST 3_N","WST","02.12.16","FIR");

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
                        byte[] sendData = "NDH".getBytes();
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

            Thread WSTUDP = new Thread(new Runnable() {
                @Override
                public void run() {
                    DatagramSocket clientSocket = null;
                    try {
                        byte[] receiveDataWST = new byte[20];
                        byte[] sendData = "NDH".getBytes();
                        DatagramPacket sendPacketToWST = new DatagramPacket(sendData, sendData.length, IPAddress, 9833);
                        clientSocket = new DatagramSocket();
                        clientSocket.send(sendPacketToWST);
                        DatagramPacket receivePacketWST = new DatagramPacket(receiveDataWST, receiveDataWST.length);
                        clientSocket.receive(receivePacketWST);

                        byte[] actualResult = new byte[receivePacketWST.getLength()];
                        System.arraycopy(receivePacketWST.getData() , receivePacketWST.getOffset() , actualResult , 0 , receivePacketWST.getLength());
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
            WSTUDP.start();

            MTLUDP.join();
            WSTUDP.join();

            lines.add("Get booked flight data requested at [" + new Date().toString() + "].");
            Files.write(fileAddress, lines, Charset.forName("UTF-8"));


            serverSentence = results[0] + " " + results[1] + " " + "NDH: " + mapVal.size();

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
    public synchronized boolean managerAuth (String mgrString){
        boolean auth = false;
        if (managerDB().containsValue(mgrString))
            auth = true;
        lines.add("Manager ["+mgrString+"] logged in at ["+new Date().toString()+"].");
        try {
            Files.write(fileAddress, lines , Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return auth;
    }

    @WebMethod
    public synchronized int addFlight(String destination, String flightDate, String ECO, String BUS, String FIR){
        synchronized (lock){
            protLines.add(Protocol.createLogMsg(Protocol.ADD_FLIGHT, destination, flightDate, ECO, BUS, FIR)+"\n");
            try {
                Files.write(protPath, protLines , Charset.forName("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String result;
        String uniqueID = UUID.randomUUID().toString().substring(0, 5);
        ArrayList<String> flightData = new ArrayList<>();
        flightData.add(departure);
        flightData.add(destination);
        flightData.add(flightDate);
        flightData.add(ECO);
        flightData.add(BUS);
        flightData.add(FIR);
        if(flightMap.containsValue(flightData)){
            result = "Flight data {" + flightData +"} already exists. Please try again.";
            System.out.println(result);
            return -1;
        }
        else
        {
            flightMap.put(uniqueID, flightData);
            result = "Flight record has been added successfully with the ID: "+uniqueID;
            System.out.println(result);
            System.out.println("\nCurrent data map is: \n");
            for (String key : flightMap.keySet())
                System.out.println(key + " " +"\n"+ flightMap.get(key));
            lines.add("Flight ["+uniqueID+"] "+flightData+" has been added on "+new Date().toString()+"].");
            try {
                Files.write(fileAddress, lines , Charset.forName("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }
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
                    if (otherCity.equals("MTL"))
                        port = 9711;
                    else
                        port = 9733;


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
                        Field f = NDH.class.getDeclaredField("mapVal" + client.get(1).toUpperCase().charAt(0));
                        HashMap<String, ArrayList<String>> a = (HashMap<String, ArrayList<String>>) f.get(new HashMap<String, ArrayList<String>>());
                        synchronized (a) {
                            a.remove(recordID);
                        }
                        isDone = 0;

                        String desti = "";
                        if (port == 9711)
                            desti = "MTL";
                        else
                            desti = "WST";
                        lines.add("Transfer reservation is done from [NDH] to [" + desti + "] at [" + new Date().toString() + "].");
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
    public synchronized int removeFlight(String recordID){

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
        result = "Flight record ["+recordID+"] has been removed successfully.";
        lines.add("Flight ["+recordID+"] has been removed on ["+new Date().toString()+"].");
        try {
            Files.write(fileAddress, lines , Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        System.out.println("\nUpdated flight map is: \n");
        for(String key:flightMap.keySet()){
            System.out.println(key + " " + flightMap.get(key));
        }
        return 0;
    }

    @WebMethod
    public synchronized int bookFlight (String firstName, String lastName, String address, String phone, String destination, String flightDate, String flightClass){
        synchronized (lock){
            protLines.add(Protocol.createLogMsg(Protocol.BOOK_FLIGHT, firstName, lastName, address, phone, destination, flightDate, flightClass)+"\n");
            try {
                Files.write(protPath, protLines , Charset.forName("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String result = "";
        int r;
        ArrayList<String> ndhPassengerList = new ArrayList<>();
        String mainKey = lastName.substring(0,1).toUpperCase();
        String uniqueID = UUID.randomUUID().toString().substring(0, 5);

        ndhPassengerList.add(firstName);
        ndhPassengerList.add(lastName);
        ndhPassengerList.add(address);
        ndhPassengerList.add(phone);
        ndhPassengerList.add(destination);
        ndhPassengerList.add(flightDate);
        ndhPassengerList.add(flightClass);

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

        if(ndhPassengerMap.containsKey(mainKey) && edit != null && !result.equals("No more seats available for reservation.")){

            if(mapVal.containsValue(ndhPassengerList)){
                result = "Passenger info already exits. Please try again.";
                System.out.println(result);
                return -3;
            }
            else{
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

                mapVal.put(uniqueID, ndhPassengerList);

                if(mainKey.equals("A")){
                    mapValA.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValA);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("B")){
                    mapValB.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValB);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("C")){
                    mapValC.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValC);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("D")){
                    mapValD.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValD);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("E")){
                    mapValE.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValE);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }
                if(mainKey.equals("F")){
                    mapValF.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValF);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("G")){
                    mapValG.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValG);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("H")){
                    mapValH.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValH);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("I")){
                    mapValI.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValI);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("J")){
                    mapValJ.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValJ);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("K")){
                    mapValK.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValK);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("L")){
                    mapValL.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValL);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("M")){
                    mapValM.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValM);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("N")){
                    mapValN.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValN);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("O")){
                    mapValO.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValO);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("P")){
                    mapValP.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValP);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("Q")){
                    mapValQ.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValQ);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("R")){
                    mapValR.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValR);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("S")){
                    mapValS.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValS);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("T")){
                    mapValT.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValT);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("U")){
                    mapValU.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValU);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("V")){
                    mapValV.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValV);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("W")){
                    mapValW.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValW);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("X")){
                    mapValX.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValX);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("Y")){
                    mapValY.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValY);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(mainKey.equals("Z")){
                    mapValZ.put(uniqueID, ndhPassengerList);
                    ndhPassengerMap.put(mainKey, mapValZ);
                    result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                    System.out.println(result);
                    lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                    try {
                        Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            return 0;
        }
        else if (edit != null && !result.equals("No more seats available for reservation.")){
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

            mapVal.put(uniqueID, ndhPassengerList);

            if(mainKey.equals("A")){
                mapValA.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValA);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("B")){
                mapValB.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValB);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("C")){
                mapValC.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValC);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("D")){
                mapValD.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValD);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("E")){
                mapValE.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValE);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("F")){
                mapValF.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValF);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("G")){
                mapValG.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValG);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("H")){
                mapValH.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValH);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("I")){
                mapValI.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValI);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("J")){
                mapValJ.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValJ);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("K")){
                mapValK.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValK);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("L")){
                mapValL.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValL);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("M")){
                mapValM.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValM);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("N")){
                mapValN.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValN);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("O")){
                mapValO.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValO);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("P")){
                mapValP.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValP);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("Q")){
                mapValQ.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValQ);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("R")){
                mapValR.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValR);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("S")){
                mapValS.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValS);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("T")){
                mapValT.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValT);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("U")){
                mapValU.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValU);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("V")){
                mapValV.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValV);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("W")){
                mapValW.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValW);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("X")){
                mapValX.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValX);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("Y")){
                mapValY.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValY);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mainKey.equals("Z")){
                mapValZ.put(uniqueID, ndhPassengerList);
                ndhPassengerMap.put(mainKey, mapValZ);
                result = "Your flight has been booked with the following information. Bon Voyage!"+"\n"+ndhPassengerList+"\n";
                System.out.println(result);
                lines.add("Passenger ["+uniqueID+"] booked the flight reservation at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (String key : ndhPassengerMap.keySet())
                System.out.println(key + " " +"\n"+ ndhPassengerMap.get(key));
            return 0;
        }
        return -1;
    }

    @WebMethod
    public int editRecord(String recordID, String fieldName, String newValue){
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

        synchronized (f){

            if (fieldName.equals("departure")){
                edit.addAll(f);
                edit.remove(0);
                edit.add(0, newValue);
                flightMap.replace(recordID, edit);
                result = fieldName+" data on flight ["+recordID+"] has been modified to: "+newValue+"\n["+recordID+"]"+ " " + flightMap.get(recordID)+"\n";
                System.out.println(result);
                lines.add("Manager has edited the flight ["+recordID+"] on the field ["+fieldName+"] to ["+newValue+"] at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fieldName.equals("destination")){
                edit.addAll(f);
                edit.remove(1);
                edit.add(1, newValue);
                flightMap.replace(recordID, edit);
                result = fieldName+" data on flight ["+recordID+"] has been modified to: "+newValue+"\n["+recordID+"]"+ " " + flightMap.get(recordID)+"\n";
                System.out.println(result);
                lines.add("Manager has edited the flight ["+recordID+"] on the field ["+fieldName+"] to ["+newValue+"] at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fieldName.equals("flightDate")){
                edit.addAll(f);
                edit.remove(2);
                edit.add(2, newValue);
                flightMap.replace(recordID, edit);
                result = fieldName+" data on flight ["+recordID+"] has been modified to: "+newValue+"\n["+recordID+"]"+ " " + flightMap.get(recordID)+"\n";
                System.out.println(result);
                lines.add("Manager has edited the flight ["+recordID+"] on the field ["+fieldName+"] to ["+newValue+"] at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fieldName.equals("ECO")){
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

                result = fieldName+" data on flight ["+recordID+"] has been modified to: "+newValue+"\n["+recordID+"]"+ " " + flightMap.get(recordID)+"\n";
                System.out.println(result);
                lines.add("Manager has edited the flight ["+recordID+"] on the field ["+fieldName+"] to ["+newValue+"] at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fieldName.equals("BUS")){
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

                result = fieldName+" data on flight ["+recordID+"] has been modified to: "+newValue+"\n["+recordID+"]"+ " " + flightMap.get(recordID)+"\n";
                System.out.println(result);
                lines.add("Manager has edited the flight ["+recordID+"] on the field ["+fieldName+"] to ["+newValue+"] at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fieldName.equals("FIR")){
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

                result = fieldName+" data on flight ["+recordID+"] has been modified to: "+newValue+"\n["+recordID+"]"+ " " + flightMap.get(recordID)+"\n";
                System.out.println(result);
                lines.add("Manager has edited the flight ["+recordID+"] on the field ["+fieldName+"] to ["+newValue+"] at ["+new Date().toString()+"].");
                try {
                    Files.write(fileAddress, lines , Charset.forName("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return 0;
        }}

    public static void main (String args[])
    {
        try{
            //flightMapDB();
            managerDB();
            //psngrDB();

            NDH ndh = new NDH(Protocol.FIRST_REPLICA_PORT_NDL);

            System.out.println
                    ("NDH Server ready and waiting ...");

            // wait for invocations from clients

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        DatagramSocket serverSocket = new DatagramSocket(9722);
                        byte[] receiveData = new byte[100];

                        while(true)
                        {
                            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                            serverSocket.receive(receivePacket);
                            byte[] actualResult = new byte[receivePacket.getLength()];
                            System.arraycopy(receivePacket.getData() , receivePacket.getOffset() , actualResult , 0 , receivePacket.getLength());
                            String[] client = new String(actualResult).split(",");
                            int result = ndh.bookFlight(client[0], client[1], client[2], client[3], client[4], client[5], client[6]);
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
                        DatagramSocket serverSocket = new DatagramSocket(9822);
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
                            serverSentence = "NDH: " + mapVal.size();
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
        }

        catch (Exception e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.out);
        }
    }

}
