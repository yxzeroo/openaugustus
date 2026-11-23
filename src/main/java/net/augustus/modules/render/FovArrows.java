package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.settings.ColorSetting;
import net.augustus.utils.combat.CombatTargetUtil;
import net.augustus.render.RenderUtil;
import net.augustus.utils.render.ColorUtil;
import net.augustus.Augustus;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class FovArrows extends Module {
   private final StringValue maxTargetDistance;
   private final StringValue crossHairDistance;
   private final BooleanValue displayOnlyArrowsForTargetsThatAreNotInMyViewfrustum;
   private final BooleanValue useMinRotationDistance;
   private final StringValue minRotationDistance;
   private final BooleanValue horizontalOnly;
   private final StringValue scale;
   private final ColorSetting color2;
   private final BooleanValue distAlpha;
   private final BooleanValue distColor;
   private final BooleanValue renderNames;
   private final BooleanValue outline;
   private final BooleanValue differentOutlineColor;
   private final ColorSetting outlineColor;
   public final BlurShadowSettings blurShadowSettings;
   private final java.util.ArrayList targetPlayers;
   private final Frustum frustum;

   public FovArrows() {
       super("FovArrows", "SichtfeldPfeile", new Color(124, 83, 245), Categorys.RENDER);
      this.maxTargetDistance = new StringValue(this, "MaxTargetDistance", 20.0, 1.0, 30.0, 0);
      this.crossHairDistance = new StringValue(this, "CrossHairDistance", 30.0, 0.0, 50.0, 0);
      this.displayOnlyArrowsForTargetsThatAreNotInMyViewfrustum = new BooleanValue(this, "Display only arrows for targets that are not in my viewfrustum", true);
      this.useMinRotationDistance = new BooleanValue(this, "UseMinRotationDistance", true);
      StringValue stringValue = new StringValue(this, "MinRotationDistance", 0.0, 0.0, 180.0, 0);
      BooleanValue useMinRotationDistance2 = this.useMinRotationDistance;
      this.useMinRotationDistance.getClass();
      BooleanValue booleanValue = useMinRotationDistance2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.minRotationDistance = stringValue.visibleWhen(condition);
      this.horizontalOnly = new BooleanValue(this, "Horizontal only", true);
      this.scale = new StringValue(this, "Scale", 2.0, 1.0, 4.0, 2);
      this.color2 = new ColorSetting(this, "Color", Color.red);
      this.distAlpha = new BooleanValue(this, "DistAlpha", true);
      this.distColor = new BooleanValue(this, "DistColor", true);
      this.renderNames = new BooleanValue(this, "RenderNames", false);
      this.outline = new BooleanValue(this, "Outline", true);
      BooleanValue booleanValue2 = new BooleanValue(this, "DifferentOutlineColor", true);
      useMinRotationDistance2 = this.outline;
      this.outline.getClass();
      BooleanValue booleanValue3 = useMinRotationDistance2;
      SettingVisibilityCondition condition2 = () -> booleanValue3.getValue();
      this.differentOutlineColor = (BooleanValue)booleanValue2.withVisibility(condition2);
      ColorSetting colorSetting = new ColorSetting(this, "OutlineColor", Color.black);
      SettingVisibilityCondition condition3 = () -> this.isOutlineColorVisible();
      this.outlineColor = (ColorSetting)colorSetting.withVisibility(condition3);
      BlurShadowSettings blurShadowSettings2 = new BlurShadowSettings(this);
      Color black = Color.black;
      this.blurShadowSettings = blurShadowSettings2.withCustomShadowColor(black).disableBlurByDefault();
      this.targetPlayers = new java.util.ArrayList();
      this.frustum = new Frustum();
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
      this.targetPlayers.clear();
   }

   @Override
   public void updateEventSubscriptions() {
   }

   public void renderArrowsPass(ShaderRenderPass shaderRenderPass) {
      int n = 0;
      if (mc.thePlayer != null) {
         double value = this.minRotationDistance.getValue();
         double value2 = this.minRotationDistance.getValue();
         value2 *= value2;

         for (EntityPlayer player : (Iterable<EntityPlayer>)(this.targetPlayers)) {
            float f = (float)(player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)mc.getTimer().renderPartialTicks);
            float f2 = (float)(player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)mc.getTimer().renderPartialTicks);
            float f3 = (float)(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)mc.getTimer().renderPartialTicks);
            double d;
            if (this.horizontalOnly.getValue()) {
               double d2 = (double)RotationUtil.getInterpolatedAngleDiffToPos(
                  (double)f, (double)f2, (double)f3, Augustus.getInstance().getYawPitchHelper().realYaw, mc.thePlayer.rotationPitch
               )[0];
               if (this.useMinRotationDistance.getValue() && Math.abs(d2) < value) {
                  continue;
               }

               d = (d2 - 90.0) / 180.0 * Math.PI;
               d = Math.atan2(Math.sin(d), -Math.cos(d));
            } else {
               float[] floats = RotationUtil.getInterpolatedAngleDiffToPos((double)f, (double)f2, (double)f3, Augustus.getInstance().getYawPitchHelper().realYaw, mc.thePlayer.rotationPitch);
               if (this.useMinRotationDistance.getValue() && (double)(floats[0] * floats[0] + floats[1] * floats[1]) < value2) {
                  continue;
               }

               d = Math.atan2((double)floats[1], (double)(-floats[0]));
            }

            ScaledResolution sr = new ScaledResolution(mc);
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F / (float)sr.getScaleFactor(), 1.0F / (float)sr.getScaleFactor(), 1.0F);
            GlStateManager.scale(this.scale.getValue(), this.scale.getValue(), 1.0);
            sr = new ScaledResolution(
               (double)mc.displayWidth * (1.0 / this.scale.getValue()), (double)mc.displayHeight * (1.0 / this.scale.getValue()), (int)this.scale.getValue()
            );
            float f4 = (float)((double)(sr.getScaledWidth() / 2) + this.crossHairDistance.getValue() * Math.cos(d));
            float f5 = (float)((double)(sr.getScaledHeight() / 2) + this.crossHairDistance.getValue() * Math.sin(d));
            if (this.renderNames.getValue()) {
               GlStateManager.scale(0.5, 0.5, 1.0);
               FontRenderer fontRendererObj = mc.fontRendererObj;
               String name2 = player.getName();
               float f6 = (f4 - (float)mc.fontRendererObj.getStringWidth(player.getName()) / 4.0F) * 2.0F;
               float f7 = (f5 + 8.0F) * 2.0F;
               byte b = -1;
               fontRendererObj.drawStringUnfiltered(name2, f6, f7, this.blurShadowSettings.resolvePassColor(shaderRenderPass, b));
               GlStateManager.scale(2.0F, 2.0F, 1.0F);
            }

            GlStateManager.translate(f4, f5, 5.0F);
            GlStateManager.rotate((float)(d / Math.PI * 180.0) - 90.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(-f4, -f5, 5.0F);
            int colorInt = this.color2.getColorInt(n);
            double d3 = 255.0 / MathHelper.clamp_double(this.maxTargetDistance.getValue(), 0.0, 255.0);
            if (this.distAlpha.getValue()) {
               colorInt = ColorUtil.withAlpha(colorInt, (int)(255.0 - Math.min((double)mc.thePlayer.getDistanceToEntity(player) * d3, 255.0)));
            }

            if (this.distColor.getValue()) {
               float[] floats2 = ColorUtil.toHsb(colorInt);
               float f8 = 30.0F / MathHelper.clamp_float((float)this.maxTargetDistance.getValue() - 6.0F, 0.0F, 255.0F);
               int n2 = Color.HSBtoRGB(
                  (float)(
                     0.01141552511 * (double)(MathHelper.clamp_float(mc.thePlayer.getDistanceToEntityBox(player) - 6.0F, 0.0F, (float)(this.maxTargetDistance.getValue() - 6.0)) * f8)
                  ),
                  floats2[1],
                  floats2[2]
               );
               colorInt = ColorUtil.withAlpha(n2, ColorUtil.getAlpha(colorInt));
            }

            colorInt = this.blurShadowSettings.resolvePassColor(shaderRenderPass, colorInt);
            RenderUtil.drawFilledTriangle(f4, f5, 10.0F, colorInt);
            if (this.outline.getValue()) {
               GL11.glLineWidth(1.0F);
               if (this.differentOutlineColor.getValue()) {
                  colorInt = this.outlineColor.getColorInt(n);
               }

               colorInt = this.blurShadowSettings.resolvePassColor(shaderRenderPass, colorInt);
               int n3 = this.differentOutlineColor.getValue() ? ColorUtil.withAlpha(colorInt, 255) : colorInt;
               float f9 = 10.0F;
               RenderUtil.drawOutlinedTriangle(f4, f5, f9, n3);
               GL11.glLineWidth(1.0F);
            }

            GlStateManager.popMatrix();
            n++;
         }

         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   public void updateTargetPlayers() {
      this.frustum.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
      this.targetPlayers.clear();
      if (mc.thePlayer != null) {
         boolean value = this.displayOnlyArrowsForTargetsThatAreNotInMyViewfrustum.getValue();

         for (Entity entity : (Iterable<Entity>)(mc.theWorld.loadedEntityList)) {
            if (entity instanceof EntityPlayer && !(entity instanceof EntityPlayerSP)) {
               boolean flag = false;
               boolean flag2 = false;
               boolean flag3 = false;
               boolean flag4 = true;
               boolean flag5 = false;
               boolean flag6 = false;
               boolean flag7 = false;
               boolean flag8 = false;
               boolean flag9 = true;
               if (CombatTargetUtil.isValidTarget(entity, flag9, flag8, flag7, flag6, flag5, flag4, flag3, flag2, flag)) {
                  EntityPlayer player = (EntityPlayer)entity;
                  if (this.isValidArrowTarget(player)
                     && (!value || !this.frustum.isBoundingBoxInFrustum(player.getEntityBoundingBox()))
                     && (double)mc.thePlayer.getDistanceToEntityBox(player) < this.maxTargetDistance.getValue()) {
                     this.targetPlayers.add(player);
                  }
               }
            }
         }
      }
   }

   public boolean isValidArrowTarget(EntityPlayer player) {
      return !ClientManagers.moduleManager.antiBot.isBot(player)
         && (!ClientManagers.moduleManager.midClick.isFriend(player) || ClientManagers.moduleManager.midClick.autoUnfriendTriggered)
         && (!ClientManagers.moduleManager.teams.isTeammate(player) || !ClientManagers.moduleManager.teams.isToggled());
   }

   private boolean isOutlineColorVisible() {
      return this.differentOutlineColor.getValue() && this.outline.getValue();
   }

}
