package net.augustus.vendor;

import net.augustus.Augustus;
import net.augustus.ClientManagers;
import net.augustus.events.TickPhase;
import net.augustus.events.listeners.InventoryCloseListener;
import net.augustus.events.listeners.RayCastListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.quests.QuestManager;
import net.augustus.settings.SettingManager;
import net.augustus.utils.AntiSamplerGuard;
import net.augustus.utils.TuplePair;
import net.augustus.utils.rotation.Rotation;
import net.augustus.utils.rotation.RotationController;
import com.google.gson.GsonBuilder;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import net.augustus.utils.interfaces.MC;
import net.augustus.vendor.packet.VendorPacket;
import net.augustus.vendor.packet.CosmeticsDisablePacket;
import net.augustus.vendor.packet.VendorPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;

public class VendorSessionTicker implements MC {
   public static volatile byte[] reconnectToken = new byte[0];
   public static CopyOnWriteArrayList scheduledTasks = new CopyOnWriteArrayList();
   public static volatile long lastHeartbeatTime = System.currentTimeMillis();
   public static volatile long lastConnectionActivity = System.currentTimeMillis();
   private static final GsonBuilder expiryHolder = new GsonBuilder();
   public static int configSyncState = 0;
   public static boolean serverChanged = false;
   public static volatile boolean heartbeatActive = false;
   public static volatile byte[] sessionToken = new byte[0];


   public static void onClientTick(int n, int n2) {
      long l = 0L;
      long l2 = 0L;
      Object obj = null;
      Iterator it = null;
      Runnable runnable = null;
      Object obj2 = null;
      byte b = 24;
      long l3 = 0L;
      l3 = -1215146458713652185L ^ (long)"⫭뎎\ude04现䥦ᕠ窉媣뛏\u0e69ꝱ\uf264\ueae6\ue101䩖墳㿅㛬癉㊝儦흺ﶿ".hashCode();

      outerLoop:
      while (true) {
         if (b >= 9) {
            if (b <= 9) {
               ClientManagers.moduleManager = null;
               l3 = l3 + 7374857098299044842L ^ 1328356243255705356L;
            } else if (b >= 14) {
               if (14 >= b) {
                  Minecraft.getMinecraft().shutdown();
                  l3 = (l3 ^ 602502948244870106L) + -1869265981571969607L;
               } else {
                  if (17 > b) {
                     if (b <= 15) {
                        String serverIP = mc.getCurrentServerData().serverIP;
                        String username = mc.session.getUsername();
                        String str = mc.thePlayer.getGameProfile().getId().toString();
                        int entityId = mc.thePlayer.getEntityId();
                        boolean flag = mc.session.getToken() == null || mc.session.getToken().isEmpty();
                        int n3 = entityId;
                        String str2 = str;
                        String str3 = username;
                        VendorPacketDispatcher.sendPresenceUpdate(serverIP, str3, str2, n3, flag);
                        Augustus.getInstance().getModuleManager().irc.syncVisibilityStatus();
                        AugustusServerClient.presenceSyncPending = false;
                        break;
                     }

                     if (mc.isSingleplayer()) {
                        l3 = l3 + 877752460268441490L ^ -7448218698918501869L;
                        break;
                     }

                     l3 = (l3 ^ -2194098590703520037L) - -1148671705156542899L;
                     continue;
                  }

                  if (b <= 17) {
                     it = scheduledTasks.iterator();
                     l3 = l3 + 6916331935400946492L ^ 2044215711395063841L;
                  } else {
                     if (18 >= b) {
                        l = System.currentTimeMillis();
                        l3 = (l3 ^ -8096373272717450307L) - -3289164889052937646L;
                        continue;
                     }

                     if (heartbeatActive) {
                        l3 = l3 + 6229469129082558147L ^ 5376914157182908906L;
                        continue;
                     }

                     l3 = l3 + 3046176554361534434L ^ 5067133937822676809L;
                  }
               }
            } else {
               if (b < 11) {
                  runnable.run();
                  l3 = l3 - -7027346987764506366L ^ -6701483654114183571L;
                  continue;
               }

               if (b <= 11) {
                  ClientManagers.settingManager.getSettings().clear();
                  l3 = (l3 ^ 6751627273926740638L) + -5296127871366405104L;
                  continue;
               }

               if (12 >= b) {
                  if (l - lastConnectionActivity > 10000L) {
                     l3 = l3 - -5569452957321810033L ^ 3043630656722574454L;
                     continue;
                  }

                  l3 = l3 - -5520056363442243913L ^ 397978144675800169L;
               } else {
                  AugustusServerHandler.incrementSessionTick();
                  l3 = (l3 ^ -2223645173639718257L) - -8947196840877410091L;
               }
            }
         } else if (4 <= b) {
            if (b <= 4) {
               TickListener.onTick2(TickPhase.EARLY);
               l3 = (l3 ^ 1559873484247985853L) - 1859355308370840041L;
               continue;
            }

            if (b >= 6) {
               if (6 >= b) {
                  if (!AugustusServerClient.presenceSyncPending) {
                     l3 = (l3 ^ -3995107375145293730L) + -1395807415305231947L;
                     break;
                  }

                  l3 = (l3 ^ 7084828990797436938L) - -5966249058978967945L;
                  continue;
               }

               if (b <= 7) {
                  if (mc.thePlayer == null) {
                     l3 = l3 - 3683663444736511099L ^ 7848816228035097016L;
                  } else {
                     l3 = (l3 ^ 2804853087539765488L) - -5003068975650865706L;
                  }
               } else {
                  InventoryCloseListener.onInventoryClose2();
                  l3 = (l3 ^ -2670240327159440641L) - 1600316456032244623L;
               }
            } else {
               l3 = (l3 ^ 2975094909060723338L) + -4814695308899241809L;
            }
         } else {
            if (1 <= b) {
               if (1 >= b) {
                  runnable = (Runnable)it.next();
                  l3 = (l3 ^ -776386932110967019L) - 5010663542038472120L;
               } else if (2 >= b) {
                  Augustus.getInstance().getRotationController().applyServerRotation();
                  l3 = l3 - 1955065610423026895L ^ -1841428428524894732L;
               } else {
                  lastConnectionActivity = l;
                  l3 = l3 + -2388472794909835006L ^ 1450268194775662282L;
               }
               continue;
            }

            AugustusServerClient instance = AugustusServerClient.getInstance();
            instance.disconnectAndReconnect();
            l3 = l3 + 8761439492460582985L ^ -8848806804650953639L;
         }

         while (true) {
            while (20 < b) {
               if (31 <= b) {
                  if (b <= 31) {
                     ClientManagers.moduleRegistry.getModules().clear();
                     l3 = l3 + 6369251162359431160L ^ -1783127532899114282L;
                     continue outerLoop;
                  }

                  if (b >= 36) {
                     if (36 >= b) {
                        if (mc.session == null) {
                           l3 = (l3 ^ -6866458928298511599L) + -1066761607709621601L;
                           break outerLoop;
                        }

                        l3 = (l3 ^ -1083822520167133009L) - -9034837082732639610L;
                        continue outerLoop;
                     }

                     if (b >= 39) {
                        if (39 >= b) {
                           AugustusServerHandler.flushPendingQueueIfReady();
                           l3 = (l3 ^ -8463982807615410730L) - 7182672185905172398L;
                           continue outerLoop;
                        }

                        if (b <= 40) {
                           ClientManagers.rotationController = null;
                           l3 = (l3 ^ -8663639094746512870L) + -2382040726534221740L;
                        } else if (l - lastHeartbeatTime <= 114000L) {
                           l3 = l3 + -8383923460019260942L ^ -8514361412340203864L;
                        } else {
                           l3 = (l3 ^ 1437075008280249025L) + 2087886687711426519L;
                        }
                     } else if (37 >= b) {
                        if (it.hasNext()) {
                           l3 = l3 - -2374888279269944497L ^ 8171456536916023784L;
                           continue outerLoop;
                        }

                        l3 = (l3 ^ -1095074435524673914L) + -9070037568075087777L;
                     } else if (n2 == 0) {
                        l3 = (l3 ^ -3778830284451990589L) - 798416125647346418L;
                     } else {
                        l3 = l3 - -1383328779731613699L ^ -1381751125993828005L;
                     }
                  } else if (33 <= b) {
                     if (b <= 33) {
                        scheduledTasks.clear();
                        l3 = (l3 ^ -5289537509825585985L) + -5179929571430436660L;
                     } else if (34 >= b) {
                        Augustus.getInstance().getClass();
                        l3 = l3 - -1380072020927016422L ^ 6128063169921553169L;
                     } else {
                        if (l - lastHeartbeatTime > 16000L) {
                           l3 = l3 - -4563425456575890910L ^ 7702292601194007379L;
                           continue outerLoop;
                        }

                        l3 = (l3 ^ 2300673472234912383L) + -9030846777945450896L;
                     }
                  } else if (!ClientManagers.moduleManager.rotationHandler.rotationMode.getSelectedValue().equals("Tick")) {
                     l3 = l3 - 525991428325480698L ^ -2887195104321424527L;
                  } else {
                     l3 = (l3 ^ -1189220284806492922L) - -2926858208370454159L;
                  }
               } else if (b >= 25) {
                  if (b <= 25) {
                     if (mc.getCurrentServerData() == null) {
                        l3 = (l3 ^ -340717817881108659L) + -9058869730418829090L;
                        break outerLoop;
                     }

                     l3 = l3 - 5125914618032301963L ^ 4829110118587406469L;
                  } else if (28 <= b) {
                     if (28 >= b) {
                        AugustusServerClient.getInstance().shutdown();
                        l3 = (l3 ^ 2400886245703785295L) + -354910634942194211L;
                        continue outerLoop;
                     }

                     if (29 >= b) {
                        RotationController rotationController = Augustus.getInstance().getRotationController();
                        Rotation rotation = new Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
                        int n4 = mc.getTimer().elapsedTicks - n;
                        int n5 = n4;
                        Rotation rotation2 = rotation;
                        rotationController.beginRotationTick(rotation2, n5);
                        l3 = (l3 ^ -4535370869952952983L) + 5712465058865661740L;
                        continue outerLoop;
                     }

                     System.exit(0);
                     l3 = l3 - 2681587098603263458L ^ -4037647124260974572L;
                  } else if (b <= 26) {
                     ClientManagers.moduleRegistry = null;
                     l3 = l3 + 6552730899349960853L ^ 7769148537684854255L;
                  } else {
                     if (mc.theWorld != null) {
                        l3 = (l3 ^ -340742005964982267L) + -5593683638252474507L;
                        continue outerLoop;
                     }

                     l3 = (l3 ^ -8791791742279778512L) + 7859787657945115253L;
                  }
               } else {
                  if (22 > b) {
                     return;
                  }

                  if (22 >= b) {
                     if (mc.thePlayer == null) {
                        l3 = (l3 ^ -4487021167998888985L) - 5396887786805937134L;
                        break outerLoop;
                     }

                     l3 = (l3 ^ 7439544709837512717L) + -4183374216333527508L;
                  } else if (b <= 23) {
                     ClientManagers.settingManager = null;
                     l3 = l3 + -3295678294565949394L ^ 5034844893978195897L;
                  } else {
                     if (!AugustusServerHandler.isConnectedAndReady()) {
                        l3 = (l3 ^ 7381304167950017149L) - 3869577898644535153L;
                        break outerLoop;
                     }

                     l3 = (l3 ^ -5867634767040933886L) + 1012539898450104653L;
                  }
               }
            }

            if (!AugustusServerHandler.getActiveHandler().isSessionReady()) {
               l3 = l3 + -6270722560665972832L ^ 7226308792284142563L;
               break outerLoop;
            }

            l3 = l3 - 4644001048273226937L ^ 4474980289326340597L;
         }
      }

      if (AugustusServerHandler.isConnectedAndReady()
         && AugustusServerHandler.getActiveHandler().isSessionReady()
         && mc.getCurrentServerData() != null
         && mc.thePlayer != null
         && mc.session != null
         && !mc.isSingleplayer()
         && (serverChanged || AugustusServerClient.presenceSyncPending)) {
         String serverIP2 = mc.getCurrentServerData().serverIP;
         String username2 = mc.session.getUsername();
         String str4 = mc.thePlayer.getGameProfile().getId().toString();
         int entityId2 = mc.thePlayer.getEntityId();
         boolean flag2 = mc.session.getToken() == null || mc.session.getToken().isEmpty();
         int n6 = entityId2;
         String str5 = str4;
         String str6 = username2;
         VendorPacketDispatcher.sendPresenceUpdate(serverIP2, str6, str5, n6, flag2);
         if (serverChanged && Augustus.getInstance().getQuestManager() != null) {
            QuestManager questManager = Augustus.getInstance().getQuestManager();
            String serverIP3 = mc.getCurrentServerData().serverIP;
            questManager.activateQuestsForServer(serverIP3);
         }

         serverChanged = false;
      }

      if (Augustus.getInstance().getModuleManager().cosmetics.isPendingSyncOnDisable() && AugustusServerHandler.isConnectedAndReady() && AugustusServerHandler.getActiveHandler().isSessionReady()) {
         AugustusServerHandler activeHandler = AugustusServerHandler.getActiveHandler();
         CosmeticsDisablePacket packet = new CosmeticsDisablePacket(sessionToken);
         activeHandler.sendPacket((VendorPacket)packet);
         boolean flag3 = false;
         Augustus.getInstance().getModuleManager().cosmetics.setPendingSyncOnDisable(flag3);
      }

      if (!mc.isSingleplayer()
         && mc.getCurrentServerData() != null
         && mc.thePlayer != null
         && mc.thePlayer.ticksExisted % 20 == 0
         && Augustus.getInstance().getQuestManager() != null) {
         QuestManager questManager2 = Augustus.getInstance().getQuestManager();
         String serverIP4 = mc.getCurrentServerData().serverIP;
         questManager2.updateQuestsForServer(serverIP4);
      }

      if (SettingManager.sessionToken != null) {
         mc.entityRenderer.getMouseOver(1.0F, false);
      }

      if (Augustus.getInstance().getModuleManager().smartRaytrace.isToggled()) {
         Augustus.getInstance().getModuleManager().smartRaytrace.updateSmartRaytrace();
      }

      if (mc.theWorld != null && mc.thePlayer != null) {
         RayCastListener.onRayCast2();
         Minecraft mc2 = mc;
         MovingObjectPosition hit = new MovingObjectPosition(mc.objectMouseOver);
         mc2.lastObjectMouseOver = hit;
      }

      if (configSyncState == 1) {
         OnlineConfigManager.requestConfigLists();
         configSyncState = 2;
      }

      Augustus.getInstance().getClass();
      String str7 = expiryHolder.toString();
      l2 = Long.parseLong(str7);
      l2 ^= 6868362108176948499L;
      if (System.currentTimeMillis() > l2 || l > l2) {
         AntiSamplerGuard.activateGuard(1);
      }

      int n7 = mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mc.objectMouseOver.entityHit instanceof EntityLivingBase
         ? mc.ticksLookingAtEntity + 1
         : 0;
      mc.ticksLookingAtEntity = n7;
      int n8 = mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mc.objectMouseOver.entityHit instanceof EntityLivingBase
         ? 0
         : mc.ticksNotLookingAtEntity + 1;
      mc.ticksNotLookingAtEntity = n8;
      mc.mcProfiler.startSection("gameMode");
      if (!mc.isGamePaused) {
         if (mc.theWorld != null) {
            mc.playerController.updateController();
         }
      }

      mc.mcProfiler.endStartSection("textures");
      if (!mc.isGamePaused) {
         mc.getTextureManager().tick();
      }

      it = VendorPacketDispatcher.pendingAckPackets.keySet().iterator();

      while (it.hasNext()) {
         Integer num = (Integer)it.next();
         obj = (TuplePair)VendorPacketDispatcher.pendingAckPackets.get(num);
         if ((TuplePair)obj != null && ((TuplePair)obj).getFirst() == null) {
            VendorPacketDispatcher.pendingAckPackets.remove(num);
         } else if ((TuplePair)obj != null
            && l - (Long)((TuplePair)obj).getFirst() > 15000L
            && AugustusServerHandler.isConnectedAndReady()
            && AugustusServerHandler.getActiveHandler().isSessionReady()) {
            obj2 = (VendorPayload)((TuplePair)obj).getSecond();
            VendorPacketDispatcher.pendingAckPackets.remove(num);
            if ((VendorPayload)obj2 != null) {
               VendorPayload vendorPayload = (VendorPayload)obj2;
               byte[] bytes = sessionToken;
               vendorPayload.setSessionToken(bytes);
               AugustusServerHandler activeHandler2 = AugustusServerHandler.getActiveHandler();
               VendorPacket packet2 = (VendorPacket)((VendorPayload)obj2);
               activeHandler2.sendPacket(packet2);
               VendorPacketDispatcher.pendingAckPackets.put(num, new TuplePair(l, ((TuplePair)obj).getSecond()));
            }
         }
      }
   }

}
