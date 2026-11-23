package net.augustus.vendor.launcher.crypto;

import java.security.InvalidParameterException;

public class HexEncoder {

   private static char nibbleToHexChar(byte b) {
      return b < '\n' ? (char)('0' + b) : (char)('A' + b - 10);
   }

   public static String encodeHex(byte[] bytes) {
      if (bytes == null) {
         throw new InvalidParameterException("Bytes array cannot be null");
      } else {
         StringBuilder sb = new StringBuilder();

         for (byte b : bytes) {
            appendHexByte(b, sb);
         }

         return sb.toString();
      }
   }

   private static void appendHexByte(byte b, StringBuilder sb) {
      byte b2 = (byte)(b & 15);
      byte b3 = (byte)(b >> 4 & 15);
      sb.append(nibbleToHexChar(b3));
      sb.append(nibbleToHexChar(b2));
   }




}
