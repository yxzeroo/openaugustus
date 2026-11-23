package net.augustus.vendor.launcher;

import net.augustus.vendor.launcher.util.Base64Codec;
import net.augustus.vendor.launcher.util.XorByteTable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CredentialStorage {
   public static String dataDirectory;

   public static void loadCredentials(TextField textField, PasswordField passwordField) {
      ensureDirectoryExists(dataDirectory + "saving");
      File file = new File(dataDirectory + "saving/user.txt");
      if (file.exists()) {
         try {
            String line = Files.newBufferedReader(file.toPath()).readLine();
            if (line == null) {
               return;
            }
            String decoded = new String(
               Base64Codec.getRfc4648()
                  .decode3(line)
            );
            textField.setText(decoded.split(":")[0]);
            passwordField.setText(decoded.substring(decoded.indexOf(":") + 1, decoded.length() - 1));
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }

   public static void saveCredentials(String str, String str2) {
      try (PrintWriter pw = new PrintWriter(dataDirectory + "saving/user.txt")) {
         pw.println(
            Base64Codec.getRfc46482()
               .encodeToString((str + ":" + str2 + ":").getBytes(StandardCharsets.UTF_8))
         );
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static String readFirstLine(String str, String str2) {
      try {
         ensureDirectoryExists(str);
         File file = new File(str, str2);
         if (!file.exists()) {
            return null;
         }
         FileReader fr = new FileReader(file);
         BufferedReader br = new BufferedReader(fr);
         String line = br.readLine();
         fr.close();
         br.close();
         return line;
      } catch (Exception e) {
         return null;
      }
   }

   public static void ensureDirectoryExists(String str) {
      Path path = new File(str).toPath();
      if (!Files.exists(path)) {
         try {
            Files.createDirectories(path);
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }

   public static void saveRememberLogin(boolean flag) {
      try (PrintWriter pw = new PrintWriter(dataDirectory + "saving/remember.txt")) {
         pw.println(
            Base64Codec.getRfc46482()
               .encodeToString((flag ? "True" : "False").getBytes(StandardCharsets.UTF_8))
         );
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void loadRememberLogin(CheckBox checkBox) {
      new XorByteTable().populate();
      ensureDirectoryExists(dataDirectory + "saving");
      File file = new File(dataDirectory + "saving/remember.txt");
      if (file.exists()) {
         try {
            String line = Files.newBufferedReader(file.toPath()).readLine();
            if (line == null) {
               return;
            }
            String decoded = new String(
               Base64Codec.getRfc4648()
                  .decode3(line)
            );
            checkBox.setSelected(decoded.equals("True"));
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }




   static {
      dataDirectory = System.getenv("APPDATA") + "/ElectricLauncher/data/";
   }
}
