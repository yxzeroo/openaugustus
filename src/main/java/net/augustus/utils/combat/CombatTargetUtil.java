package net.augustus.utils.combat;

import net.augustus.Augustus;
import net.augustus.ClientManagers;
import net.augustus.commands.impl.AlertCommand;
import net.augustus.vendor.PlayerEffectEntry;
import net.augustus.vendor.PlayerEffectRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class CombatTargetUtil {

   public static EntityLivingBase getActiveTargetOrDefault(EntityLivingBase entity) {
      return ClientManagers.moduleManager.killAura.isToggled() && ClientManagers.moduleManager.killAura.target != null
         ? ClientManagers.moduleManager.killAura.target
         : (
            ClientManagers.moduleManager.aimBot.isToggled() && ClientManagers.moduleManager.aimBot.target != null
               ? ClientManagers.moduleManager.aimBot.target
               : (ClientManagers.moduleManager.aimAssist.isToggled() && ClientManagers.moduleManager.aimAssist.target != null ? ClientManagers.moduleManager.aimAssist.target : entity)
         );
   }

   public static boolean isValidTarget(
      Entity entity,
      boolean flag,
      boolean flag2,
      boolean flag3,
      boolean flag4,
      boolean flag5,
      boolean flag6,
      boolean flag7,
      boolean flag8,
      boolean flag9,
      boolean flag10,
      boolean flag11
   ) {
      return evaluateTarget(entity, flag, flag2, flag3, flag4, flag5, flag6, flag7, flag8, flag9, flag10, false, flag11, true, true, false, (byte)0);
   }

   public static EntityLivingBase getActiveTarget() {
      return getActiveTargetOrDefault((EntityLivingBase)null);
   }

   public static boolean isValidTarget(
      Entity entity, boolean flag, boolean flag2, boolean flag3, boolean flag4, boolean flag5, boolean flag6, boolean flag7, boolean flag8, boolean flag9
   ) {
      return evaluateTarget(entity, flag, flag2, flag3, flag4, flag5, flag6, flag7, flag8, flag9, false, true, false, false, true, false, (byte)0);
   }

   public static boolean hasProtectionEffect(EntityPlayer player) {
      if (player.getGameProfile() == null) {
         return false;
      } else {
         return PlayerEffectRegistry.getPlayerEffect(player, (byte)2) != null;
      }
   }

   private static boolean evaluateTarget(
      Entity entity,
      boolean flag,
      boolean flag2,
      boolean flag3,
      boolean flag4,
      boolean flag5,
      boolean flag6,
      boolean flag7,
      boolean flag8,
      boolean flag9,
      boolean flag10,
      boolean flag11,
      boolean flag12,
      boolean flag13,
      boolean flag14,
      boolean flag15,
      int n
   ) {
      if (!(entity instanceof EntityLivingBase)) {
         return false;
      } else {
         EntityLivingBase entity2;
         boolean flag16;
         exitBlock: {
            exitBlock2: {
               entity2 = (EntityLivingBase)entity;
               String name = entity.getName();
               exitBlock3:
               if ((flag6 || !entity2.isInvisible())
                  && (flag11 || entity2.deathTime <= n && entity2.ticksExisted > 1)
                  && (!flag10 || !entity.isDead)
                  && (
                     !flag12
                        || !name.equals("§aShop")
                           && !name.equals("§e§lITEM SHOP")
                           && !name.equals("SHOP")
                           && !name.equals("UPGRADES")
                           && !name.contains("§lUpgrades")
                           && !name.contains("§lShop")
                  )) {
                  exitBlock4:
                  if (!(entity2 instanceof EntityPlayer)
                     || !flag
                     || !flag14 && entity2.isPlayerSleeping()
                     || flag7 && ClientManagers.moduleManager.teams.isTeammate(entity2)
                     || flag8 && !ClientManagers.moduleManager.midClick.autoUnfriendTriggered && ClientManagers.moduleManager.midClick.isFriend(entity2)) {
                     if (flag15) {
                        if (Augustus.getInstance().getCommandManager().alertCommand.hasAlert(entity2.getName())) {
                           break exitBlock4;
                        }
                     }

                     if ((
                           !(entity2 instanceof EntityMob)
                                 && !(entity2 instanceof EntityIronGolem)
                                 && !(entity2 instanceof EntitySnowman)
                                 && !(entity2 instanceof EntitySquid)
                              || entity2 instanceof EntityWither
                              || !flag2
                        )
                        && (!(entity2 instanceof EntityAnimal) || !flag3)
                        && (!(entity2 instanceof EntityVillager) || !flag4)
                        && (!(entity2 instanceof EntityArmorStand) || !flag5)) {
                        break exitBlock3;
                     }
                  }

                  if (flag9) {
                     if (ClientManagers.moduleManager.antiBot.isBot(entity)) {
                        break exitBlock3;
                     }
                  }

                  if (!flag13 || !ClientManagers.moduleManager.targetArea.isToggled()) {
                     break exitBlock2;
                  }

                  if (ClientManagers.moduleManager.targetArea.isEntityInArea(entity)) {
                     break exitBlock2;
                  }
               }

               flag16 = false;
               break exitBlock;
            }

            flag16 = true;
         }

         boolean flag17 = flag16;
         return flag17 && entity2 instanceof EntityPlayer && hasProtectionEffect((EntityPlayer)entity2) ? false : flag17;
      }
   }





}
