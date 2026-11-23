package net.augustus.settings.property;

import net.augustus.settings.property.serializer.ArrayPropertySerializer;
import net.augustus.utils.UnaryFunction;
import com.google.gson.JsonObject;
import net.augustus.modules.FeatureBase;

public class BooleanArrayProperty extends AbstractConfigProperty {

   public void deserialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, boolean flag, UnaryFunction unaryFunction) {
      ArrayPropertySerializer booleanArrayCodec = configSerializerProvider.getBooleanArrayCodec();
      String name2 = this.getName();
      Boolean[] bools = flag ? (Boolean[])this.getDefaultValue() : (Boolean[])this.getValue();
      String str = name2;
      Object[] array = booleanArrayCodec.readArray(str, bools, json);
      Object[] array2 = array;
      this.applyValue(array2);
   }

   public BooleanArrayProperty(FeatureBase featureBase2, String str, Boolean[] bools) {
       super(featureBase2, str, bools);
   }

   public BooleanArrayProperty(FeatureBase featureBase2, String str, Boolean[] bools, Boolean[] bools2) {
       super(featureBase2, str, bools, bools2);
   }

   @Override
   public JsonObject serialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, UnaryFunction unaryFunction) {
      super.serialize(configSerializerProvider, json, unaryFunction);
      ArrayPropertySerializer booleanArrayCodec = configSerializerProvider.getBooleanArrayCodec();
      String name2 = this.getName();
      Object[] value2 = (Object[])this.getValue();
      String str = name2;
      booleanArrayCodec.writeArray(str, value2, json);
      return json;
   }
}
