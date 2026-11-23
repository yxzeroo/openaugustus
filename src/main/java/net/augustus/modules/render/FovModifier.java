package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.animation.AnimationUtil;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;

public class FovModifier extends Module {
   public final AnimationUtil fovAnimation;
   public final BooleanValue ignoreDynamicFov;
   public final StringValue defaultFov;
   public final StringValue sprintFov;
   public final StringValue speedFov;
   public final BooleansSetting animationModes;
   public final StringValue animationTime;
   public float targetFov;
   public float previousFov;
   public boolean wasSprinting;
   public boolean wasSpeedActive;

   public FovModifier() {
       super("FovModifier", "SichtfeldModifizierer", new Color(252, 186, 3), Categorys.RENDER);
      this.fovAnimation = new AnimationUtil(0.0F, 0.0F, 0.0F, 0.0F);
      this.ignoreDynamicFov = new BooleanValue(this, "IgnoreDynamicFov", true);
      this.defaultFov = new StringValue(this, "DefaultFov", 85.0, 30.0, 150.0, 0);
      this.sprintFov = new StringValue(this, "SprintFov", 90.0, 30.0, 150.0, 0);
      this.speedFov = new StringValue(this, "SpeedFov", 100.0, 30.0, 150.0, 0);
      this.animationModes = new BooleansSetting(this, "AnimationModes", "easeOutQuint", AnimationUtil.getEasingNames(true));
      StringValue stringValue = new StringValue(this, "AnimationTime", 200.0, 1.0, 1000.0, 0);
      SettingVisibilityCondition condition = () -> this.isAnimationTimeVisible();
      this.animationTime = stringValue.visibleWhen(condition);
      this.targetFov = 70.0F;
      this.previousFov = 70.0F;
   }

   @Override
   public void onEnable() {
      this.previousFov = (float)0.0;
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
   }

   private boolean isAnimationTimeVisible() {
      return !this.animationModes.getSelectedValue().equalsIgnoreCase("None");
   }

}
