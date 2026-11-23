package net.augustus.modules.latency;

import com.google.common.collect.Queues;
import java.awt.Color;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.augustus.utils.network.PacketFlushMarker;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.events.EventPacketDelay;
import net.augustus.events.listeners.PacketDelayListener;
import net.augustus.modules.latency.helper.DelayedPacketEntry;
import net.augustus.modules.latency.helper.DelayedOutboundPacket;
import net.augustus.settings.options.PacketHandlerPacketOption;
import net.augustus.viaversion.packet.C0APacketSwingHand;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.EventManager;
import net.augustus.utils.AntiSamplerGuard;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S24PacketBlockAction;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.S31PacketWindowProperty;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.network.play.server.S34PacketMaps;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.network.play.server.S42PacketCombatEvent;
import net.minecraft.network.play.server.S43PacketCamera;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.network.play.server.S46PacketSetCompressionLevel;
import net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class PacketHandler extends Module implements PacketDelayListener, TickListener {
   private static final Map INCOMING_DELAY_OPTIONS = new HashMap();
   private static final Map INCOMING_ABORT_OPTIONS = new HashMap();
   private static final Map OUTGOING_DELAY_OPTIONS = new HashMap();
   private static final Map OUTGOING_ABORT_OPTIONS = new HashMap();
   public Queue incomingPacketQueue;
   public Queue outgoingPacketQueue;
   private final BooleanValue pingFix;
   public final DoubleValue incoming;
   public final BooleansSetting macroDelayModes;
   private final BooleanValue realTimeDamage;
   public final BooleansSetting eventToHandlePacket;
   public final MultiOptionSetting incomingPacketsToDelay;
   private final DoubleValue abortIncoming;
   public final MultiOptionSetting incomingPacketsToDelayAbort;
   public final DoubleValue outgoing;
   public final MultiOptionSetting outGoingPacketsToDelay;
   private final DoubleValue abortOutgoing;
   public final MultiOptionSetting outGoingPacketsToDelayAbort;
   private boolean flushingPackets;
   private volatile boolean delayingIncoming;
   private final CopyOnWriteArrayList guardStatusFlags;
   private volatile int consecutiveTransactionCount;

   public PacketHandler() {
       super("PacketHandler", "PacketHandler", true, Color.red, Categorys.LATENCY);
      this.incomingPacketQueue = Queues.newConcurrentLinkedQueue();
      this.outgoingPacketQueue = Queues.newConcurrentLinkedQueue();
      this.pingFix = new BooleanValue(this, "PingFix", false);
      this.incoming = new DoubleValue(this, "Incoming", true);
      this.macroDelayModes = new BooleansSetting(
         this, "MacroDelayModes", "None", new String[]{"None", "MacroDelay", "OtherMacroDelay", "ExperimentalMacroDelay", "Sandwich"}, this.incoming
      );
      this.realTimeDamage = new BooleanValue(this, "RealTimeDamage", true, this.incoming);
      this.eventToHandlePacket = new BooleansSetting(this, "EventToHandlePacket", "Vanilla", new String[]{"Vanilla", "Tick"}, this.incoming);
      MultiOptionSetting multiOptionSetting = new MultiOptionSetting(this, "IncomingPacketsToDelay", sortedPacketOptions(INCOMING_DELAY_OPTIONS), this.incoming);
      String str = "PacketsToDelay";
      this.incomingPacketsToDelay = multiOptionSetting.named(str);
      this.abortIncoming = new DoubleValue(this, "AbortIncoming", this.incoming);
      MultiOptionSetting multiOptionSetting2 = new MultiOptionSetting(this, "IncomingPacketsToDelayAbort", sortedPacketOptions(INCOMING_ABORT_OPTIONS), this.abortIncoming);
      String str2 = "PacketsToAbortDelay";
      this.incomingPacketsToDelayAbort = multiOptionSetting2.named(str2);
      this.outgoing = new DoubleValue(this, "Outgoing", true);
      MultiOptionSetting multiOptionSetting3 = new MultiOptionSetting(this, "OutGoingPacketsToDelay", sortedPacketOptions(OUTGOING_DELAY_OPTIONS), this.outgoing);
      String str3 = "PacketsToDelay";
      this.outGoingPacketsToDelay = multiOptionSetting3.named(str3);
      this.abortOutgoing = new DoubleValue(this, "AbortOutgoing", this.outgoing);
      MultiOptionSetting multiOptionSetting4 = new MultiOptionSetting(this, "OutGoingPacketsToDelayAbort", sortedPacketOptions(OUTGOING_ABORT_OPTIONS), this.abortOutgoing);
      String str4 = "PacketsToAbortDelay";
      this.outGoingPacketsToDelayAbort = multiOptionSetting4.named(str4);
      this.flushingPackets = false;
      this.guardStatusFlags = new CopyOnWriteArrayList();
      this.consecutiveTransactionCount = 2;
      EventManager.getEventBus().subscribe(25, this, 2147483640);
      EventManager.getEventBus().subscribe(40, this);
      this.guardStatusFlags.add(false);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
   }

   @Override
   public void onTick2() {
      if (mc.thePlayer.ticksExisted % 20 == 0) {
         this.guardStatusFlags.set(0, AntiSamplerGuard.isGuardActive(1) && ThreadLocalRandom.current().nextInt(0, 100) < 20);
      }
   }

   @Override
   public void onPacketDelay2(EventPacketDelay event) {
      this.delayingIncoming = event.isDelayed() || event.getIncomingDelayMs() > 0;
   }

   public void enqueueIncomingPacket(EventPacketDelay event) {
      DelayedPacketEntry delayedPacketEntry = new DelayedPacketEntry(event.getPacket());
      this.incomingPacketQueue.add(delayedPacketEntry);
      if (event.getPacket() instanceof S07PacketRespawn) {
         this.flushAllIncomingPackets();
      }
   }

   public void enqueueOutgoingPacket(EventPacketDelay event) {
      this.outgoingPacketQueue.add(new DelayedOutboundPacket(event.getPacket(), null));
   }

   public boolean isDelayingIncoming() {
      return this.delayingIncoming;
   }

   public boolean hasQueuedOutgoingPackets() {
      return !this.outgoingPacketQueue.isEmpty();
   }

   public void flushAllIncomingPackets() {

      while (!this.incomingPacketQueue.isEmpty()) {
         Object obj = this.incomingPacketQueue.poll();
         DelayedPacketEntry delayedPacketEntry = (DelayedPacketEntry)obj;
         this.processDelayedIncomingEntry(delayedPacketEntry);
      }
   }

   public void flushExpiredIncomingPackets(int n) {
      if (!this.incomingPacketQueue.isEmpty()) {
         LinkedBlockingQueue linkedBlockingQueue = Queues.newLinkedBlockingQueue();
         Iterator it = this.incomingPacketQueue.iterator();

         while (it.hasNext()) {
            DelayedPacketEntry delayedPacketEntry = (DelayedPacketEntry)it.next();
            long l = (long)n;
            if (delayedPacketEntry.isExpired(l)) {
               Packet packet = delayedPacketEntry.getPacket();
               if (this.shouldHoldTransactionInMacroDelay(packet)) {
                  linkedBlockingQueue.add(delayedPacketEntry);
               } else {
                  while (!linkedBlockingQueue.isEmpty()) {
                     DelayedPacketEntry delayedPacketEntry2 = (DelayedPacketEntry)linkedBlockingQueue.poll();
                     this.processDelayedIncomingEntry(delayedPacketEntry2);
                     this.incomingPacketQueue.remove(delayedPacketEntry2);
                  }

                  this.processDelayedIncomingEntry(delayedPacketEntry);
                  it.remove();
               }
            }
         }
      }
   }

   public void flushIncomingUntilTargetRange(double d, EntityLivingBase entity) {
      EntityPlayerSP thePlayer = mc.thePlayer;
      if (!this.incomingPacketQueue.isEmpty() && entity != null && thePlayer != null) {
         LinkedBlockingQueue linkedBlockingQueue = Queues.newLinkedBlockingQueue();
         Vec3 positionEyes = thePlayer.getPositionEyes(1.0F);
         Iterator it = this.incomingPacketQueue.iterator();

         while (it.hasNext()) {
            DelayedPacketEntry delayedPacketEntry = (DelayedPacketEntry)it.next();
            Packet packet = delayedPacketEntry.getPacket();
            Vec3 vec = this.getRecordedEntityPosForPacket(packet, entity);
            if (vec == null) {
               linkedBlockingQueue.add(delayedPacketEntry);
            } else {
               AxisAlignedBB box = RotationUtil.createEntityBoxAt(entity, vec);
               Vec3 vec2 = RotationUtil.clampPointToBox(positionEyes, box);
               if (!(positionEyes.distanceTo(vec2) < d)) {
                  return;
               }

               while (!linkedBlockingQueue.isEmpty()) {
                  DelayedPacketEntry delayedPacketEntry2 = (DelayedPacketEntry)linkedBlockingQueue.poll();
                  this.processDelayedIncomingEntry(delayedPacketEntry2);
                  this.incomingPacketQueue.remove(delayedPacketEntry2);
               }

               this.processDelayedIncomingEntry(delayedPacketEntry);
               it.remove();
            }
         }
      }
   }

   private Vec3 getRecordedEntityPosForPacket(Packet packet, EntityLivingBase entity) {
      int n = this.extractEntityIdFromPacket(packet);
      return n != entity.getEntityId() ? null : (Vec3)entity.packetPositionMap.get(packet);
   }

   private int extractEntityIdFromPacket(Packet packet) {
      if (packet instanceof S14PacketEntity) {
         return ((S14PacketEntity)packet).getEntityId();
      } else {
         return packet instanceof S18PacketEntityTeleport ? ((S18PacketEntityTeleport)packet).getEntityId() : -1;
      }
   }

   public boolean shouldHoldTransactionInMacroDelay(Packet packet) {
      boolean flag = !ClientManagers.moduleManager.packetHandler.macroDelayModes.getSelectedValue().equals("Sandwich");
      if (packet instanceof S32PacketConfirmTransaction) {
         this.consecutiveTransactionCount++;
         if (this.consecutiveTransactionCount >= 2) {
            flag = true;
         }
      }

      if (packet instanceof S14PacketEntity || packet instanceof S18PacketEntityTeleport) {
         this.consecutiveTransactionCount = 0;
      }

      boolean flag2 = (Boolean)this.guardStatusFlags.get(0);
      return flag
         && !flag2
         && (packet instanceof S32PacketConfirmTransaction || packet instanceof S00PacketKeepAlive || packet instanceof S48PacketResourcePackSend)
         && !ClientManagers.moduleManager.packetHandler.macroDelayModes.getSelectedValue().equals("None")
         && Minecraft.getMinecraft().thePlayer != null
         && Minecraft.getMinecraft().thePlayer.ticksExisted > 5;
   }

   private void processDelayedIncomingEntry(DelayedPacketEntry delayedPacketEntry) {
      if (delayedPacketEntry != null) {
         Packet packet = delayedPacketEntry.getPacket();

         try {
            if (!(packet instanceof PacketFlushMarker)) {
               packet.processPacket(mc.getNetHandler().getNetworkManager().getNetHandler());
               this.cleanupTrackedEntityPacket(packet);
            }
         } catch (Exception e) {
         }
      }
   }

   public void cleanupTrackedEntityPacket(Packet packet) {
      if (mc.theWorld != null) {
         if (packet instanceof S14PacketEntity || packet instanceof S18PacketEntityTeleport) {
            PacketThreadUtil.runOnMainThreadImmediate(() -> {
               if (mc.theWorld != null) {
                  Entity entity;
                  if (packet instanceof S14PacketEntity) {
                     entity = mc.theWorld.getEntityByID(((S14PacketEntity)packet).getEntityId());
                  } else {
                     entity = mc.theWorld.getEntityByID(((S18PacketEntityTeleport)packet).getEntityId());
                  }

                  if (entity instanceof EntityLivingBase) {
                     ((EntityLivingBase)entity).packetPositionMap.remove(packet);
                  }
               }
            });
         }
      }
   }

   public void flushAllOutgoingPackets(Queue queue, ReentrantReadWriteLock reentrantReadWriteLock) {
      reentrantReadWriteLock.writeLock().lock();

      DelayedOutboundPacket packet;
      while ((packet = (DelayedOutboundPacket)this.outgoingPacketQueue.poll()) != null) {
         queue.add(packet.toInboundHandlerTuple());
      }

      reentrantReadWriteLock.writeLock().unlock();
   }

   public void flushExpiredOutgoingPackets(EventPacketDelay event, int n) {
      if (!this.outgoingPacketQueue.isEmpty()) {
         event.getQueueLock().writeLock().lock();
         Iterator it = this.outgoingPacketQueue.iterator();

         while (it.hasNext()) {
            DelayedOutboundPacket packet = (DelayedOutboundPacket)it.next();
            long l = (long)n;
            if (packet.isExpired(l)) {
               event.getOutboundQueue().add(packet.toInboundHandlerTuple());
               it.remove();
            }
         }

         event.getQueueLock().writeLock().unlock();
      }
   }

   public boolean shouldDelayIncomingPacket(Packet packet) {
      if ((this.pingFix.getValue() || (Boolean)this.guardStatusFlags.get(0)) && packet instanceof S00PacketKeepAlive) {
         return false;
      } else if (!this.realTimeDamage.getValue()
         || !(packet instanceof S19PacketEntityStatus) && !(packet instanceof S06PacketUpdateHealth) && !(packet instanceof S29PacketSoundEffect)) {
         PacketHandlerPacketOption option = (PacketHandlerPacketOption)INCOMING_DELAY_OPTIONS.get(packet.getClass());
         return option != null ? PacketHandlerPacketOption.isOptionSelected(option) : packet instanceof PacketFlushMarker;
      } else {
         return false;
      }
   }

   public boolean shouldDelayOutgoingPacket(Packet packet) {
      if ((this.pingFix.getValue() || (Boolean)this.guardStatusFlags.get(0)) && packet instanceof C00PacketKeepAlive) {
         return false;
      } else {
         PacketHandlerPacketOption option = (PacketHandlerPacketOption)OUTGOING_DELAY_OPTIONS.get(packet.getClass());
         return option != null ? PacketHandlerPacketOption.isOptionSelected(option) : packet instanceof PacketFlushMarker;
      }
   }

   public boolean shouldAbortIncomingDelay(Packet packet) {
      if (packet instanceof S07PacketRespawn) {
         return true;
      } else {
         PacketHandlerPacketOption option = (PacketHandlerPacketOption)INCOMING_ABORT_OPTIONS.get(packet.getClass());
         return option != null ? PacketHandlerPacketOption.isOptionSelected(option) : packet instanceof PacketFlushMarker;
      }
   }

   public boolean shouldAbortOutgoingDelay(Packet packet) {
      PacketHandlerPacketOption option = (PacketHandlerPacketOption)OUTGOING_ABORT_OPTIONS.get(packet.getClass());
      return option != null ? PacketHandlerPacketOption.isOptionSelected(option) : packet instanceof PacketFlushMarker;
   }

   public synchronized boolean isFlushingPackets() {
      return this.flushingPackets;
   }

   public synchronized void setFlushingPackets(boolean flag) {
      this.flushingPackets = flag;
   }

   public boolean isWorldAndPlayerReady() {
      return Minecraft.getMinecraft() != null
         && Minecraft.getMinecraft().theWorld != null
         && Minecraft.getMinecraft().thePlayer != null
         && Minecraft.getMinecraft().thePlayer.ticksExisted > 5;
   }

   private static void registerIncomingPacketOptions(Map map) {
      PacketHandlerPacketOption option = new PacketHandlerPacketOption("S00PacketKeepAlive", S00PacketKeepAlive.class);
      putPacketOption(map, option);
      PacketHandlerPacketOption option2 = new PacketHandlerPacketOption("S01PacketJoinGame", S01PacketJoinGame.class);
      putPacketOption(map, option2);
      PacketHandlerPacketOption option3 = new PacketHandlerPacketOption("S02PacketChat", S02PacketChat.class);
      putPacketOption(map, option3);
      PacketHandlerPacketOption option4 = new PacketHandlerPacketOption("S03PacketTimeUpdate", S03PacketTimeUpdate.class);
      putPacketOption(map, option4);
      PacketHandlerPacketOption option5 = new PacketHandlerPacketOption("S04PacketEntityEquipment", S04PacketEntityEquipment.class);
      putPacketOption(map, option5);
      PacketHandlerPacketOption option6 = new PacketHandlerPacketOption("S05PacketSpawnPosition", S05PacketSpawnPosition.class);
      putPacketOption(map, option6);
      PacketHandlerPacketOption option7 = new PacketHandlerPacketOption("S06PacketUpdateHealth", S06PacketUpdateHealth.class);
      putPacketOption(map, option7);
      PacketHandlerPacketOption option8 = new PacketHandlerPacketOption("S07PacketRespawn", S07PacketRespawn.class);
      putPacketOption(map, option8);
      PacketHandlerPacketOption option9 = new PacketHandlerPacketOption("S08PacketPlayerPosLook", S08PacketPlayerPosLook.class);
      putPacketOption(map, option9);
      PacketHandlerPacketOption option10 = new PacketHandlerPacketOption("S09PacketHeldItemChange", S09PacketHeldItemChange.class);
      putPacketOption(map, option10);
      PacketHandlerPacketOption option11 = new PacketHandlerPacketOption("S0APacketUseBed", S0APacketUseBed.class);
      putPacketOption(map, option11);
      PacketHandlerPacketOption option12 = new PacketHandlerPacketOption("S0BPacketAnimation", S0BPacketAnimation.class);
      putPacketOption(map, option12);
      PacketHandlerPacketOption option13 = new PacketHandlerPacketOption("S0CPacketSpawnPlayer", S0CPacketSpawnPlayer.class);
      putPacketOption(map, option13);
      PacketHandlerPacketOption option14 = new PacketHandlerPacketOption("S0DPacketCollectItem", S0DPacketCollectItem.class);
      putPacketOption(map, option14);
      PacketHandlerPacketOption option15 = new PacketHandlerPacketOption("S0EPacketSpawnObject", S0EPacketSpawnObject.class);
      putPacketOption(map, option15);
      PacketHandlerPacketOption option16 = new PacketHandlerPacketOption("S0FPacketSpawnMob", S0FPacketSpawnMob.class);
      putPacketOption(map, option16);
      PacketHandlerPacketOption option17 = new PacketHandlerPacketOption("S10PacketSpawnPainting", S10PacketSpawnPainting.class);
      putPacketOption(map, option17);
      PacketHandlerPacketOption option18 = new PacketHandlerPacketOption("S11PacketSpawnExperienceOrb", S11PacketSpawnExperienceOrb.class);
      putPacketOption(map, option18);
      PacketHandlerPacketOption option19 = new PacketHandlerPacketOption("S12PacketEntityVelocity", S12PacketEntityVelocity.class);
      putPacketOption(map, option19);
      PacketHandlerPacketOption option20 = new PacketHandlerPacketOption("S13PacketDestroyEntities", S13PacketDestroyEntities.class);
      putPacketOption(map, option20);
      PacketHandlerPacketOption option21 = new PacketHandlerPacketOption("S14PacketEntity", S14PacketEntity.class);
      putPacketOption(map, option21);
      PacketHandlerPacketOption option22 = new PacketHandlerPacketOption("S15PacketEntityRelMove", S14PacketEntity.S15PacketEntityRelMove.class);
      putPacketOption(map, option22);
      PacketHandlerPacketOption option23 = new PacketHandlerPacketOption("S16PacketEntityLook", S14PacketEntity.S16PacketEntityLook.class);
      putPacketOption(map, option23);
      PacketHandlerPacketOption option24 = new PacketHandlerPacketOption("S17PacketEntityLookMove", S14PacketEntity.S17PacketEntityLookMove.class);
      putPacketOption(map, option24);
      PacketHandlerPacketOption option25 = new PacketHandlerPacketOption("S18PacketEntityTeleport", S18PacketEntityTeleport.class);
      putPacketOption(map, option25);
      PacketHandlerPacketOption option26 = new PacketHandlerPacketOption("S19PacketEntityHeadLook", S19PacketEntityHeadLook.class);
      putPacketOption(map, option26);
      PacketHandlerPacketOption option27 = new PacketHandlerPacketOption("S19PacketEntityStatus", S19PacketEntityStatus.class);
      putPacketOption(map, option27);
      PacketHandlerPacketOption option28 = new PacketHandlerPacketOption("S1BPacketEntityAttach", S1BPacketEntityAttach.class);
      putPacketOption(map, option28);
      PacketHandlerPacketOption option29 = new PacketHandlerPacketOption("S1CPacketEntityMetadata", S1CPacketEntityMetadata.class);
      putPacketOption(map, option29);
      PacketHandlerPacketOption option30 = new PacketHandlerPacketOption("S1DPacketEntityEffect", S1DPacketEntityEffect.class);
      putPacketOption(map, option30);
      PacketHandlerPacketOption option31 = new PacketHandlerPacketOption("S1EPacketRemoveEntityEffect", S1EPacketRemoveEntityEffect.class);
      putPacketOption(map, option31);
      PacketHandlerPacketOption option32 = new PacketHandlerPacketOption("S1FPacketSetExperience", S1FPacketSetExperience.class);
      putPacketOption(map, option32);
      PacketHandlerPacketOption option33 = new PacketHandlerPacketOption("S20PacketEntityProperties", S20PacketEntityProperties.class);
      putPacketOption(map, option33);
      PacketHandlerPacketOption option34 = new PacketHandlerPacketOption("S21PacketChunkData", S21PacketChunkData.class);
      putPacketOption(map, option34);
      PacketHandlerPacketOption option35 = new PacketHandlerPacketOption("S22PacketMultiBlockChange", S22PacketMultiBlockChange.class);
      putPacketOption(map, option35);
      PacketHandlerPacketOption option36 = new PacketHandlerPacketOption("S23PacketBlockChange", S23PacketBlockChange.class);
      putPacketOption(map, option36);
      PacketHandlerPacketOption option37 = new PacketHandlerPacketOption("S24PacketBlockAction", S24PacketBlockAction.class);
      putPacketOption(map, option37);
      PacketHandlerPacketOption option38 = new PacketHandlerPacketOption("S25PacketBlockBreakAnim", S25PacketBlockBreakAnim.class);
      putPacketOption(map, option38);
      PacketHandlerPacketOption option39 = new PacketHandlerPacketOption("S26PacketMapChunkBulk", S26PacketMapChunkBulk.class);
      putPacketOption(map, option39);
      PacketHandlerPacketOption option40 = new PacketHandlerPacketOption("S27PacketExplosion", S27PacketExplosion.class);
      putPacketOption(map, option40);
      PacketHandlerPacketOption option41 = new PacketHandlerPacketOption("S28PacketEffect", S28PacketEffect.class);
      putPacketOption(map, option41);
      PacketHandlerPacketOption option42 = new PacketHandlerPacketOption("S29PacketSoundEffect", S29PacketSoundEffect.class);
      putPacketOption(map, option42);
      PacketHandlerPacketOption option43 = new PacketHandlerPacketOption("S2APacketParticles", S2APacketParticles.class);
      putPacketOption(map, option43);
      PacketHandlerPacketOption option44 = new PacketHandlerPacketOption("S2BPacketChangeGameState", S2BPacketChangeGameState.class);
      putPacketOption(map, option44);
      PacketHandlerPacketOption option45 = new PacketHandlerPacketOption("S2CPacketSpawnGlobalEntity", S2CPacketSpawnGlobalEntity.class);
      putPacketOption(map, option45);
      PacketHandlerPacketOption option46 = new PacketHandlerPacketOption("S2DPacketOpenWindow", S2DPacketOpenWindow.class);
      putPacketOption(map, option46);
      PacketHandlerPacketOption option47 = new PacketHandlerPacketOption("S2EPacketCloseWindow", S2EPacketCloseWindow.class);
      putPacketOption(map, option47);
      PacketHandlerPacketOption option48 = new PacketHandlerPacketOption("S2FPacketSetSlot", S2FPacketSetSlot.class);
      putPacketOption(map, option48);
      PacketHandlerPacketOption option49 = new PacketHandlerPacketOption("S30PacketWindowItems", S30PacketWindowItems.class);
      putPacketOption(map, option49);
      PacketHandlerPacketOption option50 = new PacketHandlerPacketOption("S31PacketWindowProperty", S31PacketWindowProperty.class);
      putPacketOption(map, option50);
      PacketHandlerPacketOption option51 = new PacketHandlerPacketOption("S32PacketConfirmTransaction", S32PacketConfirmTransaction.class);
      putPacketOption(map, option51);
      PacketHandlerPacketOption option52 = new PacketHandlerPacketOption("S33PacketUpdateSign", S33PacketUpdateSign.class);
      putPacketOption(map, option52);
      PacketHandlerPacketOption option53 = new PacketHandlerPacketOption("S34PacketMaps", S34PacketMaps.class);
      putPacketOption(map, option53);
      PacketHandlerPacketOption option54 = new PacketHandlerPacketOption("S35PacketUpdateTileEntity", S35PacketUpdateTileEntity.class);
      putPacketOption(map, option54);
      PacketHandlerPacketOption option55 = new PacketHandlerPacketOption("S36PacketSignEditorOpen", S36PacketSignEditorOpen.class);
      putPacketOption(map, option55);
      PacketHandlerPacketOption option56 = new PacketHandlerPacketOption("S37PacketStatistics", S37PacketStatistics.class);
      putPacketOption(map, option56);
      PacketHandlerPacketOption option57 = new PacketHandlerPacketOption("S38PacketPlayerListItem", S38PacketPlayerListItem.class);
      putPacketOption(map, option57);
      PacketHandlerPacketOption option58 = new PacketHandlerPacketOption("S39PacketPlayerAbilities", S39PacketPlayerAbilities.class);
      putPacketOption(map, option58);
      PacketHandlerPacketOption option59 = new PacketHandlerPacketOption("S3APacketTabComplete", S3APacketTabComplete.class);
      putPacketOption(map, option59);
      PacketHandlerPacketOption option60 = new PacketHandlerPacketOption("S3BPacketScoreboardObjective", S3BPacketScoreboardObjective.class);
      putPacketOption(map, option60);
      PacketHandlerPacketOption option61 = new PacketHandlerPacketOption("S3CPacketUpdateScore", S3CPacketUpdateScore.class);
      putPacketOption(map, option61);
      PacketHandlerPacketOption option62 = new PacketHandlerPacketOption("S3DPacketDisplayScoreboard", S3DPacketDisplayScoreboard.class);
      putPacketOption(map, option62);
      PacketHandlerPacketOption option63 = new PacketHandlerPacketOption("S3EPacketTeams", S3EPacketTeams.class);
      putPacketOption(map, option63);
      PacketHandlerPacketOption option64 = new PacketHandlerPacketOption("S3FPacketCustomPayload", S3FPacketCustomPayload.class);
      putPacketOption(map, option64);
      PacketHandlerPacketOption option65 = new PacketHandlerPacketOption("S40PacketDisconnect", S40PacketDisconnect.class);
      putPacketOption(map, option65);
      PacketHandlerPacketOption option66 = new PacketHandlerPacketOption("S41PacketServerDifficulty", S41PacketServerDifficulty.class);
      putPacketOption(map, option66);
      PacketHandlerPacketOption option67 = new PacketHandlerPacketOption("S42PacketCombatEvent", S42PacketCombatEvent.class);
      putPacketOption(map, option67);
      PacketHandlerPacketOption option68 = new PacketHandlerPacketOption("S43PacketCamera", S43PacketCamera.class);
      putPacketOption(map, option68);
      PacketHandlerPacketOption option69 = new PacketHandlerPacketOption("S44PacketWorldBorder", S44PacketWorldBorder.class);
      putPacketOption(map, option69);
      PacketHandlerPacketOption option70 = new PacketHandlerPacketOption("S45PacketTitle", S45PacketTitle.class);
      putPacketOption(map, option70);
      PacketHandlerPacketOption option71 = new PacketHandlerPacketOption("S46PacketSetCompressionLevel", S46PacketSetCompressionLevel.class);
      putPacketOption(map, option71);
      PacketHandlerPacketOption option72 = new PacketHandlerPacketOption("S47PacketPlayerListHeaderFooter", S47PacketPlayerListHeaderFooter.class);
      putPacketOption(map, option72);
      PacketHandlerPacketOption option73 = new PacketHandlerPacketOption("S48PacketResourcePackSend", S48PacketResourcePackSend.class);
      putPacketOption(map, option73);
      PacketHandlerPacketOption option74 = new PacketHandlerPacketOption("S49PacketUpdateEntityNBT", S49PacketUpdateEntityNBT.class);
      putPacketOption(map, option74);
   }

   private static void registerOutgoingPacketOptions(Map map) {
      PacketHandlerPacketOption option = new PacketHandlerPacketOption("C00PacketKeepAlive", C00PacketKeepAlive.class);
      putPacketOption(map, option);
      PacketHandlerPacketOption option2 = new PacketHandlerPacketOption("C01PacketChatMessage", C01PacketChatMessage.class);
      putPacketOption(map, option2);
      PacketHandlerPacketOption option3 = new PacketHandlerPacketOption("C02PacketUseEntity", C02PacketUseEntity.class);
      putPacketOption(map, option3);
      PacketHandlerPacketOption option4 = new PacketHandlerPacketOption("C03PacketPlayer", C03PacketPlayer.class);
      putPacketOption(map, option4);
      PacketHandlerPacketOption option5 = new PacketHandlerPacketOption("C04PacketPlayerPosition", C03PacketPlayer.C04PacketPlayerPosition.class);
      putPacketOption(map, option5);
      PacketHandlerPacketOption option6 = new PacketHandlerPacketOption("C05PacketPlayerLook", C03PacketPlayer.C05PacketPlayerLook.class);
      putPacketOption(map, option6);
      PacketHandlerPacketOption option7 = new PacketHandlerPacketOption("C06PacketPlayerPosLook", C03PacketPlayer.C06PacketPlayerPosLook.class);
      putPacketOption(map, option7);
      PacketHandlerPacketOption option8 = new PacketHandlerPacketOption("C07PacketPlayerDigging", C07PacketPlayerDigging.class);
      putPacketOption(map, option8);
      PacketHandlerPacketOption option9 = new PacketHandlerPacketOption("C08PacketPlayerBlockPlacement", C08PacketPlayerBlockPlacement.class);
      putPacketOption(map, option9);
      PacketHandlerPacketOption option10 = new PacketHandlerPacketOption("C09PacketHeldItemChange", C09PacketHeldItemChange.class);
      putPacketOption(map, option10);
      PacketHandlerPacketOption option11 = new PacketHandlerPacketOption("C0APacketAnimation", C0APacketAnimation.class);
      putPacketOption(map, option11);
      PacketHandlerPacketOption option12 = new PacketHandlerPacketOption("C0BPacketEntityAction", C0BPacketEntityAction.class);
      putPacketOption(map, option12);
      PacketHandlerPacketOption option13 = new PacketHandlerPacketOption("C0CPacketInput", C0CPacketInput.class);
      putPacketOption(map, option13);
      PacketHandlerPacketOption option14 = new PacketHandlerPacketOption("C0DPacketCloseWindow", C0DPacketCloseWindow.class);
      putPacketOption(map, option14);
      PacketHandlerPacketOption option15 = new PacketHandlerPacketOption("C0EPacketClickWindow", C0EPacketClickWindow.class);
      putPacketOption(map, option15);
      PacketHandlerPacketOption option16 = new PacketHandlerPacketOption("C0FPacketConfirmTransaction", C0FPacketConfirmTransaction.class);
      putPacketOption(map, option16);
      PacketHandlerPacketOption option17 = new PacketHandlerPacketOption("C10PacketCreativeInventoryAction", C10PacketCreativeInventoryAction.class);
      putPacketOption(map, option17);
      PacketHandlerPacketOption option18 = new PacketHandlerPacketOption("C11PacketEnchantItem", C11PacketEnchantItem.class);
      putPacketOption(map, option18);
      PacketHandlerPacketOption option19 = new PacketHandlerPacketOption("C12PacketUpdateSign", C12PacketUpdateSign.class);
      putPacketOption(map, option19);
      PacketHandlerPacketOption option20 = new PacketHandlerPacketOption("C13PacketPlayerAbilities", C13PacketPlayerAbilities.class);
      putPacketOption(map, option20);
      PacketHandlerPacketOption option21 = new PacketHandlerPacketOption("C14PacketTabComplete", C14PacketTabComplete.class);
      putPacketOption(map, option21);
      PacketHandlerPacketOption option22 = new PacketHandlerPacketOption("C15PacketClientSettings", C15PacketClientSettings.class);
      putPacketOption(map, option22);
      PacketHandlerPacketOption option23 = new PacketHandlerPacketOption("C16PacketClientStatus", C16PacketClientStatus.class);
      putPacketOption(map, option23);
      PacketHandlerPacketOption option24 = new PacketHandlerPacketOption("C17PacketCustomPayload", C17PacketCustomPayload.class);
      putPacketOption(map, option24);
      PacketHandlerPacketOption option25 = new PacketHandlerPacketOption("C18PacketSpectate", C18PacketSpectate.class);
      putPacketOption(map, option25);
      PacketHandlerPacketOption option26 = new PacketHandlerPacketOption("C19PacketResourcePackStatus", C19PacketResourcePackStatus.class);
      putPacketOption(map, option26);
      PacketHandlerPacketOption option27 = new PacketHandlerPacketOption("CAnimateHandPacket", C0APacketSwingHand.class);
      putPacketOption(map, option27);
      PacketHandlerPacketOption option28 = new PacketHandlerPacketOption("CPacketPlayerTryUseItemOnBlock", CPacketPlayerTryUseItemOnBlock.class);
      putPacketOption(map, option28);
   }

   private static void putPacketOption(Map map, PacketHandlerPacketOption option) {
      map.put(PacketHandlerPacketOption.getPacketClass(option), option);
   }

   private static PacketHandlerPacketOption[] sortedPacketOptions(Map map) {
      return Arrays.stream((PacketHandlerPacketOption[])map.values().toArray(new PacketHandlerPacketOption[0])).sorted(Comparator.comparing(PacketHandlerPacketOption::getName)).toArray(PacketHandlerPacketOption[]::new);
   }

   static {
      registerIncomingPacketOptions(INCOMING_DELAY_OPTIONS);
      registerIncomingPacketOptions(INCOMING_ABORT_OPTIONS);
      registerOutgoingPacketOptions(OUTGOING_DELAY_OPTIONS);
      registerOutgoingPacketOptions(OUTGOING_ABORT_OPTIONS);
   }

}
