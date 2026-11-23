package net.augustus.vendor.launcher.security;

import java.lang.management.ManagementFactory;
import java.util.List;

public class LaunchEnvironmentCheck {

   private static boolean isClassPresent(String str) {
      try {
         Class.forName(str, false, ClassLoader.getSystemClassLoader());
         return true;
      } catch (Throwable e) {
         return false;
      }
   }

   public static boolean isEnvironmentClean() {
      if (StartupAntiDebugCheck.isDebugDetected()) {
         return false;
      } else {

         for (String str : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            String str2 = str.toLowerCase();
            if (str2.contains("-agentlib:jdwp")
               || str2.contains("-xdebug")
               || str2.contains("-xrunjdwp")
               || str2.startsWith("-javaagent")
               || str2.contains("-xverify:none")) {
               return false;
            }
         }

         if (isClassPresent("com.sun.tools.attach.VirtualMachine")) {
            return false;
         } else if (isClassPresent("com.sun.tools.attach.AttachNotSupportedException")) {
            return false;
         } else if (isClassPresent("net.bytebuddy.agent.ByteBuddyAgent")) {
            return false;
         } else if (isClassPresent("net.bytebuddy.agent.builder.AgentBuilder")) {
            return false;
         } else if (isClassPresent("javassist.CtClass")) {
            return false;
         } else {
            return !isClassPresent("org.aspectj.weaver.loadtime.Agent");
         }
      }
   }



}
