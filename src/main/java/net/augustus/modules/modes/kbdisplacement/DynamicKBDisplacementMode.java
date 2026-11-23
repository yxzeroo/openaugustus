package net.augustus.modules.modes.kbdisplacement;

import net.augustus.modules.combat.KnockbackSimulationResult;
import net.augustus.modules.combat.helper.KnockbackTrajectoryUtil;
import net.augustus.settings.ModeSetting;
import net.augustus.settings.SettingVisibilityCondition;
import java.util.function.BiFunction;
import net.augustus.modules.combat.KBDisplacement;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class DynamicKBDisplacementMode extends KBDisplacementMode {
   public final StringValue minFallDownDistance;
   public final StringValue candidates;
   public final ModeSetting dynamicFallbackMode;

   public DynamicKBDisplacementMode(KBDisplacement kbDisplacement, DoubleValue doubleValue) {
       super(kbDisplacement, doubleValue, "Dynamic");
      StringValue stringValue = new StringValue(this.parentModule, "MinFallDownDistance", 20.0, 0.0, 100.0, 0, this.modeSetting);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.minFallDownDistance = stringValue.visibleWhen(condition);
      StringValue stringValue2 = new StringValue(this.parentModule, "Candidates", 16.0, 4.0, 36.0, 0, this.modeSetting);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.candidates = stringValue2.visibleWhen(condition2);
      ModeSetting modeSetting2 = new ModeSetting(
         this.parentModule, "DynamicFallbackMode", "Constant", this.modeSetting, new BiFunction[]{ConstantKBDisplacementMode.factory(), NoneKBDisplacementMode.factory()}
      );
      String str = "FallbackMode";
      ModeSetting modeSetting3 = modeSetting2.named(str);
      SettingVisibilityCondition condition3 = () -> this.isSelected();
      this.dynamicFallbackMode = modeSetting3.visibleWhen(condition3);
   }

   @Override
   public Float computeDisplacementYaw(EntityLivingBase entity, Vec3 vec) {
      int value = (int)this.minFallDownDistance.getValue();
      int value2 = (int)this.candidates.getValue();
      float f = 0.0F;
      double negativeinfinity = Double.NEGATIVE_INFINITY;
      float f2 = 360.0F / (float)value2;

      for (int i = 0; i < value2; i++) {
         float f3 = (float)i * f2;
         KnockbackSimulationResult result = KnockbackTrajectoryUtil.simulateKnockback(entity, vec, f3, value);
         if (result.dropScore > negativeinfinity && result.fallsOffEdge) {
            negativeinfinity = result.dropScore;
            System.out.println(entity.posY - result.endPosition.yCoord);
            f = f3;
         }
      }

      return negativeinfinity <= 0.0 ? ((KBDisplacementMode)this.dynamicFallbackMode.getAbstractSubMode()).computeDisplacementYaw(entity, vec) : MathHelper.wrapAngleTo180_float(f);
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new DynamicKBDisplacementMode((KBDisplacement)obj, (DoubleValue)obj2);
   }

}
