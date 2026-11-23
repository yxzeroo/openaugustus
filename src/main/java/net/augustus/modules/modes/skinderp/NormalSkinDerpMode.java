package net.augustus.modules.modes.skinderp;

import net.augustus.events.listeners.TickListener;
import java.util.function.BiFunction;
import net.augustus.modules.fun.SkinDerp;
import net.augustus.settings.DoubleValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class NormalSkinDerpMode extends SkinDerpMode implements TickListener {
   private final TimeHelper timeHelper = new TimeHelper();
   private boolean partsVisible = true;

   public NormalSkinDerpMode(SkinDerp skinDerp, DoubleValue doubleValue) {
      super(skinDerp, doubleValue, "Normal");
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
      this.partsVisible = true;
      this.timeHelper.reset();
   }

   @Override
   public void onDisable() {
      this.enableAllParts();
   }

   @Override
   public void onTick3() {
      if (mc.thePlayer != null && mc.theWorld != null) {
         TimeHelper timeHelper2 = this.timeHelper;
         double delayMs = this.getDelayMs();
         if (timeHelper2.reached(delayMs)) {
            this.timeHelper.reset();
            this.partsVisible = !this.partsVisible;

            for (EnumPlayerModelParts enumPlayerModelParts : this.getModelParts()) {
               if (this.isPartEnabled(enumPlayerModelParts)) {
                  boolean partsVisible2 = this.partsVisible;
                  this.setModelPartEnabled(enumPlayerModelParts, partsVisible2);
               }
            }
         }
      }
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new NormalSkinDerpMode((SkinDerp)obj, (DoubleValue)obj2);
   }

}
