package net.augustus.vendor.launcher.util;

import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

public class Base64Decoder {
   public static final Base64Decoder RFC4648_URLSAFE;
   private final boolean isURL;
   private final boolean isMIME;
   private static final int[] fromBase64URL;
   private static final int[] fromBase64 = new int[256];
   public static final Base64Decoder RFC2045;
   public static final Base64Decoder RFC4648;

   public int decode(byte[] bytes, byte[] bytes2) {
      int n = this.outLength(bytes, (byte)0, bytes.length);
      if (bytes2.length < n) {
         throw new IllegalArgumentException("Output byte array is too small for decoding all input bytes");
      } else {
         return this.decode0(bytes, (byte)0, bytes.length, bytes2);
      }
   }

   private int decode0(byte[] bytes, int n, int n2, byte[] bytes2) {
      int n3 = 0;
      int n4 = 0;
      byte b = 18;
      int[] ints = this.isURL ? fromBase64URL : fromBase64;

      while (n < n2) {
         int n5 = bytes[n++] & 255;
         if ((n5 = ints[n5]) >= 0) {
            n4 |= n5 << b;
            b -= 6;
            if (b < 0) {
               bytes2[n3++] = (byte)(n4 >> 16);
               bytes2[n3++] = (byte)(n4 >> 8);
               bytes2[n3++] = (byte)n4;
               n4 = 0;
               b = 18;
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

   static {
      Arrays.fill(fromBase64, -1);
      int n = 0;

      while (n < Base64Encoder.getToBase64().length) {
         fromBase64[Base64Encoder.getToBase64()[n]] = n++;
      }

      fromBase64[61] = -2;
      fromBase64URL = new int[256];
      Arrays.fill(fromBase64URL, -1);
      n = 0;

      while (n < Base64Encoder.getToBase64URL().length) {
         fromBase64URL[Base64Encoder.getToBase64URL()[n]] = n++;
      }

      fromBase64URL[61] = -2;
      RFC4648 = new Base64Decoder(false, false);
      RFC4648_URLSAFE = new Base64Decoder(true, false);
      RFC2045 = new Base64Decoder(false, true);
   }

   public byte[] decode3(String str) {
      return this.decode(str.getBytes(StandardCharsets.ISO_8859_1));
   }

   public static int[] getFromBase64() {
      return fromBase64;
   }

   public byte[] decode(byte[] bytes) {
      byte[] bytes2 = new byte[this.outLength(bytes, (byte)0, bytes.length)];
      int n = this.decode0(bytes, (byte)0, bytes.length, bytes2);
      if (n != bytes2.length) {
         bytes2 = Arrays.copyOf(bytes2, n);
      }

      return bytes2;
   }

   public ByteBuffer decode2(ByteBuffer buf) {
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

   private Base64Decoder(boolean flag, boolean flag2) {
      this.isURL = flag;
      this.isMIME = flag2;
   }

   private int outLength(byte[] bytes, int n, int n2) {
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
      return new Base64DecodingInputStream(in, this.isURL ? fromBase64URL : fromBase64, this.isMIME);
   }

}
