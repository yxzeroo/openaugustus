package net.augustus.settings;

import java.util.ArrayList;
import net.augustus.modules.Module;

public abstract class AbstractNoiseSetting extends Setting {
   public ArrayList layers;
   public DoubleValue noisegroup;
   private long lastUpdateTimeMs;
   public boolean normalized;

   public ArrayList getLayers() {
      return this.layers;
   }

   public void advanceByMillis(long l) {

      for (NoiseLayerSetting noiseLayerSetting : (Iterable<NoiseLayerSetting>)(this.layers)) {
         noiseLayerSetting.advanceByMillis(l);
      }
   }

   public void addLayer(NoiseLayerSetting noiseLayerSetting) {
      this.layers.add(noiseLayerSetting);
   }

   public abstract double sample();

   public void stepLayers() {

      for (NoiseLayerSetting noiseLayerSetting : (Iterable<NoiseLayerSetting>)(this.layers)) {
         noiseLayerSetting.step();
      }
   }

   @Override
   public Setting withVisibility(SettingVisibilityCondition condition) {
      return this.withNoiseVisibility(condition);
   }

   public void update() {
      if (this.lastUpdateTimeMs == 0L) {
         this.lastUpdateTimeMs = System.currentTimeMillis();
         this.stepLayers();
      } else {
         long l = System.currentTimeMillis() - this.lastUpdateTimeMs;
         this.advanceByMillis(l);
         this.lastUpdateTimeMs = System.currentTimeMillis();
      }
   }

   public NoiseLayerBuilder newLayerBuilder() {
      Module parentModule = this.getParentModule();
      DoubleValue noisegroup2 = this.noisegroup;
      String name = this.getName();
      int n = this.layers.size() + 1;
      return new NoiseLayerBuilder(parentModule, noisegroup2, name, n, () -> {
         return this.layers.size() > 1;
      });
   }

   public AbstractNoiseSetting(Module module, String str, DoubleValue doubleValue, boolean flag) {
       super(module, doubleValue, str, false);
      this.layers = new ArrayList();
      if (doubleValue == null) {
         DoubleValue doubleValue2 = new DoubleValue(module, str + "_noisegroup");
         String displayName2 = this.getDisplayName();
         this.noisegroup = (DoubleValue)doubleValue2.withDisplayName(displayName2);
      } else {
         DoubleValue doubleValue3 = new DoubleValue(module, str + "_noisegroup", doubleValue);
         String displayName3 = this.getDisplayName();
         this.noisegroup = (DoubleValue)doubleValue3.withDisplayName(displayName3);
      }

      this.normalized = flag;
   }

   public AbstractNoiseSetting withNoiseVisibility(SettingVisibilityCondition condition) {
      this.noisegroup.visibleWhen(condition);
      return this;
   }

   public void setPhasePosition(float f) {

      for (NoiseLayerSetting noiseLayerSetting : (Iterable<NoiseLayerSetting>)(this.layers)) {
         noiseLayerSetting.setPhasePosition(f);
      }
   }

   public void addDefaultLayer() {
      this.layers.add(this.newLayerBuilder().build());
   }

   @Override
   public void renderTooltip() {
   }

   @Override
   public void resetToDefault() {
   }

}
