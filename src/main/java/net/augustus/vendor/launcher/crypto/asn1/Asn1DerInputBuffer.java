package net.augustus.vendor.launcher.crypto.asn1;

import net.augustus.vendor.launcher.crypto.math.ShadedBigInteger;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;
import sun.security.util.BitArray;
import sun.util.calendar.CalendarSystem;
import sun.util.calendar.Gregorian;

public class Asn1DerInputBuffer extends ByteArrayInputStream implements Cloneable {
   public boolean allowBER;

   public BitArray getUnalignedBitString() throws IOException {
      if (this.pos >= this.count) {
         return null;
      } else {
         int n = this.available();
         int n2 = this.buf[this.pos] & 255;
         if (n2 > 7) {
            throw new IOException("Invalid value for unused bits: " + n2);
         } else {
            byte[] bytes = new byte[n - 1];
            int n3 = bytes.length == 0 ? 0 : bytes.length * 8 - n2;
            System.arraycopy(this.buf, this.pos + 1, bytes, 0, n - 1);
            BitArray bitArray = new BitArray(n3, bytes);
            this.pos = this.count;
            return bitArray;
         }
      }
   }

   public byte[] getBitString() throws IOException {
      int n = this.available();
      return this.getBitString(n);
   }

   @Override
   public boolean equals(Object obj) {
      boolean flag;
      if (obj instanceof Asn1DerInputBuffer) {
         Asn1DerInputBuffer asn1DerInputBuffer = (Asn1DerInputBuffer)obj;
         flag = this.equals2(asn1DerInputBuffer);
      } else {
         flag = false;
      }

      return flag;
   }

   private Date getTime(int n, boolean flag) throws IOException {
      Object obj = null;
      int n2;
      if (flag) {
         obj = "Generalized";
         n2 = 1000 * Character.digit((char)this.buf[this.pos++], 10);
         n2 += 100 * Character.digit((char)this.buf[this.pos++], 10);
         n2 += 10 * Character.digit((char)this.buf[this.pos++], 10);
         n2 += Character.digit((char)this.buf[this.pos++], 10);
         n -= 2;
      } else {
         obj = "UTC";
         n2 = 10 * Character.digit((char)this.buf[this.pos++], 10);
         n2 += Character.digit((char)this.buf[this.pos++], 10);
         if (n2 < 50) {
            n2 += 2000;
         } else {
            n2 += 1900;
         }
      }

      int n3 = 10 * Character.digit((char)this.buf[this.pos++], 10);
      n3 += Character.digit((char)this.buf[this.pos++], 10);
      int n4 = 10 * Character.digit((char)this.buf[this.pos++], 10);
      n4 += Character.digit((char)this.buf[this.pos++], 10);
      int n5 = 10 * Character.digit((char)this.buf[this.pos++], 10);
      n5 += Character.digit((char)this.buf[this.pos++], 10);
      int n6 = 10 * Character.digit((char)this.buf[this.pos++], 10);
      n6 += Character.digit((char)this.buf[this.pos++], 10);
      n -= 10;
      int n7 = 0;
      int n8;
      if (n > 2 && n < 12) {
         n8 = 10 * Character.digit((char)this.buf[this.pos++], 10);
         n8 += Character.digit((char)this.buf[this.pos++], 10);
         n -= 2;
         if (this.buf[this.pos] == 46 || this.buf[this.pos] == 44) {
            n--;
            this.pos++;
            int n9 = 0;

            for (int i = this.pos; this.buf[i] != 90 && this.buf[i] != 43 && this.buf[i] != 45; n9++) {
               i++;
            }

            switch (n9) {
               case 1:
                  n7 += 100 * Character.digit((char)this.buf[this.pos++], 10);
                  break;
               case 2:
                  n7 += 100 * Character.digit((char)this.buf[this.pos++], 10);
                  n7 += 10 * Character.digit((char)this.buf[this.pos++], 10);
                  break;
               case 3:
                  n7 += 100 * Character.digit((char)this.buf[this.pos++], 10);
                  n7 += 10 * Character.digit((char)this.buf[this.pos++], 10);
                  n7 += Character.digit((char)this.buf[this.pos++], 10);
                  break;
               default:
                  throw new IOException("Parse " + obj + " time, unsupported precision for seconds value");
            }

            n -= n9;
         }
      } else {
         n8 = 0;
      }

      if (n3 != 0 && n4 != 0 && n3 <= 12 && n4 <= 31 && n5 < 24 && n6 < 60 && n8 < 60) {
         Gregorian gregorianCalendar = CalendarSystem.getGregorianCalendar();
         sun.util.calendar.CalendarDate calendarDate = gregorianCalendar.newCalendarDate((TimeZone)null);
         calendarDate.setDate(n2, n3, n4);
         calendarDate.setTimeOfDay(n5, n6, n8, n7);
         long time = gregorianCalendar.getTime(calendarDate);
         if (n != 1 && n != 5) {
            throw new IOException("Parse " + obj + " time, invalid offset");
         } else {
            switch (this.buf[this.pos++]) {
               case 43:
                  int n10 = 10 * Character.digit((char)this.buf[this.pos++], 10);
                  n10 += Character.digit((char)this.buf[this.pos++], 10);
                  int n11 = 10 * Character.digit((char)this.buf[this.pos++], 10);
                  n11 += Character.digit((char)this.buf[this.pos++], 10);
                  if (n10 >= 24 || n11 >= 60) {
                     throw new IOException("Parse " + obj + " time, +hhmm");
                  }

                  time -= (long)((n10 * 60 + n11) * 60 * 1000);
                  break;
               case 45:
                  int n12 = 10 * Character.digit((char)this.buf[this.pos++], 10);
                  n12 += Character.digit((char)this.buf[this.pos++], 10);
                  int n13 = 10 * Character.digit((char)this.buf[this.pos++], 10);
                  n13 += Character.digit((char)this.buf[this.pos++], 10);
                  if (n12 >= 24 || n13 >= 60) {
                     throw new IOException("Parse " + obj + " time, -hhmm");
                  }

                  time += (long)((n12 * 60 + n13) * 60 * 1000);
               case 90:
                  break;
               default:
                  throw new IOException("Parse " + obj + " time, garbage offset");
            }

            return new Date(time);
         }
      } else {
         throw new IOException("Parse " + obj + " time, invalid format");
      }
   }

   public boolean equals2(Asn1DerInputBuffer asn1DerInputBuffer) {
      if (this == asn1DerInputBuffer) {
         return true;
      } else {
         int n = this.available();
         if (asn1DerInputBuffer.available() != n) {
            return false;
         } else {
            for (int i = 0; i < n; i++) {
               if (this.buf[this.pos + i] != asn1DerInputBuffer.buf[asn1DerInputBuffer.pos + i]) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public void truncate(int n) throws IOException {
      if (n > this.available()) {
         throw new IOException("insufficient data");
      } else {
         this.count = this.pos + n;
      }
   }

   public int getInteger(int n) throws IOException {
      boolean flag = false;
      ShadedBigInteger bigInteger = this.getBigInteger(n, flag);
      ShadedBigInteger shadedBigInteger = ShadedBigInteger.valueOf(-2147483648L);
      ShadedBigInteger shadedBigInteger2 = shadedBigInteger;
      if (bigInteger.compareTo2(shadedBigInteger2) < 0) {
         throw new IOException("Integer below minimum valid value");
      } else {
         shadedBigInteger = ShadedBigInteger.valueOf(2147483647L);
         ShadedBigInteger shadedBigInteger3 = shadedBigInteger;
         if (bigInteger.compareTo2(shadedBigInteger3) > 0) {
            throw new IOException("Integer exceeds maximum valid value");
         } else {
            return bigInteger.intValue();
         }
      }
   }

   public int peek() throws IOException {
      if (this.pos >= this.count) {
         throw new IOException("out of data");
      } else {
         return this.buf[this.pos];
      }
   }

   public ShadedBigInteger getBigInteger(int n, boolean flag) throws IOException {
      if (n > this.available()) {
         throw new IOException("short read of integer");
      } else if (n == 0) {
         throw new IOException("Invalid encoding: zero length Int value");
      } else {
         byte[] bytes = new byte[n];
         System.arraycopy(this.buf, this.pos, bytes, 0, n);
         this.skip((long)n);
         if (!this.allowBER && n >= 2 && bytes[0] == 0 && bytes[1] >= 0) {
            throw new IOException("Invalid encoding: redundant leading 0s");
         } else {
            return flag ? new ShadedBigInteger(1, bytes) : new ShadedBigInteger(bytes);
         }
      }
   }

   public Asn1DerInputBuffer(byte[] bytes, int n, int n2, boolean flag) {
      super(bytes, n, n2);
      boolean flag2 = true;
      this.allowBER = flag2;
      this.allowBER = flag;
   }

   public Date getGeneralizedTime(int n) throws IOException {
      if (n > this.available()) {
         throw new IOException("short read of DER Generalized Time");
      } else if (n >= 13 && n <= 23) {
         boolean flag = true;
         return this.getTime(n, flag);
      } else {
         throw new IOException("DER Generalized Time length error");
      }
   }

   public Asn1DerInputBuffer(byte[] bytes) {
       this(bytes, true);
   }

   public Asn1DerInputBuffer dup() {
      try {
         Asn1DerInputBuffer asn1DerInputBuffer = (Asn1DerInputBuffer)this.clone();
         asn1DerInputBuffer.mark(2147483647);
         return asn1DerInputBuffer;
      } catch (CloneNotSupportedException e) {
         throw new IllegalArgumentException(e.toString());
      }
   }

   public Date getUTCTime(int n) throws IOException {
      if (n > this.available()) {
         throw new IOException("short read of DER UTC Time");
      } else if (n >= 11 && n <= 17) {
         boolean flag = false;
         return this.getTime(n, flag);
      } else {
         throw new IOException("DER UTC Time length error");
      }
   }

   @Override
   public int hashCode() {
      int n = 0;
      int n2 = this.available();
      int pos2 = this.pos;

      for (int i = 0; i < n2; i++) {
         n += this.buf[pos2 + i] * i;
      }

      return n;
   }

   public byte[] toByteArray() {
      int n = this.available();
      if (n <= 0) {
         return null;
      } else {
         byte[] bytes = new byte[n];
         System.arraycopy(this.buf, this.pos, bytes, 0, n);
         return bytes;
      }
   }

   public byte[] getBitString(int n) throws IOException {
      if (n > this.available()) {
         throw new IOException("short read of bit string");
      } else if (n == 0) {
         throw new IOException("Invalid encoding: zero length bit string");
      } else {
         byte b = this.buf[this.pos];
         if (b >= 0 && b <= 7) {
            byte[] bytes = new byte[n - 1];
            System.arraycopy(this.buf, this.pos + 1, bytes, 0, n - 1);
            if (b != 0) {
               bytes[n - 2] = (byte)(bytes[n - 2] & 255 << b);
            }

            this.skip((long)n);
            return bytes;
         } else {
            throw new IOException("Invalid number of padding bits");
         }
      }
   }

   public Asn1DerInputBuffer(byte[] bytes, boolean flag) {
      super(bytes);
      boolean flag2 = true;
      this.allowBER = flag2;
      this.allowBER = flag;
   }

}
