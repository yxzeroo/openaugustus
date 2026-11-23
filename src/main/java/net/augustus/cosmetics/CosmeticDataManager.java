package net.augustus.cosmetics;

import net.augustus.ClientManagers;
import net.augustus.cosmetics.model.CosmeticModel;
import net.augustus.settings.ColorSetting;
import net.augustus.utils.ChatUtil;
import java.util.HashMap;
import java.util.UUID;
import net.labymod.ConfigAccessor;
import net.labymod.ModelConfig;
import net.labymod.ModelData;
import net.labymod.SettingOverlay;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;

public class CosmeticDataManager implements CosmeticTickListener {
   private HashMap modelDataCache = new HashMap();
   private static long lastHintTimestamp;
   private static int[] flowState;

   private SettingOverlay[] createSettingOverlays(int n) {
      SettingOverlay[] settingOverlays = new SettingOverlay[n];
      getFlowState();

      for (int i = 0; i < settingOverlays.length; i++) {
         settingOverlays[i] = new SettingOverlay();
      }

      return settingOverlays;
   }

   @Override
   public void onTick(int n) {
   }

   public CosmeticDataManager(UUID uuid) {
   }

   public ModelData getOrCreateModelData(CosmeticModel cosmeticModel, boolean flag) {
      return (ModelData)(this.modelDataCache.getOrDefault(cosmeticModel, this.modelDataCache.computeIfAbsent(cosmeticModel, obj -> this.createModelDataCallback(cosmeticModel, flag, (CosmeticModel)obj))));
   }

   private ModelData buildModelData(CosmeticModel cosmeticModel, boolean flag) {
      int[] flowState2 = getFlowState();
      ModelData modelData = null;
      if (flag) {
         int id = cosmeticModel.getId();
         modelData = this.loadSavedModelData(id);
         if (modelData != null && cosmeticModel.getVersion() == modelData.getVersion()) {
            return modelData;
         }
      }

      modelData = new ModelData();
      modelData.setVersion(cosmeticModel.getVersion());
      int length = cosmeticModel.getSubModels().length;
      modelData.setSubModels(this.createSettingOverlays(length));
      if (cosmeticModel.hasTextureModels()) {
         int size = cosmeticModel.getTextureModels().size();
         modelData.setTextureModels(this.createSettingOverlays(size));
      }

      if (cosmeticModel.hasItemModels()) {
         int size2 = cosmeticModel.getItemModels().size();
         modelData.setItemModels(this.createSettingOverlays(size2));
      }

      if (cosmeticModel.hasConfigs()) {
         for (ModelConfig modelConfig : cosmeticModel.getConfigs()) {
            Object value = modelConfig.value;
            if (value != null) {
               if (modelConfig.mult != 1.0F && value instanceof Number) {
                  value = ((Number)value).floatValue() / modelConfig.mult;
               }

               ConfigAccessor.set(modelConfig, modelData, value);
            }
         }
      }

      return modelData;
   }

   public void applyCosmeticToPlayer(boolean flag, CosmeticModel cosmeticModel, AbstractClientPlayer clientPlayer, float f, float f2, ColorSetting[] colorSettings) {
      int[] flowState2 = getFlowState();
      ModelData modelData = (ModelData)clientPlayer.getCosmetics().get(cosmeticModel.getId());
      if (flag
         && !(clientPlayer instanceof EntityPlayerSP)
         && (clientPlayer.getCosmetics().size() > 3 || (double)f > 1.5)
         && System.currentTimeMillis() - lastHintTimestamp > 108000000L
         && !ClientManagers.moduleManager.cosmetics.hideCosmeticsOfOtherUsers.getValue()
         && ClientManagers.moduleManager.cosmetics.hintMessageThatRenderingOfCosmeticsCanBeDisabled.getValue()) {
         ChatUtil.sendChat3("Hint: You can disable rendering of cosmetics. Render -> Cosmetics -> Hide cosmetics of other users");
         lastHintTimestamp = System.currentTimeMillis();
      }

      if (modelData != null) {
         if (modelData.isActive()) {
            for (int i = 0; i < modelData.model.length; i++) {
               SettingOverlay settingOverlay = modelData.model[i];
               settingOverlay.scale = f;
               modelData.scale = f;
               settingOverlay.y = f2;
               modelData.y = f2;
               ColorSetting colorSetting = colorSettings[i];
               byte b = 0;
               settingOverlay.color = colorSetting.getColorInt(b);
            }
         }

         if (modelData.isActive() != flag) {
            modelData.setActive(flag);
         }
      } else {
         boolean flag2 = true;
         modelData = this.getOrCreateModelData(cosmeticModel, flag2);
         modelData.setActive(flag);
         clientPlayer.getCosmetics().put(cosmeticModel.getId(), modelData.clone_m1());
      }
   }

   private ModelData loadSavedModelData(int n) {
      return null;
   }

   private ModelData createModelDataCallback(CosmeticModel cosmeticModel, boolean flag, CosmeticModel cosmeticModel2) {
      return this.buildModelData(cosmeticModel, flag);
   }

   static {
      if (getFlowState() != null) {
         setFlowState(new int[2]);
      }

      lastHintTimestamp = 0L;
   }

   public static void setFlowState(int[] ints) {
      flowState = ints;
   }

   public static int[] getFlowState() {
      return flowState;
   }

}
