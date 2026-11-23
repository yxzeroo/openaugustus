package net.augustus.cosmetics.model;

import net.augustus.Augustus;
import net.augustus.cosmetics.CosmeticsHttpUtil;
import net.augustus.cosmetics.MoreCosmeticsCore;
import net.augustus.cosmetics.TrustedHostWhitelist;
import net.augustus.cosmetics.molang.MolangParser;
import net.augustus.settings.Setting;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.utils.file.ClientPaths;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import net.labymod.ModelConfig;
import org.apache.commons.io.FileUtils;

public abstract class CosmeticModelLoader {
   public HashMap modelsById;
   public HashMap modelsByName;
   private boolean loaded;
   private File cosmeticsDir;

   private void extractBundledCosmetics() {
      String str = Augustus.getInstance().getName().toLowerCase() + File.separator + "cosmetics";
      String str2 = "assets/augustus/cosmetics/";
      String str3 = str2 + "1.json";
      boolean flag = true;
      ClientFileUtil.extractResource(str3, str, flag);
      str3 = str2 + "2.json";
      boolean flag2 = true;
      ClientFileUtil.extractResource(str3, str, flag2);
      str3 = str2 + "3.json";
      boolean flag3 = true;
      ClientFileUtil.extractResource(str3, str, flag3);
      str3 = str2 + "4.json";
      boolean flag4 = true;
      ClientFileUtil.extractResource(str3, str, flag4);
      str3 = str2 + "5.json";
      boolean flag5 = true;
      ClientFileUtil.extractResource(str3, str, flag5);
      str3 = str2 + "6.json";
      boolean flag6 = true;
      ClientFileUtil.extractResource(str3, str, flag6);
      str3 = str2 + "7.json";
      boolean flag7 = true;
      ClientFileUtil.extractResource(str3, str, flag7);
      str3 = str2 + "8.json";
      boolean flag8 = true;
      ClientFileUtil.extractResource(str3, str, flag8);
      str3 = str2 + "10.json";
      boolean flag9 = true;
      ClientFileUtil.extractResource(str3, str, flag9);
      str3 = str2 + "11.json";
      boolean flag10 = true;
      ClientFileUtil.extractResource(str3, str, flag10);
      str3 = str2 + "12.json";
      boolean flag11 = true;
      ClientFileUtil.extractResource(str3, str, flag11);
      str3 = str2 + "13.json";
      boolean flag12 = true;
      ClientFileUtil.extractResource(str3, str, flag12);
      str3 = str2 + "14.json";
      boolean flag13 = true;
      ClientFileUtil.extractResource(str3, str, flag13);
      str3 = str2 + "16.json";
      boolean flag14 = true;
      ClientFileUtil.extractResource(str3, str, flag14);
      str3 = str2 + "17.json";
      boolean flag15 = true;
      ClientFileUtil.extractResource(str3, str, flag15);
      str3 = str2 + "18.json";
      boolean flag16 = true;
      ClientFileUtil.extractResource(str3, str, flag16);
      str3 = str2 + "19.json";
      boolean flag17 = true;
      ClientFileUtil.extractResource(str3, str, flag17);
      str3 = str2 + "20.json";
      boolean flag18 = true;
      ClientFileUtil.extractResource(str3, str, flag18);
      str3 = str2 + "21.json";
      boolean flag19 = true;
      ClientFileUtil.extractResource(str3, str, flag19);
      str3 = str2 + "22.json";
      boolean flag20 = true;
      ClientFileUtil.extractResource(str3, str, flag20);
      str3 = str2 + "23.json";
      boolean flag21 = true;
      ClientFileUtil.extractResource(str3, str, flag21);
      str3 = str2 + "24.json";
      boolean flag22 = true;
      ClientFileUtil.extractResource(str3, str, flag22);
   }

   public boolean isLoaded() {
      return this.loaded;
   }

   private void loadAllAsyncTask() {
      this.loadAllModels();
   }

   public HashMap getModelsByName() {
      return this.modelsByName;
   }

   public CosmeticModelLoader() {
       super();
      this.modelsById = new HashMap();
      this.modelsByName = new HashMap();
      this.cosmeticsDir = new File(ClientPaths.CLIENT_DIR, "cosmetics");
      this.cosmeticsDir.mkdirs();
      MoreCosmeticsCore.EXECUTOR.execute(() -> this.loadAllAsyncTask());
   }

   public void loadAllModels() {
      this.extractBundledCosmetics();

      for (File file : this.cosmeticsDir.listFiles()) {
         if (file.getName().toLowerCase().endsWith(".json")) {
            boolean flag = false;
            this.loadModelFile(file, flag);
         }
      }

      this.loaded = true;
      Setting.getControlFlowMarker();
   }

   private void readPartTransform(JsonObject json, AbstractCosmeticPart cosmeticPart) {
      if (json.has("pos")) {
         CosmeticBodyPart cosmeticBodyPart = CosmeticBodyPart.byId(json.get("pos").getAsInt());
         if (cosmeticBodyPart != null) {
            cosmeticPart.setBodyPart(cosmeticBodyPart);
         }
      }

      if (json.has("scale")) {
         float asFloat = json.get("scale").getAsFloat();
         cosmeticPart.setScale(asFloat);
      }

      if (json.has("x")) {
         float asFloat2 = json.get("x").getAsFloat();
         cosmeticPart.setOffsetX(asFloat2);
      }

      if (json.has("y")) {
         float asFloat3 = json.get("y").getAsFloat();
         cosmeticPart.setOffsetY(asFloat3);
      }

      if (json.has("z")) {
         float asFloat4 = json.get("z").getAsFloat();
         cosmeticPart.setOffsetZ(asFloat4);
      }

      if (json.has("yaw")) {
         float asFloat5 = json.get("yaw").getAsFloat();
         cosmeticPart.setYaw(asFloat5);
      }

      if (json.has("pitch")) {
         float asFloat6 = json.get("pitch").getAsFloat();
         cosmeticPart.setPitch(asFloat6);
      }

      if (json.has("roll")) {
         float asFloat7 = json.get("roll").getAsFloat();
         cosmeticPart.setRoll(asFloat7);
      }
   }

   public void loadModelFile(File file, boolean flag) {

      try (InputStreamReader inputStreamReader = new InputStreamReader(new BufferedInputStream(new FileInputStream(file)), Charset.forName("UTF-8"))) {
         JsonObject asJsonObject = MoreCosmeticsCore.JSON_PARSER.parse(inputStreamReader).getAsJsonObject();
         if (!asJsonObject.has("name")) {
            throw new IllegalArgumentException("Missing name");
         }

         if (!asJsonObject.has("model")) {
            throw new IllegalArgumentException("Missing model");
         }

         String asString = asJsonObject.get("name").getAsString();
         if (asJsonObject.has("minVersion")) {
            int asInt = asJsonObject.get("minVersion").getAsInt();
            if (asInt > 1200) {
               throw new IllegalStateException("Model requieres version " + asInt);
            }
         }

         int n = !flag && asJsonObject.has("id") ? asJsonObject.get("id").getAsInt() : asString.hashCode();
         int n2 = asJsonObject.has("category") ? asJsonObject.get("category").getAsInt() : 0;
         int n3 = asJsonObject.has("version") ? asJsonObject.get("version").getAsInt() : 0;
         String str = null;
         if (asJsonObject.has("texture")) {
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(asJsonObject.get("texture").getAsString()))) {
               str = asString.replace(" ", "").toLowerCase() + n;
               BufferedImage image = ImageIO.read(byteArrayInputStream);
               this.registerModelTexture(str, image);
            } catch (IOException e) {
               MoreCosmeticsCore.logInfo("Failed to load texture from " + asString);
            }
         }

         ModelConfig[] modelConfigs = null;
         if (asJsonObject.has("config")) {
            JsonArray asJsonArray = asJsonObject.get("config").getAsJsonArray();
            modelConfigs = new ModelConfig[asJsonArray.size()];

            for (int i = 0; i < asJsonArray.size(); i++) {
               modelConfigs[i] = (ModelConfig)MoreCosmeticsCore.GSON.fromJson(asJsonArray.get(i), ModelConfig.class);
               JsonObject asJsonObject2 = asJsonArray.get(i).getAsJsonObject();
               if (asJsonObject2.has("value")) {
                  if (modelConfigs[i].value.getClass().equals(Double.class)) {
                     String asString2 = asJsonObject2.get("value").getAsString();
                     if (!asString2.contains(".")) {
                        modelConfigs[i].value = asJsonObject2.get("value").getAsInt();
                     }
                  }

                  if (modelConfigs[i].value.getClass().equals(String.class)) {
                     String asString3 = asJsonObject2.get("value").getAsString();
                     if (asString3.startsWith("http")) {
                        if (!TrustedHostWhitelist.isTrustedHost(asString3)) {
                           String str2 = "Removed url: " + asString3;
                           MoreCosmeticsCore.logInfo(str2);
                           modelConfigs[i].value = "";
                        }
                     }
                  }

                  if (modelConfigs[i].src != null && !TrustedHostWhitelist.isTrustedHost(modelConfigs[i].src)) {
                     MoreCosmeticsCore.logInfo("Removed mask url: " + modelConfigs[i].src);
                     modelConfigs[i].src = null;
                  }
               }
            }
         }

         GeoModelParser modelparser = GeckoLibRegistry.MODEL_PARSER;
         String str3 = asJsonObject.get("model").getAsJsonObject().toString();
         GeoModel geoModel = modelparser.loadModel(asString, str3);
         CosmeticSubModel[] cosmeticSubModels = new CosmeticSubModel[0];
         if (asJsonObject.has("models")) {
            JsonArray asJsonArray2 = asJsonObject.get("models").getAsJsonArray();
            cosmeticSubModels = new CosmeticSubModel[asJsonArray2.size()];

            for (int j = 0; j < asJsonArray2.size(); j++) {
               JsonObject asJsonObject3 = asJsonArray2.get(j).getAsJsonObject();
               String str4 = asJsonObject3.has("name") ? asJsonObject3.get("name").getAsString() : "ModelComponent " + (j + 1);
               int n4 = asJsonObject3.has("color") ? 0xFF000000 | asJsonObject3.get("color").getAsInt() : -1;
               cosmeticSubModels[j] = new CosmeticSubModel(str4, n4, true);
               CosmeticSubModel cosmeticSubModel = cosmeticSubModels[j];
               this.readPartData(asJsonObject3, cosmeticSubModel);
            }
         }

         for (int k = 0; k < cosmeticSubModels.length; k++) {
            CosmeticSubModel cosmeticSubModel2 = cosmeticSubModels[k];
            List topLevelBones = geoModel.topLevelBones;
            this.bindSubModelToBones(topLevelBones, cosmeticSubModel2, k);
         }

         CosmeticModel cosmeticModel = new CosmeticModel(asString, n, n2, n3, flag, str, geoModel, cosmeticSubModels, modelConfigs, file);
         if (asJsonObject.has("animation")) {
            GeoAnimationLoader animationloader = GeckoLibRegistry.ANIMATION_LOADER;
            MolangParser molangparser = GeckoLibRegistry.MOLANG_PARSER;
            JsonObject asJsonObject4 = asJsonObject.get("animation").getAsJsonObject();
            MolangParser molangParser = molangparser;
            GeoAnimationFile geoAnimationFile = animationloader.loadAllAnimations(molangParser, asJsonObject4);
            cosmeticModel.setAnimationFile(geoAnimationFile);
         }

         if (asJsonObject.has("items")) {
            JsonArray asJsonArray3 = asJsonObject.get("items").getAsJsonArray();

            for (int m = 0; m < asJsonArray3.size(); m++) {
               JsonObject asJsonObject5 = asJsonArray3.get(m).getAsJsonObject();
               CosmeticItemModel cosmeticItemModel = new CosmeticItemModel(asJsonObject5.get("item").getAsInt());
               this.readPartData(asJsonObject5, cosmeticItemModel);
               cosmeticModel.getItemModels().add(cosmeticItemModel);
            }
         }

         if (asJsonObject.has("textures")) {
            JsonArray asJsonArray4 = asJsonObject.get("textures").getAsJsonArray();

            for (int idx = 0; idx < asJsonArray4.size(); idx++) {
               JsonObject asJsonObject6 = asJsonArray4.get(idx).getAsJsonObject();
               String asString4 = asJsonObject6.get("url").getAsString();
               if (asString4 != null && !asString4.isEmpty() && !TrustedHostWhitelist.isTrustedHost(asString4)) {
                  MoreCosmeticsCore.logInfo("Removed texture url: " + asString4);
                  asString4 = "";
               }

               CosmeticTextureModel cosmeticTextureModel = new CosmeticTextureModel(asString4, idx);
               if (asJsonObject6.has("opacity")) {
                  float asFloat = asJsonObject6.get("opacity").getAsFloat();
                  cosmeticTextureModel.setOpacity(asFloat);
               }

               if (asJsonObject6.has("color")) {
                  int asInt2 = asJsonObject6.get("color").getAsInt();
                  cosmeticTextureModel.setColor(asInt2);
               }

               if (asJsonObject6.has("bound")) {
                  int asInt3 = asJsonObject6.get("bound").getAsInt();
                  cosmeticTextureModel.setBoundSubModelIndex(asInt3);
                  if (cosmeticTextureModel.getBoundSubModelIndex() >= 0) {
                     cosmeticModel.getSubModels()[cosmeticTextureModel.getBoundSubModelIndex()].getBoundTextures().add(cosmeticTextureModel);
                  }
               }

               if (asJsonObject6.has("mask")) {
                  String asString5 = asJsonObject6.get("mask").getAsString();
                  Consumer consumer = (Consumer)null;
                  this.loadTextureMask(cosmeticTextureModel, asString, asString5, consumer);
               }

               this.readPartData(asJsonObject6, cosmeticTextureModel);
               cosmeticModel.getTextureModels().add(cosmeticTextureModel);
            }
         }

         if (asJsonObject.has("height")) {
            float asFloat2 = asJsonObject.get("height").getAsFloat();
            cosmeticModel.setHeight(asFloat2);
         }

         if (asJsonObject.has("resize")) {
            float asFloat3 = asJsonObject.get("resize").getAsFloat();
            cosmeticModel.setResizeFactor(asFloat3);
         }

         if (asJsonObject.has("previewScale")) {
            float asFloat4 = asJsonObject.get("previewScale").getAsFloat();
            cosmeticModel.setPreviewScale(asFloat4);
         }

         if (asJsonObject.has("previewY")) {
            float asFloat5 = asJsonObject.get("previewY").getAsFloat();
            cosmeticModel.setPreviewY(asFloat5);
         }

         if (asJsonObject.has("previewRot")) {
            float[] floats = (float[])MoreCosmeticsCore.GSON.fromJson(asJsonObject.get("previewRot"), float[].class);
            cosmeticModel.setPreviewRotation(floats);
         }

         if (asJsonObject.has("adjustment")) {
            float[] floats2 = (float[])MoreCosmeticsCore.GSON.fromJson(asJsonObject.get("adjustment"), float[].class);
            cosmeticModel.setAdjustment(floats2);
         }

         if (asJsonObject.has("sideTransform")) {
            float[] floats3 = (float[])MoreCosmeticsCore.GSON.fromJson(asJsonObject.get("sideTransform"), float[].class);
            cosmeticModel.setSideTransform(floats3);
         }

         this.readPartData(asJsonObject, cosmeticModel);
         this.modelsById.put(n, cosmeticModel);
         this.modelsByName.put(cosmeticModel.getName(), cosmeticModel);
      } catch (Exception e2) {
      }
   }

   private void readPartData(JsonObject json, AbstractCosmeticPart cosmeticPart) {
      this.readPartTransform(json, cosmeticPart);
      this.readPartEffects(json, cosmeticPart);
   }

   private void writePartData(JsonObject json, AbstractCosmeticPart cosmeticPart) {
      this.writePartTransform(json, cosmeticPart);
      this.writePartEffects(json, cosmeticPart);
   }

   public CosmeticModel getModelByName(String str) {
      return (CosmeticModel)this.modelsByName.get(str);
   }

   private void writePartEffects(JsonObject json, AnimatedCosmeticPart animatedCosmeticPart) {
      JsonArray array = new JsonArray();

      for (CosmeticEffect cosmeticEffect : (Iterable<CosmeticEffect>)(animatedCosmeticPart.getEffects().values())) {
         JsonObject json2 = new JsonObject();
         json2.addProperty("type", cosmeticEffect.getType().getId());
         if (cosmeticEffect.getAxis() != null) {
            json2.addProperty("axis", cosmeticEffect.getAxis().getId());
         }

         json2.addProperty("mult", cosmeticEffect.getMultiplier());
         array.add(json2);
      }

      json.add("animations", array);
   }

   public void loadTextureMask(CosmeticTextureModel cosmeticTextureModel, String str, String str2, Consumer consumer) {
      Optional optional = Optional.ofNullable(consumer);
      if (str2.startsWith("http")) {
         if (TrustedHostWhitelist.isTrustedHost(str2)) {
            MoreCosmeticsCore.EXECUTOR.execute(() -> {

               try {
                  Object[] array = new Object[0];
                  short s = 5000;

                  try (InputStream in = CosmeticsHttpUtil.openUrlStream(str2, s, array)) {
                     BufferedImage image = ImageIO.read(in);
                     cosmeticTextureModel.setMaskImage(image);
                     cosmeticTextureModel.setMaskUrl(str2);
                     optional.ifPresent(obj -> ((Consumer)obj).accept(true));
                  }
               } catch (IOException e) {
                  MoreCosmeticsCore.logInfo("Failed to load mask from " + str + ": " + e.getMessage());
                  optional.ifPresent(obj2 -> ((Consumer)obj2).accept(false));
               }
            });
         }
      } else {
         try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(str2))) {
            BufferedImage image2 = ImageIO.read(byteArrayInputStream);
            cosmeticTextureModel.setMaskImage(image2);
            optional.ifPresent(obj3 -> ((Consumer)obj3).accept(true));
         } catch (IOException e2) {
            MoreCosmeticsCore.logInfo("Failed to load mask from " + str + ": " + e2.getMessage());
            optional.ifPresent(obj4 -> ((Consumer)obj4).accept(false));
         }
      }
   }

   private void writePartTransform(JsonObject json, AbstractCosmeticPart cosmeticPart) {
      json.addProperty("pos", cosmeticPart.getBodyPart().getId());
      json.addProperty("scale", cosmeticPart.getScale());
      json.addProperty("x", cosmeticPart.getOffsetX());
      json.addProperty("y", cosmeticPart.getOffsetY());
      json.addProperty("z", cosmeticPart.getOffsetZ());
      json.addProperty("yaw", cosmeticPart.getYaw());
      json.addProperty("pitch", cosmeticPart.getPitch());
      json.addProperty("roll", cosmeticPart.getRoll());
   }

   public CosmeticModel getModelById(Integer num) {
      return (CosmeticModel)this.modelsById.get(num);
   }

   public void saveModelFile(CosmeticModel cosmeticModel) {

      try (BufferedReader reader = new BufferedReader(new FileReader(cosmeticModel.getSourceFile()))) {
         JsonObject asJsonObject = MoreCosmeticsCore.JSON_PARSER.parse(reader).getAsJsonObject();
         asJsonObject.addProperty("name", cosmeticModel.getName());
         asJsonObject.addProperty("category", cosmeticModel.getCategoryId());
         asJsonObject.addProperty("height", cosmeticModel.getHeight());
         asJsonObject.addProperty("previewY", cosmeticModel.getPreviewY());
         asJsonObject.addProperty("previewScale", cosmeticModel.getPreviewScale());
         if (cosmeticModel.getPreviewRotation() != null) {
            asJsonObject.add("previewRot", MoreCosmeticsCore.GSON.toJsonTree(cosmeticModel.getPreviewRotation()));
         }

         this.writePartData(asJsonObject, cosmeticModel);
         if (cosmeticModel.hasItemModels()) {
            JsonArray array = new JsonArray();

            for (CosmeticItemModel cosmeticItemModel : (Iterable<CosmeticItemModel>)(cosmeticModel.getItemModels())) {
               JsonObject json = new JsonObject();
               json.addProperty("item", cosmeticItemModel.getItemId());
               this.writePartData(json, cosmeticItemModel);
               array.add(json);
            }

            asJsonObject.add("items", array);
         }

         if (cosmeticModel.hasTextureModels()) {
            JsonArray array2 = new JsonArray();

            for (CosmeticTextureModel cosmeticTextureModel : (Iterable<CosmeticTextureModel>)(cosmeticModel.getTextureModels())) {
               JsonObject json2 = new JsonObject();
               json2.addProperty("url", cosmeticTextureModel.getUrl());
               json2.addProperty("opacity", cosmeticTextureModel.getOpacity());
               json2.addProperty("color", cosmeticTextureModel.getColor());
               json2.addProperty("bound", cosmeticTextureModel.getBoundSubModelIndex());
               if (cosmeticTextureModel.getMaskUrl() != null && cosmeticTextureModel.getMaskUrl().startsWith("http")) {
                  json2.addProperty("mask", cosmeticTextureModel.getMaskUrl());
               }

               this.writePartData(json2, cosmeticTextureModel);
               array2.add(json2);
            }

            asJsonObject.add("textures", array2);
         }

         if (cosmeticModel.hasSubModels()) {
            JsonArray asJsonArray = asJsonObject.get("models").getAsJsonArray();

            for (int i = 0; i < asJsonArray.size(); i++) {
               JsonObject asJsonObject2 = asJsonArray.get(i).getAsJsonObject();
               CosmeticSubModel cosmeticSubModel = cosmeticModel.getSubModels()[i];
               asJsonObject2.addProperty("visible", cosmeticSubModel.isVisible());
               this.writePartData(asJsonObject2, cosmeticSubModel);
            }
         }

         if (cosmeticModel.hasConfigs()) {
            asJsonObject.add("config", MoreCosmeticsCore.GSON.toJsonTree(cosmeticModel.getConfigs()));
         }

         FileUtils.writeStringToFile(cosmeticModel.getSourceFile(), asJsonObject.toString());
      } catch (IOException e) {
         MoreCosmeticsCore.logError(e);
      }
   }

   private void bindSubModelToBones(List list, CosmeticSubModel cosmeticSubModel, int n) {

      for (GeoBone geoBone : (Iterable<GeoBone>)(list)) {
         if (geoBone.getName().toLowerCase().startsWith(cosmeticSubModel.getName().toLowerCase())) {
            geoBone.subModelIndex = n;
         } else {
            List childBones = geoBone.childBones;
            this.bindSubModelToBones(childBones, cosmeticSubModel, n);
         }
      }
   }

   private void readPartEffects(JsonObject json, AnimatedCosmeticPart animatedCosmeticPart) {
      if (json.has("animations")) {
         for (JsonElement elem : json.get("animations").getAsJsonArray()) {
            JsonObject asJsonObject = elem.getAsJsonObject();
            CosmeticEffect cosmeticEffect = new CosmeticEffect(CosmeticEffectType.byId(asJsonObject.get("type").getAsInt()));
            if (asJsonObject.has("axis")) {
               CosmeticAxis cosmeticAxis = CosmeticAxis.byId(asJsonObject.get("axis").getAsInt());
               if (cosmeticAxis != null) {
                  cosmeticEffect.setAxis(cosmeticAxis);
               }
            }

            float asFloat = asJsonObject.get("mult").getAsFloat();
            cosmeticEffect.setMultiplier(asFloat);
            animatedCosmeticPart.addEffect(cosmeticEffect);
         }
      }
   }

   public HashMap getModelsById() {
      return this.modelsById;
   }

   public abstract void registerModelTexture(String str, BufferedImage image);

}
