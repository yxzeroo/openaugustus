package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.settings.ColorSetting;
import net.augustus.render.RenderUtil;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class Chat extends Module implements RenderListener {
   public final BooleanValue remove;
   public final BooleanValue noChatLimit;
   public final BooleanValue fontShadow;
   public final BooleanValue backGround;
   public final BooleanValue customBackGroundColor;
   public final ColorSetting backgroundColor;
   public final BlurShadowSettings blurShadowSettings;

   public Chat() {
       super("Chat", "Plaudern", new Color(169, 3, 252), Categorys.RENDER);
      this.remove = new BooleanValue(this, "Remove", false);
      BooleanValue booleanValue = new BooleanValue(this, "NoChatLimit", true);
      SettingVisibilityCondition condition = () -> this.isNoChatLimitVisible();
      this.noChatLimit = (BooleanValue)booleanValue.withVisibility(condition);
      BooleanValue booleanValue2 = new BooleanValue(this, "FontShadow", true);
      SettingVisibilityCondition condition2 = () -> {
         return !this.remove.getValue();
      };
      this.fontShadow = (BooleanValue)booleanValue2.withVisibility(condition2);
      BooleanValue booleanValue3 = new BooleanValue(this, "BackGround", true);
      SettingVisibilityCondition condition3 = () -> this.isBackgroundToggleVisible();
      this.backGround = (BooleanValue)booleanValue3.withVisibility(condition3);
      BooleanValue booleanValue4 = new BooleanValue(this, "CustomBackGroundColor", false);
      SettingVisibilityCondition condition4 = () -> this.isCustomBackgroundColorToggleVisible();
      this.customBackGroundColor = (BooleanValue)booleanValue4.withVisibility(condition4);
      ColorSetting colorSetting = new ColorSetting(this, "BackGroundColor", new Color(0, 0, 0, 150));
      SettingVisibilityCondition condition5 = () -> {
         return !this.remove.getValue() && this.backGround.getValue() && this.customBackGroundColor.getValue();
      };
      this.backgroundColor = (ColorSetting)colorSetting.withVisibility(condition5);
      BlurShadowSettings blurShadowSettings2 = new BlurShadowSettings(this);
      Color black = Color.black;
      BlurShadowSettings blurShadowSettings3 = blurShadowSettings2.withCustomShadowColor(black);
      SettingVisibilityCondition condition6 = () -> this.isBlurShadowVisible();
      this.blurShadowSettings = (BlurShadowSettings)blurShadowSettings3.withVisibility(condition6);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 32;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onRender(float f, ScaledResolution sr) {
      if (this.isChatVisible()) {
         GlStateManager.pushMatrix();
         GlStateManager.translate(0.0F, (float)(sr.getScaledHeight() - 48), 0.0F);
         GlStateManager.disableBlend();
         GuiNewChat chatGUI = mc.ingameGUI.getChatGUI();
         int updateCounter = mc.ingameGUI.getUpdateCounter();
         boolean value = this.fontShadow.getValue();
         boolean value2 = this.backGround.getValue();
         int n;
         if (this.customBackGroundColor.getValue() && this.backGround.getValue()) {
            byte b = 0;
            n = this.backgroundColor.getColorInt(b);
         } else {
            n = Integer.MIN_VALUE;
         }

         chatGUI.drawChatCustom(updateCounter, value, value2, n);
         GlStateManager.popMatrix();
      }

      RenderUtil.resetGlColorState();
   }

   private boolean isChatVisible() {
      return !this.remove.getValue();
   }

   private boolean isBlurShadowVisible() {
      return !this.remove.getValue() && this.backGround.getValue();
   }

   private boolean isCustomBackgroundColorToggleVisible() {
      return !this.remove.getValue() && this.backGround.getValue();
   }

   private boolean isBackgroundToggleVisible() {
      return !this.remove.getValue();
   }

   private boolean isNoChatLimitVisible() {
      return !this.remove.getValue();
   }


}
