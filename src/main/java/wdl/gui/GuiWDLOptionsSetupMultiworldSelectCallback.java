package wdl.gui;

import wdl.WDL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiWDLOptionsSetupMultiworldSelectCallback implements WorldSelectionCallback {
   public final GuiWDLOptionsMultiworldCallback parentCallback;

   public GuiWDLOptionsSetupMultiworldSelectCallback(GuiWDLOptionsMultiworldCallback callback) {
      this.parentCallback = callback;
   }

   @Override
   public void onCancel() {
      Minecraft mc = GuiWDL.getMinecraftForSetupCancel(this.parentCallback.parentGui);
      Object obj = null;
      mc.displayGuiScreen((GuiScreen)obj);
   }

   @Override
   public void onWorldSelected(String str) {
      WDL.worldName = str;
      WDL.isMultiworld = true;
      WDL.propsFound = true;
      WDL.worldProps = WDL.loadWorldProps(str);
      Minecraft mc = GuiWDL.getMinecraftForSetupSelected(this.parentCallback.parentGui);
      GuiWDL parentGui = this.parentCallback.parentGui;
      mc.displayGuiScreen(parentGui);
   }
}
