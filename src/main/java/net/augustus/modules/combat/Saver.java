package net.augustus.modules.combat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.awt.Color;
import java.io.File;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.utils.file.ClientPaths;
import net.augustus.events.listeners.TickListener;

public class Saver extends Module implements TickListener {
   private JsonArray recordedRotations;

   public Saver() {
       super("Saver", "Saver", Color.YELLOW, Categorys.COMBAT);
      this.recordedRotations = new JsonArray();
   }

   public void onEnable() {
      this.recordedRotations = new JsonArray();
   }

   public void onDisable() {
      String str = ClientPaths.CLIENT_DIR + File.separator + "datatest";
      String str2 = System.currentTimeMillis() + ".json";
      JsonArray recordedRotations2 = this.recordedRotations;
      String str3 = str2;
      ClientFileUtil.writeJsonArray(str, str3, recordedRotations2);
      this.recordedRotations = null;
   }

   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
   }

   public void onTick5() {
      if (mc.thePlayer != null) {
         this.recordedRotations.add(this.captureRotationJson());
      }
   }

   private JsonObject captureRotationJson() {
      JsonObject json = new JsonObject();
      json.addProperty("Yaw", mc.thePlayer.rotationYaw);
      json.addProperty("Pitch", mc.thePlayer.rotationPitch);
      return json;
   }

}
