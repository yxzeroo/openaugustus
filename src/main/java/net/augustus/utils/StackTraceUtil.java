package net.augustus.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTraceUtil {
   public static String getStackTraceString(Throwable t) {
      StringWriter stringWriter = new StringWriter();
      PrintWriter writer = new PrintWriter(stringWriter);
      t.printStackTrace(writer);
      return stringWriter.toString();
   }
}
