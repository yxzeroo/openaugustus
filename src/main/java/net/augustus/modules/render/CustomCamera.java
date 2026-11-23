package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventOrientCamera;
import net.augustus.events.listeners.OrientCameraListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.StringValue;
import net.minecraft.util.Vec3;

public class CustomCamera extends Module implements OrientCameraListener, TickListener {
   public final StringValue customHeight;
   public final StringValue customHeightF5;
   public final StringValue customX;
   public final StringValue customXF5;
   public final StringValue customZ;
   public final StringValue customZF5;
   public final StringValue customThirdPersonDistance;

   public CustomCamera() {
       super("CustomCamera", "EinstellbareKamera", new Color(105, 66, 245), Categorys.RENDER);
      this.customHeight = new StringValue(this, "CustomHeight", 0.0, -5.0, 10.0, 2);
      this.customHeightF5 = new StringValue(this, "CustomHeightF5", 0.0, -5.0, 10.0, 2);
      this.customX = new StringValue(this, "CustomX", 0.0, -5.0, 5.0, 2);
      this.customXF5 = new StringValue(this, "CustomXF5", 0.0, -5.0, 5.0, 2);
      this.customZ = new StringValue(this, "CustomZ", 0.0, -5.0, 5.0, 2);
      this.customZF5 = new StringValue(this, "CustomZF5", 0.0, -5.0, 5.0, 2);
      this.customThirdPersonDistance = new StringValue(this, "CustomThirdPersonDistance", 4.0, 0.0, 30.0, 1);
   }

   public void onEnable() {
   }

   public void onDisable() {
      if (mc.entityRenderer != null) {
         mc.entityRenderer.thirdPersonDistance = 4.0F;
      }
   }

   public void updateEventSubscriptions() {
      byte b = 11;
      byte b2 = 24;
      this.toggleEventSubscription(b2, b);
      byte b3 = 40;
      this.toggleEventSubscription(b3);
   }

   public void onTick2() {
      mc.entityRenderer.thirdPersonDistance = (float)this.customThirdPersonDistance.getValue();
   }

   public void onOrientCamera(EventOrientCamera event) {
      if (mc.gameSettings.thirdPersonView == 1) {
         float f = (float)((double)event.getEyeHeight() + this.customHeightF5.getValue());
         event.setEyeHeight(f);
         Vec3 vec = new Vec3(this.customXF5.getValue(), 0.0, this.customZF5.getValue());
         event.setOffset(vec);
      } else if (mc.gameSettings.thirdPersonView == 0) {
         float f2 = (float)((double)event.getEyeHeight() + this.customHeight.getValue());
         event.setEyeHeight(f2);
         Vec3 vec2 = new Vec3(this.customX.getValue(), 0.0, this.customZ.getValue());
         event.setOffset(vec2);
      }
   }

}
