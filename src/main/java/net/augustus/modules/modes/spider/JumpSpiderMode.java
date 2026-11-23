package net.augustus.modules.modes.spider;

import net.augustus.events.listeners.TickListener;
import net.augustus.settings.SettingVisibilityCondition;
import java.util.function.BiFunction;
import net.augustus.modules.movement.Spider;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.network.play.client.C03PacketPlayer;

public class JumpSpiderMode extends AbstractSpiderMode implements TickListener {
   public final StringValue motionToJump;
   public final BooleanValue customMotionAtJump;
   public final StringValue jumpMotion;
   public final BooleanValue groundPacket;

   public JumpSpiderMode(Spider spider, DoubleValue doubleValue) {
       super(spider, doubleValue, "Jump");
      StringValue stringValue = new StringValue(this.parentModule, "MotionToJump", -0.2, -0.42, 0.42, 2, this.modeSetting);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.motionToJump = stringValue.visibleWhen(condition);
      AbstractSpiderMode.getControlFlowState2();
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "CustomMotionAtJump", false, this.modeSetting);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.customMotionAtJump = (BooleanValue)booleanValue.withVisibility(condition2);
      StringValue stringValue2 = new StringValue(this.parentModule, "JumpMotion", 0.42, 0.0, 2.0, 3, this.modeSetting);
      SettingVisibilityCondition condition3 = () -> this.isJumpMotionVisible();
      this.jumpMotion = stringValue2.visibleWhen(condition3);
      BooleanValue booleanValue2 = new BooleanValue(this.parentModule, "GroundPacket", false, this.modeSetting);
      SettingVisibilityCondition condition4 = () -> this.isSelected();
      this.groundPacket = (BooleanValue)booleanValue2.withVisibility(condition4);
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new JumpSpiderMode((Spider)obj, (DoubleValue)obj2);
   }

   @Override
   public void onTick2() {
      int[] controlFlowState22 = AbstractSpiderMode.getControlFlowState2();
      if (this.canClimbWall()) {
         if (mc.thePlayer.onGround) {
            mc.thePlayer.jump();
         } else if (!(mc.thePlayer.motionY >= this.motionToJump.getValue())) {
            if (this.customMotionAtJump.getValue()) {
               mc.thePlayer.motionY = this.jumpMotion.getValue();
            } else {
               mc.thePlayer.jump();
            }

            if (this.groundPacket.getValue()) {
               mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C03PacketPlayer(true));
            }
         }
      }
   }

   private boolean isJumpMotionVisible() {
      int[] controlFlowState22 = AbstractSpiderMode.getControlFlowState2();
      return this.isSelected() && this.customMotionAtJump.getValue();
   }

}
