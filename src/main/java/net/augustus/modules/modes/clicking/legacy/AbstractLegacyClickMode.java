package net.augustus.modules.modes.clicking.legacy;

import net.augustus.modules.modes.AbstractSubMode;
import net.augustus.modules.Module;
import net.augustus.settings.DoubleValue;
import net.minecraft.entity.EntityLivingBase;

public abstract class AbstractLegacyClickMode extends AbstractSubMode {

   public AbstractLegacyClickMode(Module module, DoubleValue doubleValue, String str) {
      super(module, doubleValue, str);
   }

   public abstract int computeClicks(EntityLivingBase entity);

   public abstract void onClickPerformed(EntityLivingBase entity);

   public abstract void onTickUpdate(long l);




}
