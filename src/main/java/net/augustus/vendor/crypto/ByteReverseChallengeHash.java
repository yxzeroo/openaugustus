package net.augustus.vendor.crypto;


public class ByteReverseChallengeHash {
   private static long INIT_XOR_CONST;
   private static long HIGH_WORD_MASK;
   private static int MIX_ROUNDS;
   private static long LOW_WORD_MASK;
   private static long SHIFT_XOR_CONST;

   private static long foldBytesAndInvert(long l) {
      long l2 = 0L;
      boolean flag = false;
      long l3 = 0L;
      int n = 0;
      long l4 = 0L;
      l4 = l;

      for (int i = 0; i < 64; i += 8) {
         l2 = l4 >>> i & 255L;
         l2 = (l2 ^ l4 & 255L) + (l4 >>> 32 & 255L);
         l2 &= 255L;
         n = (i + 24) % 64;
         l3 = ~(255L << n);
         l4 = l4 & l3 | l2 << n;
      }

      l4 = ~l4;
      return l4 ^ 72624976668147840L;
   }

   private static long reverseByteRounds(long l) {
      boolean flag = false;
      long l2 = 0L;
      l2 = l ^ 1234605616436508552L;

      for (int i = 0; i < 4; i++) {
         l2 += l << i * 3 | l >>> 64 - i * 3;
         l2 ^= 72624976668147840L >> i * 5;
         l2 = Long.reverseBytes(l2);
         l2 ^= l & -281470681808896L >>> i * 4;
      }

      return l2;
   }

   private static long swapOuterWords(long l) {
      long l2 = 0L;
      long l3 = 0L;
      long l4 = 0L;
      long l5 = 0L;
      long l6 = 0L;
      l6 = 112774389809010640L ^ (long)"⚃葠衄疭፝㸘\uf3de\u0b59㬹䰉\uf041\uf4b3\ueb59⧺뱈ꌳ腀\ue456說濚\u001d⍊\udc13룗ᢱ".hashCode();
      l5 = l >>> 48 & 65535L;
      l6 = (l6 ^ -8482996345092816462L) - -7281621517427860371L;
      l4 = l & 65535L;
      l6 = (l6 ^ -9079326333105591824L) - 3325128039390248741L;
      l3 = l & 281474976645120L;
      l6 = l6 - -4123947841833554342L ^ -5676251367286072323L;
      l6 = l6 + 4159108980046833257L ^ 6828173783293470573L;
      l2 = l3 | l5;
      l6 = (l6 ^ 5143666904362575454L) + 5217298516224983987L;
      l2 |= l4 << 48;
      l6 = (l6 ^ 2146394450725492165L) + 4536247980423170589L;
      l2 ^= (l & 16711680L) << 16;
      l6 = (l6 ^ 4500777926334086379L) + 8631075361360624698L;
      l2 ^= (l & 65280L) >>> 8;
      l6 = l6 - -481655597692162296L ^ -7650401065379132078L;
      return l2;
   }

   private ByteReverseChallengeHash() {
   }

   private static long dynamicRotateMix(long l) {
      int n = 0;
      int n2 = 0;
      long l2 = 0L;
      long l3 = 0L;
      l3 = -8426766253772379569L ^ (long)"গ\ue5e2丗蓕瘍\ue7a2\ueb2f㇛덇㚾懟㞽ȯ픩絯䞚Ꟊ臘䅩쪑".hashCode();
      n = (int)(l & 63L);
      l3 = l3 - -162040755603150974L ^ 6809931671090025538L;
      n2 = (n + 13) % 64;
      l3 = l3 + -1692422883189242217L ^ -4715642738469373271L;
      l2 = l << n2 | l >>> 64 - n2;
      l3 = l3 - -6891506509516974036L ^ -331680404001381883L;
      if (n2 % 2 != 0) {
         l3 = l3 + 215881202980791908L ^ -3235850335540893732L;
         l2 ^= 281470681808895L;
         l3 = (l3 ^ 7316635291388336551L) + -484778076811973149L;
      } else {
         l3 = l3 - 4948630329761501245L ^ -1680178263120870725L;
         l2 ^= -281470681808896L;
         l3 = l3 + 417069061038067860L ^ -144792758931823559L;
         l3 = (l3 ^ 296031734772907452L) - 5999284264280839565L;
      }

      l2 ^= 1234605616436508552L << n2 % 8;
      l3 = (l3 ^ -301130369123238976L) + 828095999974644918L;
      return l2;
   }

   public static long hash(long l) {
      long l2 = 0L;
      l2 = reverseByteRounds(l);
      l2 = swapOuterWords(l2);
      l2 = dynamicRotateMix(l2);
      return foldBytesAndInvert(l2);
   }

}
