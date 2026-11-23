package net.augustus.vendor.launcher.security;

import java.lang.management.ManagementFactory;

public class StartupAntiDebugCheck {
   private static volatile boolean debugDetected;

   public static boolean isDebugDetected() {
      return debugDetected;
   }

   public static void runCheck() {
      if (hasDebugJvmArguments((byte)0, 0L) || hasJavaAgentCommand(0L)) {
         debugDetected = true;
      }
   }

   private static boolean hasJavaAgentCommand(long l) {
      try {
         Class.forName("java.lang.instrument.Instrumentation");
         String cmd = System.getProperty("sun.java.command");
         return cmd != null && cmd.contains("javaagent");
      } catch (ClassNotFoundException e) {
         return false;
      }
   }

   private static boolean hasDebugJvmArguments(byte b, long l) {
      try {
         for (String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            if (arg.contains("-Xdebug")
               || arg.contains("-agentlib:jdwp")
               || arg.contains("-javaagent")
               || arg.contains("-Xrunjdwp")
               || arg.contains("-Xnoagent")
               || arg.contains("-verbose:class")
               || arg.contains("-XX:+TraceClassLoading")) {
               return true;
            }
         }
      } catch (Throwable e) {
      }
      return false;
   }



}
