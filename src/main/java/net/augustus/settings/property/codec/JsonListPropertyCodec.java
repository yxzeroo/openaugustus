package net.augustus.settings.property.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.List;
import net.augustus.modules.movement.Speed;

public interface JsonListPropertyCodec extends JsonPropertyCodec {

   @Override
   default void write(String str, Object obj, JsonObject json) {
      List list = (List)obj;
      this.writeList(str, list, json);
   }

   default void collectElement(List list, JsonElement elem) {
      this.readElement(list, elem);
   }

   void writeElement(JsonArray array, Object obj);

   default void writeList(String str, List list, JsonObject json) {
      JsonArray array = new JsonArray();
      list.forEach(obj -> this.appendElement(array, obj));
      json.add(str, array);
   }

   void readElement(List list, JsonElement elem);

   @Override
   default Object read(String str, Object obj, JsonObject json) {
      List list = (List)obj;
      return this.readList(str, list, json);
   }

   default List readList(String str, List list, JsonObject json) {
      JsonElement elem = json.get(str);
      if (elem != null && elem.isJsonArray()) {
         list.clear();
         JsonArray asJsonArray = elem.getAsJsonArray();
         List list2 = asJsonArray.asList();
         list2.forEach(obj -> this.collectElement(list, (JsonElement)obj));
         return list;
      } else {
         return list;
      }
   }

   default void appendElement(JsonArray array, Object obj) {
      this.writeElement(array, obj);
   }
}
