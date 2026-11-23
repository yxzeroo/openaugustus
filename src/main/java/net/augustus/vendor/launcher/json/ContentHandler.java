package net.augustus.vendor.launcher.json;

import java.io.IOException;

public interface ContentHandler {
   boolean endObjectEntry() throws JsonParseException, IOException;

   boolean startArray() throws JsonParseException, IOException;

   void startJSON() throws JsonParseException, IOException;

   boolean endArray() throws JsonParseException, IOException;

   void endJSON() throws JsonParseException, IOException;

   boolean startObject() throws JsonParseException, IOException;

   boolean primitive(Object obj) throws JsonParseException, IOException;

   boolean endObject() throws JsonParseException, IOException;

   boolean startObjectEntry(String str) throws JsonParseException, IOException;
}
