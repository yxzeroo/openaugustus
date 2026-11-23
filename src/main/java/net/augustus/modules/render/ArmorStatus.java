package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.render.RenderPassTask;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.settings.ColorSetting;
import net.augustus.settings.options.ArmorStatusPartOption;
import net.augustus.settings.SelectableOption;
import net.augustus.utils.math.MathStatisticsUtil;
import net.augustus.render.RenderUtil;
import net.augustus.utils.render.ColorUtil;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class ArmorStatus extends Module implements RenderListener {
   private final StringValue posX;
   private final StringValue posY;
   private final StringValue scale;
   private final BooleansSetting durabilityMode;
   private final ColorSetting durabilityColor;
   private final BooleanValue backGround;
   private final ColorSetting backgroundColor;
   private final BlurShadowSettings blurShadowSettings;
   private final BooleanValue dynamicArmorRendering;
   private final MultiOptionSetting armorPartsToShowStatus;

   public ArmorStatus() {
       super("ArmorStatus", "Rüstungszustand", new Color(125, 255, 203), Categorys.RENDER);
      this.posX = new StringValue(this, "PosX", 0.0, 0.0, 100.0, 2);
      this.posY = new StringValue(this, "PosY", 0.0, 0.0, 100.0, 2);
      this.scale = new StringValue(this, "Scale", 2.0, 0.5, 4.0, 2);
      this.durabilityMode = new BooleansSetting(this, "DurabilityMode", "Percentage", new String[]{"None", "Percentage", "Absolute"});
      ColorSetting colorSetting = new ColorSetting(this, "DurabilityColor", Color.white);
      SettingVisibilityCondition condition = () -> this.isDurabilityColorVisible();
      this.durabilityColor = (ColorSetting)colorSetting.withVisibility(condition);
      this.backGround = new BooleanValue(this, "BackGround", false);
      ColorSetting colorSetting2 = new ColorSetting(this, "BackgroundColor", new Color(0, 0, 0, 120));
      BooleanValue backGround2 = this.backGround;
      this.backGround.getClass();
      BooleanValue booleanValue = backGround2;
      SettingVisibilityCondition condition2 = () -> booleanValue.getValue();
      this.backgroundColor = (ColorSetting)colorSetting2.withVisibility(condition2);
      this.blurShadowSettings = new BlurShadowSettings(this);
      this.dynamicArmorRendering = new BooleanValue(this, "DynamicArmorRendering", true);
      this.armorPartsToShowStatus = new MultiOptionSetting(this, "ArmorPartsToShowStatus", (SelectableOption[])ArmorStatusPartOption.OPTIONS.toArray(new ArmorStatusPartOption[0]));
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
      GL11.glPushMatrix();
      GL11.glScaled((double)(1.0F / (float)sr.getScaleFactor()), (double)(1.0F / (float)sr.getScaleFactor()), 1.0);
      String[] strings = this.buildDurabilityLabels();
      int n = this.computeMaxLabelWidth(strings);
      int n2 = this.countVisibleArmorPieces() * 20;
      double value = this.scale.getValue();
      GL11.glTranslated(
         ((double)mc.displayWidth - (double)(22 + n) * value) * (this.posX.getValue() / 100.0),
         ((double)mc.displayHeight - (double)n2 * value) * (this.posY.getValue() / 100.0),
         0.0
      );
      GL11.glScaled(value, value, 1.0);
      this.renderArmorList(strings, n, n2);
      GL11.glPopMatrix();
      RenderUtil.resetGlColorState();
   }

   private void renderArmorList(String[] strings, int n, int n2) {
      boolean value = this.backGround.getValue();
      if (value) {
         boolean isBlurActive = this.blurShadowSettings.isBlurActive();
         BlurShadowManager.enableOneShotPass(isBlurActive, this.blurShadowSettings.isShadowActive());
         BlurShadowManager.submitRenderTask(false, shaderRenderPass -> this.renderBackgroundRect(n, n2, shaderRenderPass));
      }

      GlStateManager.enableTexture2D();
      int n3 = 0;

      for (int i = strings.length - 1; i >= 0; i--) {
         if (ArmorStatusPartOption.isPartEnabled(i)) {
            this.renderArmorSlotWithShader(strings, i, n3);
            if (strings[i] != null || !this.dynamicArmorRendering.getValue()) {
               n3 += 20;
            }
         }
      }
   }

   private int countVisibleArmorPieces() {
      int n = 0;

      for (int i = 0; i < mc.thePlayer.inventory.armorInventory.length; i++) {
         if (ArmorStatusPartOption.isPartEnabled(i) && (!this.dynamicArmorRendering.getValue() || mc.thePlayer.inventory.armorInventory[i] != null)) {
            n++;
         }
      }

      return n;
   }

   private void renderArmorSlotWithShader(String[] strings, int n, int n2) {
      if (this.hasArmorPiece(n)) {
         if (this.backGround.getValue()) {
            this.renderArmorPieceAndDurability(strings, n, n2);
         } else {
            boolean isBlurActive = this.blurShadowSettings.isBlurActive();
            BlurShadowManager.enableOneShotPass(isBlurActive, this.blurShadowSettings.isShadowActive());
            RenderPassTask renderPassTask = shaderRenderPass -> {
               this.renderArmorPieceAndDurability(strings, n, n2);
            };
            boolean flag = true;
            BlurShadowManager.submitRenderTask(flag, renderPassTask);
         }
      }
   }

   private void renderArmorPieceAndDurability(String[] strings, int n, int n2) {
      if (mc.thePlayer != null) {
         ItemStack currentArmor = mc.thePlayer.getCurrentArmor(n);
         if (currentArmor != null) {
            int n3 = n2 + 2;
            boolean flag = false;
            int n4 = n3;
            byte b = 3;
            RenderUtil.renderItemIntoGuiElevated(currentArmor, b, n4, flag);
            String str = strings[n];
            int n5 = n2 + 6;
            String str2 = str;
            this.drawDurabilityText(str2, n5);
         }
      }
   }

   private boolean hasArmorPiece(int n) {
      return mc.thePlayer.getCurrentArmor(n) != null;
   }

   private void drawDurabilityText(String str, int n) {
      if (str != null && !str.isEmpty()) {
         byte b = 0;
         mc.fontRendererObj.drawString(str, 21, n, this.durabilityColor.getColorInt(b));
      }
   }

   private String[] buildDurabilityLabels() {
      ItemStack[] armorInventory = mc.thePlayer.inventory.armorInventory;
      String[] strings = new String[armorInventory.length];

      for (int i = 0; i < armorInventory.length; i++) {
         ItemStack stack = armorInventory[i];
         if (stack != null) {
            strings[i] = this.formatDurability(stack);
         }
      }

      return strings;
   }

   private String formatDurability(ItemStack stack) {
      String selectedValue = this.durabilityMode.getSelectedValue();
      switch (selectedValue) {
         case "Percentage":
            return MathStatisticsUtil.formatRounded(100.0 - (double)stack.getItemDamage() / (double)stack.getMaxDamage() * 100.0, 1) + "%";
         case "Absolute":
            return String.valueOf(stack.getMaxDamage() - stack.getItemDamage());
         default:
            return "";
      }
   }

   private int computeMaxLabelWidth(String[] strings) {
      boolean flag = !this.durabilityMode.getSelectedValue().equals("None");
      int n = 0;

      for (int i = 0; i < strings.length; i++) {
         String str = strings[i];
         if (str != null && ArmorStatusPartOption.isPartEnabled(i)) {
            int stringWidth = mc.fontRendererObj.getStringWidth(str);
            if (stringWidth > n) {
               n = stringWidth;
            }
         }
      }

      return n + (flag && n > 0 ? 3 : 0);
   }

   private void renderBackgroundRect(int n, int n2, ShaderRenderPass shaderRenderPass) {
      int n3 = 22 + n;
      byte b = 0;
      int colorInt = this.backgroundColor.getColorInt(b);
      RenderUtil.drawRect3(0, 0, n3, n2, ColorUtil.resolvePassColor(shaderRenderPass, colorInt));
   }

   private boolean isDurabilityColorVisible() {
      return !this.durabilityMode.getSelectedValue().equals("None");
   }

}
