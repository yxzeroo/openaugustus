package net.augustus.vendor.launcher.security;

import java.lang.management.ManagementFactory;
import java.util.List;

public class ClassLoaderAntiDebugCheck {
   public static boolean checked;

   public static boolean isDebuggerOrAgentDetected() {
      List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
      checked = true;
      if (StartupAntiDebugCheck.isDebugDetected()) {
         return true;
      } else {
         for (String str : inputArguments) {
            String str2 = str.toLowerCase();
            if (str2.contains("-agentlib:jdwp")
               || str2.contains("-xdebug")
               || str2.contains("-xrunjdwp")
               || str2.startsWith("-javaagent")
               || str2.contains("-xverify:none")) {
               return true;
            }
         }

         if (isClassPresent("com.sun.tools.attach.VirtualMachine")) {
            return true;
         } else if (isClassPresent("com.sun.tools.attach.AttachNotSupportedException")) {
            return true;
         } else if (isClassPresent("net.bytebuddy.agent.ByteBuddyAgent")) {
            return true;
         } else if (isClassPresent("net.bytebuddy.agent.builder.AgentBuilder")) {
            return true;
         } else if (isClassPresent("javassist.CtClass")) {
            return true;
         } else {
            return isClassPresent("org.aspectj.weaver.loadtime.Agent");
         }
      }
   }

   private static boolean isClassPresent(String str) {
      try {
         Class.forName(str, false, ClassLoader.getSystemClassLoader());
         return true;
      } catch (Throwable e) {
         return false;
      }
   }




   static {
      checked = false;
   }
}
