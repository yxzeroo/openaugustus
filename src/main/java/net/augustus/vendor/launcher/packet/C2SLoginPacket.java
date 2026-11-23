package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.NettyPacketBuf;
public class C2SLoginPacket implements LauncherPacket {
   private byte[] hwidHash;
   private String username;
   private byte[] usernameHash;
   private byte[] passwordHash;

   public byte[] getUsernameHash(long l) {
      return this.usernameHash;
   }

   public byte[] getHwidHash(short s, int n, int n2) {
      return this.hwidHash;
   }

   public C2SLoginPacket(byte[] bytes, byte[] bytes2, byte[] bytes3, String str) {
      this.usernameHash = bytes;
      this.passwordHash = bytes2;
      this.hwidHash = bytes3;
      this.username = str;
   }

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
      nettyPacketBuf.writeByteArray(this.usernameHash);
      nettyPacketBuf.writeNullTerminatedString(this.username);
      nettyPacketBuf.writeByteArray(this.passwordHash);
      nettyPacketBuf.writeByteArray(this.hwidHash);
   }

   public C2SLoginPacket() {
   }

   public byte[] getPasswordHash(long l) {
      return this.passwordHash;
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      this.usernameHash = nettyPacketBuf.readByteArray();
      this.username = nettyPacketBuf.readNullTerminatedString();
      this.passwordHash = nettyPacketBuf.readByteArray();
      this.hwidHash = nettyPacketBuf.readByteArray();
   }



}
