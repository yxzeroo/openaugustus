package net.augustus.vendor.crypto;

import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

public class CustomBase64Encoder {
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
   public static final CustomBase64Encoder RFC4648_URLSAFE = new CustomBase64Encoder(true, null, -1, true);
   private final int linemax;
   private final byte[] newline;
   public static final CustomBase64Encoder RFC4648 = new CustomBase64Encoder(false, null, -1, true);
   private static int MIMELINEMAX;
   private static final byte[] CRLF = new byte[]{13, 10};
   public static final CustomBase64Encoder RFC2045 = new CustomBase64Encoder(false, CRLF, 76, true);
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
   private final boolean doPadding;

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

   private CustomBase64Encoder(boolean flag, byte[] bytes, int n, boolean flag2) {
      this.isURL = flag;
      this.newline = bytes;
      this.linemax = n;
      this.doPadding = flag2;
   }

   public static char[] getToBase64() {
      return toBase64;
   }

   public CustomBase64Encoder withoutPadding(long l) {
      return this.doPadding ? new CustomBase64Encoder(this.isURL, this.newline, this.linemax, false) : this;
   }

   public static char[] getToBase64URL() {
      return toBase64URL;
   }

   public CustomBase64Encoder(boolean flag, byte[] bytes, int n, boolean flag2, CustomBase64EncoderSynthetic synthetic) {
      this(flag, bytes, n, flag2);
   }

   public ByteBuffer encode(ByteBuffer buf) {
      int n = 0;
      Object obj = null;
      Object obj2 = null;
      int n2 = 0;
      CustomBase64Encoder customBase64Encoder = this;
      int n3 = buf.remaining();
      n = customBase64Encoder.outLength(n3);
      obj2 = new byte[n];
      n2 = 0;
      if (!buf.hasArray()) {
         obj = new byte[buf.remaining()];
         buf.get((byte[])obj);
         customBase64Encoder = this;
         byte[] bytes = (byte[])obj;
         int length = ((byte[])obj).length;
         byte[] bytes2 = (byte[])obj2;
         int n4 = length;
         byte b = 0;
         byte[] bytes3 = bytes;
         n2 = customBase64Encoder.encode0(bytes3, b, n4, bytes2);
      } else {
         customBase64Encoder = this;
         byte[] bytes4 = buf.array();
         int n5 = buf.arrayOffset() + buf.position();
         int n6 = buf.arrayOffset() + buf.limit();
         byte[] bytes5 = (byte[])obj2;
         int n7 = n6;
         int n8 = n5;
         byte[] bytes6 = bytes4;
         n2 = customBase64Encoder.encode0(bytes6, n8, n7, bytes5);
         ((Buffer)buf).position(buf.limit());
      }

      if (n2 != ((byte[])obj2).length) {
         obj2 = Arrays.copyOf((byte[])obj2, n2);
      }

      return ByteBuffer.wrap((byte[])obj2);
   }

   public int encode(byte[] bytes, byte[] bytes2) {
      int n = 0;
      CustomBase64Encoder customBase64Encoder = this;
      int length = bytes.length;
      n = customBase64Encoder.outLength(length);
      if (bytes2.length >= n) {
         customBase64Encoder = this;
         byte[] bytes3 = bytes;
         int length2 = bytes.length;
         byte[] bytes4 = bytes2;
         int n2 = length2;
         byte b = 0;
         byte[] bytes5 = bytes3;
         return customBase64Encoder.encode0(bytes5, b, n2, bytes4);
      } else {
         throw new IllegalArgumentException("Output byte array is too small for encoding all input bytes");
      }
   }

   public OutputStream wrap(long l, OutputStream out) {
      Objects.requireNonNull(out);
      return new CustomBase64OutputStream(out, this.isURL ? toBase64URL : toBase64, this.newline, this.linemax, this.doPadding);
   }

   public String encodeToString(byte[] bytes) {
      byte[] bytes2 = this.encode2(bytes);
      return new String(bytes2, 0, 0, bytes2.length);
   }

   public byte[] encode2(byte[] bytes) {
      int n = 0;
      Object obj = null;
      int n2 = 0;
      CustomBase64Encoder customBase64Encoder = this;
      int length = bytes.length;
      n2 = customBase64Encoder.outLength(length);
      obj = new byte[n2];
      customBase64Encoder = this;
      byte[] bytes2 = bytes;
      int length2 = bytes.length;
      byte[] bytes3 = (byte[])obj;
      int n3 = length2;
      byte b = 0;
      byte[] bytes4 = bytes2;
      n = customBase64Encoder.encode0(bytes4, b, n3, bytes3);
      return n == ((byte[])obj).length ? (byte[])obj : Arrays.copyOf((byte[])obj, n);
   }


   private final int outLength(int n) {
      int n2 = 0;
      int n3 = 0;
      n3 = 0;
      if (!this.doPadding) {
         n2 = n % 3;
         n3 = 4 * (n / 3) + (n2 == 0 ? 0 : n2 + 1);
      } else {
         n3 = 4 * ((n + 2) / 3);
      }

      if (this.linemax > 0) {
         n3 += (n3 - 1) / this.linemax * ((byte[])this.newline).length;
      }

      return n3;
   }

}
