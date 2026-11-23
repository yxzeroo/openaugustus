package net.augustus.settings.property;

import net.augustus.utils.UnaryFunction;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.augustus.modules.FeatureBase;

public abstract class NestedFeatureProperty extends FeatureBase implements ConfigProperty {
   private FeatureBase featureBase;
   private Supplier childFactory;
   private Consumer onChildAdded;

   public abstract Supplier getEntryFactory();

   public NestedFeatureProperty(String str) {
      super(str);
      this.onChildAdded = null;
      this.childFactory = null;
      this.featureBase = null;
   }

   public NestedFeatureProperty() {
      super(null);
      this.onChildAdded = null;
      this.childFactory = null;
   }

   private static void serializeFilteredProperty(UnaryFunction unaryFunction, ConfigSerializerProvider configSerializerProvider, JsonObject json, ConfigProperty configProperty) {
      if ((Boolean)unaryFunction.apply(configProperty)) {
         configProperty.serialize(configSerializerProvider, json, unaryFunction);
      }
   }

   public void setChildFactory(Supplier supplier) {
      this.childFactory = supplier;
   }

   @Override
   public void deserialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, boolean flag, UnaryFunction unaryFunction) {
      int[] ints;
      JsonObject json2;
      exitBlock: {
         ints = BooleanProperty.getControlFlowState();
         if (this.getName() != null) {
            JsonElement elem = json.get(this.getName());
            JsonElement elem2 = elem;
            if (ints != null) {
               if (elem == null) {
                  return;
               }

               elem2 = elem;
            }

            if (ints != null) {
               if (!elem2.isJsonObject()) {
                  return;
               }

               elem2 = elem;
            }

            json2 = elem2.getAsJsonObject();
            if (ints != null) {
               break exitBlock;
            }
         }

         json2 = json;
      }

      for (Entry entry : json2.entrySet()) {
         JsonElement value = (JsonElement)entry.getValue();
         Object obj = value;
         if (ints != null) {
            if (!value.isJsonObject()) {
               continue;
            }

            obj = this.getEntryFactory().get();
         }

         ConfigProperty configProperty = (ConfigProperty)obj;
         JsonObject asJsonObject = value.getAsJsonObject();
         configProperty.deserialize(configSerializerProvider, asJsonObject, flag, unaryFunction);
         Supplier supplier = () -> configProperty;
         this.registerEntry(supplier);
         if (ints == null) {
            break;
         }
      }
   }

   @Override
   public FeatureBase getFeatureBase() {
      return this.featureBase;
   }

   public void setOnChildAdded(Consumer consumer) {
      this.onChildAdded = consumer;
   }

   public abstract void registerEntry(Supplier supplier);

   @Override
   public JsonObject serialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, UnaryFunction unaryFunction) {
      int[] controlFlowState = BooleanProperty.getControlFlowState();
      JsonObject json2 = new JsonObject();
      this.getProperties().forEach(obj -> serializeFilteredProperty(unaryFunction, configSerializerProvider, json2, (ConfigProperty)obj));
      if (controlFlowState != null) {
         if (this.getName() == null) {
            return json2;
         }

         json.add(this.getName(), json2);
      }

      return json;
   }

   public NestedFeatureProperty(long l, FeatureBase featureBase2, String str) {
      super(str);
      int[] controlFlowState = BooleanProperty.getControlFlowState();
      this.onChildAdded = null;
      this.childFactory = null;
      int[] ints = controlFlowState;
      this.featureBase = featureBase2;
      FeatureBase featureBase3 = featureBase2;
      if (ints != null) {
         if (featureBase2 == null) {
            return;
         }

         featureBase3 = featureBase2;
      }

      featureBase3.addProperty(this);
   }

   @Override
   public void setFeatureBase(FeatureBase featureBase2) {
      this.featureBase = featureBase2;
   }
}
