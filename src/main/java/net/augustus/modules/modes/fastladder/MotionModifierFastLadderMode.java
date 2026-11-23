package net.augustus.modules.modes.fastladder;

import net.augustus.events.EventMoveEntity;
import net.augustus.events.EventMoveInput;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.MoveEntityListener;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.PacketListener;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.modules.modes.motion.MotionModifierEngine;
import net.augustus.modules.movement.helper.FastLadderMotionModifier;
import net.augustus.modules.movement.helper.MotionModifierProvider;
import net.augustus.modules.misc.Dummy;
import net.augustus.modules.movement.FastLadder;
import net.augustus.settings.DoubleValue;

public class MotionModifierFastLadderMode extends FastLadderMode implements MotionModifierProvider, UpdateListener, MoveEntityListener, MoveInputListener, PacketListener {
   private final Dummy dummy;
   private final MotionModifierEngine motionModifierEngine;

   public MotionModifierFastLadderMode(FastLadder fastLadder, DoubleValue doubleValue, String str) {
       super(fastLadder, doubleValue, str);
      this.dummy = new Dummy();
      this.motionModifierEngine = new FastLadderMotionModifier(this, this.dummy, fastLadder);
      this.motionModifierEngine.initRuleSettings();
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 20;
      this.toggleEventSubscription(b);
      byte b2 = 43;
      this.toggleEventSubscription(b2);
      byte b3 = 21;
      this.toggleEventSubscription(b3);
      byte b4 = 26;
      this.toggleEventSubscription(b4);
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
   public MotionModifierEngine getMotionModifierEngine() {
      return this.motionModifierEngine;
   }

   @Override
   public Dummy getDummy() {
      return this.dummy;
   }
}
