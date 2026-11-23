package net.augustus.render;

import net.minecraft.client.renderer.GlStateManager;

public class TexGenSwitchMap {
   public static final TexGenSwitchMap INSTANCE = new TexGenSwitchMap();
   public final int[] texGenOrdinals = new int[GlStateManager.TexGen.values().length];

   public TexGenSwitchMap() {
      try {
         this.texGenOrdinals[GlStateManager.TexGen.S.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         this.texGenOrdinals[GlStateManager.TexGen.T.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         this.texGenOrdinals[GlStateManager.TexGen.R.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         this.texGenOrdinals[GlStateManager.TexGen.Q.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }
   }
}
