package wdl.gui;

import wdl.WDL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiWDLOptionsMultiworldCallback implements MultiworldCallback {
   public final GuiWDL parentGui;

   public void onSelect(boolean flag) {
      WDL.isMultiworld = flag;
      if (WDL.isMultiworld) {
         Minecraft mc = GuiWDL.getMinecraftForMultiworldSelect(this.parentGui);
         GuiWDLMultiworldSelect guiWDLMultiworldSelect = new GuiWDLMultiworldSelect(I18n.format("wdl.gui.multiworldSelect.title.changeOptions", new Object[0]), new GuiWDLOptionsSetupMultiworldSelectCallback(this));
         mc.displayGuiScreen((GuiScreen)guiWDLMultiworldSelect);
      } else {
         WDL.baseProps.setProperty("LinkedWorlds", "");
         WDL.saveProps();
         WDL.propsFound = true;
         Minecraft mc2 = GuiWDL.getMinecraftForReturnToOptions(this.parentGui);
         GuiWDL parentGui2 = this.parentGui;
         mc2.displayGuiScreen(parentGui2);
      }
   }

   public void onCancel() {
      Minecraft mc = GuiWDL.getMinecraftForMultiworldCancel(this.parentGui);
      Object obj = null;
      mc.displayGuiScreen((GuiScreen)obj);
   }

   public GuiWDLOptionsMultiworldCallback(GuiWDL guiWDL) {
      this.parentGui = guiWDL;
   }


}
