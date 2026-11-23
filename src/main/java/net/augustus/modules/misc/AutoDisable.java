package net.augustus.modules.misc;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.modules.ModuleManager;
import net.augustus.events.EventPacket;
import net.augustus.ClientManagers;
import net.augustus.settings.Setting;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.events.EventLoadWorld;
import net.augustus.utils.TupleTriple;
import net.augustus.Augustus;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class AutoDisable extends Module implements TickListener, LoadWorldListener, PacketListener {
   private final BooleanValue disableOnTeleport;
   private final StringValue minTeleportDistance;
   private final BooleansSetting mode;
   private final StringValue forcedisableModulesForXMs;
   private final StringValue retoggleModulesAfterXMs;
   private final HashMap moduleDisableSettings;
   private boolean hasPendingRetoggles;
   private final TimeHelper forceDisableTimer;
   private final TimeHelper retoggleTimer;
   private final HashSet disabledModules;

   public AutoDisable() {
       super("AutoDisable", "AutoAusschalten", new Color(169, 224, 81), Categorys.MISC);
      this.disableOnTeleport = (BooleanValue)(new BooleanValue(this, "Disable on Teleport", false)).markVisualOnly();
      StringValue stringValue = new StringValue(this, "Min teleport distance", 50.0, 0.0, 1000.0, 1);
      BooleanValue disableOnTeleport2 = this.disableOnTeleport;
      this.disableOnTeleport.getClass();
      BooleanValue booleanValue = disableOnTeleport2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.minTeleportDistance = (StringValue)stringValue.visibleWhen(condition).markVisualOnly();
      this.mode = (BooleansSetting)(new BooleansSetting(this, "Mode", "Simple", new String[]{"Simple", "Expert"})).markVisualOnly();
      StringValue stringValue2 = new StringValue(this, "Forcedisable modules for x ms", 0.0, 0.0, 5000.0, 0);
      SettingVisibilityCondition condition2 = () -> this.isSimpleForceDisableDelayVisible();
      this.forcedisableModulesForXMs = (StringValue)stringValue2.visibleWhen(condition2).markVisualOnly();
      StringValue stringValue3 = new StringValue(this, "Retoggle modules after x ms", 0.0, 0.0, 5000.0, 0);
      SettingVisibilityCondition condition3 = () -> this.isSimpleRetoggleDelayVisible();
      this.retoggleModulesAfterXMs = (StringValue)stringValue3.visibleWhen(condition3).markVisualOnly();
      this.moduleDisableSettings = new HashMap();
      this.hasPendingRetoggles = false;
      this.forceDisableTimer = new TimeHelper();
      this.retoggleTimer = new TimeHelper();
      this.disabledModules = new HashSet();
   }

   public void initModuleSettings(ModuleManager moduleManager) {
      if (this.moduleDisableSettings.isEmpty()) {
         for (Categorys categorys2 : Categorys.values()) {
            if (!categorys2.hidden) {
               DoubleValue doubleValue = (DoubleValue)(new DoubleValue(this, categorys2.name())).markVisualOnly();

               for (Module module : (Iterable<Module>)(moduleManager.getModulesByCategory(categorys2))) {
                  if (!(module instanceof AutoDisable)) {
                     BooleanValue booleanValue = (BooleanValue)(new BooleanValue(this, module.getName2(), false, doubleValue)).markVisualOnly();
                     DoubleValue doubleValue2 = new DoubleValue(this, module.getName2() + "Settings", doubleValue);
                     SettingVisibilityCondition condition = () -> this.isExpertModuleOptionVisible(booleanValue);
                     DoubleValue doubleValue3 = (DoubleValue)doubleValue2.visibleWhen(condition).markVisualOnly();
                     HashMap moduleDisableSettings2 = this.moduleDisableSettings;
                     StringValue stringValue = new StringValue(
                        this, module.getName() + "Forcedisable modules for x ms", 0.0, 0.0, 5000.0, 0, doubleValue3
                     );
                     String str = "Forcedisable modules for x ms";
                     Setting setting = stringValue.named(str).markVisualOnly();
                     StringValue stringValue2 = (StringValue)(new StringValue(
                           this, module.getName() + "Retoggle modules after x ms", 0.0, 0.0, 5000.0, 0, doubleValue3
                        ))
                        .markVisualOnly();
                     String str2 = "Retoggle modules after x ms";
                     moduleDisableSettings2.put(module, new TupleTriple(booleanValue, setting, stringValue2.named(str2)));
                  }
               }
            }
         }
      }
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 17;
      this.toggleEventSubscription(b);
      byte b2 = 26;
      this.toggleEventSubscription(b2);
      byte b3 = 40;
      this.toggleEventSubscription(b3);
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      this.hasPendingRetoggles = this.disableConfiguredModules();
   }

   @Override
   public void onTick3() {
      if (this.hasPendingRetoggles && mc.thePlayer != null && mc.theWorld != null && !this.disabledModules.isEmpty()) {
         boolean flag = false;
         Iterator it = this.disabledModules.iterator();

         while (it.hasNext()) {
            Module module = (Module)it.next();
            int forceDisableDelayMs = this.getForceDisableDelayMs(module);
            int retoggleDelayMs = this.getRetoggleDelayMs(module);
            boolean flag2 = forceDisableDelayMs > 0;
            boolean flag3 = retoggleDelayMs > 0;
            long l = (long)forceDisableDelayMs;
            boolean flag4 = this.forceDisableTimer.reached2(l);
            long l2 = (long)retoggleDelayMs;
            boolean flag5 = this.retoggleTimer.reached2(l2);
            if (flag2 && flag4) {
               boolean flag6 = false;
               module.setToggleLocked(flag6);
            }

            if (flag3 && flag4 && flag5) {
               boolean flag7 = false;
               boolean flag8 = true;
               module.setToggled(flag8, flag7);
               it.remove();
               flag = true;
            }
         }

         if (flag) {
            Augustus.getInstance().getModuleManager().sortModules();
         }

         if (this.disabledModules.isEmpty()) {
            this.hasPendingRetoggles = false;
         }
      }
   }

   @Override
   public void onPacket4(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof S08PacketPlayerPosLook) {
         PacketThreadUtil.runOnMainThreadImmediate(() -> this.handleTeleportPacket(event));
      }
   }

   private boolean disableConfiguredModules() {
      boolean flag = true;
      if (!this.hasPendingRetoggles) {
         this.disabledModules.clear();
         flag = false;
      }

      for (Module module : (Iterable<Module>)(ClientManagers.moduleManager.getModules())) {
         if (module.isToggled()) {
            TupleTriple tupleTriple = (TupleTriple)this.moduleDisableSettings.get(module);
            if (tupleTriple != null) {
               BooleanValue first = (BooleanValue)tupleTriple.getFirst();
               if (first != null && first.getValue()) {
                  int forceDisableDelayMs = this.getForceDisableDelayMs(module);
                  int retoggleDelayMs = this.getRetoggleDelayMs(module);
                  if (forceDisableDelayMs > 0 || retoggleDelayMs > 0) {
                     flag = true;
                  }

                  boolean flag2 = false;
                  module.setToggled(flag2);
                  this.disabledModules.add(module);
                  if (forceDisableDelayMs > 0) {
                     boolean flag3 = true;
                     module.setToggleLocked(flag3);
                  }
               }
            }
         }
      }

      this.forceDisableTimer.reset();
      this.retoggleTimer.reset();
      return flag;
   }

   private int getForceDisableDelayMs(Module module) {
      if (this.mode.getSelectedValue().equals("Simple")) {
         boolean flag = true;
         return (int)this.forcedisableModulesForXMs.getValue(flag);
      } else {
         StringValue second = (StringValue)((TupleTriple)this.moduleDisableSettings.get(module)).second;
         boolean flag2 = true;
         return (int)second.getValue(flag2);
      }
   }

   private int getRetoggleDelayMs(Module module) {
      if (this.mode.getSelectedValue().equals("Simple")) {
         boolean flag = true;
         return (int)this.retoggleModulesAfterXMs.getValue(flag);
      } else {
         StringValue third = (StringValue)((TupleTriple)this.moduleDisableSettings.get(module)).third;
         boolean flag2 = true;
         return (int)third.getValue(flag2);
      }
   }

   private void handleTeleportPacket(EventPacket event) {
      S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)event.getPacket();
      if (mc.thePlayer != null && mc.thePlayer.getDistance(packet.getX(), packet.getY(), packet.getZ()) > this.minTeleportDistance.getValue()) {
         boolean hasPendingRetoggles2 = this.hasPendingRetoggles;
         this.hasPendingRetoggles = this.disableConfiguredModules();
         if (this.hasPendingRetoggles && !hasPendingRetoggles2) {
            String str = "Disabled modules because of teleport!";
            this.sendChatMessage2(str);
         }
      }
   }

   private boolean isExpertModuleOptionVisible(BooleanValue booleanValue) {
      return booleanValue.getValue() && this.mode.getSelectedValue().equals("Expert");
   }

   private boolean isSimpleRetoggleDelayVisible() {
      return this.mode.getSelectedValue().equals("Simple");
   }

   private boolean isSimpleForceDisableDelayVisible() {
      return this.mode.getSelectedValue().equals("Simple");
   }

}
