package net.augustus.modules.combat;

import java.awt.Color;
import java.util.function.BiFunction;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.ClickListener;
import net.augustus.utils.input.KeyboardInputUtil;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.events.EventKey;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.events.EventClick;
import net.augustus.settings.KeybindSetting;
import net.augustus.settings.ColorSetting;
import net.augustus.settings.ModeSetting;
import net.augustus.settings.DynamicSettingList;
import net.augustus.events.listeners.KeyListener;
import net.augustus.modules.combat.helper.KnockbackTrajectoryUtil;
import net.augustus.modules.modes.kbdisplacement.ConstantKBDisplacementMode;
import net.augustus.modules.modes.kbdisplacement.DynamicKBDisplacementMode;
import net.augustus.modules.modes.kbdisplacement.KBDisplacementMode;
import net.augustus.utils.rotation.RotationController;
import net.augustus.utils.combat.CombatTargetUtil;
import net.augustus.utils.rotation.RayTraceUtil;
import net.augustus.render.RenderUtil;
import net.augustus.events.EventAttackEntity;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.modules.combat.helper.DisplacementTargetBinding;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.listeners.AttackEntityListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.rotation.Rotation;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class KBDisplacement extends Module implements ClickListener, AttackEntityListener, RenderListener, TickListener, KeyListener {
   public final ModeSetting mode;
   private final DynamicSettingList directionForceKeys;
   private final RandomizedRangeSettings pitchOffset;
   private final BooleanValue forceAttack;
   private final BooleanValue onlyAtKillAuraTarget;
   private final StringValue maxHurtTime;
   public final DoubleValue hud;
   public final BooleanValue showCompass;
   public final StringValue posX;
   public final StringValue posY;
   public final ColorSetting hudSelectedColor;
   public final BooleanValue debug;
   public DisplacementTargetBinding displacementTargetBinding;
   private boolean displacing;
   private boolean pendingForceAttack;
   private Rotation savedRotation;
   private final TimeHelper timeHelper;
   private final int hudBoxBackgroundColor;
   private final int hudBoxBorderColor;

   public KBDisplacement() {
       super("KBDisplacement", "KnockbackVerschiebung", new Color(255, 140, 0), Categorys.COMBAT);
      BiFunction[] biFunctions = new BiFunction[]{ConstantKBDisplacementMode.factory(), DynamicKBDisplacementMode.factory()};
      this.mode = new ModeSetting(this, "Mode", "Constant", biFunctions);
      this.directionForceKeys = new DynamicSettingList(this, "DirectionForceKeys", n -> {
         KBDisplacementHotkeyGroup group = new KBDisplacementHotkeyGroup(this, "Hotkey_" + n, true);
         KeybindSetting keybindSetting = new KeybindSetting(this, "Key_" + n, group);
         group.setKeybind(keybindSetting);
         RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(this, "Degree_" + n, 80.0, 100.0, -180.0, 180.0, 2, group);
         group.setDegreeRange(randomizedRangeSettings);
         return group;
      }, 0, 6);
      this.pitchOffset = new RandomizedRangeSettings(this, "PitchOffset", -5.0, 5.0, -90.0, 90.0, 2);
      this.forceAttack = new BooleanValue(this, "ForceAttack", true);
      this.onlyAtKillAuraTarget = new BooleanValue(this, "OnlyAtKillAuraTarget", false);
      this.maxHurtTime = new StringValue(this, "MaxHurtTime", 0.0, 0.0, 4.0, 0);
      this.hud = new DoubleValue(this, "HUD", true);
      this.showCompass = new BooleanValue(this, "ShowCompass", true, this.hud);
      StringValue stringValue = new StringValue(this, "PosX", 50.0, 0.0, 100.0, 2, this.hud);
      BooleanValue showCompass2 = this.showCompass;
      this.showCompass.getClass();
      BooleanValue booleanValue = showCompass2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.posX = stringValue.visibleWhen(condition);
      StringValue stringValue2 = new StringValue(this, "PosY", 50.0, 0.0, 100.0, 2, this.hud);
      showCompass2 = this.showCompass;
      this.showCompass.getClass();
      BooleanValue booleanValue2 = showCompass2;
      SettingVisibilityCondition condition2 = () -> booleanValue2.getValue();
      this.posY = stringValue2.visibleWhen(condition2);
      Color color2 = new Color(125, 211, 252, 220);
      DoubleValue hud2 = this.hud;
      Color color3 = color2;
      String str = "HudSelectedColor";
      ColorSetting colorSetting = new ColorSetting(this, str, color3, hud2);
      showCompass2 = this.showCompass;
      this.showCompass.getClass();
      BooleanValue booleanValue3 = showCompass2;
      SettingVisibilityCondition condition3 = () -> booleanValue3.getValue();
      this.hudSelectedColor = (ColorSetting)colorSetting.withVisibility(condition3);
      this.debug = new BooleanValue(this, "Debug", false);
      this.displacementTargetBinding = null;
      this.displacing = false;
      this.pendingForceAttack = false;
      this.timeHelper = new TimeHelper();
      this.hudBoxBackgroundColor = new Color(20, 23, 28, 235).getRGB();
      this.hudBoxBorderColor = new Color(48, 48, 48, 255).getRGB();
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
      this.displacementTargetBinding = null;
      this.displacing = false;
   }

   @Override
   public void updateEventSubscriptions() {
      this.onAttackEntity();
      this.onAttackEntity();
      int n = 100000;
      byte b = 40;
      this.toggleEventSubscription(b, n);
      int n2 = 100010;
      byte b2 = 7;
      this.toggleEventSubscription(b2, n2);
      this.onAttackEntity();
   }

   @Override
   public void onTick() {
      if (!this.displacing) {
         if (!mc.thePlayer.isSprinting()) {
            return;
         }

         EntityLivingBase entity = this.findTargetEntity();
         if (entity == null || (double)entity.hurtTime > this.maxHurtTime.getValue()) {
            return;
         }

         Vec3 predictedPosition = entity.getPredictedPosition();
         Float f;
         if (this.displacementTargetBinding != null) {
            f = ClientManagers.rotationController.getCurrentRotation().yaw + this.displacementTargetBinding.degreeOffset;
         } else {
            f = ((KBDisplacementMode)this.mode.getAbstractSubMode()).computeDisplacementYaw(entity, predictedPosition);
         }

         if (f == null) {
            return;
         }

         EntityPlayerSP thePlayer = mc.thePlayer;
         float f2 = f;
         double[] doubles = PlayerUtil.predictPlayerPosition(0, thePlayer, f2);
         Vec3 vec = new Vec3(doubles[0], doubles[1], doubles[2]);
         Vec3 vec2 = vec.addVector(0.0, (double)mc.thePlayer.getEyeHeight(), 0.0);
         Vec3 nearestPointOnEntityAt = RotationUtil.getNearestPointOnEntityAt(vec2, entity, predictedPosition);
         double d = nearestPointOnEntityAt.distanceTo(vec2);
         double effectiveAttackRange = this.getEffectiveAttackRange();
         if (d <= effectiveAttackRange) {
            this.savedRotation = ClientManagers.rotationController.getCurrentRotation();
            RotationController rotationController = ClientManagers.rotationController;
            Rotation rotation = RotationUtil.applyGcdFix(f, (float)((double)ClientManagers.rotationController.getCurrentRotation().pitch + this.pitchOffset.nextValue()), ClientManagers.rotationController.getPreviousRotation());
            byte b = -100;
            Rotation rotation2 = rotation;
            rotationController.requestRotation(rotation2, b, this);
            this.displacing = true;
            this.displacementTargetBinding = null;
            if (this.debug.getValue()) {
               StringBuilder sb = new StringBuilder().append("Displaced knockback by: ");
               float f3 = f;
               String str = sb.append(KnockbackTrajectoryUtil.calculateKnockbackAngleDelta(predictedPosition, f3)).toString();
               this.sendChatMessage2(str);
            }
         }
      } else {
         byte b2 = -100;
         Rotation savedRotation2 = this.savedRotation;
         ClientManagers.rotationController.requestRotation(savedRotation2, b2, this);
         this.displacing = false;
      }
   }

   @Override
   public void onClick(EventClick event) {
      int n = mc.gameSettings.keyBindAttack.getPressTime() + event.getLeftClicks();
      if (n > 0) {
         this.timeHelper.reset();
      }

      if (this.pendingForceAttack) {
         if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit instanceof EntityLivingBase && n == 0 && this.forceAttack.getValue()) {
            byte b = 1;
            event.addLeftClicks(b);
         }

         this.pendingForceAttack = false;
      }

      if (this.displacing) {
         this.pendingForceAttack = true;
      }
   }

   @Override
   public void onAttackEntity(EventAttackEntity event) {
   }

   @Override
   public void onKey2(EventKey event) {
      if (mc.theWorld != null && mc.thePlayer != null && event.getKeyCode() != 0) {
         for (KBDisplacementHotkeyGroup group : (Iterable<KBDisplacementHotkeyGroup>)(this.directionForceKeys.getEntries())) {
            if (event.getKeyCode() == group.keybind.getKeyCode()) {
               if (this.displacementTargetBinding != null && this.displacementTargetBinding.kbDisplacementHotkeyGroup == group) {
                  this.displacementTargetBinding = null;
               } else {
                  this.displacementTargetBinding = new DisplacementTargetBinding(group, (float)KBDisplacementHotkeyGroup.getDegreeRange(group).nextValue());
               }
               break;
            }
         }
      }
   }

   @Override
   public void onRender(float f, ScaledResolution sr) {
      if (this.showCompass.getValue()) {
         int n = (int)(32.0 + (double)(sr.getScaledWidth() - 64) * this.posX.getValue() / 100.0);
         int n2 = (int)(32.0 + (double)(sr.getScaledHeight() - 64) * this.posY.getValue() / 100.0);
         GlStateManager.pushMatrix();
         GlStateManager.translate((float)n, (float)n2, 0.0F);
         GlStateManager.scale(1.0 / (double)sr.getScaleFactor(), 1.0 / (double)sr.getScaleFactor(), 1.0);
         boolean flag = false;

         for (KBDisplacementHotkeyGroup group : (Iterable<KBDisplacementHotkeyGroup>)(this.directionForceKeys.getEntries())) {
            KeybindSetting keybind = group.keybind;
            RandomizedRangeSettings degreeRange = KBDisplacementHotkeyGroup.getDegreeRange(group);
            int keyCode = keybind.getKeyCode();
            if (keyCode > 0) {
               if (!flag) {
                  GlStateManager.enableBlend();
                  GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                  GlStateManager.disableTexture2D();
                  GlStateManager.disableAlpha();
                  RenderUtil.drawRoundedRectOutline(-64.0F, -64.0F, 64.0F, 64.0F, 64.0F, 1, 2, -2894893);
                  GlStateManager.enableAlpha();
                  GlStateManager.enableTexture2D();
                  flag = true;
               }

               float lastValue = (float)degreeRange.getLastValue();
               double d = Math.toRadians((double)lastValue);
               int n3 = (int)Math.round(64.0 * Math.sin(d));
               int n4 = (int)Math.round(-64.0 * Math.cos(d));
               boolean flag2 = this.displacementTargetBinding != null && this.displacementTargetBinding.degreeOffset == lastValue;
               RenderUtil.drawRoundedRect(
                  (float)(n3 - 18), (float)(n4 - 18), (float)(n3 + 18), (float)(n4 + 18), 6.0F, 1, this.hudBoxBackgroundColor
               );
               int n5;
               if (flag2) {
                  byte b = 0;
                  n5 = this.hudSelectedColor.getColorInt(b);
               } else {
                  n5 = this.hudBoxBorderColor;
               }

               int n6 = n5;
               RenderUtil.drawRoundedRectOutline((float)(n3 - 20), (float)(n4 - 20), (float)(n3 + 20), (float)(n4 + 20), 6.0F, 1, 4, n6);
               String str = KeyboardInputUtil.getKeyDisplayName(keyCode).toUpperCase();
               if (str.length() > 5) {
                  str = str.substring(0, 5);
               }

               int stringWidth = mc.fontRendererObj.getStringWidth(str);
               float f2 = stringWidth <= 16 ? 2.0F : 1.0F;
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.pushMatrix();
               GlStateManager.translate((float)n3, (float)n4, 0.0F);
               GlStateManager.scale(f2, f2, 1.0F);
               mc.fontRendererObj
                  .drawString(
                     str,
                     f2 == 1.0F ? (float)((int)((float)(-stringWidth) / 2.0F + 0.5F)) : (float)(-stringWidth) / 2.0F + 0.5F,
                     f2 == 1.0F ? -3.0F : -3.5F,
                     -788741,
                     false
                  );
               GlStateManager.popMatrix();
            }
         }

         GlStateManager.popMatrix();
         RenderUtil.resetGlColorState();
      }
   }

   private EntityLivingBase findTargetEntity() {
      if (this.onlyAtKillAuraTarget.getValue()) {
         return CombatTargetUtil.getActiveTarget();
      } else {
         long l = 250L;
         if (!this.timeHelper.reached2(l)) {
            return null;
         } else {
            Rotation currentRotation = ClientManagers.rotationController.getCurrentRotation();
            double d = 0.0;
            double d2 = 4.0;
            MovingObjectPosition hit = RayTraceUtil.rayTraceEntity(1.0F, currentRotation, d2, d);
            return hit != null && hit.entityHit instanceof EntityLivingBase ? CombatTargetUtil.getActiveTargetOrDefault((EntityLivingBase)hit.entityHit) : null;
         }
      }
   }

   private double getEffectiveAttackRange() {
      return ClientManagers.moduleManager.killAura.isToggled() && ClientManagers.moduleManager.killAura.target != null
         ? ClientManagers.moduleManager.killAura.currentRange
         : (ClientManagers.moduleManager.range.isCombatRangeActive() ? ClientManagers.moduleManager.range.getCurrentRange() : (mc.playerController.isInCreativeMode() ? 4.5 : 3.0));
   }

}
