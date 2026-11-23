package net.augustus.modules.modes.longjump;

import net.augustus.events.listeners.UpdateListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.MoveUtil;
import java.util.function.BiFunction;
import net.augustus.modules.movement.LongJump;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.util.MathHelper;

public class NcpLongJumpMode extends AbstractLongJumpMode implements UpdateListener {
   public final StringValue ncpSpeed;

   public NcpLongJumpMode(LongJump longJump, DoubleValue doubleValue) {
       super(longJump, doubleValue, "NCP");
      StringValue stringValue = new StringValue(this.parentModule, "NCPSpeed", 4.0, 1.0, 10.0, 2, this.modeSetting);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.ncpSpeed = stringValue.visibleWhen(condition);
   }

   @Override
   public void updateEventSubscriptions() {
      super.updateEventSubscriptions();
      byte b = 43;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
      ((LongJump)this.parentModule).pendingJump = true;
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new NcpLongJumpMode((LongJump)obj, (DoubleValue)obj2);
   }

   @Override
   public void onUpdate2() {
      if (((LongJump)this.parentModule).pendingJump) {
         mc.thePlayer.jump();
         MoveUtil.setSpeed2((float)(MathHelper.clamp_double(this.ncpSpeed.getValue(), 0.0, 50.0) * MoveUtil.getSpeed()));
         ((LongJump)this.parentModule).pendingJump = false;
      } else {
         MoveUtil.setSpeed2((float)MoveUtil.getSpeed());
      }
   }

}
