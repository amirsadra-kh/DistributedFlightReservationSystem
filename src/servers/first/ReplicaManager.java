package servers.first;

import frontEnd.Protocol;
import net.rudp.ReliableServerSocket;
import net.rudp.ReliableSocket;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by masseeh on 11/1/16.
 */
public class ReplicaManager {
    public static int NUMBER = 3;
    public static HashMap<String , Integer> flightCountPorts = new HashMap<>();
    public static HashMap<String , Integer> transferPorts = new HashMap<>();
    public static int ID = 0;

    private int replicaManagerPort;
    int[] errors;

    public ReplicaManager(int replicaManagerPort) {
        this.replicaManagerPort = replicaManagerPort;
        errors = new int[3];
    }

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

        ReplicaManager replicaManager = new ReplicaManager(Integer.valueOf(args[0]));

        replicaManager.listen();

    }

    public void listen() {
        try {
            ReliableServerSocket serverSocket = new ReliableServerSocket(replicaManagerPort);

            while (true) {

                ReliableSocket socket = (ReliableSocket)serverSocket.accept();
                new Thread(new Handler(socket)).start();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restartReplica(int idx) {



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

                in.close();
                socket.close();

                String msg = new String(buffer,0, size);

                int city = Integer.valueOf(msg);

                switch (city) {
                    case Protocol.MTL:
                        errors[0]++;
                        break;
                    case Protocol.NDL:
                        errors[1]++;
                        break;
                    case Protocol.WA:
                        errors[2]++;
                        break;
                }

                for (int i=0; i<3; i++) {
                    if (errors[i] >=0 ) {
                        restartReplica(i);
                        errors[i] = 0;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
