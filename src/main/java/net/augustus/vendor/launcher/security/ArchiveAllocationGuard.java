package net.augustus.vendor.launcher.security;

import net.augustus.vendor.launcher.net.LauncherClientHandler;
import net.augustus.vendor.launcher.packet.C2SRemoteClassRequestPacket;
import java.lang.management.ManagementFactory;

public class ArchiveAllocationGuard {

   public static byte[] allocateArchiveBuffer(byte[] bytes, int n) {

      if (StartupAntiDebugCheck.isDebugDetected()) {
         LauncherClientHandler.getInstance().sendPacket(new C2SRemoteClassRequestPacket(bytes, "net/h/"));
         return new byte[1];
      }

      boolean flag = false;
      for (String str : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
         String str2 = str.toLowerCase();
         if (str2.contains("-agentlib:jdwp")
            || str2.contains("-xdebug")
            || str2.contains("-xrunjdwp")
            || str2.startsWith("-javaagent")
            || str2.contains("-xverify:none")) {
            LauncherClientHandler.getInstance().sendPacket(new C2SRemoteClassRequestPacket(bytes, "net/i/"));
            flag = true;
            break;
         }
      }

      if (isClassPresent("com.sun.tools.attach.VirtualMachine") || isClassPresent("com.sun.tools.attach.AttachNotSupportedException")) {
         LauncherClientHandler.getInstance().sendPacket(new C2SRemoteClassRequestPacket(bytes, "net/j/"));
         flag = true;
      }

      if (isClassPresent("net.bytebuddy.agent.ByteBuddyAgent")
         || isClassPresent("net.bytebuddy.agent.builder.AgentBuilder")
         || isClassPresent("javassist.CtClass")
         || isClassPresent("org.aspectj.weaver.loadtime.Agent")) {
         LauncherClientHandler.getInstance().sendPacket(new C2SRemoteClassRequestPacket(bytes, "net/k/"));
         flag = true;
      }

      return flag ? new byte[1] : new byte[n];
   }

   private static boolean isClassPresent(String str) {
      try {
         Class.forName(str, false, ClassLoader.getSystemClassLoader());
         return true;
      } catch (Throwable e) {
         return false;
      }
   }




}
