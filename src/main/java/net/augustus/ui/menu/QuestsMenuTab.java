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
      double d3 = (double)mouseY;
      this.scrollBar.handleMouseClick(d, d3);
   }

   public void drawQuestIcon(int n, int n2, float f, float f2, ResourceLocation location) {
      this.mc.getTextureManager().bindTexture(location);
      GlStateManager.enableBlend();
      Gui.drawModalRectWithCustomSizedTexture(n, n2, 0.0F, 0.0F, (int)f, (int)f2, f, f2);
      GlStateManager.disableBlend();
   }

   public void drawContent(int n, int n2, float f) {
      this.scrollBar.updateAndDraw();
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
            float f5 = f4;
            float f6 = f3;
            ((AugustusGuiScreen)this).drawString(str, f6, f5, (byte)-1);
            String str3 = "§7" + serverQuest.getRemainingTimeText();
            f3 = f2 - 146.0F;
            f4 = (float)(n3 + 16);
            float f7 = f4;
            float f8 = f3;
            ((AugustusGuiScreen)this).drawString(str3, f8, f7, (byte)-1);
            String str5 = "§7" + (int)serverQuest.getProgressPercent() + "% finished";
            f3 = f2 - 146.0F;
            f4 = (float)(n3 + 28);
            float f9 = f4;
            float f10 = f3;
            ((AugustusGuiScreen)this).drawString(str5, f10, f9, (byte)-1);
            if (!serverQuest.isCompleted()) {
               String str7 = "§b" + serverQuest.getRewardExperience() + " XP";
               int n4 = this.fontRendererObj.getStringWidth(str7) * 2;
               f3 = (f2 + 146.0F - (float)n4) / 2.0F;
               f4 = (float)((n3 + 12) / 2);
               float f11 = 2.0F;
               float f12 = f4;
               float f13 = f3;
               ((AugustusGuiScreen)this).drawScaledString(str7, f13, f12, f11, (byte)-1);
            } else {
               GlStateManager.color(255.0F, 255.0F, 255.0F, 1.0F);
               int n5 = (int)(f2 + 115.0F);
               int n6 = n3 + 5;
               ResourceLocation augustustexture = GuiMultiplayer.AUGUSTUS_TEXTURE;
               float f14 = 30.0F;
               this.drawQuestIcon(n5, n6, 30.0F, f14, augustustexture);
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
      super.initContent();
      if (Augustus.getInstance().getQuestManager() != null) {
         this.scrollBar.setItemCount(Augustus.getInstance().getQuestManager().getQuests().size());
      }

      float f = (float)(this.width + 102) / 2.0F;
      int n = (int)(f + 152.0F);
      int n2 = (int)(f + 155.0F);
      int height2 = this.height;
      this.scrollBar.setBounds(n, (byte)52, n2, height2);
   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      this.scrollBar.handleMouseWheel();
   }

}
