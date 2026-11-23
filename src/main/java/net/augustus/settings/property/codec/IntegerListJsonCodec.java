package net.augustus.settings.property.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.List;

public final class IntegerListJsonCodec implements JsonListPropertyCodec {
   @Override
   public void readElement(List list, JsonElement elem) {
      list.add(elem.getAsInt());
   }

   @Override
   public void writeElement(JsonArray array, Object obj) {
      Integer num = (Integer)obj;
      this.writeIntegerElement(array, num);
   }

   public void writeIntegerElement(JsonArray array, Integer num) {
      array.add(num);
   }
}
