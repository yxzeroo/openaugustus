package net.augustus.quests;



import net.augustus.events.EventLeftClick;
import net.augustus.events.EventManager;
import net.augustus.events.listeners.LeftClickListener;
public class LeftClickQuestObjective extends QuestObjective implements LeftClickListener {

   @Override
   public void onLeftClick(EventLeftClick event) {
      double d = 1.0;
      this.addProgress(d);
   }

   @Override
   public String getActionVerb() {
      return "Leftclick";
   }

   @Override
   public String getUnitLabel() {
      return "times";
   }

   public LeftClickQuestObjective(int n) {
       super((double)n);
   }

   @Override
   public void onDeactivate() {
      EventManager.getEventBus().unsubscribe(53, this);
   }

   @Override
   public void onActivate() {
      EventManager.getEventBus().subscribe(53, this);
   }
}
