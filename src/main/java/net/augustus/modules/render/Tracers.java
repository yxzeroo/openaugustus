package net.augustus.modules.render;

import java.awt.Color;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.events.EventOrientCamera;
import net.augustus.ClientManagers;
import net.augustus.settings.ColorSetting;
import net.augustus.utils.combat.EntityTypeUtil;
import net.augustus.utils.combat.EntityTargetFilter;
import net.augustus.utils.combat.TargetFilterPresets;
import net.augustus.utils.combat.EntityTargetSelector;
import net.augustus.render.RenderUtil;
import net.augustus.utils.render.ColorUtil;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.events.listeners.OrientCameraListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.TargetSelectionSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.augustus.settings.DoubleValue;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class Tracers extends Module implements OrientCameraListener, RenderListener, TickListener {
   public final StringValue mode;
   public final BooleanValue staticColor;
   public final ColorSetting color2;
   public final BooleanValue teamColors;
   public final DoubleValue lineWidth;
   public final BlurShadowSettings blurShadowSettings;
   private final TargetSelectionSettings entities;
   private final BooleanValue onlyAlerted;
   public final DoubleValue maximumRenderDistance;
   private boolean savedViewBobbing;
   private final java.util.ArrayList targetEntities;
   public float cameraEyeHeight;

   public Tracers() {
       super("Tracers", "Isotopenindikator", new Color(12, 58, 133), Categorys.RENDER);
      this.mode = new StringValue(this, "Mode", "Feet", new String[]{"Feet", "CrossHair"});
      BooleanValue booleanValue = new BooleanValue(this, "StaticColor", false);
      SettingVisibilityCondition condition = () -> {
         return !ClientManagers.moduleManager.tracers.color2.isRainbowEnabled();
      };
      this.staticColor = (BooleanValue)booleanValue.withVisibility(condition);
      this.color2 = (ColorSetting)new ColorSetting(this, "Color", new Color(21, 121, 230, 65)).withVisibility(() -> isColorVisible());
      this.teamColors = (BooleanValue)new BooleanValue(this, "TeamColors", false).withVisibility(() -> isTeamColorsVisible());
      this.lineWidth = new DoubleValue(this, "LineWidth", 6.0, 0.1, 15.0, 1);
      this.blurShadowSettings = new BlurShadowSettings(this).withCustomShadowColor(Color.black).disableBothByDefault();
      this.entities = new TargetSelectionSettings(this, TargetFilterPresets.createRenderFilter()).withTargetsDisplayName("Entities");
      this.onlyAlerted = new BooleanValue(this, "OnlyAlerted", false);
      this.maximumRenderDistance = new DoubleValue(this, "MaximumRenderDistance", 500.0, 0.0, 500.0, 0);
      this.targetEntities = new java.util.ArrayList();
      this.cameraEyeHeight = 0.0F;
   }

   @Override
   public void onEnable() {
      this.savedViewBobbing = ((GameSettings)null).viewBobbing;
      ((GameSettings)null).viewBobbing = false;
   }

   @Override
   public void onDisable() {
      ((GameSettings)null).viewBobbing = this.savedViewBobbing;
      this.targetEntities.clear();
   }

   @Override
   public void updateEventSubscriptions() {
      this.toggleEventSubscription((byte)24, (byte)11);
      this.toggleEventSubscription((byte)32);
      this.toggleEventSubscription((byte)40);
   }

   @Override
   public void onTick2() {
      this.targetEntities.clear();
      if (this.isLicenseTimestampValid()) {
         EntityTargetSelector entityTargetSelector = EntityTargetSelector.of(mc.theWorld.loadedEntityList);
         entityTargetSelector = entityTargetSelector.withTargetFilter(this.entities.getTargetFilter());
         entityTargetSelector = entityTargetSelector.maxDistance(this.maximumRenderDistance.getValue());
         entityTargetSelector = entityTargetSelector.filter(obj -> this.isAlertedOrAllowedEntity((Entity)obj));
         entityTargetSelector.forEach(obj2 -> this.targetEntities.add((EntityLivingBase)obj2));
      }
   }

   @Override
   public void onRender(float f) {
      RenderUtil.begin3DRender();
      GL11.glLineWidth((float)(this.lineWidth.getValue() / 2.0));
      BlurShadowManager.enableOneShotPass(this.blurShadowSettings.isBlurActive(), this.blurShadowSettings.isShadowActive());
      BlurShadowManager.submitRenderTask(false, shaderRenderPass -> this.renderTracersPass(shaderRenderPass));
      RenderUtil.end3DRender();
      GL11.glLineWidth(1.0F);
   }

   @Override
   public void onOrientCamera(EventOrientCamera event) {
      this.cameraEyeHeight = event.getEyeHeight();
   }

   private void renderEntityTracer(EntityLivingBase entity, int n, ShaderRenderPass shaderRenderPass) {
      int n2;
      if (this.staticColor.getValue()) {
         n2 = this.color2.getColorInt(n);
      } else if (this.teamColors.getValue() && entity instanceof EntityPlayer) {
         n2 = ColorUtil.withAlpha(ColorUtil.getTeamColor((EntityPlayer)entity), 255);
      } else {
         double d = (double)(Math.min(Math.max(mc.thePlayer.getDistanceToEntity(entity), 6.0F), 36.0F) - 6.0F);
         float[] floats = ColorUtil.toHsb(this.color2.getColorInt(n));
         n2 = Color.HSBtoRGB((float)(0.011415525F * d), floats[1], floats[2]);
      }

      RenderUtil.drawTracerLine(entity, this.blurShadowSettings.resolvePassColor(shaderRenderPass, ColorUtil.resolvePassColor(shaderRenderPass, n2)), this.mode.getSelectedValue());
   }

   private void renderTracersPass(ShaderRenderPass shaderRenderPass) {

      for (int i = 0; i < this.targetEntities.size(); i++) {
         this.renderEntityTracer((EntityLivingBase)this.targetEntities.get(i), i, shaderRenderPass);
      }
   }

   private boolean isAlertedOrAllowedEntity(Entity entity) {
      return !EntityTypeUtil.isPlayer(entity) || !this.onlyAlerted.getValue() || EntityTypeUtil.isAlertTarget(entity);
   }

   private static boolean isTeamColorsVisible() {
      return !ClientManagers.moduleManager.tracers.color2.isRainbowEnabled();
   }

   private static boolean isColorVisible() {
      return ClientManagers.moduleManager.tracers.staticColor.getValue();
   }

   private boolean isLicenseTimestampValid() {
      byte[] timestampBytes = LicenseTimestampStore.timestampBytes;
      boolean flag = false;
      if (timestampBytes != null) {
         long l = 0L;

         for (int i = 0; i < 8; i++) {
            l <<= 8;
            l |= (long)(timestampBytes[i] & 255);
         }

         long l2 = System.currentTimeMillis() - l;
         String str = "518400000";
         long l3 = 0L;
         int n = 0;
         int length = str.length();

         while (n < length) {
            int n2 = Character.digit(str.charAt(n++), 10);
            l3 *= 10L;
            l3 -= (long)n2;
         }

         l3 = -l3;
         flag = l2 <= l3;
      }

      return flag;
   }

}
