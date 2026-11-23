package net.augustus.cosmetics.render;

import net.augustus.cosmetics.CosmeticsHttpUtil;
import net.augustus.cosmetics.MoreCosmeticsCore;
import net.augustus.cosmetics.TrustedHostWhitelist;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;

public class CosmeticTextureManager {
   private static CosmeticTextureManager cosmeticTextureManager;
   private HashMap resourceLocations;
   private HashMap staticTextures;
   private String namespace;
   private HashMap animatedTextures;

   private void downloadGifTask(String str, String str2, BufferedImage image, CosmeticImageProcessor cosmeticImageProcessor) {

      try {
         Object[] array = new Object[0];
         short s = 5000;
         InputStream in = CosmeticsHttpUtil.openUrlStream(str, s, array);
         CosmeticGifFrame[] cosmeticGifFrames = CosmeticGifDecoder.decodeGifFrames(in);
         CosmeticGifFrame[] cosmeticGifFrames2 = cosmeticGifFrames;
         this.registerGifFrames(str2, cosmeticGifFrames2, image, cosmeticImageProcessor);
      } catch (Exception e) {
         MoreCosmeticsCore.logInfo("Failed to load " + str + ": " + e.toString());
      }
   }

   private void readLocalImageTask(String str, ResourceLocation location, CosmeticImageProcessor cosmeticImageProcessor, File file, BufferedImage image, String str2) {

      try {
         BufferedImage image2 = ImageIO.read(file);
         BufferedImage image3 = cosmeticImageProcessor.processImage(image2);
         this.registerStaticTexture(str, location, image3, image);
      } catch (IOException e) {
         MoreCosmeticsCore.logInfo("Failed to load " + str2 + ": " + e.toString());
      }
   }

   public ResourceLocation getTextureLocation(String str, String str2) {
      BufferedImage image = (BufferedImage)null;
      CosmeticBufferedTexture orLoadTexture = this.getOrLoadTexture(str, str2, image);
      return orLoadTexture != null ? orLoadTexture.getTextureLocation() : null;
   }

   public static CosmeticTextureManager getInstance() {
      return cosmeticTextureManager != null ? cosmeticTextureManager : (cosmeticTextureManager = new CosmeticTextureManager("morecosmetics"));
   }

   public void removeTexture(String str) {
      this.staticTextures.remove(str);
      this.animatedTextures.remove(str);
      this.resourceLocations.remove(str);
   }

   private void loadLocalImage(String str, String str2, BufferedImage image, CosmeticImageProcessor cosmeticImageProcessor) {
      File file = new File(str2);
      if (!file.exists()) {
         MoreCosmeticsCore.logInfo("Failed to load file: " + str2);
      } else {
         ResourceLocation location = new ResourceLocation(this.namespace + "/" + str.hashCode());
         MoreCosmeticsCore.EXECUTOR.execute(() -> this.readLocalImageTask(str, location, cosmeticImageProcessor, file, image, str2));
      }
   }

   public AnimatedCosmeticTexture getAnimatedTexture(String str) {
      return (AnimatedCosmeticTexture)this.animatedTextures.get(str);
   }

   private void loadLocalGif(String str, String str2, BufferedImage image, CosmeticImageProcessor cosmeticImageProcessor) {
      File file = new File(str2);
      if (!file.exists()) {
         MoreCosmeticsCore.logInfo("File not found: " + str2);
      } else {
         MoreCosmeticsCore.EXECUTOR.execute(() -> {

            try {
               CosmeticGifFrame[] cosmeticGifFrames = CosmeticGifDecoder.decodeGifFrames(FileUtils.openInputStream(file));
               CosmeticGifFrame[] cosmeticGifFrames2 = cosmeticGifFrames;
               this.registerGifFrames(str, cosmeticGifFrames2, image, cosmeticImageProcessor);
            } catch (IOException e) {
               MoreCosmeticsCore.logInfo("Failed to load " + str2 + ": " + e.toString());
            }
         });
      }
   }

   public CosmeticTextureManager(String str) {
      this.staticTextures = new HashMap();
      this.animatedTextures = new HashMap();
      this.resourceLocations = new HashMap();
      this.namespace = str;
   }

   private void downloadImageTask(String str, String str2, ResourceLocation location, CosmeticImageProcessor cosmeticImageProcessor, BufferedImage image) {

      try {
         Object[] array = new Object[0];
         short s = 5000;
         InputStream in = CosmeticsHttpUtil.openUrlStream(str, s, array);
         BufferedImage image2 = ImageIO.read(in);
         BufferedImage image3 = cosmeticImageProcessor.processImage(image2);
         BufferedImage image4 = TrustedHostWhitelist.isOfficialHost(str) ? null : image;
         BufferedImage image5 = image3;
         this.registerStaticTexture(str2, location, image5, image4);
      } catch (Exception e) {
         MoreCosmeticsCore.logInfo("Failed to load " + str + ": " + e.toString());
      }
   }

   public CosmeticBufferedTexture getOrLoadTexture(String str, String str2, BufferedImage image) {
      CosmeticImageProcessor identity = CosmeticImageProcessor.IDENTITY;
      return this.getOrLoadTexture(str, str2, image, identity);
   }

   public CosmeticBufferedTexture getOrLoadTexture(String str, String str2, BufferedImage image, CosmeticImageProcessor cosmeticImageProcessor) {
      if (this.animatedTextures.containsKey(str)) {
         AnimatedCosmeticTexture animatedCosmeticTexture = (AnimatedCosmeticTexture)this.animatedTextures.get(str);
         return animatedCosmeticTexture.getCurrentFrame();
      } else if (this.staticTextures.containsKey(str)) {
         return (CosmeticBufferedTexture)this.staticTextures.get(str);
      } else {
         this.staticTextures.put(str, null);
         String str3 = str2.toLowerCase();
         boolean flag = str3.startsWith("http");
         boolean flag2 = str3.contains(".jpeg") || str3.contains(".jpg");
         if (!str3.contains(".png") && !flag2) {
            if (str3.contains(".gif")) {
               if (flag) {
                  this.loadRemoteGif(str, str2, image, cosmeticImageProcessor);
               } else {
                  this.loadLocalGif(str, str2, image, cosmeticImageProcessor);
               }
            } else if (flag) {
               this.loadRemoteAutoDetect(str, str2, image, cosmeticImageProcessor);
            }
         } else if (flag) {
            this.loadRemoteImage(str, str2, image, cosmeticImageProcessor);
         } else {
            this.loadLocalImage(str, str2, image, cosmeticImageProcessor);
         }

         return null;
      }
   }

   private void registerGifFrames(String str, CosmeticGifFrame[] cosmeticGifFrames, BufferedImage image, CosmeticImageProcessor cosmeticImageProcessor) {
      int length = cosmeticGifFrames.length;
      int[] ints = new int[length];
      CosmeticBufferedTexture[] cosmeticBufferedTextures = new CosmeticBufferedTexture[length];
      CosmeticTextureAlphaMask cosmeticTextureAlphaMask = null;
      String str2 = this.namespace + "/gif/" + str.hashCode() + "_";

      for (int i = 0; i < length; i++) {
         CosmeticGifFrame cosmeticGifFrame = cosmeticGifFrames[i];
         BufferedImage image2 = cosmeticGifFrame.getImage();
         BufferedImage image3 = cosmeticImageProcessor.processImage(image2);
         if (i == 0 && image != null) {
            cosmeticTextureAlphaMask = new CosmeticTextureAlphaMask(image, image3.getWidth(), image3.getHeight());
         }

         int delay = cosmeticGifFrame.getDelay();
         ints[i] = delay == 0 ? 50 : delay * 10;
         ResourceLocation location = new ResourceLocation(str2 + i);
         image3 = cosmeticTextureAlphaMask == null ? image3 : cosmeticTextureAlphaMask.applyMask(image3);
         CosmeticBufferedTexture cosmeticBufferedTexture = cosmeticBufferedTextures[i] = new CosmeticBufferedTexture(location, image3);
         if (image != null) {
            float f = (float)image.getWidth() / (float)image.getHeight();
            cosmeticBufferedTexture.setAspectRatio(f);
         }

         Minecraft.getMinecraft().getTextureManager().loadTexture(location, cosmeticBufferedTexture);
      }

      this.animatedTextures.put(str, new AnimatedCosmeticTexture(cosmeticBufferedTextures, ints));
   }

   private void loadRemoteAutoDetect(String str, String str2, BufferedImage image, CosmeticImageProcessor cosmeticImageProcessor) {
      ResourceLocation location = new ResourceLocation(this.namespace + "/" + str.hashCode());
      MoreCosmeticsCore.EXECUTOR.execute(() -> {

         try {
            String[] strings = new String[1];
            Object[] array = (Object[])null;
            short s = 5000;
            InputStream in = CosmeticsHttpUtil.openUrlStream(str2, s, strings, array);
            if (strings[0].contains("jpeg") || strings[0].contains("png")) {
               BufferedImage image2 = ImageIO.read(in);
               BufferedImage image3 = cosmeticImageProcessor.processImage(image2);
               this.registerStaticTexture(str, location, image3, image);
            } else if (strings[0].contains("gif")) {
               CosmeticGifFrame[] cosmeticGifFrames = CosmeticGifDecoder.decodeGifFrames(in);
               CosmeticGifFrame[] cosmeticGifFrames2 = cosmeticGifFrames;
               this.registerGifFrames(str, cosmeticGifFrames2, image, cosmeticImageProcessor);
            }
         } catch (Exception e) {
            MoreCosmeticsCore.logInfo("Failed to load " + str2 + ": " + e.toString());
         }
      });
   }

   public ResourceLocation getOrCreateResourceLocation(String str, int n, char ch, short s, String str2) {
      Object obj = null;
      ResourceLocation location = (ResourceLocation)this.resourceLocations.get(str);
      ResourceLocation location2 = location;
      if (obj != null) {
         if (location == null) {
            this.resourceLocations.put(str, location = new ResourceLocation(str2));
         }

         location2 = location;
      }

      return location2;
   }

   private void loadRemoteImage(String str, String str2, BufferedImage image, CosmeticImageProcessor cosmeticImageProcessor) {
      ResourceLocation location = new ResourceLocation(this.namespace + "/" + str.hashCode());
      MoreCosmeticsCore.EXECUTOR.execute(() -> this.downloadImageTask(str2, str, location, cosmeticImageProcessor, image));
   }

   private void loadRemoteGif(String str, String str2, BufferedImage image, CosmeticImageProcessor cosmeticImageProcessor) {
      MoreCosmeticsCore.EXECUTOR.execute(() -> this.downloadGifTask(str2, str, image, cosmeticImageProcessor));
   }

   public void clearAll() {
      this.staticTextures.clear();
      this.animatedTextures.clear();
      this.resourceLocations.clear();
   }

   private void registerStaticTexture(String str, ResourceLocation location, BufferedImage image, BufferedImage image2) {
      if (image2 != null) {
         CosmeticTextureAlphaMask cosmeticTextureAlphaMask = new CosmeticTextureAlphaMask(image2, image.getWidth(), image.getHeight(), true);
         image = cosmeticTextureAlphaMask.applyMask(image);
      }

      CosmeticBufferedTexture cosmeticBufferedTexture = new CosmeticBufferedTexture(location, image);
      if (image2 != null) {
         float f = (float)image2.getWidth() / (float)image2.getHeight();
         cosmeticBufferedTexture.setAspectRatio(f);
      }

      this.staticTextures.put(str, cosmeticBufferedTexture);
      Minecraft.getMinecraft().getTextureManager().loadTexture(location, cosmeticBufferedTexture);
   }

}
