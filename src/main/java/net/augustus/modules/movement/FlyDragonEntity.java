package net.augustus.modules.movement;

import net.augustus.settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.world.World;

public class FlyDragonEntity extends EntityDragon {
   public final Fly fly;

   public FlyDragonEntity(Fly fly2, World world) {
      super(world);
      this.fly = fly2;
   }

   public boolean isSilent() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return !Fly.getEnderDragonSoundSetting(this.fly).getValue();
   }

   public void collideWithEntity(Entity entity) {
   }

   public void collideWithNearbyEntities() {
   }
}
