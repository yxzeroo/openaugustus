package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.ItemPosListener;
import net.augustus.events.EventItemPos;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemStack;

public class CustomItemPos extends Module implements ItemPosListener {
   public final StringValue x;
   public final StringValue y;
   public final StringValue z;
   public final StringValue blockX;
   public final StringValue blockY;
   public final StringValue blockZ;
   public final StringValue scale;
   public final BooleanValue leftHand;

   public CustomItemPos() {
       super("CustomItemPos", "KundenspeziefischeObjektPos", new Color(42, 145, 95), Categorys.RENDER);
      this.x = new StringValue(this, "X", 0.13, -1.0, 1.0, 2);
      this.y = new StringValue(this, "Y", 0.13, -1.0, 1.0, 2);
      this.z = new StringValue(this, "Z", 0.04, -1.0, 1.0, 2);
      this.blockX = new StringValue(this, "BlockX", -0.27, -1.0, 1.0, 2);
      this.blockY = new StringValue(this, "BlockY", -0.04, -1.0, 1.0, 2);
      this.blockZ = new StringValue(this, "BlockZ", 0.0, -1.0, 1.0, 2);
      this.scale = new StringValue(this, "Scale", 0.2, 0.0, 2.0, 2);
      this.leftHand = new BooleanValue(this, "LeftHand", false);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      byte b = 13;
      this.toggleEventSubscription(b);
   }

   public void onItemPos(EventItemPos event) {
      ItemStack currentItem = mc.thePlayer.inventory.getCurrentItem();
      if (currentItem == null || !(currentItem.getItem() instanceof ItemMapBase)) {
         double value = this.x.getValue();
         event.setPosX(value);
         double value2 = this.y.getValue();
         event.setPosY(value2);
         double value3 = this.z.getValue();
         event.setPosZ(value3);
         double value4 = this.blockX.getValue();
         event.setBlockX(value4);
         double value5 = this.blockY.getValue();
         event.setBlockY(value5);
         double value6 = this.blockZ.getValue();
         event.setBlockZ(value6);
         double value7 = this.scale.getValue();
         event.setScale(value7);
      }
   }

}
