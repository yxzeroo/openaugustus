package net.augustus.modules.movement;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.ClientManagers;
import net.augustus.utils.MoveUtil;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.events.EventMoveInput;
import net.augustus.settings.BooleanValue;

public class HeadHitter extends Module implements MoveInputListener, UpdateListener {
   private final BooleanValue onlyWhileMoving;
   private final BooleanValue onlyWhileJumping;
   private final BooleanValue notWhileSneaking;
   private final BooleanValue notWhileScaffold;
   private final BooleanValue inInventory;

   public HeadHitter() {
       super("HeadHitter", "KopfStoßer", new Color(230, 250, 175), Categorys.MOVEMENT);
      this.onlyWhileMoving = new BooleanValue(this, "OnlyWhileMoving", true);
      this.onlyWhileJumping = new BooleanValue(this, "OnlyWhileJumping", true);
      this.notWhileSneaking = new BooleanValue(this, "NotWhileSneaking", true);
      this.notWhileScaffold = new BooleanValue(this, "NotWhileScaffold", false);
      this.inInventory = new BooleanValue(this, "InInventory", false);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      this.toggleEventSubscription((byte)21);
      this.toggleEventSubscription((byte)43);
   }

   public void onMoveInput3(EventMoveInput event) {
      if (!event.isJump()) {
         event.setJump(this.shouldHeadHit());
      }
   }

   public void onUpdate2() {
      if (this.shouldHeadHit()) {
         mc.thePlayer.setJumpTicks(0);
      }
   }

   private boolean shouldHeadHit() {
      return (mc.currentScreen == null || this.inInventory.getValue())
         && (!ClientManagers.moduleManager.scaffold.isToggled() || !this.notWhileScaffold.getValue())
         && (!mc.gameSettings.keyBindSneak.isKeyDown() || !this.notWhileSneaking.getValue())
         && (mc.gameSettings.keyBindJump.isKeyDown() || !this.onlyWhileJumping.getValue())
         && (MoveUtil.isMoving() || !this.onlyWhileMoving.getValue())
         && mc.theWorld.getBlockState(mc.thePlayer.posX, mc.thePlayer.lastGroundY + 2.0, mc.thePlayer.posZ).getBlock().isFullBlock();
   }

}
