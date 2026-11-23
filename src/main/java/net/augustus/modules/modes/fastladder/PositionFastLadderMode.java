package net.augustus.modules.modes.fastladder;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.augustus.events.EventMotion;
import net.augustus.events.listeners.MotionListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.modules.movement.FastLadder;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;

public class PositionFastLadderMode extends FastLadderMode implements MotionListener {
   public final StringValue positionSpeed;
   public final StringValue positionTickDelay;

   public PositionFastLadderMode(FastLadder fastLadder, DoubleValue doubleValue) {
       super(fastLadder, doubleValue, "Position");
      FastLadderMode.getControlFlowState2();
      StringValue stringValue = new StringValue(this.parentModule, "PositionSpeed", 0.4, 0.2, 2.0, 3, this.modeSetting);
      String str = "Speed";
      stringValue = stringValue.named(str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.positionSpeed = stringValue.visibleWhen(condition);
      StringValue stringValue2 = new StringValue(this.parentModule, "PositionTickDelay", 0.0, 0.0, 20.0, 0, this.modeSetting);
      String str2 = "TickDelay";
      StringValue stringValue3 = stringValue2.named(str2);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.positionTickDelay = stringValue3.visibleWhen(condition2);
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 18;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void onMotion(EventMotion event) {
      int[] controlFlowState22 = FastLadderMode.getControlFlowState2();
      if (((FastLadder)this.parentModule).isClimbing()) {
         if ((double)mc.thePlayer.ticksExisted % (this.positionTickDelay.getValue() + 1.0) == 0.0) {
            mc.thePlayer
               .sendQueue
               .addToSendQueue(
                  new C03PacketPlayer.C06PacketPlayerPosLook(
                     mc.thePlayer.posX,
                     mc.thePlayer.posY + (double)((float)this.positionSpeed.getValue()),
                     mc.thePlayer.posZ,
                     mc.thePlayer.rotationYaw,
                     mc.thePlayer.rotationPitch,
                     false
                  )
               );
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + (double)((float)this.positionSpeed.getValue()), mc.thePlayer.posZ);
         }
      }
   }

}
