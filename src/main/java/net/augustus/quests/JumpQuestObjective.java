package net.augustus.quests;

import net.augustus.events.EventManager;
import net.augustus.events.EventMove;
import net.augustus.events.listeners.MoveListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.utils.TimeHelper;

public class JumpQuestObjective extends QuestObjective implements MoveListener, TickListener {
   private final TimeHelper timeHelper;
   private boolean canCountJump;

   public void onTick() {
      if (mc.thePlayer.airTicks > 8) {
         this.canCountJump = true;
      }
   }

   @Override
   public void onDeactivate() {
      EventManager.getEventBus().unsubscribe(28, this);
      EventManager.getEventBus().unsubscribe(40, this);
   }

   @Override
   public void onActivate() {
      EventManager.getEventBus().subscribe(28, this);
      EventManager.getEventBus().subscribe(40, this);
   }

   @Override
   public String getActionVerb() {
      return "Jump";
   }

   public void onMove2(EventMove event) {
      if (mc.thePlayer.onGround && mc.currentScreen == null && this.canCountJump) {
         if (this.timeHelper.reached2(300L)) {
            this.addProgress(1.0);
            this.canCountJump = false;
         }
      }

      this.timeHelper.reset();
   }

   public JumpQuestObjective(int n) {
       super((double)n);
      this.canCountJump = false;
      this.timeHelper = new TimeHelper();
   }

   @Override
   public String getUnitLabel() {
      return "times";
   }

   public void onMove(EventMove event) {
   }

}
