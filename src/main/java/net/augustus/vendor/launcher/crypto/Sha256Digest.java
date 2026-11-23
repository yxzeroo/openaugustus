package net.augustus.vendor.launcher.crypto;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Sha256Digest {
   private static final int BLOCK_SIZE_BYTES = 64;
   private final int[] messageSchedule = new int[64];
   private static final int[] ROUND_CONSTANTS = new int[]{
      1116352408,
      1899447441,
      -1245643825,
      -373957723,
      961987163,
      1508970993,
      -1841331548,
      -1424204075,
      -670586216,
      310598401,
      607225278,
      1426881987,
      1925078388,
      -2132889090,
      -1680079193,
      -1046744716,
      -459576895,
      -272742522,
      264347078,
      604807628,
      770255983,
      1249150122,
      1555081692,
      1996064986,
      -1740746414,
      -1473132947,
      -1341970488,
      -1084653625,
      -958395405,
      -710438585,
      113926993,
      338241895,
      666307205,
      773529912,
      1294757372,
      1396182291,
      1695183700,
      1986661051,
      -2117940946,
      -1838011259,
      -1564481375,
      -1474664885,
      -1035236496,
      -949202525,
      -778901479,
      -694614492,
      -200395387,
      275423344,
      430227734,
      506948616,
      659060556,
      883997877,
      958139571,
      1322822218,
      1537002063,
      1747873779,
      1955562222,
      2024104815,
      -2067236844,
      -1933114872,
      -1866530822,
      -1538233109,
      -1090935817,
      -965641998
   };
   private final int[] workingVars;
   private final int[] hashState = new int[8];
   private static final int BLOCK_SIZE_BITS = 512;
   private static final int[] INITIAL_HASH_VALUES = new int[]{1779033703, -1150833019, 1013904242, -1521486534, 1359893119, -1694144372, 528734635, 1541459225};
   private static int[] flowState;
   private static final long e = 8L;

   private int bigSigma1(int n) {
      return Integer.rotateRight(n, 6) ^ Integer.rotateRight(n, 11) ^ Integer.rotateRight(n, 25);
   }

   private int choose(int n, int n2, int n3) {
      return n & n2 | ~n & n3;
   }

   public int[] padMessage(byte[] bytes) {
      int[] flowState2 = getFlowState();
      int n = bytes.length % 64;
      int n2 = bytes.length / 64 + (n + 1 + 8 > 64 ? 2 : 1);
      IntBuffer intBuffer = IntBuffer.allocate(n2 * 16);
      ByteBuffer buf = ByteBuffer.wrap(bytes);
      int n3 = 0;

      for (int i = bytes.length / 4; n3 < i; n3++) {
         intBuffer.put(buf.getInt());
      }

      ByteBuffer buf2 = ByteBuffer.allocate(4);
      ((Buffer)buf2.put(buf).put((byte)-128)).rewind();
      intBuffer.put(buf2.getInt());
      ((Buffer)intBuffer).position(intBuffer.capacity() - 2);
      long l = (long)bytes.length * e;
      intBuffer.put((int)(l >>> 32));
      intBuffer.put((int)l);
      return intBuffer.array();
   }

   static {
      setFlowState(null);
   }

   private int smallSigma1(int n) {
      return Integer.rotateRight(n, 17) ^ Integer.rotateRight(n, 19) ^ n >>> 10;
   }

   private int majority(int n, int n2, int n3) {
      return n & n2 | n & n3 | n2 & n3;
   }

   private byte[] intsToBytes(int[] ints) {
      ByteBuffer buf = ByteBuffer.allocate(ints.length * 4);
      getFlowState();

      for (int n : ints) {
         buf.putInt(n);
      }

      return buf.array();
   }

   private int bigSigma0(int n) {
      return Integer.rotateRight(n, 2) ^ Integer.rotateRight(n, 13) ^ Integer.rotateRight(n, 22);
   }

   public Sha256Digest() {
      this.workingVars = new int[8];
   }

   public byte[] digest(byte[] bytes) {
      System.arraycopy(INITIAL_HASH_VALUES, 0, this.hashState, 0, INITIAL_HASH_VALUES.length);
      int[] ints = this.padMessage(bytes);
      getFlowState();
      int n = 0;

      for (int i = ints.length / 16; n < i; n++) {
         System.arraycopy(ints, n * 16, this.messageSchedule, 0, 16);

         for (int j = 16; j < this.messageSchedule.length; j++) {
            int[] messageSchedule2 = this.messageSchedule;
            int n2 = this.messageSchedule[j - 2];
            int n3 = n2;
            int n4 = this.smallSigma1(n3) + this.messageSchedule[j - 7];
            int n5 = this.messageSchedule[j - 15];
            messageSchedule2[j] = n4 + this.smallSigma0(n5) + this.messageSchedule[j - 16];
         }

         System.arraycopy(this.hashState, 0, this.workingVars, 0, this.hashState.length);

         for (int k = 0; k < this.messageSchedule.length; k++) {
            int n6 = this.workingVars[7];
            int n7 = this.workingVars[4];
            n6 += this.bigSigma1(n7);
            int n8 = this.workingVars[4];
            int n9 = this.workingVars[5];
            int n10 = this.workingVars[6];
            int n11 = n9;
            int n12 = n8;
            int n13 = n6 + this.choose(n12, n11, n10) + ROUND_CONSTANTS[k] + this.messageSchedule[k];
            int n14 = this.workingVars[0];
            n6 = this.bigSigma0(n14);
            n8 = this.workingVars[0];
            n9 = this.workingVars[1];
            int n15 = this.workingVars[2];
            int n16 = n9;
            int n17 = n8;
            int n18 = n6 + this.majority(n17, n16, n15);
            System.arraycopy(this.workingVars, 0, this.workingVars, 1, this.workingVars.length - 1);
            this.workingVars[4] = this.workingVars[4] + n13;
            this.workingVars[0] = n13 + n18;
         }

         for (int m = 0; m < this.hashState.length; m++) {
            this.hashState[m] = this.hashState[m] + this.workingVars[m];
         }
      }

      int[] hashState2 = this.hashState;
      return this.intsToBytes(hashState2);
   }

   private int smallSigma0(int n) {
      return Integer.rotateRight(n, 7) ^ Integer.rotateRight(n, 18) ^ n >>> 3;
   }

   public static void setFlowState(int[] ints) {
      flowState = ints;
   }

   public static int[] getFlowState() {
      return flowState;
   }
}
