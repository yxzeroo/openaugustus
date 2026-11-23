package net.augustus.cosmetics;

import net.augustus.cosmetics.model.CosmeticModelLoader;
import net.augustus.cosmetics.model.GeckoLibRegistry;
import net.augustus.cosmetics.render.AbstractCosmeticRenderer;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MoreCosmeticsCore {
   public static final int PROTOCOL_VERSION = 1200;
   private boolean rendererInitialized;
   private ArrayList pendingTasks;
   private static MoreCosmeticsCore instance;
   public static final JsonParser JSON_PARSER = new JsonParser();
   private AbstractCosmeticRenderer cosmeticRenderer;
   public static final Gson GSON = new Gson();
   private CosmeticDataManager cosmeticDataManager;
   private CosmeticModelLoader modelLoader;
   public static final Logger LOGGER = LogManager.getLogger("MoreCosmetics");
   public static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(3);
   private MinecraftBridge minecraftBridge;
   private ArrayList tickListeners = new ArrayList();

   public void runAsync(Runnable runnable) {
      EXECUTOR.execute(runnable);
   }

   public CosmeticDataManager getCosmeticDataManager() {
      return this.cosmeticDataManager;
   }

   public static void logError(Throwable t) {
      LOGGER.catching(t);
      String stackTraceString = CosmeticsHttpUtil.getStackTraceString(t);
      ConsoleMessageType error = ConsoleMessageType.ERROR;
      CosmeticsDebugConsole.appendMessage(stackTraceString, error);
   }

   public static MoreCosmeticsCore getInstance(int n) {
      if (instance == null) {
         instance = new MoreCosmeticsCore();
         instance.initCore();
      }

      return instance;
   }

   private MoreCosmeticsCore() {
      this.pendingTasks = new ArrayList();
   }

   public void registerTickListener(CosmeticTickListener listener) {
      this.tickListeners.add(listener);
   }

   public static void logInfo(String str) {
      LOGGER.info("[MoreCosmetics] " + str);
      ConsoleMessageType log = ConsoleMessageType.LOG;
      CosmeticsDebugConsole.appendMessage(str, log);
   }

   public AbstractCosmeticRenderer getCosmeticRenderer() {
      return this.cosmeticRenderer;
   }

   public CosmeticModelLoader getModelLoader() {
      return this.modelLoader;
   }

   private MinecraftBridge createMinecraftBridge() {
      return new MinecraftBridgeImpl();
   }

   public static MoreCosmeticsCore getInstance() {
      if (instance == null) {
         instance = new MoreCosmeticsCore();
         instance.initCore();
      }

      return instance;
   }

   public void onClientTick() {

      try {
         if (!this.rendererInitialized) {
            this.rendererInitialized = true;
            this.cosmeticRenderer.registerRenderLayers();
         }

         for (CosmeticTickListener listener : (Iterable<CosmeticTickListener>)(this.tickListeners)) {
            int tickCount = CosmeticRenderState.tickCount;
            listener.onTick(tickCount);
         }

         CosmeticRenderState.tickCount++;
         if (!this.pendingTasks.isEmpty()) {
            this.pendingTasks.forEach(obj -> ((Runnable)obj).run());
            this.pendingTasks.clear();
         }
      } catch (Exception e) {
         logError(e);
      }
   }

   public MinecraftBridge getMinecraftBridge() {
      return this.minecraftBridge;
   }


   private void initCore() {
      CosmeticsEnvironment.detectEnvironment();

      try {
         GeckoLibRegistry.initialize();
         MinecraftBridge minecraftBridge2 = this.minecraftBridge = this.createMinecraftBridge();
         this.registerTickListener((CosmeticTickListener)minecraftBridge2);
         boolean flag = false;
         UUID playerUuid = this.minecraftBridge.getPlayerUuid(flag);
         CosmeticDataManager cosmeticDataManager2 = this.cosmeticDataManager = new CosmeticDataManager(playerUuid);
         this.registerTickListener(cosmeticDataManager2);
         this.modelLoader = this.minecraftBridge.createModelLoader();
         this.cosmeticRenderer = this.minecraftBridge.createCosmeticRenderer();
         CosmeticsEnvironment.detectRuntimeFlags();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void logDebug(String str) {
      ConsoleMessageType debug = ConsoleMessageType.DEBUG;
      CosmeticsDebugConsole.appendMessage(str, debug);
   }




}
