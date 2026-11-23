package net.augustus.vendor.crypto.math;



import net.augustus.vendor.crypto.CustomBigInteger;
import net.augustus.vendor.crypto.VendorAesHelper;
public class BigRationalFraction {
   private CustomBigInteger denominator;
   private CustomBigInteger numerator;
   public static final BigRationalFraction ONE;
   public static final BigRationalFraction ZERO;
   public static final BigRationalFraction TWO;
   private static ShadedMathContext defaultMathContext;

   public BigRationalFraction(CustomBigInteger customBigInteger, CustomBigInteger customBigInteger2) {
      this.numerator = customBigInteger;
      this.denominator = customBigInteger2;
      CustomBigInteger zero = CustomBigInteger.ZERO;
      if (customBigInteger2.equals(zero)) {
         throw new ArithmeticException("Created impossible fraction");
      }
   }

   public BigRationalFraction simplify() {
      Object obj = null;
      int n = 0;
      long l = 6987769132312373558L;
      long l2 = 0L;
      int invertedGuardFlag = VendorAesHelper.getInvertedGuardFlag();
      l = 7606328032337474119L ^ (long)"ꇲ馗帎⛌鍬푑\ue4ad옯奔탾੧ោ\ude33᪒먜ៜ꾃곬鏛䃁\ufdd2".hashCode();
      l2 = 311774169555011632L ^ (long)"踇겔뢉ꏩ樳신\ua7ce毌ꠓࣸ\ue1ce⥺볣ঞ叵ੂ㢎蒳\uf239槏뀨\udc37ᰩ榦ꥷ鄫夐\uf24e".hashCode();
      int n2 = invertedGuardFlag;

      outerLoop:
      while (true) {
         n = (int)(l & 3L);
         invertedGuardFlag = 1;
         int n3 = n;

         exitBlock: {
            while (true) {
               if (invertedGuardFlag <= n3) {
                  invertedGuardFlag = n;
                  n3 = 1;
                  if (n2 == 0) {
                     continue;
                  }

                  if (n2 == 0) {
                     break;
                  }

                  if (n <= 1) {
                     return new BigRationalFraction((net.augustus.vendor.crypto.CustomBigInteger)null, (net.augustus.vendor.crypto.CustomBigInteger)null);
                  }
               } else {
                  obj = null;
                  l = (l ^ 0L) + 0L;
                  l2 = l2 + 0L ^ 0L;
                  if (n2 != 0) {
                     continue outerLoop;
                  }
               }

               invertedGuardFlag = n;
               if (n2 == 0) {
                  break exitBlock;
               }

               n3 = 2;
               break;
            }

            if (invertedGuardFlag > n3) {
               break;
            }

            invertedGuardFlag = 0;
         }

         if (invertedGuardFlag == 0) {
            l = l + 0L ^ 0L;
            l2 = (l2 ^ 0L) + 0L;
            if (n2 != 0) {
               continue;
            }
         }

         l = l - 0L ^ 0L;
         l2 = l2 - 0L ^ 0L;
         if (n2 == 0) {
            break;
         }
      }

      return this;
   }

   public BigRationalFraction(int n) {
      this(String.valueOf(n), "1");
   }

   public String toDecimalString(ShadedMathContext shadedMathContext) {
      ShadedBigDecimal shadedBigDecimal = new ShadedBigDecimal(this.numerator);
      ShadedBigDecimal shadedBigDecimal2 = new ShadedBigDecimal(this.denominator);
      return shadedBigDecimal.divide(shadedBigDecimal2, shadedMathContext).toPlainString();
   }

   public BigRationalFraction(long l, long l2) {
      this(String.valueOf(l), String.valueOf(l2));
   }

   public BigRationalFraction addInt(int n) {
      CustomBigInteger numerator2 = this.numerator;
      CustomBigInteger denominator2 = this.denominator;
      CustomBigInteger customBigInteger = CustomBigInteger.valueOf((long)n);
      CustomBigInteger customBigInteger2 = denominator2.multiply2(customBigInteger);
      return (new BigRationalFraction(numerator2.add2(customBigInteger2), this.denominator)).simplify();
   }

   public BigRationalFraction add(BigRationalFraction bigRationalFraction) {
      Object obj = null;
      int invertedGuardFlag = VendorAesHelper.getInvertedGuardFlag();
      Object obj2 = null;
      Object obj3 = null;
      int n = 2;
      long l = 4353067807764738576L;
      int n2 = invertedGuardFlag;
      long l2 = 0L;
      l = 5044938336779270770L ^ (long)"丂上\uec4c嵇꽌甆縯㠕墍⟏㾋엽\ue9e1ܫ쯺\uf7ef᯿鵑\ue269ゆ瞐ⵙㅜ雘⦍瓢".hashCode();
      l2 = -6253494216657085695L ^ (long)"Ṧ궷ⓑﭟ覬贱ቜৣἭ醭ꮍะŪ餑颭砜\uf548뷛삇쥗舊豵受垍鰂‷兕".hashCode();

      while (true) {
         int n3;
         outerLoop:
         while (true) {
            n = (int)(l & 3L);
            invertedGuardFlag = 1;
            n3 = n;

            while (true) {
               if (invertedGuardFlag <= n3) {
                  invertedGuardFlag = 1;
                  n3 = n;
                  if (n2 == 0) {
                     continue;
                  }

                  if (n2 == 0) {
                     break outerLoop;
                  }

                  if (1 < n) {
                     break;
                  }

                  obj3 = null;
                  l = (l ^ 0L) + 0L;
                  l2 = l2 - 0L ^ 0L;
                  if (n2 != 0) {
                     continue outerLoop;
                  }
               }

               obj = null;
               l = (l ^ 0L) - 0L;
               l2 = (l2 ^ 0L) - 0L;
               if (n2 != 0) {
                  continue outerLoop;
               }
               break;
            }

            invertedGuardFlag = n;
            n3 = 2;
            break;
         }

         if (invertedGuardFlag <= n3) {
            new BigRationalFraction((net.augustus.vendor.crypto.CustomBigInteger)null, (CustomBigInteger)obj);
            return null;
         }

         obj2 = null;
         l = (l ^ 0L) - 0L;
         l2 = (l2 ^ 0L) + 0L;
      }
   }

   public String toFractionString() {
      return this.numerator + "/" + this.denominator;
   }

   public int compareTo(BigRationalFraction other) {
      Object obj = null;
      int guardFlag = VendorAesHelper.getGuardFlag();
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      Object obj5 = null;
      int n = 2;
      long l = 8654715442330579846L;
      long l2 = 0L;
      l = -1804287250478851374L ^ (long)"謺터홽겧鲢꩐鶖錇╠\u0bceꬱ⃝ᣏ땆\ue71a쓍谘筙া暷腠⸀柜勇뱽".hashCode();
      int n2 = guardFlag;
      l2 = -7163603003910790527L ^ (long)"䯦⦿伓볞፻⊟㰧샗텮Ⱉ㯷䲥㍥ࡒ౮麊⚯慮去\ue902묊光㚐ᯀȉ\uf519ᷠﲱ샣".hashCode();

      while (true) {
         int n3;
         exitBlock: {
            outerLoop:
            do {
               while (true) {
                  n = (int)(l % 6L);
                  if (2 <= n) {
                     guardFlag = 2;
                     n3 = n;
                     if (n2 != 0) {
                        break;
                     }

                     if (n2 != 0) {
                        break exitBlock;
                     }

                     if (2 < n) {
                        break outerLoop;
                     }

                     obj3 = null;
                     l = l - 0L ^ 0L;
                     l2 = l2 - 0L ^ 0L;
                     if (n2 == 0) {
                        continue;
                     }
                  }

                  guardFlag = n;
                  if (n2 != 0) {
                     return n;
                  }

                  n3 = 0;
                  break;
               }

               if (guardFlag <= n3) {
                  return 0;
               }

               obj = null;
               l = l + 0L ^ 0L;
               l2 = (l2 ^ 0L) + 0L;
            } while (n2 == 0);

            guardFlag = n;
            n3 = 4;
         }

         exitBlock2: {
            exitBlock3: {
               if (n2 == 0) {
                  if (guardFlag < n3) {
                     break exitBlock3;
                  }

                  guardFlag = 4;
                  n3 = n;
               }

               if (guardFlag < n3) {
                  break exitBlock2;
               }

               obj2 = null;
               l = (l ^ 0L) - 0L;
               l2 = (l2 ^ 0L) - 0L;
               if (n2 == 0) {
                  continue;
               }
            }

            obj4 = null;
            l = l + 0L ^ 0L;
            l2 = l2 - 0L ^ 0L;
            if (n2 == 0) {
               continue;
            }
         }

         obj5 = null;
         l = (l ^ 0L) + 0L;
         l2 = l2 - 0L ^ 0L;
      }
   }

   public ShadedBigDecimal toBigDecimal(ShadedMathContext shadedMathContext) {
      ShadedBigDecimal shadedBigDecimal = new ShadedBigDecimal(this.numerator);
      ShadedBigDecimal shadedBigDecimal2 = new ShadedBigDecimal(this.denominator);
      return shadedBigDecimal.divide(shadedBigDecimal2, shadedMathContext);
   }

   public BigRationalFraction multiply(BigRationalFraction bigRationalFraction) {
      CustomBigInteger numerator2 = bigRationalFraction.getNumerator();
      CustomBigInteger numerator3 = this.numerator;
      CustomBigInteger customBigInteger = numerator2.multiply2(numerator3);
      CustomBigInteger denominator2 = bigRationalFraction.getDenominator();
      CustomBigInteger denominator3 = this.denominator;
      return (new BigRationalFraction(customBigInteger, denominator2.multiply2(denominator3))).simplify();
   }

   static {
      ZERO = new BigRationalFraction(0);
      ONE = new BigRationalFraction(1);
      TWO = new BigRationalFraction(2);
      defaultMathContext = new ShadedMathContext(10, CustomRoundingMode.HALF_UP);
   }

   public BigRationalFraction subtractInt(int n) {
      CustomBigInteger denominator2 = this.denominator;
      CustomBigInteger customBigInteger = CustomBigInteger.valueOf((long)n);
      CustomBigInteger customBigInteger2 = denominator2.multiply2(customBigInteger);
      return (new BigRationalFraction(this.numerator.subtract(customBigInteger2), this.denominator)).simplify();
   }

   public static CustomBigInteger lcm(CustomBigInteger customBigInteger, CustomBigInteger customBigInteger2) {
      CustomBigInteger customBigInteger3 = customBigInteger.multiply2(customBigInteger2);
      CustomBigInteger customBigInteger4 = customBigInteger.gcd(customBigInteger2);
      return customBigInteger3.divide(customBigInteger4);
   }

   public ShadedBigDecimal toBigDecimal() {
      ShadedMathContext shadedMathContext = defaultMathContext;
      return this.toBigDecimal(shadedMathContext);
   }

   public BigRationalFraction abs() {
      return new BigRationalFraction(this.numerator.abs(), this.denominator.abs());
   }

   public CustomBigInteger getDenominator() {
      return this.denominator;
   }

   public boolean equalsFraction(BigRationalFraction bigRationalFraction, long l) {
      int n = 1;
      int invertedGuardFlag = VendorAesHelper.getInvertedGuardFlag();
      long l2 = -8532645815427904403L;
      int n2 = invertedGuardFlag;
      long l3 = 0L;
      l2 = 8892194170721628877L ^ (long)"攥퍘\uddac脄ல縖䄷枍㒺핵쿝氛紺\uf898깔ⵍ鰾픢嬅\ue5c6㲉괋䱑築態縡섲".hashCode();
      l3 = -3870522888286263589L ^ (long)"\ue443꺤\uea45ᖻ葼撽䰫䜪騴쑐垂ஶꨄ뜈휡꺟쮇⏚ř\ue336蕳\ue5caﯿ範㺸ﲶ".hashCode();

      while (true) {
         n = (int)(l2 % 3L);
         if (1 > n) {
            break;
         }

         invertedGuardFlag = n;
         if (n2 == 0) {
            return (n != 0);
         }

         if (n2 != 0) {
            if (n <= 1) {
               if (true) {
                  l2 = (l2 ^ 0L) + 0L;
                  l3 = l3 + 0L ^ 0L;
                  if (n2 != 0) {
                     return false;
                  }
               }

               l2 = l2 + 0L ^ 0L;
               l3 = l3 - 0L ^ 0L;
               if (n2 != 0) {
                  continue;
               }
               break;
            }

            invertedGuardFlag = 0;
         }

         if (invertedGuardFlag == 0) {
            l2 = (l2 ^ 0L) - 0L;
            l3 = l3 + 0L ^ 0L;
            if (n2 != 0) {
               return false;
            }
         }

         l2 = (l2 ^ 0L) - 0L;
         l3 = l3 - 0L ^ 0L;
      }

      return true;
   }

   public BigRationalFraction subtract(BigRationalFraction bigRationalFraction) {
      Object obj = null;
      Object obj2 = null;
      int guardFlag = VendorAesHelper.getGuardFlag();
      Object obj3 = null;
      int n = guardFlag;
      int n2 = 3;
      long l = 86128916981478809L;
      long l2 = 0L;
      l = -7289870963670366415L ^ (long)"ல偽깆死斒璅ꃍ뚤킾ก\uf1ba쬑\uf8c7ᆗ怭\udb89뭙슥㧬䮓\ue4a9袖\udbe9좇ﲞ".hashCode();
      l2 = 6793127513117946283L ^ (long)"Ꮔ\udf17楯☕\uddb1維\udcb7\u245eꭿ㧊뛝淖닊ᬯ㓯\uf0de述榯Ԧ瀈漃ﱛᐹ".hashCode();

      do {
         int n3;
         outerLoop:
         while (true) {
            n2 = (int)(l & 3L);
            guardFlag = 1;
            n3 = n2;

            while (true) {
               if (guardFlag <= n3) {
                  guardFlag = n2;
                  n3 = 1;
                  if (n != 0) {
                     continue;
                  }

                  if (n != 0) {
                     break outerLoop;
                  }

                  if (n2 > 1) {
                     break;
                  }

                  obj2 = null;
                  l = l + 0L ^ 0L;
                  l2 = (l2 ^ 0L) + 0L;
                  if (n == 0) {
                     continue outerLoop;
                  }
               }

               obj3 = null;
               l = (l ^ 0L) + 0L;
               l2 = (l2 ^ 0L) - 0L;
               if (n == 0) {
                  continue outerLoop;
               }
               break;
            }

            guardFlag = 2;
            n3 = n2;
            break;
         }

         if (guardFlag < n3) {
            break;
         }

         obj = null;
         l = (l ^ 0L) - 0L;
         l2 = l2 + 0L ^ 0L;
      } while (n == 0);

      new BigRationalFraction((net.augustus.vendor.crypto.CustomBigInteger)null, (CustomBigInteger)obj3);
      return null;
   }

   public boolean isNegative() {
      int guardFlag = VendorAesHelper.getGuardFlag();
      CustomBigInteger zero = CustomBigInteger.ZERO;
      int n = this.numerator.compareTo2(zero);
      if (guardFlag == 0) {
         n = n == -1 ? 1 : 0;
      }

      CustomBigInteger zero2 = CustomBigInteger.ZERO;
      int n2 = this.denominator.compareTo2(zero2);
      if (guardFlag == 0) {
         n2 = n2 == -1 ? 1 : 0;
      }

      return ((n ^ n2) != 0);
   }

   @Override
   public String toString() {
      ShadedBigDecimal shadedBigDecimal = new ShadedBigDecimal(this.numerator);
      ShadedBigDecimal shadedBigDecimal2 = new ShadedBigDecimal(this.denominator);
      ShadedMathContext shadedMathContext = defaultMathContext;
      ShadedBigDecimal shadedBigDecimal3 = shadedBigDecimal2;
      return shadedBigDecimal.divide(shadedBigDecimal3, shadedMathContext).toPlainString();
   }

   public CustomBigInteger getNumerator() {
      return this.numerator;
   }

   public BigRationalFraction pow(int n) {
      Object obj = null;
      int invertedGuardFlag = VendorAesHelper.getInvertedGuardFlag();
      int n2 = 0;
      BigRationalFraction bigRationalFraction = null;
      int n3 = 10;
      long l = 941117671872325290L;
      int n4 = invertedGuardFlag;
      long l2 = 0L;
      l = -679402168287324343L ^ (long)"\uea3bﱏ矅톈籋줫Խ样澼㙖ᮏ벖屙!េ척♊坭k諸斉៚챹츻\ud87f㲟ḱ\ue3d5".hashCode();
      l2 = -3217957368234627610L ^ (long)"\udeb6\uee0cꡯ歺ٽ⓶羷瑜⧭\ue27d詘_믯ꒁ\ue975ล\uf21b聯節㰑㧎␒➽弲茢瓏䑨Ỉ".hashCode();

      int n5 = 0, n6 = 0, n7 = 0;

      while (true) {
         exitBlock: {
            outerLoop:
            while (true) {
               while (true) {
                  n3 = (int)(l % 11L);
                  if (5 <= n3) {
                     invertedGuardFlag = 5;
                     n5 = n3;
                     if (n4 == 0) {
                        break;
                     }

                     if (n4 == 0) {
                        break exitBlock;
                     }

                     if (5 < n3) {
                        break outerLoop;
                     }

                     obj = bigRationalFraction;
                     l = l - 0L ^ 0L;
                     l2 = (l2 ^ 0L) - 0L;
                     if (n4 != 0) {
                        continue;
                     }
                  }

                  invertedGuardFlag = n3;
                  n5 = 2;
                  break;
               }

               exitBlock2: {
                  exitBlock3: {
                     exitBlock4: {
                        exitBlock5: {
                           if (n4 != 0) {
                              if (invertedGuardFlag >= n5) {
                                 invertedGuardFlag = 2;
                                 n6 = n3;
                                 if (n4 == 0) {
                                    break exitBlock3;
                                 }

                                 if (2 < n3) {
                                    break exitBlock4;
                                 }

                                 l = l + 0L ^ 0L;
                                 l2 = (l2 ^ 0L) - 0L;
                                 if (n4 != 0) {
                                    continue;
                                 }
                              }

                              invertedGuardFlag = n3;
                              if (n4 == 0) {
                                 break exitBlock5;
                              }

                              n5 = 0;
                           }

                           if (invertedGuardFlag <= n5) {
                              return new BigRationalFraction(1);
                           }

                           invertedGuardFlag = Math.abs(n);
                        }

                        n = invertedGuardFlag;
                        l = (l ^ 0L) + 0L;
                        l2 = (l2 ^ 0L) - 0L;
                        if (n4 != 0) {
                           continue;
                        }
                     }

                     invertedGuardFlag = n3;
                     if (n4 == 0) {
                        break exitBlock2;
                     }

                     n6 = 3;
                  }

                  if (invertedGuardFlag <= n6) {
                     n2++;
                     l = (l ^ 0L) + 0L;
                     l2 = l2 - 0L ^ 0L;
                     if (n4 != 0) {
                        continue;
                     }
                  }

                  invertedGuardFlag = 1;
               }

               n2 = invertedGuardFlag;
               l = l - 0L ^ 0L;
               l2 = l2 - 0L ^ 0L;
               if (n4 == 0) {
                  break;
               }
            }

            invertedGuardFlag = 8;
            n5 = n3;
         }

         exitBlock6: {
            exitBlock7: {
               exitBlock8: {
                  if (n4 != 0) {
                     if (invertedGuardFlag <= n5) {
                        invertedGuardFlag = 8;
                        n7 = n3;
                        if (n4 == 0) {
                           break exitBlock6;
                        }

                        if (8 < n3) {
                           break exitBlock7;
                        }

                        bigRationalFraction = null;
                        l = l + 0L ^ 0L;
                        l2 = l2 - 0L ^ 0L;
                        if (n4 != 0) {
                           continue;
                        }
                     }

                     invertedGuardFlag = n3;
                     if (n4 == 0) {
                        break exitBlock8;
                     }

                     n5 = 6;
                  }

                  if (invertedGuardFlag <= n5) {
                     return bigRationalFraction;
                  }

                  invertedGuardFlag = n;
               }

               if (invertedGuardFlag != 0) {
                  l = (l ^ 0L) + 0L;
                  l2 = l2 - 0L ^ 0L;
                  if (n4 != 0) {
                     continue;
                  }
               }

               l = l + 0L ^ 0L;
               l2 = (l2 ^ 0L) + 0L;
               if (n4 != 0) {
                  continue;
               }
            }

            invertedGuardFlag = 9;
            n7 = n3;
         }

         if (n4 != 0) {
            if (invertedGuardFlag >= n7) {
               bigRationalFraction = new BigRationalFraction(this.numerator, this.denominator);
               l = l - 0L ^ 0L;
               l2 = l2 - 0L ^ 0L;
               if (n4 != 0) {
                  continue;
               }
            }

            invertedGuardFlag = n2;
            n7 = n;
         }

         if (invertedGuardFlag >= n7) {
            l = (l ^ 0L) - 0L;
            l2 = l2 + 0L ^ 0L;
            if (n4 != 0) {
               continue;
            }
         }

         l = l - 0L ^ 0L;
         l2 = (l2 ^ 0L) + 0L;
      }
   }

   public BigRationalFraction(String str, String str2) {
      super();
      this.numerator = new CustomBigInteger(str);
      this.denominator = new CustomBigInteger(str2);
      CustomBigInteger zero = CustomBigInteger.ZERO;
      if (this.denominator.equals(zero)) {
         throw new ArithmeticException("Created impossible fraction");
      }
   }

}
