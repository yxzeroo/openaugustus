package net.augustus.vendor.crypto;


public class NibbleSwapChallengeHash {
   private static int LEFT_SHIFT_BITS;
   private static long HIGH_NIBBLE_MASK;
   private static long EVEN_MASK;
   private static long ODD_MASK;
   private static long ADDITIVE_CONST;
   private static long LOW_NIBBLE_MASK;
   private static int RIGHT_SHIFT_BITS;

   private static long affineByteFold(long l) {
      long l2 = 0L;
      boolean flag = false;
      long l3 = 0L;
      l2 = l;

      for (int i = 0; i < 8; i++) {
         l3 = l2 >>> i * 8 & 255L;
         l3 = l3 * 3L + 7L & 255L;
         l2 ^= l3 << (i + 3) % 8 * 8;
      }

      return l2 & -1085102592571150096L | ~l2 & 1085102592571150095L;
   }

   private static long permuteAndMaskBytes(long l) {
      long l2 = 0L;
      long l3 = 0L;
      long l4 = 0L;
      long l5 = 0L;
      long l6 = 0L;
      long l7 = 0L;
      long l8 = 0L;
      long l9 = 0L;
      long l10 = 0L;
      l8 = l >>> 0 & 255L;
      l9 = l >>> 8 & 255L;
      l6 = l >>> 16 & 255L;
      l10 = l >>> 24 & 255L;
      l5 = l >>> 32 & 255L;
      l7 = l >>> 40 & 255L;
      l4 = l >>> 48 & 255L;
      l3 = l >>> 56 & 255L;
      l2 = 0L;
      l2 |= (l3 ^ 26L) << 56;
      l2 |= (l8 ^ 43L) << 48;
      l2 |= (l7 ^ 60L) << 40;
      l2 |= (l6 ^ 77L) << 32;
      l2 |= (l10 ^ 94L) << 24;
      l2 |= (l4 ^ 111L) << 16;
      l2 |= (l9 ^ 112L) << 8;
      l2 |= l5 ^ 129L;
      return l2 ^ -6148914691236517206L;
   }

   private static long swapLowestByteNibbles(long l) {
      long l2 = 0L;
      long l3 = 0L;
      long l4 = 0L;
      long l5 = 0L;
      l2 = l & 255L;
      l3 = (l2 & 240L) >>> 4;
      l4 = (l2 & 15L) << 4;
      l5 = l3 | l4;
      return l & -256L | l5;
   }

   private NibbleSwapChallengeHash() {
   }

   public static long hash(long l) {
      long l2 = 0L;
      long l3 = 0L;
      l3 = 1143364616049274578L ^ (long)"콌ퟯ⟸\udf86걄ⷝ⭥츁唀䩦茑㰡钱\ufeff퇾⾧䛣ⓚ뜫䆿\udde6ꮃ䒉".hashCode();
      l3 = l3 - -9175944486434722912L ^ 5972432418999320376L;
      l2 = initialRotateMix(l);
      l3 = (l3 ^ 3306389284997419211L) - 2819481376958137840L;
      l2 = conditionalNibbleMix(l2);
      l3 = (l3 ^ 278923612467751961L) + 7538053177468984994L;
      l2 = permuteAndMaskBytes(l2);
      l3 = (l3 ^ 8962871771486601750L) - 8313626483736766829L;
      l2 = affineByteFold(l2);
      l3 = l3 - 5337830116957326683L ^ 8534729585685493199L;
      return l2;
   }

   private static long conditionalNibbleMix(long l) {
      int n = 0;
      long l2 = 0L;
      long l3 = 0L;
      l3 = -1281213981986009273L ^ (long)"찍ᏽ╔\ue744\ud804\uf533왶㝚\u0dc9畃콤헂赀覃ⱉ싁夰\ue3e2佁댌｠".hashCode();
      l3 = l3 + -5169227468562303313L ^ 7381112814446429143L;
      if ((l & 255L) <= 64L) {
         l3 = l3 - 2419555476576884812L ^ -5810453095548164758L;
         l2 = invertMiddleBytes(l);
         l3 = l3 - 531112353141240499L ^ -1281268163834573007L;
         l2 ^= l << 8;
         l3 = (l3 ^ -2640109904763169972L) + -4514905302565617670L;
      } else {
         l3 = l3 - 5914572407843227315L ^ -1581579870784984455L;
         l2 = swapLowestByteNibbles(l);
         l3 = (l3 ^ -2774863995991609492L) - 4333895913077128821L;
         l2 ^= l >>> 8;
         l3 = (l3 ^ 2510315508657963352L) - 5485472019015292269L;
         l3 = (l3 ^ 2887352659708028049L) - -181814082884193789L;
      }

      n = 0;
      l3 = (l3 ^ -2995384044191995535L) + 6487314580668163925L;

      while (n < 4) {
         l3 = (l3 ^ 844796455209281303L) + 5402429825290842543L;
         if ((l >> n * 8 & 1L) != 1L) {
            l3 = l3 - 5830227436269626218L ^ -8404482750234512651L;
            l2 ^= 1085102592571150095L << n * 2;
            l3 = (l3 ^ -7596147513948052637L) - 3027040578112676232L;
         } else {
            l3 = l3 + 3929391385208277955L ^ 7029364141218230624L;
            l2 ^= -1085102592571150096L >>> n * 2;
            l3 = l3 + 3191992937509062516L ^ 1912375632686325375L;
            l3 = (l3 ^ 2072344544591404664L) - 1241303927634680024L;
         }

         n++;
         l3 = l3 + 2734425104939416811L ^ -6325797170305523603L;
         l3 = l3 - 8784316204576565688L ^ 4247135877259785544L;
      }

      l3 = (l3 ^ -1590161987330189342L) + 6514062285922610907L;
      return l2;
   }

   private static long invertMiddleBytes(long l) {
      long l2 = 0L;
      long l3 = 0L;
      long l4 = 0L;
      long l5 = 0L;
      l5 = 8340649530792355580L ^ (long)"筃䢊⨱퇴\uf055捌䘳濵\uf8ffꕫቓ난⤒皾㰁\uee7f㟕ɥ두턟黇䡡嬼쩿酀".hashCode();
      l4 = 281474976645120L;
      l5 = l5 + 3378407743808566638L ^ 8826966969796211668L;
      l3 = l & l4;
      l5 = (l5 ^ 4898313021881353251L) - 6764451215520828156L;
      l2 = ~l3 & l4;
      l5 = (l5 ^ 6191303971372320289L) + 9176026033541924430L;
      return l & ~l4 | l2;
   }

   private static long initialRotateMix(long l) {
      long l2 = 0L;
      long l3 = 0L;
      l3 = 2038629006725523444L ^ (long)"Ẻ绌븫풒⎝윏ᴀ̽\ua9db붐驺ﳬ혩盍ဵ易轻쟴\ue485逜졶\ude82瞼ᨄ絪㲙".hashCode();
      l2 = l ^ -6148914691236517206L;
      l3 = (l3 ^ 7587302045653026535L) - 436525475006505106L;
      l2 = l2 << 3 | l2 >>> 61;
      l3 = (l3 ^ 1512628947694452529L) + 9141448094794274662L;
      l2 ^= l & -1085102592571150096L;
      l3 = l3 - 3690137345222373422L ^ 7978596441655123636L;
      l2 += 1393753992385309920L;
      l3 = l3 + -2005976113350654863L ^ 8425770575994478430L;
      l2 = l2 >>> 5 | l2 << 59;
      l3 = (l3 ^ 1618766122230202568L) - 7792655278219587767L;
      return l2 ^ 6148914691236517205L;
   }

}
