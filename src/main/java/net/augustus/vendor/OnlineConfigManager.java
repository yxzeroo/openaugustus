package net.augustus.vendor;

import net.augustus.ui.config.AbstractConfigListItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.augustus.ui.notifications.NotificationLevel;
import net.augustus.utils.TuplePair;
import net.augustus.Augustus;
import net.augustus.ui.config.ConfigSystemPanel;
import net.augustus.vendor.crypto.ConfigAesCipher;
import net.augustus.vendor.packet.OnlineConfigListRequestPacket;
import net.augustus.vendor.packet.OnlineConfigDownloadRequestPacket;
import net.augustus.vendor.packet.OnlineConfigHashCheckPacket;
import net.augustus.vendor.packet.OnlineConfigUploadPacket;
import net.augustus.vendor.packet.OnlineConfigActionPacket;

public class OnlineConfigManager {
   private static final HashMap configAesKeys = new HashMap();
   private static final HashMap configHashes = new HashMap();
   private static final HashMap pendingUploads = new HashMap();
   private static final HashMap configListsByCategory = new HashMap();
   private static final Gson GSON = new GsonBuilder().create();
   private static final HashMap cachedConfigPayloads = new HashMap();
   private static final HashMap actionCooldowns = new HashMap();

   public static byte[] computeSha256(byte[] bytes) {
      try {
         MessageDigest instance = MessageDigest.getInstance("SHA-256");
         return instance.digest(bytes);
      } catch (NoSuchAlgorithmException e) {
         throw new RuntimeException("SHA-256 algorithm not available", e);
      }
   }

   public static boolean isActionOnCooldown(int n, String str, char ch, short s) {
      long l = System.currentTimeMillis();
      Long val = (Long)actionCooldowns.get(str);
      if (val != null && val + 10000L >= l) {
         return true;
      } else {
         actionCooldowns.put(str, l);
         return false;
      }
   }


   private static JsonObject loadLocalConfigJson(String str) {
      String str2 = "configs";
      return Augustus.getInstance().getConfigManager().moduleConfigStorage.readConfigJson(str2, str);
   }

   public static List getConfigsByCategory(int n) {
      List list = (List)configListsByCategory.get(n);
      return (List)(list == null ? new ArrayList() : list);
   }

   public static void removeConfigEntry(int n, String str) {
      List list = (List)configListsByCategory.get(n);
      if (list != null) {
         for (int i = list.size() - 1; i >= 0; i--) {
            OnlineConfigMetadata onlineConfigMetadata = (OnlineConfigMetadata)list.get(i);
            if (onlineConfigMetadata.getName().equals(str)) {
               list.remove(i);
            }
         }
      }
   }

   public static void requestConfigLists() {
      if (AugustusServerHandler.isConnectedAndReady()) {
         AugustusServerHandler activeHandler = AugustusServerHandler.getActiveHandler();
         OnlineConfigListRequestPacket packet = new OnlineConfigListRequestPacket(VendorSessionTicker.sessionToken);
         activeHandler.sendPacket(packet);
      }
   }

   public static void addConfigEntry(int n, OnlineConfigMetadata onlineConfigMetadata) {
      Object obj = (List)configListsByCategory.get(n);
      if (obj == null) {
         obj = new ArrayList();
      }

      ((List)obj).add(onlineConfigMetadata);
      setConfigList(n, (List)obj);
   }

   private static void showConfigError(String str) {
      String str2 = "§cConfig system error: §7" + str;
      NotificationLevel error = NotificationLevel.ERROR;
      ConfigSystemPanel.notifyConfigStatus(str2, error);
   }

   public static void cacheEncryptedConfig(int n, String str, byte[] bytes, byte[] bytes2) {
      if (bytes.length > 1) {
         String str2 = n + "_" + str;
         cachedConfigPayloads.put(str2, bytes);
         configHashes.put(str2, null);
         configAesKeys.put(str2, bytes2);
      }
   }

   private static byte[] decryptConfigPayload(byte[] bytes, String str) {
      ConfigAesCipher configAesCipher = new ConfigAesCipher();
      byte[] bytes2 = (byte[])configAesKeys.get(str);
      boolean flag = false;
      configAesCipher.initKey(flag, bytes2);
      int length = bytes.length;
      byte b = 0;
      return configAesCipher.decrypt(bytes, b, length);
   }

   public static void setConfigList(int n, List list) {
      configListsByCategory.put(n, list);
   }

   private static void sendDownloadRequest(int n, String str, OnlineConfigMetadata onlineConfigMetadata) {
      String str2 = n + "_" + str;
      byte[] bytes = (byte[])configHashes.get(str2);
      if (bytes == null) {
         bytes = new byte[1];
      }

      AugustusServerHandler activeHandler = AugustusServerHandler.getActiveHandler();
      OnlineConfigDownloadRequestPacket packet = new OnlineConfigDownloadRequestPacket(VendorSessionTicker.sessionToken, bytes, str, onlineConfigMetadata.getAuthor(), n);
      activeHandler.sendPacket(packet);
   }

   public static void clearActionCooldown(String str) {
      actionCooldowns.remove(str);
   }

   public static void uploadPendingConfig(String str) {
      TuplePair tuplePair = (TuplePair)pendingUploads.remove(str);
      byte[] bytes = tuplePair != null ? (byte[])tuplePair.getFirst() : null;
      boolean flag = tuplePair != null ? (Boolean)tuplePair.getSecond() : true;
      if (tuplePair == null || tuplePair.getFirst() == null) {
         for (net.augustus.ui.config.AbstractConfigListItem configListItem : Augustus.getInstance().getClickGui().getConfigPanel().userConfigs) {
            if (configListItem.getConfigName().equals(str)) {
               flag = configListItem.isEditable();
               break;
            }
         }

         JsonObject json = loadLocalConfigJson(str);
         bytes = GSON.toJson((JsonElement)json).getBytes(StandardCharsets.UTF_8);
         JsonElement elem = json.get("Config_Header");
         if (elem != null && elem.isJsonObject()) {
            JsonObject asJsonObject = elem.getAsJsonObject();
            if (asJsonObject.has("Config_Editable")) {
               flag = Boolean.parseBoolean(asJsonObject.get("Config_Editable").getAsString());
            }
         }
      }

      if (AugustusServerHandler.isConnectedAndReady()) {
         AugustusServerHandler activeHandler = AugustusServerHandler.getActiveHandler();
         OnlineConfigUploadPacket packet = new OnlineConfigUploadPacket(VendorSessionTicker.sessionToken, str, bytes, flag);
         activeHandler.sendPacket(packet);
         sendConfigAction(0, str);
      }
   }

   private static JsonObject parseConfigJson(byte[] bytes) {
      try {
         return GSON.fromJson(new String(bytes, StandardCharsets.UTF_8), JsonObject.class);
      } catch (Exception e) {
         return null;
      }
   }

   public static void requestConfigDownload(int n, String str) {
      if (AugustusServerHandler.isConnectedAndReady()) {
         List configsByCategory = getConfigsByCategory(n);
         ((List<OnlineConfigMetadata>)configsByCategory).stream().filter((OnlineConfigMetadata onlineConfigMetadata) -> onlineConfigMetadata.getName().equals(str)).findFirst().ifPresent((OnlineConfigMetadata onlineConfigMetadata2) -> sendDownloadRequest(n, str, onlineConfigMetadata2));
      }
   }

   public static void initiateConfigUpload(String str, boolean flag) {
      JsonObject json = loadLocalConfigJson(str);
      byte[] bytes = GSON.toJson((JsonElement)json).getBytes(StandardCharsets.UTF_8);
      pendingUploads.put(str, new TuplePair(bytes, flag));
      byte[] bytes2 = computeSha256(bytes);
      AugustusServerHandler activeHandler = AugustusServerHandler.getActiveHandler();
      OnlineConfigHashCheckPacket packet = new OnlineConfigHashCheckPacket(VendorSessionTicker.sessionToken, bytes2, str);
      activeHandler.sendPacket(packet);
   }

   public static void loadOrDownloadConfig(int n, String str) {
      if (!AugustusServerHandler.isConnectedAndReady()) {
         String str2 = n + "_" + str;
         byte[] bytes = (byte[])cachedConfigPayloads.get(str2);
         if (bytes != null) {
            decryptAndApplyConfig(bytes, str, n);
         } else {
            showConfigError("Missing connection to Augustus server.");
         }
      } else {
         requestConfigDownload(n, str);
      }
   }

   public static void sendConfigAction(int n, String str) {
      if (AugustusServerHandler.isConnectedAndReady()) {
         AugustusServerHandler activeHandler = AugustusServerHandler.getActiveHandler();
         OnlineConfigActionPacket packet = new OnlineConfigActionPacket(VendorSessionTicker.sessionToken, str, n);
         activeHandler.sendPacket(packet);
      }
   }

   public static void decryptAndApplyConfig(byte[] bytes, String str, int n) {
      String str2 = n + "_" + str;
      if (bytes.length == 1) {
         bytes = (byte[])cachedConfigPayloads.get(str2);
      }

      byte[] bytes2 = decryptConfigPayload(bytes, str2);
      JsonObject json = parseConfigJson(bytes2);
      if (json != null) {
         boolean flag = true;
         Augustus.getInstance().getConfigManager().moduleConfigStorage.applyConfigJson(json, str, flag);
      } else {
         showConfigError("Failed to load config.");
      }
   }




}
