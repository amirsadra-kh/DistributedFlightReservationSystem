package shared;


/**
* shared/FSInterfacePOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from frontEnd/Interface.idl
* Friday, December 2, 2016 6:48:31 PM EST
*/

public abstract class FSInterfacePOA extends org.omg.PortableServer.Servant
 implements shared.FSInterfaceOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("bookFlight", new java.lang.Integer (0));
    _methods.put ("getBookedFlightCount", new java.lang.Integer (1));
    _methods.put ("editRecord", new java.lang.Integer (2));
    _methods.put ("addFlight", new java.lang.Integer (3));
    _methods.put ("removeFlight", new java.lang.Integer (4));
    _methods.put ("transferReservation", new java.lang.Integer (5));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // shared/FSInterface/bookFlight
       {
         String firsName = in.read_string ();
         String lastName = in.read_string ();
         String address = in.read_string ();
         String phone = in.read_string ();
         String destination = in.read_string ();
         String date = in.read_string ();
         int flightClass = in.read_long ();
         int $result = (int)0;
         $result = this.bookFlight (firsName, lastName, address, phone, destination, date, flightClass);
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       case 1:  // shared/FSInterface/getBookedFlightCount
       {
         String $result = null;
         $result = this.getBookedFlightCount ();
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 2:  // shared/FSInterface/editRecord
       {
         String recordId = in.read_string ();
         String fieldName = in.read_string ();
         String newValue = in.read_string ();
         int $result = (int)0;
         $result = this.editRecord (recordId, fieldName, newValue);
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       case 3:  // shared/FSInterface/addFlight
       {
         String destination = in.read_string ();
         String date = in.read_string ();
         int ec = in.read_long ();
         int bus = in.read_long ();
         int fir = in.read_long ();
         int $result = (int)0;
         $result = this.addFlight (destination, date, ec, bus, fir);
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       case 4:  // shared/FSInterface/removeFlight
       {
         int recordId = in.read_long ();
         int $result = (int)0;
         $result = this.removeFlight (recordId);
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       case 5:  // shared/FSInterface/transferReservation
       {
         String clientId = in.read_string ();
         String currentCity = in.read_string ();
         String otherCity = in.read_string ();
         int $result = (int)0;
         $result = this.transferReservation (clientId, currentCity, otherCity);
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:shared/FSInterface:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public FSInterface _this() 
  {
    return FSInterfaceHelper.narrow(
    super._this_object());
  }

  public FSInterface _this(org.omg.CORBA.ORB orb) 
  {
    return FSInterfaceHelper.narrow(
    super._this_object(orb));
  }


} // class FSInterfacePOA
