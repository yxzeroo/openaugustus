package net.augustus.settings.property;

import net.augustus.settings.property.serializer.SetPropertySerializer;
import net.augustus.utils.UnaryFunction;
import com.google.gson.JsonObject;
import java.util.Set;
import net.augustus.modules.FeatureBase;

public class BooleanSetProperty extends AbstractConfigProperty {

   @Override
   public JsonObject serialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, UnaryFunction unaryFunction) {
      super.serialize(configSerializerProvider, json, unaryFunction);
      SetPropertySerializer booleanSetCodec = configSerializerProvider.getBooleanSetCodec();
      String name2 = this.getName();
      Set value2 = (Set)this.getValue();
      String str = name2;
      booleanSetCodec.writeSet(str, value2, json);
      return json;
   }

   public BooleanSetProperty(FeatureBase featureBase2, String str, Set set, Set set2) {
       super(featureBase2, str, set, set2);
   }

   public BooleanSetProperty(FeatureBase featureBase2, String str, Set set) {
       super(featureBase2, str, set);
   }

   public void deserialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, boolean flag, UnaryFunction unaryFunction) {
      SetPropertySerializer booleanSetCodec = configSerializerProvider.getBooleanSetCodec();
      String name2 = this.getName();
      Set set = flag ? (Set)this.getDefaultValue() : (Set)this.getValue();
      String str = name2;
      Set set2 = booleanSetCodec.readSet(str, set, json);
      Set set3 = set2;
      this.applyValue(set3);
   }
}
