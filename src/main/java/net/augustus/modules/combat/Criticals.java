package net.augustus.modules.combat;

import java.awt.Color;
import java.util.function.BiFunction;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.settings.ModeSetting;
import net.augustus.modules.modes.criticals.PacketCriticalsMode;
import net.augustus.modules.modes.criticals.PacketEditCriticalsMode;
import net.augustus.modules.modes.criticals.LagCriticalsMode;
import net.augustus.modules.modes.criticals.NoGroundCriticalsMode;
import net.augustus.modules.modes.criticals.IntaveLowHopCriticalsMode;
import net.augustus.modules.modes.criticals.CustomPacketCriticalsMode;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;

public class Criticals extends Module implements TickListener {
   public final ModeSetting mode;
   public final BooleanValue onlyWhileJumpKeyPressed;
   public final BooleanValue onlyWhileKillAuraTarget;
   public final BooleanValue criticalsInAir;
   public final BooleanValue criticalsOnGround;

   public Criticals() {
       super("Criticals", "KritischeTreffer", Color.RED, Categorys.COMBAT);
      BiFunction[] biFunctions = new BiFunction[]{
         NoGroundCriticalsMode.factory(),
         PacketCriticalsMode.factory(),
         CustomPacketCriticalsMode.factory(),
         PacketEditCriticalsMode.factory(),
         IntaveLowHopCriticalsMode.factory(),
         LagCriticalsMode.factory()
      };
      this.mode = new ModeSetting(this, "Mode", "NoGround", biFunctions);
      BooleanValue booleanValue = new BooleanValue(this, "OnlyWhileJumpKeyPressed", false);
      SettingVisibilityCondition condition = () -> {
         return !this.mode.getSelectedValue().equals("Lag");
      };
      this.onlyWhileJumpKeyPressed = (BooleanValue)booleanValue.withVisibility(condition);
      BooleanValue booleanValue2 = new BooleanValue(this, "onlyWhileKillAuraTarget", false);
      SettingVisibilityCondition condition2 = () -> this.isKillAuraTargetSettingVisible();
      this.onlyWhileKillAuraTarget = (BooleanValue)booleanValue2.withVisibility(condition2);
      BooleanValue booleanValue3 = new BooleanValue(this, "CriticalsInAir", true);
      SettingVisibilityCondition condition3 = () -> this.isInAirSettingVisible();
      this.criticalsInAir = (BooleanValue)booleanValue3.withVisibility(condition3);
      BooleanValue booleanValue4 = new BooleanValue(this, "CriticalsOnGround", true);
      SettingVisibilityCondition condition4 = () -> this.isOnGroundSettingVisible();
      this.criticalsOnGround = (BooleanValue)booleanValue4.withVisibility(condition4);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onTick() {
      String selectedValue = this.mode.getSelectedValue();
      this.setSuffix(selectedValue);
   }

   private boolean isOnGroundSettingVisible() {
      return !this.mode.getSelectedValue().equals("Lag");
   }

   private boolean isInAirSettingVisible() {
      return !this.mode.getSelectedValue().equals("Lag");
   }

   private boolean isKillAuraTargetSettingVisible() {
      return !this.mode.getSelectedValue().equals("Lag");
   }


}
