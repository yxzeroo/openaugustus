package net.augustus.modules.modes.longjump;

import net.augustus.ClientManagers;
import net.augustus.events.EventBlockBoundingBox;
import net.augustus.events.EventClick;
import net.augustus.events.EventMoveInput;
import net.augustus.events.EventPacket;
import net.augustus.events.EventPacketDelay;
import net.augustus.events.listeners.BlockBoundingBoxListener;
import net.augustus.events.listeners.ClickListener;
import net.augustus.events.listeners.PacketDelayListener;
import net.augustus.events.listeners.PacketListener;
import net.augustus.events.listeners.RenderListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.render.RenderUtil;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.input.MouseInputUtil;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.utils.rotation.Rotation;
import net.augustus.utils.rotation.RotationController;
import net.augustus.utils.rotation.RotationUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import net.augustus.modules.Module;
import net.augustus.modules.movement.LongJump;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class IntaveBoatLongJumpMode extends AbstractLongJumpMode implements RenderListener, PacketListener, BlockBoundingBoxListener, PacketDelayListener, ClickListener, TickListener {
   private final BooleanValue intaveLonger;
   private final BooleanValue displayDirection;
   private final BooleanValue rotate;
   private final BooleanValue click;
   private final StringValue clickRange;
   private final StringValue intaveStrength;
   private final BooleanValue helpMessage;
   private boolean attachedToBoat;
   private int boostTicks;
   private double glideFloorY;
   private int attachedTicks;
   private Entity targetVehicle;
   private boolean hasInteracted;

   public IntaveBoatLongJumpMode(LongJump longJump, DoubleValue doubleValue) {
       super(longJump, doubleValue, "IntaveBoat");
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "IntaveLonger", true, this.modeSetting);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.intaveLonger = (BooleanValue)booleanValue.withVisibility(condition);
      booleanValue = new BooleanValue(this.parentModule, "DisplayDirection", true, this.modeSetting);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.displayDirection = (BooleanValue)booleanValue.withVisibility(condition2);
      booleanValue = new BooleanValue(this.parentModule, "Rotate", false, this.modeSetting);
      SettingVisibilityCondition condition3 = () -> this.isSelected();
      this.rotate = (BooleanValue)booleanValue.withVisibility(condition3);
      booleanValue = new BooleanValue(this.parentModule, "Click", true, this.modeSetting);
      SettingVisibilityCondition condition4 = () -> this.isSelected();
      this.click = (BooleanValue)booleanValue.withVisibility(condition4);
      StringValue stringValue = new StringValue(this.parentModule, "ClickRange", 6.0, 3.0, 6.0, 2, this.modeSetting);
      SettingVisibilityCondition condition5 = () -> this.isClickRangeVisible();
      this.clickRange = stringValue.visibleWhen(condition5);
      StringValue stringValue2 = new StringValue(this.parentModule, "IntaveStrength", 3.0, 1.0, 3.0, 0, this.modeSetting);
      SettingVisibilityCondition condition6 = () -> this.isSelected();
      this.intaveStrength = stringValue2.visibleWhen(condition6);
      booleanValue = new BooleanValue(this.parentModule, "HelpMessage", true, this.modeSetting);
      SettingVisibilityCondition condition7 = () -> this.isSelected();
      this.helpMessage = (BooleanValue)booleanValue.withVisibility(condition7);
      this.attachedToBoat = false;
      this.boostTicks = 0;
      this.glideFloorY = -1337.0;
      this.attachedTicks = 0;
      this.targetVehicle = null;
      this.hasInteracted = false;
   }

   @Override
   public void updateEventSubscriptions() {
      super.updateEventSubscriptions();
      byte b = 32;
      this.toggleEventSubscription(b);
      byte b2 = 26;
      this.toggleEventSubscription(b2);
      byte b3 = 3;
      this.toggleEventSubscription(b3);
      byte b4 = 25;
      this.toggleEventSubscription(b4);
      byte b5 = 7;
      this.toggleEventSubscription(b5);
      byte b6 = 40;
      this.toggleEventSubscription(b6);
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new IntaveBoatLongJumpMode((LongJump)obj, (DoubleValue)obj2);
   }

   @Override
   public void onEnable() {
      this.glideFloorY = -1337.0;
      this.attachedTicks = 0;
      this.targetVehicle = null;
      this.hasInteracted = false;
      this.attachedToBoat = false;
      this.boostTicks = 0;
      if (mc.thePlayer != null && this.helpMessage.getValue()) {
         LongJump parentModule2 = (LongJump)this.parentModule;
         String str = "1. Toggle Longjump";
         parentModule2.sendChatMessage2(str);
         if (this.click.getValue()) {
            parentModule2 = (LongJump)this.parentModule;
            String str2 = "2. Left click (stay from boat as far away as possible)";
            parentModule2.sendChatMessage2(str2);
         } else {
            parentModule2 = (LongJump)this.parentModule;
            String str3 = "2. Right click boat from far away as possible";
            parentModule2.sendChatMessage2(str3);
         }

         parentModule2 = (LongJump)this.parentModule;
         String str4 = "3. Enjoy longjumping";
         parentModule2.sendChatMessage2(str4);
      }
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void onRender(float f, ScaledResolution sr) {
      if (this.displayDirection.getValue()) {
         boolean flag = true;
         if (this.targetVehicle != null && this.click.getValue() && (double)mc.thePlayer.getDistanceToEntity(this.targetVehicle) < this.clickRange.getValue()) {
            flag = false;
         } else if (!this.click.getValue() && this.targetVehicle != null && mc.objectMouseOver != null && mc.objectMouseOver.entityHit instanceof EntityBoat) {
            flag = false;
         }

         if (flag) {
            mc.fontRendererObj
               .renderString(
                  "Vehicle out of range",
                  (float)sr.getScaledWidth() / 2.0F - (float)mc.fontRendererObj.getStringWidth("Vehicle out of range") / 2.0F,
                  (float)sr.getScaledHeight() / 2.0F,
                  Color.red.getRGB(),
                  true
               );
         }

         RenderUtil.resetGlColorState();
      }
   }

   @Override
   public void onRender(float f) {
      if (this.targetVehicle != null && this.displayDirection.getValue()) {
         RenderUtil.begin3DRender();
         Vec3 vec = new Vec3(
            mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * (double)f,
            mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * (double)f + (double)mc.thePlayer.getEyeHeight() - 0.2,
            mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * (double)f
         );
         Vec3 vec2 = RotationUtil.getInterpolatedNearestPoint(this.targetVehicle, f).subtract(vec);
         vec2 = vec2.scale(-1.0);
         System.out.println(vec2.toString());
         ArrayList list = new ArrayList(2);
         list.add(vec);
         list.add(vec.add(vec2.scale(20.0)));
         double xCoord = vec2.xCoord;
         double yCoord = vec2.yCoord;
         double zCoord = vec2.zCoord;
         Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
         boolean flag = false;
         float f2 = 180.0F;
         float f3 = 180.0F;
         Rotation rotationsToPos = RotationUtil.getRotationsToPos(xCoord, yCoord, zCoord, previousRotation, f3, f2, flag);
         rotationsToPos.yaw -= 180.0F;
         RenderUtil.setGlColor2(Color.green);
         GL11.glLineWidth(4.0F);
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         Tessellator instance = Tessellator.getInstance();
         WorldRenderer worldRenderer = instance.getWorldRenderer();
         worldRenderer.begin(3, DefaultVertexFormats.POSITION);

         for (Vec3 vec3 : (Iterable<Vec3>)(list)) {
            worldRenderer.pos(
                  (double)((float)vec3.xCoord) - mc.getRenderManager().getRenderPosX(),
                  (double)((float)vec3.yCoord) - mc.getRenderManager().getRenderPosY(),
                  (double)((float)vec3.zCoord) - mc.getRenderManager().getRenderPosZ()
               )
               .endVertex();
         }

         instance.draw();
         RenderUtil.end3DRender();
      }
   }

   @Override
   public void onPacket(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof S1BPacketEntityAttach) {
         S1BPacketEntityAttach packet2 = (S1BPacketEntityAttach)packet;
         if (mc.thePlayer == null || packet2.getEntityId() == mc.thePlayer.getEntityId()) {
            event.cancel();
            this.attachedToBoat = true;
         }
      }
   }

   @Override
   public void onBlockBoundingBox(EventBlockBoundingBox event) {
      if (this.intaveLonger.getValue()) {
         if (mc.thePlayer.motionY < -0.15 && this.glideFloorY < -1000.0 && this.attachedTicks > 0) {
            this.glideFloorY = mc.thePlayer.posY - 0.5;
         }

         if ((double)event.getBlockPos().getY() < this.glideFloorY) {
            BlockPos blockPos = event.getBlockPos();
            AxisAlignedBB box = new AxisAlignedBB(
               (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), (double)(blockPos.getX() + 1), (double)(blockPos.getY() + 1), (double)(blockPos.getZ() + 1)
            );
            event.setBoundingBox(box);
         }
      }
   }

   @Override
   public void onPacketDelay(EventPacketDelay event) {
      if (this.attachedToBoat && this.boostTicks > 1 && this.boostTicks != 1001) {
         boolean flag = true;
         event.setDelayed(flag);
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (!mc.thePlayer.onGround && this.attachedTicks > 0) {
         this.glideFloorY = -1337.0;
      }

      if (this.attachedToBoat) {
         this.attachedTicks++;
         double[] doubles = PlayerUtil.predictPlayerPosition(0, mc.thePlayer);
         if (doubles[9] == 1.0 && !mc.thePlayer.onGround && this.boostTicks > 3) {
            this.boostTicks = 1000;
         }

         if (this.boostTicks < 9 && this.boostTicks > 1 + MathHelper.clamp_int((int)this.intaveStrength.getValue(), 1, 3)) {
            boolean flag = true;
            event.setJump(flag);
         }

         if (this.boostTicks > 1010 && mc.thePlayer.onGround) {
            this.attachedToBoat = false;
            this.boostTicks = 0;
            boolean flag2 = false;
            this.setEnabled(flag2);
            return;
         }

         this.boostTicks++;
      }
   }

   @Override
   public void onClick(EventClick event) {
      if (this.targetVehicle != null
         && (double)mc.thePlayer.getDistanceToEntity(this.targetVehicle) < this.clickRange.getValue()
         && !this.hasInteracted
         && MouseInputUtil.isButtonDown(0)
         && this.click.getValue()) {
         boolean flag = true;
         event.setBlockAllClicks(flag);
         boolean flag2 = true;
         event.setIgnoreVanillaClicks(flag2);
         this.hasInteracted = true;
         Vec3 nearestPointOnEntity = RotationUtil.getNearestPointOnEntity(this.targetVehicle);
         Vec3 vec = new Vec3(nearestPointOnEntity.xCoord - this.targetVehicle.posX, nearestPointOnEntity.yCoord - this.targetVehicle.posY, nearestPointOnEntity.zCoord - this.targetVehicle.posZ);
         mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(this.targetVehicle, vec));
         mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(this.targetVehicle, C02PacketUseEntity.Action.INTERACT));
      }
   }

   @Override
   public void onTick() {
      if (this.targetVehicle == null) {
         Stream stream = mc.theWorld.loadedEntityList.stream();
         Object[] array = stream.filter(obj -> {
            return (obj instanceof EntityBoat || obj instanceof EntityMinecart) && ((Entity)obj).getDistanceToEntityBox(mc.thePlayer) < 8.0F;
         }).sorted(Comparator.comparingDouble(obj2 -> (double)mc.thePlayer.getDistanceToEntityBox((Entity)obj2))).toArray();
         if (array.length > 0) {
            this.targetVehicle = (EntityBoat)array[0];
         }
      } else if (this.rotate.getValue()) {
         Vec3 nearestPointOnEntity = RotationUtil.getNearestPointOnEntity(this.targetVehicle);
         RotationController rotationController = ClientManagers.rotationController;
         double xCoord = nearestPointOnEntity.xCoord;
         double yCoord = nearestPointOnEntity.yCoord;
         double zCoord = nearestPointOnEntity.zCoord;
         Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
         boolean flag = false;
         float f = 180.0F;
         float f2 = 180.0F;
         Rotation rotationsToPos = RotationUtil.getRotationsToPos(xCoord, yCoord, zCoord, previousRotation, f2, f, flag);
         Rotation previousRotation2 = ClientManagers.rotationController.getPreviousRotation();
         Rotation rotation = RotationUtil.applyGcdFix(rotationsToPos, previousRotation2);
         Module parentModule2 = this.parentModule;
         byte b = -10;
         Rotation rotation2 = rotation;
         rotationController.requestRotation(rotation2, b, parentModule2);
      }
   }

   private boolean isClickRangeVisible() {
      return this.isSelected() && this.click.getValue();
   }

}
