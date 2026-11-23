package net.augustus.vendor.launcher.json;

public class Yytoken {
   public static final int TYPE_VALUE = 0;
   public static final int TYPE_LEFT_SQUARE;
   public static final int TYPE_RIGHT_SQUARE;
   public Object value;
   public static final int TYPE_COLON;
   public static final int TYPE_RIGHT_BRACE;
   public int type = 0;
   public static final int TYPE_COMMA;
   public static final int TYPE_EOF = -1;
   public static final int TYPE_LEFT_BRACE = 1;

   public Yytoken(int n, Object obj) {
      this.value = null;
      this.type = n;
      this.value = obj;
   }

   @Override
   public String toString() {
      String controlFlowTag = JsonParseException.getControlFlowTag();
      StringBuffer sb = new StringBuffer();
      switch (this.type) {
         case -1:
            sb.append("END OF FILE");
            break;
         case 0:
            sb.append("VALUE(").append(this.value).append(")");
            break;
         case 1:
            sb.append("LEFT BRACE({)");
            break;
         case 2:
            sb.append("RIGHT BRACE(})");
            break;
         case 3:
            sb.append("LEFT SQUARE([)");
            break;
         case 4:
            sb.append("RIGHT SQUARE(])");
            break;
         case 5:
            sb.append("COMMA(,)");
            break;
         case 6:
            sb.append("COLON(:)");
      }

      return sb.toString();
   }

   static {
      long[] longs = new long[]{-2345420157888757754L, 1405748254884233220L, 5831027890891784197L, -1289264141906739198L, -2513248779528830973L};
      TYPE_LEFT_SQUARE = (int)longs[4];
      TYPE_RIGHT_SQUARE = (int)longs[1];
      TYPE_COLON = (int)longs[0];
      TYPE_RIGHT_BRACE = (int)longs[3];
      TYPE_COMMA = (int)longs[2];
   }
}
