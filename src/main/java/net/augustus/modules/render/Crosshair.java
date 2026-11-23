package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.events.listeners.RenderListener;
import net.augustus.ClientManagers;
import net.augustus.settings.ColorSetting;
import net.augustus.render.RenderUtil;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.ui.GuiIngameHook;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.minecraft.client.gui.ScaledResolution;

public class Crosshair extends Module implements RenderListener {
   public final BooleanValue defaultCrossHair;
   public final BooleanValue thirdPerson;
   public final StringValue scale;
   public final ColorSetting color2;
   public final StringValue length;
   public final StringValue width;
   public final StringValue margin;
   public final BooleanValue dot;
   public final BooleanValue tStyle;
   public final BooleanValue shadow;

   public Crosshair() {
       super("Crosshair", "Fadenkreuz", new Color(157, 230, 106), Categorys.RENDER);
      this.defaultCrossHair = new BooleanValue(this, "DefaultCrossHair", false);
      this.thirdPerson = new BooleanValue(this, "ThirdPerson", false);
      StringValue stringValue = new StringValue(this, "Scale", 2.0, 1.0, 4.0, 2);
      BooleanValue defaultCrossHair2 = this.defaultCrossHair;
      this.defaultCrossHair.getClass();
      BooleanValue booleanValue = defaultCrossHair2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.scale = stringValue.visibleWhen(condition);
      ColorSetting colorSetting = new ColorSetting(this, "Color", new Color(94, 0, 255, 255));
      SettingVisibilityCondition condition2 = () -> this.isColorVisible();
      this.color2 = (ColorSetting)colorSetting.withVisibility(condition2);
      StringValue stringValue2 = new StringValue(this, "Length", 4.0, 0.0, 40.0, 0);
      SettingVisibilityCondition condition3 = () -> {
         return !this.defaultCrossHair.getValue();
      };
      this.length = stringValue2.visibleWhen(condition3);
      StringValue stringValue3 = new StringValue(this, "Width", 1.0, 1.0, 20.0, 0);
      SettingVisibilityCondition condition4 = () -> this.isWidthVisible();
      this.width = stringValue3.visibleWhen(condition4);
      StringValue stringValue4 = new StringValue(this, "Margin", 1.0, 0.0, 40.0, 0);
      SettingVisibilityCondition condition5 = () -> this.isMarginVisible();
      this.margin = stringValue4.visibleWhen(condition5);
      BooleanValue booleanValue2 = new BooleanValue(this, "Dot", true);
      SettingVisibilityCondition condition6 = () -> this.isDotVisible();
      this.dot = (BooleanValue)booleanValue2.withVisibility(condition6);
      BooleanValue booleanValue3 = new BooleanValue(this, "T-Style", false);
      SettingVisibilityCondition condition7 = () -> {
         return !this.defaultCrossHair.getValue();
      };
      this.tStyle = (BooleanValue)booleanValue3.withVisibility(condition7);
      BooleanValue booleanValue4 = new BooleanValue(this, "Shadow", true);
      SettingVisibilityCondition condition8 = () -> this.isShadowVisible();
      this.shadow = (BooleanValue)booleanValue4.withVisibility(condition8);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      int n = 1000000;
      byte b = 32;
      this.toggleEventSubscription(b, n);
   }

   @Override
   public void onRender(float f, ScaledResolution sr) {
      if (!ClientManagers.moduleManager.crosshair.defaultCrossHair.getValue()) {
         if (mc.ingameGUI.showCrosshair() && (mc.gameSettings.thirdPersonView < 1 || ClientManagers.moduleManager.crosshair.isToggled() && ClientManagers.moduleManager.crosshair.thirdPerson.getValue())) {
            BlurShadowManager.enableOneShotPass(false, ClientManagers.moduleManager.crosshair.shadow.getValue() && ClientManagers.moduleManager.shadow.isToggled());
            BlurShadowManager.submitRenderTask(false, shaderRenderPass -> renderCrosshairPass(sr, shaderRenderPass));
            RenderUtil.resetGlColorState();
         }
      }
   }

   private static void renderCrosshairPass(ScaledResolution sr, ShaderRenderPass shaderRenderPass) {
      GuiIngameHook.drawCustomCrosshair(sr);
   }

   private boolean isShadowVisible() {
      return !this.defaultCrossHair.getValue();
   }

   private boolean isDotVisible() {
      return !this.defaultCrossHair.getValue();
   }

   private boolean isMarginVisible() {
      return !this.defaultCrossHair.getValue();
   }

   private boolean isWidthVisible() {
      return !this.defaultCrossHair.getValue();
   }

   private boolean isColorVisible() {
      return !this.defaultCrossHair.getValue();
   }

}
