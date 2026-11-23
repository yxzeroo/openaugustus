package net.augustus.vendor.launcher.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class JSONArray extends ArrayList implements JSONAware, JSONStreamAware {
   private static final long serialVersionUID = 3957988303675231981L;

   public static void writeJSONString(long[] longs, Writer writer) throws IOException {
      if (longs == null) {
         writer.write("null");
      } else if (longs.length == 0) {
         writer.write("[]");
      } else {
         writer.write("[");
         writer.write(String.valueOf(longs[0]));

         for (int i = 1; i < longs.length; i++) {
            writer.write(",");
            writer.write(String.valueOf(longs[i]));
         }

         writer.write("]");
      }
   }

   public static String toJSONString(float[] floats) {
      StringWriter stringWriter = new StringWriter();

      try {
         writeJSONString6(floats, stringWriter);
         return stringWriter.toString();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public static void writeJSONString4(Collection items, Writer writer) throws IOException {
      if (items == null) {
         writer.write("null");
      } else {
         boolean flag = true;
         Iterator it = items.iterator();
         writer.write(91);

         while (it.hasNext()) {
            if (flag) {
               flag = false;
            } else {
               writer.write(44);
            }

            Object obj = it.next();
            if (obj == null) {
               writer.write("null");
            } else {
               JSONValue.writeJSONString(obj, writer);
            }
         }

         writer.write(93);
      }
   }

   public static String toJSONString2(Object[] array) {
      StringWriter stringWriter = new StringWriter();

      try {
         writeJSONString7(array, stringWriter);
         return stringWriter.toString();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public static void writeJSONString3(double[] doubles, Writer writer) throws IOException {
      if (doubles == null) {
         writer.write("null");
      } else if (doubles.length == 0) {
         writer.write("[]");
      } else {
         writer.write("[");
         writer.write(String.valueOf(doubles[0]));

         for (int i = 1; i < doubles.length; i++) {
            writer.write(",");
            writer.write(String.valueOf(doubles[i]));
         }

         writer.write("]");
      }
   }

   @Override
   public String toJSONString() {
      return toJSONString6(this);
   }

   @Override
   public String toString() {
      return this.toJSONString();
   }

   public static void writeJSONString2(byte[] bytes, Writer writer) throws IOException {
      if (bytes == null) {
         writer.write("null");
      } else if (bytes.length == 0) {
         writer.write("[]");
      } else {
         writer.write("[");
         writer.write(String.valueOf(bytes[0]));

         for (int i = 1; i < bytes.length; i++) {
            writer.write(",");
            writer.write(String.valueOf(bytes[i]));
         }

         writer.write("]");
      }
   }

   public static String toJSONString3(int[] ints) {
      StringWriter stringWriter = new StringWriter();

      try {
         writeJSONString10(ints, stringWriter);
         return stringWriter.toString();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public static void writeJSONString10(int[] ints, Writer writer) throws IOException {
      if (ints == null) {
         writer.write("null");
      } else if (ints.length == 0) {
         writer.write("[]");
      } else {
         writer.write("[");
         writer.write(String.valueOf(ints[0]));

         for (int i = 1; i < ints.length; i++) {
            writer.write(",");
            writer.write(String.valueOf(ints[i]));
         }

         writer.write("]");
      }
   }

   public static String toJSONString9(char[] chars) {
      StringWriter stringWriter = new StringWriter();

      try {
         writeJSONString8(chars, stringWriter);
         return stringWriter.toString();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public static String toJSONString6(Collection items) {
      StringWriter stringWriter = new StringWriter();

      try {
         writeJSONString4(items, stringWriter);
         return stringWriter.toString();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public static void writeJSONString5(boolean[] flags, Writer writer) throws IOException {
      if (flags == null) {
         writer.write("null");
      } else if (flags.length == 0) {
         writer.write("[]");
      } else {
         writer.write("[");
         writer.write(String.valueOf(flags[0]));

         for (int i = 1; i < flags.length; i++) {
            writer.write(",");
            writer.write(String.valueOf(flags[i]));
         }

         writer.write("]");
      }
   }

   public static void writeJSONString8(char[] chars, Writer writer) throws IOException {
      if (chars == null) {
         writer.write("null");
      } else if (chars.length == 0) {
         writer.write("[]");
      } else {
         writer.write("[\"");
         writer.write(String.valueOf(chars[0]));

         for (int i = 1; i < chars.length; i++) {
            writer.write("\",\"");
            writer.write(String.valueOf(chars[i]));
         }

         writer.write("\"]");
      }
   }

   @Override
   public void writeJSONString(Writer writer) throws IOException {
      writeJSONString4(this, writer);
   }

   public JSONArray(Collection items) {
      super(items);
   }

   public static void writeJSONString6(float[] floats, Writer writer) throws IOException {
      if (floats == null) {
         writer.write("null");
      } else if (floats.length == 0) {
         writer.write("[]");
      } else {
         writer.write("[");
         writer.write(String.valueOf(floats[0]));

         for (int i = 1; i < floats.length; i++) {
            writer.write(",");
            writer.write(String.valueOf(floats[i]));
         }

         writer.write("]");
      }
   }

   public static String toJSONString5(double[] doubles) {
      StringWriter stringWriter = new StringWriter();

      try {
         writeJSONString3(doubles, stringWriter);
         return stringWriter.toString();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public static void writeJSONString9(short[] sArr, Writer writer) throws IOException {
      if (sArr == null) {
         writer.write("null");
      } else if (sArr.length == 0) {
         writer.write("[]");
      } else {
         writer.write("[");
         writer.write(String.valueOf(sArr[0]));

         for (int i = 1; i < sArr.length; i++) {
            writer.write(",");
            writer.write(String.valueOf(sArr[i]));
         }

         writer.write("]");
      }
   }

   public static String toJSONString7(boolean[] flags) {
      StringWriter stringWriter = new StringWriter();

      try {
         writeJSONString5(flags, stringWriter);
         return stringWriter.toString();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public static void writeJSONString7(Object[] array, Writer writer) throws IOException {
      if (array == null) {
         writer.write("null");
      } else if (array.length == 0) {
         writer.write("[]");
      } else {
         writer.write("[");
         JSONValue.writeJSONString(array[0], writer);

         for (int i = 1; i < array.length; i++) {
            writer.write(",");
            JSONValue.writeJSONString(array[i], writer);
         }

         writer.write("]");
      }
   }

   public static String toJSONString8(byte[] bytes) {
      StringWriter stringWriter = new StringWriter();

      try {
         writeJSONString2(bytes, stringWriter);
         return stringWriter.toString();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public JSONArray() {
   }

   public static String toJSONString4(long[] longs) {
      StringWriter stringWriter = new StringWriter();

      try {
         writeJSONString(longs, stringWriter);
         return stringWriter.toString();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public static String toJSONString10(short[] sArr) {
      StringWriter stringWriter = new StringWriter();

      try {
         writeJSONString9(sArr, stringWriter);
         return stringWriter.toString();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

}
