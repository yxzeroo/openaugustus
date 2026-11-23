package net.augustus.settings.property;

import net.augustus.settings.property.codec.JsonListPropertyCodec;
import net.augustus.utils.UnaryFunction;
import com.google.gson.JsonObject;
import java.util.List;
import net.augustus.modules.FeatureBase;

public class IntegerListProperty extends AbstractConfigProperty {

   public IntegerListProperty(FeatureBase featureBase2, String str, List list, List list2) {
       super(featureBase2, str, list, list2);
   }

   public void deserialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, boolean flag, UnaryFunction unaryFunction) {
      JsonListPropertyCodec integerListCodec = configSerializerProvider.getIntegerListCodec();
      String name2 = this.getName();
      List list = flag ? (List)this.getDefaultValue() : (List)this.getValue();
      String str = name2;
      List list2 = integerListCodec.readList(str, list, json);
      List list3 = list2;
      this.applyValue(list3);
   }

   public IntegerListProperty(FeatureBase featureBase2, String str, List list) {
       super(featureBase2, str, list);
   }

   @Override
   public JsonObject serialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, UnaryFunction unaryFunction) {
      super.serialize(configSerializerProvider, json, unaryFunction);
      JsonListPropertyCodec integerListCodec = configSerializerProvider.getIntegerListCodec();
      String name2 = this.getName();
      List value2 = (List)this.getValue();
      String str = name2;
      integerListCodec.writeList(str, value2, json);
      return json;
   }
}
