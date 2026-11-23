package net.augustus.config;

import net.augustus.utils.UnaryFunction;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.Supplier;

public class JsonFileStorageHandler {
   private final UnaryFunction encoder;
   private final UnaryFunction decoder;

   public void ensureParentDirectory(File file) {
      Path path = file.getParentFile().toPath();
      if (!Files.exists(path)) {
         try {
            Files.createDirectories(path);
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }

   public JsonElement read(File file, Gson gson, Class clazz, Supplier supplier) {
      this.ensureParentDirectory(file);
      JsonElement elem;
      try {
         byte[] bytes = Files.readAllBytes(file.toPath());
         if (this.decoder != null) {
            elem = (JsonElement)gson.fromJson((String)this.decoder.apply(bytes), clazz);
         } else {
            elem = (JsonElement)gson.fromJson(new String(bytes), clazz);
         }
      } catch (Exception e) {
         elem = supplier != null ? (JsonElement)supplier.get() : null;
      }

      if (supplier != null && this.isEmpty(elem)) {
         elem = (JsonElement)supplier.get();
      }

      return elem;
   }

   public JsonElement read(File file, Gson gson, Class clazz) {
      return this.read(file, gson, clazz, (Supplier)null);
   }

   public JsonFileStorageHandler() {
      this.encoder = null;
      this.decoder = null;
   }

   public boolean isEmpty(JsonElement elem) {
      if (elem == null) {
         return true;
      } else {
         if (elem instanceof JsonObject) {
            if (((JsonObject)elem).entrySet().isEmpty()) {
               return true;
            }
         } else if (elem instanceof JsonArray) {
            if (((JsonArray)elem).isEmpty()) {
               return true;
            }
         }

         return false;
      }
   }

   public JsonFileStorageHandler(UnaryFunction unaryFunction, UnaryFunction unaryFunction2) {
      this.encoder = unaryFunction;
      this.decoder = unaryFunction2;
   }

   public void write(File file, JsonElement elem, Gson gson) {
      this.ensureParentDirectory(file);

      try {
         String str = gson.toJson(elem);
         if (this.encoder != null) {
            byte[] bytes = (byte[])this.encoder.apply(str);
            Files.write(file.toPath(), bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
         } else {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(str);
            writer.flush();
            writer.close();
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
