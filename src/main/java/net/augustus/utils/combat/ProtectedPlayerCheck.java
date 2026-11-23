package net.augustus.utils.combat;

import net.augustus.Augustus;
import net.augustus.utils.ChatUtil;
import net.augustus.vendor.PlayerEffectEntry;
import net.augustus.vendor.PlayerEffectRegistry;
import java.util.HashSet;
import net.augustus.utils.TimeHelper;
import net.augustus.utils.interfaces.MC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class ProtectedPlayerCheck {
   private static final HashSet PROTECTED_NAMES = new HashSet();
   private static final TimeHelper timeHelper = new TimeHelper();

   public static boolean isProtectedPlayer(Entity entity) {
      Augustus.getInstance().getClass();
      if (entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)entity;
         byte b = 1;
         PlayerEffectEntry playerEffect = PlayerEffectRegistry.getPlayerEffect(player, b);
         if (playerEffect != null) {
            long l = 1000L;
            if (timeHelper.reached2(l)) {
               ChatUtil.sendChat3("§4ERROR: This person is in GOD mode. Unable to attack.");
               timeHelper.reset();
            }

            return true;
         }
      }

      return entity instanceof EntityPlayer
         && ((EntityPlayer)entity).getGameProfile() != null
         && PROTECTED_NAMES.contains(((EntityPlayer)entity).getGameProfile().getName())
         && (double)MC.mc.thePlayer.getDistanceToEntityBox(entity) >= 2.55;
   }

}
