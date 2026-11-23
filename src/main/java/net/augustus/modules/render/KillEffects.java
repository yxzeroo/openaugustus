package net.augustus.modules.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.events.EventLoadWorld;
import net.augustus.utils.TuplePair;
import net.augustus.events.EventAttackEntity;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.listeners.AttackEntityListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;

public class KillEffects extends Module implements LoadWorldListener, AttackEntityListener, TickListener {
   private final BooleansSetting executionMode;
   private final BooleanValue flash;
   private final BooleanValue dragonDeathSound;
   private final BooleanValue fireSound;
   private EntityLivingBase lastAttackedEntity;
   private EntityLivingBase lastKilledEntity;
   private final TimeHelper timeHelper;
   private final Map particleSettingsMap;

   public KillEffects() {
       super("KillEffects", "TötungsEffekte", new Color(168, 255, 176), Categorys.RENDER);
      this.executionMode = new BooleansSetting(this, "ExecutionMode", "RemovedEntity", new String[]{"RemovedEntity", "ZeroHealth"});
      this.flash = new BooleanValue(this, "Flash", true);
      this.dragonDeathSound = new BooleanValue(this, "Dragon Death Sound", true);
      this.fireSound = new BooleanValue(this, "Fire Sound", true);
      this.lastAttackedEntity = null;
      this.lastKilledEntity = null;
      this.timeHelper = new TimeHelper();
      this.particleSettingsMap = new HashMap();

      for (EnumParticleTypes enumParticleTypes : EnumParticleTypes.values()) {
         StringBuilder sb = new StringBuilder().append("Use ");
         String str = enumParticleTypes.name();
         BooleanValue booleanValue = new BooleanValue(this, sb.append(this.formatEnumName(str)).toString(), false);
         sb = new StringBuilder();
         String str2 = enumParticleTypes.name();
         StringValue stringValue = new StringValue(
            this, sb.append(this.formatEnumName(str2)).append(" Count ").toString(), 1.0, 1.0, 20.0, 0
         );
         booleanValue.getClass();
         BooleanValue booleanValue2 = booleanValue;
         SettingVisibilityCondition condition = () -> booleanValue2.getValue();
         StringValue stringValue2 = stringValue.visibleWhen(condition);
         this.particleSettingsMap.put(enumParticleTypes, new TuplePair(booleanValue, stringValue2));
      }
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
      this.lastAttackedEntity = null;
      this.lastKilledEntity = null;
   }

   @Override
   public void updateEventSubscriptions() {
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
   }

   @Override
   public void onAttackEntity(EventAttackEntity event) {
      if (event.getTarget() instanceof EntityLivingBase) {
         this.lastAttackedEntity = (EntityLivingBase)event.getTarget();
         this.timeHelper.reset();
      }
   }

   @Override
   public void onTick2() {
      if (mc.thePlayer.getCurrentHealth() <= 0.0F) {
         this.lastAttackedEntity = null;
      }

      long l = 2000L;
      if (!this.timeHelper.reached2(l)
         && this.lastAttackedEntity != null
         && this.lastAttackedEntity != this.lastKilledEntity
         && (
            !mc.theWorld.loadedEntityList.contains(this.lastAttackedEntity) && this.executionMode.getSelectedValue().equals("RemovedEntity")
               || !this.lastAttackedEntity.isEntityAlive() && this.executionMode.getSelectedValue().equals("ZeroHealth")
         )) {
         if (this.flash.getValue()) {
            EntityLightningBolt entityLightningBolt = new EntityLightningBolt(mc.theWorld, this.lastAttackedEntity.posX, this.lastAttackedEntity.posY, this.lastAttackedEntity.posZ);
            mc.theWorld.addWeatherEffect(entityLightningBolt);
            mc.getSoundHandler()
               .playSound(
                  new PositionedSoundRecord(
                     new ResourceLocation("ambient.weather.thunder"),
                     10000.0F,
                     0.8F + new Random().nextFloat() * 0.2F,
                     (float)this.lastAttackedEntity.posX,
                     (float)this.lastAttackedEntity.posY,
                     (float)this.lastAttackedEntity.posZ
                  )
               );
            mc.getSoundHandler()
               .playSound(
                  new PositionedSoundRecord(
                     new ResourceLocation("random.explode"),
                     10000.0F,
                     0.5F + new Random().nextFloat() * 0.2F,
                     (float)this.lastAttackedEntity.posX,
                     (float)this.lastAttackedEntity.posY,
                     (float)this.lastAttackedEntity.posZ
                  )
               );
         }

         if (this.dragonDeathSound.getValue()) {
            mc.theWorld.playBroadcastSound(1018, new BlockPos(this.lastAttackedEntity.posX, this.lastAttackedEntity.posY, this.lastAttackedEntity.posZ), 0);
         }

         if (this.fireSound.getValue()) {
            mc.getSoundHandler()
               .playSound(
                  new PositionedSoundRecord(
                     new ResourceLocation("item.fireCharge.use"),
                     10000.0F,
                     0.5F + new Random().nextFloat() * 0.2F,
                     (float)this.lastAttackedEntity.posX,
                     (float)this.lastAttackedEntity.posY,
                     (float)this.lastAttackedEntity.posZ
                  )
               );
         }

         for (Entry entry : (Iterable<Entry>)(this.particleSettingsMap.entrySet())) {
            if (((BooleanValue)((TuplePair)entry.getValue()).getFirst()).getValue()) {
               try {
                  for (int i = 0; (double)i < ((StringValue)((TuplePair)entry.getValue()).getSecond()).getValue(); i++) {
                     mc.effectRenderer.emitParticleAtEntity(this.lastAttackedEntity, (EnumParticleTypes)entry.getKey());
                  }
               } catch (Exception e) {
               }
            }
         }

         this.lastKilledEntity = this.lastAttackedEntity;
         this.lastAttackedEntity = null;
      }
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      this.lastKilledEntity = null;
      this.lastAttackedEntity = null;
   }

   private String formatEnumName(String str) {
      str = str.replace("_", " ");
      StringBuilder sb = new StringBuilder();
      char[] chars = str.toCharArray();

      for (int i = 0; i < chars.length; i++) {
         char ch = chars[i];
         if (i != 0 && chars[i - 1] != ' ') {
            sb.append(String.valueOf(ch).toLowerCase());
         } else {
            sb.append(ch);
         }
      }

      return sb.toString();
   }

}
