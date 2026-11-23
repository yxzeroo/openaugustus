package net.augustus.config;

import net.augustus.Augustus;
import net.augustus.settings.Setting;
import net.augustus.settings.options.NotificationCategoryOption;
import net.augustus.ui.notifications.NotificationLevel;
import net.augustus.ui.notifications.NotificationManager;
import net.augustus.utils.ChatUtil;
import net.augustus.utils.TextFormatUtil;
import net.augustus.utils.audio.ClientSounds;
import net.augustus.vendor.crypto.ConfigAesCipher;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketThreadUtil;

public class ModuleConfigStorage extends JsonConfigFile {
   private final HashMap formatCodecs;
   private static final byte[] ENCRYPTED_MAGIC_HEADER;
   public static byte[] aesKey;

   @Override
   public Class getJsonType() {
      return JsonObject.class;
   }

   @Override
   public void deserialize(JsonElement elem) {
      JsonObject json = (JsonObject)elem;
      this.deserializeEmpty(json);
   }

   public void loadConfig(String str, String str2, boolean flag) {
      String[] stateMarkers = TextFormatUtil.getStateMarkers();
      JsonObject json = this.readConfigJson(str2, str);
      if (stateMarkers != null) {
         if (json == null) {
            if (flag) {
               File file = new File(this.getDirectoryPath() + str2, str + ".json");
               String str3;
               if (!file.exists()) {
                  str3 = "Config not found: §c" + str;
               } else {
                  boolean flag2 = false;
                  try {
                     byte[] bytes = java.nio.file.Files.readAllBytes(file.toPath());
                     flag2 = hasEncryptedHeader(bytes);
                  } catch (Exception e) {
                  }
                  str3 = flag2
                     ? "Failed to decrypt config: §c" + str + " §7(encrypted with server key)"
                     : "Failed to load config: §c" + str;
               }
               this.notifyConfigStatus(str3, NotificationLevel.ERROR);
            }
            return;
         }

         this.applyConfigJson(json, str, flag);
      }
   }

   public JsonObject buildConfigJson(String str, boolean flag) {
      ModuleConfigCodec moduleConfigCodec = (ModuleConfigCodec)this.formatCodecs.get(2);
      String username = Augustus.getInstance().getUsername();
      return moduleConfigCodec.encodeConfig(str, username, flag);
   }

   public JsonObject readConfigJson(String str, String str2) {
      String[] stateMarkers = TextFormatUtil.getStateMarkers();
      File file = new File(this.getDirectoryPath() + str, str2 + ".json");
      if (!file.exists()) {
         return null;
      } else {
         JsonObject json = (JsonObject)this.readJsonFile(file);
         if (stateMarkers == null) {
            Setting.setControlFlowMarker("AjKSk");
         }

         return json;
      }
   }

   @Override
   public boolean isBackupEnabled() {
      return false;
   }

   public void applyConfigJson(JsonObject json, String str, boolean flag) {
      String[] stateMarkers = TextFormatUtil.getStateMarkers();
      if (json != null) {
         Integer num = this.readConfigVersion(json);
         Object obj = num;
         if (stateMarkers != null) {
            if (num == null) {
               return;
            }

            obj = this.formatCodecs.get(num);
         }

         ModuleConfigCodec moduleConfigCodec = (ModuleConfigCodec)obj;
         if (moduleConfigCodec != null) {
            try {
               long l = System.currentTimeMillis();
               moduleConfigCodec.applyConfig(json);
               if (flag) {
                  long l2 = System.currentTimeMillis() - l;
                  String str2 = "Config loaded: §c" + str + " (" + l2 + "ms)";
                  NotificationLevel info = NotificationLevel.INFO;
                  String str3 = str2;
                  this.notifyConfigStatus(str3, info);
                  Augustus.getInstance().getModuleManager().sortModules();
                  ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE2);
               }
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      }
   }

   public void saveConfig(String str, String str2, boolean flag) {

      try {
         JsonObject json = this.buildConfigJson(str, flag);
         File file = new File(this.getDirectoryPath() + str2, str + ".json");
         long l = System.currentTimeMillis();
         this.writeJsonFile(file, (JsonElement)json);
         long l2 = System.currentTimeMillis() - l;
         ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE2);
         String str3 = "Config saved: §c" + str + " (" + l2 + "ms)";
         NotificationLevel info = NotificationLevel.INFO;
         String str4 = str3;
         this.notifyConfigStatus(str4, info);
         System.out.println("Saved");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private static byte[] encryptConfigText(String str) {
      String[] stateMarkers = TextFormatUtil.getStateMarkers();
      if (stateMarkers != null) {
         if (str == null || str.isEmpty()) {
            return new byte[1];
         }
      }

      byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
      ConfigAesCipher configAesCipher = new ConfigAesCipher();
      configAesCipher.initKey(false, aesKey);
      byte[] bytes2 = configAesCipher.encrypt(bytes, 0, bytes.length);
      byte[] bytes3 = new byte[1];
      if (stateMarkers == null) {
         return bytes2;
      } else {
         if (bytes2 != null) {
            bytes3 = new byte[ENCRYPTED_MAGIC_HEADER.length + bytes2.length];
            System.arraycopy(ENCRYPTED_MAGIC_HEADER, 0, bytes3, 0, ENCRYPTED_MAGIC_HEADER.length);
            System.arraycopy(bytes2, 0, bytes3, ENCRYPTED_MAGIC_HEADER.length, bytes2.length);
         }

         return bytes3;
      }
   }

   public JsonObject serializeEmpty() {
      return null;
   }

   @Override
   public JsonElement serialize() {
      return this.serializeEmpty();
   }

   private void notifyConfigStatus(String str, NotificationLevel notificationLevel) {
      String[] stateMarkers = TextFormatUtil.getStateMarkers();
      ChatUtil.sendChat3(str);
      boolean isToggled = Augustus.getInstance().getModuleManager().notifications.isToggled();
      if (stateMarkers != null) {
         if (!isToggled) {
            return;
         }

         isToggled = NotificationCategoryOption.CONFIG_SYSTEM.isSelected();
      }

      if (stateMarkers != null) {
         if (!isToggled) {
            return;
         }

         isToggled = Minecraft.getMinecraft().isCallingFromMinecraftThread();
      }

      if (isToggled) {
         NotificationManager.pushNotification("Configsystem", str, notificationLevel);
         if (stateMarkers != null) {
            return;
         }
      }

      PacketThreadUtil.runOnMainThreadImmediate(() -> showNotification(str, notificationLevel));
   }

   public ModuleConfigStorage() {
      super(
         obj -> encryptConfigText(
            (String)obj
         ),
         obj2 -> decryptConfigBytes(
            (byte[])obj2
         )
      );
      TextFormatUtil.getStateMarkers();
      this.formatCodecs = new HashMap();
      this.formatCodecs.put(2, new ConfigFormatV2());
   }

   @Override
   public String getSubDirectory() {
      return "";
   }

   private static String decryptConfigBytes(byte[] bytes) {
      String[] stateMarkers = TextFormatUtil.getStateMarkers();
      int length = bytes.length;
      if (stateMarkers != null) {
         if (length < ENCRYPTED_MAGIC_HEADER.length || !hasEncryptedHeader(bytes)) {
            return new String(bytes, StandardCharsets.UTF_8);
         }

         length -= ENCRYPTED_MAGIC_HEADER.length;
      }

      byte[] bytes2 = new byte[length];
      System.arraycopy(bytes, ENCRYPTED_MAGIC_HEADER.length, bytes2, 0, bytes2.length);
      ConfigAesCipher configAesCipher = new ConfigAesCipher();
      configAesCipher.initKey(false, aesKey);
      byte[] bytes3 = configAesCipher.decrypt(bytes2);
      return bytes3 != null ? new String(bytes3, StandardCharsets.UTF_8) : "";
   }

   public void deserializeEmpty(JsonObject json) {
   }

   @Override
   public boolean isAutoManaged() {
      return false;
   }

   private static void showNotification(String str, NotificationLevel notificationLevel) {
      NotificationManager.pushNotification("Configsystem", str, notificationLevel);
   }

   public String[] getConfigHeaderInfo(String str, String str2) {
      String[] stateMarkers = TextFormatUtil.getStateMarkers();
      JsonObject json = this.readConfigJson(str2, str);
      if (json == null) {
         return null;
      } else {
         Integer num = this.readConfigVersion(json);
         if (num == null) {
            return null;
         }

         ModuleConfigCodec moduleConfigCodec = (ModuleConfigCodec)this.formatCodecs.get(num);
         if (moduleConfigCodec == null) {
            return null;
         }

         try {
            return moduleConfigCodec.readHeaderInfo(json);
         } catch (Exception e) {
            return null;
         }
      }
   }

   @Override
   public String getName() {
      return "";
   }

   private static boolean hasEncryptedHeader(byte[] bytes) {
      String[] stateMarkers = TextFormatUtil.getStateMarkers();
      if (bytes.length < ENCRYPTED_MAGIC_HEADER.length) {
         return false;
      } else {
         for (int i = 0; i < ENCRYPTED_MAGIC_HEADER.length; i++) {
            if (bytes[i] != ENCRYPTED_MAGIC_HEADER[i]) {
               return false;
            }
         }

         return true;
      }
   }

   private Integer readConfigVersion(JsonObject json) {
      String[] stateMarkers = TextFormatUtil.getStateMarkers();
      JsonElement elem = json.get("Config_Header");
      if (elem == null) {
         return 1;
      } else {
         try {
            JsonObject asJsonObject = elem.getAsJsonObject();
            return asJsonObject.get("Config_Version").getAsInt();
         } catch (Exception e) {
            return null;
         }
      }
   }

   public void loadConfig(String str, String str2) {
      boolean flag = true;
      this.loadConfig(str, str2, flag);
   }



   static {
      aesKey = new byte[]{
         114, -27, 95, -64, 18, -101, 73, -38, 126, -9, 44, -87, 61, -122, 7, -55, 89, -16, 35, -78, 103, -41, 52, -94, 67, -128, 21, -63, 98, -11, 76, -49
      };
      ENCRYPTED_MAGIC_HEADER = new byte[]{-82, 83, 1, 0};
   }
}
