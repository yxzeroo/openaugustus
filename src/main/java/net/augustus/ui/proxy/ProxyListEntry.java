package net.augustus.ui.proxy;

import net.augustus.Augustus;
import net.augustus.render.RenderUtil;
import net.augustus.utils.animation.AnimationUtil;
import net.augustus.utils.animation.EasingType;
import net.augustus.utils.input.KeyboardInputUtil;
import net.augustus.utils.network.ProxyEntry;
import java.awt.Color;
import net.augustus.utils.TimeHelper;
import net.augustus.utils.interfaces.MC;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

public class ProxyListEntry implements MC {
   private boolean hovered;
   private final TimeHelper timeHelper = new TimeHelper();
   private boolean selected;
   private int renderY;
   private int renderHeight;
   private int renderWidth;
   private final ProxyEntry proxyEntry;
   private final AnimationUtil hoverAnimation = new AnimationUtil(0.0F, 0.0F, 0.0F, 0.0F);
   private int renderX;
   private static int[] controlFlowGuard;

   public boolean isHovered() {
      return this.hovered;
   }

   public void drawEntry(int n, int n2, int n3, int n4, int n5, int n6, boolean flag) {
      this.renderX = n3;
      this.renderY = n4;
      this.renderWidth = n5;
      getControlFlowGuard();
      this.renderHeight = n6;
      this.selected = flag;
      FontRenderer fontRendererObj = mc.fontRendererObj;
      float f = (float)n5 / 2.0F;
      float f2 = (float)n6 / 2.0F;
      float f3 = (float)n3 + f;
      float f4 = (float)n4 + f2;
      GL11.glPushMatrix();
      boolean isHovered = RenderUtil.isHovered((float)n3, (float)n4, (float)n5, (float)n6, n, n2);
      if (this.hovered != isHovered) {
         this.hoverAnimation.start(100, EasingType.easeInSine, isHovered ? 1 : 0, false);
      }

      String str;
      StringBuilder sb;
      exitBlock: {
         float f5 = (float)(1.0 - this.hoverAnimation.getEasedProgress() * 0.019999999552965164);
         GL11.glScaled((double)f5, (double)f5, 0.0);
         f3 /= f5;
         f4 /= f5;
         this.hovered = isHovered;
         double d = (double)(f3 - f);
         double d2 = (double)(f4 - f2);
         double d3 = (double)(f3 + f);
         double d4 = (double)(f4 + f2);
         int rgb = new Color(0, 0, 0, flag ? 200 : 100).getRGB();
         double d5 = d4;
         double d6 = d3;
         double d7 = d2;
         RenderUtil.drawRect(d, d7, d6, d5, rgb);
         GL11.glScaled((double)(1.0F / f5), (double)(1.0F / f5), 0.0);
         ProxyEntry proxy = Augustus.getInstance().getProxy();
         StringBuilder sb2 = new StringBuilder();
         short s = 923;
         sb = sb2.append("§7IP: ");
         if (proxy != null) {
            ProxyEntry proxyEntry2 = this.proxyEntry;
            if (proxy.matches(proxyEntry2)) {
               str = "§a";
               break exitBlock;
            }
         }

         str = this.isFavorite() ? "§6" : "§f";
      }

      fontRendererObj.drawString2(sb.append(str).append(this.proxyEntry.getHost()).toString(), (float)n3 + 5.5F, (float)n4 + 3.5F, Color.white.getRGB());
      StringBuilder sb3 = new StringBuilder();
      short s2 = 29509;
      StringBuilder sb4 = sb3.append("§7Port: ");
      if (this.isFavorite()) {
         str = "§6";
      } else {
         short s3 = 23372;
         str = "§f";
      }

      fontRendererObj.drawString2(
         sb4.append(str).append(this.proxyEntry.getPort()).toString(), (float)n3 + 5.5F, (float)n4 + 7.0F + (float)fontRendererObj.FONT_HEIGHT, Color.white.getRGB()
      );
      StringBuilder sb5 = new StringBuilder();
      short s4 = 1778;
      sb5 = sb5.append("§7Ping: ");
      String str2;
      if (this.isFavorite()) {
         str2 = "§6";
      } else {
         short s5 = 23372;
         str2 = "§f";
      }

      String str3 = sb5.append(str2).append(this.proxyEntry.getPing()).toString();
      fontRendererObj.drawString2(str3, (float)n3 + ((float)(n5 - fontRendererObj.getStringWidth(str3)) - 5.5F), (float)n4 + 3.5F, Color.white.getRGB());
      GL11.glPopMatrix();
   }

   public void setFavorite(boolean flag) {
      this.proxyEntry.setMarked(flag);
   }

   public TimeHelper getTimeHelper() {
      return this.timeHelper;
   }

   public ProxyListEntry(ProxyEntry proxyEntry2) {
      this.proxyEntry = proxyEntry2;
   }

   public int getRenderHeight() {
      return this.renderHeight;
   }

   public int getRenderY() {
      return this.renderY;
   }

   public boolean isFavorite() {
      return this.proxyEntry.isMarked();
   }

   public void mouseClicked(int n, int n2, int n3, ProxyManagerScreen proxyManagerScreen) {
      int[] controlFlowGuard2 = getControlFlowGuard();
      if (n3 == 0 && RenderUtil.isHovered((float)this.renderX, (float)this.renderY, (float)this.renderWidth, (float)this.renderHeight, n, n2)) {
         if (KeyboardInputUtil.isKeyDown(157) || KeyboardInputUtil.isKeyDown(29)) {
            boolean flag = !this.isFavorite();
            this.setFavorite(flag);
            return;
         }

         long l = 250L;
         if (!this.timeHelper.reached2(l)) {
            String str = this.proxyEntry.getHost()
               + ":"
               + this.proxyEntry.getPort()
               + (
                  !this.proxyEntry.getUsername().equals("")
                     ? ":" + this.proxyEntry.getUsername() + (!this.proxyEntry.getPassword().equals("") ? ":" + this.proxyEntry.getPassword() : "")
                     : ""
               );
            boolean flag2 = true;
            String str2 = str;
            proxyManagerScreen.connectToProxy(str2, flag2);
         }

         this.timeHelper.reset();
      }
   }

   public ProxyEntry getProxyEntry() {
      return this.proxyEntry;
   }

   public boolean isSelected() {
      return this.selected;
   }

   public int getRenderWidth() {
      return this.renderWidth;
   }

   public int getRenderX() {
      return this.renderX;
   }

   public AnimationUtil getHoverAnimation() {
      return this.hoverAnimation;
   }

   public static void setControlFlowGuard(int[] ints) {
      controlFlowGuard = ints;
   }

   public static int[] getControlFlowGuard() {
      return controlFlowGuard;
   }

   static {
      setControlFlowGuard((int[])null);
   }

}
