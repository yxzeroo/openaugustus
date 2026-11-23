package net.augustus.modules.misc;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Random;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPacket;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.settings.TextInputSetting;
import net.augustus.vendor.crypto.Sha256Digest;
import net.augustus.vendor.crypto.CustomBase64;
import net.augustus.utils.math.MathStatisticsUtil;
import net.augustus.Augustus;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;
import org.apache.commons.lang3.StringUtils;

public class AutoRegister extends Module implements PacketListener, TickListener {
   private final BooleansSetting passwordMode;
   private final RandomizedRangeSettings delay;
   public final TextInputSetting customPassword;
   private final BooleanValue getPasswordFromTxtIfPresent;
   private final BooleanValue printOnRegister;
   private final BooleanValue printOnLogin;
   private final HashMap scheduledCommands;
   private final HashMap filePasswords;

   public AutoRegister() {
       super("AutoRegister", "AutoRegistrieren", new Color(189, 32, 110), Categorys.MISC);
      this.passwordMode = (BooleansSetting)(new BooleansSetting(this, "PasswordMode", "Generated", new String[]{"Generated", "Private", "Private2", "Custom"}))
         .markVisualOnly();
      this.delay = (RandomizedRangeSettings)(new RandomizedRangeSettings(this, "Delay", 700.0, 1400.0, 0.0, 4000.0, 0)).withoutNotInARow().markAllVisualOnly();
      TextInputSetting textInputSetting = new TextInputSetting(this, "Custom password", "Hallo1337", true);
      SettingVisibilityCondition condition = () -> this.isCustomPasswordMode();
      this.customPassword = (TextInputSetting)((TextInputSetting)textInputSetting.withVisibility(condition)).markVisualOnly();
      this.getPasswordFromTxtIfPresent = (BooleanValue)(new BooleanValue(this, "Get password from txt if present", false)).markVisualOnly();
      this.printOnRegister = (BooleanValue)(new BooleanValue(this, "Print on register", false)).markVisualOnly();
      this.printOnLogin = (BooleanValue)(new BooleanValue(this, "Print on login", false)).markVisualOnly();
      this.scheduledCommands = new HashMap();
      this.filePasswords = new HashMap();
      String str = Augustus.getInstance().getName().toLowerCase() + File.separator;
      boolean flag = false;
      String str2 = str;
      ClientFileUtil.extractResource("assets/augustus/autoregister.txt", str2, flag);
   }

   @Override
   public void onEnable() {
      this.scheduledCommands.clear();
      this.loadPasswordsFromFile();
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 26;
      this.toggleEventSubscription(b);
      byte b2 = 40;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onPacket(EventPacket event) {
      Packet packet = event.getPacket();
      String str = this.extractMessageText(packet);
      if (str != null) {
         if (StringUtils.containsIgnoreCase(str, "/reg") || StringUtils.containsIgnoreCase(str, "/register")) {
            boolean flag = false;
            this.scheduleAuthCommand(flag);
         } else if (StringUtils.containsIgnoreCase(str, "/login")) {
            boolean flag2 = true;
            this.scheduleAuthCommand(flag2);
         }
      }
   }

   @Override
   public void onTick2() {
      if (!this.scheduledCommands.isEmpty()) {
         for (Double d : (Iterable<Double>)(this.scheduledCommands.keySet())) {
            if (!(d > (double)System.currentTimeMillis())) {
               mc.thePlayer.sendChatMessage((String)this.scheduledCommands.get(d));
               this.scheduledCommands.remove(d);
               break;
            }
         }
      }
   }

   private String extractMessageText(Packet packet) {
      if (packet instanceof S02PacketChat) {
         S02PacketChat packet2 = (S02PacketChat)packet;
         return packet2.getChatComponent().getUnformattedText();
      } else {
         if (packet instanceof S45PacketTitle) {
            S45PacketTitle packet3 = (S45PacketTitle)packet;
            if (packet3.getMessage() != null) {
               return packet3.getMessage().getUnformattedText();
            }
         }

         return null;
      }
   }

   private void scheduleAuthCommand(boolean flag) {
      double d = this.delay.nextValue();
      double d2 = (double)System.currentTimeMillis() + d;
      String str = MathStatisticsUtil.formatRounded(d / 1000.0, 1);
      if (this.scheduledCommands.isEmpty()) {
         if (flag) {
            String selectedValue = this.passwordMode.getSelectedValue();
            boolean value = this.getPasswordFromTxtIfPresent.getValue();
            String str2 = selectedValue;
            String str3 = this.resolvePassword(str2, value);
            StringBuilder sb = new StringBuilder().append("Logging in... (").append(str);
            String str4 = sb.append("s)").append(this.printOnLogin.getValue() ? " with " + str3 : "").toString();
            this.sendChatMessage2(str4);
            this.scheduledCommands.put(d2, "/login " + str3);
         } else {
            String selectedValue2 = this.passwordMode.getSelectedValue();
            boolean value2 = this.getPasswordFromTxtIfPresent.getValue();
            String str5 = selectedValue2;
            String str6 = this.resolvePassword(str5, value2);
            StringBuilder sb2 = new StringBuilder().append("Register in... (").append(str);
            short s = 28607;
            String str7 = sb2.append("s)").append(this.printOnRegister.getValue() ? " with " + str6 : "").toString();
            this.sendChatMessage2(str7);
            this.scheduledCommands.put(d2, "/register " + str6 + " " + str6);
         }
      }
   }

   public String resolvePassword(String str, boolean flag) {
      String username = mc.getSession().getUsername();
      if (flag && this.filePasswords.containsKey(username)) {
         return (String)this.filePasswords.get(username);
      } else {
         if (str.equalsIgnoreCase("Custom")) {
            return this.customPassword.getValue();
         } else {
            byte[] bytes = username.getBytes(StandardCharsets.UTF_8);
            if (str.equalsIgnoreCase("private2")) {
               for (int i = 0; i < bytes.length; i++) {
                  bytes[i] = (byte)(bytes[i] ^ 5);
               }
            }

            if (str.equalsIgnoreCase("generated")) {
               byte[] bytes2 = Sha256Digest.digest(bytes);
               return CustomBase64.getRfc4648().encodeToString(bytes2).substring(0, 12);
            } else {
               byte[] bytes3 = Sha256Digest.digest(bytes);
               int n = Augustus.getInstance().getUsername().hashCode();

               for (int j = 0; j < bytes3.length; j++) {
                  bytes3[j] = (byte)(bytes3[j] ^ (byte)(j ^ 267 ^ bytes[j % bytes.length]) ^ (byte)(n >> j % 5 & 0xFF));
               }

               Random rand;
               if (str.equalsIgnoreCase("Private")) {
                  rand = new Random((long)n);
               } else {
                  rand = new Random((long)(n ^ username.hashCode()));
               }

               return CustomBase64.getRfc4648().encodeToString(bytes3).substring(0, rand.nextInt(3) + 10);
            }
         }
      }
   }

   private void loadPasswordsFromFile() {
      this.filePasswords.clear();

      File file;
      try {
         file = ClientFileUtil.getExistingFile("augustus/autoregister.txt");
      } catch (FileNotFoundException e) {
         String str = Augustus.getInstance().getName().toLowerCase() + File.separator;
         boolean flag = false;
         String str2 = str;
         ClientFileUtil.extractResource("assets/augustus/autoregister.txt", str2, flag);

         try {
            file = ClientFileUtil.getExistingFile("augustus/autoregister.txt");
         } catch (FileNotFoundException e2) {
            return;
         }
      }

      try {
         for (String str3 : Files.readAllLines(file.toPath(), StandardCharsets.UTF_8)) {
            if (!str3.startsWith("#")) {
               str3 = str3.replace("\\:", "\u0000");
               String[] strings = str3.split(":");
               if (strings.length >= 2) {
                  this.filePasswords.put(strings[0].replace("\u0000", ":"), strings[1].replace("\u0000", ":"));
               }
            }
         }
      } catch (Throwable e3) {
      }
   }

   private boolean isCustomPasswordMode() {
      return this.passwordMode.getSelectedValue().equals("Custom");
   }

}
