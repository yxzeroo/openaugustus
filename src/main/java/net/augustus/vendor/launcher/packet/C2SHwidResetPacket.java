package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.NettyPacketBuf;
public class C2SHwidResetPacket implements LauncherPacket {
   private byte[] hwidHash;
   private byte[] hardwareSummaryHash;
   private byte[] activationKey;
   private byte[] passwordHash;
   private byte[] usernameHash;

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
      nettyPacketBuf.writeByteArray(this.usernameHash);
      nettyPacketBuf.writeByteArray(this.passwordHash);
      nettyPacketBuf.writeByteArray(this.hwidHash);
      nettyPacketBuf.writeByteArray(this.hardwareSummaryHash);
      nettyPacketBuf.writeByteArray(this.activationKey);
   }

   public C2SHwidResetPacket() {
   }

   public C2SHwidResetPacket(byte[] bytes, byte[] bytes2, byte[] bytes3, byte[] bytes4, byte[] bytes5) {
      this.usernameHash = bytes;
      this.passwordHash = bytes2;
      this.hwidHash = bytes3;
      this.hardwareSummaryHash = bytes4;
      this.activationKey = bytes5;
   }

   public byte[] getPasswordHash(long l) {
      return this.passwordHash;
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      this.usernameHash = nettyPacketBuf.readByteArray();
      this.passwordHash = nettyPacketBuf.readByteArray();
      this.hwidHash = nettyPacketBuf.readByteArray();
      this.hardwareSummaryHash = nettyPacketBuf.readByteArray();
      this.activationKey = nettyPacketBuf.readByteArray();
   }

   public byte[] getActivationKey(long l) {
      return this.activationKey;
   }

   public byte[] getUsernameHash(long l) {
      return this.usernameHash;
   }

   public byte[] getHwidHash(long l) {
      return this.hwidHash;
   }



}
