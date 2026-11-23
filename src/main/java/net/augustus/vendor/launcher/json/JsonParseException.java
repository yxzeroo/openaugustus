package net.augustus.vendor.launcher.json;

public class JsonParseException extends Exception {
   private int errorType;
   private int position;
   public static final int ERROR_UNEXPECTED_CHAR = 0;
   private static final long serialVersionUID = -7880698968187728547L;
   public static final int ERROR_UNEXPECTED_TOKEN = 1;
   public static final int ERROR_UNEXPECTED_EXCEPTION;
   private Object unexpectedObject;
   private static String controlFlowTag;

   public void setPosition(int n) {
      this.position = n;
   }

   public void setErrorType(int n) {
      this.errorType = n;
   }

   public JsonParseException(int n) {
      this(-1, n, null);
   }

   public JsonParseException(int n, Object obj) {
      this(-1, n, obj);
   }

   @Override
   public String getMessage() {
      String controlFlowTag2 = getControlFlowTag();
      StringBuffer sb = new StringBuffer();
      switch (this.errorType) {
         case 0:
            sb.append("Unexpected character (").append(this.unexpectedObject).append(") at position ").append(this.position).append(".");
            break;
         case 1:
            sb.append("Unexpected token ").append(this.unexpectedObject).append(" at position ").append(this.position).append(".");
            break;
         case 2:
            sb.append("Unexpected exception at position ").append(this.position).append(": ").append(this.unexpectedObject);
            break;
         default:
            sb.append("Unkown error at position ").append(this.position).append(".");
      }

      return sb.toString();
   }

   public int getPosition() {
      return this.position;
   }

   public JsonParseException(int n, int n2, Object obj) {
      this.position = n;
      this.errorType = n2;
      this.unexpectedObject = obj;
   }

   public int getErrorType() {
      return this.errorType;
   }

   public Object getUnexpectedObject() {
      return this.unexpectedObject;
   }

   public void setUnexpectedObject(Object obj) {
      this.unexpectedObject = obj;
   }

   public static void setControlFlowTag(String str) {
      controlFlowTag = str;
   }

   public static String getControlFlowTag() {
      return controlFlowTag;
   }

   static {
      setControlFlowTag("s269ub");
      long l = -5012384678775095294L;
      ERROR_UNEXPECTED_EXCEPTION = (int)l;
   }
}
