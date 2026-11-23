package net.augustus.modules.modes.jesus;

import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.modules.movement.Jesus;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;

public class SolidJesusMode extends AbstractJesusMode {
   public final BooleanValue userWaterBlockBounds;

   public SolidJesusMode(Jesus jesus, DoubleValue doubleValue) {
       super(jesus, doubleValue, "Solid");
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "UserWaterBlockBounds", false, this.modeSetting);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.userWaterBlockBounds = (BooleanValue)booleanValue.withVisibility(condition);
   }

   @Override
   public void updateEventSubscriptions() {
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

}
