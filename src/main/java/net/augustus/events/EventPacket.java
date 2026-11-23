package net.augustus.events;

import net.augustus.events.listeners.PacketListener;
import de.florianmichael.dietrichevents2.CancellableEvent;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;

public class EventPacket extends CancellableEvent {
   private final INetHandler netHandler;
   private final EnumPacketDirection direction;
   private Packet packet;
   private final PacketStage stage;
   public static final int ID = 26;

   public void setPacket(Packet packet2) {
      this.packet = packet2;
   }

   public void call2(PacketListener listener) {
      switch (EventPacketStateSwitchMap.ORDINAL_MAP[this.stage.ordinal()]) {
         case 1:
            listener.onPacket4(this);
            break;
         case 2:
            listener.onPacket2(this);
            break;
         case 3:
            listener.onPacket5(this);
            break;
         case 4:
            listener.onPacket3(this);
            break;
         case 5:
            listener.onPacket(this);
      }
   }

   public INetHandler getNetHandler() {
      return this.netHandler;
   }

   public Packet getPacket() {
      return this.packet;
   }

   public EnumPacketDirection getDirection() {
      return this.direction;
   }

   public void call(Object obj) {
      PacketListener listener = (PacketListener)obj;
      this.call2(listener);
   }

   public EventPacket(Packet packet2, PacketStage packetStage) {
      this.packet = packet2;
      this.stage = packetStage;
      this.netHandler = null;
      this.direction = null;
   }




}
