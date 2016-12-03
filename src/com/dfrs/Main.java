package com.dfrs;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import shared.FSInterface;
import shared.FSInterfaceHelper;

public class Main {

    public static void main(String[] args) {

        ORB orb = ORB.init(args,null);

        try {
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);


            for (int i=0;i<2;i++) {
                final int p = i;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println(p);
                            FSInterface fsInterface = FSInterfaceHelper.narrow(ncRef.resolve_str("frontEnd"));
                            fsInterface.removeFlight("HIII " + p);

                        } catch (NotFound notFound) {
                            notFound.printStackTrace();
                        } catch (CannotProceed cannotProceed) {
                            cannotProceed.printStackTrace();
                        } catch (org.omg.CosNaming.NamingContextPackage.InvalidName invalidName) {
                            invalidName.printStackTrace();
                        }

                    }
                }).start();
            }

        } catch (InvalidName invalidName) {
            invalidName.printStackTrace();
        }


    }
}
