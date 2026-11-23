package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.border.WorldBorder;

public class S44PacketWorldBorder implements Packet {
   private S44PacketWorldBorder.Action action;
   private int size;
   private double centerX;
   private double centerZ;
   private double targetSize;
   private double diameter;
   private long timeUntilTarget;
   private int warningTime;
   private int warningDistance;

   public void processPacket(INetHandlerPlayClient handler) {
      handler.handleWorldBorder(this);
   }

   public S44PacketWorldBorder() {
   }

   @Override
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeEnumValue(this.action);
      switch (S44PacketWorldBorder.ActionSwitchMap.ORDINAL_MAP[this.action.ordinal()]) {
         case 1:
            buf.writeDouble(this.targetSize);
            break;
         case 2:
            buf.writeDouble(this.diameter);
            buf.writeDouble(this.targetSize);
            buf.writeVarLong(this.timeUntilTarget);
            break;
         case 3:
            buf.writeDouble(this.centerX);
            buf.writeDouble(this.centerZ);
            break;
         case 4:
            buf.writeVarIntToBuffer(this.warningDistance);
            break;
         case 5:
            buf.writeVarIntToBuffer(this.warningTime);
            break;
         case 6:
            buf.writeDouble(this.centerX);
            buf.writeDouble(this.centerZ);
            buf.writeDouble(this.diameter);
            buf.writeDouble(this.targetSize);
            buf.writeVarLong(this.timeUntilTarget);
            buf.writeVarIntToBuffer(this.size);
            buf.writeVarIntToBuffer(this.warningDistance);
            buf.writeVarIntToBuffer(this.warningTime);
      }
   }

   @Override
   public void processPacket(INetHandler handler) {
      this.processPacket((INetHandlerPlayClient)handler);
   }

   public S44PacketWorldBorder(WorldBorder worldBorder, S44PacketWorldBorder.Action action2) {
      this.action = action2;
      this.centerX = worldBorder.getCenterX();
      this.centerZ = worldBorder.getCenterZ();
      this.diameter = worldBorder.getDiameter();
      this.targetSize = worldBorder.getTargetSize();
      this.timeUntilTarget = worldBorder.getTimeUntilTarget();
      this.size = worldBorder.getSize();
      this.warningDistance = worldBorder.getWarningDistance();
      this.warningTime = worldBorder.getWarningTime();
   }

   @Override
   public void readPacketData(PacketBuffer buf) throws IOException {
      this.action = (S44PacketWorldBorder.Action)buf.readEnumValue(S44PacketWorldBorder.Action.class);
      switch (S44PacketWorldBorder.ActionSwitchMap.ORDINAL_MAP[this.action.ordinal()]) {
         case 1:
            this.targetSize = buf.readDouble();
            break;
         case 2:
            this.diameter = buf.readDouble();
            this.targetSize = buf.readDouble();
            this.timeUntilTarget = buf.readVarLong();
            break;
         case 3:
            this.centerX = buf.readDouble();
            this.centerZ = buf.readDouble();
            break;
         case 4:
            this.warningDistance = buf.readVarIntFromBuffer();
            break;
         case 5:
            this.warningTime = buf.readVarIntFromBuffer();
            break;
         case 6:
            this.centerX = buf.readDouble();
            this.centerZ = buf.readDouble();
            this.diameter = buf.readDouble();
            this.targetSize = buf.readDouble();
            this.timeUntilTarget = buf.readVarLong();
            this.size = buf.readVarIntFromBuffer();
            this.warningDistance = buf.readVarIntFromBuffer();
            this.warningTime = buf.readVarIntFromBuffer();
      }
   }

   public void apply(WorldBorder worldBorder) {
      switch (S44PacketWorldBorder.ActionSwitchMap.ORDINAL_MAP[this.action.ordinal()]) {
         case 1:
            worldBorder.setTransition(this.targetSize);
            break;
         case 2:
            worldBorder.setTransition(this.diameter, this.targetSize, this.timeUntilTarget);
            break;
         case 3:
            worldBorder.setCenter(this.centerX, this.centerZ);
            break;
         case 4:
            worldBorder.setWarningDistance(this.warningDistance);
            break;
         case 5:
            worldBorder.setWarningTime(this.warningTime);
            break;
         case 6:
            worldBorder.setCenter(this.centerX, this.centerZ);
            if (this.timeUntilTarget > 0L) {
               worldBorder.setTransition(this.diameter, this.targetSize, this.timeUntilTarget);
            } else {
               worldBorder.setTransition(this.targetSize);
            }

            worldBorder.setSize(this.size);
            worldBorder.setWarningDistance(this.warningDistance);
            worldBorder.setWarningTime(this.warningTime);
      }
   }


   public static class ActionSwitchMap {
      public static final int[] ORDINAL_MAP = new int[S44PacketWorldBorder.Action.values().length];

      static {
         try {
            ORDINAL_MAP[S44PacketWorldBorder.Action.SET_SIZE.ordinal()] = 1;
         } catch (NoSuchFieldError e) {
         }

         try {
            ORDINAL_MAP[S44PacketWorldBorder.Action.LERP_SIZE.ordinal()] = 2;
         } catch (NoSuchFieldError e2) {
         }

         try {
            ORDINAL_MAP[S44PacketWorldBorder.Action.SET_CENTER.ordinal()] = 3;
         } catch (NoSuchFieldError e3) {
         }

         try {
            ORDINAL_MAP[S44PacketWorldBorder.Action.SET_WARNING_BLOCKS.ordinal()] = 4;
         } catch (NoSuchFieldError e4) {
         }

         try {
            ORDINAL_MAP[S44PacketWorldBorder.Action.SET_WARNING_TIME.ordinal()] = 5;
         } catch (NoSuchFieldError e5) {
         }

         try {
            ORDINAL_MAP[S44PacketWorldBorder.Action.INITIALIZE.ordinal()] = 6;
         } catch (NoSuchFieldError e6) {
         }
      }
   }

   public enum Action {
      SET_SIZE,
      LERP_SIZE,
      SET_CENTER,
      INITIALIZE,
      SET_WARNING_TIME,
      SET_WARNING_BLOCKS;
   }
}
