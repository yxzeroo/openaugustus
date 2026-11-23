package wdl.gui;

import wdl.MessageTypeCategory;
import wdl.WDLMessages;
import wdl.api.IWDLMessageType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

public class MessageTypeEntryList extends ArrayList {
   public final GuiMessageTypeList parentList;

   public MessageTypeEntryList(GuiMessageTypeList guiMessageTypeList) {
      this.parentList = guiMessageTypeList;
      Map map = WDLMessages.getTypes().asMap();

      for (Entry entry : (Iterable<Entry>)(map.entrySet())) {
         this.add(new GuiWDLMessagesCategoryEntry(this.parentList, (MessageTypeCategory)entry.getKey()));

         for (IWDLMessageType wdlMessageType : (Iterable<IWDLMessageType>)((Collection)entry.getValue())) {
            this.add(new GuiWDLMessagesTypeEntry(this.parentList, wdlMessageType, (MessageTypeCategory)entry.getKey()));
         }
      }
   }
}
