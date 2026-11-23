package net.augustus.vendor.crypto;

import java.security.InvalidParameterException;

public class HexByteEncoder {

   private static void appendHexByte(byte b, StringBuilder sb) {
      byte b2 = 0;
      byte b3 = 0;
      b2 = (byte)(b & 15);
      b3 = (byte)(b >> 4 & 15);
      sb.append(nibbleToHexChar(b3));
      sb.append(nibbleToHexChar(b2));
   }

   public static String encodeHex(byte[] bytes) {
      Object obj = null;
      int n = 0;
      Object obj2 = null;
      boolean flag = false;
      byte b = 0;
      if (bytes == null) {
         throw new InvalidParameterException("Bytes array cannot be null");
      } else {
         obj = new StringBuilder();
         obj2 = bytes;
         n = ((byte[])obj2).length;

         for (int i = 0; i < n; i++) {
            b = ((byte[])obj2)[i];
            appendHexByte(b, (StringBuilder)obj);
         }

         return ((StringBuilder)obj).toString();
      }
   }

   private static char nibbleToHexChar(byte b) {
      return b < '\n' ? (char)('0' + b) : (char)('A' + b - 10);
   }




}
