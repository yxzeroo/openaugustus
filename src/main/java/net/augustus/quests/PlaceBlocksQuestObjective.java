package net.augustus.quests;

import net.augustus.events.EventManager;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.PacketListener;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

public class PlaceBlocksQuestObjective extends QuestObjective implements PacketListener {

   @Override
   public void onActivate() {
      EventManager.getEventBus().subscribe(26, this);
   }

   @Override
   public String getUnitLabel() {
      return "blocks";
   }

   @Override
   public String getActionVerb() {
      return "Place";
   }

   public void onPacket2(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof C08PacketPlayerBlockPlacement) {
         C08PacketPlayerBlockPlacement packet2 = (C08PacketPlayerBlockPlacement)packet;
         if (packet2.getStack() != null
            && packet2.getStack().getItem() instanceof ItemBlock
            && packet2.getPlacedBlockDirection() < 6
            && packet2.getPlacedBlockOffsetX() != 0.0F
            && packet2.getPlacedBlockOffsetY() != 0.0F
            && packet2.getPlacedBlockOffsetZ() != 0.0F) {
            double d = 1.0;
            this.addProgress(d);
         }
      }
   }

   public PlaceBlocksQuestObjective(int n) {
       super((double)n);
   }

   @Override
   public void onDeactivate() {
      EventManager.getEventBus().unsubscribe(26, this);
   }

}
