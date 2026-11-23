package net.augustus.settings.property;

import net.augustus.settings.property.codec.JsonPropertyCodec;
import net.augustus.utils.UnaryFunction;
import com.google.gson.JsonObject;
import net.augustus.modules.FeatureBase;

public class StringProperty extends AbstractConfigProperty {

   @Override
   public JsonObject serialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, UnaryFunction unaryFunction) {
      super.serialize(configSerializerProvider, json, unaryFunction);
      JsonPropertyCodec stringCodec = configSerializerProvider.getStringCodec();
      String name2 = this.getName();
      Object value2 = this.getValue();
      String str = name2;
      stringCodec.write(str, value2, json);
      return json;
   }

   public StringProperty(FeatureBase featureBase2, String str, String str2, String str3) {
       super(featureBase2, str, str2, str3);
   }

   public StringProperty(FeatureBase featureBase2, String str, String str2) {
       super(featureBase2, str, str2);
   }

   public void deserialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, boolean flag, UnaryFunction unaryFunction) {
      JsonPropertyCodec stringCodec = configSerializerProvider.getStringCodec();
      String name2 = this.getName();
      String str = flag ? (String)this.getDefaultValue() : (String)this.getValue();
      String str2 = name2;
      Object obj = stringCodec.read(str2, str, json);
      Object obj2 = obj;
      this.applyValue(obj2);
   }
}
