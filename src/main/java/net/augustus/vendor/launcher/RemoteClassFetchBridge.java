package net.augustus.vendor.launcher;

public class RemoteClassFetchBridge {
   public static RemoteClassFetcher fetcher;
   public static byte[] scratchTable;

   public static byte[] fetchRemoteClass(byte[] bytes, String str) {
      fetcher.sessionToken = bytes;
      fetcher.requestedClassName = str;
      return fetcher.requestClassBytes();
   }

   public static void initialize() {
      if (fetcher == null) {
         fetcher = new RemoteClassFetcher();
         scratchTable = new byte[100];
      } else {
         for (int i = 0; i < 100; i++) {
            if (i < 81) {
               scratchTable[i] = (byte)i;
            } else {
               scratchTable[i] = (byte)(i ^ 85);
            }
         }
      }
   }



}
