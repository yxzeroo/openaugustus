package net.augustus.vendor.launcher.crypto.asn1;

import net.augustus.vendor.launcher.crypto.math.ShadedBigInteger;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import sun.misc.IOUtils;
import sun.security.util.BitArray;

public class Asn1DerValue {
   public static final byte TAG_UNIVERSAL = 0;
   public static final byte TAG_APPLICATION = 0;
   public static final byte TAG_CONTEXT = 0;
   public static final byte TAG_PRIVATE = 0;
   public static final byte tag_Integer = 0;
   public static final byte tag_BitString = 0;
   public static final byte tag_OctetString = 0;
   public static final byte tag_Null = 0;
   public static final byte tag_ObjectId = 0;
   public static final byte tag_Enumerated = 0;
   public static final byte tag_UTF8String = 0;
   public static final byte tag_PrintableString = 0;
   public Asn1DerInputBuffer buffer;
   public static final byte tag_T61String = 0;
   public static final byte tag_IA5String = 0;
   public static final byte tag_UtcTime = 0;
   public static final byte tag_GeneralizedTime = 0;
   public static final byte tag_GeneralString = 0;
   public final Asn1DerInputStream data;
   public static final byte tag_Boolean = 1;
   public static final byte tag_UniversalString = 0;
   public byte tag;
   public static final byte tag_BMPString = 0;
   public static final byte tag_Sequence = 0;
   public static final byte tag_SequenceOf = 0;
   public static final byte tag_Set = 0;
   private int length;
   public static final byte tag_SetOf = 0;

   private static boolean doEquals(Asn1DerValue asn1DerValue, Asn1DerValue asn1DerValue2) {
      synchronized (asn1DerValue.data) {
         boolean flag;
         synchronized (asn1DerValue2.data) {
            asn1DerValue.data.reset();
            asn1DerValue2.data.reset();
            Asn1DerInputBuffer buffer2 = asn1DerValue2.buffer;
            flag = asn1DerValue.buffer.equals2(buffer2);
         }

         return flag;
      }
   }

   public final Asn1DerInputStream getData(long l) {
      return this.data;
   }

   public String getIA5String() throws IOException {
      if (this.tag != 22) {
         throw new IOException("DerValue.getIA5String, not IA5 " + this.tag);
      } else {
         return new String(this.getDataBytes(), "ASCII");
      }
   }

   public Asn1DerValue(byte b, byte[] bytes) {
       this(b, bytes, true);
   }

   public boolean equals2(Asn1DerValue asn1DerValue) {
      if (this == asn1DerValue) {
         return true;
      } else if (this.tag != asn1DerValue.tag) {
         return false;
      } else if (this.data == asn1DerValue.data) {
         return true;
      } else {
         return System.identityHashCode(this.data) > System.identityHashCode(asn1DerValue.data) ? doEquals(this, asn1DerValue) : doEquals(asn1DerValue, this);
      }
   }

   public String getT61String() throws IOException {
      if (this.tag != 20) {
         throw new IOException("DerValue.getT61String, not T61 " + this.tag);
      } else {
         return new String(this.getDataBytes(), "ISO-8859-1");
      }
   }

   public ShadedBigInteger getPositiveBigInteger() throws IOException {
      if (this.tag != 2) {
         throw new IOException("DerValue.getBigInteger, not an int " + this.tag);
      } else {
         Asn1DerInputBuffer buffer2 = this.buffer;
         int n = this.data.available();
         boolean flag = true;
         int n2 = n;
         return buffer2.getBigInteger(n2, flag);
      }
   }

   public ShadedBigInteger getBigInteger() throws IOException {
      if (this.tag != 2) {
         throw new IOException("DerValue.getBigInteger, not an int " + this.tag);
      } else {
         Asn1DerInputBuffer buffer2 = this.buffer;
         int n = this.data.available();
         boolean flag = false;
         int n2 = n;
         return buffer2.getBigInteger(n2, flag);
      }
   }

   public boolean isConstructed() {
      return (this.tag & 32) == 32;
   }

   @Override
   public int hashCode() {
      return this.toString().hashCode();
   }

   @Override
   public boolean equals(Object obj) {
      boolean flag;
      if (obj instanceof Asn1DerValue) {
         Asn1DerValue asn1DerValue = (Asn1DerValue)obj;
         flag = this.equals2(asn1DerValue);
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean isApplication(long l) {
      return (this.tag & 192) == 64;
   }

   public Date getUTCTime() throws IOException {
      if (this.tag != 23) {
         throw new IOException("DerValue.getUTCTime, not a UtcTime: " + this.tag);
      } else {
         Asn1DerInputBuffer buffer2 = this.buffer;
         int n = this.data.available();
         return buffer2.getUTCTime(n);
      }
   }

   public Asn1DerValue(String str) throws IOException {
       super();
      boolean flag = true;

      for (int i = 0; i < str.length(); i++) {
         if (!isPrintableStringChar(str.charAt(i))) {
            flag = false;
            break;
         }
      }

      byte b = (byte)(flag ? 19 : 12);
      this.data = this.init(b, str);
   }

   public boolean isContextSpecific(byte b) {
      return !this.isContextSpecific() ? false : (this.tag & 31) == b;
   }

   public BitArray getUnalignedBitString() throws IOException {
      if (this.tag != 3) {
         throw new IOException("DerValue.getBitString, not a bit string " + this.tag);
      } else {
         return this.buffer.getUnalignedBitString();
      }
   }

   public Asn1DerValue(byte[] bytes, int n, int n2, boolean flag) throws IOException {
       super();
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes, n, n2);
      boolean flag2 = true;
      this.data = this.init(flag2, byteArrayInputStream, flag);
   }

   public Asn1DerValue(InputStream in) throws IOException {
       this(in, true);
   }

   private Asn1DerInputStream init(byte b, String str) throws IOException {
      Object obj = null;
      this.tag = b;
      switch (b) {
         case 12:
            obj = "UTF8";
            break;
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 21:
         case 23:
         case 24:
         case 25:
         case 26:
         case 28:
         case 29:
         default:
            throw new IllegalArgumentException("Unsupported DER string type");
         case 19:
         case 22:
         case 27:
            obj = "ASCII";
            break;
         case 20:
            obj = "ISO-8859-1";
            break;
         case 30:
            obj = "UnicodeBigUnmarked";
      }

      byte[] bytes = str.getBytes((String)obj);
      int length2 = bytes.length;
      this.length = length2;
      Asn1DerInputBuffer asn1DerInputBuffer = new Asn1DerInputBuffer(bytes, true);
      this.buffer = asn1DerInputBuffer;
      Asn1DerInputStream asn1DerInputStream = new Asn1DerInputStream(this.buffer);
      int n = 2147483647;
      asn1DerInputStream.mark(n);
      return asn1DerInputStream;
   }

   public Asn1DerValue(byte[] bytes, boolean flag) throws IOException {
       super();
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
      boolean flag2 = true;
      this.data = this.init(flag2, byteArrayInputStream, flag);
   }

   public Asn1DerValue(byte b, String str) throws IOException {
       super();
      this.data = this.init(b, str);
   }

   public int getInteger() throws IOException {
      if (this.tag != 2) {
         throw new IOException("DerValue.getInteger, not an int " + this.tag);
      } else {
         Asn1DerInputBuffer buffer2 = this.buffer;
         int n = this.data.available();
         return buffer2.getInteger(n);
      }
   }

   public byte[] getBitString() throws IOException {
      if (this.tag != 3) {
         throw new IOException("DerValue.getBitString, not a bit string " + this.tag);
      } else {
         return this.buffer.getBitString();
      }
   }

   @Override
   public String toString() {

      try {
         String asString = this.getAsString();
         if (asString != null) {
            return "\"" + asString + "\"";
         } else if (this.tag == 5) {
            return "[DerValue, null]";
         } else {
            return this.tag == 6 ? "OID." + this.getOID() : "[DerValue, tag = " + this.tag + ", length = " + this.length + "]";
         }
      } catch (IOException e) {
         throw new IllegalArgumentException("misformatted DER value");
      }
   }

   public int getEnumerated() throws IOException {
      if (this.tag != 10) {
         throw new IOException("DerValue.getEnumerated, incorrect tag: " + this.tag);
      } else {
         Asn1DerInputBuffer buffer2 = this.buffer;
         int n = this.data.available();
         return buffer2.getInteger(n);
      }
   }

   public boolean getBoolean(short s, short s2, int n) throws IOException {
      if (this.tag != 1) {
         throw new IOException("DerValue.getBoolean, not a BOOLEAN " + this.tag);
      } else if (this.length != 1) {
         throw new IOException("DerValue.getBoolean, invalid length " + this.length);
      } else {
         return this.buffer.read() != 0;
      }
   }

   public static byte createTag(long l, byte b, boolean flag, byte b2) {
      byte b3 = (byte)(b | b2);
      if (flag) {
         b3 = (byte)(b3 | 0);
      }

      return b3;
   }

   public Date getGeneralizedTime() throws IOException {
      if (this.tag != 24) {
         throw new IOException("DerValue.getGeneralizedTime, not a GeneralizedTime: " + this.tag);
      } else {
         Asn1DerInputBuffer buffer2 = this.buffer;
         int n = this.data.available();
         return buffer2.getGeneralizedTime(n);
      }
   }

   public Asn1DerValue(Asn1DerInputBuffer asn1DerInputBuffer) throws IOException {
       super();
      byte b = (byte)asn1DerInputBuffer.read();
      this.tag = b;
      byte b2 = (byte)asn1DerInputBuffer.read();
      int length2 = Asn1DerInputStream.getLength(b2, asn1DerInputBuffer);
      this.length = length2;
      if (this.length == -1) {
         Asn1DerInputBuffer asn1DerInputBuffer2 = asn1DerInputBuffer.dup();
         int n = asn1DerInputBuffer2.available();
         byte b3 = 2;
         byte[] bytes = new byte[n + b3];
         bytes[0] = this.tag;
         bytes[1] = b2;
         DataInputStream in = new DataInputStream(asn1DerInputBuffer2);
         in.readFully(bytes, b3, n);
         in.close();
         Asn1DerIndefLenConverter asn1DerIndefLenConverter = new Asn1DerIndefLenConverter();
         asn1DerInputBuffer2 = new Asn1DerInputBuffer(asn1DerIndefLenConverter.convert(bytes), asn1DerInputBuffer.allowBER);
         if (this.tag != asn1DerInputBuffer2.read()) {
            throw new IOException("Indefinite length encoding not supported");
         }

         int length3 = Asn1DerInputStream.getLength(asn1DerInputBuffer2);
         this.length = length3;
         Asn1DerInputBuffer asn1DerInputBuffer3 = asn1DerInputBuffer2.dup();
         this.buffer = asn1DerInputBuffer3;
         int length4 = this.length;
         this.buffer.truncate(length4);
         this.data = new Asn1DerInputStream(this.buffer);
         asn1DerInputBuffer.skip((long)(this.length + b3));
      } else {
         Asn1DerInputBuffer asn1DerInputBuffer4 = asn1DerInputBuffer.dup();
         this.buffer = asn1DerInputBuffer4;
         int length5 = this.length;
         this.buffer.truncate(length5);
         this.data = new Asn1DerInputStream(this.buffer);
         asn1DerInputBuffer.skip((long)this.length);
      }
   }

   public Asn1DerValue(byte b, byte[] bytes, boolean flag) {
       super();
      this.tag = b;
      Asn1DerInputBuffer asn1DerInputBuffer = new Asn1DerInputBuffer((byte[])bytes.clone(), flag);
      this.buffer = asn1DerInputBuffer;
      int length2 = bytes.length;
      this.length = length2;
      this.data = new Asn1DerInputStream(this.buffer);
      int n = 2147483647;
      this.data.mark(n);
   }

   public String getBMPString() throws IOException {
      if (this.tag != 30) {
         throw new IOException("DerValue.getBMPString, not BMP " + this.tag);
      } else {
         return new String(this.getDataBytes(), "UnicodeBigUnmarked");
      }
   }

   private Asn1DerInputStream init(boolean flag, InputStream in, boolean flag2) throws IOException {
      byte b = (byte)in.read();
      this.tag = b;
      byte b2 = (byte)in.read();
      int length2 = Asn1DerInputStream.getLength(b2, (InputStream)in);
      this.length = length2;
      if (this.length == -1) {
         int n = in.available();
         byte b3 = 2;
         byte[] bytes = new byte[n + b3];
         bytes[0] = this.tag;
         bytes[1] = b2;
         DataInputStream in2 = new DataInputStream((InputStream)in);
         in2.readFully(bytes, b3, n);
         in2.close();
         Asn1DerIndefLenConverter asn1DerIndefLenConverter = new Asn1DerIndefLenConverter();
         in = new ByteArrayInputStream(asn1DerIndefLenConverter.convert(bytes));
         if (this.tag != in.read()) {
            throw new IOException("Indefinite length encoding not supported");
         }

         int length3 = Asn1DerInputStream.getLength((InputStream)in);
         this.length = length3;
      }

      if (flag && in.available() != this.length) {
         throw new IOException("extra data given to DerValue constructor");
      } else {
         byte[] bytes2 = IOUtils.readExactlyNBytes((InputStream)in, this.length);
         Asn1DerInputBuffer asn1DerInputBuffer = new Asn1DerInputBuffer(bytes2, flag2);
         this.buffer = asn1DerInputBuffer;
         return new Asn1DerInputStream(this.buffer);
      }
   }

   public Asn1ObjectIdentifier getOID() throws IOException {
      if (this.tag != 6) {
         throw new IOException("DerValue.getOID, not an OID " + this.tag);
      } else {
         return new Asn1ObjectIdentifier(this.buffer);
      }
   }

   public String getGeneralString() throws IOException {
      if (this.tag != 27) {
         throw new IOException("DerValue.getGeneralString, not GeneralString " + this.tag);
      } else {
         return new String(this.getDataBytes(), "ASCII");
      }
   }

   public boolean isPrivate(long l) {
      return (this.tag & 192) == 192;
   }

   public byte[] getDataBytes() throws IOException {
      byte[] bytes = new byte[this.length];
      synchronized (this.data) {
         this.data.reset();
         this.data.getBytes(bytes);
         return bytes;
      }
   }

   public Asn1DerValue(byte[] bytes) throws IOException {
       this(bytes, true);
   }

   public int length(long l) {
      return this.length;
   }

   public byte[] toByteArray() throws IOException {
      Asn1DerOutputStream asn1DerOutputStream = new Asn1DerOutputStream();
      this.encode(asn1DerOutputStream);
      this.data.reset();
      return asn1DerOutputStream.toByteArray();
   }

   public boolean isConstructed(byte b) {
      return !this.isConstructed() ? false : (this.tag & 31) == b;
   }

   public final byte getTag(long l) {
      return this.tag;
   }

   public String getUTF8String() throws IOException {
      if (this.tag != 12) {
         throw new IOException("DerValue.getUTF8String, not UTF-8 " + this.tag);
      } else {
         return new String(this.getDataBytes(), "UTF8");
      }
   }

   public Asn1DerValue(InputStream in, boolean flag) throws IOException {
       super();
      boolean flag2 = false;
      this.data = this.init(flag2, in, flag);
   }

   public static boolean isPrintableStringChar(char ch) {
      if (ch < 'a' || ch > 'z') {
         if ((ch < 'A' || ch > 'Z') && (ch < '0' || ch > '9')) {
            switch (ch) {
               case ' ':
               case '\'':
               case '(':
               case ')':
               case '+':
               case ',':
               case '-':
               case '.':
               case '/':
               case ':':
               case '=':
               case '?':
                  return true;
               case '!':
               case '"':
               case '#':
               case '$':
               case '%':
               case '&':
               case '*':
               case '0':
               case '1':
               case '2':
               case '3':
               case '4':
               case '5':
               case '6':
               case '7':
               case '8':
               case '9':
               case ';':
               case '<':
               case '>':
               default:
                  return false;
            }
         }
      }

      return true;
   }

   public byte[] getOctetString() throws IOException {
      if (this.tag != 4) {
         byte b = 4;
         if (!this.isConstructed(b)) {
            throw new IOException("DerValue.getOctetString, not an Octet String: " + this.tag);
         }
      }

      if (this.length == 0) {
         return new byte[0];
      } else {
         Asn1DerInputBuffer buffer2 = this.buffer;
         if (buffer2.available() < this.length) {
            throw new IOException("short read on DerValue buffer");
         } else {
            byte[] bytes = new byte[this.length];
            buffer2.read(bytes);
            if (this.isConstructed()) {
               Asn1DerInputStream asn1DerInputStream = new Asn1DerInputStream(bytes, 0, bytes.length, this.buffer.allowBER);
               bytes = null;

               while (asn1DerInputStream.available() != 0) {
                  byte[] octetString = asn1DerInputStream.getOctetString();
                  bytes = this.append(bytes, octetString);
               }
            }

            return bytes;
         }
      }
   }

   public void encode(Asn1DerOutputStream asn1DerOutputStream) throws IOException {
      asn1DerOutputStream.write(this.tag);
      int length2 = this.length;
      asn1DerOutputStream.putLength(length2);
      if (this.length > 0) {
         byte[] bytes = new byte[this.length];
         synchronized (this.data) {
            this.buffer.reset();
            if (this.buffer.read(bytes) != this.length) {
               throw new IOException("short DER value read (encode)");
            }

            asn1DerOutputStream.write(bytes);
         }
      }
   }

   public byte[] getBitString(boolean flag) throws IOException {
      if (!flag && this.tag != 3) {
         throw new IOException("DerValue.getBitString, not a bit string " + this.tag);
      } else {
         return this.buffer.getBitString();
      }
   }

   public boolean isUniversal(long l) {
      return (this.tag & 192) == 0;
   }

   public Asn1DerValue(byte[] bytes, int n, int n2) throws IOException {
       this(bytes, n, n2, true);
   }

   public Asn1DerInputStream toDerInputStream() throws IOException {
      if (this.tag != 48 && this.tag != 49) {
         throw new IOException("toDerInputStream rejects tag type " + this.tag);
      } else {
         return new Asn1DerInputStream(this.buffer);
      }
   }

   public String getAsString() throws IOException {
      if (this.tag == 12) {
         return this.getUTF8String();
      } else if (this.tag == 19) {
         return this.getPrintableString();
      } else if (this.tag == 20) {
         return this.getT61String();
      } else if (this.tag == 22) {
         return this.getIA5String();
      } else if (this.tag == 30) {
         return this.getBMPString();
      } else {
         return this.tag == 27 ? this.getGeneralString() : null;
      }
   }

   public BitArray getUnalignedBitString(boolean flag) throws IOException {
      if (!flag && this.tag != 3) {
         throw new IOException("DerValue.getBitString, not a bit string " + this.tag);
      } else {
         return this.buffer.getUnalignedBitString();
      }
   }

   public boolean isContextSpecific() {
      return (this.tag & 192) == 128;
   }

   private byte[] append(byte[] bytes, byte[] bytes2) {
      if (bytes == null) {
         return bytes2;
      } else {
         byte[] bytes3 = new byte[bytes.length + bytes2.length];
         System.arraycopy(bytes, 0, bytes3, 0, bytes.length);
         System.arraycopy(bytes2, 0, bytes3, bytes.length, bytes2.length);
         return bytes3;
      }
   }

   public void resetTag(byte b, long l) {
      this.tag = b;
   }

   public String getPrintableString() throws IOException {
      if (this.tag != 19) {
         throw new IOException("DerValue.getPrintableString, not a string " + this.tag);
      } else {
         return new String(this.getDataBytes(), "ASCII");
      }
   }




}
