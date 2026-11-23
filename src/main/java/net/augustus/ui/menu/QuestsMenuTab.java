package net.augustus.ui.menu;

import net.augustus.Augustus;
import net.augustus.ClientManagers;
import net.augustus.quests.ServerQuest;
import net.augustus.render.RenderUtil;
import net.augustus.ui.AugustusGuiScreen;
import net.augustus.ui.widgets.ScrollBar;
import java.io.IOException;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class QuestsMenuTab extends ClientMenuTab {
   public final ScrollBar scrollBar;

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      double d = (double)mouseX;
      double d2 = (double)mouseY;
      double d3 = d2;
      double d4 = d;
      this.scrollBar.handleMouseClick(d4, d3);
   }

   public void drawQuestIcon(int n, int n2, float f, float f2, ResourceLocation location) {
      this.mc.getTextureManager().bindTexture(location);
      GlStateManager.enableBlend();
      Gui.drawModalRectWithCustomSizedTexture(n, n2, 0.0F, 0.0F, (int)f, (int)f2, f, f2);
      GlStateManager.disableBlend();
   }

   public void drawContent(int n, int n2, float f) {
      this.scrollBar.updateAndDraw();
      CreditsSection.getControlFlowGuard();
      int n3 = (int)(52.0 + this.scrollBar.getClampedScrollOffset());
      float f2 = (float)(this.width + 102) / 2.0F;
      if (Augustus.getInstance().getQuestManager() != null) {
         for (ServerQuest serverQuest : (Iterable<ServerQuest>)(Augustus.getInstance().getQuestManager().getQuests())) {
            if (ClientManagers.moduleManager.augustusServer.roundedButtons.getValue()) {
               RenderUtil.drawRoundedRect(
                  f2 - 150.0F,
                  (float)n3,
                  f2 + 150.0F,
                  (float)n3 + 40.0F,
                  (float)ClientManagers.moduleManager.augustusServer.roundFactor.getValue(),
                  1,
                  -2147483648
               );
            } else {
               RenderUtil.drawRect2(f2 - 150.0F, (float)n3, f2 + 150.0F, (float)n3 + 40.0F, -2147483648);
            }

            String str = "§a§l" + serverQuest.getDescription();
            float f3 = f2 - 146.0F;
            float f4 = (float)(n3 + 4);
            byte b = -1;
            float f5 = f4;
            float f6 = f3;
            String str2 = str;
            ((AugustusGuiScreen)this).drawString(str2, f6, f5, b);
            String str3 = "§7" + serverQuest.getRemainingTimeText();
            f3 = f2 - 146.0F;
            f4 = (float)(n3 + 16);
            byte b2 = -1;
            float f7 = f4;
            float f8 = f3;
            String str4 = str3;
            ((AugustusGuiScreen)this).drawString(str4, f8, f7, b2);
            String str5 = "§7" + (int)serverQuest.getProgressPercent() + "% finished";
            f3 = f2 - 146.0F;
            f4 = (float)(n3 + 28);
            byte b3 = -1;
            float f9 = f4;
            float f10 = f3;
            String str6 = str5;
            ((AugustusGuiScreen)this).drawString(str6, f10, f9, b3);
            if (!serverQuest.isCompleted()) {
               String str7 = "§b" + serverQuest.getRewardExperience() + " XP";
               int n4 = this.fontRendererObj.getStringWidth(str7) * 2;
               f3 = (f2 + 146.0F - (float)n4) / 2.0F;
               f4 = (float)((n3 + 12) / 2);
               byte b4 = -1;
               float f11 = 2.0F;
               float f12 = f4;
               float f13 = f3;
               ((AugustusGuiScreen)this).drawScaledString(str7, f13, f12, f11, b4);
            } else {
               GlStateManager.color(255.0F, 255.0F, 255.0F, 1.0F);
               int n5 = (int)(f2 + 115.0F);
               int n6 = n3 + 5;
               ResourceLocation augustustexture = GuiMultiplayer.AUGUSTUS_TEXTURE;
               float f14 = 30.0F;
               float f15 = 30.0F;
               int n7 = n6;
               int n8 = n5;
               this.drawQuestIcon(n8, n7, f15, f14, augustustexture);
            }

            n3 += 44;
         }
      }
   }

   public QuestsMenuTab() {
       super("Quests");
      this.scrollBar = new ScrollBar(44);
   }

   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
      super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      double d = (double)mouseY;
      this.scrollBar.handleMouseDrag(d);
   }

   public void initContent() {
      int[] controlFlowGuard = CreditsSection.getControlFlowGuard();
      super.initContent();
      if (Augustus.getInstance().getQuestManager() != null) {
         ScrollBar scrollBar2 = this.scrollBar;
         int size = Augustus.getInstance().getQuestManager().getQuests().size();
         scrollBar2.setItemCount(size);
      }

      float f = (float)(this.width + 102) / 2.0F;
      int n = (int)(f + 152.0F);
      int n2 = (int)(f + 155.0F);
      int height2 = this.height;
      int n3 = n2;
      byte b = 52;
      int n4 = n;
      this.scrollBar.setBounds(n4, b, n3, height2);
   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      this.scrollBar.handleMouseWheel();
   }

}
