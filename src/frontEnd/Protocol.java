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

    public static byte[] createFrontEndMsg(int method , String... payloads) {

        StringBuilder msg = new StringBuilder();
        msg.append(method + ",");

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
}
