package net.augustus.settings.property;

import net.augustus.settings.property.codec.JsonPropertyCodec;
import net.augustus.utils.UnaryFunction;
import com.google.gson.JsonObject;
import net.augustus.modules.FeatureBase;

public class BooleanProperty extends AbstractConfigProperty {
   private static int[] controlFlowState;

   public BooleanProperty(FeatureBase featureBase2, String str, Boolean bool) {
       super(featureBase2, str, bool);
   }

   public void deserialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, boolean flag, UnaryFunction unaryFunction) {
      JsonPropertyCodec booleanCodec = configSerializerProvider.getBooleanCodec();
      String name2 = this.getName();
      Boolean bool = flag ? (Boolean)this.getDefaultValue() : (Boolean)this.getValue();
      String str = name2;
      Object obj = booleanCodec.read(str, bool, json);
      Object obj2 = obj;
      this.applyValue(obj2);
   }

   @Override
   public JsonObject serialize(ConfigSerializerProvider configSerializerProvider, JsonObject json, UnaryFunction unaryFunction) {
      super.serialize(configSerializerProvider, json, unaryFunction);
      JsonPropertyCodec booleanCodec = configSerializerProvider.getBooleanCodec();
      String name2 = this.getName();
      Object value2 = this.getValue();
      String str = name2;
      booleanCodec.write(str, value2, json);
      return json;
   }

   public BooleanProperty(FeatureBase featureBase2, String str, Boolean bool, Boolean bool2) {
       super(featureBase2, str, bool, bool2);
   }

   public static void setControlFlowState(int[] ints) {
      controlFlowState = ints;
   }

   public static int[] getControlFlowState() {
      return controlFlowState;
   }

   static {
      if (getControlFlowState() == null) {
         setControlFlowState(new int[4]);
      }
   }
}
