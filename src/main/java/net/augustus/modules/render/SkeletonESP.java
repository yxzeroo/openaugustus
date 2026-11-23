package net.augustus.modules.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.modules.misc.MurdererFinder;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.ClientManagers;
import net.augustus.settings.ColorSetting;
import net.augustus.render.AbstractSkeletonRenderer;
import net.augustus.render.VillagerSkeletonRenderer;
import net.augustus.render.EndermanSkeletonRenderer;
import net.augustus.render.BipedSkeletonRenderer;
import net.augustus.render.IronGolemSkeletonRenderer;
import net.augustus.utils.combat.TargetFilterPresets;
import net.augustus.render.RenderUtil;
import net.augustus.utils.render.ColorUtil;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.settings.TargetSelectionSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

public class SkeletonESP extends Module implements RenderListener {
   private final TargetSelectionSettings entities;
   private final StringValue headLengthAdd;
   private final StringValue armLengthAdd;
   private final StringValue legLengthAdd;
   private final StringValue lineWidth;
   private final ColorSetting color2;
   private final BooleanValue hurtColorBoolean;
   private final ColorSetting hurtColor;
   public final BooleanValue markMurderer;
   public final BooleanValue customMurdererAlpha;
   public final StringValue murdererAlpha;
   private final BlurShadowSettings blurShadowSettings;
   private final BooleanValue shadowColorBoolean;
   private final ColorSetting shadowColor;
   private final BooleanValue hurtShadowColorBoolean;
   private final ColorSetting hurtShadowColor;
   private final Map skeletonRenderers;

   public SkeletonESP() {
       super("SkeletonESP", "SkelettESP", new Color(235, 183, 52), Categorys.RENDER);
      TargetSelectionSettings targetSelectionSettings = new TargetSelectionSettings(this, TargetFilterPresets.createRenderFilterWithSelf());
      String str = "Entities";
      this.entities = targetSelectionSettings.withTargetsDisplayName(str);
      this.headLengthAdd = new StringValue(this, "HeadLengthAdd", 0.2, -1.0, 1.0, 3);
      this.armLengthAdd = new StringValue(this, "ArmLengthAdd", -0.1, -1.0, 1.0, 3);
      this.legLengthAdd = new StringValue(this, "LegLengthAdd", -0.1, -1.0, 1.0, 3);
      this.lineWidth = new StringValue(this, "LineWidth", 2.0, 0.1, 10.0, 2);
      this.color2 = new ColorSetting(this, "Color", Color.white);
      BooleanValue booleanValue = new BooleanValue(this, "HurtColorBoolean", false);
      String str2 = "HurtColor";
      this.hurtColorBoolean = (BooleanValue)booleanValue.withDisplayName(str2);
      ColorSetting colorSetting = new ColorSetting(this, "HurtColor", Color.white);
      BooleanValue hurtColorBoolean2 = this.hurtColorBoolean;
      this.hurtColorBoolean.getClass();
      BooleanValue booleanValue2 = hurtColorBoolean2;
      SettingVisibilityCondition condition = () -> booleanValue2.getValue();
      this.hurtColor = (ColorSetting)colorSetting.withVisibility(condition);
      this.markMurderer = new BooleanValue(this, "MarkMurderer", true);
      BooleanValue booleanValue3 = new BooleanValue(this, "Custom Murderer Alpha", false);
      hurtColorBoolean2 = this.markMurderer;
      this.markMurderer.getClass();
      BooleanValue booleanValue4 = hurtColorBoolean2;
      SettingVisibilityCondition condition2 = () -> booleanValue4.getValue();
      this.customMurdererAlpha = (BooleanValue)booleanValue3.withVisibility(condition2);
      StringValue stringValue = new StringValue(this, "Murderer Alpha", 65.0, 0.0, 255.0, 0);
      SettingVisibilityCondition condition3 = () -> this.isMurdererAlphaVisible();
      this.murdererAlpha = stringValue.visibleWhen(condition3);
      this.blurShadowSettings = (new BlurShadowSettings(this)).disableBlurByDefault();
      BooleanValue booleanValue5 = new BooleanValue(this, "ShadowColorBoolean", false);
      String str3 = "ShadowColor";
      BooleanValue booleanValue6 = (BooleanValue)booleanValue5.withDisplayName(str3);
      SettingVisibilityCondition condition4 = () -> this.isShadowColorVisible();
      this.shadowColorBoolean = (BooleanValue)booleanValue6.withVisibility(condition4);
      ColorSetting colorSetting2 = new ColorSetting(this, "ShadowColor", Color.white);
      SettingVisibilityCondition condition5 = () -> {
         return this.shadowColorBoolean.isVisible() && this.shadowColorBoolean.getValue();
      };
      this.shadowColor = (ColorSetting)colorSetting2.withVisibility(condition5);
      BooleanValue booleanValue7 = new BooleanValue(this, "HurtShadowColorBoolean", false);
      String str4 = "HurtShadowColor";
      BooleanValue booleanValue8 = (BooleanValue)booleanValue7.withDisplayName(str4);
      ColorSetting shadowColor2 = this.shadowColor;
      this.shadowColor.getClass();
      ColorSetting colorSetting3 = shadowColor2;
      SettingVisibilityCondition condition6 = () -> colorSetting3.isVisible();
      this.hurtShadowColorBoolean = (BooleanValue)booleanValue8.withVisibility(condition6);
      ColorSetting colorSetting4 = new ColorSetting(this, "HurtShadowColor", Color.white);
      SettingVisibilityCondition condition7 = () -> {
         return this.shadowColor.isVisible() && this.hurtShadowColorBoolean.getValue();
      };
      this.hurtShadowColor = (ColorSetting)colorSetting4.withVisibility(condition7);
      this.skeletonRenderers = new HashMap();
      this.skeletonRenderers.put(ModelBiped.class, new BipedSkeletonRenderer());
      this.skeletonRenderers.put(ModelPlayer.class, new BipedSkeletonRenderer());
      this.skeletonRenderers.put(ModelSkeleton.class, new BipedSkeletonRenderer());
      this.skeletonRenderers.put(ModelZombie.class, new BipedSkeletonRenderer());
      this.skeletonRenderers.put(ModelZombieVillager.class, new BipedSkeletonRenderer());
      this.skeletonRenderers.put(ModelEnderman.class, new EndermanSkeletonRenderer());
      this.skeletonRenderers.put(ModelIronGolem.class, new IronGolemSkeletonRenderer());
      this.skeletonRenderers.put(ModelVillager.class, new VillagerSkeletonRenderer());
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 32;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onRender(float f) {
      RenderUtil.begin3DRender();
      boolean isBlurActive = this.blurShadowSettings.isBlurActive();
      BlurShadowManager.enableOneShotPass(isBlurActive, this.blurShadowSettings.isShadowActive());
      BlurShadowManager.submitRenderTask(false, shaderRenderPass -> this.renderSkeletonsPass(f, shaderRenderPass));
      RenderUtil.end3DRender();
   }

   private int renderEntitySkeleton(EntityLivingBase entity, ModelBase modelBase, ShaderRenderPass shaderRenderPass, int n) {
      if (entity.isSneaking()) {
         GlStateManager.translate(0.0F, 0.2F, 0.0F);
      }

      AbstractSkeletonRenderer skeletonRenderer = (AbstractSkeletonRenderer)this.skeletonRenderers.get(modelBase.getClass());
      if (skeletonRenderer != null) {
         int n2 = entity.hurtTime != 0 && this.hurtColorBoolean.getValue() ? this.hurtColor.getColorInt(n) : this.color2.getColorInt(n);
         if (this.markMurderer.getValue()) {
            MurdererFinder murdererFinder = ClientManagers.moduleManager.murdererFinder;
            String name2 = entity.getName();
            if (murdererFinder.isMurderer(name2)) {
               int n3;
               if (this.customMurdererAlpha.getValue()) {
                  boolean flag = true;
                  n3 = (int)this.murdererAlpha.getValue(flag);
               } else {
                  n3 = ColorUtil.getAlpha(n2);
               }

               int n4 = n3;
               byte b = 0;
               n2 = ColorUtil.packRgba(255, 0, b, n4);
            }
         }

         exitBlock: {
            n++;
            if (shaderRenderPass == ShaderRenderPass.SHADOW) {
               if (this.blurShadowSettings.isShadowActive() && this.shadowColorBoolean.getValue()) {
                  RenderUtil.setGlColor(entity.hurtTime != 0 && this.hurtShadowColorBoolean.getValue() ? this.hurtShadowColor.getColorInt(n) : this.shadowColor.getColorInt(n));
                  break exitBlock;
               }
            }

            RenderUtil.setGlColor(ColorUtil.resolvePassColor(shaderRenderPass, n2));
         }

         GL11.glLineWidth((float)this.lineWidth.getValue());
         double value = this.headLengthAdd.getValue();
         double value2 = this.armLengthAdd.getValue();
         double value3 = this.legLengthAdd.getValue();
         double d = value2;
         double d2 = value;
         skeletonRenderer.renderSkeleton(modelBase, d2, d, value3);
      }

      return n;
   }

   private void renderAllSkeletons(ShaderRenderPass shaderRenderPass, float f) {
      int n = 0;

      for (Entity entity : (Iterable<Entity>)(mc.theWorld.loadedEntityList)) {
         Render entityRenderObject = mc.getRenderManager().getEntityRenderObject(entity);
         if (entityRenderObject instanceof RendererLivingEntity && entity instanceof EntityLivingBase && this.isValidTargetEntity(entity)) {
            EntityLivingBase entity2 = (EntityLivingBase)entity;
            RendererLivingEntity rendererLivingEntity = (RendererLivingEntity)entityRenderObject;
            ModelBase mainModel = rendererLivingEntity.getMainModel();
            double d = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f;
            double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f;
            double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f;
            GL11.glPushMatrix();
            float[] floats = rendererLivingEntity.setupModelPose(mainModel, entity2, f, d, d2, d3);
            mainModel.setRotationAngles(floats[0], floats[1], floats[2], floats[3], floats[4], floats[5], entity);
            n = this.renderEntitySkeleton(entity2, mainModel, shaderRenderPass, n);
            GlStateManager.disableRescaleNormal();
            GL11.glPopMatrix();
         }
      }
   }

   public boolean isValidTargetEntity(Entity entity) {
      return (entity != mc.thePlayer || mc.gameSettings.thirdPersonView != 0) && this.entities.getTargetFilter().matches(entity);
   }

   private void renderSkeletonsPass(float f, ShaderRenderPass shaderRenderPass) {
      this.renderAllSkeletons(shaderRenderPass, f);
   }

   private boolean isShadowColorVisible() {
      return this.blurShadowSettings.isShadowActive() && !ClientManagers.moduleManager.shadow.customColor.getValue();
   }

   private boolean isMurdererAlphaVisible() {
      return this.markMurderer.getValue() && this.customMurdererAlpha.getValue();
   }

}
