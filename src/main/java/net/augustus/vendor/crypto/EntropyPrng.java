package net.augustus.vendor.crypto;



public class EntropyPrng {
   private int[] indexTable;
   private long lastOutput;
   private final long[] mixMultipliers;
   private final int SALT_SIZE;
   private int stepCounter;
   private final int STATE_SIZE = 64;
   private final int INDEX_TABLE_SIZE = 256;
   private byte[] saltBytes;
   private long[] statePool;
   private int stateCursor;
   private final int[] shiftOffsets;

   public EntropyPrng() {
      this(2884292789534080718L);
   }

   public long nextLong(long l, char ch) {
      long l2 = 0L;
      long l3 = 0L;
      long l4 = 0L;
      int n = 0;
      int n2 = 0;
      long l5 = 0L;
      long l6 = 0L;
      int n3 = 0;
      long l7 = 0L;
      int n4 = 0;
      int n5 = 0;
      int n6 = 0;
      long l8 = 0L;
      long l9 = 0L;
      Object obj = null;
      long l10 = 0L;
      long l11 = 0L;
      int n7 = 0;
      Object obj2 = null;
      int n8 = 0;
      int n9 = 0;
      int n10 = 0;
      int n11 = 0;
      long l12 = 0L;
      int n12 = 0;
      int n13 = 0;
      long l13 = 0L;
      int n14 = 0;
      int n15 = 0;
      long l14 = 0L;
      l14 = 7848355469841473858L ^ (long)"꾚❋ﴒ迢⮣粊㠖异ㄆ刅❉㜩￬챻䡍\ude18\uf20aດ냷Ტ͏㌷䑩".hashCode();
      n15 = this.stateCursor;
      l14 = l14 - 1982452070520085705L ^ 8120180035110299824L;
      l12 = this.statePool[this.stateCursor];
      l14 = l14 + -8584144368023108047L ^ -5685015467538061343L;
      n12 = 0;
      l14 = l14 + 724035186742073754L ^ 4857179715380037443L;

      while (true) {
         n14 = this.stateCursor;
         l14 = l14 + 1483602327857793771L ^ -4479221995239278924L;
         n8 = (this.stateCursor + 1) % 64;
         l14 = l14 - -4930160929066472275L ^ -3557458300896912771L;
         n10 = this.indexTable[this.stepCounter % 256];
         l14 = l14 + 8957932009527989060L ^ -239550514935195672L;
         l13 = this.statePool[n14];
         l14 = (l14 ^ 679141037826763862L) + 2425763216498855531L;
         l7 = this.statePool[n8];
         l14 = (l14 ^ -465734760294344967L) - 8322240133137131462L;
         l11 = this.statePool[n10];
         l14 = (l14 ^ 3601746775110126104L) - 2587141790860568101L;
         l13 = (l13 << this.shiftOffsets[0] | l13 >>> 64 - this.shiftOffsets[0]) ^ l13 >>> this.shiftOffsets[1];
         l14 = l14 - -4596821403845380770L ^ -3440229428194625870L;
         l7 = (l7 << this.shiftOffsets[2] | l7 >>> 64 - this.shiftOffsets[2]) + (l7 >>> this.shiftOffsets[3]);
         l14 = (l14 ^ -3010043386782129879L) - 508490815281438456L;
         l11 = (l11 << this.shiftOffsets[4] | l11 >>> 64 - this.shiftOffsets[4]) ^ l11 >>> this.shiftOffsets[5];
         l14 = l14 + -4163645054363406940L ^ 1746850066600261188L;
         l3 = l13 ^ l7 ^ l11;
         l14 = (l14 ^ 2195798465786930775L) + 3797853387037522185L;
         l3 *= this.mixMultipliers[this.stepCounter % ((long[])this.mixMultipliers).length];
         l14 = l14 + 6016027312000306484L ^ 9182727496274269921L;
         l3 ^= l3 >>> this.shiftOffsets[6];
         l14 = l14 - -6087761634822423249L ^ 1820596981050390428L;
         this.statePool[n14] = l3;
         l14 = (l14 ^ 1582655799470521306L) - -3751524516453266870L;
         this.statePool[n10] = l13 + l7 + (long)(this.saltBytes[this.stepCounter % 32] & 255);
         l14 = l14 - -6184492022310706885L ^ 7218434669249062613L;
         this.stateCursor = (this.stateCursor + this.indexTable[this.stateCursor % 256] % 7 + 1) % 64;
         l14 = (l14 ^ -3194745135415762131L) + 3423018116030848293L;
         this.stepCounter++;
         l14 = l14 - -3934536566670043585L ^ -7423131329847808737L;
         n12++;
         l14 = (l14 ^ 405605698117451312L) - 3993907175028993260L;
         if (n12 <= 5) {
            l14 = (l14 ^ -5690370891621649867L) + -1741143049210838337L;
         } else {
            l14 = l14 + 7781333987318893483L ^ 9021903177220440467L;
            if ((this.statePool[this.stateCursor] & 31L) == 0L) {
               l14 = (l14 ^ -8202950065225319634L) - -7661791638615938914L;
               l14 = (l14 ^ 1968553079318807124L) - -3539173890627646401L;
               break;
            }

            l14 = (l14 ^ -1157956817157145696L) - -4185578125517098716L;
         }

         if ((this.statePool[this.stateCursor] & 7L) != 0L) {
            l14 = l14 - 969356567228692476L ^ -6463838195110109398L;
         } else {
            l14 = (l14 ^ 3700073600561868665L) - -204623860310280676L;
            n11 = 0;
            l14 = (l14 ^ 1292289861300708128L) - -3552126538173232664L;
            l4 = this.statePool[this.stateCursor];
            l14 = l14 + -4983499581159515348L ^ -3880814048339605343L;

            while (true) {
               n5 = this.stateCursor;
               l14 = l14 - -2033852377774849747L ^ -7338945607630815379L;
               n2 = (this.stateCursor + 1) % 64;
               l14 = l14 + -5152552367071598239L ^ -7868489296169780580L;
               n13 = this.indexTable[this.stepCounter % 256];
               l14 = l14 + -8424143414825096060L ^ 851894154184511833L;
               l8 = this.statePool[n5];
               l14 = (l14 ^ -1314864340747520005L) - -7347548717357190568L;
               l6 = this.statePool[n2];
               l14 = (l14 ^ 5811190428258844700L) + 4567899193689371245L;
               l5 = this.statePool[n13];
               l14 = l14 - -8106652777087328899L ^ -193498211569710328L;
               l8 = (l8 << this.shiftOffsets[0] | l8 >>> 64 - this.shiftOffsets[0]) ^ l8 >>> this.shiftOffsets[1];
               l14 = (l14 ^ 8254902559543181827L) - 2259927123831591791L;
               l6 = (l6 << this.shiftOffsets[2] | l6 >>> 64 - this.shiftOffsets[2]) + (l6 >>> this.shiftOffsets[3]);
               l14 = (l14 ^ -8158527403557567864L) - 1594188269667381674L;
               l5 = (l5 << this.shiftOffsets[4] | l5 >>> 64 - this.shiftOffsets[4]) ^ l5 >>> this.shiftOffsets[5];
               l14 = l14 - 7766044492143676268L ^ -3201798370098224506L;
               l9 = l8 ^ l6 ^ l5;
               l14 = (l14 ^ -2620458899743123374L) - -8497149519102235364L;
               l9 *= this.mixMultipliers[this.stepCounter % ((long[])this.mixMultipliers).length];
               l14 = l14 - -6318584511501175643L ^ 3682384569319978204L;
               l9 ^= l9 >>> this.shiftOffsets[6];
               l14 = (l14 ^ -1373667362765453281L) - -7841370446539081148L;
               this.statePool[n5] = l9;
               l14 = (l14 ^ -692385936289979374L) + -8662498997439783901L;
               this.statePool[n13] = l8 + l6 + (long)(this.saltBytes[this.stepCounter % 32] & 255);
               l14 = (l14 ^ 5297603671538956664L) - 7430857545064723149L;
               this.stateCursor = (this.stateCursor + this.indexTable[this.stateCursor % 256] % 7 + 1) % 64;
               l14 = l14 - -6407244225928730530L ^ 8039823918293487134L;
               this.stepCounter++;
               l14 = (l14 ^ -2063059960156355055L) - 5259103200151353874L;
               n11++;
               l14 = l14 - 3057121290629994831L ^ -7953731711586830594L;
               n12++;
               l14 = l14 + -116556714915341128L ^ -2923818492745002178L;
               if ((this.statePool[this.stateCursor] & 3L) != 0L) {
                  l14 = (l14 ^ 1070612535866552156L) - 2221655866534706745L;
               } else {
                  l14 = l14 + 8689138125622354877L ^ -5142820416267037755L;
                  n9 = this.indexTable[this.stateCursor * 5 % 256];
                  l14 = l14 - 912649773850257825L ^ 3101810436812296326L;
                  this.statePool[n9] = this.statePool[n9] ^ (this.statePool[this.stateCursor] << 11 | this.statePool[this.stateCursor] >>> 53);
                  l14 = l14 + 24039012638600632L ^ 3118838040767834104L;
               }

               if (n11 < 3) {
                  l14 = (l14 ^ -2174615874250882766L) - -2749748151270105549L;
               } else {
                  l14 = l14 - -809059925056057257L ^ -4580864260745863024L;
                  if ((this.statePool[this.stateCursor] & 15L) == (l4 & 15L)) {
                     l14 = (l14 ^ -4974069355393431850L) + -1821449867213257364L;
                     l14 = (l14 ^ -6947268454160060785L) + -4644230428964848554L;
                     break;
                  }

                  l14 = (l14 ^ -6300202920817997342L) + -6590784202733692348L;
               }

               if (n11 <= 2) {
                  l14 = l14 - 2168615998684465598L ^ -3612090425596932433L;
               } else {
                  l14 = (l14 ^ -551518300825688027L) + -5270452399707492747L;
                  if ((this.statePool[this.stateCursor] & 31L) == 31L) {
                     l14 = (l14 ^ -6771943181797650091L) - -7991980645147604217L;
                     l14 = l14 + 7519020156692836540L ^ 6951729434396251125L;
                     break;
                  }

                  l14 = (l14 ^ 5771003686616626568L) - -3311763841546024647L;
               }

               if (n11 > 10) {
                  l14 = (l14 ^ 5957740585046930539L) - -4449866787192292084L;
                  l14 = (l14 ^ -6371212771038291406L) + 7835924310373823759L;
                  break;
               }

               l14 = (l14 ^ 3573439117115721415L) - 945869677843400866L;
               l14 = l14 - -6123424443080640014L ^ -7997980353612667191L;
            }
         }

         if (n12 <= 15) {
            l14 = l14 + 2949983789876820971L ^ 7068680409333232229L;
         } else {
            l14 = l14 + 782856360691627360L ^ -9027161743673171152L;
            if ((this.statePool[this.stateCursor] & 15L) != 10L) {
               l14 = (l14 ^ 5255358119563447381L) - 9086588195001070207L;
            } else {
               l14 = l14 - -118119203640584063L ^ -3834405660796407405L;
               n11 = 0;
               l14 = (l14 ^ -7683452392815953590L) - -3427225843124597380L;

               while (true) {
                  if (n11 >= 5) {
                     l14 = l14 - 7540834150217445420L ^ -2321209787787496664L;
                     break;
                  }

                  l14 = (l14 ^ -5137718698102710486L) + -8319513390753102815L;
                  n7 = this.indexTable[(this.stateCursor + n11) % 256];
                  l14 = (l14 ^ 3171394246703921202L) + 772625085438335457L;
                  this.statePool[n7] = this.statePool[n7] ^ this.statePool[(n7 + 1) % 64];
                  l14 = l14 + -4023657259918144738L ^ 4073328933018087711L;
                  n11++;
                  l14 = (l14 ^ -2105415987991049854L) - -4001797038092171174L;
                  if ((this.statePool[n7] & 3L) != 3L) {
                     l14 = l14 - -8979265332897233307L ^ -8938221505384546070L;
                  } else {
                     l14 = l14 - 6067845654799811685L ^ -6529597018323261999L;
                     if (n11 > 2) {
                        l14 = (l14 ^ -500223440421074305L) + 1403538358210248056L;
                        l14 = l14 - -232982909959574062L ^ -579026352821450974L;
                        break;
                     }

                     l14 = (l14 ^ 8010657603902800978L) + 639133598318829271L;
                  }

                  l14 = (l14 ^ -4236681368311013747L) + 9056961325367662719L;
               }
            }
         }

         if (n12 <= 20) {
            l14 = l14 - 8851613548618653163L ^ -5523421641028758911L;
         } else {
            l14 = l14 - 6750154999227974958L ^ 6899101248873926754L;
            if (this.stateCursor == n15) {
               l14 = l14 + 7159555934127537462L ^ -5756013885015734389L;
               l14 = (l14 ^ -7468405388443267288L) - 8051732122840457616L;
               break;
            }

            l14 = (l14 ^ 8686643532914868389L) - 5579674576082000257L;
         }

         if (n12 <= 30) {
            l14 = (l14 ^ -5916592904445275526L) - -5997418251353692590L;
         } else {
            l14 = (l14 ^ 2715548840969788005L) - 3448808221227977459L;
            if ((this.statePool[this.stateCursor] & 255L) == (l12 & 255L)) {
               l14 = (l14 ^ 845127304354400127L) + 3633627152482319539L;
               l14 = l14 - 7471329989584314937L ^ 2639320043189234894L;
               break;
            }

            l14 = (l14 ^ -2405821507362995078L) + 3515914419302688984L;
         }

         if (n12 > 100) {
            l14 = (l14 ^ 4282952608037224714L) - -3568490670846294421L;
            l14 = (l14 ^ 2528992657719340120L) - -7091509995893064850L;
            break;
         }

         l14 = (l14 ^ 6286629771756759334L) - 2615894535690142169L;
         l14 = (l14 ^ 5396389016349778631L) + 7077262924089427471L;
      }

      obj = new byte[32];
      l14 = l14 + -6081827927995662778L ^ -7405124990800686212L;
      n8 = 0;
      l14 = (l14 ^ -4530321111068533936L) + 4403070724345037648L;

      while (n8 < ((byte[])obj).length) {
         l14 = (l14 ^ -3153806198192112770L) - 181269207674089106L;
         ((byte[])obj)[n8] = (byte)((int)(this.statePool[n8 % 64] & 255L));
         l14 = (l14 ^ -6012791404388551051L) - -885421072344036170L;
         n8++;
         l14 = (l14 ^ -3087134555119979931L) + -2717095998243083411L;
         l14 = (l14 ^ 5190040422602913824L) + -3574770557529947983L;
      }

      l14 = (l14 ^ -4822968432947352485L) + -5039451727989000087L;
      obj2 = (byte[])obj;
      l14 = l14 - 3752411603703419854L ^ -6653597658213757822L;
      l2 = 0L;
      l14 = l14 - -9197632918217738723L ^ 2691780985116114651L;
      n3 = 0;
      l14 = l14 - -1425395706779304095L ^ -2302251089034432694L;

      while (n3 < 8) {
         l14 = (l14 ^ 8780604783923017726L) - -8814027080120840379L;
         n = (this.stateCursor + n3 * 7) % 64;
         l14 = (l14 ^ -549256720326615377L) + 1705287214688477465L;
         l10 = this.statePool[n];
         l14 = (l14 ^ -7056166020699859727L) + -8773770618718523304L;
         n4 = n3 * 4 % ((byte[])obj2).length;
         l14 = l14 + 2956501253287977117L ^ 7195693281537690113L;
         n6 = ((byte[])obj2)[n4] & 255
            | (((byte[])obj2)[(n4 + 1) % ((byte[])obj2).length] & 255) << 8
            | (((byte[])obj2)[(n4 + 2) % ((byte[])obj2).length] & 255) << 16
            | (((byte[])obj2)[(n4 + 3) % ((byte[])obj2).length] & 255) << 24;
         l14 = l14 + -3005839525173411006L ^ 8100231744437385861L;
         l10 ^= (long)n6;
         l14 = l14 - 6776557010605259044L ^ -7250804612941596369L;
         l10 = l10 << 11 + n3 % 7 | l10 >>> 64 - (11 + n3 % 7);
         l14 = l14 + -3573833605297085466L ^ 1502257973712568385L;
         l10 *= this.mixMultipliers[n3 % ((long[])this.mixMultipliers).length];
         l14 = (l14 ^ -8418451617415753443L) - 5906969285430982180L;
         l10 ^= l10 >>> 7 + n3 % 11;
         l14 = l14 + -6572261543126364317L ^ 2077788286090268353L;
         l2 = (l2 << 5 + n3 % 8 | l2 >>> 64 - (5 + n3 % 8)) ^ l10;
         l14 = l14 + -3280979919624108122L ^ 3895775705402619010L;
         n3++;
         l14 = l14 - 6242826735413843993L ^ -5565211112874502792L;
         l14 = l14 + -6133321608984049838L ^ -28084253798470901L;
      }

      l14 = (l14 ^ -626502732440689285L) - -3780196964034390427L;
      l2 ^= l2 >>> 27;
      l14 = l14 + -1218599455942924065L ^ -5444355254213128458L;
      l2 *= 4357703544722667091L;
      l14 = (l14 ^ -6261321765522166773L) - 5406192245155019358L;
      l2 ^= l2 >>> 33;
      l14 = (l14 ^ -7215292968076561606L) + 2217284101480409441L;
      l2 *= 2047365380309298741L;
      l14 = l14 + 2599213944583809983L ^ 7676803172063187927L;
      l2 ^= l2 >>> 27;
      l14 = l14 - 355750279842654930L ^ -7196482736423296788L;
      this.lastOutput = l2;
      l14 = (l14 ^ 4886998950904790982L) - 3361584991105624457L;
      return l2;
   }

   public EntropyPrng(long l) {
      this.SALT_SIZE = 32;
      this.mixMultipliers = new long[]{
         -7046029254386353131L,
         -4658895280553007687L,
         -7723592293110705685L,
         -8163818279084223215L,
         -2643881736870682267L,
         -4908828816729415329L,
         4894412452774827399L,
         2564090487828481117L
      };
      this.shiftOffsets = new int[]{11, 23, 31, 37, 41, 13, 19, 29};
      this.statePool = new long[64];
      this.indexTable = new int[256];
      this.saltBytes = new byte[32];
      this.stateCursor = 0;
      this.stepCounter = 0;
      long l2 = l;

      for (int i = 0; i < 64; i++) {
         l2 ^= l2 >>> 12;
         l2 ^= l2 << 25;
         l2 ^= l2 >>> 27;
         this.statePool[i] = l2 * this.mixMultipliers[i % this.mixMultipliers.length];
         l2 = this.statePool[i];
      }

      for (int j = 0; j < 256; j++) {
         this.indexTable[j] = (int)Math.abs(l2 % 64L);
         l2 = l2 * 25214903917L + 11L & 281474976710655L;
      }

      for (int k = 0; k < 32; k++) {
         this.saltBytes[k] = (byte)((int)(l2 & 255L));
         l2 = l2 * 25214903917L + 11L & 281474976710655L;
      }

      for (int m = 0; m < 7; m++) {
         int stateCursor2 = this.stateCursor;
         int n = (this.stateCursor + 1) % 64;
         int n2 = this.indexTable[this.stepCounter % 256];
         long l3 = this.statePool[stateCursor2];
         long l4 = this.statePool[n];
         long l5 = this.statePool[n2];
         l3 = (l3 << this.shiftOffsets[0] | l3 >>> 64 - this.shiftOffsets[0]) ^ l3 >>> this.shiftOffsets[1];
         l4 = (l4 << this.shiftOffsets[2] | l4 >>> 64 - this.shiftOffsets[2]) + (l4 >>> this.shiftOffsets[3]);
         l5 = (l5 << this.shiftOffsets[4] | l5 >>> 64 - this.shiftOffsets[4]) ^ l5 >>> this.shiftOffsets[5];
         long l6 = l3 ^ l4 ^ l5;
         l6 *= this.mixMultipliers[this.stepCounter % this.mixMultipliers.length];
         l6 ^= l6 >>> this.shiftOffsets[6];
         this.statePool[stateCursor2] = l6;
         this.statePool[n2] = l3 + l4 + (long)(this.saltBytes[this.stepCounter % 32] & 255);
         this.stateCursor = (this.stateCursor + this.indexTable[this.stateCursor % 256] % 7 + 1) % 64;
         this.stepCounter++;
      }
   }

}
