package net.augustus.modules.modes.skinderp;

import net.augustus.events.listeners.TickListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.BiFunction;
import net.augustus.modules.fun.SkinDerp;
import net.augustus.settings.DoubleValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class RandomSkinDerpMode extends SkinDerpMode implements TickListener {
   private final TimeHelper timeHelper = new TimeHelper();
   private final Random random = new Random();
   private final Map partStates = new HashMap();

   public RandomSkinDerpMode(SkinDerp skinDerp, DoubleValue doubleValue) {
      super(skinDerp, doubleValue, "Random");
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
      this.timeHelper.reset();

      for (EnumPlayerModelParts enumPlayerModelParts : this.getModelParts()) {
         this.partStates.put(enumPlayerModelParts, true);
      }
   }

   @Override
   public void onDisable() {
      this.enableAllParts();
      this.partStates.clear();
   }

   @Override
   public void onTick3() {
      if (mc.thePlayer != null && mc.theWorld != null) {
         TimeHelper timeHelper2 = this.timeHelper;
         double delayMs = this.getDelayMs();
         if (timeHelper2.reached(delayMs)) {
            this.timeHelper.reset();

            for (EnumPlayerModelParts enumPlayerModelParts : this.getModelParts()) {
               if (this.isPartEnabled(enumPlayerModelParts) && this.random.nextBoolean()) {
                  boolean orDefault = (Boolean)(this.partStates.getOrDefault(enumPlayerModelParts, true));
                  boolean flag = !orDefault;
                  this.partStates.put(enumPlayerModelParts, flag);
                  this.setModelPartEnabled(enumPlayerModelParts, flag);
               }
            }
         }
      }
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new RandomSkinDerpMode((SkinDerp)obj, (DoubleValue)obj2);
   }

}
