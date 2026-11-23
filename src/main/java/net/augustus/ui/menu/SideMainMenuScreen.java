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
import org.lwjgl.opengl.GL11;

public final class SideMainMenuScreen extends AbstractMainMenuScreen {
   private static final int BUTTON_HEIGHT = 20;
   private static final int BUTTON_WIDTH = 150;
   private static final int BUTTON_SPACING = 3;

   @Override
   public void initContent() {
      super.initContent();
      int n = this.getCenterY() - 51;
      String singleplayerLabel2 = this.singleplayerLabel;
      GuiButtonClickHandler guiButtonClickHandler = button -> {
         GuiSelectWorld guiSelectWorld = new GuiSelectWorld(this);
         this.openScreen(guiSelectWorld);
      };
      String str = singleplayerLabel2;
      n = this.addSidebarButton(n, str, guiButtonClickHandler);
      singleplayerLabel2 = this.multiplayerLabel;
      GuiButtonClickHandler guiButtonClickHandler2 = button2 -> this.onMultiplayerClick(button2);
      String str2 = singleplayerLabel2;
      n = this.addSidebarButton(n, str2, guiButtonClickHandler2);
      singleplayerLabel2 = Augustus.getInstance().getName();
      GuiButtonClickHandler guiButtonClickHandler3 = button3 -> this.onClientMenuClick(button3);
      String str3 = singleplayerLabel2;
      n = this.addSidebarButton(n, str3, guiButtonClickHandler3);
      singleplayerLabel2 = this.optionsLabel;
      GuiButtonClickHandler guiButtonClickHandler4 = button4 -> {
         GuiOptions guiOptions = new GuiOptions(this, this.mc.gameSettings);
         this.openScreen(guiOptions);
      };
      String str4 = singleplayerLabel2;
      n = this.addSidebarButton(n, str4, guiButtonClickHandler4);
      singleplayerLabel2 = this.quitLabel;
      GuiButtonClickHandler guiButtonClickHandler5 = button5 -> this.mc.shutdown();
      String str5 = singleplayerLabel2;
      n = this.addSidebarButton(n, str5, guiButtonClickHandler5);
   }

   private void renderSidebarBackdrop(ShaderRenderPass shaderRenderPass) {
      int[] controlFlowState = EventMotion.getControlFlowState();
      if (ClientManagers.moduleManager.augustusServer.roundedButtons.getValue()) {
         RenderUtil.drawRoundedRect(
            0.0F, 0.0F, 156.0F, (float)this.height, (float)ClientManagers.moduleManager.augustusServer.roundFactor.getValue(), 1, Integer.MIN_VALUE
         );
      } else {
         RenderUtil.drawRect3(0, 0, 156, this.height, Integer.MIN_VALUE);
      }
   }

   private void onMultiplayerClick(GuiButton button) {
      GuiMultiplayer guiMultiplayer = new GuiMultiplayer(this);
      this.openScreen(guiMultiplayer);
   }

   private void onClientMenuClick(GuiButton button) {
      ClientTabContainerScreen clientTabContainerScreen = new ClientTabContainerScreen(this, 0);
      this.openScreen(clientTabContainerScreen);
   }

   private int addSidebarButton(int n, String str, GuiButtonClickHandler guiButtonClickHandler) {
      super.addButton(3, n, 150, 20, str, guiButtonClickHandler);
      return n + 20 + 3;
   }

   public SideMainMenuScreen() {
       super();
   }

   @Override
   public void drawContent(int n, int n2, float f) {
      BlurShadowManager.enableBlurOnce();
      BlurShadowManager.enableNormalPassOnce();
      BlurShadowManager.submitRenderTask(false, shaderRenderPass -> this.renderSidebarBackdrop(shaderRenderPass));
      super.drawContent(n, n2, f);
      Augustus instance = Augustus.getInstance();
      String name = instance.getName();
      float f2 = 26.0F + (float)this.getCenterX() / 3.0F;
      float f3 = (float)this.getCenterY() / 3.0F - (float)this.mc.fontRendererObj.FONT_HEIGHT / 2.0F;
      float f4 = 3.0F;
      float f5 = f3;
      float f6 = f2;
      String str = name;
      this.drawCenteredScaledString(str, f6, f5, f4);
      GL11.glPushMatrix();
      GL11.glTranslated(78.0, (double)(this.getCenterY() + this.mc.fontRendererObj.FONT_HEIGHT * 2), 0.0);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      AugustusServer.renderConnectionStatus(false);
      GL11.glPopMatrix();
   }
}
