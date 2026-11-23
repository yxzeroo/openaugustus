package net.augustus.modules.combat;

import net.minecraft.util.MovingObjectPosition;
import net.augustus.ClientManagers;
import net.augustus.events.EventClick;
import net.augustus.events.listeners.ClickListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.rotation.Rotation;
import net.augustus.utils.world.BlockUtil;
import java.awt.Color;
import java.util.Comparator;
import net.augustus.modules.Categorys;
import net.augustus.modules.EntityRotationModule;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.network.play.client.C02PacketUseEntity;

public class AntiFireBall extends EntityRotationModule implements ClickListener, TickListener {
   public final BooleanValue noRotate;
   public final DoubleValue preAimRange;
   public final DoubleValue delay;
   public final DoubleValue range;
   public final BooleanValue slowDown;
   public final DoubleValue ticksExisted;
   public final BooleanValue notWhileDigging;
   private final TimeHelper timeHelper;
   private Object[] sortedFireballs;
   private Entity targetFireball;
   private static final long b = 79445506740855L;

   public AntiFireBall() {
       super("AntiFireBall", "GegenFeuerbälle", new Color(232, 147, 0), Categorys.COMBAT);
      this.noRotate = new BooleanValue(this, "NoRotate", true);
      this.preAimRange = new DoubleValue(this, "PreAimRange", 4.0, 0.0, 15.0, 1).visibleWhen(() -> isRotateEnabled());
      this.delay = new DoubleValue(this, "Delay", 60.0, 0.0, 400.0, 0);
      this.range = new DoubleValue(this, "Range", 4.0, 0.0, 6.0, 2).visibleWhen(() -> isNoRotateEnabled());
      this.slowDown = new BooleanValue(this, "SlowDown", true);
      this.ticksExisted = new DoubleValue(this, "TicksExisted", 10.0, 0.0, 20.0, 0);
      this.notWhileDigging = new BooleanValue(this, "NotWhileDigging", false);
      this.timeHelper = new TimeHelper();
   }

   @Override
   public void onEnable() {
      super.onEnable();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.targetFireball = null;
      this.sortedFireballs = null;
   }

   @Override
   public void updateEventSubscriptions() {
      super.updateEventSubscriptions();
      this.toggleEventSubscription((byte)7);
      this.toggleEventSubscription((byte)40);
   }

   @Override
   public void onTick() {
      long l = b ^ 34658976266996L;
      long l2 = l ^ 31567849911694L;
      l2 = l ^ 54903849138326L;
      this.sortedFireballs = null;
      this.targetFireball = null;
      this.sortedFireballs = mc.theWorld
         .loadedEntityList
         .stream()
         .filter(obj -> this.isValidFireball((Entity)obj))
         .sorted(Comparator.comparingDouble(obj2 -> (double)mc.thePlayer.getDistanceToEntity((Entity)obj2)))
         .toArray();
      if (this.sortedFireballs.length > 0) {
         this.targetFireball = (Entity)this.sortedFireballs[0];
      }

      if (!this.noRotate.getValue()) {
         if (this.canTargetFireball() && !BlockUtil.isScaffoldActiveAndValid() && ClientManagers.moduleManager.killAura.target == null) {
            Rotation rotation = this.calculateRotationToEntity(this.targetFireball);
            ClientManagers.rotationController.requestRotation(rotation, (byte)-1, this);
         }
      } else if (this.targetFireball != null && (!mc.playerController.isHittingBlock || !this.notWhileDigging.getValue()) && mc.isNotInGui()) {
         double d = mc.thePlayer.canEntityBeSeen(this.targetFireball) ? this.range.getValue() : Math.min(3.0, this.range.getValue());
         if ((double)mc.thePlayer.getDistanceToEntity(this.targetFireball) < d) {
            TimeHelper timeHelper2 = this.timeHelper;
            if (this.delay.getValue() != 0.0) {
               l2 = (long)(this.delay.getValue() + (double)DefaultRandomGenerator.instance.nextSecureInt((byte)0, (byte)60));
            } else {
               l2 = 0L;
            }

            if (timeHelper2.reached2(l2)) {
               mc.thePlayer.swingItem();
               mc.playerController.attackEntity(mc.thePlayer, this.targetFireball);
               this.timeHelper.reset();
            }
         }
      }
   }

   @Override
   public void onClick(EventClick event) {
      long l = b ^ 122345520594139L;
      long l2 = l ^ 119252263007137L;
      l2 = l ^ 45348103242546L;
      int n = (int)(l2 << 48 >>> 48);
      this.rotating = this.canTargetFireball() && !this.noRotate.getValue() && ClientManagers.rotationController.isOwnedBy(this);
      if (this.canTargetFireball() && !this.noRotate.getValue() && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
         TimeHelper timeHelper2 = this.timeHelper;
         if (this.delay.getValue() != 0.0) {
            l2 = (long)(this.delay.getValue() + (double)DefaultRandomGenerator.instance.nextSecureInt((byte)0, (byte)60));
         } else {
            l2 = 0L;
         }

         if (timeHelper2.reached2(l2)) {
            if (this.slowDown.getValue()) {
               event.addLeftClicks((byte)1);
            } else {
               event.addLeftClicks((byte)1);
               char ch = (char)n;
               Runnable runnable = () -> {
                  mc.thePlayer.swingItem();
                  if (mc.objectMouseOver.entityHit != null) {
                     mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(mc.objectMouseOver.entityHit, C02PacketUseEntity.Action.ATTACK));
                  }
               };
               event.setLeftClickAction(runnable);
            }

            this.timeHelper.reset();
         }
      }
   }

   private boolean canTargetFireball() {
      return this.targetFireball != null
         && ClientManagers.moduleManager.killAura.target == null
         && !BlockUtil.isScaffoldActiveAndValid()
         && mc.isNotInGui()
         && (!mc.playerController.isHittingBlock || !this.notWhileDigging.getValue());
   }

   private boolean isValidFireball(Entity entity) {
      return entity instanceof EntityFireball
         && (double)entity.ticksExisted > this.ticksExisted.getValue()
         && (double)mc.thePlayer.getDistanceToEntity(entity) < this.preAimRange.getValue()
         && (double)mc.thePlayer.getDistanceToEntity(entity) <= mc.thePlayer.getDistance(entity.prevPosX, entity.prevPosY, entity.prevPosZ);
   }

   private static boolean isNoRotateEnabled() {
      return ClientManagers.moduleManager.antiFireBall.noRotate.getValue();
   }

   private static boolean isRotateEnabled() {
      return !ClientManagers.moduleManager.antiFireBall.noRotate.getValue();
   }

}
