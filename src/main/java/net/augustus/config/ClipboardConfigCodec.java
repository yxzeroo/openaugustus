package net.augustus.config;

import net.augustus.settings.Setting;
import net.augustus.settings.property.ConfigProperty;
import net.augustus.utils.ChatUtil;
import net.augustus.utils.UnaryFunction;
import net.augustus.utils.audio.ClientSounds;
import net.augustus.vendor.crypto.CustomBase64;
import net.augustus.vendor.crypto.CustomBase64Encoder;
import net.augustus.vendor.crypto.aes.PaddedAesCipher;
import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;
import java.util.List;
import net.augustus.modules.AbstractFeature;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.settings.SettingGroup;
import net.minecraft.client.gui.GuiScreen;

public class ClipboardConfigCodec {
   private static final byte[] ENCRYPTED_MAGIC_HEADER = new byte[]{-82, 83, 1, 0};
   private static final FeatureListJsonCodec CODEC = new FeatureListJsonCodec();
   public static byte[] aesKey = new byte[]{
      114, -27, 95, -64, 18, -101, 73, -38, 126, -9, 44, -87, 61, -122, 7, -55, 89, -16, 35, -78, 103, -41, 52, -94, 67, -128, 21, -63, 98, -11, 76, -49
   };

   public static boolean importSettingGroup(Module module, SettingGroup doubleValue, String str) {
      str = decodePayload(str);
      JsonObject json = (JsonObject)GsonConstants.GSON.fromJson(str, JsonObject.class);
      if (json.has("ModuleOrCategoryName") && json.has("Values") && json.get("Values").isJsonObject()) {
         if (!json.get("ModuleOrCategoryName").getAsString().equals(doubleValue.getName())) {
            return false;
         }

         json = json.getAsJsonObject("Values");
      }

      FeatureListJsonCodec featureListJsonCodec = CODEC;
      featureListJsonCodec.setFeatures2(new Module[]{module});
      featureListJsonCodec = CODEC;
      boolean flag2 = featureListJsonCodec.deserializeFromObject(json, true, obj -> true);
      if (flag2) {
         ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE2);
         ChatUtil.sendChat3("Imported " + doubleValue.getDisplayName() + " successfully from clipboard");
      }

      return flag2;
   }

   public static boolean importCategory(Categorys categorys) {

      try {
         String clipboardString = GuiScreen.getClipboardString();
         clipboardString = decodePayload(clipboardString);
         JsonObject json = (JsonObject)GsonConstants.GSON.fromJson(clipboardString, JsonObject.class);
         if (json.has("ModuleOrCategoryName") && json.has("Values") && json.get("Values").isJsonObject()) {
            if (!json.get("ModuleOrCategoryName").getAsString().equals(categorys.name())) {
               return false;
            }

            json = json.getAsJsonObject("Values");
         }

         FeatureListJsonCodec featureListJsonCodec = CODEC;
         featureListJsonCodec.setFeatures(categorys.getDisplayModules());
         featureListJsonCodec = CODEC;
         boolean flag2 = featureListJsonCodec.deserializeFromObject(json, true, obj -> shouldImportCategoryProperty((ConfigProperty)obj));
         if (flag2) {
            ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE2);
            ChatUtil.sendChat3("Imported " + categorys.name() + " successfully from clipboard");
         }

         return flag2;
      } catch (Exception e) {
         return false;
      }
   }

   private static String decodePayload(String str) {
      byte[] bytes = new byte[0];
      if (str.startsWith("Name_")) {
         int n = 0;

         for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '_') {
               if (++n >= 2) {
                  str = str.substring(i + 1);
                  bytes = CustomBase64.getRfc46482().decode2(str);
                  break;
               }
            }
         }
      } else {
         bytes = str.getBytes(StandardCharsets.UTF_8);
      }

      if (bytes.length >= ENCRYPTED_MAGIC_HEADER.length && hasEncryptedHeader(bytes)) {
         byte[] bytes2 = new byte[bytes.length - ENCRYPTED_MAGIC_HEADER.length];
         System.arraycopy(bytes, ENCRYPTED_MAGIC_HEADER.length, bytes2, 0, bytes2.length);
         PaddedAesCipher paddedAesCipher = new PaddedAesCipher();
         paddedAesCipher.init(true, aesKey);
         byte[] bytes4 = paddedAesCipher.decrypt(bytes2, (byte)0, bytes2.length);
         return bytes4 == null ? "" : new String(bytes4, StandardCharsets.UTF_8);
      } else {
         return new String(bytes, StandardCharsets.UTF_8);
      }
   }

   public static boolean exportCategory(Categorys categorys) {
      FeatureListJsonCodec featureListJsonCodec = CODEC;
      featureListJsonCodec.setFeatures(categorys.getDisplayModules());
      JsonObject json = new JsonObject();
      String str = categorys.name();
      json.addProperty("ModuleOrCategoryName", str);
      featureListJsonCodec = CODEC;
      JsonObject json2 = featureListJsonCodec.serializeToObject(obj -> shouldExportCategoryProperty((ConfigProperty)obj));
      if (json2 == null) {
         return false;
      } else {
         json.add("Values", json2);
         String str2 = GsonConstants.GSON.toJson(json);
         str2 = new StringBuilder().append("Name_").append(str).append("_").append(CustomBase64.getRfc4648().encodeToString(encryptPayload(str2))).toString();
         GuiScreen.setClipboardString(str2);
         ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE2);
         ChatUtil.sendChat3("Exported " + categorys.name() + " successfully to clipboard");
         return true;
      }
   }

   private static Boolean shouldExportCategoryProperty(ConfigProperty configProperty) {
      return !(configProperty.getFeatureBase() instanceof Module) || configProperty.getName().equals("M_T") && configProperty.getFeatureBase() instanceof Module || configProperty instanceof AbstractFeature;
   }


   public static String encodeSettingGroup(Module module, SettingGroup doubleValue) {
      FeatureListJsonCodec featureListJsonCodec = CODEC;
      featureListJsonCodec.setFeatures2(new Module[]{module});
      JsonObject json = new JsonObject();
      String name = doubleValue.getName();
      json.addProperty("ModuleOrCategoryName", name);
      featureListJsonCodec = CODEC;
      JsonObject json2 = featureListJsonCodec.serializeToObject(obj -> belongsToSettingGroup(doubleValue, (ConfigProperty)obj));
      if (json2 == null) {
         return null;
      } else {
         json.add("Values", json2);
         String str = GsonConstants.GSON.toJson(json);
         return new StringBuilder().append("Name_").append(name).append("_").append(CustomBase64.getRfc4648().encodeToString(encryptPayload(str))).toString();
      }
   }

   public static boolean importSettingGroupFromClipboard(Module module, SettingGroup doubleValue) {

      try {
         return importSettingGroup(module, doubleValue, GuiScreen.getClipboardString());
      } catch (Exception e) {
         return false;
      }
   }

   private static boolean hasEncryptedHeader(byte[] bytes) {
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

   private static Boolean belongsToSettingGroup(SettingGroup doubleValue, ConfigProperty configProperty) {
      return configProperty.getFeatureBase() instanceof Module && !(configProperty instanceof AbstractFeature)
         ? false
         : configProperty == doubleValue
            || configProperty.getFeatureBase() == doubleValue
            || configProperty instanceof Setting && isNestedSetting((Setting)configProperty, doubleValue)
            || configProperty.getFeatureBase() instanceof Setting && isNestedSetting((Setting)configProperty.getFeatureBase(), doubleValue);
   }

   private static boolean isNestedSetting(Setting setting, SettingGroup doubleValue) {

      for (Setting setting2 : (Iterable<Setting>)(doubleValue.getChildren())) {
         if (setting2 instanceof SettingGroup) {
            if (isNestedSetting(setting, (SettingGroup)setting2)) {
               return true;
            }
         }

         if (setting.getName().equals(setting2.getName())) {
            return true;
         }
      }

      return false;
   }

   private static Boolean shouldImportCategoryProperty(ConfigProperty configProperty) {
      return !(configProperty.getFeatureBase() instanceof Module) || configProperty.getName().equals("M_T") && configProperty.getFeatureBase() instanceof Module || configProperty instanceof AbstractFeature;
   }

   public static boolean exportSettingGroup(Module module, SettingGroup doubleValue) {
      String str = encodeSettingGroup(module, doubleValue);
      if (str == null) {
         return false;
      } else {
         GuiScreen.setClipboardString(str);
         ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE2);
         ChatUtil.sendChat3("Exported " + doubleValue.getDisplayName() + " successfully to clipboard");
         return true;
      }
   }

   public static boolean importModule(Module module) {

      try {
         String clipboardString = GuiScreen.getClipboardString();
         clipboardString = decodePayload(clipboardString);
         JsonObject json = (JsonObject)GsonConstants.GSON.fromJson(clipboardString, JsonObject.class);
         if (json.has("ModuleOrCategoryName") && json.has("Values") && json.get("Values").isJsonObject()) {
            if (!json.get("ModuleOrCategoryName").getAsString().equals(module.getName())) {
               return false;
            }

            json = json.getAsJsonObject("Values");
         }

         FeatureListJsonCodec featureListJsonCodec = CODEC;
         featureListJsonCodec.setFeatures2(new Module[]{module});
         featureListJsonCodec = CODEC;
         boolean flag2 = featureListJsonCodec.deserializeFromObject(json, true, obj -> true);
         if (flag2) {
            ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE2);
            ChatUtil.sendChat3("Imported " + module.getName() + " successfully from clipboard");
         }

         return flag2;
      } catch (Exception e) {
         return false;
      }
   }

   private static byte[] encryptPayload(String str) {
      if (str != null && !str.isEmpty()) {
         byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
         PaddedAesCipher paddedAesCipher = new PaddedAesCipher();
         paddedAesCipher.init(false, aesKey);
         byte[] bytes3 = paddedAesCipher.encrypt(bytes, (byte)0, bytes.length);
         byte[] bytes4 = new byte[1];
         if (bytes3 != null) {
            bytes4 = new byte[ENCRYPTED_MAGIC_HEADER.length + bytes3.length];
            System.arraycopy(ENCRYPTED_MAGIC_HEADER, 0, bytes4, 0, ENCRYPTED_MAGIC_HEADER.length);
            System.arraycopy(bytes3, 0, bytes4, ENCRYPTED_MAGIC_HEADER.length, bytes3.length);
         }

         return bytes4;
      } else {
         return new byte[1];
      }
   }

   public static boolean exportModule(Module module) {
      FeatureListJsonCodec featureListJsonCodec = CODEC;
      featureListJsonCodec.setFeatures2(new Module[]{module});
      JsonObject json = new JsonObject();
      String name = module.getName();
      json.addProperty("ModuleOrCategoryName", name);
      featureListJsonCodec = CODEC;
      UnaryFunction unaryFunction = obj -> {
         return !(((ConfigProperty)obj).getFeatureBase() instanceof Module) || obj instanceof AbstractFeature;
      };
      JsonObject json2 = featureListJsonCodec.serializeToObject(unaryFunction);
      if (json2 == null) {
         return false;
      } else {
         json.add("Values", json2);
         String str = GsonConstants.GSON.toJson(json);
         str = new StringBuilder().append("Name_").append(name).append("_").append(CustomBase64.getRfc4648().encodeToString(encryptPayload(str))).toString();
         GuiScreen.setClipboardString(str);
         ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE2);
         ChatUtil.sendChat3("Exported " + module.getName() + " successfully to clipboard");
         return true;
      }
   }



}
