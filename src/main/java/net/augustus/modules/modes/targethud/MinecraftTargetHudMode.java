package net.augustus.modules.modes.targethud;

import net.augustus.render.RenderUtil;
import net.augustus.render.StencilUtil;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.settings.ColorSetting;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.utils.render.ColorUtil;
import net.augustus.utils.render.ShaderRenderPass;
import java.awt.Color;
import java.util.function.BiFunction;
import net.augustus.modules.render.TargetHud;
import net.augustus.settings.DoubleValue;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class MinecraftTargetHudMode extends AbstractTargetHudMode {
   private final ColorSetting backGroundColor;
   private final ColorSetting healthBackGroundColor;
   private final ColorSetting healthColor;
   private final ColorSetting textColor;

   public MinecraftTargetHudMode(TargetHud targetHud, DoubleValue doubleValue) {
      super(targetHud, doubleValue, "Minecraft", true);
      String str = this.getName() + "BackGroundColor";
      Color color = new Color(0, 0, 0, 180);
      String str2 = str;
      ColorSetting colorSetting = new ColorSetting(targetHud, str2, color, doubleValue);
      String str3 = "BackGroundColor";
      ColorSetting colorSetting2 = (ColorSetting)colorSetting.withDisplayName(str3);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.backGroundColor = (ColorSetting)colorSetting2.withVisibility(condition);
      str = this.getName() + "HealthBackGroundColor";
      color = new Color(0, 0, 0);
      str2 = str;
      ColorSetting colorSetting3 = new ColorSetting(targetHud, str2, color, doubleValue);
      String str4 = "HealthBackGroundColor";
      ColorSetting colorSetting4 = (ColorSetting)colorSetting3.withDisplayName(str4);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.healthBackGroundColor = (ColorSetting)colorSetting4.withVisibility(condition2);
      str = this.getName() + "HealthColor";
      color = new Color(255, 0, 0);
      str2 = str;
      ColorSetting colorSetting5 = new ColorSetting(targetHud, str2, color, doubleValue);
      String str5 = "HealthColor";
      ColorSetting colorSetting6 = (ColorSetting)colorSetting5.withDisplayName(str5);
      SettingVisibilityCondition condition3 = () -> this.isSelected();
      this.healthColor = (ColorSetting)colorSetting6.withVisibility(condition3);
      str = this.getName() + "TextColor";
      color = new Color(255, 255, 255);
      str2 = str;
      ColorSetting colorSetting7 = new ColorSetting(targetHud, str2, color, doubleValue);
      String str6 = "TextColor";
      ColorSetting colorSetting8 = (ColorSetting)colorSetting7.withDisplayName(str6);
      SettingVisibilityCondition condition4 = () -> this.isSelected();
      this.textColor = (ColorSetting)colorSetting8.withVisibility(condition4);
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new MinecraftTargetHudMode((TargetHud)obj, (DoubleValue)obj2);
   }

   @Override
   public int getHalfWidth() {
      return 156;
   }

   @Override
   public int getHalfHeight() {
      return 56;
   }

   @Override
   public void drawHudContent(AbstractClientPlayer clientPlayer, NetworkPlayerInfo networkPlayerInfo, ScaledResolution sr) {
      boolean isUpsideDownPlayer = this.isUpsideDownPlayer(clientPlayer);
      byte b = 0;
      int colorInt = this.backGroundColor.getColorInt(b);
      boolean isBlurActive = ((TargetHud)this.parentModule).blurShadowSettings.isBlurActive();
      BlurShadowManager.enableOneShotPass(isBlurActive, ((TargetHud)this.parentModule).blurShadowSettings.isShadowActive());
      BlurShadowManager.submitRenderTask(false, shaderRenderPass -> {
         int[] controlFlowState22 = AbstractTargetHudMode.getControlFlowState2();
         float f = (float)(-this.getHalfWidth());
         int n;
         float f2 = (float)(-this.getHalfHeight());
         float halfWidth = (float)this.getHalfWidth();
         float halfHeight = (float)this.getHalfHeight();
         BlurShadowSettings blurShadowSettings = ((TargetHud)this.parentModule).blurShadowSettings;
         int n2 = ColorUtil.resolvePassColor(shaderRenderPass, colorInt);
         RenderUtil.drawRoundedRect(f, f2, halfWidth, halfHeight, 10.0F, 1, blurShadowSettings.resolvePassColor(shaderRenderPass, n2));
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         if (shaderRenderPass == ShaderRenderPass.NONE) {
            StencilUtil.initStencilToWrite();
            StencilUtil.beginStencilWrite();
            RenderUtil.drawRoundedRect2(50.0F, -50.0F, 150.0F, 50.0F, 8.0F, 1, Color.white);
            StencilUtil.readStencilBuffer(1);
            ResourceLocation locationSkin = networkPlayerInfo.getLocationSkin();
            byte b2 = 100;
            byte b3 = -50;
            byte b4 = 50;
            ResourceLocation location = locationSkin;
            this.drawPlayerHead(location, clientPlayer, isUpsideDownPlayer, b4, b3, b2);
            StencilUtil.uninitStencilBuffer();
         }

         byte b5 = 2;
         if (shaderRenderPass == ShaderRenderPass.NONE) {
            GlStateManager.pushMatrix();
            GlStateManager.scale((float)b5, (float)b5, 1.0F);
            FontRenderer fontRendererObj = mc.fontRendererObj;
            String name2 = clientPlayer.getGameProfile().getName();
            n = -145 / b5;
            int n3 = -45 / b5;
            byte b6 = 0;
            fontRendererObj.drawString(name2, n, n3, this.textColor.getColorInt(b6));
            GlStateManager.popMatrix();
         }

         this.renderHealthBar(clientPlayer, shaderRenderPass);
         this.renderEquipment(clientPlayer, shaderRenderPass, b5);
         BlurShadowManager.enableNormalPassOnce();
      });
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   private void renderHealthBar(AbstractClientPlayer clientPlayer, ShaderRenderPass shaderRenderPass) {
      int[] controlFlowState22 = AbstractTargetHudMode.getControlFlowState2();
      short s = 190;
      if (shaderRenderPass == ShaderRenderPass.NONE) {
         float playerHealth = this.getPlayerHealth(clientPlayer);
         this.updateHealthAnimation(clientPlayer, playerHealth);
      }

      float f = this.currentHealth / clientPlayer.getMaxHealth();
      if (shaderRenderPass == ShaderRenderPass.NONE) {
         float f2 = this.previousHealth / clientPlayer.getMaxHealth();
         this.healthAnimation.setBounds((float)s * f2, 0.0F, (float)s * f, 0.0F);
      }

      if (shaderRenderPass != ShaderRenderPass.BLUR
         && (float)s * f > -150.0F
         && mc.getCurrentServerData() != null
         && !mc.getCurrentServerData().serverIP.contains("gommehd.net")) {
         BlurShadowSettings blurShadowSettings = ((TargetHud)this.parentModule).blurShadowSettings;
         byte b = 0;
         int colorInt = this.healthBackGroundColor.getColorInt(b);
         RenderUtil.drawRoundedRect(-150.0F, -17.0F, 40.0F, 3.0F, 3.0F, 1, blurShadowSettings.resolvePassColor(shaderRenderPass, colorInt));
         float f3 = MathHelper.clamp_float(-150.0F + this.healthAnimation.updateX(), -150.0F, 40.0F);
         blurShadowSettings = ((TargetHud)this.parentModule).blurShadowSettings;
         byte b2 = 0;
         int colorInt2 = this.healthColor.getColorInt(b2);
         RenderUtil.drawRoundedRect(-150.0F, -17.0F, f3, 3.0F, 3.0F, 1, blurShadowSettings.resolvePassColor(shaderRenderPass, colorInt2));
      }
   }

   private void renderEquipment(AbstractClientPlayer clientPlayer, ShaderRenderPass shaderRenderPass, int n) {
      int[] controlFlowState22 = AbstractTargetHudMode.getControlFlowState2();
      if (shaderRenderPass != ShaderRenderPass.BLUR) {
         int n2 = -2;
         if (mc.getCurrentServerData() != null && !mc.getCurrentServerData().serverIP.contains("gommehd.net")) {
            n2 += 10;
         }

         GlStateManager.pushMatrix();
         GlStateManager.scale((float)n, (float)n, 1.0F);
         GlStateManager.pushMatrix();
         GlStateManager.enableRescaleNormal();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         RenderHelper.enableGUIStandardItemLighting();
         if (clientPlayer.getCurrentArmor(3) != null) {
            ItemStack currentArmor = clientPlayer.getCurrentArmor(3);
            boolean flag = false;
            byte b = -77;
            RenderUtil.renderItemIntoGuiElevated(currentArmor, b, n2, flag);
         }

         if (clientPlayer.getCurrentArmor(2) != null) {
            ItemStack currentArmor2 = clientPlayer.getCurrentArmor(2);
            boolean flag2 = false;
            byte b2 = -59;
            RenderUtil.renderItemIntoGuiElevated(currentArmor2, b2, n2, flag2);
         }

         if (clientPlayer.getCurrentArmor(1) != null) {
            ItemStack currentArmor3 = clientPlayer.getCurrentArmor(1);
            boolean flag3 = false;
            byte b3 = -41;
            RenderUtil.renderItemIntoGuiElevated(currentArmor3, b3, n2, flag3);
         }

         if (clientPlayer.getCurrentArmor(0) != null) {
            ItemStack currentArmor4 = clientPlayer.getCurrentArmor(0);
            boolean flag4 = false;
            byte b4 = -23;
            RenderUtil.renderItemIntoGuiElevated(currentArmor4, b4, n2, flag4);
         }

         if (clientPlayer.getCurrentEquippedItem() != null) {
            ItemStack currentEquippedItem = clientPlayer.getCurrentEquippedItem();
            int n3 = n2 - 1;
            boolean flag5 = false;
            int n4 = n3;
            byte b5 = 0;
            RenderUtil.renderItemIntoGuiElevated(currentEquippedItem, b5, n4, flag5);
         }

         GlStateManager.popMatrix();
         GlStateManager.popMatrix();
      }
   }

}
