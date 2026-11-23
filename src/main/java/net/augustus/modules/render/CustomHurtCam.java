package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;

public class CustomHurtCam extends Module {
   private final BooleanValue removeHurtCamAtDeath;
   private final StringValue strength;

   public CustomHurtCam() {
       super("CustomHurtCam", "GegenSchmerzKamera", new Color(245, 162, 54), Categorys.RENDER);
      this.removeHurtCamAtDeath = new BooleanValue(this, "RemoveHurtCamAtDeath", true);
      this.strength = new StringValue(this, "Strength", 0.0, -35.0, 35.0, 1);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
   }

   public boolean shouldRemoveHurtCamAtDeath() {
      return this.removeHurtCamAtDeath.getValue() && this.isToggled();
   }

   public float getHurtCamStrength() {
      return !this.isToggled() ? 14.0F : (float)this.strength.getValue();
   }

}
