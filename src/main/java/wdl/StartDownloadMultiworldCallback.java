package wdl;

import wdl.gui.GuiWDLMultiworldSelect;
import wdl.gui.MultiworldCallback;
import net.augustus.utils.interfaces.MC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public final class StartDownloadMultiworldCallback implements MultiworldCallback {

   public void onCancel() {
      Object obj = null;
      MC.mc.displayGuiScreen((GuiScreen)obj);
      WDL.cancelDownload();
   }

   public void onSelect(boolean flag) {
      WDL.isMultiworld = flag;
      if (WDL.isMultiworld) {
         Minecraft mc = MC.mc;
         GuiWDLMultiworldSelect guiWDLMultiworldSelect = new GuiWDLMultiworldSelect(I18n.format("wdl.gui.multiworldSelect.title.startDownload", new Object[0]), new StartDownloadSetupMultiworldSelectCallback(this));
         mc.displayGuiScreen((GuiScreen)guiWDLMultiworldSelect);
      } else {
         WDL.baseProps.setProperty("LinkedWorlds", "");
         WDL.saveProps();
         WDL.propsFound = true;
         Object obj = null;
         MC.mc.displayGuiScreen((GuiScreen)obj);
         WDL.startDownload();
      }
   }


}
