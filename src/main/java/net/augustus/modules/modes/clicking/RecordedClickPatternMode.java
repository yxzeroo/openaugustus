package net.augustus.modules.modes.clicking;

import net.augustus.ClientManagers;
import net.augustus.events.EventClick;
import net.augustus.events.EventMouseMove;
import net.augustus.events.listeners.MouseMoveListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.modules.combat.helper.ClickModuleContext;
import net.augustus.settings.SettingChangeListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.TuplePair;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.rotation.RotationUtil;
import java.util.function.Consumer;
import net.augustus.modules.Module;
import net.augustus.modules.combat.KillAura;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;

public class RecordedClickPatternMode extends AbstractClickPatternMode implements MouseMoveListener, TickListener {
   private final BooleansSetting recordedClickPattern;
   private final BooleansSetting recordedMode;
   private final StringValue multiplier;
   private final StringValue recordedAirMultiplier;
   private final BooleanValue playRecordedMouseMovements;
   private final ClickPatternManager clickPatternManager;
   private long nextClickTimeMs;
   private long releaseTimeMs;
   private long virtualTickTimeMs;
   private ClickPattern activePattern;
   private long lastMouseMoveTimeMs;
   private int[] targetMouseDelta;
   private float[] lastPatternRotation;
   private double[] accumulatedMouseDelta;
   private int[] appliedMouseDelta;
   private int tickDurationMs;

   public RecordedClickPatternMode(Module module, DoubleValue doubleValue) {
       super(module, doubleValue, "Recorded");
      this.nextClickTimeMs = 0L;
      this.releaseTimeMs = 0L;
      this.virtualTickTimeMs = 0L;
      this.activePattern = null;
      this.lastMouseMoveTimeMs = 0L;
      this.targetMouseDelta = new int[2];
      this.lastPatternRotation = new float[2];
      this.accumulatedMouseDelta = new double[2];
      this.appliedMouseDelta = new int[2];
      this.tickDurationMs = 0;
      BooleansSetting booleansSetting = new BooleansSetting(module, "RecordedClickPattern", "", new String[0], doubleValue);
      String str = "ClickPattern";
      BooleansSetting booleansSetting2 = (BooleansSetting)booleansSetting.withDisplayName(str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.recordedClickPattern = (BooleansSetting)booleansSetting2.withVisibility(condition);
      BooleansSetting booleansSetting3 = new BooleansSetting(module, "RecordedMode", "TimeMillis", new String[]{"TimeMillis", "Ticks"}, doubleValue);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.recordedMode = (BooleansSetting)booleansSetting3.withVisibility(condition2);
      StringValue stringValue = new StringValue(module, "Multiplier", 1.0, 0.0, 5.0, 2, doubleValue);
      SettingVisibilityCondition condition3 = () -> this.isSelected();
      this.multiplier = stringValue.visibleWhen(condition3);
      StringValue stringValue2 = new StringValue(module, "RecordedAirMultiplier", 0.4, 0.0, 1.0, 2, doubleValue);
      String str2 = "AirMultiplier";
      StringValue stringValue3 = stringValue2.named(str2);
      SettingVisibilityCondition condition4 = () -> this.isSelected();
      this.recordedAirMultiplier = stringValue3.visibleWhen(condition4);
      BooleanValue booleanValue = new BooleanValue(module, "PlayRecordedMouseMovements", false, doubleValue);
      SettingVisibilityCondition condition5 = () -> {
         return this.isSelected() && !(this.getParentModule() instanceof KillAura);
      };
      this.playRecordedMouseMovements = (BooleanValue)booleanValue.withVisibility(condition5);
      ClickPatternManagerBuilder builder = ClickPatternManager.builder();
      BooleansSetting recordedClickPattern2 = this.recordedClickPattern;
      Consumer consumer = obj -> this.activePattern = (ClickPattern)obj;
      BooleansSetting booleansSetting4 = recordedClickPattern2;
      this.clickPatternManager = builder.withSelectorAndCallback(booleansSetting4, consumer).build();
      BooleansSetting recordedClickPattern3 = this.recordedClickPattern;
      SettingChangeListener listener = () -> this.updateActivePattern();
      SettingChangeListener listener2 = listener;
      recordedClickPattern3.withChangeListener(listener2);
      this.clickPatternManager.startWatchingAndLoad();
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 19;
      this.toggleEventSubscription(b);
      byte b2 = 40;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onEnable() {
      this.nextClickTimeMs = System.currentTimeMillis();
      this.lastMouseMoveTimeMs = System.currentTimeMillis();
      this.resetTiming();
   }

   @Override
   public void onDisable() {
      ClickModuleContext parentModule2 = (ClickModuleContext)this.parentModule;
      boolean flag = false;
      parentModule2.setHoldingAttack(flag);
   }

   @Override
   public int computeClicks(EventClick event) {
      if (this.activePattern == null) {
         return 0;
      } else {
         int n = 0;
         if (!this.recordedMode.getSelectedValue().equals("Ticks")) {
            long l = System.currentTimeMillis();

            while (l >= this.nextClickTimeMs) {
               n++;
               TuplePair tuplePair = this.nextTotalAndPressedDelay();
               int first = (Integer)tuplePair.getFirst();
               this.releaseTimeMs = this.nextClickTimeMs;
               this.releaseTimeMs = this.releaseTimeMs + (long)((Integer)tuplePair.getSecond()).intValue();
               this.nextClickTimeMs += (long)first;
            }

            ClickModuleContext parentModule2 = (ClickModuleContext)this.parentModule;
            boolean flag = this.releaseTimeMs > l;
            parentModule2.setHoldingAttack(flag);
         } else {
            while (this.virtualTickTimeMs >= this.nextClickTimeMs) {
               n++;
               TuplePair tuplePair2 = this.nextTotalAndPressedDelay();
               int first2 = (Integer)tuplePair2.getFirst();
               this.releaseTimeMs = this.nextClickTimeMs;
               this.releaseTimeMs = this.releaseTimeMs + (long)((Integer)tuplePair2.getSecond()).intValue();
               this.nextClickTimeMs += (long)first2;
            }

            ClickModuleContext parentModule3 = (ClickModuleContext)this.parentModule;
            boolean flag2 = this.releaseTimeMs > this.virtualTickTimeMs;
            parentModule3.setHoldingAttack(flag2);
            this.virtualTickTimeMs += 50L;
         }

         this.activePattern.advanceRotationIndex();
         float[] currentRotation = this.activePattern.getCurrentRotation();
         this.targetMouseDelta = RotationUtil.computeMouseDeltas(currentRotation[0], currentRotation[1], this.lastPatternRotation[0], this.lastPatternRotation[1]);
         this.lastPatternRotation[0] = currentRotation[0];
         this.lastPatternRotation[1] = currentRotation[1];
         n = ((ClickModuleContext)this.parentModule).applyHitReduction(n);
         if (ClientManagers.moduleManager.smartClicking.isSuppressingClicks() || n != n) {
            n = 0;
            ClickModuleContext parentModule4 = (ClickModuleContext)this.parentModule;
            boolean flag3 = false;
            parentModule4.setHoldingAttack(flag3);
         }

         return n;
      }
   }

   @Override
   public void onTick2() {
   }

   @Override
   public void onTick3() {
      this.targetMouseDelta[0] = this.targetMouseDelta[1] = 0;
      this.appliedMouseDelta[0] = this.appliedMouseDelta[1] = 0;
      this.accumulatedMouseDelta[0] = this.accumulatedMouseDelta[1] = 0.0;
   }

   @Override
   public void onMouseMove(EventMouseMove event) {
      if (this.isEnabled() && this.playRecordedMouseMovements.getValue()) {
         int n = (int)(System.currentTimeMillis() - this.lastMouseMoveTimeMs);
         double d = (double)this.targetMouseDelta[0] / (double)this.tickDurationMs * (double)n;
         double d2 = (double)this.targetMouseDelta[1] / (double)this.tickDurationMs * (double)n;
         this.accumulatedMouseDelta[0] = this.accumulatedMouseDelta[0] + d;
         this.accumulatedMouseDelta[1] = this.accumulatedMouseDelta[1] + d2;
         int n2 = (int)Math.round(this.accumulatedMouseDelta[0] - (double)this.appliedMouseDelta[0]);
         int n3 = (int)Math.round(this.accumulatedMouseDelta[1] - (double)this.appliedMouseDelta[1]);
         this.appliedMouseDelta[0] = this.appliedMouseDelta[0] + n2;
         this.appliedMouseDelta[1] = this.appliedMouseDelta[1] + n3;
         int n4 = event.getDeltaX() + n2;
         event.setDeltaX(n4);
         int n5 = event.getDeltaY() + n3;
         event.setDeltaY(n5);
         this.lastMouseMoveTimeMs = System.currentTimeMillis();
      }
   }

   private TuplePair nextTotalAndPressedDelay() {
      int n = this.activePattern.nextDelay();
      int n2 = this.activePattern.nextDelay();
      if (this.isNotLookingAtEntity()) {
         n = (int)((double)n / this.recordedAirMultiplier.getValue());
         n2 = (int)((double)n2 / this.recordedAirMultiplier.getValue());
      }

      n = (int)Math.min((double)n * (1.0 / this.multiplier.getValue()), 1000.0);
      n2 = (int)Math.min((double)n2 * (1.0 / this.multiplier.getValue()), 1000.0);
      return new TuplePair(n + n2, n);
   }

   public void resetTiming() {
      long l = System.currentTimeMillis();
      byte b = 45;
      byte b2 = 0;
      this.nextClickTimeMs = l - (long)DefaultRandomGenerator.instance.nextInt2(b2, b);
      this.virtualTickTimeMs = l;
   }

   @Override
   public boolean simulatesMousePress() {
      return true;
   }

   public void setTickDurationMs(int n) {
      this.tickDurationMs = n;
   }

   public ClickPattern getActivePattern() {
      return this.activePattern;
   }

   private void updateActivePattern() {
      ClickPatternManager clickPatternManager2 = this.clickPatternManager;
      String selectedValue = this.recordedClickPattern.getSelectedValue();
      this.activePattern = clickPatternManager2.getPattern(selectedValue);
   }

}
