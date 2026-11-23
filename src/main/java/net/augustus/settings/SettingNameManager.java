package net.augustus.settings;

import net.augustus.Augustus;
import net.augustus.ClientManagers;
import net.augustus.config.DescriptionYamlManager;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.utils.file.ClientPaths;
import net.augustus.utils.file.DirectoryWatchService;
import net.augustus.utils.file.DirectoryWatcher;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SettingNameManager {
   public static String SETTINGS_NAMES_DIR = ClientPaths.CLIENT_DIR + File.separator + "settings" + File.separator + "settingsnames" + File.separator;

   public void initialize() {
      String str = Augustus.getInstance().getName().toLowerCase() + "/settings/settingsnames";
      boolean flag = true;
      String str2 = str;
      ClientFileUtil.extractResource("assets/augustus/English_New.txt", str2, flag);
      DescriptionYamlManager.getParseFlags();
      String str3 = "English_New";
      this.loadLanguageFile(str3);
      this.exportDefaultNames();
      BooleansSetting settingNames = Augustus.getInstance().getModuleManager().clickGui.settingNames;
      String[] strings = Augustus.getInstance().getSettingNameManager().listLanguageFiles();
      settingNames.setOptions(strings);
      SettingNameManager settingNameManager = Augustus.getInstance().getSettingNameManager();
      str = settingNames.getSelectedValue();
      String[] options = settingNames.getOptions();
      String str4 = str;
      String str5 = settingNameManager.resolveValidLanguage(str4, options);
      settingNames.setSelectedValue(str5);
      DirectoryWatcher directoryWatcher = DirectoryWatchService.watch(SETTINGS_NAMES_DIR);
      Consumer consumer = obj -> onNameFileCreated(settingNames, (Path)obj);
      directoryWatcher = directoryWatcher.onCreate(consumer);
      Consumer consumer2 = obj2 -> onNameFileModified(settingNames, (Path)obj2);
      directoryWatcher = directoryWatcher.onModify(consumer2);
      Consumer consumer3 = obj3 -> onNameFileDeleted(settingNames, (Path)obj3);
      directoryWatcher.onDelete(consumer3).register();
   }

   private static void onNameFileDeleted(BooleansSetting booleansSetting, Path path) {
      String[] strings = Augustus.getInstance().getSettingNameManager().listLanguageFiles();
      booleansSetting.setOptions(strings);
      SettingNameManager settingNameManager = Augustus.getInstance().getSettingNameManager();
      String selectedValue = booleansSetting.getSelectedValue();
      String[] options = booleansSetting.getOptions();
      String str = selectedValue;
      String str2 = settingNameManager.resolveValidLanguage(str, options);
      booleansSetting.setSelectedValue(str2);
   }

   private static void onNameFileModified(BooleansSetting booleansSetting, Path path) {
      File file = path.toFile();
      String name = file.getName();
      String str = name.substring(0, name.lastIndexOf(46));
      if (str.equals(booleansSetting.getSelectedValue())) {
         CompletableFuture.runAsync(() -> {
            SettingNameManager settingNameManager = Augustus.getInstance().getSettingNameManager();
            String selectedValue = booleansSetting.getSelectedValue();
            settingNameManager.loadLanguageFile(selectedValue);
         });
      }
   }

   public void exportDefaultNames() {
      ClientFileUtil.ensureDirectory(SETTINGS_NAMES_DIR);
      DescriptionYamlManager.getParseFlags();
      String str = Augustus.getInstance().getName().toLowerCase() + "/settings/settingsnames";
      boolean flag = false;
      String str2 = str;
      ClientFileUtil.extractResource("assets/augustus/English_New.txt", str2, flag);

      try (BufferedWriter writer = new BufferedWriter(new FileWriter(SETTINGS_NAMES_DIR + "English_New.txt"))) {
         for (Setting setting : (Iterable<Setting>)(ClientManagers.settingManager.getSettings())) {
            if (!setting.getParentModule().equals(ClientManagers.moduleManager.motionModifier) && !setting.getParentModule().equals(ClientManagers.moduleManager.clientAntiCheat)) {
               String[] translationKeys = setting.getTranslationKeys();

               for (String str3 : translationKeys) {
                  String str4 = setting.getParentModule().getCategorys().name().toLowerCase(Locale.ROOT)
                     + "."
                     + setting.getParentModuleName().toLowerCase(Locale.ROOT)
                     + "."
                     + str3.toLowerCase(Locale.ROOT);
                  String str5 = setting.getTranslatedName() + ";";
                  writer.write(str4 + "::" + str5);
                  writer.newLine();
               }
            }
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public synchronized void loadLanguageFile(String str) {
      int[] parseFlags = DescriptionYamlManager.getParseFlags();
      if (str.equals("English_Default")) {
         for (Setting setting : (Iterable<Setting>)(ClientManagers.settingManager.getSettings())) {
            String str2 = "";
            setting.applyTranslation(str2, str);
         }
      } else {
         String str3 = SETTINGS_NAMES_DIR + str + ".txt";
         ClientFileUtil.ensureDirectory(SETTINGS_NAMES_DIR);
         File file = new File(str3);
         if (!file.exists()) {
            this.exportDefaultNames();
         }

         try {
            BufferedReader reader = new BufferedReader(new FileReader(str3));

            String str4;
            while ((str4 = reader.readLine()) != null) {
               String[] strings = str4.split("::");
               if (strings.length > 1) {
                  String[] strings2 = strings[0].split("\\.");
                  if (strings2.length > 1) {
                     for (Setting setting2 : (Iterable<Setting>)(ClientManagers.settingManager.getSettings())) {
                        if (strings2[0].equalsIgnoreCase(setting2.getParentModule().getCategorys().name()) && strings2[1].equalsIgnoreCase(setting2.getParentModule().getName())) {
                           boolean flag = false;

                           for (String str5 : setting2.getTranslationKeys()) {
                              if (strings2[2].equalsIgnoreCase(str5)) {
                                 String str6 = strings2[2];
                                 String str7 = this.joinTranslationValue(strings);
                                 String str8 = str6;
                                 setting2.applyTranslation(str8, str7);
                                 flag = true;
                                 break;
                              }
                           }

                           if (flag) {
                              break;
                           }
                        }
                     }
                  }
               }
            }

            reader.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }

   private static void onNameFileCreated(BooleansSetting booleansSetting, Path path) {
      String[] strings = Augustus.getInstance().getSettingNameManager().listLanguageFiles();
      booleansSetting.setOptions(strings);
      SettingNameManager settingNameManager = Augustus.getInstance().getSettingNameManager();
      String selectedValue = booleansSetting.getSelectedValue();
      String[] options = booleansSetting.getOptions();
      String str = selectedValue;
      String str2 = settingNameManager.resolveValidLanguage(str, options);
      booleansSetting.setSelectedValue(str2);
   }

   private String joinTranslationValue(String[] strings) {
      StringBuilder sb = new StringBuilder();
      DescriptionYamlManager.getParseFlags();

      for (int i = 1; i < strings.length; i++) {
         sb.append(strings[i]);
         if (i > 1 && i < strings.length - 1) {
            sb.append("::");
         }
      }

      return sb.deleteCharAt(sb.length() - 1).toString();
   }

   public String[] listLanguageFiles() {
      ArrayList list = new ArrayList();
      DescriptionYamlManager.getParseFlags();
      File file = new File(SETTINGS_NAMES_DIR);
      ClientFileUtil.ensureDirectory(SETTINGS_NAMES_DIR);
      File[] files = file.listFiles();
      list.add("English_Default");
      if (files != null) {
         for (File file2 : files) {
            String name = file2.getName();
            if (name.endsWith(".txt")) {
               String str = name.substring(0, Math.max(0, name.length() - 4));
               list.add(str);
            }
         }
      }

      return (String[])list.toArray(new String[0]);
   }

   public String resolveValidLanguage(String str, String[] strings) {
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

}
