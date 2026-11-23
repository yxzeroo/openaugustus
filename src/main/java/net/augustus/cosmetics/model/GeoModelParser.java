package net.augustus.cosmetics.model;

import net.augustus.cosmetics.MoreCosmeticsCore;
import net.labymod.Converter;
import net.labymod.RawGeoModel;

public class GeoModelParser {

   public GeoModel loadModel(String str, String str2) {

      try {
         RawGeoModel rawGeoModel = Converter.fromJsonString(str2);
         String str3 = "";
         RawGeometryTree rawGeometryTree = RawGeometryTree.parseHierarchy(rawGeoModel, str3);
         GeoModelBuilder geoBuilder = DefaultGeoModelBuilder.getGeoBuilder(str);
         return geoBuilder.constructGeoModel(rawGeometryTree);
      } catch (Exception e) {
         MoreCosmeticsCore.LOGGER.error(String.format("Error parsing %S", str), e);
         throw new RuntimeException(e);
      }
   }

}
