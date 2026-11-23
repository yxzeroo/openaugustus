package net.augustus.vendor.packet;

import java.util.HashMap;
import java.util.Map;
import net.augustus.vendor.network.VendorPacketBuffer;

public class CosmeticsEquipPacket implements VendorPacket {
   private byte[] sessionToken;
   private Map heightMap;
   private Map colorFlagPrimaryMap;
   private Map colorParamPrimaryMap;
   private Map enabledMap;
   private Map colorFlagSecondaryMap;
   private Map colorRgbMap = new HashMap();
   private Map scaleMap;
   private Map colorParamSecondaryMap;

   public Map getColorFlagPrimaryMap() {
      return this.colorFlagPrimaryMap;
   }

   public Map getColorParamSecondaryMap() {
      return this.colorParamSecondaryMap;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionToken = buf.readByteArray();
      this.colorRgbMap = buf.readIntArrayMap();
      this.colorFlagPrimaryMap = buf.readBooleanArrayMap();
      this.colorFlagSecondaryMap = buf.readBooleanArrayMap();
      this.colorParamPrimaryMap = buf.readDoubleArrayMap();
      this.colorParamSecondaryMap = buf.readDoubleArrayMap();
      this.enabledMap = buf.readBooleanMap();
      this.scaleMap = buf.readFloatMap();
      this.heightMap = buf.readFloatMap();
   }

   public CosmeticsEquipPacket() {
      this.colorFlagPrimaryMap = new HashMap();
      this.colorFlagSecondaryMap = new HashMap();
      this.colorParamPrimaryMap = new HashMap();
      this.colorParamSecondaryMap = new HashMap();
      this.enabledMap = new HashMap();
      this.scaleMap = new HashMap();
      this.heightMap = new HashMap();
   }

   public Map getEnabledMap() {
      return this.enabledMap;
   }

   public Map getColorParamPrimaryMap() {
      return this.colorParamPrimaryMap;
   }

   public Map getScaleMap() {
      return this.scaleMap;
   }

   public Map getHeightMap() {
      return this.heightMap;
   }

   public Map getColorRgbMap() {
      return this.colorRgbMap;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
      buf.writeIntArrayMap(this.colorRgbMap);
      buf.writeBooleanArrayMap(this.colorFlagPrimaryMap);
      buf.writeBooleanArrayMap(this.colorFlagSecondaryMap);
      buf.writeDoubleArrayMap(this.colorParamPrimaryMap);
      buf.writeDoubleArrayMap(this.colorParamSecondaryMap);
      buf.writeBooleanMap(this.enabledMap);
      buf.writeFloatMap(this.scaleMap);
      buf.writeFloatMap(this.heightMap);
   }

   public CosmeticsEquipPacket(byte[] bytes, Map map, Map map2, long l, Map map3, Map map4, Map map5, Map map6, Map map7, Map map8) {
      this.sessionToken = bytes;
      this.colorRgbMap = map;
      this.colorFlagPrimaryMap = map2;
      this.colorFlagSecondaryMap = map3;
      this.colorParamPrimaryMap = map4;
      this.colorParamSecondaryMap = map5;
      this.enabledMap = map6;
      this.scaleMap = map7;
      this.heightMap = map8;
   }

   public Map getColorFlagSecondaryMap() {
      return this.colorFlagSecondaryMap;
   }
}
