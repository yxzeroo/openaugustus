package net.augustus.modules.modes.velocity;

import net.augustus.events.EventBlockBoundingBox;
import net.augustus.events.listeners.BlockBoundingBoxListener;
import net.augustus.settings.RangeSetting;
import net.augustus.settings.SettingVisibilityCondition;
import java.util.function.BiFunction;
import net.augustus.modules.combat.Velocity;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.block.BlockAir;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class BoundingBoxVelocityMode extends VelocityMode implements BlockBoundingBoxListener {
   private final RangeSetting endBoundingBoxHurtTime;
   private final StringValue boundingBoxHeight;

   public BoundingBoxVelocityMode(Velocity velocity, DoubleValue doubleValue) {
      super(velocity, doubleValue, "BoundingBox");
      RangeSetting rangeSetting = new RangeSetting(this.parentModule, "EndBoundingBoxHurtTime", "StartBoundingBoxHurtTime", 0.0, 10.0, 0.0, 10.0, 0, this.modeSetting);
      String str = "StartHurtTime";
      String str2 = "EndHurtTime";
      RangeSetting rangeSetting2 = rangeSetting.withBoundNames(str2, str).withMaxFirst();
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.endBoundingBoxHurtTime = rangeSetting2.visibleWhen(condition);
      StringValue stringValue = new StringValue(this.parentModule, "BoundingBoxHeight", 1.0, 0.0, 1.0, 2, this.modeSetting);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.boundingBoxHeight = stringValue.visibleWhen(condition2);
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 3;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new BoundingBoxVelocityMode((Velocity)obj, (DoubleValue)obj2);
   }

   @Override
   public void onBlockBoundingBox(EventBlockBoundingBox event) {
      if (this.canApplyVelocity() && mc.thePlayer != null) {
         double hurtTime = (double)mc.thePlayer.hurtTime;
         if (!(hurtTime > this.endBoundingBoxHurtTime.getMaxValue()) && !((double)mc.thePlayer.hurtTime < this.endBoundingBoxHurtTime.getMinValue() + 1.0) && event.getBlock() instanceof BlockAir) {
            BlockPos blockPos = event.getBlockPos();
            if (blockPos.getY() != (int)mc.thePlayer.posY + 1) {
               return;
            }

            AxisAlignedBB box = new AxisAlignedBB(
               (double)blockPos.getX(),
               (double)blockPos.getY(),
               (double)blockPos.getZ(),
               (double)(blockPos.getX() + 1),
               (double)blockPos.getY() + this.boundingBoxHeight.getValue(),
               (double)(blockPos.getZ() + 1)
            );
            event.setBoundingBox(box);
            return;
         }
      }
   }

}
