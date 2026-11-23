package net.augustus.modules.modes.kbdisplacement;

import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.SettingVisibilityCondition;
import java.util.function.BiFunction;
import net.augustus.modules.combat.KBDisplacement;
import net.augustus.settings.DoubleValue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class ConstantKBDisplacementMode extends KBDisplacementMode {
   public final RandomizedRangeSettings yawOffset;

   public ConstantKBDisplacementMode(KBDisplacement kbDisplacement, DoubleValue doubleValue) {
       super(kbDisplacement, doubleValue, "Constant");
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(this.parentModule, "YawOffset", 80.0, 100.0, -180.0, 180.0, 0, this.modeSetting);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.yawOffset = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition);
   }

   @Override
   public Float computeDisplacementYaw(EntityLivingBase entity, Vec3 vec) {
      float rotationYaw = mc.thePlayer.rotationYaw;
      return MathHelper.wrapAngleTo180_float(rotationYaw + (float)this.yawOffset.nextValue());
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new ConstantKBDisplacementMode((KBDisplacement)obj, (DoubleValue)obj2);
   }

}
