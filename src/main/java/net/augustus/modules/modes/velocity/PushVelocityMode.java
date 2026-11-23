package net.augustus.modules.modes.velocity;

import net.augustus.events.listeners.UpdateListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.MoveUtil;
import java.util.HashMap;
import java.util.function.BiFunction;
import net.augustus.modules.combat.Velocity;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;

public class PushVelocityMode extends VelocityMode implements UpdateListener {
   private final BooleansSetting pushMode;
   public final StringValue push;
   public final StringValue pushStart;
   public final StringValue pushEnd;
   public final BooleanValue onGround;
   private final HashMap hurtTimeEntries;

   public PushVelocityMode(Velocity velocity, DoubleValue doubleValue) {
       super(velocity, doubleValue, "Push");
      BooleansSetting booleansSetting = new BooleansSetting(this.parentModule, "PushMode", "Simple", new String[]{"Simple", "Expert"}, this.modeSetting);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.pushMode = (BooleansSetting)booleansSetting.withVisibility(condition);
      StringValue stringValue = new StringValue(this.parentModule, "Push", 1.1, 0.01, 20.0, 2, this.modeSetting);
      String str = "PushMotion";
      stringValue = stringValue.named(str);
      SettingVisibilityCondition condition2 = () -> this.isPushMotionVisible();
      this.push = stringValue.visibleWhen(condition2);
      stringValue = new StringValue(this.parentModule, "PushStart", 9.0, 1.0, 10.0, 0, this.modeSetting);
      SettingVisibilityCondition condition3 = () -> this.isPushStartVisible();
      this.pushStart = stringValue.visibleWhen(condition3);
      stringValue = new StringValue(this.parentModule, "PushEnd", 2.0, 1.0, 10.0, 0, this.modeSetting);
      SettingVisibilityCondition condition4 = () -> {
         return this.isSelected() && this.isSimpleMode();
      };
      this.pushEnd = stringValue.visibleWhen(condition4);
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "OnGround", false, this.modeSetting);
      SettingVisibilityCondition condition5 = () -> this.isOnGroundVisible();
      this.onGround = (BooleanValue)booleanValue.withVisibility(condition5);
      this.hurtTimeEntries = new HashMap();

      for (int i = 10; i > 0; i--) {
         DoubleValue doubleValue2 = new DoubleValue(velocity, "Hurttime " + i, doubleValue);
         SettingVisibilityCondition condition6 = () -> this.isExpertGroupVisible();
         DoubleValue doubleValue3 = doubleValue2.visibleWhen(condition6);
         StringValue stringValue2 = new StringValue(velocity, "PushMotion" + i, 0.0, 0.0, 20.0, 2, doubleValue3);
         String str2 = "PushMotion";
         StringValue stringValue3 = stringValue2.named(str2);
         SettingVisibilityCondition condition7 = () -> {
            return this.isSelected() && !this.isSimpleMode();
         };
         StringValue stringValue4 = stringValue3.visibleWhen(condition7);
         BooleanValue booleanValue2 = new BooleanValue(velocity, "PushOnGround" + i, false, doubleValue3);
         String str3 = "PushOnGround";
         BooleanValue booleanValue3 = (BooleanValue)booleanValue2.withDisplayName(str3);
         SettingVisibilityCondition condition8 = () -> {
            return this.isSelected() && !this.isSimpleMode();
         };
         BooleanValue booleanValue4 = (BooleanValue)booleanValue3.withVisibility(condition8);
         this.hurtTimeEntries.put(i, new PushVelocityHurtTimeEntry(this, stringValue4, booleanValue4));
      }
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 43;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new PushVelocityMode((Velocity)obj, (DoubleValue)obj2);
   }

   @Override
   public void onUpdate2() {
      if (this.canApplyVelocity()) {
         if (this.isSimpleMode()) {
            if ((double)mc.thePlayer.hurtTime <= Math.max(this.pushStart.getValue(), this.pushEnd.getValue())
               && (double)mc.thePlayer.hurtTime >= Math.min(this.pushStart.getValue(), this.pushEnd.getValue())) {
               MoveUtil.addMotion(this.push.getValue() / 100.0, true);
               if (this.onGround.getValue()) {
                  mc.thePlayer.onGround = true;
               }
            }
         } else {
            PushVelocityHurtTimeEntry entry = (PushVelocityHurtTimeEntry)this.hurtTimeEntries.get(mc.thePlayer.hurtTime);
            if (entry != null) {
               MoveUtil.addMotion(entry.getPushMotionFactor(), true);
               if (entry.shouldSpoofOnGround()) {
                  mc.thePlayer.onGround = true;
               }
            }
         }
      }
   }

   private boolean isSimpleMode() {
      return this.pushMode.getSelectedValue().equals("Simple");
   }

   private boolean isExpertGroupVisible() {
      return this.isSelected() && !this.isSimpleMode();
   }

   private boolean isOnGroundVisible() {
      return this.isSelected() && this.isSimpleMode();
   }

   private boolean isPushStartVisible() {
      return this.isSelected() && this.isSimpleMode();
   }

   private boolean isPushMotionVisible() {
      return this.isSelected() && this.isSimpleMode();
   }

}
