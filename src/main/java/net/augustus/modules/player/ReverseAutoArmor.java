package net.augustus.modules.player;

import java.awt.Color;
import java.io.IOException;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.ClientManagers;
import net.augustus.utils.math.RandomNumberGenerator;
import net.augustus.settings.RangeSetting;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.Augustus;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C16PacketClientStatus;

public class ReverseAutoArmor extends Module implements TickListener {
   private final StringValue startDelay;
   public final RangeSetting delay;
   public final BooleanValue drop;
   public final BooleanValue bot;
   private final TimeHelper actionTimer;
   private final TimeHelper inventoryOpenTimer;
   private long nextActionDelayMs;
   private boolean openedByBot;

   public ReverseAutoArmor() {
       super("ReverseAutoArmor", "RüstungAusziehen", new Color(252, 186, 3), Categorys.PLAYER);
      this.startDelay = new StringValue(this, "StartDelay", 200.0, 0.0, 400.0, 0);
      this.delay = new RangeSetting(this, "Delay", 70.0, 120.0, 0.0, 400.0, 0);
      this.drop = new BooleanValue(this, "Drop", false);
      this.bot = new BooleanValue(this, "Bot", false);
      this.actionTimer = new TimeHelper();
      this.inventoryOpenTimer = new TimeHelper();
      this.openedByBot = false;
   }

   public void onEnable() {
      if (mc.thePlayer != null && mc.theWorld != null && Augustus.getInstance().getModuleManager() != null && Augustus.getInstance().getModuleManager().invManager.isToggled()) {
         String str = "ReverseAutoArmor only works when InvManager is disabled!";
         this.sendChatMessage2(str);
      }

      RandomNumberGenerator instance = DefaultRandomGenerator.instance;
      double minValue = this.delay.getMinValue();
      double maxValue = this.delay.getMaxValue();
      double d = maxValue;
      double d2 = minValue;
      this.nextActionDelayMs = (long)instance.nextInt(d2, d);
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
   }

   public void onTick() {
      if (mc.currentScreen instanceof GuiInventory && !ClientManagers.moduleManager.invManager.isToggled()) {
         TimeHelper inventoryOpenTimer2 = this.inventoryOpenTimer;
         double value = this.startDelay.getValue();
         if (inventoryOpenTimer2.reached(value)) {
            boolean flag = true;

            for (int i = 5; i < 9; i++) {
               ItemStack stack = ((Slot)mc.thePlayer.inventoryContainer.inventorySlots.get(i)).getStack();
               if (stack != null) {
                  flag = false;
                  long nextActionDelayMs2 = this.nextActionDelayMs;
                  if (this.actionTimer.reached2(nextActionDelayMs2)) {
                     RandomNumberGenerator instance = DefaultRandomGenerator.instance;
                     double minValue = this.delay.getMinValue();
                     double maxValue = this.delay.getMaxValue();
                     double d = maxValue;
                     double d2 = minValue;
                     this.nextActionDelayMs = (long)instance.nextInt(d2, d);
                     if (this.drop.getValue()) {
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 1, 4, mc.thePlayer);
                        if (this.nextActionDelayMs != 0L) {
                           return;
                        }
                     } else {
                        GuiInventory currentScreen = (GuiInventory)mc.currentScreen;
                        Slot slot = currentScreen.inventorySlots.getSlot(i);

                        try {
                           currentScreen.forceShiftClick = true;
                           currentScreen.mouseClicked(slot.xDisplayPosition + 2 + currentScreen.guiLeft, slot.yDisplayPosition + 2 + currentScreen.guiTop, 0);
                        } catch (IOException e) {
                           throw new RuntimeException(e);
                        }

                        if (this.nextActionDelayMs != 0L) {
                           return;
                        }
                     }

                     this.actionTimer.reset();
                  }
               }
            }

            if (this.bot.getValue() && flag && this.openedByBot) {
               Object obj = null;
               mc.displayGuiScreen((GuiScreen)obj);
               this.openedByBot = false;
            }
         }
      } else {
         this.inventoryOpenTimer.reset();
         if (mc.currentScreen instanceof GuiInventory && this.openedByBot) {
            Object obj2 = null;
            mc.displayGuiScreen((GuiScreen)obj2);
            this.openedByBot = false;
         }
      }

      if (this.bot.getValue() && mc.thePlayer.ticksExisted == 10 && mc.isNotInGui()) {
         for (int j = 5; j < 9; j++) {
            ItemStack stack2 = ((Slot)mc.thePlayer.inventoryContainer.inventorySlots.get(j)).getStack();
            if (stack2 != null) {
               mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
               Minecraft mc2 = mc;
               GuiInventory guiInventory = new GuiInventory(mc.thePlayer);
               mc2.displayGuiScreen(guiInventory);
               this.openedByBot = true;
               break;
            }
         }
      }
   }

}
