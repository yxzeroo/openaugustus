package net.augustus.utils.network;

import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class IpApiLookupService implements IpLookupService {
   private static final String API_URL = null;

   private Integer parseIntOrNull(String str) {
      try {
         return Integer.parseInt(str);
      } catch (NumberFormatException e) {
         return null;
      }
   }

   public IpLookupResult lookup(String str) throws Exception {
      long l = System.currentTimeMillis();
      URL url = new URL(
         String.format(
            "http://ip-api.com/json/%s?fields=status,message,continent,continentCode,country,countryCode,region,regionName,city,district,zip,lat,lon,timezone,currency,isp,org,as,asname,reverse,mobile,proxy,query,hosting",
            str
         )
      );
      BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
      StringBuilder sb = new StringBuilder();
      reader.lines().forEach(sb::append);
      reader.close();
      double d = (double)(System.currentTimeMillis() - l) / 1000.0;
      JsonObject json = (JsonObject)GSON.fromJson(sb.toString(), JsonObject.class);
      String str2 = "as";
      String stringOrNull = this.getStringOrNull(json, str2);
      if (stringOrNull != null) {
         stringOrNull = stringOrNull.split(" ")[0].replace("AS", "");
      }

      String str3 = "query";
      String stringOrNull2 = this.getStringOrNull(json, str3);
      String str4 = "mobile";
      boolean booleanOrNull = this.getBooleanOrNull(json, str4);
      String str5 = "hosting";
      boolean booleanOrNull2 = this.getBooleanOrNull(json, str5);
      String str6 = "proxy";
      boolean booleanOrNull3 = this.getBooleanOrNull(json, str6);
      String str7 = "isp";
      IpCompanyInfo ipCompanyInfo = new IpCompanyInfo(this.getStringOrNull(json, str7), null, null, "isp", null, null);
      int n = this.parseIntOrNull(stringOrNull);
      String str8 = "as";
      String stringOrNull3 = this.getStringOrNull(json, str8);
      String str9 = "country";
      String stringOrNull4 = this.getStringOrNull(json, str9);
      String str10 = "org";
      IpAsnInfo ipAsnInfo = new IpAsnInfo(
         n, null, null, stringOrNull3, stringOrNull4, true, this.getStringOrNull(json, str10), null, null, "isp", null, null, null, null
      );
      String str11 = "continent";
      String stringOrNull5 = this.getStringOrNull(json, str11);
      String str12 = "country";
      String stringOrNull6 = this.getStringOrNull(json, str12);
      String str13 = "countryCode";
      stringOrNull3 = this.getStringOrNull(json, str13);
      String str14 = "regionName";
      stringOrNull4 = this.getStringOrNull(json, str14);
      String str15 = "city";
      String stringOrNull7 = this.getStringOrNull(json, str15);
      String str16 = "lat";
      double doubleOrNull = this.getDoubleOrNull(json, str16);
      String str17 = "lon";
      double doubleOrNull2 = this.getDoubleOrNull(json, str17);
      String str18 = "zip";
      String stringOrNull8 = this.getStringOrNull(json, str18);
      String str19 = "timezone";
      return new IpLookupResult(
         stringOrNull2,
         null,
         false,
         booleanOrNull,
         false,
         booleanOrNull2,
         false,
         booleanOrNull3,
         false,
         ipCompanyInfo,
         ipAsnInfo,
         new IpLocationInfo(stringOrNull5, stringOrNull6, stringOrNull3, stringOrNull4, stringOrNull7, doubleOrNull, doubleOrNull2, stringOrNull8, this.getStringOrNull(json, str19), null, null, false),
         d
      );
   }


}
