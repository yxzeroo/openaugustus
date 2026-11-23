package net.augustus.modules.render;

import com.google.gson.JsonObject;
import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.settings.options.BlockMarkerRenderOption;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.settings.options.BlockMarkerBlockOption;
import net.augustus.modules.render.blockmarker.MarkedBlockCoord;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.utils.file.ClientPaths;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.settings.ColorSetting;
import net.augustus.settings.TextInputSetting;
import net.augustus.settings.ActionSetting;
import net.augustus.modules.render.blockmarker.MarkedBlockGroup;
import net.augustus.render.RenderUtil;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.modules.render.blockmarker.BlockMarkerProfile;
import net.augustus.settings.SettingChangeListener;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

public class BlockMarker extends Module implements RenderListener {
   private static final String PROFILES_DIR = ClientPaths.CLIENT_DIR + File.separator + "blockmarker";
   private BooleansSetting scripts;
   private final MultiOptionSetting renderMode;
   private final StringValue renderDistance;
   private final ColorSetting color2;
   private final BlurShadowSettings blurShadowSettings;
   private final ActionSetting openFolder;
   private final TextInputSetting nameOfNewScript;
   private final StringValue radiusToFill;
   private BlockMarkerProfile blockMarkerProfile;
   private final HashMap blockOptions;

   public BlockMarker() {
       super("BlockMarker", "Block Markierer", new Color(121, 242, 236), Categorys.RENDER);
      BooleansSetting booleansSetting = new BooleansSetting(this, "Scripts", "", new String[0]);
      SettingChangeListener listener = () -> {
         String selectedValue = this.scripts.getSelectedValue();
         if (!selectedValue.isEmpty()) {
            BlockMarkerProfile blockMarkerProfile2 = this.loadProfile(selectedValue);
            if (blockMarkerProfile2 != null) {
               this.blockMarkerProfile = blockMarkerProfile2;
            } else {
               String str = "§cFailed to load " + selectedValue + ".json to BlockMarker";
               this.sendChatMessage2(str);
            }
         }
      };
      SettingChangeListener listener2 = listener;
      this.scripts = (BooleansSetting)booleansSetting.withChangeListener(listener2);
      this.renderMode = new MultiOptionSetting(this, "RenderMode", BlockMarkerRenderOption.values());
      this.renderDistance = new StringValue(this, "Render distance", 30.0, 0.0, 200.0, 0);
      this.color2 = new ColorSetting(this, "Color", new Color(54, 250, 202, 100));
      this.blurShadowSettings = (new BlurShadowSettings(this)).disableBlurByDefault();
      this.openFolder = new ActionSetting(this, "Open Folder", () -> openProfilesFolder());
      this.blockMarkerProfile = null;
      this.blockOptions = new HashMap();
      ClientFileUtil.ensureDirectory(PROFILES_DIR);
      this.refreshProfileList();
      HashMap blockOptions2 = this.blockOptions;
      this.populateBlockOptions(blockOptions2);
      BlockMarkerBlockOption[] blockMarkerBlockOptions = (BlockMarkerBlockOption[])this.blockOptions.values().toArray(new BlockMarkerBlockOption[0]);
      Arrays.sort(blockMarkerBlockOptions, Comparator.comparing(blockMarkerBlockOption -> blockMarkerBlockOption.getName()));
      DoubleValue doubleValue = new DoubleValue(this, "Creation tab", true);
      this.nameOfNewScript = new TextInputSetting(this, "Name of new script", "ExampleName", doubleValue);
      new MultiOptionSetting(this, "Blocks to add to script", blockMarkerBlockOptions, doubleValue);
      this.radiusToFill = new StringValue(this, "Radius to fill", 100.0, 0.0, 200.0, 0, doubleValue);
      new ActionSetting(this, "Create new script", () -> this.createNewProfile(), doubleValue);
      new ActionSetting(this, "Add to selected script", () -> this.addBlocksToSelectedProfile(), doubleValue);
      new ActionSetting(this, "Delete selected script", () -> this.deleteSelectedProfile(), doubleValue);
   }

   @Override
   public void onEnable() {
      this.refreshProfileList();
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 32;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onRender(float f) {
      if (this.blockMarkerProfile != null) {
         RenderUtil.begin3DRender();
         boolean isBlurActive = this.blurShadowSettings.isBlurActive();
         BlurShadowManager.enableOneShotPass(isBlurActive, this.blurShadowSettings.isShadowActive());
         BlurShadowManager.submitRenderTask(false, shaderRenderPass -> this.renderActiveProfilePass(shaderRenderPass));
         RenderUtil.end3DRender();
      }
   }

   private void renderActiveProfilePass(ShaderRenderPass shaderRenderPass) {
      List blockGroups = BlockMarkerProfile.getBlockGroups(this.blockMarkerProfile);
      int n = 0;

      for (int i = 0; i < blockGroups.size(); i++) {
         MarkedBlockGroup markedBlockGroup = (MarkedBlockGroup)blockGroups.get(i);
         n = this.renderMarkedBlockGroup(markedBlockGroup, n);
      }
   }

   private void populateBlockOptions(HashMap map) {

      for (ResourceLocation location : (Iterable<ResourceLocation>)(Block.blockRegistry.getKeys())) {
         String resourcePath = location.getResourcePath();
         Block blockFromName = Block.getBlockFromName(resourcePath);
         map.put(blockFromName, new BlockMarkerBlockOption(this, resourcePath, blockFromName));
      }
   }

   private int renderMarkedBlockGroup(MarkedBlockGroup markedBlockGroup, int n) {
      double renderPosX = mc.getRenderManager().getRenderPosX();
      double renderPosY = mc.getRenderManager().getRenderPosY();
      double renderPosZ = mc.getRenderManager().getRenderPosZ();
      double value = this.renderDistance.getValue();
      Block block = MarkedBlockGroup.getBlock(markedBlockGroup);
      boolean flag = BlockMarkerRenderOption.NOT_SAME_NOT_AIR.isSelected() && BlockMarkerRenderOption.SAME_BLOCK.isSelected() && BlockMarkerRenderOption.NOT_SAME_AIR.isSelected();

      for (int i = 0; i < MarkedBlockGroup.getCoords(markedBlockGroup).size(); i++) {
         MarkedBlockCoord markedBlockCoord = (MarkedBlockCoord)MarkedBlockGroup.getCoords(markedBlockGroup).get(i);
         if (mc.thePlayer.getDistanceSq((double)MarkedBlockCoord.getX(markedBlockCoord), (double)MarkedBlockCoord.getY(markedBlockCoord), (double)MarkedBlockCoord.getZ(markedBlockCoord)) < value * value) {
            if (!flag) {
               if (!this.shouldRenderMarkedCoord(block, markedBlockCoord)) {
                  continue;
               }
            }

            RenderUtil.setGlColor(this.color2.getColorInt(n));
            RenderUtil.drawFilledBox(
               (double)MarkedBlockCoord.getX(markedBlockCoord) - renderPosX,
               (double)MarkedBlockCoord.getY(markedBlockCoord) - renderPosY,
               (double)MarkedBlockCoord.getZ(markedBlockCoord) - renderPosZ,
               (double)(MarkedBlockCoord.getX(markedBlockCoord) + 1) - renderPosX,
               (double)(MarkedBlockCoord.getY(markedBlockCoord) + 1) - renderPosY,
               (double)(MarkedBlockCoord.getZ(markedBlockCoord) + 1) - renderPosZ
            );
            n++;
         }
      }

      return n;
   }

   private boolean shouldRenderMarkedCoord(Block block, MarkedBlockCoord markedBlockCoord) {
      Block block2 = mc.theWorld.getBlockState2(MarkedBlockCoord.getX(markedBlockCoord), MarkedBlockCoord.getY(markedBlockCoord), MarkedBlockCoord.getZ(markedBlockCoord)).getBlock();
      if (block2.equals(block) && BlockMarkerRenderOption.SAME_BLOCK.isSelected()) {
         return true;
      } else {
         return !block2.equals(block) && BlockMarkerRenderOption.NOT_SAME_AIR.isSelected() && block2.equals(Blocks.air)
            ? true
            : !block2.equals(block) && BlockMarkerRenderOption.NOT_SAME_NOT_AIR.isSelected() && !block2.equals(Blocks.air);
      }
   }

   private void refreshProfileList() {
      BooleansSetting scripts2 = this.scripts;
      String[] availableProfileNames = this.getAvailableProfileNames();
      scripts2.setOptions(availableProfileNames);
   }

   public String[] getAvailableProfileNames() {
      java.util.ArrayList list = new java.util.ArrayList();
      File[] files = this.listProfileJsonFiles();
      if (files != null) {
         for (File file : files) {
            String name2 = file.getName();
            String str = name2.substring(0, name2.length() - 5);
            list.add(str);
         }

         list.sort(String.CASE_INSENSITIVE_ORDER);
      }

      return (String[])list.toArray(new String[0]);
   }

   public BlockMarkerProfile loadProfile(String str) {
      str = str + ".json";
      File file = new File(PROFILES_DIR + File.separator + str);
      BlockMarkerProfile blockMarkerProfile2 = null;
      if (file.exists()) {
         String absolutePath = file.getParentFile().getAbsolutePath();
         Object obj = null;
         JsonObject json = ClientFileUtil.readJsonObject(absolutePath, str, (Runnable)obj);

         try {
            blockMarkerProfile2 = this.parseProfileJson(json);
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

      return blockMarkerProfile2;
   }

   private void saveProfile(BlockMarkerProfile blockMarkerProfile2) {
      JsonObject json = new JsonObject();
      BlockMarkerProfile.serializeProfile(blockMarkerProfile2, json);
      String str = PROFILES_DIR;
      String str2 = BlockMarkerProfile.getName(blockMarkerProfile2) + ".json";
      ClientFileUtil.writeJsonObject(str, str2, json);
   }

   private BlockMarkerProfile buildNewProfileFromWorld() {
      BlockMarkerProfile blockMarkerProfile2 = new BlockMarkerProfile(this, this.nameOfNewScript.getValue());
      int n = (int)Math.max(0.0, this.radiusToFill.getValue());
      this.scanAndMergeIntoProfile(blockMarkerProfile2, n);
      return blockMarkerProfile2;
   }

   private BlockMarkerProfile appendWorldBlocksToActiveProfile() {
      if (this.blockMarkerProfile == null) {
         return null;
      } else {
         BlockMarkerProfile blockMarkerProfile2 = this.blockMarkerProfile;
         int n = (int)Math.max(0.0, this.radiusToFill.getValue());
         BlockMarkerProfile blockMarkerProfile3 = blockMarkerProfile2;
         this.scanAndMergeIntoProfile(blockMarkerProfile3, n);
         return this.blockMarkerProfile;
      }
   }

   private void scanAndMergeIntoProfile(BlockMarkerProfile blockMarkerProfile2, int n) {
      Map map = this.createSelectedBlockGroups();
      this.scanWorldBlocksInRadius(map, n);
      if (BlockMarkerProfile.getBlockGroups(blockMarkerProfile2).isEmpty()) {
         BlockMarkerProfile.getBlockGroups(blockMarkerProfile2).addAll(map.values());
      } else {
         Collection values = map.values();
         this.mergeBlockGroupsIntoProfile(blockMarkerProfile2, values);
      }
   }

   private void mergeBlockGroupsIntoProfile(BlockMarkerProfile blockMarkerProfile2, Collection items) {
      HashMap map = new HashMap();

      for (MarkedBlockGroup markedBlockGroup : (Iterable<MarkedBlockGroup>)(BlockMarkerProfile.getBlockGroups(blockMarkerProfile2))) {
         map.put(MarkedBlockGroup.getBlockName(markedBlockGroup), markedBlockGroup);
      }

      for (MarkedBlockGroup markedBlockGroup2 : (Iterable<MarkedBlockGroup>)(items)) {
         MarkedBlockGroup markedBlockGroup3 = (MarkedBlockGroup)map.get(MarkedBlockGroup.getBlockName(markedBlockGroup2));
         if (markedBlockGroup3 != null) {
            List coords = MarkedBlockGroup.getCoords(markedBlockGroup2);
            List coords2 = MarkedBlockGroup.getCoords(markedBlockGroup3);
            List list = coords;
            this.mergeUniqueBlockCoords(list, coords2);
         } else {
            BlockMarkerProfile.getBlockGroups(blockMarkerProfile2).add(markedBlockGroup2);
         }
      }
   }

   private void mergeUniqueBlockCoords(List list, List list2) {
      LinkedList list3 = new LinkedList();

      for (MarkedBlockCoord markedBlockCoord : (Iterable<MarkedBlockCoord>)(list)) {
         boolean flag = false;

         for (MarkedBlockCoord markedBlockCoord2 : (Iterable<MarkedBlockCoord>)(list2)) {
            if (MarkedBlockCoord.matchesCoord(markedBlockCoord, markedBlockCoord2)) {
               flag = true;
               break;
            }
         }

         if (!flag) {
            list3.add(markedBlockCoord);
         }
      }

      list2.addAll(list3);
   }

   private Map createSelectedBlockGroups() {
      HashMap map = new HashMap();

      for (BlockMarkerBlockOption blockMarkerBlockOption : (Iterable<BlockMarkerBlockOption>)(this.blockOptions.values())) {
         if (blockMarkerBlockOption.isSelected()) {
            Block block = BlockMarkerBlockOption.getBlock(blockMarkerBlockOption);
            MarkedBlockGroup markedBlockGroup = new MarkedBlockGroup(this, ((ResourceLocation)Block.blockRegistry.getNameForObject(block)).getResourcePath(), block);
            map.put(block, markedBlockGroup);
         }
      }

      return map;
   }

   private void scanWorldBlocksInRadius(Map map, int n) {
      int posX = (int)mc.thePlayer.posX;
      int posY = (int)mc.thePlayer.posY;
      int posZ = (int)mc.thePlayer.posZ;
      int n2 = Math.max(0, posY - n);
      int n3 = Math.min(255, posY + n);

      for (int i = -n + posX; i <= n + posX; i++) {
         for (int j = n2; j <= n3; j++) {
            for (int k = -n + posZ; k <= n + posZ; k++) {
               IBlockState blockState2 = mc.theWorld.getBlockState2(i, j, k);
               Block block = blockState2.getBlock();
               MarkedBlockGroup markedBlockGroup = (MarkedBlockGroup)map.get(block);
               if (markedBlockGroup != null) {
                  MarkedBlockGroup.getCoords(markedBlockGroup).add(new MarkedBlockCoord(this, i, j, k));
               }
            }
         }
      }
   }

   private BlockMarkerProfile parseProfileJson(JsonObject json) {
      BlockMarkerProfile blockMarkerProfile2 = null;
      if (json != null) {
         try {
            String asString = json.get("Name").getAsString();
            blockMarkerProfile2 = new BlockMarkerProfile(this, asString);
            JsonObject asJsonObject = json.getAsJsonObject("Blocks");
            BlockMarkerProfile.deserializeProfile(blockMarkerProfile2, asJsonObject);
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

      return blockMarkerProfile2;
   }

   public File[] listProfileJsonFiles() {
      File file = new File(PROFILES_DIR);
      if (!file.exists()) {
         return null;
      } else {
         return file.listFiles(file2 -> {
            return file2.isFile() && file2.exists() && file2.getName().endsWith(".json");
         });
      }
   }

   private void deleteSelectedProfile() {
      if (this.blockMarkerProfile != null) {
         File file = new File(PROFILES_DIR + File.separator + BlockMarkerProfile.getName(this.blockMarkerProfile) + ".json");
         if (file.exists()) {
            ClientFileUtil.moveToTrashOrDelete(file);
            this.blockMarkerProfile = null;
         }
      } else {
         String str = "§4No script selected to delete. Please select one.";
         this.sendChatMessage2(str);
      }

      this.refreshProfileList();
   }

   private void addBlocksToSelectedProfile() {
      BlockMarkerProfile blockMarkerProfile2 = this.appendWorldBlocksToActiveProfile();
      if (blockMarkerProfile2 == null) {
         String str = "§4No script selected to add blocks to. Please create a new one.";
         this.sendChatMessage2(str);
      } else {
         this.saveProfile(blockMarkerProfile2);
      }

      this.refreshProfileList();
   }

   private void createNewProfile() {
      if (!this.nameOfNewScript.getValue().isEmpty()) {
         BlockMarkerProfile blockMarkerProfile2 = this.buildNewProfileFromWorld();
         this.saveProfile(blockMarkerProfile2);
         this.refreshProfileList();
      }
   }

   private static void openProfilesFolder() {
      ClientFileUtil.openFolder(PROFILES_DIR);
   }

}
