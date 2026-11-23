package net.augustus.modules.modes.speed;

import net.augustus.ClientManagers;
import net.augustus.events.EventMotion;
import net.augustus.events.EventMoveEntity;
import net.augustus.events.EventMoveInput;
import net.augustus.events.EventTimer;
import net.augustus.modules.movement.Speed;

public class TimeManipulationSpeedMode extends SpeedMode {
   public TimeManipulationSpeedMode(String str, Speed speed2) {
      super(str, speed2);
   }

   @Override
   public void onEnable() {
      if (!ClientManagers.moduleManager.timeManipulation.isToggled()) {
         String str = "Please activate module Timemanipulation";
         this.speed.sendChatMessage2(str);
      }
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void onPreUpdate() {
   }

   @Override
   public void onPostUpdate() {
   }

   @Override
   public void onMotion(EventMotion event) {
   }

   @Override
   public void onMoveEntity(EventMoveEntity event) {
   }

   @Override
   public void onPreMoveInput(EventMoveInput event) {
   }

   @Override
   public void onTimer(EventTimer event) {
      float f = Math.max(0.1F, (float)ClientManagers.moduleManager.speed.timeManipulationTimer.getValue());
      event.setTimerSpeed(f);
      int value = (int)ClientManagers.moduleManager.speed.timeManipulationTPTicks.getValue();
      event.setExtraTicks(value);
   }

}
