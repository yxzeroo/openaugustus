package net.augustus.vendor.crypto.aes;



import net.augustus.vendor.crypto.VendorAesGcmGHashTable;
public class InlinedAesGcmCipher {
   private byte[] cachedKey;
   private static int IV_SIZE;
   private static int BLOCK_SIZE;
   private final int[] hashSubkeyStateBuffer = new int[0];
   private static final int[][] GF_MUL_TABLE = new int[16][256];
   private static final int[] SBOX = new int[]{
      99,
      124,
      119,
      123,
      242,
      107,
      111,
      197,
      48,
      1,
      103,
      43,
      254,
      215,
      171,
      118,
      202,
      130,
      201,
      125,
      250,
      89,
      71,
      240,
      173,
      212,
      162,
      175,
      156,
      164,
      114,
      192,
      183,
      253,
      147,
      38,
      54,
      63,
      247,
      204,
      52,
      165,
      229,
      241,
      113,
      216,
      49,
      21,
      4,
      199,
      35,
      195,
      24,
      150,
      5,
      154,
      7,
      18,
      128,
      226,
      235,
      39,
      178,
      117,
      9,
      131,
      44,
      26,
      27,
      110,
      90,
      160,
      82,
      59,
      214,
      179,
      41,
      227,
      47,
      132,
      83,
      209,
      0,
      237,
      32,
      252,
      177,
      91,
      106,
      203,
      190,
      57,
      74,
      76,
      88,
      207,
      208,
      239,
      170,
      251,
      67,
      77,
      51,
      133,
      69,
      249,
      2,
      127,
      80,
      60,
      159,
      168,
      81,
      163,
      64,
      143,
      146,
      157,
      56,
      245,
      188,
      182,
      218,
      33,
      16,
      255,
      243,
      210,
      205,
      12,
      19,
      236,
      95,
      151,
      68,
      23,
      196,
      167,
      126,
      61,
      100,
      93,
      25,
      115,
      96,
      129,
      79,
      220,
      34,
      42,
      144,
      136,
      70,
      238,
      184,
      20,
      222,
      94,
      11,
      219,
      224,
      50,
      58,
      10,
      73,
      6,
      36,
      92,
      194,
      211,
      172,
      98,
      145,
      149,
      228,
      121,
      231,
      200,
      55,
      109,
      141,
      213,
      78,
      169,
      108,
      86,
      244,
      234,
      101,
      122,
      174,
      8,
      186,
      120,
      37,
      46,
      28,
      166,
      180,
      198,
      232,
      221,
      116,
      31,
      75,
      189,
      139,
      138,
      112,
      62,
      181,
      102,
      72,
      3,
      246,
      14,
      97,
      53,
      87,
      185,
      134,
      193,
      29,
      158,
      225,
      248,
      152,
      17,
      105,
      217,
      142,
      148,
      155,
      30,
      135,
      233,
      206,
      85,
      40,
      223,
      140,
      161,
      137,
      13,
      191,
      230,
      66,
      104,
      65,
      153,
      45,
      15,
      176,
      84,
      187,
      22
   };
   private static int AES_128_KEY_SIZE;
   private final byte[] counterBlockByteBuffer;
   private static final int[] RCON = new int[]{0, 1, 2, 4, 8, 16, 32, 64, 128, 27, 54};
   private byte[] cachedHashSubkey;
   private static final int[] INV_SBOX = new int[]{
      82,
      9,
      106,
      213,
      48,
      54,
      165,
      56,
      191,
      64,
      163,
      158,
      129,
      243,
      215,
      251,
      124,
      227,
      57,
      130,
      155,
      47,
      255,
      135,
      52,
      142,
      67,
      68,
      196,
      222,
      233,
      203,
      84,
      123,
      148,
      50,
      166,
      194,
      35,
      61,
      238,
      76,
      149,
      11,
      66,
      250,
      195,
      78,
      8,
      46,
      161,
      102,
      40,
      217,
      36,
      178,
      118,
      91,
      162,
      73,
      109,
      139,
      209,
      37,
      114,
      248,
      246,
      100,
      134,
      104,
      152,
      22,
      212,
      164,
      92,
      204,
      93,
      101,
      182,
      146,
      108,
      112,
      72,
      80,
      253,
      237,
      185,
      218,
      94,
      21,
      70,
      87,
      167,
      141,
      157,
      132,
      144,
      216,
      171,
      0,
      140,
      188,
      211,
      10,
      247,
      228,
      88,
      5,
      184,
      179,
      69,
      6,
      208,
      44,
      30,
      143,
      202,
      63,
      15,
      2,
      193,
      175,
      189,
      3,
      1,
      19,
      138,
      107,
      58,
      145,
      17,
      65,
      79,
      103,
      220,
      234,
      151,
      242,
      207,
      206,
      240,
      180,
      230,
      115,
      150,
      172,
      116,
      34,
      231,
      173,
      53,
      133,
      226,
      249,
      55,
      232,
      28,
      117,
      223,
      110,
      71,
      241,
      26,
      113,
      29,
      41,
      197,
      137,
      111,
      183,
      98,
      14,
      170,
      24,
      190,
      27,
      252,
      86,
      62,
      75,
      198,
      210,
      121,
      32,
      154,
      219,
      192,
      254,
      120,
      205,
      90,
      244,
      31,
      221,
      168,
      51,
      136,
      7,
      199,
      49,
      177,
      18,
      16,
      89,
      39,
      128,
      236,
      95,
      96,
      81,
      127,
      169,
      25,
      181,
      74,
      13,
      45,
      229,
      122,
      159,
      147,
      201,
      156,
      239,
      160,
      224,
      59,
      77,
      174,
      42,
      245,
      176,
      200,
      235,
      187,
      60,
      131,
      83,
      153,
      97,
      23,
      43,
      4,
      126,
      186,
      119,
      214,
      38,
      225,
      105,
      20,
      99,
      85,
      33,
      12,
      125
   };
   private VendorAesGcmGHashTable ghashTable;
   private final int[] counterBlockStateBuffer = new int[0];
   private static int AES_256_KEY_SIZE;
   private int[] cachedRoundKeys;
   private static int TAG_SIZE;
   private final byte[] hashSubkeyByteBuffer = new byte[0];

   public byte[] decrypt(byte[] bytes, byte[] bytes2, byte[] bytes3) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      int n = 0;
      Object obj4 = null;
      long l = 0L;
      long l2 = 0L;
      int n2 = 0;
      Object obj5 = null;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      int[] ints = null;
      int n6 = 0;
      int n7 = 0;
      long l3 = 0L;
      Object obj6 = null;
      boolean flag = false;
      int n8 = 0;
      Object obj7 = null;
      int n9 = 0;
      int n10 = 0;
      int n11 = 0;
      int n12 = 0;
      int n13 = 0;
      int n14 = 0;
      Object obj8 = null;
      boolean flag2 = false;
      int n15 = 0;
      boolean flag3 = false;
      boolean flag4 = false;
      long l4 = 0L;
      Object obj9 = null;
      int n16 = 0;
      byte b = 0;
      Object obj10 = null;
      Object obj11 = null;
      int n17 = 0;
      long l5 = 0L;
      int n18 = 0;
      int n19 = 0;
      boolean flag5 = false;
      boolean flag6 = false;
      int n20 = 0;
      int n21 = 0;
      long l6 = 0L;
      Object obj12 = null;
      Object obj13 = null;
      int n22 = 0;
      int n23 = 0;
      int n24 = 0;
      int n25 = 0;
      long l7 = 0L;
      l7 = -6681222960306750297L ^ (long)"鵆馭⇉駐歵鄼濾䉗鼛쒔鞑豠掽둚鈑㺟싺去ꩴ뛇礭뗌钗䪎\ue921\u05f7\ue634놰".hashCode();
      if (bytes2.length == 16) {
         l7 = l7 - -5522329371713737589L ^ 6535497077280732435L;
      } else {
         l7 = l7 + -2859645460209772614L ^ -4277619890669893969L;
         if (bytes2.length == 24) {
            l7 = l7 - -6947202043068683971L ^ -6774927512457980510L;
         } else {
            l7 = (l7 ^ -2667541827628308545L) - 429172468572403677L;
            if (bytes2.length != 32) {
               l7 = l7 + -6234299561334700824L ^ -3670412884941229835L;
               throw new IllegalArgumentException("Invalid key size. Must be 16, 24, or 32 bytes");
            }

            l7 = (l7 ^ -258078410094663772L) + 1923598844057672473L;
         }
      }

      if (bytes.length < 28) {
         l7 = (l7 ^ -4317328457142054727L) + 8195653336093677093L;
         throw new IllegalArgumentException("Data too short for GCM mode");
      } else {
         l7 = (l7 ^ -5288560505181427013L) + -2039436982929919078L;
         obj10 = new byte[12];
         l7 = (l7 ^ 8365973081113101084L) + 6622792314180397420L;
         System.arraycopy(bytes, 0, (byte[])obj10, 0, ((byte[])obj10).length);
         l7 = (l7 ^ -7519369751425893468L) - 1681500369420363361L;
         n9 = bytes.length - ((byte[])obj10).length - 16;
         l7 = l7 - 8183925626426802957L ^ -6238076877699230460L;
         obj = new byte[n9];
         l7 = l7 - 2144199995044870194L ^ 19955804697606997L;
         System.arraycopy(bytes, ((byte[])obj10).length, (byte[])obj, 0, n9);
         l7 = (l7 ^ 7753648160467462078L) + -727674758480409409L;
         obj12 = new byte[16];
         l7 = (l7 ^ -7161379735057458028L) + -6400552412700374402L;
         System.arraycopy(bytes, ((byte[])obj10).length + n9, (byte[])obj12, 0, 16);
         l7 = (l7 ^ 4471232193738561163L) + -4668329392118812795L;
         obj3 = null;
         l7 = l7 - 2505551635284045731L ^ 6409632032211910628L;
         flag2 = false;
         l7 = (l7 ^ 8062846196542000999L) - -2668614743319726930L;
         if (this.cachedKey == null) {
            l7 = l7 - -3454177096314878837L ^ -8010358549673599246L;
         } else {
            l7 = l7 + -6372654938762220638L ^ 485212682230810804L;
            if (bytes2.length != this.cachedKey.length) {
               l7 = (l7 ^ -4141291429929201041L) + -2916760621807260676L;
            } else {
               l7 = (l7 ^ -7008088641235947521L) - -7168898171716275234L;
               b = 1;
               l7 = (l7 ^ 1611544049776185806L) + 7960437855935271217L;
               n24 = 0;
               l7 = (l7 ^ 4253823339025397005L) - -1625008111817316910L;

               exitBlock: {
                  while (n24 < ((byte[])bytes2).length) {
                     l7 = l7 + -4780961272428719108L ^ 5323507401661518406L;
                     if (bytes2[n24] != this.cachedKey[n24]) {
                        l7 = (l7 ^ 6860267282573267115L) + -2582641397884639394L;
                        b = 0;
                        l7 = l7 + 5824889828311561553L ^ -4827061417215097750L;
                        l7 = l7 - -6230949409497191741L ^ 1537232922741829695L;
                        break exitBlock;
                     }

                     l7 = l7 - -8901592896543845215L ^ -2843771221400567993L;
                     n24++;
                     l7 = l7 - 1628138979422715213L ^ 1258424851322622562L;
                     l7 = (l7 ^ -690403109052767965L) - 4268635260832308688L;
                  }

                  l7 = (l7 ^ -5984231646206840360L) + -133330562229434624L;
               }

               if (b == 0) {
                  l7 = (l7 ^ 320948624055267L) - 6524571126448048417L;
               } else {
                  l7 = l7 + -8154662556945320349L ^ -1155598244970681261L;
                  if (this.cachedRoundKeys == null) {
                     l7 = (l7 ^ 9165826355733011917L) - 8727787937296230491L;
                  } else {
                     l7 = l7 + 8082283584909961714L ^ -5382984141005708404L;
                     ints = this.cachedRoundKeys;
                     l7 = (l7 ^ 3898073975629451025L) - -1761051272175584723L;
                     flag2 = true;
                     l7 = l7 + -910423704335449678L ^ -309700442508629242L;
                  }
               }
            }
         }

         if (flag2) {
            l7 = l7 - 937867991918509524L ^ 535143653081483892L;
         } else {
            l7 = (l7 ^ -2677489368240625791L) + 3939106613839006077L;
            b = 0;
            l7 = l7 + 3137235947986666753L ^ -589728853825679660L;
            if (bytes2.length != 16) {
               l7 = (l7 ^ 6803839595547202465L) - 5512607854004491306L;
               if (bytes2.length != 24) {
                  l7 = l7 + -324144709030949409L ^ 7017896090145156458L;
                  b = 14;
                  l7 = l7 + 6629007122650473656L ^ -533565882917298909L;
               } else {
                  l7 = (l7 ^ -81508050426875245L) + -8380268013914882857L;
                  b = 12;
                  l7 = (l7 ^ 7129566454931480679L) - 184423800159382610L;
                  l7 = (l7 ^ -7986239664797608550L) + -5658136616735050951L;
               }
            } else {
               l7 = (l7 ^ 3301942826689716729L) - -8689194316635942589L;
               b = 10;
               l7 = (l7 ^ 5853708272419142437L) - -1830904668419824468L;
               l7 = (l7 ^ 5763636896442285612L) + -5020584772936012564L;
            }

            n24 = bytes2.length / 4;
            l7 = l7 - 6563852966751610918L ^ -6984999864160541562L;
            n4 = 4 * (b + 1);
            l7 = (l7 ^ -6484961545368192746L) + -8585866094297566311L;
            obj4 = new int[n4];
            l7 = l7 + -5073974569374398709L ^ -4989410568350884067L;
            n20 = 0;
            l7 = (l7 ^ -3997511090649788672L) - 2746179960362109363L;

            while (n20 < n24) {
               l7 = (l7 ^ -5416341235815969125L) - 3608880974080284753L;
               ((int[])obj4)[n20] = (bytes2[4 * n20] & 255) << 24
                  | (bytes2[4 * n20 + 1] & 255) << 16
                  | (bytes2[4 * n20 + 2] & 255) << 8
                  | bytes2[4 * n20 + 3] & 255;
               l7 = (l7 ^ 4981992783106434519L) - -2356168578789037229L;
               n20++;
               l7 = l7 + 3137988206514671643L ^ 452850318473096176L;
               l7 = (l7 ^ 1492375183893920041L) - -387229734396210340L;
            }

            l7 = (l7 ^ 5926477805055056536L) + -1772775248724016270L;
            n20 = n24;
            l7 = (l7 ^ 1348958155905047427L) + 4113672569516989975L;

            while (n20 < n4) {
               l7 = (l7 ^ -5357986184349797126L) - -4012400028358129462L;
               n10 = ((int[])obj4)[n20 - 1];
               l7 = l7 + -1490685393263305422L ^ 1393788277926457065L;
               if (n20 % n24 != 0) {
                  l7 = (l7 ^ 3289163755686663348L) - 2514889058182185661L;
                  if (n24 <= 6) {
                     l7 = (l7 ^ -2193826330738103850L) - -4415288172889451375L;
                  } else {
                     l7 = (l7 ^ 6824807896894624828L) + 3826542354144754497L;
                     if (n20 % n24 != 4) {
                        l7 = l7 - -8107295173776319219L ^ 2100435814735444001L;
                     } else {
                        l7 = l7 - 5378989056399838399L ^ -9157569301928314548L;
                        n10 = SBOX[n10 >>> 24 & 0xFF] << 24 | SBOX[n10 >>> 16 & 0xFF] << 16 | SBOX[n10 >>> 8 & 0xFF] << 8 | SBOX[n10 & 0xFF];
                        l7 = l7 + 8967035908001730168L ^ -2737887534850068441L;
                     }
                  }
               } else {
                  l7 = (l7 ^ 2990954451913451795L) - 3528622165301082355L;
                  n10 = n10 << 8 & -256 | n10 >>> 24 & 0xFF;
                  l7 = l7 - -2567256291535985889L ^ -3006942062527291111L;
                  n10 = SBOX[n10 >>> 24 & 0xFF] << 24 | SBOX[n10 >>> 16 & 0xFF] << 16 | SBOX[n10 >>> 8 & 0xFF] << 8 | SBOX[n10 & 0xFF];
                  l7 = (l7 ^ 985572244648574065L) - 8190315882932548552L;
                  n10 ^= RCON[n20 / n24] << 24;
                  l7 = (l7 ^ -8998291808194891731L) + -4895585354738707809L;
                  l7 = (l7 ^ 2789375895394204530L) - -8090012176682504171L;
               }

               ((int[])obj4)[n20] = ((int[])obj4)[n20 - n24] ^ n10;
               l7 = l7 + 8567861066388097069L ^ 2544678234802823081L;
               n20++;
               l7 = l7 + -2708177343451113850L ^ 533358091873960614L;
               l7 = l7 + -6026032494239414180L ^ -8220360208577280571L;
            }

            l7 = l7 + -1989032703363279397L ^ 5549764611153104956L;
            obj9 = new int[n4 * 4];
            l7 = l7 + -7317530275931972246L ^ -1702121429277965537L;
            n10 = 0;
            l7 = (l7 ^ -5669952828363632750L) + -9126744028818680125L;

            while (n10 < n4) {
               l7 = l7 - -2892199220534190164L ^ 5197574526827741717L;
               ((int[])obj9)[4 * n10] = ((int[])obj4)[n10] >>> 24 & 0xFF;
               l7 = (l7 ^ 8526760239573382543L) - -5959958243909377130L;
               ((int[])obj9)[4 * n10 + 1] = ((int[])obj4)[n10] >>> 16 & 0xFF;
               l7 = l7 + -6324589794369342434L ^ 6390165674343785369L;
               ((int[])obj9)[4 * n10 + 2] = ((int[])obj4)[n10] >>> 8 & 0xFF;
               l7 = l7 - 2111625061504705851L ^ 4980558163703604955L;
               ((int[])obj9)[4 * n10 + 3] = ((int[])obj4)[n10] & 0xFF;
               l7 = (l7 ^ 9022816663875422823L) + -4344142734506178446L;
               n10++;
               l7 = l7 + -2601624077119930218L ^ 3176513411242691555L;
               l7 = (l7 ^ 1443979337385235649L) - -8468176988603990260L;
            }

            l7 = l7 + 242006424179960793L ^ 471992066448130837L;
            this.cachedKey = new byte[((byte[])bytes2).length];
            l7 = l7 - 3351155482349906568L ^ -6015258368277053976L;
            System.arraycopy(bytes2, 0, this.cachedKey, 0, bytes2.length);
            l7 = (l7 ^ 6062138146133653181L) + 377320546214343980L;
            this.cachedRoundKeys = (int[])obj9;
            l7 = l7 + -2157840190603222648L ^ 8714820059482020238L;
            ints = (int[])obj9;
            l7 = (l7 ^ -5580439224818548241L) + -518969432178999939L;
         }

         obj13 = this.hashSubkeyByteBuffer;
         l7 = (l7 ^ -1502938421949894137L) - 7216922185929946283L;
         boolean flag7 = false;
         l7 = (l7 ^ 1400802425844594075L) - 6169627899641839032L;
         if (this.cachedKey == null) {
            l7 = l7 - 5456576102706855607L ^ -8143025958811667228L;
         } else {
            l7 = (l7 ^ -8111444490601424025L) + 3283213214351517482L;
            if (this.cachedHashSubkey == null) {
               l7 = (l7 ^ 6617018122948400613L) + 3731812600820609401L;
            } else {
               l7 = l7 - -8110240617302499121L ^ -3416291359079184624L;
               boolean flag8 = true;
               l7 = l7 + 4632209916239252892L ^ -651455545681213594L;
               n16 = 0;
               l7 = l7 + 8600870065164948568L ^ -4737560317489227825L;

               exitBlock2: {
                  while (n16 < ((byte[])bytes2).length) {
                     l7 = l7 + -4015774690438949593L ^ -202419680427718958L;
                     if (bytes2[n16] != this.cachedKey[n16]) {
                        l7 = (l7 ^ 6924855580608880502L) - -7175584933739886299L;
                        flag8 = false;
                        l7 = (l7 ^ -4351279435474547089L) - -3759052429132387670L;
                        l7 = (l7 ^ 2509598898064063388L) + -4029533187596097189L;
                        break exitBlock2;
                     }

                     l7 = l7 - -4962938070338890787L ^ 7178270519918894482L;
                     n16++;
                     l7 = l7 - 757630182605517365L ^ 5563833129526787278L;
                     l7 = (l7 ^ -8921599538083336352L) + -4048785071956046019L;
                  }

                  l7 = (l7 ^ 625087164959325835L) - 3894924517592389437L;
               }

               if (!flag8) {
                  l7 = l7 + 4165314231204767021L ^ -2078250129559720647L;
               } else {
                  l7 = (l7 ^ 6338245256071958908L) + -3117453589896932243L;
                  obj13 = this.cachedHashSubkey;
                  l7 = l7 - 6021219448078826779L ^ -6145298364446186867L;
                  flag7 = true;
                  l7 = (l7 ^ -2629351050415913311L) - 637627308872505430L;
               }
            }
         }

         if (flag7) {
            l7 = l7 - 3897537388015231574L ^ 1582195243787028050L;
         } else {
            l7 = l7 + -291982315979205069L ^ -2344404053973531621L;
            n4 = 0;
            l7 = l7 - -698290616188130623L ^ -5264720486735780750L;

            while (n4 < 16) {
               l7 = l7 + -1607796391071140391L ^ -7419932794681691961L;
               this.hashSubkeyStateBuffer[n4] = 0;
               l7 = (l7 ^ 3191854014890720744L) + -7967049742305529590L;
               n4++;
               l7 = (l7 ^ 3712509000952189007L) - -8460878799819378546L;
               l7 = l7 + -9028491543698002584L ^ 6597428723254309247L;
            }

            l7 = l7 + 3993250349928672244L ^ 8608707338932712042L;
            n4 = ints.length / 16 - 1;
            l7 = (l7 ^ -5982161390540634068L) - 8479965553538240797L;
            n16 = 0;
            l7 = l7 - -4616874983761151765L ^ 4012695286168754474L;

            while (n16 < 16) {
               l7 = (l7 ^ -260571622486614675L) - 7203472724625591042L;
               this.hashSubkeyStateBuffer[n16] = this.hashSubkeyStateBuffer[n16] ^ ints[n16];
               l7 = l7 - -6776833114381815454L ^ -8011252974863801167L;
               n16++;
               l7 = (l7 ^ -7483340037407469609L) + -625295301811438784L;
               l7 = (l7 ^ 2907970355323485200L) - 7816159123639792777L;
            }

            l7 = l7 + -7326414444644153645L ^ -3292432945655003403L;
            n16 = 1;
            l7 = l7 - 8770828359970430238L ^ 7391161257313052863L;

            while (n16 < n4) {
               l7 = l7 - -3305122870235573910L ^ -6037165284365006320L;
               n20 = 0;
               l7 = l7 - -3349241480288140106L ^ 2611072675193089558L;

               while (n20 < 16) {
                  l7 = l7 - 5169205029771693124L ^ -7297343272793308522L;
                  this.hashSubkeyStateBuffer[n20] = SBOX[this.hashSubkeyStateBuffer[n20]];
                  l7 = l7 + -1633515935021779430L ^ 8000700939554573208L;
                  n20++;
                  l7 = l7 + 2270464048599974032L ^ -893257861903776027L;
                  l7 = l7 + -8315133125129740485L ^ 712468291278126620L;
               }

               l7 = (l7 ^ -6552308456866122070L) + 4655026852263739896L;
               n20 = this.hashSubkeyStateBuffer[1];
               l7 = l7 - 3105267895970182139L ^ -6598684938637204369L;
               this.hashSubkeyStateBuffer[1] = this.hashSubkeyStateBuffer[5];
               l7 = l7 - -6482998986275314159L ^ -5612000131276192674L;
               this.hashSubkeyStateBuffer[5] = this.hashSubkeyStateBuffer[9];
               l7 = (l7 ^ 4338423395071423729L) - 7441543378267028798L;
               this.hashSubkeyStateBuffer[9] = this.hashSubkeyStateBuffer[13];
               l7 = (l7 ^ -949932466055794342L) - -8626926471536931499L;
               this.hashSubkeyStateBuffer[13] = n20;
               l7 = l7 - -8772631722988209601L ^ 3938308200885671287L;
               n20 = this.hashSubkeyStateBuffer[2];
               l7 = l7 - 4651550263459465105L ^ -6006696220953184128L;
               n10 = this.hashSubkeyStateBuffer[6];
               l7 = (l7 ^ 5143157742598923369L) - -897984877416936283L;
               this.hashSubkeyStateBuffer[2] = this.hashSubkeyStateBuffer[10];
               l7 = l7 + -4295406286750051539L ^ 7990955700552858999L;
               this.hashSubkeyStateBuffer[6] = this.hashSubkeyStateBuffer[14];
               l7 = l7 - -8065535128070306085L ^ 6084098308077620130L;
               this.hashSubkeyStateBuffer[10] = n20;
               l7 = (l7 ^ -2107273153081243599L) + -6877898289123090244L;
               this.hashSubkeyStateBuffer[14] = n10;
               l7 = l7 - 7518792573819521038L ^ 7258975061061410345L;
               n20 = this.hashSubkeyStateBuffer[15];
               l7 = (l7 ^ -6624554286272949720L) - 3781070130049599669L;
               this.hashSubkeyStateBuffer[15] = this.hashSubkeyStateBuffer[11];
               l7 = (l7 ^ -7401836335819145469L) + -3309874978631672698L;
               this.hashSubkeyStateBuffer[11] = this.hashSubkeyStateBuffer[7];
               l7 = (l7 ^ -2477812203769086732L) - -6641219722107779389L;
               this.hashSubkeyStateBuffer[7] = this.hashSubkeyStateBuffer[3];
               l7 = (l7 ^ -1510065472297195687L) - -7916248667402862374L;
               this.hashSubkeyStateBuffer[3] = n20;
               l7 = l7 + -7332549238624260467L ^ -8894765432816643951L;
               n6 = 0;
               l7 = (l7 ^ 6761547260253548488L) - -2952343896949677060L;

               while (n6 < 4) {
                  l7 = l7 - -3315358782424528608L ^ 7388124893737021436L;
                  n20 = this.hashSubkeyStateBuffer[n6 * 4];
                  l7 = (l7 ^ 7402097582851054776L) + -8691395613929789080L;
                  n10 = this.hashSubkeyStateBuffer[n6 * 4 + 1];
                  l7 = l7 - -3985700316709131373L ^ 8502802084184797798L;
                  n12 = this.hashSubkeyStateBuffer[n6 * 4 + 2];
                  l7 = (l7 ^ -8934120333964011227L) - -5862197343659877858L;
                  n2 = this.hashSubkeyStateBuffer[n6 * 4 + 3];
                  l7 = l7 + -3766267780192491240L ^ 7554464949690278838L;
                  this.hashSubkeyStateBuffer[n6 * 4] = GF_MUL_TABLE[2][n20] ^ GF_MUL_TABLE[3][n10] ^ n12 ^ n2;
                  l7 = l7 - 6083035440910001389L ^ 4686012022219742771L;
                  this.hashSubkeyStateBuffer[n6 * 4 + 1] = n20 ^ GF_MUL_TABLE[2][n10] ^ GF_MUL_TABLE[3][n12] ^ n2;
                  l7 = (l7 ^ 3232100012547056932L) - 3779915842493448303L;
                  this.hashSubkeyStateBuffer[n6 * 4 + 2] = n20 ^ n10 ^ GF_MUL_TABLE[2][n12] ^ GF_MUL_TABLE[3][n2];
                  l7 = (l7 ^ 879407579613692415L) + -8733256287838275511L;
                  this.hashSubkeyStateBuffer[n6 * 4 + 3] = GF_MUL_TABLE[3][n20] ^ n10 ^ n12 ^ GF_MUL_TABLE[2][n2];
                  l7 = (l7 ^ 383745625141194591L) - -3816454490696132951L;
                  n6++;
                  l7 = (l7 ^ -2019189062202570702L) - 6060381680831047574L;
                  l7 = (l7 ^ 799032770662046553L) + -8741465078247472166L;
               }

               l7 = l7 - 2191257731676970969L ^ -1404912785295700995L;
               n6 = n16 * 16;
               l7 = l7 + -3587660071731562626L ^ 269732300863493481L;
               n = 0;
               l7 = (l7 ^ 6701641076895572038L) + 6064156310720797699L;

               while (n < 16) {
                  l7 = l7 - -5541130113553237929L ^ 5937949511340214237L;
                  this.hashSubkeyStateBuffer[n] = this.hashSubkeyStateBuffer[n] ^ ints[n6 + n];
                  l7 = l7 + -3201228750121846901L ^ -6651721332320745143L;
                  n++;
                  l7 = l7 + 6370864779852833269L ^ 6775029658620987093L;
                  l7 = l7 + 5068180263314981186L ^ 1826015057215252590L;
               }

               l7 = (l7 ^ 4975399354497371947L) + -6328633421784806523L;
               n16++;
               l7 = (l7 ^ 5224441682035150882L) + -1311799144685653426L;
               l7 = l7 - 3391887873977696144L ^ -6229427385151900539L;
            }

            l7 = l7 - 6353744531643629560L ^ 8127332904239637273L;
            n16 = 0;
            l7 = l7 - 7807197592250671096L ^ 1970598297537708371L;

            while (n16 < 16) {
               l7 = (l7 ^ -1351069606966425580L) + 3544155432992333173L;
               this.hashSubkeyStateBuffer[n16] = SBOX[this.hashSubkeyStateBuffer[n16]];
               l7 = (l7 ^ 7632781981366735891L) - -3974370208414947794L;
               n16++;
               l7 = (l7 ^ 1325908602878007661L) - -8227920164916789675L;
               l7 = l7 + 8448031412487386434L ^ 7011752690543669934L;
            }

            l7 = (l7 ^ 4395194768517646177L) - 3927877638807865092L;
            n16 = this.hashSubkeyStateBuffer[1];
            l7 = (l7 ^ -8274636948772898456L) + -5919781718178086697L;
            this.hashSubkeyStateBuffer[1] = this.hashSubkeyStateBuffer[5];
            l7 = (l7 ^ 8421999635797479844L) - -491033042131321403L;
            this.hashSubkeyStateBuffer[5] = this.hashSubkeyStateBuffer[9];
            l7 = (l7 ^ 5063911586405680571L) - 3758890748942442946L;
            this.hashSubkeyStateBuffer[9] = this.hashSubkeyStateBuffer[13];
            l7 = l7 + -646719487907212372L ^ -4760264059932251716L;
            this.hashSubkeyStateBuffer[13] = n16;
            l7 = (l7 ^ -7151195821414443550L) - -213039083513883108L;
            n16 = this.hashSubkeyStateBuffer[2];
            l7 = (l7 ^ 487458638953269362L) - 7591828360375490711L;
            n20 = this.hashSubkeyStateBuffer[6];
            l7 = l7 + -8747386764965319811L ^ -6346597122663607067L;
            this.hashSubkeyStateBuffer[2] = this.hashSubkeyStateBuffer[10];
            l7 = l7 + -3436784502662405980L ^ -468834445217411930L;
            this.hashSubkeyStateBuffer[6] = this.hashSubkeyStateBuffer[14];
            l7 = (l7 ^ -5813229968933706810L) + 1889254005383954237L;
            this.hashSubkeyStateBuffer[10] = n16;
            l7 = l7 + -319369798054338583L ^ 2731214889282814672L;
            this.hashSubkeyStateBuffer[14] = n20;
            l7 = (l7 ^ -7700660033084557293L) - 7574226260133337381L;
            n16 = this.hashSubkeyStateBuffer[15];
            l7 = (l7 ^ 4510530189213613550L) - -1128943121224509341L;
            this.hashSubkeyStateBuffer[15] = this.hashSubkeyStateBuffer[11];
            l7 = (l7 ^ 3907105409461139714L) + 6048369781206504159L;
            this.hashSubkeyStateBuffer[11] = this.hashSubkeyStateBuffer[7];
            l7 = l7 - -4918910887633482823L ^ -5346685996041913834L;
            this.hashSubkeyStateBuffer[7] = this.hashSubkeyStateBuffer[3];
            l7 = l7 - -5753273371791780511L ^ 8101726869871539107L;
            this.hashSubkeyStateBuffer[3] = n16;
            l7 = l7 + 4290932486240819099L ^ -2911142839279561236L;
            n10 = n4 * 16;
            l7 = (l7 ^ 8706973743892251345L) + 2981015390004735469L;
            n12 = 0;
            l7 = (l7 ^ -7371705153264213867L) + -2619141949958378710L;

            while (n12 < 16) {
               l7 = (l7 ^ -5203640142050761198L) - -2294000836693011017L;
               this.hashSubkeyStateBuffer[n12] = this.hashSubkeyStateBuffer[n12] ^ ints[n10 + n12];
               l7 = l7 - 7191549256076468977L ^ -4593070403554879740L;
               n12++;
               l7 = l7 - 8740880998018872139L ^ 5836987516156954121L;
               l7 = (l7 ^ -8625870223332232314L) - -6129896274858561636L;
            }

            l7 = (l7 ^ -8147247804818721171L) + 6720905854889993042L;
            n12 = 0;
            l7 = (l7 ^ 5397155408160934141L) + -410729957954174526L;

            while (n12 < 16) {
               l7 = (l7 ^ 7404024870412261495L) - 3166715243317115655L;
               this.hashSubkeyByteBuffer[n12] = (byte)this.hashSubkeyStateBuffer[n12];
               l7 = (l7 ^ 6485587824099578623L) - 3421299429506212953L;
               n12++;
               l7 = l7 + 1879255913664562630L ^ 9190348926999316566L;
               l7 = l7 - -7664438660349397979L ^ -5817499986896527355L;
            }

            l7 = l7 + 3307060117814749238L ^ -959939407729688257L;
            if (this.cachedHashSubkey != null) {
               l7 = l7 - 5437540543832999479L ^ 7319952302217054634L;
            } else {
               l7 = l7 + 430920944327753806L ^ 1763050948353828870L;
               this.cachedHashSubkey = new byte[16];
               l7 = l7 + 8064326728522927010L ^ 174483266815836099L;
            }

            System.arraycopy(this.hashSubkeyByteBuffer, 0, this.cachedHashSubkey, 0, 16);
            l7 = l7 - -3998874568896753966L ^ -8719788588637320560L;
         }

         int n26;
         if (bytes3 == null) {
            l7 = l7 + 1888427978863114074L ^ -3567731305159783223L;
            n26 = 0;
         } else {
            l7 = l7 - 7509596125152935790L ^ 5611868199199540721L;
            n26 = bytes3.length;
         }

         exitBlock3: {
            n4 = n26;
            if (this.ghashTable != null) {
               byte[] bytes4 = (byte[])obj13;
               if (this.ghashTable.matchesSubkey(bytes4)) {
                  break exitBlock3;
               }
            }

            this.ghashTable = new VendorAesGcmGHashTable((byte[])obj13);
         }

         obj4 = new byte[16];
         if (bytes3 != null && n4 > 0) {
            n20 = (n4 + 16 - 1) / 16;

            for (int i = 0; i < n20; i++) {
               n12 = Math.min(16, n4 - i * 16);

               for (int j = 0; j < n12; j++) {
                  ((byte[])obj4)[j] = (byte)(((byte[])obj4)[j] ^ bytes3[i * 16 + j]);
               }

               l = 0L;
               l2 = 0L;

               for (int k = 0; k < 16; k++) {
                  n17 = ((byte[])obj4)[k] & 255;
                  if (n17 != 0) {
                     l6 = this.ghashTable.mulTable[k][n17];
                     l ^= l6 >>> 32;
                     l2 ^= l6 & 4294967295L;
                  }
               }

               for (int m = 0; m < 8; m++) {
                  ((byte[])obj4)[m] = (byte)((int)(l >>> 56 - m * 8 & 255L));
                  ((byte[])obj4)[m + 8] = (byte)((int)(l2 >>> 56 - m * 8 & 255L));
               }
            }
         }

         if (((byte[])obj).length > 0) {
            n20 = (((byte[])obj).length + 16 - 1) / 16;

            for (int idx = 0; idx < n20; idx++) {
               short s = 30770;
               n12 = Math.min(16, ((byte[])obj).length - idx * 16);

               for (int index = 0; index < n12; index++) {
                  ((byte[])obj4)[index] = (byte)(((byte[])obj4)[index] ^ ((byte[])obj)[idx * 16 + index]);
               }

               l = 0L;
               l2 = 0L;

               for (int i2 = 0; i2 < 16; i2++) {
                  n17 = ((byte[])obj4)[i2] & 255;
                  if (n17 != 0) {
                     l6 = this.ghashTable.mulTable[i2][n17];
                     l ^= l6 >>> 32;
                     l2 ^= l6 & 4294967295L;
                  }
               }

               for (int i3 = 0; i3 < 8; i3++) {
                  ((byte[])obj4)[i3] = (byte)((int)(l >>> 56 - i3 * 8 & 255L));
                  ((byte[])obj4)[i3 + 8] = (byte)((int)(l2 >>> 56 - i3 * 8 & 255L));
               }
            }
         }

         obj9 = new byte[16];
         short s2 = 17510;
         l3 = (long)n4 * 8L;
         l = (long)((byte[])obj).length * 8L;

         for (int i4 = 7; i4 >= 0; i4--) {
            ((byte[])obj9)[i4] = (byte)((int)(l3 & 255L));
            l3 >>>= 8;
            ((byte[])obj9)[i4 + 8] = (byte)((int)(l & 255L));
            l >>>= 8;
         }

         for (int i5 = 0; i5 < 16; i5++) {
            ((byte[])obj4)[i5] = (byte)(((byte[])obj4)[i5] ^ ((byte[])obj9)[i5]);
         }

         l2 = 0L;
         l4 = 0L;

         for (int i6 = 0; i6 < 16; i6++) {
            n18 = ((byte[])obj4)[i6] & 255;
            if (n18 != 0) {
               l5 = this.ghashTable.mulTable[i6][n18];
               l2 ^= l5 >>> 32;
               l4 ^= l5 & 4294967295L;
            }
         }

         for (int i7 = 0; i7 < 8; i7++) {
            ((byte[])obj4)[i7] = (byte)((int)(l2 >>> 56 - i7 * 8 & 255L));
            ((byte[])obj4)[i7 + 8] = (byte)((int)(l4 >>> 56 - i7 * 8 & 255L));
         }

         obj7 = new byte[16];
         System.arraycopy((byte[])obj10, 0, (byte[])obj7, 0, ((byte[])obj10).length);
         ((byte[])obj7)[15] = 1;

         for (int i8 = 0; i8 < 16; i8++) {
            this.counterBlockStateBuffer[i8] = ((byte[])obj7)[i8] & 255;
         }

         n18 = ints.length / 16 - 1;

         for (int i9 = 0; i9 < 16; i9++) {
            this.counterBlockStateBuffer[i9] = this.counterBlockStateBuffer[i9] ^ ints[i9];
         }

         for (int i10 = 1; i10 < n18; i10++) {
            for (int i11 = 0; i11 < 16; i11++) {
               this.counterBlockStateBuffer[i11] = SBOX[this.counterBlockStateBuffer[i11]];
            }

            n7 = this.counterBlockStateBuffer[1];
            this.counterBlockStateBuffer[1] = this.counterBlockStateBuffer[5];
            this.counterBlockStateBuffer[5] = this.counterBlockStateBuffer[9];
            this.counterBlockStateBuffer[9] = this.counterBlockStateBuffer[13];
            this.counterBlockStateBuffer[13] = n7;
            n7 = this.counterBlockStateBuffer[2];
            n13 = this.counterBlockStateBuffer[6];
            this.counterBlockStateBuffer[2] = this.counterBlockStateBuffer[10];
            this.counterBlockStateBuffer[6] = this.counterBlockStateBuffer[14];
            this.counterBlockStateBuffer[10] = n7;
            this.counterBlockStateBuffer[14] = n13;
            n7 = this.counterBlockStateBuffer[15];
            this.counterBlockStateBuffer[15] = this.counterBlockStateBuffer[11];
            this.counterBlockStateBuffer[11] = this.counterBlockStateBuffer[7];
            this.counterBlockStateBuffer[7] = this.counterBlockStateBuffer[3];
            this.counterBlockStateBuffer[3] = n7;

            for (int i12 = 0; i12 < 4; i12++) {
               n7 = this.counterBlockStateBuffer[i12 * 4];
               n13 = this.counterBlockStateBuffer[i12 * 4 + 1];
               n19 = this.counterBlockStateBuffer[i12 * 4 + 2];
               n25 = this.counterBlockStateBuffer[i12 * 4 + 3];
               this.counterBlockStateBuffer[i12 * 4] = GF_MUL_TABLE[2][n7] ^ GF_MUL_TABLE[3][n13] ^ n19 ^ n25;
               this.counterBlockStateBuffer[i12 * 4 + 1] = n7 ^ GF_MUL_TABLE[2][n13] ^ GF_MUL_TABLE[3][n19] ^ n25;
               this.counterBlockStateBuffer[i12 * 4 + 2] = n7 ^ n13 ^ GF_MUL_TABLE[2][n19] ^ GF_MUL_TABLE[3][n25];
               this.counterBlockStateBuffer[i12 * 4 + 3] = GF_MUL_TABLE[3][n7] ^ n13 ^ n19 ^ GF_MUL_TABLE[2][n25];
            }

            n15 = i10 * 16;

            for (int i13 = 0; i13 < 16; i13++) {
               this.counterBlockStateBuffer[i13] = this.counterBlockStateBuffer[i13] ^ ints[n15 + i13];
            }
         }

         for (int i14 = 0; i14 < 16; i14++) {
            this.counterBlockStateBuffer[i14] = SBOX[this.counterBlockStateBuffer[i14]];
         }

         n11 = this.counterBlockStateBuffer[1];
         this.counterBlockStateBuffer[1] = this.counterBlockStateBuffer[5];
         this.counterBlockStateBuffer[5] = this.counterBlockStateBuffer[9];
         this.counterBlockStateBuffer[9] = this.counterBlockStateBuffer[13];
         this.counterBlockStateBuffer[13] = n11;
         n11 = this.counterBlockStateBuffer[2];
         n7 = this.counterBlockStateBuffer[6];
         this.counterBlockStateBuffer[2] = this.counterBlockStateBuffer[10];
         this.counterBlockStateBuffer[6] = this.counterBlockStateBuffer[14];
         this.counterBlockStateBuffer[10] = n11;
         this.counterBlockStateBuffer[14] = n7;
         n11 = this.counterBlockStateBuffer[15];
         this.counterBlockStateBuffer[15] = this.counterBlockStateBuffer[11];
         this.counterBlockStateBuffer[11] = this.counterBlockStateBuffer[7];
         this.counterBlockStateBuffer[7] = this.counterBlockStateBuffer[3];
         this.counterBlockStateBuffer[3] = n11;
         n13 = n18 * 16;

         for (int i15 = 0; i15 < 16; i15++) {
            this.counterBlockStateBuffer[i15] = this.counterBlockStateBuffer[i15] ^ ints[n13 + i15];
         }

         for (int i16 = 0; i16 < 16; i16++) {
            this.counterBlockByteBuffer[i16] = (byte)this.counterBlockStateBuffer[i16];
         }

         obj8 = new byte[16];

         for (int i17 = 0; i17 < 16; i17++) {
            ((byte[])obj8)[i17] = (byte)(((byte[])obj4)[i17] ^ this.counterBlockByteBuffer[i17]);
         }

         obj2 = (byte[])obj8;
         n15 = 0;

         for (int i18 = 0; i18 < 16; i18++) {
            n15 |= ((byte[])obj12)[i18] ^ ((byte[])obj2)[i18];
         }

         if (n15 != 0) {
            throw new SecurityException("GCM authentication failed");
         } else {
            s2 = 30770;
            obj6 = new byte[16];
            System.arraycopy((byte[])obj10, 0, (byte[])obj6, 0, ((byte[])obj10).length);
            ((byte[])obj6)[15] = 1;
            s2 = 30770;
            obj5 = new byte[n9];
            obj11 = new int[16];

            for (int i19 = 0; i19 < ((byte[])obj).length; i19 += 16) {
               for (int i20 = 0; i20 < 16; i20++) {
                  ((int[])obj11)[i20] = ((byte[])obj6)[i20] & 255;
               }

               n5 = ints.length / 16 - 1;

               for (int i21 = 0; i21 < 16; i21++) {
                  ((int[])obj11)[i21] = ((int[])obj11)[i21] ^ ints[i21];
               }

               for (int i22 = 1; i22 < n5; i22++) {
                  for (int i23 = 0; i23 < 16; i23++) {
                     ((int[])obj11)[i23] = SBOX[((int[])obj11)[i23]];
                  }

                  n21 = ((int[])obj11)[1];
                  ((int[])obj11)[1] = ((int[])obj11)[5];
                  ((int[])obj11)[5] = ((int[])obj11)[9];
                  ((int[])obj11)[9] = ((int[])obj11)[13];
                  ((int[])obj11)[13] = n21;
                  n21 = ((int[])obj11)[2];
                  n23 = ((int[])obj11)[6];
                  ((int[])obj11)[2] = ((int[])obj11)[10];
                  ((int[])obj11)[6] = ((int[])obj11)[14];
                  ((int[])obj11)[10] = n21;
                  ((int[])obj11)[14] = n23;
                  n21 = ((int[])obj11)[15];
                  ((int[])obj11)[15] = ((int[])obj11)[11];
                  ((int[])obj11)[11] = ((int[])obj11)[7];
                  ((int[])obj11)[7] = ((int[])obj11)[3];
                  ((int[])obj11)[3] = n21;

                  for (int i24 = 0; i24 < 4; i24++) {
                     n21 = ((int[])obj11)[i24 * 4];
                     n23 = ((int[])obj11)[i24 * 4 + 1];
                     n8 = ((int[])obj11)[i24 * 4 + 2];
                     n3 = ((int[])obj11)[i24 * 4 + 3];
                     ((int[])obj11)[i24 * 4] = GF_MUL_TABLE[2][n21] ^ GF_MUL_TABLE[3][n23] ^ n8 ^ n3;
                     ((int[])obj11)[i24 * 4 + 1] = n21 ^ GF_MUL_TABLE[2][n23] ^ GF_MUL_TABLE[3][n8] ^ n3;
                     ((int[])obj11)[i24 * 4 + 2] = n21 ^ n23 ^ GF_MUL_TABLE[2][n8] ^ GF_MUL_TABLE[3][n3];
                     ((int[])obj11)[i24 * 4 + 3] = GF_MUL_TABLE[3][n21] ^ n23 ^ n8 ^ GF_MUL_TABLE[2][n3];
                  }

                  n22 = i22 * 16;

                  for (int i25 = 0; i25 < 16; i25++) {
                     ((int[])obj11)[i25] = ((int[])obj11)[i25] ^ ints[n22 + i25];
                  }
               }

               for (int i26 = 0; i26 < 16; i26++) {
                  ((int[])obj11)[i26] = SBOX[((int[])obj11)[i26]];
               }

               n14 = ((int[])obj11)[1];
               ((int[])obj11)[1] = ((int[])obj11)[5];
               ((int[])obj11)[5] = ((int[])obj11)[9];
               ((int[])obj11)[9] = ((int[])obj11)[13];
               ((int[])obj11)[13] = n14;
               n14 = ((int[])obj11)[2];
               n21 = ((int[])obj11)[6];
               ((int[])obj11)[2] = ((int[])obj11)[10];
               ((int[])obj11)[6] = ((int[])obj11)[14];
               ((int[])obj11)[10] = n14;
               ((int[])obj11)[14] = n21;
               n14 = ((int[])obj11)[15];
               ((int[])obj11)[15] = ((int[])obj11)[11];
               ((int[])obj11)[11] = ((int[])obj11)[7];
               ((int[])obj11)[7] = ((int[])obj11)[3];
               ((int[])obj11)[3] = n14;
               n23 = n5 * 16;

               for (int i27 = 0; i27 < 16; i27++) {
                  ((int[])obj11)[i27] = ((int[])obj11)[i27] ^ ints[n23 + i27];
               }

               n8 = Math.min(16, ((byte[])obj).length - i19);

               for (int i28 = 0; i28 < n8; i28++) {
                  ((byte[])obj5)[i19 + i28] = (byte)(((byte[])obj)[i19 + i28] ^ (byte)((int[])obj11)[i28]);
               }

               n3 = ((byte[])obj6).length - 1;

               while (n3 >= 12 && ++((byte[])obj6)[n3] == 0) {
                  n3--;
               }
            }

            return (byte[])obj5;
         }
      }
   }

   public InlinedAesGcmCipher(short s, int n, char ch) {
      this.counterBlockByteBuffer = new byte[0];
      this.cachedKey = null;
      this.cachedRoundKeys = null;
      this.cachedHashSubkey = null;
   }

   private static String bytesToHex(byte[] bytes) {
      return bytesToHex(bytes, (byte)0, bytes.length);
   }

   private byte[] computeAuthTag(byte[] bytes, byte[] bytes2, byte[] bytes3, int[] ints, byte[] bytes4) {
      int n = bytes3 != null ? bytes3.length : 0;
      if (this.ghashTable == null || !this.ghashTable.matchesSubkey(bytes4)) {
         this.ghashTable = new VendorAesGcmGHashTable(bytes4);
      }

      byte[] bytes5 = new byte[16];
      if (bytes3 != null && n > 0) {
         int n2 = (n + 16 - 1) / 16;

         for (int i = 0; i < n2; i++) {
            int n3 = Math.min(16, n - i * 16);

            for (int j = 0; j < n3; j++) {
               bytes5[j] ^= bytes3[i * 16 + j];
            }

            long l = 0L;
            long l2 = 0L;

            for (int k = 0; k < 16; k++) {
               int n4 = bytes5[k] & 255;
               if (n4 != 0) {
                  long l3 = this.ghashTable.mulTable[k][n4];
                  l ^= l3 >>> 32;
                  l2 ^= l3 & 4294967295L;
               }
            }

            for (int m = 0; m < 8; m++) {
               bytes5[m] = (byte)((int)(l >>> 56 - m * 8 & 255L));
               bytes5[m + 8] = (byte)((int)(l2 >>> 56 - m * 8 & 255L));
            }
         }
      }

      if (bytes2.length > 0) {
         int n5 = (bytes2.length + 16 - 1) / 16;

         for (int idx = 0; idx < n5; idx++) {
            int n6 = Math.min(16, bytes2.length - idx * 16);

            for (int index = 0; index < n6; index++) {
               bytes5[index] ^= bytes2[idx * 16 + index];
            }

            long l4 = 0L;
            long l5 = 0L;

            for (int i2 = 0; i2 < 16; i2++) {
               int n7 = bytes5[i2] & 255;
               if (n7 != 0) {
                  long l6 = this.ghashTable.mulTable[i2][n7];
                  l4 ^= l6 >>> 32;
                  l5 ^= l6 & 4294967295L;
               }
            }

            for (int i3 = 0; i3 < 8; i3++) {
               bytes5[i3] = (byte)((int)(l4 >>> 56 - i3 * 8 & 255L));
               bytes5[i3 + 8] = (byte)((int)(l5 >>> 56 - i3 * 8 & 255L));
            }
         }
      }

      byte[] bytes6 = new byte[16];
      long l7 = (long)n * 8L;
      long l8 = (long)bytes2.length * 8L;

      for (int i4 = 7; i4 >= 0; i4--) {
         bytes6[i4] = (byte)((int)(l7 & 255L));
         l7 >>>= 8;
         bytes6[i4 + 8] = (byte)((int)(l8 & 255L));
         l8 >>>= 8;
      }

      for (int i5 = 0; i5 < 16; i5++) {
         bytes5[i5] ^= bytes6[i5];
      }

      long l9 = 0L;
      long l10 = 0L;

      for (int i6 = 0; i6 < 16; i6++) {
         int n8 = bytes5[i6] & 255;
         if (n8 != 0) {
            long l11 = this.ghashTable.mulTable[i6][n8];
            l9 ^= l11 >>> 32;
            l10 ^= l11 & 4294967295L;
         }
      }

      for (int i7 = 0; i7 < 8; i7++) {
         bytes5[i7] = (byte)((int)(l9 >>> 56 - i7 * 8 & 255L));
         bytes5[i7 + 8] = (byte)((int)(l10 >>> 56 - i7 * 8 & 255L));
      }

      byte[] bytes7 = new byte[16];
      System.arraycopy(bytes, 0, bytes7, 0, bytes.length);
      bytes7[15] = 1;

      for (int i8 = 0; i8 < 16; i8++) {
         this.counterBlockStateBuffer[i8] = bytes7[i8] & 255;
      }

      int n9 = ints.length / 16 - 1;

      for (int i9 = 0; i9 < 16; i9++) {
         this.counterBlockStateBuffer[i9] = this.counterBlockStateBuffer[i9] ^ ints[i9];
      }

      for (int i10 = 1; i10 < n9; i10++) {
         for (int i11 = 0; i11 < 16; i11++) {
            this.counterBlockStateBuffer[i11] = SBOX[this.counterBlockStateBuffer[i11]];
         }

         int n10 = this.counterBlockStateBuffer[1];
         this.counterBlockStateBuffer[1] = this.counterBlockStateBuffer[5];
         this.counterBlockStateBuffer[5] = this.counterBlockStateBuffer[9];
         this.counterBlockStateBuffer[9] = this.counterBlockStateBuffer[13];
         this.counterBlockStateBuffer[13] = n10;
         n10 = this.counterBlockStateBuffer[2];
         int n11 = this.counterBlockStateBuffer[6];
         this.counterBlockStateBuffer[2] = this.counterBlockStateBuffer[10];
         this.counterBlockStateBuffer[6] = this.counterBlockStateBuffer[14];
         this.counterBlockStateBuffer[10] = n10;
         this.counterBlockStateBuffer[14] = n11;
         n10 = this.counterBlockStateBuffer[15];
         this.counterBlockStateBuffer[15] = this.counterBlockStateBuffer[11];
         this.counterBlockStateBuffer[11] = this.counterBlockStateBuffer[7];
         this.counterBlockStateBuffer[7] = this.counterBlockStateBuffer[3];
         this.counterBlockStateBuffer[3] = n10;

         for (int i12 = 0; i12 < 4; i12++) {
            n10 = this.counterBlockStateBuffer[i12 * 4];
            n11 = this.counterBlockStateBuffer[i12 * 4 + 1];
            int n12 = this.counterBlockStateBuffer[i12 * 4 + 2];
            int n13 = this.counterBlockStateBuffer[i12 * 4 + 3];
            this.counterBlockStateBuffer[i12 * 4] = GF_MUL_TABLE[2][n10] ^ GF_MUL_TABLE[3][n11] ^ n12 ^ n13;
            this.counterBlockStateBuffer[i12 * 4 + 1] = n10 ^ GF_MUL_TABLE[2][n11] ^ GF_MUL_TABLE[3][n12] ^ n13;
            this.counterBlockStateBuffer[i12 * 4 + 2] = n10 ^ n11 ^ GF_MUL_TABLE[2][n12] ^ GF_MUL_TABLE[3][n13];
            this.counterBlockStateBuffer[i12 * 4 + 3] = GF_MUL_TABLE[3][n10] ^ n11 ^ n12 ^ GF_MUL_TABLE[2][n13];
         }

         int n14 = i10 * 16;

         for (int i13 = 0; i13 < 16; i13++) {
            this.counterBlockStateBuffer[i13] = this.counterBlockStateBuffer[i13] ^ ints[n14 + i13];
         }
      }

      for (int i14 = 0; i14 < 16; i14++) {
         this.counterBlockStateBuffer[i14] = SBOX[this.counterBlockStateBuffer[i14]];
      }

      int n15 = this.counterBlockStateBuffer[1];
      this.counterBlockStateBuffer[1] = this.counterBlockStateBuffer[5];
      this.counterBlockStateBuffer[5] = this.counterBlockStateBuffer[9];
      this.counterBlockStateBuffer[9] = this.counterBlockStateBuffer[13];
      this.counterBlockStateBuffer[13] = n15;
      n15 = this.counterBlockStateBuffer[2];
      int n16 = this.counterBlockStateBuffer[6];
      this.counterBlockStateBuffer[2] = this.counterBlockStateBuffer[10];
      this.counterBlockStateBuffer[6] = this.counterBlockStateBuffer[14];
      this.counterBlockStateBuffer[10] = n15;
      this.counterBlockStateBuffer[14] = n16;
      n15 = this.counterBlockStateBuffer[15];
      this.counterBlockStateBuffer[15] = this.counterBlockStateBuffer[11];
      this.counterBlockStateBuffer[11] = this.counterBlockStateBuffer[7];
      this.counterBlockStateBuffer[7] = this.counterBlockStateBuffer[3];
      this.counterBlockStateBuffer[3] = n15;
      int n17 = n9 * 16;

      for (int i15 = 0; i15 < 16; i15++) {
         this.counterBlockStateBuffer[i15] = this.counterBlockStateBuffer[i15] ^ ints[n17 + i15];
      }

      for (int i16 = 0; i16 < 16; i16++) {
         this.counterBlockByteBuffer[i16] = (byte)this.counterBlockStateBuffer[i16];
      }

      byte[] bytes8 = new byte[16];

      for (int i17 = 0; i17 < 16; i17++) {
         bytes8[i17] = (byte)(bytes5[i17] ^ this.counterBlockByteBuffer[i17]);
      }

      return bytes8;
   }

   private void encryptBlock(int[] ints, int n, char ch, char ch2, int[] ints2) {
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      int n6 = 0;
      int n7 = 0;
      int n8 = 0;
      boolean flag = false;
      n6 = ints2.length / 16 - 1;

      for (int i = 0; i < 16; i++) {
         ints[i] = ints[i] ^ ints2[i];
      }

      for (int j = 1; j < n6; j++) {
         for (int k = 0; k < 16; k++) {
            ints[k] = SBOX[((int[])ints)[k]];
         }

         n8 = ints[1];
         ints[1] = ints[5];
         ints[5] = ints[9];
         ints[9] = ints[13];
         ints[13] = n8;
         n8 = ints[2];
         n5 = ints[6];
         ints[2] = ints[10];
         ints[6] = ints[14];
         ints[10] = n8;
         ints[14] = n5;
         n8 = ints[15];
         ints[15] = ints[11];
         ints[11] = ints[7];
         ints[7] = ints[3];
         ints[3] = n8;

         for (int m = 0; m < 4; m++) {
            n8 = ints[m * 4];
            n5 = ints[m * 4 + 1];
            n7 = ints[m * 4 + 2];
            n3 = ints[m * 4 + 3];
            ints[m * 4] = GF_MUL_TABLE[2][n8] ^ GF_MUL_TABLE[3][n5] ^ n7 ^ n3;
            ints[m * 4 + 1] = n8 ^ GF_MUL_TABLE[2][n5] ^ GF_MUL_TABLE[3][n7] ^ n3;
            ints[m * 4 + 2] = n8 ^ n5 ^ GF_MUL_TABLE[2][n7] ^ GF_MUL_TABLE[3][n3];
            ints[m * 4 + 3] = GF_MUL_TABLE[3][n8] ^ n5 ^ n7 ^ GF_MUL_TABLE[2][n3];
         }

         n4 = j * 16;

         for (int idx = 0; idx < 16; idx++) {
            ints[idx] = ints[idx] ^ ints2[n4 + idx];
         }
      }

      for (int index = 0; index < 16; index++) {
         ints[index] = SBOX[((int[])ints)[index]];
      }

      n2 = ints[1];
      ints[1] = ints[5];
      ints[5] = ints[9];
      ints[9] = ints[13];
      ints[13] = n2;
      n2 = ints[2];
      n8 = ints[6];
      ints[2] = ints[10];
      ints[6] = ints[14];
      ints[10] = n2;
      ints[14] = n8;
      n2 = ints[15];
      ints[15] = ints[11];
      ints[11] = ints[7];
      ints[7] = ints[3];
      ints[3] = n2;
      n5 = n6 * 16;

      for (int i2 = 0; i2 < 16; i2++) {
         ints[i2] = ints[i2] ^ ints2[n5 + i2];
      }
   }

   static {
      for (int i = 0; i < 256; i++) {
         GF_MUL_TABLE[2][i] = (i & 128) != 0 ? (i << 1 ^ 27) & 0xFF : i << 1 & 0xFF;
         GF_MUL_TABLE[3][i] = GF_MUL_TABLE[2][i] ^ i;
         GF_MUL_TABLE[9][i] = GF_MUL_TABLE[2][GF_MUL_TABLE[2][GF_MUL_TABLE[2][i]]] ^ i;
         GF_MUL_TABLE[11][i] = GF_MUL_TABLE[2][GF_MUL_TABLE[2][i] ^ i] ^ i;
         GF_MUL_TABLE[13][i] = GF_MUL_TABLE[2][GF_MUL_TABLE[2][i ^ i]] ^ i;
         GF_MUL_TABLE[14][i] = GF_MUL_TABLE[2][GF_MUL_TABLE[2][i ^ i] ^ i];
      }
   }

   public byte[] encrypt(byte[] bytes, byte[] bytes2, byte[] bytes3) {
      int n = 0;
      long l = 0L;
      Object obj = null;
      int n2 = 0;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      int n3 = 0;
      int n4 = 0;
      Object obj5 = null;
      int n5 = 0;
      boolean flag = false;
      long l2 = 0L;
      Object obj6 = null;
      Object obj7 = null;
      int n6 = 0;
      Object obj8 = null;
      int n7 = 0;
      int n8 = 0;
      int n9 = 0;
      int[] ints = null;
      int n10 = 0;
      int n11 = 0;
      long l3 = 0L;
      int n12 = 0;
      int n13 = 0;
      int n14 = 0;
      Object obj9 = null;
      long l4 = 0L;
      Object obj10 = null;
      int n15 = 0;
      byte b = 0;
      int n16 = 0;
      int n17 = 0;
      int n18 = 0;
      int n19 = 0;
      int n20 = 0;
      Object obj11 = null;
      int n21 = 0;
      long l5 = 0L;
      long l6 = 0L;
      Object obj12 = null;
      boolean flag2 = false;
      long l7 = 0L;
      long l8 = 0L;
      long l9 = 0L;
      int n22 = 0;
      boolean flag3 = false;
      boolean flag4 = false;
      long l10 = 0L;
      long l11 = 0L;
      l11 = 2410146104314594910L ^ (long)"텴큁쮡畔ﭲ刻\uaaca筅殚쯎\ue686랿晪䀷쮜鍙꛰ւ䵁硧癁\ue95c".hashCode();
      if (bytes2.length == 16) {
         l11 = l11 + 2650918704823856267L ^ -5329979511440306887L;
      } else {
         l11 = l11 + -6601318706167977837L ^ -8905254637777580269L;
         if (bytes2.length == 24) {
            l11 = (l11 ^ 8315560163467334062L) - -4567568357478894340L;
         } else {
            l11 = l11 - -8748617431718180683L ^ -6686626100239950957L;
            if (bytes2.length != 32) {
               l11 = (l11 ^ 4140465156507428936L) - -4432523763299712308L;
               throw new IllegalArgumentException("Invalid key size. Must be 16, 24, or 32 bytes");
            }

            l11 = (l11 ^ 7200899320763611194L) - -8394949473010836432L;
         }
      }

      obj4 = new byte[12];
      l11 = l11 + 6747812364517132236L ^ 2849632097439201307L;
      l4 = System.nanoTime();
      l11 = (l11 ^ -4617708850808884976L) + 6960123017430672278L;
      l6 = 25214903917L;
      l11 = (l11 ^ -3424793372815648218L) + 2496858544756853644L;
      l9 = 11L;
      l11 = l11 - 1781027567651610388L ^ 5898410430009303040L;
      l7 = 281474976710655L;
      l11 = l11 + -3378247926588834030L ^ -2630877462162164828L;
      n17 = 0;
      l11 = l11 + -8158744755881897593L ^ 2682178043162401476L;

      while (n17 < ((byte[])obj4).length) {
         l11 = (l11 ^ 8682788606199533085L) - 609512804567261962L;
         l4 = l4 * l6 + l9 & l7;
         l11 = l11 - -3121948027185145060L ^ 6407405617486995014L;
         ((byte[])obj4)[n17] = (byte)((int)(l4 >> 16 & 255L));
         l11 = l11 + 616914100676106909L ^ -6513574555984514508L;
         n17++;
         l11 = l11 - -4059569671700662217L ^ 3935454614007547855L;
         l11 = (l11 ^ -4868921796406618734L) + -1758502662939848906L;
      }

      l11 = l11 - 6037501784475068735L ^ 8573914027118935437L;
      obj2 = null;
      l11 = (l11 ^ -1475427265605814437L) + 6077550753494863842L;
      flag = false;
      l11 = (l11 ^ 5369994908463447273L) + 7632483214809967440L;
      if (this.cachedKey == null) {
         l11 = l11 - -7415183281653641070L ^ 6129161947097657193L;
      } else {
         l11 = l11 - -4941242593306356881L ^ -3671520034446758844L;
         if (bytes2.length != this.cachedKey.length) {
            l11 = l11 - 2054183736514418194L ^ 4715525467443236956L;
         } else {
            l11 = l11 - -762409605213065319L ^ -5588505462837456058L;
            b = 1;
            l11 = (l11 ^ 1151247472273832185L) + 1386199982325915224L;
            n8 = 0;
            l11 = l11 + 3804778047043579648L ^ -8505240479233661043L;

            exitBlock: {
               while (n8 < ((byte[])bytes2).length) {
                  l11 = l11 - 8577465938426701525L ^ -4260383405673038860L;
                  if (bytes2[n8] != this.cachedKey[n8]) {
                     l11 = (l11 ^ 1108451778025728014L) + -8815955707518770323L;
                     b = 0;
                     l11 = (l11 ^ 3583269057033157816L) - 4989582479121060299L;
                     l11 = (l11 ^ -2827242771671277236L) - 516314514146210919L;
                     break exitBlock;
                  }

                  l11 = (l11 ^ 7053029246018317429L) - -3967033209917959194L;
                  n8++;
                  l11 = (l11 ^ -7555772059218865823L) + -7737488690501017437L;
                  l11 = l11 - -9215917757180024097L ^ -7446048661857452951L;
               }

               l11 = l11 - -5352088106400403980L ^ 2974542326080114860L;
            }

            if (b == 0) {
               l11 = l11 + -7337790253925806113L ^ -7890298943777357902L;
            } else {
               l11 = (l11 ^ 1119914273597717225L) - -4781809186925280366L;
               if (this.cachedRoundKeys == null) {
                  l11 = (l11 ^ -7207771566574509802L) - -6881973644454448660L;
               } else {
                  l11 = (l11 ^ 6361092282631426072L) - 8840705876778469236L;
                  ints = this.cachedRoundKeys;
                  l11 = l11 - -7780214170757947122L ^ 585436812659828086L;
                  flag = true;
                  l11 = (l11 ^ -8637165048431589695L) + -5996358845215906085L;
               }
            }
         }
      }

      if (flag) {
         l11 = l11 + -7680711856726736240L ^ 8707599546509114919L;
      } else {
         l11 = (l11 ^ 381214096228632359L) - -4746628669553259758L;
         b = 0;
         l11 = l11 - 3539611264215464324L ^ 7204829069652659487L;
         if (bytes2.length != 16) {
            l11 = l11 - 1479022393945043296L ^ -8046036305752877060L;
            if (bytes2.length != 24) {
               l11 = l11 - -5338020229919034809L ^ -7996622838754903896L;
               b = 14;
               l11 = (l11 ^ -4398203883642157263L) - -6039170631664449256L;
            } else {
               l11 = l11 + -2021589356594961130L ^ -2940158033439682344L;
               b = 12;
               l11 = l11 - 1216579003074956499L ^ -7970452585691729473L;
               l11 = l11 + -4761382385821068446L ^ 2025813179558155326L;
            }
         } else {
            l11 = (l11 ^ 1662695772964785392L) + -6998353364343820111L;
            b = 10;
            l11 = (l11 ^ -7787697881552949337L) - -3515407317660793423L;
            l11 = l11 - -2776891155342099485L ^ 8583905639784747310L;
         }

         n8 = bytes2.length / 4;
         l11 = (l11 ^ -2712743271709575139L) - -3135530868449188265L;
         n12 = 4 * (b + 1);
         l11 = l11 - 3583419793352087258L ^ -7157159292868971430L;
         obj9 = new int[n12];
         l11 = l11 - -4861101344067625802L ^ 8032469799336084467L;
         n10 = 0;
         l11 = (l11 ^ -7115119583391024647L) - 5077842512577727828L;

         while (n10 < n8) {
            l11 = (l11 ^ 470139508724622445L) + 7916566138415447410L;
            ((int[])obj9)[n10] = (bytes2[4 * n10] & 255) << 24
               | (bytes2[4 * n10 + 1] & 255) << 16
               | (bytes2[4 * n10 + 2] & 255) << 8
               | bytes2[4 * n10 + 3] & 255;
            l11 = (l11 ^ -9088966129563773496L) - -3630955073389255006L;
            n10++;
            l11 = l11 - -8257709988822688976L ^ -5868667232128675868L;
            l11 = l11 - 5343095595477377482L ^ 3076830224996424951L;
         }

         l11 = (l11 ^ 3065159661965955493L) + -6419265191778862122L;
         n10 = n8;
         l11 = (l11 ^ 3512445031990613267L) - 8425413950467421610L;

         while (n10 < n12) {
            l11 = l11 + -2374984769680059137L ^ 8452102881836129913L;
            n13 = ((int[])obj9)[n10 - 1];
            l11 = l11 + 8412956477727758150L ^ 6234691507627929997L;
            if (n10 % n8 != 0) {
               l11 = l11 + 2446631785540622867L ^ -7438370918546676279L;
               if (n8 <= 6) {
                  l11 = l11 + 7199705090497359143L ^ 5753719218068885545L;
               } else {
                  l11 = l11 + -511271195851354761L ^ 2264674645424840826L;
                  if (n10 % n8 != 4) {
                     l11 = l11 + -1680678127356727106L ^ -4316285977911656255L;
                  } else {
                     l11 = (l11 ^ 5740933838273417037L) - 3285151745795302563L;
                     n13 = SBOX[n13 >>> 24 & 0xFF] << 24 | SBOX[n13 >>> 16 & 0xFF] << 16 | SBOX[n13 >>> 8 & 0xFF] << 8 | SBOX[n13 & 0xFF];
                     l11 = (l11 ^ -3861197752850442480L) + -3369314064762842555L;
                  }
               }
            } else {
               l11 = (l11 ^ -6590422598956940463L) - 9134608199382963147L;
               n13 = n13 << 8 & -256 | n13 >>> 24 & 0xFF;
               l11 = (l11 ^ 2242586664633170589L) - 7535290191695897998L;
               n13 = SBOX[n13 >>> 24 & 0xFF] << 24 | SBOX[n13 >>> 16 & 0xFF] << 16 | SBOX[n13 >>> 8 & 0xFF] << 8 | SBOX[n13 & 0xFF];
               l11 = l11 + 2141123409415739670L ^ 4925762183821591336L;
               n13 ^= RCON[n10 / n8] << 24;
               l11 = (l11 ^ 2410815435464196692L) + -7241047923989283505L;
               l11 = (l11 ^ 2465699811558672172L) - -860093570369468438L;
            }

            ((int[])obj9)[n10] = ((int[])obj9)[n10 - n8] ^ n13;
            l11 = l11 + 4357179015865936384L ^ 8755032889280470587L;
            n10++;
            l11 = l11 + 5617659938993712707L ^ -9037163565803057319L;
            l11 = l11 - -3046018858881719958L ^ 5926798214606673194L;
         }

         l11 = l11 + 650787297951229760L ^ -5783168934154216431L;
         obj8 = new int[n12 * 4];
         l11 = (l11 ^ 3193377072758141128L) + -4593478721510982683L;
         n13 = 0;
         l11 = l11 + -5303197297501461493L ^ 5073022712750260710L;

         while (n13 < n12) {
            l11 = (l11 ^ 4060799619323706218L) - -8096594820638901204L;
            ((int[])obj8)[4 * n13] = ((int[])obj9)[n13] >>> 24 & 0xFF;
            l11 = l11 + -3409987059105960571L ^ -1370251469850810779L;
            ((int[])obj8)[4 * n13 + 1] = ((int[])obj9)[n13] >>> 16 & 0xFF;
            l11 = (l11 ^ 7691267241088252392L) + 4032741951599228508L;
            ((int[])obj8)[4 * n13 + 2] = ((int[])obj9)[n13] >>> 8 & 0xFF;
            l11 = (l11 ^ 1733420964797236718L) - -882914201625004122L;
            ((int[])obj8)[4 * n13 + 3] = ((int[])obj9)[n13] & 0xFF;
            l11 = (l11 ^ 973244181862439529L) + -2222967824492842577L;
            n13++;
            l11 = (l11 ^ 3888872882090443184L) + 6604670253036601800L;
            l11 = (l11 ^ 4198799187782005323L) + 1453405268856483387L;
         }

         l11 = (l11 ^ 148253908560106766L) - -7559237033537265089L;
         this.cachedKey = new byte[((byte[])bytes2).length];
         l11 = l11 - 1859557408611934634L ^ 3309493135295662165L;
         System.arraycopy(bytes2, 0, this.cachedKey, 0, bytes2.length);
         l11 = l11 - -4892658365545666050L ^ 3835218147139611440L;
         this.cachedRoundKeys = (int[])obj8;
         l11 = (l11 ^ -911626482763127152L) - 4612933625552928792L;
         ints = (int[])obj8;
         l11 = l11 - 8630426911125102751L ^ 7657670877590780397L;
      }

      obj6 = this.hashSubkeyByteBuffer;
      l11 = l11 + 1814433741692937745L ^ 7100677934320272681L;
      boolean flag5 = false;
      l11 = l11 - -1721937778059771992L ^ 2033847981077769442L;
      if (this.cachedKey == null) {
         l11 = (l11 ^ 3473335048833633586L) - 9119617267464616287L;
      } else {
         l11 = (l11 ^ -8895790311237088286L) + -686697794755377239L;
         if (this.cachedHashSubkey == null) {
            l11 = l11 + 8673234900882586048L ^ -6481302961343554424L;
         } else {
            l11 = l11 - 683408264403457753L ^ 6265513566187079339L;
            boolean flag6 = true;
            l11 = (l11 ^ 8655681268576945242L) - -3612509007875316758L;
            n22 = 0;
            l11 = (l11 ^ 5886014278308983076L) - 283960768162691194L;

            exitBlock2: {
               while (n22 < ((byte[])bytes2).length) {
                  l11 = l11 + -7765070233561000437L ^ -5411044868305474134L;
                  if (bytes2[n22] != this.cachedKey[n22]) {
                     l11 = l11 - 4377643763548983141L ^ -2685575621535246174L;
                     flag6 = false;
                     l11 = (l11 ^ 173107244632504618L) + -9099619537124465207L;
                     l11 = l11 + -1335056930095304557L ^ 6695225435016660149L;
                     break exitBlock2;
                  }

                  l11 = (l11 ^ 3119688183087072724L) - 2694138346738794483L;
                  n22++;
                  l11 = l11 - 880469514270258760L ^ 4741403413502998714L;
                  l11 = l11 - -1419173302443068501L ^ 2979625680388815911L;
               }

               l11 = (l11 ^ -3155637313364940845L) + -7381687961969558354L;
            }

            if (!flag6) {
               l11 = (l11 ^ -7013271597929923253L) + 4263726493634816982L;
            } else {
               l11 = l11 + -2863643899996635762L ^ -8914239487483692580L;
               obj6 = this.cachedHashSubkey;
               l11 = l11 - -2787589866879262314L ^ 2799694431325304229L;
               flag5 = true;
               l11 = (l11 ^ -4759423713133925327L) + -4468312147045960195L;
            }
         }
      }

      if (flag5) {
         l11 = l11 + 4012941824101752249L ^ -8027263182050335349L;
      } else {
         l11 = l11 - 3826567990939642534L ^ 4265541000809887049L;
         n12 = 0;
         l11 = (l11 ^ 5221711409679805457L) + -7054696769814004668L;

         while (n12 < 16) {
            l11 = l11 + 455282939731042476L ^ -8401575459321114577L;
            this.hashSubkeyStateBuffer[n12] = 0;
            l11 = (l11 ^ -68518819698047694L) - -4635780706933449401L;
            n12++;
            l11 = (l11 ^ 5707701600033149272L) - 2921599774257896710L;
            l11 = (l11 ^ 1305543830357206740L) + 4237914360240846114L;
         }

         l11 = (l11 ^ -8818513236061964655L) + -1368350021798032026L;
         n12 = ints.length / 16 - 1;
         l11 = l11 + -3049032162143055942L ^ 3128684288544768180L;
         n22 = 0;
         l11 = (l11 ^ -4071864947728636668L) - 4692598548888480911L;

         while (n22 < 16) {
            l11 = (l11 ^ -6767486530484063737L) - -9077160383022004519L;
            this.hashSubkeyStateBuffer[n22] = this.hashSubkeyStateBuffer[n22] ^ ints[n22];
            l11 = l11 + -2840497841926415862L ^ 5265207098726709555L;
            n22++;
            l11 = (l11 ^ 6483868927208623860L) - -2064247267740363871L;
            l11 = l11 + -1121705364620761118L ^ 3538920790276555984L;
         }

         l11 = (l11 ^ 2553775131553303725L) - 6902395650468967323L;
         n22 = 1;
         l11 = (l11 ^ 8961998637370659270L) + 716896093680908370L;

         while (n22 < n12) {
            l11 = (l11 ^ 2857405484009059878L) - 6209195209837947278L;
            n10 = 0;
            l11 = (l11 ^ 3924980505139272421L) + -2555982730359218553L;

            while (n10 < 16) {
               l11 = l11 + -7580809177799563608L ^ 2519083886699576449L;
               this.hashSubkeyStateBuffer[n10] = SBOX[this.hashSubkeyStateBuffer[n10]];
               l11 = l11 + 1484423512100027107L ^ -2971856292601757500L;
               n10++;
               l11 = l11 + 1736954461737275581L ^ 8957063759850144572L;
               l11 = (l11 ^ -1712043294512000601L) - 7088817746911495058L;
            }

            l11 = l11 - 7774887328142821626L ^ -5941175516074276296L;
            n10 = this.hashSubkeyStateBuffer[1];
            l11 = l11 + -6382395115636659094L ^ -555269732457787619L;
            this.hashSubkeyStateBuffer[1] = this.hashSubkeyStateBuffer[5];
            l11 = (l11 ^ 1562575702768689621L) - -1322592571742573262L;
            this.hashSubkeyStateBuffer[5] = this.hashSubkeyStateBuffer[9];
            l11 = l11 - 4219803881583409766L ^ 1200910170979554908L;
            this.hashSubkeyStateBuffer[9] = this.hashSubkeyStateBuffer[13];
            l11 = (l11 ^ -1180223480165261423L) - -8511148376535158925L;
            this.hashSubkeyStateBuffer[13] = n10;
            l11 = (l11 ^ 4886730759897860252L) - 8070464587199409837L;
            n10 = this.hashSubkeyStateBuffer[2];
            l11 = (l11 ^ 4398042423747429952L) + 5046299954452337486L;
            n13 = this.hashSubkeyStateBuffer[6];
            l11 = l11 - 8015230153728411630L ^ 2708470457888249215L;
            this.hashSubkeyStateBuffer[2] = this.hashSubkeyStateBuffer[10];
            l11 = (l11 ^ 7065230873206220987L) + -8724100616148868829L;
            this.hashSubkeyStateBuffer[6] = this.hashSubkeyStateBuffer[14];
            l11 = (l11 ^ 8948315837717572529L) - 8178715218894748322L;
            this.hashSubkeyStateBuffer[10] = n10;
            l11 = l11 - -5092680772647699687L ^ -7651389845227897835L;
            this.hashSubkeyStateBuffer[14] = n13;
            l11 = l11 + 7508838595793639831L ^ -6175156083266661679L;
            n10 = this.hashSubkeyStateBuffer[15];
            l11 = (l11 ^ -5668855799813748255L) + -4115218872221767362L;
            this.hashSubkeyStateBuffer[15] = this.hashSubkeyStateBuffer[11];
            l11 = l11 - 4622879700830640162L ^ 7653351550957826771L;
            this.hashSubkeyStateBuffer[11] = this.hashSubkeyStateBuffer[7];
            l11 = (l11 ^ 6777991738493640914L) - -1284970292265752470L;
            this.hashSubkeyStateBuffer[7] = this.hashSubkeyStateBuffer[3];
            l11 = l11 + -4327134852554495096L ^ -8360603544748850438L;
            this.hashSubkeyStateBuffer[3] = n10;
            l11 = (l11 ^ 2179899346285185851L) + 9211438813782179597L;
            n7 = 0;
            l11 = l11 + -1161276418395471615L ^ -6381104352194746350L;

            while (n7 < 4) {
               l11 = l11 + 8865066960541688696L ^ -6122317067061751685L;
               n10 = this.hashSubkeyStateBuffer[n7 * 4];
               l11 = l11 - -1908902391069430070L ^ 1769879082133196358L;
               n13 = this.hashSubkeyStateBuffer[n7 * 4 + 1];
               l11 = l11 - 8805890193412283478L ^ 1710885461077480388L;
               n20 = this.hashSubkeyStateBuffer[n7 * 4 + 2];
               l11 = (l11 ^ 7712922568847428090L) - -671060392691047042L;
               n19 = this.hashSubkeyStateBuffer[n7 * 4 + 3];
               l11 = l11 - 2076935309814991969L ^ 1496961603199442262L;
               this.hashSubkeyStateBuffer[n7 * 4] = GF_MUL_TABLE[2][n10] ^ GF_MUL_TABLE[3][n13] ^ n20 ^ n19;
               l11 = l11 - 7454624284158272062L ^ 4667324506001768142L;
               this.hashSubkeyStateBuffer[n7 * 4 + 1] = n10 ^ GF_MUL_TABLE[2][n13] ^ GF_MUL_TABLE[3][n20] ^ n19;
               l11 = (l11 ^ -1019617216029750514L) - 6918154815519478452L;
               this.hashSubkeyStateBuffer[n7 * 4 + 2] = n10 ^ n13 ^ GF_MUL_TABLE[2][n20] ^ GF_MUL_TABLE[3][n19];
               l11 = l11 - -4923300400912833234L ^ -3993361001056261701L;
               this.hashSubkeyStateBuffer[n7 * 4 + 3] = GF_MUL_TABLE[3][n10] ^ n13 ^ n20 ^ GF_MUL_TABLE[2][n19];
               l11 = (l11 ^ 1893636028013301414L) - 4724044841928778723L;
               n7++;
               l11 = l11 + 2504517496143515591L ^ 6729175969886872750L;
               l11 = (l11 ^ 6390707087686025007L) - 7328674742696958464L;
            }

            l11 = (l11 ^ -843434607459884735L) + -507157761745854792L;
            n7 = n22 * 16;
            l11 = l11 - 5734560575929415074L ^ 1304961499189897599L;
            n = 0;
            l11 = (l11 ^ -8313730681307544819L) + -2743147116635418959L;

            while (n < 16) {
               l11 = l11 + 8542810904218024354L ^ 6525437766807229364L;
               this.hashSubkeyStateBuffer[n] = this.hashSubkeyStateBuffer[n] ^ ints[n7 + n];
               l11 = l11 - 6046457820831148908L ^ -8008444385449427096L;
               n++;
               l11 = (l11 ^ 3201791934612539249L) - 3939528468339575392L;
               l11 = l11 + -8530957797070417396L ^ 4969028756754662767L;
            }

            l11 = (l11 ^ -8739788134142681787L) + -4866832960048555964L;
            n22++;
            l11 = l11 + 2788690296203485199L ^ 2766913068150674946L;
            l11 = (l11 ^ -8744671955760682725L) + -5676826762577695645L;
         }

         l11 = (l11 ^ -1608986913174929375L) - 598253981348799679L;
         n22 = 0;
         l11 = (l11 ^ 3666005308135349483L) - 4540896988687972692L;

         while (n22 < 16) {
            l11 = (l11 ^ -2511211759750279740L) - 2591761903515868680L;
            this.hashSubkeyStateBuffer[n22] = SBOX[this.hashSubkeyStateBuffer[n22]];
            l11 = (l11 ^ -7804591550219131773L) + 1167038194671125749L;
            n22++;
            l11 = (l11 ^ 8073531494264411037L) - -717055789591915611L;
            l11 = l11 + 9009674831247075226L ^ -7819637662695034426L;
         }

         l11 = (l11 ^ 2743480369936730566L) + 1768747374942556430L;
         n22 = this.hashSubkeyStateBuffer[1];
         l11 = l11 + -5908338086630829432L ^ 456772815923145983L;
         this.hashSubkeyStateBuffer[1] = this.hashSubkeyStateBuffer[5];
         l11 = (l11 ^ 25009824636410664L) - -3475668584503127991L;
         this.hashSubkeyStateBuffer[5] = this.hashSubkeyStateBuffer[9];
         l11 = l11 + -4456208575225229832L ^ 4527365702400387773L;
         this.hashSubkeyStateBuffer[9] = this.hashSubkeyStateBuffer[13];
         l11 = l11 - -6003432390136632247L ^ -2036170227738007353L;
         this.hashSubkeyStateBuffer[13] = n22;
         l11 = (l11 ^ -8049695726042992472L) + -7517369689868713265L;
         n22 = this.hashSubkeyStateBuffer[2];
         l11 = l11 - -1677022899276198727L ^ -2923212844459278853L;
         n10 = this.hashSubkeyStateBuffer[6];
         l11 = (l11 ^ 2233068927073786979L) + -5451553796705472752L;
         this.hashSubkeyStateBuffer[2] = this.hashSubkeyStateBuffer[10];
         l11 = (l11 ^ 7727962842552679868L) + 5979978765218461042L;
         this.hashSubkeyStateBuffer[6] = this.hashSubkeyStateBuffer[14];
         l11 = (l11 ^ -6676034954292394145L) + -1636391913437678670L;
         this.hashSubkeyStateBuffer[10] = n22;
         l11 = l11 - 5813407363322375425L ^ 8373076981116791830L;
         this.hashSubkeyStateBuffer[14] = n10;
         l11 = (l11 ^ 8265546771806775697L) - 2088381364357781452L;
         n22 = this.hashSubkeyStateBuffer[15];
         l11 = (l11 ^ 5975414491208872993L) + -5694197808792601725L;
         this.hashSubkeyStateBuffer[15] = this.hashSubkeyStateBuffer[11];
         l11 = (l11 ^ -1090501050658977250L) - -8490158882289871546L;
         this.hashSubkeyStateBuffer[11] = this.hashSubkeyStateBuffer[7];
         l11 = l11 - -3474210483894787605L ^ 443759507703805052L;
         this.hashSubkeyStateBuffer[7] = this.hashSubkeyStateBuffer[3];
         l11 = l11 - 2889281519182277504L ^ -294217216616349092L;
         this.hashSubkeyStateBuffer[3] = n22;
         l11 = (l11 ^ 3439467521917059131L) + 7943108161641024348L;
         n13 = n12 * 16;
         l11 = l11 + 2406944836993724933L ^ 1150556946125733413L;
         n20 = 0;
         l11 = (l11 ^ -90427324932098056L) - -5072443044187582758L;

         while (n20 < 16) {
            l11 = (l11 ^ 5386352043835697091L) - 5064601624270250178L;
            this.hashSubkeyStateBuffer[n20] = this.hashSubkeyStateBuffer[n20] ^ ints[n13 + n20];
            l11 = (l11 ^ -5697840868810773226L) + -5040894579616664551L;
            n20++;
            l11 = l11 - -4865558202965111706L ^ -3648282292071091570L;
            l11 = (l11 ^ 5059602850632530488L) - 6236650993161328296L;
         }

         l11 = (l11 ^ -2434058307012429511L) - -4996767788448737281L;
         n20 = 0;
         l11 = (l11 ^ 7526445833572090122L) + 8454948674955320284L;

         while (n20 < 16) {
            l11 = l11 + 9046739741567611578L ^ -9008535467327288414L;
            this.hashSubkeyByteBuffer[n20] = (byte)this.hashSubkeyStateBuffer[n20];
            l11 = l11 - 6239606250951558378L ^ 6986688874004378264L;
            n20++;
            l11 = (l11 ^ 2285181360370039569L) + 8345555785631162279L;
            l11 = (l11 ^ -1901991404310343814L) + 3533304270913135940L;
         }

         l11 = l11 + 1246999567529993781L ^ -6129023122054940725L;
         if (this.cachedHashSubkey != null) {
            l11 = (l11 ^ 7676529647559860556L) + 5422734547210634668L;
         } else {
            l11 = (l11 ^ 5547872144272503926L) + -8651869927861400370L;
            this.cachedHashSubkey = new byte[16];
            l11 = (l11 ^ -3540303611682959208L) - 6140733131925482148L;
         }

         System.arraycopy(this.hashSubkeyByteBuffer, 0, this.cachedHashSubkey, 0, 16);
         l11 = l11 - -6582956429272523396L ^ 938144011361394411L;
      }

      obj3 = new byte[16];
      l11 = (l11 ^ -2461943149107228979L) - 5793790481794749324L;
      System.arraycopy((byte[])obj4, 0, (byte[])obj3, 0, ((byte[])obj4).length);
      l11 = l11 - 197117938690848866L ^ -4538886375031910785L;
      ((byte[])obj3)[15] = 1;
      l11 = (l11 ^ 2831328368567113357L) + -4409343968555011501L;
      obj9 = new byte[((byte[])bytes).length];
      l11 = l11 - 2715815895098651420L ^ 5528146473849292789L;
      obj8 = new int[16];
      l11 = l11 + -160776420897879776L ^ -5621241020948953502L;
      int n23 = 0;
      l11 = l11 + 726842492506019203L ^ -373147739812622507L;

      while (n23 < ((byte[])bytes).length) {
         l11 = l11 - 4256220318821731186L ^ -2308667255890732818L;
         n20 = 0;
         l11 = (l11 ^ -8450694644533114485L) + -4639271728320904507L;

         while (n20 < 16) {
            l11 = l11 + -1099352544674564410L ^ -7592551977511365296L;
            ((int[])obj8)[n20] = ((byte[])obj3)[n20] & 255;
            l11 = (l11 ^ 989562795517451253L) + 5292007135513874211L;
            n20++;
            l11 = (l11 ^ -3130408630630310878L) + -395676935464800236L;
            l11 = (l11 ^ -2150774537947738935L) - -496773145558172679L;
         }

         l11 = (l11 ^ -2296085822372356408L) - -3532077054499798799L;
         n20 = ints.length / 16 - 1;
         l11 = l11 - -5427244446239548278L ^ 4261170232773752698L;
         n19 = 0;
         l11 = (l11 ^ -7021297169949367967L) + 3822632411886306325L;

         while (n19 < 16) {
            l11 = l11 - 6172028937669498128L ^ 2145138895173566065L;
            ((int[])obj8)[n19] = ((int[])obj8)[n19] ^ ints[n19];
            l11 = (l11 ^ 1228339293854180145L) - 5365591222888286249L;
            n19++;
            l11 = (l11 ^ -7077899575486171190L) - 4390452151957080239L;
            l11 = (l11 ^ 1245215288762346095L) + 7226659749674935501L;
         }

         l11 = (l11 ^ 1061114665152913683L) + -4071056280851848039L;
         n19 = 1;
         l11 = l11 - 3516016088201827949L ^ 4461655883965710861L;

         while (n19 < n20) {
            l11 = l11 + -7861035299936185360L ^ -8619690231740941007L;
            n7 = 0;
            l11 = (l11 ^ -1127021322275848263L) + -2887374245339072067L;

            while (n7 < 16) {
               l11 = (l11 ^ 4907614721217535989L) - -3662639706290800007L;
               ((int[])obj8)[n7] = SBOX[((int[])obj8)[n7]];
               l11 = (l11 ^ -1442332244895608926L) - 86975825860983870L;
               n7++;
               l11 = l11 + -1504745385003635353L ^ 5409442882691574927L;
               l11 = l11 + 563822572993631135L ^ 6194398606617064811L;
            }

            l11 = (l11 ^ 2942115740856640950L) - 7972663644388689015L;
            n7 = ((int[])obj8)[1];
            l11 = l11 + 3939852654341948942L ^ 374529774451195792L;
            ((int[])obj8)[1] = ((int[])obj8)[5];
            l11 = (l11 ^ 4791205891071841724L) + -5943445849664423226L;
            ((int[])obj8)[5] = ((int[])obj8)[9];
            l11 = (l11 ^ -4623587103310605613L) + 1517841008587221000L;
            ((int[])obj8)[9] = ((int[])obj8)[13];
            l11 = l11 + -4020067738852874747L ^ -4893677526250411117L;
            ((int[])obj8)[13] = n7;
            l11 = l11 + -8412088189294398848L ^ 8989010323887205723L;
            n7 = ((int[])obj8)[2];
            l11 = (l11 ^ -4390479936055359298L) + 2145207246865166610L;
            n = ((int[])obj8)[6];
            l11 = (l11 ^ 863822216945825459L) + 879494013466516238L;
            ((int[])obj8)[2] = ((int[])obj8)[10];
            l11 = l11 + -4760933368332363446L ^ -8811821540387992469L;
            ((int[])obj8)[6] = ((int[])obj8)[14];
            l11 = l11 + -648705791797218339L ^ -777188722498938830L;
            ((int[])obj8)[10] = n7;
            l11 = l11 + -2853833595804244918L ^ 5993177638930214716L;
            ((int[])obj8)[14] = n;
            l11 = (l11 ^ -5158425017821359220L) - -8387302739024475466L;
            n7 = ((int[])obj8)[15];
            l11 = (l11 ^ -6721853473038065619L) + -15517337619350191L;
            ((int[])obj8)[15] = ((int[])obj8)[11];
            l11 = (l11 ^ -1750510317987410954L) + 2015526157708405727L;
            ((int[])obj8)[11] = ((int[])obj8)[7];
            l11 = (l11 ^ 3025006228417949082L) - 1206101731319871396L;
            ((int[])obj8)[7] = ((int[])obj8)[3];
            l11 = l11 + 5049470690613723331L ^ -5527522687686956355L;
            ((int[])obj8)[3] = n7;
            l11 = (l11 ^ -2477902560202865937L) + -2941867480233587568L;
            n15 = 0;
            l11 = (l11 ^ -13702040934733565L) + -1947958079511633939L;

            while (n15 < 4) {
               l11 = (l11 ^ -5424281932306049322L) - -5480026125938740170L;
               n7 = ((int[])obj8)[n15 * 4];
               l11 = l11 + -3721448433253274662L ^ -1070189388169071315L;
               n = ((int[])obj8)[n15 * 4 + 1];
               l11 = l11 - -5629365771166001117L ^ 2265367531394687618L;
               n2 = ((int[])obj8)[n15 * 4 + 2];
               l11 = l11 - -1145970733809626666L ^ 984675056255515879L;
               n3 = ((int[])obj8)[n15 * 4 + 3];
               l11 = l11 + 4526374124323579235L ^ 2579066833608396748L;
               ((int[])obj8)[n15 * 4] = GF_MUL_TABLE[2][n7] ^ GF_MUL_TABLE[3][n] ^ n2 ^ n3;
               l11 = (l11 ^ -3152268419753978186L) + -5995329452789225473L;
               ((int[])obj8)[n15 * 4 + 1] = n7 ^ GF_MUL_TABLE[2][n] ^ GF_MUL_TABLE[3][n2] ^ n3;
               l11 = l11 - -3276910075110238708L ^ -8752509952276859778L;
               ((int[])obj8)[n15 * 4 + 2] = n7 ^ n ^ GF_MUL_TABLE[2][n2] ^ GF_MUL_TABLE[3][n3];
               l11 = l11 - -1333558959075303381L ^ 697730938505180545L;
               ((int[])obj8)[n15 * 4 + 3] = GF_MUL_TABLE[3][n7] ^ n ^ n2 ^ GF_MUL_TABLE[2][n3];
               l11 = l11 + -5610409639694531068L ^ -3522597425352207567L;
               n15++;
               l11 = l11 - -5935463876015719640L ^ -4373652165676831866L;
               l11 = l11 + -8922435052817618986L ^ -7264342270771856786L;
            }

            l11 = l11 - 268405315757266052L ^ 519861386925093318L;
            n15 = n19 * 16;
            l11 = (l11 ^ 3729903025668835307L) + -3829838362977113150L;
            n16 = 0;
            l11 = (l11 ^ 5786677310374754673L) + -2940854896447241787L;

            while (n16 < 16) {
               l11 = (l11 ^ -3224015129442309866L) - -1911676112129351064L;
               ((int[])obj8)[n16] = ((int[])obj8)[n16] ^ ints[n15 + n16];
               l11 = (l11 ^ -666124034287894206L) - 8204486118997284390L;
               n16++;
               l11 = (l11 ^ 2635722045245587955L) + -5517308542407233829L;
               l11 = (l11 ^ 653500473305298629L) - 4052218450265599277L;
            }

            l11 = (l11 ^ 2120663504094163066L) - -5204910340433887477L;
            n19++;
            l11 = l11 - 1246847414733248527L ^ 9075619177646458502L;
            l11 = (l11 ^ -2162537567701161166L) + 2003038865953978634L;
         }

         l11 = l11 - -3853342468036535203L ^ -1135097804947363064L;
         n19 = 0;
         l11 = (l11 ^ 6323899995419489517L) - -6911001219116293789L;

         while (n19 < 16) {
            l11 = l11 - 1394484543431494046L ^ 395679084118037430L;
            ((int[])obj8)[n19] = SBOX[((int[])obj8)[n19]];
            l11 = (l11 ^ -974979356530793519L) + 8751379022259266906L;
            n19++;
            l11 = l11 + 6239540676310660728L ^ 8396732105839536333L;
            l11 = (l11 ^ -4779252042264665304L) + -3662882471996206638L;
         }

         l11 = (l11 ^ -8637111054193986028L) + -3720263458150254004L;
         n19 = ((int[])obj8)[1];
         l11 = l11 + -7335703872755597273L ^ 198306705564644660L;
         ((int[])obj8)[1] = ((int[])obj8)[5];
         l11 = (l11 ^ -2956588678907765817L) - 3611699359130461902L;
         ((int[])obj8)[5] = ((int[])obj8)[9];
         l11 = (l11 ^ 8918638270434826411L) - -5843791912706243466L;
         ((int[])obj8)[9] = ((int[])obj8)[13];
         l11 = l11 + -2527202340040307025L ^ -6249744106707026993L;
         ((int[])obj8)[13] = n19;
         l11 = (l11 ^ 1664200258942214467L) - 4461781892108317449L;
         n19 = ((int[])obj8)[2];
         l11 = l11 - 873973557961933163L ^ -3644034493650105852L;
         n7 = ((int[])obj8)[6];
         l11 = (l11 ^ -2525923330103456979L) + 4942323117730485562L;
         ((int[])obj8)[2] = ((int[])obj8)[10];
         l11 = (l11 ^ -3620476707554344858L) - 5364459400656470801L;
         ((int[])obj8)[6] = ((int[])obj8)[14];
         l11 = l11 + 4343157742470562626L ^ -7199146857575956854L;
         ((int[])obj8)[10] = n19;
         l11 = (l11 ^ -1639326975651405014L) - 551867390615966416L;
         ((int[])obj8)[14] = n7;
         l11 = l11 - 2915794479909315064L ^ 1264560148200715006L;
         n19 = ((int[])obj8)[15];
         l11 = (l11 ^ -8763269143229558592L) + 6346103662460677092L;
         ((int[])obj8)[15] = ((int[])obj8)[11];
         l11 = (l11 ^ -3408224698392187275L) + -5134344245123942184L;
         ((int[])obj8)[11] = ((int[])obj8)[7];
         l11 = (l11 ^ -1564128378751967568L) + 2429115774245952832L;
         ((int[])obj8)[7] = ((int[])obj8)[3];
         l11 = l11 - 830497436970004739L ^ 9025523972302224471L;
         ((int[])obj8)[3] = n19;
         l11 = l11 - -7219355570985501286L ^ -1858794407195570819L;
         n = n20 * 16;
         l11 = (l11 ^ -4445568611154932474L) + 7837407561056048277L;
         n2 = 0;
         l11 = (l11 ^ -6191696279947789705L) - -7085710079016160735L;

         while (n2 < 16) {
            l11 = (l11 ^ -5340162510126166327L) - -3299022679060337257L;
            ((int[])obj8)[n2] = ((int[])obj8)[n2] ^ ints[n + n2];
            l11 = l11 - 6335721654760184534L ^ -1856503064258356665L;
            n2++;
            l11 = l11 - -1916370749797339330L ^ 5705336396858675584L;
            l11 = l11 - -8775131978674953694L ^ 5258417030647562077L;
         }

         l11 = l11 + 7796336743116550958L ^ 1623174796428438688L;
         n2 = Math.min(16, bytes.length - n23);
         l11 = (l11 ^ 5516094588009458631L) - -6203543921423460702L;
         n3 = 0;
         l11 = (l11 ^ -7108785496346468032L) - 848566335107960305L;

         while (n3 < n2) {
            l11 = (l11 ^ 5061663518941551348L) + -5448370375957379074L;
            ((byte[])obj9)[n23 + n3] = (byte)(bytes[n23 + n3] ^ (byte)((int[])obj8)[n3]);
            l11 = l11 - 6945193408132125965L ^ -500920135509122060L;
            n3++;
            l11 = (l11 ^ -4148154757972903963L) - -4000176297958085506L;
            l11 = (l11 ^ 5459372479445878927L) - -2948356942943621819L;
         }

         l11 = l11 + -2363677714509042996L ^ -3064416292071262229L;
         n3 = ((byte[])obj3).length - 1;
         l11 = l11 - 4915692984371918906L ^ 7886338331559853722L;

         exitBlock3: {
            while (n3 >= 12) {
               l11 = (l11 ^ 1482482330995274928L) - 414546787446400564L;
               if (++((byte[])obj3)[n3] != 0) {
                  l11 = (l11 ^ -7823542108378613051L) + -3842742468303154021L;
                  l11 = l11 - 1997451897925583421L ^ 7995005666225801865L;
                  break exitBlock3;
               }

               l11 = (l11 ^ -2262970917861329543L) - 8720015865508879433L;
               n3--;
               l11 = (l11 ^ -3698773783149376612L) - -5582078209062629590L;
               l11 = (l11 ^ 1372065002438585490L) + -9023639245115525520L;
            }

            l11 = (l11 ^ -3922662425496025079L) - 7721947109325720547L;
         }

         n23 += 16;
         l11 = l11 - -9028025308146032121L ^ -3757961931568590235L;
         l11 = (l11 ^ 7093906574405351009L) - 973414786975385525L;
      }

      l11 = (l11 ^ 4083481327646349889L) + 7299580342402263849L;
      int n24;
      if (bytes3 == null) {
         l11 = (l11 ^ -7666640727391613682L) - 6526002935112100066L;
         n24 = 0;
      } else {
         l11 = l11 - 6554262276034077195L ^ -5619893478246114787L;
         n24 = bytes3.length;
      }

      exitBlock4: {
         n23 = n24;
         if (this.ghashTable != null) {
            byte[] bytes4 = (byte[])obj6;
            if (this.ghashTable.matchesSubkey(bytes4)) {
               break exitBlock4;
            }
         }

         this.ghashTable = new VendorAesGcmGHashTable((byte[])obj6);
      }

      obj12 = new byte[16];
      if (bytes3 != null && n23 > 0) {
         n19 = (n23 + 16 - 1) / 16;

         for (int i = 0; i < n19; i++) {
            n = Math.min(16, n23 - i * 16);

            for (int j = 0; j < n; j++) {
               ((byte[])obj12)[j] = (byte)(((byte[])obj12)[j] ^ bytes3[i * 16 + j]);
            }

            l3 = 0L;
            l = 0L;

            for (int k = 0; k < 16; k++) {
               n6 = ((byte[])obj12)[k] & 255;
               if (n6 != 0) {
                  l8 = this.ghashTable.mulTable[k][n6];
                  l3 ^= l8 >>> 32;
                  l ^= l8 & 4294967295L;
               }
            }

            for (int m = 0; m < 8; m++) {
               ((byte[])obj12)[m] = (byte)((int)(l3 >>> 56 - m * 8 & 255L));
               ((byte[])obj12)[m + 8] = (byte)((int)(l >>> 56 - m * 8 & 255L));
            }
         }
      }

      if (((byte[])obj9).length > 0) {
         n19 = (((byte[])obj9).length + 16 - 1) / 16;

         for (int idx = 0; idx < n19; idx++) {
            short s = 30770;
            n = Math.min(16, ((byte[])obj9).length - idx * 16);

            for (int index = 0; index < n; index++) {
               ((byte[])obj12)[index] = (byte)(((byte[])obj12)[index] ^ ((byte[])obj9)[idx * 16 + index]);
            }

            l3 = 0L;
            l = 0L;

            for (int i2 = 0; i2 < 16; i2++) {
               n6 = ((byte[])obj12)[i2] & 255;
               if (n6 != 0) {
                  l8 = this.ghashTable.mulTable[i2][n6];
                  l3 ^= l8 >>> 32;
                  l ^= l8 & 4294967295L;
               }
            }

            for (int i3 = 0; i3 < 8; i3++) {
               ((byte[])obj12)[i3] = (byte)((int)(l3 >>> 56 - i3 * 8 & 255L));
               ((byte[])obj12)[i3 + 8] = (byte)((int)(l >>> 56 - i3 * 8 & 255L));
            }
         }
      }

      obj7 = new byte[16];
      short s2 = 17510;
      l2 = (long)n23 * 8L;
      l3 = (long)((byte[])obj9).length * 8L;

      for (int i4 = 7; i4 >= 0; i4--) {
         ((byte[])obj7)[i4] = (byte)((int)(l2 & 255L));
         l2 >>>= 8;
         ((byte[])obj7)[i4 + 8] = (byte)((int)(l3 & 255L));
         l3 >>>= 8;
      }

      for (int i5 = 0; i5 < 16; i5++) {
         ((byte[])obj12)[i5] = (byte)(((byte[])obj12)[i5] ^ ((byte[])obj7)[i5]);
      }

      l = 0L;
      l10 = 0L;

      for (int i6 = 0; i6 < 16; i6++) {
         n14 = ((byte[])obj12)[i6] & 255;
         if (n14 != 0) {
            l5 = this.ghashTable.mulTable[i6][n14];
            l ^= l5 >>> 32;
            l10 ^= l5 & 4294967295L;
         }
      }

      for (int i7 = 0; i7 < 8; i7++) {
         ((byte[])obj12)[i7] = (byte)((int)(l >>> 56 - i7 * 8 & 255L));
         ((byte[])obj12)[i7 + 8] = (byte)((int)(l10 >>> 56 - i7 * 8 & 255L));
      }

      obj5 = new byte[16];
      System.arraycopy((byte[])obj4, 0, (byte[])obj5, 0, ((byte[])obj4).length);
      ((byte[])obj5)[15] = 1;

      for (int i8 = 0; i8 < 16; i8++) {
         this.counterBlockStateBuffer[i8] = ((byte[])obj5)[i8] & 255;
      }

      n14 = ints.length / 16 - 1;

      for (int i9 = 0; i9 < 16; i9++) {
         this.counterBlockStateBuffer[i9] = this.counterBlockStateBuffer[i9] ^ ints[i9];
      }

      for (int i10 = 1; i10 < n14; i10++) {
         for (int i11 = 0; i11 < 16; i11++) {
            this.counterBlockStateBuffer[i11] = SBOX[this.counterBlockStateBuffer[i11]];
         }

         n5 = this.counterBlockStateBuffer[1];
         this.counterBlockStateBuffer[1] = this.counterBlockStateBuffer[5];
         this.counterBlockStateBuffer[5] = this.counterBlockStateBuffer[9];
         this.counterBlockStateBuffer[9] = this.counterBlockStateBuffer[13];
         this.counterBlockStateBuffer[13] = n5;
         n5 = this.counterBlockStateBuffer[2];
         n11 = this.counterBlockStateBuffer[6];
         this.counterBlockStateBuffer[2] = this.counterBlockStateBuffer[10];
         this.counterBlockStateBuffer[6] = this.counterBlockStateBuffer[14];
         this.counterBlockStateBuffer[10] = n5;
         this.counterBlockStateBuffer[14] = n11;
         n5 = this.counterBlockStateBuffer[15];
         this.counterBlockStateBuffer[15] = this.counterBlockStateBuffer[11];
         this.counterBlockStateBuffer[11] = this.counterBlockStateBuffer[7];
         this.counterBlockStateBuffer[7] = this.counterBlockStateBuffer[3];
         this.counterBlockStateBuffer[3] = n5;

         for (int i12 = 0; i12 < 4; i12++) {
            n5 = this.counterBlockStateBuffer[i12 * 4];
            n11 = this.counterBlockStateBuffer[i12 * 4 + 1];
            n9 = this.counterBlockStateBuffer[i12 * 4 + 2];
            n18 = this.counterBlockStateBuffer[i12 * 4 + 3];
            this.counterBlockStateBuffer[i12 * 4] = GF_MUL_TABLE[2][n5] ^ GF_MUL_TABLE[3][n11] ^ n9 ^ n18;
            this.counterBlockStateBuffer[i12 * 4 + 1] = n5 ^ GF_MUL_TABLE[2][n11] ^ GF_MUL_TABLE[3][n9] ^ n18;
            this.counterBlockStateBuffer[i12 * 4 + 2] = n5 ^ n11 ^ GF_MUL_TABLE[2][n9] ^ GF_MUL_TABLE[3][n18];
            this.counterBlockStateBuffer[i12 * 4 + 3] = GF_MUL_TABLE[3][n5] ^ n11 ^ n9 ^ GF_MUL_TABLE[2][n18];
         }

         n4 = i10 * 16;

         for (int i13 = 0; i13 < 16; i13++) {
            this.counterBlockStateBuffer[i13] = this.counterBlockStateBuffer[i13] ^ ints[n4 + i13];
         }
      }

      for (int i14 = 0; i14 < 16; i14++) {
         this.counterBlockStateBuffer[i14] = SBOX[this.counterBlockStateBuffer[i14]];
      }

      n21 = this.counterBlockStateBuffer[1];
      this.counterBlockStateBuffer[1] = this.counterBlockStateBuffer[5];
      this.counterBlockStateBuffer[5] = this.counterBlockStateBuffer[9];
      this.counterBlockStateBuffer[9] = this.counterBlockStateBuffer[13];
      this.counterBlockStateBuffer[13] = n21;
      n21 = this.counterBlockStateBuffer[2];
      n5 = this.counterBlockStateBuffer[6];
      this.counterBlockStateBuffer[2] = this.counterBlockStateBuffer[10];
      this.counterBlockStateBuffer[6] = this.counterBlockStateBuffer[14];
      this.counterBlockStateBuffer[10] = n21;
      this.counterBlockStateBuffer[14] = n5;
      n21 = this.counterBlockStateBuffer[15];
      this.counterBlockStateBuffer[15] = this.counterBlockStateBuffer[11];
      this.counterBlockStateBuffer[11] = this.counterBlockStateBuffer[7];
      this.counterBlockStateBuffer[7] = this.counterBlockStateBuffer[3];
      this.counterBlockStateBuffer[3] = n21;
      n11 = n14 * 16;

      for (int i15 = 0; i15 < 16; i15++) {
         this.counterBlockStateBuffer[i15] = this.counterBlockStateBuffer[i15] ^ ints[n11 + i15];
      }

      for (int i16 = 0; i16 < 16; i16++) {
         this.counterBlockByteBuffer[i16] = (byte)this.counterBlockStateBuffer[i16];
      }

      obj10 = new byte[16];

      for (int i17 = 0; i17 < 16; i17++) {
         ((byte[])obj10)[i17] = (byte)(((byte[])obj12)[i17] ^ this.counterBlockByteBuffer[i17]);
      }

      obj = (byte[])obj10;
      obj11 = new byte[((byte[])obj4).length + ((byte[])obj9).length + ((byte[])obj).length];
      System.arraycopy((byte[])obj4, 0, (byte[])obj11, 0, ((byte[])obj4).length);
      System.arraycopy((byte[])obj9, 0, (byte[])obj11, ((byte[])obj4).length, ((byte[])obj9).length);
      System.arraycopy((byte[])obj, 0, (byte[])obj11, ((byte[])obj4).length + ((byte[])obj9).length, ((byte[])obj).length);
      return (byte[])obj11;
   }

   private byte[] computeHashSubkey(byte[] bytes, long l, int[] ints) {
      int n = 0;
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      boolean flag = false;
      int n5 = 0;
      int n6 = 0;
      int n7 = 0;
      if (this.cachedKey != null && this.cachedHashSubkey != null) {
         n5 = 1;

         for (int i = 0; i < ((byte[])bytes).length; i++) {
            if (bytes[i] != this.cachedKey[i]) {
               n5 = 0;
               break;
            }
         }

         if (n5 != 0) {
            return this.cachedHashSubkey;
         }
      }

      for (int j = 0; j < 16; j++) {
         this.hashSubkeyStateBuffer[j] = 0;
      }

      n5 = ints.length / 16 - 1;

      for (int k = 0; k < 16; k++) {
         this.hashSubkeyStateBuffer[k] = this.hashSubkeyStateBuffer[k] ^ ints[k];
      }

      for (int m = 1; m < n5; m++) {
         for (int idx = 0; idx < 16; idx++) {
            this.hashSubkeyStateBuffer[idx] = SBOX[this.hashSubkeyStateBuffer[idx]];
         }

         n4 = this.hashSubkeyStateBuffer[1];
         this.hashSubkeyStateBuffer[1] = this.hashSubkeyStateBuffer[5];
         this.hashSubkeyStateBuffer[5] = this.hashSubkeyStateBuffer[9];
         this.hashSubkeyStateBuffer[9] = this.hashSubkeyStateBuffer[13];
         this.hashSubkeyStateBuffer[13] = n4;
         n4 = this.hashSubkeyStateBuffer[2];
         n2 = this.hashSubkeyStateBuffer[6];
         this.hashSubkeyStateBuffer[2] = this.hashSubkeyStateBuffer[10];
         this.hashSubkeyStateBuffer[6] = this.hashSubkeyStateBuffer[14];
         this.hashSubkeyStateBuffer[10] = n4;
         this.hashSubkeyStateBuffer[14] = n2;
         n4 = this.hashSubkeyStateBuffer[15];
         this.hashSubkeyStateBuffer[15] = this.hashSubkeyStateBuffer[11];
         this.hashSubkeyStateBuffer[11] = this.hashSubkeyStateBuffer[7];
         this.hashSubkeyStateBuffer[7] = this.hashSubkeyStateBuffer[3];
         this.hashSubkeyStateBuffer[3] = n4;

         for (int index = 0; index < 4; index++) {
            n4 = this.hashSubkeyStateBuffer[index * 4];
            n2 = this.hashSubkeyStateBuffer[index * 4 + 1];
            n6 = this.hashSubkeyStateBuffer[index * 4 + 2];
            n3 = this.hashSubkeyStateBuffer[index * 4 + 3];
            this.hashSubkeyStateBuffer[index * 4] = GF_MUL_TABLE[2][n4] ^ GF_MUL_TABLE[3][n2] ^ n6 ^ n3;
            this.hashSubkeyStateBuffer[index * 4 + 1] = n4 ^ GF_MUL_TABLE[2][n2] ^ GF_MUL_TABLE[3][n6] ^ n3;
            this.hashSubkeyStateBuffer[index * 4 + 2] = n4 ^ n2 ^ GF_MUL_TABLE[2][n6] ^ GF_MUL_TABLE[3][n3];
            this.hashSubkeyStateBuffer[index * 4 + 3] = GF_MUL_TABLE[3][n4] ^ n2 ^ n6 ^ GF_MUL_TABLE[2][n3];
         }

         n7 = m * 16;

         for (int i2 = 0; i2 < 16; i2++) {
            this.hashSubkeyStateBuffer[i2] = this.hashSubkeyStateBuffer[i2] ^ ints[n7 + i2];
         }
      }

      for (int i3 = 0; i3 < 16; i3++) {
         this.hashSubkeyStateBuffer[i3] = SBOX[this.hashSubkeyStateBuffer[i3]];
      }

      n = this.hashSubkeyStateBuffer[1];
      this.hashSubkeyStateBuffer[1] = this.hashSubkeyStateBuffer[5];
      this.hashSubkeyStateBuffer[5] = this.hashSubkeyStateBuffer[9];
      this.hashSubkeyStateBuffer[9] = this.hashSubkeyStateBuffer[13];
      this.hashSubkeyStateBuffer[13] = n;
      n = this.hashSubkeyStateBuffer[2];
      n4 = this.hashSubkeyStateBuffer[6];
      this.hashSubkeyStateBuffer[2] = this.hashSubkeyStateBuffer[10];
      this.hashSubkeyStateBuffer[6] = this.hashSubkeyStateBuffer[14];
      this.hashSubkeyStateBuffer[10] = n;
      this.hashSubkeyStateBuffer[14] = n4;
      n = this.hashSubkeyStateBuffer[15];
      this.hashSubkeyStateBuffer[15] = this.hashSubkeyStateBuffer[11];
      this.hashSubkeyStateBuffer[11] = this.hashSubkeyStateBuffer[7];
      this.hashSubkeyStateBuffer[7] = this.hashSubkeyStateBuffer[3];
      this.hashSubkeyStateBuffer[3] = n;
      n2 = n5 * 16;

      for (int i4 = 0; i4 < 16; i4++) {
         this.hashSubkeyStateBuffer[i4] = this.hashSubkeyStateBuffer[i4] ^ ints[n2 + i4];
      }

      for (int i5 = 0; i5 < 16; i5++) {
         this.hashSubkeyByteBuffer[i5] = (byte)this.hashSubkeyStateBuffer[i5];
      }

      if (this.cachedHashSubkey == null) {
         this.cachedHashSubkey = new byte[16];
      }

      System.arraycopy(this.hashSubkeyByteBuffer, 0, this.cachedHashSubkey, 0, 16);
      return this.hashSubkeyByteBuffer;
   }

   private static String bytesToHex(byte[] bytes, int n, int n2) {
      boolean flag = false;
      Object obj = null;
      obj = new StringBuilder();

      for (int i = n; i < n + n2; i++) {
         ((StringBuilder)obj).append(String.format("%02X", bytes[i]));
      }

      return ((StringBuilder)obj).toString();
   }

   private int[] expandKey(byte[] bytes, long l) {
      int n = 0;
      int n2 = 0;
      Object obj = null;
      boolean flag = false;
      int n3 = 0;
      Object obj2 = null;
      byte b = 0;
      if (this.cachedKey != null && bytes.length == this.cachedKey.length) {
         b = 1;

         for (int i = 0; i < ((byte[])bytes).length; i++) {
            if (bytes[i] != this.cachedKey[i]) {
               b = 0;
               break;
            }
         }

         if (b != 0 && this.cachedRoundKeys != null) {
            return this.cachedRoundKeys;
         }
      }

      if (bytes.length != 16) {
         if (bytes.length != 24) {
            b = 14;
         } else {
            b = 12;
         }
      } else {
         b = 10;
      }

      n2 = bytes.length / 4;
      n3 = 4 * (b + 1);
      obj = new int[n3];

      for (int j = 0; j < n2; j++) {
         ((int[])obj)[j] = (bytes[4 * j] & 255) << 24
            | (bytes[4 * j + 1] & 255) << 16
            | (bytes[4 * j + 2] & 255) << 8
            | bytes[4 * j + 3] & 255;
      }

      for (int k = n2; k < n3; k++) {
         n = ((int[])obj)[k - 1];
         if (k % n2 != 0) {
            if (n2 > 6 && k % n2 == 4) {
               n = SBOX[n >>> 24 & 0xFF] << 24 | SBOX[n >>> 16 & 0xFF] << 16 | SBOX[n >>> 8 & 0xFF] << 8 | SBOX[n & 0xFF];
            }
         } else {
            n = n << 8 & -256 | n >>> 24 & 0xFF;
            n = SBOX[n >>> 24 & 0xFF] << 24 | SBOX[n >>> 16 & 0xFF] << 16 | SBOX[n >>> 8 & 0xFF] << 8 | SBOX[n & 0xFF];
            n ^= RCON[k / n2] << 24;
         }

         ((int[])obj)[k] = ((int[])obj)[k - n2] ^ n;
      }

      obj2 = new int[n3 * 4];

      for (int m = 0; m < n3; m++) {
         ((int[])obj2)[4 * m] = ((int[])obj)[m] >>> 24 & 0xFF;
         ((int[])obj2)[4 * m + 1] = ((int[])obj)[m] >>> 16 & 0xFF;
         ((int[])obj2)[4 * m + 2] = ((int[])obj)[m] >>> 8 & 0xFF;
         ((int[])obj2)[4 * m + 3] = ((int[])obj)[m] & 0xFF;
      }

      this.cachedKey = new byte[((byte[])bytes).length];
      System.arraycopy(bytes, 0, this.cachedKey, 0, bytes.length);
      this.cachedRoundKeys = (int[])obj2;
      return (int[])obj2;
   }

   public byte[] decrypt(byte[] bytes, byte[] bytes2) {
      return this.decrypt(bytes, bytes2, (byte[])null);
   }

   private void ghashMultiply(long l, byte[] bytes) {
      long l2 = 0L;
      boolean flag = false;
      int n = 0;
      long l3 = 0L;
      long l4 = 0L;
      l2 = 0L;
      l4 = 0L;

      for (int i = 0; i < 16; i++) {
         n = bytes[i] & 255;
         if (n != 0) {
            l3 = this.ghashTable.mulTable[i][n];
            l2 ^= l3 >>> 32;
            l4 ^= l3 & 4294967295L;
         }
      }

      for (int j = 0; j < 8; j++) {
         bytes[j] = (byte)((int)(l2 >>> 56 - j * 8 & 255L));
         bytes[j + 8] = (byte)((int)(l4 >>> 56 - j * 8 & 255L));
      }
   }

   public byte[] encrypt(byte[] bytes, byte[] bytes2) {
      return this.encrypt(bytes, bytes2, (byte[])null);
   }

}
