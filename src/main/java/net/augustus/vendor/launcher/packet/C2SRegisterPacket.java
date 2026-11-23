package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.NettyPacketBuf;
public class C2SRegisterPacket implements LauncherPacket {
   private byte[] passwordHash;
   private String username;
   private byte[] activationKey;
   private byte[] hardwareSummaryHash;
   private byte[] usernameHash;
   private byte[] hwidHash;

   public byte[] getHwidHash(long l) {
      return this.hwidHash;
   }

   public byte[] getHardwareSummaryHash(long l) {
      return this.hardwareSummaryHash;
   }

   public byte[] getActivationKey(long l) {
      return this.activationKey;
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      this.usernameHash = nettyPacketBuf.readByteArray();
      this.passwordHash = nettyPacketBuf.readByteArray();
      this.activationKey = nettyPacketBuf.readByteArray();
      this.hwidHash = nettyPacketBuf.readByteArray();
      this.hardwareSummaryHash = nettyPacketBuf.readByteArray();
      this.username = nettyPacketBuf.readNullTerminatedString();
   }

   public byte[] getPasswordHash(long l) {
      return this.passwordHash;
   }

   public byte[] getUsernameHash(int n, char ch, char ch2) {
      return this.usernameHash;
   }

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
      nettyPacketBuf.writeByteArray(this.usernameHash);
      nettyPacketBuf.writeByteArray(this.passwordHash);
      nettyPacketBuf.writeByteArray(this.activationKey);
      nettyPacketBuf.writeByteArray(this.hwidHash);
      nettyPacketBuf.writeByteArray(this.hardwareSummaryHash);
      nettyPacketBuf.writeNullTerminatedString(this.username);
   }

   public C2SRegisterPacket(byte[] bytes, byte[] bytes2, byte[] bytes3, byte[] bytes4, byte[] bytes5, String str) {
      this.usernameHash = bytes;
      this.passwordHash = bytes2;
      this.activationKey = bytes3;
      this.hwidHash = bytes4;
      this.hardwareSummaryHash = bytes5;
      this.username = str;
   }

   public String getUsername(long l) {
      return this.username;
   }

   public C2SRegisterPacket() {
   }




}
