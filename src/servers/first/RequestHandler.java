package servers.first;

import frontEnd.Protocol;
import net.rudp.ReliableServerSocket;
import net.rudp.ReliableSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by masseeh on 12/3/16.
 */
public class RequestHandler {

    private ParentServant servant;
    private ReliableServerSocket serverSocket;
    private ConcurrentHashMap<Integer,String> holdBack;
    private final Object lock = new Object();

    private Integer sequenceNumber = 0;


    public RequestHandler(ParentServant servant) {
        this.servant = servant;
        holdBack = new ConcurrentHashMap<>();

        try {
            serverSocket = new ReliableServerSocket(servant.cityPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        while (true) {

            try {

                ReliableSocket socket = (ReliableSocket) serverSocket.accept();

                new Thread(new Handler(socket)).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] invoke(String msg) {

        String[] tokenizer = msg.split(",");

        String seq = tokenizer[2];

        byte[] returnResult;
        String res = "";

        int method = Integer.valueOf(tokenizer[1]);

        switch (method) {
            case Protocol.BOOK_FLIGHT :
                res = servant.bookFlight(tokenizer[3], tokenizer[4], tokenizer[5], tokenizer[6], tokenizer[7], tokenizer[8], tokenizer[9]) + "";

                break;
            case Protocol.GET_BOOKED_FLIGHT_COUNT :
                res = servant.getBookedFlightCount();

                break;
            case Protocol.EDIT_RECORD :
                res = servant.editRecord(tokenizer[3], tokenizer[4], tokenizer[5]) + "";

                break;
            case Protocol.ADD_FLIGHT :
                res = servant.addFlight(tokenizer[3], tokenizer[4], tokenizer[5], tokenizer[6], tokenizer[7]) + "";

                break;
            case Protocol.REMOVE_FLIGHT :
                res = servant.removeFlight(tokenizer[3]) + "";

                break;
            case Protocol.TRANSFER_RESERVATION :
                res = servant.transferReservation(tokenizer[3], tokenizer[4], tokenizer[5]) + "";

                break;
        }

        returnResult = Protocol.createResultMsg(ReplicaManager.ID, method, seq, res);

        return returnResult;
    }

    public void sendResult(byte[] buffer) {
        try {
            ReliableSocket socket = new ReliableSocket();
            socket.connect(new InetSocketAddress("127.0.0.1", Protocol.FRONT_END_PORT));

            OutputStream out = socket.getOutputStream();

            out.write(buffer);

            out.flush();
            out.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Handler implements Runnable {

        private ReliableSocket socket;
        private boolean isAllowed;

        public Handler(ReliableSocket socket) {
            this.socket = socket;
            isAllowed = false;
        }

        @Override
        public void run() {

            try {
                InputStream in = socket.getInputStream();
                byte[] buffer = new byte[Protocol.MSG_LENGTH];

                int size = in.read(buffer);

                in.close();
                socket.close();

                String msg = new String(buffer, 0, size);

                System.out.println(msg);

                String[] tokenizer = msg.split(",");

                int seq = Integer.valueOf(tokenizer[0]);

                synchronized (lock) {
                    if (seq == sequenceNumber) {
                        sequenceNumber++;
                        isAllowed = true;

                    } else {
                        holdBack.put(seq, msg);
                        isAllowed = false;
                    }
                }

                if (isAllowed) {
                    byte[] bytes = invoke(msg);
                    sendResult(bytes);
                    while (holdBack.contains(sequenceNumber)) {
                        String s = holdBack.remove(sequenceNumber);
                        byte[] b = invoke(s);
                        sendResult(b);
                        sequenceNumber++;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
