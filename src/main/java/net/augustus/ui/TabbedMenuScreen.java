package net.augustus.ui;

import net.augustus.render.RenderUtil;
import net.augustus.ui.menu.ClientMenuTab;
import net.augustus.ui.widgets.ScrollBar;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.utils.render.ShaderRenderPass;
import java.io.IOException;
import java.util.List;
import net.augustus.modules.misc.AugustusServer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

public class TabbedMenuScreen extends AugustusGuiScreen {
   public static final int TAB_BUTTON_HEIGHT = 20;
   private final List tabs;
   public static final int HEADER_HEIGHT = 52;
   public static final int TAB_SPACING = 4;
   public static final int SIDEBAR_WIDTH = 102;
   private int selectedTabIndex;

   private void onTabSelected(ClientMenuTab clientMenuTab, GuiButton button) {
      this.selectedTabIndex = this.getTabs().indexOf(clientMenuTab);
      ClientMenuTab selectedTab = this.getSelectedTab();
      int height2 = this.height;
      int width2 = this.width;
      Minecraft mc2 = this.mc;
      selectedTab.onResize(mc2, width2, height2);
   }

   @Override
   public void handleKeyboardInput() throws IOException {
      String controlFlowGuard = ScrollBar.getControlFlowGuard();
      super.handleKeyboardInput();
      if (this.getSelectedTab() != null) {
         this.getSelectedTab().handleKeyboardInput();
      }
   }

   public List getTabs() {
      return this.tabs;
   }

   public TabbedMenuScreen(List list, GuiScreen screen, int n) {
       super(screen);
      ScrollBar.getControlFlowGuard();
      this.selectedTabIndex = n;
      this.setParentScreen(screen);
      this.tabs = list;

      for (ClientMenuTab clientMenuTab : (Iterable<ClientMenuTab>)(this.tabs)) {
         if (this.shouldHideTab(clientMenuTab)) {
            list.remove(clientMenuTab);
         }
      }

      this.disableDefaultBackground();
   }

   public boolean shouldHideTab(ClientMenuTab clientMenuTab) {
      return false;
   }

   @Override
   public void updateScreen() {
      String controlFlowGuard = ScrollBar.getControlFlowGuard();
      super.updateScreen();
      if (this.getSelectedTab() != null) {
         this.getSelectedTab().updateScreen();
      }
   }

   @Override
   public void onGuiClosed() {
      String controlFlowGuard = ScrollBar.getControlFlowGuard();
      super.onGuiClosed();
      if (this.getSelectedTab() != null) {
         this.getSelectedTab().onGuiClosed();
      }
   }

   @Override
   public void initContent() {
      super.initContent();
      ScrollBar.getControlFlowGuard();
      NO_TOGGLE_SCREENS.add(this);
      int n = 0;

      for (ClientMenuTab clientMenuTab : (Iterable<ClientMenuTab>)(this.getTabs())) {
         int n2 = 52 + n * 24;
         String tabTitle = clientMenuTab.getTabTitle();
         GuiButtonClickHandler guiButtonClickHandler = button -> this.onTabSelected(clientMenuTab, button);
         String str = tabTitle;
         byte b = 20;
         byte b2 = 90;
         int n3 = n2;
         byte b3 = 5;
         this.addButton(b3, n3, b2, b, str, guiButtonClickHandler);
         n++;
      }

      this.getSelectedTab().setWorldAndResolution(Minecraft.getMinecraft(), this.width, this.height);
   }

   @Override
   public void onResize(Minecraft mc2, int n, int n2) {
      String controlFlowGuard = ScrollBar.getControlFlowGuard();
      super.onResize(mc2, n, n2);
      if (this.getSelectedTab() != null) {
         this.getSelectedTab().onResize(mc2, n, n2);
      }
   }

   @Override
   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      this.getSelectedTab().disableDefaultBackground();
      this.getSelectedTab().drawScreen(mouseX, mouseY, partialTicks);
      ScaledResolution sr = new ScaledResolution(this.mc);
      boolean flag = true;
      BlurShadowManager.flushRenderQueues(sr, flag);
      BlurShadowManager.enableOneShotPass(true, false);
      BlurShadowManager.submitRenderTask(false, shaderRenderPass -> {
         RenderUtil.drawRect3(0, 50, 100, this.height, Integer.MIN_VALUE);
         RenderUtil.drawRect3(0, 0, this.width, 50, Integer.MIN_VALUE);
      });
      super.drawScreen(mouseX, mouseY, partialTicks);
      GL11.glPushMatrix();
      GL11.glTranslatef(51.0F, 0.0F, 0.0F);
      ScrollBar.getControlFlowGuard();
      String str = EnumChatFormatting.RED + this.getSelectedTab().getTabTitle();
      float f = (float)this.width / 4.0F;
      float f2 = 2.0F;
      float f3 = 8.0F;
      float f4 = f;
      String str2 = str;
      this.drawCenteredScaledString(str2, f4, f3, f2);
      GL11.glPopMatrix();
      AugustusServer.renderMainMenuAccountInfo();
      AugustusServer.renderConnectionStatus(true);
      String statusText = ClientMenuTab.getStatusText(this.getSelectedTab());
      if (statusText != null) {
         float f5 = (float)(102 + this.width) / 2.0F;
         int rgb = ClientMenuTab.getStatusColor(this.getSelectedTab()).getRGB();
         float f6 = 38.0F;
         float f7 = f5;
         this.drawCenteredString(statusText, f7, f6, rgb);
      }
   }

   public ClientMenuTab getSelectedTab() {
      return (ClientMenuTab)this.getTabs().get(this.selectedTabIndex);
   }

   @Override
   public void handleMouseInput() throws IOException {
      String controlFlowGuard = ScrollBar.getControlFlowGuard();
      super.handleMouseInput();
      if (this.getSelectedTab() != null) {
         this.getSelectedTab().handleMouseInput();
      }
   }
}
