package net.augustus.settings.property;

import net.augustus.settings.property.codec.JsonListPropertyCodec;
import net.augustus.utils.UnaryFunction;
import com.google.gson.JsonObject;
import java.util.List;
import net.augustus.modules.FeatureBase;

public class DoubleListProperty extends AbstractConfigProperty {

   public DoubleListProperty(FeatureBase featureBase2, String str, List list, List list2) {
       super(featureBase2, str, list, list2);
   }

   public DoubleListProperty(FeatureBase featureBase2, String str, List list) {
       super(featureBase2, str, list);
   }

   public void deserialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, boolean flag, UnaryFunction unaryFunction) {
      JsonListPropertyCodec doubleListCodec = configSerializerProvider.getDoubleListCodec();
      String name2 = this.getName();
      List list = flag ? (List)this.getDefaultValue() : (List)this.getValue();
      String str = name2;
      List list2 = doubleListCodec.readList(str, list, json);
      List list3 = list2;
      this.applyValue(list3);
   }

   @Override
   public JsonObject serialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, UnaryFunction unaryFunction) {
      super.serialize(configSerializerProvider, json, unaryFunction);
      JsonListPropertyCodec doubleListCodec = configSerializerProvider.getDoubleListCodec();
      String name2 = this.getName();
      List value2 = (List)this.getValue();
      String str = name2;
      doubleListCodec.writeList(str, value2, json);
      return json;
   }
}
