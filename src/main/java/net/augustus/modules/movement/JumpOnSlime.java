package net.augustus.modules.movement;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.settings.Setting;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.EventMoveInput;
import net.augustus.settings.BooleanValue;
import net.minecraft.block.BlockSlime;
import net.minecraft.util.BlockPos;

public class JumpOnSlime extends Module implements MoveInputListener {
   private final BooleanValue inInv;

   public JumpOnSlime() {
       super("JumpOnSlime", "SpringeAufSchleim", new Color(35, 52, 45), Categorys.MOVEMENT);
      this.inInv = new BooleanValue(this, "InInv", false);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      byte b = 21;
      this.toggleEventSubscription(b);
   }

   public void onMoveInput3(EventMoveInput event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if ((mc.currentScreen == null || this.inInv.getValue())
         && mc.thePlayer.onGround
         && mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ)).getBlock() instanceof BlockSlime) {
         boolean flag = true;
         event.setJump(flag);
      }
   }

}
