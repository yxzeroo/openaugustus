package net.augustus.ui.menu;

import net.augustus.Augustus;
import net.augustus.ClientManagers;
import net.augustus.changelog.ChangelogEntry;
import net.augustus.render.RenderUtil;
import net.augustus.ui.AugustusGuiScreen;
import net.augustus.ui.widgets.ScrollBar;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class ChangelogMenuTab extends ClientMenuTab {
   private List wrappedSections;
   private final ScrollBar scrollBar;

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      double d = (double)mouseX;
      double d2 = (double)mouseY;
      double d3 = d2;
      double d4 = d;
      this.scrollBar.handleMouseClick(d4, d3);
   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      this.scrollBar.handleMouseWheel();
   }

   private List getWrappedSections() {
      return this.wrappedSections;
   }

   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
      super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      double d = (double)mouseY;
      this.scrollBar.handleMouseDrag(d);
   }

   public void initContent() {
      super.initContent();
      CreditsSection.getControlFlowGuard();
      ChangeLogLoader changeLogLoader = Augustus.getInstance().getChangeLogLoader();
      double d = 296.0;
      this.wrappedSections = changeLogLoader.getWrappedSections(d);
      int n = 0;
      float f = (float)(this.width + 102) / 2.0F;

      for (ChangeLogSection changeLogSection : (Iterable<ChangeLogSection>)(this.getWrappedSections())) {
         n += changeLogSection.calculateHeight();
      }

      int n2 = (int)((double)n / this.scrollBar.itemHeight);
      this.scrollBar.setItemCount(n2);
      int n3 = (int)(f + 152.0F);
      int n4 = (int)(f + 155.0F);
      int height2 = this.height;
      int n5 = n4;
      byte b = 52;
      int n6 = n3;
      this.scrollBar.setBounds(n6, b, n5, height2);
   }

   public ChangelogMenuTab() {
       super("Changelog");
      this.scrollBar = new ScrollBar(Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 1);
      this.wrappedSections = new ArrayList();
   }

   public void drawContent(int n, int n2, float f) {
      super.drawContent(n, n2, f);
      CreditsSection.getControlFlowGuard();
      this.scrollBar.updateAndDraw();
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, 52.0F, 0.0F);
      int n3 = 0;

      for (ChangeLogSection changeLogSection : (Iterable<ChangeLogSection>)(this.getWrappedSections())) {
         float f2 = (float)(this.width + 102) / 2.0F;
         float f3 = (float)changeLogSection.calculateHeight();
         float f4 = (float)((double)((float)n3) + this.scrollBar.getClampedScrollOffset());
         if (ClientManagers.moduleManager.augustusServer.roundedButtons.getValue()) {
            RenderUtil.drawRoundedRect(
               f2 - 150.0F,
               f4,
               f2 + 150.0F,
               f4 + f3 - 1.0F,
               MathHelper.clamp_float((float)ClientManagers.moduleManager.augustusServer.roundFactor.getValue(), 0.0F, 6.0F),
               1,
               -2147483648
            );
         } else {
            RenderUtil.drawRect2(f2 - 150.0F, f4, f2 + 150.0F, f4 + f3 - 1.0F, -2147483648);
         }

         String versionTitle = changeLogSection.getVersionTitle();
         float f5 = f2 - 148.0F;
         float f6 = f4 + 2.0F;
         float f7 = f5;
         String str = versionTitle;
         ((AugustusGuiScreen)this).drawString(str, f7, f6);
         int n4 = 0;

         for (ChangelogEntry changelogEntry : (Iterable<ChangelogEntry>)(changeLogSection.getEntries())) {
            for (int i = 0; i < changelogEntry.getLines().length; i++) {
               String str2 = changelogEntry.getLines()[i];
               float f8 = (float)((double)f4 + this.scrollBar.itemHeight + 6.0 + (double)n4 * this.scrollBar.itemHeight);
               float f9 = f2 - 148.0F + (i > 0 ? 6.5F : 0.0F);
               float f10 = f8 + 2.0F;
               int rgb = changelogEntry.getType().getColor().getRGB();
               float f11 = f10;
               float f12 = f9;
               ((AugustusGuiScreen)this).drawString(str2, f12, f11, rgb);
               n4++;
            }
         }

         n3 = (int)((float)n3 + f3);
      }

      GL11.glPopMatrix();
   }

}
