package net.augustus.modules.modes.spider;

import net.augustus.events.EventBlockBoundingBox;
import net.augustus.events.listeners.BlockBoundingBoxListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.SettingVisibilityCondition;
import java.util.function.BiFunction;
import net.augustus.modules.movement.Spider;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.util.AxisAlignedBB;

public class BoundingBoxSpiderMode extends AbstractSpiderMode implements BlockBoundingBoxListener, TickListener {
   private final BooleanValue customMotion;
   private final RandomizedRangeSettings motion;
   private final StringValue motionTickDelay;
   private int motionTickCounter;

   public BoundingBoxSpiderMode(Spider spider, DoubleValue doubleValue) {
      super(spider, doubleValue, "BoundingBox");
      AbstractSpiderMode.getControlFlowState2();
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "Custom Motion", false, this.modeSetting);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.customMotion = (BooleanValue)booleanValue.withVisibility(condition);
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(this.parentModule, "Motion", 0.6, 0.6, 0.0, 1.0, 4, this.modeSetting);
      SettingVisibilityCondition condition2 = () -> this.isMotionVisible();
      this.motion = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition2);
      StringValue stringValue = new StringValue(this.parentModule, "MotionTickDelay", 0.0, 0.0, 10.0, 0, this.modeSetting);
      SettingVisibilityCondition condition3 = () -> this.isMotionTickDelayVisible();
      this.motionTickDelay = stringValue.visibleWhen(condition3);
      this.motionTickCounter = 0;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 3;
      this.toggleEventSubscription(b);
      byte b2 = 40;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new BoundingBoxSpiderMode((Spider)obj, (DoubleValue)obj2);
   }

   @Override
   public void onBlockBoundingBox(EventBlockBoundingBox event) {
      int[] controlFlowState22 = AbstractSpiderMode.getControlFlowState2();
      if (this.canShrinkBoundingBox() && event.getBoundingBox() != null && (double)event.getBlockPos().getY() >= mc.thePlayer.posY) {
         AxisAlignedBB box = event.getBoundingBox().contract(1.0E-5, 0.0, 1.0E-5);
         event.setBoundingBox(box);
      }
   }

   @Override
   public void onTick2() {
      int[] controlFlowState22 = AbstractSpiderMode.getControlFlowState2();
      if (this.customMotion.getValue() && this.canClimbWall()) {
         this.motionTickCounter++;
         if ((double)this.motionTickCounter > this.motionTickDelay.getValue()) {
            mc.thePlayer.motionY = (double)((float)this.motion.nextValue());
            this.motionTickCounter = 0;
         }
      } else {
         this.motionTickCounter = 0;
      }
   }

   private boolean canShrinkBoundingBox() {
      int[] controlFlowState22 = AbstractSpiderMode.getControlFlowState2();
      return mc.thePlayer != null && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater() && !mc.thePlayer.isInLava();
   }

   private boolean isMotionTickDelayVisible() {
      int[] controlFlowState22 = AbstractSpiderMode.getControlFlowState2();
      return this.customMotion.getValue() && this.isSelected();
   }

   private boolean isMotionVisible() {
      int[] controlFlowState22 = AbstractSpiderMode.getControlFlowState2();
      return this.customMotion.getValue() && this.isSelected();
   }

}
