package net.augustus.vendor.launcher;

import net.augustus.vendor.launcher.net.LauncherClientHandler;
import net.augustus.vendor.launcher.security.ClassIntegrityWatchdog;
import net.augustus.vendor.launcher.security.StartupAntiDebugCheck;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ElectricLauncherMain extends Application {
   private static Stage stage;
   private static Scene scene;
   public static long startTime;

   public static void main() {
      StartupAntiDebugCheck.runCheck();
      ClassIntegrityWatchdog.start();
      launch(new String[0]);
      while (LauncherClientHandler.archiveHolder.isAlive()) {
         sleep();
      }
   }

   private static void sleep() {
      try {
         Thread.sleep(50L);
      } catch (Exception e) {
      }
   }

   public static synchronized Stage getStage() {
      return stage;
   }

   @Override
   public void start(Stage stage2) throws Exception {
      while (!LauncherClientHandler.connected) {
         if (System.currentTimeMillis() - startTime > 60000L) {
            System.exit(0);
         }
         sleep();
      }

      Parent root = FXMLLoader.load(ElectricLauncherMain.class.getClassLoader().getResource("style.fxml"));
      scene = new Scene(root);
      stage2.setTitle("Electric-Launcher b1.37");
      stage2.setScene(scene);
      stage2.show();
      stage2.setResizable(false);
      stage2.setOnCloseRequest(windowEvent -> close());
      stage = stage2;
      stage.getIcons().add(new Image(ElectricLauncherMain.class.getClassLoader().getResourceAsStream("icon.jfif")));
   }

   private static void close() {
      if (LauncherClientHandler.archiveHolder == null) {
         System.exit(0);
      }
   }




   static {
      stage = null;
      startTime = 0L;
   }
}
