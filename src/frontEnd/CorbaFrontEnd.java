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


    @Override
    public int bookFlight(String firsName, String lastName, String address, String phone, String destination, String date, String flightClass) {
        try {

            ReliableSocket socket = establish();

            OutputStream out = socket.getOutputStream();

            byte[] buffer;

            int localId = 0;

            synchronized (clientId) {
                localId = clientId;
                Pair pair = new Pair(localId);
                holdBack.put(Protocol.BOOK_FLIGHT, pair);
                buffer = Protocol.createFrontEndMsg(Protocol.BOOK_FLIGHT, clientId, firsName, lastName, address, phone, destination, date, flightClass);
                clientId++;
            }


            out.write(buffer);
            out.flush();
            out.close();

            clean(socket);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public String getBookedFlightCount() {

        try {
            ReliableSocket socket = establish();

            OutputStream out = socket.getOutputStream();

            byte[] buffer;

            int localId = 0;

            synchronized (clientId) {
                localId = clientId;
                Pair pair = new Pair(localId);
                holdBack.put(Protocol.GET_BOOKED_FLIGHT_COUNT, pair);
                buffer = Protocol.createFrontEndMsg(Protocol.GET_BOOKED_FLIGHT_COUNT, clientId);
                clientId++;
            }

            out.write(buffer);
            out.flush();
            out.close();

            clean(socket);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int editRecord(String recordId, String fieldName, String newValue) {

        try {
            ReliableSocket socket = establish();

            OutputStream out = socket.getOutputStream();

            byte[] buffer;

            int localId = 0;

            synchronized (clientId) {
                localId = clientId;
                Pair pair = new Pair(localId);
                holdBack.put(Protocol.EDIT_RECORD, pair);
                buffer = Protocol.createFrontEndMsg(Protocol.EDIT_RECORD, clientId, recordId, fieldName, newValue);
                clientId++;
            }

            out.write(buffer);
            out.flush();
            out.close();

            clean(socket);



        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public int addFlight(String destination, String date, String ec, String bus, String fir) {

        try {
            ReliableSocket socket = establish();

            OutputStream out = socket.getOutputStream();

            byte[] buffer;

            int localId = 0;

            synchronized (clientId) {
                localId = clientId;
                Pair pair = new Pair(localId);
                holdBack.put(Protocol.ADD_FLIGHT, pair);
                buffer = Protocol.createFrontEndMsg(Protocol.ADD_FLIGHT, clientId, destination, date, ec, bus, fir);
                clientId++;
            }

            out.write(buffer);
            out.flush();
            out.close();

            clean(socket);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public int removeFlight(String recordId) {

        try {
            ReliableSocket socket = establish();

            OutputStream out = socket.getOutputStream();

            byte[] buffer;

            int localId = 0;

            synchronized (clientId) {
                localId = clientId;
                Pair pair = new Pair(localId);
                holdBack.put(Protocol.REMOVE_FLIGHT, pair);
                buffer = Protocol.createFrontEndMsg(Protocol.REMOVE_FLIGHT, clientId, recordId);
                clientId++;
            }

            out.write(buffer);
            out.flush();
            out.close();

            clean(socket);



        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public int transferReservation(String clientId, String currentCity, String otherCity) {
        try {
            ReliableSocket socket = establish();

            OutputStream out = socket.getOutputStream();

            byte[] buffer;

            int localId = 0;

            synchronized (this.clientId) {
                localId = this.clientId;
                Pair pair = new Pair(localId);
                holdBack.put(Protocol.TRANSFER_RESERVATION, pair);
                buffer = Protocol.createFrontEndMsg(Protocol.TRANSFER_RESERVATION, this.clientId, clientId, currentCity, otherCity);
                this.clientId++;
            }

            out.write(buffer);
            out.flush();
            out.close();

            clean(socket);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private ReliableSocket establish() throws IOException {
        ReliableSocket socket = new ReliableSocket();
        socket.connect(new InetSocketAddress("127.0.0.1" , 9876));
        return socket;
    }

    private void clean(ReliableSocket socket) throws IOException {
        socket.close();
    }

    private void afterTimeOut(int method, int id) {
        try {
            Thread.sleep(Protocol.TIME_OUT);

            Pair pairs = holdBack.get(method);

            ArrayList<String> results = pairs.entry.get(id);









        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
            ReliableServerSocket serverSocket = new ReliableServerSocket(9999);

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

                int method = Integer.valueOf(tokenizer[0]);

                int id = Integer.valueOf(tokenizer[1]);

                String res = tokenizer[2];

                synchronized (lock) {

                    Pair msges = holdBack.get(method);
                    ArrayList<String> s = msges.entry.get(id);
                    s.add(res);
                    msges.entry.put(id, s);
                    holdBack.put(method, msges);

                }

            } catch (IOException e) {
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
