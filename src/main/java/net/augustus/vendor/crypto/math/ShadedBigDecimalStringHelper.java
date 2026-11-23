package net.augustus.vendor.crypto.math;



import net.augustus.vendor.crypto.CustomBigInteger;
public class ShadedBigDecimalStringHelper {
   public static final boolean assertionsDisabled = !ShadedBigDecimal.class.desiredAssertionStatus();
   public static final char[] DIGIT_ONES = new char[]{
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9'
   };
   public final char[] cmpCharArray;
   public static final char[] DIGIT_TENS = new char[]{
      '0',
      '0',
      '0',
      '0',
      '0',
      '0',
      '0',
      '0',
      '0',
      '0',
      '1',
      '1',
      '1',
      '1',
      '1',
      '1',
      '1',
      '1',
      '1',
      '1',
      '2',
      '2',
      '2',
      '2',
      '2',
      '2',
      '2',
      '2',
      '2',
      '2',
      '3',
      '3',
      '3',
      '3',
      '3',
      '3',
      '3',
      '3',
      '3',
      '3',
      '4',
      '4',
      '4',
      '4',
      '4',
      '4',
      '4',
      '4',
      '4',
      '4',
      '5',
      '5',
      '5',
      '5',
      '5',
      '5',
      '5',
      '5',
      '5',
      '5',
      '6',
      '6',
      '6',
      '6',
      '6',
      '6',
      '6',
      '6',
      '6',
      '6',
      '7',
      '7',
      '7',
      '7',
      '7',
      '7',
      '7',
      '7',
      '7',
      '7',
      '8',
      '8',
      '8',
      '8',
      '8',
      '8',
      '8',
      '8',
      '8',
      '8',
      '9',
      '9',
      '9',
      '9',
      '9',
      '9',
      '9',
      '9',
      '9',
      '9'
   };
   public final StringBuilder sb = new StringBuilder();

   public int putIntCompact(long l) {
      int n = 0;
      CustomBigInteger.getOpaqueStateTag();
      int n2 = 0;
      long l2 = 0L;
      int n3 = 0;
      int n4 = 0;
      long l3 = 0L;
      l3 = -5351561882544288507L ^ (long)"Ỽᔶ桮瘘\ueb70꜈侰㦸軱ᄹ棣╌휛ꗆራꇺ퉵\uf74eਾᰭ㗨鍥Ա".hashCode();
      if (assertionsDisabled) {
         l3 = l3 - -1167560810769099666L ^ 2381903316681481581L;
      } else {
         l3 = (l3 ^ 3455507158570979804L) + 6602551646856633768L;
         if (l < 0L) {
            l3 = (l3 ^ 5161138190730872674L) - -1287510876904224386L;
            throw new AssertionError();
         }

         l3 = l3 + 7971026012659042974L ^ 6090581897903966413L;
      }

      n4 = this.cmpCharArray.length;
      l3 = l3 - -2264374060367004283L ^ -1284490875473033899L;

      while (l > 2147483647L) {
         l3 = l3 + -1276283985134544730L ^ 865279164630945609L;
         l2 = l / 100L;
         l3 = (l3 ^ -41462735847703284L) + -3179419081094931290L;
         n3 = (int)(l - l2 * 100L);
         l3 = (l3 ^ -6025127213669408224L) + -8309087832177129223L;
         l = l2;
         l3 = l3 + 7839640875221087967L ^ 5067635804555518169L;
         n4--;
         this.cmpCharArray[n4] = DIGIT_ONES[n3];
         l3 = l3 + -8623500464479927811L ^ 3549946642141329062L;
         n4--;
         this.cmpCharArray[n4] = DIGIT_TENS[n3];
         l3 = l3 + -7766375998195768056L ^ -8757179608723513934L;
         l3 = (l3 ^ -5386415373751553642L) + -1304109166025077439L;
      }

      l3 = l3 - 7580703574910340742L ^ -9108009522389794151L;
      n2 = (int)l;
      l3 = (l3 ^ -8664008715243547555L) + 3713875555280076094L;

      while (n2 >= 100) {
         l3 = (l3 ^ 9058034177524618350L) - -6430317633302784557L;
         n = n2 / 100;
         l3 = l3 - -3999578140011400489L ^ -7330993713353078693L;
         n3 = n2 - n * 100;
         l3 = l3 - 7845881809885784737L ^ 8680785052744446733L;
         n2 = n;
         l3 = (l3 ^ 699424360064987268L) - 4578949539262161023L;
         n4--;
         this.cmpCharArray[n4] = DIGIT_ONES[n3];
         l3 = (l3 ^ -4166117295382516981L) + 5829237434502126302L;
         n4--;
         this.cmpCharArray[n4] = DIGIT_TENS[n3];
         l3 = l3 - 6100577036174473976L ^ 8703491135064132662L;
         l3 = (l3 ^ -1542341395254297037L) - -3844633599917867332L;
      }

      l3 = (l3 ^ -4545731721691961348L) + -7705809778940846130L;
      n4--;
      this.cmpCharArray[n4] = DIGIT_ONES[n2];
      l3 = (l3 ^ -8195666466569139310L) - 8745171118630301329L;
      if (n2 < 10) {
         l3 = (l3 ^ -8628271027934352735L) + -8770361007685748676L;
      } else {
         l3 = l3 - 3270472471914171148L ^ 3380868144351041229L;
         n4--;
         this.cmpCharArray[n4] = DIGIT_TENS[n2];
         l3 = (l3 ^ 7479813617663167818L) + 7136112504715542118L;
      }

      return n4;
   }

   public StringBuilder getStringBuilder() {
      this.sb.setLength(0);
      return this.sb;
   }

   public char[] getCmpCharArray() {
      return this.cmpCharArray;
   }

   public ShadedBigDecimalStringHelper() {
      this.cmpCharArray = new char[19];
   }

}
