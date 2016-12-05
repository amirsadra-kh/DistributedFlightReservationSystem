package frontEnd;

import net.rudp.ReliableServerSocket;
import net.rudp.ReliableSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * Created by masseeh on 12/2/16.
 */
public class Sequencer {

    private ReliableServerSocket sequencerSocket;
    private long sequenceNumber = 0;

    private void init() {
        try {

            sequencerSocket = new ReliableServerSocket(Protocol.SEQUENCER_PORT);
            while (true) {

                System.out.println("Waiting...");
                ReliableSocket socket = (ReliableSocket)sequencerSocket.accept();
                new Thread(new Handler(socket,sequenceNumber)).start();
                System.out.println(sequenceNumber + " Connected...");
                sequenceNumber++;

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

        public void sendToReplica(int city, byte[] sendBuffer) {

            int[] replicaPorts =  new int[3];


            switch (city) {
                case Protocol.MTL:
                    replicaPorts[0] = Protocol.FIRST_REPLICA_PORT_MTL;
                    replicaPorts[1] = Protocol.SECOND_REPLICA_PORT_MTL;
                    replicaPorts[2] = 1;
                    break;
                case Protocol.NDL:
                    replicaPorts[0] = Protocol.FIRST_REPLICA_PORT_NDL;
                    replicaPorts[1] = Protocol.SECOND_REPLICA_PORT_NDL;
                    replicaPorts[2] = 1;
                    break;
                case Protocol.WA:
                    replicaPorts[0] = Protocol.FIRST_REPLICA_PORT_WA;
                    replicaPorts[1] = Protocol.SECOND_REPLICA_PORT_WA;
                    replicaPorts[2] = 1;
                    break;
            }

            //for(int i=0;i<3;i++){}
            try {
                ReliableSocket sendToReplica = new ReliableSocket();

                sendToReplica.connect(new InetSocketAddress("127.0.0.1" , replicaPorts[0]));

                OutputStream out = sendToReplica.getOutputStream();
                out.write(sendBuffer);

                out.flush();
                out.close();
                sendToReplica.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            try {
                InputStream in = socket.getInputStream();
                byte[] buffer = new byte[1024];
                int size = in.read(buffer);

                String msg = new String(buffer,0,size);

                System.out.println(msg);

                String[] tokenizer = msg.split(",");

                int city = Integer.valueOf(tokenizer[0]);

                String newMsg = Protocol.mergeMsg(Arrays.copyOfRange(tokenizer, 1, tokenizer.length));

                byte[] sendBuffer = Protocol.createSequencerMsg(seq, newMsg);

                in.close();
                socket.close();

                sendToReplica(city, sendBuffer);



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
