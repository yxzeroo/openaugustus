package net.augustus.modules.misc;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.ClickListener;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.events.EventClick;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.events.EventLoadWorld;
import net.augustus.clickgui.ClickGuiScreen;
import net.augustus.events.listeners.TickListener;
import net.augustus.utils.player.SilentSlotManager;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S40PacketDisconnect;
import org.lwjgl.input.Mouse;

public class AutoPlay extends Module implements LoadWorldListener, ClickListener, TickListener, RenderListener, PacketListener, SilentSlotListener {
   private final BooleansSetting mode;
   private final BooleansSetting gamemodeMMC;
   private final BooleansSetting gamemodeKit1v1;
   private final BooleansSetting gamemodeGamster;
   public final BooleanValue removeEscape;
   public final BooleanValue cancelSound;
   private final BooleanValue silentSlotSwitching;
   private final StringValue hotbarItemID;
   private final StringValue chestslot;
   private final BooleanValue setSlotAfterRespawn;
   private final RandomizedRangeSettings delayToSetSlowAfterRespawn;
   private final StringValue slotToSetAfterRespawn;
   private final TimeHelper chestClickTimer;
   private final TimeHelper pikaPlayAgainTimer;
   private final TimeHelper itemUseTimer;
   private int ticksSinceRespawn;
   private float savedMasterVolume;
   public float[] savedRotations;
   private int randomChestClickDelay;
   private boolean autoGambleCompleted;
   private final HashMap gamsterPlayCommands;

   public AutoPlay() {
       super("AutoPlay", "AutoSpielen", Color.green, Categorys.MISC);
      this.mode = (BooleansSetting)(new BooleansSetting(
            this,
            "Mode",
            "MinemenRanked",
            new String[]{
               "MinemenUnranked",
               "MinemenRanked",
               "PikaSkywars",
               "HypixelNormal",
               "HypixelInsane",
               "GommeKit1v1Ranked",
               "GommeKit1v1Unranked",
               "Kitpvp",
               "PvpDojo",
               "UniversocraftSkywars",
               "AutoGamblePVPDojo",
               "Gamster"
            }
         ))
         .markVisualOnly();
      BooleansSetting booleansSetting = new BooleansSetting(this, "GamemodeMMC", "Sumo", new String[]{"Sumo", "Boxing", "Parkour"});
      String str = "Gamemode";
      BooleansSetting booleansSetting2 = (BooleansSetting)booleansSetting.withDisplayName(str);
      SettingVisibilityCondition condition = () -> this.isMinemenModeSelected();
      this.gamemodeMMC = (BooleansSetting)((BooleansSetting)booleansSetting2.withVisibility(condition)).markVisualOnly();
      BooleansSetting booleansSetting3 = new BooleansSetting(this, "GamemodeKit1v1", "OnlySword", new String[]{"UHC", "OnlySword", "Soup", "SG", "SW", "QSG"});
      String str2 = "Gamemode";
      BooleansSetting booleansSetting4 = (BooleansSetting)booleansSetting3.withDisplayName(str2);
      SettingVisibilityCondition condition2 = () -> this.isGommeKit1v1Selected();
      this.gamemodeKit1v1 = (BooleansSetting)((BooleansSetting)booleansSetting4.withVisibility(condition2)).markVisualOnly();
      BooleansSetting booleansSetting5 = new BooleansSetting(
         this,
         "GamemodeGamster",
         "SkyWarsSolo",
         new String[]{"SkyWarsSolo", "SkyWarsTeams", "SkyWarsLucky", "BedWarsSolo", "BedWarsDuos", "BedWarsTrios", "BedWarsTeams"}
      );
      SettingVisibilityCondition condition3 = () -> this.isGamsterModeSelected();
      this.gamemodeGamster = (BooleansSetting)((BooleansSetting)booleansSetting5.withVisibility(condition3)).markVisualOnly();
      this.removeEscape = (BooleanValue)(new BooleanValue(this, "RemoveEscape", false)).markVisualOnly();
      this.cancelSound = (BooleanValue)(new BooleanValue(this, "CancelSound", false)).markVisualOnly();
      this.silentSlotSwitching = new BooleanValue(this, "SilentSlotSwitching", true);
      this.hotbarItemID = (StringValue)(new StringValue(this, "HotbarItemID", 54.0, 0.0, 2267.0, 0)).markVisualOnly();
      StringValue stringValue = new StringValue(this, "Chestslot", 0.0, 0.0, 54.0, 0);
      SettingVisibilityCondition condition4 = () -> this.isChestSlotVisible();
      this.chestslot = (StringValue)stringValue.visibleWhen(condition4).markVisualOnly();
      this.setSlotAfterRespawn = (BooleanValue)(new BooleanValue(this, "SetSlotAfterRespawn", false)).markVisualOnly();
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(this, "DelayToSetSlowAfterRespawn", 5.0, 10.0, 0.0, 20.0, 0)).withoutNotInARow();
      BooleanValue setSlotAfterRespawn2 = this.setSlotAfterRespawn;
      this.setSlotAfterRespawn.getClass();
      BooleanValue booleanValue = setSlotAfterRespawn2;
      SettingVisibilityCondition condition5 = () -> booleanValue.getValue();
      this.delayToSetSlowAfterRespawn = (RandomizedRangeSettings)((RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition5)).markAllVisualOnly();
      StringValue stringValue2 = new StringValue(this, "SlotToSetAfterRespawn", 0.0, 0.0, 8.0, 0);
      setSlotAfterRespawn2 = this.setSlotAfterRespawn;
      this.setSlotAfterRespawn.getClass();
      BooleanValue booleanValue2 = setSlotAfterRespawn2;
      SettingVisibilityCondition condition6 = () -> booleanValue2.getValue();
      this.slotToSetAfterRespawn = (StringValue)stringValue2.visibleWhen(condition6).markVisualOnly();
      this.chestClickTimer = new TimeHelper();
      this.pikaPlayAgainTimer = new TimeHelper();
      this.itemUseTimer = new TimeHelper();
      this.ticksSinceRespawn = 0;
      this.savedRotations = new float[2];
      this.randomChestClickDelay = 0;
      this.autoGambleCompleted = false;
      this.gamsterPlayCommands = new HashMap();
      this.gamsterPlayCommands.put("SkyWarsSolo", "/play sw-solo");
      this.gamsterPlayCommands.put("SkyWarsTeams", "/play sw-teams");
      this.gamsterPlayCommands.put("SkyWarsLucky", "/play sw-lucky");
      this.gamsterPlayCommands.put("BedWarsSolo", "/play bw-solo");
      this.gamsterPlayCommands.put("BedWarsDuos", "/play bw-duo");
      this.gamsterPlayCommands.put("BedWarsTrios", "/play bw-trio");
      this.gamsterPlayCommands.put("BedWarsTeams", "/play bw-teams");
   }

   @Override
   public void onEnable() {
      this.savedMasterVolume = mc.gameSettings.getSoundLevel(SoundCategory.MASTER);
      if (this.cancelSound.getValue()) {
         mc.gameSettings.setSoundLevel(SoundCategory.MASTER, 0.0F);
      }

      this.autoGambleCompleted = false;
   }

   @Override
   public void onDisable() {
      mc.gameSettings.setSoundLevel(SoundCategory.MASTER, this.savedMasterVolume);
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 7;
      this.toggleEventSubscription(b);
      byte b2 = 40;
      this.toggleEventSubscription(b2);
      byte b3 = 32;
      this.toggleEventSubscription(b3);
      byte b4 = 26;
      this.toggleEventSubscription(b4);
      byte b5 = 34;
      this.toggleEventSubscription(b5);
      byte b6 = 17;
      this.toggleEventSubscription(b6);
   }

   @Override
   public void onRender(float f) {
      if (mc.currentScreen instanceof ClickGuiScreen && Mouse.isGrabbed()) {
         Mouse.setGrabbed(false);
      }

      if (mc.currentScreen instanceof GuiIngameMenu && this.removeEscape.getValue()) {
         Object obj = null;
         mc.currentScreen = (GuiScreen)obj;
         mc.thePlayer.setSpoofedScreen(null);
         boolean flag = true;
         mc.inGameHasFocus = flag;
      }
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      this.autoGambleCompleted = false;
      this.delayToSetSlowAfterRespawn.nextValue();
      this.ticksSinceRespawn = 0;
   }

   @Override
   public void onPacket(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof S08PacketPlayerPosLook) {
         S08PacketPlayerPosLook packet2 = (S08PacketPlayerPosLook)packet;
         if (mc.thePlayer.getDistance(packet2.getX(), packet2.getY(), packet2.getZ()) > 30.0) {
            this.delayToSetSlowAfterRespawn.nextValue();
            this.ticksSinceRespawn = 0;
         }
      }
   }

   @Override
   public void onPacket4(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof S40PacketDisconnect) {
         mc.mouseHelper.ungrabMouseCursor();
      }

      if (packet instanceof S02PacketChat) {
         S02PacketChat packet2 = (S02PacketChat)packet;
         String unformattedText = packet2.getChatComponent().getUnformattedText();
         if (this.mode.getSelectedValue().equals("HypixelNormal")) {
            if (unformattedText.contains("You won! Want to play again? Click here!")) {
               mc.thePlayer.sendChatMessage("/play solo_normal");
            }

            if (unformattedText.contains("You died!")) {
               mc.thePlayer.sendChatMessage("/play solo_normal");
            }
         }

         if (this.mode.getSelectedValue().equals("HypixelInsane")) {
            if (unformattedText.contains("You won! Want to play again? Click here!")) {
               mc.thePlayer.sendChatMessage("/play solo_insane");
            }

            if (unformattedText.contains("You died!")) {
               mc.thePlayer.sendChatMessage("/play solo_insane");
            }
         }

         if (this.mode.getSelectedValue().equals("UniversocraftSkywars") && unformattedText.contains("Jugar de nuevo - Salir")) {
            mc.thePlayer.sendChatMessage("/skywars random");
         }
      }
   }

   @Override
   public void onClick(EventClick event) {
      if (mc.isNotInGui()) {
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(mc.thePlayer.inventory.currentItem + 36).getStack();
         if (stack != null) {
            if (stack.getDisplayName().contains("Unranked") && this.mode.getSelectedValue().equals("MinemenUnranked")) {
               byte b = 1;
               event.addRightClicks(b);
            }

            if (!stack.getDisplayName().contains("Unranked") && stack.getDisplayName().contains("Ranked") && this.mode.getSelectedValue().equals("MinemenRanked")) {
               byte b2 = 1;
               event.addRightClicks(b2);
            }

            if (stack.getDisplayName().contains("Play Again") && this.mode.getSelectedValue().equals("MinemenUnranked")) {
               byte b3 = 1;
               event.addRightClicks(b3);
            }

            if (stack.getDisplayName().contains("Play Again")) {
               long l = 6000L;
               if (this.pikaPlayAgainTimer.reached2(l) && this.mode.getSelectedValue().equals("PikaSkywars")) {
                  byte b4 = 1;
                  event.addRightClicks(b4);
                  this.pikaPlayAgainTimer.reset();
               }
            }

            if (this.mode.getSelectedValue().equals("GommeKit1v1Ranked")) {
               String displayName2 = stack.getDisplayName();
               if (this.matchesGommeKitItemName(displayName2) || stack.getItem().equals(Items.blaze_rod)) {
                  TimeHelper itemUseTimer2 = this.itemUseTimer;
                  short s = 250;
                  short s2 = 150;
                  long l2 = (long)DefaultRandomGenerator.instance.nextInt2(s2, s);
                  if (itemUseTimer2.reached2(l2)) {
                     byte b5 = 1;
                     event.addRightClicks(b5);
                     this.itemUseTimer.reset();
                  }
               }
            }

            if (this.mode.getSelectedValue().equals("GommeKit1v1Unranked")) {
               String displayName3 = stack.getDisplayName();
               if (this.matchesGommeKitItemName(displayName3) || stack.getItem().equals(Items.blaze_rod)) {
                  TimeHelper itemUseTimer3 = this.itemUseTimer;
                  short s3 = 250;
                  short s4 = 150;
                  long l3 = (long)DefaultRandomGenerator.instance.nextInt2(s4, s3);
                  if (itemUseTimer3.reached2(l3)) {
                     byte b6 = 1;
                     event.addLeftClicks(b6);
                     this.itemUseTimer.reset();
                  }
               }
            }

            if (this.mode.getSelectedValue().equalsIgnoreCase("kitpvp")) {
               TimeHelper itemUseTimer4 = this.itemUseTimer;
               short s5 = 2000;
               short s6 = 1000;
               long l4 = (long)DefaultRandomGenerator.instance.nextInt2(s6, s5);
               if (itemUseTimer4.reached2(l4) && Item.getItemById((int)this.hotbarItemID.getValue()) == stack.getItem()) {
                  byte b7 = 1;
                  event.addRightClicks(b7);
                  this.itemUseTimer.reset();
               }
            }

            if (this.mode.getSelectedValue().equals("AutoGamblePVPDojo") && !this.autoGambleCompleted && SilentSlotManager.isOwnedBy(this)) {
               TimeHelper itemUseTimer5 = this.itemUseTimer;
               short s7 = 2000;
               short s8 = 1000;
               long l5 = (long)DefaultRandomGenerator.instance.nextInt2(s8, s7);
               if (itemUseTimer5.reached2(l5) && stack.getDisplayName().contains("Collection")) {
                  byte b8 = 1;
                  event.addRightClicks(b8);
                  this.itemUseTimer.reset();
               }
            }
         }
      }
   }

   @Override
   public void onSilentSlot2() {
      if (mc.isNotInGui() && this.setSlotAfterRespawn.getValue()) {
         if (this.ticksSinceRespawn == Math.max(0, this.delayToSetSlowAfterRespawn.getLastIntValue())) {
            boolean flag = true;
            SilentSlotManager.realHotbarSlot = mc.thePlayer.inventory.currentItem = (int)this.slotToSetAfterRespawn.getValue(flag);
         }

         this.ticksSinceRespawn++;
      }
   }

   @Override
   public void onSilentSlot() {
      if (mc.isNotInGui()) {
         for (int i = 36; i < mc.thePlayer.inventoryContainer.inventorySlots.size(); i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null) {
               Item item = stack.getItem();
               String displayName2 = stack.getDisplayName();
               String selectedValue = this.mode.getSelectedValue();
               switch (selectedValue) {
                  case "MinemenUnranked":
                     if (displayName2.contains("Unranked")) {
                        int n = i - 36;
                        boolean value = this.silentSlotSwitching.getValue();
                        int n2 = n;
                        byte b = 20;
                        SilentSlotManager.requestSlot(this, b, n2, value);
                     } else if (displayName2.contains("Play Again")) {
                        int n3 = i - 36;
                        boolean value2 = this.silentSlotSwitching.getValue();
                        int n4 = n3;
                        byte b2 = 20;
                        SilentSlotManager.requestSlot(this, b2, n4, value2);
                     }
                     break;
                  case "MinemenRanked":
                     if (!displayName2.contains("Unranked") && stack.getDisplayName().contains("Ranked")) {
                        int n5 = i - 36;
                        boolean value3 = this.silentSlotSwitching.getValue();
                        int n6 = n5;
                        byte b3 = 20;
                        SilentSlotManager.requestSlot(this, b3, n6, value3);
                     }
                     break;
                  case "PikaSkywars":
                     if (displayName2.contains("Play Again")) {
                        int n7 = i - 36;
                        boolean value4 = this.silentSlotSwitching.getValue();
                        int n8 = n7;
                        byte b4 = 20;
                        SilentSlotManager.requestSlot(this, b4, n8, value4);
                     }
                     break;
                  case "GommeKit1v1Ranked":
                  case "GommeKit1v1Unranked":
                     if (this.matchesGommeKitItemName(displayName2)) {
                        int n9 = i - 36;
                        boolean value5 = this.silentSlotSwitching.getValue();
                        int n10 = n9;
                        byte b5 = 20;
                        SilentSlotManager.requestSlot(this, b5, n10, value5);
                     }

                     if (item.equals(Items.blaze_rod)) {
                        int n11 = i - 36;
                        boolean value6 = this.silentSlotSwitching.getValue();
                        int n12 = n11;
                        byte b6 = 20;
                        SilentSlotManager.requestSlot(this, b6, n12, value6);
                     }
                     break;
                  case "AutoGamblePVPDojo":
                     if (displayName2.contains("Collection") && !this.autoGambleCompleted) {
                        int n13 = i - 36;
                        boolean value7 = this.silentSlotSwitching.getValue();
                        int n14 = n13;
                        byte b7 = 20;
                        SilentSlotManager.requestSlot(this, b7, n14, value7);
                     }
                     break;
                  case "Gamster":
                     this.handleGamsterPlayAgainItem(stack, item);
               }
            }
         }
      }
   }

   @Override
   public void onTick() {
      if (mc.isNotInGui()) {
         this.chestClickTimer.reset();
         short s = 150;
         byte b = 0;
         this.randomChestClickDelay = DefaultRandomGenerator.instance.nextInt2(b, s);
      } else if (mc.currentScreen instanceof GuiChest) {
         long l = (long)(350 + this.randomChestClickDelay);
         if (this.chestClickTimer.reached2(l)) {
            GuiChest currentScreen = (GuiChest)mc.currentScreen;
            ItemStack stack = currentScreen.inventorySlots.getSlot(8).getStack();
            boolean flag = false;

            for (int i = 0; i < currentScreen.inventorySlots.inventorySlots.size(); i++) {
               ItemStack stack2 = currentScreen.inventorySlots.getSlot(i).getStack();
               if ((double)i == this.chestslot.getValue() && stack2 != null && this.mode.getSelectedValue().equals("Kitpvp")) {
                  this.clickChestSlot(i);
                  break;
               }

               if (stack2 != null) {
                  boolean flag2 = false;
                  String selectedValue = this.mode.getSelectedValue();
                  switch (selectedValue) {
                     case "MinemenUnranked":
                        if (stack2.getDisplayName().contains(this.gamemodeMMC.getSelectedValue())) {
                           System.out.println(stack2 + " " + this.gamemodeMMC.getSelectedValue());
                           this.clickChestSlot(i);
                           flag2 = true;
                        }
                        break;
                     case "MinemenRanked":
                        if (stack2.getDisplayName().contains(this.gamemodeMMC.getSelectedValue())) {
                           this.clickChestSlot(i);
                           flag2 = true;
                        }
                        break;
                     case "GamsterKitpvp":
                        if (stack2.getItem() == Items.diamond_sword) {
                           this.clickChestSlot(i);
                           flag2 = true;
                        }
                        break;
                     case "PvpDojo":
                        if (stack2.getItem() == Items.stone_sword || stack2.getItem() == Items.nether_star) {
                           this.clickChestSlot(i);
                           flag2 = true;
                        }
                        break;
                     case "AutoGamblePVPDojo":
                        if (stack2.getDisplayName().contains("Collection") && i < 36) {
                           flag = true;
                           this.clickChestSlot(i);
                        }
                  }

                  if (flag2) {
                     break;
                  }
               }
            }

            if (this.mode.getSelectedValue().equals("AutoGamblePVPDojo") && !flag && stack != null && stack.getItem().equals(Items.sign)) {
               this.autoGambleCompleted = true;
            }
         }
      }
   }

   private void handleGamsterPlayAgainItem(ItemStack stack, Item item) {
      if (Items.paper.equals(item) && stack.getDisplayName().contains("Play")) {
         String displayName2 = stack.getDisplayName();
         if (displayName2.contains("Again")) {
            String str = (String)this.gamsterPlayCommands.get(this.gamemodeGamster.getSelectedValue());
            if (str == null) {
               return;
            }

            mc.thePlayer.sendChatMessage(str);
         }
      }
   }

   private void clickChestSlot(int n) {
      GuiChest currentScreen = (GuiChest)mc.currentScreen;
      Slot slot = currentScreen.inventorySlots.getSlot(n);

      try {
         currentScreen.mouseClicked(slot.xDisplayPosition + 2 + currentScreen.guiLeft, slot.yDisplayPosition + 2 + currentScreen.guiTop, 0);
      } catch (IOException e) {
         e.printStackTrace();
      }

      this.chestClickTimer.reset();
   }

   private boolean matchesGommeKitItemName(String str) {
      String selectedValue = this.gamemodeKit1v1.getSelectedValue();
      switch (selectedValue) {
         case "UHC":
            return str.equals("§7Kit: §f§eUHC");
         case "OnlySword":
            return str.equals("§7Kit: §f§bOnly Sword");
         case "Soup":
            return str.equals("§7Kit: §f§cSOUP");
         case "SG":
            return str.equals("§7Kit: §f§aSG");
         case "SW":
            return str.equals("§7Kit: §f§4SW");
         case "QSG":
            return str.equals("§6Season-Kit: §f§5QSG");
         default:
            return false;
      }
   }

   public boolean shouldSuppressEscapeMenu() {
      return this.isToggled() && this.removeEscape.getValue();
   }

   private boolean isChestSlotVisible() {
      return this.mode.getSelectedValue().equalsIgnoreCase("Kitpvp");
   }

   private boolean isGamsterModeSelected() {
      return this.mode.getSelectedValue().equals("Gamster");
   }

   private boolean isGommeKit1v1Selected() {
      return this.mode.getSelectedValue().equals("GommeKit1v1Ranked") || this.mode.getSelectedValue().equals("GommeKit1v1Unranked");
   }

   private boolean isMinemenModeSelected() {
      return this.mode.getSelectedValue().equals("MinemenUnranked") || this.mode.getSelectedValue().equals("MinemenRanked");
   }

}
