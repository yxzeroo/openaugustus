package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.NettyPacketBuf;
public class C2SStartClientPacket implements LauncherPacket {
   private byte[] nativesHash;
   private String clientName;
   private byte[] sessionToken;
   private byte[] hwidHash;
   private byte[] resourcesHash;

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
      nettyPacketBuf.writeByteArray(this.resourcesHash);
      nettyPacketBuf.writeByteArray(this.nativesHash);
      nettyPacketBuf.writeByteArray(this.sessionToken);
      nettyPacketBuf.writeByteArray(this.hwidHash);
      nettyPacketBuf.writeNullTerminatedString(this.clientName);
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      this.resourcesHash = nettyPacketBuf.readByteArray();
      this.nativesHash = nettyPacketBuf.readByteArray();
      this.sessionToken = nettyPacketBuf.readByteArray();
      this.hwidHash = nettyPacketBuf.readByteArray();
      this.clientName = nettyPacketBuf.readNullTerminatedString();
   }

   public C2SStartClientPacket(String str, byte[] bytes, byte[] bytes2, byte[] bytes3, byte[] bytes4) {
      this.clientName = str;
      this.resourcesHash = bytes;
      this.nativesHash = bytes2;
      this.sessionToken = bytes3;
      this.hwidHash = bytes4;
   }

   public byte[] getNativesHash(long l) {
      return this.nativesHash;
   }

   public byte[] getResourcesHash(long l) {
      return this.resourcesHash;
   }

   public byte[] getSessionToken(short s, long l) {
      return this.sessionToken;
   }

   public String getClientName(long l) {
      return this.clientName;
   }

   public C2SStartClientPacket() {
   }

   public byte[] getHwidHash(long l) {
      return this.hwidHash;
   }



}
