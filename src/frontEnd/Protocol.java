package frontEnd;

/**
 * Created by masseeh on 12/3/16.
 */
public class Protocol {
    public static final int BOOK_FLIGHT = 0;
    public static final int GET_BOOKED_FLIGHT_COUNT = 1;
    public static final int EDIT_RECORD = 2;
    public static final int ADD_FLIGHT = 3;
    public static final int REMOVE_FLIGHT = 4;
    public static final int TRANSFER_RESERVATION = 5;

    public static final int MSG_LENGTH = 1024;
    public static final int TIME_OUT = 6000;

    public static final int MTL = 10;
    public static final int NDL = 11;
    public static final int WA = 12;

    public static final int FRONT_END_PORT = 9999;
    public static final int SEQUENCER_PORT = 9876;

    public static final int FIRST_REPLICA_PORT_MTL = 9998;
    public static final int FIRST_REPLICA_PORT_NDL = 9997;
    public static final int FIRST_REPLICA_PORT_WA = 9996;

    public static final int SECOND_REPLICA_PORT_MTL = 9995;
    public static final int SECOND_REPLICA_PORT_NDL = 9994;
    public static final int SECOND_REPLICA_PORT_WA = 9993;

    public static final int FIRST_REPLICA_MANAGER = 9992;
    public static final int SECOND_REPLICA_MANAGER = 9991;
    public static final int THIRD_REPLICA_MANAGER = 9990;

    public static final int HA_MODE = 1;
    public static final int BY_MODE = 2;




    public static String mergeMsg(String[] oldMsg) {

        StringBuilder msg = new StringBuilder();

        for(String payload : oldMsg) {
            msg.append(payload + ",");
        }

        msg.deleteCharAt(msg.length()-1);

        return msg.toString();

    }

    public static byte[] createFrontEndMsg(int city, int method, int clientId , String... payloads) {

        StringBuilder msg = new StringBuilder();
        msg.append(city + "," + method + "," + clientId + ",");

        for(String payload : payloads) {
            msg.append(payload + ",");
        }

        msg.deleteCharAt(msg.length()-1);

        return msg.toString().getBytes();
    }

    public static byte[] createSequencerMsg(long seq, String frontEndMsg) {
        String msg = seq + "," + frontEndMsg;
        return msg.getBytes();
    }

    public static String createLogMsg(int method, String... payloads) {

        StringBuilder msg = new StringBuilder();
        msg.append(method + ",");

        for(String payload : payloads) {
            msg.append(payload + ",");
        }

        msg.deleteCharAt(msg.length()-1);

        return msg.toString();

    }

    public static byte[] createResultMsg(int replica, int method, String seq, String result) {

        String msg = replica + "," + method + "," + seq + "," + result;

        return msg.getBytes();

    }
}
