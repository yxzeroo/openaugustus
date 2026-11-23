package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.NettyPacketBuf;
public class C2SActivateClientPacket implements LauncherPacket {
   public byte[] sessionToken;
   public byte[] activationKey;

   public C2SActivateClientPacket(byte[] bytes, byte[] bytes2) {
      this.activationKey = bytes;
      this.sessionToken = bytes2;
   }

   public byte[] getActivationKey(long l) {
      return this.activationKey;
   }

   public C2SActivateClientPacket() {
   }

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
      nettyPacketBuf.writeByteArray(this.activationKey);
      nettyPacketBuf.writeByteArray(this.sessionToken);
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      this.activationKey = nettyPacketBuf.readByteArray();
      this.sessionToken = nettyPacketBuf.readByteArray();
   }

   public byte[] getSessionToken(long l) {
      return this.sessionToken;
   }



}
