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
import net.augustus.settings.DoubleValue;
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
   public final DoubleValue healHearts;
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
      this.healHearts = new DoubleValue(this, "HealHearts", 14.0, 2.0, 20.0, 0);
      this.instant = new BooleanValue(this, "Instant", true);
      this.tickDelayBeforeClick = new RangeSetting(this, "TickDelayBeforeClick", 0.0, 0.0, 0.0, 7.0, 0).visibleWhen(() -> this.isDelayBeforeClickVisible());
      this.tickDelayBetweenClick = (RandomizedRangeSettings)(new RandomizedRangeSettings(this, "TickDelayBetweenClick", 0.0, 0.0, 0.0, 7.0, 0)).withoutNotInARow().withVisibility(() -> this.isDelayBetweenClickVisible());
      RangeSetting rangeSetting2 = new RangeSetting(this, "TickDelayAfterClick", 1.0, 1.0, 0.0, 7.0, 0);
      SettingVisibilityCondition condition3 = () -> {
         return !this.instant.getValue();
      };
      this.tickDelayAfterClick = rangeSetting2.visibleWhen(condition3);
      this.silentSlotSwitching = (BooleanValue)new BooleanValue(this, "SilentSlotSwitching", true).withVisibility(() -> this.isSilentSlotSettingVisible());
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
      this.toggleEventSubscription((byte)40);
      this.toggleEventSubscription((byte)18);
      this.toggleEventSubscription((byte)34);
      this.toggleEventSubscription((byte)7, (byte)20);
      this.toggleEventSubscription((byte)26);
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
      if (this.timeHelper.reached2(1000L) || this.needsAdditionalHeal) {
         if (this.canThrowPotion()) {
            TuplePair tuplePair = this.findBestPotionSlot(this.hotbarOnly.getValue());
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
            rotationController.requestPitch(RotationUtil.updatePitchWithGcd(pitch, 90.0F, instance.nextSecureFloat(minValue, maxValue)), (byte)1, this);
         }
      }
   }

   @Override
   public void onClick(EventClick event) {
      this.ticksSinceLastClick++;
      if (!this.instant.getValue()) {
         if (this.canThrowPotion() && this.hasValidPotionTarget()) {
            int n = this.resolveHotbarSlot(this.potionSlot);
            if (n != this.potionSlot - 36) {
               this.swapToHotbar(this.potionSlot, n);
               this.potionSlot = n;
            }

            ItemStack currentEquippedItem = mc.thePlayer.getCurrentEquippedItem();
            if (currentEquippedItem != null && currentEquippedItem.getItem() instanceof ItemPotion && n == mc.thePlayer.inventory.currentItem) {
               if (((ItemPotion)currentEquippedItem.getItem()).equals(this.selectedPotion) && ClientManagers.rotationController.getPreviousRotation().pitch > 85.0F && this.ticksSinceSlotSwitch > this.delayBeforeClick && (this.ticksSinceLastClick > this.tickDelayBetweenClick.getLastIntValue() || !this.hasThrownInSequence)
                  )
                {
                  this.tickDelayBetweenClick.nextValue();
                  event.addRightClicks((byte)1);
                  event.setRightMouseDown(true);
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
         SilentSlotManager.requestSlot(this, (byte)4, this.getTargetHotbarSlot(), this.silentSlotSwitching.getValue());
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
               int n = this.resolveHotbarSlot(this.potionSlot);
               if (n != this.potionSlot - 36) {
                  this.swapToHotbar(this.potionSlot, n);
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
            this.needsAdditionalHeal = (double)((int)((mc.thePlayer.getMaxHealth() - this.predictedHealth) / (float)n)) > 0.85;
            this.predictedHealth += (float)n;
            System.out.println(this.needsAdditionalHeal);
         }
      }
   }

   private void randomizeClickDelays() {
      RandomNumberGenerator instance = DefaultRandomGenerator.instance;
      double minValue = this.tickDelayBeforeClick.getMinValue();
      double maxValue = this.tickDelayBeforeClick.getMaxValue();
      this.delayBeforeClick = instance.nextInt(minValue, maxValue);
      RandomNumberGenerator instance2 = DefaultRandomGenerator.instance;
      minValue = this.tickDelayAfterClick.getMinValue();
      maxValue = this.tickDelayAfterClick.getMaxValue();
      this.delayAfterClick = instance2.nextInt(minValue, maxValue);
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
                        if (this.lacksPotionEffect(potionEffect.getPotionID()) && this.isBestPotionOfType(item, potionEffect, flag)) {
                           this.needsAdditionalHeal = false;
                           return new TuplePair(i, stack);
                        }
                     }
                     break;
                  default:
                     if (!Potion.potionTypes[potionEffect.getPotionID()].isBadEffect() && AutoPotPotionOption.ALL_POSITIVE.isSelected()) {
                        if (this.lacksPotionEffect(potionEffect.getPotionID()) && this.isBestPotionOfType(item, potionEffect, flag)) {
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
      return this.resolveHotbarSlot(this.potionSlot);
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
