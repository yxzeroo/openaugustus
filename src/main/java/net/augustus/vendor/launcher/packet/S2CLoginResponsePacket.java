package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.EncryptedClassLoader;
import net.augustus.vendor.launcher.NettyPacketBuf;
public class S2CLoginResponsePacket implements LauncherPacket {
   private int statusCode;
   private byte[] sessionToken;
   private String message;
   private String availableClients;
   private byte[] sessionSecret;

   public byte[] getSessionToken() {
      EncryptedClassLoader.loginSessionToken = this.sessionToken;
      return this.sessionToken;
   }

   public int getStatusCode() {
      return this.statusCode;
   }

   public S2CLoginResponsePacket(int n, byte[] bytes, byte[] bytes2, long l, String str, String str2) {
      this.statusCode = n;
      this.sessionToken = bytes;
      this.sessionSecret = bytes2;
      this.availableClients = str;
      this.message = str2;
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      this.statusCode = nettyPacketBuf.readEnumOrdinal();
      this.sessionToken = nettyPacketBuf.readByteArray();
      this.sessionSecret = nettyPacketBuf.readByteArray();
      this.availableClients = nettyPacketBuf.readNullTerminatedString();
      this.message = nettyPacketBuf.readNullTerminatedString();
   }

   public S2CLoginResponsePacket() {
   }

   public byte[] getSessionSecret() {
      return this.sessionSecret;
   }

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
   }

   public String getMessage() {
      return this.message;
   }

   public String getAvailableClients() {
      return this.availableClients;
   }




}
