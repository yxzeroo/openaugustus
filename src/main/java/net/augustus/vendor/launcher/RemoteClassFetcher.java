package net.augustus.vendor.launcher;


import net.augustus.vendor.launcher.net.LauncherClientHandler;
import net.augustus.vendor.launcher.packet.C2SRemoteClassRequestPacket;
public class RemoteClassFetcher {
   public String requestedClassName;
   public static final Object RESPONSE_LOCK;
   public byte[] sessionToken;

   public byte[] requestClassBytes() {
      LauncherClientHandler.getInstance().sendPacket(new C2SRemoteClassRequestPacket(this.sessionToken, this.requestedClassName));
      synchronized (RESPONSE_LOCK) {
         while (LauncherClientHandler.getInstance().remoteClassResponseBytes == null) {
            this.waitForResponse();
         }
      }

      return LauncherClientHandler.getInstance().remoteClassResponseBytes;
   }

   public void waitForResponse() {
      try {
         RESPONSE_LOCK.wait();
      } catch (InterruptedException e) {
      }
   }




   static {
      RESPONSE_LOCK = new Object();
   }
}
