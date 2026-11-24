package net.augustus.cosmetics.model;

public enum CosmeticBodyPart {
   FREE(0),
   HEAD(1, 3),
   BODY(2, 2),
   ABOVE_HEAD(3),
   RIGHT_ARM(4, 2),
   LEFT_ARM(5, 2),
   RIGHT_LEG(6, 1),
   LEFT_LEG(7, 1);

public static final CosmeticBodyPart[] VALUES;
   private int armorSlot;
private int id;

   private CosmeticBodyPart(int n) {
      this(n, -1);
   }

   public int getId() {
      return this.id;
   }

   @Override
   public String toString() {
      return this.name().replace("_", " ");
   }

   public static CosmeticBodyPart byId(int n) {

      for (CosmeticBodyPart cosmeticBodyPart : VALUES) {
         if (cosmeticBodyPart.getId() == n) {
            return cosmeticBodyPart;
         }
      }

      return FREE;
   }

   private CosmeticBodyPart(int n, int n2) {
      this.id = n;
      this.armorSlot = n2;
   }

   static {
      VALUES = values();
   }



   public int getArmorSlot() {
      return this.armorSlot;
   }



}
