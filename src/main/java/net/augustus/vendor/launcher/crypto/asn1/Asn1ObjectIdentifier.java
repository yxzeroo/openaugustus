package net.augustus.vendor.launcher.crypto.asn1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;

public final class Asn1ObjectIdentifier implements Serializable {
   private static final long serialVersionUID = 0L;
   private Object components;
   private int componentLen;
   public static final boolean assertionsDisabled = !Asn1ObjectIdentifier.class.desiredAssertionStatus();
   private byte[] encoding;
   private transient boolean componentsCalculated;
   private transient volatile String stringForm;

   private static void checkOtherComponent2(int n, BigInteger bigInt) throws IOException {
      if (bigInt.signum() == -1) {
         throw new IOException("ObjectIdentifier() -- oid component #" + (n + 1) + " must be non-negative ");
      }
   }

   private static byte[] pack(byte[] bytes, int n, int n2, int n3, int n4) {
      if (assertionsDisabled || n3 > 0 && n3 <= 8) {
         if (assertionsDisabled || n4 > 0 && n4 <= 8) {
            if (n3 == n4) {
               return (byte[])bytes.clone();
            } else {
               int n5 = n2 * n3;
               byte[] bytes2 = new byte[(n5 + n4 - 1) / n4];
               int n6 = 0;
               int n7 = (n5 + n4 - 1) / n4 * n4 - n5;

               while (n6 < n5) {
                  int n8 = n3 - n6 % n3;
                  if (n8 > n4 - n7 % n4) {
                     n8 = n4 - n7 % n4;
                  }

                  bytes2[n7 / n4] = (byte)(
                     bytes2[n7 / n4] | (bytes[n + n6 / n3] + 256 >> n3 - n6 % n3 - n8 & (1 << n8) - 1) << n4 - n7 % n4 - n8
                  );
                  n6 += n8;
                  n7 += n8;
               }

               return bytes2;
            }
         } else {
            throw new AssertionError("output NUB must be between 1 and 8");
         }
      } else {
         throw new AssertionError("input NUB must be between 1 and 8");
      }
   }

   @Override
   public int hashCode() {
      return Arrays.hashCode(this.encoding);
   }

   private static void checkSecondComponent2(int n, int n2) throws IOException {
      if (n2 < 0 || n != 2 && n2 > 39) {
         throw new IOException("ObjectIdentifier() -- Second oid component is invalid ");
      }
   }

   private void init(int[] ints, int n) {
      byte b = 0;
      byte[] bytes = new byte[n * 5 + 1];
      int n2;
      if (ints[1] < 2147483647 - ints[0] * 40) {
         n2 = b + pack7Oid2(ints[0] * 40 + ints[1], bytes, b);
      } else {
         BigInteger bigInt = BigInteger.valueOf((long)ints[1]);
         bigInt = bigInt.add(BigInteger.valueOf((long)(ints[0] * 40)));
         n2 = b + pack7Oid(bigInt, bytes, b);
      }

      for (int i = 2; i < n; i++) {
         int n4 = ints[i];
         n2 += pack7Oid2(n4, bytes, n2);
      }

      this.encoding = new byte[n2];
      System.arraycopy(bytes, 0, this.encoding, 0, n2);
   }

   private static void checkCount(int n) throws IOException {
      if (n < 2) {
         throw new IOException("ObjectIdentifier() -- Must be at least two oid components ");
      }
   }

   public Asn1ObjectIdentifier(Asn1DerInputBuffer asn1DerInputBuffer) throws IOException {
       super();
      this.encoding = (byte[])null;
      this.components = null;
      this.componentLen = (byte)-1;
      this.componentsCalculated = false;
      Asn1DerInputStream asn1DerInputStream = new Asn1DerInputStream(asn1DerInputBuffer);
      this.encoding = new byte[asn1DerInputStream.available()];
      asn1DerInputStream.getBytes(this.encoding);
      check(this.encoding);
   }

   private static void checkFirstComponent2(int n) throws IOException {
      if (n < 0 || n > 2) {
         throw new IOException("ObjectIdentifier() -- First oid component is invalid ");
      }
   }

   private int[] toIntArray() {
      int length = this.encoding.length;
      int[] ints = new int[20];
      int n = 0;
      int n2 = 0;

      for (int i = 0; i < length; i++) {
         if ((this.encoding[i] & 128) == 0) {
            if (i - n2 + 1 > 4) {
               int n3 = i - n2 + 1;
               BigInteger bigInt = new BigInteger(pack(this.encoding, n2, n3, (byte)7, (byte)8));
               if (n2 == 0) {
                  ints[n++] = 2;
                  BigInteger bigInt2 = bigInt.subtract(BigInteger.valueOf(80L));
                  if (bigInt2.compareTo(BigInteger.valueOf(2147483647L)) == 1) {
                     return null;
                  }

                  ints[n++] = bigInt2.intValue();
               } else {
                  if (bigInt.compareTo(BigInteger.valueOf(2147483647L)) == 1) {
                     return null;
                  }

                  ints[n++] = bigInt.intValue();
               }
            } else {
               int n5 = 0;

               for (int j = n2; j <= i; j++) {
                  n5 <<= 7;
                  byte b3 = this.encoding[j];
                  n5 |= b3 & 127;
               }

               if (n2 == 0) {
                  if (n5 < 80) {
                     ints[n++] = n5 / 40;
                     ints[n++] = n5 % 40;
                  } else {
                     ints[n++] = 2;
                     ints[n++] = n5 - 80;
                  }
               } else {
                  ints[n++] = n5;
               }
            }

            n2 = i + 1;
         }

         if (n >= ints.length) {
            ints = Arrays.copyOf(ints, n + 10);
         }
      }

      return Arrays.copyOf(ints, n);
   }


   private static void checkSecondComponent(int n, BigInteger bigInt) throws IOException {
      if (bigInt.signum() == -1 || n != 2 && bigInt.compareTo(BigInteger.valueOf(39L)) == 1) {
         throw new IOException("ObjectIdentifier() -- Second oid component is invalid ");
      }
   }

   private static int pack8(byte[] bytes, int n, int n2, byte[] bytes2, int n3) {
      byte[] bytes3 = pack(bytes, n, n2, (byte)7, (byte)8);
      int n4 = bytes3.length - 1;

      for (int i = bytes3.length - 2; i >= 0; i--) {
         if (bytes3[i] != 0) {
            n4 = i;
         }
      }

      System.arraycopy(bytes3, n4, bytes2, n3, bytes3.length - n4);
      return bytes3.length - n4;
   }

   public Asn1ObjectIdentifier(int[] ints) throws IOException {
       super();
      this.encoding = (byte[])null;
      this.components = null;
      this.componentLen = (byte)-1;
      this.componentsCalculated = false;
      checkCount(ints.length);
      checkFirstComponent2(ints[0]);
      checkSecondComponent2(ints[0], ints[1]);

      for (int i = 2; i < ints.length; i++) {
         checkOtherComponent(i, ints[i]);
      }

      this.init(ints, ints.length);
   }

   private static int pack7Oid(byte[] bytes, int n, int n2, byte[] bytes2, int n3) {
      byte[] bytes3 = pack(bytes, n, n2, (byte)8, (byte)7);
      int n4 = bytes3.length - 1;

      for (int i = bytes3.length - 2; i >= 0; i--) {
         if (bytes3[i] != 0) {
            n4 = i;
         }

         bytes3[i] = (byte)(bytes3[i] | 128);
      }

      System.arraycopy(bytes3, n4, bytes2, n3, bytes3.length - n4);
      return bytes3.length - n4;
   }

   private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
      objectInputStream.defaultReadObject();
      if (this.encoding == null) {
         int[] components2 = (int[])this.components;
         if (this.componentLen > components2.length) {
            this.componentLen = components2.length;
         }

         this.init(components2, this.componentLen);
      }
   }

   private static void check(byte[] bytes) throws IOException {
      int length = bytes.length;
      if (length >= 1 && (bytes[length - 1] & 128) == 0) {
         for (int i = 0; i < length; i++) {
            if (bytes[i] == -128 && (i == 0 || (bytes[i - 1] & 128) == 0)) {
               throw new IOException("ObjectIdentifier() -- Invalid DER encoding, useless extra octet detected");
            }
         }
      } else {
         throw new IOException("ObjectIdentifier() -- Invalid DER encoding, not ended");
      }
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof Asn1ObjectIdentifier)) {
         return false;
      } else {
         Asn1ObjectIdentifier asn1ObjectIdentifier = (Asn1ObjectIdentifier)obj;
         return Arrays.equals(this.encoding, asn1ObjectIdentifier.encoding);
      }
   }

   @Deprecated
   public boolean equals(Asn1ObjectIdentifier asn1ObjectIdentifier, long l) {
      return this.equals(asn1ObjectIdentifier);
   }

   private static void checkOtherComponent(int n, int n2) throws IOException {
      if (n2 < 0) {
         throw new IOException("ObjectIdentifier() -- oid component #" + (n + 1) + " must be non-negative ");
      }
   }

   @Override
   public String toString() {
      String stringForm2 = this.stringForm;
      if (stringForm2 == null) {
         int length = this.encoding.length;
         StringBuffer sb = new StringBuffer(length * 4);
         int n = 0;

         for (int i = 0; i < length; i++) {
            if ((this.encoding[i] & 128) == 0) {
               if (n != 0) {
                  sb.append('.');
               }

               if (i - n + 1 > 4) {
                  int n2 = i - n + 1;
                  BigInteger bigInt = new BigInteger(pack(this.encoding, n, n2, (byte)7, (byte)8));
                  if (n == 0) {
                     sb.append("2.");
                     sb.append(bigInt.subtract(BigInteger.valueOf(80L)));
                  } else {
                     sb.append(bigInt);
                  }
               } else {
                  int n4 = 0;

                  for (int j = n; j <= i; j++) {
                     n4 <<= 7;
                     byte b3 = this.encoding[j];
                     n4 |= b3 & 127;
                  }

                  if (n == 0) {
                     if (n4 < 80) {
                        sb.append(n4 / 40);
                        sb.append('.');
                        sb.append(n4 % 40);
                     } else {
                        sb.append("2.");
                        sb.append(n4 - 80);
                     }
                  } else {
                     sb.append(n4);
                  }
               }

               n = i + 1;
            }
         }

         stringForm2 = sb.toString();
         this.stringForm = stringForm2;
      }

      return stringForm2;
   }

   public Asn1ObjectIdentifier(Asn1DerInputStream asn1DerInputStream) throws IOException {
       super();
      this.encoding = (byte[])null;
      this.components = null;
      this.componentLen = (byte)-1;
      this.componentsCalculated = false;
      byte b2 = (byte)asn1DerInputStream.getByte();
      if (b2 != 6) {
         throw new IOException("ObjectIdentifier() -- data isn't an object ID (tag = " + b2 + ")");
      } else {
         int length = asn1DerInputStream.getLength();
         if (length > asn1DerInputStream.available()) {
            throw new IOException("ObjectIdentifier() -- length exceedsdata available.  Length: " + length + ", Available: " + asn1DerInputStream.available());
         } else {
            this.encoding = new byte[length];
            asn1DerInputStream.getBytes(this.encoding);
            check(this.encoding);
         }
      }
   }

   public void encode(Asn1DerOutputStream asn1DerOutputStream) throws IOException {
      asn1DerOutputStream.write((byte)6, this.encoding);
   }

   private static int pack7Oid(BigInteger bigInt, byte[] bytes, int n) {
      byte[] bytes2 = bigInt.toByteArray();
      return pack7Oid(bytes2, (byte)0, bytes2.length, bytes, n);
   }

   private static void checkFirstComponent(BigInteger bigInt) throws IOException {
      if (bigInt.signum() == -1 || bigInt.compareTo(BigInteger.valueOf(2L)) == 1) {
         throw new IOException("ObjectIdentifier() -- First oid component is invalid ");
      }
   }

   public Asn1ObjectIdentifier(String str) throws IOException {
       super();
      this.encoding = (byte[])null;
      this.components = null;
      this.componentLen = (byte)-1;
      this.componentsCalculated = false;
      int n = 0;
      int n2 = 0;
      byte[] bytes = new byte[str.length()];
      int n3 = 0;
      int n4 = 0;

      try {
         Object obj3 = null;

         int n5;
         do {
            n5 = str.indexOf((byte)46, n);
            int n6;
            if (n5 == -1) {
               obj3 = str.substring(n);
               n6 = str.length() - n;
            } else {
               obj3 = str.substring(n, n5);
               n6 = n5 - n;
            }

            if (n6 > 9) {
               BigInteger bigInt = new BigInteger((String)obj3);
               if (n4 == 0) {
                  checkFirstComponent(bigInt);
                  n3 = bigInt.intValue();
               } else {
                  if (n4 == 1) {
                     checkSecondComponent(n3, bigInt);
                     bigInt = bigInt.add(BigInteger.valueOf((long)(40 * n3)));
                  } else {
                     checkOtherComponent2(n4, bigInt);
                  }

                  n2 += pack7Oid(bigInt, bytes, n2);
               }
            } else {
               int n7 = Integer.parseInt((String)obj3);
               if (n4 == 0) {
                  checkFirstComponent2(n7);
                  n3 = n7;
               } else {
                  if (n4 == 1) {
                     checkSecondComponent2(n3, n7);
                     n7 += 40 * n3;
                  } else {
                     checkOtherComponent(n4, n7);
                  }

                  n2 += pack7Oid2(n7, bytes, n2);
               }
            }

            n = n5 + 1;
            n4++;
         } while (n5 != -1);

         checkCount(n4);
         this.encoding = new byte[n2];
         System.arraycopy(bytes, 0, this.encoding, 0, n2);
         this.stringForm = str;
      } catch (IOException e) {
         throw e;
      } catch (Exception e2) {
         throw new IOException("ObjectIdentifier() -- Invalid format: " + e2.toString(), e2);
      }
   }

   public static Asn1ObjectIdentifier newInternal(int[] ints) {

      try {
         return new Asn1ObjectIdentifier(ints);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   private static int pack7Oid2(int n, byte[] bytes, int n2) {
      return pack7Oid(new byte[]{(byte)(n >> 24), (byte)(n >> 16), (byte)(n >> 8), (byte)n}, (byte)0, (byte)4, bytes, n2);
   }

   private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
      if (!this.componentsCalculated) {
         int[] ints = this.toIntArray();
         if (ints != null) {
            this.components = ints;
            this.componentLen = ints.length;
         } else {
            this.components = HugeOidNotSupportedMarker.theOne;
         }

         this.componentsCalculated = true;
      }

      objectOutputStream.defaultWriteObject();
   }

}
