package net.augustus.modules.modes.skinderp;

import net.augustus.events.listeners.TickListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import net.augustus.modules.fun.SkinDerp;
import net.augustus.settings.DoubleValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class SequentialSkinDerpMode extends SkinDerpMode implements TickListener {
   private final TimeHelper timeHelper = new TimeHelper();
   private int currentPartIndex = 0;
   private boolean enablingPhase = false;
   private List enabledParts = new ArrayList();

   public SequentialSkinDerpMode(SkinDerp skinDerp, DoubleValue doubleValue) {
      super(skinDerp, doubleValue, "Sequential");
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
      this.enablingPhase = false;
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
               EnumPlayerModelParts enumPlayerModelParts = (EnumPlayerModelParts)this.enabledParts.get(this.currentPartIndex);
               boolean enablingPhase2 = this.enablingPhase;
               this.setModelPartEnabled(enumPlayerModelParts, enablingPhase2);
               this.currentPartIndex++;
               if (this.currentPartIndex >= this.enabledParts.size()) {
                  this.currentPartIndex = 0;
                  this.enablingPhase = !this.enablingPhase;
               }
            }
         }
      }
   }

   private void refreshEnabledParts() {
      this.enabledParts.clear();
      EnumPlayerModelParts[] modelParts = this.getModelParts();
      int length = modelParts.length;

      for (int i = 0; i < length; i++) {
         EnumPlayerModelParts enumPlayerModelParts = modelParts[i];
         if (this.isPartEnabled(enumPlayerModelParts)) {
            this.enabledParts.add(enumPlayerModelParts);
         }
      }
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new SequentialSkinDerpMode((SkinDerp)obj, (DoubleValue)obj2);
   }

}
