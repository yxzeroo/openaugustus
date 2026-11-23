package net.augustus.settings.property;

import net.augustus.settings.property.serializer.ArrayPropertySerializer;
import net.augustus.utils.UnaryFunction;
import com.google.gson.JsonObject;
import net.augustus.modules.FeatureBase;

public class DoubleArrayProperty extends AbstractConfigProperty {

   public DoubleArrayProperty(FeatureBase featureBase2, String str, Double[] doubles, Double[] doubles2) {
       super(featureBase2, str, doubles, doubles2);
   }

   public DoubleArrayProperty(FeatureBase featureBase2, String str, Double[] doubles) {
       super(featureBase2, str, doubles);
   }

   @Override
   public JsonObject serialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, UnaryFunction unaryFunction) {
      super.serialize(configSerializerProvider, json, unaryFunction);
      ArrayPropertySerializer doubleArrayCodec = configSerializerProvider.getDoubleArrayCodec();
      String name2 = this.getName();
      Object[] value2 = (Object[])this.getValue();
      String str = name2;
      doubleArrayCodec.writeArray(str, value2, json);
      return json;
   }

   public void deserialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, boolean flag, UnaryFunction unaryFunction) {
      ArrayPropertySerializer doubleArrayCodec = configSerializerProvider.getDoubleArrayCodec();
      String name2 = this.getName();
      Double[] doubles = flag ? (Double[])this.getDefaultValue() : (Double[])this.getValue();
      String str = name2;
      Object[] array = doubleArrayCodec.readArray(str, doubles, json);
      Object[] array2 = array;
      this.applyValue(array2);
   }
}
