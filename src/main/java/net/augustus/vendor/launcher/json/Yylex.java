package net.augustus.vendor.launcher.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Yylex {
   private static final String ZZ_ACTION_PACKED_0 = "\u0002\u0000\u0002\u0001\u0001\u0002\u0001\u0003\u0001\u0004\u0003\u0001\u0001\u0005\u0001\u0006\u0001\u0007\u0001\b\u0001\t\u0001\n\u0001\u000b\u0001\f\u0001\r\u0005\u0000\u0001\f\u0001\u000e\u0001\u000f\u0001\u0010\u0001\u0011\u0001\u0012\u0001\u0013\u0001\u0014\u0001\u0000\u0001\u0015\u0001\u0000\u0001\u0015\u0004\u0000\u0001\u0016\u0001\u0017\u0002\u0000\u0001\u0018";
   public static final int YYINITIAL = 0;
   private static final char[] ZZ_CMAP;
   private int zzStartRead;
   private static final int[] ZZ_ATTRIBUTE;
   private char[] zzBuffer;
   private int yychar;
   private int yycolumn;
   private static final int ZZ_NO_MATCH = 1;
   public static final int STRING_BEGIN = 0;
   private static final int[] ZZ_ROWMAP;
   private int yyline;
   private static final int ZZ_UNKNOWN_ERROR = 0;
   private static final int[] ZZ_ACTION;
   private StringBuffer sb;
   private static final String[] ZZ_ERROR_MSG = new String[]{"Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large"};
   private boolean zzAtBOL;
   private static final int ZZ_PUSHBACK_2BIG = 0;
   private static final String ZZ_ROWMAP_PACKED_0 = "\u0000\u0000\u0000\u001b\u00006\u0000Q\u0000l\u0000\u0087\u00006\u0000¢\u0000½\u0000Ø\u00006\u00006\u00006\u00006\u00006\u00006\u0000ó\u0000Ď\u00006\u0000ĩ\u0000ń\u0000ş\u0000ź\u0000ƕ\u00006\u00006\u00006\u00006\u00006\u00006\u00006\u00006\u0000ư\u0000ǋ\u0000Ǧ\u0000Ǧ\u0000ȁ\u0000Ȝ\u0000ȷ\u0000ɒ\u00006\u00006\u0000ɭ\u0000ʈ\u00006";
   private static final int ZZ_BUFFERSIZE = 0;
   private static final int[] ZZ_LEXSTATE = new int[]{0, 0, 1, 1};
   private static final int[] ZZ_TRANS = new int[]{
      2,
      2,
      3,
      4,
      2,
      2,
      2,
      5,
      2,
      6,
      2,
      2,
      7,
      8,
      2,
      9,
      2,
      2,
      2,
      2,
      2,
      10,
      11,
      12,
      13,
      14,
      15,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      17,
      18,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      4,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      4,
      19,
      20,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      20,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      5,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      21,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      22,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      23,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      -1,
      -1,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      16,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      24,
      25,
      26,
      27,
      28,
      29,
      30,
      31,
      32,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      33,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      34,
      35,
      -1,
      -1,
      34,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      36,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      37,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      38,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      39,
      -1,
      39,
      -1,
      39,
      -1,
      -1,
      -1,
      -1,
      -1,
      39,
      39,
      -1,
      -1,
      -1,
      -1,
      39,
      39,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      33,
      -1,
      20,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      20,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      35,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      38,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      40,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      41,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      42,
      -1,
      42,
      -1,
      42,
      -1,
      -1,
      -1,
      -1,
      -1,
      42,
      42,
      -1,
      -1,
      -1,
      -1,
      42,
      42,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      43,
      -1,
      43,
      -1,
      43,
      -1,
      -1,
      -1,
      -1,
      -1,
      43,
      43,
      -1,
      -1,
      -1,
      -1,
      43,
      43,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      44,
      -1,
      44,
      -1,
      44,
      -1,
      -1,
      -1,
      -1,
      -1,
      44,
      44,
      -1,
      -1,
      -1,
      -1,
      44,
      44,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1
   };
   private int zzCurrentPos;
   private static final String ZZ_CMAP_PACKED = "\t\u0000\u0001\u0007\u0001\u0007\u0002\u0000\u0001\u0007\u0012\u0000\u0001\u0007\u0001\u0000\u0001\t\b\u0000\u0001\u0006\u0001\u0019\u0001\u0002\u0001\u0004\u0001\n\n\u0003\u0001\u001a\u0006\u0000\u0004\u0001\u0001\u0005\u0001\u0001\u0014\u0000\u0001\u0017\u0001\b\u0001\u0018\u0003\u0000\u0001\u0012\u0001\u000b\u0002\u0001\u0001\u0011\u0001\f\u0005\u0000\u0001\u0013\u0001\u0000\u0001\r\u0003\u0000\u0001\u000e\u0001\u0014\u0001\u000f\u0001\u0010\u0005\u0000\u0001\u0015\u0001\u0000\u0001\u0016ﾂ\u0000";
   private int zzState;
   private int zzMarkedPos;
   private int zzEndRead;
   private Reader zzReader;
   private static final String ZZ_ATTRIBUTE_PACKED_0 = "\u0002\u0000\u0001\t\u0003\u0001\u0001\t\u0003\u0001\u0006\t\u0002\u0001\u0001\t\u0005\u0000\b\t\u0001\u0000\u0001\u0001\u0001\u0000\u0001\u0001\u0004\u0000\u0002\t\u0002\u0000\u0001\t";
   private int zzLexicalState = 0;
   private boolean zzAtEOF;
   public static final int YYEOF = -1;
   private static final long a = 87006333598913L;

   public final void setZzLexicalState(int n) {
      this.zzLexicalState = n;
   }

   private static int zzUnpackAction(String str, int n, int[] ints) {
      int n2 = 0;
      int n3 = n;
      int length = str.length();

      while (n2 < length) {
         int n4 = str.charAt(n2++);
         char ch = str.charAt(n2++);

         do {
            ints[n3++] = ch;
         } while (--n4 <= 0);
      }

      return n3;
   }

   public final int yylength() {
      return this.zzMarkedPos - this.zzStartRead;
   }

   private static int[] zzUnpackRowMap() {
      int[] ints = new int[45];
      int n = 0;
      n = zzUnpackRowMap(
         "\u0000\u0000\u0000\u001b\u00006\u0000Q\u0000l\u0000\u0087\u00006\u0000¢\u0000½\u0000Ø\u00006\u00006\u00006\u00006\u00006\u00006\u0000ó\u0000Ď\u00006\u0000ĩ\u0000ń\u0000ş\u0000ź\u0000ƕ\u00006\u00006\u00006\u00006\u00006\u00006\u00006\u00006\u0000ư\u0000ǋ\u0000Ǧ\u0000Ǧ\u0000ȁ\u0000Ȝ\u0000ȷ\u0000ɒ\u00006\u00006\u0000ɭ\u0000ʈ\u00006",
         n,
         ints
      );
      return ints;
   }

   public final int getZzLexicalState() {
      return this.zzLexicalState;
   }

   public final String yytext() {
      return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
   }

   private static char[] zzUnpackCMap(String str) {
      char[] chars = new char[65536];
      int n = 0;
      int n2 = 0;

      while (n < 90) {
         int n3 = str.charAt(n++);
         char ch = str.charAt(n++);

         do {
            chars[n2++] = ch;
         } while (--n3 <= 0);
      }

      return chars;
   }

   private static int[] zzUnpackAction() {
      int[] ints = new int[45];
      int n = 0;
      n = zzUnpackAction(
         "\u0002\u0000\u0002\u0001\u0001\u0002\u0001\u0003\u0001\u0004\u0003\u0001\u0001\u0005\u0001\u0006\u0001\u0007\u0001\b\u0001\t\u0001\n\u0001\u000b\u0001\f\u0001\r\u0005\u0000\u0001\f\u0001\u000e\u0001\u000f\u0001\u0010\u0001\u0011\u0001\u0012\u0001\u0013\u0001\u0014\u0001\u0000\u0001\u0015\u0001\u0000\u0001\u0015\u0004\u0000\u0001\u0016\u0001\u0017\u0002\u0000\u0001\u0018",
         n,
         ints
      );
      return ints;
   }

   private static int[] zzUnpackAttribute() {
      int[] ints = new int[45];
      int n = 0;
      n = zzUnpackAttribute(
         "\u0002\u0000\u0001\t\u0003\u0001\u0001\t\u0003\u0001\u0006\t\u0002\u0001\u0001\t\u0005\u0000\b\t\u0001\u0000\u0001\u0001\u0001\u0000\u0001\u0001\u0004\u0000\u0002\t\u0002\u0000\u0001\t",
         n,
         ints
      );
      return ints;
   }

   private static int zzUnpackAttribute(String str, int n, int[] ints) {
      int n2 = 0;
      int n3 = n;
      int length = str.length();

      while (n2 < length) {
         int n4 = str.charAt(n2++);
         char ch = str.charAt(n2++);

         do {
            ints[n3++] = ch;
         } while (--n4 <= 0);
      }

      return n3;
   }

   public Yylex(InputStream in) {
       this(new InputStreamReader(in));
   }

   public final char yycharat(int n) {
      return this.zzBuffer[this.zzStartRead + n];
   }

   public final void yyclose(int n, short s, short s2) throws IOException {
      this.zzAtEOF = true;
      JsonParseException.getControlFlowTag();
      this.zzEndRead = this.zzStartRead;
      if (this.zzReader != null) {
         this.zzReader.close();
      }
   }

   private boolean zzRefill(long l) throws IOException {
      l = a ^ l;
      String controlFlowTag = JsonParseException.getControlFlowTag();
      if (this.zzStartRead > 0) {
         System.arraycopy(this.zzBuffer, this.zzStartRead, this.zzBuffer, 0, this.zzEndRead - this.zzStartRead);
         this.zzEndRead = this.zzEndRead - this.zzStartRead;
         this.zzCurrentPos = this.zzCurrentPos - this.zzStartRead;
         this.zzMarkedPos = this.zzMarkedPos - this.zzStartRead;
         this.zzStartRead = 0;
      }

      exitBlock: {
         int zzCurrentPos2 = this.zzCurrentPos;
         int length = this.zzBuffer.length;
         if (l >= 0L) {
            if (this.zzCurrentPos < length) {
               break exitBlock;
            }

            zzCurrentPos2 = this.zzCurrentPos;
            length = 2;
         }

         char[] chars = new char[zzCurrentPos2 * length];
         System.arraycopy(this.zzBuffer, 0, chars, 0, this.zzBuffer.length);
         this.zzBuffer = chars;
      }

      int n = this.zzReader.read(this.zzBuffer, this.zzEndRead, this.zzBuffer.length - this.zzEndRead);
      int n2 = n;
      if (l >= 0L) {
         if (n > 0) {
            this.zzEndRead += n;
            return false;
         }

         n2 = n;
      }

      if (n2 == 0) {
         int n3 = this.zzReader.read();
         if (n3 == -1) {
            return true;
         } else {
            this.zzBuffer[this.zzEndRead++] = (char)n3;
            return false;
         }
      } else {
         return true;
      }
   }

   private static int zzUnpackRowMap(String str, int n, int[] ints) {
      int n2 = 0;
      int n3 = n;
      int length = str.length();

      while (n2 < length) {
         int n4 = str.charAt(n2++) << 16;
         ints[n3++] = n4 | str.charAt(n2++);
      }

      return n3;
   }

   static {
      ZZ_CMAP = zzUnpackCMap(
         "\t\u0000\u0001\u0007\u0001\u0007\u0002\u0000\u0001\u0007\u0012\u0000\u0001\u0007\u0001\u0000\u0001\t\b\u0000\u0001\u0006\u0001\u0019\u0001\u0002\u0001\u0004\u0001\n\n\u0003\u0001\u001a\u0006\u0000\u0004\u0001\u0001\u0005\u0001\u0001\u0014\u0000\u0001\u0017\u0001\b\u0001\u0018\u0003\u0000\u0001\u0012\u0001\u000b\u0002\u0001\u0001\u0011\u0001\f\u0005\u0000\u0001\u0013\u0001\u0000\u0001\r\u0003\u0000\u0001\u000e\u0001\u0014\u0001\u000f\u0001\u0010\u0005\u0000\u0001\u0015\u0001\u0000\u0001\u0016ﾂ\u0000"
      );
      ZZ_ACTION = zzUnpackAction();
      ZZ_ROWMAP = zzUnpackRowMap();
      ZZ_ATTRIBUTE = zzUnpackAttribute();
   }

   public void yypushback(long l, int n) {
      if (n > this.yylength()) {
         this.zzScanError(2);
      }

      this.zzMarkedPos -= n;
   }

   public final void yyreset(Reader reader) {
      this.zzReader = reader;
      JsonParseException.getControlFlowTag();
      this.zzAtBOL = true;
      this.zzAtEOF = false;
      this.zzEndRead = this.zzStartRead = 0;
      this.zzCurrentPos = this.zzMarkedPos = 0;
      this.yyline = this.yychar = this.yycolumn = 0;
      this.zzLexicalState = 0;
   }

   public Yylex(Reader reader) {
      this.zzBuffer = new char[0];
      this.zzAtBOL = true;
      this.sb = new StringBuffer();
      this.zzReader = reader;
   }

   public int getYychar() {
      return this.yychar;
   }

   private void zzScanError(int n) {
      String str;
      try {
         str = ZZ_ERROR_MSG[n];
      } catch (ArrayIndexOutOfBoundsException e) {
         str = ZZ_ERROR_MSG[0];
      }

      throw new Error(str);
   }

   public Yytoken yylex(long l) throws IOException, JsonParseException {
      int n = 0;
      Exception e = null;
      l = a ^ l;
      long l2 = l ^ 75880169320127L;
      int zzEndRead2 = this.zzEndRead;
      String controlFlowTag = JsonParseException.getControlFlowTag();
      char[] zzBuffer2 = this.zzBuffer;
      char[] chars = ZZ_CMAP;
      int[] ints = ZZ_TRANS;
      int[] ints2 = ZZ_ROWMAP;
      int[] ints3 = ZZ_ATTRIBUTE;
      String str = controlFlowTag;

      while (true) {
         int zzMarkedPos2 = this.zzMarkedPos;
         this.yychar = this.yychar + (zzMarkedPos2 - this.zzStartRead);
         int n2 = -1;
         int n3 = this.zzCurrentPos = this.zzStartRead = zzMarkedPos2;
         this.zzState = ZZ_LEXSTATE[this.zzLexicalState];

         int n4;
         outerLoop:
         while (true) {
            if (n3 < zzEndRead2) {
               n4 = zzBuffer2[n3++];
               if (l >= 0L) {
                  ;
               }

               int n5 = ints[ints2[this.zzState] + chars[n4]];
               if (n5 != -1) {
                  this.zzState = n5;
                  int n6 = ints3[this.zzState];
                  if ((n6 & 1) != 1) {
                     continue;
                  }

                  n2 = this.zzState;
                  zzMarkedPos2 = n3;
                  if ((n6 & 0) != 0) {
                     continue;
                  }
               }

               this.zzMarkedPos = zzMarkedPos2;
               n = n2;
               if (l > 0L) {
                  break;
               }
            } else {
               n = this.zzAtEOF ? 1 : 0;
            }

            while (true) {
               while (n == 0) {
                  this.zzCurrentPos = n3;
                  this.zzMarkedPos = zzMarkedPos2;
                  int n7 = (this.zzRefill(l2)) ? 1 : 0;
                  n3 = this.zzCurrentPos;
                  zzMarkedPos2 = this.zzMarkedPos;
                  zzBuffer2 = this.zzBuffer;
                  zzEndRead2 = this.zzEndRead;
                  if (n7 != 0) {
                     n4 = -1;
                     if (l > 0L) {
                        this.zzMarkedPos = zzMarkedPos2;
                     }

                     n = n2;
                     if (l > 0L) {
                        break outerLoop;
                     }
                  } else {
                     n4 = zzBuffer2[n3++];
                     n7 = ints[ints2[this.zzState] + chars[n4]];
                     if (n7 != -1) {
                        this.zzState = n7;
                        int n8 = ints3[this.zzState];
                        if ((n8 & 1) != 1) {
                           continue outerLoop;
                        }

                        n2 = this.zzState;
                        zzMarkedPos2 = n3;
                        if ((n8 & 0) != 0) {
                           continue outerLoop;
                        }
                     }

                     this.zzMarkedPos = zzMarkedPos2;
                     n = n2;
                     if (l > 0L) {
                        break outerLoop;
                     }
                  }
               }

               n4 = -1;
               if (l >= 0L) {
                  this.zzMarkedPos = zzMarkedPos2;
               }

               n = n2;
               if (l > 0L) {
                  break outerLoop;
               }
            }
         }

         exitBlock: {
            exitBlock2: {
               exitBlock3: {
                  exitBlock4: {
                     switch (n < 0 ? n2 : ZZ_ACTION[n2]) {
                        case 1:
                           throw new JsonParseException(this.yychar, 0, new Character(this.yycharat(0)));
                        case 2:
                           controlFlowTag = this.yytext();
                           break exitBlock3;
                        case 3:
                        case 48:
                           continue;
                        case 4:
                           this.sb = null;
                           this.sb = new StringBuffer();
                           this.setZzLexicalState(2);
                           continue;
                        case 5:
                           return new Yytoken(1, null);
                        case 6:
                           Yytoken yytoken = new Yytoken(2, null);
                           if (l > 0L) {
                              return yytoken;
                           }

                           return yytoken;
                        case 7:
                           return new Yytoken(3, null);
                        case 8:
                           return new Yytoken(4, null);
                        case 9:
                           return new Yytoken(5, null);
                        case 10:
                           return new Yytoken(0, null);
                        case 11:
                           this.sb.append(this.yytext());
                           continue;
                        case 12:
                           this.sb.append('\u0000');
                        case 32:
                           controlFlowTag = str;
                           if (l >= 0L) {
                              continue;
                           }
                           break exitBlock2;
                        case 13:
                           this.setZzLexicalState(0);
                           return new Yytoken(0, this.sb.toString());
                        case 14:
                           this.sb.append('\u0000');
                           continue;
                        case 15:
                           this.sb.append('\u0000');
                           continue;
                        case 16:
                           this.sb.append('\u0000');
                           continue;
                        case 17:
                           this.sb.append('\u0000');
                        case 41:
                           controlFlowTag = str;
                           if (l >= 0L) {
                              continue;
                           }
                           break;
                        case 18:
                           this.sb.append('\u0000');
                           continue;
                        case 19:
                           this.sb.append('\u0000');
                           continue;
                        case 20:
                           this.sb.append('\u0000');
                           continue;
                        case 21:
                           controlFlowTag = this.yytext();
                           break exitBlock2;
                        case 22:
                           return new Yytoken(0, null);
                        case 23:
                           controlFlowTag = this.yytext();
                           break exitBlock;
                        case 24:
                           try {
                              controlFlowTag = this.yytext().substring(2);
                              break;
                           } catch (Exception e2) {
                              e = e2;
                              boolean flag = false;
                              break exitBlock4;
                           }
                        case 25:
                        case 26:
                        case 27:
                        case 29:
                        case 30:
                        case 31:
                        case 33:
                        case 34:
                        case 35:
                        case 36:
                        case 37:
                        case 38:
                        case 39:
                        case 40:
                        case 42:
                        case 43:
                        case 45:
                        case 46:
                           continue;
                        case 28:
                           controlFlowTag = str;
                           if (l >= 0L) {
                              continue;
                           }
                           break exitBlock;
                        case 44:
                           controlFlowTag = str;
                           if (l >= 0L) {
                              continue;
                           }
                           break exitBlock3;
                        case 47:
                           if (l > 0L) {
                              ;
                           }
                           continue;
                        default:
                           if (n4 == -1 && this.zzStartRead == this.zzCurrentPos) {
                              this.zzAtEOF = true;
                              return null;
                           }

                           this.zzScanError(1);
                           continue;
                     }

                     try {
                        int n9 = Integer.parseInt(controlFlowTag, 0);
                        this.sb.append((char)n9);
                        continue;
                     } catch (Exception e3) {
                        e = e3;
                        boolean flag2 = false;
                     }
                  }

                  Exception e4 = e;
                  throw new JsonParseException(this.yychar, 2, e4);
               }

               Long val = Long.valueOf(controlFlowTag);
               return new Yytoken(0, val);
            }

            Double d = Double.valueOf(controlFlowTag);
            return new Yytoken(0, d);
         }

         Boolean bool = Boolean.valueOf(controlFlowTag);
         return new Yytoken(0, bool);
      }
   }
}
