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
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * Created by masseeh on 12/2/16.
 */
public class CorbaFrontEnd extends FSInterfacePOA {


    @Override
    public int bookFlight(String firsName, String lastName, String address, String phone, String destination, String date, String flightClass) {
        try {
            ReliableSocket socket = establish();

            OutputStream out = socket.getOutputStream();

            byte[] buffer = Protocol.createFrontEndMsg(Protocol.BOOK_FLIGHT, firsName, lastName, address, phone, destination, date, flightClass);

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

            byte[] buffer = Protocol.createFrontEndMsg(Protocol.GET_BOOKED_FLIGHT_COUNT);

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
        return 0;
    }

    @Override
    public int addFlight(String destination, String date, String ec, String bus, String fir) {

        try {
            ReliableSocket socket = establish();

            OutputStream out = socket.getOutputStream();

            byte[] buffer = Protocol.createFrontEndMsg(Protocol.ADD_FLIGHT, destination, date, ec, bus, fir);

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

            byte[] buffer = Protocol.createFrontEndMsg(Protocol.REMOVE_FLIGHT , recordId);

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

            byte[] buffer = Protocol.createFrontEndMsg(Protocol.TRANSFER_RESERVATION, clientId, currentCity, otherCity);

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

                new Thread(new Handler()).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    class Handler implements Runnable {
        @Override
        public void run() {

        }
    }
}
