package wdl;

import wdl.gui.WorldSelectionCallback;
import net.augustus.utils.interfaces.MC;
import net.minecraft.client.gui.GuiScreen;

public class StartDownloadSetupMultiworldSelectCallback implements WorldSelectionCallback {
   public final StartDownloadMultiworldCallback parentCallback;

   @Override
   public void onWorldSelected(String str) {
      WDL.worldName = str;
      WDL.isMultiworld = true;
      WDL.propsFound = true;
      Object obj = null;
      MC.mc.displayGuiScreen((GuiScreen)obj);
      WDL.startDownload();
   }

   @Override
   public void onCancel() {
      Object obj = null;
      MC.mc.displayGuiScreen((GuiScreen)obj);
      WDL.cancelDownload();
   }

   public StartDownloadSetupMultiworldSelectCallback(StartDownloadMultiworldCallback callback) {
      this.parentCallback = callback;
   }
}
