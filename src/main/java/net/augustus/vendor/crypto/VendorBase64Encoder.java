package net.augustus.vendor.crypto;

import net.augustus.vendor.crypto.base64.ShadedBase64EncoderSynthetic;
import net.augustus.vendor.crypto.base64.ShadedBase64OutputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

public class VendorBase64Encoder {
   private final int linemax;
   private static final byte[] CRLF = new byte[]{13, 10};
   private final boolean isURL;
   public static final VendorBase64Encoder RFC4648 = new VendorBase64Encoder(false, null, -1, true);
   public static final VendorBase64Encoder RFC2045 = new VendorBase64Encoder(false, CRLF, 76, true);
   public static final VendorBase64Encoder RFC4648_URLSAFE = new VendorBase64Encoder(true, null, -1, true);
   private static final int MIMELINEMAX = 76;
   private final boolean doPadding;
   private final byte[] newline;
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

   public int encode(byte[] bytes, byte[] bytes2) {
      int n = 0;
      byte b = 1;

      while (true) {
         while (1 < b) {
            if (2 >= b) {
               return this.encode0(bytes, (byte)0, bytes.length, bytes2);
            }

            if (bytes2.length < n) {
               throw new IllegalArgumentException("Output byte array is too small for encoding all input bytes");
            }
         }

         n = this.outLength(bytes.length);
      }
   }

   public String encodeToString(byte[] bytes) {
      byte[] bytes2 = this.encode(bytes);
      return new String(bytes2, 0, 0, bytes2.length);
   }

   private VendorBase64Encoder(boolean flag, byte[] bytes, int n, boolean flag2) {
      this.isURL = flag;
      this.newline = bytes;
      this.linemax = n;
      this.doPadding = flag2;
   }

   public static char[] getToBase64URL() {
      return toBase64URL;
   }

   public VendorBase64Encoder withoutPadding(long l) {
      return this.doPadding ? new VendorBase64Encoder(this.isURL, this.newline, this.linemax, false) : this;
   }

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

   public VendorBase64Encoder(boolean flag, byte[] bytes, int n, boolean flag2, ShadedBase64EncoderSynthetic synthetic) {
      this(flag, bytes, n, flag2);
   }

   public static char[] getToBase64() {
      return toBase64;
   }

   private final int outLength(int n) {
      int n2 = 0;
      int n3 = 0;
      byte b = 4;
      n3 = 0;
      if (this.doPadding) {
         ;
      }

      while (true) {
         while (b > 2) {
            if (b >= 4) {
               if (4 < b) {
                  n3 = 4 * (n / 3) + (n2 == 0 ? 0 : n2 + 1);
               }

               if (this.linemax > 0) {
                  n3 += (n3 - 1) / this.linemax * ((byte[])this.newline).length;
               }

               return n3;
            }

            n3 = 4 * ((n + 2) / 3);
         }

         n2 = n % 3;
      }
   }

   public OutputStream wrap(char ch, short s, OutputStream out, int n) {
      Objects.requireNonNull(out);
      return new ShadedBase64OutputStream(out, this.isURL ? toBase64URL : toBase64, this.newline, this.linemax, this.doPadding);
   }

   public byte[] encode(byte[] bytes) {
      byte[] bytes2 = null;
      int n = 0;
      int n2 = 0;
      byte b = 2;
      VendorBase64Encoder vendorBase64Encoder = this;
      n = vendorBase64Encoder.outLength(bytes.length);

      while (true) {
         while (2 < b) {
            if (b < 4) {
               return bytes2;
            }

            if (4 < b) {
               return Arrays.copyOf(bytes2, n2);
            }

            if (n2 != bytes2.length) {
               ;
            }
         }

         bytes2 = new byte[n];
         vendorBase64Encoder = this;
         n2 = vendorBase64Encoder.encode0(bytes, (byte)0, bytes.length, bytes2);
      }
   }

   public ByteBuffer encode2(ByteBuffer buf) {
      byte[] bytes = null;
      int n = 0;
      int n2 = 0;
      byte[] bytes2 = null;
      byte b = 7;

      outerLoop:
      while (true) {
         if (2 <= b) {
            if (2 >= b) {
               bytes = new byte[n];
               continue;
            }

            if (4 > b) {
               n2 = 0;
               continue;
            }

            if (b <= 4) {
               n = this.outLength(buf.remaining());
               continue;
            }

            n2 = this.encode0(buf.array(), buf.arrayOffset() + buf.position(), buf.arrayOffset() + buf.limit(), bytes);
         } else if (b <= 0) {
            bytes = Arrays.copyOf(bytes, n2);
         } else if (buf.hasArray()) {
            continue;
         }

         while (true) {
            while (b > 6) {
               if (b >= 9) {
                  if (9 >= b) {
                     bytes2 = new byte[buf.remaining()];
                  } else if (11 <= b) {
                     if (b <= 11) {
                        return ByteBuffer.wrap(bytes);
                     }

                     n2 = this.encode0(bytes2, (byte)0, bytes2.length, bytes);
                  } else if (n2 != bytes.length) {
                     continue outerLoop;
                  }
               } else if (7 < b) {
                  ((Buffer)buf).position(buf.limit());
               }
            }

            buf.get(bytes2);
         }
      }
   }

}
