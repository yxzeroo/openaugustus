package net.augustus.modules.movement;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPacket;
import net.augustus.events.EventPacketDelay;
import net.augustus.events.listeners.PacketDelayListener;
import net.augustus.events.listeners.MoveEntityListener;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.events.EventMoveInput;
import net.augustus.events.EventMoveEntity;
import net.augustus.modules.modes.motion.MotionModifierEngine;
import net.augustus.events.listeners.PacketListener;

public class MotionModifier extends Module implements MoveEntityListener, UpdateListener, MoveInputListener, PacketDelayListener, PacketListener {
   private final MotionModifierEngine motionModifierEngine;

   public MotionModifier() {
       super("MotionModifier", "BewegungsModifizierer", new Color(50, 168, 82), Categorys.MOVEMENT);
      this.motionModifierEngine = new MotionModifierEngine(this);
      this.motionModifierEngine.initRuleSettings();
   }

   @Override
   public void onEnable() {
      this.motionModifierEngine.onEnable();
   }

   @Override
   public void onDisable() {
      this.motionModifierEngine.onDisable();
   }

   @Override
   public void updateEventSubscriptions() {
      this.toggleEventSubscription((byte)20);
      this.toggleEventSubscription((byte)43);
      this.toggleEventSubscription((byte)21);
      this.toggleEventSubscription((byte)25);
      this.toggleEventSubscription((byte)26);
   }

   @Override
   public void onUpdate2() {
      this.motionModifierEngine.onPreUpdate();
   }

   @Override
   public void onUpdate() {
      this.motionModifierEngine.onPostUpdate();
   }

   @Override
   public void onMoveEntity(EventMoveEntity event) {
      this.motionModifierEngine.onMoveEntity(event);
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      this.motionModifierEngine.onMoveInput(event);
   }

   @Override
   public void onPacket2(EventPacket event) {
      this.motionModifierEngine.onSendPacket(event);
   }

   @Override
   public void onPacketDelay(EventPacketDelay event) {
      this.motionModifierEngine.onOutgoingPacketDelay(event);
   }
}
