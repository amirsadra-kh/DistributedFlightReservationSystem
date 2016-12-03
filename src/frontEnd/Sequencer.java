package frontEnd;

import net.rudp.ReliableServerSocket;
import net.rudp.ReliableSocket;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by masseeh on 12/2/16.
 */
public class Sequencer {

    private ReliableServerSocket sequencerSocket;
    private long sequenceNumber = 0;

    private void init() {
        try {

            sequencerSocket = new ReliableServerSocket(9876);
            while (true) {

                System.out.println("Waiting...");
                ReliableSocket socket = (ReliableSocket)sequencerSocket.accept();
                sequenceNumber++;
                System.out.println(sequenceNumber + " Connected...");
                new Thread(new Handler(socket,sequenceNumber)).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Sequencer sequencer = new Sequencer();
        sequencer.init();

    }

    class Handler implements Runnable {

        private ReliableSocket socket;
        private long seq;

        public Handler(ReliableSocket socket, long seq) {
            this.socket = socket;
            this.seq = seq;
        }

        @Override
        public void run() {

            try {
                InputStream in = socket.getInputStream();
                byte[] buffer = new byte[1024];
                int size = in.read(buffer);

                String msg = new String(buffer,0,size);

                System.out.println(msg);

                byte[] sendBuffer = Protocol.createSequencerMsg(seq, msg);




                in.close();
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
