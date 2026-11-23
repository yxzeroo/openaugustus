package net.augustus.settings.options;

import net.augustus.settings.SelectableOption;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.modules.Module;
import net.augustus.modules.player.InvManager;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.potion.Potion;

public enum AutoUsePotionTriggerOption implements SelectableOption {
   SPEED("Speed", Potion.moveSpeed, true),
   SLOWNESS("Slowness", Potion.moveSlowdown),
   HASTE("Haste", Potion.digSpeed),
   MINING_FATIGUE("Mining Fatigue", Potion.digSlowdown),
   STRENGTH("Strength", Potion.damageBoost),
   JUMP_BOOST("Jump Boost", Potion.jump),
   NAUSEA("Nausea", Potion.confusion),
   REGENERATION("Regeneration", Potion.regeneration),
   RESISTANCE("Resistance", Potion.resistance),
   FIRE_RESISTANCE("Fire Resistance", Potion.fireResistance),
   WATER_BREATHING("Water Breathing", Potion.waterBreathing),
   INVISIBILITY("Invisibility", Potion.invisibility),
   BLINDNESS("Blindness", Potion.blindness),
   NIGHT_VISION("Night Vision", Potion.nightVision),
   HUNGER("Hunger", Potion.nightVision),
   WEAKNESS("Weakness", Potion.weakness),
   POISON("Poison", Potion.poison),
   WITHER("Wither", Potion.wither),
   HEALTH_BOOST("Health Boost", Potion.healthBoost),
   ABSORPTION("Absorption", Potion.absorption),
   SATURATION("Saturation", Potion.saturation);

   private final String name;
   private final Potion potion;
   private boolean selected;
   private StringValue minDurationSetting;

   private AutoUsePotionTriggerOption(String str, Potion potion2) {
      this.name = str;
      this.potion = potion2;
   }

   private AutoUsePotionTriggerOption(String str, Potion potion2, boolean flag) {
      this.name = str;
      this.selected = flag;
      this.potion = potion2;
   }

   public String getName() {
      return this.name;
   }

   public boolean isSelected() {
      return this.selected;
   }

   public void setSelected(boolean flag) {
      this.selected = flag;
   }

   public static void initSettings(Module module) {
      AutoUsePotionTriggerOption[] values = values();

      for (AutoUsePotionTriggerOption option : values) {
         DoubleValue doubleValue = new DoubleValue(module, option.getName(), true);
         SettingVisibilityCondition condition = option::isSelected;
         DoubleValue doubleValue2 = doubleValue.visibleWhen(condition);
         StringValue stringValue = new StringValue(module, option.getName() + "Minimum effect duration left in seconds", 5.0, 0.0, 100.0, 0, doubleValue2);
         String str = "Minimum effect duration left";
         option.minDurationSetting = stringValue.named(str);
      }
   }

   public static Potion getPotion(AutoUsePotionTriggerOption option) {
      return option.potion;
   }

   public static StringValue getMinDurationSetting(AutoUsePotionTriggerOption option) {
      return option.minDurationSetting;
   }

}
