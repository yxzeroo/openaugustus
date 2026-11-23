package net.augustus.ui.menu;

import net.augustus.Augustus;
import net.augustus.ClientManagers;
import net.augustus.events.EventMotion;
import net.augustus.render.RenderUtil;
import net.augustus.ui.GuiButtonClickHandler;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.modules.misc.AugustusServer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.renderer.GlStateManager;

public class ClassicMainMenuScreen extends AbstractMainMenuScreen {

   private void onClientMenuClick(GuiButton button) {
      ClientTabContainerScreen clientTabContainerScreen = new ClientTabContainerScreen(this, 0);
      this.openScreen(clientTabContainerScreen);
   }

   private void renderMenuBackdrop(ShaderRenderPass shaderRenderPass) {
      int[] controlFlowState = EventMotion.getControlFlowState();
      if (ClientManagers.moduleManager.augustusServer.roundedButtons.getValue()) {
         RenderUtil.drawRoundedRect(
            (float)(this.getCenterX() - 115),
            (float)(this.getCenterY() / 2 + 21),
            (float)(this.getCenterX() + 115),
            (float)(this.getCenterY() / 2 + 154),
            (float)ClientManagers.moduleManager.augustusServer.roundFactor.getValue(),
            1,
            Integer.MIN_VALUE
         );
      } else {
         RenderUtil.drawRect3(this.getCenterX() - 115, this.getCenterY() / 2 + 21, this.getCenterX() + 115, this.getCenterY() / 2 + 154, Integer.MIN_VALUE);
      }
   }

   public ClassicMainMenuScreen() {
       super();
   }

   @Override
   public void initContent() {
      super.initContent();
      int n = this.getCenterY() / 2 + 60;
      int n2 = this.getCenterX() - 100;
      int n3 = n + 60;
      String optionsLabel2 = this.optionsLabel;
      GuiButtonClickHandler guiButtonClickHandler = button -> this.onOptionsClick(button);
      String str = optionsLabel2;
      byte b = 20;
      byte b2 = 98;
      int n4 = n3;
      int n5 = n2;
      this.addButton(n5, n4, b2, b, str, guiButtonClickHandler);
      int n6 = this.getCenterX() + 2;
      n3 = n + 60;
      optionsLabel2 = this.quitLabel;
      GuiButtonClickHandler guiButtonClickHandler2 = button2 -> this.mc.shutdown();
      String str2 = optionsLabel2;
      byte b3 = 20;
      byte b4 = 98;
      int n7 = n3;
      int n8 = n6;
      this.addButton(n8, n7, b4, b3, str2, guiButtonClickHandler2);
      EventMotion.getControlFlowState();
      n3 = this.getCenterX() - 100;
      int n9 = n - 24;
      String singleplayerLabel2 = this.singleplayerLabel;
      GuiButtonClickHandler guiButtonClickHandler3 = button3 -> {
         GuiSelectWorld guiSelectWorld = new GuiSelectWorld(this);
         this.openScreen(guiSelectWorld);
      };
      String str3 = singleplayerLabel2;
      byte b5 = 20;
      short s = 200;
      int n10 = n9;
      int n11 = n3;
      this.addButton(n11, n10, s, b5, str3, guiButtonClickHandler3);
      int n12 = this.getCenterX() - 100;
      optionsLabel2 = this.multiplayerLabel;
      GuiButtonClickHandler guiButtonClickHandler4 = button4 -> this.onMultiplayerClick(button4);
      String str4 = optionsLabel2;
      byte b6 = 20;
      short s2 = 200;
      int n13 = n12;
      this.addButton(n13, n, s2, b6, str4, guiButtonClickHandler4);
      int n14 = this.getCenterX() - 100;
      n3 = n + 24;
      optionsLabel2 = Augustus.getInstance().getName();
      GuiButtonClickHandler guiButtonClickHandler5 = button5 -> this.onClientMenuClick(button5);
      String str5 = optionsLabel2;
      byte b7 = 20;
      short s3 = 200;
      int n15 = n3;
      int n16 = n14;
      this.addButton(n16, n15, s3, b7, str5, guiButtonClickHandler5);
   }

   private void onMultiplayerClick(GuiButton button) {
      GuiMultiplayer guiMultiplayer = new GuiMultiplayer(this);
      this.openScreen(guiMultiplayer);
   }

   @Override
   public void drawContent(int n, int n2, float f) {
      BlurShadowManager.enableBlurOnce();
      BlurShadowManager.enableNormalPassOnce();
      BlurShadowManager.submitRenderTask(false, shaderRenderPass -> this.renderMenuBackdrop(shaderRenderPass));
      super.drawContent(n, n2, f);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      AugustusServer.renderConnectionStatus(false);
      Augustus instance = Augustus.getInstance();
      String name = instance.getName();
      float f2 = (float)this.getCenterX() / 5.0F;
      float f3 = (((float)this.getCenterY() / 2.0F - 24.0F) / 5.0F - 3.0F) / 2.0F + 3.0F;
      float f4 = 5.0F;
      float f5 = f3;
      float f6 = f2;
      String str = name;
      this.drawCenteredScaledString(str, f6, f5, f4);
   }

   private void onOptionsClick(GuiButton button) {
      GuiOptions guiOptions = new GuiOptions(this, this.mc.gameSettings);
      this.openScreen(guiOptions);
   }
}
