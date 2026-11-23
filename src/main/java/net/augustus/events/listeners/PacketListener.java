package net.augustus.events.listeners;

import net.augustus.events.EventManager;
import net.augustus.events.EventPacket;
import net.augustus.events.PacketStage;
import net.minecraft.network.Packet;

public interface PacketListener {
   default void onPacket(EventPacket event) {
   }

   default void onPacket2(EventPacket event) {
   }

   default void onPacket3(EventPacket event) {
   }

   default void onPacket4(EventPacket event) {
   }

   default void onPacket5(EventPacket event) {
   }

   static EventPacket onPacket(Packet packet, PacketStage packetStage) {
      EventPacket event = new EventPacket(packet, packetStage);
      EventManager.getEventBus().post(26, event);
      return event;
   }
}
