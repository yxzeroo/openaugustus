package net.augustus.utils.network;

public enum IpLookupProvider implements IpLookupService {
   INCOLUMITAS("Incolumitas.com (BEST)", new IncolumitasIpLookup()),
   IP_API("ip-api.com", new IpApiLookupService());

private final IpLookupService service;
private final String displayName;
   private static int[] stateFlags;

   static {
      if (getStateFlags() == null) {
         setStateFlags(new int[2]);
      }
   }


   public String getDisplayName() {
      return this.displayName;
   }

   @Override
   public IpLookupResult lookup(String str) throws Exception {
      return this.service.lookup(str);
   }

   private IpLookupProvider(String str, IpLookupService ipLookupService) {
      this.displayName = str;
      this.service = ipLookupService;
   }

   public static void setStateFlags(int[] ints) {
      stateFlags = ints;
   }

   public static int[] getStateFlags() {
      return stateFlags;
   }
}
