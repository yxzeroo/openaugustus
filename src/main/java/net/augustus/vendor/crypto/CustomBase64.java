package net.augustus.vendor.crypto;

import java.util.Objects;

public class CustomBase64 {

   public static CustomBase64Encoder getMimeEncoder(int n, byte[] bytes, long l) {
      boolean flag = false;
      Object obj = null;
      byte b = 0;
      int n2 = 0;
      Object obj2 = null;
      Objects.requireNonNull(bytes);
      obj2 = CustomBase64Decoder.getFromBase64();
      obj = bytes;
      n2 = ((byte[])obj).length;

      for (int i = 0; i < n2; i++) {
         b = ((byte[])obj)[i];
         if (((int[])obj2)[b & 255] != -1) {
            throw new IllegalArgumentException("Illegal base64 line separator character 0x" + Integer.toString(b, 16));
         }
      }

      return n > 0 ? new CustomBase64Encoder(false, bytes, n >> 2 << 2, true, null) : CustomBase64Encoder.RFC4648;
   }

   public static CustomBase64Decoder getRfc20452() {
      return CustomBase64Decoder.RFC2045;
   }

   public static CustomBase64Encoder getRfc4648Urlsafe2() {
      return CustomBase64Encoder.RFC4648_URLSAFE;
   }

   private CustomBase64() {
   }

   public static CustomBase64Encoder getRfc2045() {
      return CustomBase64Encoder.RFC2045;
   }

   public static CustomBase64Decoder getRfc4648Urlsafe() {
      return CustomBase64Decoder.RFC4648_URLSAFE;
   }

   public static CustomBase64Encoder getRfc4648() {
      return CustomBase64Encoder.RFC4648;
   }

   public static CustomBase64Decoder getRfc46482() {
      return CustomBase64Decoder.RFC4648;
   }

}
