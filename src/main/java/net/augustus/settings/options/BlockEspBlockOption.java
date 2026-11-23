package net.augustus.settings.options;

import net.augustus.Augustus;
import net.augustus.settings.ColorSetting;
import net.augustus.settings.SelectableOption;
import net.augustus.settings.SettingVisibilityCondition;
import java.awt.Color;
import net.augustus.modules.render.BlockESP;
import net.augustus.modules.render.ClickGui;
import net.augustus.settings.DoubleValue;

public class BlockEspBlockOption implements SelectableOption {
   public String name;
   public boolean selected;
   public ColorSetting colorSetting;
   public final BlockESP blockESP;

   private BlockEspBlockOption(BlockESP blockESP2, String str, boolean flag) {
      this.blockESP = blockESP2;
      this.name = str;
      this.selected = flag;
   }

   private void initColorSetting() {
      DoubleValue doubleValue = new DoubleValue(this.blockESP, this.name, true);
      SettingVisibilityCondition condition = () -> {
         return this.isSelected() && !BlockESP.getOneColorSetting(this.blockESP).getValue();
      };
      DoubleValue doubleValue2 = doubleValue.visibleWhen(condition);
      BlockESP blockESP2 = this.blockESP;
      String str = "Color_" + this.name;
      Color color = new Color(21, 121, 230, 65);
      String str2 = str;
      this.colorSetting = new ColorSetting(blockESP2, str2, color, doubleValue2);
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public boolean isSelected() {
      return this.selected;
   }

   @Override
   public void setSelected(boolean flag) {
      BlockESP.clearTrackedBlocksBridge(Augustus.getInstance().getModuleManager().blockESP);
      this.selected = flag;
      boolean flag2 = true;
      BlockESP.setNeedsRendererReload(this.blockESP, flag2);
   }

   public BlockEspBlockOption(BlockESP blockESP2, String str, boolean flag, BlockEspBlockOptionSyntheticMarker marker) {
      this(blockESP2, str, flag);
   }

   public static void initOptionColorSetting(BlockEspBlockOption blockEspBlockOption) {
      blockEspBlockOption.initColorSetting();
   }

}
