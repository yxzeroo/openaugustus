package net.augustus.modules.render;

import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.settings.ActionSetting;
import net.augustus.font.FontManager;
import net.augustus.font.FormattedUnicodeFontRenderer;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.settings.SettingChangeListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

public class NameTags extends Module {
   public final BooleansSetting mode;
   public final StringValue scale;
   public final StringValue height;
   public final BooleanValue outline;
   public final BooleanValue armor;
   public final BooleanValue armorDurability;
   public final BooleanValue armorEnchantments;
   public final BooleanValue potionEffects;
   public final BooleanValue showExtraServerNametag;
   public final BooleansSetting healthMode;
   public final BooleanValue ping;
   public final BooleanValue throughWalls;
   public final BooleanValue ignoreTeammates;
   public final BooleanValue myself;
   public final BooleanValue armorStands;
   public final BooleanValue invisible;
   public final BooleanValue invisibleTeam;
   public final BooleanValue transparent;
   public final BooleanValue shadow;
   public final BooleanValue shadowFont;
   public final BooleanValue blur;
   public final BooleansSetting font;
   public final ActionSetting openCustomFontFolder;
   public final StringValue maximumRenderDistance;
   public HashMap enchantmentAbbreviations;
   public FormattedUnicodeFontRenderer customFontRenderer;

   public NameTags() {
       super("NameTags", "Namensschilder", new Color(21, 35, 81), Categorys.RENDER);
      this.mode = new BooleansSetting(this, "Mode", "Custom", new String[]{"None", "Vanilla", "Custom"});
      StringValue stringValue = new StringValue(this, "Scale", 1.2, 0.0, 3.0, 2);
      SettingVisibilityCondition condition = () -> this.isScaleVisible();
      this.scale = stringValue.visibleWhen(condition);
      StringValue stringValue2 = new StringValue(this, "Height", 0.0, -3.0, 3.0, 2);
      SettingVisibilityCondition condition2 = () -> this.isHeightVisible();
      this.height = stringValue2.visibleWhen(condition2);
      BooleanValue booleanValue = new BooleanValue(this, "Outline", true);
      SettingVisibilityCondition condition3 = () -> this.isOutlineVisible();
      this.outline = (BooleanValue)booleanValue.withVisibility(condition3);
      BooleanValue booleanValue2 = new BooleanValue(this, "Armor", false);
      SettingVisibilityCondition condition4 = () -> this.isArmorVisible();
      this.armor = (BooleanValue)booleanValue2.withVisibility(condition4);
      BooleanValue booleanValue3 = new BooleanValue(this, "ArmorDurability", false);
      SettingVisibilityCondition condition5 = () -> this.isArmorDurabilityVisible();
      this.armorDurability = (BooleanValue)booleanValue3.withVisibility(condition5);
      BooleanValue booleanValue4 = new BooleanValue(this, "ArmorEnchantments", false);
      SettingVisibilityCondition condition6 = () -> this.isArmorEnchantmentsVisible();
      this.armorEnchantments = (BooleanValue)booleanValue4.withVisibility(condition6);
      this.potionEffects = new BooleanValue(this, "PotionEffects", false);
      this.showExtraServerNametag = new BooleanValue(this, "ShowExtraServerNametag", false);
      BooleansSetting booleansSetting = new BooleansSetting(this, "HealthMode", "None", new String[]{"None", "Entity", "TabList", "NameTags"});
      SettingVisibilityCondition condition7 = () -> this.isHealthModeVisible();
      this.healthMode = (BooleansSetting)booleansSetting.withVisibility(condition7);
      this.ping = new BooleanValue(this, "Ping", false);
      this.throughWalls = new BooleanValue(this, "ThroughWalls", true);
      this.ignoreTeammates = new BooleanValue(this, "IgnoreTeammates", false);
      this.myself = new BooleanValue(this, "Myself", true);
      this.armorStands = new BooleanValue(this, "ArmorStands", false);
      this.invisible = new BooleanValue(this, "Invisible", true);
      this.invisibleTeam = new BooleanValue(this, "InvisibleTeam", true);
      this.transparent = new BooleanValue(this, "Transparent", false);
      this.shadow = new BooleanValue(this, "Shadow", true);
      BooleanValue booleanValue5 = new BooleanValue(this, "ShadowFont", true);
      SettingVisibilityCondition condition8 = () -> {
         return this.shadow.getValue() && this.transparent.getValue();
      };
      this.shadowFont = (BooleanValue)booleanValue5.withVisibility(condition8);
      BooleanValue booleanValue6 = new BooleanValue(this, "Blur", true);
      SettingVisibilityCondition condition9 = () -> this.isBlurVisible();
      this.blur = (BooleanValue)booleanValue6.withVisibility(condition9);
      BooleansSetting booleansSetting2 = new BooleansSetting(this, "Font", "Minecraft", new String[]{"Minecraft"});
      SettingChangeListener listener = () -> this.updateSelectedFont();
      SettingChangeListener listener2 = listener;
      BooleansSetting booleansSetting3 = (BooleansSetting)booleansSetting2.withChangeListener(listener2);
      SettingVisibilityCondition condition10 = () -> this.isFontVisible();
      this.font = (BooleansSetting)booleansSetting3.withVisibility(condition10);
      this.openCustomFontFolder = new ActionSetting(this, "Open custom font folder", () -> openCustomFontFolder());
      this.maximumRenderDistance = new StringValue(this, "MaximumRenderDistance", 500.0, 0.0, 500.0, 0);
      this.enchantmentAbbreviations = new HashMap();
   }

   @Override
   public void onEnable() {
      int[] ints = new int[]{
         Enchantment.protection.effectId,
         Enchantment.sharpness.effectId,
         Enchantment.unbreaking.effectId,
         Enchantment.knockback.effectId,
         Enchantment.power.effectId,
         Enchantment.punch.effectId,
         Enchantment.fireAspect.effectId,
         Enchantment.thorns.effectId,
         Enchantment.fireProtection.effectId,
         Enchantment.flame.effectId,
         Enchantment.infinity.effectId
      };
      this.enchantmentAbbreviations.clear();

      for (int n : ints) {
         String str = StatCollector.translateToLocal(Enchantment.getEnchantmentById(n).getName());

         for (int i = 1; i <= str.length(); i++) {
            String str2 = str.substring(0, i).toLowerCase();
            if (!this.enchantmentAbbreviations.containsValue(str2)) {
               this.enchantmentAbbreviations.put(n, str2);
               break;
            }
         }
      }

      FontManager.reloadCustomFonts();
      this.refreshAvailableFonts();
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
   }

   public float getPlayerHealth(EntityPlayer player) {
      return PlayerUtil.getPlayerHealthByMode(this.healthMode, player);
   }

   public int getPlayerPing(AbstractClientPlayer clientPlayer) {
      NetworkPlayerInfo playerInfo = clientPlayer.getPlayerInfo();
      return playerInfo == null ? -1 : clientPlayer.getPlayerInfo().getResponseTime();
   }

   private void refreshAvailableFonts() {
      HashSet set = new HashSet<>(Arrays.asList(this.font.getOptions()));
      java.util.ArrayList list = new java.util.ArrayList(FontManager.customDynamicFonts.keySet());
      set.addAll(list);
      java.util.ArrayList list2 = new java.util.ArrayList(set);
      Collections.sort(list2);
      BooleansSetting font2 = this.font;
      String[] strings = (String[])list2.toArray(new String[0]);
      font2.setOptions(strings);
      this.customFontRenderer = (FormattedUnicodeFontRenderer)FontManager.customFormattedFonts.get(this.font.getSelectedValue());
   }

   private void updateSelectedFont() {
      this.customFontRenderer = (FormattedUnicodeFontRenderer)FontManager.customFormattedFonts.get(this.font.getSelectedValue());
   }

   public boolean isRenderableEntity(Entity entity) {
      return entity instanceof EntityPlayer || this.armorStands.getValue() && entity instanceof EntityArmorStand;
   }

   private static void openCustomFontFolder() {
      ClientFileUtil.openFolder(FontManager.CLIENT_DATA_DIR + File.separator + "fonts");
   }

   private boolean isFontVisible() {
      return this.mode.getSelectedValue().equals("Custom");
   }

   private boolean isBlurVisible() {
      return this.mode.getSelectedValue().equals("Custom");
   }

   private boolean isHealthModeVisible() {
      return !this.mode.getSelectedValue().equals("None");
   }

   private boolean isArmorEnchantmentsVisible() {
      return this.armor.isVisible() && this.armor.getValue();
   }

   private boolean isArmorDurabilityVisible() {
      return this.armor.isVisible() && this.armor.getValue();
   }

   private boolean isArmorVisible() {
      return !this.mode.getSelectedValue().equals("None");
   }

   private boolean isOutlineVisible() {
      return this.mode.getSelectedValue().equals("Custom");
   }

   private boolean isHeightVisible() {
      return !this.mode.getSelectedValue().equals("None");
   }

   private boolean isScaleVisible() {
      return !this.mode.getSelectedValue().equals("None");
   }


}
