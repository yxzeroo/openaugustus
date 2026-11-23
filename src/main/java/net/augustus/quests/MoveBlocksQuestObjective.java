package net.augustus.quests;

import net.augustus.events.EventManager;
import net.augustus.events.listeners.TickListener;
import net.augustus.utils.TimeHelper;
import net.minecraft.util.MathHelper;

public class MoveBlocksQuestObjective extends QuestObjective implements TickListener {
   private int ticksMovedInWindow;
   private final TimeHelper timeHelper;

   public void onTick() {
      if (this.timeHelper.reached2(900L)) {
         this.ticksMovedInWindow = 0;
         this.timeHelper.reset();
      }

      double d = Math.abs(mc.thePlayer.posX - mc.thePlayer.lastTickPosX);
      double d2 = Math.abs(mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ);
      if (d < 1.0 && d2 < 1.0 && this.ticksMovedInWindow < 21) {
         this.addProgress((double)MathHelper.sqrt_double(d * d + d2 * d2));
         this.ticksMovedInWindow++;
      }
   }

   @Override
   public String getActionVerb() {
      return "Move";
   }

   @Override
   public void onDeactivate() {
      EventManager.getEventBus().unsubscribe(40, this);
   }

   @Override
   public String getUnitLabel() {
      return "blocks";
   }

   public MoveBlocksQuestObjective(int n) {
       super((double)n);
      this.timeHelper = new TimeHelper();
      this.ticksMovedInWindow = 0;
   }

   @Override
   public void onActivate() {
      EventManager.getEventBus().subscribe(40, this);
   }

}
