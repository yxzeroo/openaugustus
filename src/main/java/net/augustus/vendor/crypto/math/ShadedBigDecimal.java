package net.augustus.vendor.crypto.math;

import net.augustus.vendor.crypto.CustomBigInteger;
import java.util.Arrays;

public class ShadedBigDecimal extends Number implements Comparable {
   public static final ShadedBigDecimal ONE;
   public static final int ROUND_UP = 0;
   private static final double[] double10pow = new double[]{
      1.0,
      10.0,
      100.0,
      1000.0,
      10000.0,
      100000.0,
      1000000.0,
      1.0E7,
      1.0E8,
      1.0E9,
      1.0E10,
      1.0E11,
      1.0E12,
      1.0E13,
      1.0E14,
      1.0E15,
      1.0E16,
      1.0E17,
      1.0E18,
      1.0E19,
      1.0E20,
      1.0E21,
      1.0E22
   };
   private static final ShadedBigDecimal[] zeroThroughTen;
   private final transient long intCompact;
   private static final long HALF_LONG_MAX_VALUE = 0L;
   public static final int ROUND_CEILING = 0;
   private static final long[] LONG_TEN_POWERS_TABLE = new long[]{
      1L,
      10L,
      100L,
      1000L,
      10000L,
      100000L,
      1000000L,
      10000000L,
      100000000L,
      1000000000L,
      10000000000L,
      100000000000L,
      1000000000000L,
      10000000000000L,
      100000000000000L,
      1000000000000000L,
      10000000000000000L,
      100000000000000000L,
      1000000000000000000L
   };
   private static final float[] float10pow = new float[]{1.0F, 10.0F, 100.0F, 1000.0F, 10000.0F, 100000.0F, 1000000.0F, 1.0E7F, 1.0E8F, 1.0E9F, 1.0E10F};
   private final int scale;
   private static final CustomBigInteger INFLATED_BIGINT;
   private static final int BIG_TEN_POWERS_TABLE_MAX;
   public static final int ROUND_FLOOR = 0;
   private transient int precision;
   public static final long INFLATED = 0L;
   private final CustomBigInteger intVal;
   private static final ThreadLocal threadLocalStringBuilderHelper = new ShadedBigDecimalThreadLocalBuffer();
   public static final int ROUND_HALF_UP = 0;
   private static final long serialVersionUID = 0L;
   private static final long HALF_LONG_MIN_VALUE = 0L;
   private static final ShadedBigDecimal[] ZERO_SCALED_BY;
   public static final int ROUND_HALF_DOWN = 0;
   private static final int BIG_TEN_POWERS_TABLE_INITLEN;
   public static final ShadedBigDecimal TEN;
   private static final int MAX_COMPACT_DIGITS = 0;
   public static final ShadedBigDecimal ZERO;
   public static final boolean assertionsDisabled = !ShadedBigDecimal.class.desiredAssertionStatus();
   private static final long DIV_NUM_BASE = 0L;
   public static final int ROUND_HALF_EVEN = 0;
   private transient String stringCache;
   public static final int ROUND_DOWN = 1;
   private static final long[][] LONGLONG_TEN_POWERS_TABLE = new long[][]{
      {0L, -8446744073709551616L},
      {5L, 7766279631452241920L},
      {54L, 3875820019684212736L},
      {542L, 1864712049423024128L},
      {5421L, 200376420520689664L},
      {54210L, 2003764205206896640L},
      {542101L, 1590897978359414784L},
      {5421010L, -2537764290115403776L},
      {54210108L, -6930898827444486144L},
      {542101086L, 4477988020393345024L},
      {5421010862L, 7886392056514347008L},
      {54210108624L, 5076944270305263616L},
      {542101086242L, -4570789518076018688L},
      {5421010862427L, -8814407033341083648L},
      {54210108624275L, 4089650035136921600L},
      {542101086242752L, 4003012203950112768L},
      {5421010862427522L, 3136633892082024448L},
      {54210108624275221L, -5527149226598858752L},
      {542101086242752217L, 68739955140067328L},
      {5421010862427522170L, 687399551400673280L}
   };
   public static final int ROUND_UNNECESSARY = 0;
   private static volatile CustomBigInteger[] BIG_TEN_POWERS_TABLE;
   private static final long[] THRESHOLDS_TABLE = new long[]{
      9223372036854775807L,
      922337203685477580L,
      92233720368547758L,
      9223372036854775L,
      922337203685477L,
      92233720368547L,
      9223372036854L,
      922337203685L,
      92233720368L,
      9223372036L,
      922337203L,
      92233720L,
      9223372L,
      922337L,
      92233L,
      9223L,
      922L,
      92L,
      9L
   };

   public ShadedBigDecimal max(ShadedBigDecimal shadedBigDecimal) {
      return this.compareTo2(shadedBigDecimal) >= 0 ? this : shadedBigDecimal;
   }

   private static ShadedBigDecimal divideAndRound(long l, long l2, int n, int n2, int n3) {
      int n4 = 0;
      long l3 = 0L;
      boolean flag = false;
      long l4 = 0L;
      byte b = 1;
      long l5 = 0L;
      l5 = 1355168526367229028L ^ (long)"틻〜᷺樤캘\ued01黐麶\uebb1䪥헯ᔤ톱鄫닖ͧ삠䖴ࣵ癑\udc56瓲\uf79d\uf7c9".hashCode();

      boolean flag2;
      outerLoop:
      while (true) {
         if (b <= 3) {
            if (l >= 0L) {
               l5 = l5 + -5447772821046431581L ^ 1989501310107768892L;
               flag2 = false;
               break;
            }

            l5 = l5 + -7672858785758499970L ^ 4377156667134222105L;
         } else if (5 <= b) {
            if (5 < b) {
               return valueOf(l4, n);
            }

            l4 = l / l2;
            l5 = l5 - 3839085386113553743L ^ -5280403793350170170L;
         } else {
            if (n == n3) {
               l5 = (l5 ^ -7377154421456517602L) + -8874244082385199367L;
               continue;
            }

            l5 = (l5 ^ -9125751804875906512L) + 2813046150799068939L;
         }

         while (b >= 1) {
            if (b <= 1) {
               flag2 = true;
               break outerLoop;
            }

            if (n2 == 1) {
               l5 = (l5 ^ -6030803310926326613L) + -2059852307242438925L;
               continue outerLoop;
            }

            l5 = l5 - -6371834286982833000L ^ -7987403254640260239L;
         }

         l3 = l % l2;
         l5 = (l5 ^ -1424636595987944777L) + 8102912719195711263L;
      }

      n4 = flag2 == l2 < 0L ? 1 : -1;
      if (l3 != 0L) {
         flag = needIncrement2(l2, n2, n4, l4, l3);
         return valueOf(flag ? l4 + (long)n4 : l4, n);
      } else {
         return n3 != n ? createAndStripZerosToMatchScale2(l4, n, (long)n3) : valueOf(l4, n);
      }
   }

   private static long divideAndRound3(long l, long l2, int n) {
      long l3 = 0L;
      int n2 = 0;
      long l4 = 0L;
      boolean flag = false;
      byte b = 3;
      long l5 = 0L;
      l5 = -5747936970741940026L ^ (long)"\ude66\uf452娬숎蓡傗⡴\uddfa\uff00讯Ó㘜戄椘뺑鿷渇쬭ⲥ\uf1eaჼ".hashCode();

      boolean flag2;
      while (true) {
         if (b <= 2) {
            l4 = l % l2;
            l5 = (l5 ^ -8279224906532479796L) - -7853951401894987897L;
            if (l >= 0L) {
               l5 = (l5 ^ 3544152742935666665L) + 8797751612701063490L;
               flag2 = false;
               break;
            }

            l5 = (l5 ^ -7452690849311841314L) - -2493949135405001999L;
         } else {
            if (b < 4) {
               flag2 = true;
               break;
            }

            if (b <= 4) {
               return l3;
            }

            l3 = l / l2;
            l5 = (l5 ^ -6826515921867010620L) + -794010873576071049L;
            if (n != 1) {
               l5 = (l5 ^ 7457781146581361744L) + 6708079766573666016L;
            } else {
               l5 = l5 + 5768005153884710478L ^ -356706000119574043L;
            }
         }
      }

      n2 = flag2 == l2 < 0L ? 1 : -1;
      if (l4 != 0L) {
         flag = needIncrement2(l2, n, n2, l3, l4);
         return flag ? l3 + (long)n2 : l3;
      } else {
         return l3;
      }
   }

   public ShadedBigDecimal movePointRight(int n) {
      Object obj = null;
      int n2 = 0;
      ShadedBigDecimal shadedBigDecimal = this;
      n2 = shadedBigDecimal.checkScale((long)this.scale - (long)n);
      obj = new ShadedBigDecimal(this.intVal, this.intCompact, n2, 0);
      if (((ShadedBigDecimal)obj).scale >= 0) {
         shadedBigDecimal = (ShadedBigDecimal)obj;
      } else {
         shadedBigDecimal = (ShadedBigDecimal)obj;
         shadedBigDecimal = shadedBigDecimal.setScale2((byte)0, (byte)7);
      }

      return shadedBigDecimal;
   }

   private static int longCompareMagnitude(long l, long l2) {
      byte b = 2;
      long l3 = 0L;
      l3 = -8650254317502495923L ^ (long)"긢ꔷ탰⇒乍䂠傫奟왠슭㿮颊䯏돱\ueca1\uf6bf沗ꛬ᧔ग़镄න攭窊攪狔".hashCode();
      if (l >= 0L) {
         l3 = (l3 ^ -6291321294879475752L) + -7383742834744258071L;
      } else {
         l3 = (l3 ^ 6522990410934553626L) - -5013086462976013113L;
      }

      int n;
      while (true) {
         while (2 >= b) {
            l = -l;
            l3 = l3 - -4962848493634196286L ^ -5823256913529187500L;
         }

         if (b >= 4) {
            if (b <= 4) {
               n = -1;
               break;
            }

            if (l2 >= 0L) {
               l3 = l3 - 5649190002857874849L ^ -1542566883626592692L;
            } else {
               l3 = (l3 ^ 8730838487023497064L) + -1408287245828176985L;
               l2 = -l2;
               l3 = (l3 ^ 6568510668346745969L) - 3783389477995627475L;
            }
         } else {
            if (l >= l2) {
               l3 = (l3 ^ 1709651874828523596L) - -7938849375291011268L;
               n = l == l2 ? 0 : 1;
               break;
            }

            l3 = (l3 ^ -2063193428350285789L) - 9095991134368915285L;
         }
      }

      return n;
   }

   private static boolean needIncrement3(ShadedMutableBigInteger shadedMutableBigInteger, int n, int n2, ShadedMutableBigInteger shadedMutableBigInteger2, ShadedMutableBigInteger shadedMutableBigInteger3) {
      int n3 = 0;
      byte b = 1;
      if (!assertionsDisabled) {
         ;
      }

      do {
         while (b > 2) {
            if (b > 3) {
               return commonNeedIncrement(n, n2, n3, shadedMutableBigInteger2.isOdd());
            }

            n3 = shadedMutableBigInteger3.compareHalf(shadedMutableBigInteger);
         }
      } while (!shadedMutableBigInteger3.isZero());

      throw new AssertionError();
   }

   private String layoutChars(boolean flag) {
      long l;
      int n = 0;
      boolean flag2 = false;
      Object obj = null;
      boolean flag3 = false;
      long l2 = 0L;
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      Object obj2 = null;
      int n5 = 0;
      Object obj3 = null;
      long l3 = 0L;
      l3 = -4609547298235603649L ^ (long)"耵雴琬这꩘憎⻙뇯\uea60ⶅ좭㙈ꀋ쭳⨽噺\uea9d㢲\ue85c㎷ᔂᮆ罷ᔂ苸".hashCode();
      if (this.scale == 0) {
         l3 = (l3 ^ 8056281295208675686L) - 3423175903710898032L;
         String str;
         if (this.intCompact == -9223372036854775808L) {
            l3 = l3 + 3555236893002278216L ^ 5188667292308244837L;
            str = this.intVal.toString();
         } else {
            l3 = (l3 ^ 8267856243169500339L) + 479836595921242234L;
            str = Long.toString(this.intCompact);
         }

         return str;
      } else {
         l3 = (l3 ^ 1850407888720601683L) + 2302505581002976232L;
         if (this.scale == 2 && this.intCompact >= 0L && this.intCompact < 2147483647L) {
            n = (int)this.intCompact % 100;
            n3 = (int)this.intCompact / 100;
            return Integer.toString(n3) + '.' + ShadedBigDecimalStringHelper.DIGIT_TENS[n] + ShadedBigDecimalStringHelper.DIGIT_ONES[n];
         } else {
            obj3 = (ShadedBigDecimalStringHelper)threadLocalStringBuilderHelper.get();
            if (this.intCompact != -9223372036854775808L) {
               ShadedBigDecimalStringHelper helper = (ShadedBigDecimalStringHelper)obj3;
               l = Math.abs(this.intCompact);
               n5 = helper.putIntCompact(l);
               obj2 = ((ShadedBigDecimalStringHelper)obj3).getCmpCharArray();
            } else {
               n5 = 0;
               obj2 = this.intVal.abs().toString().toCharArray();
            }

            obj = ((ShadedBigDecimalStringHelper)obj3).getStringBuilder();
            if (this.signum() < 0) {
               ((StringBuilder)obj).append('-');
            }

            n4 = ((char[])obj2).length - n5;
            l2 = -((long)this.scale) + (long)(n4 - 1);
            if (this.scale >= 0 && l2 >= -6L) {
               n2 = this.scale - n4;
               if (n2 < 0) {
                  ((StringBuilder)obj).append((char[])obj2, n5, -n2);
                  ((StringBuilder)obj).append('.');
                  ((StringBuilder)obj).append((char[])obj2, -n2 + n5, this.scale);
               } else {
                  ((StringBuilder)obj).append('0');
                  ((StringBuilder)obj).append('.');

                  while (n2 > 0) {
                     ((StringBuilder)obj).append('0');
                     n2--;
                  }

                  ((StringBuilder)obj).append((char[])obj2, n5, n4);
               }
            } else {
               if (flag) {
                  ((StringBuilder)obj).append(((char[])obj2)[n5]);
                  if (n4 > 1) {
                     ((StringBuilder)obj).append('.');
                     ((StringBuilder)obj).append((char[])obj2, n5 + 1, n4 - 1);
                  }
               } else {
                  n2 = (int)(l2 % 3L);
                  if (n2 < 0) {
                     n2 += 3;
                  }

                  l2 -= (long)n2;
                  n2++;
                  if (this.signum() == 0) {
                     if (n2 != 2) {
                        if (n2 != 1) {
                           if (n2 != 3) {
                              throw new AssertionError("Unexpected sig value " + n2);
                           }

                           ((StringBuilder)obj).append("0.0");
                           l2 += 3L;
                        } else {
                           ((StringBuilder)obj).append('0');
                        }
                     } else {
                        ((StringBuilder)obj).append("0.00");
                        l2 += 3L;
                     }
                  } else if (n2 < n4) {
                     ((StringBuilder)obj).append((char[])obj2, n5, n2);
                     ((StringBuilder)obj).append('.');
                     ((StringBuilder)obj).append((char[])obj2, n5 + n2, n4 - n2);
                  } else {
                     ((StringBuilder)obj).append((char[])obj2, n5, n4);

                     for (int i = n2 - n4; i > 0; i--) {
                        ((StringBuilder)obj).append('0');
                     }
                  }
               }

               if (l2 != 0L) {
                  ((StringBuilder)obj).append('E');
                  if (l2 > 0L) {
                     ((StringBuilder)obj).append('+');
                  }

                  ((StringBuilder)obj).append(l2);
               }
            }

            return ((StringBuilder)obj).toString();
         }
      }
   }

   public CustomBigInteger toBigInteger() {
      return this.setScale2((byte)0, (byte)1).inflated();
   }

   private static int compareMagnitudeNormalized2(CustomBigInteger customBigInteger, int n, CustomBigInteger customBigInteger2, int n2) {
      int n3 = 0;
      long l = 0L;
      l = -7616008257448988054L ^ (long)"䚴諆㣶Ǆꎩᔍ\ue67d㣥亚ꮶ蔯쏘鏢뤀⩚ᩓ\ued7aꯓ\ue6a5饜ϫ䐗".hashCode();
      n3 = n - n2;
      l = (l ^ -9045598588079394661L) + 4206192387264620525L;
      if (n3 >= 0) {
         l = (l ^ 1482879745320982698L) + -2353012793386205543L;
         return customBigInteger.compareMagnitude(bigMultiplyPowerTen2(customBigInteger2, n3));
      } else {
         l = (l ^ -2892322117498063800L) + 2094920801062154177L;
         return bigMultiplyPowerTen2(customBigInteger, -n3).compareMagnitude(customBigInteger2);
      }
   }

   private static int checkScale2(long l, long l2) {
      int n = 0;
      byte b = 2;
      long l3 = 0L;
      l3 = -5117957771434525564L ^ (long)"b톧愭츠闞㿊ᣗ榴➴㒜枷㌑貒淾鼻㰂㉖\uaac7녍堖嚊泔ᾖ\uf575页쩵⛶".hashCode();
      n = (int)l2;
      l3 = (l3 ^ 5842142003033227531L) + 1613481910368286762L;

      while (true) {
         int n2;
         if (b <= 1) {
            n2 = 2147483647;
         } else {
            if (2 < b) {
               if ((long)n == l2) {
                  l3 = l3 + -7270814811888318621L ^ 7916971677099556873L;
                  break;
               }

               l3 = l3 - 1918477115571880824L ^ 659168349409659370L;
               continue;
            }

            if (l2 > 2147483647L) {
               l3 = (l3 ^ 5214171664195360954L) + 5900492814218828674L;
               continue;
            }

            l3 = (l3 ^ 8025747999605057513L) - -8272999522947472591L;
            n2 = -2147483648;
         }

         n = n2;
         if (l != 0L) {
            throw new ArithmeticException(n > 0 ? "Underflow" : "Overflow");
         }
         break;
      }

      return n;
   }

   public ShadedBigDecimal subtract(ShadedBigDecimal shadedBigDecimal, ShadedMathContext shadedMathContext) {
      if (shadedMathContext.precision != 0) {
         return this.add(shadedBigDecimal.negate(), shadedMathContext);
      } else {
         return this.subtract(shadedBigDecimal);
      }
   }

   public ShadedBigDecimal stripTrailingZeros() {
      byte b = 6;
      long l = 0L;
      l = 2185224376369278722L ^ (long)"鞜⯾\u2feb暘⮋\u0a7c쪨红⊣\ue44fᢈഛ䱝酂꧟拋ᩩ螩쮗릍岧緊魾炶컊䷪".hashCode();
      if (this.intCompact == 0L) {
         l = l - -8890477864836713693L ^ 2369173233114642044L;
      } else {
         l = l - -8733160694989113702L ^ -8939489809944394354L;
         if (this.intVal == null) {
            l = l + -4374114216506591486L ^ -1333831132033425220L;
         } else {
            l = l + 2387776416561525822L ^ 829425098714930690L;
         }

         while (true) {
            while (3 < b) {
               if (5 > b) {
                  int scale2 = this.scale;
                  return createAndStripZerosToMatchScale(this.intVal, scale2, -9223372036854775808L);
               }

               if (b > 5) {
                  return createAndStripZerosToMatchScale2(this.intCompact, this.scale, -9223372036854775808L);
               }

               if (this.intCompact == -9223372036854775808L) {
                  l = (l ^ 1537906579376449657L) - 6110194083321716462L;
               } else {
                  l = l + 6959226380365718651L ^ 2936514773525457813L;
               }
            }

            if (this.intVal.getSignum() == 0) {
               l = (l ^ 4718986621560350715L) - 3657183376181989964L;
               break;
            }

            l = l - -863262541725945568L ^ -8301874057044596094L;
         }
      }

      return ZERO;
   }

   private static CustomBigInteger bigMultiplyPowerTen(long l, int n) {
      return n > 0 ? bigTenToThe(n).multiply(l) : CustomBigInteger.valueOf(l);
   }

   private static long parseExp(char[] chars, int n, int n2) {
      long l = 0L;
      boolean flag = false;
      char ch = 0;
      int n3 = 0;
      byte b = 3;

      boolean flag2;
      while (true) {
         while (2 >= b) {
            l = 0L;
         }

         if (b >= 4) {
            if (b > 4) {
               flag2 = true;
               break;
            }

            n2--;
         } else {
            n++;
         }

         if (b <= 0) {
            ch = chars[n];
         } else if (ch != '-') {
            flag2 = false;
            break;
         }
      }

      flag = flag2;
      if (flag || ch == '+') {
         ch = chars[++n];
         n2--;
      }

      if (n2 <= 0) {
         throw new NumberFormatException();
      } else {
         while (n2 > 10 && (ch == '0' || Character.digit(ch, 10) == 0)) {
            ch = chars[++n];
            n2--;
         }

         if (n2 > 10) {
            throw new NumberFormatException();
         } else {
            while (true) {
               if (ch >= '0' && ch <= '9') {
                  n3 = ch - '0';
               } else {
                  n3 = Character.digit(ch, 10);
                  if (n3 < 0) {
                     throw new NumberFormatException();
                  }
               }

               l = l * 10L + (long)n3;
               if (n2 == 1) {
                  if (flag) {
                     l = -l;
                  }

                  return l;
               }

               ch = chars[++n];
               n2--;
            }
         }
      }
   }

   private static int precision(long l, long l2) {
      int n = 0;
      int n2 = 0;
      byte b = 2;
      long l3 = 0L;
      l3 = -3850049099477070134L ^ (long)"덼舟\uece5姦캕\udca1ꉫᦽ懏㧨⨹숐듟ﯱɀ죈慕뭏ඓ栁".hashCode();

      while (2 < b) {
         if (b <= 3) {
            if (l != 0L) {
               l3 = (l3 ^ -3859561760147037187L) + -2430947381450898291L;
               n = (128 - Long.numberOfLeadingZeros(l) + 1) * 1233 >>> 12;
               n2 = n - 19;
               return n2 < ((long[][])LONGLONG_TEN_POWERS_TABLE).length && !longLongCompareMagnitude(l, l2, LONGLONG_TEN_POWERS_TABLE[n2][0], LONGLONG_TEN_POWERS_TABLE[n2][1]) ? n + 1 : n;
            }

            l3 = (l3 ^ 4870838172328084306L) - -6120467830949056617L;
         } else {
            if (l2 >= 0L) {
               l3 = (l3 ^ 8224457877573680791L) - -8670695397236616826L;
               return longDigitLength(l2);
            }

            l3 = (l3 ^ 5978258251070362669L) + 6698422607670193837L;
         }
      }

      byte b2;
      if (!unsignedLongCompareEq(l2, LONGLONG_TEN_POWERS_TABLE[0][1])) {
         l3 = (l3 ^ 2641899834242556867L) + 1618945349459639530L;
         b2 = 19;
      } else {
         l3 = (l3 ^ 5139944775998784423L) + 3734767373023468878L;
         b2 = 20;
      }

      return b2;
   }

   public ShadedBigDecimal(int n, ShadedMathContext shadedMathContext) {
       super();
      int precision2 = shadedMathContext.precision;
      long l = (long)n;
      int n2 = 0;
      int n3 = 0;
      if (precision2 > 0) {
         n3 = longDigitLength(l);

         for (int i = n3 - precision2; i > 0; i = n3 - precision2) {
            n2 = checkScaleNonZero((long)n2 - (long)i);
            l = divideAndRound3(l, LONG_TEN_POWERS_TABLE[i], shadedMathContext.roundingMode.oldMode);
            n3 = longDigitLength(l);
         }
      }

      this.intVal = null;
      this.intCompact = l;
      this.scale = n2;
      this.precision = n3;
   }

   public String toPlainString() {
      String str = null;
      int n = 0;
      StringBuilder sb = null;
      String str2 = null;
      int n2 = 0;
      byte b = 9;
      long l = 0L;
      l = 9168811079060177589L ^ (long)"⚽ᡗ뱠貵斌霊뎚螻ꂧ뒚⫛富꯶ዄ본넬隷쬔ၦ䶘䡧ꑦ얍ީ눷".hashCode();

      outerLoop:
      while (true) {
         if (12 >= b) {
            if (this.intCompact == -9223372036854775808L) {
               l = (l ^ -8193453819347447077L) + 8976394742002709343L;
               continue;
            }

            l = l - 4651821932998544261L ^ -1816945356539044575L;
         } else if (b >= 19) {
            if (b <= 19) {
               n = 0;
               l = (l ^ -3068638593636074149L) - 4275017580378767370L;
            } else if (22 <= b) {
               if (b <= 22) {
                  if (this.scale >= 0) {
                     l = (l ^ -7996976257984130180L) - -6839104193372159246L;
                     continue;
                  }

                  l = l + -3840569760692342323L ^ -5977111163086308223L;
               } else {
                  if (b < 24) {
                     return this.getValueString(this.signum(), str2, this.scale);
                  }

                  if (b <= 24) {
                     sb = new StringBuilder(20 + n2);
                     l = (l ^ -8048329358311068648L) + 629649328817162428L;
                  } else {
                     str2 = Long.toString(Math.abs(this.intCompact));
                     l = l + 8395850327266270422L ^ -8904156542483008215L;
                  }
               }
            } else {
               if (20 < b) {
                  l = (l ^ -3532084716735102087L) + -5188819173431841355L;
                  continue;
               }

               if (this.intCompact != -9223372036854775808L) {
                  l = (l ^ -7693940692740780012L) - 3856686731939027809L;
                  continue;
               }

               l = l - 8540678215286021741L ^ -8960457986900691733L;
            }
         } else {
            if (15 > b) {
               if (b <= 13) {
                  if (this.scale != 0) {
                     l = l - -4125458735086682695L ^ 6192138613732750223L;
                  } else {
                     l = (l ^ 3771826600927827523L) - 6363410757956332231L;
                  }
               } else if (this.intCompact == -9223372036854775808L) {
                  l = l + -4941039308319025833L ^ 8188478803541924269L;
               } else {
                  l = l - 8372669288201492894L ^ -8204858293980576531L;
               }
               continue;
            }

            if (b <= 15) {
               sb.append('0');
               l = l + 148301238125788648L ^ 1621075936818052031L;
            } else {
               if (17 > b) {
                  sb.append(str);
                  l = (l ^ -3664646743766780779L) + 6392024407865463848L;
                  continue;
               }

               if (b <= 17) {
                  return this.intVal.toString();
               }

               str = this.intVal.toString();
               l = l - 5968961741620622741L ^ -5550405996119760287L;
            }
         }

         while (true) {
            while (5 > b) {
               if (2 > b) {
                  if (b <= 0) {
                     l = (l ^ 6882828977124053032L) + -5852906815766567268L;
                  } else {
                     str2 = this.intVal.abs().toString();
                     l = l + 1817349406445460806L ^ -798375913648416326L;
                  }
                  continue outerLoop;
               }

               if (2 >= b) {
                  return "0";
               }

               if (3 >= b) {
                  return sb.toString();
               }

               if (this.signum() != 0) {
                  l = l + 369976136145645808L ^ 5857362519505542847L;
               } else {
                  l = (l ^ 8151227581689624325L) - 3635995963051129049L;
               }
            }

            if (b <= 5) {
               n++;
               l = (l ^ -8058751259636995505L) - -2232697931780129879L;
            } else if (8 <= b) {
               if (8 >= b) {
                  return Long.toString(this.intCompact);
               }

               if (10 > b) {
                  sb.append(this.intCompact);
                  l = (l ^ -6637382398499917365L) + 504281114776242692L;
                  break;
               }

               if (10 >= b) {
                  n2 = checkScaleNonZero(-((long)this.scale));
                  l = (l ^ -2954672331311288847L) + 2053364422525491567L;
                  break;
               }

               if (n < n2) {
                  l = (l ^ 570527392196263926L) + -5671198697537905901L;
                  break;
               }

               l = l - -8864891242163933229L ^ -3533780799775536476L;
            } else {
               if (b <= 6) {
                  sb = new StringBuilder(str.length() + n2);
                  l = l + -8721274600330852653L ^ 640200991627402424L;
                  break;
               }

               l = (l ^ 7941963976473079918L) + 4961806824075264682L;
            }
         }
      }
   }

   private static int bigDigitLength(CustomBigInteger customBigInteger) {
      int n = 0;
      byte b = 3;
      if (customBigInteger.signum == 0) {
         ;
      }

      while (b > 2) {
         if (3 >= b) {
            return 1;
         }

         n = (int)(((long)customBigInteger.bitLength() + 1L) * 646456993L >>> 31);
      }

      return customBigInteger.compareMagnitude(bigTenToThe(n)) >= 0 ? n + 1 : n;
   }

   private static ShadedBigDecimal divide4(CustomBigInteger customBigInteger, int n, CustomBigInteger customBigInteger2, int n2, int n3, int n4) {
      int n5 = 0;
      CustomBigInteger customBigInteger3 = null;
      int n6 = 0;
      byte b = 5;

      outerLoop:
      while (true) {
         while (b < 1) {
            customBigInteger3 = bigMultiplyPowerTen2(customBigInteger2, n6);
         }

         if (1 >= b) {
            return divideAndRound3(customBigInteger, customBigInteger3, n3, n4, n3);
         }

         if (b <= 2) {
            return divideAndRound3(customBigInteger3, customBigInteger2, n3, n4, n3);
         }

         CustomBigInteger customBigInteger8 = customBigInteger;
         if (checkScale(customBigInteger8, (long)n3 + (long)n2) > n) {
            ;
         }

         while (4 < b) {
            if (6 <= b) {
               if (b <= 6) {
                  n5 = n3 + n2;
               } else {
                  if (b <= 7) {
                     customBigInteger3 = bigMultiplyPowerTen2(customBigInteger, n6);
                     continue outerLoop;
                  }

                  n6 = n5 - n;
               }
            } else {
               customBigInteger8 = customBigInteger2;
               n5 = checkScale(customBigInteger8, (long)n - (long)n3);
            }
         }

         n6 = n5 - n2;
      }
   }

   private static ShadedBigDecimal createAndStripZerosToMatchScale(CustomBigInteger customBigInteger, int n, long l) {
      CustomBigInteger[] customBigIntegers = null;
      byte b = 2;
      long l2 = 0L;
      l2 = -2509725131496015323L ^ (long)"橱р訰뛆귯ꆐ\udd39\u0099ܞ뛝椷乒ᵷ鬆\uf5eb舫肭ꕜ艼\ue783耒ᒫ鍱".hashCode();

      while (true) {
         outerLoop:
         while (b > 5) {
            if (b >= 8) {
               if (b <= 8) {
                  return valueOf(customBigInteger, n, (byte)0);
               }

               if (9 >= b) {
                  l2 = l2 - 910885070623300793L ^ -2343759132551866626L;
                  continue;
               }

               if (customBigIntegers[1].getSignum() != 0) {
                  l2 = l2 - -7181000537731445207L ^ 6985821351348452219L;
                  continue;
               }

               l2 = l2 - 5930558034993327031L ^ 8191786461936113122L;
            } else {
               if (b <= 6) {
                  if (customBigInteger.compareMagnitude(CustomBigInteger.TEN) < 0) {
                     l2 = l2 + -1652331219966719793L ^ 4256640520233891868L;
                  } else {
                     l2 = (l2 ^ -8329983209732398345L) - -754675467414429497L;
                  }
                  continue;
               }

               if ((long)n <= l) {
                  l2 = l2 + 55682734738886633L ^ -6799712613507714822L;
                  continue;
               }

               l2 = (l2 ^ -1075965015706350977L) - -4333088434887842996L;
            }

            while (true) {
               while (b < 2) {
                  if (b <= 0) {
                     n = checkScale(customBigInteger, (long)n - 1L);
                     l2 = l2 + -8448709526031089374L ^ 6403327319580854550L;
                  } else {
                     if (customBigInteger.testBit((byte)0)) {
                        l2 = l2 + -3952594451791442520L ^ -6997462877045460607L;
                        continue outerLoop;
                     }

                     l2 = (l2 ^ 7025030188139140548L) - -6782033623530611039L;
                  }
               }

               if (2 < b) {
                  if (3 >= b) {
                     l2 = (l2 ^ 810891648705778418L) + -3674450338509899826L;
                  } else {
                     customBigIntegers = customBigInteger.divideAndRemainder(CustomBigInteger.TEN);
                     l2 = (l2 ^ -3633771570386356424L) + 3392882561629005446L;
                  }
                  break;
               }

               customBigInteger = customBigIntegers[0];
               l2 = (l2 ^ 1380110488686354787L) - -5945267498878002744L;
            }
         }

         l2 = l2 - 5542212256517036524L ^ 8078523897141189719L;
      }
   }

   private static CustomBigInteger bigTenToThe(int n) {
      CustomBigInteger[] customBigIntegers = null;
      byte b = 2;
      if (n >= 0) {
         if (n < BIG_TEN_POWERS_TABLE_MAX) {
            ;
         }
      }

      while (b > 3) {
         if (5 <= b) {
            if (b > 5) {
               if (6 >= b) {
                  return customBigIntegers[n];
               }

               return CustomBigInteger.ZERO;
            }

            if (n >= customBigIntegers.length) {
               return expandBigIntegerTenPowers(n);
            }
         } else {
            customBigIntegers = BIG_TEN_POWERS_TABLE;
         }
      }

      return CustomBigInteger.TEN.pow(n);
   }

   private static ShadedBigDecimal multiply2(CustomBigInteger customBigInteger, CustomBigInteger customBigInteger2, int n) {
      return new ShadedBigDecimal(customBigInteger.multiply2(customBigInteger2), -9223372036854775808L, n, 0);
   }

   private static ShadedBigDecimal multiplyDivideAndRound(long l, long l2, long l3, int n, int n2, int n3) {
      long l4 = 0L;
      long l5 = 0L;
      long l6 = 0L;
      long l7 = 0L;
      long l8 = 0L;
      long l9 = 0L;
      long l10 = 0L;
      int n4 = 0;
      long l11 = 0L;
      long l12 = 0L;
      long l13 = 0L;
      long l14 = 0L;
      byte b = 18;

      outerLoop:
      while (true) {
         if (12 >= b) {
            l6 += l8 >>> 32;
         } else if (b >= 18) {
            if (18 >= b) {
               l8 = l7 * l4;
               continue;
            }

            if (b >= 21) {
               if (21 >= b) {
                  n4 = Long.signum(l) * Long.signum(l2) * Long.signum(l3);
               } else if (b >= 23) {
                  if (b <= 23) {
                     l3 = Math.abs(l3);
                  } else {
                     l7 = l & 4294967295L;
                  }
               } else {
                  l5 = l8 & 4294967295L;
               }
            } else {
               if (19 >= b) {
                  l6 &= 4294967295L;
                  continue;
               }

               l10 = l8 & 4294967295L;
            }
         } else {
            if (15 <= b) {
               if (b <= 15) {
                  l4 = l2 & 4294967295L;
               } else if (16 >= b) {
                  l6 = l8 & 4294967295L;
               } else {
                  l8 = l13 * l12 + l6;
               }
               continue;
            }

            if (13 >= b) {
               l9 = (l8 >>> 32) + l9 & 4294967295L;
               continue;
            }

            l11 = make64(l9, l6);
         }

         while (true) {
            while (b < 5) {
               if (2 <= b) {
                  if (b <= 2) {
                     l2 = Math.abs(l2);
                     continue outerLoop;
                  }

                  if (b > 3) {
                     l9 = l6 >>> 32;
                     continue outerLoop;
                  }

                  l14 = make64(l10, l5);
               } else {
                  if (b <= 0) {
                     l12 = l2 >>> 32;
                     continue outerLoop;
                  }

                  l6 = l8 >>> 32;
               }
            }

            if (b <= 5) {
               return divideAndRound128(l11, l14, l3, n4, n, n2, n3);
            }

            if (b >= 8) {
               if (8 >= b) {
                  l10 = l8 & 4294967295L;
                  break;
               }

               if (10 <= b) {
                  if (b <= 10) {
                     l13 = l >>> 32;
                     break;
                  }

                  l = Math.abs(l);
               } else {
                  l8 = l7 * l12 + l10;
               }
            } else {
               if (b <= 6) {
                  l8 = l13 * l4 + l10;
                  break;
               }

               l10 = l8 >>> 32;
            }
         }
      }
   }

   public ShadedBigDecimal divide(ShadedBigDecimal shadedBigDecimal, int n, int n2) {
      byte b = 9;
      long l = 0L;
      l = -6908042274286624626L ^ (long)"ꪕ뻁끫\uf763駉蜻䶒䫨胄텃\ue140ꮳ權륕৷𢡄꠲楊팈ꉲ䒊Ⱉ".hashCode();

      outerLoop:
      while (true) {
         while (true) {
            while (4 >= b) {
               if (n2 < 0) {
                  l = l + 435811095162997003L ^ -6420632222571056226L;
               } else {
                  l = (l ^ -2921672197217568866L) - -6433267295980660084L;
               }
            }

            if (b < 7) {
               if (b <= 5) {
                  throw new IllegalArgumentException("Invalid rounding mode");
               }

               int scale2 = shadedBigDecimal.scale;
               CustomBigInteger intVal2 = shadedBigDecimal.intVal;
               int scale3 = this.scale;
               return divide4(this.intVal, scale3, intVal2, scale2, n, n2);
            }

            if (b > 7) {
               if (8 >= b) {
                  if (shadedBigDecimal.intCompact == -9223372036854775808L) {
                     l = l + -6484814926791531363L ^ 2971915364954392974L;
                  } else {
                     l = l + 9217549466342035496L ^ -6480342613165697213L;
                  }
                  break;
               }

               if (this.intCompact == -9223372036854775808L) {
                  l = (l ^ -2200889189177146705L) - 2617314864130504406L;
                  break;
               }

               l = (l ^ 953482698590847773L) + -671942067742686628L;
            } else if (n2 <= 7) {
               l = (l ^ 4495936620509701167L) + -5432302019489034356L;
            } else {
               l = l - -5511078539150315800L ^ 2149128476605756601L;
            }
         }

         while (1 <= b) {
            if (b <= 1) {
               int scale4 = shadedBigDecimal.scale;
               return divide7(this.intCompact, this.scale, shadedBigDecimal.intVal, scale4, n, n2);
            }

            if (2 < b) {
               return divide2(this.intCompact, this.scale, shadedBigDecimal.intCompact, shadedBigDecimal.scale, n, n2);
            }

            if (shadedBigDecimal.intCompact == -9223372036854775808L) {
               l = l - -6390217426988059851L ^ -4037966259505922155L;
               continue outerLoop;
            }

            l = (l ^ 2912555614346256938L) + 5380203754029118356L;
         }

         int scale5 = shadedBigDecimal.scale;
         long intCompact2 = shadedBigDecimal.intCompact;
         int scale6 = this.scale;
         return divide5(this.intVal, scale6, intCompact2, scale5, n, n2);
      }
   }

   private static int saturateLong(long l) {
      int n = 0;
      byte b = 2;
      long l2 = 0L;

      for (l2 = 6478958471245506030L ^ (long)"쑪䛘㧲揉침剾揈ͭ䠁쓰镥૯눲皴竮냖\ud890\uece0\ue1df鰞ニজ\uf8e8驄䭔劘".hashCode();
         b > 1;
         l2 = l2 - -1001381536573808648L ^ 4636215647306632711L
      ) {
         n = (int)l;
      }

      int n2;
      if (l != (long)n) {
         l2 = l2 - -8168135918349547322L ^ 4229469203082094036L;
         n2 = l < 0L ? -2147483648 : 2147483647;
      } else {
         l2 = l2 + -7582737237265247687L ^ -3043029933988127021L;
         n2 = n;
      }

      return n2;
   }

   public ShadedBigDecimal abs(ShadedMathContext shadedMathContext) {
      ShadedBigDecimal shadedBigDecimal;
      if (this.signum() < 0) {
         shadedBigDecimal = this.negate(shadedMathContext);
      } else {
         shadedBigDecimal = this.round(shadedMathContext);
      }

      return shadedBigDecimal;
   }

   private static ShadedBigDecimal divideAndRound2(CustomBigInteger customBigInteger, long l, int n, int n2, int n3) {
      long l2 = 0L;
      int n4 = 0;
      boolean flag = false;
      ShadedMutableBigInteger shadedMutableBigInteger = null;
      ShadedMutableBigInteger shadedMutableBigInteger2 = null;
      Object obj = null;
      long l3 = 0L;
      byte b = 2;
      long l4 = 0L;
      l4 = -3753501137350360046L ^ (long)"ꁅ簉ꆷ䢹\u2429儏섀栄凜㈀寕䡴͟䶴６㶤㮄\udb1f눰뢑哖풖뒷뵘살剶\u0a4a㑰".hashCode();

      boolean flag2;
      outerLoop:
      while (true) {
         while (b <= 3) {
            shadedMutableBigInteger = new ShadedMutableBigInteger();
            l4 = l4 + 5305354074682576399L ^ 2486205096119085045L;
         }

         if (5 <= b) {
            if (5 >= b) {
               flag2 = true;
               break;
            }

            l3 = shadedMutableBigInteger2.divide(l, shadedMutableBigInteger);
            l4 = l4 + -3235499263399373601L ^ -3506455832727824296L;
         } else {
            l3 = 0L;
            l4 = l4 + 7904832589244455649L ^ -4115167044236495057L;
         }

         while (b >= 1) {
            if (1 >= b) {
               if (l3 != 0L) {
                  l4 = l4 - 6666857293528317151L ^ 6235084402668327899L;
                  flag2 = false;
                  break outerLoop;
               }

               l4 = l4 + 8229950139926052412L ^ 2867477585264217854L;
               continue outerLoop;
            }

            shadedMutableBigInteger = null;
            l4 = l4 - -5278156048858156447L ^ 951481798965397620L;
         }

         shadedMutableBigInteger2 = new ShadedMutableBigInteger(customBigInteger.mag);
         l4 = l4 + -1876896949089598380L ^ -7529027456522630153L;
      }

      flag = flag2;
      n4 = l < 0L ? -customBigInteger.signum : customBigInteger.signum;
      if (!flag) {
         if (needIncrement(l, n2, n4, shadedMutableBigInteger, l3)) {
            shadedMutableBigInteger.add(ShadedMutableBigInteger.ONE);
         }

         return shadedMutableBigInteger.toBigDecimal(n4, n);
      } else if (n3 != n) {
         l2 = shadedMutableBigInteger.toCompactValue(n4);
         if (l2 != -9223372036854775808L) {
            return createAndStripZerosToMatchScale2(l2, n, (long)n3);
         } else {
            obj = shadedMutableBigInteger.toBigInteger(n4);
            return createAndStripZerosToMatchScale((CustomBigInteger)obj, n, (long)n3);
         }
      } else {
         return shadedMutableBigInteger.toBigDecimal(n4, n);
      }
   }

   public ShadedBigDecimal round(ShadedMathContext shadedMathContext) {
      if (shadedMathContext.precision != 0) {
         return doRound(this, shadedMathContext);
      } else {
         return this;
      }
   }

   public ShadedBigDecimal(CustomBigInteger customBigInteger, int n) {
       super();
      this.intVal = customBigInteger;
      this.intCompact = compactValFor(customBigInteger);
      this.scale = n;
   }

   private ShadedBigDecimal[] preAlign(ShadedBigDecimal shadedBigDecimal, ShadedBigDecimal shadedBigDecimal2, long l, ShadedMathContext shadedMathContext) {
      ShadedBigDecimal[] shadedBigDecimals = null;
      long l2 = 0L;
      ShadedBigDecimal shadedBigDecimal3 = null;
      ShadedBigDecimal shadedBigDecimal4 = null;
      long l3 = 0L;
      byte b = 6;
      long l4 = 0L;
      l4 = 9079658772212658028L ^ (long)"럁\ueb6f\ueb96ꝍᤎ졸ཤ닝邺䌲ﾐ⏲ꡨ氎츚唙\uded6ћ키⏴쭴\ue5e2戞⅀\uee40尫⑂ד".hashCode();

      outerLoop:
      while (true) {
         if (b >= 3) {
            if (3 >= b) {
               shadedBigDecimal3 = shadedBigDecimal;
               l4 = (l4 ^ -6541793469077643395L) + 6205561625496427040L;
               continue;
            }

            if (b >= 5) {
               if (5 >= b) {
                  if (l != 0L) {
                     l4 = l4 + 785399233827563163L ^ 3658051363521235401L;
                  } else {
                     l4 = l4 + -1886168723792121894L ^ 9014868013676913424L;
                  }
               } else {
                  if (l2 <= (long)(shadedBigDecimal4.scale + 2)) {
                     l4 = (l4 ^ -3333780312260401886L) + 528979173246124867L;
                     continue;
                  }

                  l4 = (l4 ^ 780097776148474047L) + 4985791396119066860L;
               }
            } else {
               l3 = (long)shadedBigDecimal4.scale - (long)shadedBigDecimal4.precision() + (long)shadedMathContext.precision;
               l4 = l4 - -2587233058985264781L ^ 2044658813839749847L;
            }
         } else if (1 <= b) {
            if (1 >= b) {
               shadedBigDecimals = new ShadedBigDecimal[]{shadedBigDecimal4, shadedBigDecimal3};
               l4 = l4 - -5846443897889994206L ^ 3534960035528295426L;
            } else {
               shadedBigDecimal4 = shadedBigDecimal;
               l4 = l4 + 2782398270030004579L ^ -818773541845948905L;
            }
         } else {
            if (!assertionsDisabled) {
               l4 = l4 + -1383394485530795299L ^ -6135335801359523987L;
               continue;
            }

            l4 = l4 + -6349714973516392766L ^ -2459962955364323288L;
         }

         while (b > 7) {
            if (11 <= b) {
               if (11 >= b) {
                  shadedBigDecimal4 = shadedBigDecimal2;
                  l4 = (l4 ^ 905226916369946900L) - 5432019124390783880L;
                  continue outerLoop;
               }

               if (b < 13) {
                  return shadedBigDecimals;
               }

               if (13 < b) {
                  if (b <= 14) {
                     l2 = (long)shadedBigDecimal3.scale - (long)shadedBigDecimal3.precision() + 1L;
                     l4 = (l4 ^ -9162108048856141395L) - 5238089118081976695L;
                  } else {
                     shadedBigDecimal3 = valueOf((long)shadedBigDecimal3.signum(), this.checkScale(Math.max((long)shadedBigDecimal4.scale, l3) + 3L));
                     l4 = l4 - -2838718030777464927L ^ -1434109215312435920L;
                  }
                  continue outerLoop;
               }

               if (l2 <= l3 + 2L) {
                  l4 = (l4 ^ 3699881695615751466L) + -7152273620200464192L;
                  continue outerLoop;
               }

               l4 = l4 - 4762755981089998895L ^ -1999583676297591922L;
            } else {
               if (9 > b) {
                  throw new AssertionError();
               }

               if (b <= 9) {
                  shadedBigDecimal3 = shadedBigDecimal2;
                  l4 = (l4 ^ 7879163894030332267L) + 1442432141661074640L;
               } else {
                  if (l < 0L) {
                     l4 = l4 - -215480782565981675L ^ 173147622561212903L;
                     continue outerLoop;
                  }

                  l4 = (l4 ^ -7327838104095988598L) - 5309138477172782885L;
               }
            }
         }

         l4 = l4 - 7823341860293653096L ^ -1256334406750624581L;
      }
   }

   public byte byteValueExact() {
      long l = 0L;
      long l2 = 0L;
      l2 = -8612098170026383189L ^ (long)"충ꍽ\ue07a뜑㍬Ꮸ쀩셅쬉周\u10c9歊鷚ぷ⾩뿫椁鳥愛왞숁ȍꛘ鵫崩❍".hashCode();
      l = this.longValueExact();
      l2 = (l2 ^ -6964324481012119888L) - 4819792501050644469L;
      if ((long)((byte)((int)l)) == l) {
         l2 = l2 - 2688965525356307429L ^ 237537684459059029L;
         return (byte)((int)l);
      } else {
         l2 = l2 - 7159586428341699892L ^ -132928747895673021L;
         throw new ArithmeticException("Overflow");
      }
   }

   public ShadedBigDecimal(char[] chars, int n, int n2, ShadedMathContext shadedMathContext) {
       super();
      if ((chars.length | n2 | n) >= 0 && n2 <= chars.length - n) {
         int n3 = 0;
         int n4 = 0;
         long l = 0L;
         CustomBigInteger customBigInteger = null;

         try {
            boolean flag = false;
            if (chars[n] == '-') {
               flag = true;
               n++;
               n2--;
            } else if (chars[n] == '+') {
               n++;
               n2--;
            }

            boolean flag2 = false;
            long l2 = 0L;
            boolean flag3 = n2 <= 18;
            int n5 = 0;
            if (!flag3) {
               char[] chars2;
               for (chars2 = new char[n2]; n2 > 0; n2--) {
                  char ch = chars[n];
                  if ((ch < '0' || ch > '9') && !Character.isDigit(ch)) {
                     if (ch != '.') {
                        if (ch != 'e' && ch != 'E') {
                           throw new NumberFormatException();
                        }

                        l2 = parseExp(chars, n, n2);
                        if ((long)((int)l2) != l2) {
                           throw new NumberFormatException();
                        }
                        break;
                     }

                     if (flag2) {
                        throw new NumberFormatException();
                     }

                     flag2 = true;
                  } else {
                     if (ch != '0' && Character.digit(ch, 10) != 0) {
                        if (n3 != 1 || n5 != 0) {
                           n3++;
                        }

                        chars2[n5++] = ch;
                     } else if (n3 == 0) {
                        chars2[n5] = ch;
                        n3 = 1;
                     } else if (n5 != 0) {
                        chars2[n5++] = ch;
                        n3++;
                     }

                     if (flag2) {
                        n4++;
                     }
                  }

                  n++;
               }

               if (n3 == 0) {
                  throw new NumberFormatException();
               }

               if (l2 != 0L) {
                  n4 = this.adjustScale(n4, l2);
               }

               int n6 = flag ? -1 : 1;
               customBigInteger = new CustomBigInteger(chars2, n6, n3);
               l = compactValFor(customBigInteger);
               int precision2 = shadedMathContext.precision;
               if (precision2 > 0 && n3 > precision2) {
                  if (l == -9223372036854775808L) {
                     for (int i = n3 - precision2; i > 0; i = n3 - precision2) {
                        n4 = checkScaleNonZero((long)n4 - (long)i);
                        customBigInteger = divideAndRoundByTenPow(customBigInteger, i, shadedMathContext.roundingMode.oldMode);
                        l = compactValFor(customBigInteger);
                        if (l != -9223372036854775808L) {
                           n3 = longDigitLength(l);
                           break;
                        }

                        n3 = bigDigitLength(customBigInteger);
                     }
                  }

                  if (l != -9223372036854775808L) {
                     for (int j = n3 - precision2; j > 0; j = n3 - precision2) {
                        n4 = checkScaleNonZero((long)n4 - (long)j);
                        l = divideAndRound3(l, LONG_TEN_POWERS_TABLE[j], shadedMathContext.roundingMode.oldMode);
                        n3 = longDigitLength(l);
                     }

                     customBigInteger = null;
                  }
               }
            } else {
               while (n2 > 0) {
                  char ch2 = chars[n];
                  if (ch2 == '0') {
                     if (n3 == 0) {
                        n3 = 1;
                     } else if (l != 0L) {
                        l *= 10L;
                        n3++;
                     }

                     if (flag2) {
                        n4++;
                     }
                  } else if (ch2 >= '1' && ch2 <= '9') {
                     int n7 = ch2 - '0';
                     if (n3 != 1 || l != 0L) {
                        n3++;
                     }

                     l = l * 10L + (long)n7;
                     if (flag2) {
                        n4++;
                     }
                  } else if (ch2 == '.') {
                     if (flag2) {
                        throw new NumberFormatException();
                     }

                     flag2 = true;
                  } else {
                     if (!Character.isDigit(ch2)) {
                        if (ch2 != 'e' && ch2 != 'E') {
                           throw new NumberFormatException();
                        }

                        l2 = parseExp(chars, n, n2);
                        if ((long)((int)l2) != l2) {
                           throw new NumberFormatException();
                        }
                        break;
                     }

                     int n8 = Character.digit(ch2, 10);
                     if (n8 == 0) {
                        if (n3 == 0) {
                           n3 = 1;
                        } else if (l != 0L) {
                           l *= 10L;
                           n3++;
                        }
                     } else {
                        if (n3 != 1 || l != 0L) {
                           n3++;
                        }

                        l = l * 10L + (long)n8;
                     }

                     if (flag2) {
                        n4++;
                     }
                  }

                  n++;
                  n2--;
               }

               if (n3 == 0) {
                  throw new NumberFormatException();
               }

               if (l2 != 0L) {
                  n4 = this.adjustScale(n4, l2);
               }

               l = flag ? -l : l;
               int precision3 = shadedMathContext.precision;
               int n9 = n3 - precision3;
               if (precision3 > 0 && n9 > 0) {
                  while (n9 > 0) {
                     n4 = checkScaleNonZero((long)n4 - (long)n9);
                     l = divideAndRound3(l, LONG_TEN_POWERS_TABLE[n9], shadedMathContext.roundingMode.oldMode);
                     n3 = longDigitLength(l);
                     n9 = n3 - precision3;
                  }
               }
            }
         } catch (ArrayIndexOutOfBoundsException e) {
            throw new NumberFormatException();
         } catch (NegativeArraySizeException e2) {
            throw new NumberFormatException();
         }

         this.scale = n4;
         this.precision = n3;
         this.intCompact = l;
         this.intVal = customBigInteger;
      } else {
         throw new NumberFormatException("Bad offset or len arguments for char[] input.");
      }
   }

   public int signum() {
      return this.intCompact != -9223372036854775808L ? Long.signum(this.intCompact) : this.intVal.getSignum();
   }

   public ShadedBigDecimal negate(ShadedMathContext shadedMathContext) {
      return this.negate().round(shadedMathContext);
   }

   private static ShadedBigDecimal multiply3(long l, CustomBigInteger customBigInteger, int n) {
      long l2 = 0L;
      l2 = 5013777912008361554L ^ (long)"㰭\uf0e3潗ꇟᑉ獇쩅憄ꪟ橥쪅䊓褿ᢹ\ue0f1\uea28ࡽퟹ趢윓".hashCode();
      if (l != 0L) {
         l2 = l2 - -1505490256353459752L ^ 8220819637441672443L;
         return new ShadedBigDecimal(customBigInteger.multiply(l), -9223372036854775808L, n, 0);
      } else {
         l2 = l2 - 6207760645132998375L ^ -8072703896976800375L;
         return zeroValueOf(n);
      }
   }

   public static ShadedBigDecimal zeroValueOf(int n) {
      byte b = 1;

      while (1 < b) {
         if (b <= 2) {
            return ZERO_SCALED_BY[n];
         }

         if (n >= 0 && n < ZERO_SCALED_BY.length) {
            ;
         }
      }

      return new ShadedBigDecimal(CustomBigInteger.ZERO, 0L, n, 1);
   }

   public ShadedBigDecimal(String str) {
       this(str.toCharArray(), 0, str.length());
   }

   private static ShadedBigDecimal doRound(CustomBigInteger customBigInteger, int n, ShadedMathContext shadedMathContext) {
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      long l = 0L;
      byte b = 5;
      long l2 = 0L;
      l2 = 6097741660478780825L ^ (long)"㎱\ue9fcㄪ鰁礙\ue20f䬳፹ꦩ橳\ue947\ue742\ue089䷳ﷷ\ua97e䅩⪎\uebc8슄".hashCode();

      outerLoop:
      while (true) {
         if (6 <= b) {
            if (b <= 6) {
               customBigInteger = divideAndRoundByTenPow(customBigInteger, n3, n2);
               l2 = l2 - 2934783053956238409L ^ -8605533072435150316L;
            } else if (9 <= b) {
               if (9 >= b) {
                  if (l == -9223372036854775808L) {
                     l2 = l2 - 155190893845307619L ^ 7788767762266577761L;
                  } else {
                     l2 = (l2 ^ -1510732026857969745L) + 2026540481448083707L;
                  }
               } else {
                  if (b >= 11) {
                     if (b <= 11) {
                        n5 = shadedMathContext.precision;
                        l2 = (l2 ^ -7228619744633588561L) - 1850633531569577255L;
                     } else {
                        n = checkScaleNonZero((long)n - (long)n3);
                        l2 = (l2 ^ 703832193042311424L) - -414316960561666551L;
                     }
                     continue;
                  }

                  n4 = 0;
                  l2 = (l2 ^ -6128074664188173051L) - -748439288133244809L;
               }
            } else {
               if (b > 7) {
                  l2 = (l2 ^ -7074815082857753008L) - 1562224578814581854L;
                  continue;
               }

               n4 = bigDigitLength(customBigInteger);
               l2 = (l2 ^ -3536622663149929804L) - 2402505920257319038L;
            }
         } else if (2 <= b) {
            if (2 >= b) {
               n4 = bigDigitLength(customBigInteger);
               l2 = l2 + 6091101808015822862L ^ -4921852780064888058L;
            } else if (b >= 4) {
               if (b <= 4) {
                  l2 = (l2 ^ 8396619961695567836L) - 7515803258517369450L;
               } else {
                  l = compactValFor(customBigInteger);
                  l2 = (l2 ^ 3225828255287266531L) + -6927789726777789591L;
               }
            } else {
               n4 = longDigitLength(l);
               l2 = l2 - 2155374665562204759L ^ 9209657899039090355L;
            }
         } else {
            if (b <= 0) {
               l = divideAndRound3(l, LONG_TEN_POWERS_TABLE[n3], shadedMathContext.roundingMode.oldMode);
               l2 = (l2 ^ 5011471392587348841L) - 4925958205064639320L;
               continue;
            }

            n3 = n4 - n5;
            l2 = l2 + -2694398111780140509L ^ -8723033902826258203L;
         }

         while (true) {
            while (13 < b) {
               if (b >= 20) {
                  if (b <= 20) {
                     if (n5 > 0) {
                        l2 = (l2 ^ -6749302977495297279L) + 4033888829208629431L;
                        continue outerLoop;
                     }

                     l2 = l2 + 8430462888448046773L ^ 3162305699407333948L;
                  } else {
                     if (b < 24) {
                        if (22 <= b) {
                           if (22 >= b) {
                              return new ShadedBigDecimal(customBigInteger, -9223372036854775808L, n, n4);
                           }

                           n3 = n4 - n5;
                           l2 = (l2 ^ -7260149696399921136L) - 8738682784587301731L;
                        } else if (l != -9223372036854775808L) {
                           l2 = (l2 ^ 6764691074247996408L) + -6413974095061230845L;
                        } else {
                           l2 = l2 + 5617486229196147013L ^ 3984084902064964436L;
                        }
                        continue outerLoop;
                     }

                     if (b <= 24) {
                        return valueOf2(l, n, n4);
                     }

                     if (b >= 26) {
                        if (b <= 26) {
                           if (n3 <= 0) {
                              l2 = (l2 ^ -8093097933992182492L) + -2935105927864690498L;
                           } else {
                              l2 = (l2 ^ 8747437743765882181L) + 4582967114489268164L;
                           }
                           continue outerLoop;
                        }

                        n2 = shadedMathContext.roundingMode.oldMode;
                        l2 = (l2 ^ -8410737087295759391L) + -2154864942166268593L;
                     } else {
                        l2 = (l2 ^ 375390553120762747L) + -5840433344548963798L;
                     }
                  }
               } else if (b >= 16) {
                  if (16 >= b) {
                     if (n3 <= 0) {
                        l2 = (l2 ^ -8984483000954667268L) - -4152258562387636616L;
                     } else {
                        l2 = (l2 ^ -2354549283395905339L) + -2001206551152175925L;
                     }
                  } else {
                     if (b < 18) {
                        if (l == -9223372036854775808L) {
                           l2 = l2 + -7276761852805895663L ^ -288053942272536173L;
                        } else {
                           l2 = (l2 ^ 6364764162162723435L) + -6118584285157546924L;
                        }
                        continue outerLoop;
                     }

                     if (b <= 18) {
                        n4 = longDigitLength(l);
                        l2 = (l2 ^ -5675147312416460332L) + 8620666921357379913L;
                        continue outerLoop;
                     }

                     l = compactValFor(customBigInteger);
                     l2 = (l2 ^ 5089775341713535867L) + -8784322657812781315L;
                  }
               } else {
                  if (14 < b) {
                     n = checkScaleNonZero((long)n - (long)n3);
                     l2 = (l2 ^ 3651542493521602168L) - 3080308563450984040L;
                     continue outerLoop;
                  }

                  n3 = n4 - n5;
                  l2 = l2 - -7535792131359119805L ^ -2325974790291736097L;
               }
            }

            n3 = n4 - n5;
            l2 = l2 + 2054171572550502469L ^ 924966054588039427L;
         }
      }
   }

   public CustomBigInteger toBigIntegerExact() {
      return this.setScale2((byte)0, (byte)7).inflated();
   }

   public ShadedBigDecimal multiply(ShadedBigDecimal shadedBigDecimal) {
      int n = 0;
      byte b = 0;
      long l = 0L;
      l = 6767825000077350443L ^ (long)"ഃ믃\uedd5䄃䉮ᴱ꒸\ua633\uee27툉ڼ淗\uaacf\ue106潩熏`ۿ䷍ɋ횡⧼㠺㋁쓜".hashCode();

      while (b > 3) {
         if (b < 5) {
            return multiply3(shadedBigDecimal.intCompact, this.intVal, n);
         }

         if (5 >= b) {
            if (shadedBigDecimal.intCompact == -9223372036854775808L) {
               l = (l ^ -5727820080738908887L) - -4759400085402727942L;
            } else {
               l = l + 281464807694820371L ^ -1137644057702010579L;
            }
         } else {
            if (6 < b) {
               n = this.checkScale((long)this.scale + (long)shadedBigDecimal.scale);
               l = (l ^ 7981950784746683729L) - 9165182646861594029L;
               continue;
            }

            if (this.intCompact != -9223372036854775808L) {
               l = l - -2014567096142946534L ^ -5574312459796719918L;
               continue;
            }

            l = l - -6843057160339776859L ^ -4661605947124877380L;
         }

         if (b < 1) {
            return multiply3(this.intCompact, shadedBigDecimal.intVal, n);
         }

         if (1 < b) {
            return multiply(this.intCompact, shadedBigDecimal.intCompact, n);
         }

         if (shadedBigDecimal.intCompact == -9223372036854775808L) {
            l = (l ^ 4957901580693477680L) - 125861439779773669L;
         } else {
            l = l - -4315661757865773141L ^ -7553249948122537952L;
         }
      }

      CustomBigInteger intVal2 = shadedBigDecimal.intVal;
      return multiply2(this.intVal, intVal2, n);
   }

   public ShadedBigDecimal(CustomBigInteger customBigInteger, int n, ShadedMathContext shadedMathContext) {
       super();
      long l = compactValFor(customBigInteger);
      int precision2 = shadedMathContext.precision;
      int n2 = 0;
      if (precision2 > 0) {
         int oldMode = shadedMathContext.roundingMode.oldMode;
         if (l == -9223372036854775808L) {
            n2 = bigDigitLength(customBigInteger);

            for (int i = n2 - precision2; i > 0; i = n2 - precision2) {
               n = checkScaleNonZero((long)n - (long)i);
               customBigInteger = divideAndRoundByTenPow(customBigInteger, i, oldMode);
               l = compactValFor(customBigInteger);
               if (l != -9223372036854775808L) {
                  break;
               }

               n2 = bigDigitLength(customBigInteger);
            }
         }

         if (l != -9223372036854775808L) {
            n2 = longDigitLength(l);

            for (int j = n2 - precision2; j > 0; j = n2 - precision2) {
               n = checkScaleNonZero((long)n - (long)j);
               l = divideAndRound3(l, LONG_TEN_POWERS_TABLE[j], oldMode);
               n2 = longDigitLength(l);
            }

            customBigInteger = null;
         }
      }

      this.intVal = customBigInteger;
      this.intCompact = l;
      this.scale = n;
      this.precision = n2;
   }

   public static ShadedBigDecimal valueOf(CustomBigInteger customBigInteger, int n, int n2) {
      long l = 0L;
      byte b = 0;
      long l2 = 0L;
      l2 = -7520263162877837347L ^ (long)"\ue459\ue99c蝪村\uf453隂滊燾蛡磙ⶏ橔댪⾨ꄧ잖᱈擐૾鎃飼﮷".hashCode();

      while (true) {
         if (b <= 3) {
            if (l != 0L) {
               l2 = (l2 ^ -2009234902540118120L) + 1055774681594837167L;
               if (n != 0) {
                  l2 = (l2 ^ -8052031037725636148L) - -8566450080560697291L;
                  break;
               }

               l2 = (l2 ^ -690059672386388573L) - 626553720911596448L;
            } else {
               l2 = (l2 ^ -8916263809989315252L) + -5336795950197574262L;
            }
         } else {
            if (b < 5) {
               if (l < (long)zeroThroughTen.length) {
                  l2 = l2 - 7632120077856569037L ^ -557128287761537942L;
                  return zeroThroughTen[(int)l];
               }

               l2 = l2 + 1699004266780716554L ^ 6370621808033674739L;
               break;
            }

            if (b <= 5) {
               return zeroValueOf(n);
            }

            if (b <= 6) {
               if (l < 0L) {
                  l2 = (l2 ^ -5457031943181687349L) - -3586431110455345863L;
                  break;
               }

               l2 = (l2 ^ 1673453704686326627L) - -4511309368471788680L;
            } else {
               l = compactValFor(customBigInteger);
               l2 = l2 + 3580048615089461084L ^ -7644220756154928209L;
            }
         }
      }

      return new ShadedBigDecimal(customBigInteger, l, n, n2);
   }

   public String toEngineeringString() {
      return this.layoutChars(false);
   }

   public ShadedBigDecimal divideToIntegralValue(ShadedBigDecimal shadedBigDecimal) {
      ShadedBigDecimal shadedBigDecimal2 = null;
      int n = 0;
      int n2 = 0;
      byte b = 7;

      outerLoop:
      while (true) {
         if (2 <= b) {
            if (b <= 2) {
               n2 = (int)Math.min(
                  (long)this.precision()
                     + (long)Math.ceil(10.0 * (double)shadedBigDecimal.precision() / 3.0)
                     + Math.abs((long)this.getScale() - (long)shadedBigDecimal.getScale())
                     + 2L,
                  2147483647L
               );
               continue;
            }

            if (b >= 4) {
               if (b > 4) {
                  return shadedBigDecimal2;
               }

               shadedBigDecimal2 = this.divide(shadedBigDecimal, new ShadedMathContext(n2, CustomRoundingMode.DOWN));
            } else if (shadedBigDecimal2.scale >= n) {
               continue;
            }
         } else if (b <= 0) {
            n = saturateLong((long)this.scale - (long)shadedBigDecimal.scale);
         } else if (this.signum() != 0) {
            continue;
         }

         while (b > 6) {
            if (b < 10) {
               if (b < 8) {
                  return this.setScale2(n, (byte)7);
               }

               if (b > 8) {
                  return zeroValueOf(n);
               }

               int scale2 = shadedBigDecimal2.scale;
               long intCompact2 = shadedBigDecimal2.intCompact;
               shadedBigDecimal2 = stripZerosToMatchScale(shadedBigDecimal2.intVal, intCompact2, scale2, n);
               continue outerLoop;
            }

            if (b <= 10) {
               if (shadedBigDecimal2.scale <= 0) {
                  continue outerLoop;
               }
            } else if (b >= 12) {
               if (12 >= b) {
                  shadedBigDecimal2 = shadedBigDecimal2.setScale((byte)0, CustomRoundingMode.DOWN);
               } else if (shadedBigDecimal.signum() == 0) {
                  continue outerLoop;
               }
            } else {
               if (this.compareMagnitude(shadedBigDecimal) >= 0) {
                  continue outerLoop;
               }
            }
         }

         shadedBigDecimal2 = shadedBigDecimal2.setScale2(n, (byte)7);
      }
   }

   private static long compactValFor(CustomBigInteger customBigInteger) {
      int[] ints = null;
      int n = 0;
      long l = 0L;
      int n2 = 0;
      byte b = 1;

      long l2;
      outerLoop:
      while (true) {
         if (2 <= b) {
            if (b <= 2) {
               l2 = ((long)ints[1] & 4294967295L) + ((long)n2 << 32);
               break;
            }

            if (3 >= b) {
               return 0L;
            }

            ints = customBigInteger.mag;
         } else {
            if (0 >= b) {
               n = ints.length;
            } else {
               n2 = ints[0];
            }

            while (true) {
               while (b > 5) {
                  if (8 <= b) {
                     if (8 < b) {
                        if (b <= 9) {
                           if (n == 0) {
                              ;
                           }
                           continue outerLoop;
                        }

                        if (n == 2) {
                           continue outerLoop;
                        }

                        l2 = (long)n2 & 4294967295L;
                        break outerLoop;
                     }

                     if (n2 < 0) {
                        ;
                     }
                  } else {
                     if (b <= 6) {
                        return -9223372036854775808L;
                     }

                     if (n <= 2) {
                        ;
                     }
                  }
               }

               if (n == 2) {
                  ;
               }
            }
         }
      }

      l = l2;
      return customBigInteger.signum < 0 ? -l : l;
   }

   private boolean fractionOnly() {
      byte b = 1;

      while (true) {
         if (2 >= b) {
            if (assertionsDisabled) {
               break;
            }
         } else {
            if (3 < b) {
               throw new AssertionError();
            }

            if (this.signum() != 0) {
               break;
            }
         }
      }

      boolean flag;
      while (true) {
         if (0 >= b) {
            flag = true;
            break;
         }

         if (this.precision() - this.scale > 0) {
            flag = false;
            break;
         }
      }

      return flag;
   }

   private static ShadedBigDecimal divideAndRound128(long l, long l2, long l3, int n, int n2, int n3, int n4) {
      long[] longs = null;
      boolean flag = false;
      long l4 = 0L;
      long l5 = 0L;
      long l6 = (long)0;
      long l7 = 0L;
      long l8 = 0L;
      long l9 = 0L;
      long l10 = 0L;
      long l11 = 0L;
      long l12 = 0L;
      long l13 = 0L;
      ShadedMutableBigInteger shadedMutableBigInteger = null;
      CustomBigInteger customBigInteger = null;
      long l14 = 0L;
      long[] longs2 = null;
      long l15 = 0L;
      long l16 = 0L;
      byte b = 18;
      long l17 = 0L;
      l17 = 3155351433579600180L ^ (long)"軃˳雉玀伇楷\uee19⩲퓒\uda6a쉢炗ၕ\ueb3a\uf86cម몁ﱯ椧ෘｯ擃᚛\uf3baᘀ뿃".hashCode();

      long l18;
      outerLoop:
      while (true) {
         while (35 >= b) {
            l4 = l5;
            l17 = l17 - 1252028457515436527L ^ -7783108221160156058L;
         }

         if (53 <= b) {
            if (b <= 53) {
               l10 = 0L;
               l17 = l17 - -2559710183121649294L ^ 5155343943000433161L;
               continue;
            }

            if (b >= 62) {
               if (b <= 62) {
                  if (l < l3) {
                     l17 = l17 - -284111293181559253L ^ -6725260921364323528L;
                  } else {
                     l17 = l17 + -588541245239885433L ^ -1969942373199921109L;
                  }
               } else if (b >= 66) {
                  if (66 >= b) {
                     l17 = (l17 ^ -3462235727365296964L) - 5723521411175639915L;
                     continue;
                  }

                  if (b >= 68) {
                     if (b <= 68) {
                        if (l10 < 4294967296L) {
                           l17 = l17 - -532484221838993484L ^ -8449452681899462531L;
                        } else {
                           l17 = l17 - 534237743623954341L ^ 678370616667016122L;
                        }
                     } else if (69 >= b) {
                        l16 = l5;
                        l17 = l17 + -4929047010773229205L ^ -2304253763588721703L;
                     } else {
                        l10 = longs[0];
                        l17 = l17 + 1798953146255462151L ^ -2210897816889070163L;
                     }
                     continue;
                  }

                  if (l16 < 4294967296L) {
                     l17 = l17 + -2090433201860072177L ^ -1429531459014113920L;
                     continue;
                  }

                  l17 = (l17 ^ 9199960698536343419L) - 2344219770238117130L;
               } else {
                  if (b < 64) {
                     l17 = (l17 ^ 1684952787066214631L) + -6122556297715652585L;
                     continue;
                  }

                  if (64 < b) {
                     flag = needIncrement2(l3 >>> l6, n3, n, l15, l14);
                     l17 = l17 + -3906748636051464097L ^ 7725715708506964586L;
                     continue;
                  }

                  if (l5 >= 0L) {
                     l17 = (l17 ^ -2464383246373508320L) - -3621424633656932472L;
                     continue;
                  }

                  l17 = l17 - -6396923385622813828L ^ 3255734315460789609L;
               }
            } else if (57 <= b) {
               if (57 >= b) {
                  l16 = l5 / l13;
                  l17 = (l17 ^ -2409463576653634437L) + 7985391366807936156L;
                  continue;
               }

               if (59 > b) {
                  return valueOf(l15, n2);
               }

               if (b <= 59) {
                  l12 = l5 & 4294967295L;
                  l17 = l17 - 8828586129302644808L ^ 522636100308091147L;
                  continue;
               }

               if (b <= 60) {
                  l4--;
                  l17 = l17 + -2820387072591468055L ^ 6650339034350864854L;
               } else {
                  l5 = l << l6 | l2 >>> 64 - l6;
                  l17 = l17 - 1567922323648393788L ^ -2525374966088723661L;
               }
            } else if (55 <= b) {
               if (b <= 55) {
                  l17 = l17 - -1320116705970035250L ^ 913873987486759758L;
               } else {
                  if (unsignedLongCompare(l4 * l9, make64(l10, l12))) {
                     l17 = (l17 ^ 8593923163877073211L) + -3888584719987706532L;
                     continue;
                  }

                  l17 = l17 - -8786926251919432106L ^ 9094546984008530897L;
               }
            } else {
               l4 = l5 / l13;
               l17 = l17 + 2123811169373054290L ^ 2646489368468006272L;
            }
         } else if (44 <= b) {
            if (b <= 44) {
               if (!needIncrement(l3 >>> l6, n3, n, shadedMutableBigInteger, l11)) {
                  l17 = (l17 ^ -2594425920060805560L) - -6028592323879452371L;
               } else {
                  l18 = l17 + 3888236741906994961L;
                  short s = 11233;
                  l17 = l18 ^ 4880196684606591235L;
               }
            } else if (b >= 48) {
               if (b <= 48) {
                  if (n2 == n4) {
                     l17 = (l17 ^ 7511215984197106053L) - 1485804641886650927L;
                     continue;
                  }

                  l17 = l17 - -5359906441285612995L ^ 3163912077617022654L;
               } else if (b >= 50) {
                  if (b <= 50) {
                     longs2 = divRemNegativeLong(l5, l13);
                     l17 = (l17 ^ 732225111476882279L) + -8902010013644520987L;
                  } else {
                     if (51 < b) {
                        l11 = mulsub(l7, l12, l13, l9, l4) >>> l6;
                        l17 = l17 - -3208023984704640563L ^ -4805209241668282735L;
                        continue;
                     }

                     if (!unsignedLongCompare(l16 * l9, make64(l10, l7))) {
                        l17 = (l17 ^ 6744120957210766257L) + 2149726743696875417L;
                     } else {
                        l17 = l17 - -3959787173199491829L ^ 7281471910798108669L;
                     }
                  }
               } else {
                  l10 = l5 - l16 * l13;
                  l17 = l17 - 1702083921388931141L ^ 5554138992624257108L;
               }
            } else {
               if (46 <= b) {
                  if (b <= 46) {
                     l18 = l15 + (long)n;
                     break;
                  }

                  if (l14 == 0L) {
                     l17 = l17 - 5369031750531287773L ^ 2345808586978713189L;
                     if (n4 != n2) {
                        return createAndStripZerosToMatchScale2(l15, n2, (long)n4);
                     }

                     return valueOf(l15, n2);
                  }

                  l17 = l17 + -1578836612244166591L ^ -1427625053108119363L;
                  continue;
               }

               if (l11 != 0L) {
                  l17 = l17 + 10941396376476388L ^ -7211550848182312988L;
                  continue;
               }

               l17 = l17 + -8266749340947040025L ^ 6829333044132781048L;
            }
         } else {
            if (39 > b) {
               if (37 > b) {
                  return shadedMutableBigInteger.toBigDecimal(n, n2);
               }

               if (37 >= b) {
                  l3 <<= l6;
                  l17 = (l17 ^ -3204176782921872476L) + -1307633020800237976L;
               } else {
                  l10 = 0L;
                  l17 = l17 - -647276837040872190L ^ -5848893230290108578L;
               }
               continue;
            }

            if (39 >= b) {
               if (n2 != n4) {
                  l17 = (l17 ^ -8926901075141402600L) + -4525949431088025820L;
               } else {
                  l17 = l17 - 61761079481840593L ^ 4362042863439193343L;
               }
               continue;
            }

            if (41 > b) {
               if (l4 >= 4294967296L) {
                  l17 = (l17 ^ 7279702749059525573L) + -5684953275118514879L;
               } else {
                  l17 = l17 - -2011587785025895801L ^ 7307750682243186028L;
               }
               continue;
            }

            if (41 >= b) {
               l5 = l2 << l6;
               l17 = l17 - -3722491499244957440L ^ 7657957554672873683L;
            } else {
               if (42 < b) {
                  if (!flag) {
                     l17 = l17 + 4066211614015406531L ^ 2476636128619574286L;
                     l18 = l15;
                     break;
                  }

                  l17 = l17 - 4299033567922967492L ^ -5893394127902956671L;
                  continue;
               }

               l13 = l3 >>> 32;
               l17 = (l17 ^ -3145038509364665283L) - 6569632116194183279L;
            }
         }

         while (true) {
            while (17 > b) {
               if (b >= 8) {
                  if (b <= 8) {
                     l17 = l17 - -1190989729542018186L ^ 3125638516184033798L;
                     continue outerLoop;
                  }

                  if (12 <= b) {
                     if (12 >= b) {
                        l15 = make64(l16, l4);
                        l17 = (l17 ^ -7626609657115510101L) + 1334465230068027074L;
                     } else if (14 <= b) {
                        if (b <= 14) {
                           shadedMutableBigInteger.add(ShadedMutableBigInteger.ONE);
                           l17 = l17 + 7551701883204806992L ^ 5326132198078445637L;
                        } else {
                           if (b <= 15) {
                              if (l5 < 0L) {
                                 l17 = l17 - -8389588902765778771L ^ -1480178138328741369L;
                              } else {
                                 l17 = l17 - 6768552788093160918L ^ 4574903037967734000L;
                              }
                              continue outerLoop;
                           }

                           if (l13 == 1L) {
                              l17 = l17 + -5383798978067372515L ^ 1884624803821983458L;
                              continue outerLoop;
                           }

                           l17 = l17 + -6813879521336293831L ^ -7839225618063192632L;
                        }
                     } else {
                        l10 += l13;
                        l17 = l17 - -6564639263771811115L ^ -3225558439847036934L;
                     }
                  } else if (b >= 10) {
                     if (10 >= b) {
                        l7 = l5 >>> 32;
                        l17 = l17 + -5735828254909810898L ^ 2294699984625713407L;
                        continue outerLoop;
                     }

                     if (l10 < 4294967296L) {
                        l17 = (l17 ^ -6452186132829737347L) + 8279573778422402455L;
                        continue outerLoop;
                     }

                     l17 = (l17 ^ -9062791985988242195L) + -5991387656588803246L;
                  } else {
                     l15 *= (long)n;
                     l17 = (l17 ^ 8115988552868467987L) + -7574306379484824031L;
                  }
               } else if (b >= 3) {
                  if (3 >= b) {
                     if (n4 == n2) {
                        l17 = (l17 ^ -8347857116417260028L) + -7473615379774142086L;
                     } else {
                        l17 = (l17 ^ -711152080536289736L) - -9098809223437767294L;
                     }
                  } else {
                     if (5 > b) {
                        l10 = longs2[0];
                        l17 = (l17 ^ -1781745312536131192L) - 5109406079269951691L;
                        continue outerLoop;
                     }

                     if (b <= 5) {
                        l7 = l5 & 4294967295L;
                        l17 = l17 - 913760306833300676L ^ -2379253625821148662L;
                     } else {
                        if (6 >= b) {
                           if (n3 != 1) {
                              l17 = (l17 ^ -8633852870855918011L) + 9222085023465642016L;
                           } else {
                              l17 = (l17 ^ -4173365976818696084L) + -244214761967483157L;
                           }
                           continue outerLoop;
                        }

                        l10 = l5 - l4 * l13;
                        l17 = (l17 ^ -6780593107600623490L) - -643586035455929103L;
                     }
                  }
               } else {
                  if (b < 1) {
                     return null;
                  }

                  if (b <= 1) {
                     if (l13 != 1L) {
                        l17 = l17 - -8892017125503570234L ^ 6401152668456993460L;
                     } else {
                        l17 = l17 + -8283904790824420378L ^ 3562438732929915661L;
                     }
                     continue outerLoop;
                  }

                  longs = divRemNegativeLong(l5, l13);
                  l17 = (l17 ^ -4472422286294406713L) + -1921187449639310755L;
               }
            }

            if (b <= 17) {
               l8 = l5 & 4294967295L;
               l17 = (l17 ^ 4686187499768140265L) - -5885852649285270402L;
            } else if (b >= 26) {
               if (26 >= b) {
                  l4 = longs2[1];
                  l17 = l17 - 5803882428003304578L ^ 1827954134433326387L;
               } else if (b >= 30) {
                  if (b <= 30) {
                     if (n3 == 1) {
                        l17 = l17 - 3563301170392728473L ^ -8268662182646557755L;
                        break;
                     }

                     l17 = (l17 ^ -1350452448973101048L) + 2140245712779690273L;
                  } else {
                     if (b < 32) {
                        l10 += l13;
                        l17 = l17 + -5333941847869957726L ^ -2064001738160833182L;
                        break;
                     }

                     if (32 >= b) {
                        l6 = (long)Long.numberOfLeadingZeros(l3);
                        l17 = (l17 ^ 7736579266229734878L) - 3975083687626193775L;
                        break;
                     }

                     if (33 >= b) {
                        shadedMutableBigInteger = new ShadedMutableBigInteger(new int[]{(int)l16, (int)l4});
                        l17 = (l17 ^ -138960767171728270L) - 4082887685234681930L;
                     } else {
                        customBigInteger = shadedMutableBigInteger.toBigInteger(n);
                        l17 = (l17 ^ -1261037334738007510L) - -1802803132620526998L;
                     }
                  }
               } else if (28 <= b) {
                  if (28 >= b) {
                     l17 = (l17 ^ -4303516340415547458L) + 3364586420454364355L;
                     break;
                  }

                  if ((int)l16 >= 0) {
                     l17 = (l17 ^ -6622264198392811137L) + 7768705024355622685L;
                  } else {
                     l17 = l17 - 2585241306451089324L ^ 1274652776780604041L;
                  }
               } else {
                  l16--;
                  l17 = (l17 ^ 7097348507666829053L) - -5516831867089579573L;
               }
            } else if (b >= 21) {
               if (21 >= b) {
                  l14 = mulsub(l7, l12, l13, l9, l4) >>> l6;
                  l17 = l17 - 298858035988657102L ^ -1278956979051227825L;
                  break;
               }

               if (b < 23) {
                  return shadedMutableBigInteger.toBigDecimal(n, n2);
               }

               if (23 >= b) {
                  l9 = l3 & 4294967295L;
                  l17 = (l17 ^ -6125130200025490499L) - 590755424394719980L;
                  break;
               }

               if (24 < b) {
                  return shadedMutableBigInteger.toBigDecimal(n, n2);
               }

               l17 = (l17 ^ -8191307825578903078L) - 6504956202235288780L;
            } else {
               if (19 <= b) {
                  if (19 < b) {
                     return createAndStripZerosToMatchScale(customBigInteger, n2, (long)n4);
                  }

                  l16 = longs[1];
                  l17 = (l17 ^ -3700963835569164097L) - -5443526965791875702L;
                  break;
               }

               l5 = mulsub(l8, l7, l13, l9, l16);
               l17 = (l17 ^ -8120145972262413406L) - 5178410696034164231L;
            }
         }
      }

      return valueOf(l18, n2);
   }

   public ShadedBigDecimal plus() {
      return this;
   }

   public ShadedBigDecimal divideToIntegralValue(ShadedBigDecimal shadedBigDecimal, ShadedMathContext shadedMathContext) {
      int n = 0;
      ShadedBigDecimal shadedBigDecimal2 = null;
      ShadedBigDecimal shadedBigDecimal3 = null;
      int n2 = 0;
      byte b = 10;

      outerLoop:
      while (true) {
         while (true) {
            if (b >= 3) {
               if (3 >= b) {
                  if ((n2 = shadedMathContext.precision - shadedBigDecimal2.precision()) > 0) {
                     ;
                  }
                  break;
               }

               if (5 <= b) {
                  if (5 >= b) {
                     shadedBigDecimal2 = this.divide(shadedBigDecimal, new ShadedMathContext(shadedMathContext.precision, CustomRoundingMode.DOWN));
                     break;
                  }

                  n = saturateLong((long)this.scale - (long)shadedBigDecimal.scale);
                  continue;
               }

               if (shadedMathContext.precision != 0) {
                  ;
               }
               break;
            }

            if (1 <= b) {
               if (b <= 1) {
                  shadedBigDecimal3 = shadedBigDecimal2.multiply(shadedBigDecimal);
               }
            } else {
               shadedBigDecimal2 = shadedBigDecimal2.setScale((byte)0, CustomRoundingMode.DOWN);
            }
            break;
         }

         do {
            while (b > 7) {
               if (11 <= b) {
                  if (11 >= b) {
                     throw new ArithmeticException("Division impossible");
                  }

                  if (13 > b) {
                     return shadedBigDecimal2.setScale(shadedBigDecimal2.getScale() + Math.min(n2, n - shadedBigDecimal2.scale));
                  }

                  if (b <= 13) {
                     int scale2 = shadedBigDecimal2.scale;
                     long intCompact2 = shadedBigDecimal2.intCompact;
                     return stripZerosToMatchScale(shadedBigDecimal2.intVal, intCompact2, scale2, n);
                  }

                  if (b <= 14) {
                     return this.divideToIntegralValue(shadedBigDecimal);
                  }

                  if (n > shadedBigDecimal2.getScale()) {
                     continue outerLoop;
                  }
               } else if (b >= 9) {
                  if (9 >= b) {
                     if (shadedBigDecimal2.getScale() < 0) {
                        continue outerLoop;
                     }
                  } else {
                     ShadedBigDecimal shadedBigDecimal12 = this;
                     shadedBigDecimal12 = shadedBigDecimal12.subtract(shadedBigDecimal3);
                     if (shadedBigDecimal12.compareMagnitude(shadedBigDecimal) < 0) {
                        continue outerLoop;
                     }
                  }
               } else {
                  if (this.compareMagnitude(shadedBigDecimal) >= 0) {
                     continue outerLoop;
                  }
               }
            }
         } while (shadedBigDecimal2.getScale() > 0);
      }
   }

   private static ShadedBigDecimal divide(long l, int n, CustomBigInteger customBigInteger, int n2, long l2, ShadedMathContext shadedMathContext) {
      int n3 = 0;
      int n4 = 0;
      CustomBigInteger customBigInteger2 = null;
      int n5 = 0;
      int n6 = 0;
      ShadedBigDecimal shadedBigDecimal = null;
      int n7 = 0;
      CustomBigInteger customBigInteger3 = null;
      byte b = 13;
      long l3 = 0L;
      l3 = -4243126798031450137L ^ (long)"䟻썮戴\ueff6鄲撶ᮆꓵ韠刷듎ᐪ鏉䭩톒亇펇䅃睯툭⅘셮⑹柃ﻄ턄ᓀ행漢".hashCode();

      while (true) {
         if (b <= 7) {
            if (compareMagnitudeNormalized(l, n, customBigInteger, n2) > 0) {
               long l4 = l3 + 1535292127964116754L;
               short s = 25086;
               l3 = l4 ^ 1649786803719539888L;
               continue;
            }

            l3 = (l3 ^ 8955956351234347491L) + -2103402162556260967L;
         } else if (b >= 11) {
            if (11 >= b) {
               n3 = checkScaleNonZero((long)n5 - (long)n2);
               l3 = (l3 ^ -4779038823176879432L) - -5670743309146703162L;
               continue;
            }

            if (13 > b) {
               n5 = checkScaleNonZero((long)n - (long)n7);
               l3 = (l3 ^ 3678257427122703464L) + -5352939996316228623L;
               continue;
            }

            if (b <= 13) {
               if (checkScaleNonZero((long)n7 + (long)n2 - (long)n) <= 0) {
                  l3 = l3 + -7808289750703612087L ^ 4190534473409816357L;
                  continue;
               }

               l3 = l3 - 1185727197906394822L ^ -774141643570771398L;
            } else {
               customBigInteger2 = bigMultiplyPowerTen2(customBigInteger, n3);
               l3 = (l3 ^ -4548779484904167808L) - -7649576652352526081L;
            }
         } else {
            if (b < 9) {
               n4 = checkScaleNonZero(l2 + (long)n2 - (long)n + (long)n7);
               l3 = (l3 ^ -5248046478674931933L) + -9071263338349696905L;
               continue;
            }

            if (b <= 9) {
               l3 = (l3 ^ -6466201397228342667L) + -3862902966156918409L;
            } else {
               n2--;
               l3 = l3 + 8862983587913093583L ^ -1681694567779870659L;
            }
         }

         while (true) {
            while (b < 3) {
               if (1 <= b) {
                  if (b <= 1) {
                     n7 = shadedMathContext.precision;
                     l3 = (l3 ^ 2722958758644619472L) - -8684202627526489155L;
                  } else {
                     shadedBigDecimal = divideAndRound3(CustomBigInteger.valueOf(l), customBigInteger2, n4, n6, checkScaleNonZero(l2));
                     l3 = (l3 ^ 257146299311960218L) + 5286254154429967993L;
                  }
               } else {
                  n5 = checkScaleNonZero((long)n7 + (long)n2 - (long)n);
                  l3 = l3 - 3609435794080217887L ^ 5061300423193255628L;
               }
            }

            if (3 >= b) {
               n6 = shadedMathContext.roundingMode.oldMode;
               l3 = (l3 ^ -6395057109847599536L) + 6871097011256505263L;
               break;
            }

            if (5 > b) {
               return doRound(shadedBigDecimal, shadedMathContext);
            }

            if (5 >= b) {
               shadedBigDecimal = divideAndRound3(customBigInteger3, customBigInteger, n4, n6, checkScaleNonZero(l2));
               l3 = l3 - -3430130445477629872L ^ -6246172486473955165L;
               break;
            }

            customBigInteger3 = bigMultiplyPowerTen(l, n5);
            l3 = l3 - -5983431244975094882L ^ -4194516920938732403L;
         }
      }
   }

   private static long[] divRemNegativeLong(long l, long l2) {
      long l3 = 0L;
      long l4 = 0L;
      byte b = 8;
      long l5 = 0L;
      l5 = 7400628866210122133L ^ (long)"給ꢀ俍큼㣪檞ﵨ꒾芺窪\udd62ꝡ鮣樓\uf3eeº\uf427뒳媌뒕䤼瘩ꬕ짞ᥐ".hashCode();

      outerLoop:
      while (true) {
         if (b <= 8) {
            if (l < 0L) {
               l5 = (l5 ^ 4324280101644934369L) - -7905268815674655999L;
            } else {
               l5 = (l5 ^ -4513142195685400707L) + 7898995223241192211L;
            }
         } else if (b >= 12) {
            if (12 >= b) {
               if (!assertionsDisabled) {
                  l5 = l5 + 9007624262118992340L ^ 5486191672623505774L;
                  continue;
               }

               l5 = (l5 ^ 4181718336299688262L) - -3521336113377878914L;
            } else if (b >= 14) {
               if (14 >= b) {
                  l4 = (l >>> 1) / (l2 >>> 1);
                  l5 = l5 + 4515646745031978125L ^ -5312516958321309172L;
                  continue;
               }

               if (b <= 15) {
                  l4++;
                  l5 = l5 - 3835492713512751389L ^ 1638463381176799192L;
                  continue;
               }

               if (l2 != 1L) {
                  l5 = (l5 ^ -7174014021890527290L) + 5807183499546054122L;
                  continue;
               }

               l5 = (l5 ^ 2394500451764654776L) + -8358290617537576497L;
            } else {
               l5 = l5 - 3134432023352671821L ^ -3367323439007306758L;
            }
         } else {
            if (10 > b) {
               l3 -= l2;
               l5 = l5 - 2062097736509573235L ^ 3458474628191763503L;
               continue;
            }

            if (b > 10) {
               l3 = l - l4 * l2;
               l5 = (l5 ^ -7744876909438451030L) - 6995309375634921361L;
               continue;
            }

            if (l3 >= 0L) {
               l5 = (l5 ^ 5747376469440769758L) + 760431923813001609L;
            } else {
               l5 = l5 - 910821101304378636L ^ -1327806645911771313L;
            }
         }

         while (true) {
            while (b < 3) {
               if (1 > b) {
                  return new long[]{l3, l4};
               }

               if (1 >= b) {
                  if (assertionsDisabled) {
                     l5 = (l5 ^ 2203682513496943930L) + 4776727211160735415L;
                  } else {
                     l5 = (l5 ^ 8133888096700576247L) - -8887760157504080972L;
                  }
                  continue outerLoop;
               }

               l3 += l2;
               l5 = (l5 ^ -289995040028860198L) + -206985000906540154L;
            }

            if (3 >= b) {
               if (l3 >= l2) {
                  l5 = l5 - 101318872040833477L ^ -8090023642865849015L;
                  break;
               }

               l5 = (l5 ^ -8952754789776809846L) - 1209119962020971945L;
            } else {
               if (b < 5) {
                  throw new AssertionError("Unity denominator");
               }

               if (b <= 5) {
                  throw new AssertionError("Non-negative numerator " + l);
               }

               if (b <= 6) {
                  l5 = l5 - 3940961172725051849L ^ -3854806811626203907L;
                  break;
               }

               l4--;
               l5 = l5 - -3083879207362102825L ^ -1422618821042334758L;
            }
         }
      }
   }

   public static int longDigitLength(long l) {
      long[] longs = null;
      int n = 0;
      byte b = 3;
      long l2 = 0L;
      l2 = -1903056407078958400L ^ (long)"ﱡ胱\uf736杀ⷎ᠆\ue6d4梾䳗茗図儿\uf38a㧿邱\uf7c9㢩尵繟䬋\uebabL톙\uda11".hashCode();

      int n2;
      outerLoop:
      while (true) {
         if (5 >= b) {
            n2 = n;
            break;
         }

         if (b >= 8) {
            if (b <= 8) {
               if (!assertionsDisabled) {
                  l2 = l2 + 233103214828519792L ^ 922694033226596697L;
                  continue;
               }

               l2 = l2 + -1147200408136096025L ^ -5436885547564434697L;
            } else {
               if (10 <= b) {
                  if (10 >= b) {
                     throw new AssertionError();
                  }

                  if (l >= longs[n]) {
                     l2 = l2 + -8286522621446724020L ^ -5611259114636896294L;
                     n2 = n + 1;
                     break;
                  }

                  l2 = (l2 ^ -5179989184338013798L) - -7121262398810522620L;
                  continue;
               }

               n = (64 - Long.numberOfLeadingZeros(l) + 1) * 1233 >>> 12;
               l2 = l2 + -1286449581317195061L ^ -7419914164936846285L;
            }
         } else {
            if (b > 6) {
               if (n >= longs.length) {
                  l2 = l2 - -4042340813757555320L ^ 595864297865882305L;
               } else {
                  l2 = l2 - -4829302458543606399L ^ 3643473801410363224L;
               }
               continue;
            }

            if (l == -9223372036854775808L) {
               l2 = l2 + -6939488150661337551L ^ 2663533640461257938L;
               continue;
            }

            l2 = l2 + -9050441957707656031L ^ 3251754969332992138L;
         }

         while (true) {
            while (2 > b) {
               if (0 >= b) {
                  longs = LONG_TEN_POWERS_TABLE;
                  l2 = (l2 ^ -5262004271815135335L) - 498254582447521182L;
                  continue outerLoop;
               }

               if (l >= 10L) {
                  l2 = (l2 ^ 293159818832800454L) + 8846190320966491732L;
                  continue outerLoop;
               }

               l2 = l2 - 3218389866697008341L ^ -1877930268241138418L;
            }

            if (2 >= b) {
               if (l >= 0L) {
                  l2 = l2 + -8888581525744756882L ^ 663131404445352127L;
               } else {
                  l2 = (l2 ^ 5557061938389484811L) + -4205112147703084891L;
               }
            } else {
               if (3 < b) {
                  return 1;
               }

               l = -l;
               l2 = l2 - -107384340245450709L ^ -1029986571749849924L;
            }
         }
      }

      return n2;
   }

   private CustomBigInteger inflated() {
      return this.intVal != null ? this.intVal : CustomBigInteger.valueOf(this.intCompact);
   }

   private static int checkScaleNonZero(long l) {
      int n = 0;
      long l2 = 0L;
      l2 = -2357409634226818925L ^ (long)"\udd40稅㦮\u2065㹽忷黝࿈빼ⶭ쉢ꮚ쳑\ufffeꊼ\ue374┘㢺㽃꙾杕˄灳ꓽƛ뜷".hashCode();
      n = (int)l;
      l2 = l2 - 7460522938646581198L ^ 7485293174395625321L;
      if ((long)n == l) {
         l2 = (l2 ^ 4127431875670106274L) + 9136944103425204414L;
         return n;
      } else {
         l2 = l2 + -3985533989843969868L ^ 9144873094175271340L;
         throw new ArithmeticException(n > 0 ? "Underflow" : "Overflow");
      }
   }

   private static boolean longLongCompareMagnitude(long l, long l2, long l3, long l4) {
      long l5 = 0L;
      l5 = 9180932170353606573L ^ (long)"㒋⿶ᤏ뉛払匛ጩ贽주淪齆性뵮䬲\uf3bf鬤ࠅ᭔蕒杁⛕\ue7db覰꧱座貶\udfaa蠺".hashCode();
      if (l == l3) {
         l5 = (l5 ^ -8214333923119394431L) - 2881845161047060616L;
         return l2 + -9223372036854775808L < l4 + -9223372036854775808L;
      } else {
         l5 = (l5 ^ -4617381903297984768L) + -4034046960456491721L;
         boolean flag;
         if (l >= l3) {
            l5 = (l5 ^ -5038487107570225331L) + -7577600909283846913L;
            flag = false;
         } else {
            l5 = (l5 ^ 433091323437408320L) - -4244080760095236098L;
            flag = true;
         }

         return flag;
      }
   }

   public ShadedBigDecimal subtract(ShadedBigDecimal shadedBigDecimal) {
      byte b = 3;
      long l = 0L;
      l = 4647452255115752003L ^ (long)"觫⁴\u2d73ѷ\ue813\uf456﹎뙷쾴攅姺阧玜ᆀ戮\uf6ac揼響싁衛໒䯅㯇饪㶜".hashCode();
      if (this.intCompact == -9223372036854775808L) {
         l = l - -5464715051977045218L ^ 5294798902832777176L;
         if (shadedBigDecimal.intCompact == -9223372036854775808L) {
            l = (l ^ -2344840262747269658L) + -1920077860929919127L;
         } else {
            l = l + 3484406828422903072L ^ 87518505109916954L;
         }
      } else {
         l = (l ^ 342665663726139068L) + 3369670021464049129L;
         if (shadedBigDecimal.intCompact == -9223372036854775808L) {
            l = l + -3417238367247306458L ^ 3200181985554145078L;
         } else {
            l = (l ^ -1140878442804996315L) + 6431409676429532861L;
         }
      }

      if (3 >= b) {
         return add2(this.intCompact, this.scale, shadedBigDecimal.intVal.negate(), shadedBigDecimal.scale);
      } else if (b >= 5) {
         if (b <= 5) {
            return add(this.intCompact, this.scale, -shadedBigDecimal.intCompact, shadedBigDecimal.scale);
         } else {
            long l2 = -shadedBigDecimal.intCompact;
            int scale4 = this.scale;
            return add2(l2, shadedBigDecimal.scale, this.intVal, scale4);
         }
      } else {
         return add3(this.intVal, this.scale, shadedBigDecimal.intVal.negate(), shadedBigDecimal.scale);
      }
   }

   private static ShadedBigDecimal divide7(long l, int n, CustomBigInteger customBigInteger, int n2, int n3, int n4) {
      CustomBigInteger customBigInteger2 = null;
      int n5 = 0;
      int n6 = 0;
      byte b = 0;
      long l2 = 0L;
      l2 = 1172424547998407627L ^ (long)"湳츜▔\uf71c횡暗謙ﮗ煁남눲\ufdcdṍ躎餧㼐쏂ᔯ㲌홃".hashCode();

      while (true) {
         outerLoop:
         while (b > 4) {
            if (6 <= b) {
               if (b <= 6) {
                  n5 = n6 - n2;
                  l2 = l2 - 9191204267596203593L ^ -6467897997455208675L;
                  continue;
               }

               if (b > 7) {
                  return divideAndRound3(customBigInteger2, customBigInteger, n3, n4, n3);
               }

               customBigInteger2 = bigMultiplyPowerTen2(customBigInteger, n5);
               l2 = l2 - 2409384396812078296L ^ -3218704762426607353L;
            } else {
               if (checkScale2(l, (long)n3 + (long)n2) <= n) {
                  l2 = l2 - -7685166708236824348L ^ 6551516857855008311L;
               } else {
                  l2 = (l2 ^ 3508927140718462025L) + 4170225617741909216L;
               }
            }

            while (1 <= b) {
               if (1 < b) {
                  if (2 >= b) {
                     n5 = n6 - n;
                     l2 = (l2 ^ -4374093931527562444L) - 2441921088159456483L;
                  } else {
                     n6 = checkScale(customBigInteger, (long)n - (long)n3);
                     l2 = l2 + 9185713894140929583L ^ -2574834263530959713L;
                  }
                  continue outerLoop;
               }

               n6 = n3 + n2;
               l2 = (l2 ^ 7983919382797963127L) + -346587284261860357L;
            }

            return divideAndRound3(CustomBigInteger.valueOf(l), customBigInteger2, n3, n4, n3);
         }

         customBigInteger2 = bigMultiplyPowerTen(l, n5);
         l2 = (l2 ^ 1374284425776741912L) + 5079569026678586740L;
      }
   }

   private int adjustScale(int n, long l) {
      long l2 = 0L;
      byte b = 2;
      long l3 = 0L;
      l3 = 1403928461590101987L ^ (long)"⚬⧖쭴ꬶ隰䪱\ueea6䳦벟ᢢ듉쿛톯猍禭첰⾖پ㘇0".hashCode();

      while (true) {
         while (b > 2) {
            if (b >= 4) {
               if (4 < b) {
                  throw new NumberFormatException("Scale out of range.");
               }

               l2 = (long)n - l;
               l3 = l3 - -495866386767127780L ^ 5349774906386977484L;
            } else {
               if (l2 >= -2147483648L) {
                  l3 = l3 + 2568274623058710983L ^ -1962447734611243765L;
                  n = (int)l2;
                  l3 = l3 - -4392819409869102412L ^ -1964120939616670106L;
                  return n;
               }

               l3 = (l3 ^ -406972576654294122L) + -3973038608365800852L;
            }
         }

         if (l2 > 2147483647L) {
            l3 = l3 + 5451729109557488249L ^ 6857483863767927235L;
         } else {
            l3 = (l3 ^ -3733586281510945284L) - -7192626324319529606L;
         }
      }
   }

   private static ShadedBigDecimal stripZerosToMatchScale(CustomBigInteger customBigInteger, long l, int n, int n2) {
      byte b = 3;
      long l2 = 0L;
      l2 = 1250597830845554370L ^ (long)"銷챉秽㋍屣롚\uf5f8穃瓙荞䌫䥩䇘兰懷賨\ue9f0秌䵥\uf493뇧薔ਛ䰅跥᧥".hashCode();

      CustomBigInteger customBigInteger2;
      while (true) {
         if (b > 1) {
            if (b <= 2) {
               return createAndStripZerosToMatchScale2(l, n, (long)n2);
            }

            customBigInteger2 = INFLATED_BIGINT;
            break;
         }

         if (l == -9223372036854775808L) {
            l2 = l2 + -7358371696520810374L ^ 2373592468609634177L;
            if (customBigInteger != null) {
               l2 = l2 + 6178941865983896270L ^ 2286833770092927309L;
               customBigInteger2 = customBigInteger;
               break;
            }

            l2 = l2 - -8024441847514456046L ^ 367305663520577133L;
         } else {
            l2 = l2 + 6592632262661698920L ^ 2816516402927638183L;
         }
      }

      return createAndStripZerosToMatchScale(customBigInteger2, n, (long)n2);
   }

   private String getValueString(int n, String str, int n2) {
      boolean flag = false;
      Object obj = null;
      int n3 = 0;
      n3 = str.length() - n2;
      if (n3 == 0) {
         StringBuilder sb = new StringBuilder();
         String str2;
         if (n < 0) {
            str2 = "-0.";
         } else {
            short s = 20012;
            str2 = "0.";
         }

         return sb.append(str2).append(str).toString();
      } else {
         if (n3 > 0) {
            obj = new StringBuilder(str);
            ((StringBuilder)obj).insert(n3, '.');
            if (n < 0) {
               ((StringBuilder)obj).insert(0, '-');
            }
         } else {
            obj = new StringBuilder(3 - n3 + str.length());
            StringBuilder sb2 = (StringBuilder)obj;
            String str3;
            if (n < 0) {
               str3 = "-0.";
            } else {
               short s2 = 20512;
               str3 = "0.";
            }

            sb2.append(str3);

            for (int i = 0; i < -n3; i++) {
               ((StringBuilder)obj).append('0');
            }

            ((StringBuilder)obj).append(str);
         }

         return ((StringBuilder)obj).toString();
      }
   }

   public int getScale() {
      return this.scale;
   }

   public ShadedBigDecimal remainder(ShadedBigDecimal shadedBigDecimal) {
      return this.divideAndRemainder(shadedBigDecimal)[1];
   }

   private static CustomBigInteger expandBigIntegerTenPowers(int n) {
      synchronized (ShadedBigDecimal.class) {
         CustomBigInteger[] customBigIntegers = BIG_TEN_POWERS_TABLE;
         int length = customBigIntegers.length;
         if (length <= n) {
            int n2 = length << 1;

            while (n2 <= n) {
               n2 <<= 1;
            }

            customBigIntegers = Arrays.copyOf(customBigIntegers, n2);

            for (int i = length; i < n2; i++) {
               customBigIntegers[i] = customBigIntegers[i - 1].multiply2(CustomBigInteger.TEN);
            }

            BIG_TEN_POWERS_TABLE = customBigIntegers;
         }

         return customBigIntegers[n];
      }
   }

   private static ShadedBigDecimal createAndStripZerosToMatchScale2(long l, int n, long l2) {
      long l3 = 0L;
      byte b = 6;
      long l4 = 0L;
      l4 = 2807821931743654037L ^ (long)"雅랓뭥뭦鿤뒃婗샶뚯誚緦偈倖ﱛ㟃\uecbf攭灐\uf52a\ue7d6\ufe75ቨ".hashCode();

      while (true) {
         while (b > 5) {
            if (8 <= b) {
               if (8 >= b) {
                  l4 = l4 + -3943208948087927312L ^ 8015774842887402746L;
                  continue;
               }

               if (9 < b) {
                  return valueOf(l, n);
               }

               if (l3 != 0L) {
                  l4 = (l4 ^ -2199756827450617225L) - 4663610837448911629L;
                  continue;
               }

               l4 = (l4 ^ 2605251902678245669L) - 1990239681691871201L;
            } else {
               if (6 < b) {
                  l4 = (l4 ^ 7242503030795078699L) + -21033508665106096L;
                  continue;
               }

               if (Math.abs(l) < 10L) {
                  l4 = (l4 ^ 1009369940889139593L) - -7787156079773072181L;
                  continue;
               }

               l4 = (l4 ^ 6047487388868821696L) - -6290041052272707238L;
            }

            while (true) {
               while (b < 2) {
                  if (0 >= b) {
                     n = checkScale2(l, (long)n - 1L);
                     l4 = l4 + -8389016713421036784L ^ 293683165066709846L;
                  } else {
                     l /= 10L;
                     l4 = l4 - 1908777566820985117L ^ 1466223244020684446L;
                  }
               }

               if (b <= 2) {
                  if ((l & 1L) == 0L) {
                     l4 = l4 - 2910203797284518554L ^ 8574509751936902614L;
                  } else {
                     l4 = (l4 ^ -2445487694858789975L) + 4161447639913765093L;
                  }
                  break;
               }

               if (3 >= b) {
                  l4 = l4 - 596413895694505351L ^ -3522414577346633962L;
                  break;
               }

               if ((long)n <= l2) {
                  l4 = (l4 ^ -8367977003458108270L) - -5570460222519073466L;
                  break;
               }

               l4 = l4 - -753554432944705531L ^ -5708839231507669784L;
            }
         }

         l3 = l % 10L;
         l4 = (l4 ^ 6559251258624237816L) - 9182639355496713899L;
      }
   }

   public ShadedBigDecimal min(ShadedBigDecimal shadedBigDecimal) {
      return this.compareTo2(shadedBigDecimal) <= 0 ? this : shadedBigDecimal;
   }

   public ShadedBigDecimal abs() {
      ShadedBigDecimal shadedBigDecimal;
      if (this.signum() < 0) {
         shadedBigDecimal = this.negate();
      } else {
         shadedBigDecimal = this;
      }

      return shadedBigDecimal;
   }

   public ShadedBigDecimal remainder(ShadedBigDecimal shadedBigDecimal, ShadedMathContext shadedMathContext) {
      return this.divideAndRemainder(shadedBigDecimal, shadedMathContext)[1];
   }

   public ShadedBigDecimal(char[] chars, int n, int n2) {
       this(chars, n, n2, ShadedMathContext.UNLIMITED);
   }

   private int compareMagnitude(ShadedBigDecimal shadedBigDecimal) {
      long l = 0L;
      long l2 = 0L;
      Object obj = null;
      long l3 = 0L;
      long l4 = 0L;
      long l5 = 0L;
      byte b = 2;
      long l6 = 0L;
      l6 = 1765493945360103053L ^ (long)"軠惒襯娿㭰㤘吟螈徟婃启㠹䓇Ꭸ鴇誒嫁\ude5d謯轍偟\udacb㋾".hashCode();

      byte b2;
      while (true) {
         if (2 >= b) {
            b2 = 0;
            break;
         }

         if (b <= 3) {
            l4 = shadedBigDecimal.intCompact;
            l6 = (l6 ^ 8856725532009855005L) + 8048080112147374642L;
         } else {
            l5 = this.intCompact;
            l6 = l6 - -6094429739453432743L ^ 5884686489049826836L;
            if (l5 != 0L) {
               l6 = (l6 ^ -540065393319519658L) - -8485486358303390160L;
               if (l4 == 0L) {
                  return 1;
               }

               l3 = (long)this.scale - (long)shadedBigDecimal.scale;
               if (l3 != 0L) {
                  l = (long)this.precision() - (long)this.scale;
                  l2 = (long)shadedBigDecimal.precision() - (long)shadedBigDecimal.scale;
                  if (l < l2) {
                     return -1;
                  }

                  if (l > l2) {
                     return 1;
                  }

                  obj = null;
                  if (l3 < 0L) {
                     if (l3 > -2147483648L
                        && (l5 == -9223372036854775808L || (l5 = longMultiplyPowerTen(l5, (int)(-l3))) == -9223372036854775808L)
                        && l4 == -9223372036854775808L) {
                        obj = this.bigMultiplyPowerTen((int)(-l3));
                        return ((CustomBigInteger)obj).compareMagnitude(shadedBigDecimal.intVal);
                     }
                  } else if (l3 <= 2147483647L
                     && (l4 == -9223372036854775808L || (l4 = longMultiplyPowerTen(l4, (int)l3)) == -9223372036854775808L)
                     && l5 == -9223372036854775808L) {
                     obj = shadedBigDecimal.bigMultiplyPowerTen((int)l3);
                     CustomBigInteger customBigInteger2 = (CustomBigInteger)obj;
                     return this.intVal.compareMagnitude(customBigInteger2);
                  }
               }

               if (l5 != -9223372036854775808L) {
                  return l4 != -9223372036854775808L ? longCompareMagnitude(l5, l4) : -1;
               }

               if (l4 != -9223372036854775808L) {
                  return 1;
               }

               CustomBigInteger intVal3 = shadedBigDecimal.intVal;
               return this.intVal.compareMagnitude(intVal3);
            }

            l6 = (l6 ^ -9211263010078620548L) - 7166380550709947458L;
            if (l4 != 0L) {
               l6 = (l6 ^ 6065504418099525152L) + 4446004518234580492L;
               b2 = -1;
               break;
            }

            l6 = (l6 ^ -5170594719395713429L) + -2755434675002319403L;
         }
      }

      return b2;
   }

   private static ShadedBigDecimal roundedTenPower(int n, int n2, int n3, int n4) {
      int n5 = 0;
      byte b = 5;

      while (b > 2) {
         if (4 <= b) {
            if (b <= 4) {
               return scaledTenPow(n2 - n5, n, n4);
            }

            if (n5 >= n2) {
               return valueOf((long)n, n3 - n2);
            }
         } else if (n3 > n4) {
            n5 = n3 - n4;
         }
      }

      return scaledTenPow(n2, n, n3);
   }

   public ShadedBigDecimal pow(int n, ShadedMathContext shadedMathContext) {
      boolean flag = false;
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      ShadedBigDecimal shadedBigDecimal = null;
      ShadedBigDecimal shadedBigDecimal2 = null;
      ShadedMathContext shadedMathContext2 = null;
      byte b = 9;

      outerLoop:
      while (true) {
         if (15 >= b) {
            if (shadedMathContext.precision == 0) {
               ;
            }
         } else if (23 <= b) {
            if (23 >= b) {
               shadedMathContext2 = shadedMathContext;
               continue;
            }

            if (27 <= b) {
               if (27 >= b) {
                  if (n4 < 0) {
                     continue;
                  }
               } else {
                  if (b < 29) {
                     continue;
                  }

                  if (b <= 29) {
                     if (shadedMathContext.precision > 0) {
                        ;
                     }
                  } else if (flag) {
                     continue;
                  }
               }
            } else if (b >= 25) {
               if (b <= 25) {
                  if (n < 0) {
                     ;
                  }
               } else {
                  flag = true;
               }
            } else if (n == 0) {
               ;
            }
         } else if (19 <= b) {
            if (b <= 19) {
               throw new ArithmeticException("Invalid operation");
            }

            if (21 > b) {
               continue;
            }

            if (21 >= b) {
               flag = false;
            } else if (n <= 999999999) {
               continue;
            }
         } else {
            if (17 > b) {
               n4 = Math.abs(n);
               continue;
            }

            if (b > 17) {
               n4 += n4;
               continue;
            }

            shadedBigDecimal2 = shadedBigDecimal2.multiply(shadedBigDecimal2, shadedMathContext2);
         }

         while (true) {
            while (b < 7) {
               if (3 <= b) {
                  if (b <= 3) {
                     throw new ArithmeticException("Invalid operation");
                  }

                  if (5 > b) {
                     n3++;
                     continue outerLoop;
                  }

                  if (b <= 5) {
                     shadedMathContext2 = new ShadedMathContext(shadedMathContext.precision + n2 + 1, shadedMathContext.roundingMode);
                  } else {
                     ShadedBigDecimal shadedBigDecimal6 = shadedBigDecimal2;
                     ShadedMathContext shadedMathContext4 = shadedMathContext2;
                     shadedBigDecimal2 = ONE.divide(shadedBigDecimal6, shadedMathContext4);
                  }
               } else {
                  if (b >= 1) {
                     if (1 < b) {
                        return ONE;
                     }

                     if (n3 == 31) {
                        ;
                     }
                     continue outerLoop;
                  }

                  shadedBigDecimal2 = shadedBigDecimal2.multiply(shadedBigDecimal, shadedMathContext2);
               }
            }

            if (7 >= b) {
               n2 = longDigitLength((long)n4);
            } else if (b >= 11) {
               if (11 < b) {
                  if (b < 13) {
                     return doRound(shadedBigDecimal2, shadedMathContext);
                  }

                  if (13 >= b) {
                     return this.pow(n);
                  }

                  shadedBigDecimal2 = ONE;
                  break;
               }

               if (n2 > shadedMathContext.precision) {
                  break;
               }
            } else {
               if (9 > b) {
                  shadedBigDecimal = this;
                  break;
               }

               if (b <= 9) {
                  n3 = 1;
                  break;
               }

               if (n >= -999999999) {
                  break;
               }
            }
         }
      }
   }

   private static ShadedBigDecimal add3(CustomBigInteger customBigInteger, int n, CustomBigInteger customBigInteger2, int n2) {
      int n3 = 0;
      long l = 0L;
      CustomBigInteger customBigInteger3 = null;
      int n4 = 0;
      byte b = 9;
      long l2 = 0L;
      l2 = 3450051893770008804L ^ (long)"ⵒ褂쀒珡\uf36b陫ʵ旆㌽庉䈑\uf4db숻ỷ鈟틯\u0ef0⒴Ｔ屯\u2d28\ueb0b\udd51".hashCode();

      outerLoop:
      while (true) {
         if (b <= 6) {
            n4 = n;
            l2 = l2 + -8117122956313919272L ^ -8011853091051257280L;
         } else if (b >= 9) {
            if (b <= 9) {
               return new ShadedBigDecimal(customBigInteger3, -9223372036854775808L, n4, 0);
            }

            if (b >= 11) {
               if (11 >= b) {
                  if (l == 0L) {
                     l2 = l2 - 5079097702442365301L ^ -5276053137046305616L;
                     continue;
                  }

                  l2 = l2 + 1221176020593916748L ^ 6905156481529047842L;
               } else {
                  customBigInteger3 = customBigInteger.add2(customBigInteger2);
                  l2 = (l2 ^ 8057294564616337645L) + 5724171319932592649L;
               }
            } else {
               n3 = checkScale(customBigInteger, -l);
               l2 = l2 - -903238912471015650L ^ 3257636709869840084L;
            }
         } else {
            if (b <= 7) {
               customBigInteger2 = bigMultiplyPowerTen2(customBigInteger2, n3);
               l2 = l2 + 4342676956894633036L ^ -2825105756101828324L;
               continue;
            }

            customBigInteger = bigMultiplyPowerTen2(customBigInteger, n3);
            l2 = l2 + 6847475883281536300L ^ 2956770432613015391L;
         }

         while (b >= 2) {
            if (b <= 2) {
               l = (long)n4 - (long)n2;
               l2 = l2 + -6689103635727910970L ^ -8025299312795574496L;
               continue outerLoop;
            }

            if (4 > b) {
               if (customBigInteger.signum != customBigInteger2.signum) {
                  l2 = (l2 ^ 3743361531044656216L) - 4325305512716967362L;
                  return valueOf(customBigInteger3, n4, (byte)0);
               }

               l2 = (l2 ^ 2392910111469400781L) - 5638592625443546167L;
               continue outerLoop;
            }

            if (b <= 4) {
               n3 = checkScale(customBigInteger2, l);
               l2 = l2 - -7573859892348605758L ^ 2514470636575812317L;
               continue outerLoop;
            }

            if (l < 0L) {
               l2 = l2 - 2476719404705838737L ^ 2343455028641837842L;
               continue outerLoop;
            }

            l2 = l2 + 498251652181674010L ^ 1773417994793611322L;
         }

         if (0 >= b) {
            n4 = n2;
            l2 = (l2 ^ -2343319624733480027L) + 570536626185450087L;
         } else {
            l2 = (l2 ^ 197112131300383935L) - 6119195404937516886L;
         }
      }
   }

   public static ShadedBigDecimal valueOf2(double d) {
      return new ShadedBigDecimal(Double.toString(d));
   }

   public ShadedBigDecimal setScale(int n, CustomRoundingMode customRoundingMode) {
      return this.setScale2(n, customRoundingMode.oldMode);
   }

   private static boolean needIncrement(long l, int n, int n2, ShadedMutableBigInteger shadedMutableBigInteger, long l2) {
      int n3 = 0;
      byte b = 3;
      long l3 = 0L;
      l3 = -5369572308623247908L ^ (long)"̛휦试බ\uf042ᶊ욑샜蓇嘚첵믚ᮅ㩤Ⱞ῝祪偊㮀⬒".hashCode();
      if (assertionsDisabled) {
         l3 = (l3 ^ 1555809723764206889L) + 8442806027756931476L;
      } else {
         l3 = (l3 ^ 7461450714470281495L) - -6670085355108196524L;
      }

      while (true) {
         if (b <= 4) {
            if (l2 > -4611686018427387904L) {
               l3 = (l3 ^ 4061253056193692312L) + 1152564439102978653L;
               continue;
            }

            l3 = (l3 ^ 8070781521934411883L) - 8941550478774323339L;
         } else if (6 <= b) {
            if (b <= 6) {
               return commonNeedIncrement(n, n2, n3, shadedMutableBigInteger.isOdd());
            }

            if (7 < b) {
               l3 = (l3 ^ -270379226063476075L) + -5848346611462839028L;
               continue;
            }

            if (l2 != 0L) {
               l3 = (l3 ^ 7254564258764404956L) + -1798616311189191710L;
               continue;
            }

            l3 = l3 + 5607281879008287693L ^ -7005616127359748402L;
         } else if (l2 <= 4611686018427387903L) {
            l3 = l3 - -3172431847749698709L ^ 7922768051862427819L;
         } else {
            l3 = (l3 ^ 1986031026073633154L) + 4628162745364879113L;
         }

         if (b <= 1) {
            n3 = 1;
            l3 = (l3 ^ 6300427735556024383L) + -440974660394769425L;
         } else {
            if (b <= 2) {
               throw new AssertionError();
            }

            n3 = longCompareMagnitude(2L * l2, l);
            l3 = l3 - -5901197579130650560L ^ -6598315755653678114L;
         }
      }
   }

   private static ShadedBigDecimal divideAndRound3(CustomBigInteger customBigInteger, CustomBigInteger customBigInteger2, int n, int n2, int n3) {
      ShadedMutableBigInteger shadedMutableBigInteger = null;
      long l = 0L;
      Object obj = null;
      ShadedMutableBigInteger shadedMutableBigInteger2 = null;
      ShadedMutableBigInteger shadedMutableBigInteger3 = null;
      boolean flag = false;
      ShadedMutableBigInteger shadedMutableBigInteger4 = null;
      int n4 = 0;
      byte b = 5;

      while (b > 3) {
         if (b >= 5) {
            if (5 >= b) {
               flag = shadedMutableBigInteger2.isZero();
            } else {
               shadedMutableBigInteger3 = new ShadedMutableBigInteger();
               shadedMutableBigInteger4 = new ShadedMutableBigInteger(customBigInteger2.mag);
               shadedMutableBigInteger2 = shadedMutableBigInteger.divide2(shadedMutableBigInteger4, shadedMutableBigInteger3);
            }
         } else {
            shadedMutableBigInteger = new ShadedMutableBigInteger(customBigInteger.mag);
         }
      }

      n4 = customBigInteger.signum == customBigInteger2.signum ? 1 : -1;
      if (!flag) {
         ShadedMutableBigInteger shadedMutableBigInteger9 = shadedMutableBigInteger4;
         if (needIncrement3(shadedMutableBigInteger9, n2, n4, shadedMutableBigInteger3, shadedMutableBigInteger2)) {
            shadedMutableBigInteger9 = shadedMutableBigInteger3;
            shadedMutableBigInteger9.add(ShadedMutableBigInteger.ONE);
         }

         return shadedMutableBigInteger3.toBigDecimal(n4, n);
      } else if (n3 != n) {
         l = shadedMutableBigInteger3.toCompactValue(n4);
         if (l != -9223372036854775808L) {
            return createAndStripZerosToMatchScale2(l, n, (long)n3);
         } else {
            obj = shadedMutableBigInteger3.toBigInteger(n4);
            return createAndStripZerosToMatchScale((CustomBigInteger)obj, n, (long)n3);
         }
      } else {
         return shadedMutableBigInteger3.toBigDecimal(n4, n);
      }
   }

   public static ShadedBigDecimal valueOf(long l, int n) {
      byte b = 1;
      long l2 = 0L;
      l2 = -1394462103306789671L ^ (long)"\u139b慝趐\u05cd栨❵鈜왍슧啐瑏Ჯ菥綱桑\ue1a2ꢱ靳\ueaf2ꥡ煓\ueacc翉釦ꥲ".hashCode();

      while (true) {
         while (b > 2) {
            if (3 < b) {
               return new ShadedBigDecimal(l == -9223372036854775808L ? INFLATED_BIGINT : null, l, n, 0);
            }

            if (l == 0L) {
               l2 = (l2 ^ -7787896986447620398L) - 3416766374004585464L;
               return zeroValueOf(n);
            }

            l2 = l2 + -6099283584762689465L ^ -3476263375861283463L;
         }

         if (n == 0) {
            l2 = (l2 ^ 8273712523859789258L) + 2577022401269482418L;
            return valueOf(l);
         }

         l2 = (l2 ^ 7155856498795965190L) + 1684302762997132828L;
      }
   }

   private static ShadedBigDecimal add(long l, int n, long l2, int n2) {
      CustomBigInteger customBigInteger = null;
      long l3 = 0L;
      int n3 = 0;
      long l4 = 0L;
      byte b = 2;
      long l5 = 0L;
      l5 = -5081498621675572103L ^ (long)"֫ਬ몧붠⑺\ueb24ၼ씦쿉낍㌇繨\ue42a躣黪鋊\udfd6㥫硭뭜乌釜౫".hashCode();

      outerLoop:
      while (true) {
         if (2 <= b) {
            if (2 < b) {
               if (3 >= b) {
                  return add(l3, l2, n2);
               }

               l4 = (long)n - (long)n2;
               l5 = l5 + 6204520755431026484L ^ -6299531010150733701L;
               continue;
            }

            if (l4 != 0L) {
               l5 = (l5 ^ -4413348347569915376L) - 7288608839572198542L;
            } else {
               l5 = (l5 ^ 7781379179222235244L) + 7758326604319874059L;
            }
         } else {
            if (b <= 0) {
               return new ShadedBigDecimal(customBigInteger, -9223372036854775808L, n2, 0);
            }

            customBigInteger = bigMultiplyPowerTen(l, n3).add(l2);
            l5 = l5 + 5527154860876587272L ^ 7217323476243580853L;
         }

         while (true) {
            while (b > 5) {
               if (b >= 8) {
                  if (8 >= b) {
                     return add(l, l2, n);
                  }

                  if (9 >= b) {
                     if (l3 == -9223372036854775808L) {
                        l5 = l5 - -5405206332990244500L ^ 2970463719510865944L;
                     } else {
                        l5 = l5 + -9066567134747232083L ^ 1361310256866117566L;
                     }
                     continue outerLoop;
                  }

                  n3 = checkScale2(l, -l4);
                  l5 = (l5 ^ -7001932367132079224L) - -3012794313077621576L;
               } else {
                  if (b > 6) {
                     if ((l ^ l2) < 0L) {
                        l5 = (l5 ^ 7905268478800145589L) + -2100224214006657782L;
                        return valueOf(customBigInteger, n2, (byte)0);
                     }

                     l5 = l5 + -851362715415416460L ^ 3751006705692510263L;
                     continue outerLoop;
                  }

                  l3 = longMultiplyPowerTen(l, n3);
                  l5 = (l5 ^ -798371075481645408L) + -8111575678835970955L;
               }
            }

            if (l4 >= 0L) {
               l5 = (l5 ^ 2640351063863044246L) - -8765488563864173040L;
               n3 = checkScale2(l2, l4);
               l3 = longMultiplyPowerTen(l2, n3);
               if (l3 != -9223372036854775808L) {
                  return add(l, l3, n);
               }

               customBigInteger = bigMultiplyPowerTen(l2, n3).add(l);
               ShadedBigDecimal shadedBigDecimal;
               if ((l ^ l2) >= 0L) {
                  shadedBigDecimal = new ShadedBigDecimal(customBigInteger, -9223372036854775808L, n, 0);
               } else {
                  shadedBigDecimal = valueOf(customBigInteger, n, (byte)0);
               }

               return shadedBigDecimal;
            }

            l5 = l5 + 5812631363499661753L ^ -5650603041608135714L;
         }
      }
   }

   private static ShadedBigDecimal multiplyAndRound2(long l, CustomBigInteger customBigInteger, int n, ShadedMathContext shadedMathContext) {
      long l2 = 0L;
      l2 = -6369467045289444974L ^ (long)"둕뇸龶ﶋ껏쏈䋆荗螓\udcbc釦ꝣ麓姲쉹櫮Ⴕ\ue911\uf708䋞겭ᵆ奿".hashCode();
      if (l != 0L) {
         l2 = (l2 ^ 5241966705647205003L) + -891259281616430814L;
         return doRound(customBigInteger.multiply(l), n, shadedMathContext);
      } else {
         l2 = (l2 ^ -444668468252604089L) + 8151580594377682002L;
         return zeroValueOf(n);
      }
   }

   public ShadedBigDecimal divide2(ShadedBigDecimal shadedBigDecimal, int n, CustomRoundingMode customRoundingMode) {
      return this.divide(shadedBigDecimal, n, customRoundingMode.oldMode);
   }

   public ShadedBigDecimal(char[] chars, ShadedMathContext shadedMathContext) {
       this(chars, 0, chars.length, shadedMathContext);
   }

   public ShadedBigDecimal setScale2(int n, int n2) {
      int n3 = 0;
      Object obj = null;
      int n4 = 0;
      long l = 0L;
      int n5 = 0;
      CustomBigInteger customBigInteger = null;
      byte b = 12;
      long l2 = 0L;
      l2 = -209569885779983257L ^ (long)"ﺝ䀐凐툒髏ຳ謵緃赓肙\u175f\u2060\uebd0⠦\uf4e1礡\ud91a⨡쐐콁倃羫몦千鮽♐廰".hashCode();

      outerLoop:
      while (true) {
         if (3 <= b) {
            if (3 >= b) {
               return valueOf(l, n);
            }

            if (b >= 5) {
               if (b <= 5) {
                  if (this.intCompact == -9223372036854775808L) {
                     l2 = (l2 ^ 4502744561068914868L) - -7896168728513547301L;
                     if (n > n5) {
                        n4 = this.checkScale((long)n - (long)n5);
                        obj = bigMultiplyPowerTen2(this.intVal, n4);
                        return new ShadedBigDecimal((CustomBigInteger)obj, -9223372036854775808L, n, this.precision > 0 ? this.precision + n4 : 0);
                     }

                     n4 = this.checkScale((long)n5 - (long)n);
                     if (n4 < LONG_TEN_POWERS_TABLE.length) {
                        long l7 = LONG_TEN_POWERS_TABLE[n4];
                        return divideAndRound2(this.intVal, l7, n, n2, n);
                     }

                     return divideAndRound3(this.intVal, bigTenToThe(n4), n, n2, n);
                  }

                  l2 = (l2 ^ -5780563741668888099L) - -7632833676561787063L;
               } else {
                  if (n2 >= 0) {
                     l2 = (l2 ^ -5722589708692932339L) + -9204458648067213475L;
                     continue;
                  }

                  l2 = l2 - 3838746312041687509L ^ -3239629313728926477L;
               }
            } else {
               if (n != n5) {
                  l2 = l2 - -1954230562248101609L ^ 4287493612548170613L;
                  continue;
               }

               l2 = (l2 ^ 8660161371457211521L) + -147954330171424114L;
            }
         } else if (b >= 1) {
            if (1 >= b) {
               if (n2 <= 7) {
                  l2 = l2 + 1779590302940908757L ^ -4922028914291781523L;
               } else {
                  l2 = (l2 ^ -6159895248548946675L) - 8762673333661087881L;
               }
            } else {
               if (n <= n5) {
                  l2 = (l2 ^ 946489247171304627L) + -3858252754250296094L;
                  ShadedBigDecimal shadedBigDecimal3 = this;
                  n3 = shadedBigDecimal3.checkScale((long)n5 - (long)n);
                  if (n3 < LONG_TEN_POWERS_TABLE.length) {
                     return divideAndRound(l, LONG_TEN_POWERS_TABLE[n3], n, n2, n);
                  }

                  shadedBigDecimal3 = this;
                  return divideAndRound3(shadedBigDecimal3.inflated(), bigTenToThe(n3), n, n2, n);
               }

               l2 = l2 + 2248435727633453739L ^ -3762705831426019270L;
            }
         } else {
            if (this.signum() != 0) {
               l2 = (l2 ^ 4523338684267538959L) - 2806607750360613589L;
               continue;
            }

            l2 = l2 - 317826206180752427L ^ -3849116661000451478L;
         }

         while (7 < b) {
            if (b < 11) {
               if (b < 9) {
                  return new ShadedBigDecimal(customBigInteger, -9223372036854775808L, n, this.precision > 0 ? this.precision + n3 : 0);
               }

               if (b <= 9) {
                  return zeroValueOf(n);
               }

               l = this.intCompact;
               l2 = l2 - -7745368544596594522L ^ 7503895032738907182L;
               continue outerLoop;
            }

            if (11 >= b) {
               n5 = this.scale;
               l2 = (l2 ^ 4136272706379582496L) - -4481257717142845072L;
               continue outerLoop;
            }

            if (b >= 13) {
               if (13 >= b) {
                  throw new IllegalArgumentException("Invalid rounding mode");
               }

               if (b <= 14) {
                  n3 = this.checkScale((long)n - (long)n5);
                  l2 = (l2 ^ 1322166060400511044L) + -6437605309920116872L;
               } else {
                  if ((l = longMultiplyPowerTen(l, n3)) != -9223372036854775808L) {
                     l2 = l2 - -1658380346063922530L ^ 6274475352866576518L;
                     continue outerLoop;
                  }

                  l2 = (l2 ^ -6067650855935587475L) - -3732330651400081964L;
               }
            } else {
               customBigInteger = this.bigMultiplyPowerTen(n3);
               l2 = (l2 ^ 9037022655993238839L) - -3484889306648476504L;
            }
         }

         return this;
      }
   }

   public ShadedBigDecimal divide(ShadedBigDecimal shadedBigDecimal) {
      if (shadedBigDecimal.signum() == 0) {
         if (this.signum() == 0) {
            throw new ArithmeticException("Division undefined");
         } else {
            throw new ArithmeticException("Division by zero");
         }
      } else {
         int n = saturateLong((long)this.scale - (long)shadedBigDecimal.scale);
         if (this.signum() == 0) {
            return zeroValueOf(n);
         } else {
            ShadedMathContext shadedMathContext = new ShadedMathContext(
               (int)Math.min((long)this.precision() + (long)Math.ceil(10.0 * (double)shadedBigDecimal.precision() / 3.0), 2147483647L),
               CustomRoundingMode.UNNECESSARY
            );

            ShadedBigDecimal shadedBigDecimal2;
            try {
               shadedBigDecimal2 = this.divide(shadedBigDecimal, shadedMathContext);
            } catch (ArithmeticException e) {
               throw new ArithmeticException("Non-terminating decimal expansion; no exact representable decimal result.");
            }

            if (n > shadedBigDecimal2.getScale()) {
               return shadedBigDecimal2.setScale2(n, (byte)7);
            } else {
               return shadedBigDecimal2;
            }
         }
      }
   }

   public ShadedBigDecimal[] divideAndRemainder(ShadedBigDecimal shadedBigDecimal) {
      ShadedBigDecimal[] shadedBigDecimals = null;
      byte b = 3;

      while (true) {
         while (1 < b) {
            if (2 < b) {
               return shadedBigDecimals;
            }

            shadedBigDecimals = new ShadedBigDecimal[2];
            shadedBigDecimals[0] = this.divideToIntegralValue(shadedBigDecimal);
         }

         shadedBigDecimals[1] = this.subtract(shadedBigDecimals[0].multiply(shadedBigDecimal));
      }
   }

   private static void matchScale(ShadedBigDecimal[] shadedBigDecimals) {
      byte b = 2;

      outerLoop:
      while (true) {
         if (1 <= b) {
            if (1 >= b) {
               if (shadedBigDecimals[0].scale == shadedBigDecimals[1].scale) {
                  ;
               }
            } else if (shadedBigDecimals[1].scale < shadedBigDecimals[0].scale) {
               continue;
            }
         } else {
            shadedBigDecimals[1] = shadedBigDecimals[1].setScale2(shadedBigDecimals[0].scale, (byte)7);
         }

         while (3 < b) {
            if (b >= 5) {
               if (b > 5) {
                  if (b <= 6) {
                     return;
                  }

                  if (shadedBigDecimals[0].scale >= shadedBigDecimals[1].scale) {
                     continue outerLoop;
                  }
               }
            } else {
               shadedBigDecimals[0] = shadedBigDecimals[0].setScale2(shadedBigDecimals[1].scale, (byte)7);
            }
         }

         return;
      }
   }

   private static ShadedBigDecimal divide5(CustomBigInteger customBigInteger, int n, long l, int n2, int n3, int n4) {
      long l2 = 0L;
      int n5 = 0;
      int n6 = 0;
      CustomBigInteger customBigInteger2 = null;
      byte b = 9;
      long l3 = 0L;
      l3 = 5144697223679082673L ^ (long)"꼙椛\uf7bd┰\uf6fe﮲⡃퇮ꕥ蟘൭㡨윥㪞\uecd6ﰂ첵鐡\ue366憌".hashCode();

      outerLoop:
      while (true) {
         if (b <= 6) {
            if (checkScale(customBigInteger, (long)n3 + (long)n2) > n) {
               l3 = (l3 ^ -8163156877800098360L) + 2247107104006246501L;
               continue;
            }

            l3 = (l3 ^ 7581632959829381788L) + -4570861851082961078L;
         } else if (b >= 9) {
            if (9 >= b) {
               n6 = n3 + n2;
               l3 = (l3 ^ -5836562603480956782L) + 272717370662571006L;
               continue;
            }

            if (11 <= b) {
               if (11 >= b) {
                  return divideAndRound3(customBigInteger, customBigInteger2, n3, n4, n3);
               }

               customBigInteger2 = bigMultiplyPowerTen(l, n5);
               l3 = (l3 ^ 5357755037448067558L) - 784056105906311843L;
               continue;
            }

            if ((l2 = longMultiplyPowerTen(l2, n5)) == -9223372036854775808L) {
               l3 = l3 - -4038582693526566178L ^ 765308076479576311L;
               continue;
            }

            l3 = l3 + -853666203665162061L ^ 1967365079372015172L;
         } else {
            if (7 >= b) {
               return divideAndRound2(customBigInteger2, l, n3, n4, n3);
            }

            n5 = n6 - n;
            l3 = l3 + 355153694671806068L ^ 1912498482002072982L;
         }

         while (true) {
            while (b < 2) {
               if (0 >= b) {
                  l2 = l;
                  l3 = (l3 ^ -7262734881618549592L) + 6075444401588967374L;
                  continue outerLoop;
               }

               n5 = n6 - n2;
               l3 = (l3 ^ 5250422134079846700L) + 8208379210010836006L;
            }

            if (2 >= b) {
               return divideAndRound2(customBigInteger, l2, n3, n4, n3);
            }

            if (b >= 4) {
               if (b <= 4) {
                  customBigInteger2 = bigMultiplyPowerTen2(customBigInteger, n5);
                  l3 = (l3 ^ -6916847249498482156L) + -2091969658219440259L;
                  break;
               }

               if (n5 >= LONG_TEN_POWERS_TABLE.length) {
                  l3 = (l3 ^ -2025517706574098980L) - -6944004428169760226L;
                  break;
               }

               l3 = l3 - 137670688026516377L ^ 6731651289198434046L;
            } else {
               n6 = checkScale2(l, (long)n - (long)n3);
               l3 = l3 - -7482929168026942755L ^ 780256155159520387L;
            }
         }
      }
   }

   public ShadedBigDecimal(int n) {
      this.intCompact = (long)n;
      this.scale = 0;
      this.intVal = null;
   }

   private static boolean unsignedLongCompare(long l, long l2) {
      return l + -9223372036854775808L > l2 + -9223372036854775808L;
   }

   private static long longMultiplyPowerTen(long l, int n) {
      long[] longs = null;
      long[] longs2 = null;
      long l2 = 0L;
      byte b = 2;
      long l3 = 0L;
      l3 = -3376233517521730757L ^ (long)"瀧Ѕ\uf074峭宷ﰑ흵툒\ue4e7暦ߟ仢\uf876ᦋ\uefca쭡큗\u2e6d昀᧺ࡶ䋖⬍\ue79a｜魺\ue583Ц䴉".hashCode();

      outerLoop:
      while (true) {
         if (b <= 6) {
            l2 = longs[n];
            l3 = l3 - 8747265060917250015L ^ -5555838316152557971L;
         } else if (b >= 9) {
            if (b <= 9) {
               if (l != 0L) {
                  l3 = (l3 ^ -2158525099315657878L) + -5823579589711534397L;
                  continue;
               }

               l3 = l3 - -1962352077666818101L ^ 488493398917126594L;
            } else if (11 <= b) {
               if (11 >= b) {
                  longs = LONG_TEN_POWERS_TABLE;
                  l3 = l3 - -5654424827926239753L ^ 910967381127739079L;
               } else if (Math.abs(l) > longs2[n]) {
                  l3 = (l3 ^ 7093859636538300219L) - 8765651604197921198L;
               } else {
                  l3 = (l3 ^ 8878516702047855716L) + -8979373026298180623L;
               }
            } else if (n >= longs.length) {
               l3 = (l3 ^ 4821731817613644179L) - 1944829271326303627L;
            } else {
               l3 = l3 + -4147623292053497865L ^ 9186147721341255565L;
            }
         } else {
            if (b <= 7) {
               return l2;
            }

            if (n > 0) {
               l3 = l3 + -5700944749911128535L ^ -6113745067904226806L;
               continue;
            }

            l3 = l3 + 1885685845484881043L ^ 1598645520557747487L;
         }

         while (2 <= b) {
            if (b > 2) {
               if (b < 4) {
                  return -9223372036854775808L;
               }

               if (4 >= b) {
                  return l * l2;
               }

               longs2 = THRESHOLDS_TABLE;
               l3 = (l3 ^ -7650907104424206650L) - 8272987487485424380L;
               continue outerLoop;
            }

            if (n < longs2.length) {
               l3 = (l3 ^ 5300644626073025766L) - 7829004423036582576L;
               continue outerLoop;
            }

            l3 = l3 - -9050225446392591595L ^ 6885036475329276141L;
         }

         if (b > 0) {
            return l;
         }

         if (l != 1L) {
            l3 = (l3 ^ -6950840155053624043L) - 4992575773673091752L;
         } else {
            l3 = l3 - 7800737920352668295L ^ -1956679192224604254L;
         }
      }
   }

   private static CustomBigInteger bigMultiplyPowerTen2(CustomBigInteger customBigInteger, int n) {
      byte b = 0;

      while (2 < b) {
         if (3 >= b) {
            return customBigInteger;
         }

         if (n > 0) {
         }
      }

      if (n >= LONG_TEN_POWERS_TABLE.length) {
         return customBigInteger.multiply2(bigTenToThe(n));
      } else {
         return customBigInteger.multiply(LONG_TEN_POWERS_TABLE[n]);
      }
   }

   public ShadedBigDecimal negate() {
      long l = 0L;
      l = 1769424986536934361L ^ (long)"郺ὡ鸵\ue655팎䰷\u09b1ᠯ\uf6b3镃䥂\ue604ꦃ䆵鐠儹궆녎㛠箨".hashCode();
      if (this.intCompact != -9223372036854775808L) {
         l = (l ^ -2678494758500279608L) - 3766659443661743171L;
         return valueOf2(-this.intCompact, this.scale, this.precision);
      } else {
         l = (l ^ 6513450354356851848L) - 178674005589990248L;
         return new ShadedBigDecimal(this.intVal.negate(), -9223372036854775808L, this.scale, this.precision);
      }
   }

   public ShadedBigDecimal(CustomBigInteger customBigInteger, long l, int n, int n2) {
      this.scale = n;
      this.precision = n2;
      this.intCompact = l;
      this.intVal = customBigInteger;
   }

   private static CustomBigInteger divideAndRound2(CustomBigInteger customBigInteger, CustomBigInteger customBigInteger2, int n) {
      ShadedMutableBigInteger shadedMutableBigInteger = null;
      ShadedMutableBigInteger shadedMutableBigInteger2 = null;
      ShadedMutableBigInteger shadedMutableBigInteger3 = null;
      boolean flag = false;
      ShadedMutableBigInteger shadedMutableBigInteger4 = null;
      int n2 = 0;
      byte b = 3;

      while (true) {
         while (b > 3) {
            if (b >= 5) {
               if (b <= 5) {
                  flag = shadedMutableBigInteger3.isZero();
                  n2 = customBigInteger.signum == customBigInteger2.signum ? 1 : -1;
                  if (!flag) {
                     ShadedMutableBigInteger shadedMutableBigInteger5 = shadedMutableBigInteger2;
                     if (needIncrement3(shadedMutableBigInteger5, n, n2, shadedMutableBigInteger, shadedMutableBigInteger3)) {
                        shadedMutableBigInteger5 = shadedMutableBigInteger;
                        shadedMutableBigInteger5.add(ShadedMutableBigInteger.ONE);
                     }
                  }

                  return shadedMutableBigInteger.toBigInteger(n2);
               }

               shadedMutableBigInteger = new ShadedMutableBigInteger();
            } else {
               shadedMutableBigInteger4 = new ShadedMutableBigInteger(customBigInteger.mag);
            }
         }

         shadedMutableBigInteger2 = new ShadedMutableBigInteger(customBigInteger2.mag);
         shadedMutableBigInteger3 = shadedMutableBigInteger4.divide2(shadedMutableBigInteger2, shadedMutableBigInteger);
      }
   }

   private static boolean needIncrement2(long l, int n, int n2, long l2, long l3) {
      int n3 = 0;
      byte b = 4;
      long l4 = 0L;
      l4 = 435466652532974022L ^ (long)"\ue951㲚蘻䏮㊄띋ￂ槥镵颛Ｇ纯䓒層ᆫ漣\uf398\uf512ﶒ㡬ઓ㳧ꍀ".hashCode();

      while (true) {
         if (1 >= b) {
            if (assertionsDisabled) {
               l4 = (l4 ^ 2685807614537938550L) - 5251901429095561086L;
               break;
            }

            l4 = l4 - 8481521509732250985L ^ 6186131625338925636L;
         } else {
            if (2 >= b) {
               throw new AssertionError();
            }

            if (l3 != 0L) {
               l4 = l4 + -5158464775432411776L ^ 1859398625161623725L;
               break;
            }

            l4 = (l4 ^ -472367461950151986L) + 3452577809310719267L;
         }
      }

      while (true) {
         while (b > 4) {
            if (6 <= b) {
               if (b <= 6) {
                  return commonNeedIncrement(n, n2, n3, (l2 & 1L) != 0L);
               }

               if (7 >= b) {
                  if (l3 <= 4611686018427387903L) {
                     l4 = (l4 ^ 2878365864810547557L) - 4654854139983602459L;
                     continue;
                  }

                  l4 = (l4 ^ 2143334000952613147L) - 7552726934448891023L;
               } else {
                  if (l3 > -4611686018427387904L) {
                     l4 = (l4 ^ 1101182178812474815L) + -8415110512451755820L;
                     continue;
                  }

                  l4 = l4 - 182236572145176722L ^ 4641181747593454101L;
               }

               n3 = 1;
               l4 = l4 + 2468770116662080281L ^ 1163234997686497685L;
            } else {
               n3 = longCompareMagnitude(2L * l3, l);
               l4 = l4 - 4668716414495325659L ^ -7834969205585063667L;
            }
         }

         l4 = l4 + -8199346204184389217L ^ -7196591002274276815L;
      }
   }

   private static ShadedBigDecimal multiply(long l, long l2, int n) {
      long l3 = 0L;
      byte b = 1;
      long l4 = 0L;
      l4 = 1814351322053215766L ^ (long)"⡀쓵擱鳻ƕ谎哄ր㫄☄鰰\ue788冇༕䝀\ud978⤤鍍힑왯쿌듻ￄ雟컌\uf070".hashCode();
      l3 = multiply2(l, l2);
      l4 = l4 + 2392625664837442197L ^ 7666733370182709984L;

      while (b <= 1) {
         if (l3 == -9223372036854775808L) {
            l4 = l4 + -5110453164125284678L ^ 7881204388149177594L;
         } else {
            l4 = l4 - 5573675848307024390L ^ 8740736836642532475L;
         }
      }

      return 2 >= b ? new ShadedBigDecimal(CustomBigInteger.valueOf(l).multiply(l2), -9223372036854775808L, n, 0) : valueOf(l3, n);
   }

   private static ShadedBigDecimal divide3(long l, int n, long l2, int n2, long l3, ShadedMathContext shadedMathContext) {
      int n3 = 0;
      int n4 = 0;
      ShadedBigDecimal shadedBigDecimal = null;
      int n5 = 0;
      CustomBigInteger customBigInteger = null;
      long l4 = 0L;
      int n6 = 0;
      int n7 = 0;
      long l5 = 0L;
      CustomBigInteger customBigInteger2 = null;
      byte b = 7;
      long l6 = 0L;
      l6 = -471610965507700229L ^ (long)"윐찇쒖袩눴㣯\ue96d\udb1aﲝ齃룚Ქࠬ湳俷\ue2a9釂퀠쌢럶ྟ踧ꦵᅽ\ue579㶐쒕朐拉".hashCode();

      outerLoop:
      while (true) {
         if (b >= 6) {
            if (6 >= b) {
               n2--;
               l6 = (l6 ^ 101611158025678285L) - 8676844280993346801L;
            } else if (b >= 9) {
               if (9 >= b) {
                  customBigInteger2 = bigMultiplyPowerTen(l2, n7);
                  l6 = l6 - -4039416169433939515L ^ 5185969710231937708L;
                  continue;
               }

               if (11 <= b) {
                  if (11 >= b) {
                     if (compareMagnitudeNormalized3(l, n, l2, n2) > 0) {
                        long l7 = l6 ^ -1438688327218943340L;
                        short s = 23087;
                        l6 = l7 - -8611904542440099318L;
                        continue;
                     }

                     l6 = l6 - 2326944966944503516L ^ 1027715581596760322L;
                  } else {
                     n5 = checkScaleNonZero((long)n3 + (long)n2 - (long)n);
                     l6 = l6 - 9211712652103333032L ^ 8830814703314942347L;
                  }
               } else {
                  shadedBigDecimal = divideAndRound2(customBigInteger, l2, n4, n6, checkScaleNonZero(l3));
                  l6 = l6 + -4576186228746204607L ^ -2632129147286724854L;
               }
            } else {
               if (7 < b) {
                  shadedBigDecimal = divideAndRound(l4, l2, n4, n6, checkScaleNonZero(l3));
                  l6 = (l6 ^ -40789526220822397L) + 6493551568869166158L;
                  continue;
               }

               if (n > n2) {
                  l6 = l6 + -1355348912666655725L ^ -5025068428210141216L;
                  continue;
               }

               l6 = l6 + -5850823862761022431L ^ 3439373775896115655L;
            }
         } else {
            if (b < 2) {
               if (0 >= b) {
                  n3 = shadedMathContext.precision;
                  l6 = l6 + 5638609375714217172L ^ -7916240949164089126L;
               } else {
                  n4 = checkScaleNonZero(l3 + (long)n2 - (long)n + (long)n3);
                  l6 = l6 - -6954404405319873125L ^ 2140427730533037970L;
               }
               continue;
            }

            if (2 >= b) {
               shadedBigDecimal = divideAndRound(l, l2, n4, n6, checkScaleNonZero(l3));
               l6 = l6 + 6905759028366007807L ^ -2149044369254007812L;
            } else if (b >= 4) {
               if (4 >= b) {
                  if (checkScaleNonZero((long)n3 + (long)n2 - (long)n) > 0) {
                     l6 = (l6 ^ -3653884738171812281L) - 1461940012291617456L;
                     continue;
                  }

                  l6 = l6 - 2721189068575420447L ^ 4997948141732821037L;
               } else {
                  shadedBigDecimal = divideAndRound3(CustomBigInteger.valueOf(l), customBigInteger2, n4, n6, checkScaleNonZero(l3));
                  l6 = (l6 ^ -5232976076871799553L) - 7880951872828491611L;
               }
            } else {
               l6 = (l6 ^ -596934180530866946L) - -7802041276602407304L;
            }
         }

         while (13 < b) {
            if (20 <= b) {
               if (20 >= b) {
                  return divideSmallFastPath(l, n, l2, n2, l3, shadedMathContext);
               }

               if (b >= 24) {
                  if (b <= 24) {
                     n7 = checkScaleNonZero((long)n5 - (long)n2);
                     l6 = (l6 ^ -8329067787501752573L) + -6169946986744389047L;
                  } else {
                     if (26 > b) {
                        return doRound(shadedBigDecimal, shadedMathContext);
                     }

                     if (26 < b) {
                        l6 = (l6 ^ -1310992887459265125L) + -7659422061927150078L;
                        continue outerLoop;
                     }

                     if ((l4 = longMultiplyPowerTen(l, n5)) != -9223372036854775808L) {
                        l6 = (l6 ^ 6558529043069966299L) + 8239417697980020039L;
                        continue outerLoop;
                     }

                     l6 = (l6 ^ -8870575898552742110L) + 8748594375357490785L;
                  }
               } else if (b >= 22) {
                  if (22 >= b) {
                     l6 = (l6 ^ -4879883180370752598L) + -5934677824929887743L;
                  } else {
                     l6 = (l6 ^ 5292261907439223156L) - 3049429494085027055L;
                  }
               } else {
                  if ((l5 = longMultiplyPowerTen(l2, n7)) == -9223372036854775808L) {
                     l6 = l6 + -4368173411137216159L ^ 2483700830405746101L;
                     continue outerLoop;
                  }

                  l6 = (l6 ^ -6267333423978099207L) + -6386065158249517840L;
               }
            } else if (b >= 16) {
               if (b <= 16) {
                  if (n5 == n2) {
                     l6 = (l6 ^ 1808204647417935674L) + -3909132450086357630L;
                     continue outerLoop;
                  }

                  l6 = l6 + -3483676482754897359L ^ 2725797716916794721L;
               } else if (b >= 18) {
                  if (18 < b) {
                     customBigInteger = bigMultiplyPowerTen(l, n5);
                     l6 = l6 - -806675924121651710L ^ -4156641548929928030L;
                     continue outerLoop;
                  }

                  shadedBigDecimal = divideAndRound(l, l5, n4, n6, checkScaleNonZero(l3));
                  l6 = l6 + -6961985477783523125L ^ -265277717324534736L;
               } else {
                  n5 = checkScaleNonZero((long)n - (long)n3);
                  l6 = (l6 ^ -3248459716317488985L) + -6117821456763618704L;
               }
            } else if (14 >= b) {
               if (n3 >= 18) {
                  l6 = (l6 ^ 7638820521677317911L) + -2700318093758312620L;
                  continue outerLoop;
               }

               l6 = l6 - -2696294322387580258L ^ -1317957926744911116L;
            } else {
               if (n2 >= 18) {
                  l6 = l6 - 1725447681696369801L ^ 5496409153784093080L;
                  continue outerLoop;
               }

               l6 = l6 + 6432180666772342144L ^ -1548061209604637614L;
            }
         }

         n6 = shadedMathContext.roundingMode.oldMode;
         l6 = (l6 ^ 5127107505728671852L) + 6746438414851742141L;
      }
   }

   public ShadedBigDecimal(CustomBigInteger customBigInteger) {
       super();
      this.scale = 0;
      this.intVal = customBigInteger;
      this.intCompact = compactValFor(customBigInteger);
   }

   public ShadedBigDecimal pow(int n) {
      int n2 = 0;
      byte b = 2;

      while (true) {
         if (2 >= b) {
            if (n < 0) {
               break;
            }
         } else {
            if (b > 3) {
               n2 = this.checkScale((long)this.scale * (long)n);
               CustomBigInteger customBigInteger = this.inflated();
               return new ShadedBigDecimal(customBigInteger.pow(n), n2);
            }

            if (n > 999999999) {
               break;
            }
         }
      }

      throw new ArithmeticException("Invalid operation");
   }

   private static ShadedBigDecimal doRound2(long l, int n, ShadedMathContext shadedMathContext) {
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      byte b = 12;

      outerLoop:
      while (true) {
         if (b >= 2) {
            if (b <= 2) {
               n2 = longDigitLength(l);
               continue;
            }

            if (b >= 4) {
               if (b > 4) {
                  n3 = n2 - n4;
                  continue;
               }

               if (n3 > 0) {
                  ;
               }
            } else {
               n2 = longDigitLength(l);
            }
         } else {
            if (b > 0) {
               continue;
            }

            n4 = shadedMathContext.precision;
         }

         while (true) {
            while (b > 6) {
               if (9 > b) {
                  if (b <= 7) {
                     l = divideAndRound3(l, LONG_TEN_POWERS_TABLE[n3], shadedMathContext.roundingMode.oldMode);
                  } else {
                     n3 = n2 - n4;
                  }
                  continue outerLoop;
               }

               if (9 >= b) {
                  if (n4 > 0) {
                     ;
                  }
               } else {
                  if (11 > b) {
                     return valueOf2(l, n, n2);
                  }

                  if (11 >= b) {
                     return valueOf(l, n);
                  }

                  if (n4 < 19) {
                     continue outerLoop;
                  }
               }
            }

            n = checkScaleNonZero((long)n - (long)n3);
         }
      }
   }

   private static ShadedBigDecimal divide2(long l, int n, long l2, int n2, int n3, int n4) {
      long l3 = 0L;
      CustomBigInteger customBigInteger = null;
      int n5 = 0;
      ShadedBigDecimal shadedBigDecimal = null;
      int n6 = 0;
      byte b = 13;
      long l4 = 0L;
      l4 = 8263046835475010110L ^ (long)"⅄郁䒮武䧦䵐\uf30a䰘㡙\ue2b2㸍쭍ꏧ섊욑練㼷麸\u20fe뉷趥".hashCode();

      outerLoop:
      while (true) {
         if (4 <= b) {
            if (b <= 4) {
               if ((l3 = longMultiplyPowerTen(l3, n6)) == -9223372036854775808L) {
                  l4 = l4 + 6897575498540200021L ^ 7271016877360885050L;
               } else {
                  l4 = l4 - 244112279052340259L ^ 5490224232171396580L;
               }
            } else if (b >= 6) {
               if (b > 6) {
                  if (b <= 7) {
                     return shadedBigDecimal;
                  }

                  return divideAndRound(l3, l2, n3, n4, n3);
               }

               n5 = checkScale2(l2, (long)n - (long)n3);
               l4 = l4 + -4198383923895887550L ^ -1265143073760122875L;
            } else {
               if ((l3 = longMultiplyPowerTen(l3, n6)) != -9223372036854775808L) {
                  l4 = (l4 ^ -5067453292879836473L) + 3970175197120511907L;
                  continue;
               }

               l4 = l4 + -1409426844962033529L ^ 7076465427634432638L;
            }
         } else if (1 <= b) {
            if (1 >= b) {
               if (shadedBigDecimal != null) {
                  l4 = l4 - 7819101205604922366L ^ 4078892748738979080L;
                  continue;
               }

               l4 = l4 + 8605022962514711348L ^ 5155046050821360212L;
            } else {
               if (b > 2) {
                  n6 = n5 - n;
                  l4 = (l4 ^ -6156356524489136657L) - 4295551303570224995L;
                  continue;
               }

               if (checkScale2(l, (long)n3 + (long)n2) <= n) {
                  l4 = (l4 ^ 2526196883764480285L) + -8647891126199924751L;
                  continue;
               }

               l4 = (l4 ^ -1117246638916438219L) + -95310387046981185L;
            }
         } else if (n6 >= LONG_TEN_POWERS_TABLE.length) {
            l4 = (l4 ^ 8757931029230245808L) - 4401197353516313064L;
         } else {
            l4 = (l4 ^ 8265723273884558098L) + -5953642489632466444L;
         }

         while (9 < b) {
            if (b >= 14) {
               if (b <= 14) {
                  return divideAndRound2(customBigInteger, l2, n3, n4, n3);
               }

               if (b < 17) {
                  if (15 >= b) {
                     return divideAndRound3(CustomBigInteger.valueOf(l), customBigInteger, n3, n4, n3);
                  }

                  shadedBigDecimal = multiplyDivideAndRound(LONG_TEN_POWERS_TABLE[n6], l, l2, n3, n4, n3);
                  l4 = l4 + 8977825080583661077L ^ 5447189163771621140L;
                  continue outerLoop;
               }

               if (b <= 17) {
                  customBigInteger = bigMultiplyPowerTen(l, n6);
                  l4 = (l4 ^ -9004074793102815167L) + 5373912353589320247L;
               } else {
                  if (18 < b) {
                     n5 = n3 + n2;
                     l4 = (l4 ^ -3420037229763036752L) - -4254527669134705608L;
                     continue outerLoop;
                  }

                  n6 = n5 - n2;
                  l4 = (l4 ^ 1525095266076527735L) + 3495598625987556552L;
               }
            } else {
               if (11 > b) {
                  return divideAndRound(l, l3, n3, n4, n3);
               }

               if (11 >= b) {
                  if (n6 >= LONG_TEN_POWERS_TABLE.length) {
                     l4 = l4 - -6991554351920290926L ^ -2647040928383322069L;
                  } else {
                     l4 = (l4 ^ 7433239227909562087L) + -856271978650080234L;
                  }
               } else {
                  if (b > 12) {
                     l3 = l2;
                     l4 = l4 + -3843918682226092648L ^ 6342271039373359369L;
                     continue outerLoop;
                  }

                  customBigInteger = bigMultiplyPowerTen(l2, n6);
                  l4 = l4 + 5801147113325580840L ^ 4390376632988323164L;
               }
            }
         }

         l3 = l;
         l4 = (l4 ^ 8134605061842582553L) + 1418631323181188777L;
      }
   }

   private static ShadedBigDecimal multiplyAndRound3(CustomBigInteger customBigInteger, CustomBigInteger customBigInteger2, int n, ShadedMathContext shadedMathContext) {
      return doRound(customBigInteger.multiply2(customBigInteger2), n, shadedMathContext);
   }

   public ShadedBigDecimal(double d) {
       this(d, ShadedMathContext.UNLIMITED);
   }

   public ShadedBigDecimal movePointLeft(int n) {
      Object obj = null;
      int n2 = 0;
      ShadedBigDecimal shadedBigDecimal = this;
      n2 = shadedBigDecimal.checkScale((long)this.scale + (long)n);
      obj = new ShadedBigDecimal(this.intVal, this.intCompact, n2, 0);
      if (((ShadedBigDecimal)obj).scale >= 0) {
         shadedBigDecimal = (ShadedBigDecimal)obj;
      } else {
         shadedBigDecimal = (ShadedBigDecimal)obj;
         shadedBigDecimal = shadedBigDecimal.setScale2((byte)0, (byte)7);
      }

      return shadedBigDecimal;
   }

   public short shortValueExact() {
      long l = 0L;
      long l2 = 0L;
      l2 = -4345342428669542984L ^ (long)"콶䫾蛄宿뵥鐡캄끅㑬䦳얾ㄋ╀疕⭜䔿﹪濠㳸굂亥륰쇈唣".hashCode();
      l = this.longValueExact();
      l2 = l2 + -8282782000394725218L ^ 6616159387063622818L;
      if ((long)((short)((int)l)) == l) {
         l2 = l2 + -447704293657133462L ^ 5204121395197385756L;
         return (short)((int)l);
      } else {
         l2 = (l2 ^ 1785663341508507486L) - -7769944504225212890L;
         throw new ArithmeticException("Overflow");
      }
   }

   public static ShadedBigDecimal valueOf(long l) {
      byte b = 0;
      long l2 = 0L;
      l2 = 3122133252339760275L ^ (long)"\ue682瘈ꖇ㾄偪诲뽲\ue550\u085c\uf4efꘜ聠⏫∧솮쪲䟵鐇艨\ud843".hashCode();

      while (true) {
         while (2 < b) {
            if (4 <= b) {
               if (b > 4) {
                  return new ShadedBigDecimal(INFLATED_BIGINT, l, 0, 0);
               }

               if (l != -9223372036854775808L) {
                  l2 = (l2 ^ -7296831867998922615L) + 775616025986320335L;
                  return new ShadedBigDecimal(null, l, 0, 0);
               }

               l2 = l2 + 2788661055670258180L ^ -649915365824026082L;
            } else {
               if (l < (long)zeroThroughTen.length) {
                  l2 = (l2 ^ 8519076532784926722L) + -755757426453398450L;
                  return zeroThroughTen[(int)l];
               }

               l2 = l2 - 6490740476185306963L ^ -2483991904312688657L;
            }
         }

         if (l < 0L) {
            l2 = (l2 ^ 6401338349768658651L) - 4969926466293867777L;
         } else {
            l2 = (l2 ^ -5694322472458810749L) + -8087446699067886367L;
         }
      }
   }

   public ShadedBigDecimal multiply(ShadedBigDecimal shadedBigDecimal, ShadedMathContext shadedMathContext) {
      int n = 0;
      byte b = 4;
      long l = 0L;
      l = -3244010881115267434L ^ (long)"霽用秉扉鍃덫릤ꄨ襎\uedeb㥌\ud8a4ヌﺓ鼕㞑৸\udab7ӄ\uf5d1ᐕ".hashCode();

      outerLoop:
      while (1 <= b) {
         if (b <= 1) {
            return this.multiply(shadedBigDecimal);
         }

         if (2 >= b) {
            return multiplyAndRound(this.intCompact, shadedBigDecimal.intCompact, n, shadedMathContext);
         }

         if (shadedMathContext.precision == 0) {
            l = l - 420305097793961404L ^ -224005987386876361L;
         } else {
            l = l + -9111440682525590360L ^ 1464624433485176814L;

            while (true) {
               while (4 < b) {
                  if (7 > b) {
                     if (5 < b) {
                        CustomBigInteger intVal2 = shadedBigDecimal.intVal;
                        return multiplyAndRound3(this.intVal, intVal2, n, shadedMathContext);
                     }

                     if (shadedBigDecimal.intCompact == -9223372036854775808L) {
                        l = (l ^ 3526016692108206300L) + 5965592001080677596L;
                     } else {
                        l = (l ^ 665057898553967448L) + 4757774862991372722L;
                     }
                     continue outerLoop;
                  }

                  if (b <= 7) {
                     n = this.checkScale((long)this.scale + (long)shadedBigDecimal.scale);
                     l = l - 3299651899643313276L ^ 3791760262950748172L;
                  } else {
                     if (8 < b) {
                        return multiplyAndRound2(shadedBigDecimal.intCompact, this.intVal, n, shadedMathContext);
                     }

                     if (shadedBigDecimal.intCompact == -9223372036854775808L) {
                        l = l - 5641870008689488925L ^ -7175026828060046689L;
                     } else {
                        l = (l ^ -4579826594582467234L) - -7671925404896801618L;
                     }
                  }
               }

               if (this.intCompact == -9223372036854775808L) {
                  l = l - -4450993025643006586L ^ 5951790104372815703L;
               } else {
                  l = l - 56053770845004898L ^ -7455429875351693842L;
               }
            }
         }
      }

      return multiplyAndRound2(this.intCompact, shadedBigDecimal.intVal, n, shadedMathContext);
   }

   public ShadedBigDecimal(byte b, int n, int n2, long l) {
      this.intCompact = l;
      this.intVal = l == 0L ? INFLATED_BIGINT : null;
      this.scale = 0;
   }

   @Override
   public String toString() {
      Object obj = null;
      obj = this.stringCache;
      if ((String)obj == null) {
         this.stringCache = (String)(obj = this.layoutChars(true));
      }

      return (String)obj;
   }

   private static long add2(long l, long l2) {
      long l3 = 0L;
      long l4 = 0L;
      l4 = -5971857921702148990L ^ (long)"\uec74\u177f\uf1ae乫ꮻ䷭\uec2d뗜⎩\uf577ᚹ쩉\u0dc8ൽ蠕甍ｑ겍熿\ud83c烞".hashCode();
      l3 = l + l2;
      l4 = (l4 ^ 7412305257033527580L) - 333294489214478894L;
      if (((l3 ^ l) & (l3 ^ l2)) < 0L) {
         l4 = l4 - -6690572301250661279L ^ -2415714127185824434L;
         return -9223372036854775808L;
      } else {
         l4 = (l4 ^ -3936977907391405957L) - -423375213105930493L;
         return l3;
      }
   }

   private ShadedBigDecimal audit() {
      long l = 0L;
      byte b = 12;
      long l2 = 0L;
      l2 = 1739495174312937262L ^ (long)"䎓뇏า\uea4b稗尓\uf1b5\ueadf惏\udacf를嚆螔嵉冷깰⽝띔\uf44d藡问砉缠\udd5b⣟༷\udad3왤攧".hashCode();

      outerLoop:
      while (true) {
         if (3 <= b) {
            if (b <= 3) {
               print("audit", this);
               l2 = l2 - 2576101945987823349L ^ 2579857851030508611L;
            } else if (b >= 5) {
               if (b > 5) {
                  if (6 >= b) {
                     return this;
                  }

                  if (this.precision <= 0) {
                     l2 = l2 - 5597331222422896725L ^ -3010806857729759150L;
                  } else {
                     l2 = l2 + 1233725405540626952L ^ -2268696487251003055L;
                  }
                  continue;
               }

               print("audit", this);
               l2 = l2 + -2861892797379176474L ^ 2712027150393052027L;
            } else {
               print("audit", this);
               l2 = l2 + -6832717714482445053L ^ -4463564468569040654L;
            }
         } else {
            if (b < 1) {
               if (this.precision == longDigitLength(this.intCompact)) {
                  l2 = (l2 ^ -280789299227708106L) + -2113637247587325576L;
               } else {
                  l2 = l2 - -6120691788018215098L ^ 6354633195042994934L;
               }
               continue;
            }

            if (1 >= b) {
               if (this.intCompact != -9223372036854775808L) {
                  l2 = (l2 ^ 8961650999366390105L) - -5498540591545951761L;
                  continue;
               }

               l2 = (l2 ^ 501304548567245698L) - -3078019214580367453L;
            } else {
               if (this.intVal == null) {
                  l2 = (l2 ^ 8976761583049101609L) - -2215568296659402879L;
                  continue;
               }

               l2 = (l2 ^ 583455634284780382L) + -7187171724925639950L;
            }
         }

         while (b > 8) {
            if (b >= 13) {
               if (13 >= b) {
                  if (this.precision == bigDigitLength(this.intVal)) {
                     l2 = (l2 ^ 1196674306222700105L) + 4688370528742360609L;
                  } else {
                     l2 = l2 - 887471883686280589L ^ -5373280592651589350L;
                  }
                  continue outerLoop;
               }

               if (15 > b) {
                  throw new AssertionError("null intVal");
               }

               if (b <= 15) {
                  if (this.precision <= 0) {
                     l2 = (l2 ^ 6471954472248003526L) - 135872432371705896L;
                     continue outerLoop;
                  }

                  l2 = l2 + 3741652097482780927L ^ 8091944236803874643L;
               } else {
                  if (16 >= b) {
                     throw new AssertionError("Inconsistent state, intCompact=" + this.intCompact + "\t intVal=" + l);
                  }

                  print("audit", this);
                  l2 = l2 - 2240355143490707280L ^ -3197376265625338493L;
               }
            } else {
               if (b < 10) {
                  throw new AssertionError("precision mismatch");
               }

               if (b <= 10) {
                  l = this.intVal.longValue();
                  l2 = l2 - 1202857677762336796L ^ -5041029286242817581L;
               } else if (11 >= b) {
                  if (this.intVal == null) {
                     l2 = (l2 ^ -7109500340349472165L) - -1516368786440048380L;
                     continue outerLoop;
                  }

                  l2 = l2 - 3973310619308203230L ^ 3542997001010390552L;
               } else {
                  if (l == this.intCompact) {
                     l2 = (l2 ^ -5590623639276196585L) - -5100877627162312706L;
                     continue outerLoop;
                  }

                  l2 = (l2 ^ 2189382315515853248L) + -4615339531643956560L;
               }
            }
         }

         throw new AssertionError("precision mismatch");
      }
   }

   private static int checkScale(CustomBigInteger customBigInteger, long l) {
      int n = 0;
      byte b = 1;
      long l2 = 0L;
      l2 = 8423862166123269846L ^ (long)"䕜妾㬞竇ﶸ떁ᩋ灰\uf32b㸩ほ埪ᢒ㉺「帝\ue480䏾\ufff6秿\ufddd\u0bdf".hashCode();

      while (true) {
         int n2;
         if (1 >= b) {
            if ((long)n == l) {
               l2 = (l2 ^ -4167743996825901499L) + -5152318268121503953L;
               break;
            }

            l2 = l2 - 8014509866357142872L ^ 7933482616873315815L;
            if (l > 2147483647L) {
               l2 = (l2 ^ 2344778325843429250L) - 6691630668957347673L;
               continue;
            }

            l2 = (l2 ^ 5412668377319421460L) + 906475987072483125L;
            n2 = -2147483648;
         } else {
            if (b <= 2) {
               n = (int)l;
               l2 = (l2 ^ -8193526355249668959L) + 2957695177281372920L;
               continue;
            }

            n2 = 2147483647;
         }

         n = n2;
         if (customBigInteger.getSignum() != 0) {
            throw new ArithmeticException(n > 0 ? "Underflow" : "Overflow");
         }
         break;
      }

      return n;
   }

   public ShadedBigDecimal(char[] chars) {
       this(chars, 0, chars.length);
   }

   @Override
   public int compareTo(Object other) {
      return this.compareTo2((ShadedBigDecimal)other);
   }

   public long longValueExact() {
      ShadedBigDecimal shadedBigDecimal = null;
      byte b = 4;
      long l = 0L;
      l = -556663372372270175L ^ (long)"䴚뱺ᝦ榭帬꾄檰⭊\uf528外ꪤ垢캶큋첏ธ퍆\ue9c8쭀둼ᅋɐ볥".hashCode();

      outerLoop:
      while (6 < b) {
         if (9 <= b) {
            if (9 >= b) {
               return 0L;
            }

            if (11 > b) {
               if (this.precision() - this.scale <= 19) {
                  l = (l ^ -5390852068430783541L) + -3266486041966329797L;
               } else {
                  l = (l ^ -2397214606489478493L) - -8246359369815909738L;
               }
               continue;
            }

            if (11 >= b) {
               throw new ArithmeticException("Overflow");
            }

            shadedBigDecimal = this.setScale2((byte)0, (byte)7);
            l = (l ^ -8346122342788295673L) + 7024251986122973741L;
         } else {
            if (b <= 7) {
               ShadedBigDecimalLongOverflow.check(shadedBigDecimal);
               l = (l ^ 140348607721318357L) - -3744846982098380367L;
               continue;
            }

            if (this.intCompact == -9223372036854775808L) {
               l = (l ^ 6661714522590210918L) + -4853457107261516279L;
            } else {
               l = l + 8157597346709095335L ^ 1448339968273766142L;
            }
         }

         while (true) {
            while (2 > b) {
               if (b > 0) {
                  return this.intCompact;
               }

               if (this.signum() == 0) {
                  l = (l ^ -7476926246594696237L) + 3899913265810020144L;
                  continue outerLoop;
               }

               l = l - 2386944088384596634L ^ -2125425480643807755L;
            }

            if (b <= 2) {
               if (!this.fractionOnly()) {
                  l = l - 2047011188143236526L ^ -7072117244397360788L;
                  break;
               }

               l = (l ^ -8005528894907659458L) - 8783394353888743126L;
            } else {
               if (4 > b) {
                  if (shadedBigDecimal.precision() < 19) {
                     l = (l ^ 3140765929283953288L) + -913479154906207761L;
                  } else {
                     l = l + -7473138361988687661L ^ 4706403304122110050L;
                  }
                  break;
               }

               if (4 >= b) {
                  throw new ArithmeticException("Rounding necessary");
               }

               if (this.scale != 0) {
                  l = l + -1181432834875899795L ^ 4971427949813955625L;
               } else {
                  l = l - 5054716226656907484L ^ -146147843534539236L;
               }
            }
         }
      }

      return shadedBigDecimal.inflated().longValue();
   }

   public static ShadedBigDecimal valueOf2(long l, int n, int n2) {
      byte b = 2;
      long l2 = 0L;
      l2 = -6289358028056125215L ^ (long)"侎\ued6c쵅礿䭢鲓蹱\uf8de铡㵯炄礓\u0b31깎蓝\ue1ff摺㚃凢\ue234َ煏ת蹶찇뒽ꡚ\udb37".hashCode();

      while (true) {
         while (b > 3) {
            if (5 > b) {
               return zeroThroughTen[(int)l];
            }

            if (b > 5) {
               if (l != 0L) {
                  l2 = l2 + -832727502467454119L ^ 2141998787070687477L;
                  return new ShadedBigDecimal(l == -9223372036854775808L ? INFLATED_BIGINT : null, l, n, n2);
               }

               l2 = (l2 ^ -5092490787729996980L) + 1209223849615986888L;
               return zeroValueOf(n);
            }

            if (n != 0) {
               l2 = l2 - -3581352971669336980L ^ -8462687150931970214L;
            } else {
               l2 = l2 - -8158689121436191167L ^ -7091345251840026741L;
            }
         }

         l2 = (l2 ^ -912417073176080227L) + 808468090128435059L;
         if (l >= (long)zeroThroughTen.length) {
            l2 = l2 + 8839464701481797090L ^ -5913003614393591352L;
         } else {
            l2 = l2 - 9044940298251642693L ^ 1506022333703660669L;
         }
      }
   }

   static {
      INFLATED_BIGINT = CustomBigInteger.valueOf(-9223372036854775808L);
      zeroThroughTen = new ShadedBigDecimal[]{
         new ShadedBigDecimal(CustomBigInteger.ZERO, 0L, 0, 1),
         new ShadedBigDecimal(CustomBigInteger.ONE, 1L, 0, 1),
         new ShadedBigDecimal(CustomBigInteger.valueOf(2L), 2L, 0, 1),
         new ShadedBigDecimal(CustomBigInteger.valueOf(3L), 3L, 0, 1),
         new ShadedBigDecimal(CustomBigInteger.valueOf(4L), 4L, 0, 1),
         new ShadedBigDecimal(CustomBigInteger.valueOf(5L), 5L, 0, 1),
         new ShadedBigDecimal(CustomBigInteger.valueOf(6L), 6L, 0, 1),
         new ShadedBigDecimal(CustomBigInteger.valueOf(7L), 7L, 0, 1),
         new ShadedBigDecimal(CustomBigInteger.valueOf(8L), 8L, 0, 1),
         new ShadedBigDecimal(CustomBigInteger.valueOf(9L), 9L, 0, 1),
         new ShadedBigDecimal(CustomBigInteger.TEN, 10L, 0, 2)
      };
      ZERO_SCALED_BY = new ShadedBigDecimal[]{
         zeroThroughTen[0],
         new ShadedBigDecimal(CustomBigInteger.ZERO, 0L, 1, 1),
         new ShadedBigDecimal(CustomBigInteger.ZERO, 0L, 2, 1),
         new ShadedBigDecimal(CustomBigInteger.ZERO, 0L, 3, 1),
         new ShadedBigDecimal(CustomBigInteger.ZERO, 0L, 4, 1),
         new ShadedBigDecimal(CustomBigInteger.ZERO, 0L, 5, 1),
         new ShadedBigDecimal(CustomBigInteger.ZERO, 0L, 6, 1),
         new ShadedBigDecimal(CustomBigInteger.ZERO, 0L, 7, 1),
         new ShadedBigDecimal(CustomBigInteger.ZERO, 0L, 8, 1),
         new ShadedBigDecimal(CustomBigInteger.ZERO, 0L, 9, 1),
         new ShadedBigDecimal(CustomBigInteger.ZERO, 0L, 10, 1),
         new ShadedBigDecimal(CustomBigInteger.ZERO, 0L, 11, 1),
         new ShadedBigDecimal(CustomBigInteger.ZERO, 0L, 12, 1),
         new ShadedBigDecimal(CustomBigInteger.ZERO, 0L, 13, 1),
         new ShadedBigDecimal(CustomBigInteger.ZERO, 0L, 14, 1),
         new ShadedBigDecimal(CustomBigInteger.ZERO, 0L, 15, 1)
      };
      ZERO = zeroThroughTen[0];
      ONE = zeroThroughTen[1];
      TEN = zeroThroughTen[10];
      BIG_TEN_POWERS_TABLE = new CustomBigInteger[]{
         CustomBigInteger.ONE,
         CustomBigInteger.valueOf(10L),
         CustomBigInteger.valueOf(100L),
         CustomBigInteger.valueOf(1000L),
         CustomBigInteger.valueOf(10000L),
         CustomBigInteger.valueOf(100000L),
         CustomBigInteger.valueOf(1000000L),
         CustomBigInteger.valueOf(10000000L),
         CustomBigInteger.valueOf(100000000L),
         CustomBigInteger.valueOf(1000000000L),
         CustomBigInteger.valueOf(10000000000L),
         CustomBigInteger.valueOf(100000000000L),
         CustomBigInteger.valueOf(1000000000000L),
         CustomBigInteger.valueOf(10000000000000L),
         CustomBigInteger.valueOf(100000000000000L),
         CustomBigInteger.valueOf(1000000000000000L),
         CustomBigInteger.valueOf(10000000000000000L),
         CustomBigInteger.valueOf(100000000000000000L),
         CustomBigInteger.valueOf(1000000000000000000L)
      };
      BIG_TEN_POWERS_TABLE_INITLEN = BIG_TEN_POWERS_TABLE.length;
      BIG_TEN_POWERS_TABLE_MAX = 16 * BIG_TEN_POWERS_TABLE_INITLEN;
   }

   public static ShadedBigDecimal scaledTenPow(int n, int n2, int n3) {
      CustomBigInteger customBigInteger = null;
      byte b = 1;

      while (2 < b) {
         if (b >= 4) {
            if (b <= 4) {
               customBigInteger = bigTenToThe(n);
            } else if (n >= LONG_TEN_POWERS_TABLE.length) {
               continue;
            }

            if (b <= 0) {
               return valueOf((long)n2 * LONG_TEN_POWERS_TABLE[n], n3);
            }

            if (n2 == -1) {
               ;
            }
         } else {
            customBigInteger = customBigInteger.negate();
         }
      }

      return new ShadedBigDecimal(customBigInteger, -9223372036854775808L, n3, n + 1);
   }

   public ShadedBigDecimal add(ShadedBigDecimal shadedBigDecimal, ShadedMathContext shadedMathContext) {
      int n = 0;
      Object obj = null;
      long l = 0L;
      Object obj2 = null;
      int n2 = 0;
      boolean flag = false;
      boolean flag2 = false;
      ShadedBigDecimal shadedBigDecimal2 = null;
      int n3 = 0;
      byte b = 4;

      boolean flag3;
      while (true) {
         if (b <= 2) {
            return this.add(shadedBigDecimal);
         }

         if (3 < b) {
            flag3 = true;
            break;
         }

         if (shadedMathContext.precision != 0) {
            shadedBigDecimal2 = this;
            if (shadedBigDecimal2.signum() != 0) {
               flag3 = false;
               break;
            }
         }
      }

      flag = flag3;
      flag2 = shadedBigDecimal.signum() == 0;
      if (!flag && !flag2) {
         l = (long)shadedBigDecimal2.scale - (long)shadedBigDecimal.scale;
         if (l != 0L) {
            obj2 = this.preAlign(shadedBigDecimal2, shadedBigDecimal, l, shadedMathContext);
            matchScale((ShadedBigDecimal[])obj2);
            shadedBigDecimal2 = ((ShadedBigDecimal[])obj2)[0];
            shadedBigDecimal = ((ShadedBigDecimal[])obj2)[1];
         }

         return doRound(shadedBigDecimal2.inflated().add2(shadedBigDecimal.inflated()), shadedBigDecimal2.scale, shadedMathContext);
      } else {
         n2 = Math.max(shadedBigDecimal2.getScale(), shadedBigDecimal.getScale());
         if (flag && flag2) {
            return zeroValueOf(n2);
         } else {
            ShadedBigDecimal shadedBigDecimal13;
            if (flag) {
               shadedBigDecimal13 = doRound(shadedBigDecimal, shadedMathContext);
            } else {
               shadedBigDecimal13 = doRound(shadedBigDecimal2, shadedMathContext);
            }

            obj = shadedBigDecimal13;
            if (((ShadedBigDecimal)obj).getScale() == n2) {
               return (ShadedBigDecimal)obj;
            } else if (((ShadedBigDecimal)obj).getScale() > n2) {
               int scale3 = ((ShadedBigDecimal)obj).scale;
               long intCompact2 = ((ShadedBigDecimal)obj).intCompact;
               return stripZerosToMatchScale(((ShadedBigDecimal)obj).intVal, intCompact2, scale3, n2);
            } else {
               n3 = shadedMathContext.precision - ((ShadedBigDecimal)obj).precision();
               n = n2 - ((ShadedBigDecimal)obj).getScale();
               if (n3 >= n) {
                  return ((ShadedBigDecimal)obj).setScale(n2);
               } else {
                  int n4 = ((ShadedBigDecimal)obj).getScale() + n3;
                  return ((ShadedBigDecimal)obj).setScale(n4);
               }
            }
         }
      }
   }

   public ShadedBigDecimal ulp() {
      return valueOf2(1L, this.getScale(), 1);
   }

   @Override
   public long longValue() {
      long l = 0L;
      l = 4724744281663981809L ^ (long)"풍根▗幰\uab18\ud815\uef2d琌p≃Δ跪衐ᑠ䧎硈愆⸇ﮈዾ麘⓰煕݊༈햾匓".hashCode();
      if (this.intCompact == -9223372036854775808L) {
         l = (l ^ -3878557857934464320L) + -8929815302759746915L;
      } else {
         l = (l ^ -2018125302759545825L) - 3380983435032001409L;
         if (this.scale == 0) {
            l = l - -3919400596408139947L ^ 1490551501256551887L;
            return this.intCompact;
         }

         l = l + 1869951184429640594L ^ -9032146489093747795L;
      }

      if (this.signum() == 0) {
         l = (l ^ 3261034311633652827L) + -311747502110392900L;
      } else {
         l = l - 4024960923958676406L ^ 7968595024157247048L;
         if (this.fractionOnly()) {
            l = l - -425150797516629452L ^ -6811930635450406439L;
         } else {
            l = (l ^ -1636015754355763303L) - -5887007974049913944L;
            if (this.scale > -64) {
               l = (l ^ 1309276307269642952L) - 6639343133441944445L;
               return this.toBigInteger().longValue();
            }

            l = (l ^ 2035500519436649139L) - -6942787227137209013L;
         }
      }

      return 0L;
   }

   public ShadedBigDecimal setScale(int n) {
      return this.setScale2(n, (byte)7);
   }

   public CustomBigInteger unscaledValue() {
      return this.inflated();
   }

   public ShadedBigDecimal(CustomBigInteger customBigInteger, ShadedMathContext shadedMathContext) {
       this(customBigInteger, 0, shadedMathContext);
   }

   public int compareTo2(ShadedBigDecimal shadedBigDecimal) {
      int n = 0;
      long l = 0L;
      long l2 = 0L;
      int n2 = 0;
      int n3 = 0;
      byte b = 0;
      long l3 = 0L;
      l3 = -1899810869949929652L ^ (long)"娀눛\udcd4頀艥\u0992\ue435㟮玪ꀨ징伥ේ褾즵컾ﭵ풾섂꺺寋".hashCode();

      byte b2;
      while (true) {
         if (b <= 3) {
            if (l != -9223372036854775808L) {
               l3 = l3 + 1373630082196418871L ^ 5583714616604103703L;
               if (l2 == l) {
                  l3 = (l3 ^ -3423706086994140509L) + 6210498425921556109L;
                  b2 = 0;
               } else {
                  l3 = l3 - -1232761968818422694L ^ -3728262803355218112L;
                  if (l2 > l) {
                     l3 = l3 - -5949620255055272131L ^ 3034892950397129053L;
                     continue;
                  }

                  l3 = l3 - -2597970163723676253L ^ 1484937797155800827L;
                  b2 = -1;
               }
               break;
            }

            l3 = (l3 ^ -2422060419448083338L) + 279503457060022790L;
         } else if (b >= 5) {
            if (b <= 5) {
               l2 = this.intCompact;
               l3 = l3 - -3766881274857795274L ^ -1529056393672882864L;
               continue;
            }

            if (b <= 6) {
               b2 = 1;
               break;
            }

            l = shadedBigDecimal.intCompact;
            l3 = l3 - 8551630446278600678L ^ 6426406305226205604L;
            if (l2 != -9223372036854775808L) {
               l3 = l3 + -2410688349651690830L ^ 3063153955207047337L;
               continue;
            }

            l3 = l3 + -7118614065843115252L ^ 3924968190209388223L;
         } else {
            if (this.scale == shadedBigDecimal.scale) {
               l3 = l3 + -6258909325633364281L ^ 8287015571180107140L;
               continue;
            }

            l3 = (l3 ^ -2532401460559395707L) + -7546768147475009177L;
         }

         n = this.signum();
         n2 = shadedBigDecimal.signum();
         if (n != n2) {
            return n > n2 ? 1 : -1;
         }

         if (n == 0) {
            return 0;
         }

         n3 = this.compareMagnitude(shadedBigDecimal);
         return n > 0 ? n3 : -n3;
      }

      return b2;
   }

   private static CustomBigInteger divideAndRoundByTenPow(CustomBigInteger customBigInteger, int n, int n2) {
      byte b = 1;

      while (b > 0) {
         if (n < LONG_TEN_POWERS_TABLE.length) {
            ;
         }

         while (2 >= b) {
            customBigInteger = divideAndRound(customBigInteger, LONG_TEN_POWERS_TABLE[n], n2);
         }

         if (b > 3) {
            customBigInteger = divideAndRound2(customBigInteger, bigTenToThe(n), n2);
         }
      }

      return customBigInteger;
   }

   public ShadedBigDecimal scaleByPowerOfTen(int n) {
      long l2 = (long)this.scale - (long)n;
      return new ShadedBigDecimal(this.intVal, this.intCompact, this.checkScale(l2), this.precision);
   }

   private static ShadedBigDecimal multiplyAndRound(long l, long l2, int n, ShadedMathContext shadedMathContext) {
      byte b = 0;
      long l3 = 0L;
      long l4 = 0L;
      long l5 = 0L;
      long l6 = 0L;
      ShadedBigDecimal shadedBigDecimal = null;
      long l7 = 0L;
      long l8 = 0L;
      long l9 = 0L;
      long l10 = 0L;
      long l11 = 0L;
      long l12 = 0L;
      long l13 = 0L;
      byte b2 = 4;
      long l14 = 0L;
      l14 = 6127774745979591486L ^ (long)"\ud9d3\ue3ce脠ᠭ쟽ὗ瘞㺋䊍竆ﯷ酁湖ℾ쟿ⷌ鵩匯烳ό㝟䭖".hashCode();

      while (true) {
         outerLoop:
         while (17 < b2) {
            if (b2 >= 26) {
               if (b2 <= 26) {
                  l5 = multiply2(l, l2);
                  l14 = (l14 ^ -2691098378994178981L) + -680004140481232947L;
               } else if (30 <= b2) {
                  if (30 >= b2) {
                     l9 = make64(l13, l3);
                     l14 = (l14 ^ -8283812532813909370L) + -7123142149459436814L;
                     continue;
                  }

                  if (32 > b2) {
                     l5 = l12 * l4;
                     l14 = l14 - 8076768121978960668L ^ 5510428243931618013L;
                     continue;
                  }

                  if (b2 <= 32) {
                     l10 = l5 & 4294967295L;
                     l14 = (l14 ^ -2773413874491900508L) - -3307777568915364284L;
                  } else if (b2 <= 33) {
                     b = 1;
                     l14 = l14 - -131074862264917456L ^ -6401017960607870277L;
                  } else {
                     l8 = l5 & 4294967295L;
                     l14 = (l14 ^ -6265103009940193335L) - 5940933685563998704L;
                  }
               } else {
                  if (28 > b2) {
                     l7 = l2 >>> 32;
                     l14 = l14 + 1483038954304588582L ^ -4973931409102235196L;
                     continue;
                  }

                  if (28 < b2) {
                     return doRound(shadedBigDecimal, shadedMathContext);
                  }

                  if (l2 >= 0L) {
                     l14 = l14 + 673975585797929733L ^ -7745344102459782034L;
                     continue;
                  }

                  l14 = l14 - 283461044380219076L ^ -5708282522457805717L;
               }
            } else if (b2 >= 21) {
               if (b2 <= 21) {
                  l13 = l3 >>> 32;
                  l14 = (l14 ^ 4890169032783452153L) + 1241982693425134195L;
                  continue;
               }

               if (23 > b2) {
                  l4 = l2 & 4294967295L;
                  l14 = l14 + 9069482723847946571L ^ 5320506108151383230L;
                  continue;
               }

               if (b2 <= 23) {
                  l8 = l5 & 4294967295L;
                  l14 = (l14 ^ -3084165287085405735L) + -2600593775578470045L;
               } else if (b2 <= 24) {
                  l3 = l5 & 4294967295L;
                  l14 = l14 - -3575668034433506457L ^ -2172388300398098253L;
               } else {
                  l3 &= 4294967295L;
                  l14 = l14 - -7205908468529797378L ^ 115934622504892997L;
               }
            } else if (19 <= b2) {
               if (19 >= b2) {
                  l6 = make64(l8, l10);
                  l14 = l14 + -6659462808729029009L ^ -4016473718586008682L;
                  continue;
               }

               l11 = l >>> 32;
               l14 = (l14 ^ -4690426151803768899L) + 6148259235371982968L;
            } else {
               shadedBigDecimal = doRound128(l9, l6, b, n, shadedMathContext);
               l14 = l14 + -6826617046200085179L ^ 4687204224330566478L;
            }

            while (true) {
               while (8 > b2) {
                  if (3 <= b2) {
                     if (b2 <= 3) {
                        l5 = l11 * l4 + l8;
                        l14 = (l14 ^ -6210283061498361511L) - 8237800479231998190L;
                        continue outerLoop;
                     }

                     if (5 <= b2) {
                        if (5 >= b2) {
                           l13 = (l5 >>> 32) + l13 & 4294967295L;
                           l14 = (l14 ^ 7136257676392318946L) + -7704542446760396843L;
                        } else {
                           if (b2 > 6) {
                              return doRound2(l5, n, shadedMathContext);
                           }

                           l5 = l12 * l7 + l8;
                           l14 = (l14 ^ 3367787429060344920L) + 4007690989596371491L;
                        }
                        continue outerLoop;
                     }

                     l = -l;
                     l14 = (l14 ^ 7911267012007329648L) + 59964897137523048L;
                  } else {
                     if (b2 < 1) {
                        CustomBigInteger customBigInteger = CustomBigInteger.valueOf(l);
                        long l15 = l2 * (long)b;
                        shadedBigDecimal = new ShadedBigDecimal(customBigInteger.multiply(l15), -9223372036854775808L, n, 0);
                        l14 = l14 + 4488054218319707779L ^ 2154553183542841072L;
                        continue outerLoop;
                     }

                     if (1 < b2) {
                        l2 = -l2;
                        l14 = l14 - -6131639957356044458L ^ -2802886948032796681L;
                        continue outerLoop;
                     }

                     l8 = l5 >>> 32;
                     l14 = (l14 ^ 4554300439804280168L) + 4914937014060495025L;
                  }
               }

               if (b2 <= 8) {
                  if (shadedBigDecimal == null) {
                     l14 = (l14 ^ 2785776794489293531L) - -3041955938887486837L;
                  } else {
                     l14 = l14 + 8368590650234950155L ^ -3088971398639797880L;
                  }
               } else {
                  if (12 > b2) {
                     if (b2 >= 10) {
                        if (b2 <= 10) {
                           return shadedBigDecimal;
                        }

                        l5 = l11 * l7 + l3;
                        l14 = (l14 ^ -9177617639413338341L) - -860152964801056282L;
                     } else {
                        l12 = l & 4294967295L;
                        l14 = (l14 ^ -5456655112600291253L) - -998721933483105815L;
                     }
                     break;
                  }

                  if (b2 <= 12) {
                     l3 = l5 >>> 32;
                     l14 = l14 + -3304311235464148551L ^ -3045871180335612732L;
                  } else if (b2 >= 14) {
                     if (b2 <= 14) {
                        l3 += l5 >>> 32;
                        l14 = (l14 ^ 962194749146008544L) + 1186848316761666499L;
                        break;
                     }

                     if (b2 <= 15) {
                        b = -1;
                        l14 = (l14 ^ 6119891476489345248L) - -846981532642689609L;
                        break;
                     }

                     if (l5 == -9223372036854775808L) {
                        l14 = (l14 ^ -7875941760237795132L) - -2341502701971113981L;
                        break;
                     }

                     l14 = l14 - 5612648181243260331L ^ 8659578807337724898L;
                  } else {
                     if (l >= 0L) {
                        l14 = l14 - 1714963071251735088L ^ 7438457634968380209L;
                        break;
                     }

                     l14 = (l14 ^ 449043495290314183L) - 7971770274276664863L;
                  }
               }
            }
         }

         b *= -1;
         l14 = (l14 ^ 227336819543742956L) - 8235960229850474631L;
      }
   }

   private static ShadedBigDecimal divideSmallFastPath(long l, int n, long l2, int n2, long l3, ShadedMathContext shadedMathContext) {
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      long l4 = 0L;
      int n6 = 0;
      long l5 = 0L;
      int n7 = 0;
      long l6 = 0L;
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      long l7 = 0L;
      int n8 = 0;
      int n9 = 0;
      byte b = 6;
      long l8 = 0L;
      l8 = 5876567787349341089L ^ (long)"굑\ue101쿫聱啴ﱛᒮ㈱펞伒疎↠춴柷彚風ੳ\ua63b衘⟐ʺ畬".hashCode();

      long l9;
      outerLoop:
      while (true) {
         while (b <= 4) {
            if (n3 < 18) {
               l8 = (l8 ^ -6296345422462039899L) + 1267617396914876095L;
            } else {
               l8 = (l8 ^ 2563346464320577508L) - -4999509705837116977L;
            }
         }

         if (b >= 7) {
            if (7 >= b) {
               n5 = n2 - n;
               l8 = (l8 ^ -3380543961534350868L) + -8258918641892699270L;
               continue;
            }

            if (b <= 8) {
               n3 = shadedMathContext.precision;
               l8 = l8 - 1123141112827775426L ^ 9081919279317944012L;
               n7 = shadedMathContext.roundingMode.oldMode;
               l8 = l8 + -7442929648382910344L ^ -3248935165169109690L;
               continue;
            }

            if (n5 != 0) {
               l8 = l8 + -7781129224766857747L ^ 3933266907612216908L;
               l9 = longMultiplyPowerTen(l, n5);
               break;
            }

            l8 = l8 - 7672971599867864539L ^ 582656158731450996L;
         } else {
            if (5 < b) {
               throw new AssertionError();
            }

            if (assertionsDisabled) {
               l8 = (l8 ^ 2389517428107639027L) - 9162041916487267936L;
               continue;
            }

            l8 = l8 - -2890695132884459956L ^ -5234792815938043302L;
         }

         while (1 < b) {
            if (2 >= b) {
               l9 = l;
               break outerLoop;
            }

            if (n > n2) {
               l8 = (l8 ^ 2562428784057314956L) - 5733706791944982116L;
               continue outerLoop;
            }

            l8 = l8 + -8563557418564533841L ^ 853832441518474794L;
         }

         if (n2 >= 18) {
            l8 = l8 - 9176862358518631783L ^ -3374997917567953378L;
         } else {
            l8 = l8 + -352137280330661261L ^ 3640301840078683027L;
         }
      }

      l5 = l9;
      n6 = longCompareMagnitude(l5, l2);
      if (n6 > 0) {
         n2--;
         n9 = checkScaleNonZero(l3 + (long)n2 - (long)n + (long)n3);
         if (checkScaleNonZero((long)n3 + (long)n2 - (long)n) > 0) {
            l9 = (long)n3 + (long)n2 - (long)n;
            n8 = checkScaleNonZero(l9);
            if ((l7 = longMultiplyPowerTen(l, n8)) == -9223372036854775808L) {
               obj = null;
               if (n3 - 1 >= 0 && n3 - 1 < LONG_TEN_POWERS_TABLE.length) {
                  obj = multiplyDivideAndRound(LONG_TEN_POWERS_TABLE[n3 - 1], l5, l2, n9, n7, checkScaleNonZero(l3));
               }

               if ((ShadedBigDecimal)obj == null) {
                  obj2 = bigMultiplyPowerTen(l5, n3 - 1);
                  obj = divideAndRound2((CustomBigInteger)obj2, l2, n9, n7, checkScaleNonZero(l3));
               }
            } else {
               obj = divideAndRound(l7, l2, n9, n7, checkScaleNonZero(l3));
            }
         } else {
            n8 = checkScaleNonZero((long)n - (long)n3);
            if (n8 == n2) {
               obj = divideAndRound(l, l2, n9, n7, checkScaleNonZero(l3));
            } else {
               n4 = checkScaleNonZero((long)n8 - (long)n2);
               if ((l4 = longMultiplyPowerTen(l2, n4)) == -9223372036854775808L) {
                  obj4 = bigMultiplyPowerTen(l2, n4);
                  obj = divideAndRound3(CustomBigInteger.valueOf(l), (CustomBigInteger)obj4, n9, n7, checkScaleNonZero(l3));
               } else {
                  obj = divideAndRound(l, l4, n9, n7, checkScaleNonZero(l3));
               }
            }
         }
      } else {
         n9 = checkScaleNonZero(l3 + (long)n2 - (long)n + (long)n3);
         if (n6 == 0) {
            obj = roundedTenPower(l5 < 0L == l2 < 0L ? 1 : -1, n3, n9, checkScaleNonZero(l3));
         } else if ((l6 = longMultiplyPowerTen(l5, n3)) == -9223372036854775808L) {
            obj = null;
            if (n3 < LONG_TEN_POWERS_TABLE.length) {
               obj = multiplyDivideAndRound(LONG_TEN_POWERS_TABLE[n3], l5, l2, n9, n7, checkScaleNonZero(l3));
            }

            if ((ShadedBigDecimal)obj == null) {
               obj3 = bigMultiplyPowerTen(l5, n3);
               obj = divideAndRound2((CustomBigInteger)obj3, l2, n9, n7, checkScaleNonZero(l3));
            }
         } else {
            obj = divideAndRound(l6, l2, n9, n7, checkScaleNonZero(l3));
         }
      }

      return doRound((ShadedBigDecimal)obj, shadedMathContext);
   }

   public int intValueExact() {
      long l = 0L;
      long l2 = 0L;
      l2 = 1150341354913240200L ^ (long)"䙲㌛䶢\udfc6쁜㎜↺൰㳆疾\ue791옇㍟맕恹鬿ｐ\ue812੧嘐奢Ọ".hashCode();
      l = this.longValueExact();
      l2 = (l2 ^ 6571582584498908883L) + -1933780736102182119L;
      if ((long)((int)l) == l) {
         l2 = (l2 ^ -5345822823787624934L) - 5943730798494381880L;
         return (int)l;
      } else {
         l2 = l2 - -4380220689976750172L ^ 3192194929280656690L;
         throw new ArithmeticException("Overflow");
      }
   }

   private static ShadedBigDecimal add(long l, long l2, int n) {
      long l3 = 0L;
      long l4 = 0L;
      l4 = -3310096529464417276L ^ (long)"졇里ﰤᕈ坝絧唶몛祤뽚\ue033⛚甤ﮒោ\udcad㦯伧⥨襶퀉㬢쥬ﺊ\u05f6㹾".hashCode();
      l3 = add2(l, l2);
      l4 = (l4 ^ -8794742565892082333L) + 642932719221699293L;
      if (l3 == -9223372036854775808L) {
         l4 = (l4 ^ 7091589686927190376L) + 2785142578837996206L;
         return new ShadedBigDecimal(CustomBigInteger.valueOf(l).add(l2), n);
      } else {
         l4 = l4 - 2514057840579250740L ^ 3785571834286317651L;
         return valueOf(l3, n);
      }
   }

   private static ShadedBigDecimal doRound(ShadedBigDecimal shadedBigDecimal, ShadedMathContext shadedMathContext) {
      CustomBigInteger customBigInteger = null;
      boolean flag = false;
      int n = 0;
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      long l = 0L;
      byte b = 5;
      long l2 = 0L;
      l2 = 5076490793466362792L ^ (long)"컖Ɣ글馕淠\u2fe2缩紕쨷\uea39봀\uf10f\uf5fb䠴\uf489逓㽹ᱸ᱗䦙茶͋鴁캞".hashCode();

      outerLoop:
      while (true) {
         while (true) {
            if (16 >= b) {
               l = divideAndRound3(l, LONG_TEN_POWERS_TABLE[n3], shadedMathContext.roundingMode.oldMode);
               l2 = l2 - 5001822238883411022L ^ 8735289051559661835L;
               break;
            }

            if (b >= 24) {
               if (b <= 24) {
                  l2 = (l2 ^ -9117143562979190131L) + -3436907137552101906L;
                  break;
               }

               if (b >= 28) {
                  if (28 >= b) {
                     n5 = checkScaleNonZero((long)n5 - (long)n3);
                     l2 = l2 + -7267627716497437251L ^ -5049761321983206600L;
                  } else {
                     if (b < 30) {
                        n5 = checkScaleNonZero((long)n5 - (long)n3);
                        l2 = l2 - -7125034759078027794L ^ 3810653655170023581L;
                        break;
                     }

                     if (b <= 30) {
                        l2 = (l2 ^ 1908672791537216212L) + -8324286580365051492L;
                     } else if (31 >= b) {
                        n3 = n2 - n4;
                        l2 = l2 - -1199033807940530272L ^ 301808067230835050L;
                     } else {
                        if (n3 <= 0) {
                           l2 = (l2 ^ 7806729188732147235L) - -8755924032740805947L;
                           break;
                        }

                        l2 = (l2 ^ 6927479105602141237L) - 3942918685584499510L;
                     }
                  }
               } else {
                  if (26 > b) {
                     n3 = n2 - n4;
                     l2 = l2 - -2258252016611256077L ^ -7175485216875562388L;
                     break;
                  }

                  if (b <= 26) {
                     customBigInteger = shadedBigDecimal.intVal;
                     l2 = (l2 ^ 4239088518142411615L) + -5381055817879561116L;
                     break;
                  }

                  if (l == -9223372036854775808L) {
                     l2 = (l2 ^ 5436352880295506757L) + -6160601506248773572L;
                     break;
                  }

                  l2 = l2 - 6430974951839565733L ^ -9002308381307913564L;
               }
            } else if (b >= 20) {
               if (20 >= b) {
                  n2 = longDigitLength(l);
                  l2 = (l2 ^ 933233394937975805L) - -4132163935516717374L;
               } else {
                  if (b < 22) {
                     n4 = shadedMathContext.precision;
                     l2 = l2 + -1194839084652491216L ^ 6872499752597826133L;
                     break;
                  }

                  if (b <= 22) {
                     flag = true;
                     l2 = l2 - -7084357171044255834L ^ 5109746915407845908L;
                     break;
                  }

                  n2 = longDigitLength(l);
                  l2 = l2 - 8134902149010341985L ^ 3513936774927997838L;
               }
            } else if (18 <= b) {
               if (b > 18) {
                  n5 = shadedBigDecimal.scale;
                  l2 = l2 + -5913494782751702623L ^ 8474526756918581598L;
                  break;
               }

               n3 = n2 - n4;
               l2 = l2 - 3729666989158602176L ^ -6283728032695040228L;
            } else {
               if (n3 <= 0) {
                  l2 = l2 - -4240689754523501033L ^ -4620450239902836510L;
                  break;
               }

               l2 = (l2 ^ -4796632292692486627L) - -2437539097464924420L;
            }
         }

         while (true) {
            while (7 > b) {
               if (b >= 3) {
                  if (b <= 3) {
                     if (n4 <= 0) {
                        l2 = l2 + 7019074348253679011L ^ 3596944275239080557L;
                        return shadedBigDecimal;
                     }

                     l2 = (l2 ^ -8275407847432056342L) + 6803403453661836517L;
                  } else if (5 <= b) {
                     if (5 >= b) {
                        l = compactValFor(customBigInteger);
                        l2 = (l2 ^ 4352990907505343687L) + 7670549249251676445L;
                     } else {
                        customBigInteger = divideAndRoundByTenPow(customBigInteger, n3, n);
                        l2 = l2 - 599075738581133675L ^ -2628543551113555771L;
                     }
                  } else {
                     flag = true;
                     l2 = (l2 ^ 6438356857257984985L) + 8778254224697090858L;
                  }
                  continue outerLoop;
               }

               if (1 > b) {
                  l2 = (l2 ^ -4663083923675587523L) + -8932265596959049108L;
                  continue outerLoop;
               }

               if (1 < b) {
                  l = shadedBigDecimal.intCompact;
                  l2 = (l2 ^ 2844187891363312405L) - 8869161829035095066L;
                  continue outerLoop;
               }

               if (l == -9223372036854775808L) {
                  l2 = l2 + 2743982311434436242L ^ 4178544221738940815L;
                  continue outerLoop;
               }

               l2 = (l2 ^ -6094617495467402550L) + 8613011227702287138L;
            }

            if (7 >= b) {
               if (l != -9223372036854775808L) {
                  l2 = l2 - 5584445958990638860L ^ 7888740057504418466L;
                  break;
               }

               l2 = l2 - 8879859830957116L ^ -2824120860567586206L;
            } else if (b >= 11) {
               if (b <= 11) {
                  customBigInteger = null;
                  l2 = l2 - 8600688171047332577L ^ -3182732239189831705L;
                  break;
               }

               if (b >= 13) {
                  if (b <= 13) {
                     return new ShadedBigDecimal(customBigInteger, l, n5, n2);
                  }

                  if (b <= 14) {
                     n2 = shadedBigDecimal.precision();
                     l2 = (l2 ^ -8095242811239498705L) - 8477502570355572042L;
                  } else {
                     n3 = n2 - n4;
                     l2 = l2 - 5121144086530411737L ^ 5823487555504342407L;
                  }
               } else {
                  if (!flag) {
                     l2 = (l2 ^ 334022400275421675L) + -1513970572933774703L;
                     return shadedBigDecimal;
                  }

                  l2 = l2 + 5700206798520375575L ^ -1205962567640415901L;
               }
            } else if (b >= 9) {
               if (9 >= b) {
                  flag = false;
                  l2 = l2 + 6161692360911097213L ^ -3380768985078674931L;
               } else {
                  n2 = bigDigitLength(customBigInteger);
                  l2 = (l2 ^ 2283822359230761543L) + -6119700472971133919L;
               }
            } else {
               n = shadedMathContext.roundingMode.oldMode;
               l2 = l2 + 8583793807786645800L ^ 2854214740289816203L;
            }
         }
      }
   }

   @Override
   public float floatValue() {
      long l = 0L;
      l = -8064802883141762564L ^ (long)"ᚿﾣ剤䏆ꦂ\uee65깲⬋๘뷠죿\ue6dd쐯䈘臟炎ేఉ鶽蕧밠✊\u0ad9ﾡ".hashCode();
      if (this.intCompact == -9223372036854775808L) {
         l = (l ^ -2559724963963100946L) - 6657631982181235862L;
      } else {
         l = (l ^ -8339307145430562139L) - -3633525351158277935L;
         if (this.scale == 0) {
            l = l + -969411832791343589L ^ 5976190954227273741L;
            return (float)this.intCompact;
         }

         l = (l ^ 6201330759372239734L) + 2495287278201289444L;
         if (Math.abs(this.intCompact) >= 4194304L) {
            l = (l ^ -8135517365518255251L) + 7602297068843519341L;
         } else {
            l = l - 763635742420002269L ^ -7113120514170512415L;
            if (this.scale <= 0) {
               l = l + 2519612839813494294L ^ -5281956745267352375L;
            } else {
               l = l - 624391697767072405L ^ -8518398593508838305L;
               if (this.scale < float10pow.length) {
                  l = (l ^ 5783744446134586778L) + 2968132649686245182L;
                  return (float)this.intCompact / float10pow[this.scale];
               }

               l = (l ^ -703133644509789729L) - 7234032839942340508L;
            }

            if (this.scale >= 0) {
               l = (l ^ -1456084578811263751L) + -6774862837178975766L;
            } else {
               l = l + 920771380074490493L ^ 387057919745333840L;
               if (this.scale > -float10pow.length) {
                  l = (l ^ -2716581690687045873L) + -8833515322403279439L;
                  return (float)this.intCompact * float10pow[-this.scale];
               }

               l = (l ^ 836066807698316860L) - 1030552069650377000L;
            }
         }
      }

      return Float.parseFloat(this.toString());
   }

   private static ShadedBigDecimal divide6(CustomBigInteger customBigInteger, int n, CustomBigInteger customBigInteger2, int n2, long l, ShadedMathContext shadedMathContext) {
      CustomBigInteger customBigInteger3 = null;
      ShadedBigDecimal shadedBigDecimal = null;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      int n6 = 0;
      CustomBigInteger customBigInteger4 = null;
      int n7 = 0;
      byte b = 13;
      long l2 = 0L;
      l2 = 4782647670004408140L ^ (long)"䈒儣㡍렟◮鬗�禁嶛鰛\ue3b0陀궑逭碮铇헭\uf440䲪\udf89䚜镇\uf4e0ᙺ䭰ᒱ".hashCode();

      outerLoop:
      while (true) {
         if (b <= 7) {
            shadedBigDecimal = divideAndRound3(customBigInteger4, customBigInteger2, n7, n5, checkScaleNonZero(l));
            l2 = l2 + 5260359153895303655L ^ 6007148528073555874L;
         } else if (11 <= b) {
            if (11 < b) {
               if (13 > b) {
                  return doRound(shadedBigDecimal, shadedMathContext);
               }

               if (b <= 13) {
                  customBigInteger3 = bigMultiplyPowerTen2(customBigInteger2, n4);
                  l2 = l2 + 4398225649652547922L ^ -3603117650870922158L;
               } else {
                  customBigInteger4 = bigMultiplyPowerTen2(customBigInteger, n6);
                  l2 = (l2 ^ -3437417945476381987L) - -1730439714863200700L;
               }
               continue;
            }

            if (compareMagnitudeNormalized2(customBigInteger, n, customBigInteger2, n2) > 0) {
               l2 = l2 + 4118810767925832443L ^ 1889297368207199168L;
               continue;
            }

            l2 = (l2 ^ -894008434756880173L) + 4915230379831748837L;
         } else {
            if (9 > b) {
               shadedBigDecimal = divideAndRound3(customBigInteger, customBigInteger3, n7, n5, checkScaleNonZero(l));
               l2 = (l2 ^ 8398653235573638293L) - -8116658868709634606L;
               continue;
            }

            if (b <= 9) {
               n4 = checkScaleNonZero((long)n6 - (long)n2);
               l2 = l2 - 2032931192110041154L ^ -5546104073980624610L;
               continue;
            }

            n2--;
            l2 = l2 + -5378377948630050107L ^ -2093119533143025034L;
         }

         while (true) {
            while (b < 3) {
               if (b < 1) {
                  n6 = checkScaleNonZero((long)n - (long)n3);
                  l2 = (l2 ^ 2928554989955712616L) + 284418840369339683L;
                  continue outerLoop;
               }

               if (1 >= b) {
                  n3 = shadedMathContext.precision;
                  l2 = l2 - -7875854256266060696L ^ -2372271078534487025L;
               } else {
                  n5 = shadedMathContext.roundingMode.oldMode;
                  l2 = (l2 ^ 4075735942399293908L) - -3490334452532153595L;
               }
            }

            if (b <= 3) {
               n7 = checkScaleNonZero(l + (long)n2 - (long)n + (long)n3);
               l2 = l2 - -7875846309207354835L ^ 1699537973995281306L;
            } else {
               if (5 <= b) {
                  if (b <= 5) {
                     l2 = l2 + -2947173328726902671L ^ -2009883784302249993L;
                  } else {
                     n6 = checkScaleNonZero((long)n3 + (long)n2 - (long)n);
                     l2 = (l2 ^ -7217265430736230391L) + 4172116157567027143L;
                  }
                  break;
               }

               if (checkScaleNonZero((long)n3 + (long)n2 - (long)n) <= 0) {
                  l2 = l2 - 7698623736001680530L ^ -5397910168625118249L;
               } else {
                  l2 = (l2 ^ -4791955028110594454L) - 9127858940018765361L;
               }
            }
         }
      }
   }

   @Override
   public int hashCode() {
      int n = 0;
      long l = 0L;
      long l2 = 0L;
      l2 = 8484146284776329336L ^ (long)"能빭隁偮ᇕ꡶脐铊觡峦\ue5ec过䝉\ue92e炷朝\udd50ⲕ\uf877㶕ꂣ쬍쐘㍃".hashCode();
      if (this.intCompact == -9223372036854775808L) {
         l2 = (l2 ^ 4209746325730928819L) + -203369410696055275L;
         return 31 * this.intVal.hashCode() + this.scale;
      } else {
         l2 = l2 + 1000273909271276366L ^ 5132896277642215171L;
         long l3;
         if (this.intCompact >= 0L) {
            l2 = (l2 ^ -2378388837674082999L) + -6091759507469692950L;
            l3 = this.intCompact;
         } else {
            l2 = (l2 ^ -1219320236794076931L) - -9029543257409442966L;
            l3 = -this.intCompact;
         }

         l = l3;
         n = (int)((long)((int)(l >>> 32) * 31) + (l & 4294967295L));
         return 31 * (this.intCompact < 0L ? -n : n) + this.scale;
      }
   }

   private static long mulsub(long l, long l2, long l3, long l4, long l5) {
      long l6 = l2 - l5 * l4;
      return make64(l + (l6 >>> 32) - l5 * l3, l6 & 4294967295L);
   }

   private static boolean unsignedLongCompareEq(long l, long l2) {
      return l + -9223372036854775808L >= l2 + -9223372036854775808L;
   }

   private int checkScale(long l) {
      int n = 0;
      Object obj = null;
      byte b = 1;
      long l2 = 0L;
      l2 = 8418736344408709582L ^ (long)"菬䩛傂瘠ᴋଢ⩷拵\ue639髽\uf37f壂甙\uebf2䓆恖鯷沚ᝉ뼶ಏ鞉霿\uf088\u0a00".hashCode();

      while (true) {
         if (1 >= b) {
            n = (int)l;
            l2 = (l2 ^ 2380565233508450744L) - 9053682230069110499L;
            if ((long)n != l) {
               l2 = l2 - 7270723405881485157L ^ 4616485624321415449L;
            } else {
               l2 = (l2 ^ -3495864304778187885L) + 4082018616837013598L;
               break;
            }
         } else {
            int n2;
            if (2 >= b) {
               if (l > 2147483647L) {
                  l2 = l2 - 5985303625038739660L ^ 3449309363831964679L;
                  continue;
               }

               l2 = l2 - 6166076692600272515L ^ 3052354990089995982L;
               n2 = -2147483648;
            } else {
               n2 = 2147483647;
            }

            n = n2;
            if (this.intCompact == 0L) {
               break;
            }

            obj = this.intVal;
            if (this.intVal != null && ((CustomBigInteger)obj).getSignum() == 0) {
               break;
            }

            throw new ArithmeticException(n > 0 ? "Underflow" : "Overflow");
         }
      }

      return n;
   }

   public ShadedBigDecimal divide3(ShadedBigDecimal shadedBigDecimal, int n) {
      return this.divide(shadedBigDecimal, this.scale, n);
   }

   private static int compareMagnitudeNormalized(long l, int n, CustomBigInteger customBigInteger, int n2) {
      int n3 = 0;
      byte b = 1;
      long l2 = 0L;
      l2 = 557676334202136214L ^ (long)"Ἰ㨷쏂㙏騁氬킧꿽㌔숻㨙곾씮엇逧̥\ueaa5뺩ꦭ\ue1a4᧦﹢\u2d7a㘀ᐓ\ud918\uea6b\ue443".hashCode();

      while (b > 3) {
         if (5 <= b) {
            if (5 >= b) {
               return -1;
            }

            if (l != 0L) {
               l2 = l2 + -4373363484902179372L ^ -6927410791609300641L;
            } else {
               l2 = (l2 ^ 6719582768166074724L) + -969790208127196110L;
            }
         } else {
            if (n3 >= 0) {
               l2 = l2 - 3594969383869742261L ^ 3610835755719650337L;
               continue;
            }

            l2 = l2 - -6155575966242706772L ^ 7751086476793581222L;
         }

         if (1 <= b) {
            if (1 >= b) {
               return -1;
            }

            if (longMultiplyPowerTen(l, -n3) != -9223372036854775808L) {
               l2 = l2 + -4339203517567771356L ^ -105216276452737102L;
            } else {
               l2 = (l2 ^ -3439541964060772337L) - -9009124597169005301L;
            }
         } else {
            n3 = n - n2;
            l2 = (l2 ^ 5316190162156040657L) - 2055384680865200105L;
         }
      }

      return bigMultiplyPowerTen(l, -n3).compareMagnitude(customBigInteger);
   }

   private static void print(String str, ShadedBigDecimal shadedBigDecimal) {
      System.err.format("%s:\tintCompact %d\tintVal %d\tscale %d\tprecision %d%n", str, shadedBigDecimal.intCompact, shadedBigDecimal.intVal, shadedBigDecimal.scale, shadedBigDecimal.precision);
   }

   @Override
   public double doubleValue() {
      long l = 0L;
      l = -7427641822093929465L ^ (long)"ዬ\udbcb闚⫺⫋㏒ꤵ囨돉縂뀲뎳ᥠ餇ⴡओ鳴꾕䛽龎쵳\uda64駀གྷ".hashCode();
      if (this.intCompact == -9223372036854775808L) {
         l = (l ^ -9042766112891703017L) + -3656961794586207068L;
      } else {
         l = (l ^ 1267641859007348676L) + 4762506342698767632L;
         if (this.scale == 0) {
            l = (l ^ -1243629481908730720L) - 343753507080483705L;
            return (double)this.intCompact;
         }

         l = l - -1153846137945997841L ^ -5200593912272835025L;
         if (Math.abs(this.intCompact) >= 4503599627370496L) {
            l = (l ^ -7700786292333430408L) - 4005585738829403263L;
         } else {
            l = l - 3582227071706445782L ^ -9067525212398188129L;
            if (this.scale <= 0) {
               l = (l ^ -5067328818883146724L) + 7016734205301202448L;
            } else {
               l = (l ^ -3198289189430256901L) - -4336918468797542921L;
               if (this.scale < double10pow.length) {
                  l = l - -1900463384357152625L ^ 8521980072400018379L;
                  return (double)this.intCompact / double10pow[this.scale];
               }

               l = l - 1211093668684920750L ^ -8002338077123743958L;
            }

            if (this.scale >= 0) {
               l = l + 3595504174233720878L ^ -4932130963595159068L;
            } else {
               l = (l ^ 6670906616652990492L) - -8793221185575663026L;
               if (this.scale > -double10pow.length) {
                  l = l + -1906295153222223754L ^ 5022954735608296231L;
                  return (double)this.intCompact * double10pow[-this.scale];
               }

               l = (l ^ -3046455012662836327L) - 2124535572365867197L;
            }
         }
      }

      return Double.parseDouble(this.toString());
   }

   private CustomBigInteger bigMultiplyPowerTen(int n) {
      byte b = 1;
      long l = 0L;
      l = -5869902278384555143L ^ (long)"၁琘趸呮ᦌꞼ歧㔤푲\udab0ﲐ䷯\ue41d펱︐쓘挓邒乵銤ꇆ".hashCode();

      while (b <= 2) {
         if (n > 0) {
            l = (l ^ 6007411298034649307L) + -3543460446866585408L;
            if (this.intCompact != -9223372036854775808L) {
               l = (l ^ -7170374584901642061L) + -2294070485122013170L;
               return bigTenToThe(n).multiply(this.intCompact);
            }

            l = (l ^ 69201132513513616L) + 3106873899323444412L;
         } else {
            l = (l ^ 5229221140244889035L) - 3454854234070965506L;
         }
      }

      if (3 >= b) {
         return this.intVal.multiply2(bigTenToThe(n));
      } else {
         return this.inflated();
      }
   }

   public ShadedBigDecimal(String str, ShadedMathContext shadedMathContext) {
       this(str.toCharArray(), 0, str.length(), shadedMathContext);
   }

   public ShadedBigDecimal[] divideAndRemainder(ShadedBigDecimal shadedBigDecimal, ShadedMathContext shadedMathContext) {
      Object obj = null;
      ShadedBigDecimal[] shadedBigDecimals = null;
      byte b = 0;

      while (true) {
         while (3 < b) {
            if (b < 5) {
               return this.divideAndRemainder(shadedBigDecimal);
            }

            if (5 >= b) {
               obj = this;

               while (true) {
                  while (1 > b) {
                     shadedBigDecimals[0] = ((ShadedBigDecimal)obj).divideToIntegralValue(shadedBigDecimal, shadedMathContext);
                  }

                  if (b > 1) {
                     return shadedBigDecimals;
                  }

                  shadedBigDecimals[1] = ((ShadedBigDecimal)obj).subtract(shadedBigDecimals[0].multiply(shadedBigDecimal));
               }
            }

            shadedBigDecimals = new ShadedBigDecimal[2];
         }

         if (shadedMathContext.precision == 0) {
            ;
         }
      }
   }

   public int precision() {
      int n = 0;
      long l = 0L;
      byte b = 2;
      long l2 = 0L;
      l2 = -4888621487015650810L ^ (long)"弬Ặ틬烍铰㙜똦ୈ簂蚴雦싈삘ꪇ醤\uf6b7갱\ude81⊔㤂忐諤눫".hashCode();

      outerLoop:
      while (b > 1) {
         if (b <= 2) {
            n = this.precision;
            l2 = (l2 ^ -1055991496331883163L) + 4574686492441517221L;
            if (n != 0) {
               l2 = (l2 ^ -3212608571692985630L) + 7912470779336076900L;
            } else {
               l2 = (l2 ^ 8847679419836621323L) + 9028559472053748359L;

               while (b > 4) {
                  if (6 <= b) {
                     if (b <= 6) {
                        l = this.intCompact;
                        l2 = l2 - 404802494004146044L ^ -7853507332449538955L;
                     } else {
                        if (b > 7) {
                           n = bigDigitLength(this.intVal);
                           l2 = (l2 ^ -7343476483704456820L) + -4273020093965581394L;
                           continue outerLoop;
                        }

                        if (l == -9223372036854775808L) {
                           l2 = (l2 ^ 9015837406062917713L) + 4389826952324746500L;
                        } else {
                           l2 = (l2 ^ 1727405233028235918L) - 8677262683947480532L;
                        }
                     }
                  } else {
                     n = longDigitLength(l);
                     l2 = (l2 ^ 1840551829501726050L) + 3962038322996150470L;
                  }
               }

               l2 = l2 - 3904156577303198060L ^ 8459631519735371693L;
            }
         } else {
            this.precision = n;
            l2 = l2 + 547124757633108477L ^ -1464775213823759857L;
         }
      }

      return n;
   }

   public ShadedBigDecimal divide2(ShadedBigDecimal shadedBigDecimal, CustomRoundingMode customRoundingMode) {
      int oldMode = customRoundingMode.oldMode;
      return this.divide(shadedBigDecimal, this.scale, oldMode);
   }

   public ShadedBigDecimal(double d, ShadedMathContext shadedMathContext) {
       super();
      if (!Double.isInfinite(d) && !Double.isNaN(d)) {
         long l = Double.doubleToLongBits(d);
         int n = l >> 63 == 0L ? 1 : -1;
         int n2 = (int)(l >> 52 & 2047L);
         long l2 = n2 == 0 ? (l & 4503599627370495L) << 1 : l & 4503599627370495L | 4503599627370496L;
         n2 -= 1075;
         if (l2 == 0L) {
            this.intVal = CustomBigInteger.ZERO;
            this.scale = 0;
            this.intCompact = 0L;
            this.precision = 1;
         } else {
            while ((l2 & 1L) == 0L) {
               l2 >>= 1;
               n2++;
            }

            int n3 = 0;
            long l3 = (long)n * l2;
            CustomBigInteger customBigInteger;
            if (n2 == 0) {
               customBigInteger = l3 == -9223372036854775808L ? INFLATED_BIGINT : null;
            } else {
               if (n2 < 0) {
                  customBigInteger = CustomBigInteger.valueOf(5L).pow(-n2).multiply(l3);
                  n3 = -n2;
               } else {
                  short s = 29601;
                  customBigInteger = CustomBigInteger.valueOf(2L).pow(n2).multiply(l3);
               }

               l3 = compactValFor(customBigInteger);
            }

            int n6 = 0;
            int precision2 = shadedMathContext.precision;
            if (precision2 > 0) {
               int oldMode = shadedMathContext.roundingMode.oldMode;
               if (l3 == -9223372036854775808L) {
                  n6 = bigDigitLength(customBigInteger);

                  for (int i = n6 - precision2; i > 0; i = n6 - precision2) {
                     n3 = checkScaleNonZero((long)n3 - (long)i);
                     customBigInteger = divideAndRoundByTenPow(customBigInteger, i, oldMode);
                     l3 = compactValFor(customBigInteger);
                     if (l3 != -9223372036854775808L) {
                        break;
                     }

                     n6 = bigDigitLength(customBigInteger);
                  }
               }

               if (l3 != -9223372036854775808L) {
                  n6 = longDigitLength(l3);

                  for (int j = n6 - precision2; j > 0; j = n6 - precision2) {
                     n3 = checkScaleNonZero((long)n3 - (long)j);
                     l3 = divideAndRound3(l3, LONG_TEN_POWERS_TABLE[j], shadedMathContext.roundingMode.oldMode);
                     n6 = longDigitLength(l3);
                  }

                  customBigInteger = null;
               }
            }

            this.intVal = customBigInteger;
            this.intCompact = l3;
            this.scale = n3;
            this.precision = n6;
         }
      } else {
         throw new NumberFormatException("Infinite or NaN");
      }
   }

   public static CustomBigInteger accessInflated(ShadedBigDecimal shadedBigDecimal) {
      return shadedBigDecimal.inflated();
   }

   private static long multiply2(long l, long l2) {
      long l3 = 0L;
      long l4 = 0L;
      long l5 = 0L;
      byte b = 1;
      long l6 = 0L;
      l6 = 3877873500178067646L ^ (long)"㨷띤饡궆念\uf690츮\udfe0웫ࣀ鋣얘仮鸅\u209d\uebeb㠬㿵\uf270ۮ⊽\uf0d5诿쥖ᚺ待燓ꭶ칯".hashCode();

      while (true) {
         outerLoop:
         while (b > 3) {
            if (b >= 5) {
               if (5 >= b) {
                  return l3;
               }

               if (b <= 6) {
                  l5 = Math.abs(l);
                  l6 = l6 - 8297993792985003253L ^ -6140377541498095653L;
               } else {
                  if (l3 / l2 == l) {
                     l6 = l6 + -2875915880398403518L ^ -4516974736199766577L;
                     continue;
                  }

                  l6 = (l6 ^ -8478661317869723596L) + -5889482690600927719L;
               }

               while (b >= 1) {
                  if (b <= 1) {
                     if ((l5 | l4) >>> 31 == 0L) {
                        l6 = (l6 ^ 2724667176542794445L) + -665530085612990701L;
                     } else {
                        l6 = l6 + 7351179136282887654L ^ -8862284405353295740L;
                     }
                     continue outerLoop;
                  }

                  l4 = Math.abs(l2);
                  l6 = (l6 ^ 2842805375340311880L) - -4220174009423771897L;
               }

               return -9223372036854775808L;
            } else if (l2 == 0L) {
               l6 = l6 + -6695841002222813011L ^ -5573122852025344433L;
            } else {
               l6 = l6 + 6098522984821181155L ^ 321348968733671944L;
            }
         }

         l3 = l * l2;
         l6 = (l6 ^ -1956529362713822826L) - -8750191730368014013L;
      }
   }

   public ShadedBigDecimal divide(ShadedBigDecimal shadedBigDecimal, ShadedMathContext shadedMathContext) {
      ShadedBigDecimal shadedBigDecimal2 = null;
      long l = 0L;
      int n = 0;
      int n2 = 0;
      int n3 = 0;
      byte b = 5;
      long l2 = 0L;
      l2 = 4927124146702118258L ^ (long)"캗唏옑䕁\ue718冈﵅屖↤豰䭠㞄ㄇ\uda07减\uf016㹳肾䉄\uee94ⵟ\uf4f6".hashCode();

      outerLoop:
      while (true) {
         if (b <= 9) {
            if (shadedBigDecimal2.intCompact == -9223372036854775808L) {
               l2 = l2 - -8293854933952890423L ^ -787784536674693771L;
               continue;
            }

            l2 = l2 - 315953088874048602L ^ -3974289718504471261L;
         } else if (b >= 14) {
            if (14 >= b) {
               n3 = shadedBigDecimal2.precision();
               l2 = l2 + 8907793021212748902L ^ -1974225340199653235L;
            } else {
               if (b >= 17) {
                  if (b <= 17) {
                     return divide3(shadedBigDecimal2.intCompact, n3, shadedBigDecimal.intCompact, n, l, shadedMathContext);
                  }

                  if (b > 18) {
                     CustomBigInteger intVal2 = shadedBigDecimal.intVal;
                     return divide6(shadedBigDecimal2.intVal, n3, intVal2, n, l, shadedMathContext);
                  }

                  n2 = shadedMathContext.precision;
                  l2 = (l2 ^ 9109256350404864487L) + 1297668391487036813L;
                  continue;
               }

               if (15 < b) {
                  throw new ArithmeticException("Division undefined");
               }

               l = (long)shadedBigDecimal2.scale - (long)shadedBigDecimal.scale;
               l2 = l2 - -1350522079393583498L ^ 1879523393368001016L;
            }
         } else if (11 <= b) {
            if (11 >= b) {
               if (shadedBigDecimal.intCompact == -9223372036854775808L) {
                  l2 = l2 + 3186602775430648221L ^ -2961184899985546970L;
                  continue;
               }

               l2 = (l2 ^ 5441280334981190524L) - 3736749172395712156L;
            } else {
               if (12 >= b) {
                  return zeroValueOf(saturateLong(l));
               }

               if (shadedBigDecimal2.signum() == 0) {
                  l2 = l2 - 1649176895397232396L ^ -3346130762810186562L;
                  continue;
               }

               l2 = (l2 ^ -6813350606406681179L) + 4307237418339529344L;
            }
         } else if (n2 != 0) {
            l2 = (l2 ^ 8472611432314447028L) + -3100892426710278403L;
         } else {
            l2 = l2 - -5279244319502069731L ^ 1881568577481078069L;
         }

         while (true) {
            while (b < 4) {
               if (1 > b) {
                  if (shadedBigDecimal2.signum() != 0) {
                     l2 = l2 + -1370766167691114220L ^ 4877590751424527616L;
                  } else {
                     l2 = l2 - 9070196709549467937L ^ 6426455631321634657L;
                  }
                  continue outerLoop;
               }

               if (b > 1) {
                  if (b <= 2) {
                     return divide(shadedBigDecimal2.intCompact, n3, shadedBigDecimal.intVal, n, l, shadedMathContext);
                  }

                  return this.divide(shadedBigDecimal);
               }

               if (shadedBigDecimal.intCompact != -9223372036854775808L) {
                  l2 = l2 - -5395939354299370423L ^ -4919313238494959287L;
                  continue outerLoop;
               }

               l2 = l2 + -9146646226866364711L ^ 6372903263117304634L;
            }

            if (4 >= b) {
               long intCompact2 = shadedBigDecimal.intCompact;
               return divide8(shadedBigDecimal2.intVal, n3, intCompact2, n, l, shadedMathContext);
            }

            if (b < 6) {
               n = shadedBigDecimal.precision();
               l2 = (l2 ^ 7565137755802773740L) + 6438120032847914501L;
               break;
            }

            if (6 >= b) {
               shadedBigDecimal2 = this;
               l2 = l2 - 8224302471129181506L ^ -3143671275994743801L;
               break;
            }

            if (b <= 7) {
               throw new ArithmeticException("Division by zero");
            }

            if (shadedBigDecimal.signum() == 0) {
               l2 = l2 + -981920582915518722L ^ 7334005185637003577L;
               break;
            }

            l2 = l2 - -5209931540445745083L ^ -1989323181909089531L;
         }
      }
   }

   private static boolean commonNeedIncrement(int n, int n2, int n3, boolean flag) {
      int n4 = 0;
      byte b = 4;

      outerLoop:
      while (true) {
         exitBlock: {
            if (b <= 7) {
               if (n4 == 0) {
                  continue;
               }
            } else if (b >= 11) {
               if (b <= 11) {
                  throw new ArithmeticException("Rounding necessary");
               }

               if (b >= 13) {
                  if (b > 13) {
                     return true;
                  }

                  return true;
               }

               if (n4 == 1) {
                  continue;
               }
            } else {
               if (b < 9) {
                  n4 = n;
                  continue;
               }

               if (9 < b) {
                  return false;
               }

               if (n4 == 6) {
                  break exitBlock;
               }
            }

            while (true) {
               while (b < 3) {
                  if (1 <= b) {
                     if (1 < b) {
                        if (n2 > 0) {
                           continue outerLoop;
                        }

                        return false;
                     }

                     if (n4 == 7) {
                        continue outerLoop;
                     }
                  } else if (n4 == 3) {
                     return n2 < 0;
                  }
               }

               if (b <= 3) {
                  if (n4 != 4) {
                     continue outerLoop;
                  }
                  break;
               }

               if (b < 5) {
                  break;
               }

               if (b <= 5) {
                  if (n4 == 5) {
                     break;
                  }
               } else if (n4 != 2) {
                  continue outerLoop;
               }
            }
         }

         if (assertionsDisabled || n >= 4 && n <= 6) {
            if (n3 < 0) {
               return false;
            }

            if (n3 > 0) {
               return true;
            }

            if (!assertionsDisabled && n3 != 0) {
               throw new AssertionError();
            }

            if (n != 4) {
               if (n != 6) {
                  if (n != 5) {
                     throw new AssertionError("Unexpected rounding mode" + n);
                  }

                  return false;
               }

               return flag;
            }

            return true;
         }

         throw new AssertionError("Unexpected rounding mode" + CustomRoundingMode.valueOf2(n));
      }
   }

   public ShadedBigDecimal add(ShadedBigDecimal shadedBigDecimal) {
      byte b = 4;
      long l = 0L;
      l = -7798792888791929766L ^ (long)"弄읊\udfa3껣磑♛⋆ᙶݜ稐ᑯ❬ꬰ㬏㫣᳧ŕꝅ淅\ueef4ಖ䥗".hashCode();

      while (true) {
         if (3 >= b) {
            if (shadedBigDecimal.intCompact == -9223372036854775808L) {
               l = l - -2913207653837858473L ^ 6060479157785714495L;
            } else {
               l = l + -4028434561512001043L ^ -942456204084775914L;
            }
            break;
         }

         if (5 <= b) {
            if (b > 5) {
               int scale2 = shadedBigDecimal.scale;
               CustomBigInteger intVal2 = shadedBigDecimal.intVal;
               int scale3 = this.scale;
               return add3(this.intVal, scale3, intVal2, scale2);
            }

            if (this.intCompact == -9223372036854775808L) {
               l = (l ^ -8028210309678100166L) + 4886498704038838588L;
            } else {
               l = l - -612723153052713415L ^ 5310883385944230027L;
            }
         } else {
            if (shadedBigDecimal.intCompact != -9223372036854775808L) {
               l = l - 3622987870262467504L ^ 5879218462564056911L;
               break;
            }

            l = l - 4673978001459722705L ^ -5989809383901476066L;
         }
      }

      if (1 <= b) {
         if (b <= 1) {
            int scale4 = shadedBigDecimal.scale;
            return add2(this.intCompact, this.scale, shadedBigDecimal.intVal, scale4);
         } else {
            int scale5 = this.scale;
            return add2(shadedBigDecimal.intCompact, shadedBigDecimal.scale, this.intVal, scale5);
         }
      } else {
         return add(this.intCompact, this.scale, shadedBigDecimal.intCompact, shadedBigDecimal.scale);
      }
   }

   private static ShadedBigDecimal divide8(CustomBigInteger customBigInteger, int n, long l, int n2, long l2, ShadedMathContext shadedMathContext) {
      CustomBigInteger customBigInteger2 = null;
      int n3 = 0;
      int n4 = 0;
      ShadedBigDecimal shadedBigDecimal = null;
      int n5 = 0;
      CustomBigInteger customBigInteger3 = null;
      int n6 = 0;
      int n7 = 0;
      long l3 = 0L;
      byte b = 17;
      long l4 = 0L;
      l4 = -7841480672906865652L ^ (long)"뤭棠\uf233\uf1c1柂\uda36롣\u0cc9뵾\ue0aa櫧\uda16\ue0ce\uf45b่\ue306遢ᚍ俳ꖴ㠸霣럨ᐱ\uec07ᒙ\uf6c3\u17ea".hashCode();

      outerLoop:
      while (b > 10) {
         if (15 <= b) {
            if (15 >= b) {
               l4 = (l4 ^ -6910750431523230015L) - 5547869517952574162L;
               continue;
            }

            if (18 <= b) {
               if (b <= 18) {
                  if (-compareMagnitudeNormalized(l, n2, customBigInteger, n) <= 0) {
                     l4 = (l4 ^ -381996739151499029L) - -1617983129325599759L;
                     continue;
                  }

                  long l5 = l4 ^ 3117912107544050374L;
                  short s = 32035;
                  l4 = l5 - 630293965433293903L;
               } else {
                  if (b > 19) {
                     shadedBigDecimal = divideAndRound2(customBigInteger, l3, n3, n5, checkScaleNonZero(l2));
                     l4 = (l4 ^ 3600228780316918447L) - -3665097981159587185L;
                     continue;
                  }

                  if ((l3 = longMultiplyPowerTen(l, n6)) != -9223372036854775808L) {
                     l4 = (l4 ^ -7933912141848370806L) + 9158432082784868615L;
                     continue;
                  }

                  l4 = (l4 ^ -6666385242327387170L) + 2321236810425012259L;
               }
            } else {
               if (16 < b) {
                  l4 = (l4 ^ 2700671658203529300L) - 8485137850399897530L;
                  continue;
               }

               if (checkScaleNonZero((long)n4 + (long)n2 - (long)n) <= 0) {
                  l4 = l4 - 1046516501715756310L ^ 1568293053882269733L;
               } else {
                  l4 = (l4 ^ 7588887062757004185L) + 670498134644427180L;
               }
            }
         } else if (12 <= b) {
            if (12 >= b) {
               shadedBigDecimal = divideAndRound3(customBigInteger, customBigInteger2, n3, n5, checkScaleNonZero(l2));
               l4 = (l4 ^ 7764452476933360328L) + -2481511956487017319L;
               continue;
            }

            if (13 < b) {
               n3 = checkScaleNonZero(l2 + (long)n2 - (long)n + (long)n4);
               l4 = l4 + -3751290552452915114L ^ -4149664710402530481L;
               continue;
            }

            if (n7 != n2) {
               l4 = (l4 ^ 6239015794637337211L) + 2431603367677140919L;
            } else {
               l4 = (l4 ^ -5537578050458499216L) - -3867650821734860726L;
            }
         } else {
            n4 = shadedMathContext.precision;
            l4 = l4 - -5035221939758621970L ^ -5378165240593542670L;
         }

         while (true) {
            while (b < 4) {
               if (b >= 1) {
                  if (1 >= b) {
                     n7 = checkScaleNonZero((long)n - (long)n4);
                     l4 = (l4 ^ 1851929524772324657L) - -3372298752349129011L;
                     continue outerLoop;
                  }

                  if (b > 2) {
                     shadedBigDecimal = divideAndRound2(customBigInteger3, l, n3, n5, checkScaleNonZero(l2));
                     l4 = l4 + 2089479033118558807L ^ 230689758497535280L;
                     continue outerLoop;
                  }

                  customBigInteger3 = bigMultiplyPowerTen2(customBigInteger, n7);
                  l4 = l4 - -1903662106629590545L ^ -4726899936627579046L;
               } else {
                  shadedBigDecimal = divideAndRound2(customBigInteger, l, n3, n5, checkScaleNonZero(l2));
                  l4 = l4 + -7838309556647293859L ^ 5456440390294581074L;
               }
            }

            if (4 >= b) {
               n6 = checkScaleNonZero((long)n7 - (long)n2);
               l4 = l4 + 2750501636752525766L ^ 8859679246649561373L;
               break;
            }

            if (b < 7) {
               if (b <= 5) {
                  customBigInteger2 = bigMultiplyPowerTen(l, n6);
                  l4 = (l4 ^ 271597373996242936L) - 813427167507613911L;
               } else {
                  n5 = shadedMathContext.roundingMode.oldMode;
                  l4 = l4 + 6754641856621143137L ^ -4506915967422280593L;
               }
               break;
            }

            if (7 >= b) {
               n2--;
               l4 = (l4 ^ -1463024181227222794L) - -4321329368923523861L;
               break;
            }

            if (8 >= b) {
               l4 = l4 + -575075127460557071L ^ -2564522379540473864L;
               break;
            }

            n7 = checkScaleNonZero((long)n4 + (long)n2 - (long)n);
            l4 = l4 - 8737549302556448503L ^ -786178036174890576L;
         }
      }

      return doRound(shadedBigDecimal, shadedMathContext);
   }

   public ShadedBigDecimal plus(ShadedMathContext shadedMathContext) {
      return this.round(shadedMathContext);
   }

   @Override
   public int intValue() {
      long l = 0L;
      l = -6731990366617168951L ^ (long)"ᒥ腯ℾ鉠쟫뵟涒㽹០䥖⣿䚁㛊㞭౻ᖹ蟰콓鿼骒\uf18cপ⩜檛".hashCode();
      if (this.intCompact == -9223372036854775808L) {
         l = (l ^ 7125769295943140615L) - 4503560137872062689L;
      } else {
         l = l - 4639075022339599075L ^ -8123976227834412579L;
         if (this.scale == 0) {
            l = (l ^ 3716463058247145955L) - 6697537269930054411L;
            return (int)this.intCompact;
         }

         l = (l ^ -4929457422166661924L) - -3101318197656687098L;
      }

      return (int)this.longValue();
   }

   private static long make64(long l, long l2) {
      return l << 32 | l2;
   }

   private static int compareMagnitudeNormalized3(long l, int n, long l2, int n2) {
      int n3 = 0;
      byte b = 2;
      long l3 = 0L;
      l3 = 4533571628604779388L ^ (long)"栣㛫攘鉮鼺哩怖Ị툝订믂\u0005ញឦ㜜䫔쐤䗯愮露ᖦ㪖㔎࣊뷏\ud92a靈".hashCode();

      int n4;
      while (true) {
         if (4 >= b) {
            n4 = longCompareMagnitude(l, l2);
            break;
         }

         if (6 <= b) {
            if (6 >= b) {
               n3 = n - n2;
               l3 = (l3 ^ -1465556025760469164L) - -4955042024895239337L;
            } else {
               if (7 >= b) {
                  if (l == -9223372036854775808L) {
                     l3 = (l3 ^ 3622893110853917171L) - -8070314082690944509L;
                     return 1;
                  }

                  l3 = (l3 ^ -3208792619486366926L) + -3915631796631689549L;
                  if (l2 == -9223372036854775808L) {
                     l3 = (l3 ^ 7356854799928725313L) + -2070309290018221492L;
                     n4 = -1;
                     break;
                  }

                  l3 = l3 + 3456414501404599976L ^ 8907612775222351741L;
                  continue;
               }

               l = longMultiplyPowerTen(l, -n3);
               l3 = (l3 ^ 1105144059879580044L) - 5991535370157644883L;
            }
         } else {
            if (n3 < 0) {
               l3 = (l3 ^ -1798533119023534647L) - -7382666028644661948L;
               continue;
            }

            l3 = (l3 ^ 626969432114972588L) + 315320027950166472L;
         }

         if (b <= 1) {
            l2 = longMultiplyPowerTen(l2, n3);
            l3 = (l3 ^ -7813618560437840902L) + 5646377289350354259L;
         } else if (2 >= b) {
            if (n3 == 0) {
               l3 = (l3 ^ -7641800262787075290L) - 4976974551938001376L;
            } else {
               l3 = l3 - -2219300468237870140L ^ 6546197003261528529L;
            }
         } else {
            l3 = l3 + 2388103168414380925L ^ -520771547325311536L;
         }
      }

      return n4;
   }

   private static CustomBigInteger divideAndRound(CustomBigInteger customBigInteger, long l, int n) {
      long l2 = 0L;
      int n2 = 0;
      boolean flag = false;
      ShadedMutableBigInteger shadedMutableBigInteger = null;
      ShadedMutableBigInteger shadedMutableBigInteger2 = null;
      byte b = 3;
      long l3 = 0L;
      l3 = 5282037793932805940L ^ (long)"肑\uf7e4娓冈ဴᒝ\uf502糫ｫᙳ淴橶膉랪檲ḅᛩᬐ嶳눖漼봩摌Ҥ燚".hashCode();

      boolean flag2;
      outerLoop:
      while (true) {
         if (b <= 3) {
            if (l2 != 0L) {
               l3 = (l3 ^ 3937934131318800333L) - 8948595524721486037L;
               flag2 = false;
               break;
            }

            l3 = l3 + -4633321739261854560L ^ 8267145383550587824L;
         } else {
            if (b < 5) {
               flag2 = true;
               break;
            }

            if (5 >= b) {
               shadedMutableBigInteger2 = new ShadedMutableBigInteger();
               l3 = l3 + -4189911395371250231L ^ 5775774502888769584L;
            } else {
               l2 = 0L;
               l3 = (l3 ^ 8611522343708102000L) - -3851372922929115941L;
            }

            while (1 <= b) {
               if (1 < b) {
                  shadedMutableBigInteger = new ShadedMutableBigInteger(customBigInteger.mag);
                  l3 = (l3 ^ -5449460709100988422L) - -585497651680470172L;
                  continue outerLoop;
               }

               shadedMutableBigInteger2 = null;
               l3 = (l3 ^ -2164135794927010383L) + -6327941620756097959L;
            }

            l2 = shadedMutableBigInteger.divide(l, shadedMutableBigInteger2);
            l3 = l3 + -8060106705910046533L ^ -7999957544237883590L;
         }
      }

      flag = flag2;
      n2 = l < 0L ? -customBigInteger.signum : customBigInteger.signum;
      if (!flag && needIncrement(l, n, n2, shadedMutableBigInteger2, l2)) {
         shadedMutableBigInteger2.add(ShadedMutableBigInteger.ONE);
      }

      return shadedMutableBigInteger2.toBigInteger(n2);
   }

   private static ShadedBigDecimal doRound128(long l, long l2, int n, int n2, ShadedMathContext shadedMathContext) {
      ShadedBigDecimal shadedBigDecimal = null;
      int n3 = 0;
      int n4 = 0;
      byte b = 1;
      long l3 = 0L;
      l3 = -490999574537811801L ^ (long)"칠羠\ue7e7켱ẞᎶ廳辏㚵晁倕Ὧ螾뗿暌셡\ue5de鬾崫ểࡇ".hashCode();
      n4 = shadedMathContext.precision;
      l3 = (l3 ^ -3442986426731891712L) + 5229787176141049882L;

      outerLoop:
      while (b > 4) {
         if (6 <= b) {
            if (6 < b) {
               if (b <= 7) {
                  shadedBigDecimal = divideAndRound128(l, l2, LONG_TEN_POWERS_TABLE[n3], n, n2, shadedMathContext.roundingMode.oldMode, n2);
                  l3 = l3 - -3936394054579375998L ^ 8701078896685229791L;
               } else {
                  n2 = checkScaleNonZero((long)n2 - (long)n3);
                  l3 = l3 - -5429516866665538520L ^ -1488200317340178272L;
               }
               continue;
            }

            shadedBigDecimal = null;
            l3 = (l3 ^ -5492851228172271599L) + -6091175125707567429L;
         } else {
            if (shadedBigDecimal != null) {
               l3 = (l3 ^ -3116157092691787579L) - -8427676788284748072L;
               continue;
            }

            l3 = (l3 ^ -7772075439650883059L) + 8207518192491053224L;
         }

         while (b > 1) {
            if (b > 2) {
               return null;
            }

            if ((n3 = precision(l, l2) - n4) <= 0) {
               l3 = (l3 ^ 4513978398346988949L) - -890190473210032156L;
               continue outerLoop;
            }

            l3 = l3 - -3078764619895704571L ^ -593711321118069032L;
         }

         if (n3 >= LONG_TEN_POWERS_TABLE.length) {
            l3 = l3 + -220176616588538190L ^ 3733312518552387156L;
         } else {
            l3 = l3 + 167736035256997233L ^ -6828451840671672516L;
         }
      }

      return doRound(shadedBigDecimal, shadedMathContext);
   }

   @Override
   public boolean equals(Object obj) {
      Object obj2 = null;
      long l = 0L;
      long l2 = 0L;
      long l3 = 0L;
      l3 = 6430632460395642007L ^ (long)"ꆼ쫩\ue95dⷍ퓸必菩ન鐖錸톒圹ꛐ茎㔼躤㠅儭뿽䌷ꠌ镹狏輡䜆".hashCode();
      if (obj instanceof ShadedBigDecimal) {
         l3 = (l3 ^ 4169292026684204205L) - -8980109764146261006L;
         obj2 = (ShadedBigDecimal)obj;
         l3 = l3 + -4028335348125793436L ^ 8822379302295642159L;
         if (obj != this) {
            l3 = l3 - -5450398540793745615L ^ -3334656775671735928L;
            if (this.scale == ((ShadedBigDecimal)obj2).scale) {
               l3 = l3 - -2150401891005422678L ^ 4814069946225475281L;
               l2 = this.intCompact;
               l3 = l3 + 6449249111271249846L ^ 2577434063196155559L;
               l = ((ShadedBigDecimal)obj2).intCompact;
               l3 = l3 + 5937229110079780128L ^ 8025658598978903471L;
               if (l2 == -9223372036854775808L) {
                  l3 = l3 + -2813810713794029779L ^ -3555579725122605854L;
                  if (l != -9223372036854775808L) {
                     return l == compactValFor(this.intVal);
                  } else {
                     return this.inflated().equals(((ShadedBigDecimal)obj2).inflated());
                  }
               } else {
                  l3 = l3 + -6727957025883883914L ^ -5392912268004256844L;
                  if (l != -9223372036854775808L) {
                     l3 = (l3 ^ -2605182905091697290L) + -581488779459619480L;
                  } else {
                     l3 = (l3 ^ 864978657718991013L) + 8870999511402319254L;
                     l = compactValFor(((ShadedBigDecimal)obj2).intVal);
                     l3 = l3 - 3338249879337975955L ^ 193270947351143286L;
                  }

                  boolean flag;
                  if (l != l2) {
                     l3 = l3 - -4736739084852994578L ^ 3481736631323545697L;
                     flag = false;
                  } else {
                     l3 = (l3 ^ 761776952960194316L) - -575700026850926847L;
                     flag = true;
                  }

                  return flag;
               }
            } else {
               l3 = l3 + 2129905103192667708L ^ 984505563816080251L;
               return false;
            }
         } else {
            l3 = l3 + -2656497170086606997L ^ -9155555927796547109L;
            return true;
         }
      } else {
         l3 = l3 - -608475342172242820L ^ -7824425804619189193L;
         return false;
      }
   }

   private static ShadedBigDecimal add2(long l, int n, CustomBigInteger customBigInteger, int n2) {
      long l2 = 0L;
      int n3 = 0;
      Object obj = null;
      long l3 = 0L;
      boolean flag = false;
      int n4 = 0;
      byte b = 1;
      long l4 = 0L;
      l4 = 8498860236473603233L ^ (long)"뀇罛\ua9ce⌢钄櫺\ue919弚⢷&蜵ƙΪ뼮被ꝸእѤ얡\uddca⛠".hashCode();

      boolean flag2;
      while (true) {
         while (1 >= b) {
            n3 = n;
            l4 = (l4 ^ -4050198342279422699L) + -2084285082559972889L;
         }

         if (b <= 2) {
            flag2 = true;
            break;
         }

         l3 = (long)n3 - (long)n2;
         l4 = l4 + 5505326667442695996L ^ 1261570563090189473L;
         if (Long.signum(l) != customBigInteger.signum) {
            l4 = (l4 ^ -4323091227684798327L) - 2442294142289131997L;
            flag2 = false;
            break;
         }

         l4 = l4 - -5921354782527101876L ^ 4049033505310161430L;
      }

      flag = flag2;
      if (l3 < 0L) {
         n4 = checkScale2(l, -l3);
         n3 = n2;
         l2 = longMultiplyPowerTen(l, n4);
         if (l2 == -9223372036854775808L) {
            obj = customBigInteger.add2(bigMultiplyPowerTen(l, n4));
         } else {
            obj = customBigInteger.add(l2);
         }
      } else {
         n4 = checkScale(customBigInteger, l3);
         customBigInteger = bigMultiplyPowerTen2(customBigInteger, n4);
         obj = customBigInteger.add(l);
      }

      ShadedBigDecimal shadedBigDecimal;
      if (flag) {
         shadedBigDecimal = new ShadedBigDecimal((CustomBigInteger)obj, -9223372036854775808L, n3, 0);
      } else {
         shadedBigDecimal = valueOf((CustomBigInteger)obj, n3, (byte)0);
      }

      return shadedBigDecimal;
   }

   public ShadedBigDecimal(long l, ShadedMathContext shadedMathContext) {
       super();
      int precision2 = shadedMathContext.precision;
      int oldMode = shadedMathContext.roundingMode.oldMode;
      int n = 0;
      int n2 = 0;
      CustomBigInteger customBigInteger = l == -9223372036854775808L ? INFLATED_BIGINT : null;
      if (precision2 > 0) {
         if (l == -9223372036854775808L) {
            n = 19;

            for (int i = n - precision2; i > 0; i = n - precision2) {
               n2 = checkScaleNonZero((long)n2 - (long)i);
               customBigInteger = divideAndRoundByTenPow(customBigInteger, i, oldMode);
               l = compactValFor(customBigInteger);
               if (l != -9223372036854775808L) {
                  break;
               }

               n = bigDigitLength(customBigInteger);
            }
         }

         if (l != -9223372036854775808L) {
            n = longDigitLength(l);

            for (int j = n - precision2; j > 0; j = n - precision2) {
               n2 = checkScaleNonZero((long)n2 - (long)j);
               l = divideAndRound3(l, LONG_TEN_POWERS_TABLE[j], shadedMathContext.roundingMode.oldMode);
               n = longDigitLength(l);
            }

            customBigInteger = null;
         }
      }

      this.intVal = customBigInteger;
      this.intCompact = l;
      this.scale = n2;
      this.precision = n;
   }

}
