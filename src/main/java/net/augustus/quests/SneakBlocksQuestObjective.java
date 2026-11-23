package net.augustus.quests;

import net.augustus.events.EventManager;
import net.augustus.events.listeners.TickListener;
import net.augustus.utils.TimeHelper;
import net.minecraft.util.MathHelper;

public class SneakBlocksQuestObjective extends QuestObjective implements TickListener {
   private final TimeHelper timeHelper;
   private int ticksMovedInWindow;

   @Override
   public String getUnitLabel() {
      return "blocks";
   }

   @Override
   public String getActionVerb() {
      return "Sneak";
   }

   public SneakBlocksQuestObjective(int n) {
       super((double)n);
      this.timeHelper = new TimeHelper();
      this.ticksMovedInWindow = 0;
   }

   public void onTick() {
      if (mc.thePlayer.isServerSneakState()) {
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
   }

   @Override
   public void onActivate() {
      EventManager.getEventBus().subscribe(40, this);
   }

   @Override
   public void onDeactivate() {
      EventManager.getEventBus().unsubscribe(40, this);
   }




}
