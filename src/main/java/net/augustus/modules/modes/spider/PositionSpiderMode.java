package net.augustus.modules.modes.spider;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.augustus.events.EventMotion;
import net.augustus.events.listeners.MotionListener;
import net.augustus.settings.SettingVisibilityCondition;
import java.util.function.BiFunction;
import net.augustus.modules.movement.Spider;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;

public class PositionSpiderMode extends AbstractSpiderMode implements MotionListener {
   public final StringValue positionSpeed;
   public final StringValue tickDelayPosition;

   public PositionSpiderMode(Spider spider, DoubleValue doubleValue) {
       super(spider, doubleValue, "Position");
      StringValue stringValue = new StringValue(this.parentModule, "PositionSpeed", 0.4, 0.2, 2.0, 3, this.modeSetting);
      String str = "Speed";
      StringValue stringValue2 = stringValue.named(str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.positionSpeed = stringValue2.visibleWhen(condition);
      StringValue stringValue3 = new StringValue(this.parentModule, "TickDelayPosition", 0.0, 0.0, 20.0, 0, this.modeSetting);
      String str2 = "TickDelay";
      StringValue stringValue4 = stringValue3.named(str2);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.tickDelayPosition = stringValue4.visibleWhen(condition2);
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

   public static BiFunction factory() {
      return (obj, obj2) -> new PositionSpiderMode((Spider)obj, (DoubleValue)obj2);
   }

   @Override
   public void onMotion(EventMotion event) {
      int[] controlFlowState22 = AbstractSpiderMode.getControlFlowState2();
      if (this.canClimbWall()) {
         if ((double)mc.thePlayer.ticksExisted % (this.tickDelayPosition.getValue() + 1.0) == 0.0) {
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
