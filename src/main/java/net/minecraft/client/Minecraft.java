package net.minecraft.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.IStatStringFormat;
import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.event.events.BlockInteractEvent;
import baritone.api.event.events.PlayerUpdateEvent;
import baritone.api.event.events.TickEvent;
import baritone.api.event.events.WorldEvent;
import baritone.api.event.events.type.EventState;
import baritone.api.utils.IPlayerContext;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.dietrichevents2.StateTypes;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.SocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.function.BiFunction;
import javax.imageio.ImageIO;
import net.augustus.modules.render.MotionBlur;
import net.augustus.modules.world.NoPlaceDelay;
import net.augustus.events.listeners.LeftClickListener;
import net.augustus.events.EventGameLoop;
import net.augustus.events.listeners.ClickListener;
import net.augustus.events.listeners.WindowResizeListener;
import net.augustus.render.GpuVendorDetector;
import net.augustus.commands.impl.ClickerCommand;
import net.augustus.utils.HolidayDetector;
import net.augustus.utils.network.PacketFlushMarker;
import net.augustus.events.EventKey;
import net.augustus.ClientManagers;
import net.augustus.events.listeners.BlockPlaceListener;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.utils.ChatUtil;
import net.augustus.utils.file.ClientPaths;
import net.augustus.events.EventClick;
import net.augustus.settings.Setting;
import net.augustus.render.AbstractSkeletonRenderer;
import net.augustus.utils.combat.ProtectedPlayerCheck;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.RawMouseHelper;
import net.augustus.events.ClickButtonType;
import net.augustus.events.listeners.KeyListener;
import net.augustus.discord.DiscordRpcManager;
import net.augustus.utils.render.ResizableFramebuffer;
import net.augustus.events.TimerEventType;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.events.listeners.TimerListener;
import net.augustus.events.KeyInputSource;
import net.augustus.events.EventBlockPlace;
import net.augustus.utils.math.DoubleSampleWindow;
import net.augustus.events.listeners.GameLoopListener;
import net.augustus.utils.input.RawMouseInputThread;
import net.augustus.utils.rotation.RotationController;
import net.augustus.viaversion.ViaAugustus;
import net.augustus.events.EventTimer;
import net.augustus.events.TickPhase;
import net.augustus.utils.MouseWheelTracker;
import net.augustus.events.BlockPlaceStage;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.events.listeners.DropItemListener;
import net.augustus.Augustus;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.ui.GuiIngameHook;
import net.augustus.utils.WorldLoadWarmup;
import net.augustus.events.listeners.TickListener;
import net.augustus.utils.audio.JavaFxAudioPlayer;
import net.augustus.utils.input.ToggleUtil;
import net.augustus.utils.player.SilentSlotManager;
import net.augustus.events.listeners.InventoryCloseListener;
import net.augustus.events.listeners.RayCastListener;
import net.augustus.utils.ClientHoliday;
import net.augustus.utils.rotation.Rotation;
import net.augustus.utils.OSUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMemoryErrorScreen;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.FoliageColorReloadListener;
import net.minecraft.client.resources.GrassColorReloadListener;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.ResourceIndex;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.AnimationMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.client.resources.data.FontMetadataSectionSerializer;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.LanguageMetadataSectionSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.resources.data.PackMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSectionSerializer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.FrameTimer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MinecraftError;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import net.minecraft.util.Util;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

public class Minecraft implements IThreadListener {
   public final JavaFxAudioPlayer audioPlayer;
   public long realElapsedTicks;
   public long gameElapsedTicks;
   public long projectedRealTicks;
   public long projectedGameTicks;
   public float realTotalTicks;
   public float gameTotalTicks;
   private static final Logger LOGGER;
   public static final boolean isRunningOnMac;
   public static byte[] memoryReserve;
   private static final List macDisplayModes;
   private final File fileResourcepacks;
   private final PropertyMap twitchDetails;
   private ServerData currentServerData;
   private TextureManager renderEngine;
   private static Minecraft theMinecraft;
   public PlayerControllerMP playerController;
   private boolean fullscreen;
   private boolean enableGLErrorChecking;
   private boolean hasCrashed;
   private CrashReport crashReporter;
   public int displayWidth;
   public int displayHeight;
   private boolean connectedToRealms;
   public Timer timer;
   private Timer realTimer;
   public WorldClient theWorld;
   public RenderGlobal renderGlobal;
   private RenderManager renderManager;
   private RenderItem renderItem;
   private ItemRenderer itemRenderer;
   public EntityPlayerSP thePlayer;
   private Entity renderViewEntity;
   public Entity pointedEntity;
   public EffectRenderer effectRenderer;
   public Session session;
   public boolean isGamePaused;
   public FontRenderer fontRendererObj;
   public FontRenderer standardGalacticFontRenderer;
   public GuiScreen currentScreen;
   public GuiScreen previousScreen;
   public GuiScreen lastDisplayedScreen;
   public LoadingScreenRenderer loadingScreen;
   public EntityRenderer entityRenderer;
   public int leftClickCounter;
   private int tempDisplayWidth;
   private int tempDisplayHeight;
   private IntegratedServer theIntegratedServer;
   public GuiAchievement guiAchievement;
   public GuiIngameHook ingameGUI;
   public boolean skipRenderWorld;
   public MovingObjectPosition objectMouseOver;
   public MovingObjectPosition lastObjectMouseOver;
   public MovingObjectPosition previousObjectMouseOver;
   public int ticksLookingAtEntity;
   public int ticksNotLookingAtEntity;
   public GameSettings gameSettings;
   public RawMouseHelper mouseHelper;
   public final File mcDataDir;
   private final File fileAssets;
   private final String version;
   private final Proxy proxy;
   private ISaveFormat saveLoader;
   private static int debugFPS;
   public int rightClickDelayTimer;
   private String serverName;
   private int serverPort;
   public boolean inGameHasFocus;
   public long systemTime;
   private int joinPlayerCounter;
   public final FrameTimer frameTimer;
   public long startNanoTime;
   private final boolean java64bit;
   private final boolean isDemo;
   private NetworkManager myNetworkManager;
   private boolean integratedServerIsRunning;
   public final Profiler mcProfiler;
   private long debugCrashKeyPressTime;
   private IReloadableResourceManager mcResourceManager;
   private final IMetadataSerializer metadataSerializer_;
   private final List defaultResourcePacks;
   public final DefaultResourcePack mcDefaultResourcePack;
   private ResourcePackRepository mcResourcePackRepository;
   private LanguageManager mcLanguageManager;
   private Framebuffer framebufferMc;
   public Framebuffer blurFramebuffer;
   private TextureMap textureMapBlocks;
   private SoundHandler mcSoundHandler;
   private MusicTicker mcMusicTicker;
   private ResourceLocation mojangLogo;
   private MinecraftSessionService sessionService;
   private SkinManager skinManager;
   private final Queue scheduledTasks;
   private final Queue priorityScheduledTasks;
   private final Thread mcThread;
   private ModelManager modelManager;
   private BlockRendererDispatcher blockRenderDispatcher;
   public volatile boolean running;
   public String debug;
   public boolean renderChunksMany;
   private boolean wasLeftClicking;
   public long debugUpdateTime;
   public int fpsCounter;
   public long prevFrameTime;
   private String debugProfilerName;
   private int ticksWithoutScreen;
   private boolean skipGameTick;
   private long lastRenderTime;
   private DoubleSampleWindow tickTimeWindow;
   private DoubleSampleWindow frameTimeWindow;
   public boolean ranMultipleTicks;
   private boolean hittingBlockCustom;
   private boolean prevHittingBlockCustom;
   public int remainingTicksInFrame;
   private final ArrayList tickHistory;
   private static boolean controlFlowFlag;

   private void displayDebugInfo(long l) {
   }

   public void setIngameFocus() {
      Minecraft mc;
      exitBlock: {
         boolean isControlFlowDisabled = isControlFlowDisabled();
         boolean isActive = Display.isActive();
         if (isControlFlowDisabled) {
            if (!isActive) {
               return;
            }

            mc = this;
            if (!isControlFlowDisabled) {
               break exitBlock;
            }

            isActive = this.inGameHasFocus;
         }

         if (isActive) {
            return;
         }

         boolean flag = true;
         this.inGameHasFocus = flag;
         this.mouseHelper.grabMouseCursor();
         GuiScreen screen = (GuiScreen)null;
         this.displayGuiScreen(screen);
         mc = this;
      }

      short s = 10000;
      mc.leftClickCounter = s;
      Augustus.getInstance().getModuleManager().saveMoveKeys.resetInGameTicks();
   }

   public void middleClickMouse() {
      MovingObjectPosition hit = null;
      Block block = null;
      boolean isControlFlowDisabled = isControlFlowDisabled();
      Minecraft mc = this;
      if (isControlFlowDisabled) {
         if (this.objectMouseOver == null) {
            return;
         }

         ClickListener.onClick2(ClickButtonType.MID);
         mc = this;
      }

      byte b;
      int n;
      boolean flag;
      TileEntity tileEntity;
      Object obj;
      exitBlock: {
         exitBlock2: {
            b = (byte)((mc.thePlayer.capabilities.isCreativeMode) ? 1 : 0);
            n = 0;
            flag = false;
            tileEntity = null;
            MovingObjectPosition.MovingObjectType typeOfHit = this.objectMouseOver.typeOfHit;
            MovingObjectPosition.MovingObjectType block2 = MovingObjectPosition.MovingObjectType.BLOCK;
            if (isControlFlowDisabled) {
               if (this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                  BlockPos blockPos = this.objectMouseOver.getBlockPos();
                  Block block3 = this.theWorld.getBlockState(blockPos).getBlock();
                  Block block4 = block3;
                  if (isControlFlowDisabled) {
                     if (block3.getMaterial() == Material.air) {
                        return;
                     }

                     block4 = block3;
                  }

                  obj = block4.getItem(this.theWorld, blockPos);
                  if (obj == null) {
                     return;
                  }

                  exitBlock3: {
                     byte b2 = b;
                     if (isControlFlowDisabled) {
                        if (b == 0) {
                           break exitBlock3;
                        }

                        b2 = (byte)((GuiScreen.isCtrlKeyDown()) ? 1 : 0);
                     }

                     if (b2 != 0) {
                        tileEntity = this.theWorld.getTileEntity(blockPos);
                     }
                  }

                  exitBlock4: {
                     exitBlock5: {
                        boolean flag2 = obj instanceof ItemBlock;
                        if (isControlFlowDisabled) {
                           if (!flag2) {
                              break exitBlock5;
                           }

                           block = block3;
                           if (!isControlFlowDisabled) {
                              break exitBlock4;
                           }

                           flag2 = block3.isFlowerPot();
                        }

                        if (!flag2) {
                           block = Block.getBlockFromItem((Item)obj);
                           break exitBlock4;
                        }
                     }

                     block = block3;
                  }

                  Block block5 = block;
                  n = block5.getDamageValue(this.theWorld, blockPos);
                  flag = ((Item)obj).getHasSubtypes();
                  break exitBlock;
               }

               hit = this.objectMouseOver;
               if (!isControlFlowDisabled) {
                  break exitBlock2;
               }

               typeOfHit = this.objectMouseOver.typeOfHit;
               block2 = MovingObjectPosition.MovingObjectType.ENTITY;
            }

            if (typeOfHit != block2) {
               return;
            }

            hit = this.objectMouseOver;
         }

         if (hit.entityHit == null) {
            return;
         }

         byte b3 = b;
         if (isControlFlowDisabled) {
            if (b == 0) {
               return;
            }

            b3 = (byte)((this.objectMouseOver.entityHit instanceof EntityPainting) ? 1 : 0);
         }

         if (isControlFlowDisabled) {
            if (b3 != 0) {
               obj = Items.painting;
               if (isControlFlowDisabled) {
                  break exitBlock;
               }
            }

            b3 = (byte)((this.objectMouseOver.entityHit instanceof EntityLeashKnot) ? 1 : 0);
         }

         if (isControlFlowDisabled) {
            if (b3 != 0) {
               obj = Items.lead;
               if (isControlFlowDisabled) {
                  break exitBlock;
               }
            }

            b3 = (byte)((this.objectMouseOver.entityHit instanceof EntityItemFrame) ? 1 : 0);
         }

         if (isControlFlowDisabled) {
            if (b3 != 0) {
               exitBlock6: {
                  EntityItemFrame entityHit = (EntityItemFrame)this.objectMouseOver.entityHit;
                  ItemStack displayedItem = entityHit.getDisplayedItem();
                  ItemStack stack = displayedItem;
                  if (isControlFlowDisabled) {
                     if (displayedItem == null) {
                        obj = Items.item_frame;
                        if (isControlFlowDisabled) {
                           break exitBlock6;
                        }
                     }

                     stack = displayedItem;
                  }

                  obj = stack.getItem();
                  n = displayedItem.getMetadata();
                  flag = true;
               }

               if (isControlFlowDisabled) {
                  break exitBlock;
               }
            }

            b3 = (byte)((this.objectMouseOver.entityHit instanceof EntityMinecart) ? 1 : 0);
         }

         if (isControlFlowDisabled) {
            if (b3 != 0) {
               EntityMinecart entityHit2 = (EntityMinecart)this.objectMouseOver.entityHit;
               switch (Minecraft.EnumMinecartTypeSwitchMap.MINECART_TYPE_ORDINAL_MAP[entityHit2.getMinecartType().ordinal()]) {
                  case 1:
                     obj = Items.furnace_minecart;
                     if (isControlFlowDisabled) {
                        break;
                     }
                  case 2:
                     obj = Items.chest_minecart;
                     if (isControlFlowDisabled) {
                        break;
                     }
                  case 3:
                     obj = Items.tnt_minecart;
                     if (isControlFlowDisabled) {
                        break;
                     }
                  case 4:
                     obj = Items.hopper_minecart;
                     if (isControlFlowDisabled) {
                        break;
                     }
                  case 5:
                     obj = Items.command_block_minecart;
                     if (isControlFlowDisabled) {
                        break;
                     }
                  default:
                     obj = Items.minecart;
               }

               if (isControlFlowDisabled) {
                  break exitBlock;
               }
            }

            b3 = (byte)((this.objectMouseOver.entityHit instanceof EntityBoat) ? 1 : 0);
         }

         if (isControlFlowDisabled) {
            if (b3 != 0) {
               obj = Items.boat;
               if (isControlFlowDisabled) {
                  break exitBlock;
               }
            }

            b3 = (byte)((this.objectMouseOver.entityHit instanceof EntityArmorStand) ? 1 : 0);
         }

         if (b3 != 0) {
            obj = Items.armor_stand;
            if (isControlFlowDisabled) {
               break exitBlock;
            }
         }

         obj = Items.spawn_egg;
         n = EntityList.getEntityID(this.objectMouseOver.entityHit);
         flag = true;
         if (!EntityList.entityEggs.containsKey(n)) {
            return;
         }
      }

      InventoryPlayer inventoryPlayer;
      exitBlock7: {
         exitBlock8: {
            inventoryPlayer = this.thePlayer.inventory;
            if (isControlFlowDisabled) {
               if (tileEntity != null) {
                  break exitBlock8;
               }

               inventoryPlayer.setCurrentItem((Item)obj, n, flag, (b != 0));
            }

            if (isControlFlowDisabled) {
               break exitBlock7;
            }
         }

         ItemStack stack2 = this.storeTEInStack((Item)obj, n, tileEntity);
         inventoryPlayer.setInventorySlotContents(inventoryPlayer.currentItem, stack2);
      }

      int n2 = b;
      if (isControlFlowDisabled) {
         if (b == 0) {
            return;
         }

         n2 = this.thePlayer.inventoryContainer.inventorySlots.size() - 9 + inventoryPlayer.currentItem;
      }

      int n3 = n2;
      this.playerController.sendSlotPacket(inventoryPlayer.getStackInSlot(inventoryPlayer.currentItem), n3);
   }

   public void setConnectedToRealms(boolean flag) {
      this.connectedToRealms = flag;
   }

   public int getLimitFramerate() {
      boolean flag;
      byte b;
      flag = isControlFlowFlag();
      byte b2 = (byte)((Display.isActive()) ? 1 : 0);
      b = (byte)((Augustus.getInstance().getModuleManager().augustusServer.removeFramerateLimitInScreens.getValue()) ? 1 : 0);
      exitBlock:
      if (!flag) {
         if (b != 0) {
            b = b2;
            if (flag) {
               break exitBlock;
            }

            if (b2 != 0) {
               return this.gameSettings.limitFramerate;
            }
         }

         b = b2;
      }

      exitBlock2: {
         if (!flag) {
            if (b != 0) {
               break exitBlock2;
            }

            b = (byte)((Augustus.getInstance().getModuleManager().augustusServer.limitFrameRateIfScreenNotFocused.getValue()) ? 1 : 0);
         }

         if (flag) {
            return b;
         }

         if (b != 0) {
            return 30;
         }
      }

      Minecraft mc = this;
      if (!flag) {
         if (this.theWorld == null) {
            if (flag) {
               return this.gameSettings.limitFramerate;
            }

            if (this.currentScreen != null) {
               return 30;
            }
         }

         mc = this;
      }

      return mc.gameSettings.limitFramerate;
   }

   public void updateDisplay() {
      this.mcProfiler.startSection("display_update");
      Display.update();
      this.mcProfiler.endSection();
      this.checkWindowResize();
   }

   public static long getSystemTime() {
      return Sys.getTime() * 1000L / Sys.getTimerResolution();
   }

   public synchronized void setSession(Session session2) {
      this.session = session2;
   }

   public int getRightClickDelayTimer() {
      return this.rightClickDelayTimer;
   }

   public boolean isIntegratedServerRunning() {
      return this.integratedServerIsRunning;
   }

   public IResourceManager getResourceManager() {
      return this.mcResourceManager;
   }

   public ISaveFormat getSaveLoader() {
      return this.saveLoader;
   }

   public void refreshResources() {
      RuntimeException e = null;
      ArrayList list = Lists.newArrayList(this.defaultResourcePacks);
      boolean isControlFlowDisabled = isControlFlowDisabled();
      Iterator it = this.mcResourcePackRepository.getRepositoryEntries().iterator();
      boolean flag = isControlFlowDisabled;

      while (true) {
         if (it.hasNext()) {
            ResourcePackRepository.Entry entry = (ResourcePackRepository.Entry)it.next();
            list.add(entry.getResourcePack());
            if (!flag) {
               break;
            }

            if (flag) {
               continue;
            }
         }

         exitBlock: {
            Minecraft mc = this;
            if (flag) {
               if (this.mcResourcePackRepository.getResourcePackInstance() != null) {
                  list.add(this.mcResourcePackRepository.getResourcePackInstance());
               }

               try {
                  mc = this;
               } catch (RuntimeException e2) {
                  e = e2;
                  break exitBlock;
               }
            }

            try {
               mc.mcResourceManager.reloadResources(list);
               break;
            } catch (RuntimeException e3) {
               e = e3;
               boolean flag2 = false;
            }
         }

         RuntimeException e4 = e;
         LOGGER.info("Caught error stitching, removing all assigned resourcepacks", e4);
         list.clear();
         list.addAll(this.defaultResourcePacks);
         this.mcResourcePackRepository.setRepositories(Collections.emptyList());
         this.mcResourceManager.reloadResources(list);
         this.gameSettings.resourcePacks.clear();
         this.gameSettings.incompatibleResourcePacks.clear();
         this.gameSettings.saveOptions();
         break;
      }

      this.mcLanguageManager.parseLanguageMetadata(list);
      RenderGlobal renderGlobal2 = this.renderGlobal;
      if (flag) {
         if (this.renderGlobal == null) {
            return;
         }

         renderGlobal2 = this.renderGlobal;
      }

      renderGlobal2.loadRenderers();
   }

   private ItemStack storeTEInStack(Item item, int n, TileEntity tileEntity) {
      ItemStack stack;
      NBTTagCompound tag;
      exitBlock: {
         stack = new ItemStack(item, 1, n);
         boolean isControlFlowDisabled = isControlFlowDisabled();
         NBTTagCompound tag2 = new NBTTagCompound();
         boolean flag = isControlFlowDisabled;
         tileEntity.writeToNBT(tag2);
         if (flag) {
            if (item == Items.skull) {
               tag = tag2;
               if (!flag) {
                  break exitBlock;
               }

               if (tag2.hasKey("Owner")) {
                  NBTTagCompound compoundTag = tag2.getCompoundTag("Owner");
                  NBTTagCompound tag3 = new NBTTagCompound();
                  tag3.setTag("SkullOwner", compoundTag);
                  stack.setTagCompound(tag3);
                  return stack;
               }
            }

            stack.setTagInfo("BlockEntityTag", tag2);
         }

         tag = new NBTTagCompound();
      }

      NBTTagCompound tag4 = tag;
      NBTTagList tagList = new NBTTagList();
      tagList.appendTag(new NBTTagString("(+NBT)"));
      tag4.setTag("Lore", tagList);
      stack.setTagInfo("display", tag4);
      return stack;
   }

   public boolean isFullScreen() {
      return this.fullscreen;
   }

   public void runTick(int n, int n2) throws IOException {
      GuiScreen screen = null, screen2 = null;
      Minecraft mc = null, mc2 = null, mc3 = null, mc4 = null, mc5 = null, mc6 = null, mc7 = null, mc8 = null, mc9 = null, mc10 = null, mc11 = null, mc12 = null, mc13 = null, mc14 = null, mc15 = null;
      EntityPlayerSP player = null, player2 = null;
      TickEvent.Type type = null, type2 = null;
      boolean flag = false, flag2 = false;
      int n3 = 0, n4 = 0, n5 = 0, n6 = 0, n7 = 0, n8 = 0, n9 = 0;
      boolean isControlFlowFlag = isControlFlowFlag();
      int n10 = (ClientManagers.moduleManager.packetHandler.eventToHandlePacket.getSelectedValue().equals("Tick")) ? 1 : 0;
      if (!isControlFlowFlag) {
         if (n10 != 0) {
            this.runScheduledTasks();
         }

         this.remainingTicksInFrame = n;
         n10 = this.rightClickDelayTimer;
      }

      if (!isControlFlowFlag) {
         if (n10 > 0) {
            int n11 = this.rightClickDelayTimer - 1;
            this.rightClickDelayTimer = n11;
         }

         this.mcProfiler.startSection("gui");
         n10 = (this.isGamePaused) ? 1 : 0;
      }

      if (!isControlFlowFlag) {
         if (n10 == 0) {
            this.ingameGUI.updateTick();
         }

         this.mcProfiler.endSection();
         MovingObjectPosition hit = new MovingObjectPosition(this.lastObjectMouseOver);
         this.previousObjectMouseOver = hit;
         n10 = n;
         if (this.theWorld != null && this.thePlayer != null) {
            if (n2 == 0 || ClientManagers.moduleManager.rotationHandler.rotationMode.getSelectedValue().equals("Tick")) {
               RotationController rotHandler = Augustus.getInstance().getRotationController();
               rotHandler.beginRotationTick(
                  new Rotation(this.thePlayer.rotationYaw, this.thePlayer.rotationPitch),
                  this.getTimer().elapsedTicks - n
               );
               TickListener.onTick2(TickPhase.EARLY);
               rotHandler.applyServerRotation();
               InventoryCloseListener.onInventoryClose2();
            }
         }
         this.entityRenderer.getMouseOver(1.0F, false);
         if (Augustus.getInstance().getModuleManager().smartRaytrace.isToggled()) {
            Augustus.getInstance().getModuleManager().smartRaytrace.updateSmartRaytrace();
         }
         if (this.theWorld != null && this.thePlayer != null) {
            RayCastListener.onRayCast2();
            this.lastObjectMouseOver = new MovingObjectPosition(this.objectMouseOver);
         }
         boolean hitLiving = this.objectMouseOver != null
            && this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY
            && this.objectMouseOver.entityHit instanceof net.minecraft.entity.EntityLivingBase;
         this.ticksLookingAtEntity = hitLiving ? this.ticksLookingAtEntity + 1 : 0;
         this.ticksNotLookingAtEntity = hitLiving ? 0 : this.ticksNotLookingAtEntity + 1;
         this.mcProfiler.startSection("gameMode");
         if (!this.isGamePaused && this.theWorld != null) {
            this.playerController.updateController();
         }
         this.mcProfiler.endStartSection("textures");
         if (!this.isGamePaused) {
            this.getTextureManager().tick();
         }
      }

      exitBlock: {
         exitBlock2: {
            exitBlock3: {
               screen = this.currentScreen;
               if (!isControlFlowFlag) {
                  exitBlock4: {
                     if (this.currentScreen == null) {
                        mc = this;
                        if (isControlFlowFlag) {
                           break exitBlock4;
                        }

                        if (this.thePlayer != null) {
                           float f;
                           n10 = (f = this.thePlayer.getCurrentHealth() - 0.0F) == 0.0F ? 0 : (f < 0.0F ? -1 : 1);
                           if (!isControlFlowFlag) {
                              if (n10 <= 0) {
                                 GuiScreen screen3 = (GuiScreen)null;
                                 this.displayGuiScreen(screen3);
                                 if (!isControlFlowFlag) {
                                    break exitBlock3;
                                 }
                              }

                              mc2 = this;
                              if (isControlFlowFlag) {
                                 break exitBlock2;
                              }

                              n10 = (this.thePlayer.isPlayerSleeping()) ? 1 : 0;
                           }

                           if (n10 == 0) {
                              break exitBlock3;
                           }

                           mc2 = this;
                           if (isControlFlowFlag) {
                              break exitBlock2;
                           }

                           if (this.theWorld == null) {
                              break exitBlock3;
                           }

                           GuiSleepMP guiSleepMP = new GuiSleepMP();
                           this.displayGuiScreen(guiSleepMP);
                           if (!isControlFlowFlag) {
                              break exitBlock3;
                           }
                        }
                     }

                     mc = this;
                  }

                  screen = mc.currentScreen;
               }

               if (isControlFlowFlag) {
                  break exitBlock;
               }

               if (screen != null) {
                  screen = this.currentScreen;
                  if (isControlFlowFlag) {
                     break exitBlock;
                  }

                  if (this.currentScreen instanceof GuiSleepMP) {
                     mc2 = this;
                     if (isControlFlowFlag) {
                        break exitBlock2;
                     }

                     if (!this.thePlayer.isPlayerSleeping()) {
                        GuiScreen screen4 = (GuiScreen)null;
                        this.displayGuiScreen(screen4);
                     }
                  }
               }
            }

            mc2 = this;
         }

         screen = mc2.currentScreen;
      }

      exitBlock5:
      if (screen != null) {
         exitBlock6: {
            boolean isToggled = ClientManagers.moduleManager.killAura.isToggled();
            if (!isControlFlowFlag) {
               if (!isToggled) {
                  break exitBlock6;
               }

               isToggled = ClientManagers.moduleManager.killAura.inInv.getValue();
            }

            if (isToggled) {
               break exitBlock5;
            }
         }

         short s = 10000;
         this.leftClickCounter = s;
      }

      BiFunction biFunction;
      exitBlock7: {
         exitBlock8: {
            exitBlock9: {
               exitBlock10: {
                  exitBlock11: {
                     exitBlock12: {
                        exitBlock13: {
                           exitBlock14: {
                              exitBlock15: {
                                 exitBlock16: {
                                    exitBlock17: {
                                       biFunction = TickEvent.createNextProvider();
                                       if (Augustus.getInstance().getModuleManager().baritone.isToggled()) {
                                          for (IBaritone baritone : (Iterable<IBaritone>)(BaritoneAPI.getProvider().getAllBaritones())) {
                                             exitBlock18: {
                                                exitBlock19: {
                                                   IPlayerContext playerContext = baritone.getPlayerContext();
                                                   if (!isControlFlowFlag) {
                                                      player = playerContext.player();
                                                      if (isControlFlowFlag) {
                                                         break exitBlock17;
                                                      }

                                                      if (player == null) {
                                                         break exitBlock19;
                                                      }

                                                      playerContext = baritone.getPlayerContext();
                                                   }

                                                   if (playerContext.world() != null) {
                                                      type = TickEvent.Type.IN;
                                                      break exitBlock18;
                                                   }
                                                }

                                                type = TickEvent.Type.OUT;
                                             }

                                             TickEvent.Type type3 = type;
                                             baritone.getGameEventHandler().onTick((TickEvent)biFunction.apply(EventState.PRE, type3));
                                             if (isControlFlowFlag) {
                                                break;
                                             }
                                          }
                                       }

                                       exitBlock20: {
                                          screen2 = this.currentScreen;
                                          exitBlock21:
                                          if (!isControlFlowFlag) {
                                             if (this.currentScreen != null) {
                                                try {
                                                   this.currentScreen.handleInput();
                                                } catch (Throwable e) {
                                                   CrashReport crashReport = CrashReport.makeCrashReport(e, "Updating screen events");
                                                   CrashReportCategory crashReportCategory = crashReport.makeCategory("Affected screen");
                                                   crashReportCategory.addCrashSectionCallable("Screen name", new Minecraft.ValueCallable(this));
                                                   throw new ReportedException(crashReport);
                                                }

                                                screen2 = this.currentScreen;
                                                if (isControlFlowFlag) {
                                                   break exitBlock21;
                                                }

                                                if (this.currentScreen != null) {
                                                   try {
                                                      this.currentScreen.updateScreen();
                                                   } catch (Throwable e2) {
                                                      CrashReport crashReport2 = CrashReport.makeCrashReport(e2, "Ticking screen");
                                                      CrashReportCategory crashReportCategory2 = crashReport2.makeCategory("Affected screen");
                                                      crashReportCategory2.addCrashSectionCallable("Screen name", new Minecraft.ValueCallable2(this));
                                                      throw new ReportedException(crashReport2);
                                                   }
                                                }
                                             }

                                             mc3 = this;
                                             if (isControlFlowFlag) {
                                                break exitBlock20;
                                             }

                                             screen2 = this.currentScreen;
                                          }

                                          if (screen2 != null) {
                                             GuiScreen currentScreen2 = this.currentScreen;
                                             boolean flag3 = this.shouldAllowUserInput(currentScreen2);
                                             if (isControlFlowFlag) {
                                                if (flag3) {
                                                   int n12 = this.ticksWithoutScreen + 1;
                                                   this.ticksWithoutScreen = n12;
                                                   if (!isControlFlowFlag) {
                                                      if (isControlFlowFlag) {
                                                         n10 = (this.entityRenderer.isShaderActive()) ? 1 : 0;
                                                         if (isControlFlowFlag) {
                                                            break exitBlock8;
                                                         }

                                                         if (n10 != 0) {
                                                            this.entityRenderer.stopUseShader();
                                                         }
                                                         break exitBlock9;
                                                      }

                                                      if (this.theWorld == null) {
                                                         break exitBlock11;
                                                      }

                                                      mc14 = this;
                                                      if (isControlFlowFlag) {
                                                         break exitBlock12;
                                                      }

                                                      if (this.thePlayer == null) {
                                                         this.mcProfiler.endStartSection("gameRenderer");
                                                         mc14 = this;
                                                         break exitBlock12;
                                                      }

                                                      TickListener.onTick2(TickPhase.TICK);
                                                      int n13 = this.joinPlayerCounter + 1;
                                                      this.joinPlayerCounter = n13;
                                                      n10 = this.joinPlayerCounter;
                                                      if (isControlFlowFlag) {
                                                         if (!isControlFlowFlag) {
                                                            if (this.joinPlayerCounter == 0) {
                                                               this.entityRenderer.updateRenderer();
                                                            }

                                                            this.mcProfiler.endStartSection("levelRenderer");
                                                            n10 = (this.isGamePaused) ? 1 : 0;
                                                         }

                                                         if (!isControlFlowFlag) {
                                                            if (n10 == 0) {
                                                               this.renderGlobal.updateClouds();
                                                            }

                                                            this.mcProfiler.endStartSection("level");
                                                            n10 = (this.isGamePaused) ? 1 : 0;
                                                         }

                                                         if (isControlFlowFlag) {
                                                            break exitBlock8;
                                                         }

                                                         if (n10 != 0) {
                                                            break exitBlock10;
                                                         }

                                                         n10 = this.theWorld.getLastLightningBolt();
                                                         if (!isControlFlowFlag) {
                                                            if (n10 > 0) {
                                                               this.theWorld.setLastLightningBolt(this.theWorld.getLastLightningBolt() - 1);
                                                            }

                                                            this.theWorld.updateEntities();
                                                            n10 = (Augustus.getInstance().getModuleManager().baritone.isToggled()) ? 1 : 0;
                                                         }

                                                         if (isControlFlowFlag) {
                                                            break exitBlock8;
                                                         }

                                                         if (n10 == 0) {
                                                            break exitBlock9;
                                                         }

                                                         exitBlock22: {
                                                            IBaritone baritoneForPlayer = BaritoneAPI.getProvider().getBaritoneForPlayer(this.thePlayer);
                                                            IBaritone baritone2 = baritoneForPlayer;
                                                            if (!isControlFlowFlag) {
                                                               if (baritoneForPlayer == null) {
                                                                  break exitBlock22;
                                                               }

                                                               baritone2 = baritoneForPlayer;
                                                            }

                                                            baritone2.getGameEventHandler().onPlayerUpdate(new PlayerUpdateEvent(EventState.POST));
                                                         }

                                                         if (!isControlFlowFlag) {
                                                            break exitBlock9;
                                                         }
                                                         break exitBlock11;
                                                      }

                                                      n9 = 30;
                                                      break exitBlock13;
                                                   }
                                                }

                                                mc8 = this;
                                                break exitBlock14;
                                             }

                                             if (!flag3) {
                                                break exitBlock16;
                                             }
                                          }

                                          mc3 = this;
                                       }

                                       exitBlock23: {
                                          mc3.mcProfiler.endStartSection("mouse");
                                          Augustus.getInstance().getModuleManager().saveMoveKeys.restoreKeyStates();
                                          n10 = (ClickerCommand.CLICK_PATTERN_RECORDER.isRunning()) ? 1 : 0;
                                          exitBlock24:
                                          if (!isControlFlowFlag) {
                                             if (n10 == 0) {
                                                while (Mouse.next()) {
                                                   int eventButton = Mouse.getEventButton();
                                                   ToggleUtil.onMouseInput(false);
                                                   EventKey event = KeyListener.onKey(eventButton, true, Mouse.getEventButtonState(), KeyInputSource.MOUSE);
                                                   n10 = (event.isCancelled()) ? 1 : 0;
                                                   if (isControlFlowFlag) {
                                                      break exitBlock24;
                                                   }

                                                   if (!isControlFlowFlag) {
                                                      if (n10 != 0) {
                                                         continue;
                                                      }

                                                      KeyBinding.setKeyBindState(eventButton - 100, Mouse.getEventButtonState());
                                                      n10 = (Mouse.getEventButtonState()) ? 1 : 0;
                                                   }

                                                   if (!isControlFlowFlag) {
                                                      exitBlock25:
                                                      if (n10 != 0) {
                                                         n10 = (this.thePlayer.isSpectator()) ? 1 : 0;
                                                         if (!isControlFlowFlag) {
                                                            exitBlock26: {
                                                               if (n10 != 0) {
                                                                  n10 = eventButton;
                                                                  n8 = 2;
                                                                  if (isControlFlowFlag) {
                                                                     break exitBlock26;
                                                                  }

                                                                  if (eventButton == 2) {
                                                                     this.ingameGUI.getSpectatorGui().onMiddleClick();
                                                                     if (!isControlFlowFlag) {
                                                                        break exitBlock25;
                                                                     }
                                                                  }
                                                               }

                                                               n10 = eventButton;
                                                               n8 = 100;
                                                            }

                                                            n10 -= n8;
                                                         }

                                                         KeyBinding.onTick(n10);
                                                      }

                                                      n10 = (this.gameSettings.keyBindTogglePerspective.isPressed()) ? 1 : 0;
                                                   }

                                                   exitBlock27: {
                                                      if (!isControlFlowFlag) {
                                                         if (n10 == 0) {
                                                            break exitBlock27;
                                                         }

                                                         this.gameSettings.thirdPersonView++;
                                                         n10 = this.gameSettings.thirdPersonView;
                                                      }

                                                      if (!isControlFlowFlag) {
                                                         if (n10 > 2) {
                                                            this.gameSettings.thirdPersonView = 0;
                                                         }

                                                         n10 = this.gameSettings.thirdPersonView;
                                                      }

                                                      exitBlock28: {
                                                         exitBlock29: {
                                                            if (!isControlFlowFlag) {
                                                               if (n10 == 0) {
                                                                  this.entityRenderer.loadEntityShader(this.getRenderViewEntity());
                                                                  if (!isControlFlowFlag) {
                                                                     break exitBlock29;
                                                                  }
                                                               }

                                                               mc4 = this;
                                                               if (isControlFlowFlag) {
                                                                  break exitBlock28;
                                                               }

                                                               n10 = this.gameSettings.thirdPersonView;
                                                            }

                                                            if (n10 == 1) {
                                                               this.entityRenderer.loadEntityShader((Entity)null);
                                                            }
                                                         }

                                                         mc4 = this;
                                                      }

                                                      mc4.renderGlobal.setDisplayListEntitiesDirty();
                                                   }

                                                   exitBlock30: {
                                                      long l = getSystemTime() - this.systemTime;
                                                      long l2;
                                                      n10 = (l2 = l - 200L) == 0L ? 0 : (l2 < 0L ? -1 : 1);
                                                      if (!isControlFlowFlag) {
                                                         if (n10 > 0) {
                                                            break exitBlock30;
                                                         }

                                                         n10 = Mouse.getEventDWheel();
                                                      }

                                                      exitBlock31: {
                                                         int n14;
                                                         exitBlock32: {
                                                            n14 = n10;
                                                            n10 = n14;
                                                            if (!isControlFlowFlag) {
                                                               if (n14 == 0) {
                                                                  break exitBlock31;
                                                               }

                                                               player2 = this.thePlayer;
                                                               if (isControlFlowFlag) {
                                                                  break exitBlock32;
                                                               }

                                                               n10 = (this.thePlayer.isSpectator()) ? 1 : 0;
                                                            }

                                                            if (n10 != 0) {
                                                               n10 = n14;
                                                               if (!isControlFlowFlag) {
                                                                  n10 = n14 < 0 ? -1 : 1;
                                                               }

                                                               n14 = n10;
                                                               Minecraft mc16 = this;
                                                               if (!isControlFlowFlag) {
                                                                  if (this.ingameGUI.getSpectatorGui().isMenuActive()) {
                                                                     this.ingameGUI.getSpectatorGui().onMouseScroll(-n14);
                                                                     if (!isControlFlowFlag) {
                                                                        break exitBlock31;
                                                                     }
                                                                  }

                                                                  mc16 = this;
                                                               }

                                                               float f2 = MathHelper.clamp_float(
                                                                  mc16.thePlayer.capabilities.getFlySpeed() + (float)n14 * 0.005F, 0.0F, 0.2F
                                                               );
                                                               this.thePlayer.capabilities.setFlySpeed(f2);
                                                               if (!isControlFlowFlag) {
                                                                  break exitBlock31;
                                                               }
                                                            }

                                                            player2 = this.thePlayer;
                                                         }

                                                         player2.inventory.changeCurrentItem(n14);
                                                      }

                                                      GuiScreen currentScreen3 = this.currentScreen;
                                                      if (!isControlFlowFlag) {
                                                         if (this.currentScreen == null) {
                                                            boolean inGameHasFocus2 = this.inGameHasFocus;
                                                            if (!isControlFlowFlag) {
                                                               if (this.inGameHasFocus) {
                                                                  break exitBlock30;
                                                               }

                                                               inGameHasFocus2 = Mouse.getEventButtonState();
                                                            }

                                                            if (!inGameHasFocus2) {
                                                               break exitBlock30;
                                                            }

                                                            this.setIngameFocus();
                                                            if (!isControlFlowFlag) {
                                                               break exitBlock30;
                                                            }
                                                         }

                                                         currentScreen3 = this.currentScreen;
                                                      }

                                                      currentScreen3.handleMouseInput();
                                                   }

                                                   if (isControlFlowFlag) {
                                                      break;
                                                   }
                                                }
                                             }

                                             mc5 = this;
                                             if (isControlFlowFlag) {
                                                break exitBlock23;
                                             }

                                             n10 = this.leftClickCounter;
                                          }

                                          if (n10 > 0) {
                                             int n15 = this.leftClickCounter - 1;
                                             this.leftClickCounter = n15;
                                          }

                                          mc5 = this;
                                       }

                                       mc5.mcProfiler.endStartSection("keyboard");

                                       while (Keyboard.next()) {
                                          exitBlock33: {
                                             n10 = Keyboard.getEventKey();
                                             if (isControlFlowFlag) {
                                                break exitBlock15;
                                             }

                                             if (!isControlFlowFlag) {
                                                n10 = n10 == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
                                             }

                                             int n16;
                                             n16 = n10;
                                             boolean eventKeyState = Keyboard.getEventKeyState();
                                             EventKey event2 = KeyListener.onKey(n16, false, Keyboard.getEventKeyState(), KeyInputSource.KEYBOARD);
                                             n10 = (event2.isCancelled()) ? 1 : 0;
                                             exitBlock34:
                                             if (!isControlFlowFlag) {
                                                if (n10 == 0) {
                                                   KeyBinding.setKeyBindState(n16, eventKeyState);
                                                   n10 = (Keyboard.getEventKeyState()) ? 1 : 0;
                                                   if (isControlFlowFlag) {
                                                      break exitBlock34;
                                                   }

                                                   if (n10 != 0) {
                                                      KeyBinding.onTick(n16);
                                                   }
                                                }

                                                long l3;
                                                n10 = (l3 = this.debugCrashKeyPressTime - 0L) == 0L ? 0 : (l3 < 0L ? -1 : 1);
                                             }

                                             exitBlock35: {
                                                if (!isControlFlowFlag) {
                                                   if (n10 > 0) {
                                                      long l4;
                                                      n10 = (l4 = getSystemTime() - this.debugCrashKeyPressTime - 6000L) == 0L ? 0 : (l4 < 0L ? -1 : 1);
                                                      if (!isControlFlowFlag) {
                                                         if (n10 >= 0) {
                                                            throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));
                                                         }

                                                         n10 = (Keyboard.isKeyDown(46)) ? 1 : 0;
                                                      }

                                                      exitBlock36: {
                                                         if (!isControlFlowFlag) {
                                                            if (n10 == 0) {
                                                               break exitBlock36;
                                                            }

                                                            n10 = (Keyboard.isKeyDown(61)) ? 1 : 0;
                                                         }

                                                         if (n10 != 0) {
                                                            break exitBlock35;
                                                         }
                                                      }

                                                      long l5 = -1L;
                                                      this.debugCrashKeyPressTime = l5;
                                                      if (!isControlFlowFlag) {
                                                         break exitBlock35;
                                                      }
                                                   }

                                                   n10 = (Keyboard.isKeyDown(46)) ? 1 : 0;
                                                }

                                                if (!isControlFlowFlag) {
                                                   if (n10 == 0) {
                                                      break exitBlock35;
                                                   }

                                                   n10 = (Keyboard.isKeyDown(61)) ? 1 : 0;
                                                }

                                                if (n10 != 0) {
                                                   long systemTime2 = getSystemTime();
                                                   this.debugCrashKeyPressTime = systemTime2;
                                                }
                                             }

                                             this.dispatchKeypresses();
                                             if (this.currentScreen == null) {
                                                ToggleUtil.onKeyboardInput(false);
                                             }

                                             n10 = (Keyboard.getEventKeyState()) ? 1 : 0;
                                             if (!isControlFlowFlag) {
                                                if (n10 == 0) {
                                                   break exitBlock33;
                                                }

                                                n10 = n16;
                                             }

                                             exitBlock37: {
                                                if (n10 == 62) {
                                                   mc6 = this;
                                                   if (isControlFlowFlag) {
                                                      break exitBlock37;
                                                   }

                                                   if (this.entityRenderer != null) {
                                                      this.entityRenderer.switchUseShader();
                                                   }
                                                }

                                                mc6 = this;
                                             }

                                             exitBlock38: {
                                                exitBlock39: {
                                                   exitBlock40: {
                                                      GuiScreen currentScreen4 = mc6.currentScreen;
                                                      if (!isControlFlowFlag) {
                                                         if (mc6.currentScreen == null) {
                                                            break exitBlock40;
                                                         }

                                                         currentScreen4 = this.currentScreen;
                                                      }

                                                      currentScreen4.handleKeyboardInput();
                                                      if (!isControlFlowFlag) {
                                                         break exitBlock39;
                                                      }
                                                   }

                                                   n10 = n16;
                                                   byte b = 1;
                                                   if (!isControlFlowFlag) {
                                                      if (n16 == 1) {
                                                         this.displayInGameMenu();
                                                      }

                                                      n10 = n16;
                                                      b = 32;
                                                   }

                                                   if (!isControlFlowFlag) {
                                                      exitBlock41: {
                                                         if (n10 == b) {
                                                            n10 = (Keyboard.isKeyDown(61)) ? 1 : 0;
                                                            if (isControlFlowFlag) {
                                                               break exitBlock41;
                                                            }

                                                            exitBlock42:
                                                            if (n10 != 0) {
                                                               GuiIngameHook ingameGUI2 = this.ingameGUI;
                                                               if (!isControlFlowFlag) {
                                                                  if (this.ingameGUI == null) {
                                                                     break exitBlock42;
                                                                  }

                                                                  ingameGUI2 = this.ingameGUI;
                                                               }

                                                               ingameGUI2.getChatGUI().clearChatMessages();
                                                            }
                                                         }

                                                         n10 = n16;
                                                      }

                                                      b = 31;
                                                   }

                                                   if (!isControlFlowFlag) {
                                                      exitBlock43: {
                                                         if (n10 == b) {
                                                            n10 = (Keyboard.isKeyDown(61)) ? 1 : 0;
                                                            if (isControlFlowFlag) {
                                                               break exitBlock43;
                                                            }

                                                            if (n10 != 0) {
                                                               this.refreshResources();
                                                            }
                                                         }

                                                         n10 = n16;
                                                      }

                                                      b = 17;
                                                   }

                                                   if (!isControlFlowFlag) {
                                                      if (n10 == b && Keyboard.isKeyDown(61)) {
                                                      }

                                                      n10 = n16;
                                                      b = 18;
                                                   }

                                                   if (!isControlFlowFlag) {
                                                      if (n10 == b && Keyboard.isKeyDown(61)) {
                                                      }

                                                      n10 = n16;
                                                      b = 47;
                                                   }

                                                   if (!isControlFlowFlag) {
                                                      if (n10 == b && Keyboard.isKeyDown(61)) {
                                                      }

                                                      n10 = n16;
                                                      b = 38;
                                                   }

                                                   if (!isControlFlowFlag) {
                                                      if (n10 == b && Keyboard.isKeyDown(61)) {
                                                      }

                                                      n10 = n16;
                                                      b = 22;
                                                   }

                                                   if (!isControlFlowFlag) {
                                                      if (n10 == b && Keyboard.isKeyDown(61)) {
                                                      }

                                                      n10 = n16;
                                                      b = 20;
                                                   }

                                                   if (!isControlFlowFlag) {
                                                      exitBlock44: {
                                                         if (n10 == b) {
                                                            n10 = (Keyboard.isKeyDown(61)) ? 1 : 0;
                                                            if (isControlFlowFlag) {
                                                               break exitBlock44;
                                                            }

                                                            if (n10 != 0) {
                                                               this.refreshResources();
                                                            }
                                                         }

                                                         n10 = n16;
                                                      }

                                                      b = 33;
                                                   }

                                                   if (!isControlFlowFlag) {
                                                      exitBlock45: {
                                                         if (n10 == b) {
                                                            n10 = (Keyboard.isKeyDown(61)) ? 1 : 0;
                                                            if (isControlFlowFlag) {
                                                               break exitBlock45;
                                                            }

                                                            if (n10 != 0) {
                                                               GameSettings gameSettings2 = this.gameSettings;
                                                               GameSettings.Options renderdistance = GameSettings.Options.RENDER_DISTANCE;
                                                               int n17 = (GuiScreen.isShiftKeyDown()) ? 1 : 0;
                                                               if (!isControlFlowFlag) {
                                                                  n17 = n17 != 0 ? -1 : 1;
                                                               }

                                                               gameSettings2.setOptionValue(renderdistance, n17);
                                                            }
                                                         }

                                                         n10 = n16;
                                                      }

                                                      b = 30;
                                                   }

                                                   if (!isControlFlowFlag) {
                                                      exitBlock46: {
                                                         if (n10 == b) {
                                                            n10 = (Keyboard.isKeyDown(61)) ? 1 : 0;
                                                            if (isControlFlowFlag) {
                                                               break exitBlock46;
                                                            }

                                                            if (n10 != 0) {
                                                               this.renderGlobal.loadRenderers();
                                                            }
                                                         }

                                                         n10 = n16;
                                                      }

                                                      b = 35;
                                                   }

                                                   if (!isControlFlowFlag) {
                                                      exitBlock47: {
                                                         if (n10 == b) {
                                                            n10 = (Keyboard.isKeyDown(61)) ? 1 : 0;
                                                            if (isControlFlowFlag) {
                                                               break exitBlock47;
                                                            }

                                                            if (n10 != 0) {
                                                               boolean advancedItemTooltips = this.gameSettings.advancedItemTooltips;
                                                               if (!isControlFlowFlag) {
                                                                  advancedItemTooltips = !this.gameSettings.advancedItemTooltips;
                                                               }

                                                               this.gameSettings.advancedItemTooltips = advancedItemTooltips;
                                                               this.gameSettings.saveOptions();
                                                            }
                                                         }

                                                         n10 = n16;
                                                      }

                                                      b = 48;
                                                   }

                                                   if (!isControlFlowFlag) {
                                                      exitBlock48: {
                                                         if (n10 == b) {
                                                            n10 = (Keyboard.isKeyDown(61)) ? 1 : 0;
                                                            if (isControlFlowFlag) {
                                                               break exitBlock48;
                                                            }

                                                            if (n10 != 0) {
                                                               RenderManager renderManager2 = this.renderManager;
                                                               boolean isDebugBoundingBox = this.renderManager.isDebugBoundingBox();
                                                               if (!isControlFlowFlag) {
                                                                  isDebugBoundingBox = !isDebugBoundingBox;
                                                               }

                                                               renderManager2.setDebugBoundingBox(isDebugBoundingBox);
                                                            }
                                                         }

                                                         n10 = n16;
                                                      }

                                                      b = 25;
                                                   }

                                                   if (!isControlFlowFlag) {
                                                      exitBlock49: {
                                                         if (n10 == b) {
                                                            n10 = (Keyboard.isKeyDown(61)) ? 1 : 0;
                                                            if (isControlFlowFlag) {
                                                               break exitBlock49;
                                                            }

                                                            if (n10 != 0) {
                                                               boolean pauseOnLostFocus = this.gameSettings.pauseOnLostFocus;
                                                               if (!isControlFlowFlag) {
                                                                  pauseOnLostFocus = !this.gameSettings.pauseOnLostFocus;
                                                               }

                                                               this.gameSettings.pauseOnLostFocus = pauseOnLostFocus;
                                                               this.gameSettings.saveOptions();
                                                            }
                                                         }

                                                         n10 = n16;
                                                      }

                                                      b = 59;
                                                   }

                                                   exitBlock50: {
                                                      if (!isControlFlowFlag) {
                                                         if (n10 == b) {
                                                            boolean hideGUI = this.gameSettings.hideGUI;
                                                            if (!isControlFlowFlag) {
                                                               hideGUI = !this.gameSettings.hideGUI;
                                                            }

                                                            this.gameSettings.hideGUI = hideGUI;
                                                         }

                                                         n10 = n16;
                                                         if (isControlFlowFlag) {
                                                            break exitBlock50;
                                                         }

                                                         b = 61;
                                                      }

                                                      if (n10 == b) {
                                                         boolean showDebugInfo = this.gameSettings.showDebugInfo;
                                                         if (!isControlFlowFlag) {
                                                            showDebugInfo = !this.gameSettings.showDebugInfo;
                                                         }

                                                         this.gameSettings.showDebugInfo = showDebugInfo;
                                                         this.gameSettings.showDebugProfilerChart = GuiScreen.isShiftKeyDown();
                                                         this.gameSettings.showLagometer = GuiScreen.isAltKeyDown();
                                                      }

                                                      n10 = (this.gameSettings.keyBindTogglePerspective.isPressed()) ? 1 : 0;
                                                   }

                                                   if (!isControlFlowFlag) {
                                                      if (n10 != 0) {
                                                         this.gameSettings.thirdPersonView++;
                                                         n10 = this.gameSettings.thirdPersonView;
                                                         if (!isControlFlowFlag) {
                                                            if (this.gameSettings.thirdPersonView > 2) {
                                                               this.gameSettings.thirdPersonView = 0;
                                                            }

                                                            n10 = this.gameSettings.thirdPersonView;
                                                         }

                                                         exitBlock51: {
                                                            exitBlock52: {
                                                               if (!isControlFlowFlag) {
                                                                  if (n10 == 0) {
                                                                     this.entityRenderer.loadEntityShader(this.getRenderViewEntity());
                                                                     if (!isControlFlowFlag) {
                                                                        break exitBlock52;
                                                                     }
                                                                  }

                                                                  mc7 = this;
                                                                  if (isControlFlowFlag) {
                                                                     break exitBlock51;
                                                                  }

                                                                  n10 = this.gameSettings.thirdPersonView;
                                                               }

                                                               if (n10 == 1) {
                                                                  this.entityRenderer.loadEntityShader((Entity)null);
                                                               }
                                                            }

                                                            mc7 = this;
                                                         }

                                                         mc7.renderGlobal.setDisplayListEntitiesDirty();
                                                      }

                                                      n10 = (this.gameSettings.keyBindSmoothCamera.isPressed()) ? 1 : 0;
                                                   }

                                                   if (isControlFlowFlag) {
                                                      break exitBlock38;
                                                   }

                                                   if (n10 != 0) {
                                                      boolean smoothCamera = this.gameSettings.smoothCamera;
                                                      if (!isControlFlowFlag) {
                                                         smoothCamera = !this.gameSettings.smoothCamera;
                                                      }

                                                      this.gameSettings.smoothCamera = smoothCamera;
                                                   }
                                                }

                                                n10 = (this.gameSettings.showDebugInfo) ? 1 : 0;
                                             }

                                             if (!isControlFlowFlag) {
                                                if (n10 == 0) {
                                                   break exitBlock33;
                                                }

                                                n10 = (this.gameSettings.showDebugProfilerChart) ? 1 : 0;
                                             }

                                             if (!isControlFlowFlag) {
                                                if (n10 == 0) {
                                                   break exitBlock33;
                                                }

                                                n10 = n16;
                                             }

                                             if (!isControlFlowFlag) {
                                                if (n10 == 11) {
                                                   byte b2 = 0;
                                                   this.updateDebugProfilerName(b2);
                                                }

                                                n10 = 0;
                                             }

                                             int n18 = n10;

                                             while (n18 < 9) {
                                                n10 = n16;
                                                n9 = 2 + n18;
                                                if (isControlFlowFlag) {
                                                   break exitBlock13;
                                                }

                                                if (n16 == n9) {
                                                   int n19 = n18 + 1;
                                                   this.updateDebugProfilerName(n19);
                                                }

                                                n18++;
                                                if (isControlFlowFlag) {
                                                   break;
                                                }
                                             }
                                          }

                                          if (isControlFlowFlag) {
                                             break;
                                          }
                                       }

                                       mc8 = this;
                                       if (isControlFlowFlag) {
                                          if (isControlFlowFlag) {
                                             break exitBlock14;
                                          }

                                          n10 = (this.isNotInGui()) ? 1 : 0;
                                          break exitBlock15;
                                       }

                                       player = this.thePlayer;
                                    }

                                    if (player != null) {
                                       Augustus.getInstance().getModuleManager().saveMoveKeys.incrementInGameTicks();
                                       int n20 = 0;

                                       exitBlock53: {
                                          while (true) {
                                             if (n20 < 9) {
                                                n10 = (this.gameSettings.keyBindsHotbar[n20].isPressed()) ? 1 : 0;
                                                if (isControlFlowFlag) {
                                                   break;
                                                }

                                                exitBlock54: {
                                                   if (!isControlFlowFlag) {
                                                      if (n10 == 0) {
                                                         break exitBlock54;
                                                      }

                                                      n10 = (this.thePlayer.isSpectator()) ? 1 : 0;
                                                   }

                                                   if (!isControlFlowFlag) {
                                                      if (n10 != 0) {
                                                         this.ingameGUI.getSpectatorGui().onHotbarSelected(n20);
                                                         if (!isControlFlowFlag) {
                                                            break exitBlock54;
                                                         }
                                                      }

                                                      n10 = n20;
                                                   }

                                                   SilentSlotManager.realHotbarSlot = n10;
                                                   this.thePlayer.inventory.currentItem = n20;
                                                }

                                                n20++;
                                                if (!isControlFlowFlag) {
                                                   continue;
                                                }
                                             }

                                             SilentSlotListener.onSilentSlot(StateTypes.PRE);
                                             SilentSlotManager.resetSlot(this.thePlayer.inventory.currentItem);
                                             SilentSlotListener.onSilentSlot(StateTypes.POST);
                                             mc9 = this;
                                             if (isControlFlowFlag) {
                                                break exitBlock53;
                                             }

                                             n10 = (this.isNotInGui()) ? 1 : 0;
                                             break;
                                          }

                                          if (n10 != 0) {
                                             this.thePlayer.inventory.currentItem = SilentSlotManager.tickAndResolveSlot();
                                          }

                                          mc9 = this;
                                       }

                                       n20 = mc9.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN ? 1 : 0;

                                       exitBlock55: {
                                          while (this.gameSettings.keyBindInventory.isPressed()) {
                                             Minecraft mc17 = this;
                                             if (!isControlFlowFlag) {
                                                flag = this.playerController.isRidingHorse();
                                                if (isControlFlowFlag) {
                                                   break exitBlock55;
                                                }

                                                if (flag) {
                                                   this.thePlayer.sendHorseInventory();
                                                   if (!isControlFlowFlag) {
                                                      continue;
                                                   }
                                                }

                                                ViaAugustus.sendOpenInventoryPacketIfLegacy();
                                                mc17 = this;
                                             }

                                             GuiInventory guiInventory = new GuiInventory(this.thePlayer);
                                             mc17.displayGuiScreen(guiInventory);
                                             if (isControlFlowFlag) {
                                                break;
                                             }
                                          }

                                          flag = this.thePlayer.isSpectator();
                                       }

                                       exitBlock56: {
                                          exitBlock57: {
                                             if (!isControlFlowFlag) {
                                                if (!flag) {
                                                   DropItemListener.onDropItem();
                                                }

                                                if (!this.gameSettings.keyBindDrop.isPressed()) {
                                                   flag2 = this.gameSettings.keyBindChat.isPressed();
                                                   break exitBlock57;
                                                }
                                             } else if (!flag) {
                                                flag2 = this.gameSettings.keyBindChat.isPressed();
                                                break exitBlock57;
                                             }

                                             while (true) {
                                                if (!isControlFlowFlag) {
                                                   flag2 = this.thePlayer.isSpectator();
                                                   if (isControlFlowFlag) {
                                                      break;
                                                   }

                                                   if (!flag2) {
                                                      this.thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
                                                      if (isControlFlowFlag) {
                                                         flag2 = this.gameSettings.keyBindChat.isPressed();
                                                         break;
                                                      }
                                                   }
                                                } else {
                                                   this.thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
                                                   if (isControlFlowFlag) {
                                                      flag2 = this.gameSettings.keyBindChat.isPressed();
                                                      break;
                                                   }
                                                }

                                                if (!this.gameSettings.keyBindDrop.isPressed()) {
                                                   flag2 = this.gameSettings.keyBindChat.isPressed();
                                                   break;
                                                }
                                             }
                                          }

                                          while (flag2) {
                                             n10 = n20;
                                             if (isControlFlowFlag) {
                                                break exitBlock56;
                                             }

                                             if (n20 == 0) {
                                                break;
                                             }

                                             GuiChat guiChat = new GuiChat();
                                             this.displayGuiScreen(guiChat);
                                             if (isControlFlowFlag) {
                                                break;
                                             }

                                             flag2 = this.gameSettings.keyBindChat.isPressed();
                                          }

                                          exitBlock58: {
                                             Minecraft mc18 = this;
                                             if (!isControlFlowFlag) {
                                                if (this.currentScreen != null) {
                                                   break exitBlock58;
                                                }

                                                mc18 = this;
                                             }

                                             n10 = (mc18.gameSettings.keyBindCommand.isPressed()) ? 1 : 0;
                                             if (isControlFlowFlag) {
                                                break exitBlock56;
                                             }

                                             if (n10 != 0) {
                                                n10 = n20;
                                                if (isControlFlowFlag) {
                                                   break exitBlock56;
                                                }

                                                if (n20 != 0) {
                                                   GuiChat guiChat2 = new GuiChat("/");
                                                   this.displayGuiScreen(guiChat2);
                                                }
                                             }
                                          }

                                          EventClick.tickCounter.increment();
                                          n10 = 0;
                                       }

                                       EventClick.clicksThisTick = n10;
                                       ClickButtonType left = ClickButtonType.LEFT;
                                       boolean isKeyDown = this.gameSettings.keyBindUseItem.isKeyDown();
                                       boolean isKeyDown2 = this.gameSettings.keyBindAttack.isKeyDown();
                                       Runnable runnable = this::rightClickMouse;
                                       Runnable runnable2 = this::clickMouse;
                                       boolean flag4 = isKeyDown2;
                                       boolean flag5 = isKeyDown;
                                       EventClick event3 = ClickListener.onClick(left, flag5, flag4, runnable2, runnable);
                                       n10 = event3.getLeftClicks();
                                       int n21 = (event3.isIgnoreVanillaClicks()) ? 1 : 0;
                                       if (!isControlFlowFlag) {
                                          n21 = n21 != 0 ? 0 : this.gameSettings.keyBindAttack.getPressTime();
                                       }

                                       int n22 = n10 + n21;
                                       n10 = event3.getRightClicks();
                                       int n23 = (event3.isIgnoreVanillaClicks()) ? 1 : 0;
                                       if (!isControlFlowFlag) {
                                          n23 = n23 != 0 ? 0 : this.gameSettings.keyBindUseItem.getPressTime();
                                       }

                                       int n24 = n10 + n23;
                                       n10 = (event3.isIgnoreVanillaClicks()) ? 1 : 0;
                                       if (!isControlFlowFlag) {
                                          n10 = n10 != 0 ? 0 : this.gameSettings.keyBindPickBlock.getPressTime();
                                       }

                                       int n25 = n10;
                                       byte b3 = (byte)((event3.isRightMouseDown()) ? 1 : 0);
                                       byte b4 = (byte)((event3.isLeftMouseDown()) ? 1 : 0);
                                       int n26 = (event3.isBlockAllClicks()) ? 1 : 0;
                                       if (!isControlFlowFlag) {
                                          if (n26 != 0) {
                                             n22 = 0;
                                             n24 = 0;
                                             n25 = 0;
                                             b3 = 0;
                                             b4 = 0;
                                          }

                                          this.gameSettings.keyBindAttack.setPressTime(0);
                                          this.gameSettings.keyBindUseItem.setPressTime(0);
                                          this.gameSettings.keyBindPickBlock.setPressTime(0);
                                          n26 = 0;
                                       }

                                       int n27 = n26;
                                       byte b5 = 0;
                                       n26 = (event3.isForceBreakBlock()) ? 1 : 0;
                                       if (!isControlFlowFlag) {
                                          if (n26 != 0) {
                                             n27 = 1;
                                             b5 = 1;
                                          }

                                          n26 = (event3.isCancelled()) ? 1 : 0;
                                       }

                                       exitBlock59:
                                       if (!isControlFlowFlag) {
                                          exitBlock60: {
                                             exitBlock61:
                                             if (n26 == 0) {
                                                EntityPlayerSP thePlayer2 = this.thePlayer;
                                                if (!isControlFlowFlag) {
                                                   if (this.thePlayer == null) {
                                                      break exitBlock61;
                                                   }

                                                   thePlayer2 = this.thePlayer;
                                                }

                                                exitBlock62: {
                                                   exitBlock63: {
                                                      int n28 = (thePlayer2.isUsingItem()) ? 1 : 0;
                                                      if (!isControlFlowFlag) {
                                                         if (n28 != 0) {
                                                            exitBlock64: {
                                                               n26 = b3;
                                                               if (!isControlFlowFlag) {
                                                                  if (b3 == 0) {
                                                                     break exitBlock64;
                                                                  }

                                                                  n26 = (ClientManagers.moduleManager.fixes.shouldApplyIntaveBlockingFix()) ? 1 : 0;
                                                               }

                                                               if (isControlFlowFlag) {
                                                                  break exitBlock62;
                                                               }

                                                               if (n26 == 0) {
                                                                  break exitBlock63;
                                                               }
                                                            }

                                                            this.playerController.onStoppedUsingItem(this.thePlayer);
                                                            if (!isControlFlowFlag) {
                                                               break exitBlock63;
                                                            }
                                                         }

                                                         n28 = 0;
                                                      }

                                                      int n29 = n28;
                                                      int n30 = 0;

                                                      exitBlock65: {
                                                         while (n30 < n22) {
                                                            n28 = (event3.isBreakingBlock()) ? 1 : 0;
                                                            if (isControlFlowFlag) {
                                                               break exitBlock65;
                                                            }

                                                            exitBlock66:
                                                            if (!isControlFlowFlag) {
                                                               if (n28 == 0) {
                                                                  n28 = event3.getLeftClicks();
                                                                  if (isControlFlowFlag) {
                                                                     break exitBlock66;
                                                                  }

                                                                  exitBlock67:
                                                                  if (n28 > 0) {
                                                                     exitBlock68: {
                                                                        MovingObjectPosition objectMouseOver2 = this.objectMouseOver;
                                                                        if (!isControlFlowFlag) {
                                                                           if (this.objectMouseOver == null) {
                                                                              break exitBlock67;
                                                                           }

                                                                           mc10 = this;
                                                                           if (isControlFlowFlag) {
                                                                              break exitBlock68;
                                                                           }

                                                                           objectMouseOver2 = this.objectMouseOver;
                                                                        }

                                                                        if (objectMouseOver2.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
                                                                           break exitBlock67;
                                                                        }

                                                                        mc10 = this;
                                                                     }

                                                                     n28 = (mc10.wasLeftClicking) ? 1 : 0;
                                                                     if (isControlFlowFlag) {
                                                                        break exitBlock66;
                                                                     }

                                                                     if (mc10.wasLeftClicking) {
                                                                        byte b6 = 100;
                                                                        byte b7 = 0;
                                                                        n28 = DefaultRandomGenerator.instance.nextInt2(b7, b6);
                                                                        if (isControlFlowFlag) {
                                                                           break exitBlock65;
                                                                        }

                                                                        if (n28 >= 20) {
                                                                           break;
                                                                        }
                                                                     }
                                                                  }
                                                               }

                                                               event3.getLeftClickAction().run();
                                                               EventClick.pendingBlockBreakDelay = true;
                                                               EventClick.clicksThisTick++;
                                                               n28 = 1;
                                                            }

                                                            n29 = n28;
                                                            n30++;
                                                            if (isControlFlowFlag) {
                                                               break;
                                                            }
                                                         }

                                                         n28 = (event3.isOverrideBlockBreak()) ? 1 : 0;
                                                      }

                                                      exitBlock69: {
                                                         if (!isControlFlowFlag) {
                                                            if (n28 != 0) {
                                                               exitBlock70: {
                                                                  exitBlock71: {
                                                                     n28 = this.leftClickCounter;
                                                                     if (!isControlFlowFlag) {
                                                                        if (this.leftClickCounter > 100) {
                                                                           break exitBlock71;
                                                                        }

                                                                        n28 = (event3.isBreakingBlock()) ? 1 : 0;
                                                                     }

                                                                     if (n28 != 0 && this.currentScreen == null) {
                                                                        n3 = 1;
                                                                        break exitBlock70;
                                                                     }
                                                                  }

                                                                  n3 = 0;
                                                               }

                                                               n27 = n3;
                                                               if (!isControlFlowFlag) {
                                                                  break exitBlock69;
                                                               }
                                                            }

                                                            n28 = (event3.isDelayBlockBreak()) ? 1 : 0;
                                                         }

                                                         exitBlock72: {
                                                            exitBlock73: {
                                                               exitBlock74: {
                                                                  if (!isControlFlowFlag) {
                                                                     if (n28 != 0) {
                                                                        exitBlock75: {
                                                                           exitBlock76: {
                                                                              n28 = this.leftClickCounter;
                                                                              if (!isControlFlowFlag) {
                                                                                 if (this.leftClickCounter > 100) {
                                                                                    break exitBlock76;
                                                                                 }

                                                                                 n28 = (EventClick.shouldBreakBlockAfterDelay(event3.isDelayBlockBreak())) ? 1 : 0;
                                                                              }

                                                                              if (isControlFlowFlag) {
                                                                                 break exitBlock75;
                                                                              }

                                                                              if (n28 != 0) {
                                                                                 n28 = 1;
                                                                                 break exitBlock75;
                                                                              }
                                                                           }

                                                                           n28 = 0;
                                                                        }

                                                                        n27 = n28;
                                                                        if (!isControlFlowFlag) {
                                                                           break exitBlock69;
                                                                        }
                                                                     }

                                                                     mc11 = this;
                                                                     if (isControlFlowFlag) {
                                                                        break exitBlock74;
                                                                     }

                                                                     n28 = this.leftClickCounter;
                                                                  }

                                                                  if (n28 > 100) {
                                                                     break exitBlock73;
                                                                  }

                                                                  mc11 = this;
                                                               }

                                                               exitBlock77:
                                                               if (mc11.currentScreen == null) {
                                                                  n4 = b4;
                                                                  if (!isControlFlowFlag) {
                                                                     if (b4 == 0) {
                                                                        break exitBlock77;
                                                                     }

                                                                     n4 = (this.inGameHasFocus) ? 1 : 0;
                                                                  }

                                                                  if (isControlFlowFlag) {
                                                                     break exitBlock72;
                                                                  }

                                                                  if (n4 != 0) {
                                                                     n4 = 1;
                                                                     break exitBlock72;
                                                                  }
                                                               }
                                                            }

                                                            n4 = 0;
                                                         }

                                                         n27 = n4;
                                                      }

                                                      b5 = 1;
                                                      n28 = n27;
                                                      exitBlock78:
                                                      if (!isControlFlowFlag) {
                                                         if (n27 != 0) {
                                                            n28 = n29;
                                                            if (isControlFlowFlag) {
                                                               break exitBlock78;
                                                            }

                                                            if (n29 == 0) {
                                                               n28 = (EventClick.wasBreakingBlock) ? 1 : 0;
                                                               if (isControlFlowFlag) {
                                                                  break exitBlock78;
                                                               }

                                                               if (!EventClick.wasBreakingBlock && this.objectMouseOver != null) {
                                                                  event3.getLeftClickAction().run();
                                                                  EventClick.pendingBlockBreakDelay = true;
                                                                  EventClick.clicksThisTick++;
                                                               }
                                                            }
                                                         }

                                                         n30 = 0;
                                                         n28 = 0;
                                                      }

                                                      int n31 = n28;

                                                      exitBlock79: {
                                                         do {
                                                            exitBlock80: {
                                                               exitBlock81: {
                                                                  exitBlock82: {
                                                                     if (n31 < n24) {
                                                                        n28 = (event3.isAllowRightClick()) ? 1 : 0;
                                                                        if (isControlFlowFlag || isControlFlowFlag) {
                                                                           break exitBlock82;
                                                                        }

                                                                        if (n28 != 0) {
                                                                           n5 = (ClientManagers.moduleManager.fixes.shouldApplyIntaveBlockingFix()) ? 1 : 0;
                                                                           if (isControlFlowFlag) {
                                                                              break exitBlock80;
                                                                           }

                                                                           if (n5 == 0) {
                                                                              break exitBlock81;
                                                                           }
                                                                        }
                                                                     }

                                                                     n28 = n30;
                                                                  }

                                                                  if (isControlFlowFlag) {
                                                                     break exitBlock79;
                                                                  }

                                                                  if (n28 == 0) {
                                                                     break;
                                                                  }

                                                                  n28 = (event3.isAllowMultiRightClick()) ? 1 : 0;
                                                                  if (isControlFlowFlag) {
                                                                     break exitBlock79;
                                                                  }

                                                                  if (n28 == 0) {
                                                                     break;
                                                                  }

                                                                  n28 = n31;
                                                                  if (isControlFlowFlag) {
                                                                     break exitBlock79;
                                                                  }

                                                                  if (n31 >= 10) {
                                                                     break;
                                                                  }
                                                               }

                                                               n5 = (ClientManagers.moduleManager.noMiningTrace.isToggled()) ? 1 : 0;
                                                            }

                                                            exitBlock83:
                                                            if (!isControlFlowFlag) {
                                                               if (n5 != 0) {
                                                                  n5 = (ClientManagers.moduleManager.noMiningTrace.alwaysAtRightClicking.getValue()) ? 1 : 0;
                                                                  if (isControlFlowFlag) {
                                                                     break exitBlock83;
                                                                  }

                                                                  if (n5 != 0) {
                                                                     this.entityRenderer.getMouseOver(1.0F, true);
                                                                  }
                                                               }

                                                               event3.getRightClickAction().run();
                                                               n5 = (ClientManagers.moduleManager.noPlaceDelay.isToggled()) ? 1 : 0;
                                                            }

                                                            exitBlock84: {
                                                               if (!isControlFlowFlag) {
                                                                  exitBlock85:
                                                                  if (n5 != 0) {
                                                                     MovingObjectPosition objectMouseOver3 = this.objectMouseOver;
                                                                     if (!isControlFlowFlag) {
                                                                        if (this.objectMouseOver == null) {
                                                                           break exitBlock85;
                                                                        }

                                                                        objectMouseOver3 = this.objectMouseOver;
                                                                     }

                                                                     exitBlock86: {
                                                                        if (!isControlFlowFlag) {
                                                                           if (objectMouseOver3.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
                                                                              break exitBlock85;
                                                                           }

                                                                           mc12 = this;
                                                                           if (isControlFlowFlag) {
                                                                              break exitBlock86;
                                                                           }

                                                                           objectMouseOver3 = this.objectMouseOver;
                                                                        }

                                                                        if (objectMouseOver3.getBlockPos() == null) {
                                                                           break exitBlock85;
                                                                        }

                                                                        mc12 = this;
                                                                     }

                                                                     mc12.entityRenderer.getMouseOver(1.0F, false);
                                                                     byte b8 = (byte)((Augustus.getInstance().getModuleManager().smartRaytrace.isToggled()) ? 1 : 0);
                                                                     if (!isControlFlowFlag) {
                                                                        if (b8 != 0) {
                                                                           Augustus.getInstance().getModuleManager().smartRaytrace.updateSmartRaytrace();
                                                                        }

                                                                        ItemStack currentEquippedItem = this.thePlayer.getCurrentEquippedItem();
                                                                        BlockPos blockPos = this.objectMouseOver.getBlockPos();
                                                                        Vec3 hitVec = this.objectMouseOver.hitVec;
                                                                        EnumFacing sideHit = this.objectMouseOver.sideHit;
                                                                        BlockPos pos = blockPos;
                                                                        b8 = (byte)((PlayerUtil.canPlaceBlockAt(currentEquippedItem, pos, sideHit, hitVec)) ? 1 : 0);
                                                                     }

                                                                     exitBlock87: {
                                                                        exitBlock88: {
                                                                           if (!isControlFlowFlag) {
                                                                              if (b8 == 0) {
                                                                                 break exitBlock88;
                                                                              }

                                                                              b8 = (byte)((NoPlaceDelay.isFacingEnabled(this.objectMouseOver.sideHit)) ? 1 : 0);
                                                                           }

                                                                           if (isControlFlowFlag) {
                                                                              break exitBlock87;
                                                                           }

                                                                           if (b8 != 0) {
                                                                              b8 = 1;
                                                                              break exitBlock87;
                                                                           }
                                                                        }

                                                                        b8 = 0;
                                                                     }

                                                                     n30 = b8;
                                                                     if (!isControlFlowFlag) {
                                                                        break exitBlock84;
                                                                     }
                                                                  }

                                                                  n5 = 0;
                                                               }

                                                               n30 = n5;
                                                            }

                                                            n31++;
                                                         } while (!isControlFlowFlag);

                                                         n28 = 0;
                                                      }

                                                      n31 = n28;

                                                      while (n31 < n25) {
                                                         this.middleClickMouse();
                                                         n31++;
                                                         if (isControlFlowFlag) {
                                                            break exitBlock60;
                                                         }

                                                         if (isControlFlowFlag) {
                                                            break;
                                                         }
                                                      }
                                                   }

                                                   n26 = b3;
                                                }

                                                if (isControlFlowFlag) {
                                                   break exitBlock59;
                                                }

                                                if (n26 != 0) {
                                                   n26 = this.rightClickDelayTimer;
                                                   if (isControlFlowFlag) {
                                                      break exitBlock59;
                                                   }

                                                   if (this.rightClickDelayTimer == 0) {
                                                      n26 = (this.thePlayer.isUsingItem()) ? 1 : 0;
                                                      if (isControlFlowFlag) {
                                                         break exitBlock59;
                                                      }

                                                      if (n26 == 0) {
                                                         n26 = (event3.isAllowRightClick()) ? 1 : 0;
                                                         if (isControlFlowFlag) {
                                                            break exitBlock59;
                                                         }

                                                         if (n26 != 0) {
                                                            n26 = (ClientManagers.moduleManager.fixes.shouldApplyIntaveBlockingFix()) ? 1 : 0;
                                                            if (isControlFlowFlag) {
                                                               break exitBlock59;
                                                            }

                                                            if (n26 == 0) {
                                                               event3.getRightClickAction().run();
                                                            }
                                                         }
                                                      }
                                                   }
                                                }
                                             }

                                             double clicksThisTick = (double)EventClick.clicksThisTick;
                                             EventClick.recentClicksBuffer.add(clicksThisTick);
                                          }

                                          n26 = b5;
                                       }

                                       exitBlock89: {
                                          if (!isControlFlowFlag) {
                                             if (n26 != 0) {
                                                break exitBlock89;
                                             }

                                             n26 = (event3.isOverrideBlockBreak()) ? 1 : 0;
                                          }

                                          if (!isControlFlowFlag) {
                                             if (n26 != 0) {
                                                exitBlock90: {
                                                   exitBlock91: {
                                                      n26 = this.leftClickCounter;
                                                      if (!isControlFlowFlag) {
                                                         if (this.leftClickCounter > 100) {
                                                            break exitBlock91;
                                                         }

                                                         n26 = (event3.isBreakingBlock()) ? 1 : 0;
                                                      }

                                                      if (n26 != 0 && this.currentScreen == null) {
                                                         n6 = 1;
                                                         break exitBlock90;
                                                      }
                                                   }

                                                   n6 = 0;
                                                }

                                                n27 = n6;
                                                if (!isControlFlowFlag) {
                                                   break exitBlock89;
                                                }
                                             }

                                             n26 = (event3.isDelayBlockBreak()) ? 1 : 0;
                                          }

                                          exitBlock92: {
                                             exitBlock93: {
                                                exitBlock94: {
                                                   if (!isControlFlowFlag) {
                                                      if (n26 != 0) {
                                                         exitBlock95: {
                                                            exitBlock96: {
                                                               n26 = this.leftClickCounter;
                                                               if (!isControlFlowFlag) {
                                                                  if (this.leftClickCounter > 100) {
                                                                     break exitBlock96;
                                                                  }

                                                                  n26 = (EventClick.shouldBreakBlockAfterDelay(event3.isDelayBlockBreak())) ? 1 : 0;
                                                               }

                                                               if (isControlFlowFlag) {
                                                                  break exitBlock95;
                                                               }

                                                               if (n26 != 0) {
                                                                  n26 = 1;
                                                                  break exitBlock95;
                                                               }
                                                            }

                                                            n26 = 0;
                                                         }

                                                         n27 = n26;
                                                         if (!isControlFlowFlag) {
                                                            break exitBlock89;
                                                         }
                                                      }

                                                      mc13 = this;
                                                      if (isControlFlowFlag) {
                                                         break exitBlock94;
                                                      }

                                                      n26 = this.leftClickCounter;
                                                   }

                                                   if (n26 > 100) {
                                                      break exitBlock93;
                                                   }

                                                   mc13 = this;
                                                }

                                                exitBlock97:
                                                if (mc13.currentScreen == null) {
                                                   n7 = b4;
                                                   if (!isControlFlowFlag) {
                                                      if (b4 == 0) {
                                                         break exitBlock97;
                                                      }

                                                      n7 = (this.inGameHasFocus) ? 1 : 0;
                                                   }

                                                   if (isControlFlowFlag) {
                                                      break exitBlock92;
                                                   }

                                                   if (n7 != 0) {
                                                      n7 = 1;
                                                      break exitBlock92;
                                                   }
                                                }
                                             }

                                             n7 = 0;
                                          }

                                          n27 = n7;
                                       }

                                       exitBlock98: {
                                          Runnable blockBreakAction = event3.getBlockBreakAction();
                                          if (!isControlFlowFlag) {
                                             if (blockBreakAction == null) {
                                                this.sendClickBlockToController((n27 != 0));
                                                EventClick.wasBreakingBlock = (n27 != 0);
                                                if (!isControlFlowFlag) {
                                                   break exitBlock98;
                                                }
                                             }

                                             blockBreakAction = event3.getBlockBreakAction();
                                          }

                                          blockBreakAction.run();
                                       }

                                       this.wasLeftClicking = (n27 != 0);
                                    }
                                 }

                                 mc8 = this;
                                 if (isControlFlowFlag) {
                                    break exitBlock14;
                                 }

                                 n10 = (this.isNotInGui()) ? 1 : 0;
                              }

                              if (n10 != 0) {
                                 int n32 = this.ticksWithoutScreen + 1;
                                 this.ticksWithoutScreen = n32;
                                 if (!isControlFlowFlag) {
                                    if (isControlFlowFlag) {
                                       n10 = (this.entityRenderer.isShaderActive()) ? 1 : 0;
                                       if (isControlFlowFlag) {
                                          break exitBlock8;
                                       }

                                       if (n10 != 0) {
                                          this.entityRenderer.stopUseShader();
                                       }
                                       break exitBlock9;
                                    }

                                    if (this.theWorld == null) {
                                       break exitBlock11;
                                    }

                                    mc14 = this;
                                    if (isControlFlowFlag) {
                                       break exitBlock12;
                                    }

                                    if (this.thePlayer == null) {
                                       this.mcProfiler.endStartSection("gameRenderer");
                                       mc14 = this;
                                       break exitBlock12;
                                    }

                                    TickListener.onTick2(TickPhase.TICK);
                                    int n33 = this.joinPlayerCounter + 1;
                                    this.joinPlayerCounter = n33;
                                    n10 = this.joinPlayerCounter;
                                    if (isControlFlowFlag) {
                                       if (!isControlFlowFlag) {
                                          if (this.joinPlayerCounter == 0) {
                                             this.entityRenderer.updateRenderer();
                                          }

                                          this.mcProfiler.endStartSection("levelRenderer");
                                          n10 = (this.isGamePaused) ? 1 : 0;
                                       }

                                       if (!isControlFlowFlag) {
                                          if (n10 == 0) {
                                             this.renderGlobal.updateClouds();
                                          }

                                          this.mcProfiler.endStartSection("level");
                                          n10 = (this.isGamePaused) ? 1 : 0;
                                       }

                                       if (isControlFlowFlag) {
                                          break exitBlock8;
                                       }

                                       if (n10 != 0) {
                                          break exitBlock10;
                                       }

                                       n10 = this.theWorld.getLastLightningBolt();
                                       if (!isControlFlowFlag) {
                                          if (n10 > 0) {
                                             this.theWorld.setLastLightningBolt(this.theWorld.getLastLightningBolt() - 1);
                                          }

                                          this.theWorld.updateEntities();
                                          n10 = (Augustus.getInstance().getModuleManager().baritone.isToggled()) ? 1 : 0;
                                       }

                                       if (isControlFlowFlag) {
                                          break exitBlock8;
                                       }

                                       if (n10 == 0) {
                                          break exitBlock9;
                                       }

                                       exitBlock99: {
                                          IBaritone baritoneForPlayer2 = BaritoneAPI.getProvider().getBaritoneForPlayer(this.thePlayer);
                                          IBaritone baritone3 = baritoneForPlayer2;
                                          if (!isControlFlowFlag) {
                                             if (baritoneForPlayer2 == null) {
                                                break exitBlock99;
                                             }

                                             baritone3 = baritoneForPlayer2;
                                          }

                                          baritone3.getGameEventHandler().onPlayerUpdate(new PlayerUpdateEvent(EventState.POST));
                                       }

                                       if (!isControlFlowFlag) {
                                          break exitBlock9;
                                       }
                                       break exitBlock11;
                                    }

                                    n9 = 30;
                                    break exitBlock13;
                                 }
                              }

                              mc8 = this;
                           }

                           byte b9 = 0;
                           mc8.ticksWithoutScreen = b9;
                           if (isControlFlowFlag) {
                              n10 = (this.entityRenderer.isShaderActive()) ? 1 : 0;
                              if (isControlFlowFlag) {
                                 break exitBlock8;
                              }

                              if (n10 != 0) {
                                 this.entityRenderer.stopUseShader();
                              }
                              break exitBlock9;
                           }

                           if (this.theWorld == null) {
                              break exitBlock11;
                           }

                           mc14 = this;
                           if (isControlFlowFlag) {
                              break exitBlock12;
                           }

                           if (this.thePlayer == null) {
                              this.mcProfiler.endStartSection("gameRenderer");
                              mc14 = this;
                              break exitBlock12;
                           }

                           TickListener.onTick2(TickPhase.TICK);
                           int n34 = this.joinPlayerCounter + 1;
                           this.joinPlayerCounter = n34;
                           n10 = this.joinPlayerCounter;
                           if (isControlFlowFlag) {
                              if (!isControlFlowFlag) {
                                 if (this.joinPlayerCounter == 0) {
                                    this.entityRenderer.updateRenderer();
                                 }

                                 this.mcProfiler.endStartSection("levelRenderer");
                                 n10 = (this.isGamePaused) ? 1 : 0;
                              }

                              if (!isControlFlowFlag) {
                                 if (n10 == 0) {
                                    this.renderGlobal.updateClouds();
                                 }

                                 this.mcProfiler.endStartSection("level");
                                 n10 = (this.isGamePaused) ? 1 : 0;
                              }

                              if (isControlFlowFlag) {
                                 break exitBlock8;
                              }

                              if (n10 != 0) {
                                 break exitBlock10;
                              }

                              n10 = this.theWorld.getLastLightningBolt();
                              if (!isControlFlowFlag) {
                                 if (n10 > 0) {
                                    this.theWorld.setLastLightningBolt(this.theWorld.getLastLightningBolt() - 1);
                                 }

                                 this.theWorld.updateEntities();
                                 n10 = (Augustus.getInstance().getModuleManager().baritone.isToggled()) ? 1 : 0;
                              }

                              if (isControlFlowFlag) {
                                 break exitBlock8;
                              }

                              if (n10 == 0) {
                                 break exitBlock9;
                              }

                              exitBlock100: {
                                 IBaritone baritoneForPlayer3 = BaritoneAPI.getProvider().getBaritoneForPlayer(this.thePlayer);
                                 IBaritone baritone4 = baritoneForPlayer3;
                                 if (!isControlFlowFlag) {
                                    if (baritoneForPlayer3 == null) {
                                       break exitBlock100;
                                    }

                                    baritone4 = baritoneForPlayer3;
                                 }

                                 baritone4.getGameEventHandler().onPlayerUpdate(new PlayerUpdateEvent(EventState.POST));
                              }

                              if (!isControlFlowFlag) {
                                 break exitBlock9;
                              }
                              break exitBlock11;
                           }

                           n9 = 30;
                        }

                        if (n10 == n9) {
                           byte b10 = 0;
                           this.joinPlayerCounter = b10;
                           this.theWorld.joinEntityInSurroundings(this.thePlayer);
                        }

                        this.mcProfiler.endStartSection("gameRenderer");
                        mc14 = this;
                     }

                     n10 = (mc14.isGamePaused) ? 1 : 0;
                     if (!isControlFlowFlag) {
                        if (!mc14.isGamePaused) {
                           this.entityRenderer.updateRenderer();
                        }

                        this.mcProfiler.endStartSection("levelRenderer");
                        n10 = (this.isGamePaused) ? 1 : 0;
                     }

                     if (!isControlFlowFlag) {
                        if (n10 == 0) {
                           this.renderGlobal.updateClouds();
                        }

                        this.mcProfiler.endStartSection("level");
                        n10 = (this.isGamePaused) ? 1 : 0;
                     }

                     if (isControlFlowFlag) {
                        break exitBlock8;
                     }

                     if (n10 != 0) {
                        break exitBlock10;
                     }

                     n10 = this.theWorld.getLastLightningBolt();
                     if (!isControlFlowFlag) {
                        if (n10 > 0) {
                           this.theWorld.setLastLightningBolt(this.theWorld.getLastLightningBolt() - 1);
                        }

                        this.theWorld.updateEntities();
                        n10 = (Augustus.getInstance().getModuleManager().baritone.isToggled()) ? 1 : 0;
                     }

                     if (isControlFlowFlag) {
                        break exitBlock8;
                     }

                     if (n10 == 0) {
                        break exitBlock9;
                     }

                     exitBlock101: {
                        IBaritone baritoneForPlayer4 = BaritoneAPI.getProvider().getBaritoneForPlayer(this.thePlayer);
                        IBaritone baritone5 = baritoneForPlayer4;
                        if (!isControlFlowFlag) {
                           if (baritoneForPlayer4 == null) {
                              break exitBlock101;
                           }

                           baritone5 = baritoneForPlayer4;
                        }

                        baritone5.getGameEventHandler().onPlayerUpdate(new PlayerUpdateEvent(EventState.POST));
                     }

                     if (!isControlFlowFlag) {
                        break exitBlock9;
                     }
                  }

                  n10 = (this.entityRenderer.isShaderActive()) ? 1 : 0;
                  if (isControlFlowFlag) {
                     break exitBlock8;
                  }

                  if (n10 != 0) {
                     this.entityRenderer.stopUseShader();
                  }
                  break exitBlock9;
               }

               mc15 = this;
               if (isControlFlowFlag) {
                  break exitBlock7;
               }

               n10 = (this.isGamePaused) ? 1 : 0;
               break exitBlock8;
            }

            mc15 = this;
            if (isControlFlowFlag) {
               break exitBlock7;
            }

            n10 = (this.isGamePaused) ? 1 : 0;
         }

         if (n10 == 0) {
            this.mcMusicTicker.update();
            this.mcSoundHandler.update();
         }

         mc15 = this;
      }

      exitBlock102: {
         exitBlock103: {
            if (!isControlFlowFlag) {
               if (mc15.theWorld != null) {
                  boolean isGamePaused2 = this.isGamePaused;
                  if (!isControlFlowFlag) {
                     if (!this.isGamePaused) {
                        this.theWorld.setAllowedSpawnTypes(this.theWorld.getDifficulty() != EnumDifficulty.PEACEFUL, true);

                        try {
                           this.theWorld.tick();
                        } catch (Throwable e3) {
                           CrashReport crashReport3 = CrashReport.makeCrashReport(e3, "Exception in world tick");
                           WorldClient theWorld2 = this.theWorld;
                           if (!isControlFlowFlag) {
                              if (this.theWorld == null) {
                                 CrashReportCategory crashReportCategory3 = crashReport3.makeCategory("Affected level");
                                 crashReportCategory3.addCrashSection("Problem", "Level is null!");
                                 if (!isControlFlowFlag) {
                                    throw new ReportedException(crashReport3);
                                 }
                              }

                              theWorld2 = this.theWorld;
                           }

                           theWorld2.addWorldInfoToCrashReport(crashReport3);
                           throw new ReportedException(crashReport3);
                        }
                     }

                     this.mcProfiler.endStartSection("animateTick");
                     isGamePaused2 = this.isGamePaused;
                  }

                  if (!isControlFlowFlag) {
                     exitBlock104: {
                        if (!isGamePaused2) {
                           mc15 = this;
                           if (isControlFlowFlag) {
                              break exitBlock104;
                           }

                           if (this.theWorld != null) {
                              this.theWorld
                                 .doVoidFogParticles(
                                    MathHelper.floor_double(this.thePlayer.posX),
                                    MathHelper.floor_double(this.thePlayer.posY),
                                    MathHelper.floor_double(this.thePlayer.posZ)
                                 );
                           }
                        }

                        this.mcProfiler.endStartSection("particles");
                        mc15 = this;
                     }

                     if (isControlFlowFlag) {
                        break exitBlock102;
                     }

                     isGamePaused2 = mc15.isGamePaused;
                  }

                  if (isGamePaused2) {
                     break exitBlock103;
                  }

                  this.effectRenderer.updateEffects();
                  if (!isControlFlowFlag) {
                     break exitBlock103;
                  }
               }

               mc15 = this;
            }

            if (isControlFlowFlag) {
               break exitBlock102;
            }

            if (mc15.myNetworkManager != null) {
               this.mcProfiler.endStartSection("pendingConnection");
               this.myNetworkManager.processReceivedPackets();
            }
         }

         mc15 = this;
      }

      exitBlock105:
      if (!isControlFlowFlag) {
         if (mc15.theWorld != null) {
            mc15 = this;
            if (isControlFlowFlag) {
               break exitBlock105;
            }

            if (this.thePlayer != null) {
               TickListener.onTick2(TickPhase.LATE);
            }
         }

         this.mcProfiler.endSection();
         mc15 = this;
      }

      long systemTime3 = getSystemTime();
      mc15.systemTime = systemTime3;
      ToggleUtil.flushPendingToggles();
      if (Augustus.getInstance().getModuleManager().baritone.isToggled()) {
         for (IBaritone baritone6 : (Iterable<IBaritone>)(BaritoneAPI.getProvider().getAllBaritones())) {
            exitBlock106: {
               exitBlock107: {
                  IPlayerContext playerContext2 = baritone6.getPlayerContext();
                  if (!isControlFlowFlag) {
                     if (playerContext2.player() == null) {
                        break exitBlock107;
                     }

                     playerContext2 = baritone6.getPlayerContext();
                  }

                  if (playerContext2.world() != null) {
                     type2 = TickEvent.Type.IN;
                     break exitBlock106;
                  }
               }

               type2 = TickEvent.Type.OUT;
            }

            TickEvent.Type type4 = type2;
            baritone6.getGameEventHandler().onPostTick((TickEvent)biFunction.apply(EventState.POST, type4));
            if (isControlFlowFlag) {
               break;
            }
         }
      }
   }

   public void setServerData(ServerData serverData) {
      this.currentServerData = serverData;
      getMinecraft().fontRendererObj.clearFilterPatterns();
   }

   public void setIngameNotInFocus() {
      boolean isControlFlowDisabled = isControlFlowDisabled();
      Minecraft mc = this;
      if (isControlFlowDisabled) {
         if (!this.inGameHasFocus) {
            return;
         }

         KeyBinding.unPressAllKeys();
         boolean flag = false;
         this.inGameHasFocus = flag;
         mc = this;
      }

      mc.mouseHelper.ungrabMouseCursor();
   }

   public PropertyMap getTwitchDetails() {
      boolean isControlFlowFlag = isControlFlowFlag();
      PropertyMap twitchDetails2 = this.twitchDetails;
      if (!isControlFlowFlag) {
         if (this.twitchDetails.isEmpty()) {
            GameProfile profile = this.getSessionService().fillProfileProperties(this.session.getProfile(), false);
            this.twitchDetails.putAll(profile.getProperties());
         }

         twitchDetails2 = this.twitchDetails;
      }

      return twitchDetails2;
   }

   public CrashReport addGraphicsAndWorldToCrashReport(CrashReport crashReport) {
      crashReport.getCategory().addCrashSectionCallable("Launched Version", new Minecraft.ValueCallable3(this));
      boolean isControlFlowDisabled = isControlFlowDisabled();
      crashReport.getCategory().addCrashSectionCallable("LWJGL", new Minecraft.ValueCallable4(this));
      boolean flag = isControlFlowDisabled;
      crashReport.getCategory().addCrashSectionCallable("OpenGL", new Minecraft.ValueCallable5(this));
      crashReport.getCategory().addCrashSectionCallable("GL Caps", new Minecraft.ValueCallable6(this));
      crashReport.getCategory().addCrashSectionCallable("Using VBOs", new Minecraft.ValueCallable7(this));
      crashReport.getCategory().addCrashSectionCallable("Is Modded", new Minecraft.ValueCallable8(this));
      crashReport.getCategory().addCrashSectionCallable("Type", new Minecraft.ValueCallable9(this));
      crashReport.getCategory().addCrashSectionCallable("Resource Packs", new Minecraft.ValueCallable10(this));
      crashReport.getCategory().addCrashSectionCallable("Current Language", new Minecraft.ValueCallable11(this));
      crashReport.getCategory().addCrashSectionCallable("Profiler Position", new Minecraft.ValueCallable12(this));
      CrashReport crashReport2 = crashReport;
      if (flag) {
         crashReport.getCategory().addCrashSectionCallable("CPU", new Minecraft.ValueCallable13(this));
         if (this.theWorld != null) {
            this.theWorld.addWorldInfoToCrashReport(crashReport);
         }

         crashReport2 = crashReport;
      }

      return crashReport2;
   }

   public Proxy getProxy() {
      return this.proxy;
   }

   private void runScheduledTasks() {
      boolean isControlFlowFlag = isControlFlowFlag();
      synchronized (this.scheduledTasks) {
         while (!this.scheduledTasks.isEmpty()) {
            Util.runTask((FutureTask)this.scheduledTasks.poll(), LOGGER);
            if (!isControlFlowFlag && !isControlFlowFlag) {
               continue;
            }
            break;
         }
      }
   }

   private void updateDebugProfilerName(int n) {
      boolean isControlFlowFlag = isControlFlowFlag();
      ArrayList list = new ArrayList();
      Object obj = list;
      if (!isControlFlowFlag) {
         if (list == null) {
            return;
         }

         obj = list;
      }

      if (!isControlFlowFlag) {
         if (((ArrayList)obj).isEmpty()) {
            return;
         }

         obj = list.remove(0);
      }

      Profiler.Result result = (Profiler.Result)obj;
      int n2 = n;
      if (!isControlFlowFlag) {
         if (n == 0) {
            int length = result.profilerName.length();
            if (!isControlFlowFlag) {
               if (length <= 0) {
                  return;
               }

               length = this.debugProfilerName.lastIndexOf(".");
            }

            int n3 = length;
            if (n3 >= 0) {
               String str = this.debugProfilerName.substring(0, n3);
               this.debugProfilerName = str;
            }

            if (!isControlFlowFlag) {
               return;
            }
         }

         n2 = --n;
      }

      if (!isControlFlowFlag) {
         if (n2 >= list.size()) {
            return;
         }

         n2 = (((Profiler.Result)list.get(n)).profilerName.equals("unspecified")) ? 1 : 0;
      }

      exitBlock: {
         if (!isControlFlowFlag) {
            if (n2 != 0) {
               return;
            }

            obj = this;
            if (isControlFlowFlag) {
               break exitBlock;
            }

            n2 = this.debugProfilerName.length();
         }

         if (n2 > 0) {
            String str2 = this.debugProfilerName + ".";
            this.debugProfilerName = str2;
         }

         obj = this;
      }

      String str3 = this.debugProfilerName + ((Profiler.Result)list.get(n)).profilerName;
      ((Minecraft)obj).debugProfilerName = str3;
   }

   public boolean isSingleplayer() {
      boolean isControlFlowFlag = isControlFlowFlag();
      Minecraft mc = this;
      if (!isControlFlowFlag) {
         if (!this.integratedServerIsRunning) {
            return false;
         }

         mc = this;
      }

      return mc.theIntegratedServer != null;
   }

   public boolean isUnicode() {
      return this.mcLanguageManager.isCurrentLocaleUnicode() || this.gameSettings.forceUnicodeFont;
   }

   public RenderManager getRenderManager() {
      return this.renderManager;
   }

   public String getVersion() {
      return this.version;
   }

   public boolean isConnectedToRealms() {
      return this.connectedToRealms;
   }

   public BlockRendererDispatcher getBlockRendererDispatcher() {
      return this.blockRenderDispatcher;
   }

   public Timer getTimer() {
      return this.timer;
   }

   public static Map getSessionInfo() {
      HashMap map = Maps.newHashMap();
      map.put("X-Minecraft-Username", getMinecraft().getSession().getUsername());
      map.put("X-Minecraft-UUID", getMinecraft().getSession().getPlayerID());
      map.put("X-Minecraft-Version", "1.8.9");
      return map;
   }

   public void setWindowIcon(boolean flag) {
      boolean isControlFlowDisabled = isControlFlowDisabled();
      Util.EnumOS osType = Util.getOSType();
      if (osType != Util.EnumOS.OSX) {
         InputStream in = null;
         InputStream in2 = null;
         boolean flag2 = false /* VF: Semaphore variable */;

         InputStream in3;
         exitBlock: {
            exitBlock2: {
               try {
                  flag2 = true;
                  if (flag) {
                     in = this.mcDefaultResourcePack.getInputStreamAssets(new ResourceLocation("icons/icon_16x16.png"));
                     in2 = this.mcDefaultResourcePack.getInputStreamAssets(new ResourceLocation("icons/icon_32x32.png"));
                  } else {
                     in = Minecraft.class.getClassLoader().getResourceAsStream("assets/augustus/pictures/logo_16x16.png");
                     in2 = Minecraft.class.getClassLoader().getResourceAsStream("assets/augustus/pictures/logo_32x32.png");
                  }

                  in3 = in;
                  if (!isControlFlowDisabled) {
                     flag2 = false;
                     break exitBlock;
                  }

                  if (in != null) {
                     in3 = in2;
                     if (!isControlFlowDisabled) {
                        flag2 = false;
                        break exitBlock;
                     }

                     if (in2 != null) {
                        Display.setIcon(new ByteBuffer[]{this.readImageToBuffer(in), this.readImageToBuffer(in2)});
                        flag2 = false;
                     } else {
                        flag2 = false;
                     }
                  } else {
                     flag2 = false;
                  }
               } catch (IOException e) {
                  LOGGER.error("Couldn't set icon", e);
                  flag2 = false;
                  break exitBlock2;
               } finally {
                  if (flag2) {
                     IOUtils.closeQuietly(in);
                     IOUtils.closeQuietly(in2);
                  }
               }

               IOUtils.closeQuietly(in);
               in3 = in2;
               break exitBlock;
            }

            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(in2);
            return;
         }

         IOUtils.closeQuietly(in3);
      }
   }

   public void rightClickMouse() {
      boolean flag;
      int n;
      flag = isControlFlowDisabled();
      n = (this.playerController.isHittingBlock()) ? 1 : 0;
      exitBlock:
      if (flag) {
         if (n != 0) {
            n = (ClientManagers.moduleManager.fixes.isPlaceWhileBreakEnabled()) ? 1 : 0;
            if (!flag) {
               break exitBlock;
            }

            if (n == 0) {
               return;
            }
         }

         n = this.rightClickDelayTimer;
      }

      exitBlock2: {
         byte b;
         exitBlock3: {
            int n2;
            ItemStack stack;
            exitBlock4: {
               n2 = n;
               byte b2 = 4;
               this.rightClickDelayTimer = b2;
               b = 1;
               stack = this.thePlayer.inventory.getCurrentItem();
               if (flag) {
                  if (this.objectMouseOver != null) {
                     break exitBlock4;
                  }

                  LOGGER.warn("Null returned as 'hitResult', this shouldn't happen!");
               }

               if (flag) {
                  break exitBlock3;
               }
            }

            n = Minecraft.EnumMinecartTypeSwitchMap.MOVING_OBJECT_TYPE_ORDINAL_MAP[this.objectMouseOver.typeOfHit.ordinal()];
            if (!flag) {
               break exitBlock2;
            }

            switch (n) {
               case 1:
                  n = (this.playerController.isPlayerRightClickingOnEntity(this.thePlayer, this.objectMouseOver.entityHit, this.objectMouseOver)) ? 1 : 0;
                  if (flag) {
                     if (n != 0) {
                        b = 0;
                        if (flag) {
                           break;
                        }
                     }

                     n = (this.playerController.interactWithEntitySendPacket(this.thePlayer, this.objectMouseOver.entityHit)) ? 1 : 0;
                  }

                  if (!flag) {
                     break exitBlock2;
                  }

                  if (n == 0) {
                     break;
                  }

                  b = 0;
                  if (flag) {
                     break;
                  }
               case 2:
                  BlockPos blockPos = this.objectMouseOver.getBlockPos();
                  exitBlock5:
                  if (this.theWorld.getBlockState(blockPos).getBlock().getMaterial() != Material.air) {
                     exitBlock6: {
                        ItemStack stack2 = stack;
                        if (flag) {
                           if (stack == null) {
                              n = 0;
                              break exitBlock6;
                           }

                           stack2 = stack;
                        }

                        n = stack2.stackSize;
                     }

                     int n3 = n;
                     EventBlockPlace event = BlockPlaceListener.onBlockPlace3(BlockPlaceStage.PRE);
                     boolean isCancelled = event.isCancelled();
                     if (flag) {
                        if (isCancelled) {
                           this.rightClickDelayTimer = n2;
                           return;
                        }

                        isCancelled = this.playerController.onPlayerRightClick(this.thePlayer, this.theWorld, stack, blockPos, this.objectMouseOver.sideHit, this.objectMouseOver.hitVec);
                     }

                     exitBlock7: {
                        if (flag) {
                           if (!isCancelled) {
                              break exitBlock7;
                           }

                           b = 0;
                           this.thePlayer.swingItem();
                           isCancelled = Augustus.getInstance().getModuleManager().baritone.isToggled();
                        }

                        if (isCancelled) {
                           BaritoneAPI.getProvider().getPrimaryBaritone().getGameEventHandler().onBlockInteract(new BlockInteractEvent(blockPos, BlockInteractEvent.Type.USE));
                        }
                     }

                     ItemStack stack3 = stack;
                     if (flag) {
                        if (stack == null) {
                           return;
                        }

                        stack3 = stack;
                     }

                     n = stack3.stackSize;
                     if (flag) {
                        if (stack3.stackSize == 0) {
                           this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
                           if (flag) {
                              break exitBlock5;
                           }
                        }

                        n = stack.stackSize;
                     }

                     exitBlock8: {
                        if (flag) {
                           if (n != n3) {
                              break exitBlock8;
                           }

                           n = (this.playerController.isInCreativeMode()) ? 1 : 0;
                        }

                        if (!flag) {
                           break exitBlock2;
                        }

                        if (n == 0) {
                           break exitBlock5;
                        }
                     }

                     this.entityRenderer.itemRenderer.resetEquippedProgress();
                  }
            }
         }

         n = b;
      }

      if (n != 0) {
         ItemStack currentItem = this.thePlayer.inventory.getCurrentItem();
         if (currentItem != null) {
            Minecraft mc = this;
            if (flag) {
               if (!this.playerController.sendUseItem(this.thePlayer, this.theWorld, currentItem)) {
                  return;
               }

               mc = this;
            }

            mc.entityRenderer.itemRenderer.resetEquippedProgress2();
         }
      }
   }

   public ResourcePackRepository getResourcePackRepository() {
      return this.mcResourcePackRepository;
   }

   public TextureManager getTextureManager() {
      return this.renderEngine;
   }

   public void shutdownMinecraftApplet() {
      boolean isControlFlowFlag = isControlFlowFlag();
      boolean flag = false /* VF: Semaphore variable */;

      try {
         flag = true;
         Augustus.getInstance().getDiscordRpcManager().shutdown();
         LOGGER.info("Stopping!");

         try {
            WorldClient world = (WorldClient)null;
            this.loadWorld(world);
         } catch (Throwable e) {
         }

         this.mcSoundHandler.unloadSounds();
         flag = false;
      } finally {
         if (flag) {
            exitBlock: {
               Display.destroy();
               byte b = (byte)((this.hasCrashed) ? 1 : 0);
               if (!isControlFlowFlag) {
                  if (this.hasCrashed) {
                     break exitBlock;
                  }

                  b = 0;
               }

               System.exit(b);
            }
         }
      }

      Display.destroy();
      if (!isControlFlowFlag) {
         if (!this.hasCrashed) {
            System.exit(0);
         }

         System.gc();
      }
   }

   public Session getSession() {
      return this.session;
   }

   public LanguageManager getLanguageManager() {
      return this.mcLanguageManager;
   }

   public void loadWorld(WorldClient world) {
      String str = "";
      this.loadWorld(world, str);
   }

   public void toggleFullscreen() {

      try {
         this.fullscreen = !this.fullscreen;
         this.gameSettings.fullScreen = this.fullscreen;
         if (this.fullscreen) {
            this.updateDisplayMode();
            this.displayWidth = Display.getDisplayMode().getWidth();
            this.displayHeight = Display.getDisplayMode().getHeight();
            if (this.displayWidth <= 0) {
               this.displayWidth = 1;
            }

            if (this.displayHeight <= 0) {
               this.displayHeight = 1;
            }
         } else {
            Display.setDisplayMode(new DisplayMode(this.tempDisplayWidth, this.tempDisplayHeight));
            this.displayWidth = this.tempDisplayWidth;
            this.displayHeight = this.tempDisplayHeight;
            if (this.displayWidth <= 0) {
               this.displayWidth = 1;
            }

            if (this.displayHeight <= 0) {
               this.displayHeight = 1;
            }
         }

         WindowResizeListener.onWindowResize(this.displayWidth, this.displayHeight);
         if (this.currentScreen != null) {
            this.resize(this.displayWidth, this.displayHeight);
         } else {
            this.updateFramebufferSize();
         }

         Display.setFullscreen(this.fullscreen);
         Display.setVSyncEnabled(this.gameSettings.enableVsync);
         this.updateDisplay();
      } catch (Exception e) {
         LOGGER.error("Couldn't toggle fullscreen", e);
      }
   }

   public void clickMouse() {
      Minecraft mc = null, mc2 = null, mc3 = null;
      boolean flag = false;
      boolean flag2;
      int n;
      exitBlock: {
         exitBlock2: {
            flag2 = isControlFlowFlag();
            MovingObjectPosition objectMouseOver2 = this.objectMouseOver;
            if (!flag2) {
               if (this.objectMouseOver == null) {
                  break exitBlock2;
               }

               objectMouseOver2 = this.objectMouseOver;
            }

            if (!flag2) {
               if (objectMouseOver2.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) {
                  break exitBlock2;
               }

               objectMouseOver2 = this.objectMouseOver;
            }

            n = (ProtectedPlayerCheck.isProtectedPlayer(objectMouseOver2.entityHit)) ? 1 : 0;
            if (flag2) {
               break exitBlock;
            }

            if (n != 0) {
               return;
            }
         }

         n = (ClientManagers.moduleManager.fixes.isToggled()) ? 1 : 0;
      }

      exitBlock3:
      if (!flag2) {
         exitBlock4: {
            exitBlock5:
            if (n != 0) {
               exitBlock6: {
                  exitBlock7: {
                     n = (ClientManagers.moduleManager.fixes.hitDelayFixMode.getSelectedValue().equals("OnlyAtTargetLite")) ? 1 : 0;
                     if (!flag2) {
                        if (n != 0) {
                           mc = this;
                           if (flag2) {
                              break exitBlock6;
                           }

                           if (this.leftClickCounter < 9) {
                              break exitBlock7;
                           }
                        }

                        n = (ClientManagers.moduleManager.fixes.hitDelayFixMode.getSelectedValue().equals("OnlyAtTarget")) ? 1 : 0;
                     }

                     if (flag2) {
                        break exitBlock3;
                     }

                     if (n == 0) {
                        break exitBlock5;
                     }

                     int leftClickCounter2 = this.leftClickCounter;
                     if (!flag2) {
                        leftClickCounter2 = this.leftClickCounter < 9 ? 1 : 0;
                     }

                     int n2 = leftClickCounter2;
                     n = (ClientManagers.moduleManager.fixes.shouldApplyHitDelayFix((n2 != 0))) ? 1 : 0;
                     if (flag2) {
                        break exitBlock3;
                     }

                     if (n == 0) {
                        break exitBlock5;
                     }
                  }

                  mc = this;
               }

               if (flag2) {
                  break exitBlock4;
               }

               if (mc.objectMouseOver != null) {
                  mc = this;
                  if (flag2) {
                     break exitBlock4;
                  }

                  if (this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                     byte b = 0;
                     this.leftClickCounter = b;
                  }
               }
            }

            mc = this;
         }

         n = mc.leftClickCounter;
      }

      exitBlock8:
      if (!flag2) {
         exitBlock9: {
            if (n > 0) {
               n = (ClientManagers.moduleManager.debug.lostHitDebug.getValue()) ? 1 : 0;
               if (flag2) {
                  break exitBlock8;
               }

               if (n != 0) {
                  n = (ClientManagers.moduleManager.debug.isToggled()) ? 1 : 0;
                  if (flag2) {
                     break exitBlock8;
                  }

                  if (n != 0) {
                     mc2 = this;
                     if (flag2) {
                        break exitBlock9;
                     }

                     if (this.objectMouseOver != null) {
                        mc2 = this;
                        if (flag2) {
                           break exitBlock9;
                        }

                        if (this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                           ChatUtil.sendChat3("Lost Hit");
                        }
                     }
                  }
               }
            }

            mc2 = this;
         }

         n = mc2.leftClickCounter;
      }

      exitBlock10: {
         if (!flag2) {
            if (n > 0) {
               break exitBlock10;
            }

            n = (ViaAugustus.targetProtocolVersion.olderThanOrEqualTo(ProtocolVersion.v1_8)) ? 1 : 0;
         }

         if (n != 0) {
            this.thePlayer.swingItem();
         }

         exitBlock11: {
            Minecraft mc4 = this;
            if (!flag2) {
               if (this.objectMouseOver != null) {
                  break exitBlock11;
               }

               LOGGER.error("Null returned as 'hitResult', this shouldn't happen!");
               if (flag2) {
                  return;
               }

               mc4 = this;
            }

            if (!mc4.playerController.isNotCreative()) {
               break exitBlock10;
            }

            byte b2 = 10;
            this.leftClickCounter = b2;
            if (!flag2) {
               break exitBlock10;
            }
         }

         exitBlock12: {
            exitBlock13: {
               exitBlock14: {
                  n = Minecraft.EnumMinecartTypeSwitchMap.MOVING_OBJECT_TYPE_ORDINAL_MAP[this.objectMouseOver.typeOfHit.ordinal()];
                  if (!flag2) {
                     switch (n) {
                        case 1:
                           this.playerController.attackEntity(this.thePlayer, this.objectMouseOver.entityHit);
                           if (!flag2) {
                              break exitBlock13;
                           }
                        case 2:
                           BlockPos blockPos = this.objectMouseOver.getBlockPos();
                           mc3 = this;
                           if (flag2) {
                              break;
                           }

                           if (this.theWorld.getBlockState(blockPos).getBlock().getMaterial() != Material.air) {
                              this.playerController.clickBlock(blockPos, this.objectMouseOver.sideHit);
                              flag = Augustus.getInstance().getModuleManager().baritone.isToggled();
                              if (flag2) {
                                 break exitBlock12;
                              }

                              if (!flag) {
                                 break exitBlock13;
                              }

                              BaritoneAPI.getProvider().getPrimaryBaritone().getGameEventHandler().onBlockInteract(new BlockInteractEvent(blockPos, BlockInteractEvent.Type.START_BREAK));
                              if (!flag2) {
                                 break exitBlock13;
                              }
                           }
                        case 3:
                        default:
                           mc3 = this;
                     }

                     if (flag2) {
                        break exitBlock14;
                     }

                     n = (mc3.playerController.isNotCreative()) ? 1 : 0;
                  }

                  if (n != 0) {
                     mc3 = this;
                     if (flag2) {
                        break exitBlock14;
                     }

                     byte b3 = 10;
                     this.leftClickCounter = b3;
                     if (ClientManagers.moduleManager.fixes.isToggled() && ClientManagers.moduleManager.fixes.hitDelayFixMode.getSelectedValue().equals("Default")) {
                        byte b4 = 0;
                        this.leftClickCounter = b4;
                     }
                  }

                  mc3 = this;
               }

               mc3.thePlayer.resetTicksSinceLastSwing();
            }

            flag = ViaAugustus.targetProtocolVersion.newerThanOrEqualTo(ProtocolVersion.v1_9);
         }

         if (flag) {
            this.thePlayer.swingItem();
         }
      }

      LeftClickListener.onLeftClick();
   }

   public final boolean isDemo() {
      return this.isDemo;
   }

   public void shutdown() {
      boolean flag = false;
      this.running = flag;
   }

   public TextureMap getTextureMapBlocks() {
      return this.textureMapBlocks;
   }

   public IntegratedServer getIntegratedServer() {
      return this.theIntegratedServer;
   }

   private boolean shouldAllowUserInput(GuiScreen screen) {
      boolean isControlFlowDisabled = isControlFlowDisabled();
      boolean isToggled = Augustus.getInstance().getModuleManager().baritone.isToggled();
      if (isControlFlowDisabled) {
         if (!isToggled) {
            return screen.allowUserInput;
         }

         isToggled = BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing();
      }

      if (isControlFlowDisabled) {
         if (isToggled && this.thePlayer != null) {
            return true;
         }

         isToggled = screen.allowUserInput;
      }

      return !isControlFlowDisabled ? isToggled : isToggled;
   }

   public FrameTimer getFrameTimer2() {
      return this.frameTimer;
   }

   private void startTimerHackThread() {
      Minecraft.WorkerTask obj = new Minecraft.WorkerTask(this, "Timer hack thread");
      obj.setDaemon(true);
      obj.start();
   }

   public static int getGLMaximumTextureSize() {
      boolean isControlFlowDisabled = isControlFlowDisabled();
      int n = 16384;

      int n2;
      while (true) {
         if (n > 0) {
            GL11.glTexImage2D(32868, 0, 6408, n, n, 0, 6408, 5121, (ByteBuffer)null);
            int n3 = GL11.glGetTexLevelParameteri(32868, 0, 4096);
            n2 = n3;
            if (!isControlFlowDisabled) {
               break;
            }

            if (isControlFlowDisabled) {
               if (n3 != 0) {
                  return n;
               }

               n2 = n >> 1;
            }

            n = n2;
            if (isControlFlowDisabled) {
               continue;
            }
         }

         n2 = -1;
         break;
      }

      return n2;
   }

   public void dispatchKeypresses() {
      boolean isControlFlowDisabled = isControlFlowDisabled();
      int eventKey = Keyboard.getEventKey();
      if (isControlFlowDisabled) {
         eventKey = eventKey == 0 ? Keyboard.getEventCharacter() : Keyboard.getEventKey();
      }

      int n = eventKey;
      eventKey = n;
      if (isControlFlowDisabled) {
         if (n == 0) {
            return;
         }

         eventKey = (Keyboard.isRepeatEvent()) ? 1 : 0;
      }

      if (isControlFlowDisabled) {
         if (eventKey != 0) {
            return;
         }

         eventKey = (this.currentScreen instanceof GuiControls) ? 1 : 0;
      }

      exitBlock:
      if (isControlFlowDisabled) {
         if (eventKey != 0) {
            long l;
            eventKey = (l = ((GuiControls)this.currentScreen).time - (getSystemTime() - 20L)) == 0L ? 0 : (l < 0L ? -1 : 1);
            if (!isControlFlowDisabled) {
               break exitBlock;
            }

            if (eventKey > 0) {
               return;
            }
         }

         eventKey = (Keyboard.getEventKeyState()) ? 1 : 0;
      }

      if (isControlFlowDisabled) {
         if (eventKey == 0) {
            return;
         }

         eventKey = n;
      }

      int keyCode = this.gameSettings.keyBindFullscreen.getKeyCode();
      if (isControlFlowDisabled) {
         if (eventKey == keyCode) {
            this.toggleFullscreen();
            if (isControlFlowDisabled) {
               return;
            }
         }

         eventKey = n;
         keyCode = this.gameSettings.keyBindScreenshot.getKeyCode();
      }

      if (eventKey == keyCode) {
         ScreenShotHelper.saveScreenshotAsync(this.mcDataDir, this.displayWidth, this.displayHeight, this.framebufferMc);
      }
   }

   private void updateDisplayMode() throws LWJGLException {
      HashSet set = Sets.newHashSet();
      int n = (isControlFlowFlag()) ? 1 : 0;
      Collections.addAll(set, Display.getAvailableDisplayModes());
      boolean flag = (n != 0);
      DisplayMode desktopDisplayMode = Display.getDesktopDisplayMode();
      if (!flag) {
         if (!set.contains(desktopDisplayMode) && Util.getOSType() == Util.EnumOS.OSX) {
            outerLoop:
            for (DisplayMode displayMode : (Iterable<DisplayMode>)(macDisplayModes)) {
               int n2 = 1;
               if (flag) {
                  return;
               }

               Iterator it = set.iterator();

               outerLoop2:
               while (true) {
                  DisplayMode displayMode2;
                  int n3;
                  if (it.hasNext()) {
                     displayMode2 = (DisplayMode)it.next();
                     n = displayMode2.getBitsPerPixel();
                     n3 = 32;
                     if (!flag) {
                        exitBlock: {
                           if (!flag) {
                              if (n != 32) {
                                 if (!flag) {
                                    continue;
                                 }
                                 break exitBlock;
                              }

                              n = displayMode2.getWidth();
                              n3 = displayMode.getWidth();
                           }

                           if (!flag) {
                              if (n == n3) {
                                 n = displayMode2.getHeight();
                                 if (!flag) {
                                    if (n == displayMode.getHeight()) {
                                       n2 = 0;
                                       if (flag && !flag) {
                                          continue;
                                       }
                                    } else if (!flag) {
                                       continue;
                                    }
                                 } else {
                                    n2 = n;
                                    if (flag && !flag) {
                                       continue;
                                    }
                                 }
                              } else if (!flag) {
                                 continue;
                              }
                           } else if (n == n3) {
                              n2 = 0;
                              if (flag && !flag) {
                                 continue;
                              }
                           } else if (!flag) {
                              continue;
                           }
                        }

                        if (n2 != 0) {
                           break;
                        }

                        it = set.iterator();
                        if (!it.hasNext()) {
                           continue outerLoop;
                        }

                        displayMode2 = (DisplayMode)it.next();
                        n = displayMode2.getBitsPerPixel();
                        n3 = 32;
                     }
                  } else {
                     if (n2 != 0) {
                        break;
                     }

                     it = set.iterator();
                     if (!it.hasNext()) {
                        continue outerLoop;
                     }

                     displayMode2 = (DisplayMode)it.next();
                     n = displayMode2.getBitsPerPixel();
                     n3 = 32;
                  }

                  while (true) {
                     if (!flag) {
                        if (n == n3) {
                           n = displayMode2.getWidth();
                           n3 = displayMode.getWidth() / 2;
                           if (!flag) {
                              if (n == n3 && displayMode2.getHeight() == displayMode.getHeight() / 2) {
                                 desktopDisplayMode = displayMode2;
                                 break outerLoop2;
                              }
                           } else if (n == n3) {
                              desktopDisplayMode = displayMode2;
                              break outerLoop2;
                           }
                        }
                     } else if (!flag) {
                        if (n == n3 && displayMode2.getHeight() == displayMode.getHeight() / 2) {
                           desktopDisplayMode = displayMode2;
                           break outerLoop2;
                        }
                     } else if (n == n3) {
                        desktopDisplayMode = displayMode2;
                        break outerLoop2;
                     }

                     if (!it.hasNext()) {
                        continue outerLoop;
                     }

                     displayMode2 = (DisplayMode)it.next();
                     n = displayMode2.getBitsPerPixel();
                     n3 = 32;
                  }
               }

               if (flag) {
                  break;
               }
            }
         }

         Display.setDisplayMode(desktopDisplayMode);
         this.displayWidth = desktopDisplayMode.getWidth();
         this.displayHeight = desktopDisplayMode.getHeight();
      }
   }

   public static String accessVersion(Minecraft mc) {
      return mc.version;
   }

   public void crashed(CrashReport crashReport) {
      boolean flag = true;
      this.hasCrashed = flag;
      this.crashReporter = crashReport;
   }

   public static Minecraft getMinecraft() {
      return theMinecraft;
   }

   private void startGame() throws LWJGLException, IOException {
      ClientHoliday clientHoliday = null, clientHoliday2 = null;
      File[] files = null;
      boolean flag = false, flag2 = false;
      boolean flag3;
      Minecraft mc;
      RawMouseInputThread.INSTANCE.start();
      boolean isControlFlowDisabled = isControlFlowDisabled();
      GameSettings gameSettings2 = new GameSettings(this, this.mcDataDir);
      this.gameSettings = gameSettings2;
      KeyBinding.rebuildMovementKeyMap();
      this.defaultResourcePacks.add(this.mcDefaultResourcePack);
      flag3 = isControlFlowDisabled;
      this.startTimerHackThread();
      mc = this;
      exitBlock:
      if (flag3) {
         if (this.gameSettings.overrideHeight > 0) {
            mc = this;
            if (!flag3) {
               break exitBlock;
            }

            if (this.gameSettings.overrideWidth > 0) {
               this.displayWidth = this.gameSettings.overrideWidth;
               this.displayHeight = this.gameSettings.overrideHeight;
            }
         }

         LOGGER.info("LWJGL Version: " + Sys.getVersion());
         boolean flag4 = false;
         this.setWindowIcon(flag4);
         this.setInitialDisplayMode();
         mc = this;
      }

      mc.createDisplay();
      AbstractSkeletonRenderer.captureModelViewMatrix();
      Augustus.getInstance().getClass();

      exitBlock2: {
         exitBlock3: {
            exitBlock4: {
               exitBlock5: {
                  try {
                     clientHoliday = HolidayDetector.getCurrentHoliday();
                     clientHoliday2 = ClientHoliday.HALLOWEEN;
                     if (!flag3) {
                        break exitBlock5;
                     }

                     if (clientHoliday == ClientHoliday.HALLOWEEN) {
                        String str = "sounds/halloween.mp3";
                        this.audioPlayer.playResource(str);
                        double d = 0.6;
                        this.audioPlayer.setVolume(d);
                        byte b = 10;
                        this.audioPlayer.setCycleCount(b);
                        if (flag3) {
                           break exitBlock4;
                        }
                     }
                  } catch (Exception e) {
                     boolean flag5 = false;
                     break exitBlock4;
                  }

                  try {
                     clientHoliday = HolidayDetector.getCurrentHoliday();
                     clientHoliday2 = ClientHoliday.FIRST_APRIL;
                  } catch (Exception e2) {
                     boolean flag6 = false;
                     break exitBlock4;
                  }
               }

               exitBlock6: {
                  exitBlock7: {
                     try {
                        if (!flag3) {
                           break exitBlock6;
                        }

                        if (clientHoliday != clientHoliday2) {
                           break exitBlock7;
                        }
                     } catch (Exception e3) {
                        boolean flag7 = false;
                        break exitBlock4;
                     }

                     try {
                        String str2 = "sounds/start.mp3";
                        this.audioPlayer.playResource(str2);
                        double d2 = 1.0;
                        this.audioPlayer.setVolume(d2);
                        byte b2 = 10;
                        this.audioPlayer.setCycleCount(b2);
                        if (flag3) {
                           break exitBlock4;
                        }
                     } catch (Exception e4) {
                        boolean flag8 = false;
                        break exitBlock4;
                     }
                  }

                  try {
                     clientHoliday = HolidayDetector.getCurrentHoliday();
                     clientHoliday2 = ClientHoliday.CHRISTMAS_24;
                  } catch (Exception e5) {
                     boolean flag9 = false;
                     break exitBlock4;
                  }
               }

               exitBlock8: {
                  exitBlock9: {
                     try {
                        if (!flag3) {
                           break exitBlock8;
                        }

                        if (clientHoliday != clientHoliday2) {
                           break exitBlock9;
                        }
                     } catch (Exception e6) {
                        boolean flag10 = false;
                        break exitBlock4;
                     }

                     try {
                        String str3 = "sounds/christmas24.mp3";
                        this.audioPlayer.playResource(str3);
                        double d3 = 1.0;
                        this.audioPlayer.setVolume(d3);
                        byte b3 = 10;
                        this.audioPlayer.setCycleCount(b3);
                        if (flag3) {
                           break exitBlock4;
                        }
                     } catch (Exception e7) {
                        boolean flag11 = false;
                        break exitBlock4;
                     }
                  }

                  try {
                     clientHoliday = HolidayDetector.getCurrentHoliday();
                     clientHoliday2 = ClientHoliday.CHRISTMAS_25;
                  } catch (Exception e8) {
                     boolean flag12 = false;
                     break exitBlock4;
                  }
               }

               exitBlock10: {
                  exitBlock11: {
                     try {
                        if (!flag3) {
                           break exitBlock10;
                        }

                        if (clientHoliday != clientHoliday2) {
                           break exitBlock11;
                        }
                     } catch (Exception e9) {
                        boolean flag13 = false;
                        break exitBlock4;
                     }

                     try {
                        String str4 = "sounds/christmas25.mp3";
                        this.audioPlayer.playResource(str4);
                        double d4 = 1.0;
                        this.audioPlayer.setVolume(d4);
                        byte b4 = 10;
                        this.audioPlayer.setCycleCount(b4);
                        if (flag3) {
                           break exitBlock4;
                        }
                     } catch (Exception e10) {
                        boolean flag14 = false;
                        break exitBlock4;
                     }
                  }

                  try {
                     clientHoliday = HolidayDetector.getCurrentHoliday();
                     clientHoliday2 = ClientHoliday.CHRISTMAS_26;
                  } catch (Exception e11) {
                     boolean flag15 = false;
                     break exitBlock4;
                  }
               }

               exitBlock12: {
                  exitBlock13: {
                     try {
                        if (!flag3) {
                           break exitBlock12;
                        }

                        if (clientHoliday != clientHoliday2) {
                           break exitBlock13;
                        }
                     } catch (Exception e12) {
                        boolean flag16 = false;
                        break exitBlock4;
                     }

                     try {
                        String str5 = "sounds/christmas26.mp3";
                        this.audioPlayer.playResource(str5);
                        double d5 = 1.0;
                        this.audioPlayer.setVolume(d5);
                        byte b5 = 10;
                        this.audioPlayer.setCycleCount(b5);
                        if (flag3) {
                           break exitBlock4;
                        }
                     } catch (Exception e13) {
                        boolean flag17 = false;
                        break exitBlock4;
                     }
                  }

                  try {
                     clientHoliday = HolidayDetector.getCurrentHoliday();
                     clientHoliday2 = ClientHoliday.MINIMI;
                  } catch (Exception e14) {
                     boolean flag18 = false;
                     break exitBlock4;
                  }
               }

               try {
                  if (clientHoliday == clientHoliday2) {
                     String str6 = "sounds/hackersong.mp3";
                     this.audioPlayer.playResource(str6);
                     double d6 = 1.0;
                     this.audioPlayer.setVolume(d6);
                     byte b6 = 10;
                     this.audioPlayer.setCycleCount(b6);
                     if (flag3) {
                        break exitBlock4;
                     }
                  }
               } catch (Exception e15) {
                  boolean flag19 = false;
                  break exitBlock4;
               }

               exitBlock14: {
                  File[] files2;
                  try {
                     File file = new File(ClientPaths.CLIENT_DIR + "/sounds");
                     ClientFileUtil.ensureDirectory(ClientPaths.CLIENT_DIR + "/sounds");
                     files2 = file.listFiles();
                     files = files2;
                     if (!flag3) {
                        break exitBlock14;
                     }

                     if (files2 == null) {
                        break exitBlock4;
                     }
                  } catch (Exception e16) {
                     boolean flag20 = false;
                     break exitBlock4;
                  }

                  try {
                     files = files2;
                  } catch (Exception e17) {
                     boolean flag21 = false;
                     break exitBlock4;
                  }
               }

               File[] files3;
               int n;
               int n2;
               try {
                  files3 = files;
                  n = files3.length;
                  n2 = 0;
               } catch (Exception e18) {
                  boolean flag22 = false;
                  break exitBlock4;
               }

               while (true) {
                  exitBlock15: {
                     File file2;
                     try {
                        if (n2 >= n) {
                           break;
                        }

                        file2 = files3[n2];
                        if (!flag3) {
                           break exitBlock15;
                        }

                        flag = file2.getName().split("\\.")[0].equals("start");
                        if (!flag3) {
                           break exitBlock3;
                        }
                     } catch (Exception e19) {
                        boolean flag23 = false;
                        break;
                     }

                     exitBlock16: {
                        try {
                           if (!flag) {
                              break exitBlock16;
                           }
                        } catch (Exception e20) {
                           boolean flag24 = false;
                           break;
                        }

                        try {
                           JavaFxAudioPlayer audioPlayer2 = this.audioPlayer;
                           String absolutePath = file2.getAbsolutePath();
                           String str7 = absolutePath;
                           audioPlayer2.playFile(str7);
                           double d7 = 0.6;
                           this.audioPlayer.setVolume(d7);
                           byte b7 = 10;
                           this.audioPlayer.setCycleCount(b7);
                           if (flag3) {
                              break;
                           }
                        } catch (Exception e21) {
                           boolean flag25 = false;
                           break;
                        }
                     }

                     try {
                        n2++;
                     } catch (Exception e22) {
                        boolean flag26 = false;
                        break;
                     }
                  }

                  try {
                     if (!flag3) {
                        break;
                     }
                  } catch (Exception e23) {
                     boolean flag27 = false;
                     break;
                  }
               }
            }

            OpenGlHelper.initializeTextures();
            ResizableFramebuffer resizableFramebuffer = new ResizableFramebuffer(this.displayWidth, this.displayHeight, true);
            this.framebufferMc = resizableFramebuffer;
            ResizableFramebuffer resizableFramebuffer2 = new ResizableFramebuffer(this.displayWidth, this.displayHeight, true);
            this.blurFramebuffer = resizableFramebuffer2;
            this.framebufferMc.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
            this.blurFramebuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
            this.registerMetadataSerializers();
            ResourcePackRepository resourcePackRepository = new ResourcePackRepository(
               this.fileResourcepacks, new File(this.mcDataDir, "server-resource-packs"), this.mcDefaultResourcePack, this.metadataSerializer_, this.gameSettings
            );
            this.mcResourcePackRepository = resourcePackRepository;
            SimpleReloadableResourceManager manager = new SimpleReloadableResourceManager(this.metadataSerializer_);
            this.mcResourceManager = manager;
            LanguageManager languageManager = new LanguageManager(this.metadataSerializer_, this.gameSettings.language);
            this.mcLanguageManager = languageManager;
            this.mcResourceManager.registerReloadListener(this.mcLanguageManager);
            this.refreshResources();
            TextureManager textureManager = new TextureManager(this.mcResourceManager);
            this.renderEngine = textureManager;
            this.mcResourceManager.registerReloadListener(this.renderEngine);
            TextureManager renderEngine2 = this.renderEngine;
            this.drawSplashScreen(renderEngine2);
            SkinManager skinManager2 = new SkinManager(this.renderEngine, new File(this.fileAssets, "skins"), this.sessionService);
            this.skinManager = skinManager2;
            AnvilSaveConverter anvilSaveConverter = new AnvilSaveConverter(new File(this.mcDataDir, "saves"));
            this.saveLoader = anvilSaveConverter;
            this.mcSoundHandler = new SoundHandler(this.mcResourceManager, this.gameSettings);
            this.mcResourceManager.registerReloadListener(this.mcSoundHandler);
            MusicTicker musicTicker = new MusicTicker(this);
            this.mcMusicTicker = musicTicker;
            FontRenderer fontRenderer = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii.png"), this.renderEngine, false);
            this.fontRendererObj = fontRenderer;
            mc = this;
            if (!flag3) {
               break exitBlock2;
            }

            if (this.gameSettings.language != null) {
               this.fontRendererObj.setUnicodeFlag(this.isUnicode());
               this.fontRendererObj.setBidiFlag(this.mcLanguageManager.isCurrentLanguageBidirectional());
            }

            FontRenderer fontRenderer2 = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii_sga.png"), this.renderEngine, false);
            this.standardGalacticFontRenderer = fontRenderer2;
            this.mcResourceManager.registerReloadListener(this.fontRendererObj);
            this.mcResourceManager.registerReloadListener(this.standardGalacticFontRenderer);
            this.mcResourceManager.registerReloadListener(new GrassColorReloadListener());
            this.mcResourceManager.registerReloadListener(new FoliageColorReloadListener());
            AchievementList.openInventory.setStatStringFormatter(new Minecraft.DefaultStatStringFormat(this));
            RawMouseHelper rawMouseHelper = new RawMouseHelper();
            this.mouseHelper = rawMouseHelper;
            this.checkGLError("Pre startup");
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7425);
            GlStateManager.clearDepth(1.0);
            GlStateManager.enableDepth();
            GlStateManager.depthFunc(515);
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.cullFace(1029);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            this.checkGLError("Startup");
            TextureMap textureMap = new TextureMap("textures");
            this.textureMapBlocks = textureMap;
            this.textureMapBlocks.setMipmapLevels(this.gameSettings.mipmapLevels);
            this.renderEngine.loadTickableTexture(TextureMap.locationBlocksTexture, this.textureMapBlocks);
         }

         this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
         mc = this;
      }

      int mipmapLevels = this.gameSettings.mipmapLevels;
      if (flag3) {
         mipmapLevels = this.gameSettings.mipmapLevels > 0 ? 1 : 0;
      }

      exitBlock17: {
         mc.textureMapBlocks.setBlurMipmapDirect(false, (mipmapLevels != 0));
         ModelManager modelManager2 = new ModelManager(this.textureMapBlocks);
         this.modelManager = modelManager2;
         this.mcResourceManager.registerReloadListener(this.modelManager);
         RenderItem renderItem2 = new RenderItem(this.renderEngine, this.modelManager);
         this.renderItem = renderItem2;
         RenderManager renderManager2 = new RenderManager(this.renderEngine, this.renderItem);
         this.renderManager = renderManager2;
         ItemRenderer itemRenderer2 = new ItemRenderer(this);
         this.itemRenderer = itemRenderer2;
         this.mcResourceManager.registerReloadListener(this.renderItem);
         EntityRenderer entityRenderer2 = new EntityRenderer(this, this.mcResourceManager);
         this.entityRenderer = entityRenderer2;
         this.mcResourceManager.registerReloadListener(this.entityRenderer);
         BlockRendererDispatcher blockRendererDispatcher = new BlockRendererDispatcher(this.modelManager.getBlockModelShapes(), this.gameSettings);
         this.blockRenderDispatcher = blockRendererDispatcher;
         this.mcResourceManager.registerReloadListener(this.blockRenderDispatcher);
         RenderGlobal renderGlobal2 = new RenderGlobal(this);
         this.renderGlobal = renderGlobal2;
         this.mcResourceManager.registerReloadListener(this.renderGlobal);
         GuiAchievement guiAchievement2 = new GuiAchievement(this);
         this.guiAchievement = guiAchievement2;
         GlStateManager.viewport(0, 0, this.displayWidth, this.displayHeight);
         EffectRenderer effectRenderer2 = new EffectRenderer(this.theWorld, this.renderEngine);
         this.effectRenderer = effectRenderer2;
         this.checkGLError("Post startup");
         GuiIngameHook guiIngameHook = new GuiIngameHook(this);
         this.ingameGUI = guiIngameHook;
         Augustus.getInstance().preStart();
         Augustus instance = Augustus.getInstance();
         instance.start();
         Runtime.getRuntime().addShutdownHook(new Thread(Augustus.getInstance()::getMouseWheelTracker));
         mc = this;
         if (flag3) {
            if (this.serverName != null) {
               GuiConnecting guiConnecting = new GuiConnecting(new GuiMainMenu(), this, this.serverName, this.serverPort);
               this.displayGuiScreen(guiConnecting);
               if (flag3) {
                  break exitBlock17;
               }
            }

            mc = this;
         }

         GuiMainMenu guiMainMenu = new GuiMainMenu();
         mc.displayGuiScreen(guiMainMenu);
      }

      exitBlock18: {
         exitBlock19: {
            this.renderEngine.deleteTexture(this.mojangLogo);
            Object obj = null;
            this.mojangLogo = (ResourceLocation)obj;
            LoadingScreenRenderer loadingScreenRenderer = new LoadingScreenRenderer(this);
            this.loadingScreen = loadingScreenRenderer;
            flag2 = this.gameSettings.fullScreen;
            exitBlock20:
            if (flag3) {
               if (this.gameSettings.fullScreen) {
                  flag2 = this.fullscreen;
                  if (!flag3) {
                     break exitBlock20;
                  }

                  if (!this.fullscreen) {
                     this.toggleFullscreen();
                  }
               }

               try {
                  flag2 = this.gameSettings.enableVsync;
               } catch (OpenGLException e24) {
                  boolean flag28 = false;
                  break exitBlock19;
               }
            }

            try {
               Display.setVSyncEnabled(flag2);
               break exitBlock18;
            } catch (OpenGLException e25) {
               boolean flag29 = false;
            }
         }

         this.gameSettings.enableVsync = false;
         this.gameSettings.saveOptions();
      }

      this.renderGlobal.makeEntityOutlineShader();
      BaritoneAPI.getProvider().getPrimaryBaritone();
   }

   public static boolean isAmbientOcclusionEnabled() {
      boolean isControlFlowDisabled = isControlFlowDisabled();
      Minecraft mc = theMinecraft;
      if (isControlFlowDisabled) {
         if (theMinecraft == null) {
            return false;
         }

         mc = theMinecraft;
      }

      if (!isControlFlowDisabled) {
         return mc.gameSettings.ambientOcclusion != 0;
      } else {
         return ((mc.gameSettings.ambientOcclusion != 0 ? 1 : 0) != 0);
      }
   }

   private void runGameLoop() throws IOException {
      boolean flag = false, flag2 = false, flag3 = false;
      int n = 0;
      boolean flag4;
      long l;
      exitBlock: {
         Minecraft mc;
         exitBlock2: {
            l = System.nanoTime();
            boolean isControlFlowDisabled = isControlFlowDisabled();
            this.mcProfiler.startSection("root");
            flag4 = isControlFlowDisabled;
            flag = Display.isCreated();
            exitBlock3:
            if (flag4) {
               if (flag) {
                  flag = Display.isCloseRequested();
                  if (!flag4) {
                     break exitBlock3;
                  }

                  if (flag) {
                     this.shutdown();
                  }
               }

               mc = this;
               if (!flag4) {
                  break exitBlock2;
               }

               flag = this.isGamePaused;
            }

            if (flag) {
               mc = this;
               if (!flag4) {
                  break exitBlock2;
               }

               if (this.theWorld != null) {
                  float renderPartialTicks = this.timer.renderPartialTicks;
                  this.timer.updateTimer();
                  this.timer.renderPartialTicks = renderPartialTicks;
                  float renderPartialTicks2 = this.realTimer.renderPartialTicks;
                  this.realTimer.updateTimer();
                  this.realTimer.renderPartialTicks = renderPartialTicks2;
                  if (flag4) {
                     break exitBlock;
                  }
               }
            }

            this.timer.updateTimer();
            mc = this;
         }

         mc.realTimer.updateTimer();
      }

      float f;
      exitBlock4: {
         f = this.timer.renderPartialTicks;
         MouseWheelTracker mouseWheelTracker = Augustus.getInstance().getMouseWheelTracker();
         if (flag4) {
            if (mouseWheelTracker == null) {
               break exitBlock4;
            }

            mouseWheelTracker = Augustus.getInstance().getMouseWheelTracker();
         }

         mouseWheelTracker.pollMouseWheel();
      }

      exitBlock5: {
         Minecraft mc2;
         exitBlock6: {
            exitBlock7: {
               int n2 = (ClientManagers.moduleManager.timeManipulation.isToggled()) ? 1 : 0;
               if (flag4) {
                  if (n2 == 0) {
                     break exitBlock7;
                  }

                  n2 = this.timer.elapsedTicks;
               }

               exitBlock8: {
                  exitBlock9: {
                     int n3 = n2;
                     int elapsedTicks = this.realTimer.elapsedTicks;
                     n = this.realTimer.elapsedTicks;
                     byte b = 1;
                     if (flag4) {
                        if (this.realTimer.elapsedTicks >= 1) {
                           long l2 = this.realElapsedTicks + (long)elapsedTicks;
                           this.projectedRealTicks = l2;
                           long l3 = this.gameElapsedTicks + (long)n3;
                           this.projectedGameTicks = l3;
                        }

                        n = this.realTimer.elapsedTicks;
                        if (!flag4) {
                           break exitBlock9;
                        }

                        b = 1;
                     }

                     if (n >= b) {
                        int n4 = (int)(this.projectedRealTicks - this.projectedGameTicks);
                        boolean flag5 = ClientManagers.moduleManager.timeManipulation.shouldChargeTick(n4);
                        this.skipGameTick = flag5;
                        n = (this.skipGameTick) ? 1 : 0;
                        if (!flag4) {
                           break exitBlock9;
                        }

                        if (this.skipGameTick) {
                           long l4 = this.projectedGameTicks - (long)n3;
                           this.projectedGameTicks = l4;
                        }
                     }

                     mc2 = this;
                     if (!flag4) {
                        break exitBlock8;
                     }

                     n = (this.skipGameTick) ? 1 : 0;
                  }

                  if (n != 0) {
                     this.timer.elapsedTicks = 0;
                     this.timer.renderPartialTicks = 0.0F;
                  }

                  mc2 = this;
               }

               long l5 = mc2.gameElapsedTicks + (long)this.timer.elapsedTicks;
               long l6 = this.realElapsedTicks + (long)this.realTimer.elapsedTicks;
               int n5 = (int)(l5 - l6);
               mc2 = this;
               if (!flag4) {
                  break exitBlock6;
               }

               exitBlock10:
               if (!this.skipGameTick) {
                  double d;
                  int n6 = (d = (double)n5 - (ClientManagers.moduleManager.timeManipulation.minTicksAvailable.getValue() - 1.0)) == 0.0 ? 0 : (d < 0.0 ? -1 : 1);
                  if (flag4) {
                     exitBlock11: {
                        if (n6 < 0) {
                           mc2 = this;
                           if (!flag4) {
                              break exitBlock11;
                           }

                           if (this.theWorld != null) {
                              exitBlock12: {
                                 EventTimer event = TimerListener.onTimer(0, this.realTimer.timerSpeed, TimerEventType.TIMER);
                                 this.timer.timerSpeed = event.getTimerSpeed();
                                 int elapsedTicks2 = this.timer.elapsedTicks;
                                 if (flag4) {
                                    if (this.timer.elapsedTicks < 1) {
                                       break exitBlock12;
                                    }

                                    elapsedTicks2 = 0;
                                 }

                                 TimerEventType tick = TimerEventType.TICK;
                                 float timerSpeed = this.realTimer.timerSpeed;
                                 int n7 = elapsedTicks2;
                                 event = TimerListener.onTimer(n7, timerSpeed, tick);
                                 this.timer.elapsedTicks = this.timer.elapsedTicks
                                    + (int)Math.min((double)event.getExtraTicks(), ClientManagers.moduleManager.timeManipulation.minTicksAvailable.getValue() - (double)n5);
                              }

                              if (flag4) {
                                 break exitBlock10;
                              }
                           }
                        }

                        mc2 = this;
                     }

                     mc2.timer.timerSpeed = this.realTimer.timerSpeed;
                     if (!flag4) {
                        break exitBlock5;
                     }

                     double d2;
                     n6 = (d2 = (double)n5 - ClientManagers.moduleManager.timeManipulation.minTicksAvailable.getValue()) == 0.0 ? 0 : (d2 < 0.0 ? -1 : 1);
                  }

                  if (n6 < 0) {
                     mc2 = this;
                     if (!flag4) {
                        break exitBlock6;
                     }

                     if (this.realTimer.elapsedTicks >= 1) {
                        int elapsedTicks3 = this.timer.elapsedTicks;
                        if (flag4) {
                           elapsedTicks3 = this.timer.elapsedTicks >= 1 ? 1 : 0;
                        }

                        int n8 = elapsedTicks3;
                        this.timer.copyFrom(this.realTimer);
                        if (!flag4) {
                           break exitBlock5;
                        }

                        if (n8 != 0) {
                           this.timer.elapsedTicks -= n5;
                        }
                     }
                  }
               }
            }

            this.realElapsedTicks = this.realElapsedTicks + (long)this.realTimer.elapsedTicks;
            this.gameElapsedTicks = this.gameElapsedTicks + (long)this.timer.elapsedTicks;
            this.realTotalTicks = (float)this.realElapsedTicks + this.realTimer.elapsedPartialTicks;
            this.gameTotalTicks = (float)this.gameElapsedTicks + this.timer.elapsedPartialTicks;
            mc2 = this;
         }

         mc2.mcProfiler.startSection("scheduledExecutables");
      }

      EventGameLoop event2;
      exitBlock13: {
         event2 = null;
         Minecraft mc3 = this;
         if (flag4) {
            if (this.thePlayer == null) {
               break exitBlock13;
            }

            mc3 = this;
         }

         if (flag4) {
            if (mc3.theWorld == null) {
               break exitBlock13;
            }

            mc3 = this;
         }

         event2 = GameLoopListener.onGameLoop(mc3.timer.elapsedTicks, this.timer.renderPartialTicks);
         this.timer.elapsedTicks = event2.getElapsedTicks();
         this.timer.renderPartialTicks = event2.getRenderPartialTicks();
      }

      Minecraft mc4;
      mc4 = this;
      exitBlock14:
      if (flag4) {
         if (this.timer.elapsedTicks >= 1 && Augustus.getInstance().getModuleManager() != null) {
            mc4 = this;
            if (!flag4) {
               break exitBlock14;
            }

            if (this.theWorld != null) {
               mc4 = this;
               if (!flag4) {
                  break exitBlock14;
               }

               exitBlock15:
               if (this.thePlayer != null) {
                  exitBlock16: {
                     boolean flag6 = ClientManagers.moduleManager.packetHandler.macroDelayModes.getSelectedValue().equals("OtherMacroDelay");
                     if (flag4) {
                        if (flag6) {
                           break exitBlock16;
                        }

                        flag6 = ClientManagers.moduleManager.packetHandler.macroDelayModes.getSelectedValue().equals("ExperimentalMacroDelay");
                     }

                     if (!flag6) {
                        break exitBlock15;
                     }
                  }

                  PacketFlushMarker packetFlushMarker = new PacketFlushMarker();
                  packetFlushMarker.tickBoundary = true;
                  this.getNetHandler().getNetworkManager().receivePacketAsync(packetFlushMarker);
                  boolean flag7 = false;
                  ClientManagers.moduleManager.packetHandler.setFlushingPackets(flag7);
               }
            }
         }

         mc4 = this;
      }

      synchronized (mc4.priorityScheduledTasks) {
         while (!this.priorityScheduledTasks.isEmpty()) {
            Util.runTask((FutureTask)this.priorityScheduledTasks.poll(), LOGGER);
            if (flag4 && flag4) {
               continue;
            }
            break;
         }
      }

      exitBlock17: {
         exitBlock18: {
            exitBlock19: {
               exitBlock20: {
                  flag2 = ClientManagers.moduleManager.packetHandler.macroDelayModes.getSelectedValue().equals("ExperimentalMacroDelay");
                  if (flag4) {
                     if (flag2) {
                        flag2 = ClientManagers.moduleManager.packetHandler.isFlushingPackets();
                        if (!flag4) {
                           break exitBlock19;
                        }

                        if (flag2) {
                           break exitBlock20;
                        }
                     }

                     flag2 = ClientManagers.moduleManager.packetHandler.eventToHandlePacket.getSelectedValue().equals("Vanilla");
                  }

                  if (!flag4) {
                     break exitBlock19;
                  }

                  exitBlock21:
                  if (flag2) {
                     exitBlock22: {
                        flag2 = ClientManagers.moduleManager.timeManipulation.isToggled();
                        if (flag4) {
                           if (!flag2) {
                              break exitBlock22;
                           }

                           flag2 = this.skipGameTick;
                        }

                        if (flag4) {
                           if (!flag2) {
                              break exitBlock22;
                           }

                           flag2 = ClientManagers.moduleManager.timeManipulation.updateEntityPositions.getValue();
                        }

                        if (flag4) {
                           if (flag2) {
                              break exitBlock22;
                           }

                           flag2 = ClientManagers.moduleManager.timeManipulation.correct.getValue();
                        }

                        if (!flag4) {
                           break exitBlock19;
                        }

                        if (flag2) {
                           break exitBlock21;
                        }
                     }

                     exitBlock23: {
                        EventGameLoop event3 = event2;
                        if (flag4) {
                           if (event2 == null) {
                              break exitBlock23;
                           }

                           event3 = event2;
                        }

                        flag2 = event3.isSkipRender();
                        if (flag4) {
                           if (!flag2) {
                              break exitBlock23;
                           }

                           flag2 = event2.isAllowSkipRender();
                        }

                        if (!flag4) {
                           break exitBlock19;
                        }

                        if (flag2) {
                           break exitBlock21;
                        }
                     }

                     this.runScheduledTasks();
                  }
               }

               mc4 = this;
               if (!flag4) {
                  break exitBlock18;
               }

               flag2 = this.skipGameTick;
            }

            exitBlock24:
            if (flag2) {
               byte b2 = (byte)((ClientManagers.moduleManager.timeManipulation.updateEntityPositions.getValue()) ? 1 : 0);
               if (flag4) {
                  if (b2 == 0) {
                     break exitBlock24;
                  }

                  b2 = 0;
               }

               int n9 = b2;

               while (n9 < this.realTimer.elapsedTicks) {
                  ClientManagers.moduleManager.timeManipulation.updateOtherEntities();
                  n9++;
                  if (!flag4) {
                     break exitBlock17;
                  }

                  if (!flag4) {
                     break;
                  }
               }
            }

            mc4 = this;
         }

         mc4.mcProfiler.endSection();
      }

      long l7 = System.nanoTime();
      this.mcProfiler.startSection("tick");
      int n10 = this.timer.elapsedTicks - 1;
      ClientManagers.moduleManager.rotationHandler.framesSinceLastTick++;
      mc4 = this;
      if (flag4) {
         if (this.timer.elapsedTicks > 0) {
            TickListener.onTick2(TickPhase.PRE);
            int n11 = 0;

            while (true) {
               if (n11 < this.timer.elapsedTicks) {
                  int n12 = n10 - n11;
                  this.runTick(n12, n11);
                  n11++;
                  if (!flag4) {
                     break;
                  }

                  if (flag4) {
                     continue;
                  }
               }

               int elapsedTicks4 = this.timer.elapsedTicks;
               if (flag4) {
                  elapsedTicks4 = this.timer.elapsedTicks > 1 ? 1 : 0;
               }

               int n13 = elapsedTicks4;
               this.ranMultipleTicks = (n13 != 0);
               TickListener.onTick2(TickPhase.POST);
               ClientManagers.moduleManager.rotationHandler.framesPerTick = ClientManagers.moduleManager.rotationHandler.framesSinceLastTick;
               ClientManagers.moduleManager.rotationHandler.framesSinceLastTick = 0;
               break;
            }

            GuiScreen currentScreen2 = this.currentScreen;
            this.previousScreen = currentScreen2;
         }

         mc4 = this;
      }

      long l8;
      exitBlock25: {
         mc4.mcProfiler.endStartSection("preRenderErrors");
         l8 = System.nanoTime() - l7;
         this.checkGLError("Pre render");
         this.mcProfiler.endStartSection("sound");
         this.mcSoundHandler.setListener(this.thePlayer, this.timer.renderPartialTicks);
         this.mcProfiler.endSection();
         this.mcProfiler.startSection("render");
         GlStateManager.pushMatrix();
         GlStateManager.clear(16640);
         this.framebufferMc.bindFramebuffer(true);
         this.mcProfiler.startSection("display");
         GlStateManager.enableTexture2D();
         mc4 = this;
         if (flag4) {
            if (this.thePlayer != null) {
               flag3 = this.thePlayer.isEntityInsideOpaqueBlock();
               if (!flag4) {
                  break exitBlock25;
               }

               if (flag3) {
                  this.gameSettings.thirdPersonView = 0;
               }
            }

            this.mcProfiler.endSection();
            mc4 = this;
         }

         flag3 = mc4.skipRenderWorld;
      }

      exitBlock26: {
         exitBlock27: {
            if (flag4) {
               if (!flag3) {
                  this.mcProfiler.endStartSection("gameRenderer");
                  this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks, l, this.lastRenderTime);
                  this.mcProfiler.endSection();
               }

               this.mcProfiler.endSection();
               mc4 = this;
               if (!flag4) {
                  break exitBlock27;
               }

               flag3 = this.gameSettings.showDebugInfo;
            }

            if (flag3) {
               mc4 = this;
               if (!flag4) {
                  break exitBlock27;
               }

               if (this.gameSettings.showDebugProfilerChart) {
                  mc4 = this;
                  if (!flag4) {
                     break exitBlock27;
                  }

                  if (!this.gameSettings.hideGUI) {
                     Profiler mcProfiler2 = this.mcProfiler;
                     if (flag4) {
                        if (!this.mcProfiler.profilingEnabled) {
                           this.mcProfiler.clearProfiling();
                        }

                        mcProfiler2 = this.mcProfiler;
                     }

                     mcProfiler2.profilingEnabled = true;
                     this.displayDebugInfo(l8);
                     if (flag4) {
                        break exitBlock26;
                     }
                  }
               }
            }

            this.mcProfiler.profilingEnabled = false;
            mc4 = this;
         }

         long l9 = System.nanoTime();
         mc4.prevFrameTime = l9;
      }

      exitBlock28: {
         boolean isToggled = ClientManagers.moduleManager.antiAchievement.isToggled();
         if (flag4) {
            if (!isToggled) {
               this.guiAchievement.updateAchievementWindow();
            }

            this.framebufferMc.unbindFramebuffer();
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            this.framebufferMc.framebufferRender(this.displayWidth, this.displayHeight);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.popMatrix();
            this.mcProfiler.startSection("root");
            if (!flag4) {
               break exitBlock28;
            }

            isToggled = GpuVendorDetector.isNvidia();
         }

         exitBlock29:
         if (isToggled) {
            MotionBlur motionBlur = ClientManagers.moduleManager.motionBlur;
            if (flag4) {
               if (!ClientManagers.moduleManager.motionBlur.isToggled()) {
                  break exitBlock29;
               }

               motionBlur = ClientManagers.moduleManager.motionBlur;
            }

            motionBlur.applyMotionBlur();
         }

         this.updateDisplay();
         Thread.yield();
         this.mcProfiler.startSection("stream");
         this.mcProfiler.startSection("update");
         this.mcProfiler.endStartSection("submit");
         this.mcProfiler.endSection();
         this.mcProfiler.endSection();
         this.checkGLError("Post render");
         int n14 = this.fpsCounter + 1;
         this.fpsCounter = n14;
      }

      boolean flag8;
      exitBlock30: {
         exitBlock31: {
            Minecraft mc5 = this;
            if (flag4) {
               if (!this.isSingleplayer()) {
                  break exitBlock31;
               }

               mc5 = this;
            }

            GuiScreen currentScreen3 = mc5.currentScreen;
            if (flag4) {
               if (mc5.currentScreen == null) {
                  break exitBlock31;
               }

               currentScreen3 = this.currentScreen;
            }

            flag8 = currentScreen3.doesGuiPauseGame();
            if (flag4) {
               if (!flag8) {
                  break exitBlock31;
               }

               flag8 = this.theIntegratedServer.getPublic();
            }

            if (!flag4) {
               break exitBlock30;
            }

            if (!flag8) {
               flag8 = true;
               break exitBlock30;
            }
         }

         flag8 = false;
      }

      boolean flag9 = flag8;
      this.isGamePaused = flag9;
      long l10 = System.nanoTime();
      this.frameTimer.addFrame(l10 - this.startNanoTime);
      this.startNanoTime = l10;

      while (true) {
         if (getSystemTime() >= this.debugUpdateTime + 1000L) {
            debugFPS = this.fpsCounter;
            mc4 = this;
            if (!flag4) {
               break;
            }

            Object[] array;
            Object obj;
            exitBlock32: {
               array = new Object[]{debugFPS, RenderChunk.renderChunksUpdated, RenderChunk.renderChunksUpdated != 1 ? "s" : "", null, null, null, null, null};
               float f2;
               int n15 = (f2 = (float)this.gameSettings.limitFramerate - GameSettings.Options.FRAMERATE_LIMIT.getValueMax()) == 0.0F
                  ? 0
                  : (f2 < 0.0F ? -1 : 1);
               if (flag4) {
                  if (n15 == 0) {
                     obj = "inf";
                     break exitBlock32;
                  }

                  n15 = this.gameSettings.limitFramerate;
               }

               obj = n15;
            }

            exitBlock33: {
               array[3] = obj;
               array[4] = this.gameSettings.enableVsync ? " vsync" : "";
               array[5] = this.gameSettings.fancyGraphics ? "" : " fast";
               int clouds = this.gameSettings.clouds;
               if (flag4) {
                  if (this.gameSettings.clouds == 0) {
                     obj = "";
                     break exitBlock33;
                  }

                  clouds = this.gameSettings.clouds;
               }

               obj = clouds == 1 ? " fast-clouds" : " fancy-clouds";
            }

            array[6] = obj;
            array[7] = OpenGlHelper.useVbo() ? " vbo" : "";
            String str = String.format("%d fps (%d chunk update%s) T: %s%s%s%s%s", array);
            this.debug = str;
            RenderChunk.renderChunksUpdated = 0;
            long l11 = this.debugUpdateTime + 1000L;
            this.debugUpdateTime = l11;
            byte b3 = 0;
            this.fpsCounter = b3;
            if (flag4) {
               continue;
            }
         }

         mc4 = this;
         break;
      }

      exitBlock34: {
         exitBlock35: {
            boolean flag10 = mc4.isFramerateLimitBelowMax();
            if (flag4) {
               if (flag10) {
                  this.mcProfiler.startSection("fpslimit_wait");
                  Display.sync(this.getLimitFramerate());
                  this.mcProfiler.endSection();
                  if (flag4) {
                     break exitBlock35;
                  }
               }

               mc4 = this;
               if (!flag4) {
                  break exitBlock34;
               }

               flag10 = this.gameSettings.enableVsync;
            }

            exitBlock36:
            if (flag10) {
               byte b4 = (byte)((Display.isVisible()) ? 1 : 0);
               if (flag4) {
                  if (b4 != 0) {
                     break exitBlock36;
                  }

                  b4 = 60;
               }

               Display.sync(b4);
            }
         }

         this.mcProfiler.endSection();
         mc4 = this;
      }

      mc4.timer.renderPartialTicks = f;
   }

   public ListenableFuture addScheduledTask(Callable callable) {
      Object obj;
      exitBlock: {
         boolean isControlFlowFlag = isControlFlowFlag();
         Validate.notNull(callable);
         obj = this;
         if (!isControlFlowFlag) {
            if (!this.isCallingFromMinecraftThread()) {
               ListenableFutureTask listenableFutureTask = ListenableFutureTask.create(callable);
               synchronized (this.scheduledTasks) {
                  this.scheduledTasks.add(listenableFutureTask);
                  return listenableFutureTask;
               }
            }

            try {
               obj = (Exception)callable.call();
            } catch (Exception e) {
               obj = e;
               break exitBlock;
            }
         }

         try {
            return Futures.immediateFuture(obj);
         } catch (Exception e2) {
            obj = e2;
            boolean flag = false;
         }
      }

      Exception e3 = (Exception)obj;
      return Futures.immediateFailedFuture(e3);
   }

   private void createDisplay() throws LWJGLException {
      Display.setResizable(true);
      boolean isControlFlowFlag = isControlFlowFlag();
      Display.setTitle(Augustus.getInstance().getName() + " " + Augustus.getInstance().getVersion());
      boolean flag = isControlFlowFlag;

      try {
         Display.create(new PixelFormat().withDepthBits(24));
      } catch (LWJGLException e) {
         LOGGER.error("Couldn't set pixel format", e);

         try {
            Thread.sleep(1000L);
         } catch (InterruptedException e2) {
         }

         exitBlock: {
            Minecraft mc = this;
            if (!flag) {
               if (!this.fullscreen) {
                  break exitBlock;
               }

               mc = this;
            }

            mc.updateDisplayMode();
         }

         Display.create();
      }
   }

   public ByteBuffer readImageToBuffer(InputStream in) throws IOException {
      BufferedImage image = ImageIO.read(in);
      int[] rgb = image.getRGB(0, 0, image.getWidth(), image.getHeight(), (int[])null, 0, image.getWidth());
      boolean isControlFlowDisabled = isControlFlowDisabled();
      ByteBuffer buf = ByteBuffer.allocate(4 * rgb.length);
      int[] ints = rgb;
      boolean flag = isControlFlowDisabled;
      int length = rgb.length;
      int n = 0;

      while (true) {
         if (n < length) {
            int n2 = ints[n];
            buf.putInt(n2 << 8 | n2 >> 24 & 0xFF);
            n++;
            if (!flag) {
               break;
            }

            if (flag) {
               continue;
            }
         }

         ((Buffer)buf).flip();
         break;
      }

      return buf;
   }

   public void freeMemory() {
      try {
         memoryReserve = new byte[0];
         this.renderGlobal.deleteAllDisplayLists();
      } catch (Throwable e) {
      }

      try {
         System.gc();
         WorldClient world = (WorldClient)null;
         this.loadWorld(world);
      } catch (Throwable e2) {
      }

      System.gc();
   }

   public ItemRenderer getItemRenderer() {
      return this.itemRenderer;
   }

   public Minecraft(GameConfiguration gameConfiguration) {
      super();
      boolean flag;
      Proxy proxy2;
      exitBlock: {
         this.audioPlayer = new JavaFxAudioPlayer();
         this.realElapsedTicks = 0L;
         this.gameElapsedTicks = 0L;
         long l = 0L;
         this.projectedRealTicks = l;
         long l2 = 0L;
         this.projectedGameTicks = l2;
         this.realTotalTicks = 0.0F;
         this.gameTotalTicks = 0.0F;
         boolean flag2 = true;
         this.enableGLErrorChecking = flag2;
         boolean isControlFlowFlag = isControlFlowFlag();
         boolean flag3 = false;
         this.connectedToRealms = flag3;
         this.timer = new Timer(20.0F);
         Timer timer2 = new Timer(20.0F);
         this.realTimer = timer2;
         byte b = 0;
         this.ticksLookingAtEntity = b;
         byte b2 = 0;
         this.ticksNotLookingAtEntity = b2;
         long systemTime2 = getSystemTime();
         this.systemTime = systemTime2;
         this.frameTimer = new FrameTimer();
         long l3 = System.nanoTime();
         this.startNanoTime = l3;
         this.mcProfiler = new Profiler();
         long l4 = -1L;
         this.debugCrashKeyPressTime = l4;
         this.metadataSerializer_ = new IMetadataSerializer();
         this.defaultResourcePacks = Lists.newArrayList();
         this.scheduledTasks = Queues.newArrayDeque();
         this.priorityScheduledTasks = Queues.newArrayDeque();
         this.mcThread = Thread.currentThread();
         boolean flag4 = true;
         this.running = flag4;
         String str = "";
         this.debug = str;
         boolean flag5 = true;
         this.renderChunksMany = flag5;
         boolean flag6 = false;
         this.wasLeftClicking = flag6;
         long systemTime3 = getSystemTime();
         this.debugUpdateTime = systemTime3;
         long l5 = -1L;
         this.prevFrameTime = l5;
         String str2 = "root";
         this.debugProfilerName = str2;
         byte b3 = 0;
         this.ticksWithoutScreen = b3;
         boolean flag7 = false;
         this.skipGameTick = flag7;
         long systemTime4 = getSystemTime();
         this.lastRenderTime = systemTime4;
         DoubleSampleWindow doubleSampleWindow = new DoubleSampleWindow(40);
         this.tickTimeWindow = doubleSampleWindow;
         DoubleSampleWindow doubleSampleWindow2 = new DoubleSampleWindow(40);
         this.frameTimeWindow = doubleSampleWindow2;
         boolean flag8 = false;
         this.ranMultipleTicks = flag8;
         flag = isControlFlowFlag;
         byte b4 = 0;
         this.remainingTicksInFrame = b4;
         this.tickHistory = new ArrayList();
         theMinecraft = this;
         this.mcDataDir = gameConfiguration.folderInfo.mcDataDir;
         this.fileAssets = gameConfiguration.folderInfo.assetsDir;
         this.fileResourcepacks = gameConfiguration.folderInfo.resourcePacksDir;
         this.version = gameConfiguration.gameInfo.version;
         this.twitchDetails = gameConfiguration.userInfo.profileProperties;
         this.mcDefaultResourcePack = new DefaultResourcePack(new ResourceIndex(gameConfiguration.folderInfo.assetsDir, gameConfiguration.folderInfo.assetIndex).getResourceMap());
         GameConfiguration.UserInformation userInfo = gameConfiguration.userInfo;
         if (!flag) {
            if (gameConfiguration.userInfo.proxy == null) {
               proxy2 = Proxy.NO_PROXY;
               break exitBlock;
            }

            userInfo = gameConfiguration.userInfo;
         }

         proxy2 = userInfo.proxy;
      }

      this.proxy = proxy2;
      MinecraftSessionService minecraftSessionService = new YggdrasilAuthenticationService(gameConfiguration.userInfo.proxy, UUID.randomUUID().toString()).createMinecraftSessionService();
      this.sessionService = minecraftSessionService;
      Session session2 = gameConfiguration.userInfo.session;
      this.session = session2;
      LOGGER.info("Setting user: " + this.session.getUsername());
      LOGGER.info("(Session ID is " + this.session.getSessionID() + ")");
      this.isDemo = gameConfiguration.gameInfo.isDemo;
      int width = gameConfiguration.displayInfo.width;
      if (!flag) {
         width = gameConfiguration.displayInfo.width > 0 ? gameConfiguration.displayInfo.width : 1;
      }

      this.displayWidth = width;
      int height = gameConfiguration.displayInfo.height;
      if (!flag) {
         height = gameConfiguration.displayInfo.height > 0 ? gameConfiguration.displayInfo.height : 1;
      }

      this.displayHeight = height;
      int width2 = gameConfiguration.displayInfo.width;
      this.tempDisplayWidth = width2;
      int height2 = gameConfiguration.displayInfo.height;
      this.tempDisplayHeight = height2;
      this.fullscreen = gameConfiguration.displayInfo.fullscreen;
      this.java64bit = isJvm64bit();
      IntegratedServer integratedServer = new IntegratedServer(this);
      this.theIntegratedServer = integratedServer;
      if (!flag) {
         if (gameConfiguration.serverInfo.serverName != null) {
            String serverName2 = gameConfiguration.serverInfo.serverName;
            this.serverName = serverName2;
            int serverPort2 = gameConfiguration.serverInfo.serverPort;
            this.serverPort = serverPort2;
         }

         ImageIO.setUseCache(false);
         Bootstrap.register();
      }

      if (flag) {
         Setting.setControlFlowMarker("oKZXzb");
      }
   }

   public ServerData getCurrentServerData() {
      return this.currentServerData;
   }

   private void resize(int n, int n2) {
      this.displayWidth = Math.max(1, n);
      boolean isControlFlowFlag = isControlFlowFlag();
      this.displayHeight = Math.max(1, n2);
      WindowResizeListener.onWindowResize(this.displayWidth, this.displayHeight);
      boolean flag = isControlFlowFlag;
      Minecraft mc = this;
      if (!flag) {
         if (this.currentScreen != null) {
            ScaledResolution sr = new ScaledResolution(this);
            this.currentScreen.onResize(this, sr.getScaledWidth(), sr.getScaledHeight());
         }

         LoadingScreenRenderer loadingScreenRenderer = new LoadingScreenRenderer(this);
         this.loadingScreen = loadingScreenRenderer;
         mc = this;
      }

      mc.updateFramebufferSize();
   }

   private void checkGLError(String str) {
      boolean isControlFlowDisabled = isControlFlowDisabled();
      int n = (this.enableGLErrorChecking) ? 1 : 0;
      if (isControlFlowDisabled) {
         if (!this.enableGLErrorChecking) {
            return;
         }

         n = GL11.glGetError();
      }

      int n2 = n;
      n = n2;
      if (isControlFlowDisabled) {
         if (n2 == 0) {
            return;
         }

         n = n2;
      }

      String str2 = GLU.gluErrorString(n);
      LOGGER.error("########## GL ERROR ##########");
      LOGGER.error("@ " + str);
      LOGGER.error(n2 + ": " + str2);
   }

   public void run() {
      this.running = true;

      try {
         this.startGame();
      } catch (Throwable e) {
         CrashReport crashReport = CrashReport.makeCrashReport(e, "Initializing game");
         crashReport.makeCategory("Initialization");
         this.displayCrashReport(this.addGraphicsAndWorldToCrashReport(crashReport));
         return;
      }

      try {
         while (this.running) {
            if (!this.hasCrashed || this.crashReporter == null) {
               try {
                  this.runGameLoop();
               } catch (OutOfMemoryError e2) {
                  this.freeMemory();
                  this.displayGuiScreen(new GuiMemoryErrorScreen());
                  System.gc();
               }
            } else {
               this.displayCrashReport(this.crashReporter);
            }
         }
      } catch (MinecraftError e3) {
      } catch (ReportedException e4) {
         this.addGraphicsAndWorldToCrashReport(e4.getCrashReport());
         this.freeMemory();
         LOGGER.fatal("Reported exception thrown!", e4);
         this.displayCrashReport(e4.getCrashReport());
      } catch (Throwable e5) {
         CrashReport crashReport2 = this.addGraphicsAndWorldToCrashReport(new CrashReport("Unexpected error", e5));
         this.freeMemory();
         LOGGER.fatal("Unreported exception thrown!", e5);
         this.displayCrashReport(crashReport2);
      } finally {
         this.shutdownMinecraftApplet();
      }
   }

   @Override
   public ListenableFuture addScheduledTaskDirect(Runnable runnable) {
      Validate.notNull(runnable);
      Callable callable = Executors.callable(runnable);
      return this.addPriorityScheduledTask(callable);
   }

   public boolean isJava64bit() {
      return this.java64bit;
   }

   public MusicTicker getMcMusicTicker() {
      return this.mcMusicTicker;
   }

   public void setRenderViewEntity(Entity entity) {
      this.renderViewEntity = entity;
      this.entityRenderer.loadEntityShader(entity);
   }

   public boolean isSnooperEnabled() {
      return this.gameSettings.snooperEnabled;
   }

   private String getConnectionState() {
      boolean isControlFlowFlag = isControlFlowFlag();
      Minecraft mc = this;
      if (!isControlFlowFlag) {
         if (this.theIntegratedServer != null) {
            return this.theIntegratedServer.getPublic() ? "hosting_lan" : "singleplayer";
         }

         mc = this;
      }

      ServerData currentServerData2 = mc.currentServerData;
      if (!isControlFlowFlag) {
         if (mc.currentServerData == null) {
            return "out_of_game";
         }

         currentServerData2 = this.currentServerData;
      }

      return currentServerData2.isLanServer() ? "playing_lan" : "multiplayer";
   }

   private void setInitialDisplayMode() throws LWJGLException {
      exitBlock: {
         boolean isControlFlowDisabled = isControlFlowDisabled();
         boolean fullscreen2 = this.fullscreen;
         if (isControlFlowDisabled) {
            if (!this.fullscreen) {
               break exitBlock;
            }

            fullscreen2 = true;
         }

         Display.setFullscreen(fullscreen2);
         DisplayMode displayMode = Display.getDisplayMode();
         this.displayWidth = Math.max(1, displayMode.getWidth());
         this.displayHeight = Math.max(1, displayMode.getHeight());
         if (isControlFlowDisabled) {
            return;
         }
      }

      Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
   }

   public static LanguageManager accessMcLanguageManager(Minecraft mc) {
      return mc.mcLanguageManager;
   }

   public RenderItem getRenderItem() {
      return this.renderItem;
   }

   public Timer getRealTimer() {
      return this.realTimer;
   }

   public static void stopIntegratedServer() {
      boolean isControlFlowFlag = isControlFlowFlag();
      Minecraft mc = theMinecraft;
      if (!isControlFlowFlag) {
         if (theMinecraft == null) {
            return;
         }

         mc = theMinecraft;
      }

      IntegratedServer integratedServer = mc.getIntegratedServer();
      IntegratedServer integratedServer2 = integratedServer;
      if (!isControlFlowFlag) {
         if (integratedServer == null) {
            return;
         }

         integratedServer2 = integratedServer;
      }

      integratedServer2.stopServer();
   }

   public FrameTimer getFrameTimer() {
      return this.frameTimer;
   }

   public void checkWindowResize() {
      Minecraft mc = null;
      boolean isControlFlowFlag = isControlFlowFlag();
      int n = (this.fullscreen) ? 1 : 0;
      if (!isControlFlowFlag) {
         if (this.fullscreen) {
            return;
         }

         n = (Display.wasResized()) ? 1 : 0;
      }

      if (!isControlFlowFlag) {
         if (n == 0) {
            return;
         }

         n = this.displayWidth;
      }

      int n2 = n;
      int displayHeight2 = this.displayHeight;
      this.displayWidth = Display.getWidth();
      this.displayHeight = Display.getHeight();
      n = this.displayWidth;
      exitBlock:
      if (!isControlFlowFlag) {
         if (this.displayWidth == n2) {
            n = this.displayHeight;
            if (isControlFlowFlag) {
               break exitBlock;
            }

            if (this.displayHeight == displayHeight2) {
               return;
            }
         }

         n = this.displayWidth;
      }

      exitBlock2: {
         if (!isControlFlowFlag) {
            if (n <= 0) {
               this.displayWidth = 1;
            }

            mc = this;
            if (isControlFlowFlag) {
               break exitBlock2;
            }

            n = this.displayHeight;
         }

         if (n <= 0) {
            this.displayHeight = 1;
         }

         mc = this;
      }

      int displayHeight3 = this.displayHeight;
      int displayWidth2 = this.displayWidth;
      mc.resize(displayWidth2, displayHeight3);
   }

   public static int getDebugFPS() {
      return debugFPS;
   }

   public Entity getRenderViewEntity() {
      return this.renderViewEntity;
   }

   public void setDimensionAndSpawnPlayer(int n) {
      boolean flag;
      int n2;
      String str;
      exitBlock: {
         this.theWorld.setInitialSpawnLocation();
         this.theWorld.removeAllEntities();
         n2 = 0;
         boolean isControlFlowDisabled = isControlFlowDisabled();
         str = null;
         flag = isControlFlowDisabled;
         EntityPlayerSP thePlayer2 = this.thePlayer;
         if (flag) {
            if (this.thePlayer == null) {
               break exitBlock;
            }

            n2 = this.thePlayer.getEntityId();
            this.theWorld.removeEntity(this.thePlayer);
            thePlayer2 = this.thePlayer;
         }

         str = thePlayer2.getClientBrand();
      }

      EntityPlayerSP player;
      StatFileWriter statFileWriter;
      PlayerControllerMP playerControllerMP;
      WorldClient world;
      exitBlock2: {
         Object obj = null;
         this.renderViewEntity = (Entity)obj;
         player = this.thePlayer;
         playerControllerMP = this.playerController;
         world = this.theWorld;
         EntityPlayerSP thePlayer3 = this.thePlayer;
         if (flag) {
            if (this.thePlayer == null) {
               statFileWriter = new StatFileWriter();
               break exitBlock2;
            }

            thePlayer3 = this.thePlayer;
         }

         statFileWriter = thePlayer3.getStatFileWriter();
      }

      EntityPlayerSP player2 = playerControllerMP.createClientPlayer(world, statFileWriter);
      this.thePlayer = player2;
      this.thePlayer.getDataWatcher().updateWatchedObjectsFromList(player.getDataWatcher().getAllWatched());
      this.thePlayer.dimension = n;
      EntityPlayerSP thePlayer4 = this.thePlayer;
      this.renderViewEntity = thePlayer4;
      this.thePlayer.preparePlayerToSpawn();
      this.thePlayer.setClientBrand(str);
      this.theWorld.spawnEntityInWorld(this.thePlayer);
      this.playerController.flipPlayer(this.thePlayer);
      this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
      this.thePlayer.setEntityId(n2);
      this.playerController.setPlayerCapabilities(this.thePlayer);
      this.thePlayer.setReducedDebug(player.hasReducedDebug());
      Minecraft mc = this;
      if (flag) {
         if (!(this.currentScreen instanceof GuiGameOver)) {
            return;
         }

         mc = this;
      }

      GuiScreen screen = (GuiScreen)null;
      mc.displayGuiScreen(screen);
   }

   public boolean isNotInGui() {
      boolean flag;
      Minecraft mc;
      exitBlock: {
         flag = isControlFlowFlag();
         GuiScreen currentScreen2 = this.currentScreen;
         if (!flag) {
            if (this.currentScreen != null) {
               return false;
            }

            mc = this;
            if (flag) {
               break exitBlock;
            }

            currentScreen2 = this.previousScreen;
         }

         if (currentScreen2 != null) {
            return false;
         }

         mc = this;
      }

      EntityPlayerSP thePlayer2 = mc.thePlayer;
      if (!flag) {
         if (mc.thePlayer == null) {
            return false;
         }

         thePlayer2 = this.thePlayer;
      }

      return thePlayer2.getSpoofedScreen() == null;
   }

   public ListenableFuture scheduleResourcesRefresh() {
      Runnable runnable = this::refreshResources;
      return this.addScheduledTask(runnable);
   }

   public void loadWorld(WorldClient world, String str) {
      Minecraft mc = null;
      boolean flag;
      WorldClient world2;
      exitBlock: {
         flag = isControlFlowDisabled();
         exitBlock2:
         if (Augustus.getInstance().getModuleManager().baritone.isToggled()) {
            exitBlock3: {
               world2 = this.theWorld;
               if (flag) {
                  if (this.theWorld != null) {
                     break exitBlock3;
                  }

                  world2 = world;
               }

               if (!flag) {
                  break exitBlock;
               }

               if (world2 == null) {
                  break exitBlock2;
               }
            }

            BaritoneAPI.getProvider().getPrimaryBaritone().getGameEventHandler().onWorldEvent(new WorldEvent(world, EventState.PRE));
         }

         world2 = world;
      }

      exitBlock4: {
         if (flag) {
            if (world2 != this.theWorld) {
               this.entityRenderer.getMapItemRenderer().clearLoadedMaps();
            }

            LoadWorldListener.onLoadWorld2(world);
            if (!flag) {
               break exitBlock4;
            }

            world2 = world;
         }

         if (world2 == null) {
            exitBlock5: {
               NetHandlerPlayClient netHandler = this.getNetHandler();
               NetHandlerPlayClient netHandlerPlayClient = netHandler;
               if (flag) {
                  if (netHandler == null) {
                     break exitBlock5;
                  }

                  netHandlerPlayClient = netHandler;
               }

               netHandlerPlayClient.cleanup();
            }

            mc = this;
            exitBlock6:
            if (flag) {
               if (this.theIntegratedServer != null) {
                  mc = this;
                  if (!flag) {
                     break exitBlock6;
                  }

                  if (this.theIntegratedServer.isAnvilFileSet()) {
                     this.theIntegratedServer.initiateShutdown();
                     this.theIntegratedServer.setStaticInstance();
                  }
               }

               Object obj = null;
               this.theIntegratedServer = (IntegratedServer)obj;
               this.guiAchievement.clearAchievements();
               mc = this;
            }

            mc.entityRenderer.getMapItemRenderer().clearLoadedMaps();
         }

         Object obj2 = null;
         this.renderViewEntity = (Entity)obj2;
         Object obj3 = null;
         this.myNetworkManager = (NetworkManager)obj3;
      }

      exitBlock7: {
         LoadingScreenRenderer loadingScreen2 = this.loadingScreen;
         if (flag) {
            if (this.loadingScreen == null) {
               break exitBlock7;
            }

            this.loadingScreen.resetProgressAndMessage(str);
            loadingScreen2 = this.loadingScreen;
         }

         loadingScreen2.displayLoadingString("");
      }

      exitBlock8: {
         exitBlock9: {
            world2 = world;
            exitBlock10:
            if (flag) {
               if (world == null) {
                  world2 = this.theWorld;
                  if (!flag) {
                     break exitBlock10;
                  }

                  if (this.theWorld != null) {
                     this.mcResourcePackRepository.clearResourcePack();
                     this.ingameGUI.resetPlayersOverlayFooterHeader();
                     ServerData serverData = (ServerData)null;
                     this.setServerData(serverData);
                     boolean flag2 = false;
                     this.integratedServerIsRunning = flag2;
                  }
               }

               this.mcSoundHandler.stopSounds();
               this.theWorld = world;
               if (!flag) {
                  break exitBlock9;
               }

               world2 = world;
            }

            if (world2 != null) {
               Minecraft mc2 = this;
               if (flag) {
                  if (this.renderGlobal != null) {
                     this.renderGlobal.setWorldAndLoadRenderers(world);
                  }

                  mc2 = this;
               }

               if (flag) {
                  if (mc2.effectRenderer != null) {
                     this.effectRenderer.clearEffects(world);
                  }

                  mc2 = this;
               }

               EntityPlayerSP thePlayer2 = mc2.thePlayer;
               if (flag) {
                  if (mc2.thePlayer == null) {
                     EntityPlayerSP player = this.playerController.createClientPlayer(world, new StatFileWriter());
                     this.thePlayer = player;
                     this.playerController.flipPlayer(this.thePlayer);
                  }

                  this.thePlayer.preparePlayerToSpawn();
                  world.spawnEntityInWorld(this.thePlayer);
                  thePlayer2 = this.thePlayer;
               }

               thePlayer2.movementInput = new MovementInputFromOptions(this.gameSettings);
               this.playerController.setPlayerCapabilities(this.thePlayer);
               EntityPlayerSP thePlayer3 = this.thePlayer;
               this.renderViewEntity = thePlayer3;

               try {
                  WorldLoadWarmup.warmup();
                  break exitBlock8;
               } catch (Throwable e) {
                  e.printStackTrace();
                  if (flag) {
                     break exitBlock8;
                  }
               }
            }

            this.saveLoader.flushCache();
         }

         Object obj4 = null;
         this.thePlayer = (EntityPlayerSP)obj4;
      }

      long l = 0L;
      this.systemTime = l;
      if (Augustus.getInstance().getModuleManager().baritone.isToggled()) {
         BaritoneAPI.getProvider().getPrimaryBaritone().getGameEventHandler().onWorldEvent(new WorldEvent(world, EventState.POST));
      }
   }

   @Override
   public boolean isCallingFromMinecraftThread() {
      return Thread.currentThread() == this.mcThread;
   }

   private void registerMetadataSerializers() {
      this.metadataSerializer_.registerMetadataSectionType(new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
      this.metadataSerializer_.registerMetadataSectionType(new FontMetadataSectionSerializer(), FontMetadataSection.class);
      this.metadataSerializer_.registerMetadataSectionType(new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
      this.metadataSerializer_.registerMetadataSectionType(new PackMetadataSectionSerializer(), PackMetadataSection.class);
      this.metadataSerializer_.registerMetadataSectionType(new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
   }

   private static boolean isJvm64bit() {
      String[] strings = new String[]{"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"};
      String[] strings2 = strings;
      boolean isControlFlowDisabled = isControlFlowDisabled();
      int length = strings.length;
      int n = 0;
      boolean flag = isControlFlowDisabled;

      while (true) {
         if (n < length) {
            String str = strings2[n];
            String property = System.getProperty(str);
            if (flag) {
               if (property != null) {
                  isControlFlowDisabled = property.contains("64");
                  if (!flag) {
                     break;
                  }

                  if (isControlFlowDisabled) {
                     isControlFlowDisabled = true;
                     break;
                  }
               }

               n++;
            }

            if (flag) {
               continue;
            }
         }

         return false;
      }

      return isControlFlowDisabled;
   }

   public boolean isFramerateLimitBelowMax() {
      boolean isControlFlowDisabled = isControlFlowDisabled();
      float f;
      int n = (f = (float)this.getLimitFramerate() - GameSettings.Options.FRAMERATE_LIMIT.getValueMax()) == 0.0F ? 0 : (f < 0.0F ? -1 : 1);
      if (isControlFlowDisabled) {
         n = n < 0 ? 1 : 0;
      }

      return (n != 0);
   }

   public MusicTicker.MusicType getAmbientMusicType() {
      boolean isControlFlowFlag = isControlFlowFlag();
      EntityPlayerSP thePlayer2 = this.thePlayer;
      if (!isControlFlowFlag) {
         if (this.thePlayer == null) {
            return MusicTicker.MusicType.MENU;
         }

         thePlayer2 = this.thePlayer;
      }

      boolean flag = thePlayer2.entityWorld.provider instanceof WorldProviderHell;
      if (!isControlFlowFlag) {
         if (flag) {
            return MusicTicker.MusicType.NETHER;
         }

         flag = this.thePlayer.entityWorld.provider instanceof WorldProviderEnd;
      }

      if (!isControlFlowFlag) {
         if (flag) {
            return BossStatus.bossName != null && BossStatus.statusBarTime > 0 ? MusicTicker.MusicType.END_BOSS : MusicTicker.MusicType.END;
         }

         flag = this.thePlayer.capabilities.isCreativeMode;
      }

      if (!isControlFlowFlag) {
         if (!flag) {
            return MusicTicker.MusicType.GAME;
         }

         flag = this.thePlayer.capabilities.allowFlying;
      }

      return flag ? MusicTicker.MusicType.CREATIVE : MusicTicker.MusicType.GAME;
   }

   public SkinManager getSkinManager() {
      return this.skinManager;
   }

   public SoundHandler getSoundHandler() {
      return this.mcSoundHandler;
   }

   public static boolean isGuiEnabled() {
      boolean isControlFlowDisabled = isControlFlowDisabled();
      Minecraft mc = theMinecraft;
      if (isControlFlowDisabled) {
         if (theMinecraft == null) {
            return true;
         }

         mc = theMinecraft;
      }

      return !isControlFlowDisabled ? mc.gameSettings.hideGUI : !mc.gameSettings.hideGUI;
   }

   public int getTicksWithoutScreen() {
      return this.ticksWithoutScreen;
   }

   public void sendClickBlockToController(boolean flag) {
      Minecraft mc = null;
      boolean isControlFlowDisabled = isControlFlowDisabled();
      int n = (flag) ? 1 : 0;
      if (isControlFlowDisabled) {
         if (!flag) {
            byte b = 0;
            this.leftClickCounter = b;
         }

         n = this.leftClickCounter;
      }

      exitBlock:
      if (isControlFlowDisabled) {
         if (n <= 0) {
            n = (this.thePlayer.isUsingItem()) ? 1 : 0;
            if (!isControlFlowDisabled) {
               break exitBlock;
            }

            exitBlock2:
            if (n == 0) {
               exitBlock3: {
                  if (flag) {
                     mc = this;
                     if (!isControlFlowDisabled) {
                        break exitBlock3;
                     }

                     if (this.objectMouseOver != null) {
                        mc = this;
                        if (!isControlFlowDisabled) {
                           break exitBlock3;
                        }

                        if (this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                           exitBlock4: {
                              BlockPos blockPos = this.objectMouseOver.getBlockPos();
                              Minecraft mc2 = this;
                              if (isControlFlowDisabled) {
                                 if (this.theWorld.getBlockState(blockPos).getBlock().getMaterial() == Material.air) {
                                    break exitBlock4;
                                 }

                                 mc2 = this;
                              }

                              boolean flag2 = mc2.playerController.onPlayerDamageBlock(blockPos, this.objectMouseOver.sideHit);
                              if (isControlFlowDisabled) {
                                 if (!flag2) {
                                    break exitBlock4;
                                 }

                                 flag2 = ClientManagers.moduleManager.noBlockParticles.isToggled();
                              }

                              if (!flag2) {
                                 this.effectRenderer.addBlockHitEffects(blockPos, this.objectMouseOver.sideHit);
                              }

                              this.thePlayer.swingItem();
                           }

                           if (isControlFlowDisabled) {
                              break exitBlock2;
                           }
                        }
                     }
                  }

                  mc = this;
               }

               mc.playerController.resetBlockRemoving();
            }
         }

         n = (ClientManagers.moduleManager.fixes.isToggled()) ? 1 : 0;
      }

      if (isControlFlowDisabled) {
         if (n == 0) {
            return;
         }

         n = (ClientManagers.moduleManager.fixes.hitDelayFixMode.getSelectedValue().equals("WithoutDoubleClick")) ? 1 : 0;
      }

      if (n != 0) {
         byte b2 = 0;
         this.leftClickCounter = b2;
      }
   }

   public void displayGuiScreen(GuiScreen screen) {
      int n = 0;
      boolean isControlFlowDisabled = isControlFlowDisabled();
      Object currentScreen2 = this.currentScreen;
      if (isControlFlowDisabled) {
         if (this.currentScreen != null) {
            this.currentScreen.onGuiClosed();
         }

         currentScreen2 = screen;
      }

      exitBlock: {
         exitBlock2: {
            exitBlock3: {
               exitBlock4: {
                  exitBlock5: {
                     exitBlock6: {
                        if (isControlFlowDisabled) {
                           if (currentScreen2 == null && this.theWorld == null) {
                              screen = new GuiMainMenu();
                              if (isControlFlowDisabled) {
                                 break exitBlock6;
                              }
                           }

                           currentScreen2 = screen;
                        }

                        if (!isControlFlowDisabled) {
                           break exitBlock5;
                        }

                        if (currentScreen2 == null) {
                           float f;
                           n = (f = this.thePlayer.getCurrentHealth() - 0.0F) == 0.0F ? 0 : (f < 0.0F ? -1 : 1);
                           if (!isControlFlowDisabled) {
                              break exitBlock4;
                           }

                           if (n <= 0) {
                              screen = new GuiGameOver();
                           }
                        }
                     }

                     currentScreen2 = screen;
                  }

                  if (!isControlFlowDisabled) {
                     break exitBlock3;
                  }

                  n = (currentScreen2 instanceof GuiMainMenu) ? 1 : 0;
               }

               if (n != 0) {
                  this.gameSettings.showDebugInfo = false;
                  this.ingameGUI.getChatGUI().clearChatMessages();
               }

               this.currentScreen = (GuiScreen)screen;
               this.lastDisplayedScreen = (GuiScreen)screen;
               if (!isControlFlowDisabled) {
                  break exitBlock2;
               }

               currentScreen2 = screen;
            }

            if (currentScreen2 != null) {
               this.setIngameNotInFocus();
               ScaledResolution sr = new ScaledResolution(this);
               int scaledWidth = sr.getScaledWidth();
               int scaledHeight = sr.getScaledHeight();
               ((GuiScreen)screen).setWorldAndResolution(this, scaledWidth, scaledHeight);
               boolean flag = false;
               this.skipRenderWorld = flag;
               if (isControlFlowDisabled) {
                  break exitBlock;
               }
            }

            this.mcSoundHandler.resumeSounds();
         }

         this.setIngameFocus();
      }

      currentScreen2 = this.thePlayer;
      if (isControlFlowDisabled) {
         if (this.thePlayer == null) {
            return;
         }

         currentScreen2 = this.thePlayer;
      }

      ((EntityPlayerSP)currentScreen2).setSpoofedScreen((GuiScreen)screen);
   }

   @Override
   public ListenableFuture addScheduledTask(Runnable runnable) {
      Validate.notNull(runnable);
      Callable callable = Executors.callable(runnable);
      return this.addScheduledTask(callable);
   }

   public Framebuffer getFramebuffer() {
      return BlurShadowManager.isHudCompositionActive() ? this.blurFramebuffer : this.framebufferMc;
   }

   public NetHandlerPlayClient getNetHandler() {
      return this.thePlayer != null ? this.thePlayer.sendQueue : null;
   }

   public void sendClickBlockToControllerWithSlot(boolean flag, MovingObjectPosition hit, BlockPos pos, int n) {
      EntityPlayerSP player = null;
      Minecraft mc;
      exitBlock: {
         boolean flag2 = false;
         this.hittingBlockCustom = flag2;
         boolean isControlFlowDisabled = isControlFlowDisabled();
         int currentItem = this.thePlayer.inventory.currentItem;
         boolean flag3 = isControlFlowDisabled;
         this.thePlayer.inventory.currentItem = n;
         int n2 = (flag) ? 1 : 0;
         if (flag3) {
            if (!flag) {
               byte b = 0;
               this.leftClickCounter = b;
            }

            mc = this;
            if (!flag3) {
               break exitBlock;
            }

            n2 = this.leftClickCounter;
         }

         exitBlock2: {
            if (n2 <= 0) {
               player = this.thePlayer;
               if (!flag3) {
                  break exitBlock2;
               }

               exitBlock3:
               if (!this.thePlayer.isUsingItem()) {
                  exitBlock4:
                  if (flag) {
                     MovingObjectPosition hit2 = hit;
                     if (flag3) {
                        if (hit == null) {
                           break exitBlock4;
                        }

                        hit2 = hit;
                     }

                     if (hit2.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                        mc = this;
                        if (!flag3) {
                           break exitBlock;
                        }

                        if (this.theWorld.getBlockState(pos).getBlock().getMaterial() == Material.air) {
                           break exitBlock3;
                        }

                        mc = this;
                        if (!flag3) {
                           break exitBlock;
                        }

                        if (!this.playerController.onPlayerDamageBlockWithSlot(pos, hit.sideHit, n)) {
                           break exitBlock3;
                        }

                        this.effectRenderer.addBlockHitEffects(pos, hit.sideHit);
                        this.thePlayer.swingItem();
                        boolean flag4 = true;
                        this.hittingBlockCustom = flag4;
                        if (flag3) {
                           break exitBlock3;
                        }
                     }
                  }

                  boolean flag5;
                  exitBlock5: {
                     exitBlock6: {
                        flag5 = this.hittingBlockCustom;
                        if (flag3) {
                           if (this.hittingBlockCustom) {
                              break exitBlock6;
                           }

                           flag5 = this.prevHittingBlockCustom;
                        }

                        if (!flag3) {
                           break exitBlock5;
                        }

                        if (flag5) {
                           flag5 = true;
                           break exitBlock5;
                        }
                     }

                     flag5 = false;
                  }

                  this.playerController.abortBlockBreaking(flag5);
                  this.thePlayer.resetTicksSinceLastSwing();
               }
            }

            player = this.thePlayer;
         }

         player.inventory.currentItem = currentItem;
         mc = this;
      }

      boolean prevHittingBlockCustom2 = this.prevHittingBlockCustom;
      mc.hittingBlockCustom = prevHittingBlockCustom2;
   }

   public void displayInGameMenu() {
      boolean isControlFlowFlag = isControlFlowFlag();
      Minecraft mc = this;
      if (!isControlFlowFlag) {
         if (this.currentScreen != null) {
            return;
         }

         GuiIngameMenu guiIngameMenu = new GuiIngameMenu();
         this.displayGuiScreen(guiIngameMenu);
         mc = this;
      }

      exitBlock: {
         boolean isSingleplayer = mc.isSingleplayer();
         if (!isControlFlowFlag) {
            if (!isSingleplayer) {
               return;
            }

            mc = this;
            if (isControlFlowFlag) {
               break exitBlock;
            }

            isSingleplayer = this.theIntegratedServer.getPublic();
         }

         if (isSingleplayer) {
            return;
         }

         mc = this;
      }

      mc.mcSoundHandler.pauseSounds();
   }

   public static boolean isFancyGraphicsEnabled() {
      boolean isControlFlowDisabled = isControlFlowDisabled();
      Minecraft mc = theMinecraft;
      if (isControlFlowDisabled) {
         if (theMinecraft == null) {
            return false;
         }

         mc = theMinecraft;
      }

      return !isControlFlowDisabled ? mc.gameSettings.fancyGraphics : mc.gameSettings.fancyGraphics;
   }

   public void launchIntegratedServer(String str, String str2, WorldSettings worldSettings) {
      boolean flag;
      WorldInfo worldInfo;
      WorldSettings worldSettings2;
      exitBlock: {
         WorldClient world = (WorldClient)null;
         this.loadWorld(world);
         System.gc();
         ISaveHandler saveLoader2 = this.saveLoader.getSaveLoader(str, false);
         boolean isControlFlowFlag = isControlFlowFlag();
         worldInfo = saveLoader2.loadWorldInfo();
         flag = isControlFlowFlag;
         if (worldInfo == null) {
            worldSettings2 = worldSettings;
            if (flag) {
               break exitBlock;
            }

            if (worldSettings != null) {
               worldInfo = new WorldInfo(worldSettings, str);
               saveLoader2.saveWorldInfo(worldInfo);
            }
         }

         worldSettings2 = worldSettings;
      }

      exitBlock2: {
         if (!flag) {
            if (worldSettings2 != null) {
               break exitBlock2;
            }

            worldSettings2 = new WorldSettings(worldInfo);
         }

         worldSettings = worldSettings2;
      }

      try {
         IntegratedServer integratedServer = new IntegratedServer(this, str, str2, worldSettings);
         this.theIntegratedServer = integratedServer;
         this.theIntegratedServer.startServerThread();
         boolean flag2 = true;
         this.integratedServerIsRunning = flag2;
      } catch (Throwable e) {
         CrashReport crashReport = CrashReport.makeCrashReport(e, "Starting integrated server");
         CrashReportCategory crashReportCategory = crashReport.makeCategory("Starting integrated server");
         crashReportCategory.addCrashSection("Level ID", str);
         crashReportCategory.addCrashSection("Level Name", str2);
         throw new ReportedException(crashReport);
      }

      this.loadingScreen.displaySavingString(I18n.format("menu.loadingLevel", new Object[0]));

      while (true) {
         if (!this.theIntegratedServer.serverIsInRunLoop()) {
            String userMessage = this.theIntegratedServer.getUserMessage();
            if (flag) {
               break;
            }

            exitBlock3: {
               exitBlock4: {
                  if (!flag) {
                     if (userMessage == null) {
                        break exitBlock4;
                     }

                     this.loadingScreen.displayLoadingString(I18n.format(userMessage, new Object[0]));
                  }

                  if (!flag) {
                     break exitBlock3;
                  }
               }

               this.loadingScreen.displayLoadingString("");
            }

            try {
               Thread.sleep(200L);
            } catch (InterruptedException e2) {
            }

            if (!flag) {
               continue;
            }
         }

         GuiScreen screen = (GuiScreen)null;
         this.displayGuiScreen(screen);
         break;
      }

      SocketAddress socketAddress = this.theIntegratedServer.getNetworkSystem().addLocalEndpoint();
      NetworkManager networkManager = NetworkManager.provideLocalClient(socketAddress);
      networkManager.setNetHandler(new NetHandlerLoginClient(networkManager, this, (GuiScreen)null));
      networkManager.sendPacket(new C00Handshake(47, socketAddress.toString(), 0, EnumConnectionState.LOGIN));
      networkManager.sendPacket(new C00PacketLoginStart(this.getSession().getProfile()));
      this.myNetworkManager = networkManager;
      DiscordRpcManager discordRpcManager = Augustus.getInstance().getDiscordRpcManager();
      String str3 = "Ingame";
      String str4 = "Playing lonely in singleplayer";
      discordRpcManager.updatePresence(str4, str3);
   }

   public ListenableFuture addPriorityScheduledTask(Callable callable) {
      Object obj;
      exitBlock: {
         boolean isControlFlowFlag = isControlFlowFlag();
         Validate.notNull(callable);
         obj = this;
         if (!isControlFlowFlag) {
            if (!this.isCallingFromMinecraftThread()) {
               ListenableFutureTask listenableFutureTask = ListenableFutureTask.create(callable);
               synchronized (this.priorityScheduledTasks) {
                  this.priorityScheduledTasks.add(listenableFutureTask);
                  return listenableFutureTask;
               }
            }

            try {
               obj = (Exception)callable.call();
            } catch (Exception e) {
               obj = e;
               break exitBlock;
            }
         }

         try {
            return Futures.immediateFuture(obj);
         } catch (Exception e2) {
            obj = e2;
            boolean flag = false;
         }
      }

      Exception e3 = (Exception)obj;
      return Futures.immediateFailedFuture(e3);
   }

   public boolean isGamePaused() {
      return this.isGamePaused;
   }

   public void draw(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10) {
      float f = 0.00390625F;
      float f2 = 0.00390625F;
      isControlFlowFlag();
      WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      worldRenderer.pos((double)n, (double)(n2 + n6), 0.0)
         .tex((double)((float)n3 * f), (double)((float)(n4 + n6) * f2))
         .color(n7, n8, n9, n10)
         .endVertex();
      worldRenderer.pos((double)(n + n5), (double)(n2 + n6), 0.0)
         .tex((double)((float)(n3 + n5) * f), (double)((float)(n4 + n6) * f2))
         .color(n7, n8, n9, n10)
         .endVertex();
      worldRenderer.pos((double)(n + n5), (double)n2, 0.0)
         .tex((double)((float)(n3 + n5) * f), (double)((float)n4 * f2))
         .color(n7, n8, n9, n10)
         .endVertex();
      worldRenderer.pos((double)n, (double)n2, 0.0).tex((double)((float)n3 * f), (double)((float)n4 * f2)).color(n7, n8, n9, n10).endVertex();
      Tessellator.getInstance().draw();
   }

   public MinecraftSessionService getSessionService() {
      return this.sessionService;
   }

   public void displayCrashReport(CrashReport crashReport) {
      boolean isControlFlowDisabled = isControlFlowDisabled();

      File file = new File(new File(OSUtil.detectOS().getMinecraftDirectory()), "crash-reports");
      ClientFileUtil.ensureDirectory(file.getAbsolutePath());
      File file2 = new File(file, "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-client.txt");
      Bootstrap.printToSYSOUT(crashReport.getCompleteReport());
      CrashReport crashReport2 = crashReport;
      if (isControlFlowDisabled) {
         if (crashReport.getFile() != null) {
            Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + crashReport.getFile());
            System.exit(-1);
            if (isControlFlowDisabled) {
               return;
            }
         }

         crashReport2 = crashReport;
      }

      byte b = (byte)((crashReport2.saveToFile(file2)) ? 1 : 0);
      if (isControlFlowDisabled) {
         if (b != 0) {
            Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + file2.getAbsolutePath());
            System.exit(-1);
            if (isControlFlowDisabled) {
               return;
            }
         }

         Bootstrap.printToSYSOUT("#@?@# Game crashed! Crash report could not be saved. #@?@#");
         b = -2;
      }

      System.exit(b);
   }

   private void drawSplashScreen(TextureManager textureManager) throws LWJGLException {
      ScaledResolution sr = new ScaledResolution(this);
      int scaleFactor = sr.getScaleFactor();
      Framebuffer framebuffer = new Framebuffer(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor, true);
      framebuffer.bindFramebuffer(false);
      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      GlStateManager.ortho(0.0, (double)sr.getScaledWidth(), (double)sr.getScaledHeight(), 0.0, 1000.0, 3000.0);
      GlStateManager.matrixMode(5888);
      GlStateManager.loadIdentity();
      GlStateManager.translate(0.0F, 0.0F, -2000.0F);
      GlStateManager.disableLighting();
      GlStateManager.disableFog();
      GlStateManager.disableDepth();
      GlStateManager.enableTexture2D();
      BufferedImage image = null;
      InputStream resourceAsStream = Augustus.class.getClassLoader().getResourceAsStream("assets/augustus/pictures/startLogo.png");

      try {
         image = ImageIO.read(resourceAsStream);
      } catch (IOException e) {
         e.printStackTrace();
      }

      ResourceLocation dynamicTextureLocation = textureManager.getDynamicTextureLocation("logo", new DynamicTexture(image));
      this.mojangLogo = dynamicTextureLocation;
      textureManager.bindTexture(this.mojangLogo);
      Tessellator instance = Tessellator.getInstance();
      WorldRenderer worldRenderer = instance.getWorldRenderer();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      worldRenderer.pos(0.0, (double)this.displayHeight, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
      worldRenderer.pos((double)this.displayWidth, (double)this.displayHeight, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
      worldRenderer.pos((double)this.displayWidth, 0.0, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
      worldRenderer.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
      instance.draw();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      short s = 256;
      short s2 = 256;
      int n = (sr.getScaledWidth() - s) / 2;
      int n2 = (sr.getScaledHeight() - s2) / 2;
      short s3 = 255;
      short s4 = 255;
      short s5 = 255;
      short s6 = 255;
      byte b = 0;
      byte b2 = 0;
      int n3 = n2;
      int n4 = n;
      this.draw(n4, n3, b2, b, s, s2, s6, s5, s4, s3);
      GlStateManager.disableLighting();
      GlStateManager.disableFog();
      framebuffer.unbindFramebuffer();
      framebuffer.framebufferRender(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor);
      GlStateManager.enableAlpha();
      GlStateManager.alphaFunc(516, 0.1F);
      this.updateDisplay();
   }

   private void updateFramebufferSize() {
      boolean isControlFlowDisabled = isControlFlowDisabled();
      this.framebufferMc.createBindFramebuffer(this.displayWidth, this.displayHeight);
      EntityRenderer entityRenderer2 = this.entityRenderer;
      if (isControlFlowDisabled) {
         if (this.entityRenderer == null) {
            return;
         }

         entityRenderer2 = this.entityRenderer;
      }

      entityRenderer2.updateShaderGroupSize(this.displayWidth, this.displayHeight);
   }

   public static void setControlFlowFlag(boolean flag) {
      controlFlowFlag = flag;
   }

   public static boolean isControlFlowFlag() {
      return controlFlowFlag;
   }

   public static boolean isControlFlowDisabled() {
      boolean isControlFlowFlag = isControlFlowFlag();
      return !isControlFlowFlag;
   }

   static {
      setControlFlowFlag(false);
      LOGGER = LogManager.getLogger();
      isRunningOnMac = Util.getOSType() == Util.EnumOS.OSX;
      memoryReserve = new byte[10485760];
      macDisplayModes = Lists.newArrayList(new DisplayMode[]{new DisplayMode(2560, 1600), new DisplayMode(2880, 1800)});
   }


   public static class DefaultStatStringFormat implements IStatStringFormat {
      public final Minecraft mc;

      public DefaultStatStringFormat(Minecraft mc2) {
         this.mc = mc2;
      }

      @Override
      public String formatString(String str) {
         try {
            return String.format(str, GameSettings.getKeyDisplayString(this.mc.gameSettings.keyBindInventory.getKeyCode()));
         } catch (Exception e) {
            return "Error: " + e.getLocalizedMessage();
         }
      }
   }

   public static class WorkerTask extends Thread {
      public final Minecraft mc;

      @Override
      public void run() {
         while (this.mc.running) {
            try {
               Thread.sleep(2147483647L);
            } catch (InterruptedException e) {
            }
         }
      }

      public WorkerTask(Minecraft mc2, String str) {
         super(str);
         this.mc = mc2;
      }
   }

   public static class ValueCallable implements Callable {
      public final Minecraft mc;

      public ValueCallable(Minecraft mc2) {
         this.mc = mc2;
      }

      public String getCurrentScreenName() throws Exception {
         return this.mc.currentScreen.getClass().getCanonicalName();
      }

      @Override
      public Object call() throws Exception {
         return this.getCurrentScreenName();
      }
   }

   public static class ValueCallable2 implements Callable {
      public final Minecraft mc;

      public ValueCallable2(Minecraft mc2) {
         this.mc = mc2;
      }

      @Override
      public Object call() throws Exception {
         return this.getScreenName();
      }

      public String getScreenName() throws Exception {
         return this.mc.currentScreen.getClass().getCanonicalName();
      }
   }

   public static class ValueCallable3 implements Callable {
      public final Minecraft mc;

      public String getLaunchedVersion() throws Exception {
         return Minecraft.accessVersion(this.mc);
      }

      @Override
      public Object call() throws Exception {
         return this.getLaunchedVersion();
      }

      public ValueCallable3(Minecraft mc2) {
         this.mc = mc2;
      }
   }

   public static class ValueCallable4 implements Callable {
      public final Minecraft mc;

      public String getLwjglVersion() {
         return Sys.getVersion();
      }

      @Override
      public Object call() throws Exception {
         return this.getLwjglVersion();
      }

      public ValueCallable4(Minecraft mc2) {
         this.mc = mc2;
      }
   }

   public static class ValueCallable5 implements Callable {
      public final Minecraft mc;

      public ValueCallable5(Minecraft mc2) {
         this.mc = mc2;
      }

      public String getOpenGlInfo() {
         return GL11.glGetString(7937) + " GL version " + GL11.glGetString(7938) + ", " + GL11.glGetString(7936);
      }

      @Override
      public Object call() throws Exception {
         return this.getOpenGlInfo();
      }
   }

   public static class ValueCallable6 implements Callable {
      public final Minecraft mc;

      public ValueCallable6(Minecraft mc2) {
         this.mc = mc2;
      }

      @Override
      public Object call() throws Exception {
         return this.getGlCapsInfo();
      }

      public String getGlCapsInfo() {
         return OpenGlHelper.getLogText();
      }
   }

   public static class ValueCallable7 implements Callable {
      public final Minecraft mc;

      public String getUsingVbosStatus() {
         return this.mc.gameSettings.useVbo ? "Yes" : "No";
      }

      public ValueCallable7(Minecraft mc2) {
         this.mc = mc2;
      }

      @Override
      public Object call() throws Exception {
         return this.getUsingVbosStatus();
      }
   }

   public static class ValueCallable8 implements Callable {
      public final Minecraft mc;

      public String getModdedStatus() throws Exception {
         String clientModName = ClientBrandRetriever.getClientModName();
         return !clientModName.equals("vanilla")
            ? "Definitely; Client brand changed to '" + clientModName + "'"
            : (
               Minecraft.class.getSigners() == null
                  ? "Very likely; Jar signature invalidated"
                  : "Probably not. Jar signature remains and client brand is untouched."
            );
      }

      public ValueCallable8(Minecraft mc2) {
         this.mc = mc2;
      }

      @Override
      public Object call() throws Exception {
         return this.getModdedStatus();
      }
   }

   public static class ValueCallable9 implements Callable {
      public final Minecraft mc;

      public ValueCallable9(Minecraft mc2) {
         this.mc = mc2;
      }

      public String getClientType() throws Exception {
         return "Client (map_client.txt)";
      }

      @Override
      public Object call() throws Exception {
         return this.getClientType();
      }
   }

   public static class ValueCallable10 implements Callable {
      public final Minecraft mc;

      @Override
      public Object call() throws Exception {
         return this.getResourcePacksList();
      }

      public ValueCallable10(Minecraft mc2) {
         this.mc = mc2;
      }

      public String getResourcePacksList() throws Exception {
         StringBuilder sb = new StringBuilder();

         for (Object obj : this.mc.gameSettings.resourcePacks) {
            if (sb.length() > 0) {
               sb.append(", ");
            }

            sb.append(obj);
            if (this.mc.gameSettings.incompatibleResourcePacks.contains(obj)) {
               sb.append(" (incompatible)");
            }
         }

         return sb.toString();
      }
   }

   public static class ValueCallable11 implements Callable {
      public final Minecraft mc;

      public String getCurrentLanguage() throws Exception {
         return Minecraft.accessMcLanguageManager(this.mc).getCurrentLanguage().toString();
      }

      public ValueCallable11(Minecraft mc2) {
         this.mc = mc2;
      }

      @Override
      public Object call() throws Exception {
         return this.getCurrentLanguage();
      }
   }

   public static class ValueCallable12 implements Callable {
      public final Minecraft mc;

      public ValueCallable12(Minecraft mc2) {
         this.mc = mc2;
      }

      @Override
      public Object call() throws Exception {
         return this.getProfilerPosition();
      }

      public String getProfilerPosition() throws Exception {
         return this.mc.mcProfiler.profilingEnabled ? this.mc.mcProfiler.getNameOfLastSection() : "N/A (disabled)";
      }
   }

   public static class ValueCallable13 implements Callable {
      public final Minecraft mc;

      @Override
      public Object call() throws Exception {
         return this.getCpuInfo();
      }

      public String getCpuInfo() {
         return OpenGlHelper.getCpu();
      }

      public ValueCallable13(Minecraft mc2) {
         this.mc = mc2;
      }
   }

   public static class EnumMinecartTypeSwitchMap {
      public static final int[] MOVING_OBJECT_TYPE_ORDINAL_MAP;
      public static final int[] MINECART_TYPE_ORDINAL_MAP = new int[EntityMinecart.EnumMinecartType.values().length];

      static {
         try {
            MINECART_TYPE_ORDINAL_MAP[EntityMinecart.EnumMinecartType.FURNACE.ordinal()] = 1;
         } catch (NoSuchFieldError e) {
         }

         try {
            MINECART_TYPE_ORDINAL_MAP[EntityMinecart.EnumMinecartType.CHEST.ordinal()] = 2;
         } catch (NoSuchFieldError e2) {
         }

         try {
            MINECART_TYPE_ORDINAL_MAP[EntityMinecart.EnumMinecartType.TNT.ordinal()] = 3;
         } catch (NoSuchFieldError e3) {
         }

         try {
            MINECART_TYPE_ORDINAL_MAP[EntityMinecart.EnumMinecartType.HOPPER.ordinal()] = 4;
         } catch (NoSuchFieldError e4) {
         }

         try {
            MINECART_TYPE_ORDINAL_MAP[EntityMinecart.EnumMinecartType.COMMAND_BLOCK.ordinal()] = 5;
         } catch (NoSuchFieldError e5) {
         }

         MOVING_OBJECT_TYPE_ORDINAL_MAP = new int[MovingObjectPosition.MovingObjectType.values().length];

         try {
            MOVING_OBJECT_TYPE_ORDINAL_MAP[MovingObjectPosition.MovingObjectType.ENTITY.ordinal()] = 1;
         } catch (NoSuchFieldError e6) {
         }

         try {
            MOVING_OBJECT_TYPE_ORDINAL_MAP[MovingObjectPosition.MovingObjectType.BLOCK.ordinal()] = 2;
         } catch (NoSuchFieldError e7) {
         }

         try {
            MOVING_OBJECT_TYPE_ORDINAL_MAP[MovingObjectPosition.MovingObjectType.MISS.ordinal()] = 3;
         } catch (NoSuchFieldError e8) {
         }
      }
   }
}
