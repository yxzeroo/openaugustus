package net.augustus.commands.impl;

import net.augustus.Augustus;
import net.augustus.ClientManagers;
import net.augustus.commands.Command;
import net.augustus.config.FeatureListJsonCodec;
import net.augustus.config.GsonConstants;
import net.augustus.settings.property.ConfigProperty;
import net.augustus.utils.UnaryFunction;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.utils.file.ClientPaths;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.augustus.modules.AbstractFeature;
import net.augustus.modules.Module;

public class MotionModifierCommand extends Command {
   private final String configDirectory = ClientPaths.CLIENT_DIR + File.separator + "motionmodifier" + File.separator + "motionmodifier";
   private CompletableFuture loadTask;
   private static final FeatureListJsonCodec CONFIG_CODEC = new FeatureListJsonCodec();

   private void listConfigs() {
      ClientFileUtil.ensureDirectory(this.configDirectory);
      File file = new File(this.configDirectory);
      File[] files = file.listFiles(file2 -> {
         return file2 != null && file2.isFile() && file2.getName().endsWith(".json");
      });
      if (files != null && files.length != 0) {
         String str = "Motion modifier scripts:";
         this.sendChat(str);

         for (File file3 : files) {
            String name = file3.getName();
            String str2 = name.substring(0, name.length() - 5);
            this.sendChat(str2);
         }
      } else {
         String str3 = "No motion modifier scripts found!";
         this.sendChat(str3);
      }
   }

   @Override
   public void helpMessage() {
      String str = "§c" + this.getCommand() + "§7 (Saves, loads, or lists motion modifier configs)";
      this.sendChat(str);
   }

   private void loadConfigAsync(String[] strings) {
      List settings = ClientManagers.settingManager.getSettings();
      String str = strings[2];
      List list = settings;
      this.loadConfig(list, str);
   }

   @Override
   public void commandAction(String[] strings, String str) {
      if (strings.length == 3) {
         if (strings[1].equalsIgnoreCase("load")) {
            if (this.loadTask == null || this.loadTask.isDone()) {
               this.loadTask = CompletableFuture.runAsync(() -> this.loadConfigAsync(strings));
            }

            return;
         }

         if (strings[1].equalsIgnoreCase("save")) {
            if (!Augustus.getInstance().isConfigEditingEnabled()) {
               String str2 = "You are not allowed to use this command in a protected config. Sorry :/";
               this.sendChat(str2);
               return;
            }

            String str3 = strings[2];
            String str4 = str3;
            this.saveConfig(str4);
            return;
         }
      } else if (strings.length == 2 && strings[1].equalsIgnoreCase("list")) {
         this.listConfigs();
         return;
      }

      this.sendUsageMessage();
   }

   public MotionModifierCommand() {
      super("mm");
      this.loadTask = null;
      if (!Files.exists(Paths.get(this.configDirectory))) {
         try {
            Files.createDirectories(Paths.get(this.configDirectory));
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }

   public void sendUsageMessage() {
      String str = "§7" + this.getCommand() + " [§csave/load/list§7] [§2fileName§7]";
      this.sendChat(str);
   }

   public void saveConfig(String str) {
      FeatureListJsonCodec featureListJsonCodec = CONFIG_CODEC;
      Module[] modules = new Module[]{ClientManagers.moduleManager.motionModifier};
      featureListJsonCodec.setFeatures2(modules);
      featureListJsonCodec = CONFIG_CODEC;
      UnaryFunction unaryFunction = obj -> {
         return !(((ConfigProperty)obj).getFeatureBase() instanceof Module) || obj instanceof AbstractFeature;
      };
      JsonObject json = featureListJsonCodec.serializeToObject(unaryFunction);
      if (json == null) {
         String str2 = "Failed to save the file §c" + str + "§7!";
         this.sendChat(str2);
      } else {
         String str3 = str + ".json";
         Gson gson = GsonConstants.GSON;
         String str4 = str3;
         ClientFileUtil.writeJsonObject(this.configDirectory, str4, json, gson);
         String str5 = "The file §c" + str + "§7 saved successfully!";
         this.sendChat(str5);
      }
   }

   private void loadConfig(List list, String str) {
      if (!Files.exists(Paths.get(this.configDirectory))) {
         try {
            Files.createDirectories(Paths.get(this.configDirectory));
         } catch (IOException e) {
            e.printStackTrace();
         }
      }

      File file = new File(this.configDirectory + "/" + str + ".json");
      if (!file.exists()) {
         String str2 = "The file §c" + str + "§7 doesn't exist!";
         this.sendChat(str2);
      } else {
         String configDirectory2 = this.configDirectory;
         String name = file.getName();
         Object obj = null;
         String str3 = name;
         JsonObject json = ClientFileUtil.readJsonObject(configDirectory2, str3, (Runnable)obj);
         if (json != null) {
            FeatureListJsonCodec featureListJsonCodec = CONFIG_CODEC;
            Module[] modules = new Module[]{ClientManagers.moduleManager.motionModifier};
            featureListJsonCodec.setFeatures2(modules);
            FeatureListJsonCodec featureListJsonCodec2 = CONFIG_CODEC;
            UnaryFunction unaryFunction = obj2 -> {
               return !(((ConfigProperty)obj2).getFeatureBase() instanceof Module) || obj2 instanceof AbstractFeature;
            };
            boolean flag = true;
            boolean flag2 = featureListJsonCodec2.deserializeFromObject(json, flag, unaryFunction);
            if (!flag2) {
               String str4 = "Failed to load motion modifier configuration";
               this.sendChat(str4);
            } else {
               String str5 = "The file §c" + str + "§7 loaded successfully!";
               this.sendChat(str5);
            }
         }
      }
   }

}
