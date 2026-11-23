package net.augustus.quests;

import net.augustus.events.EventAttackEntity;
import net.augustus.events.EventManager;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.AttackEntityListener;
import net.augustus.events.listeners.PacketListener;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S19PacketEntityStatus;

public class AttackPlayersQuestObjective extends QuestObjective implements AttackEntityListener, PacketListener {
   private boolean canCountHit;
   private int lastTargetEntityId;

   @Override
   public String getActionVerb() {
      return "Attack";
   }

   public AttackPlayersQuestObjective(int n) {
       super((double)n);
      this.canCountHit = true;
      this.lastTargetEntityId = -1;
   }

   public void onAttackEntity(EventAttackEntity event) {
      if (event.getTarget() instanceof EntityLivingBase) {
         if (this.canCountHit) {
            double d = 1.0;
            this.addProgress(d);
            this.canCountHit = false;
         }

         this.lastTargetEntityId = event.getTarget().getEntityId();
      }
   }

   @Override
   public void onActivate() {
      EventManager.getEventBus().subscribe(1, this);
      EventManager.getEventBus().subscribe(26, this);
      this.canCountHit = true;
   }

   @Override
   public String getUnitLabel() {
      return "players";
   }

   public void onPacket(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof S19PacketEntityStatus) {
         S19PacketEntityStatus packet2 = (S19PacketEntityStatus)packet;
         if (this.lastTargetEntityId == packet2.getEntityId() && 2 == packet2.getOpCode()) {
            this.canCountHit = true;
         }
      }

      if (packet instanceof S0BPacketAnimation) {
         S0BPacketAnimation packet3 = (S0BPacketAnimation)packet;
         if (this.lastTargetEntityId == packet3.getEntityID() && 1 == packet3.getAnimationType()) {
            this.canCountHit = true;
         }
      }
   }

   @Override
   public void onDeactivate() {
      EventManager.getEventBus().unsubscribe(1, this);
      EventManager.getEventBus().unsubscribe(26, this);
   }

}
