package net.augustus.events;

public class EventPacketStateSwitchMap {
   public static final int[] ORDINAL_MAP = new int[PacketStage.values().length];

   static {
      try {
         ORDINAL_MAP[PacketStage.READ_NETTY.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[PacketStage.SEND.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         ORDINAL_MAP[PacketStage.SEND_NETTY.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         ORDINAL_MAP[PacketStage.SEND_NETTY_DELAY.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }

      try {
         ORDINAL_MAP[PacketStage.READ.ordinal()] = 5;
      } catch (NoSuchFieldError e5) {
      }
   }
}
