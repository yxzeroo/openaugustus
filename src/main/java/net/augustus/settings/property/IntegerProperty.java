package net.augustus.settings.property;

import net.augustus.settings.property.codec.JsonPropertyCodec;
import net.augustus.utils.UnaryFunction;
import com.google.gson.JsonObject;
import net.augustus.modules.FeatureBase;

public class IntegerProperty extends AbstractConfigProperty {

   public IntegerProperty(FeatureBase featureBase2, String str, Integer num, Integer num2) {
       super(featureBase2, str, num, num2);
   }

   public IntegerProperty(FeatureBase featureBase2, String str, Integer num) {
       super(featureBase2, str, num);
   }

   @Override
   public JsonObject serialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, UnaryFunction unaryFunction) {
      super.serialize(configSerializerProvider, json, unaryFunction);
      JsonPropertyCodec integerCodec = configSerializerProvider.getIntegerCodec();
      String name2 = this.getName();
      Object value2 = this.getValue();
      String str = name2;
      integerCodec.write(str, value2, json);
      return json;
   }

   public void deserialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, boolean flag, UnaryFunction unaryFunction) {
      JsonPropertyCodec integerCodec = configSerializerProvider.getIntegerCodec();
      String name2 = this.getName();
      Integer num = flag ? (Integer)this.getDefaultValue() : (Integer)this.getValue();
      String str = name2;
      Object obj = integerCodec.read(str, num, json);
      Object obj2 = obj;
      this.applyValue(obj2);
   }
}
