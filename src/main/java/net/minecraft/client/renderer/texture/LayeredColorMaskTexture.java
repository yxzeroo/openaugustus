package net.minecraft.client.renderer.texture;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.optifine.config.Config;
import net.optifine.shaders.ShadersTex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LayeredColorMaskTexture extends AbstractTexture {
   private static final Logger LOGGER = LogManager.getLogger();
   private final ResourceLocation textureLocation;
   private final List listTextures;
   private final List listDyeColors;

   public LayeredColorMaskTexture(ResourceLocation location, List list, List list2) {
      this.textureLocation = location;
      this.listTextures = list;
      this.listDyeColors = list2;
   }

   @Override
   public void loadTexture(IResourceManager resourceManager) throws IOException {
      this.deleteGlTexture();

      BufferedImage image;
      try {
         BufferedImage image2 = TextureUtil.readBufferedImage(resourceManager.getResource(this.textureLocation).getInputStream());
         int type = image2.getType();
         if (type == 0) {
            type = 6;
         }

         image = new BufferedImage(image2.getWidth(), image2.getHeight(), type);
         Graphics graphics = image.getGraphics();
         graphics.drawImage(image2, 0, 0, (ImageObserver)null);

         for (int i = 0; i < 17 && i < this.listTextures.size() && i < this.listDyeColors.size(); i++) {
            String str = (String)this.listTextures.get(i);
            MapColor mapColor = ((EnumDyeColor)this.listDyeColors.get(i)).getMapColor();
            if (str != null) {
               InputStream inputStream = resourceManager.getResource(new ResourceLocation(str)).getInputStream();
               BufferedImage image3 = TextureUtil.readBufferedImage(inputStream);
               if (image3.getWidth() == image.getWidth() && image3.getHeight() == image.getHeight() && image3.getType() == 6) {
                  for (int j = 0; j < image3.getHeight(); j++) {
                     for (int k = 0; k < image3.getWidth(); k++) {
                        int rgb = image3.getRGB(k, j);
                        if ((rgb & 0xFF000000) != 0) {
                           int n = (rgb & 0xFF0000) << 8 & 0xFF000000;
                           int rgb2 = image2.getRGB(k, j);
                           int n2 = MathHelper.multiplyColor(rgb2, mapColor.colorValue) & 16777215;
                           image3.setRGB(k, j, n | n2);
                        }
                     }
                  }

                  image.getGraphics().drawImage(image3, 0, 0, (ImageObserver)null);
               }
            }
         }
      } catch (IOException e) {
         LOGGER.error("Couldn't load layered image", e);
         return;
      }

      if (Config.isShaderPackLoaded()) {
         ShadersTex.loadSimpleTexture(this.getGlTextureId(), image, false, false, resourceManager, this.textureLocation, this.getMultiTexID());
      } else {
         TextureUtil.uploadTextureImage(this.getGlTextureId(), image);
      }
   }
}
