package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.settings.ColorSetting;
import net.augustus.render.RenderUtil;
import net.augustus.utils.render.ColorUtil;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.IChatComponent;

public class GoldSpawnTimer extends Module implements RenderListener, TickListener, PacketListener {
   private final StringValue xCoord;
   private final StringValue yCoord;
   private final StringValue scale;
   private final BooleanValue backGround;
   private final ColorSetting backgroundColor;
   public final BlurShadowSettings blurShadowSettings;
   private boolean languageCommandSent;
   private long roundStartTimeMs;
   private boolean trackingActive;

   public GoldSpawnTimer() {
       super("GoldSpawnTimer", "GoldEntstehungsUhr", new Color(140, 255, 190), Categorys.RENDER);
      this.xCoord = new StringValue(this, "X-Coord", 0.0, 0.0, 100.0, 2);
      this.yCoord = new StringValue(this, "Y-Coord", 0.0, 0.0, 100.0, 2);
      this.scale = new StringValue(this, "Scale", 2.0, 0.5, 4.0, 2);
      this.backGround = new BooleanValue(this, "BackGround", true);
      ColorSetting colorSetting = new ColorSetting(this, "BackgroundColor", new Color(0, 0, 0, 150));
      BooleanValue backGround2 = this.backGround;
      this.backGround.getClass();
      BooleanValue booleanValue = backGround2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.backgroundColor = (ColorSetting)colorSetting.withVisibility(condition);
      BlurShadowSettings blurShadowSettings2 = new BlurShadowSettings(this);
      Color black = Color.black;
      this.blurShadowSettings = blurShadowSettings2.withCustomShadowColor(black);
      this.languageCommandSent = false;
      this.roundStartTimeMs = 0L;
      this.trackingActive = false;
   }

   @Override
   public void onEnable() {
      this.languageCommandSent = false;
      this.trackingActive = false;
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 32;
      this.toggleEventSubscription(b);
      byte b2 = 40;
      this.toggleEventSubscription(b2);
      byte b3 = 26;
      this.toggleEventSubscription(b3);
   }

   @Override
   public void onTick2() {
      this.sendGermanLanguageCommand();
      if (mc.thePlayer.ticksExisted > 20) {
         IChatComponent header = mc.ingameGUI.getTabList().getHeader();
         if (mc.ingameGUI != null && mc.ingameGUI.getTabList() != null && mc.ingameGUI.getTabList().getHeader() != null && !header.getFormattedText().contains("BW")) {
            this.trackingActive = false;
         }
      }

      if (mc.isSingleplayer()) {
         this.trackingActive = false;
      }
   }

   @Override
   public void onRender(float f, ScaledResolution sr) {
      if (this.trackingActive) {
         GlStateManager.pushMatrix();
         double value = this.scale.getValue();
         GlStateManager.scale(1.0F / (float)sr.getScaleFactor(), 1.0F / (float)sr.getScaleFactor(), 1.0F);
         GlStateManager.scale(value, value, 1.0);
         ScaledResolution sr2 = new ScaledResolution((double)mc.displayWidth * (1.0 / value), (double)mc.displayHeight * (1.0 / value), (int)value);
         int n = (int)((System.currentTimeMillis() - this.roundStartTimeMs) / 1000L);
         int n2 = 1 + (n >= 60 ? 29 - n % 30 : 59 - n % 60);
         String str = n2 + " sec";
         int n3 = 20 + mc.fontRendererObj.getStringWidth(str) + 3;
         byte b = 15;
         int n4 = (int)((double)(sr2.getScaledWidth() - n3) * (this.xCoord.getValue() / 100.0));
         int n5 = (int)((double)(sr2.getScaledHeight() - b) * (this.yCoord.getValue() / 100.0));
         if (this.backGround.getValue()) {
            byte b2 = 0;
            int colorInt = this.backgroundColor.getColorInt(b2);
            boolean isBlurActive = this.blurShadowSettings.isBlurActive();
            BlurShadowManager.enableOneShotPass(isBlurActive, this.blurShadowSettings.isShadowActive());
            BlurShadowManager.submitRenderTask(false, shaderRenderPass -> this.renderBackgroundPass(n4, n5, n3, b, colorInt, shaderRenderPass));
         }

         ItemStack stack = new ItemStack(Items.gold_ingot);
         int n6 = n4 + 2;
         boolean flag = false;
         int n7 = n6;
         RenderUtil.renderItemIntoGuiElevated(stack, n7, n5, flag);
         mc.fontRendererObj.drawString(str, n4 + 22, n5 + 4, -1);
         GlStateManager.popMatrix();
      }

      RenderUtil.resetGlColorState();
   }

   private void sendGermanLanguageCommand() {
      if (mc.thePlayer != null && mc.currentScreen == null && mc.theWorld != null && !this.languageCommandSent && mc.thePlayer.ticksExisted > 20) {
         this.languageCommandSent = true;
         mc.thePlayer.sendChatMessage("/language german");
      }
   }

   @Override
   public void onPacket(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof S02PacketChat) {
         S02PacketChat packet2 = (S02PacketChat)packet;
         if (packet2.getChatComponent() != null && packet2.getChatComponent().getFormattedText().contains("§r§aDie Runde beginnt!§r")) {
            String str = "Started gold spawn time tracking.";
            this.sendChatMessage2(str);
            this.roundStartTimeMs = System.currentTimeMillis() + 500L;
            this.trackingActive = true;
         }
      }
   }

   private void renderBackgroundPass(int n, int n2, int n3, int n4, int n5, ShaderRenderPass shaderRenderPass) {
      if (shaderRenderPass == ShaderRenderPass.SHADOW) {
         int n6 = n + n3;
         int n7 = n2 + n4;
         BlurShadowSettings blurShadowSettings2 = this.blurShadowSettings;
         int n8 = ColorUtil.withAlpha(n5, 255);
         int n9 = blurShadowSettings2.resolveShadowColor(n8);
         RenderUtil.drawRect3(n, n2, n6, n7, n9);
      } else {
         int n10 = n + n3;
         int n11 = n2 + n4;
         RenderUtil.drawRect3(n, n2, n10, n11, n5);
      }
   }

}
