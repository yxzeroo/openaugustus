package net.augustus.settings.property;

import net.augustus.settings.property.serializer.ArrayPropertySerializer;
import net.augustus.utils.UnaryFunction;
import com.google.gson.JsonObject;
import net.augustus.modules.FeatureBase;

public class IntegerArrayProperty extends AbstractConfigProperty {

   public void deserialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, boolean flag, UnaryFunction unaryFunction) {
      ArrayPropertySerializer integerArrayCodec = configSerializerProvider.getIntegerArrayCodec();
      String name2 = this.getName();
      Integer[] nums = flag ? (Integer[])this.getDefaultValue() : (Integer[])this.getValue();
      String str = name2;
      Object[] array = integerArrayCodec.readArray(str, nums, json);
      Object[] array2 = array;
      this.applyValue(array2);
   }

   public IntegerArrayProperty(FeatureBase featureBase2, String str, Integer[] nums, Integer[] nums2) {
       super(featureBase2, str, nums, nums2);
   }

   public IntegerArrayProperty(FeatureBase featureBase2, String str, Integer[] nums) {
       super(featureBase2, str, nums);
   }

   @Override
   public JsonObject serialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, UnaryFunction unaryFunction) {
      super.serialize(configSerializerProvider, json, unaryFunction);
      ArrayPropertySerializer integerArrayCodec = configSerializerProvider.getIntegerArrayCodec();
      String name2 = this.getName();
      Object[] value2 = (Object[])this.getValue();
      String str = name2;
      integerArrayCodec.writeArray(str, value2, json);
      return json;
   }
}
