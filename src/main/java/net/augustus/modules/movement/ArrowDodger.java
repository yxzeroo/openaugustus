package net.augustus.modules.movement;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.ClickListener;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.events.EventClick;
import net.augustus.settings.Setting;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.EventMoveInput;
import net.augustus.events.MoveInputStage;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.utils.simulation.ProjectileSimulator;
import net.augustus.utils.player.SilentSlotManager;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class ArrowDodger extends Module implements MoveInputListener, ClickListener, SilentSlotListener {
   private final BooleanValue doge;
   private final StringValue arrowHitboxExpand;
   private final BooleanValue releaseItemAtCollisionIncoming;
   private final BooleanValue notWhileEating;
   private final BooleanValue inInv;
   private int dodgeForward;
   private int dodgeStrafe;
   private boolean arrowIncoming;

   public ArrowDodger() {
       super("ArrowDodger", "PfeilAusweicher", new Color(49, 234, 247), Categorys.MOVEMENT);
      this.doge = (BooleanValue)(new BooleanValue(this, "Doge", true)).markVisualOnly();
      StringValue stringValue = new StringValue(this, "Arrow hitbox expand", 0.2, 0.0, 2.0, 2);
      BooleanValue doge2 = this.doge;
      this.doge.getClass();
      BooleanValue booleanValue = doge2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.arrowHitboxExpand = (StringValue)stringValue.visibleWhen(condition).markVisualOnly();
      this.releaseItemAtCollisionIncoming = (BooleanValue)(new BooleanValue(this, "Release item at collision incoming", true)).markVisualOnly();
      this.notWhileEating = (BooleanValue)(new BooleanValue(this, "NotWhileEating", false)).markVisualOnly();
      this.inInv = (BooleanValue)(new BooleanValue(this, "InInv", false)).markVisualOnly();
      this.dodgeForward = -2;
      this.dodgeStrafe = -2;
      this.arrowIncoming = false;
   }

   @Override
   public void onEnable() {
      this.arrowIncoming = false;
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 21;
      this.toggleEventSubscription(b);
      byte b2 = 7;
      this.toggleEventSubscription(b2);
      byte b3 = 34;
      this.toggleEventSubscription(b3);
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (this.dodgeForward != -2 && this.doge.getValue()) {
         float dodgeStrafe2 = (float)this.dodgeStrafe;
         event.setMoveStrafe(dodgeStrafe2);
         float dodgeForward2 = (float)this.dodgeForward;
         event.setMoveForward(dodgeForward2);
      }
   }

   @Override
   public void onClick(EventClick event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (this.releaseItemAtCollisionIncoming.getValue() && this.arrowIncoming && mc.thePlayer.getCurrentEquippedItem() != null && !SilentSlotManager.isOwnedBy(this)) {
         boolean flag = false;
         event.setAllowRightClick(flag);
         boolean flag2 = false;
         event.setRightMouseDown(flag2);
      }
   }

   @Override
   public void onSilentSlot() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if ((!this.notWhileEating.getValue() || !mc.thePlayer.isEating()) && (this.inInv.getValue() || mc.isNotInGui())) {
         this.dodgeForward = -2;
         this.dodgeStrafe = -2;
         int maxvalue = Integer.MAX_VALUE;
         ArrayList list = new ArrayList();

         for (Entity entity : (Iterable<Entity>)(mc.theWorld.loadedEntityList)) {
            if (entity instanceof EntityArrow && entity.ticksExisted >= 1) {
               ProjectileSimulator projectileSimulator = new ProjectileSimulator();
               EntityArrow entityArrow = (EntityArrow)entity;
               projectileSimulator.initFromArrow(entityArrow);
               float value = (float)this.arrowHitboxExpand.getValue();
               float f = value;
               short s = 200;
               ArrayList list2 = projectileSimulator.simulateArrowTrajectoryWithExpand(s, f);
               if (projectileSimulator.getHitResult() != null && projectileSimulator.getHitResult().entityHit instanceof EntityPlayerSP) {
                  list.add(list2);
                  if (projectileSimulator.hitTick < maxvalue) {
                     maxvalue = projectileSimulator.hitTick;
                  }
               }
            }
         }

         if (!list.isEmpty() && this.doge.getValue()) {
            list.sort(Comparator.comparingDouble(obj -> ((ArrayList)obj).size()));
            double minvalue = Double.MIN_VALUE;

            for (int i = -1; i < 2; i++) {
               for (int j = -1; j < 2; j++) {
                  EntityPlayerSP thePlayer = mc.thePlayer;
                  double posX = mc.thePlayer.posX;
                  double posY = mc.thePlayer.posY;
                  double posZ = mc.thePlayer.posZ;
                  float rotationYaw = mc.thePlayer.rotationYaw;
                  EventMoveInput event = new EventMoveInput(
                     (float)j, (float)i, mc.thePlayer.movementInput.jump, mc.thePlayer.movementInput.sneak, MoveInputStage.PRE
                  );
                  float f2 = rotationYaw;
                  double d = posZ;
                  double d2 = posY;
                  double d3 = posX;
                  double[] doubles = PlayerUtil.predictPlayerPosition(maxvalue, thePlayer, d3, d2, d, f2, event);
                  float f3 = mc.thePlayer.width / 2.0F + 0.1F;
                  AxisAlignedBB box = new AxisAlignedBB(
                     doubles[0] - (double)f3,
                     doubles[1] - 0.1F,
                     doubles[2] - (double)f3,
                     doubles[0] + (double)f3,
                     doubles[1] + (double)mc.thePlayer.height + 0.1F,
                     doubles[2] + (double)f3
                  );
                  double d4 = 0.0;

                  for (ArrayList list3 : (Iterable<ArrayList>)(list)) {
                     Vec3 vec = (Vec3)(list3.stream().min(Comparator.comparingDouble(obj2 -> ((Vec3)obj2).distanceTo(RotationUtil.clampPointToBox((Vec3)obj2, box)))).orElse(null));
                     if (vec != null) {
                        d4 += vec.distanceTo(RotationUtil.clampPointToBox(vec, box));
                     }
                  }

                  if (d4 > minvalue) {
                     this.dodgeForward = i;
                     this.dodgeStrafe = j;
                     minvalue = d4;
                  }
               }
            }
         }

         this.arrowIncoming = !list.isEmpty();
         if (this.releaseItemAtCollisionIncoming.getValue() && this.arrowIncoming && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getItemInUseCount() > 0) {
            int n = mc.thePlayer.inventory.currentItem == 8 ? mc.thePlayer.inventory.currentItem - 1 : mc.thePlayer.inventory.currentItem + 1;
            byte b = 6;
            SilentSlotManager.requestSlot(this, b, n);
         }
      } else {
         this.arrowIncoming = false;
         this.dodgeForward = -2;
      }
   }

}
