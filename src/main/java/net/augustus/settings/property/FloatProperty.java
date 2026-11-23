package net.augustus.settings.property;

import net.augustus.settings.property.codec.JsonPropertyCodec;
import net.augustus.utils.UnaryFunction;
import com.google.gson.JsonObject;
import net.augustus.modules.FeatureBase;

public class FloatProperty extends AbstractConfigProperty {

   public FloatProperty(FeatureBase featureBase2, String str, Float f) {
       super(featureBase2, str, f);
   }

   @Override
   public JsonObject serialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, UnaryFunction unaryFunction) {
      super.serialize(configSerializerProvider, json, unaryFunction);
      JsonPropertyCodec floatCodec = configSerializerProvider.getFloatCodec();
      String name2 = this.getName();
      Object value2 = this.getValue();
      String str = name2;
      floatCodec.write(str, value2, json);
      return json;
   }

   public FloatProperty(FeatureBase featureBase2, String str, Float f, Float f2) {
       super(featureBase2, str, f, f2);
   }

   public void deserialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, boolean flag, UnaryFunction unaryFunction) {
      JsonPropertyCodec floatCodec = configSerializerProvider.getFloatCodec();
      String name2 = this.getName();
      Float f = flag ? (Float)this.getDefaultValue() : (Float)this.getValue();
      String str = name2;
      Object obj = floatCodec.read(str, f, json);
      Object obj2 = obj;
      this.applyValue(obj2);
   }
}
