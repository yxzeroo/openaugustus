package net.augustus.modules.render;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.vendor.OnlineUserRegistry;
import net.augustus.cosmetics.model.CosmeticModel;
import net.augustus.cosmetics.MoreCosmeticsCore;
import net.augustus.settings.ColorSetting;
import net.augustus.cosmetics.CosmeticSettingEntry;
import net.augustus.utils.TupleTriple;
import net.augustus.events.listeners.TickListener;
import net.augustus.cosmetics.CosmeticDataManager;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.labymod.ModelData;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;

public class Cosmetics extends Module implements TickListener {
   private final HashMap remotePlayerCosmetics;
   private final java.util.ArrayList localCosmeticEntries;
   private final TimeHelper timeHelper;
   private boolean pendingSyncOnEnable;
   private boolean pendingSyncOnDisable;
   private int lastSyncHash;
   public final BooleanValue hideCosmeticsOfOtherUsers;
   public final BooleanValue hintMessageThatRenderingOfCosmeticsCanBeDisabled;

   public Cosmetics() {
       super("Cosmetics", "Kosmetische Gegenstände", new Color(0, 244, 231, 255), Categorys.RENDER);
      this.remotePlayerCosmetics = new HashMap();
      this.localCosmeticEntries = new java.util.ArrayList();
      this.timeHelper = new TimeHelper();
      this.pendingSyncOnEnable = false;
      this.pendingSyncOnDisable = false;
      MoreCosmeticsCore.getInstance().getModelLoader().getModelsByName().forEach((obj, obj2) -> this.registerLocalCosmeticSetting((String)obj, (CosmeticModel)obj2));
      this.hideCosmeticsOfOtherUsers = new BooleanValue(this, "Hide cosmetics of other users", false);
      BooleanValue booleanValue = new BooleanValue(this, "Hint message that rendering of cosmetics can be disabled", true);
      SettingVisibilityCondition condition = () -> this.isCosmeticsHintVisible();
      this.hintMessageThatRenderingOfCosmeticsCanBeDisabled = (BooleanValue)booleanValue.withVisibility(condition);
   }

   public void onEnable() {
      this.pendingSyncOnEnable = true;
   }

   public void onDisable() {
      this.pendingSyncOnDisable = true;
      this.clearLocalPlayerCosmetics();
   }

   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
   }

   public void onTick5() {
   }

   public void applyPlayerCosmetics(AbstractClientPlayer clientPlayer) {
      if (clientPlayer instanceof EntityPlayerSP) {
         this.applyLocalPlayerCosmetics(clientPlayer);
      } else if (clientPlayer.getGameProfile() != null) {
         TupleTriple tupleTriple = (TupleTriple)this.remotePlayerCosmetics.get(clientPlayer.getGameProfile().getName());
         if (tupleTriple == null) {
            return;
         }

         int first = (Integer)tupleTriple.getFirst();
         String second = (String)tupleTriple.getSecond();
         int n = first;
         if (!OnlineUserRegistry.matchesPlayerIdentity(clientPlayer, n, second)) {
            return;
         }

         HashMap third = (HashMap)tupleTriple.getThird();
         if (third == null) {
            return;
         }

         Collection values = third.values();
         this.applyCosmeticEntries(values, clientPlayer);
      }
   }

   public void applyLocalPlayerCosmetics(AbstractClientPlayer clientPlayer) {
      if (this.isToggled()) {
         java.util.ArrayList localCosmeticEntries2 = this.localCosmeticEntries;
         this.applyCosmeticEntries(localCosmeticEntries2, clientPlayer);
      }
   }

   public void updateRemotePlayerCosmetics(String str, int n, String str2, int n2, Map map, Map map2, Map map3, Map map4, Map map5, Map map6, Map map7, Map map8) {
      TupleTriple tupleTriple = (TupleTriple)this.remotePlayerCosmetics.get(str);
      HashMap map9 = tupleTriple != null ? (HashMap)tupleTriple.getThird() : new HashMap();
      if (tupleTriple == null) {
         this.initRemoteCosmeticEntries(map9);
      }

      AtomicInteger atomicInteger = new AtomicInteger();
      atomicInteger.set(0);
      map9.forEach((k, v) -> applyRemoteCosmeticEntryData(n2, map, map2, map3, map4, map5, map6, map7, map8, atomicInteger, (String)k, (CosmeticSettingEntry)v));
      this.remotePlayerCosmetics.put(str, new TupleTriple(n, str2, map9));
   }

   private void initRemoteCosmeticEntries(HashMap map) {
      MoreCosmeticsCore.getInstance().getModelLoader().getModelsByName().forEach((obj, obj2) -> createRemoteCosmeticEntry(map, (String)obj, (CosmeticModel)obj2));
   }

   public void removeRemotePlayerCosmetics(String str) {
      TupleTriple tupleTriple = (TupleTriple)this.remotePlayerCosmetics.get(str);
      if (tupleTriple != null) {
         HashMap third = (HashMap)tupleTriple.getThird();
         EntityPlayer player = this.findWorldPlayerByName(str);
         if (player instanceof AbstractClientPlayer) {
            Collection values = third.values();
            AbstractClientPlayer clientPlayer = (AbstractClientPlayer)player;
            boolean flag = true;
            AbstractClientPlayer clientPlayer2 = clientPlayer;
            Collection items = values;
            this.updateCosmeticEntries(items, clientPlayer2, flag);
            this.remotePlayerCosmetics.remove(str);
         }
      }
   }

   public void clearAllRemoteCosmetics() {
      this.remotePlayerCosmetics.forEach((obj, obj2) -> this.disableRemotePlayerEntry((String)obj, (TupleTriple)obj2));
      this.remotePlayerCosmetics.clear();
   }

   private void applyCosmeticEntries(Collection items, AbstractClientPlayer clientPlayer) {
      boolean flag = false;
      this.updateCosmeticEntries(items, clientPlayer, flag);
   }

   private void updateCosmeticEntries(Collection items, AbstractClientPlayer clientPlayer, boolean flag) {
      if (clientPlayer != null && items != null) {
         for (CosmeticSettingEntry cosmeticSettingEntry : (Iterable<CosmeticSettingEntry>)(items)) {
            CosmeticDataManager cosmeticDataManager = MoreCosmeticsCore.getInstance().getCosmeticDataManager();
            boolean flag2 = !flag && cosmeticSettingEntry.getEnabledSetting().getValue();
            CosmeticModel model = cosmeticSettingEntry.getModel();
            float scale = (float)cosmeticSettingEntry.getScale();
            float height = (float)cosmeticSettingEntry.getHeight();
            ColorSetting[] colorSettings = cosmeticSettingEntry.getColorSettings();
            float f = height;
            float f2 = scale;
            CosmeticModel cosmeticModel = model;
            boolean flag3 = flag2;
            cosmeticDataManager.applyCosmeticToPlayer(flag3, cosmeticModel, clientPlayer, f2, f, colorSettings);
         }
      }
   }

   private EntityPlayer findWorldPlayerByName(String str) {
      return mc.theWorld != null && !mc.theWorld.playerEntities.isEmpty() ? (EntityPlayer)mc.theWorld.playerEntities.stream().filter(obj -> ((EntityPlayer)obj).getGameProfile() != null && ((EntityPlayer)obj).getGameProfile().getName().equals(str)).findFirst().orElse(null) : null;
   }

   public boolean isPendingSyncOnDisable() {
      return this.pendingSyncOnDisable;
   }

   public void setPendingSyncOnDisable(boolean flag) {
      this.pendingSyncOnDisable = flag;
   }

   public void initLocalPlayerModelData() {

      for (CosmeticSettingEntry cosmeticSettingEntry : (Iterable<CosmeticSettingEntry>)(this.localCosmeticEntries)) {
         CosmeticModel model = cosmeticSettingEntry.getModel();
         CosmeticDataManager cosmeticDataManager = MoreCosmeticsCore.getInstance().getCosmeticDataManager();
         CosmeticModel model2 = cosmeticSettingEntry.getModel();
         boolean flag = true;
         CosmeticModel cosmeticModel = model2;
         ModelData orCreateModelData = cosmeticDataManager.getOrCreateModelData(cosmeticModel, flag);
         mc.thePlayer.getCosmetics().putIfAbsent(model.getId(), orCreateModelData.clone_m1());
      }
   }

   public void clearLocalPlayerCosmetics() {
      boolean flag = true;
      EntityPlayerSP thePlayer = mc.thePlayer;
      java.util.ArrayList localCosmeticEntries2 = this.localCosmeticEntries;
      this.updateCosmeticEntries(localCosmeticEntries2, (AbstractClientPlayer)thePlayer, flag);
   }

   private static boolean matchesPlayerName(char ch, short s, String str, int n, EntityPlayer player) {
      return player.getGameProfile() != null && player.getGameProfile().getName().equals(str);
   }

   private void disableRemotePlayerEntry(String str, TupleTriple tupleTriple) {
      if (tupleTriple != null && str != null) {
         HashMap third = (HashMap)tupleTriple.getThird();
         EntityPlayer player = this.findWorldPlayerByName(str);
         if (player instanceof AbstractClientPlayer) {
            Collection values = third.values();
            AbstractClientPlayer clientPlayer = (AbstractClientPlayer)player;
            boolean flag = true;
            AbstractClientPlayer clientPlayer2 = clientPlayer;
            Collection items = values;
            this.updateCosmeticEntries(items, clientPlayer2, flag);
         }
      }
   }

   private static void createRemoteCosmeticEntry(HashMap map, String str, CosmeticModel cosmeticModel) {
      if (map.size() <= 40) {
         BooleanValue booleanValue = new BooleanValue(null, str, false);
         StringValue stringValue = new StringValue(null, str + "Scale", 1.0, 0.5, 2.0, 2);
         StringValue stringValue2 = new StringValue(null, str + "Height", 0.0, -2.0, 2.0, 2);
         CosmeticDataManager cosmeticDataManager = MoreCosmeticsCore.getInstance().getCosmeticDataManager();
         boolean flag = true;
         ModelData orCreateModelData = cosmeticDataManager.getOrCreateModelData(cosmeticModel, flag);
         int length = orCreateModelData.model.length;
         ColorSetting[] colorSettings = new ColorSetting[length];

         for (int i = 0; i < length; i++) {
            colorSettings[i] = new ColorSetting((net.augustus.modules.Module)null, str + "Color" + i, new Color(orCreateModelData.model[i].color));
         }

         map.put(str, new CosmeticSettingEntry(cosmeticModel, booleanValue, stringValue, stringValue2, colorSettings, null));
      }
   }

   private static void applyRemoteCosmeticEntryData(int maxCount, Map enabledMap, Map f1Map, Map f2Map, Map cMap, Map b1Map, Map b2Map, Map d1Map, Map d2Map, AtomicInteger atomicInteger, String str, CosmeticSettingEntry cosmeticSettingEntry) {
      Boolean bool = (Boolean)enabledMap.get(str);
      if (bool != null) {
         int n = 0;
         if (bool) {
            n = atomicInteger.incrementAndGet();
         }

         BooleanValue enabledSetting = cosmeticSettingEntry.getEnabledSetting();
         boolean flag = bool && n <= maxCount;
         enabledSetting.setValue(flag);
         if (bool) {
            Float f = (Float)f1Map.get(str);
            if (f != null && !Float.isNaN(f)) {
               double d = (double)f.floatValue();
               cosmeticSettingEntry.setScale(d);
            }

            Float f2 = (Float)f2Map.get(str);
            if (f2 != null && !Float.isNaN(f2)) {
               double d2 = (double)f2.floatValue();
               cosmeticSettingEntry.setHeight(d2);
            }

            int[] ints = (int[])cMap.get(str);
            boolean[] flags = (boolean[])b1Map.get(str);
            boolean[] flags2 = (boolean[])b2Map.get(str);
            double[] doubles = (double[])d1Map.get(str);
            double[] doubles2 = (double[])d2Map.get(str);

            for (int i = 0; i < cosmeticSettingEntry.getColorSettings().length; i++) {
               ColorSetting colorSetting = cosmeticSettingEntry.getColorSettings()[i];
               Color color2 = new Color(ints[i], true);
               colorSetting.setPrimaryColor(color2);
               double d3 = doubles[i];
               colorSetting.setRainbowSpeed(d3);
               double d4 = doubles2[i];
               colorSetting.setAstolfoSpeed(d4);
               boolean flag2 = flags[i];
               colorSetting.setRainbowEnabled(flag2);
               boolean flag3 = flags2[i];
               colorSetting.setAstolfoEnabled(flag3);
            }
         }
      }
   }

   private boolean isCosmeticsHintVisible() {
      return !this.hideCosmeticsOfOtherUsers.getValue();
   }

   private void registerLocalCosmeticSetting(String str, CosmeticModel cosmeticModel) {
      DoubleValue doubleValue = (DoubleValue)(new DoubleValue(this, "Group" + str, true)).withDisplayName(str);
      BooleanValue booleanValue = new BooleanValue(this, str, false, doubleValue);
      StringValue stringValue = new StringValue(this, str + "Scale", 1.0, 0.5, 2.0, 2, doubleValue);
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      StringValue stringValue2 = stringValue.visibleWhen(condition);
      stringValue = new StringValue(this, str + "Height", 0.0, -2.0, 2.0, 2, doubleValue);
      SettingVisibilityCondition condition2 = () -> booleanValue.getValue();
      StringValue stringValue3 = stringValue.visibleWhen(condition2);
      CosmeticDataManager cosmeticDataManager = MoreCosmeticsCore.getInstance().getCosmeticDataManager();
      boolean flag = true;
      ModelData orCreateModelData = cosmeticDataManager.getOrCreateModelData(cosmeticModel, flag);
      int length = orCreateModelData.model.length;
      ColorSetting[] colorSettings = new ColorSetting[length];

      for (int i = 0; i < length; i++) {
         String str2 = str + "Color" + i;
         Color color2 = new Color(orCreateModelData.model[i].color);
         String str3 = str2;
         ColorSetting colorSetting = new ColorSetting(this, str3, color2, doubleValue);
         SettingVisibilityCondition condition3 = () -> booleanValue.getValue();
         colorSettings[i] = (ColorSetting)colorSetting.withVisibility(condition3);
      }

      this.localCosmeticEntries.add(new CosmeticSettingEntry(cosmeticModel, booleanValue, stringValue2, stringValue3, colorSettings, null));
   }


}
