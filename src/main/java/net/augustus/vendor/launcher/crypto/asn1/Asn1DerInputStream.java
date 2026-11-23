package net.augustus.vendor.launcher.crypto.asn1;

import net.augustus.vendor.launcher.crypto.math.ShadedBigInteger;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Vector;
import sun.security.util.BitArray;

public class Asn1DerInputStream {
   public Asn1DerInputBuffer buffer;
   public byte tag;

   public String getGeneralString() throws IOException {
      String str = "ASCII";
      return this.readString((byte)27, "General", str);
   }

   public void mark(int n) {
      this.buffer.mark(n);
   }

   public void getNull(long l) throws IOException {
      if (this.buffer.read() != 5 || this.buffer.read() != 0) {
         throw new IOException("getNull, bad data");
      }
   }

   public Asn1DerInputStream(Asn1DerInputBuffer asn1DerInputBuffer) {
      this.buffer = asn1DerInputBuffer;
      this.buffer.mark(2147483647);
   }

   public Asn1DerValue[] getSequence(int n) throws IOException {
      this.tag = (byte)this.buffer.read();
      if (this.tag != 48) {
         throw new IOException("Sequence tag error");
      } else {
         return this.readVector(n);
      }
   }

   public void reset() {
      this.buffer.reset();
   }

   public String getUTF8String() throws IOException {
      String str = "UTF8";
      return this.readString((byte)12, "UTF-8", str);
   }

   public Date getUTCTime() throws IOException {
      if (this.buffer.read() != 23) {
         throw new IOException("DER input, UTCtime tag invalid ");
      } else {
         return this.buffer.getUTCTime(getLength(this.buffer));
      }
   }

   public Date getGeneralizedTime() throws IOException {
      if (this.buffer.read() != 24) {
         throw new IOException("DER input, GeneralizedTime tag invalid ");
      } else {
         return this.buffer.getGeneralizedTime(getLength(this.buffer));
      }
   }

   public int available() {
      return this.buffer.available();
   }

   public void getBytes(byte[] bytes) throws IOException {
      if (bytes.length != 0 && this.buffer.read(bytes) != bytes.length) {
         throw new IOException("Short read of DER octet string");
      }
   }

   public Asn1DerInputStream(byte[] bytes, int n, int n2, boolean flag) throws IOException {
       super();
      this.init(bytes, n, n2, flag);
   }

   public Asn1DerValue[] getSet(int n, boolean flag) throws IOException {
      this.tag = (byte)this.buffer.read();
      if (!flag && this.tag != 49) {
         throw new IOException("Set tag error");
      } else {
         return this.readVector(n);
      }
   }

   private String readString(byte b, String str, String str2) throws IOException {
      if (this.buffer.read() != b) {
         throw new IOException("DER input not a " + str + " string");
      } else {
         int length = getLength(this.buffer);
         byte[] bytes = new byte[length];
         if (length != 0 && this.buffer.read(bytes) != length) {
            throw new IOException("Short read of DER " + str + " string");
         } else {
            return new String(bytes, str2);
         }
      }
   }

   public Asn1DerValue getDerValue() throws IOException {
      return new Asn1DerValue(this.buffer);
   }

   public String getT61String() throws IOException {
      String str = "ISO-8859-1";
      return this.readString((byte)20, "T61", str);
   }

   public int getInteger() throws IOException {
      if (this.buffer.read() != 2) {
         throw new IOException("DER input, Integer tag error");
      } else {
         return this.buffer.getInteger(getLength(this.buffer));
      }
   }

   public static int getLength(InputStream in) throws IOException {
      return getLength(in.read(), in);
   }

   public Asn1DerInputStream subStream(int n, boolean flag) throws IOException {
      Asn1DerInputBuffer asn1DerInputBuffer = this.buffer.dup();
      asn1DerInputBuffer.truncate(n);
      if (flag) {
         this.buffer.skip((long)n);
      }

      return new Asn1DerInputStream(asn1DerInputBuffer);
   }

   public byte[] getBitString() throws IOException {
      if (this.buffer.read() != 3) {
         throw new IOException("DER input not an bit string");
      } else {
         return this.buffer.getBitString(getLength(this.buffer));
      }
   }

   public ShadedBigInteger getPositiveBigInteger() throws IOException {
      if (this.buffer.read() != 2) {
         throw new IOException("DER input, Integer tag error");
      } else {
         return this.buffer.getBigInteger(getLength(this.buffer), true);
      }
   }

   public int peekByte() throws IOException {
      return this.buffer.peek();
   }

   public Asn1DerValue[] readVector(int n) throws IOException {
      byte b = (byte)this.buffer.read();
      int length = getLength(b, this.buffer);
      if (length == -1) {
         int n2 = this.buffer.available();
         byte b2 = 2;
         byte[] bytes = new byte[n2 + b2];
         bytes[0] = this.tag;
         bytes[1] = b;
         DataInputStream in = new DataInputStream(this.buffer);
         in.readFully(bytes, b2, n2);
         in.close();
         Asn1DerIndefLenConverter asn1DerIndefLenConverter = new Asn1DerIndefLenConverter();
         this.buffer = new Asn1DerInputBuffer(asn1DerIndefLenConverter.convert(bytes), this.buffer.allowBER);
         if (this.tag != this.buffer.read()) {
            throw new IOException("Indefinite length encoding not supported");
         }

         length = getLength(this.buffer);
      }

      if (length == 0) {
         return new Asn1DerValue[0];
      } else {
         Asn1DerInputStream asn1DerInputStream;
         if (this.buffer.available() == length) {
            asn1DerInputStream = this;
         } else {
            asn1DerInputStream = this.subStream(length, true);
         }

         Vector vector = new Vector(n);

         do {
            vector.addElement(new Asn1DerValue(asn1DerInputStream.buffer, this.buffer.allowBER));
         } while (asn1DerInputStream.available() > 0);

         if (asn1DerInputStream.available() != 0) {
            throw new IOException("Extra data at end of vector");
         } else {
            int size = vector.size();
            Asn1DerValue[] asn1DerValues = new Asn1DerValue[size];

            for (int i = 0; i < size; i++) {
               asn1DerValues[i] = (Asn1DerValue)vector.elementAt(i);
            }

            return asn1DerValues;
         }
      }
   }

   public int getEnumerated() throws IOException {
      if (this.buffer.read() != 10) {
         throw new IOException("DER input, Enumerated tag error");
      } else {
         return this.buffer.getInteger(getLength(this.buffer));
      }
   }

   public Asn1DerInputStream(byte[] bytes) throws IOException {
       super();
      this.init(bytes, (byte)0, bytes.length, true);
   }

   public int getLength() throws IOException {
      return getLength(this.buffer);
   }

   public byte[] getOctetString() throws IOException {
      if (this.buffer.read() != 4) {
         throw new IOException("DER input not an octet string");
      } else {
         int length = getLength(this.buffer);
         byte[] bytes = new byte[length];
         if (length != 0 && this.buffer.read(bytes) != length) {
            throw new IOException("Short read of DER octet string");
         } else {
            return bytes;
         }
      }
   }

   public int getByte() throws IOException {
      return 0xFF & this.buffer.read();
   }

   public byte[] toByteArray() {
      return this.buffer.toByteArray();
   }

   public ShadedBigInteger getBigInteger() throws IOException {
      if (this.buffer.read() != 2) {
         throw new IOException("DER input, Integer tag error");
      } else {
         return this.buffer.getBigInteger(getLength(this.buffer), false);
      }
   }

   public String getIA5String() throws IOException {
      String str = "ASCII";
      return this.readString((byte)22, "IA5", str);
   }

   public Asn1DerValue[] getSet(int n) throws IOException {
      this.tag = (byte)this.buffer.read();
      if (this.tag != 49) {
         throw new IOException("Set tag error");
      } else {
         return this.readVector(n);
      }
   }

   public Asn1DerInputStream(byte[] bytes, int n, int n2) throws IOException {
       super();
      this.init(bytes, n, n2, true);
   }

   public BitArray getUnalignedBitString() throws IOException {
      if (this.buffer.read() != 3) {
         throw new IOException("DER input not a bit string");
      } else {
         int n = getLength(this.buffer) - 1;
         int n2 = this.buffer.read();
         if (n2 < 0) {
            throw new IOException("Unused bits of bit string invalid");
         } else {
            int n3 = n * 8 - n2;
            if (n3 < 0) {
               throw new IOException("Valid bits of bit string invalid");
            } else {
               byte[] bytes = new byte[n];
               if (n != 0 && this.buffer.read(bytes) != n) {
                  throw new IOException("Short read of DER bit string");
               } else {
                  return new BitArray(n3, bytes);
               }
            }
         }
      }
   }

   private void init(byte[] bytes, int n, int n2, boolean flag) throws IOException {
      if (n + 2 <= bytes.length && n + n2 <= bytes.length) {
         if (Asn1DerIndefLenConverter.isIndefinite(bytes[n + 1])) {
            if (!flag) {
               throw new IOException("Indefinite length BER encoding found");
            }

            byte[] bytes2 = new byte[n2];
            System.arraycopy(bytes, n, bytes2, 0, n2);
            Asn1DerIndefLenConverter asn1DerIndefLenConverter = new Asn1DerIndefLenConverter();
            this.buffer = new Asn1DerInputBuffer(asn1DerIndefLenConverter.convert(bytes2), flag);
         } else {
            this.buffer = new Asn1DerInputBuffer(bytes, n, n2, flag);
         }

         this.buffer.mark(2147483647);
      } else {
         throw new IOException("Encoding bytes too short");
      }
   }

   public Asn1ObjectIdentifier getOID() throws IOException {
      return new Asn1ObjectIdentifier(this);
   }

   public static int getLength(int n, InputStream in) throws IOException {
      if (n == -1) {
         throw new IOException("Short read of DER length");
      } else {
         String str = "DerInputStream.getLength(): ";
         int n2;
         if ((n & 128) == 0) {
            n2 = n;
         } else {
            int n3 = n & 127;
            if (n3 == 0) {
               return -1;
            }

            if (n3 < 0 || n3 > 4) {
               throw new IOException(str + "lengthTag=" + n3 + ", " + (n3 < 0 ? "incorrect DER encoding." : "too big."));
            }

            n2 = 0xFF & in.read();
            n3--;
            if (n2 == 0) {
               throw new IOException(str + "Redundant length bytes found");
            }

            while (n3-- > 0) {
               n2 <<= 8;
               n2 += 0xFF & in.read();
            }

            if (n2 < 0) {
               throw new IOException(str + "Invalid length bytes");
            }

            if (n2 <= 127) {
               throw new IOException(str + "Should use short form for length");
            }
         }

         return n2;
      }
   }

   public String getPrintableString() throws IOException {
      String str = "ASCII";
      return this.readString((byte)19, "Printable", str);
   }

   public String getBMPString() throws IOException {
      String str = "UnicodeBigUnmarked";
      return this.readString((byte)30, "BMP", str);
   }


}
