package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventOrientCamera;
import net.augustus.events.listeners.OrientCameraListener;
import net.augustus.settings.BooleanValue;

public class Child extends Module implements OrientCameraListener {
   public final BooleanValue editEyeHeight;
   public final BooleanValue removeCapes;

   public Child() {
       super("Child", "Kind", new Color(52, 217, 148), Categorys.RENDER);
      this.editEyeHeight = new BooleanValue(this, "EditEyeHeight", true);
      this.removeCapes = new BooleanValue(this, "RemoveCapes", true);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      byte b = 3;
      byte b2 = 24;
      this.toggleEventSubscription(b2, b);
   }

   public void onOrientCamera(EventOrientCamera event) {
      if (this.editEyeHeight.getValue()) {
         float f = 0.85F;
         event.setEyeHeight(f);
      }
   }


}
