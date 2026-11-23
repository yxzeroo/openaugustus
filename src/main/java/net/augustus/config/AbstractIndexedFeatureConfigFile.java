package net.augustus.config;

import net.augustus.settings.Setting;
import net.augustus.settings.property.ConfigProperty;
import net.augustus.settings.property.codec.JsonCodecRegistryHolder;
import net.augustus.settings.property.serializer.GsonConfigSerializerProvider;
import net.augustus.utils.UnaryFunction;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Supplier;
import net.augustus.modules.FeatureBase;

public abstract class AbstractIndexedFeatureConfigFile extends JsonConfigFile {
   public final List features;

   public UnaryFunction getLoadFilter() {
      return obj -> acceptLoadProperty((ConfigProperty)obj);
   }

   private static Boolean acceptLoadProperty(ConfigProperty configProperty) {
      return true;
   }

   private static Boolean acceptSaveProperty(ConfigProperty configProperty) {
      return true;
   }

   @Override
   public void deserialize(JsonElement elem) {
      JsonObject json = (JsonObject)elem;
      JsonObject json2 = json;
      this.deserializeIndexed(json2);
   }

   public JsonObject serializeIndexed() {
      JsonObject json = new JsonObject();
      JsonObject json2;
      Setting[] cachedSettings = ConfigManager.getCachedSettings();
      int n = 0;
      Setting[] settings = cachedSettings;

      while (true) {
         if (n < this.features.size()) {
            FeatureBase featureBase = (FeatureBase)this.features.get(n);
            GsonConfigSerializerProvider gsonserializerprovider = JsonCodecRegistryHolder.GSON_SERIALIZER_PROVIDER;
            UnaryFunction saveFilter = this.getSaveFilter();
            Object obj = null;
            GsonConfigSerializerProvider provider = gsonserializerprovider;
            JsonObject json3 = featureBase.serialize(provider, (JsonObject)obj, saveFilter);
            json2 = json;
            if (settings == null) {
               break;
            }

            json.add("E_" + n, json3);
            n++;
            if (settings != null) {
               continue;
            }
         }

         json2 = json;
         break;
      }

      return json2;
   }

   public AbstractIndexedFeatureConfigFile(List list) {
      this.features = list;
   }

   public void deserializeIndexed(JsonObject json) {
      Setting[] cachedSettings = ConfigManager.getCachedSettings();

      for (Entry entry : json.entrySet()) {
         exitBlock: {
            JsonElement value = (JsonElement)entry.getValue();
            Object obj = value;
            if (cachedSettings != null) {
               if (!value.isJsonObject()) {
                  break exitBlock;
               }

               obj = this.createEntrySupplier().get();
            }

            FeatureBase featureBase = (FeatureBase)obj;
            JsonObject asJsonObject = value.getAsJsonObject();
            GsonConfigSerializerProvider gsonserializerprovider = JsonCodecRegistryHolder.GSON_SERIALIZER_PROVIDER;
            UnaryFunction loadFilter = this.getLoadFilter();
            boolean flag = false;
            GsonConfigSerializerProvider provider = gsonserializerprovider;
            featureBase.deserialize(provider, asJsonObject, flag, loadFilter);
            Supplier supplier = () -> featureBase;
            this.onEntryLoaded(supplier);
         }

         if (cachedSettings == null) {
            break;
         }
      }
   }

   public abstract Supplier createEntrySupplier();

   public abstract void onEntryLoaded(Supplier supplier);

   @Override
   public JsonElement serialize() {
      return this.serializeIndexed();
   }

   @Override
   public Class getJsonType() {
      return JsonObject.class;
   }

   public UnaryFunction getSaveFilter() {
      return obj -> acceptSaveProperty((ConfigProperty)obj);
   }

}
