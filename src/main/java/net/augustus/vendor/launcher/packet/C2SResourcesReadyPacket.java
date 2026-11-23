package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.NettyPacketBuf;
public class C2SResourcesReadyPacket implements LauncherPacket {
   private boolean environmentClean;
   private String clientName;
   private byte[] sessionToken;
   private long timestamp;
   private byte[] hwidHash;

   public long getTimestamp(long l) {
      return this.timestamp;
   }

   public C2SResourcesReadyPacket(String str, byte[] bytes, byte[] bytes2, long l, boolean flag) {
      this.clientName = str;
      this.hwidHash = bytes;
      this.sessionToken = bytes2;
      this.timestamp = l;
      this.environmentClean = flag;
   }

   public C2SResourcesReadyPacket() {
   }

   public byte[] getSessionToken(long l) {
      return this.sessionToken;
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      this.clientName = nettyPacketBuf.readNullTerminatedString();
      this.hwidHash = nettyPacketBuf.readByteArray();
      this.sessionToken = nettyPacketBuf.readByteArray();
      this.timestamp = nettyPacketBuf.readLong();
      this.environmentClean = nettyPacketBuf.readBoolean();
   }

   public byte[] getHwidHash(long l) {
      return this.hwidHash;
   }

   public String getClientName(long l, short s) {
      return this.clientName;
   }

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
      nettyPacketBuf.writeNullTerminatedString(this.clientName);
      nettyPacketBuf.writeByteArray(this.hwidHash);
      nettyPacketBuf.writeByteArray(this.sessionToken);
      nettyPacketBuf.writeLong(this.timestamp);
      nettyPacketBuf.writeBoolean(this.environmentClean);
   }



}
