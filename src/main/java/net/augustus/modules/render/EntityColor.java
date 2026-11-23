package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventEntityHurt;
import net.augustus.events.listeners.EntityHurtListener;
import net.augustus.settings.ColorSetting;
import net.augustus.events.EventEntityColor;
import net.augustus.events.listeners.EntityColorListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.animation.AnimationUtil;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.minecraft.util.MathHelper;

public class EntityColor extends Module implements EntityColorListener, EntityHurtListener {
   private final ColorSetting hitColor;
   private final BooleansSetting hitColorAnimation;
   private final BooleanValue reverseAnimation;
   private final ColorSetting color2;

   public EntityColor() {
       super("EntityColor", "EntitätFarbe", new Color(12, 171, 152), Categorys.RENDER);
      this.hitColor = new ColorSetting(this, "HitColor", new Color(0, 255, 0, 36));
      this.hitColorAnimation = new BooleansSetting(this, "HitColorAnimation", "easeInSine", AnimationUtil.getEasingNames(true));
      BooleanValue booleanValue = new BooleanValue(this, "ReverseAnimation", false);
      SettingVisibilityCondition condition = () -> this.isReverseAnimationVisible();
      this.reverseAnimation = (BooleanValue)booleanValue.withVisibility(condition);
      this.color2 = new ColorSetting(this, "Color", new Color(0, 0, 0, 0));
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 10;
      this.toggleEventSubscription(b);
      byte b2 = 50;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onEntityColor(EventEntityColor event) {
      if (event.getEntity().hurtTime > 0) {
         if (!this.hitColorAnimation.getSelectedValue().equals("None")) {
            byte b = 0;
            this.hitColor.getColorInt(b);
            float redFloat = this.hitColor.getRedFloat();
            event.setRed(redFloat);
            float greenFloat = this.hitColor.getGreenFloat();
            event.setGreen(greenFloat);
            float blueFloat = this.hitColor.getBlueFloat();
            event.setBlue(blueFloat);
            float f = (float)MathHelper.clamp_int((int)((double)this.hitColor.getAlpha() * event.getEntity().colorAnimation.getEasedProgress()), 0, 255) / 255.0F;
            event.setAlpha(f);
            return;
         }

         byte b2 = 0;
         int colorInt = this.hitColor.getColorInt(b2);
         event.setColor(colorInt);
      } else if (this.color2.getPrimaryAlpha() > 10) {
         byte b3 = 0;
         int colorInt2 = this.color2.getColorInt(b3);
         event.setColor(colorInt2);
         boolean flag = true;
         event.setOverrideColor(flag);
      }
   }

   @Override
   public void onEntityHurt(EventEntityHurt event) {
      if (!this.hitColorAnimation.getSelectedValue().equals("None")) {
         event.getEntity().colorAnimation.start(500, AnimationUtil.parseEasingType(this.hitColorAnimation.getSelectedValue()), this.reverseAnimation.getValue() ? 1 : 0, true);
      }
   }

   private boolean isReverseAnimationVisible() {
      return !this.hitColorAnimation.getSelectedValue().equals("None");
   }

}
