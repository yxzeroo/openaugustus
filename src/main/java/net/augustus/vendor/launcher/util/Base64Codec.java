package net.augustus.vendor.launcher.util;

import net.augustus.vendor.launcher.crypto.Sha256Digest;
import java.util.Objects;

public class Base64Codec {

   public static Base64Decoder getRfc4648() {
      return Base64Decoder.RFC4648;
   }

   public static Base64Decoder getUrlDecoder(short s, short s2, int n) {
      return null;
   }

   private Base64Codec() {
   }

   public static Base64Encoder getMimeEncoder(long l) {
      return Base64Encoder.RFC2045;
   }

   public static Base64Encoder getRfc46482() {
      return Base64Encoder.RFC4648;
   }

   public static Base64Decoder getMimeDecoder(long l) {
      return null;
   }

   public static Base64Encoder getMimeEncoder(int n, byte[] bytes) {
      Objects.requireNonNull(bytes);
      Sha256Digest.getFlowState();
      int[] fromBase64 = Base64Decoder.getFromBase64();

      for (byte b : bytes) {
         if (fromBase64[b & 255] != -1) {
            throw new IllegalArgumentException("Illegal base64 line separator character 0x" + Integer.toString(b, 16));
         }
      }

      return n <= 0 ? Base64Encoder.RFC4648 : new Base64Encoder(false, bytes, n >> 2 << 2, true, null);
   }

   public static Base64Encoder getUrlEncoder(int n, char ch, int n2) {
      return Base64Encoder.RFC4648_URLSAFE;
   }

}
