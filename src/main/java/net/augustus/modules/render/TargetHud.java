package net.augustus.modules.render;

import java.awt.Color;
import java.util.function.BiFunction;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.ClientManagers;
import net.augustus.settings.ModeSetting;
import net.augustus.modules.modes.targethud.ModernTargetHudMode;
import net.augustus.modules.modes.targethud.MinecraftTargetHudMode;
import net.augustus.modules.modes.targethud.AbstractTargetHudMode;
import net.augustus.modules.modes.targethud.LiquidGlassTargetHudMode;
import net.augustus.render.RenderUtil;
import net.augustus.render.ProjectionUtil;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.animation.AnimationUtil;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.Vec3;

public class TargetHud extends Module implements RenderListener, TickListener {
   public final ModeSetting modes;
   public final BooleansSetting healthMode;
   public final BooleanValue trackEntity;
   public final StringValue trackY;
   public final StringValue trackX;
   public final StringValue xCoord;
   public final StringValue yCoord;
   public final BooleansSetting toggleAnimation2;
   public final StringValue animationTime;
   public final BlurShadowSettings blurShadowSettings;
   private AbstractClientPlayer displayedTarget;
   private AbstractClientPlayer lastActiveTarget;
   public Vec3 projectedTargetPos;

   public TargetHud() {
       super("TargetHud", "Angriffszieloberfläche", new Color(50, 168, 158), Categorys.RENDER);
      BiFunction[] biFunctions = new BiFunction[]{
         MinecraftTargetHudMode.factory(), ModernTargetHudMode.factory(), LiquidGlassTargetHudMode.factory()
      };
      this.modes = new ModeSetting(this, "Modes", "LiquidGlass", biFunctions);
      this.healthMode = new BooleansSetting(this, "HealthMode", "Entity", new String[]{"Entity", "Scoreboard", "NameTags"});
      this.trackEntity = new BooleanValue(this, "TrackEntity", true);
      StringValue stringValue = new StringValue(this, "TrackY", 0.0, -2.0, 2.0, 2);
      BooleanValue trackEntity2 = this.trackEntity;
      this.trackEntity.getClass();
      BooleanValue booleanValue = trackEntity2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.trackY = stringValue.visibleWhen(condition);
      StringValue stringValue2 = new StringValue(this, "TrackX", 0.0, -200.0, 200.0, 2);
      trackEntity2 = this.trackEntity;
      this.trackEntity.getClass();
      BooleanValue booleanValue2 = trackEntity2;
      SettingVisibilityCondition condition2 = () -> booleanValue2.getValue();
      this.trackX = stringValue2.visibleWhen(condition2);
      StringValue stringValue3 = new StringValue(this, "XCoord", 60.0, 0.0, 100.0, 2);
      SettingVisibilityCondition condition3 = () -> this.isXCoordVisible();
      this.xCoord = stringValue3.visibleWhen(condition3);
      StringValue stringValue4 = new StringValue(this, "YCoord", 50.0, 0.0, 100.0, 2);
      SettingVisibilityCondition condition4 = () -> this.isYCoordVisible();
      this.yCoord = stringValue4.visibleWhen(condition4);
      this.toggleAnimation2 = new BooleansSetting(this, "ToggleAnimation", "easeOutBack", AnimationUtil.getEasingNames(true));
      StringValue stringValue5 = new StringValue(this, "AnimationTime", 500.0, 0.0, 2000.0, 0);
      SettingVisibilityCondition condition5 = () -> this.isAnimationTimeVisible();
      this.animationTime = stringValue5.visibleWhen(condition5);
      BlurShadowSettings blurShadowSettings2 = new BlurShadowSettings(this);
      Color black = Color.black;
      this.blurShadowSettings = blurShadowSettings2.withCustomShadowColor(black);
      this.displayedTarget = null;
      this.lastActiveTarget = null;
      this.projectedTargetPos = new Vec3(0.0, 0.0, 0.0);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
      this.displayedTarget = null;
      this.lastActiveTarget = null;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 32;
      this.toggleEventSubscription(b);
      byte b2 = 40;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onTick() {
      String selectedValue = this.modes.getSelectedValue();
      this.setSuffix(selectedValue);
   }

   @Override
   public void onRender(float f, ScaledResolution sr) {
      AbstractClientPlayer activeTarget = this.getActiveTarget();
      if (activeTarget != this.lastActiveTarget && this.modes.getAbstractSubMode() != null) {
         ((AbstractTargetHudMode)this.modes.getAbstractSubMode()).onTargetChanged(activeTarget);
      }

      if (activeTarget != null) {
         this.displayedTarget = activeTarget;
      }

      if ((!this.trackEntity.getValue() || this.projectedTargetPos.zCoord >= 0.0 && this.projectedTargetPos.zCoord <= 1.0) && this.displayedTarget != null) {
         AbstractTargetHudMode abstractSubMode = (AbstractTargetHudMode)this.modes.getAbstractSubMode();
         if (abstractSubMode != null) {
            AbstractClientPlayer displayedTarget2 = this.displayedTarget;
            abstractSubMode.renderTargetHud(displayedTarget2, activeTarget, sr);
            if (this.displayedTarget != null) {
               AbstractClientPlayer displayedTarget3 = this.displayedTarget;
               abstractSubMode.recordLastTarget(displayedTarget3);
            }
         }
      }

      this.lastActiveTarget = activeTarget;
      RenderUtil.resetGlColorState();
   }

   @Override
   public void onRender(float f) {
      if (this.trackEntity.getValue()
         && this.displayedTarget != null
         && this.modes.getAbstractSubMode() != null
         && (((AbstractTargetHudMode)this.modes.getAbstractSubMode()).toggleAnimation.getLinearProgress() != 1.0 || this.lastActiveTarget != null)) {
         Vec3 vec = new Vec3(
            this.displayedTarget.lastTickPosX + (this.displayedTarget.posX - this.displayedTarget.lastTickPosX) * (double)mc.getTimer().renderPartialTicks - mc.getRenderManager().getRenderPosX(),
            this.displayedTarget.lastTickPosY + (this.displayedTarget.posY - this.displayedTarget.lastTickPosY) * (double)mc.getTimer().renderPartialTicks - mc.getRenderManager().getRenderPosY(),
            this.displayedTarget.lastTickPosZ + (this.displayedTarget.posZ - this.displayedTarget.lastTickPosZ) * (double)mc.getTimer().renderPartialTicks - mc.getRenderManager().getRenderPosZ()
         );
         this.projectedTargetPos = ProjectionUtil.project3DTo2D(
            vec.xCoord, vec.yCoord + (double)(this.displayedTarget.height / 2.0F) + this.trackY.getValue(), vec.zCoord, new ScaledResolution(mc)
         );
      }
   }

   private AbstractClientPlayer getActiveTarget() {
      if (ClientManagers.moduleManager.killAura.target instanceof AbstractClientPlayer) {
         return (AbstractClientPlayer)ClientManagers.moduleManager.killAura.target;
      } else if (ClientManagers.moduleManager.aimBot.target instanceof AbstractClientPlayer) {
         return (AbstractClientPlayer)ClientManagers.moduleManager.aimBot.target;
      } else {
         return ClientManagers.moduleManager.aimAssist.target instanceof AbstractClientPlayer ? (AbstractClientPlayer)ClientManagers.moduleManager.aimAssist.target : null;
      }
   }

   private boolean isAnimationTimeVisible() {
      return !this.toggleAnimation2.getSelectedValue().equals("None");
   }

   private boolean isYCoordVisible() {
      return !this.trackEntity.getValue();
   }

   private boolean isXCoordVisible() {
      return !this.trackEntity.getValue();
   }

}
