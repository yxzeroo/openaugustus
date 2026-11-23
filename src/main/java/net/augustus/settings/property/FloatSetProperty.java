package net.augustus.settings.property;

import net.augustus.settings.property.serializer.SetPropertySerializer;
import net.augustus.utils.UnaryFunction;
import com.google.gson.JsonObject;
import java.util.Set;
import net.augustus.modules.FeatureBase;

public class FloatSetProperty extends AbstractConfigProperty {

   @Override
   public JsonObject serialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, UnaryFunction unaryFunction) {
      super.serialize(configSerializerProvider, json, unaryFunction);
      SetPropertySerializer floatSetCodec = configSerializerProvider.getFloatSetCodec();
      String name2 = this.getName();
      Set value2 = (Set)this.getValue();
      String str = name2;
      floatSetCodec.writeSet(str, value2, json);
      return json;
   }

   public void deserialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, boolean flag, UnaryFunction unaryFunction) {
      SetPropertySerializer floatSetCodec = configSerializerProvider.getFloatSetCodec();
      String name2 = this.getName();
      Set set = flag ? (Set)this.getDefaultValue() : (Set)this.getValue();
      String str = name2;
      Set set2 = floatSetCodec.readSet(str, set, json);
      Set set3 = set2;
      this.applyValue(set3);
   }

   public FloatSetProperty(FeatureBase featureBase2, String str, Set set) {
       super(featureBase2, str, set);
   }

   public FloatSetProperty(FeatureBase featureBase2, String str, Set set, Set set2) {
       super(featureBase2, str, set, set2);
   }
}
