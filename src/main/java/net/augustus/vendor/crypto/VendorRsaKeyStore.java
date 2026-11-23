package net.augustus.vendor.crypto;

public class VendorRsaKeyStore {
   public static String[] serverKeys;
   public static String[] clientKeys;

   public static void loadAllKeys() {
      VendorRsaKeyTableInitializer.loadServerKeys();
      VendorClientRsaKeyLoader.loadClientKeys();
   }

   static {
      long[] longs = new long[]{168703034031866056L, -776164374946512696L};
      serverKeys = new String[(int)longs[1]];
      clientKeys = new String[(int)longs[0]];
   }

}
