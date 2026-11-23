package net.augustus.modules.misc;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.ClickListener;
import net.augustus.vendor.OnlineUserRegistry;
import net.augustus.utils.input.KeyboardInputUtil;
import net.augustus.ClientManagers;
import net.augustus.events.EventClick;
import net.augustus.settings.StringSetSetting;
import net.augustus.settings.ActionSetting;
import net.augustus.vendor.OnlineUserEntry;
import net.augustus.utils.rotation.RayTraceUtil;
import net.augustus.vendor.ElectricFriend;
import net.augustus.settings.SettingChangeListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.vendor.ElectricFriendManager;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.MovingObjectPosition;

public class MidClick extends Module implements ClickListener, TickListener {
   public HashSet friends;
   private final HashSet partyFriends;
   private final StringValue midClickDistance;
   private final BooleanValue ignoreWalls;
   public final BooleanValue autoUnfriend;
   private final BooleanValue partyFriend;
   private final BooleanValue partyFriendGomme;
   public final BooleanValue reverse;
   public final BooleanValue simple;
   private final ActionSetting deleteAllFriends;
   private final StringSetSetting friends2;
   public final Set savedFriendsCache;
   public boolean autoUnfriendTriggered;
   public boolean friendsLoaded;

   public MidClick() {
       super("MidClick", "Mittelklick", new Color(76, 78, 76), Categorys.MISC);
      this.friends = new HashSet();
      this.partyFriends = new HashSet();
      this.midClickDistance = (StringValue)(new StringValue(this, "MidClickDistance", 4.5, 3.0, 10.0, 2)).markVisualOnly();
      this.ignoreWalls = new BooleanValue(this, "IgnoreWalls", true);
      this.autoUnfriend = (BooleanValue)(new BooleanValue(this, "AutoUnfriend", true)).markVisualOnly();
      this.partyFriend = (BooleanValue)(new BooleanValue(this, "PartyFriend", true)).markVisualOnly();
      BooleanValue booleanValue = new BooleanValue(this, "PartyFriendGomme", true);
      BooleanValue partyFriend2 = this.partyFriend;
      this.partyFriend.getClass();
      BooleanValue booleanValue2 = partyFriend2;
      SettingVisibilityCondition condition = () -> booleanValue2.getValue();
      this.partyFriendGomme = (BooleanValue)((BooleanValue)booleanValue.withVisibility(condition)).markVisualOnly();
      this.reverse = (BooleanValue)(new BooleanValue(this, "Reverse", false)).markVisualOnly();
      this.simple = (BooleanValue)(new BooleanValue(this, "Simple", false)).markVisualOnly();
      this.deleteAllFriends = (ActionSetting)(new ActionSetting(this, "DeleteAllFriends", () -> this.clearAllFriends())).markVisualOnly();
      StringSetSetting stringSetSetting = new StringSetSetting(this, "Friends");
      SettingChangeListener listener = () -> this.reloadFriendsFromSetting();
      SettingChangeListener listener2 = listener;
      this.friends2 = (StringSetSetting)((StringSetSetting)stringSetSetting.withChangeListener(listener2)).markVisualOnly();
      this.savedFriendsCache = new HashSet();
      this.autoUnfriendTriggered = false;
      this.friendsLoaded = false;
   }

   @Override
   public void onEnable() {
      this.partyFriends.clear();
      this.reloadFriendsFromSetting();
   }

   @Override
   public void onDisable() {
      this.partyFriends.clear();
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 7;
      this.toggleEventSubscription(b);
      byte b2 = 40;
      this.toggleEventSubscription(b2);
   }

   public void clearAllFriends() {
      this.friends.clear();
      this.friends2.clear();
      this.savedFriendsCache.clear();
   }

   public boolean isFriend(Entity entity) {
      String name2 = entity.getName();
      return this.isFriend2(name2);
   }

   public boolean isFriend(String str, boolean flag) {
      boolean flag2 = false;
      if (this.isToggled()) {
         flag2 = this.reverse.getValue() != this.friends.contains(str);
      }

      if (!flag && ClientManagers.moduleManager.irc.isToggled() && ClientManagers.moduleManager.irc.clientFriends.getValue()) {
         OnlineUserEntry onlineUserEntry = (OnlineUserEntry)OnlineUserRegistry.usersByIngameName.get(str);
         if (ClientManagers.moduleManager.irc.yourFriendsOnly.getValue() && onlineUserEntry != null) {
            String clientUsername = onlineUserEntry.getClientUsername();
            ElectricFriend electricFriend = (ElectricFriend)ElectricFriendManager.getFriends2().get(clientUsername);
            flag2 = electricFriend != null && electricFriend.getStatus() > 0;
         } else if (onlineUserEntry != null) {
            flag2 = true;
         }
      }

      return flag2;
   }

   public boolean isFriend2(String str) {
      boolean flag = false;
      return this.isFriend(str, flag);
   }

   @Override
   public void onTick2() {
      if (!this.friendsLoaded) {
         this.friends.addAll(this.friends2.getSet());
         this.savedFriendsCache.clear();
         this.savedFriendsCache.addAll(this.friends2.getSet());
         this.friendsLoaded = true;
      }

      if (this.partyFriend.getValue()) {
         NetHandlerPlayClient sendQueue = mc.thePlayer.sendQueue;

         for (NetworkPlayerInfo networkPlayerInfo : (Iterable<NetworkPlayerInfo>)(sendQueue.getPlayerInfoMap())) {
            String name2 = networkPlayerInfo.getGameProfile().getName();
            if (!name2.equals(mc.thePlayer.getGameProfile().getName())) {
               String str = networkPlayerInfo.getDisplayName() != null
                  ? networkPlayerInfo.getDisplayName().getUnformattedText()
                  : ScorePlayerTeam.formatPlayerName(networkPlayerInfo.getPlayerTeam(), name2);
               if (this.partyFriendGomme.getValue() ? str.contains("§5Party§7") : str.contains("Party") || str.contains("party")) {
                  if (!this.partyFriends.contains(name2)) {
                     this.friends.add(name2);
                     this.partyFriends.add(name2);
                  }
               } else if (this.partyFriends.contains(name2)) {
                  this.partyFriends.remove(name2);
                  this.friends.remove(name2);
               }
            }
         }
      } else {
         for (String str2 : (Iterable<String>)(this.partyFriends)) {
            this.friends.remove(str2);
         }

         this.partyFriends.clear();
      }

      ClientManagers.moduleManager.irc.updateAutoUnfriendState();
   }

   @Override
   public void onClick(EventClick event) {
      if (mc.gameSettings.keyBindPickBlock.getPressTime() > 0) {
         MovingObjectPosition hit = RayTraceUtil.rayTrace(
            1.0F,
            ClientManagers.rotationController.getPreviousRotation().yaw,
            ClientManagers.rotationController.getPreviousRotation().pitch,
            this.midClickDistance.getValue(),
            this.midClickDistance.getValue(),
            0.0,
            this.ignoreWalls.getValue()
         );
         if (hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && hit.entityHit instanceof EntityPlayer) {
            String name2 = hit.entityHit.getName();
            if (this.friends.contains(name2)) {
               this.friends.remove(name2);
               this.removeSavedFriend(name2);
            } else {
               if (KeyboardInputUtil.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
                  this.saveFriend(name2);
               }

               this.friends.add(name2);
            }
         }
      }
   }

   public void saveFriend(String str) {
      this.friends2.updateDisplayName(str);
      this.savedFriendsCache.add(str);
   }

   public void removeSavedFriend(String str) {
      this.friends2.remove(str);
      this.savedFriendsCache.remove(str);
   }

   private void reloadFriendsFromSetting() {

      for (String str : (Iterable<String>)(this.savedFriendsCache)) {
         this.friends.remove(str);
      }

      this.friendsLoaded = false;
   }


}
