package net.augustus.vendor.launcher;


import net.augustus.vendor.launcher.net.LauncherNetworkClient;
public class LauncherBootstrap {

   public static void main(String[] args) {
      new LauncherNetworkClient().start();
      ElectricLauncherMain.startTime = System.currentTimeMillis();
      if (new Exception().getStackTrace().length <= 1) {
         ElectricLauncherMain.main();
      }
   }



}
