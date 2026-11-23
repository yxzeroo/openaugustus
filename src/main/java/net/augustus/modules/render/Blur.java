package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.events.EventRenderEntity;
import net.augustus.render.RenderPassTask;
import net.augustus.ClientManagers;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.settings.options.BlurItemOption;
import net.augustus.utils.combat.TargetFilterPresets;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.events.listeners.RenderEntityListener;
import net.augustus.settings.options.EspRenderModeOption;
import net.augustus.settings.TargetSelectionSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class Blur extends Module implements RenderEntityListener {
   public final BooleansSetting modesNew;
   public final StringValue sigma;
   public final BooleanValue inScreens;
   public final BooleanValue blurEntities;
   private final TargetSelectionSettings entities;
   private final MultiOptionSetting itemsToBlur;

   public Blur() {
       super("Blur", "Unschärfe", new Color(170, 181, 182, 255), Categorys.RENDER);
      BooleansSetting booleansSetting = new BooleansSetting(this, "ModesNew", "Kawase", new String[]{"Kawase", "Gaussian", "Box"});
      String str = "Modes";
      this.modesNew = (BooleansSetting)booleansSetting.withDisplayName(str);
      this.sigma = new StringValue(this, "Sigma", 7.0, 4.0, 30.0, 1);
      this.inScreens = new BooleanValue(this, "InScreens", true);
      this.blurEntities = new BooleanValue(this, "BlurEntities", false);
      TargetSelectionSettings targetSelectionSettings = new TargetSelectionSettings(this, TargetFilterPresets.createRenderFilterWithSelf());
      String str2 = "Entities";
      TargetSelectionSettings targetSelectionSettings2 = targetSelectionSettings.withTargetsDisplayName(str2);
      BooleanValue blurEntities2 = this.blurEntities;
      this.blurEntities.getClass();
      BooleanValue booleanValue = blurEntities2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.entities = targetSelectionSettings2.withVisibility(condition);
      this.itemsToBlur = new MultiOptionSetting(this, "ItemsToBlur", BlurItemOption.values());
   }

   @Override
   public void onEnable() {
      BlurShadowManager.shouldWarnIncompatibleSettings = true;
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 31;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onRenderEntity(EventRenderEntity event) {
      if (this.blurEntities.getValue() && (!ClientManagers.moduleManager.esp.isToggled() || !EspRenderModeOption.VANILLA.isSelected())) {
         EntityLivingBase entity = event.getEntity();
         if (this.entities.getTargetFilter().matches(entity)) {
            BlurShadowManager.enableBlurOnce();
            BlurShadowManager.disableNormalPass();
            RendererLivingEntity renderer = event.getRenderer();
            EntityLivingBase entity2 = event.getEntity();
            boolean flag = !entity.isRenderNameTag();
            RenderPassTask renderPassTask = shaderRenderPass -> renderEntityBlurPass(event, entity2, renderer, entity, shaderRenderPass);
            boolean flag2 = flag;
            BlurShadowManager.submitRenderTask(flag2, renderPassTask);
         }
      }
   }

   private static void renderEntityBlurPass(EventRenderEntity event, EntityLivingBase entity, RendererLivingEntity rendererLivingEntity, Entity entity2, ShaderRenderPass shaderRenderPass) {
      double d = entity2.lastTickPosX + (entity2.posX - entity2.lastTickPosX) * (double)mc.getTimer().renderPartialTicks;
      double d2 = entity2.lastTickPosY + (entity2.posY - entity2.lastTickPosY) * (double)mc.getTimer().renderPartialTicks;
      double d3 = entity2.lastTickPosZ + (entity2.posZ - entity2.lastTickPosZ) * (double)mc.getTimer().renderPartialTicks;
      boolean flag = true;
      double d4 = d3;
      double d5 = d2;
      double d6 = d;
      event.renderEntityModel(entity, rendererLivingEntity, d6, d5, d4, flag);
   }

}
