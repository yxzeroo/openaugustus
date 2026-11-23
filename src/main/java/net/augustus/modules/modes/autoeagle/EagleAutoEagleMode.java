package net.augustus.modules.modes.autoeagle;

import net.augustus.events.EventMoveInput;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.MoveUtil;
import java.util.function.BiFunction;
import net.augustus.modules.legit.AutoEagle;
import net.augustus.settings.DoubleValue;
import net.minecraft.util.AxisAlignedBB;

public class EagleAutoEagleMode extends AutoEagleMode implements MoveInputListener {
   private final RandomizedRangeSettings minSneakTicks;
   private final RandomizedRangeSettings startDistToBlockEagle;
   private int ticksSinceEdge;
   private boolean wasSneaking;

   public EagleAutoEagleMode(AutoEagle autoEagle, DoubleValue doubleValue) {
       super(autoEagle, doubleValue, "Eagle");
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(this.parentModule, "MinSneakTicks", 1.0, 2.0, 1.0, 5.0, 0, this.modeSetting)).withoutNotInARow();
      double d = 25.0;
      double d2 = 15.0;
      double d3 = 2.0;
      double d4 = 1.0;
      double d5 = 3.0;
      double d6 = 3.0;
      randomizedRangeSettings = randomizedRangeSettings.withOutliers(d6, d5, d4, d3, d2, d);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.minSneakTicks = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition);
      randomizedRangeSettings = (new RandomizedRangeSettings(this.parentModule, "StartDistToBlockEagle", 0.05, 0.2, -0.2, 0.5, 2, this.modeSetting)).withoutNotInARow();
      String str = "StartDistToBlock";
      randomizedRangeSettings = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.startDistToBlockEagle = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition2);
      this.ticksSinceEdge = 0;
      this.wasSneaking = false;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 21;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
      this.minSneakTicks.nextValue();
      this.ticksSinceEdge = 0;
      this.wasSneaking = false;
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (!((AutoEagle)this.parentModule).isPitchAboveMinimum()) {
         ((AutoEagle)this.parentModule).eagling = false;
      } else {
         if (!((AutoEagle)this.parentModule).onlyWhileSneak.getValue()) {
            boolean flag = true;
            event.setSneak(flag);
         }

         ((AutoEagle)this.parentModule).eagling = ((AutoEagle)this.parentModule).fastPlace.getValue() && event.isSneak() && ((AutoEagle)this.parentModule).isPitchAboveMinimum();
         if (event.isSneak() || !((AutoEagle)this.parentModule).onlyWhileSneak.getValue()) {
            this.handleEdgeSneak(event);
         }

         if (!event.isSneak() && this.wasSneaking) {
            this.startDistToBlockEagle.nextValue();
            this.minSneakTicks.nextValue();
         }

         this.wasSneaking = event.isSneak();
      }
   }

   private void handleEdgeSneak(EventMoveInput event) {
      if (MoveUtil.isMoving()) {
         boolean flag = false;

         for (AxisAlignedBB box : (Iterable<AxisAlignedBB>)(mc.theWorld.getCollidingBoundingBoxesCapped(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, -1.0, 0.0)))) {
            if (mc.thePlayer
               .getEntityBoundingBox()
               .expand(-0.3, 0.0, -0.3)
               .offset(0.0, -1.0, 0.0)
               .intersectsWith(
                  box.expand(
                     Math.abs(mc.thePlayer.motionX) > 0.02 ? -this.startDistToBlockEagle.getLastValue() : 0.0, 0.0, Math.abs(mc.thePlayer.motionZ) > 0.02 ? -this.startDistToBlockEagle.getLastValue() : 0.0
                  )
               )) {
               flag = true;
               break;
            }
         }

         if (event.isSneak() && !flag) {
            boolean flag2 = true;
            event.setSneak(flag2);
            this.ticksSinceEdge = 0;
         } else if (this.ticksSinceEdge >= this.minSneakTicks.getLastIntValue() && mc.gameSettings.keyBindUseItem.isKeyDown()) {
            boolean flag3 = false;
            event.setSneak(flag3);
         }

         this.ticksSinceEdge++;
      }
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new EagleAutoEagleMode((AutoEagle)obj, (DoubleValue)obj2);
   }

}
