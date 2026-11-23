package net.augustus.modules.modes.skinderp;

import net.augustus.events.listeners.TickListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import net.augustus.modules.fun.SkinDerp;
import net.augustus.settings.DoubleValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class WaveSkinDerpMode extends SkinDerpMode implements TickListener {
   private final TimeHelper timeHelper = new TimeHelper();
   private int currentPartIndex = 0;
   private List enabledParts = new ArrayList();

   public WaveSkinDerpMode(SkinDerp skinDerp, DoubleValue doubleValue) {
      super(skinDerp, doubleValue, "Wave");
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
      this.timeHelper.reset();
      this.currentPartIndex = 0;
      this.refreshEnabledParts();
   }

   @Override
   public void onDisable() {
      this.enableAllParts();
      this.enabledParts.clear();
   }

   @Override
   public void onTick3() {
      if (mc.thePlayer != null && mc.theWorld != null) {
         TimeHelper timeHelper2 = this.timeHelper;
         double delayMs = this.getDelayMs();
         if (timeHelper2.reached(delayMs)) {
            this.timeHelper.reset();
            this.refreshEnabledParts();
            if (!this.enabledParts.isEmpty()) {
               for (EnumPlayerModelParts enumPlayerModelParts : (Iterable<EnumPlayerModelParts>)(this.enabledParts)) {
                  boolean flag = false;
                  this.setModelPartEnabled(enumPlayerModelParts, flag);
               }

               EnumPlayerModelParts enumPlayerModelParts2 = (EnumPlayerModelParts)this.enabledParts.get(this.currentPartIndex);
               boolean flag2 = true;
               this.setModelPartEnabled(enumPlayerModelParts2, flag2);
               this.currentPartIndex = (this.currentPartIndex + 1) % this.enabledParts.size();
            }
         }
      }
   }

   private void refreshEnabledParts() {
      this.enabledParts.clear();
      EnumPlayerModelParts[] modelParts = this.getModelParts();

      for (EnumPlayerModelParts enumPlayerModelParts : modelParts) {
         if (this.isPartEnabled(enumPlayerModelParts)) {
            this.enabledParts.add(enumPlayerModelParts);
         }
      }
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new WaveSkinDerpMode((SkinDerp)obj, (DoubleValue)obj2);
   }

}
