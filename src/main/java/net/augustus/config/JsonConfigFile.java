package net.augustus.config;

import net.augustus.Augustus;
import net.augustus.settings.Setting;
import net.augustus.utils.UnaryFunction;
import net.augustus.utils.file.ClientFileUtil;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class JsonConfigFile implements NamedConfigComponent {
   public static final String BASE_CONFIG_DIR;
   private boolean loaded;
   public final JsonFileStorageHandler storageHandler;
   private JsonElement cachedJson = null;

   public String getBackupDirectory() {
      StringBuilder sb = new StringBuilder().append(BASE_CONFIG_DIR).append(File.separator);
      return sb.append(this.getSubDirectory())
         .append(File.separator)
         .append("backups")
         .append(File.separator)
         .append(this.getName())
         .toString();
   }

   private void pruneOldBackups(List list, File file) {
      Setting[] cachedSettings = ConfigManager.getCachedSettings();
      if (list.size() > 10) {
         List list2 = (List)(list.stream().sorted().limit((long)(list.size() - 10)).collect(Collectors.toList()));
         list2.forEach(obj -> deleteBackupFile(file, (Long)obj));
      }
   }

   public abstract String getSubDirectory();

   private static void deleteBackupFile(File file, Long val) {
      ClientFileUtil.moveToTrashOrDelete(new File(file, val + ".json"));
   }

   private void writeBackup(JsonElement elem) {
      if (this.isBackupEnabled()) {
         File file = new File(this.getBackupDirectory());
         File[] files = file.listFiles(file2 -> file2.getName().endsWith(".json"));
         String str = String.valueOf(System.currentTimeMillis());
         if (files != null) {
            ArrayList list = Lists.newArrayList(files);
            ArrayList list2 = list;
            List list3 = this.parseBackupTimestamps(list2);
            this.pruneOldBackups(list3, file);
         }

         File file3 = new File(this.getBackupDirectory(), str + ".json");
         JsonFileStorageHandler storageHandler2 = this.storageHandler;
         Gson prettyGson = this.getPrettyGson();
         storageHandler2.write(file3, elem, prettyGson);
      }
   }

   private List parseBackupTimestamps(List list) {
      return ((List<File>)list).stream()
         .map(File::getName)
         .filter(str -> {
            Setting[] cachedSettings = ConfigManager.getCachedSettings();
            return str.length() >= 7 && str.endsWith(".json");
         })
         .map(str2 -> str2.substring(0, str2.length() - 5))
         .filter(str3 -> str3.chars().allMatch(Character::isDigit))
         .map(Long::parseLong)
         .collect(Collectors.toList());
   }

   public boolean isAutoManaged() {
      return true;
   }

   public void setCachedJson(JsonElement elem) {
      this.cachedJson = elem;
   }

   public abstract Class getJsonType();

   public boolean isBackupEnabled() {
      return true;
   }

   public Gson getPrettyGson() {
      return GsonConstants.PRETTY_GSON;
   }

   static {
      BASE_CONFIG_DIR = ClientFileUtil.WORKING_DIR + Augustus.getInstance().getName().toLowerCase(Locale.ROOT);
   }

   public String getDirectoryPath() {
      StringBuilder sb = new StringBuilder().append(BASE_CONFIG_DIR).append(File.separator);
      return sb.append(this.getSubDirectory()).toString();
   }

   public void captureSnapshot() {
      this.cachedJson = this.serialize();
   }

   public JsonConfigFile(UnaryFunction unaryFunction, UnaryFunction unaryFunction2) {
      this.loaded = false;
      this.storageHandler = new JsonFileStorageHandler(unaryFunction, unaryFunction2);
      ConfigManager.CONFIG_FILES.add(this);
   }

   public JsonElement getCachedJson() {
      return this.cachedJson;
   }

   public void writeJsonFile(File file, JsonElement elem) {
      JsonFileStorageHandler storageHandler2 = this.storageHandler;
      Gson prettyGson = this.getPrettyGson();
      storageHandler2.write(file, elem, prettyGson);
   }

   public JsonElement readJsonFile(File file) {
      JsonFileStorageHandler storageHandler2 = this.storageHandler;
      Gson prettyGson = this.getPrettyGson();
      Class jsonType = this.getJsonType();
      Supplier supplier = () -> this.loadLatestBackup();
      Class clazz = jsonType;
      Gson gson = prettyGson;
      return storageHandler2.read(file, gson, clazz, supplier);
   }

   public void load() {
      Setting[] cachedSettings = ConfigManager.getCachedSettings();
      File file = new File(this.getFilePath());
      if (!file.exists()) {
         JsonElement elem = this.loadLatestBackup();
         if (elem != null) {
            this.deserialize(elem);
            this.loaded = true;
            return;
         }

         boolean flag = true;
         this.save(flag);
      }

      JsonElement elem2 = this.readJsonFile(file);
      if (this.markLoadedEvenIfEmpty()) {
         this.loaded = true;
      }

      if (elem2 != null) {
         this.deserialize(elem2);
         this.loaded = true;
      }
   }

   public void save() {
      boolean flag = false;
      this.save(flag);
   }

   public abstract void deserialize(JsonElement elem);

   public String getFilePath() {
      return this.getDirectoryPath() + File.separator + this.getName() + ".json";
   }

   public boolean alwaysReserializeOnSave() {
      return false;
   }

   public JsonConfigFile() {
      this(null, null);
   }

   private JsonElement loadLatestBackup() {
      Setting[] cachedSettings = ConfigManager.getCachedSettings();
      if (!this.isBackupEnabled()) {
         return null;
      } else {
         File file = new File(this.getBackupDirectory());
         File[] files = file.listFiles(file2 -> file2.getName().endsWith(".json"));
         if (files != null) {
            ArrayList list = Lists.newArrayList(files);
            ArrayList list2 = list;
            List list3 = this.parseBackupTimestamps(list2);
            Collections.sort(list3);
            Collections.reverse(list3);

            for (long l : (Iterable<Long>)(list3)) {
               File file3 = new File(file, l + ".json");
               if (file3.exists()) {
                  JsonFileStorageHandler storageHandler2 = this.storageHandler;
                  Class jsonType = this.getJsonType();
                  Gson prettyGson = this.getPrettyGson();
                  JsonElement elem = storageHandler2.read(file3, prettyGson, jsonType);
                  if (!this.storageHandler.isEmpty(elem) && elem != null) {
                     return elem;
                  }
               }
            }
         }

         return null;
      }
   }

   public void save(boolean flag) {
      Setting[] cachedSettings = ConfigManager.getCachedSettings();
      if (flag || this.loaded) {
         if (this.cachedJson == null || this.alwaysReserializeOnSave()) {
            this.captureSnapshot();
         }

         JsonElement cachedJson2 = this.cachedJson;
         if (cachedJson2 != null) {
            File file = new File(this.getFilePath());
            this.writeJsonFile(file, cachedJson2);
            this.writeBackup(cachedJson2);
         }
      }
   }

   public boolean markLoadedEvenIfEmpty() {
      return false;
   }

   public abstract JsonElement serialize();

}
