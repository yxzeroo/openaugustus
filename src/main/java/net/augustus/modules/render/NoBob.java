package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.settings.BooleansSetting;

public class NoBob extends Module implements UpdateListener {
   public final BooleansSetting mode;

   public NoBob() {
       super("NoBob", "KeinBob", new Color(121, 232, 163), Categorys.RENDER);
      this.mode = new BooleansSetting(this, "Mode", "Normal", new String[]{"Normal", "NoScreenBobbing"});
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      byte b = 43;
      this.toggleEventSubscription(b);
   }

   public void onUpdate() {
      if (this.mode.getSelectedValue().equals("Normal")) {
         mc.thePlayer.distanceWalkedModified = 0.0F;
      }
   }

}
