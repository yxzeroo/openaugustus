package net.augustus.modules.misc.helper;

import net.augustus.events.EventAIMoveSpeed;
import net.augustus.events.EventAttackEntity;
import net.augustus.events.EventAttackSlowdown;
import net.augustus.events.EventBlockBoundingBox;
import net.augustus.events.EventBlockChange;
import net.augustus.events.EventBlockOverlay;
import net.augustus.events.EventChat;
import net.augustus.events.EventChunkLoad;
import net.augustus.events.EventClick;
import net.augustus.events.EventEntityColor;
import net.augustus.events.EventEntityHurt;
import net.augustus.events.EventGameLoop;
import net.augustus.events.EventInventoryClick;
import net.augustus.events.EventInventoryClose;
import net.augustus.events.EventItemGlint;
import net.augustus.events.EventItemPos;
import net.augustus.events.EventKey;
import net.augustus.events.EventLadder;
import net.augustus.events.EventLeftClick;
import net.augustus.events.EventLoadWorld;
import net.augustus.events.EventMotion;
import net.augustus.events.EventMouseMove;
import net.augustus.events.EventMove;
import net.augustus.events.EventMoveEntity;
import net.augustus.events.EventMoveInput;
import net.augustus.events.EventNightVision;
import net.augustus.events.EventOrientCamera;
import net.augustus.events.EventPacket;
import net.augustus.events.EventPacketDelay;
import net.augustus.events.EventPerspective;
import net.augustus.events.EventRenderBlock;
import net.augustus.events.EventRenderEntity;
import net.augustus.events.EventRenderGuiBackground;
import net.augustus.events.EventSafeWalk;
import net.augustus.events.EventSlowDown;
import net.augustus.events.EventSound;
import net.augustus.events.EventSprint;
import net.augustus.events.EventStep;
import net.augustus.events.EventSwim;
import net.augustus.events.EventSwing;
import net.augustus.events.EventTimer;
import net.augustus.events.EventTimerUpdate;
import net.augustus.events.EventUpdate;
import net.augustus.events.EventWebSlowDown;
import net.augustus.events.EventWorldColor;
import net.augustus.events.EventWorldTime;
import net.augustus.events.EventZoom;
import net.augustus.events.listeners.AIMoveSpeedListener;
import net.augustus.events.listeners.AttackEntityListener;
import net.augustus.events.listeners.AttackSlowdownListener;
import net.augustus.events.listeners.BlockBoundingBoxListener;
import net.augustus.events.listeners.BlockChangeListener;
import net.augustus.events.listeners.BlockOverlayListener;
import net.augustus.events.listeners.ChatListener;
import net.augustus.events.listeners.ChunkLoadListener;
import net.augustus.events.listeners.ClickGuiListener;
import net.augustus.events.listeners.ClickListener;
import net.augustus.events.listeners.DownloadTerrainListener;
import net.augustus.events.listeners.DropItemListener;
import net.augustus.events.listeners.EntityColorListener;
import net.augustus.events.listeners.EntityHurtListener;
import net.augustus.events.listeners.GameLoopListener;
import net.augustus.events.listeners.GuiScaleListener;
import net.augustus.events.listeners.InventoryClickListener;
import net.augustus.events.listeners.InventoryCloseListener;
import net.augustus.events.listeners.ItemGlintListener;
import net.augustus.events.listeners.ItemPosListener;
import net.augustus.events.listeners.KeyListener;
import net.augustus.events.listeners.LadderListener;
import net.augustus.events.listeners.LeftClickListener;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.events.listeners.MotionListener;
import net.augustus.events.listeners.MouseMoveListener;
import net.augustus.events.listeners.MoveEntityListener;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.MoveListener;
import net.augustus.events.listeners.NightVisionListener;
import net.augustus.events.listeners.NoClipListener;
import net.augustus.events.listeners.OrientCameraListener;
import net.augustus.events.listeners.PacketDelayListener;
import net.augustus.events.listeners.PacketListener;
import net.augustus.events.listeners.PerspectiveListener;
import net.augustus.events.listeners.RayCastListener;
import net.augustus.events.listeners.RenderBlockListener;
import net.augustus.events.listeners.RenderEntityListener;
import net.augustus.events.listeners.RenderGuiBackgroundListener;
import net.augustus.events.listeners.RenderListener;
import net.augustus.events.listeners.SafeWalkListener;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.events.listeners.SlowDownListener;
import net.augustus.events.listeners.SoundListener;
import net.augustus.events.listeners.SprintListener;
import net.augustus.events.listeners.StepListener;
import net.augustus.events.listeners.SwimListener;
import net.augustus.events.listeners.SwingListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.listeners.TimerListener;
import net.augustus.events.listeners.TimerUpdateListener;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.events.listeners.WebSlowDownListener;
import net.augustus.events.listeners.WindowResizeListener;
import net.augustus.events.listeners.WorldColorListener;
import net.augustus.events.listeners.WorldTimeListener;
import net.augustus.events.listeners.ZoomListener;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.modules.misc.Dummy;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.gui.ScaledResolution;

public class EventWarmupDummyModule
   extends Dummy
   implements AIMoveSpeedListener,
   AttackEntityListener,
   AttackSlowdownListener,
   BlockBoundingBoxListener,
   BlockOverlayListener,
   BlockChangeListener,
   ChatListener,
   ChunkLoadListener,
   ClickGuiListener,
   ClickListener,
   DownloadTerrainListener,
   DropItemListener,
   EntityColorListener,
   GuiScaleListener,
   EntityHurtListener,
   InventoryClickListener,
   InventoryCloseListener,
   ItemGlintListener,
   ItemPosListener,
   KeyListener,
   LadderListener,
   GameLoopListener,
   LeftClickListener,
   LoadWorldListener,
   MotionListener,
   MouseMoveListener,
   MoveEntityListener,
   MoveInputListener,
   NightVisionListener,
   SlowDownListener,
   WebSlowDownListener,
   OrientCameraListener,
   PacketDelayListener,
   PacketListener,
   PerspectiveListener,
   NoClipListener,
   MoveListener,
   RayCastListener,
   RenderBlockListener,
   RenderEntityListener,
   RenderGuiBackgroundListener,
   RenderListener,
   SafeWalkListener,
   SilentSlotListener,
   SoundListener,
   SprintListener,
   StepListener,
   SwimListener,
   SwingListener,
   TickListener,
   TimerListener,
   TimerUpdateListener,
   UpdateListener,
   WindowResizeListener,
   WorldColorListener,
   WorldTimeListener,
   ZoomListener {
   private boolean warmupActive;
   private final TimeHelper timeHelper;

   public EventWarmupDummyModule() {
       super();
      this.warmupActive = false;
      this.timeHelper = new TimeHelper();
   }

   public void setWarmupActive(boolean flag) {
      this.timeHelper.reset();
      this.updateEventSubscriptions();
      this.warmupActive = flag;
   }

   public void updateEventSubscriptions() {
      byte b = 0;
      ((Module)this).toggleEventSubscription(b);
      byte b2 = 1;
      ((Module)this).toggleEventSubscription(b2);
      byte b3 = 2;
      ((Module)this).toggleEventSubscription(b3);
      byte b4 = 3;
      ((Module)this).toggleEventSubscription(b4);
      byte b5 = 4;
      ((Module)this).toggleEventSubscription(b5);
      byte b6 = 55;
      ((Module)this).toggleEventSubscription(b6);
      byte b7 = 5;
      ((Module)this).toggleEventSubscription(b7);
      byte b8 = 56;
      ((Module)this).toggleEventSubscription(b8);
      byte b9 = 6;
      ((Module)this).toggleEventSubscription(b9);
      byte b10 = 7;
      ((Module)this).toggleEventSubscription(b10);
      byte b11 = 8;
      ((Module)this).toggleEventSubscription(b11);
      byte b12 = 9;
      ((Module)this).toggleEventSubscription(b12);
      byte b13 = 10;
      ((Module)this).toggleEventSubscription(b13);
      byte b14 = 11;
      ((Module)this).toggleEventSubscription(b14);
      byte b15 = 50;
      ((Module)this).toggleEventSubscription(b15);
      byte b16 = 51;
      ((Module)this).toggleEventSubscription(b16);
      byte b17 = 52;
      ((Module)this).toggleEventSubscription(b17);
      byte b18 = 12;
      ((Module)this).toggleEventSubscription(b18);
      byte b19 = 13;
      ((Module)this).toggleEventSubscription(b19);
      byte b20 = 14;
      ((Module)this).toggleEventSubscription(b20);
      byte b21 = 15;
      ((Module)this).toggleEventSubscription(b21);
      byte b22 = 16;
      ((Module)this).toggleEventSubscription(b22);
      byte b23 = 53;
      ((Module)this).toggleEventSubscription(b23);
      byte b24 = 17;
      ((Module)this).toggleEventSubscription(b24);
      byte b25 = 18;
      ((Module)this).toggleEventSubscription(b25);
      byte b26 = 19;
      ((Module)this).toggleEventSubscription(b26);
      byte b27 = 20;
      ((Module)this).toggleEventSubscription(b27);
      byte b28 = 21;
      ((Module)this).toggleEventSubscription(b28);
      byte b29 = 22;
      ((Module)this).toggleEventSubscription(b29);
      byte b30 = 23;
      ((Module)this).toggleEventSubscription(b30);
      byte b31 = 49;
      ((Module)this).toggleEventSubscription(b31);
      byte b32 = 24;
      ((Module)this).toggleEventSubscription(b32);
      byte b33 = 25;
      ((Module)this).toggleEventSubscription(b33);
      byte b34 = 26;
      ((Module)this).toggleEventSubscription(b34);
      byte b35 = 48;
      ((Module)this).toggleEventSubscription(b35);
      byte b36 = 27;
      ((Module)this).toggleEventSubscription(b36);
      byte b37 = 28;
      ((Module)this).toggleEventSubscription(b37);
      byte b38 = 29;
      ((Module)this).toggleEventSubscription(b38);
      byte b39 = 30;
      ((Module)this).toggleEventSubscription(b39);
      byte b40 = 31;
      ((Module)this).toggleEventSubscription(b40);
      byte b41 = 54;
      ((Module)this).toggleEventSubscription(b41);
      byte b42 = 32;
      ((Module)this).toggleEventSubscription(b42);
      byte b43 = 33;
      ((Module)this).toggleEventSubscription(b43);
      byte b44 = 34;
      ((Module)this).toggleEventSubscription(b44);
      byte b45 = 35;
      ((Module)this).toggleEventSubscription(b45);
      byte b46 = 36;
      ((Module)this).toggleEventSubscription(b46);
      byte b47 = 37;
      ((Module)this).toggleEventSubscription(b47);
      byte b48 = 38;
      ((Module)this).toggleEventSubscription(b48);
      byte b49 = 39;
      ((Module)this).toggleEventSubscription(b49);
      byte b50 = 40;
      ((Module)this).toggleEventSubscription(b50);
      byte b51 = 41;
      ((Module)this).toggleEventSubscription(b51);
      byte b52 = 42;
      ((Module)this).toggleEventSubscription(b52);
      byte b53 = 43;
      ((Module)this).toggleEventSubscription(b53);
      byte b54 = 47;
      ((Module)this).toggleEventSubscription(b54);
      byte b55 = 44;
      ((Module)this).toggleEventSubscription(b55);
      byte b56 = 45;
      ((Module)this).toggleEventSubscription(b56);
      byte b57 = 46;
      ((Module)this).toggleEventSubscription(b57);
   }

   public boolean isToggled() {
      return this.warmupActive;
   }

   public void onAIMoveSpeed(EventAIMoveSpeed event) {
   }

   public void onAttackEntity(EventAttackEntity event) {
   }

   public void onAttackEntity() {
      AttackEntityListener.super.onAttackEntity();
   }

   public void onBlockBoundingBox(EventBlockBoundingBox event) {
   }

   public void onBlockOverlay(EventBlockOverlay event) {
   }

   public void onBlockChange(EventBlockChange event) {
   }

   public void onChat(EventChat event) {
   }

   public void onChunkLoad(EventChunkLoad event) {
   }

   public void onClickGui2() {
   }

   public void onClickGui() {
      ClickGuiListener.super.onClickGui();
   }

   public void onClick(EventClick event) {
   }

   public void onClick() {
      ClickListener.super.onClick();
   }

   public void onDownloadTerrain() {
   }

   public void onDropItem2() {
   }

   public void onEntityColor(EventEntityColor event) {
   }

   public void onGuiScale() {
   }

   public void onEntityHurt(EventEntityHurt event) {
   }

   public void onInventoryClick(EventInventoryClick event) {
   }

   @Override
   public boolean onInventoryClose() {
      return false;
   }

   @Override
   public void onInventoryClose(EventInventoryClose event) {
   }

   public void onItemGlint(EventItemGlint event) {
   }

   public void onItemPos(EventItemPos event) {
   }

   public void onKey2(EventKey event) {
   }

   public void onLadder(EventLadder event) {
   }

   public void onGameLoop(EventGameLoop event) {
   }

   public void onLeftClick(EventLeftClick event) {
   }

   public void onLoadWorld(EventLoadWorld event) {
   }

   public void onMotion(EventMotion event) {
   }

   public void onMotion() {
      MotionListener.super.onMotion();
   }

   public void onMouseMove(EventMouseMove event) {
   }

   public void onMouseMove() {
      MouseMoveListener.super.onMouseMove();
   }

   public void onMoveInput3(EventMoveInput event) {
   }

   public void onMoveInput(EventMoveInput event) {
   }

   public void onNightVision(EventNightVision event) {
   }

   public void onNightVision2(EventNightVision event) {
   }

   public void onSlowDown(EventSlowDown event) {
   }

   public void onWebSlowDown(EventWebSlowDown event) {
   }

   public void onOrientCamera(EventOrientCamera event) {
   }

   public void onPerspective(EventPerspective event) {
   }

   public void onNoClip2() {
   }

   public void onMove(EventMove event) {
   }

   public void onMove2(EventMove event) {
   }

   public void onRayCast() {
   }

   public void onRenderBlock(EventRenderBlock event) {
   }

   public void onRenderEntity(EventRenderEntity event) {
   }

   public void onRenderGuiBackground(EventRenderGuiBackground event) {
   }

   public void onSafeWalk(EventSafeWalk event) {
   }

   public void onSafeWalk2(EventSafeWalk event) {
      SafeWalkListener.super.onSafeWalk2(event);
   }

   public void onSilentSlot() {
   }

   public void onSound(EventSound event) {
   }

   public void onStep(EventStep event) {
   }

   public void onStep() {
   }

   public void onSwim(EventSwim event) {
   }

   public void onSwing(EventSwing event) {
   }

   public void onTimer2(EventTimer event) {
   }

   public void onTimer(EventTimer event) {
   }

   public void onTimerUpdate(EventTimerUpdate event) {
   }

   public void onWindowResize2(int n, int n2) {
   }

   public void onWorldTime(EventWorldTime event) {
   }

   public void onZoom(EventZoom event) {
   }

   public void onAttackSlowdown(EventAttackSlowdown event) {
      AttackSlowdownListener.super.onAttackSlowdown(event);
   }

   public void onAttackSlowdown2(EventAttackSlowdown event) {
      AttackSlowdownListener.super.onAttackSlowdown2(event);
   }

   public void onMoveEntity(EventMoveEntity event) {
      MoveEntityListener.super.onMoveEntity(event);
   }

   public void onMoveEntity2(EventMoveEntity event) {
      MoveEntityListener.super.onMoveEntity2(event);
   }

   public void onPacketDelay2(EventPacketDelay event) {
      PacketDelayListener.super.onPacketDelay2(event);
   }

   public void onPacketDelay(EventPacketDelay event) {
      PacketDelayListener.super.onPacketDelay(event);
   }

   public void onPacket2(EventPacket event) {
      PacketListener.super.onPacket2(event);
   }

   public void onPacket5(EventPacket event) {
      PacketListener.super.onPacket5(event);
   }

   public void onPacket3(EventPacket event) {
      PacketListener.super.onPacket3(event);
   }

   public void onPacket(EventPacket event) {
      PacketListener.super.onPacket(event);
   }

   public void onPacket4(EventPacket event) {
      PacketListener.super.onPacket4(event);
   }

   public void onRender(float f) {
      RenderListener.super.onRender(f);
   }

   public void onRender2(float f, ScaledResolution sr) {
      RenderListener.super.onRender2(f, sr);
   }

   public void onRender(float f, ScaledResolution sr) {
      RenderListener.super.onRender(f, sr);
   }

   public void onSprint2(EventSprint event) {
      SprintListener.super.onSprint2(event);
   }

   public void onSprint(EventSprint event) {
      SprintListener.super.onSprint(event);
   }

   public void onSprint3(EventSprint event) {
      SprintListener.super.onSprint3(event);
   }

   public void onTick() {
      TickListener.super.onTick();
   }

   public void onTick2() {
      boolean isControlFlowFlag = Categorys.isControlFlowFlag();
      TickListener.super.onTick2();
      long l = 10000L;
      if (this.timeHelper.reached2(l)) {
         boolean flag = false;
         this.setWarmupActive(flag);
      }
   }

   public void onTick5() {
      TickListener.super.onTick5();
   }

   public void onTick3() {
      TickListener.super.onTick3();
   }

   public void onTick4() {
      TickListener.super.onTick4();
   }

   public void onUpdate2() {
      UpdateListener.super.onUpdate2();
   }

   public void onUpdate(EventUpdate event) {
      UpdateListener.super.onUpdate(event);
   }

   public void onUpdate() {
      UpdateListener.super.onUpdate();
   }

   public void onWorldColor(EventWorldColor event) {
      WorldColorListener.super.onWorldColor(event);
   }

   public void onWorldColor2(EventWorldColor event) {
      WorldColorListener.super.onWorldColor2(event);
   }
}
