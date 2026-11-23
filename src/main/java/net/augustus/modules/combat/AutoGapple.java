package net.augustus.modules.combat;

import java.awt.Color;
import java.util.OptionalInt;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.ClickListener;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.ClientManagers;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.events.EventClick;
import net.augustus.utils.world.BlockUtil;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.utils.combat.CombatTargetUtil;
import net.augustus.events.EventMoveInput;
import net.augustus.utils.player.ContainerSlotQuery;
import net.augustus.utils.player.SilentSlotManager;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.BooleanValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class AutoGapple extends Module implements ClickListener, PacketListener, SilentSlotListener, MoveInputListener {
   private final RandomizedRangeSettings health;
   private final BooleanValue considerTargetHealth;
   private final BooleanValue notWhileKillAura;
   private final BooleanValue whileRegeneration;
   private final BooleanValue jumpAndPressS;
   private final BooleanValue silentSlotSwitching;
   private final TimeHelper timeHelper;
   private int gappleSlot;

   public AutoGapple() {
       super("AutoGapple", "AutoApfel", new Color(100, 217, 203), Categorys.COMBAT);
      this.health = (new RandomizedRangeSettings(this, "Health", 13.0, 15.0, 0.0, 20.0, 0)).withoutNotInARow();
      this.considerTargetHealth = new BooleanValue(this, "ConsiderTargetHealth", false);
      this.notWhileKillAura = new BooleanValue(this, "NotWhileKillAura", false);
      this.whileRegeneration = new BooleanValue(this, "WhileRegeneration", false);
      this.jumpAndPressS = new BooleanValue(this, "JumpAndPressS", false);
      this.silentSlotSwitching = new BooleanValue(this, "SilentSlotSwitching", true);
      this.timeHelper = new TimeHelper();
      this.gappleSlot = -1;
      this.health.nextValue();
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      this.toggleEventSubscription((byte)7);
      this.toggleEventSubscription((byte)26);
      this.toggleEventSubscription((byte)34);
      this.toggleEventSubscription((byte)21, (byte)100);
   }

   public void onMoveInput3(EventMoveInput event) {
      if (mc.thePlayer.inventory.currentItem == this.gappleSlot || this.gappleSlot <= -1) {
         if (this.gappleSlot >= 0 && mc.isNotInGui() && mc.getTicksWithoutScreen() > 3 && this.jumpAndPressS.getValue()) {
            event.setMoveForward(-1.0F);
            event.setJump(true);
         }
      }
   }

   public void onClick(EventClick event) {
      if (mc.thePlayer.inventory.currentItem == this.gappleSlot || this.gappleSlot <= -1) {
         if (this.gappleSlot >= 0 && mc.isNotInGui() && mc.getTicksWithoutScreen() > 3) {
            event.setRightMouseDown(true);
         }
      }
   }

   public void onPacket(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof S07PacketRespawn || packet instanceof S08PacketPlayerPosLook) {
         this.timeHelper.reset();
      }
   }

   private boolean shouldEatGapple() {
      boolean flag = false;
      boolean flag2 = false;

      for (PotionEffect potionEffect : (Iterable<PotionEffect>)(mc.thePlayer.getActivePotionEffects())) {
         if (potionEffect.getPotionID() == Potion.regeneration.id) {
            flag = true;
         } else if (potionEffect.getPotionID() == Potion.absorption.id) {
            flag2 = true;
         }
      }

      boolean flag3 = true;
      if (this.considerTargetHealth.getValue()) {
         EntityLivingBase activeTarget = CombatTargetUtil.getActiveTarget();
         if (activeTarget != null && activeTarget.getCurrentHealth() < mc.thePlayer.getCurrentHealth()) {
            flag3 = false;
         }
      }

      return flag3
         && mc.thePlayer.getCurrentHealth() < (float)this.health.getLastIntValue()
         && this.isLicenseTimestampValid()
         && (!this.notWhileKillAura.getValue() || !ClientManagers.moduleManager.killAura.isToggled() || ClientManagers.moduleManager.killAura.target == null)
         && !BlockUtil.isScaffoldActiveAndValid()
         && (!flag && !flag2 || flag2 && !flag || mc.thePlayer.getAbsorptionAmount() == 0.0F && this.whileRegeneration.getValue());
   }

   public int getGappleSlot() {
      return this.gappleSlot;
   }

   public void onSilentSlot() {
      this.gappleSlot = -1;
      if (this.shouldEatGapple() && mc.thePlayer.ticksExisted > 10 && mc.isNotInGui() && mc.getTicksWithoutScreen() > 3) {
         ContainerSlotQuery containerSlotQuery = ContainerSlotQuery.of(mc.thePlayer.inventoryContainer);
         containerSlotQuery = containerSlotQuery.fromSlot((byte)36);
         OptionalInt optionalInt = containerSlotQuery.filterItemExact(Item.getByNameOrId("golden_apple")).findFirstSlotIndex();
         if (optionalInt.isPresent()) {
            this.gappleSlot = optionalInt.getAsInt() - 36;
         }
      } else {
         this.health.nextValue();
      }

      if (!this.timeHelper.reached2(250L)) {
         this.gappleSlot = -1;
      }

      if (this.gappleSlot >= 0) {
         SilentSlotManager.requestSlot(this, (byte)3, this.gappleSlot, this.silentSlotSwitching.getValue());
      }
   }

   private boolean isLicenseTimestampValid() {
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
