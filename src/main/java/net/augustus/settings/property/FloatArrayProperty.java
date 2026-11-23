package net.augustus.settings.property;

import net.augustus.settings.property.serializer.ArrayPropertySerializer;
import net.augustus.utils.UnaryFunction;
import com.google.gson.JsonObject;
import net.augustus.modules.FeatureBase;

public class FloatArrayProperty extends AbstractConfigProperty {

   public void deserialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, boolean flag, UnaryFunction unaryFunction) {
      ArrayPropertySerializer floatArrayCodec = configSerializerProvider.getFloatArrayCodec();
      String name2 = this.getName();
      Float[] floats = flag ? (Float[])this.getDefaultValue() : (Float[])this.getValue();
      String str = name2;
      Object[] array = floatArrayCodec.readArray(str, floats, json);
      Object[] array2 = array;
      this.applyValue(array2);
   }

   @Override
   public JsonObject serialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, UnaryFunction unaryFunction) {
      super.serialize(configSerializerProvider, json, unaryFunction);
      ArrayPropertySerializer floatArrayCodec = configSerializerProvider.getFloatArrayCodec();
      String name2 = this.getName();
      Object[] value2 = (Object[])this.getValue();
      String str = name2;
      floatArrayCodec.writeArray(str, value2, json);
      return json;
   }

   public FloatArrayProperty(FeatureBase featureBase2, String str, Float[] floats, Float[] floats2) {
       super(featureBase2, str, floats, floats2);
   }

   public FloatArrayProperty(FeatureBase featureBase2, String str, Float[] floats) {
       super(featureBase2, str, floats);
   }
}
