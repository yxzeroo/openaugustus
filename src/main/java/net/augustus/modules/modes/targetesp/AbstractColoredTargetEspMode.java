package net.augustus.modules.modes.targetesp;

import net.augustus.settings.ColorSetting;
import net.augustus.settings.SettingVisibilityCondition;
import java.awt.Color;
import net.augustus.modules.render.TargetESP;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.minecraft.entity.EntityLivingBase;

public abstract class AbstractColoredTargetEspMode extends AbstractTargetEspMode {
   private final ColorSetting color;
   private final BooleanValue hitColorBoolean;
   private final ColorSetting hitColor;

   public AbstractColoredTargetEspMode(TargetESP targetESP, DoubleValue doubleValue, String str) {
       super(targetESP, doubleValue, str);
      String str2 = str + "Color";
      Color color2 = new Color(41, 146, 222, 120);
      String str3 = str2;
      ColorSetting colorSetting = new ColorSetting(targetESP, str3, color2, doubleValue);
      String str4 = "Color";
      colorSetting = (ColorSetting)colorSetting.withDisplayName(str4);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.color = (ColorSetting)colorSetting.withVisibility(condition);
      BooleanValue booleanValue = new BooleanValue(targetESP, str + "HitColorBoolean", true, doubleValue);
      String str5 = "Hit color";
      BooleanValue booleanValue2 = (BooleanValue)booleanValue.withDisplayName(str5);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.hitColorBoolean = (BooleanValue)booleanValue2.withVisibility(condition2);
      String str6 = str + "HitColor";
      color2 = new Color(255, 0, 0, 120);
      str3 = str6;
      ColorSetting colorSetting2 = new ColorSetting(targetESP, str3, color2, doubleValue);
      String str7 = "Hit color";
      ColorSetting colorSetting3 = (ColorSetting)colorSetting2.withDisplayName(str7);
      SettingVisibilityCondition condition3 = () -> this.isHitColorVisible();
      this.hitColor = (ColorSetting)colorSetting3.withVisibility(condition3);
   }

   public float getInterpolatedRenderX(EntityLivingBase entity) {
      return (float)(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)mc.getTimer().renderPartialTicks - mc.getRenderManager().getRenderPosX());
   }

   public float getInterpolatedRenderY(EntityLivingBase entity) {
      return (float)(entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)mc.getTimer().renderPartialTicks - mc.getRenderManager().getRenderPosY());
   }

   public float getInterpolatedRenderZ(EntityLivingBase entity) {
      return (float)(entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)mc.getTimer().renderPartialTicks - mc.getRenderManager().getRenderPosZ());
   }

   public int getTargetColor(EntityLivingBase entity) {
      int n;
      if (this.hitColorBoolean.getValue() && entity.hurtTime != 0) {
         byte b = 0;
         n = this.hitColor.getColorInt(b);
      } else {
         byte b2 = 0;
         n = this.color.getColorInt(b2);
      }

      return n;
   }

   private boolean isHitColorVisible() {
      return this.isSelected() && this.hitColorBoolean.getValue();
   }

}
