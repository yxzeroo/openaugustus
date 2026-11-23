package net.augustus.vendor.launcher.util;

import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

public class Base64Encoder {
   private static final int MIMELINEMAX = 0;
   private static final byte[] CRLF = new byte[]{13, 10};
   public static final Base64Encoder RFC2045 = new Base64Encoder(false, CRLF, 76, true);
   public static final Base64Encoder RFC4648 = new Base64Encoder(false, null, -1, true);
   private final boolean doPadding;
   private static final char[] toBase64 = new char[]{
      'A',
      'B',
      'C',
      'D',
      'E',
      'F',
      'G',
      'H',
      'I',
      'J',
      'K',
      'L',
      'M',
      'N',
      'O',
      'P',
      'Q',
      'R',
      'S',
      'T',
      'U',
      'V',
      'W',
      'X',
      'Y',
      'Z',
      'a',
      'b',
      'c',
      'd',
      'e',
      'f',
      'g',
      'h',
      'i',
      'j',
      'k',
      'l',
      'm',
      'n',
      'o',
      'p',
      'q',
      'r',
      's',
      't',
      'u',
      'v',
      'w',
      'x',
      'y',
      'z',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '+',
      '/'
   };
   private final boolean isURL;
   private final byte[] newline;
   private final int linemax;
   private static final char[] toBase64URL = new char[]{
      'A',
      'B',
      'C',
      'D',
      'E',
      'F',
      'G',
      'H',
      'I',
      'J',
      'K',
      'L',
      'M',
      'N',
      'O',
      'P',
      'Q',
      'R',
      'S',
      'T',
      'U',
      'V',
      'W',
      'X',
      'Y',
      'Z',
      'a',
      'b',
      'c',
      'd',
      'e',
      'f',
      'g',
      'h',
      'i',
      'j',
      'k',
      'l',
      'm',
      'n',
      'o',
      'p',
      'q',
      'r',
      's',
      't',
      'u',
      'v',
      'w',
      'x',
      'y',
      'z',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '-',
      '_'
   };
   public static final Base64Encoder RFC4648_URLSAFE = new Base64Encoder(true, null, -1, true);

   private int encode0(byte[] bytes, int n, int n2, byte[] bytes2) {
      char[] chars = this.isURL ? toBase64URL : toBase64;
      int n3 = n;
      int n4 = (n2 - n) / 3 * 3;
      int n5 = n + n4;
      if (this.linemax > 0 && n4 > this.linemax / 4 * 3) {
         n4 = this.linemax / 4 * 3;
      }

      int n6 = 0;

      while (n3 < n5) {
         int n7 = Math.min(n3 + n4, n5);
         int n8 = n3;
         int n9 = n6;

         while (n8 < n7) {
            int n10 = (bytes[n8++] & 255) << 16 | (bytes[n8++] & 255) << 8 | bytes[n8++] & 255;
            bytes2[n9++] = (byte)chars[n10 >>> 18 & 63];
            bytes2[n9++] = (byte)chars[n10 >>> 12 & 63];
            bytes2[n9++] = (byte)chars[n10 >>> 6 & 63];
            bytes2[n9++] = (byte)chars[n10 & 63];
         }

         n8 = (n7 - n3) / 3 * 4;
         n6 += n8;
         n3 = n7;
         if (n8 == this.linemax && n7 < n2) {
            for (byte b : this.newline) {
               bytes2[n6++] = b;
            }
         }
      }

      if (n3 < n2) {
         int n11 = bytes[n3++] & 255;
         bytes2[n6++] = (byte)chars[n11 >> 2];
         if (n3 == n2) {
            bytes2[n6++] = (byte)chars[n11 << 4 & 63];
            if (this.doPadding) {
               bytes2[n6++] = 61;
               bytes2[n6++] = 61;
            }
         } else {
            int n12 = bytes[n3++] & 255;
            bytes2[n6++] = (byte)chars[n11 << 4 & 63 | n12 >> 4];
            bytes2[n6++] = (byte)chars[n12 << 2 & 63];
            if (this.doPadding) {
               bytes2[n6++] = 61;
            }
         }
      }

      return n6;
   }

   private final int outLength(int n) {
      int n2 = 0;
      if (this.doPadding) {
         n2 = 4 * ((n + 2) / 3);
      } else {
         int n3 = n % 3;
         n2 = 4 * (n / 3) + (n3 == 0 ? 0 : n3 + 1);
      }

      if (this.linemax > 0) {
         n2 += (n2 - 1) / this.linemax * this.newline.length;
      }

      return n2;
   }

   private Base64Encoder(boolean flag, byte[] bytes, int n, boolean flag2) {
      this.isURL = flag;
      this.newline = bytes;
      this.linemax = n;
      this.doPadding = flag2;
   }

   public static char[] getToBase64URL() {
      return toBase64URL;
   }

   public byte[] encode2(byte[] bytes) {
      byte[] bytes2 = new byte[this.outLength(bytes.length)];
      int n2 = this.encode0(bytes, (byte)0, bytes.length, bytes2);
      return n2 != bytes2.length ? Arrays.copyOf(bytes2, n2) : bytes2;
   }

   public int encode(byte[] bytes, byte[] bytes2) {
      int n = this.outLength(bytes.length);
      if (bytes2.length < n) {
         throw new IllegalArgumentException("Output byte array is too small for encoding all input bytes");
      } else {
         return this.encode0(bytes, (byte)0, bytes.length, bytes2);
      }
   }

   public Base64Encoder(boolean flag, byte[] bytes, int n, boolean flag2, Base64EncoderSyntheticMarker marker) {
      this(flag, bytes, n, flag2);
   }

   public Base64Encoder withoutPadding(long l) {
      return !this.doPadding ? this : new Base64Encoder(this.isURL, this.newline, this.linemax, false);
   }

   public String encodeToString(byte[] bytes) {
      byte[] bytes2 = this.encode2(bytes);
      return new String(bytes2, 0, 0, bytes2.length);
   }

   public ByteBuffer encode(ByteBuffer buf) {
      byte[] bytes = new byte[this.outLength(buf.remaining())];
      int n3 = 0;
      if (buf.hasArray()) {
         byte[] bytes2 = buf.array();
         int n4 = buf.arrayOffset() + buf.limit();
         n3 = this.encode0(bytes2, buf.arrayOffset() + buf.position(), n4, bytes);
         ((Buffer)buf).position(buf.limit());
      } else {
         byte[] bytes4 = new byte[buf.remaining()];
         buf.get(bytes4);
         n3 = this.encode0(bytes4, (byte)0, bytes4.length, bytes);
      }

      if (n3 != bytes.length) {
         bytes = Arrays.copyOf(bytes, n3);
      }

      return ByteBuffer.wrap(bytes);
   }

   public OutputStream wrap(OutputStream out) {
      Objects.requireNonNull(out);
      return new Base64EncodingOutputStream(out, this.isURL ? toBase64URL : toBase64, this.newline, this.linemax, this.doPadding);
   }

   public static char[] getToBase64() {
      return toBase64;
   }

}
