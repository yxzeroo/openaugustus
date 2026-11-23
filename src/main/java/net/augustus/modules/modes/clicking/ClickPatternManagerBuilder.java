package net.augustus.modules.modes.clicking;

import java.util.function.Consumer;
import net.augustus.settings.BooleansSetting;

public class ClickPatternManagerBuilder {
   private Consumer onPatternChanged;
   private BooleansSetting patternSelectorSetting = null;

   public ClickPatternManager build() {
      return new ClickPatternManager(this.patternSelectorSetting, this.onPatternChanged, null);
   }

   public ClickPatternManagerBuilder() {
      this.onPatternChanged = null;
   }

   public ClickPatternManagerBuilder withSelectorAndCallback(BooleansSetting booleansSetting, Consumer consumer) {
      this.patternSelectorSetting = booleansSetting;
      this.onPatternChanged = consumer;
      return this;
   }
}
