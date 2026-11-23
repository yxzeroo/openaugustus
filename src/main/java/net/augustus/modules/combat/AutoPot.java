package net.augustus.modules.combat;

import net.minecraft.network.play.client.C03PacketPlayer;
import java.awt.Color;
import java.util.List;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.ClickListener;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.events.listeners.MotionListener;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.utils.math.RandomNumberGenerator;
import net.augustus.events.EventClick;
import net.augustus.settings.RangeSetting;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.settings.options.AutoPotPotionOption;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.TuplePair;
import net.augustus.utils.rotation.RotationController;
import net.augustus.utils.world.BlockUtil;
import net.augustus.Augustus;
import net.augustus.events.listeners.TickListener;
import net.augustus.utils.player.SilentSlotManager;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.events.EventMotion;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class AutoPot extends Module implements TickListener, MotionListener, SilentSlotListener, ClickListener, PacketListener {
   public final RangeSetting pitchSpeed;
   public final StringValue healHearts;
   public final BooleanValue instant;
   public final RangeSetting tickDelayBeforeClick;
   public final RandomizedRangeSettings tickDelayBetweenClick;
   public final RangeSetting tickDelayAfterClick;
   private final BooleanValue silentSlotSwitching;
   public final BooleanValue hotbarOnly;
   public final BooleanValue inInv;
   public final BooleanValue notWhileKillAura;
   private final MultiOptionSetting potions;
   public final TimeHelper timeHelper;
   private int potionSlot;
   private ItemPotion selectedPotion;
   private ItemStack selectedPotionStack;
   private int delayBeforeClick;
   private int delayAfterClick;
   private int ticksSinceSlotSwitch;
   private int ticksSinceThrow;
   private int ticksSinceLastClick;
   private boolean needsAdditionalHeal;
   private float predictedHealth;
   private boolean wasSwitchingSlot;
   private float lastServerHealth;
   private boolean hasThrownInSequence;

   public AutoPot() {
       super("AutoPot", "AutoTrank", new Color(227, 66, 101), Categorys.COMBAT);
      this.pitchSpeed = new RangeSetting(this, "PitchSpeed", 30.0, 50.0, 0.0, 180.0, 0);
      this.healHearts = new StringValue(this, "HealHearts", 14.0, 2.0, 20.0, 0);
      this.instant = new BooleanValue(this, "Instant", true);
      RangeSetting rangeSetting = new RangeSetting(this, "TickDelayBeforeClick", 0.0, 0.0, 0.0, 7.0, 0);
      SettingVisibilityCondition condition = () -> this.isDelayBeforeClickVisible();
      this.tickDelayBeforeClick = rangeSetting.visibleWhen(condition);
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(this, "TickDelayBetweenClick", 0.0, 0.0, 0.0, 7.0, 0)).withoutNotInARow();
      SettingVisibilityCondition condition2 = () -> this.isDelayBetweenClickVisible();
      this.tickDelayBetweenClick = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition2);
      RangeSetting rangeSetting2 = new RangeSetting(this, "TickDelayAfterClick", 1.0, 1.0, 0.0, 7.0, 0);
      SettingVisibilityCondition condition3 = () -> {
         return !this.instant.getValue();
      };
      this.tickDelayAfterClick = rangeSetting2.visibleWhen(condition3);
      BooleanValue booleanValue = new BooleanValue(this, "SilentSlotSwitching", true);
      SettingVisibilityCondition condition4 = () -> this.isSilentSlotSettingVisible();
      this.silentSlotSwitching = (BooleanValue)booleanValue.withVisibility(condition4);
      this.hotbarOnly = new BooleanValue(this, "HotbarOnly", true);
      this.inInv = new BooleanValue(this, "InInv", false);
      this.notWhileKillAura = new BooleanValue(this, "NotWhileKillAura", true);
      this.potions = new MultiOptionSetting(this, "Potions", AutoPotPotionOption.values());
      this.timeHelper = new TimeHelper();
      this.potionSlot = -1;
      this.selectedPotion = null;
      this.selectedPotionStack = null;
      this.delayBeforeClick = 1;
      this.delayAfterClick = 1;
      this.ticksSinceSlotSwitch = 0;
      this.ticksSinceThrow = 0;
      this.ticksSinceLastClick = 0;
      this.needsAdditionalHeal = false;
      this.predictedHealth = 0.0F;
      this.wasSwitchingSlot = false;
      this.hasThrownInSequence = false;
   }

   @Override
   public void onEnable() {
      this.potionSlot = -1;
      this.selectedPotion = null;
      this.wasSwitchingSlot = false;
      this.randomizeClickDelays();
   }

   @Override
   public void onDisable() {
      this.potionSlot = -1;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
      byte b2 = 18;
      this.toggleEventSubscription(b2);
      byte b3 = 34;
      this.toggleEventSubscription(b3);
      byte b4 = 20;
      byte b5 = 7;
      this.toggleEventSubscription(b5, b4);
      byte b6 = 26;
      this.toggleEventSubscription(b6);
   }

   @Override
   public void onPacket(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof S06PacketUpdateHealth) {
         S06PacketUpdateHealth packet2 = (S06PacketUpdateHealth)packet;
         if (packet2.getHealth() < this.lastServerHealth) {
            this.predictedHealth = packet2.getHealth();
         }

         this.lastServerHealth = packet2.getHealth();
      }
   }

   @Override
   public void onTick() {
      long l = 1000L;
      if (this.timeHelper.reached2(l) || this.needsAdditionalHeal) {
         if (this.canThrowPotion()) {
            boolean value = this.hotbarOnly.getValue();
            TuplePair tuplePair = this.findBestPotionSlot(value);
            this.potionSlot = (Integer)tuplePair.getFirst();
            this.selectedPotionStack = (ItemStack)tuplePair.getSecond();
            this.selectedPotion = this.selectedPotionStack != null ? (ItemPotion)this.selectedPotionStack.getItem() : null;
            this.ticksSinceThrow = 1000;
         }
      }

      if (!this.instant.getValue()) {
         if (this.canThrowPotion() && this.hasValidPotionTarget()) {
            RotationController rotationController = ClientManagers.rotationController;
            float pitch = ClientManagers.rotationController.getPreviousRotation().pitch;
            RandomNumberGenerator instance = DefaultRandomGenerator.instance;
            double minValue = this.pitchSpeed.getMinValue();
            double maxValue = this.pitchSpeed.getMaxValue();
            double d = minValue;
            float f = RotationUtil.updatePitchWithGcd(pitch, 90.0F, instance.nextSecureFloat(d, maxValue));
            byte b = 1;
            float f2 = f;
            rotationController.requestPitch(f2, b, this);
         }
      }
   }

   @Override
   public void onClick(EventClick event) {
      this.ticksSinceLastClick++;
      if (!this.instant.getValue()) {
         if (this.canThrowPotion() && this.hasValidPotionTarget()) {
            int potionSlot2 = this.potionSlot;
            int n = this.resolveHotbarSlot(potionSlot2);
            if (n != this.potionSlot - 36) {
               int potionSlot3 = this.potionSlot;
               this.swapToHotbar(potionSlot3, n);
               this.potionSlot = n;
            }

            ItemStack currentEquippedItem = mc.thePlayer.getCurrentEquippedItem();
            if (currentEquippedItem != null && currentEquippedItem.getItem() instanceof ItemPotion && n == mc.thePlayer.inventory.currentItem) {
               ItemPotion item = (ItemPotion)currentEquippedItem.getItem();
               if (item.equals(this.selectedPotion) && ClientManagers.rotationController.getPreviousRotation().pitch > 85.0F && this.ticksSinceSlotSwitch > this.delayBeforeClick && (this.ticksSinceLastClick > this.tickDelayBetweenClick.getLastIntValue() || !this.hasThrownInSequence)
                  )
                {
                  this.tickDelayBetweenClick.nextValue();
                  byte b = 1;
                  event.addRightClicks(b);
                  boolean flag = true;
                  event.setRightMouseDown(flag);
                  this.updateChainHealState();
                  this.ticksSinceThrow = 0;
                  this.hasThrownInSequence = true;
                  this.ticksSinceLastClick = 0;
                  this.timeHelper.reset();
                  this.selectedPotion = null;
               }
            }

            return;
         }
      }

      this.ticksSinceLastClick = 0;
      this.hasThrownInSequence = false;
   }

   @Override
   public void onSilentSlot() {
      this.ticksSinceSlotSwitch++;
      boolean flag = false;
      if (this.getTargetHotbarSlot() >= 0 && Augustus.getInstance().getRotationController().isOwnedBy(this) && ClientManagers.rotationController.getPreviousRotation().pitch > 85.0F
         || this.ticksSinceThrow < (this.needsAdditionalHeal ? Math.max(this.delayAfterClick, this.tickDelayBetweenClick.getLastIntValue()) : this.delayAfterClick)) {
         int targetHotbarSlot = this.getTargetHotbarSlot();
         boolean value = this.silentSlotSwitching.getValue();
         int n = targetHotbarSlot;
         byte b = 4;
         SilentSlotManager.requestSlot(this, b, n, value);
         flag = true;
      }

      if (flag != this.wasSwitchingSlot) {
         this.randomizeClickDelays();
      }

      this.wasSwitchingSlot = flag;
      this.ticksSinceThrow++;
   }

   @Override
   public void onMotion(EventMotion event) {
      if (this.instant.getValue()) {
         if (this.canThrowPotion() && this.hasValidPotionTarget()) {
            int currentItem = mc.thePlayer.inventory.currentItem;
            if (currentItem != this.potionSlot - 36) {
               int potionSlot2 = this.potionSlot;
               int n = this.resolveHotbarSlot(potionSlot2);
               if (n != this.potionSlot - 36) {
                  int potionSlot3 = this.potionSlot;
                  this.swapToHotbar(potionSlot3, n);
               }

               mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n));
               mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, 90.0F, mc.thePlayer.onGround));
               mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(n)));
               this.updateChainHealState();
               mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(currentItem));
            } else {
               mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, 90.0F, mc.thePlayer.onGround));
               mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(this.potionSlot - 36)));
            }

            this.timeHelper.reset();
            this.potionSlot = -1;
         }
      }
   }

   private void updateChainHealState() {
      if (this.selectedPotion != null && this.selectedPotionStack != null && this.selectedPotion.hasEffect(this.selectedPotionStack)) {
         PotionEffect potionEffect = (PotionEffect)this.selectedPotion.getEffects(this.selectedPotionStack).get(0);
         if (potionEffect.getPotionID() != 6) {
            this.needsAdditionalHeal = false;
         } else {
            int n = (int)(1.0 * (double)(4 << potionEffect.getAmplifier()) + 0.5);
            float f = mc.thePlayer.getMaxHealth() - this.predictedHealth;
            this.needsAdditionalHeal = (double)((int)(f / (float)n)) > 0.85;
            this.predictedHealth += (float)n;
            System.out.println(this.needsAdditionalHeal);
         }
      }
   }

   private void randomizeClickDelays() {
      RandomNumberGenerator instance = DefaultRandomGenerator.instance;
      double minValue = this.tickDelayBeforeClick.getMinValue();
      double maxValue = this.tickDelayBeforeClick.getMaxValue();
      double d = maxValue;
      double d2 = minValue;
      this.delayBeforeClick = instance.nextInt(d2, d);
      RandomNumberGenerator instance2 = DefaultRandomGenerator.instance;
      minValue = this.tickDelayAfterClick.getMinValue();
      maxValue = this.tickDelayAfterClick.getMaxValue();
      double d3 = maxValue;
      double d4 = minValue;
      this.delayAfterClick = instance2.nextInt(d4, d3);
      this.ticksSinceSlotSwitch = 0;
   }

   private int resolveHotbarSlot(int n) {
      if (n < 0 || n > mc.thePlayer.inventoryContainer.inventorySlots.size()) {
         return n;
      } else if (n >= 36) {
         return n - 36;
      } else {
         for (int i = 36; i < mc.thePlayer.inventoryContainer.inventorySlots.size(); i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack == null || stack.getItem() instanceof ItemPotion) {
               return i - 36;
            }
         }

         return 6;
      }
   }

   private TuplePair findBestPotionSlot(boolean flag) {

      for (int i = flag ? 36 : 9; i < mc.thePlayer.inventoryContainer.inventorySlots.size(); i++) {
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
         if (stack != null && stack.getItem() instanceof ItemPotion) {
            ItemPotion item = (ItemPotion)stack.getItem();
            List effects = item.getEffects(stack);
            if (effects != null && !effects.isEmpty() && ItemPotion.isSplash(stack.getMetadata())) {
               PotionEffect potionEffect = (PotionEffect)item.getEffects(stack).get(0);
               switch (potionEffect.getPotionID()) {
                  case 6:
                     if ((double)mc.thePlayer.getCurrentHealth() < this.healHearts.getValue() && AutoPotPotionOption.HEALTH.isSelected() && this.isBestPotionOfType(item, potionEffect, flag)) {
                        return new TuplePair(i, stack);
                     }
                     break;
                  case 10:
                     if (!AutoPotPotionOption.REGENERATION.isSelected()) {
                        break;
                     }
                  case 1:
                     if ((!((double)mc.thePlayer.getCurrentHealth() >= this.healHearts.getValue()) || potionEffect.getPotionID() != 10)
                        && (AutoPotPotionOption.SPEED.isSelected() || potionEffect.getPotionID() != 1)) {
                        int potionID = potionEffect.getPotionID();
                        if (this.lacksPotionEffect(potionID) && this.isBestPotionOfType(item, potionEffect, flag)) {
                           this.needsAdditionalHeal = false;
                           return new TuplePair(i, stack);
                        }
                     }
                     break;
                  default:
                     Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
                     if (!potion.isBadEffect() && AutoPotPotionOption.ALL_POSITIVE.isSelected()) {
                        int potionID2 = potionEffect.getPotionID();
                        if (this.lacksPotionEffect(potionID2) && this.isBestPotionOfType(item, potionEffect, flag)) {
                           this.needsAdditionalHeal = false;
                           return new TuplePair(i, stack);
                        }
                     }
               }
            }
         }
      }

      this.needsAdditionalHeal = false;
      return new TuplePair(-1, null);
   }

   private boolean lacksPotionEffect(int n) {

      for (PotionEffect potionEffect : (Iterable<PotionEffect>)(mc.thePlayer.getActivePotionEffects())) {
         if (potionEffect.getPotionID() == n) {
            return false;
         }
      }

      return true;
   }

   private boolean isBestPotionOfType(ItemPotion itemPotion, PotionEffect potionEffect, boolean flag) {

      for (int i = flag ? 36 : 9; i < mc.thePlayer.inventoryContainer.inventorySlots.size(); i++) {
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
         if (stack != null && stack.getItem() instanceof ItemPotion) {
            ItemPotion item = (ItemPotion)stack.getItem();
            if (item != itemPotion && !itemPotion.getEffects(stack).isEmpty() && ItemPotion.isSplash(stack.getMetadata())) {
               PotionEffect potionEffect2 = (PotionEffect)item.getEffects(stack).get(0);
               if (potionEffect2.getPotionID() == potionEffect.getPotionID()) {
                  if (potionEffect.getAmplifier() > potionEffect2.getAmplifier() && this.isLicenseTimestampValid()) {
                     return false;
                  }

                  return potionEffect.getDuration() <= potionEffect2.getDuration() || potionEffect.getAmplifier() != potionEffect2.getAmplifier();
               }
            }
         }
      }

      return true;
   }

   private void swapToHotbar(int n, int n2) {
      mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, n, n2, 2, mc.thePlayer);
   }

   private boolean canThrowPotion() {
      return !mc.thePlayer.isEating()
         && (!mc.gameSettings.keyBindUseItem.isKeyDown() || mc.playerController.isNotCreative())
         && (!this.notWhileKillAura.getValue() || !ClientManagers.moduleManager.killAura.isToggled() || ClientManagers.moduleManager.killAura.target == null)
         && !BlockUtil.isScaffoldActiveAndValid()
         && !mc.thePlayer.isUsingItem()
         && (mc.isNotInGui() || this.inInv.getValue() && mc.currentScreen instanceof GuiInventory);
   }

   public int getTargetHotbarSlot() {
      int potionSlot2 = this.potionSlot;
      return this.resolveHotbarSlot(potionSlot2);
   }

   private boolean hasValidPotionTarget() {
      return this.potionSlot > 0 && this.selectedPotion != null;
   }

   private boolean isSilentSlotSettingVisible() {
      return !this.instant.getValue();
   }

   private boolean isDelayBetweenClickVisible() {
      return !this.instant.getValue();
   }

   private boolean isDelayBeforeClickVisible() {
      return !this.instant.getValue();
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
