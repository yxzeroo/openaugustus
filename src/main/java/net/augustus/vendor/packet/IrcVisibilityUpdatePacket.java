package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class IrcVisibilityUpdatePacket implements VendorPayload, VendorPacket {
   private int ackId;
   private int visibilityMode;
   private byte[] sessionToken;
   private boolean visible;

   public IrcVisibilityUpdatePacket() {
   }

   public IrcVisibilityUpdatePacket(byte[] bytes, boolean flag, int n, int n2) {
      this.sessionToken = bytes;
      this.visible = flag;
      this.visibilityMode = n;
      this.ackId = n2;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
      buf.writeBoolean(this.visible);
      buf.writeInt(this.visibilityMode);
      buf.writeInt(this.ackId);
   }

   @Override
   public void setSessionToken(byte[] bytes) {
      this.sessionToken = bytes;
   }
}
