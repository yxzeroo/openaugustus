package net.augustus.modules.legit;

import net.minecraft.util.MovingObjectPosition;
import net.augustus.Augustus;
import net.augustus.ClientManagers;
import net.augustus.events.EventLoadWorld;
import net.augustus.events.EventMouseMove;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.events.listeners.MouseMoveListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.LookDownDisableSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.TargetSelectionSettings;
import net.augustus.settings.WeaponFilterSettings;
import net.augustus.utils.TickCounter;
import net.augustus.utils.combat.EntityTargetFilter;
import net.augustus.utils.combat.EntityTargetSelector;
import net.augustus.utils.combat.TargetFilterPresets;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.math.RandomNumberGenerator;
import net.augustus.utils.rotation.Rotation;
import net.augustus.utils.rotation.RotationUtil;
import java.awt.Color;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.ToDoubleFunction;
import net.augustus.modules.Categorys;
import net.augustus.modules.EntityRotationModule;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class AimBot extends EntityRotationModule implements LoadWorldListener, TickListener, MouseMoveListener {
   public final StringValue preAimRange;
   public final StringValue ownYawMul;
   public final StringValue ownPitchMul;
   public final StringValue assistYawMul;
   public final StringValue assistPitchMul;
   public final StringValue yawFov;
   public final StringValue pitchFov;
   public final BooleanValue click;
   public final BooleanValue diggingCheck;
   private final StringValue aimTickDelayAfterDigging;
   private final TargetSelectionSettings targetSelectionSettings;
   private final BooleanValue selectTargetThroughWalls;
   private final WeaponFilterSettings weaponFilterSettings;
   private final LookDownDisableSettings lookDownDisableSettings;
   private final TickCounter tickCounter;
   public EntityLivingBase target;
   private long lastFrameTimeMs;
   private long lastTickTimeMs;
   private int tickDurationMs;
   private final double[] assistDeltaRemainder;
   private final double[] ownDeltaRemainder;
   private final double[] accumulatedMouseDelta;
   private final int[] appliedMouseDelta;
   private int[] targetMouseDelta;

   public AimBot() {
       super("AimBot", "Ziehlhilfe", new Color(66, 135, 245), Categorys.LEGIT);
      this.preAimRange = new StringValue(this, "PreAimRange", 5.0, 0.0, 10.0, 1);
      this.ownYawMul = new StringValue(this, "OwnYawMul", 1.0, 0.0, 1.0, 2);
      this.ownPitchMul = new StringValue(this, "OwnPitchMul", 1.0, 0.0, 1.0, 2);
      this.assistYawMul = new StringValue(this, "AssistYawMul", 0.5, 0.0, 1.0, 2);
      this.assistPitchMul = new StringValue(this, "AssistPitchMul", 0.5, 0.0, 1.0, 2);
      this.yawFov = (StringValue)(new StringValue(this, "YawFov", 45.0, 0.0, 180.0, 0)).markVisualOnly();
      this.pitchFov = (StringValue)(new StringValue(this, "PitchFov", 26.0, 0.0, 180.0, 0)).markVisualOnly();
      this.click = (BooleanValue)(new BooleanValue(this, "Click", true)).markVisualOnly();
      this.diggingCheck = (BooleanValue)(new BooleanValue(this, "DiggingCheck", true)).markVisualOnly();
      StringValue stringValue = new StringValue(this, "Aim tick delay after digging", 0.0, 0.0, 20.0, 0);
      BooleanValue diggingCheck2 = this.diggingCheck;
      this.diggingCheck.getClass();
      BooleanValue booleanValue = diggingCheck2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.aimTickDelayAfterDigging = (StringValue)stringValue.visibleWhen(condition).markVisualOnly();
      this.targetSelectionSettings = (new TargetSelectionSettings(this, TargetFilterPresets.createCombatFilter())).withSorting();
      this.selectTargetThroughWalls = new BooleanValue(this, "SelectTargetThroughWalls", true);
      this.weaponFilterSettings = new WeaponFilterSettings(this);
      this.lookDownDisableSettings = new LookDownDisableSettings(this);
      this.tickCounter = new TickCounter();
      this.target = null;
      this.lastFrameTimeMs = 0L;
      this.lastTickTimeMs = 0L;
      this.tickDurationMs = 0;
      this.assistDeltaRemainder = new double[2];
      this.ownDeltaRemainder = new double[2];
      this.accumulatedMouseDelta = new double[2];
      this.appliedMouseDelta = new int[2];
      this.targetMouseDelta = new int[2];
   }

   @Override
   public void onEnable() {
      boolean flag = true;
      this.moveFixInRotationModule.setValue(flag);
      BooleanValue booleanValue = this.moveFixInRotationModule;
      SettingVisibilityCondition condition = () -> false;
      booleanValue.withVisibility(condition);
      String str = "None";
      this.silentMoveFix.setSelectedValue(str);
      this.rotating = true;
      super.onEnable();
      this.target = null;
      this.lastFrameTimeMs = System.currentTimeMillis();
      this.lastTickTimeMs = System.currentTimeMillis();
      this.targetMouseDelta[0] = 0;
      this.targetMouseDelta[1] = 0;
      this.appliedMouseDelta[0] = this.appliedMouseDelta[1] = 0;
      this.accumulatedMouseDelta[0] = this.accumulatedMouseDelta[1] = 0.0;
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.target = null;
   }

   @Override
   public void updateEventSubscriptions() {
      super.updateEventSubscriptions();
      byte b = 40;
      this.toggleEventSubscription(b);
      byte b2 = 19;
      this.toggleEventSubscription(b2);
      byte b3 = 17;
      this.toggleEventSubscription(b3);
   }

   @Override
   public void onMouseMove(EventMouseMove event) {
      if (ClientManagers.moduleManager.freelook.isToggled()) {
         byte b = 0;
         event.setDeltaX(b);
         byte b2 = 0;
         event.setDeltaY(b2);
      }

      if (mc.thePlayer != null
         && !mc.thePlayer.isDead
         && mc.thePlayer.ticksExisted > 20
         && this.target != null
         && mc.isNotInGui()
         && (!this.click.getValue() || mc.gameSettings.keyBindAttack.isKeyDown())) {
         AutoRod autoRod = ClientManagers.moduleManager.autoRod;
         if (!ClientManagers.rotationController.isOwnedBy(autoRod) && this.passesDiggingCheck()) {
            int n = (int)(System.currentTimeMillis() - this.lastFrameTimeMs);
            EntityLivingBase target2 = this.target;
            float wrappedYaw = this.getWrappedYaw();
            Rotation rotation = new Rotation(wrappedYaw, this.getPitch());
            float partialTicks = event.getPartialTicks();
            float f = 180.0F;
            float f2 = 180.0F;
            float f3 = partialTicks;
            Rotation rotation2 = rotation;
            EntityLivingBase entity = target2;
            Rotation rotation3 = this.calculateSmoothRotationToEntity(entity, rotation2, f3, f2, f);
            if (rotation3 == null) {
               return;
            }

            float yaw = rotation3.yaw;
            float pitch = rotation3.pitch;
            partialTicks = this.getWrappedYaw();
            int[] ints = RotationUtil.computeMouseDeltas(yaw, pitch, partialTicks, this.getPitch());
            double d = (double)this.targetMouseDelta[0] / (double)this.tickDurationMs * (double)n;
            double d2 = (double)this.targetMouseDelta[1] / (double)this.tickDurationMs * (double)n;
            this.accumulatedMouseDelta[0] = this.accumulatedMouseDelta[0] + d;
            this.accumulatedMouseDelta[1] = this.accumulatedMouseDelta[1] + d2;
            int n2 = (int)Math.round(this.accumulatedMouseDelta[0] - (double)this.appliedMouseDelta[0]);
            int n3 = (int)Math.round(this.accumulatedMouseDelta[1] - (double)this.appliedMouseDelta[1]);
            this.appliedMouseDelta[0] = this.appliedMouseDelta[0] + n2;
            this.appliedMouseDelta[1] = this.appliedMouseDelta[1] + n3;
            if (event.getDeltaX() != 0) {
               int n4 = ints[0];
               if (n4 < 0) {
                  if (n4 - event.getDeltaX() <= n4) {
                     double d3 = (double)event.getDeltaX() * this.assistYawMul.getValue();
                     double d4 = d3 + this.assistDeltaRemainder[0];
                     int n5 = (int)Math.round(d4);
                     event.setDeltaX(n5);
                     this.assistDeltaRemainder[0] = d4 - (double)n5;
                  }
               } else if (n4 - event.getDeltaX() >= n4) {
                  double d5 = (double)event.getDeltaX() * this.assistYawMul.getValue();
                  double d6 = d5 + this.assistDeltaRemainder[0];
                  int n6 = (int)Math.round(d6);
                  event.setDeltaX(n6);
                  this.assistDeltaRemainder[0] = d6 - (double)n6;
               }
            }

            if (event.getDeltaY() != 0) {
               int n7 = ints[1];
               if (n7 < 0) {
                  if (n7 - event.getDeltaY() <= n7) {
                     double d7 = (double)event.getDeltaY() * this.assistPitchMul.getValue();
                     double d8 = d7 + this.assistDeltaRemainder[1];
                     int n8 = (int)Math.round(d8);
                     event.setDeltaY(n8);
                     this.assistDeltaRemainder[1] = d8 - (double)n8;
                  }
               } else if (n7 - event.getDeltaY() >= n7) {
                  double d9 = (double)event.getDeltaY() * this.assistPitchMul.getValue();
                  double d10 = d9 + this.assistDeltaRemainder[1];
                  int n9 = (int)Math.round(d10);
                  event.setDeltaY(n9);
                  this.assistDeltaRemainder[1] = d10 - (double)n9;
               }
            }

            double d11 = (double)event.getDeltaX() * this.ownYawMul.getValue();
            double d12 = d11 + this.ownDeltaRemainder[0];
            int n10 = (int)Math.round(d12);
            event.setDeltaX(n10);
            this.ownDeltaRemainder[0] = d12 - (double)n10;
            double d13 = (double)event.getDeltaY() * this.ownPitchMul.getValue();
            double d14 = d13 + this.ownDeltaRemainder[1];
            int n11 = (int)Math.round(d14);
            event.setDeltaY(n11);
            this.ownDeltaRemainder[1] = d14 - (double)n11;
            int n12 = n2 + event.getDeltaX();
            event.setDeltaX(n12);
            int n13 = n3 + event.getDeltaY();
            event.setDeltaY(n13);
         }
      }

      this.lastFrameTimeMs = System.currentTimeMillis();
   }

   @Override
   public void onTick3() {
      this.tickDurationMs = (int)Math.abs(this.lastTickTimeMs - System.currentTimeMillis());
      this.lastTickTimeMs = System.currentTimeMillis();
      this.appliedMouseDelta[0] = this.appliedMouseDelta[1] = 0;
      this.accumulatedMouseDelta[0] = this.accumulatedMouseDelta[1] = 0.0;
      this.firstRotationTick = true;
   }

   @Override
   public void onTick5() {
      this.tickCounter.increment();
      EntityLivingBase target2 = this.target;
      this.targetSelectionSettings.setCurrentTarget(target2);
      this.target = null;
      if (mc.isNotInGui() && (!this.click.getValue() || mc.gameSettings.keyBindAttack.isKeyDown()) && this.weaponFilterSettings.isHeldItemAllowed() && !this.lookDownDisableSettings.shouldDisable()) {
         this.target = this.findTarget();
         if (this.target != null) {
            RandomNumberGenerator instance = DefaultRandomGenerator.instance;
            double minValue = this.yawSpeedInRotationModule.getMinValue();
            double maxValue = this.yawSpeedInRotationModule.getMaxValue();
            double d = minValue;
            float f = instance.nextSecureFloat(d, maxValue) / 2.0F;
            instance = DefaultRandomGenerator.instance;
            double minValue2 = this.pitchSpeedInRotationModule.getMinValue();
            double maxValue2 = this.pitchSpeedInRotationModule.getMaxValue();
            double d2 = minValue2;
            float f2 = instance.nextSecureFloat(d2, maxValue2) / 2.0F;
            EntityLivingBase target3 = this.target;
            float wrappedYaw = this.getWrappedYaw();
            Rotation rotation = new Rotation(wrappedYaw, this.getPitch());
            float f3 = 1.0F;
            Rotation rotation2 = rotation;
            EntityLivingBase entity = target3;
            Rotation rotation3 = this.calculateSmoothRotationToEntity(entity, rotation2, f3, f, f2);
            float yaw = rotation3.yaw;
            float pitch = rotation3.pitch;
            wrappedYaw = this.getWrappedYaw();
            this.targetMouseDelta = RotationUtil.computeMouseDeltas(yaw, pitch, wrappedYaw, this.getPitch());
            System.out.println(Arrays.toString(this.targetMouseDelta));
            return;
         }
      }

      this.targetMouseDelta[0] = 0;
      this.targetMouseDelta[1] = 1;
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      this.target = null;
      Object obj = null;
      this.targetSelectionSettings.setCurrentTarget((Entity)obj);
   }

   private float getWrappedYaw() {
      float realYaw = Augustus.getInstance().getYawPitchHelper().realYaw;

      while (mc.thePlayer.rotationYaw - realYaw >= 180.0F) {
         realYaw += 360.0F;
      }

      while (mc.thePlayer.rotationYaw - realYaw < -180.0F) {
         realYaw -= 360.0F;
      }

      return realYaw;
   }

   private float getPitch() {
      return Augustus.getInstance().getYawPitchHelper().realPitch;
   }

   private boolean passesDiggingCheck() {
      boolean flag = this.diggingCheck.getValue() && mc.gameSettings.keyBindAttack.isKeyDown() && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK;
      if (flag) {
         this.tickCounter.reset();
      }

      TickCounter tickCounter2 = this.tickCounter;
      double value = this.aimTickDelayAfterDigging.getValue();
      return tickCounter2.hasReached(value) && !flag;
   }

   private EntityLivingBase findTarget() {
      EntityTargetSelector entityTargetSelector = EntityTargetSelector.of(mc.theWorld.loadedEntityList);
      EntityTargetFilter targetFilter = this.targetSelectionSettings.getTargetFilter();
      EntityTargetFilter entityTargetFilter = targetFilter;
      entityTargetSelector = entityTargetSelector.withTargetFilter(entityTargetFilter);
      boolean value = this.selectTargetThroughWalls.getValue();
      entityTargetSelector = entityTargetSelector.throughWalls(value);
      double value2 = this.preAimRange.getValue();
      entityTargetSelector = entityTargetSelector.maxDistance(value2);
      double value3 = this.yawFov.getValue();
      double value4 = this.pitchFov.getValue();
      double d = value3;
      entityTargetSelector = entityTargetSelector.withinFov(d, value4).filterFocus();
      TargetSelectionSettings targetSelectionSettings2 = this.targetSelectionSettings;
      Rotation rotation = new Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
      double d2 = ClientManagers.moduleManager.range.isCombatRangeActive() ? ClientManagers.moduleManager.range.getCurrentRange() : 3.0;
      Rotation rotation2 = rotation;
      ToDoubleFunction toDoubleFunction = targetSelectionSettings2.createTargetComparatorKey(rotation2, d2);
      Optional optional = entityTargetSelector.sortBy(toDoubleFunction).findBest();
      return optional.isPresent() && optional.get() instanceof EntityLivingBase ? (EntityLivingBase)optional.get() : null;
   }

}
