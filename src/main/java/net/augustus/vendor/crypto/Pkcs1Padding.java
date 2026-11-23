package net.augustus.vendor.crypto;


public class Pkcs1Padding {

   public static byte[] unpad(byte[] bytes) {
      int n = 0;
      Object obj = null;
      int n2 = 0;
      if (bytes.length >= 11 && bytes[0] == 0 && bytes[1] == 2) {
         n = -1;

         for (int i = 2; i < ((byte[])bytes).length; i++) {
            if (bytes[i] == 0) {
               n = i;
               break;
            }
         }

         if (n >= 10) {
            n2 = bytes.length - n - 1;
            obj = new byte[n2];
            System.arraycopy(bytes, n + 1, (byte[])obj, 0, n2);
            return (byte[])obj;
         } else {
            throw new IllegalArgumentException("Invalid PKCS#1 padding");
         }
      } else {
         throw new IllegalArgumentException("Invalid PKCS#1 padding");
      }
   }

   public static byte[] pad(byte[] bytes, int n) {
      byte b = 0;
      Object obj = null;
      Object obj2 = null;
      boolean flag = false;
      int n2 = 0;
      Object obj3 = null;
      if (bytes.length > n - 11) {
         throw new IllegalArgumentException("Data too large for block size");
      } else {
         obj = new byte[n];
         ((byte[])obj)[0] = 0;
         ((byte[])obj)[1] = 2;
         obj2 = new CustomSecureRandom();
         n2 = n - bytes.length - 3;

         for (int i = 0; i < n2; i++) {
            do {
               obj3 = new byte[1];
               ((CustomSecureRandom)obj2).nextBytes((byte[])obj3);
               b = ((byte[])obj3)[0];
            } while (b == 0);

            ((byte[])obj)[i + 2] = b;
         }

         ((byte[])obj)[n2 + 2] = 0;
         System.arraycopy(bytes, 0, (byte[])obj, n2 + 3, bytes.length);
         return (byte[])obj;
      }
   }




}
