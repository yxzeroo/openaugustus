package net.augustus.settings;

import net.augustus.Augustus;
import net.augustus.config.DescriptionYamlManager;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.utils.file.ClientPaths;
import net.augustus.utils.file.DirectoryWatchService;
import net.augustus.utils.file.DirectoryWatcher;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SettingDescriptionManager {
   public static final String DESCRIPTIONS_DIR = ClientPaths.CLIENT_DIR + File.separator + "settings" + File.separator + "descriptions" + File.separator;
   private final DescriptionYamlManager descriptionYamlManager = new DescriptionYamlManager();

   public DescriptionYamlManager getDescriptionYamlManager() {
      return this.descriptionYamlManager;
   }

   private static void reloadSelectedDescriptionFile(BooleansSetting booleansSetting) {
      SettingDescriptionManager manager = Augustus.getInstance().getSettingDescriptionManager();
      String selectedValue = booleansSetting.getSelectedValue();
      String str = selectedValue;
      manager.loadDescriptions(str);
   }

   public void writePresetDescriptions() {
      ClientFileUtil.ensureDirectory(DESCRIPTIONS_DIR);
      DescriptionYamlManager.getParseFlags();
      String str = DESCRIPTIONS_DIR + "Preset.yml";
      File file = new File(str);
      if (file.exists()) {
         System.out.println(file.delete());
      }

      try {
         this.descriptionYamlManager.saveToFile(str);
      } catch (Exception e) {
         System.err.println("Failed to save descriptions to: " + str);
         e.printStackTrace();
      }
   }

   private static void onDescriptionFileDeleted(BooleansSetting booleansSetting, Path path) {
      String[] strings = Augustus.getInstance().getSettingDescriptionManager().listDescriptionFiles();
      booleansSetting.setOptions(strings);
      SettingDescriptionManager manager = Augustus.getInstance().getSettingDescriptionManager();
      String selectedValue = booleansSetting.getSelectedValue();
      String[] options = booleansSetting.getOptions();
      String str = selectedValue;
      String str2 = manager.resolveValidOption(str, options);
      booleansSetting.setSelectedValue(str2);
   }

   public synchronized void loadDescriptions(String str) {
      String str2 = DESCRIPTIONS_DIR + str + ".yml";
      ClientFileUtil.ensureDirectory(DESCRIPTIONS_DIR);

      try {
         this.descriptionYamlManager.loadAndApply(str2);
      } catch (Exception e) {
         System.err.println("Failed to read descriptions from: " + str2);
         e.printStackTrace();
      }
   }

   public String[] listDescriptionFiles() {
      ArrayList list = new ArrayList();
      DescriptionYamlManager.getParseFlags();
      File file = new File(DESCRIPTIONS_DIR);
      ClientFileUtil.ensureDirectory(DESCRIPTIONS_DIR);
      File[] files = file.listFiles();
      if (files != null) {
         for (File file2 : files) {
            String name = file2.getName();
            if (name.endsWith(".yml")) {
               if (!name.equals("Preset.yml")) {
                  String str = name.substring(0, Math.max(0, name.length() - 4));
                  list.add(str);
               }
            }
         }
      }

      return (String[])list.toArray(new String[0]);
   }

   public String resolveValidOption(String str, String[] strings) {
      int[] parseFlags = DescriptionYamlManager.getParseFlags();
      if (strings.length < 1) {
         return "";
      } else {
         for (String str2 : strings) {
            if (str2.equals(str)) {
               return str;
            }
         }

         return strings[0];
      }
   }

   public void initialize() {
      String str = Augustus.getInstance().getName().toLowerCase() + "/settings/descriptions";
      boolean flag = true;
      String str2 = str;
      ClientFileUtil.extractResource("assets/augustus/Default.yml", str2, flag);
      DescriptionYamlManager.getParseFlags();
      String str3 = "Default";
      this.loadDescriptions(str3);
      this.writePresetDescriptions();
      BooleansSetting descriptions = Augustus.getInstance().getModuleManager().clickGui.descriptions;
      String[] strings = Augustus.getInstance().getSettingDescriptionManager().listDescriptionFiles();
      descriptions.setOptions(strings);
      SettingDescriptionManager manager = Augustus.getInstance().getSettingDescriptionManager();
      str = descriptions.getSelectedValue();
      String[] options = descriptions.getOptions();
      String str4 = str;
      String str5 = manager.resolveValidOption(str4, options);
      descriptions.setSelectedValue(str5);
      DirectoryWatcher directoryWatcher = DirectoryWatchService.watch(DESCRIPTIONS_DIR);
      Consumer consumer = obj -> {
         String[] strings2 = Augustus.getInstance().getSettingDescriptionManager().listDescriptionFiles();
         descriptions.setOptions(strings2);
         SettingDescriptionManager manager2 = Augustus.getInstance().getSettingDescriptionManager();
         String selectedValue = descriptions.getSelectedValue();
         String[] options2 = descriptions.getOptions();
         String str6 = selectedValue;
         String str7 = manager2.resolveValidOption(str6, options2);
         descriptions.setSelectedValue(str7);
      };
      directoryWatcher = directoryWatcher.onCreate(consumer);
      Consumer consumer2 = obj2 -> {
         File file = ((Path)obj2).toFile();
         String name = file.getName();
         String str8 = name.substring(0, name.lastIndexOf(46));
         if (str8.equals(descriptions.getSelectedValue())) {
            CompletableFuture.runAsync(() -> reloadSelectedDescriptionFile(descriptions));
         }
      };
      directoryWatcher = directoryWatcher.onModify(consumer2);
      Consumer consumer3 = obj3 -> onDescriptionFileDeleted(descriptions, (Path)obj3);
      directoryWatcher.onDelete(consumer3).register();
   }

}
