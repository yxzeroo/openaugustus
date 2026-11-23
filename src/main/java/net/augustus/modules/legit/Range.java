package net.augustus.modules.legit;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.utils.ActionRateAccumulator;
import net.augustus.ClientManagers;
import net.augustus.settings.RangeSetting;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.events.EventLoadWorld;
import net.augustus.settings.options.RangeBuildSlotOption;
import net.augustus.events.EventAttackEntity;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.listeners.AttackEntityListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class Range extends Module implements LoadWorldListener, TickListener, AttackEntityListener {
   private final DoubleValue combat;
   private final BooleansSetting rangeMode;
   private final StringValue rangeInternal;
   private final RangeSetting baseRange;
   private final StringValue chanceOfExtendedRange;
   private final BooleanValue onlyWhileSprinting;
   public final BooleanValue throughWalls;
   private final DoubleValue interaction;
   public final StringValue buildRange;
   public final MultiOptionSetting slotsToApplyBuildRange;
   private double currentRange;
   private boolean useExtendedRange;
   private EntityLivingBase lastAttackedTarget;
   private final TimeHelper timeHelper;
   private final ActionRateAccumulator actionRateAccumulator;

   public Range() {
       super("Range", "Distanz", new Color(65, 140, 35), Categorys.LEGIT);
      this.combat = new DoubleValue(this, "Combat", true);
      this.rangeMode = new BooleansSetting(this, "RangeMode", "Default", new String[]{"Default", "Dual"}, this.combat);
      StringValue stringValue = new StringValue(this, "RangeInternal", 3.0, 3.0, 4.5, 2, this.combat);
      String str = "Range";
      StringValue stringValue2 = stringValue.named(str);
      SettingVisibilityCondition condition = () -> {
         return this.rangeMode.getSelectedValue().equals("Default");
      };
      this.rangeInternal = stringValue2.visibleWhen(condition);
      RangeSetting rangeSetting = new RangeSetting(this, "BaseRange", "ExtendedRange", 3.0, 3.7, 3.0, 6.0, 2, this.combat);
      SettingVisibilityCondition condition2 = () -> this.isBaseRangeVisible();
      this.baseRange = rangeSetting.visibleWhen(condition2);
      StringValue stringValue3 = new StringValue(this, "Chance of Extended Range", 50.0, 0.0, 100.0, 0, this.combat);
      SettingVisibilityCondition condition3 = () -> this.isChanceOfExtendedRangeVisible();
      this.chanceOfExtendedRange = stringValue3.visibleWhen(condition3);
      this.onlyWhileSprinting = new BooleanValue(this, "OnlyWhileSprinting", false, this.combat);
      this.throughWalls = new BooleanValue(this, "ThroughWalls", false, this.combat);
      this.interaction = new DoubleValue(this, "Interaction", true);
      this.buildRange = new StringValue(this, "BuildRange", 4.5, 4.5, 6.0, 2, this.interaction);
      this.slotsToApplyBuildRange = (MultiOptionSetting)(new MultiOptionSetting(this, "SlotsToApplyBuildRange", RangeBuildSlotOption.values(), this.interaction)).markVisualOnly();
      this.currentRange = 3.0;
      this.useExtendedRange = false;
      this.lastAttackedTarget = null;
      this.timeHelper = new TimeHelper();
      this.actionRateAccumulator = new ActionRateAccumulator(100, 0.0);
   }

   @Override
   public void onEnable() {
      this.currentRange = 3.0;
      this.useExtendedRange = false;
   }

   @Override
   public void onDisable() {
      this.lastAttackedTarget = null;
   }

   @Override
   public void updateEventSubscriptions() {
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
   }

   @Override
   public void onTick2() {
      String str = "" + this.currentRange;
      this.setSuffix(str);
      if (this.rangeMode.getSelectedValue().equals("Default")) {
         this.currentRange = this.rangeInternal.getValue();
      } else {
         long l = 3000L;
         if (this.timeHelper.reached2(l)) {
            this.lastAttackedTarget = null;
         }

         if (this.lastAttackedTarget != null) {
            double d;
            if (this.useExtendedRange) {
               d = this.baseRange.getMaxValue();
            } else {
               d = this.baseRange.getMinValue();
            }

            this.currentRange = d;
            if (this.lastAttackedTarget.hurtTime == 9) {
               int n = MathHelper.clamp_int((int)this.chanceOfExtendedRange.getValue(), 0, 100);
               this.useExtendedRange = this.actionRateAccumulator.shouldTrigger(n);
            }
         }
      }
   }

   public boolean isCombatRangeActive() {
      return ClientManagers.moduleManager.range.isToggled() && this.isLicenseValid() && (!ClientManagers.moduleManager.range.onlyWhileSprinting.getValue() || mc.thePlayer.isSprinting());
   }

   public double getCurrentRange() {
      return this.currentRange;
   }

   @Override
   public void onAttackEntity(EventAttackEntity event) {
      if (event.getTarget() instanceof EntityLivingBase) {
         this.lastAttackedTarget = (EntityLivingBase)event.getTarget();
         this.timeHelper.reset();
      }
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      this.lastAttackedTarget = null;
   }

   public double getBuildRange(double d) {
      return this.isToggled() && RangeBuildSlotOption.isCurrentSlotAllowed() ? Math.max(this.buildRange.getValue(), 4.5) : d;
   }

   private boolean isChanceOfExtendedRangeVisible() {
      return this.rangeMode.getSelectedValue().equals("Dual");
   }

   private boolean isBaseRangeVisible() {
      return this.rangeMode.getSelectedValue().equals("Dual");
   }

   private boolean isLicenseValid() {
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
