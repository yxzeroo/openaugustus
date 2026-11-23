package net.augustus.cosmetics.render;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public interface CosmeticImageProcessor {
   long a = 121552110899642L;
   CosmeticImageProcessor IDENTITY = img -> img;
   CosmeticImageProcessor CLOAK_NORMALIZER = img -> normalizeCloakImage(
      (char)((int)(((a ^ 41190234252336L) ^ 99036597791849L) >>> 48)),
      (int)(((a ^ 41190234252336L) ^ 99036597791849L) << 16 >>> 32),
      (int)(((a ^ 41190234252336L) ^ 99036597791849L) << 48 >>> 48),
      img
   );

   BufferedImage processImage(BufferedImage image);

   static BufferedImage normalizeCloakImage(char ch, int n, int n2, BufferedImage image) {
      if (image.getWidth() / 2 == image.getHeight()) {
         return image;
      } else {
         int n3 = 64;
         int n4 = 32;
         int width = image.getWidth();

         for (int i = image.getHeight(); n3 < width || n4 < i; n4 *= 2) {
            n3 *= 2;
         }

         BufferedImage image2 = new BufferedImage(n3, n4, 2);
         Graphics graphics = image2.getGraphics();
         graphics.drawImage(image, 0, 0, (ImageObserver)null);
         graphics.dispose();
         return image2;
      }
   }
}
