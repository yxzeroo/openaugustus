package wdl.gui;

import wdl.WDL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiWDLOptionsMultiworldSelectCallback implements WorldSelectionCallback {
   public final GuiWDL parentGui;

   @Override
   public void onCancel() {
      Minecraft mc = GuiWDL.getMinecraftForSelectCancel(this.parentGui);
      Object obj = null;
      mc.displayGuiScreen((GuiScreen)obj);
   }

   @Override
   public void onWorldSelected(String str) {
      WDL.worldName = str;
      WDL.isMultiworld = true;
      WDL.propsFound = true;
      WDL.worldProps = WDL.loadWorldProps(str);
      Minecraft mc = GuiWDL.getMinecraftForWorldSelected(this.parentGui);
      GuiWDL parentGui2 = this.parentGui;
      mc.displayGuiScreen(parentGui2);
   }

   public GuiWDLOptionsMultiworldSelectCallback(GuiWDL guiWDL) {
      this.parentGui = guiWDL;
   }
}
