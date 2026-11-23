package net.augustus.vendor.crypto.keccak;


public class KeccakParameters {
   public static int STATE_WIDTH;
   public static int ROUND_COUNT;
   public static String DOMAIN_SUFFIX;

   public static int getCapacityBits(Sha3Algorithm sha3Algorithm) {
      int n = 0;
      n = KeccakAlgorithmSwitchMap.ORDINAL_MAP[sha3Algorithm.ordinal()];
      if (n == 1) {
         return 448;
      } else if (n == 2) {
         return 512;
      } else if (n == 3) {
         return 768;
      } else if (n == 4) {
         return 1024;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public static int getRateBits(Sha3Algorithm sha3Algorithm) {
      int n = 0;
      long l = 0L;
      l = -5525969849554931777L ^ (long)"珼롼ꄷ\udfe7䋛\ue75d蔕償᜴\uf745㕐䂰\udf55箧덩窧䓐8犦줂앹ꩪ啦ꅊ".hashCode();
      n = KeccakAlgorithmSwitchMap.ORDINAL_MAP[sha3Algorithm.ordinal()];
      l = (l ^ -7078928543693815019L) + -160973984534298442L;
      if (n == 3) {
         l = (l ^ 598653684419325246L) + 2330324409874189453L;
         return 832;
      } else {
         l = l + 9100923127086133489L ^ -446119034033361260L;
         if (n == 2) {
            l = l - -2575677185741057925L ^ 8781562368442900676L;
            return 1088;
         } else {
            l = l - -8770498117398578316L ^ 581842770198510711L;
            if (n == 4) {
               l = (l ^ -722804851909070563L) + -5413343540442628465L;
               return 576;
            } else {
               l = (l ^ 6312121599558235599L) + 5693516359153992705L;
               if (n == 1) {
                  l = (l ^ -1226269317177723841L) - -3450857505467753153L;
                  return 1152;
               } else {
                  l = l - -4265108990887449551L ^ 2198995889131080580L;
                  l = (l ^ 61900139378585993L) - -8930251788332471826L;
                  throw new IllegalArgumentException();
               }
            }
         }
      }
   }


}
