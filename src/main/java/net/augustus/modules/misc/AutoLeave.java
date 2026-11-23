package net.augustus.modules.misc;

import net.minecraft.network.play.client.C03PacketPlayer;
import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.RenderListener;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.events.EventKey;
import net.augustus.ClientManagers;
import net.augustus.settings.KeybindSetting;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.utils.math.NumberFormatUtil;
import net.augustus.events.listeners.KeyListener;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.events.EventLoadWorld;
import net.augustus.settings.options.AutoLeaveAfkConditionOption;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.utils.math.MathStatisticsUtil;
import net.augustus.events.EventMoveInput;
import net.augustus.render.RenderUtil;
import net.augustus.Augustus;
import net.augustus.events.listeners.SoundListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.EventSound;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.Packet;

public class AutoLeave extends Module implements KeyListener, RenderListener, LoadWorldListener, TickListener, SoundListener, MoveInputListener, PacketListener {
   public final BooleanValue calculateWithHit;
   public final StringValue time;
   public final BooleanValue displayTime;
   public final StringValue xCoord;
   public final StringValue yCoord;
   public final BooleanValue autoLeaveIfHeatsLow;
   public final StringValue hearts;
   public final BooleanValue leaveOnDeath;
   private final BooleanValue leaveIfAfk;
   private final MultiOptionSetting afkConditions;
   private final StringValue afktimerS;
   private final BooleanValue resetAfkTimerOnWorldchange;
   public final BooleanValue leaveIfStaffArrives;
   public final BooleanValue leaveIfInLava;
   public final BooleanValue leaveIfInWater;
   public final BooleanValue leaveOnKey;
   public final KeybindSetting key;
   public final BooleansSetting mode;
   private final TimeHelper movementAfkTimer;
   private final TimeHelper rotationAfkTimer;
   private final TimeHelper otherKeyAfkTimer;
   private boolean leftDueToAfk;
   private long lastCombatHitTimeMs;
   private final TimeHelper fallDamageSoundTimer;
   private boolean wasDead;
   private final TimeHelper staffLeaveCooldownTimer;

   public AutoLeave() {
       super("AutoLeave", "AutomatischesVerlassen", new Color(50, 168, 82), Categorys.MISC);
      this.calculateWithHit = (BooleanValue)(new BooleanValue(this, "CalculateWithHit", true)).markVisualOnly();
      StringValue stringValue = new StringValue(this, "Time", 12000.0, 0.0, 20000.0, 0);
      BooleanValue calculateWithHit2 = this.calculateWithHit;
      this.calculateWithHit.getClass();
      BooleanValue booleanValue = calculateWithHit2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.time = (StringValue)stringValue.visibleWhen(condition).markVisualOnly();
      BooleanValue booleanValue2 = new BooleanValue(this, "DisplayTime", true);
      calculateWithHit2 = this.calculateWithHit;
      this.calculateWithHit.getClass();
      BooleanValue booleanValue3 = calculateWithHit2;
      SettingVisibilityCondition condition2 = () -> booleanValue3.getValue();
      this.displayTime = (BooleanValue)((BooleanValue)booleanValue2.withVisibility(condition2)).markVisualOnly();
      StringValue stringValue2 = new StringValue(this, "X-Coord", 100.0, 0.0, 100.0, 2);
      SettingVisibilityCondition condition3 = () -> this.isTimerXCoordVisible();
      this.xCoord = (StringValue)stringValue2.visibleWhen(condition3).markVisualOnly();
      StringValue stringValue3 = new StringValue(this, "Y-Coord", 100.0, 0.0, 100.0, 2);
      SettingVisibilityCondition condition4 = () -> this.isTimerYCoordVisible();
      this.yCoord = (StringValue)stringValue3.visibleWhen(condition4).markVisualOnly();
      BooleanValue booleanValue4 = new BooleanValue(this, "AutoLeaveIfHeatsLow", false);
      String str = "AutoLeaveIfHeatsLow";
      this.autoLeaveIfHeatsLow = (BooleanValue)((BooleanValue)booleanValue4.withDisplayName(str)).markVisualOnly();
      StringValue stringValue4 = new StringValue(this, "Hearts", 2.0, 0.0, 20.0, 0);
      calculateWithHit2 = this.autoLeaveIfHeatsLow;
      this.autoLeaveIfHeatsLow.getClass();
      BooleanValue booleanValue5 = calculateWithHit2;
      SettingVisibilityCondition condition5 = () -> booleanValue5.getValue();
      this.hearts = (StringValue)stringValue4.visibleWhen(condition5).markVisualOnly();
      this.leaveOnDeath = (BooleanValue)(new BooleanValue(this, "LeaveOnDeath", false)).markVisualOnly();
      this.leaveIfAfk = (BooleanValue)(new BooleanValue(this, "LeaveIfAfk", false)).markVisualOnly();
      MultiOptionSetting multiOptionSetting = new MultiOptionSetting(this, "AfkConditions", AutoLeaveAfkConditionOption.values());
      calculateWithHit2 = this.leaveIfAfk;
      this.leaveIfAfk.getClass();
      BooleanValue booleanValue6 = calculateWithHit2;
      SettingVisibilityCondition condition6 = () -> booleanValue6.getValue();
      this.afkConditions = (MultiOptionSetting)multiOptionSetting.visibleWhen(condition6).markVisualOnly();
      StringValue stringValue5 = new StringValue(this, "AfkTimer (s)", 30.0, 0.0, 120.0, 0);
      calculateWithHit2 = this.leaveIfAfk;
      this.leaveIfAfk.getClass();
      BooleanValue booleanValue7 = calculateWithHit2;
      SettingVisibilityCondition condition7 = () -> booleanValue7.getValue();
      this.afktimerS = (StringValue)stringValue5.visibleWhen(condition7).markVisualOnly();
      BooleanValue booleanValue8 = new BooleanValue(this, "Reset AfkTimer on worldchange", false);
      calculateWithHit2 = this.leaveIfAfk;
      this.leaveIfAfk.getClass();
      BooleanValue booleanValue9 = calculateWithHit2;
      SettingVisibilityCondition condition8 = () -> booleanValue9.getValue();
      this.resetAfkTimerOnWorldchange = (BooleanValue)((BooleanValue)booleanValue8.withVisibility(condition8)).markVisualOnly();
      BooleanValue booleanValue10 = new BooleanValue(this, "LeaveIfStaffArrives", false);
      SettingVisibilityCondition condition9 = () -> isStaffDetectorEnabled();
      this.leaveIfStaffArrives = (BooleanValue)((BooleanValue)booleanValue10.withVisibility(condition9)).markVisualOnly();
      this.leaveIfInLava = (BooleanValue)(new BooleanValue(this, "LeaveIfInLava", false)).markVisualOnly();
      this.leaveIfInWater = (BooleanValue)(new BooleanValue(this, "LeaveIfInWater", false)).markVisualOnly();
      this.leaveOnKey = (BooleanValue)(new BooleanValue(this, "LeaveOnKey", false)).markVisualOnly();
      KeybindSetting keybindSetting = new KeybindSetting(this, "Key");
      calculateWithHit2 = this.leaveOnKey;
      this.leaveOnKey.getClass();
      BooleanValue booleanValue11 = calculateWithHit2;
      SettingVisibilityCondition condition10 = () -> booleanValue11.getValue();
      this.key = (KeybindSetting)keybindSetting.visibleWhen(condition10).markVisualOnly();
      this.mode = (BooleansSetting)(new BooleansSetting(this, "Mode", "Lobby", new String[]{"Lobby", "Server"})).markVisualOnly();
      this.movementAfkTimer = new TimeHelper();
      this.rotationAfkTimer = new TimeHelper();
      this.otherKeyAfkTimer = new TimeHelper();
      this.leftDueToAfk = false;
      this.lastCombatHitTimeMs = System.currentTimeMillis();
      this.fallDamageSoundTimer = new TimeHelper();
      this.wasDead = false;
      this.staffLeaveCooldownTimer = new TimeHelper();
   }

   @Override
   public void onEnable() {
      this.wasDead = false;
      this.leftDueToAfk = false;
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 14;
      this.toggleEventSubscription(b);
      byte b2 = 32;
      this.toggleEventSubscription(b2);
      byte b3 = 17;
      this.toggleEventSubscription(b3);
      byte b4 = 40;
      this.toggleEventSubscription(b4);
      byte b5 = 35;
      this.toggleEventSubscription(b5);
      byte b6 = 21;
      this.toggleEventSubscription(b6);
      byte b7 = 26;
      this.toggleEventSubscription(b7);
   }

   @Override
   public void onRender(float f, ScaledResolution sr) {
      if (this.calculateWithHit.getValue() && this.displayTime.getValue()) {
         double d = (double)((int)(System.currentTimeMillis() - this.lastCombatHitTimeMs));
         String str = NumberFormatUtil.formatDecimal(d / 1000.0, 1);
         FontRenderer fontRendererObj = mc.fontRendererObj;
         int stringWidth = fontRendererObj.getStringWidth(str);
         int n = fontRendererObj.FONT_HEIGHT - 1;
         int n2 = (int)((double)(sr.getScaledWidth() - stringWidth) / 100.0 * this.xCoord.getValue());
         int n3 = (int)((double)(sr.getScaledHeight() - n) / 100.0 * this.yCoord.getValue());
         fontRendererObj.drawString(str, n2, n3, d > this.time.getValue() ? Color.GREEN.getRGB() : Color.RED.getRGB());
      }

      RenderUtil.resetGlColorState();
   }

   @Override
   public void onSound(EventSound event) {
      if (MathStatisticsUtil.isWithinTolerance(event.getX(), mc.thePlayer.posX, 0.01)
         && MathStatisticsUtil.isWithinTolerance(event.getY(), mc.thePlayer.posY, 0.01)
         && MathStatisticsUtil.isWithinTolerance(event.getZ(), mc.thePlayer.posZ, 0.01)) {
         if (event.getSoundName().equals("game.player.hurt.fall.small") || event.getSoundName().equals("game.player.hurt.fall.big")) {
            this.fallDamageSoundTimer.reset();
         }

         if (event.getSoundName().equals("game.player.hurt")) {
            long l = 100L;
            if (this.fallDamageSoundTimer.reached2(l)) {
               this.lastCombatHitTimeMs = System.currentTimeMillis();
            }
         }
      }
   }

   @Override
   public void onTick() {
      if (this.autoLeaveIfHeatsLow.getValue() && (double)mc.thePlayer.getCurrentHealth() <= this.hearts.getValue()) {
         this.tryLeaveIfCombatSafe();
      }

      if (this.leaveIfInLava.getValue() && mc.thePlayer.isInLava()) {
         this.tryLeaveIfCombatSafe();
      }

      if (this.leaveIfInWater.getValue() && mc.thePlayer.isInWater()) {
         this.tryLeaveIfCombatSafe();
      }

      if (this.leaveIfStaffArrives.getValue() && ClientManagers.moduleManager.staffDetector.isToggled() && this.isLicenseTimestampValid() && ClientManagers.moduleManager.staffDetector.hasConfirmedStaff()) {
         long l = 1000L;
         if (this.staffLeaveCooldownTimer.reached2(l)) {
            this.executeLeave();
            this.staffLeaveCooldownTimer.reset();
         }
      }

      if (mc.thePlayer.isDead && !this.wasDead && this.leaveOnDeath.getValue()) {
         this.executeLeave();
      }

      exitBlock:
      if (this.leaveIfAfk.getValue()) {
         exitBlock2: {
            exitBlock3: {
               int n = (int)this.afktimerS.getValue() * 1000;
               if (AutoLeaveAfkConditionOption.isOptionSelected(AutoLeaveAfkConditionOption.ROTATIONS)) {
                  long l2 = (long)n;
                  if (this.rotationAfkTimer.reached2(l2)) {
                     break exitBlock3;
                  }
               }

               if (AutoLeaveAfkConditionOption.isOptionSelected(AutoLeaveAfkConditionOption.MOVEMENT)) {
                  long l3 = (long)n;
                  if (this.movementAfkTimer.reached2(l3)) {
                     break exitBlock3;
                  }
               }

               if (!AutoLeaveAfkConditionOption.isOptionSelected(AutoLeaveAfkConditionOption.OTHER_KEY_INPUTS)) {
                  break exitBlock2;
               }

               long l4 = (long)n;
               if (!this.otherKeyAfkTimer.reached2(l4)) {
                  break exitBlock2;
               }
            }

            if (!this.leftDueToAfk) {
               this.executeLeave();
               this.leftDueToAfk = true;
            }
            break exitBlock;
         }

         this.leftDueToAfk = false;
      }

      this.wasDead = mc.thePlayer.isDead;
   }

   @Override
   public void onKey2(EventKey event) {
      int keyCode = event.getKeyCode();
      if (this.leaveOnKey.getValue() && keyCode == this.key.getKeyCode()) {
         this.tryLeaveIfCombatSafe();
      }

      if (keyCode != mc.gameSettings.keyBindForward.getKeyCode()
         && keyCode != mc.gameSettings.keyBindBack.getKeyCode()
         && keyCode != mc.gameSettings.keyBindLeft.getKeyCode()
         && keyCode != mc.gameSettings.keyBindRight.getKeyCode()
         && keyCode != mc.gameSettings.keyBindJump.getKeyCode()
         && keyCode != mc.gameSettings.keyBindSneak.getKeyCode()) {
         this.otherKeyAfkTimer.reset();
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
   }

   @Override
   public void onPacket(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof C03PacketPlayer.C05PacketPlayerLook || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
         this.rotationAfkTimer.reset();
      }
   }

   @Override
   public void onMoveInput(EventMoveInput event) {
      if (event.getMoveForward() != 0.0F || event.getMoveStrafe() != 0.0F || event.isSneak() || event.isJump()) {
         this.movementAfkTimer.reset();
      }
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      this.lastCombatHitTimeMs = System.currentTimeMillis();
      if (this.resetAfkTimerOnWorldchange.getValue()) {
         this.rotationAfkTimer.reset();
         this.movementAfkTimer.reset();
         this.otherKeyAfkTimer.reset();
      }
   }

   private void tryLeaveIfCombatSafe() {
      int n = (int)(System.currentTimeMillis() - this.lastCombatHitTimeMs);
      if ((double)n > this.time.getValue() || !this.calculateWithHit.getValue()) {
         this.executeLeave();
         this.lastCombatHitTimeMs = System.currentTimeMillis();
      }
   }

   private void executeLeave() {
      String selectedValue = this.mode.getSelectedValue();
      switch (selectedValue) {
         case "Lobby":
            mc.thePlayer.sendChatMessage("/hub");
            break;
         case "Server":
            boolean flag = true;
            mc.isGamePaused = flag;
            boolean flag2 = mc.isIntegratedServerRunning();
            mc.theWorld.sendQuittingDisconnectingPacket();
            Object obj = null;
            mc.loadWorld((WorldClient)obj);
            if (flag2) {
               Minecraft mc2 = mc;
               GuiMainMenu guiMainMenu = new GuiMainMenu();
               mc2.displayGuiScreen(guiMainMenu);
            } else {
               Minecraft mc3 = mc;
               GuiMultiplayer guiMultiplayer = new GuiMultiplayer(new GuiMainMenu());
               mc3.displayGuiScreen(guiMultiplayer);
            }
      }
   }

   private static boolean isStaffDetectorEnabled() {
      return Augustus.getInstance().getModuleManager().staffDetector.isToggled();
   }

   private boolean isTimerYCoordVisible() {
      return this.calculateWithHit.getValue() && this.displayTime.getValue();
   }

   private boolean isTimerXCoordVisible() {
      return this.calculateWithHit.getValue() && this.displayTime.getValue();
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
