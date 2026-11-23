package net.augustus.modules.latency;

import net.minecraft.util.MovingObjectPosition;
import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.ClickListener;
import net.augustus.events.EventClick;
import net.augustus.events.EventPacketDelay;
import net.augustus.events.listeners.PacketDelayListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.utils.TimeHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;

public class TestLatencyModule extends Module implements PacketDelayListener, TickListener, ClickListener {
   private EntityLivingBase targetEntity;
   private boolean delayingOutgoing;
   private int remainingTicks;
   private boolean pendingExtraClick;
   private boolean unusedFlag;
   private final TimeHelper timeHelper;

   public TestLatencyModule() {
       super("TestLatencyModule", "TestModul", Color.red, Categorys.LATENCY);
      this.delayingOutgoing = false;
      this.remainingTicks = 0;
      this.pendingExtraClick = false;
      this.unusedFlag = false;
      this.timeHelper = new TimeHelper();
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      byte b = 25;
      this.toggleEventSubscription(b);
      byte b2 = 40;
      this.toggleEventSubscription(b2);
      byte b3 = 7;
      this.toggleEventSubscription(b3);
   }

   public void onPacketDelay(EventPacketDelay event) {
      Packet packet = event.getPacket();
      if (this.delayingOutgoing) {
         boolean flag = true;
         event.setDelayed(flag);
      }

      if (packet instanceof C02PacketUseEntity) {
         C02PacketUseEntity packet2 = (C02PacketUseEntity)packet;
         if (packet2.getAction() == C02PacketUseEntity.Action.ATTACK) {
            Entity entityFromWorld = packet2.getEntityFromWorld(mc.theWorld);
            if (entityFromWorld instanceof EntityLivingBase) {
               this.targetEntity = (EntityLivingBase)entityFromWorld;
               this.timeHelper.reset();
               if (this.targetEntity.hurtTime <= 1 && this.remainingTicks <= 0 && !event.isDelayed()) {
                  this.remainingTicks = 10;
                  this.pendingExtraClick = true;
               }
            }
         }
      }
   }

   public void onTick() {
      long l = 2000L;
      if (this.timeHelper.reached2(l)) {
         this.targetEntity = null;
      }

      if (this.targetEntity != null) {
         this.remainingTicks--;
      }
   }

   public void onTick4() {
      if (this.targetEntity != null) {
         if (this.remainingTicks <= 0 && (this.delayingOutgoing || this.pendingExtraClick)) {
            this.pendingExtraClick = false;
            this.delayingOutgoing = false;
            this.unusedFlag = false;
         }
      }
   }

   public void onClick(EventClick event) {
      if (this.targetEntity != null && this.remainingTicks > 0) {
         if (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY || !mc.objectMouseOver.entityHit.equals(this.targetEntity)) {
            this.delayingOutgoing = true;
         } else if (this.pendingExtraClick && !this.delayingOutgoing) {
            byte b = 1;
            event.addLeftClicks(b);
            boolean flag = true;
            event.setBreakingBlock(flag);
            boolean flag2 = true;
            event.setCriticalHit(flag2);
            this.delayingOutgoing = true;
         }
      }
   }

}
