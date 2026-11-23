package net.augustus.vendor.crypto;

import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

public class VendorBase64Decoder {
   private final boolean isURL;
   private static final int[] fromBase64URL;
   private static final int[] fromBase64 = new int[256];
   public static final VendorBase64Decoder RFC2045;
   public static final VendorBase64Decoder RFC4648_URLSAFE;
   public static final VendorBase64Decoder RFC4648;
   private final boolean isMIME;

   private int decode0(byte[] bytes, int n, int n2, byte[] bytes2) {
      int invertedGuardFlag = VendorAesHelper.getInvertedGuardFlag();
      int[] ints = this.isURL ? fromBase64URL : fromBase64;
      int n3 = 0;
      int n4 = 0;
      byte b = 18;

      while (n < n2) {
         int n5 = bytes[n++] & 255;
         if ((n5 = ints[n5]) >= 0) {
            n4 |= n5 << b;
            b -= 6;
            if (b < 0) {
               bytes2[n3++] = (byte)(n4 >> 16);
               bytes2[n3++] = (byte)(n4 >> 8);
               bytes2[n3++] = (byte)n4;
               b = 18;
               n4 = 0;
            }
         } else {
            if (n5 == -2) {
               if (b == 6 && (n == n2 || bytes[n++] != 61) || b == 18) {
                  throw new IllegalArgumentException("Input byte array has wrong 4-byte ending unit");
               }
               break;
            }

            if (!this.isMIME) {
               throw new IllegalArgumentException("Illegal base64 character " + Integer.toString(bytes[n - 1], 16));
            }
         }
      }

      if (b == 6) {
         bytes2[n3++] = (byte)(n4 >> 16);
      } else if (b == 0) {
         bytes2[n3++] = (byte)(n4 >> 16);
         bytes2[n3++] = (byte)(n4 >> 8);
      } else if (b == 12) {
         throw new IllegalArgumentException("Last unit does not have enough valid bits");
      }

      while (n < n2) {
         if (!this.isMIME || ints[bytes[n++]] >= 0) {
            throw new IllegalArgumentException("Input byte array has incorrect ending byte at " + n);
         }
      }

      return n3;
   }

   private VendorBase64Decoder(boolean flag, boolean flag2) {
      this.isURL = flag;
      this.isMIME = flag2;
   }

   public byte[] decode(byte[] bytes) {
      Object obj = null;
      int n = 0;
      int invertedGuardFlag = VendorAesHelper.getInvertedGuardFlag();
      VendorBase64Decoder vendorBase64Decoder = this;
      byte[] bytes2 = bytes;
      int length = bytes.length;
      byte b = 0;
      byte[] bytes3 = bytes2;
      obj = new byte[vendorBase64Decoder.outLength(bytes3, b, length)];
      vendorBase64Decoder = this;
      byte[] bytes4 = bytes;
      int length2 = bytes.length;
      byte[] bytes5 = (byte[])obj;
      int n2 = length2;
      byte b2 = 0;
      byte[] bytes6 = bytes4;
      n = vendorBase64Decoder.decode0(bytes6, b2, n2, bytes5);
      if (n != ((byte[])obj).length) {
         obj = Arrays.copyOf((byte[])obj, n);
      }

      return (byte[])obj;
   }

   public ByteBuffer decode3(ByteBuffer buf) {
      int guardFlag = VendorAesHelper.getGuardFlag();
      int n = buf.position();

      try {
         byte[] bytes;
         int n2;
         int n3;
         if (buf.hasArray()) {
            bytes = buf.array();
            n2 = buf.arrayOffset() + buf.position();
            n3 = buf.arrayOffset() + buf.limit();
            ((Buffer)buf).position(buf.limit());
         } else {
            bytes = new byte[buf.remaining()];
            buf.get(bytes);
            n2 = 0;
            n3 = bytes.length;
         }

         byte[] bytes2 = new byte[this.outLength(bytes, n2, n3)];
         return ByteBuffer.wrap(bytes2, 0, this.decode0(bytes, n2, n3, bytes2));
      } catch (IllegalArgumentException e) {
         ((Buffer)buf).position(n);
         throw e;
      }
   }

   public byte[] decode2(String str) {
      byte[] bytes = str.getBytes(StandardCharsets.ISO_8859_1);
      byte[] bytes2 = bytes;
      return this.decode(bytes2);
   }

   static {
      Arrays.fill(fromBase64, -1);
      int n = 0;

      while (n < VendorBase64Encoder.getToBase64().length) {
         fromBase64[VendorBase64Encoder.getToBase64()[n]] = n++;
      }

      fromBase64[61] = -2;
      fromBase64URL = new int[256];
      Arrays.fill(fromBase64URL, -1);
      n = 0;

      while (n < VendorBase64Encoder.getToBase64URL().length) {
         fromBase64URL[VendorBase64Encoder.getToBase64URL()[n]] = n++;
      }

      fromBase64URL[61] = -2;
      RFC4648 = new VendorBase64Decoder(false, false);
      RFC4648_URLSAFE = new VendorBase64Decoder(true, false);
      RFC2045 = new VendorBase64Decoder(false, true);
   }

   public static int[] getFromBase64() {
      return fromBase64;
   }

   private int outLength(byte[] bytes, int n, int n2) {
      int invertedGuardFlag = VendorAesHelper.getInvertedGuardFlag();
      int[] ints = this.isURL ? fromBase64URL : fromBase64;
      int n3 = 0;
      int n4 = n2 - n;
      if (n4 == 0) {
         return 0;
      } else if (n4 < 2) {
         if (this.isMIME && ints[0] == -1) {
            return 0;
         } else {
            throw new IllegalArgumentException("Input byte[] should at least have 2 bytes for base64 bytes");
         }
      } else {
         if (this.isMIME) {
            int n5 = 0;

            while (n < n2) {
               int n6 = bytes[n++] & 255;
               if (n6 == 61) {
                  n4 -= n2 - n + 1;
                  break;
               }

               if ((n6 = ints[n6]) == -1) {
                  n5++;
               }
            }

            n4 -= n5;
         } else if (bytes[n2 - 1] == 61) {
            n3++;
            if (bytes[n2 - 2] == 61) {
               n3++;
            }
         }

         if (n3 == 0 && (n4 & 3) != 0) {
            n3 = 4 - (n4 & 3);
         }

         return 3 * ((n4 + 3) / 4) - n3;
      }
   }

   public InputStream wrap(InputStream in) {
      Objects.requireNonNull(in);
      int[] ints = this.isURL ? fromBase64URL : fromBase64;
      boolean isMIME2 = this.isMIME;
      int[] ints2 = ints;
      return new VendorBase64InputStream(in, ints2, isMIME2);
   }

   public int decode(byte[] bytes, byte[] bytes2) {
      int n = 0;
      int guardFlag = VendorAesHelper.getGuardFlag();
      VendorBase64Decoder vendorBase64Decoder = this;
      byte[] bytes3 = bytes;
      int length = bytes.length;
      byte b = 0;
      byte[] bytes4 = bytes3;
      n = vendorBase64Decoder.outLength(bytes4, b, length);
      if (bytes2.length >= n) {
         vendorBase64Decoder = this;
         byte[] bytes5 = bytes;
         int length2 = bytes.length;
         byte[] bytes6 = bytes2;
         int n2 = length2;
         byte b2 = 0;
         byte[] bytes7 = bytes5;
         return vendorBase64Decoder.decode0(bytes7, b2, n2, bytes6);
      } else {
         throw new IllegalArgumentException("Output byte array is too small for decoding all input bytes");
      }
   }

}
