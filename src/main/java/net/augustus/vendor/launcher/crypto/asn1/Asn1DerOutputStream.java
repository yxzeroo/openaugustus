package net.augustus.vendor.launcher.crypto.asn1;

import net.augustus.vendor.launcher.crypto.math.ShadedBigInteger;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import sun.security.util.BitArray;
import sun.security.util.ByteArrayLexOrder;
import sun.security.util.ByteArrayTagOrder;
import sun.security.util.DerEncoder;

public class Asn1DerOutputStream extends ByteArrayOutputStream implements DerEncoder {
   private static ByteArrayLexOrder lexOrder = new ByteArrayLexOrder();
   private static ByteArrayTagOrder tagOrder = new ByteArrayTagOrder();

   public void putInteger2(Integer num) throws IOException {
      this.putInteger2(num);
   }

   public void writeImplicit(byte b, Asn1DerOutputStream asn1DerOutputStream) throws IOException {
      this.write(b);
      this.write(asn1DerOutputStream.buf, 1, asn1DerOutputStream.count - 1);
   }


   public void putGeneralString(String str) throws IOException {
      String str2 = "ASCII";
      byte b = 27;
      this.writeString(str, b, str2);
   }

   public void putLength(int n) throws IOException {
      if (n < 128) {
         this.write((byte)n);
      } else if (n < 256) {
         this.write(-127);
         this.write((byte)n);
      } else if (n < 65536) {
         this.write(-126);
         this.write((byte)(n >> 8));
         this.write((byte)n);
      } else if (n < 16777216) {
         this.write(-125);
         this.write((byte)(n >> 16));
         this.write((byte)(n >> 8));
         this.write((byte)n);
      } else {
         this.write(-124);
         this.write((byte)(n >> 24));
         this.write((byte)(n >> 16));
         this.write((byte)(n >> 8));
         this.write((byte)n);
      }
   }

   public void write2(byte b, Asn1DerOutputStream asn1DerOutputStream) throws IOException {
      this.write(b);
      int count2 = asn1DerOutputStream.count;
      this.putLength(count2);
      this.write(asn1DerOutputStream.buf, 0, asn1DerOutputStream.count);
   }

   private void writeString(String str, byte b, String str2) throws IOException {
      byte[] bytes = str.getBytes(str2);
      this.write(b);
      int length = bytes.length;
      this.putLength(length);
      this.write(bytes);
   }

   private void putOrderedSet(byte b, DerEncoder[] derEncoders, Comparator comparator) throws IOException {
      Asn1DerOutputStream[] asn1DerOutputStreams = new Asn1DerOutputStream[derEncoders.length];

      for (int i = 0; i < derEncoders.length; i++) {
         asn1DerOutputStreams[i] = new Asn1DerOutputStream();
         derEncoders[i].derEncode(asn1DerOutputStreams[i]);
      }

      byte[][] bytesArr = new byte[asn1DerOutputStreams.length][];

      for (int j = 0; j < asn1DerOutputStreams.length; j++) {
         bytesArr[j] = asn1DerOutputStreams[j].toByteArray();
      }

      Arrays.sort(bytesArr, comparator);
      Asn1DerOutputStream asn1DerOutputStream = new Asn1DerOutputStream();

      for (int k = 0; k < asn1DerOutputStreams.length; k++) {
         asn1DerOutputStream.write(bytesArr[k]);
      }

      this.write2(b, asn1DerOutputStream);
   }

   public void putSequence(Asn1DerValue[] asn1DerValues) throws IOException {
      Asn1DerOutputStream asn1DerOutputStream = new Asn1DerOutputStream();

      for (int i = 0; i < asn1DerValues.length; i++) {
         asn1DerValues[i].encode(asn1DerOutputStream);
      }

      byte b = 48;
      this.write2(b, asn1DerOutputStream);
   }

   public Asn1DerOutputStream() {
   }

   public Asn1DerOutputStream(int n) {
      super(n);
   }

   public void putT61String(String str) throws IOException {
      String str2 = "ISO-8859-1";
      byte b = 20;
      this.writeString(str, b, str2);
   }

   public void putDerValue(Asn1DerValue asn1DerValue) throws IOException {
      asn1DerValue.encode(this);
   }

   public void putBMPString(String str) throws IOException {
      String str2 = "UnicodeBigUnmarked";
      byte b = 30;
      this.writeString(str, b, str2);
   }

   private void putIntegerContents(int n) throws IOException {
      byte[] bytes = new byte[4];
      int n2 = 0;
      bytes[3] = (byte)(n & 0xFF);
      bytes[2] = (byte)((n & 0xFF00) >>> 8);
      bytes[1] = (byte)((n & 0xFF0000) >>> 16);
      bytes[0] = (byte)((n & 0xFF000000) >>> 24);
      if (bytes[0] == -1) {
         for (int i = 0; i < 3 && bytes[i] == -1 && (bytes[i + 1] & 128) == 128; i++) {
            n2++;
         }
      } else if (bytes[0] == 0) {
         for (int j = 0; j < 3 && bytes[j] == 0 && (bytes[j + 1] & 128) == 0; j++) {
            n2++;
         }
      }

      int n3 = 4 - n2;
      this.putLength(n3);

      for (int k = n2; k < 4; k++) {
         this.write(bytes[k]);
      }
   }

   public void putOctetString(byte[] bytes) throws IOException {
      byte[] bytes2 = bytes;
      byte b = 4;
      this.write(b, bytes2);
   }

   public void putBitString(byte[] bytes) throws IOException {
      this.write(3);
      int n = bytes.length + 1;
      this.putLength(n);
      this.write(0);
      this.write(bytes);
   }

   public void putOrderedSetOf(byte b, DerEncoder[] derEncoders) throws IOException {
      ByteArrayLexOrder byteArrayLexOrder = lexOrder;
      this.putOrderedSet(b, derEncoders, byteArrayLexOrder);
   }

   public void write(byte b, byte[] bytes) throws IOException {
      this.write(b);
      int length = bytes.length;
      this.putLength(length);
      this.write(bytes, 0, bytes.length);
   }

   public void putSet(Asn1DerValue[] asn1DerValues) throws IOException {
      Asn1DerOutputStream asn1DerOutputStream = new Asn1DerOutputStream();

      for (int i = 0; i < asn1DerValues.length; i++) {
         asn1DerValues[i].encode(asn1DerOutputStream);
      }

      byte b = 49;
      this.write2(b, asn1DerOutputStream);
   }

   public void putOrderedSet(byte b, DerEncoder[] derEncoders) throws IOException {
      ByteArrayTagOrder byteArrayTagOrder = tagOrder;
      this.putOrderedSet(b, derEncoders, byteArrayTagOrder);
   }

   public void putUTCTime(Date date) throws IOException {
      byte b = 23;
      this.putTime(date, b);
   }

   public void putTag(byte b, char ch, boolean flag, byte b2, long l) {
      byte b3 = (byte)(b | b2);
      if (flag) {
         b3 = (byte)(b3 | 0);
      }

      this.write(b3);
   }

   public void putInteger(int n) throws IOException {
      this.write(2);
      this.putIntegerContents(n);
   }

   public void derEncode(OutputStream out) throws IOException {
      out.write(this.toByteArray());
   }

   public void putOID(Asn1ObjectIdentifier asn1ObjectIdentifier) throws IOException {
      asn1ObjectIdentifier.encode(this);
   }

   public void putTruncatedUnalignedBitString(BitArray bitArray) throws IOException {
      BitArray bitArray2 = bitArray.truncate();
      BitArray bitArray3 = bitArray2;
      this.putUnalignedBitString(bitArray3);
   }

   private void putTime(Date date, byte b) throws IOException {
      TimeZone timeZone = TimeZone.getTimeZone("GMT");
      Object obj = null;
      if (b == 23) {
         obj = "yyMMddHHmmss'Z'";
      } else {
         b = 24;
         obj = "yyyyMMddHHmmss'Z'";
      }

      SimpleDateFormat simpleDateFormat = new SimpleDateFormat((String)obj, Locale.US);
      simpleDateFormat.setTimeZone(timeZone);
      byte[] bytes = simpleDateFormat.format(date).getBytes("ISO-8859-1");
      this.write(b);
      int length = bytes.length;
      this.putLength(length);
      this.write(bytes);
   }

   public void putPrintableString(String str) throws IOException {
      String str2 = "ASCII";
      byte b = 19;
      this.writeString(str, b, str2);
   }

   public void putEnumerated(int n) throws IOException {
      this.write(10);
      this.putIntegerContents(n);
   }

   public void putIA5String(String str) throws IOException {
      String str2 = "ASCII";
      byte b = 22;
      this.writeString(str, b, str2);
   }

   public void putUnalignedBitString(BitArray bitArray) throws IOException {
      byte[] bytes = bitArray.toByteArray();
      this.write(3);
      int n = bytes.length + 1;
      this.putLength(n);
      this.write(bytes.length * 8 - bitArray.length());
      this.write(bytes);
   }

   public void putGeneralizedTime(Date date) throws IOException {
      byte b = 24;
      this.putTime(date, b);
   }

   public void putBoolean(boolean flag) throws IOException {
      this.write(1);
      byte b = 1;
      this.putLength(b);
      if (flag) {
         this.write(255);
      } else {
         this.write(0);
      }
   }

   public void putUTF8String(String str) throws IOException {
      String str2 = "UTF8";
      byte b = 12;
      this.writeString(str, b, str2);
   }

   public void putInteger3(ShadedBigInteger shadedBigInteger) throws IOException {
      this.write(2);
      byte[] bytes = shadedBigInteger.toByteArray();
      int length = bytes.length;
      this.putLength(length);
      this.write(bytes, 0, bytes.length);
   }

   public void putNull() throws IOException {
      this.write(5);
      byte b = 0;
      this.putLength(b);
   }

}
