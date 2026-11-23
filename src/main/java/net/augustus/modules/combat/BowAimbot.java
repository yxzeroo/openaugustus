package net.augustus.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.ToDoubleFunction;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.rotation.RotationEngine;
import net.augustus.events.listeners.ClickListener;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.utils.rotation.HitboxAngleUtil;
import net.augustus.events.EventMove;
import net.augustus.utils.simulation.TrajectoryPredictionUtil;
import net.augustus.ClientManagers;
import net.augustus.events.EventClick;
import net.augustus.utils.rotation.MoveFixMode;
import net.augustus.events.listeners.MoveListener;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.utils.combat.EntityTargetFilter;
import net.augustus.events.EventLoadWorld;
import net.augustus.utils.TuplePair;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.utils.combat.TargetFilterPresets;
import net.augustus.utils.combat.EntityTargetSelector;
import net.augustus.events.EventMoveInput;
import net.augustus.Augustus;
import net.augustus.utils.simulation.ProjectileSimulator;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.TargetSelectionSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.rotation.Rotation;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class BowAimbot extends Module implements MoveListener, ClickListener, TickListener, MoveInputListener, LoadWorldListener {
   private final BooleansSetting mode;
   public final DoubleValue rotations;
   public final StringValue fovYaw;
   public final StringValue fovPitch;
   public final BooleansSetting moveFix;
   public final BooleanValue moveFixIntave;
   public final BooleanValue autoShoot;
   public final BooleanValue noHorizontalBruteforce;
   private final TargetSelectionSettings targetSelection;
   private EntityLivingBase target;
   private long aimTicks;
   private boolean hasValidTrajectory;
   private boolean wasFullyCharged;
   private final RotationEngine rotationEngine;

   public BowAimbot() {
       super("BowAimbot", "BogenZiehlhilfe", new Color(12, 219, 77), Categorys.COMBAT);
      this.mode = new BooleansSetting(this, "Mode", "Old", new String[]{"Old", "New"});
      this.rotations = new DoubleValue(this, "Rotations", true);
      this.fovYaw = (StringValue)(new StringValue(this, "FovYaw", 90.0, 0.0, 180.0, 0)).markVisualOnly();
      this.fovPitch = (StringValue)(new StringValue(this, "FovPitch", 90.0, 0.0, 180.0, 0)).markVisualOnly();
      BooleansSetting booleansSetting = new BooleansSetting(this, "MoveFix", "Default", new String[]{"None", "Default", "Advanced"});
      String str = "SilentMoveFix";
      this.moveFix = (BooleansSetting)booleansSetting.withDisplayName(str);
      BooleanValue booleanValue = new BooleanValue(this, "MoveFixIntave", true);
      SettingVisibilityCondition condition = () -> {
         return !ClientManagers.moduleManager.scaffold.moveFix.getSelectedValue().equalsIgnoreCase("None");
      };
      this.moveFixIntave = (BooleanValue)booleanValue.withVisibility(condition);
      this.autoShoot = new BooleanValue(this, "AutoShoot", true);
      BooleanValue booleanValue2 = new BooleanValue(this, "No horizontal bruteforce", false);
      SettingVisibilityCondition condition2 = () -> this.isNewModeSelected();
      this.noHorizontalBruteforce = (BooleanValue)booleanValue2.withVisibility(condition2);
      this.targetSelection = (new TargetSelectionSettings(this, TargetFilterPresets.createCombatFilter())).withSorting();
      this.target = null;
      this.aimTicks = 0L;
      this.hasValidTrajectory = false;
      this.wasFullyCharged = false;
      this.rotationEngine = new RotationEngine(this, this.rotations);
   }

   @Override
   public void onEnable() {
      this.aimTicks = 0L;
   }

   @Override
   public void onDisable() {
      this.target = null;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 28;
      this.toggleEventSubscription(b);
      byte b2 = 7;
      this.toggleEventSubscription(b2);
      byte b3 = 40;
      this.toggleEventSubscription(b3);
      byte b4 = 21;
      this.toggleEventSubscription(b4);
      byte b5 = 17;
      this.toggleEventSubscription(b5);
   }

   @Override
   public void onTick() {
      EntityLivingBase target2 = this.target;
      this.targetSelection.setCurrentTarget(target2);
      this.target = null;
      this.hasValidTrajectory = false;
      if (this.canAimBow()) {
         this.target = this.findTarget();
         if (this.target != null) {
            int n = 0;
            float f = 0.0F;
            float f2 = 0.0F;
            if (this.mode.getSelectedValue().equals("New")) {
               EntityLivingBase target3 = this.target;
               ItemStack currentEquippedItem = mc.thePlayer.getCurrentEquippedItem();
               byte b = 4;
               ItemStack stack = currentEquippedItem;
               EntityLivingBase entity = target3;
               TuplePair tuplePair = this.calculateTrajectoryAngles(entity, stack, b);
               n = (Integer)tuplePair.getFirst();
               f = ((float[])tuplePair.getSecond())[1];
               f2 = ((float[])tuplePair.getSecond())[0];
               if ((Integer)tuplePair.getFirst() == -1) {
                  this.aimTicks = 0L;
                  return;
               }

               this.hasValidTrajectory = true;
            } else {
               double distanceToEntity = (double)mc.thePlayer.getDistanceToEntity(this.target);
               n = (int)(distanceToEntity / 1.75);
               int n2 = mc.thePlayer.ticksExisted + n;
               boolean flag = true;
               int n3 = n2;
               int ticksExisted = mc.thePlayer.ticksExisted;
               EntityLivingBase target4 = this.target;
               ArrayList list = TrajectoryPredictionUtil.predictTargetPositions(mc.thePlayer, target4, ticksExisted, n3, flag);
               int n4 = 999999;
               Vec3 clampedPosition = TrajectoryPredictionUtil.getClampedPosition(list, n4);
               double distanceToVec = mc.thePlayer.getDistanceToVec(clampedPosition);
               double d = distanceToVec - distanceToEntity;
               if (d > 3.0) {
                  if (distanceToEntity > 80.0) {
                     n = (int)(distanceToEntity / 1.25);
                  } else {
                     n = (int)(distanceToEntity / 1.5);
                  }
               } else if (d < 3.0) {
                  n = (int)(distanceToEntity / 1.8);
               }

               int n5 = mc.thePlayer.ticksExisted + n;
               int ticksExisted2 = mc.thePlayer.ticksExisted;
               EntityLivingBase target5 = this.target;
               ArrayList list2 = TrajectoryPredictionUtil.predictTargetPositions(mc.thePlayer, target5, ticksExisted2, n5);
               int n6 = 999999;
               clampedPosition = TrajectoryPredictionUtil.getClampedPosition(list2, n6);
               double d2 = clampedPosition.xCoord - mc.thePlayer.posX;
               double d3 = clampedPosition.yCoord + (double)this.target.getEyeHeight() - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
               double d4 = clampedPosition.zCoord - mc.thePlayer.posZ;
               f2 = (float)Math.toDegrees(Math.atan2(d4, d2)) - 90.0F;
               double d5 = (double)MathHelper.sqrt_double(d2 * d2 + d4 * d4);
               if (distanceToEntity > 30.0) {
                  distanceToEntity += (distanceToEntity - 30.0) * 0.75;
               }

               f = (float)(-(Math.atan2(d3, d5) * 180.0 / Math.PI)) - (float)distanceToEntity * 0.11F;
            }

            RotationEngine rotationEngine2 = this.rotationEngine;
            Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
            Rotation rotation = rotationEngine2.rotateTowards(f2, f, previousRotation);
            byte b2 = 4;
            ClientManagers.rotationController.requestRotation(rotation, b2, this);
            this.aimTicks++;
         } else {
            this.aimTicks = 0L;
         }
      } else {
         this.aimTicks = 0L;
      }
   }

   @Override
   public void onClick(EventClick event) {
      if (this.canAimBow() && this.target != null && this.autoShoot.getValue()) {
         BowAimbot bowAimbot = ClientManagers.moduleManager.bowAimbot;
         if (ClientManagers.rotationController.isOwnedBy(bowAimbot) && this.aimTicks > 1L && this.shouldReleaseBow()) {
            boolean flag = true;
            event.setBlockAllClicks(flag);
         }
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (this.canAimBow() && !this.moveFix.getSelectedValue().equals("None") && ClientManagers.rotationController.isOwnedBy(this) && this.target != null) {
         BowAimbot bowAimbot = ClientManagers.moduleManager.bowAimbot;
         if (ClientManagers.rotationController.isOwnedBy(bowAimbot)) {
            if (this.moveFix.getSelectedValue().equals("Advanced")) {
               MoveFixMode advanced = MoveFixMode.ADVANCED;
               event.setMoveFixMode(advanced);
            } else {
               MoveFixMode moveFixMode = MoveFixMode.DEFAULT;
               event.setMoveFixMode(moveFixMode);
            }

            if (this.moveFixIntave.getValue()) {
               boolean flag = true;
               event.setPreventInstantReverse(flag);
            }
         }
      }
   }

   @Override
   public void onMove(EventMove event) {
      if (this.canAimBow() && this.moveFix.getSelectedValue().equals("None") && this.target != null) {
         BowAimbot bowAimbot = ClientManagers.moduleManager.bowAimbot;
         if (ClientManagers.rotationController.isOwnedBy(bowAimbot)) {
            float realYaw = Augustus.getInstance().getYawPitchHelper().realYaw;
            event.setYaw(realYaw);
         }
      }
   }

   @Override
   public void onMove2(EventMove event) {
      if (this.canAimBow() && this.moveFix.getSelectedValue().equals("None") && this.target != null) {
         BowAimbot bowAimbot = ClientManagers.moduleManager.bowAimbot;
         if (ClientManagers.rotationController.isOwnedBy(bowAimbot)) {
            float realYaw = Augustus.getInstance().getYawPitchHelper().realYaw;
            event.setYaw(realYaw);
         }
      }
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      this.target = null;
      Object obj = null;
      this.targetSelection.setCurrentTarget((Entity)obj);
   }

   private boolean hasArrowsInInventory() {

      for (int i = 8; i < mc.thePlayer.inventoryContainer.inventorySlots.size(); i++) {
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
         if (stack != null) {
            Item item = stack.getItem();
            if (item.equals(Items.arrow)) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean canAimBow() {
      return mc.thePlayer != null
         && mc.theWorld != null
         && mc.isNotInGui()
         && mc.gameSettings.keyBindUseItem.isKeyDown()
         && mc.thePlayer.getCurrentEquippedItem() != null
         && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow
         && this.hasArrowsInInventory();
   }

   private boolean shouldReleaseBow() {
      if (!this.hasValidTrajectory && this.mode.getSelectedValue().equals("New")) {
         return false;
      } else {
         ItemStack currentEquippedItem = mc.thePlayer.getCurrentEquippedItem();
         if (currentEquippedItem != null) {
            ItemBow item = (ItemBow)currentEquippedItem.getItem();
            int n = item.getMaxItemUseDuration(currentEquippedItem) - mc.thePlayer.getItemInUseCount();
            if (n >= 62000 && this.target != null) {
               mc.gameSettings.keyBindUseItem.setPressTime(1);
               this.wasFullyCharged = false;
               return false;
            }

            float f = (float)n / 20.0F;
            f = (f * f + f * 2.0F) / 3.0F;
            if (f > 1.0F) {
               boolean wasFullyCharged2 = this.wasFullyCharged;
               this.wasFullyCharged = true;
               if (!wasFullyCharged2) {
                  return false;
               }

               return true;
            }
         }

         this.wasFullyCharged = false;
         return false;
      }
   }

   public EntityLivingBase getTarget() {
      return this.target;
   }

   private TuplePair calculateTrajectoryAngles(EntityLivingBase entity, ItemStack stack, int n) {
      int n2 = -1;
      float f = 0.0F;
      float f2 = 0.0F;
      ArrayList list = new ArrayList();
      int n3 = 1;

      for (int i = 0; i < n; i++) {
         double d = entity.posX - mc.thePlayer.posX;
         double d2 = entity.posY - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
         double d3 = entity.posZ - mc.thePlayer.posZ;
         ArrayList list2;
         if (n3 <= list.size()) {
            list2 = list;
         } else {
            int n4 = mc.thePlayer.ticksExisted + n3;
            boolean flag = true;
            int n5 = n4;
            int ticksExisted = mc.thePlayer.ticksExisted;
            list2 = list = TrajectoryPredictionUtil.predictTargetPositions(mc.thePlayer, entity, ticksExisted, n5, flag);
         }

         Vec3 clampedPosition = TrajectoryPredictionUtil.getClampedPosition(list2, n3);
         Vec3 positionEyes = mc.thePlayer.getPositionEyes(1.0F);
         d = clampedPosition.xCoord - mc.thePlayer.posX;
         d2 = clampedPosition.yCoord + (double)(entity.height / 2.0F) - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
         d3 = clampedPosition.zCoord - mc.thePlayer.posZ;
         float f3 = (float)(-(MathHelper.atan2(d2, (double)MathHelper.sqrt_double(d * d + d3 * d3)) * 180.0 / Math.PI));
         double d4 = (double)(entity.width / 2.0F);
         AxisAlignedBB box = new AxisAlignedBB(
            clampedPosition.xCoord - d4,
            clampedPosition.yCoord + (double)(entity.height / 2.0F),
            clampedPosition.zCoord - d4,
            clampedPosition.xCoord + d4,
            clampedPosition.yCoord + (double)entity.height,
            clampedPosition.zCoord + d4
         );
         double[] yawRange = HitboxAngleUtil.getYawRange(positionEyes, box);
         int n6 = n3;
         boolean value = this.noHorizontalBruteforce.getValue();

         outerLoop:
         for (float j = 0.15F; j < 60.0F; j += 0.2F) {
            for (float k = 0.0F; (double)k < 0.5 && (!value || !(k > 0.0F)); k += 0.1F) {
               for (int m = -1; m <= 1; m += 2) {
                  if (m != 1 || k != 0.0F) {
                     float f4 = MathHelper.clamp_float(f3 - j, -90.0F, 90.0F);
                     double d5 = 0.5 + (double)(k * (float)m);
                     float f5 = MathHelper.wrapAngleTo180_float((float)HitboxAngleUtil.lerpRange(yawRange, d5));
                     ProjectileSimulator projectileSimulator = new ProjectileSimulator();
                     EntityPlayerSP thePlayer = mc.thePlayer;
                     projectileSimulator.initFromShooterAngles(thePlayer, stack, f5, f4);
                     byte b = 50;
                     int n7 = projectileSimulator.simulateArrowHitTick(b, entity, box);
                     if (n7 != Integer.MAX_VALUE) {
                        n2 = n3 = n7 + 1;
                        f2 = f5;
                        f = f4;
                        break outerLoop;
                     }
                  }
               }
            }
         }

         if (n6 == n3) {
            break;
         }
      }

      return n2 == -1 ? new TuplePair(-1, new float[]{0.0F, 0.0F}) : new TuplePair(n2, new float[]{f2, f});
   }

   private EntityLivingBase findTarget() {
      EntityTargetSelector entityTargetSelector = EntityTargetSelector.of(mc.theWorld.loadedEntityList);
      EntityTargetFilter targetFilter = this.targetSelection.getTargetFilter();
      EntityTargetFilter entityTargetFilter = targetFilter;
      entityTargetSelector = entityTargetSelector.withTargetFilter(entityTargetFilter);
      boolean flag = false;
      entityTargetSelector = entityTargetSelector.throughWalls(flag);
      double value = this.fovYaw.getValue();
      double value2 = this.fovPitch.getValue();
      double d = value;
      entityTargetSelector = entityTargetSelector.withinHitboxFov(d, value2).filterFocus();
      TargetSelectionSettings targetSelection2 = this.targetSelection;
      Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
      double d2 = -1.0;
      Rotation rotation = previousRotation;
      ToDoubleFunction toDoubleFunction = targetSelection2.createTargetComparatorKey(rotation, d2);
      Optional optional = entityTargetSelector.sortBy(toDoubleFunction).findBest();
      return optional.isPresent() && this.isLicenseTimestampValid() && optional.get() instanceof EntityLivingBase ? (EntityLivingBase)optional.get() : null;
   }

   private boolean isNewModeSelected() {
      return this.mode.getSelectedValue().equals("New");
   }

   private boolean isLicenseTimestampValid() {
      byte[] timestampBytes = LicenseTimestampStore.timestampBytes;
      boolean flag = false;
      if (timestampBytes != null) {
         long l = 0L;

         for (int i = 0; i < 8; i++) {
            l <<= 8;
            l |= (long)(timestampBytes[i] & 255);
         }

         long l2 = System.currentTimeMillis() - l;
         String str = "518400000";
         long l3 = 0L;
         int n = 0;
         int length = str.length();

         while (n < length) {
            int n2 = Character.digit(str.charAt(n++), 10);
            l3 *= 10L;
            l3 -= (long)n2;
         }

         l3 = -l3;
         flag = l2 <= l3;
      }

      return flag;
   }

}
