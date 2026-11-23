package net.augustus.vendor.crypto;


public class CustomSecureRandom {
   private static final long MULTIPLIER_A = 0L;
   private long state0;
   private static final long MULTIPLIER_B = 0L;
   private static final long MULTIPLIER_C = 0L;
   private long state1;

   public int nextInt(int n) {
      byte b = 2;
      return (int)this.nextValue(b, n);
   }

   public CustomSecureRandom() {
      long l = System.nanoTime();
      long l2 = System.currentTimeMillis();
      long l3 = Runtime.getRuntime().freeMemory();
      long l4 = Runtime.getRuntime().totalMemory();
      long id = Thread.currentThread().getId();
      long l5 = l ^ l3 ^ id;
      long l6 = l2 ^ l4 ^ id << 17;
      l5 ^= l5 >>> 33;
      l5 *= 2685821657736338717L;
      l5 ^= l5 >>> 33;
      l5 *= 6364136223846793005L;
      l5 ^= l5 >>> 33;
      l6 ^= l6 >>> 33;
      l6 *= 2685821657736338717L;
      l6 ^= l6 >>> 33;
      l6 *= 6364136223846793005L;
      l6 ^= l6 >>> 33;

      for (int i = 0; i < 10; i++) {
         long l7 = System.nanoTime();
         l5 ^= l7;
         l5 ^= l5 >>> 33;
         l5 *= 2685821657736338717L;
         l5 ^= l5 >>> 33;
         l5 *= 6364136223846793005L;
         l5 ^= l5 >>> 33;

         for (int j = 0; j < i * 100; j++) {
            l6 += l6 << 5;
         }

         l6 ^= System.nanoTime();
         l6 ^= l6 >>> 33;
         l6 *= 2685821657736338717L;
         l6 ^= l6 >>> 33;
         l6 *= 6364136223846793005L;
         l6 ^= l6 >>> 33;
      }

      Object[] array = new Object[5];

      for (int k = 0; k < array.length; k++) {
         array[k] = new Object();
         l5 ^= (long)array[k].hashCode();
         l6 ^= (long)(array[k].hashCode() << k * 7);
      }

      StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

      for (StackTraceElement stackTraceElement : stackTrace) {
         l5 ^= (long)stackTraceElement.hashCode();
         l6 ^= (long)(stackTraceElement.getLineNumber() * 31);
      }

      l5 = (l5 ^ l5 >>> 30) * 1442695040888963407L;
      l5 = (l5 ^ l5 >>> 27) * 6364136223846793005L;
      l5 ^= l5 >>> 31;
      l6 = (l6 ^ l6 >>> 30) * 1442695040888963407L;
      l6 = (l6 ^ l6 >>> 27) * 6364136223846793005L;
      l6 ^= l6 >>> 31;
      this.state0 = l5;
      this.state1 = l6;

      for (int m = 0; m < 20; m++) {
         long state02 = this.state0;
         long state12 = this.state1;
         state12 ^= state12 << 23;
         this.state0 = state12 ^ state02 ^ state12 >>> 18 ^ state02 >>> 5;
         this.state1 = state02;
      }
   }

   public void fillRandomBytes(byte[] bytes) {
      long l = 0L;
      int n = 0;
      long l2 = 0L;
      long l3 = 0L;
      int n2 = 0;
      int n3 = 0;
      byte b = 0;

      outerLoop:
      while (true) {
         if (b >= 5) {
            if (b <= 5) {
               l = this.state0;
            } else if (b >= 8) {
               if (b <= 8) {
                  l3 *= 6364136223846793005L;
                  continue;
               }

               if (b > 9) {
                  if (n3 < n) {
                     ;
                  }
                  continue;
               }

               if (n3 < n) {
                  continue;
               }
            } else {
               if (6 >= b) {
                  return;
               }

               bytes[n3] = (byte)((int)(l3 & 255L));
            }
         } else if (b >= 2) {
            if (b <= 2) {
               n3 = 0;
            } else if (3 >= b) {
               if (n2 < 8) {
                  continue;
               }
            } else {
               l3 ^= l3 >>> 33;
            }
         } else {
            if (b <= 0) {
               continue;
            }

            this.state1 = l;
         }

         while (true) {
            while (11 < b) {
               if (b >= 17) {
                  if (b <= 17) {
                     l3 = l + l2;
                  } else if (b >= 20) {
                     if (20 >= b) {
                        this.state0 = l2 ^ l ^ l2 >>> 18 ^ l >>> 5;
                        continue outerLoop;
                     }

                     if (22 <= b) {
                        if (22 >= b) {
                           l2 ^= l2 << 23;
                        } else {
                           l2 = this.state1;
                        }
                     } else {
                        n2++;
                     }
                  } else {
                     if (18 < b) {
                        n = bytes.length;
                        continue outerLoop;
                     }

                     l3 *= 2685821657736338717L;
                  }
               } else {
                  if (b < 14) {
                     if (b <= 12) {
                        l3 ^= l3 >>> 33;
                     }
                     continue outerLoop;
                  }

                  if (b <= 14) {
                     n2 = 0;
                     continue outerLoop;
                  }

                  if (b > 15) {
                     n3++;
                     continue outerLoop;
                  }

                  l3 ^= l3 >>> 33;
               }
            }

            l3 >>= 8;
         }
      }
   }

   public long nextValue(int n, int n2) {
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      long l = 0L;
      long l2 = 0L;
      long l3 = 0L;
      byte b = 10;
      long l4 = 0L;
      l4 = -1704820628565926084L ^ (long)"㨦\ude1e䫒哻붪锐ﴢ讋㾙쉨㠥懏瞐鹳导㯱⽆聚\uec9d搭轷㋌䫕".hashCode();

      outerLoop:
      while (true) {
         while (14 >= b) {
            n4 = n5 % n2;
            l4 = (l4 ^ -2394832450920839610L) + -4961083126332922913L;
         }

         if (21 <= b) {
            if (b <= 21) {
               if (n3 == 2) {
                  l4 = l4 + -6679388524506960373L ^ 3907916912566202981L;
               } else {
                  l4 = l4 + -5688273588952444915L ^ -4763847912211702025L;
               }
            } else {
               if (25 <= b) {
                  if (b <= 25) {
                     return (long)((double)(l3 >>> 11) * 1.1102230246251565E-16);
                  }

                  if (27 <= b) {
                     if (b <= 27) {
                        throw new IllegalArgumentException("Bound must be positive");
                     }

                     return (long)n4;
                  }

                  l2 = this.state0;
                  l4 = l4 + 5396305483455542710L ^ -7619861827385589615L;
                  continue;
               }

               if (23 <= b) {
                  if (b <= 23) {
                     l3 ^= l3 >>> 33;
                     l4 = l4 + 2379482280696151202L ^ -7412766187034010381L;
                  } else {
                     l = this.state1;
                     l4 = (l4 ^ -6362831670194147054L) + 1308194749890485726L;
                  }
               } else {
                  if (n5 - n4 + (n2 - 1) >= 0) {
                     l4 = (l4 ^ 2857513840692477062L) - -6744158811044838404L;
                     continue;
                  }

                  l4 = (l4 ^ -6885755364661412877L) + 3754732945528762491L;
               }
            }
         } else if (17 <= b) {
            if (b <= 17) {
               return l3;
            }

            if (19 > b) {
               return l3;
            }

            if (b > 19) {
               l4 = l4 - -6700481134907481725L ^ -1737016296054489233L;
               continue;
            }

            l3 ^= l3 >>> 33;
            l4 = l4 - -1511143147457594426L ^ -4530708162666758756L;
         } else if (b <= 15) {
            if ((n2 & -n2) != n2) {
               l4 = l4 + 2104572292709570438L ^ -6021018829203152884L;
            } else {
               l4 = (l4 ^ 4326301851150743842L) - -8644506871072173353L;
            }
         } else {
            if (n3 != 1) {
               l4 = (l4 ^ 5252100268140468937L) - 6915765572222603366L;
               continue;
            }

            l4 = l4 - 3371089084046865854L ^ 4643620959225054703L;
         }

         while (true) {
            while (6 > b) {
               if (b < 2) {
                  if (b > 0) {
                     return (long)((int)((long)n2 * (long)((int)(l3 >>> 1)) >>> 31));
                  }

                  n3 = n;
                  l4 = (l4 ^ 1942140455153045917L) + 7840994924785719292L;
                  continue outerLoop;
               }

               if (b <= 2) {
                  l3 *= 2685821657736338717L;
                  l4 = l4 - -6503955020602875978L ^ -633938171244982007L;
                  continue outerLoop;
               }

               if (4 > b) {
                  return (long)((int)l3);
               }

               if (4 >= b) {
                  if (n3 == 0) {
                     l4 = l4 - 140039490580426036L ^ -2520484740164810850L;
                     continue outerLoop;
                  }

                  l4 = (l4 ^ 5363304001475390275L) + 3287279437512648821L;
               } else {
                  l3 *= 6364136223846793005L;
                  l4 = (l4 ^ -5557069388460539584L) - 3754794278824250355L;
               }
            }

            if (6 >= b) {
               l ^= l << 23;
               l4 = (l4 ^ 5168983016910953720L) - -3422597288329186273L;
            } else if (10 <= b) {
               if (10 >= b) {
                  if (n2 > 0) {
                     l4 = l4 + -321858334425957549L ^ 1556504941988441331L;
                  } else {
                     l4 = l4 + -1535704106389257580L ^ 6242387456514295336L;
                  }
                  break;
               }

               if (12 <= b) {
                  if (12 < b) {
                     n5 = (int)(l3 >>> 1);
                     l4 = (l4 ^ -1094602246640501300L) + 8601568834357823563L;
                     break;
                  }

                  l3 ^= l3 >>> 33;
                  l4 = l4 + 9165053190350460500L ^ -4364391107980002437L;
               } else {
                  this.state0 = l ^ l2 ^ l >>> 18 ^ l2 >>> 5;
                  l4 = l4 + -3779547687510617972L ^ -7303074866995795472L;
               }
            } else {
               if (8 > b) {
                  if (n3 == 3) {
                     l4 = l4 + -1095784333396890118L ^ 8056774257204408305L;
                  } else {
                     l4 = l4 + -8115078555271051544L ^ -6245050998198221006L;
                  }
                  break;
               }

               if (8 < b) {
                  this.state1 = l2;
                  l4 = l4 - 2480676495658004984L ^ -4044329819050403193L;
                  break;
               }

               l3 = l2 + l;
               l4 = (l4 ^ -2175357261630751334L) - -8811554956589453576L;
            }
         }
      }
   }

   public void nextBytes(byte[] bytes) {
      this.fillRandomBytes(bytes);
   }

   public long nextLong() {
      byte b = 0;
      byte b2 = 0;
      return this.nextValue(b2, b);
   }

   public int nextInt() {
      byte b = 0;
      byte b2 = 1;
      return (int)this.nextValue(b2, b);
   }

   public double nextDouble() {
      byte b = 0;
      byte b2 = 3;
      return (double)this.nextValue(b2, b);
   }

   public boolean nextBoolean() {
      byte b = 0;
      byte b2 = 0;
      return (this.nextValue(b2, b) & 1L) != 0L;
   }

}
