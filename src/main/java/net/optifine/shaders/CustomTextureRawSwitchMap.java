package net.optifine.shaders;


import net.optifine.texture.TextureType;
public class CustomTextureRawSwitchMap {
   public static final int[] ORDINAL_MAP = new int[TextureType.values().length];

   static {
      try {
         ORDINAL_MAP[TextureType.TEXTURE_1D.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[TextureType.TEXTURE_2D.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         ORDINAL_MAP[TextureType.TEXTURE_3D.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         ORDINAL_MAP[TextureType.TEXTURE_RECTANGLE.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }
   }
}
