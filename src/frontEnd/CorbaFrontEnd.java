package frontEnd;

import net.rudp.ReliableServerSocket;
import net.rudp.ReliableSocket;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import shared.FSInterface;
import shared.FSInterfaceHelper;
import shared.FSInterfacePOA;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by masseeh on 12/2/16.
 */
public class CorbaFrontEnd extends FSInterfacePOA {

    private ConcurrentHashMap<Integer,Pair> holdBack = new ConcurrentHashMap<>();
    private Object lock = new Object();
    private Integer clientId = 0;
    private int mode = Protocol.HA_MODE;


    @Override
    public int bookFlight(String city, String firsName, String lastName, String address, String phone, String destination, String date, String flightClass) {
        try {

            ReliableSocket socket = establish();

            OutputStream out = socket.getOutputStream();

            byte[] buffer;

            int localId = 0;

            Pair pair;

            synchronized (clientId) {
                localId = clientId;
                pair = new Pair(localId);
                holdBack.put(Protocol.BOOK_FLIGHT, pair);
                buffer = Protocol.createFrontEndMsg(findCity(city), Protocol.BOOK_FLIGHT, clientId, firsName, lastName, address, phone, destination, date, flightClass);
                clientId++;
            }


            out.write(buffer);
            out.flush();
            out.close();

            clean(socket);

            String r = afterTimeOut(Protocol.BOOK_FLIGHT, localId, pair);
            if (!r.equals("")) {
                return Integer.valueOf(r);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public String getBookedFlightCount(String city) {

        try {
            ReliableSocket socket = establish();

            OutputStream out = socket.getOutputStream();

            byte[] buffer;

            int localId = 0;

            Pair pair;

            synchronized (clientId) {
                localId = clientId;
                pair = new Pair(localId);
                holdBack.put(Protocol.GET_BOOKED_FLIGHT_COUNT, pair);
                buffer = Protocol.createFrontEndMsg(findCity(city), Protocol.GET_BOOKED_FLIGHT_COUNT, clientId);
                clientId++;
            }

            out.write(buffer);
            out.flush();
            out.close();

            clean(socket);


            String r = afterTimeOut(Protocol.GET_BOOKED_FLIGHT_COUNT, localId, pair);
            if (!r.equals("")) {
                return r;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int editRecord(String city, String recordId, String fieldName, String newValue) {

        try {
            ReliableSocket socket = establish();

            OutputStream out = socket.getOutputStream();

            byte[] buffer;

            int localId = 0;

            Pair pair;

            synchronized (clientId) {
                localId = clientId;
                pair = new Pair(localId);
                holdBack.put(Protocol.EDIT_RECORD, pair);
                buffer = Protocol.createFrontEndMsg(findCity(city), Protocol.EDIT_RECORD, clientId, recordId, fieldName, newValue);
                clientId++;
            }

            out.write(buffer);
            out.flush();
            out.close();

            clean(socket);


            String r = afterTimeOut(Protocol.EDIT_RECORD, localId, pair);
            if (!r.equals("")) {
                return Integer.valueOf(r);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public int addFlight(String city, String destination, String date, String ec, String bus, String fir) {

        try {
            ReliableSocket socket = establish();

            OutputStream out = socket.getOutputStream();

            byte[] buffer;

            int localId = 0;

            Pair pair;

            synchronized (clientId) {
                localId = clientId;
                pair = new Pair(localId);
                holdBack.put(Protocol.ADD_FLIGHT, pair);
                buffer = Protocol.createFrontEndMsg(findCity(city), Protocol.ADD_FLIGHT, clientId, destination, date, ec, bus, fir);
                clientId++;
            }

            out.write(buffer);
            out.flush();
            out.close();

            clean(socket);


            String r = afterTimeOut(Protocol.ADD_FLIGHT, localId, pair);
            if (!r.equals("")) {
                return Integer.valueOf(r);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public int removeFlight(String city, String recordId) {

        try {
            ReliableSocket socket = establish();

            OutputStream out = socket.getOutputStream();

            byte[] buffer;

            int localId = 0;

            Pair pair;

            synchronized (clientId) {
                localId = clientId;
                pair = new Pair(localId);
                holdBack.put(Protocol.REMOVE_FLIGHT, pair);
                buffer = Protocol.createFrontEndMsg(findCity(city), Protocol.REMOVE_FLIGHT, clientId, recordId);
                clientId++;
            }

            out.write(buffer);
            out.flush();
            out.close();

            clean(socket);


            String r = afterTimeOut(Protocol.REMOVE_FLIGHT, localId, pair);
            if (!r.equals("")) {
                return Integer.valueOf(r);
            }



        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public int transferReservation(String city, String clientId, String currentCity, String otherCity) {
        try {
            ReliableSocket socket = establish();

            OutputStream out = socket.getOutputStream();

            byte[] buffer;

            int localId = 0;

            Pair pair;

            synchronized (this.clientId) {
                localId = this.clientId;
                pair = new Pair(localId);
                holdBack.put(Protocol.TRANSFER_RESERVATION, pair);
                buffer = Protocol.createFrontEndMsg(findCity(city), Protocol.TRANSFER_RESERVATION, this.clientId, clientId, currentCity, otherCity);
                this.clientId++;
            }

            out.write(buffer);
            out.flush();
            out.close();

            clean(socket);


            String r = afterTimeOut(Protocol.TRANSFER_RESERVATION, localId, pair);
            if (!r.equals("")) {
                return Integer.valueOf(r);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private int findCity(String city) {
        int res=0;

        switch (city) {
            case "MTL":
                res = Protocol.MTL;
                break;
            case "NDL":
                res = Protocol.NDL;
                break;
            case "WA":
                res = Protocol.WA;
                break;
        }

        return res;
    }

    private ReliableSocket establish() throws IOException {
        ReliableSocket socket = new ReliableSocket();
        socket.connect(new InetSocketAddress("127.0.0.1" , Protocol.SEQUENCER_PORT));
        return socket;
    }

    private void clean(ReliableSocket socket) throws IOException {
        socket.close();
    }

    private String afterTimeOut(int method, int id, Pair pair) {

        String f = "";

        if (mode == Protocol.HA_MODE) {

            synchronized (pair) {
                try {

                    pair.wait();

                    ArrayList<String> results = pair.entry.get(id);

                    int size = results.size();

                    if (size > 0) {
                        String p = results.get(0);
                        String[] s = p.split(",");
                        f = s[0];
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            ErrorHandler res = new ErrorHandler(pair, id);
            res.start();
        }

        else {
            ErrorHandler res = new ErrorHandler(pair, id);
            res.start();

            try {

                res.join();

                f = res.readResult();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

        return f;
    }

    public static void main(String[] args) {
        try {

            ORB orb = ORB.init(args, null);

            // get reference to rootpoa & activate the POAManager
            POA rootpoa =
                    (POA) orb.resolve_initial_references("RootPOA");
            rootpoa.the_POAManager().activate();

            // create servant and register it with the ORB
            CorbaFrontEnd impl = new CorbaFrontEnd();

            // get object reference from the servant
            org.omg.CORBA.Object ref =
                    rootpoa.servant_to_reference(impl);
            // and cast the reference to a CORBA reference
            FSInterface href = FSInterfaceHelper.narrow(ref);

            // get the root naming context
            // NameService invokes the transient name service
            org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");
            // Use NamingContextExt, which is part of the
            // Interoperable Naming Service (INS) specification.
            NamingContextExt ncRef =
                    NamingContextExtHelper.narrow(objRef);

            // bind the Object Reference in Naming
            NameComponent path[] = ncRef.to_name("frontEnd");
            ncRef.rebind(path, href);

            impl.listen();

            orb.run();

        }
        catch (Exception e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.out);
        }

    }

    public void listen() {

        try {
            ReliableServerSocket serverSocket = new ReliableServerSocket(Protocol.FRONT_END_PORT);

            while (true) {
                ReliableSocket socket = (ReliableSocket)serverSocket.accept();

                new Thread(new Handler(socket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    class Handler implements Runnable {

        private ReliableSocket socket;

        public Handler(ReliableSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            try {
                InputStream in = socket.getInputStream();
                byte[] buffer = new byte[Protocol.MSG_LENGTH];

                int size = in.read(buffer);

                String msg = new String(buffer, 0, size);

                String[] tokenizer = msg.split(",");

                int replica = Integer.valueOf(tokenizer[0]);

                int method = Integer.valueOf(tokenizer[1]);

                int id = Integer.valueOf(tokenizer[2]);

                String res = tokenizer[3] + "," + replica;

                Pair msges = holdBack.get(method);

                synchronized (lock) {
                    ArrayList<String> s = msges.entry.get(id);
                    s.add(res);
                    msges.entry.put(id, s);
                    holdBack.put(method, msges);
                }

                synchronized (msges) {
                    msges.notify();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    class ErrorHandler extends Thread {

        private Pair pair;
        private int id;

        public ErrorHandler(Pair pair, int id) {
            this.pair = pair;
            this.id = id;
        }

        public String readResult() {
            return "";
        }

        @Override
        public void run() {

            try {
                Thread.sleep(Protocol.TIME_OUT);

                ArrayList<String> results = pair.entry.get(id);

                int size = results.size();

//                synchronized (results) {
//
//                    for (int i = 0; i <) {
//
//                    }
//
//                }

                if (size > 0) {

                    String p = results.get(0);
                    String[] s = p.split(",");

                }



            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    class Pair {

        public ConcurrentHashMap<Integer, ArrayList<String>> entry;

        public Pair(int id) {
            entry = new ConcurrentHashMap<>();
            ArrayList<String> msg = new ArrayList<>();
            entry.put(id , msg);
        }
    }
}
