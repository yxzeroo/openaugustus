package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class SessionTokenRefreshPacket implements VendorPacket {
   private byte[] sessionToken;

   public SessionTokenRefreshPacket(byte[] bytes, String str, String str2, String str3) {
      this.sessionToken = bytes;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionToken = buf.readByteArray();
   }

   public byte[] getSessionToken() {
      return this.sessionToken;
   }

   public SessionTokenRefreshPacket() {
   }
}
