package net.augustus.settings.property;

import net.augustus.settings.property.codec.JsonListPropertyCodec;
import net.augustus.utils.UnaryFunction;
import com.google.gson.JsonObject;
import java.util.List;
import net.augustus.modules.FeatureBase;

public class FloatListProperty extends AbstractConfigProperty {

   public FloatListProperty(FeatureBase featureBase2, String str, List list, List list2) {
       super(featureBase2, str, list, list2);
   }

   public void deserialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, boolean flag, UnaryFunction unaryFunction) {
      JsonListPropertyCodec floatListCodec = configSerializerProvider.getFloatListCodec();
      String name2 = this.getName();
      List list = flag ? (List)this.getDefaultValue() : (List)this.getValue();
      String str = name2;
      List list2 = floatListCodec.readList(str, list, json);
      List list3 = list2;
      this.applyValue(list3);
   }

   @Override
   public JsonObject serialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, UnaryFunction unaryFunction) {
      super.serialize(configSerializerProvider, json, unaryFunction);
      JsonListPropertyCodec floatListCodec = configSerializerProvider.getFloatListCodec();
      String name2 = this.getName();
      List value2 = (List)this.getValue();
      String str = name2;
      floatListCodec.writeList(str, value2, json);
      return json;
   }

   public FloatListProperty(FeatureBase featureBase2, String str, List list) {
       super(featureBase2, str, list);
   }
}
