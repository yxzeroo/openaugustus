package net.augustus.utils.render;

import net.minecraft.client.renderer.GlStateManager;
import net.optifine.config.Config;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL11;

public class GlStateSaver {
   private boolean savedRescaleNormal;
   private boolean savedBlend;
   private boolean savedFog;
   private boolean savedLineSmooth;
   private boolean savedDepth;
   private boolean savedColorLogic;
   private boolean savedNormalize;
   private boolean savedLighting;
   private int savedLightMask;
   private boolean savedAlpha;
   private boolean savedTexture2D;
   private boolean savedColorMaterial;
   private boolean savedCull;
   private boolean savedPolygonOffset;

   public void setCull(boolean flag) {
      this.savedCull = GlStateManager.isCullEnabled();
      if (flag) {
         GlStateManager.enableCull();
      } else {
         GlStateManager.disableCull();
      }
   }

   public void setDepth(boolean flag) {
      this.savedDepth = GlStateManager.isDepthEnabled();
      if (flag) {
         GlStateManager.enableDepth();
      } else {
         GlStateManager.disableDepth();
      }
   }

   public void restoreBlend() {
      if (this.savedBlend) {
         GlStateManager.enableBlend();
      } else {
         GlStateManager.disableBlend();
      }
   }

   public void restorePolygonOffset(long l) {
      if (this.savedPolygonOffset) {
         GlStateManager.enablePolygonOffset();
      } else {
         GlStateManager.disablePolygonOffset();
      }
   }

   public void restoreTexture2D() {
      if (this.savedTexture2D) {
         GlStateManager.enableTexture2D();
         if (Config.isShaderPackLoaded()) {
            Shaders.enableTexture2D();
         }
      } else {
         GlStateManager.disableTexture2D();
         if (Config.isShaderPackLoaded()) {
            Shaders.disableTexture2D();
         }
      }
   }

   public void restoreAlpha() {
      if (this.savedAlpha) {
         GlStateManager.enableAlpha();
      } else {
         GlStateManager.disableAlpha();
      }
   }

   public void setLighting(boolean flag) {
      this.savedLighting = GlStateManager.isLightingEnabled();
      if (flag) {
         GlStateManager.enableLighting();
      } else {
         GlStateManager.disableLighting();
      }
   }

   public void restoreCull() {
      if (this.savedCull) {
         GlStateManager.enableCull();
      } else {
         GlStateManager.disableCull();
      }
   }

   public void setAlpha(boolean flag) {
      this.savedAlpha = GlStateManager.isAlphaEnabled();
      if (flag) {
         GlStateManager.enableAlpha();
      } else {
         GlStateManager.disableAlpha();
      }
   }

   public void setTexture2D(boolean flag) {
      this.savedTexture2D = GlStateManager.isTexture2DEnabled();
      if (flag) {
         GlStateManager.enableTexture2D();
         if (Config.isShaderPackLoaded()) {
            Shaders.enableTexture2D();
         }
      } else {
         GlStateManager.disableTexture2D();
         if (Config.isShaderPackLoaded()) {
            Shaders.disableTexture2D();
         }
      }
   }

   public void restoreLighting() {
      if (this.savedLighting) {
         GlStateManager.enableLighting();
      } else {
         GlStateManager.disableLighting();
      }
   }

   private GlStateSaver() {
   }

   public void restoreDepth() {
      if (this.savedDepth) {
         GlStateManager.enableDepth();
      } else {
         GlStateManager.disableDepth();
      }
   }

   public void setFog(boolean flag, long l) {
      this.savedFog = GlStateManager.isFogEnabled();
      if (flag) {
         GlStateManager.enableFog();
      } else {
         GlStateManager.disableFog();
      }
   }

   public void restoreFog(int n, short s, char ch) {
      if (this.savedFog) {
         GlStateManager.enableFog();
      } else {
         GlStateManager.disableFog();
      }
   }

   public void restoreNormalize(char ch, short s, int n) {
      if (this.savedNormalize) {
         GlStateManager.enableNormalize();
      } else {
         GlStateManager.disableNormalize();
      }
   }

   public void setPolygonOffset(long l, boolean flag) {
      this.savedPolygonOffset = GlStateManager.isPolygonOffsetEnabled();
      if (flag) {
         GlStateManager.enablePolygonOffset();
      } else {
         GlStateManager.disablePolygonOffset();
      }
   }

   public void restoreColorMaterial(long l) {
      if (this.savedColorMaterial) {
         GlStateManager.enableColorMaterial();
      } else {
         GlStateManager.disableColorMaterial();
      }
   }

   public void restoreLight(int n, short s, short s2, int n2) {
      boolean flag = (this.savedLightMask & 1 << n2) != 0;
      if (flag) {
         GlStateManager.enableLight(n2);
      } else {
         GlStateManager.disableLight(n2);
      }
   }

   public void setNormalize(long l, boolean flag) {
      this.savedNormalize = GlStateManager.isNormalizeEnabled();
      if (flag) {
         GlStateManager.enableNormalize();
      } else {
         GlStateManager.disableNormalize();
      }
   }

   public void setColorLogic(boolean flag, long l) {
      this.savedColorLogic = GlStateManager.isColorLogicEnabled();
      if (flag) {
         GlStateManager.enableColorLogic();
      } else {
         GlStateManager.disableColorLogic();
      }
   }

   public void restoreColorLogic(long l) {
      if (this.savedColorLogic) {
         GlStateManager.enableColorLogic();
      } else {
         GlStateManager.disableColorLogic();
      }
   }

   public void restoreLineSmooth() {
      if (this.savedLineSmooth) {
         GL11.glEnable(2848);
      } else {
         GL11.glDisable(2848);
      }
   }

   public void setBlend(boolean flag) {
      this.savedBlend = GlStateManager.isBlendEnabled();
      if (flag) {
         GlStateManager.enableBlend();
      } else {
         GlStateManager.disableBlend();
      }
   }

   public void setLight(long l, int n, short s, boolean flag) {
      boolean isLightEnabled = GlStateManager.isLightEnabled(n);
      if (isLightEnabled) {
         this.savedLightMask |= 1 << n;
      } else {
         this.savedLightMask &= ~(1 << n);
      }

      if (flag) {
         GlStateManager.enableLight(n);
      } else {
         GlStateManager.disableLight(n);
      }
   }

   public void setRescaleNormal(boolean flag, long l) {
      this.savedRescaleNormal = GlStateManager.isRescaleNormalEnabled();
      if (flag) {
         GlStateManager.enableRescaleNormal();
      } else {
         GlStateManager.disableRescaleNormal();
      }
   }

   public void restoreRescaleNormal(long l) {
      if (this.savedRescaleNormal) {
         GlStateManager.enableRescaleNormal();
      } else {
         GlStateManager.disableRescaleNormal();
      }
   }

   public void setColorMaterial(long l, boolean flag) {
      this.savedColorMaterial = GlStateManager.isColorMaterialEnabled();
      if (flag) {
         GlStateManager.enableColorMaterial();
      } else {
         GlStateManager.disableColorMaterial();
      }
   }

   public static GlStateSaver create() {
      return new GlStateSaver();
   }

   public void setLineSmooth(boolean flag) {
      this.savedLineSmooth = GL11.glIsEnabled(2848);
      if (flag) {
         GL11.glEnable(2848);
      } else {
         GL11.glDisable(2848);
      }
   }




}
