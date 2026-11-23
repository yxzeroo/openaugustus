package net.augustus.vendor.packet;

import net.augustus.vendor.crypto.SplitInterleaveTokenCipher;
import net.augustus.settings.Setting;
import net.augustus.vendor.crypto.MultiRoundKeyedTokenCipher;
import net.augustus.vendor.network.VendorPacketBuffer;
import net.augustus.vendor.crypto.SBoxSubstitutionTokenCipher;
import net.augustus.vendor.crypto.LcgStreamTokenCipher;

public class ClientAuthLoginPacket implements VendorPacket {
   private byte[] sessionToken;
   public static byte[] obfuscatedAuthDigest;
   private byte[] authPayload;

   public ClientAuthLoginPacket() {
   }

   public ClientAuthLoginPacket(byte[] bytes, byte[] bytes2) {
      this.sessionToken = bytes;
      this.authPayload = bytes2;
      LcgStreamTokenCipher lcgStreamTokenCipher = new LcgStreamTokenCipher(-889318860);
      SplitInterleaveTokenCipher cipher = new SplitInterleaveTokenCipher(-6066929684898893296L, 287454020);
      SBoxSubstitutionTokenCipher cipher2 = new SBoxSubstitutionTokenCipher(1437269760, (byte)126);
      MultiRoundKeyedTokenCipher cipher3 = new MultiRoundKeyedTokenCipher(81985529216486895L, -2023406815);
      int length = bytes2.length;
      byte[] bytes5 = cipher.transform(lcgStreamTokenCipher.transform(bytes2, 54321), new byte[]{-95, -78, -61, -44, -27});
      int[] ints = new int[]{17, 23, 31, 42};
      Setting[] opaqueStateTag = CosmeticsRemovePacket.getOpaqueStateTag();
      byte[] bytes8 = cipher3.transform(cipher2.transform(bytes5, ints, -7457980998977572336L), new byte[]{-66, -17, 0, 0}, (byte)3);
      byte[] bytes9 = new byte[length];
      System.arraycopy(bytes8, 0, bytes9, 0, Math.min(bytes8.length, length));
      obfuscatedAuthDigest = bytes9;
      if (opaqueStateTag == null) {
         Setting.setControlFlowMarker("iYXPgb");
      }
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
      buf.writeByteArray(this.authPayload);
   }

   public byte[] getAuthPayload() {
      return this.authPayload;
   }

   public byte[] getSessionToken() {
      return this.sessionToken;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionToken = buf.readByteArray();
      this.authPayload = buf.readByteArray();
   }
}
