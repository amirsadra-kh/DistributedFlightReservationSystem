package servers.first;

import java.util.HashMap;

/**
 * Created by masseeh on 11/1/16.
 */
public class ReplicaManager {
    public static int NUMBER = 3;
    public static HashMap<String , Integer> flightCountPorts = new HashMap<>();
    public static HashMap<String , Integer> transferPorts = new HashMap<>();
    public static int ID = 0;


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


    }
}
