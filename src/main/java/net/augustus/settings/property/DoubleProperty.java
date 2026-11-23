package net.augustus.settings.property;

import net.augustus.settings.property.codec.JsonPropertyCodec;
import net.augustus.utils.UnaryFunction;
import com.google.gson.JsonObject;
import net.augustus.modules.FeatureBase;

public class DoubleProperty extends AbstractConfigProperty {

   public void deserialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, boolean flag, UnaryFunction unaryFunction) {
      JsonPropertyCodec doubleCodec = configSerializerProvider.getDoubleCodec();
      String name2 = this.getName();
      Double d = flag ? (Double)this.getDefaultValue() : (Double)this.getValue();
      String str = name2;
      Object obj = doubleCodec.read(str, d, json);
      Object obj2 = obj;
      this.applyValue(obj2);
   }

   public DoubleProperty(FeatureBase featureBase2, String str, double d, double d2) {
       super(featureBase2, str, d, d2);
   }

   public DoubleProperty(FeatureBase featureBase2, String str, double d) {
       super(featureBase2, str, d);
   }

   @Override
   public JsonObject serialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, UnaryFunction unaryFunction) {
      super.serialize(configSerializerProvider, json, unaryFunction);
      JsonPropertyCodec doubleCodec = configSerializerProvider.getDoubleCodec();
      String name2 = this.getName();
      Object value2 = this.getValue();
      String str = name2;
      doubleCodec.write(str, value2, json);
      return json;
   }
}
