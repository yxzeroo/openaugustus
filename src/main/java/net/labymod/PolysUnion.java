package net.labymod;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
public class PolysUnion {
   public double[][][] doubleArrayArrayArrayValue;
   public PolysEnum enumValue;


   public static class Deserializer implements JsonDeserializer {
      public PolysUnion deserialize_m1(JsonElement elem, Type type, JsonDeserializationContext context) throws JsonParseException {
         PolysUnion polysUnion = new PolysUnion();
         if (elem.isJsonPrimitive()) {
            try {
               String asString = elem.getAsString();
               polysUnion.enumValue = PolysEnum.forValue(asString);
            } catch (Exception e) {
            }
         } else {
            try {
               if (elem.isJsonArray()) {
                  polysUnion.doubleArrayArrayArrayValue = (double[][][])context.deserialize(elem, double[][][].class);
               }
            } catch (JsonParseException e2) {
               throw e2;
            }
         }

         return polysUnion;
      }

      public Object deserialize(JsonElement elem, Type type, JsonDeserializationContext context) throws JsonParseException {
         return this.deserialize_m1(elem, type, context);
      }

   }
}
