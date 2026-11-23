package net.augustus.vendor.launcher.net;

public class HandshakeKeyTable {
   public static String[] SERVER_KEYS;
   public static String[] CLIENT_KEYS;

   public static void initialize() {
      ClientHandshakeKeyTableData.populateClientKeys();
      ServerHandshakeKeyTableData.populateServerKeys();
   }



   static {

      CLIENT_KEYS = new String[200];
      SERVER_KEYS = new String[200];
   }
}
