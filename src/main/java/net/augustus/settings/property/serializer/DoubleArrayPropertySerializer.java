package net.augustus.settings.property.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.List;

public final class DoubleArrayPropertySerializer implements ArrayPropertySerializer {
   @Override
   public void writeElement(JsonArray array, Object obj) {
      Double d = (Double)obj;
      this.writeDoubleElement(array, d);
   }

   public void writeDoubleElement(JsonArray array, Double d) {
      array.add(d);
   }

   @Override
   public void readElement(List list, JsonElement elem) {
      list.add(elem.getAsDouble());
   }
}
