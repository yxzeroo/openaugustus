package net.augustus.quests;



import net.augustus.events.EventClick;
import net.augustus.events.EventManager;
import net.augustus.events.listeners.ClickListener;
public class RightClickQuestObjective extends QuestObjective implements ClickListener {

   public RightClickQuestObjective(int n) {
       super((double)n);
   }

   @Override
   public void onActivate() {
      EventManager.getEventBus().subscribe(7, this, 10000);
   }

   @Override
   public String getUnitLabel() {
      return "times";
   }

   @Override
   public void onDeactivate() {
      EventManager.getEventBus().unsubscribe(7, this);
   }

   public void onClick(EventClick event) {
      if (event.getRightClicks() + (event.isIgnoreVanillaClicks() ? 0 : mc.gameSettings.keyBindUseItem.getPressTime()) > 0 && !event.isBlockAllClicks() && !event.isCancelled()) {
         double d = 1.0;
         this.addProgress(d);
      }
   }

   @Override
   public String getActionVerb() {
      return "Rightclick";
   }

}
