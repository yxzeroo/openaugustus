package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.NettyPacketBuf;
public class C2SRawPayloadPacket implements LauncherPacket {
   private byte[] payload;

   public C2SRawPayloadPacket() {
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      this.payload = nettyPacketBuf.readByteArray();
   }

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
      nettyPacketBuf.writeByteArray(this.payload);
   }

   public C2SRawPayloadPacket(int n, int n2, byte[] bytes) {
      this.payload = bytes;
   }



}
