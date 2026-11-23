package net.augustus.modules.misc;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.vendor.OnlineUserRegistry;
import net.augustus.ClientManagers;
import net.augustus.vendor.OnlineUserEntry;
import net.augustus.Augustus;
import net.augustus.vendor.ElectricFriend;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.EventManager;
import net.augustus.vendor.ElectricFriendManager;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;

public class IRC extends Module implements TickListener {
   public final StringValue yourVisibility;
   public final StringValue ircFormatting;
   public final BooleanValue clientFriends;
   public final BooleanValue yourFriendsOnly;
   public final BooleanValue autoUnfriend;
   private int lastVisibilityMode;
   private boolean needsVisibilitySync;
   private boolean pendingDisconnect;

   public IRC() {
       super("IRC", "ZwischennetzWeiterleitung", new Color(66, 245, 200), Categorys.MISC);
      this.yourVisibility = (StringValue)(new StringValue(this, "Your visibility", "All", new String[]{"All", "Friends"})).markVisualOnly();
      this.ircFormatting = (StringValue)(new StringValue(
            this, "IRC-Formatting", "Xanax", new String[]{"Xanax", "Xanax2", "Centaurus", "Centaurus2", "Skid", "Simple"}
         ))
         .markVisualOnly();
      this.clientFriends = (BooleanValue)(new BooleanValue(this, "ClientFriends", true)).markVisualOnly();
      BooleanValue booleanValue = new BooleanValue(this, "YourFriendsOnly", false);
      this.yourFriendsOnly = (BooleanValue)((BooleanValue)booleanValue.withVisibility(() -> this.clientFriends.getValue())).markVisualOnly();
      booleanValue = new BooleanValue(this, "AutoUnfriend", true);
      this.autoUnfriend = (BooleanValue)((BooleanValue)booleanValue.withVisibility(() -> this.clientFriends.getValue())).markVisualOnly();
      this.lastVisibilityMode = 0;
      this.needsVisibilitySync = false;
      this.pendingDisconnect = false;
   }

   public void onClientInit() {
      this.setToggled(true);
   }

   public void onEnable() {
      EventManager.getEventBus().subscribe(40, this);
      this.needsVisibilitySync = true;
      if (mc.theWorld != null && mc.thePlayer != null) {
         this.sendChatMessage2("Information: IRC has an delay up to 10 seconds");
      }
   }

   public void onDisable() {
      this.pendingDisconnect = true;
      this.lastVisibilityMode = 0;
   }

   public void updateEventSubscriptions() {
   }

   public void onTick3() {
      if (false) {
         if (this.pendingDisconnect) {
            EventManager.getEventBus().unsubscribe(40, this);
            this.pendingDisconnect = false;
         } else {
            int n;
            exitBlock: {
               if (!this.yourVisibility.getSelectedValue().equals("All")) {
                  if (!this.yourVisibility.getSelectedValue().equals("Ghost")) {
                     n = this.yourVisibility.getSelectedValue().equals("Friends") ? 1 : 1;
                     break exitBlock;
                  }

                  Augustus.getInstance().getClass();
               }

               n = 2;
            }

            int n2 = n;
            if (this.needsVisibilitySync || n2 != this.lastVisibilityMode) {
               this.needsVisibilitySync = false;
               this.lastVisibilityMode = n2;
            }

            if (!ClientManagers.moduleManager.midClick.isToggled()) {
               this.updateAutoUnfriendState();
            }
         }
      }
   }

   public void updateAutoUnfriendState() {
      if (mc.thePlayer != null && mc.theWorld != null) {
         boolean flag = ClientManagers.moduleManager.midClick.isToggled() && ClientManagers.moduleManager.midClick.autoUnfriend.getValue();
         boolean flag2 = false;
         NetHandlerPlayClient sendQueue = mc.thePlayer.sendQueue;

         for (NetworkPlayerInfo networkPlayerInfo : (Iterable<NetworkPlayerInfo>)(sendQueue.getPlayerInfoMap())) {
            String name2 = networkPlayerInfo.getGameProfile().getName();
            if (!mc.thePlayer.getName().equals(name2)
               && flag
               && !ClientManagers.moduleManager.midClick.friends.contains(name2)
               && (!ClientManagers.moduleManager.teams.isToggled() || !ClientManagers.moduleManager.teams.isTeammateName(name2))) {
               flag2 = true;
               break;
            }
         }

         boolean flag3 = this.isToggled()
            && this.autoUnfriend.getValue()
            && this.clientFriends.getValue()
            && false;
         boolean flag4 = false;

         for (NetworkPlayerInfo networkPlayerInfo2 : (Iterable<NetworkPlayerInfo>)(sendQueue.getPlayerInfoMap())) {
            String name3 = networkPlayerInfo2.getGameProfile().getName();
            if (!mc.thePlayer.getName().equals(name3) && this.isToggled() && this.clientFriends.getValue() && this.autoUnfriend.getValue()) {
               OnlineUserEntry onlineUserEntry = (OnlineUserEntry)OnlineUserRegistry.usersByIngameName.get(name3);
               if ((
                     onlineUserEntry == null
                        || onlineUserEntry.getClientUsername() == null
                        || this.yourFriendsOnly.getValue()
                           && (ElectricFriendManager.getFriends2().get(onlineUserEntry.getClientUsername()) == null || ((ElectricFriend)ElectricFriendManager.getFriends2().get(onlineUserEntry.getClientUsername())).getStatus() == 0)
                  )
                  && (!ClientManagers.moduleManager.teams.isToggled() || !ClientManagers.moduleManager.teams.isTeammateName(name3))) {
                  flag4 = true;
                  break;
               }
            }
         }

         boolean flag5 = false;
         if (flag && flag3) {
            flag5 = !flag2 || !flag4;
         } else if (flag) {
            flag5 = !flag2;
         } else if (flag3) {
            flag5 = !flag4;
         }

         ClientManagers.moduleManager.midClick.autoUnfriendTriggered = flag5;
      }
   }

   public void syncVisibilityStatus() {
      if (this.isToggled()) {
         int n = this.yourVisibility.getSelectedValue().equals("All") ? 2 : (this.yourVisibility.getSelectedValue().equals("Friends") ? 1 : 1);
      } else {
      }
   }


}
