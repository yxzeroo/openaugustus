package net.augustus.vendor.crypto;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Sha256Digest {
   private static final int BLOCK_BYTES = 64;
   private static final int BLOCK_BITS = 512;
   private static final int[] messageSchedule = new int[64];
   private static final int[] workingVars = new int[8];
   private static final int[] INITIAL_HASH = new int[]{1779033703, -1150833019, 1013904242, -1521486534, 1359893119, -1694144372, 528734635, 1541459225};
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
   private static final int[] hashState = new int[8];

   private static int smallSigma0(int n) {
      return Integer.rotateRight(n, 7) ^ Integer.rotateRight(n, 18) ^ n >>> 3;
   }

   private static int bigSigma0(int n) {
      return Integer.rotateRight(n, 2) ^ Integer.rotateRight(n, 13) ^ Integer.rotateRight(n, 22);
   }

   public static byte[] digest(byte[] bytes) {
      int n = 0;
      int n2 = 0;
      Object obj = null;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      long l = 0L;
      l = 5147000228266559690L ^ (long)"\uaad5\uf7a3짇呟\ueb95\ufff5工㞐ᘠ﵄ί㮿\u0efa\uf383럈롶펤瘄烚﮽".hashCode();
      System.arraycopy(INITIAL_HASH, 0, hashState, 0, INITIAL_HASH.length);
      l = (l ^ 8793220121375815786L) + 2115312605378535414L;
      obj = padMessage(bytes);
      l = (l ^ 8410634607981383453L) + 3190375794153967777L;
      n5 = 0;
      l = l + -4194752469013046588L ^ 5660661277325271149L;
      n = ((int[])obj).length / 16;
      l = (l ^ 8461603585463092234L) - 7375053365348793641L;

      while (n5 < n) {
         l = l + 1730498320373474317L ^ 382427512301770819L;
         System.arraycopy((int[])obj, n5 * 16, messageSchedule, 0, 16);
         l = (l ^ 1489850512893325952L) - -3051447018457526085L;
         n2 = 16;
         l = l - 8352730866191248682L ^ -2970295315971594318L;

         while (n2 < ((int[])messageSchedule).length) {
            l = l + 8465254218220507307L ^ 4127612639535129826L;
            messageSchedule[n2] = smallSigma1(messageSchedule[n2 - 2]) + messageSchedule[n2 - 7] + smallSigma0(messageSchedule[n2 - 15]) + messageSchedule[n2 - 16];
            l = l - -6445104931716770336L ^ 6472886893731373460L;
            n2++;
            l = (l ^ -1323046951103317426L) + -9032985647044729483L;
            l = l + -8167621865091999766L ^ -1200019237831427226L;
         }

         l = (l ^ -4705684386205761006L) - -688041557223238497L;
         System.arraycopy(hashState, 0, workingVars, 0, hashState.length);
         l = (l ^ -4802323114753777464L) + -5354491820857179227L;
         n2 = 0;
         l = l + 8598660304143581502L ^ -357023886131769558L;

         while (n2 < ((int[])messageSchedule).length) {
            l = (l ^ 4448717752436038595L) + 8073366192158738583L;
            short s = 8055;
            n4 = workingVars[7] + bigSigma1(workingVars[4]) + ch(workingVars[4], workingVars[5], workingVars[6]) + ROUND_CONSTANTS[n2] + messageSchedule[n2];
            l = (l ^ 5749031934944396036L) - 5839444651157180022L;
            n3 = bigSigma0(workingVars[0]) + maj(workingVars[0], workingVars[1], workingVars[2]);
            l = l + 2345268464331193899L ^ -2344029625782719057L;
            System.arraycopy(workingVars, 0, workingVars, 1, workingVars.length - 1);
            l = l + -7384871024660182777L ^ -2050950395567324578L;
            workingVars[4] = workingVars[4] + n4;
            l = l - 6234042517235614671L ^ -3990494299337561081L;
            workingVars[0] = n4 + n3;
            l = (l ^ 916742700119497142L) + -3933017009274802363L;
            n2++;
            l = l + -2261691778981734371L ^ -1933449164965782616L;
            l = (l ^ 6525982105424449434L) - -7187066000525114397L;
         }

         l = (l ^ 4025764033261784574L) - 6765581200282873075L;
         n2 = 0;
         l = l + -2439435765004429690L ^ 911194867581778567L;

         while (n2 < ((int[])hashState).length) {
            l = l - 1228588317151816996L ^ -6935862992113531972L;
            hashState[n2] = hashState[n2] + workingVars[n2];
            l = (l ^ 7768669485871963459L) - 1999960918907474542L;
            n2++;
            l = (l ^ -6142262281836745213L) - -8841498098955436472L;
            l = (l ^ -7182462974931071696L) - 4586702669063052374L;
         }

         l = (l ^ 4902141343682963158L) - 8042932924959082412L;
         n5++;
         l = l + -2204718071358755295L ^ 7910971995202688632L;
         l = (l ^ 4193726709957191297L) - -6311428925964537315L;
      }

      l = l - 691543005916728312L ^ 1535414513098042283L;
      return intsToBytes(hashState);
   }

   private static int maj(int n, int n2, int n3) {
      return n & n2 | n & n3 | n2 & n3;
   }


   private static int smallSigma1(int n) {
      return Integer.rotateRight(n, 17) ^ Integer.rotateRight(n, 19) ^ n >>> 10;
   }

   private static int bigSigma1(int n) {
      return Integer.rotateRight(n, 6) ^ Integer.rotateRight(n, 11) ^ Integer.rotateRight(n, 25);
   }

   private static byte[] intsToBytes(int[] ints) {
      Object obj = null;
      Object obj2 = null;
      int n = 0;
      boolean flag = false;
      int n2 = 0;
      obj = ByteBuffer.allocate(ints.length * 4);
      obj2 = ints;
      n = ((int[])obj2).length;

      for (int i = 0; i < n; i++) {
         n2 = ((int[])obj2)[i];
         ((ByteBuffer)obj).putInt(n2);
      }

      return ((ByteBuffer)obj).array();
   }

   public static int[] padMessage(byte[] bytes) {
      int n = bytes.length % 64;
      IntBuffer intBuffer = IntBuffer.allocate((bytes.length / 64 + (n + 1 + 8 > 64 ? 2 : 1)) * 16);
      ByteBuffer buf = ByteBuffer.wrap(bytes);
      int n3 = 0;

      for (int i = bytes.length / 4; n3 < i; n3++) {
         intBuffer.put(buf.getInt());
      }

      ByteBuffer buf2 = ByteBuffer.allocate(4);
      ((Buffer)buf2.put(buf).put((byte)-128)).rewind();
      intBuffer.put(buf2.getInt());
      ((Buffer)intBuffer).position(intBuffer.capacity() - 2);
      long l = (long)bytes.length * 8L;
      intBuffer.put((int)(l >>> 32));
      intBuffer.put((int)l);
      return intBuffer.array();
   }

   private static int ch(int n, int n2, int n3) {
      return n & n2 | ~n & n3;
   }

}
