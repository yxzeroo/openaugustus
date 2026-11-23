package net.augustus.modules.modes.fastladder;

import net.augustus.events.EventLadder;
import net.augustus.events.listeners.LadderListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.modules.movement.FastLadder;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class VulcanFastLadderMode extends FastLadderMode implements LadderListener {
   public final StringValue vulcanMotionY;

   public VulcanFastLadderMode(FastLadder fastLadder, DoubleValue doubleValue) {
       super(fastLadder, doubleValue, "Vulcan");
      StringValue stringValue = new StringValue(this.parentModule, "VulcanMotionY", 5.0, 0.2, 10.0, 3, this.modeSetting);
      String str = "MotionY";
      StringValue stringValue2 = stringValue.named(str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.vulcanMotionY = stringValue2.visibleWhen(condition);
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 15;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void onLadder(EventLadder event) {
      int[] controlFlowState22 = FastLadderMode.getControlFlowState2();
      if (((FastLadder)this.parentModule).isOnClimbable()) {
         double d = 0.0;

         for (double i = this.vulcanMotionY.getValue() % 1.0; i <= this.vulcanMotionY.getValue(); d = i++) {
            if (!this.isClimbableAtYOffset(i)) {
               double d2 = i - 1.0;
               if (!this.isClimbableAtYOffset(d2)) {
                  break;
               }
            }
         }

         event.setMotionY(d);
         mc.thePlayer
            .sendQueue
            .addToSendQueue(
               new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 1.0, mc.thePlayer.posZ), EnumFacing.DOWN)
            );
      }
   }

}
