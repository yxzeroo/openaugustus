package net.augustus.vendor.launcher.json;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

public class JSONValue {
   private static final long a = 128618945181187L;

   public static void escape(String str, StringBuffer sb) {
      int length = str.length();

      for (int i = 0; i < length; i++) {
         char ch = str.charAt(i);
         switch (ch) {
            case '\b':
               sb.append("\\b");
               continue;
            case '\t':
               sb.append("\\t");
               continue;
            case '\n':
               sb.append("\\n");
               continue;
            case '\f':
               sb.append("\\f");
               continue;
            case '\r':
               sb.append("\\r");
               continue;
            case '"':
               sb.append("\\\"");
               continue;
            case '/':
               sb.append("\\/");
               continue;
            case '\\':
               sb.append("\\\\");
               continue;
         }

         if ((ch < 0 || ch > 31) && (ch < 127 || ch > 159)) {
            if (ch < 8192 || ch > 8447) {
               sb.append(ch);
               continue;
            }
         }

         String str2 = Integer.toHexString(ch);
         sb.append("\\u");

         for (int j = 0; j < 4 - str2.length(); j++) {
            sb.append('0');
         }

         sb.append(str2.toUpperCase());
      }
   }

   public static String escape(String str) {
      if (str == null) {
         return null;
      } else {
         StringBuffer sb = new StringBuffer();
         escape(str, sb);
         return sb.toString();
      }
   }

   public static Object parseWithException2(int n, byte b, Reader reader, int n2) throws IOException, JsonParseException {
      long l = ((long)n << 32 | (long)b << 56 >>> 32 | (long)n2 << 40 >>> 40) ^ a;
      long l2 = l ^ 16948404116413L;
      JSONParser jsonParser = new JSONParser();
      return jsonParser.parse(reader, l2);
   }

   /** @deprecated */
   public static Object parse(Reader reader, long l) {
      l = a ^ l;
      long l2 = l ^ 48243072627398L;

      try {
         JSONParser jsonParser = new JSONParser();
         return jsonParser.parse(reader, l2);
      } catch (Exception e) {
         return null;
      }
   }

   public static String toJSONString(Object obj) {
      StringWriter stringWriter = new StringWriter();

      try {
         writeJSONString(obj, stringWriter);
         return stringWriter.toString();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public static void writeJSONString(Object obj, Writer writer) throws IOException {
      if (obj == null) {
         writer.write("null");
      } else if (obj instanceof String) {
         writer.write(34);
         writer.write(escape((String)obj));
         writer.write(34);
      } else if (obj instanceof Double) {
         if (!((Double)obj).isInfinite() && !((Double)obj).isNaN()) {
            writer.write(obj.toString());
         } else {
            writer.write("null");
         }
      } else if (!(obj instanceof Float)) {
         if (obj instanceof Number) {
            writer.write(obj.toString());
         } else if (obj instanceof Boolean) {
            writer.write(obj.toString());
         } else if (obj instanceof JSONStreamAware) {
            ((JSONStreamAware)obj).writeJSONString(writer);
         } else if (obj instanceof JSONAware) {
            writer.write(((JSONAware)obj).toJSONString());
         } else if (obj instanceof Map) {
            JSONObject.writeJSONString((Map)obj, writer);
         } else if (obj instanceof Collection) {
            JSONArray.writeJSONString4((Collection)obj, writer);
         } else if (obj instanceof byte[]) {
            JSONArray.writeJSONString2((byte[])obj, writer);
         } else if (obj instanceof short[]) {
            JSONArray.writeJSONString9((short[])obj, writer);
         } else if (obj instanceof int[]) {
            JSONArray.writeJSONString10((int[])obj, writer);
         } else if (obj instanceof long[]) {
            JSONArray.writeJSONString((long[])obj, writer);
         } else if (obj instanceof float[]) {
            JSONArray.writeJSONString6((float[])obj, writer);
         } else if (obj instanceof double[]) {
            JSONArray.writeJSONString3((double[])obj, writer);
         } else if (obj instanceof boolean[]) {
            JSONArray.writeJSONString5((boolean[])obj, writer);
         } else if (obj instanceof char[]) {
            JSONArray.writeJSONString8((char[])obj, writer);
         } else if (obj instanceof Object[]) {
            JSONArray.writeJSONString7((Object[])obj, writer);
         } else {
            writer.write(obj.toString());
         }
      } else {
         if (!((Float)obj).isInfinite() && !((Float)obj).isNaN()) {
            writer.write(obj.toString());
         } else {
            writer.write("null");
         }
      }
   }

   /** @deprecated */
   public static Object parse2(String str, long l) {
      l = a ^ l;
      long l2 = l ^ 32429272102447L;
      StringReader stringReader = new StringReader(str);
      return parse(stringReader, l2);
   }

   public static Object parseWithException(String str, byte b, int n, int n2) throws JsonParseException {
      long l = ((long)b << 56 | (long)n << 32 >>> 8 | (long)n2 << 40 >>> 40) ^ a;
      long l2 = l ^ 52796174813594L;
      int n3 = (int)((l ^ 52796174813594L) >>> 32);
      int n4 = (int)((l ^ 52796174813594L) << 32 >>> 48);
      int n5 = (int)(l2 << 48 >>> 48);
      JSONParser jsonParser = new JSONParser();
      return jsonParser.parse2(str, n3, (short)n4, (char)n5);
   }

}
