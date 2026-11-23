package net.augustus.modules.player;

import java.awt.Color;
import java.util.Comparator;
import java.util.OptionalInt;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.ClickListener;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.events.EventClick;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.events.EventLoadWorld;
import net.augustus.events.EventAttackEntity;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.utils.simulation.ProjectileSimulator;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.listeners.AttackEntityListener;
import net.augustus.utils.player.ContainerSlotQuery;
import net.augustus.utils.player.SilentSlotManager;
import net.augustus.utils.rotation.Rotation;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class CombatPearl extends Module implements TickListener, AttackEntityListener, LoadWorldListener, SilentSlotListener, ClickListener {
   private final StringValue minDistance;
   private final StringValue minHealth;
   private final BooleanValue silentSlotSwitching;
   private EntityLivingBase lastAttackedTarget;
   private Rotation throwRotation;
   private int aimTicks;
   private int pearlHotbarSlot;
   private EntityEnderPearl trackedEnemyPearl;

   public CombatPearl() {
       super("CombatPearl", "CombatPearl", new Color(238, 255, 194), Categorys.PLAYER);
      this.minDistance = new StringValue(this, "MinDistance", 10.0, 0.0, 20.0, 0);
      this.minHealth = new StringValue(this, "MinHealth", 4.0, 0.0, 20.0, 0);
      this.silentSlotSwitching = new BooleanValue(this, "SilentSlotSwitching", true);
      this.throwRotation = null;
      this.aimTicks = 0;
      this.trackedEnemyPearl = null;
   }

   @Override
   public void onEnable() {
      this.resetPearlState();
   }

   @Override
   public void onDisable() {
      this.lastAttackedTarget = null;
   }

   @Override
   public void updateEventSubscriptions() {
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
   }

   @Override
   public void onTick() {
      this.pearlHotbarSlot = this.findPearlHotbarSlot();
      if (this.pearlHotbarSlot < 0) {
         this.resetPearlState();
      } else if (mc.isNotInGui()) {
         if (!((double)mc.thePlayer.getCurrentHealth() < this.minHealth.getValue())) {
            if (this.throwRotation != null) {
               byte b = 0;
               Rotation throwRotation2 = this.throwRotation;
               ClientManagers.rotationController.requestRotation(throwRotation2, b, this);
               this.aimTicks++;
            } else {
               EntityEnderPearl entityEnderPearl = this.findEnemyThrownPearl();
               if (entityEnderPearl != null && this.trackedEnemyPearl == null) {
                  this.trackedEnemyPearl = entityEnderPearl;
               }

               if (this.trackedEnemyPearl != null) {
                  if (this.trackedEnemyPearl.ticksExisted == 0) {
                     double distanceToEntityBox = (double)mc.thePlayer.getDistanceToEntityBox(this.trackedEnemyPearl);
                     double distanceToEntityBox2 = (double)this.lastAttackedTarget.getDistanceToEntityBox(this.trackedEnemyPearl);
                     if (distanceToEntityBox < distanceToEntityBox2) {
                        this.resetPearlState();
                        return;
                     }
                  }

                  if (this.trackedEnemyPearl.ticksExisted == 3) {
                     ProjectileSimulator projectileSimulator = new ProjectileSimulator();
                     EntityEnderPearl trackedEnemyPearl2 = this.trackedEnemyPearl;
                     projectileSimulator.initFromThrowable(trackedEnemyPearl2);
                     short s = 300;
                     projectileSimulator.simulateThrowableTrajectory(s);
                     MovingObjectPosition hitResult = projectileSimulator.getHitResult();
                     if (hitResult == null || hitResult.hitVec == null) {
                        this.resetPearlState();
                        return;
                     }

                     Vec3 hitVec = hitResult.hitVec;
                     if (mc.thePlayer.getDistance(hitVec.xCoord, hitVec.yCoord, hitVec.zCoord) < this.minDistance.getValue()) {
                        this.resetPearlState();
                        return;
                     }

                     Vec3 hitVec2 = hitResult.hitVec;
                     Rotation rotation = this.calculateInterceptRotation(hitVec2);
                     if (rotation.yaw == 0.0F && rotation.pitch == 0.0F) {
                        this.resetPearlState();
                        return;
                     }

                     Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
                     this.throwRotation = RotationUtil.applyGcdFix(rotation, previousRotation);
                  }

                  if (this.throwRotation != null) {
                     byte b2 = 0;
                     Rotation throwRotation3 = this.throwRotation;
                     ClientManagers.rotationController.requestRotation(throwRotation3, b2, this);
                     this.aimTicks++;
                  }
               }
            }
         }
      }
   }

   @Override
   public void onSilentSlot() {
      if (mc.isNotInGui()) {
         if (this.throwRotation != null) {
            int pearlHotbarSlot2 = this.pearlHotbarSlot;
            boolean value = this.silentSlotSwitching.getValue();
            int n = pearlHotbarSlot2;
            byte b = 0;
            SilentSlotManager.requestSlot(this, b, n, value);
         }
      }
   }

   @Override
   public void onClick(EventClick event) {
      if (mc.isNotInGui()) {
         if (this.throwRotation != null && this.aimTicks > 2) {
            byte b = 1;
            event.addRightClicks(b);
            this.resetPearlState();
         }
      }
   }

   @Override
   public void onAttackEntity(EventAttackEntity event) {
      Entity target = event.getTarget();
      if (target instanceof EntityLivingBase) {
         this.lastAttackedTarget = (EntityLivingBase)target;
      }
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      this.lastAttackedTarget = null;
   }

   private void resetPearlState() {
      this.throwRotation = null;
      this.aimTicks = 0;
      this.trackedEnemyPearl = null;
   }

   private int findPearlHotbarSlot() {
      ContainerSlotQuery containerSlotQuery = ContainerSlotQuery.of(mc.thePlayer.inventoryContainer);
      byte b = 36;
      containerSlotQuery = containerSlotQuery.fromSlot(b);
      Item enderPearl = Items.ender_pearl;
      OptionalInt optionalInt = containerSlotQuery.filterItemExact(enderPearl).findFirstSlotIndex();
      return !optionalInt.isPresent() ? -1 : optionalInt.getAsInt() - 36;
   }

   private EntityEnderPearl findEnemyThrownPearl() {
      return this.lastAttackedTarget == null ? null : (EntityEnderPearl)mc.theWorld.loadedEntityList.stream().filter(obj -> {
         return obj instanceof EntityEnderPearl && ((Entity)obj).ticksExisted == 0;
      }).min(Comparator.comparingDouble(obj2 -> (double)this.lastAttackedTarget.getDistanceToEntityBox((Entity)obj2))).orElse(null);
   }

   private Rotation calculateInterceptRotation(Vec3 vec) {
      float f = 0.0F;
      float f2 = 0.0F;
      double[] doubles = PlayerUtil.predictPlayerPosition(0, mc.thePlayer);
      double d = vec.xCoord - doubles[0];
      double d2 = vec.yCoord - (doubles[1] + (double)mc.thePlayer.getEyeHeight());
      double d3 = vec.zCoord - doubles[2];
      float f3 = (float)Math.toDegrees(Math.atan2(d3, d)) - 90.0F;
      float f4 = (float)(-(MathHelper.atan2(d2, (double)MathHelper.sqrt_double(d * d + d3 * d3)) * 180.0 / Math.PI));
      float f5 = RotationUtil.updateAngle(ClientManagers.rotationController.getPreviousRotation().yaw, f3, 180.0F);

      for (float i = 0.0F; i < 60.0F; i += 0.2F) {
         boolean flag = false;

         for (int j = -1; j < 2; j += 2) {
            float f6 = RotationUtil.updateAngle(ClientManagers.rotationController.getPreviousRotation().pitch, f4 + i * (float)j, 180.0F);
            ProjectileSimulator projectileSimulator = new ProjectileSimulator();
            EntityPlayerSP thePlayer = mc.thePlayer;
            ItemStack stack = new ItemStack(Items.ender_pearl);
            double d4 = doubles[0];
            double d5 = doubles[1];
            double d6 = doubles[2];
            double d7 = d5;
            double d8 = d4;
            ItemStack stack2 = stack;
            EntityPlayerSP player = thePlayer;
            projectileSimulator.initFromPosAndAngles(player, stack2, f5, f6, d8, d7, d6);
            EntityLivingBase lastAttackedTarget2 = this.lastAttackedTarget;
            AxisAlignedBB box = new AxisAlignedBB(
               vec.xCoord - 0.5, vec.yCoord - 1.0, vec.zCoord - 0.5, vec.xCoord + 0.5, vec.yCoord + 1.0, vec.zCoord + 0.5
            );
            EntityLivingBase entity = lastAttackedTarget2;
            short s = 200;
            int n = projectileSimulator.simulateThrowableHitTick(s, entity, box);
            if (n != Integer.MAX_VALUE) {
               flag = true;
               f = f5;
               f2 = f6;
               break;
            }
         }

         if (flag) {
            break;
         }
      }

      return new Rotation(f, f2);
   }

}
