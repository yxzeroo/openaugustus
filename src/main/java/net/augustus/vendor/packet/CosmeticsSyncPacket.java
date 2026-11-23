package net.augustus.vendor.packet;

import java.util.HashMap;
import java.util.Map;
import net.augustus.vendor.network.VendorPacketBuffer;

public class CosmeticsSyncPacket implements VendorPacket {
   private Map heightMap;
   private int entityId;
   private Map enabledMap;
   private Map colorParamPrimaryMap;
   private int maxCosmetics = 0;
   private String playerName;
   private Map scaleMap;
   private String playerUuid;
   private Map colorRgbMap = new HashMap();
   private Map colorFlagSecondaryMap;
   private Map colorFlagPrimaryMap = new HashMap();
   private int ackId;
   private Map colorParamSecondaryMap;

   public int getAckId() {
      return this.ackId;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.ackId = buf.readInt();
      this.colorRgbMap = buf.readIntArrayMap();
      this.colorFlagPrimaryMap = buf.readBooleanArrayMap();
      this.colorFlagSecondaryMap = buf.readBooleanArrayMap();
      this.colorParamPrimaryMap = buf.readDoubleArrayMap();
      this.colorParamSecondaryMap = buf.readDoubleArrayMap();
      this.enabledMap = buf.readBooleanMap();
      this.scaleMap = buf.readFloatMap();
      this.heightMap = buf.readFloatMap();
      this.playerName = buf.readUtfString();
      this.maxCosmetics = buf.readInt();
      this.entityId = buf.readInt();
      this.playerUuid = buf.readUtfString();
   }

   public int getEntityId() {
      return this.entityId;
   }

   public String getPlayerName() {
      return this.playerName;
   }

   public Map getColorRgbMap() {
      return this.colorRgbMap;
   }

   public Map getColorParamPrimaryMap() {
      return this.colorParamPrimaryMap;
   }

   public Map getScaleMap() {
      return this.scaleMap;
   }

   public Map getColorFlagSecondaryMap() {
      return this.colorFlagSecondaryMap;
   }

   public Map getHeightMap() {
      return this.heightMap;
   }

   public String getPlayerUuid() {
      return this.playerUuid;
   }

   public CosmeticsSyncPacket() {
      this.colorFlagSecondaryMap = new HashMap();
      this.colorParamPrimaryMap = new HashMap();
      this.colorParamSecondaryMap = new HashMap();
      this.enabledMap = new HashMap();
      this.scaleMap = new HashMap();
      this.heightMap = new HashMap();
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeInt(this.ackId);
      buf.writeIntArrayMap(this.colorRgbMap);
      buf.writeBooleanArrayMap(this.colorFlagPrimaryMap);
      buf.writeBooleanArrayMap(this.colorFlagSecondaryMap);
      buf.writeDoubleArrayMap(this.colorParamPrimaryMap);
      buf.writeDoubleArrayMap(this.colorParamSecondaryMap);
      buf.writeBooleanMap(this.enabledMap);
      buf.writeFloatMap(this.scaleMap);
      buf.writeFloatMap(this.heightMap);
      buf.writeUtfString(this.playerName);
      buf.writeInt(this.maxCosmetics);
      buf.writeInt(this.entityId);
      buf.writeUtfString(this.playerUuid);
   }

   public Map getEnabledMap() {
      return this.enabledMap;
   }

   public Map getColorFlagPrimaryMap() {
      return this.colorFlagPrimaryMap;
   }

   public Map getColorParamSecondaryMap() {
      return this.colorParamSecondaryMap;
   }

   public int getMaxCosmetics() {
      return this.maxCosmetics;
   }
}
