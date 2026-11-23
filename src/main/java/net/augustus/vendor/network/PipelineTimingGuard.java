package net.augustus.vendor.network;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PipelineTimingGuard {
   private final AtomicLong step3AtomicCounter = new AtomicLong(0L);
   private volatile long step4Count;
   private volatile long step3Count;
   private final Random random;
   private volatile boolean parityFlag;
   private volatile long step2Count;
   private volatile long step1Count;
   private final double[] mathConstants = new double[]{1.618033988749895, Math.E, Math.PI};

   public void enterStep1() {
      long l = 0L;
      long l2 = 0L;
      double d = 0.0;
      double[][] doublesArr = null;
      byte b = 6;

      outerLoop:
      while (true) {
         if (b >= 1) {
            if (1 >= b) {
               l2 = System.nanoTime();
            } else {
               if (2 >= b) {
                  return;
               }

               doublesArr = this.generateRandomMatrix((byte)3, (byte)3);
            }
         } else {
            l = System.nanoTime();
         }

         while (true) {
            while (4 < b) {
               if (b < 7) {
                  if (b <= 5) {
                     this.markStepEntry("STEP1_ENTRY");
                  } else {
                     long l3 = l2 - l;
                     this.recordStepDuration("step1", l3);
                  }
                  continue outerLoop;
               }

               if (7 >= b) {
                  this.applyRandomJitter(d);
                  continue outerLoop;
               }

               if (b <= 8) {
                  d = this.computeLogLogistic(this.step1Count);
               } else {
                  this.computeMatrixDeterminant(doublesArr);
               }
            }

            this.step1Count++;
         }
      }
   }

   private int computeStepChecksum() {
      return (int)(this.step1Count * 31L + this.step2Count * 17L + this.step3Count * 13L + this.step4Count * 7L + System.nanoTime() % 1000L);
   }

   public void enterStep3() {
      double d = 0.0;
      byte b = 7;
      long l = 0L;
      l = 4030601439426835478L ^ (long)"ꋁ譜㨦䒷鱀鏩⿐蛙䃣ⷤ供ᴲ⟺뾷\udf2b팯ᇄ屄ꐿꐷ㛾儕䝷".hashCode();

      while (4 < b) {
         if (6 <= b) {
            if (6 >= b) {
               if (!this.parityFlag) {
                  l = (l ^ 1907540469936860809L) + -5056777415010097465L;
                  continue;
               }

               l = l - -4265182229231074787L ^ 1592876241140471650L;
            } else {
               if (b > 7) {
                  this.step3AtomicCounter.incrementAndGet();
                  l = l + -7838041110704478869L ^ -5583331952914110154L;
                  continue;
               }

               if (this.step2Count >= this.step3Count) {
                  l = (l ^ 8423351697577329832L) + 8871613697835786977L;
               } else {
                  l = l + -3353532327429912756L ^ 7075769492724188898L;
               }
            }
         } else {
            this.computeSineNoise(d);
            l = (l ^ 6837442600559283041L) - 7906444591811815249L;
         }

         if (1 <= b) {
            if (1 >= b) {
               long step3Count2 = this.step3Count;
               d = this.computeTanhLogRatio(this.step2Count, step3Count2);
               l = l - -3836496183820738113L ^ -711290324011446114L;
            } else {
               if (2 < b) {
                  throw new RuntimeException();
               }

               this.populateNoiseVector();
               l = l + 6057576994596274425L ^ 6784183512163156948L;
            }
         } else {
            this.step3Count++;
            l = (l ^ -6794118877842313025L) + -4075737931115319159L;
         }
      }
   }

   private void recordStepDuration(String str, long l) {
      double d = (double)l / 1000000.0;
   }

   private void computeSineNoise(double d) {
      double d2 = 0.5 + 0.1 * Math.sin(d * Math.PI);
   }

   private void applyRandomJitter(double d) {
      double d2 = d * (this.random.nextDouble() - 0.5);
   }

   private void markStepEntry(String str) {
      long l = 0L;
      double d = 0.0;
      l = System.nanoTime() % 10000L;
      d = (double)l / 10000.0;
   }

   private void sampleMillisecondFraction() {
      double d = 0.0;
      long l = 0L;
      l = System.currentTimeMillis();
      d = (double)(l % 1000L) / 1000.0;
   }

   private void populateNoiseVector() {
      double[] doubles = null;
      int n = 0;
      byte b = 5;

      while (true) {
         while (true) {
            if (3 >= b) {
               n++;
               break;
            }

            if (5 <= b) {
               if (b <= 5) {
                  return;
               }

               doubles = new double[4];
               n = 0;
               break;
            }

            doubles[n] = this.random.nextDouble() * this.mathConstants[n % ((double[])this.mathConstants).length];
         }

         while (1 < b) {
         }

         if (n < doubles.length) {
            ;
         }
      }
   }

   public PipelineTimingGuard() {
      this.parityFlag = false;
      this.random = new Random(System.nanoTime());
   }

   private double computeCosineWeight(long l) {
      return (Math.cos((double)l) + 1.0) / 2.0 * this.mathConstants[this.random.nextInt(this.mathConstants.length)];
   }

   private void computeModuloRatio(long l) {
      double d = (double)(l % 7L) / 3.0;
   }

   private void computeMatrixDeterminant(double[][] doublesArr) {
      int n = 0;
      double d = 0.0;
      byte b = 1;

      while (true) {
         if (b > 3) {
            if (b < 5) {
               return;
            }

            if (5 >= b) {
               if (n >= doublesArr.length) {
                  continue;
               }
            } else {
               if (b <= 6) {
                  n++;
                  continue;
               }

               d = 0.0;
            }

            while (1 > b) {
               d += ((double[][])doublesArr)[n][0]
                  * ((double[][])doublesArr)[(n + 1) % ((double[][])doublesArr).length][1]
                  * ((double[][])doublesArr)[(n + 2) % ((double[][])doublesArr).length][2];
            }

            if (b <= 1) {
               n = 0;
            } else {
               d -= ((double[][])doublesArr)[n][2]
                  * ((double[][])doublesArr)[(n + 1) % ((double[][])doublesArr).length][1]
                  * ((double[][])doublesArr)[(n + 2) % ((double[][])doublesArr).length][0];
            }
         }
      }
   }

   private double computeLogLogistic(long l) {
      return Math.log((double)(1L + Math.abs(l))) * this.mathConstants[0] / (1.0 + Math.exp((double)(-l) / this.mathConstants[1]));
   }

   private double computeQuadraticWave(long l, long l2) {
      return Math.pow((double)l, 2.0) - Math.sqrt((double)(1L + Math.abs(l2))) + this.mathConstants[2] * Math.sin((double)l / (1.0 + (double)l2));
   }

   private void validateStepParity() {
      long l = 0L;
      byte b = 1;
      long l2 = 0L;
      l2 = -480702986131445788L ^ (long)"䘰\ueb0d鋙⡢㳕㿀딋ࡺ捫놠钲뇐\uf2f8䕣咃ꪚ뷀鱆끣\udb59䲒鿢播ȃ飚ᰶ輮\ue607".hashCode();

      while (true) {
         if (1 >= b) {
            boolean flag = true;
            break;
         }

         if (b <= 2) {
            if (l % 3L != 0L) {
               l2 = (l2 ^ 3908828814147334937L) - 4507916976532395056L;
            } else {
               l2 = (l2 ^ -4563109332848994411L) + -3706503221214119140L;
               if (this.step3AtomicCounter.get() % 2L != 0L) {
                  l2 = (l2 ^ 3410265005472116575L) - 1765620238095994243L;
                  break;
               }

               l2 = l2 - 158247328071701110L ^ 1479214059289276326L;
            }
         } else {
            l = this.step1Count ^ this.step2Count ^ this.step3Count << 2 ^ this.step4Count >> 1;
            l2 = (l2 ^ 8496830093477674987L) + 3220565514698598408L;
         }
      }
   }

   private double[][] generateRandomMatrix(int n, int n2) {
      int n3 = 0;
      double[][] doublesArr = null;
      int n4 = 0;
      byte b = 7;

      outerLoop:
      while (true) {
         if (b >= 2) {
            if (b > 2) {
               if (b > 3) {
                  return doublesArr;
               }
               continue;
            }

            if (n4 >= n) {
               continue;
            }
         } else if (b <= 0) {
            doublesArr = new double[n][n2];
         } else {
            n3++;
         }

         while (5 < b) {
            if (8 <= b) {
               if (b <= 8) {
                  n4++;
                  continue outerLoop;
               }

               if (9 >= b) {
                  if (n3 < n2) {
                     ;
                  }
               } else {
                  n3 = 0;
               }
            } else if (6 < b) {
               doublesArr[n4][n3] = this.random.nextDouble() * this.mathConstants[n4 % ((double[])this.mathConstants).length];
               continue outerLoop;
            }
         }

         n4 = 0;
      }
   }

   public void enterStep4() {
      long l = 0L;
      long l2 = 0L;
      double d = 0.0;
      byte b = 7;
      long l3 = 0L;
      l3 = 7247086090665111746L ^ (long)"걚ᇀ䣧眿훍䓷\udff7认姥㫟녋璽뱬穓储냛\uf8edп\ue3f0\uedd0쳫데ٳꦐꒁ".hashCode();

      outerLoop:
      while (true) {
         while (true) {
            if (4 >= b) {
               d = this.computeCosineWeight(l2);
               l3 = l3 + -6997109785616017013L ^ -1966458928791079525L;
               break;
            }

            if (6 <= b) {
               if (6 >= b) {
                  this.computeTrigNoise();
                  l3 = (l3 ^ 324747773309979935L) - -8070043662036062094L;
                  break;
               }

               if (b <= 7) {
                  l = this.computePackedStepHash();
                  l3 = (l3 ^ 5892987322308382034L) - 2782407013351691213L;
                  break;
               }

               l2 = Thread.currentThread().getId();
               l3 = (l3 ^ -7794421416600861572L) + -4016928009361083903L;
            } else {
               this.step4Count++;
               l3 = l3 - 6204258505994418415L ^ -4369844538314784395L;
            }
         }

         while (b >= 1) {
            if (b > 1) {
               if (b <= 2) {
                  return;
               }

               this.computeModuloRatio(l);
               l3 = (l3 ^ 7075159939639556155L) - -5782207707539506423L;
               continue outerLoop;
            }

            if (this.step3Count >= this.step4Count) {
               l3 = (l3 ^ -4413624466107074806L) + -5196816266413565779L;
               continue outerLoop;
            }

            l3 = l3 + 3113168088097308235L ^ -4973268407027341593L;
         }

         throw new RuntimeException();
      }
   }

   public void enterStep2() {
      double d = 0.0;
      int n = 0;
      byte b = 1;
      long l = 0L;
      l = 3990699575504720303L ^ (long)"㭃궦બ箰ߺ쥛忂샩ꔛသ堩\uef2cౘ禀\uf8ce킒刓亰⯓㌄ㅃﲴ".hashCode();

      outerLoop:
      while (true) {
         if (1 <= b) {
            if (b <= 1) {
               throw new RuntimeException();
            }

            n = this.computeStepChecksum();
            l = (l ^ -8235007487696603356L) + 6575439972669188316L;
         } else {
            double d2 = this.random.nextDouble();
            long step2Count2 = this.step2Count;
            d = d2 * this.computeQuadraticWave(this.step1Count, step2Count2);
            l = (l ^ 2529132424220959272L) + 7869285713675637016L;
         }

         while (3 < b) {
            if (5 <= b) {
               if (b > 5) {
                  if (b <= 6) {
                     this.parityFlag = n % 2 == 0;
                     return;
                  }

                  this.sampleMillisecondFraction();
                  l = (l ^ 4261197078716163605L) + 6393355903526815478L;
                  continue outerLoop;
               }

               this.validateStepParity();
               l = l + 8724612933026742117L ^ 5219979606940334285L;
            } else if (this.step1Count >= this.step2Count) {
               l = (l ^ 6680956263875720016L) + 5766854602570809604L;
            } else {
               l = (l ^ 2035974897523735162L) - -3008282682974212767L;
            }
         }

         this.step2Count++;
         l = l + -5437788485663159916L ^ 4551606738994689433L;
      }
   }

   private double computeTanhLogRatio(long l, long l2) {
      double d = 0.0;
      double d2 = 0.0;
      long l3 = 0L;
      l3 = 6975809196883308352L ^ (long)"픈㚀⒭႘廒윣鋘뎜肹덧\u17eb⎝쏑㟐聯\ufdeb砲쯒\uf225跌䲫楴".hashCode();
      d2 = (double)(l * l2);
      l3 = l3 - 4055570696109056272L ^ -4512236531874078001L;
      double d3;
      if (l2 != 0L) {
         l3 = l3 + 8418712592233234380L ^ -4469720864521869999L;
         d3 = (double)l / (double)l2;
      } else {
         l3 = l3 + 2963739986751999576L ^ -8337175297005980707L;
         d3 = (double)l;
      }

      d = d3;
      return Math.tanh(d2) * Math.log1p(Math.abs(d));
   }

   private long computePackedStepHash() {
      return this.step1Count + (this.step2Count << 8) + (this.step3Count << 16) + (this.step4Count << 24) + (long)this.random.nextInt(1000);
   }

   private void computeTrigNoise() {
      double d = Math.sin((double)this.step4Count * Math.PI / 180.0) * Math.cos((double)this.step3AtomicCounter.get() * Math.PI / 90.0);
   }


}
